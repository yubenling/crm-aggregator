package com.kycrm.transferdata.thread;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.service.item.IItemTransferService;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.transferdata.item.service.IOldItemService;
import com.kycrm.transferdata.smsblacklist.service.IOldSmsBlackListService;
import com.kycrm.transferdata.util.PidThreadFactory;
import com.kycrm.util.DateUtils;

/**
 * MySQL为数据源迁移数据
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年9月5日下午3:40:07
 * @Tags
 */
public class TransferMysqlDataBaseThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransferMysqlDataBaseThread.class);

	private int i;

	// 从老的RDS数据库上获取黑名单
	private IOldSmsBlackListService oldSmsBlackListService;

	// 分库分表存储黑名单
	private ISmsBlackListDTOService smsBlackListDTOService;

	private IOldItemService oldItemService;

	private IItemTransferService itemTransferService;

	private Long uid;

	// 老板数据库用户昵称
	private String userId;

	// 新版数据库用户UID对应Map
	private Map<String, Long> userUidMap;

	private String tableName;

	// 每批次处理数据量
	private Integer pageSize;

	// 迁移数据起始日期
	private Date startDate;

	// 迁移数据截止日期
	private Date endDate;

	private ExecutorService threadPool;

	private Integer threadPoolSize = 5;

	private Long threadSleepMilliseconds = 500L;

	public TransferMysqlDataBaseThread(IOldSmsBlackListService oldSmsBlackListService,
			ISmsBlackListDTOService smsBlackListDTOService, String userId, String tableName, Integer pageSize,
			Date startDate, Date endDate, Integer threadPoolSize, Long threadSleepMilliseconds) {
		super();
		this.oldSmsBlackListService = oldSmsBlackListService;
		this.smsBlackListDTOService = smsBlackListDTOService;
		this.userId = userId;
		this.tableName = tableName;
		this.pageSize = pageSize;
		this.startDate = startDate;
		this.endDate = endDate;
		this.threadPoolSize = threadPoolSize;
		this.threadSleepMilliseconds = threadSleepMilliseconds;
	}

	public TransferMysqlDataBaseThread(IOldItemService oldItemService, IItemTransferService itemTransferService,
			Long uid, String userId, Map<String, Long> userUidMap, String tableName, Integer pageSize, Date startDate,
			Date endDate, Integer threadPoolSize, Long threadSleepMilliseconds) {
		super();
		this.oldItemService = oldItemService;
		this.itemTransferService = itemTransferService;
		this.uid = uid;
		this.userId = userId;
		this.userUidMap = userUidMap;
		this.tableName = tableName;
		this.pageSize = pageSize;
		this.startDate = startDate;
		this.endDate = endDate;
		this.threadPoolSize = threadPoolSize;
		this.threadSleepMilliseconds = threadSleepMilliseconds;
	}

	public TransferMysqlDataBaseThread(int i, IOldSmsBlackListService oldSmsBlackListService,
			ISmsBlackListDTOService smsBlackListDTOService, IOldItemService oldItemService,
			IItemTransferService itemTransferService, Long uid, String userId, Map<String, Long> userUidMap,
			String tableName, Integer pageSize, Date startDate, Date endDate) {
		super();
		this.i = i;
		this.oldSmsBlackListService = oldSmsBlackListService;
		this.smsBlackListDTOService = smsBlackListDTOService;
		this.oldItemService = oldItemService;
		this.itemTransferService = itemTransferService;
		this.uid = uid;
		this.userId = userId;
		this.userUidMap = userUidMap;
		this.tableName = tableName;
		this.pageSize = pageSize;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	@Override
	public void run() {
		StopWatch watch = new StopWatch();
		watch.start();
		try {
			LOGGER.info(
					"tableName = crm_item" + " startDate = " + DateUtils.formatDate(startDate, "yyyy-MM-dd HH:mm:ss")
							+ " endDate = " + DateUtils.formatDate(endDate, "yyyy-MM-dd HH:mm:ss"));
			LOGGER.info("迁移数据起始时间 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
			Long totalCount = null;
			if ("crm_item".equals(tableName)) {
				// 商品
				totalCount = this.oldItemService.getCount(uid, startDate, endDate);
			} else {
				// 黑名单
				totalCount = this.oldSmsBlackListService.getCount(userId, startDate, endDate);
			}
			int totalPageNo = 0;
			if (totalCount % pageSize == 0) {
				totalPageNo = (int) (totalCount / pageSize);
			} else {
				totalPageNo = (int) (totalCount / pageSize) + 1;
			}
			LOGGER.info("用户昵称 = " + userId + " 总记录数 = " + totalCount + " 共分 " + totalPageNo + " 批次迁移数据");
			if (totalPageNo != 0) {
				// 创建线程池
				threadPool = Executors.newFixedThreadPool(threadPoolSize,
						new PidThreadFactory("TransferMysqlDataThreadPool", true));
				if ("crm_item".equals(tableName)) {
					// 商品
					this.transferItem(i, totalPageNo);
				} else {
					// 黑名单
					this.transferSmsBlackList(i, totalPageNo);
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
	 * @Description 迁商品
	 * @Date 2018年10月15日下午5:45:24
	 * @param i2
	 * @param totalPageNo
	 * @throws Exception
	 * @ReturnType void
	 */
	private void transferItem(int threadNum, int totalPageNo) throws Exception {
		// 线程集合
		List<ItemTransferDataThread> threadList = new ArrayList<ItemTransferDataThread>(totalPageNo);
		for (i = 1; i < totalPageNo + 1; i++) {
			int startPosition = 0;
			if (i == 1) {
				startPosition = 0;
			} else {
				startPosition = (i - 1) * pageSize;
			}
			int limit = pageSize;
			if (userUidMap.containsKey(userId)) {
				threadList.add(new ItemTransferDataThread(userId, userUidMap.get(userId), oldItemService,
						itemTransferService, startPosition, limit, startDate, endDate));
			} else {
				LOGGER.error("CRM_USER表中不存在 " + userId + " 用户");
			}
		}
		// 提交线程
		List<Future<Integer>> futureList = new ArrayList<Future<Integer>>(totalPageNo);
		for (i = 0; i < totalPageNo; i++) {
			futureList.add(threadPool.submit(threadList.get(i)));
			Thread.sleep(threadSleepMilliseconds);
		}
		Integer totalTransferDataResultCount = 0;
		Integer singleTransferDataResultCount = 0;
		// 获取结果
		for (i = 0; i < totalPageNo; i++) {
			singleTransferDataResultCount = futureList.get(i).get();
			totalTransferDataResultCount += singleTransferDataResultCount;
			LOGGER.info("第 " + (i + 1) + " 批次迁移数据量 = " + singleTransferDataResultCount);
		}
		LOGGER.info("用户昵称 = " + userId + " 实际迁移数据数量 = " + totalTransferDataResultCount);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 迁移黑名单数据
	 * @Date 2018年9月5日上午10:08:45
	 * @param i
	 * @param totalPageNo
	 * @ReturnType void
	 */
	private void transferSmsBlackList(int i, int totalPageNo) throws Exception {
		// 线程集合
		List<SmsBlackListTransferDataThread> threadList = new ArrayList<SmsBlackListTransferDataThread>(totalPageNo);
		for (i = 1; i < totalPageNo + 1; i++) {
			int startPosition = 0;
			if (i == 1) {
				startPosition = 0;
			} else {
				startPosition = (i - 1) * pageSize;
			}
			int limit = pageSize;
			if (userUidMap.containsKey(userId)) {
				threadList.add(new SmsBlackListTransferDataThread(userId, userUidMap.get(userId),
						oldSmsBlackListService, smsBlackListDTOService, startPosition, limit, startDate, endDate));
			} else {
				LOGGER.error("CRM_USER表中不存在 " + userId + " 用户");
			}
		}
		// 提交线程
		List<Future<Integer>> futureList = new ArrayList<Future<Integer>>(totalPageNo);
		for (i = 0; i < totalPageNo; i++) {
			futureList.add(threadPool.submit(threadList.get(i)));
			Thread.sleep(threadSleepMilliseconds);
		}
		Integer totalTransferDataResultCount = 0;
		Integer singleTransferDataResultCount = 0;
		// 获取结果
		for (i = 0; i < totalPageNo; i++) {
			singleTransferDataResultCount = futureList.get(i).get();
			totalTransferDataResultCount += singleTransferDataResultCount;
			LOGGER.info("第 " + (i + 1) + " 批次迁移数据量 = " + singleTransferDataResultCount);
		}
		LOGGER.info("用户昵称 = " + userId + " 实际迁移数据数量 = " + totalTransferDataResultCount);
	}

}
