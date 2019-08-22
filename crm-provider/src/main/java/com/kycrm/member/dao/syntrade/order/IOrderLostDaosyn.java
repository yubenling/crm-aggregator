package com.kycrm.member.dao.syntrade.order;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface IOrderLostDaosyn {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询有order没有trade的tid集合
	 * @Date 2018年9月27日下午9:49:15
	 * @param uid
	 * @throws Exception
	 * @ReturnType List<Long>
	 */
	public List<Long> getHasOrderButNotFoundTradeList(@Param("uid") Long uid) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询有order没有member的tid集合
	 * @Date 2018年9月27日下午9:49:29
	 * @param uid
	 * @throws Exception
	 * @ReturnType List<Long>
	 */
	public List<Long> getHasOrderButNotFoundMemberList(@Param("uid") Long uid) throws Exception;
}
