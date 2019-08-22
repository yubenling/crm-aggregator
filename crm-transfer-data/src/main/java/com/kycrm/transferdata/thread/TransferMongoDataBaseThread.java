package com.kycrm.transferdata.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.trade.IMongoHistroyTradeService;
import com.kycrm.transferdata.smsrecord.service.ISmsRecordTransferService;
import com.kycrm.transferdata.trade.service.ITradeTransferService;
import com.kycrm.transferdata.util.PidThreadFactory;
import com.kycrm.util.DateUtils;

/**
 * MongoDB为数据源迁移数据
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年9月5日下午3:39:55
 * @Tags
 */
public class TransferMongoDataBaseThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransferMongoDataBaseThread.class);

	// 订单迁移数据服务
	private ITradeTransferService tradeTransferService;

	// 短信记录迁移数据服务
	private ISmsRecordTransferService smsRecordTransferService;

	// 短信记录Dubbo服务
	private ISmsRecordDTOService smsRecordDTOService;

	// 迁移订单Dubbo服务
	private IMongoHistroyTradeService mongoHistroyTradeService;

	// MongoDB的集合名称
	private String collectionName;

	// 每批次处理数据量
	private Integer pageSize;

	// 迁移数据起始日期
	private Date startDate;

	// 迁移数据截止日期
	private Date endDate;

	private ExecutorService threadPool;

	private Integer threadPoolSize = 5;

	private int i;

	private Long threadSleepMilliseconds = 1000L;

	public TransferMongoDataBaseThread(ITradeTransferService tradeTransferService,
			IMongoHistroyTradeService mongoHistroyTradeService, String collectionName, Integer pageSize, Date startDate,
			Date endDate, Integer threadPoolSize, Long sleepMilliseconds) {
		super();
		this.tradeTransferService = tradeTransferService;
		this.mongoHistroyTradeService = mongoHistroyTradeService;
		this.collectionName = collectionName;
		this.pageSize = pageSize;
		this.startDate = startDate;
		this.endDate = endDate;
		this.threadPoolSize = threadPoolSize;
		this.threadSleepMilliseconds = sleepMilliseconds;
	}

	public TransferMongoDataBaseThread(ISmsRecordTransferService smsRecordTransferService,
			ISmsRecordDTOService smsRecordDTOService, String collectionName, Integer pageSize, Date startDate,
			Date endDate, Integer threadPoolSize, Long sleepMilliseconds) {
		super();
		this.smsRecordTransferService = smsRecordTransferService;
		this.smsRecordDTOService = smsRecordDTOService;
		this.collectionName = collectionName;
		this.pageSize = pageSize;
		this.startDate = startDate;
		this.endDate = endDate;
		this.threadPoolSize = threadPoolSize;
		this.threadSleepMilliseconds = sleepMilliseconds;
	}

	public TransferMongoDataBaseThread(int i, ITradeTransferService tradeTransferService,
			IMongoHistroyTradeService mongoHistroyTradeService, ISmsRecordTransferService smsRecordTransferService,
			ISmsRecordDTOService smsRecordDTOService, String collectionName, Integer pageSize, Date startDate,
			Date endDate) {
		super();
		this.i = i;
		this.tradeTransferService = tradeTransferService;
		this.mongoHistroyTradeService = mongoHistroyTradeService;
		this.smsRecordTransferService = smsRecordTransferService;
		this.smsRecordDTOService = smsRecordDTOService;
		this.collectionName = collectionName;
		this.pageSize = pageSize;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public void run() {
		StopWatch watch = new StopWatch();
		watch.start();
		try {
			LOGGER.info("collectionName = " + collectionName + " startDate = "
					+ DateUtils.formatDate(startDate, "yyyy-MM-dd HH:mm:ss") + " endDate = "
					+ DateUtils.formatDate(endDate, "yyyy-MM-dd HH:mm:ss"));
			LOGGER.info("迁移数据起始时间 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			// 获取指定集合的文档数据
			Long totalCount = null;
			if (collectionName.startsWith("TradeDTO")) {// 订单及子订单
				totalCount = this.tradeTransferService.getCount(collectionName, startDate, endDate);
			} else {// 短信记录
				totalCount = this.smsRecordTransferService.getCount(collectionName, startDate, endDate);
			}
			int totalPageNo = 0;
			if (totalCount % pageSize == 0) {
				totalPageNo = (int) (totalCount / pageSize);
			} else {
				totalPageNo = (int) (totalCount / pageSize) + 1;
			}
			LOGGER.info("集合名称 = " + collectionName + " 总记录数 = " + totalCount + " 共分 " + totalPageNo + " 批次迁移数据 每批次共 "
					+ pageSize + " 条数据");
			if (totalPageNo != 0) {
				// 创建线程池
				threadPool = Executors.newFixedThreadPool(threadPoolSize,
						new PidThreadFactory("TransferMongoDataBaseThread", true));
				if (collectionName.startsWith("TradeDTO")) {// 订单及子订单
					this.transferTradeAndOrderData(i, totalPageNo);
				} else {// 短信记录
					this.transferSmsRecordData(i, totalPageNo);
				}
				threadPool.shutdown();
			}
			watch.stop();
			LOGGER.info("迁移数据结束时间 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			LOGGER.error("第 " + i + " 批次迁移数据出错");
			LOGGER.error("TransferDataBaseThread 发送错误 : ", e);
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 迁移TradeDTO数据
	 * @Date 2018年8月22日下午5:40:40
	 * @param i
	 * @param totalPageNo
	 * @throws Exception
	 * @ReturnType void
	 */
	private void transferTradeAndOrderData(int i, Integer totalPageNo) throws Exception {
		Long uid = Long.valueOf(collectionName.substring(8));
		// 线程集合
		List<TradeTransferDataThread> threadList = new ArrayList<TradeTransferDataThread>(totalPageNo);
		for (i = 1; i < totalPageNo + 1; i++) {
			int startPosition = 0;
			if (i == 1) {
				startPosition = 0;
			} else {
				startPosition = (i - 1) * pageSize;
			}
			int limit = pageSize;
			LOGGER.info("UID = " + uid + " startPosition = " + startPosition + " limit = " + limit);
			threadList.add(new TradeTransferDataThread(collectionName, tradeTransferService, mongoHistroyTradeService,
					startPosition, limit, startDate, endDate));
		}
		// 提交线程
		List<Future<Integer>> futureList = new ArrayList<Future<Integer>>(totalPageNo);
		for (i = 0; i < totalPageNo; i++) {
			futureList.add(threadPool.submit(threadList.get(i)));
			Thread.sleep(threadSleepMilliseconds * 5);
		}
		Integer totalTransferDataResultCount = 0;
		Integer singleTransferDataResultCount = 0;
		// 获取结果
		for (i = 0; i < totalPageNo; i++) {
			singleTransferDataResultCount = futureList.get(i).get();
			totalTransferDataResultCount += singleTransferDataResultCount;
			LOGGER.info("UID = " + uid + " 第 " + (i + 1) + " 批次迁移数据量 = " + singleTransferDataResultCount);
		}
		LOGGER.info("UID = " + uid + " 集合名称 = " + collectionName + " 实际迁移数据数量 = " + totalTransferDataResultCount);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 迁移SmsRecordDTO数据
	 * @Date 2018年8月22日下午5:40:40
	 * @param i
	 * @param totalPageNo
	 * @throws Exception
	 * @ReturnType void
	 */
	private void transferSmsRecordData(int i, Integer totalPageNo) throws Exception {
		// 线程集合
		List<SmsRecordTransferDataThread> threadList = new ArrayList<SmsRecordTransferDataThread>(totalPageNo);
		for (i = 1; i < totalPageNo + 1; i++) {
			int startPosition = 0;
			if (i == 1) {
				startPosition = 0;
			} else {
				startPosition = (i - 1) * pageSize;
			}
			int limit = pageSize;
			LOGGER.info("startPosition = " + startPosition + " limit = " + limit);
			threadList.add(new SmsRecordTransferDataThread(smsRecordTransferService, smsRecordDTOService,
					collectionName, startPosition, limit, startDate, endDate));
		}
		// 提交线程
		List<Future<Integer>> futureList = new ArrayList<Future<Integer>>(totalPageNo);
		for (i = 0; i < totalPageNo; i++) {
			futureList.add(threadPool.submit(threadList.get(i)));
			Thread.sleep(threadSleepMilliseconds * 5);
		}
		Integer totalTransferDataResultCount = 0;
		Integer singleTransferDataResultCount = 0;
		// 获取结果
		for (i = 0; i < totalPageNo; i++) {
			singleTransferDataResultCount = futureList.get(i).get();
			totalTransferDataResultCount += singleTransferDataResultCount;
			LOGGER.info("第 " + (i + 1) + " 批次迁移数据量 = " + singleTransferDataResultCount);
		}
		LOGGER.info("集合名称 = " + collectionName + " 实际迁移数据数量 = " + totalTransferDataResultCount);
	}

}
