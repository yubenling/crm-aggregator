package com.kycrm.member.handler;
import java.util.List;

import com.kycrm.member.domain.entity.base.TradeAndOrderDataCollector;
import com.kycrm.member.domain.entity.trade.TbTrade;
import com.kycrm.member.domain.entity.trade.TradeDTO;

public interface ITradeSysInfoServiceSyn {
	
	/**
	 * 相同间隔时间的订单同步只允许一个job执行
	 * 
	 * @author: wy
	 * @time: 2018年1月30日 下午4:16:00
	 */
	public void startSynTradeBySysInfo() ;

	
	/**
	 * 订单数据转换，根据用户分类并保存<
	 * 
	 * @author: wy
	 * @time: 2018年1月30日 下午2:20:30
	 * @param tradeList
	 *            要保存的订单集合
	 */
	public void processTbTradeData(List<TbTrade> tbTradeList) throws Exception;

	/**
	 * 分离保存Trade和Order
	 * 
	 * @param tradeDTO
	 */
	public TradeAndOrderDataCollector assembleTradeAndOrder(List<TbTrade> tbTradeList) throws Exception ;



	public void batchProcessTradeByRedisLock(Long uid, List<TbTrade> tbTradeList) throws Exception;

	/**
	 * 从TbTrade获取TradeDTO
	 * 
	 * @param tbTrade
	 * @return TradeDTO
	 */
	public TradeDTO getTradeDTO(TbTrade tbTrade) ;

	

	

}
