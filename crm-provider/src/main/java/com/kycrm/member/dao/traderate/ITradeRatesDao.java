package com.kycrm.member.dao.traderate;

import java.util.Map;

import com.kycrm.member.domain.entity.traderate.TradeRates;

public interface ITradeRatesDao {

	/**
	 * 保存一条记录
	 * @Title: saveTradeRate 
	 * @param  设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveTradeRates(TradeRates tradeRate);
	
	/**
	 * 保存多条记录
	 * @Title: saveTradeRateList 
	 * @param @param map 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveTradeRatesList(Map<String, Object> map);
	
	/**
	 * 根据条件查询评价订单数
	 * @Title: countTradeRateByParam 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long countTradeByParam(Map<String, Object> map);
	
	/**
	 * 保存拉取的淘宝评价实体集合
	 * @Title: saveTBTradeRateList 
	 * @param @param map 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public Long saveTBTradeRateList(Map<String, Object> map);
}
