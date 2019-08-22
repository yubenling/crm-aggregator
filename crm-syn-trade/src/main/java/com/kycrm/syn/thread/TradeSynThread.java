package com.kycrm.syn.thread;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.service.trade.ITradeDTOService;

/**
 * @author wy
 * @version 创建时间：2018年2月28日 上午11:26:45
 */
public class TradeSynThread implements Runnable {

	// private CountDownLatch latch;

	private ITradeDTOService tradeDTOService;

	private Long uid;

	private List<TradeDTO> tradeDTOList = new ArrayList<TradeDTO>();

	private Logger logger = LoggerFactory.getLogger(TradeSynThread.class);

	public TradeSynThread(/* CountDownLatch countDownLatch, */ ITradeDTOService tradeDTOService, Long uid,
			List<TradeDTO> tradeDTOList) {
		super();
		/* this.latch = countDownLatch; */
		this.tradeDTOService = tradeDTOService;
		this.uid = uid;
		if (tradeDTOList != null)
			this.tradeDTOList.addAll(tradeDTOList);

	}

	@Override
	public void run() {
		try {
			if (uid == null) {
				logger.info("TradeSynThread的uid为空");
				return;
			}
			tradeDTOService.batchInsertTradeList(uid, tradeDTOList);
		} catch (Exception e) {
			e.printStackTrace();
		} /*
			 * finally { latch.countDown(); }
			 */
	}

}
