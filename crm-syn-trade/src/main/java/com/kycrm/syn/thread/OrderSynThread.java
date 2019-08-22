package com.kycrm.syn.thread;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.service.order.IOrderDTOService;

/**
 * @author wy
 * @version 创建时间：2018年2月28日 上午11:26:45
 */
public class OrderSynThread implements Runnable {

	private CountDownLatch latch;

	private IOrderDTOService orderDTOService;

	private Long uid;

	private List<OrderDTO> orderDTOList;

	private Logger logger = LoggerFactory.getLogger(OrderSynThread.class);

	public OrderSynThread(/* CountDownLatch countDownLatch, */ IOrderDTOService orderDTOService, Long uid,
			List<OrderDTO> orderDTOList) {
		super();
		/* this.latch = countDownLatch; */
		this.orderDTOService = orderDTOService;
		this.uid = uid;
		this.orderDTOList = orderDTOList;
	}

	@Override
	public void run() {
		try {
			if (uid == null) {
				logger.info("OrderSynThread的uid为空");
				return;
			}
			orderDTOService.batchInsertOrderList(uid, orderDTOList);
		} catch (Exception e) {
			e.printStackTrace();
		} /*
			 * finally { latch.countDown(); }
			 */
	}

}
