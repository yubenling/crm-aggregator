package com.kycrm.member.dao.tradecenter;

import java.util.List;
import java.util.Map;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;
import com.kycrm.member.domain.vo.tradecenter.OrderReminderEffectVo;
import com.kycrm.member.domain.vo.tradecenter.TradeSetupVO;

public interface ITradeSetupDao  {

	/**
	* @Title: querySetupsByStatus
	* @Description: (展示各种订单中心的设置是否开启或者关闭)
	* @return List<TradeSetup>    返回类型
	* @author:jackstraw_yu
	*/
	public List<TradeSetup> querySetupsByStatus(Map<String, Object> map);
	
	
	
	/**
	* @Title: saveTradeSetup
	* @Description: (保存各种订单中心的设置)
	* @return void    返回类型
	* @author:jackstraw_yu
	*/
	void saveTradeSetup(TradeSetup tradeSetup);


	/** 
	* @Description 根据类型查询用户该设置的条数
	* @param uid
	* @param type
	* @return long    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午4:49:31
	*/
	long queryTradeSetupCount(Map<String, Object> map);


	/** 
	* @Description 根据设置的任务名称查询该类型的设置是否存在 
	* @param  sourceVO
	* @return TradeSetup    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午5:18:02
	*/
	 TradeSetup queryTradeSetupByTaskName(TradeSetupVO vo);

	/**
	* @Title: queryTradeSetupTable
	* @Description: (筛选订单中心相关设置列表)
	* @return List<TradeSetup>    返回类型
	* @author:jackstraw_yu
	*/
	List<TradeSetup> queryTradeSetupTable(TradeSetupVO setVo);

	/**
	* @Title: querySingleTradeSetup
	* @Description: (筛选单个订单中心相关设置)
	* @return  TradeSetup    返回类型
	* @author:jackstraw_yu
	*/
	TradeSetup querySingleTradeSetup(TradeSetupVO setVo);
	
	/**
	 * 订单中心根据条件筛选任务名称列表
	 * ztk2017年9月25日下午4:07:26
	 */
	List<TradeSetup> queryTradeSetupTaskNames(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 订单中心根据任务名称查询任务Id
	 * ztk2017年10月30日下午11:11:26
	 */
	Long queryTradeSetupId(OrderReminderEffectVo orderEffectVo);
	
	/**
	* @Title: deleteTradeSetup
	* @Description: (筛选订单中心相关设置)
	* @return Long    返回类型
	* @author:jackstraw_yu
	*/
	Long deleteTradeSetup(TradeSetupVO setVo);

	/**
	* @Title: switchTradeSetup
	* @Description: (打开或者关闭订单中心相关设置)
	* @return Long    返回类型
	* @author:jackstraw_yu
	*/
	Long switchTradeSetup(TradeSetupVO setVo);

	/**
	* @Title: updateTradeSetup
	* @Description: (修改订单中心相关设置)
	* @return Long    返回类型
	* @author:jackstraw_yu
	*/
	int updateTradeSetup(TradeSetup tradeSetup);

	/**
	* @Title: resetTradeSetupLevel
	* @Description: (修改订单中心设置的任务级别)
	* @return Long    返回类型
	* @author:jackstraw_yu
	*/
	void resetTradeSetupLevel(TradeSetupVO setVo);

	/** 
	* @Description 查询当前类型设置的最大任务级别
	* @param  tradeSetup
	* @return Integer    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午5:54:45
	*/
	Integer queryMaxTaskLevelByType(TradeSetup tradeSetup);


	/**
	* @Title: queryTradeSetupById
	* @Description: (通过id查询当前设置)
	* @return TradeSetup    返回类型
	* @author:jackstraw_yu
	*/
	TradeSetup queryTradeSetupById(Long id);

	/**
	* @Title: findTypeOpenBySellerNick
	* @Description: (判断该设置是否存在)
	* @return boolean   返回类型
	* @author:jackstraw_yu
	*/
	List<String> findTypeOpenBySellerNick(Long uid);
	
	/**
	 * 查询taskName对应的id
	 * ztk2017年11月3日上午10:05:14
	 */
	Long queryIdByTaskName(Map<String, Object> dataMap);
	
	
	
	/**
	* @Title: isExist
	* @Description: (判断该设置是否存在)
	* @return boolean   返回类型
	* @author:jackstraw_yu
	*/
	@Deprecated
	TradeSetup isExist(Map<String, String> map);
	@Deprecated
	void updateTradeSetupSync(TradeSetup tradeSetup);
	@Deprecated
	void deleteErrorData();
	@Deprecated
	long getRightDataCount();
	@Deprecated
	List<TradeSetup> getRightLimitData(Map<String, Object> map);

	/**
	 * 根据用户查询设置的任务(需要效果分析的类型中'2','3','4','6','7','8','9','12','14','37')
	 * ztk2017年11月30日下午4:54:13
	 */
	List<Long> queryTaskIdInUse(TradeSetup tradeSetup);
	
	/**
	 * 查询中差评监控是否开启
	 * @Title: queryStatusIsOpen 
	 * @param @return 设定文件 
	 * @return Boolean 返回类型 
	 * @throws
	 */
	Boolean queryStatusIsOpen(Long uid);
    /**
     * 查询所有订单中心的设置
     * @return
     */
	public List<TradeSetup> listAllTradeStep();
}
