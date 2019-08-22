package com.kycrm.member.dao.order;

import java.util.Map;

/**
 * 测试使用
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月19日下午3:20:11
 * @Tags
 */
public interface ITestOrderDao {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 测试批量插入订单信息
	 * @Date 2018年7月19日下午3:20:18
	 * @param orderList
	 * @throws Exception
	 * @ReturnType void
	 */
	public void batchSaveOrderDTO(Map<String, Object> paramMap) throws Exception;

}
