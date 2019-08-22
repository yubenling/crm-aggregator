package com.kycrm.member.handler;



public interface IItemSysInfoService {
	
	/**
	 * 相同间隔时间的订单同步只允许一个job执行
	 * 
	 * @author: sungk
	 * @time: 2018年1月30日 下午4:16:00
	 */
	public void startSynItemBySysInfo();

}
