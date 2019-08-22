package com.kycrm.member.service.trade;

import java.util.List;

import com.kycrm.member.domain.entity.trade.TbTrade;

public interface IMongoHistroyTradeService {

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 迁移主订单和子订单数据
	 * @Date 2018年9月29日上午10:25:25
	 * @param uid
	 * @param compress
	 * @throws Exception
	 * @ReturnType void
	 */
	public void synTradeAndOrderMongoDate(Long uid, byte[] compress) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 迁移会员和多地址数据
	 * @Date 2018年9月29日上午10:25:10
	 * @param uid
	 * @param compress
	 * @throws Exception
	 * @ReturnType void
	 */
	public void synMemberAndReceiveDetailMongoDate(Long uid, byte[] compress) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 同步历史订单
	 * @Date 2018年9月29日上午10:25:00
	 * @param tradeList
	 * @throws Exception
	 * @ReturnType void
	 */
	public void synOrderHistory(List<TbTrade> tradeList) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取丢失数据重新迁移
	 * @Date 2018年9月29日上午10:24:42
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType List<Long>
	 */
	public List<Long> getLostTidList(Long uid) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取非该用户的所有脏数据
	 * @Date 2018年9月29日上午10:27:21
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType byte[]
	 */
	public byte[] getDirtyData(Long uid, String sellerNick) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询Trade表中num字段等于0或者等于null的tid
	 * @Date 2018年10月8日下午5:07:56
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType List<Long>
	 */
	public List<Long> getTidWhetherTradeNumIsNullOrEqualsZero(Long uid) throws Exception;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取多子订单的tid
	 * @Date 2018年10月9日下午4:52:23
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType byte[]
	 */
	public byte[] getMultiOrderTidList(Long uid) throws Exception;

	public void updateLastMarketingTime(Long uid);

}
