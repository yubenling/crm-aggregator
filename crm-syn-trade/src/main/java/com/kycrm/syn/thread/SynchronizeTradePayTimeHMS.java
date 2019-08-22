package com.kycrm.syn.thread;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.service.order.IOrderDTOService;

public class SynchronizeTradePayTimeHMS implements Runnable {

	private Logger logger = LoggerFactory.getLogger(SynchronizeTradePayTimeHMS.class);

	private Long uid;

	private String column;

	private IOrderDTOService orderDTOService;

	public SynchronizeTradePayTimeHMS(Long uid, String column, IOrderDTOService orderDTOService) {
		super();
		this.uid = uid;
		this.column = column;
		this.orderDTOService = orderDTOService;
	}

	@Override
	public void run() {
		try {
			List<Long> idList = null;
			while (true) {
				idList = this.orderDTOService.getIdList(uid, column, 0L, 1000L);
				if (idList != null && idList.size() == 0) {
					logger.info("crm_order_dto" + uid + " 的trade_pay_time_hms同步完成");
					break;
				}
				this.orderDTOService.batchUpdateTradePayTimeHMS(uid, idList);
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
