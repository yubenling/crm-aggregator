package com.kycrm.member.dao.effect;

import java.util.List;

import com.kycrm.member.domain.entity.effect.TradeCenterEffect;
import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;

public interface ITradeCenterEffectDao {
	
	
	public String tableIsExist(Long uid);

	/**
	 * 保存一条记录
	 * @Title: saveTradeCenterEffect 
	 * @param @param tradeCenterEffectVO 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveTradeCenterEffect(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 更新一条记录
	 * @Title: updateTradeCenterEffect 
	 * @param @param tradeCenterEffectVO 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void updateTradeCenterEffect(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 查询一条记录
	 * @Title: queryTradeEffect 
	 * @param @param tradeCenterEffectVO
	 * @param @return 设定文件 
	 * @return TradeCenterEffect 返回类型 
	 * @throws
	 */
	public TradeCenterEffect queryTradeEffect(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 查询多条记录
	 * @Title: queryTradeEffectList 
	 * @param @param tradeCenterEffectVO
	 * @param @return 设定文件 
	 * @return List<TradeCenterEffect> 返回类型 
	 * @throws
	 */
	public List<TradeCenterEffect> queryTradeEffectList(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 订单中心效果分析列表聚合查询
	 * @Title: aggregateTradeCenterList 
	 * @param @param tradeCenterEffectVO
	 * @param @return 设定文件 
	 * @return List<TradeCenterEffect> 返回类型 
	 * @throws
	 */
	public List<TradeCenterEffect> aggregateTradeCenterList(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 首页催付金额聚合查询
	 * @Title: aggregateEarningFee 
	 * @param @param tradeCenterEffectVO
	 * @param @return 设定文件 
	 * @return TradeCenterEffect 返回类型 
	 * @throws
	 */
	public Double aggregateEarningFee(TradeCenterEffectVO tradeCenterEffectVO);
}
