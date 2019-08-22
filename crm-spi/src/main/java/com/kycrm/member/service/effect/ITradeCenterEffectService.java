package com.kycrm.member.service.effect;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.effect.TradeCenterEffect;
import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;


public interface ITradeCenterEffectService {

	/**
	 * 订单中心效果分析新增一条记录
	 * @Title: saveTradeCenterEffect 
	 * @param @param tradeCenterEffectVO 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void saveTradeCenterEffect(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 订单中心效果分析更新一条记录
	 * @Title: updateTradeCenterEffect 
	 * @param @param tradeCenterEffectVO 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void updateTradeCenterEffect(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 订单中心效果分析根据条件查询一条记录
	 * @Title: queryTradeEffect 
	 * @param @param tradeCenterEffectVO
	 * @param @return 设定文件 
	 * @return TradeCenterEffect 返回类型 
	 * @throws
	 */
	TradeCenterEffect queryTradeEffect(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 订单中心效果分析查询多条记录
	 * @Title: queryTradeEffectList 
	 * @param @param tradeCenterEffectVO
	 * @param @return 设定文件 
	 * @return List<TradeCenterEffectVO> 返回类型 
	 * @throws
	 */
	List<TradeCenterEffect> queryTradeEffectList(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 订单中心效果分析聚合查询(按照日期正序排列)
	 * @Title: aggregateTradeCenterList 
	 * @param @param tradeCenterEffectVO
	 * @param @return 设定文件 
	 * @return List<TradeCenterEffect> 返回类型 
	 * @throws
	 */
	List<TradeCenterEffect> aggregateTradeCenterList(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 首页催付金额聚合查询
	 * @Title: sumRefundFee 
	 * @param @param uid
	 * @param @param typeList
	 * @param @param startTime
	 * @param @param endTime
	 * @param @return 设定文件 
	 * @return double 返回类型 
	 * @throws
	 */
	double sumRefundFee(Long uid,List<String> typeList,Date startTime,Date endTime);
	
	/**
	 * 每天定时2小时跑定时，保存订单中心效果分析数据到mysql
	 * @Title: tradeCenterEffectJob 
	 * @param @param synchTime 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	void tradeCenterEffectJob(Date synchTime);

	void tradeCenterEffectJob(Date synchTime, Integer h, Integer m);
}
