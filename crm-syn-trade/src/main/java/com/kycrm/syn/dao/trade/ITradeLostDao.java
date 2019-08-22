package com.kycrm.syn.dao.trade;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface ITradeLostDao {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询有trade没有order的tid集合
	 * @Date 2018年9月27日下午9:49:00
	 * @param uid
	 * @throws Exception
	 * @ReturnType List<Long>
	 */
	public List<Long> getHasTradeButNotFoundOrderList(@Param("uid") Long uid) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询有trade没有member的tid集合
	 * @Date 2018年9月27日下午9:49:26
	 * @param uid
	 * @throws Exception
	 * @ReturnType List<Long>
	 */
	public List<Long> getHasTradeButNotFoundMemberList(@Param("uid") Long uid) throws Exception;

}
