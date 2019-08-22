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

import com.kycrm.member.service.trade.IMongoHistroyTradeService;
import com.kycrm.transferdata.trade.service.ITradeTransferService;
import com.kycrm.transferdata.util.PidThreadFactory;
import com.kycrm.util.DateUtils;

public class GetLostDataBaseThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(GetLostDataBaseThread.class);

	// 订单迁移数据服务
	private ITradeTransferService tradeTransferService;

	// 迁移订单Dubbo服务
	private IMongoHistroyTradeService mongoHistroyTradeService;

	private int i;

	private Long uid;

	private List<Long> tidList;

	// 每批次处理数据量
	private Integer pageSize;

	private ExecutorService threadPool;

	private static final Long THREAD_BASE_SLEEP_TIME = 1000L;

	public GetLostDataBaseThread(int i, ITradeTransferService tradeTransferService,
			IMongoHistroyTradeService mongoHistroyTradeService, List<Long> tidList, Integer pageSize, Long uid) {
		this.i = i;
		this.tradeTransferService = tradeTransferService;
		this.mongoHistroyTradeService = mongoHistroyTradeService;
		this.uid = uid;
		this.tidList = tidList;
		this.pageSize = pageSize;
	}

	@Override
	public void run() {
		StopWatch watch = new StopWatch();
		watch.start();
		try {
			LOGGER.info("迁移丢失数据起始时间 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
			// 获取指定集合的文档数据
			int totalCount = tidList.size();
			int totalPageNo = 0;
			if (totalCount % pageSize == 0) {
				totalPageNo = (int) (totalCount / pageSize);
			} else {
				totalPageNo = (int) (totalCount / pageSize) + 1;
			}
			LOGGER.info("集合名称 = TradeDTO" + uid + " 总记录数 = " + totalCount + " 共分 " + totalPageNo + " 批次迁移数据 每批次共 "
					+ pageSize + " 条数据");
			if (totalPageNo != 0) {
				// 创建线程池
				threadPool = Executors.newFixedThreadPool(1, new PidThreadFactory("TransferMongoDataBaseThread", true));
				this.transferTradeAndOrderData(i, uid, totalPageNo);
				threadPool.shutdown();
			}
			watch.stop();
			LOGGER.info("迁移丢失数据结束时间 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd hh:mm:ss"));
		} catch (Exception e) {
			e.printStackTrace();
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
	private void transferTradeAndOrderData(int i, Long uid, Integer totalPageNo) throws Exception {
		String collectionName = "TradeDTO" + uid;
		// 线程集合
		List<TradeGetLostDataThread> threadList = new ArrayList<TradeGetLostDataThread>(totalPageNo);
		for (i = 1; i < totalPageNo + 1; i++) {
			threadList.add(new TradeGetLostDataThread(uid, collectionName, tradeTransferService,
					mongoHistroyTradeService, tidList, true));
		}
		// 提交线程
		List<Future<Integer>> futureList = new ArrayList<Future<Integer>>(totalPageNo);
		for (i = 0; i < totalPageNo; i++) {
			futureList.add(threadPool.submit(threadList.get(i)));
			Thread.sleep(THREAD_BASE_SLEEP_TIME * 2);
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

}
