package com.kycrm.member.service.traderate;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.traderate.TradeRates;

public interface ITradeRatesService {

	/**
	 * 保存一条记录
	 * @Title: saveTradeRate 
	 * @param  设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveTradeRates(TradeRates tradeRates);
	
	/**
	 * 保存多条记录
	 * @Title: saveTradeRateList 
	 * @param @param map 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveTradeRatesList(List<TradeRates> tradeRates);
	
	/**
	 * 根据条件查询评价订单数
	 * @Title: countTradeByParam 
	 * @param @param uid
	 * @param @param role
	 * @param @param beginTime
	 * @param @param endTime
	 * @param @param resultList
	 * @param @return 设定文件 
	 * @return long 返回类型 
	 * @throws
	 */
	public long countTradeByParam(Long uid,Date beginTime,Date endTime,List<String> resultList);
}
