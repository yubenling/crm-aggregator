package com.kycrm.transferdata.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.service.trade.IMongoHistroyTradeService;
import com.kycrm.transferdata.trade.service.ITradeTransferService;
import com.kycrm.transferdata.util.PidThreadFactory;
import com.kycrm.util.DateUtils;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;

public class RedistributeDataBaseThread implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedistributeDataBaseThread.class);

	private IMongoHistroyTradeService mongoHistroyTradeService;

	private ITradeTransferService tradeTransferService;

	private Long uid;

	private String sellerNick;

	// 每批次处理数据量
	private Integer pageSize = 1000;

	private ExecutorService threadPool;

	private static final Long THREAD_BASE_SLEEP_TIME = 1000L;

	public RedistributeDataBaseThread(IMongoHistroyTradeService mongoHistroyTradeService,
			ITradeTransferService tradeTransferService, Long uid, String sellerNick) {
		super();
		this.mongoHistroyTradeService = mongoHistroyTradeService;
		this.tradeTransferService = tradeTransferService;
		this.uid = uid;
		this.sellerNick = sellerNick;
	}

	@Override
	public void run() {
		StopWatch watch = new StopWatch();
		watch.start();
		try {
			byte[] compressData = null;
			byte[] uncompress = null;
			compressData = mongoHistroyTradeService.getDirtyData(uid, sellerNick);
			uncompress = GzipUtil.uncompress(compressData);
			List<TradeDTO> tradeDTOList = JsonUtil.readValuesAsArrayList(new String(uncompress), TradeDTO.class);
			int tradeDTOListSize = tradeDTOList.size();
			if (tradeDTOList != null && tradeDTOListSize != 0) {
				LOGGER.info("UID = " + uid + " 的非本用户数据大小 = " + tradeDTOListSize);
				Thread.sleep(2000);
				Map<Long, List<Long>> redistributeMap = new HashMap<Long, List<Long>>();
				List<Long> redistributeList = null;
				TradeDTO tradeDTO = null;
				Long uid = null;
				// 按uid归集trade
				for (int i = 0; i < tradeDTOListSize; i++) {
					tradeDTO = tradeDTOList.get(i);
					// 非本用户的UID
					uid = tradeDTO.getUid();
					if (redistributeMap.containsKey(uid)) {
						redistributeList = redistributeMap.get(uid);
					} else {
						redistributeList = new ArrayList<Long>();
					}
					redistributeList.add(tradeDTO.getTid());
					redistributeMap.put(uid, redistributeList);
				}
				int i = 0;
				// 按uid按pageSize分批发送
				for (Entry<Long, List<Long>> trade : redistributeMap.entrySet()) {
					int totalCount = trade.getValue().size();
					int totalPageNo = 0;
					if (totalCount % pageSize == 0) {
						totalPageNo = (int) (totalCount / pageSize);
					} else {
						totalPageNo = (int) (totalCount / pageSize) + 1;
					}
					LOGGER.info("集合名称 = TradeDTO" + trade.getKey() + " 总记录数 = " + totalCount + "共分 " + totalPageNo
							+ " 批次迁移数据 每批次共 " + pageSize + " 条数据");
					if (totalPageNo != 0) {
						// 创建线程池
						threadPool = Executors.newFixedThreadPool(1,
								new PidThreadFactory("TransferMongoDataBaseThread", true));
						this.transferRedistributeData(i, totalPageNo, trade.getKey(), trade.getValue());
						threadPool.shutdown();
					}
					Thread.sleep(2000);
				}
			}
			watch.stop();
			LOGGER.info("迁移数据结束时间 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
		} catch (IOException e) {
			e.printStackTrace();
			LOGGER.error("重新分发非指定用户的数据出错", e);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("重新分发非指定用户的数据出错", e);
		}
	}

	private void transferRedistributeData(int i, Integer totalPageNo, Long uid, List<Long> tidList) throws Exception {
		String collectionName = "TradeDTO" + uid;
		// 线程集合
		List<TradeGetLostDataThread> threadList = new ArrayList<TradeGetLostDataThread>(totalPageNo);
		for (i = 1; i < totalPageNo + 1; i++) {
			threadList.add(new TradeGetLostDataThread(uid, collectionName, tradeTransferService,
					mongoHistroyTradeService, tidList, false));
		}
		// 提交线程
		List<Future<Integer>> futureList = new ArrayList<Future<Integer>>(totalPageNo);
		for (i = 0; i < totalPageNo; i++) {
			futureList.add(threadPool.submit(threadList.get(i)));
			Thread.sleep(THREAD_BASE_SLEEP_TIME * 5);
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
