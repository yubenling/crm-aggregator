package com.kycrm.syn.thread;

import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.trade.ITradeDTOService;

/**
 * 修改MySQL数据库表
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年9月28日下午8:37:43
 * @Tags
 */
public class UpdateTable implements Runnable {

	private ITradeDTOService tradeDTOService;

	private IOrderDTOService orderDTOService;

	private Long uid;

	private String taoBaoUserNick;

	private String methodName;

	public UpdateTable(ITradeDTOService tradeDTOService, IOrderDTOService orderDTOService, Long uid,
			String taoBaoUserNick, String methodName) {
		super();
		this.tradeDTOService = tradeDTOService;
		this.orderDTOService = orderDTOService;
		this.uid = uid;
		this.taoBaoUserNick = taoBaoUserNick;
		this.methodName = methodName;
	}

	@Override
	public void run() {
		try {
			if ("fixColumnLength".equals(methodName)) {// 修改字段长度
				this.orderDTOService.fixColumnLength(uid);
			} else if ("updateTradeCreatedTime".equals(methodName)) {// 更新字段值
				this.orderDTOService.updateTradeCreatedTime(uid);
			} else if ("deleteDirtyData".equals(methodName)) {// 删除trade表中的脏数据
				this.tradeDTOService.deleteDirtyDataBySellerNick(uid, taoBaoUserNick);
			} else {
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
