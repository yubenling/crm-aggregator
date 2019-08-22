package com.kycrm.syn.thread;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.service.order.IOrderDTOService;

public class SynchronizeResult implements Runnable {

	private Logger logger = LoggerFactory.getLogger(SynchronizeResult.class);

	private Long uid;

	private String column;

	private IOrderDTOService orderDTOService;

	public SynchronizeResult(Long uid, String column, IOrderDTOService orderDTOService) {
		super();
		this.uid = uid;
		this.column = column;
		this.orderDTOService = orderDTOService;
	}

	@Override
	public void run() {
		try {
			List<Long> oidList = null;
			while (true) {
				oidList = this.orderDTOService.getOidList(uid, column, 0, 1000);
				if (oidList != null && oidList.size() == 0) {
					logger.info("crm_order_dto" + uid + " 的result同步完成");
					break;
				}
				this.orderDTOService.batchUpdateResult(uid, oidList);
				int hour = GregorianCalendar.getInstance().get(Calendar.HOUR_OF_DAY);
				if (hour >= 0 && hour <= 8) {
					Thread.sleep(200);
				} else {
					Thread.sleep(5000);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
