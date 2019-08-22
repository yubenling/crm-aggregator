package com.kycrm.syn.thread;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.util.DateUtils;

public class SynchronizeOrder implements Runnable {

	private Logger logger = LoggerFactory.getLogger(SynchronizeTradePayTimeHMS.class);

	private Long uid;

	private IOrderDTOService orderDTOService;

	public SynchronizeOrder(Long uid, IOrderDTOService orderDTOService) {
		super();
		this.uid = uid;
		this.orderDTOService = orderDTOService;
	}

	@Override
	public void run() {
		try {
			List<OrderDTO> orderList = null;
			Calendar c = GregorianCalendar.getInstance();
			while (true) {
				orderList = this.orderDTOService.getOrderList(uid, 0, 1000);
				if (orderList != null && orderList.size() == 0) {
					logger.info("crm_order_dto" + uid + " 的trade_pay_time_hms, trade_end_time_hms, week同步完成");
					break;
				}
				OrderDTO orderDTO = null;
				Date tradePayTime = null;
				Date tradeEndTime = null;
				for (int i = 0; i < orderList.size(); i++) {
					orderDTO = orderList.get(i);
					tradePayTime = orderDTO.getTradePayTime();
					if (tradePayTime != null) {
						orderDTO.setTradePayTimeHms(
								DateUtils.formatDate(tradePayTime, "yyyy-MM-dd HH:mm:ss").substring(11));
						c.setTime(tradePayTime);
						orderDTO.setWeek(c.get(Calendar.DAY_OF_WEEK) + "");
					}
					tradeEndTime = orderDTO.getTradeEndTime();
					if (tradeEndTime != null) {
						orderDTO.setTradeEndTimeHms(
								DateUtils.formatDate(tradeEndTime, "yyyy-MM-dd HH:mm:ss").substring(11));
					}
					orderList.set(i, orderDTO);
				}
				this.orderDTOService.batchUpdateOrder(uid, orderList);
				int hour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if (hour >= 0 && hour <= 8) {
					Thread.sleep(2000);
				} else {
					Thread.sleep(5000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
