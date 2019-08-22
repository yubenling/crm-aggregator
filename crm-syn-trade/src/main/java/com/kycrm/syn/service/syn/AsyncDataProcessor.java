package com.kycrm.syn.service.syn;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.domain.entity.trade.TbTrade;
import com.taobao.api.internal.util.NamedThreadFactory;

@Component
public class AsyncDataProcessor {

	private static Logger logger = LoggerFactory.getLogger(AsyncDataProcessor.class);

	@Autowired
	private TradeSysInfoService tradeSysInfoService;

	@Autowired
	private MemberSyncArtifactService memberSyncArtifactService;

	private static ExecutorService threadPool = Executors.newFixedThreadPool(100,
			new NamedThreadFactory("AsyncDataProcessorBaseThread", true));

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 处理主订单和子订单数据
	 * @Date 2018年9月22日下午7:00:37
	 * @throws Exception
	 * @ReturnType void
	 */
	public void submitTradeAndOrderTask(List<TbTrade> tradeList) throws Exception {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (null != tradeList && tradeList.size() > 0) {
						// 开始处理数据
						tradeSysInfoService.processTbTradeData(tradeList);
					}
				} catch (Throwable t) {
					t.printStackTrace();
					logger.error("trade处理线程异常", t);
				}
			}
		});
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 处理会员和多地址数据
	 * @Date 2018年9月22日下午7:00:51
	 * @throws Exception
	 * @ReturnType void
	 */
	public void submitMemberAndReceiveDetailTask(List<TbTrade> tradeList) throws Exception {
		threadPool.execute(new Runnable() {
			@Override
			public void run() {
				try {
					if (null != tradeList && tradeList.size() > 0) {
						// 开始处理数据
						memberSyncArtifactService.convertAndSaveData(tradeList);
					}
				} catch (Throwable t) {
					t.printStackTrace();
					logger.info("Member批处理线程开启或执行失败 :" + t.getMessage());
				}
			}
		});
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 出现异常情况停止处理数据并销毁线程池
	 * @Date 2018年9月22日下午7:14:20
	 * @throws Exception
	 * @ReturnType void
	 */
	public void destory() throws Exception {
		threadPool.shutdown();
	}

}
