package com.kycrm.member.service.tradecenter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;
import com.kycrm.member.domain.vo.tradecenter.TradeSetupVO;

/** 
* @ClassName: ITradeSetupService 
* @Description 订单中心设置
* @author jackstraw_yu
* @date 2018年1月23日 上午11:27:28 
*/
public interface ITradeSetupService {

	public List<String> findTypeBySellerNickTmc(Long uid);
	
	/** 
	* @Description 展示各种订单中心的设置是否开启或者关闭<br/>
	* key:type(订单中心各种设置类型)<br/>
	* value:status(true:有开启;false:未设置或者未开启)
	* @param  uid
	* @param  set
	* @return Map<String,Boolean>    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 上午11:52:41
	*/
	public Map<String, Boolean> showSetupMenu(Long uid,Set<String> hashSet);

	/** 
	* @Description 根据类型查询用户该设置的条数
	* @param uid
	* @param type
	* @return long    返回类型 
	* @author jackstraw_yu
	 * @param status 
	* @date 2018年1月23日 下午4:49:31
	*/
	public long queryTradeSetupCount(Long uid, String type, Boolean status);

	/** 
	* @Description 根据设置的任务名称查询该类型的设置是否存在 
	* @param  sourceVO
	* @return TradeSetup    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午5:18:02
	*/
	public TradeSetup queryTradeSetupByTaskName(TradeSetupVO sourceVO);

	/** 
	* @Description 根据类型查询该类型的设置的最大任务级别
	* @param @param tradeSetup
	* @param @return    设定文件 
	* @return Integer    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午5:53:09
	* @throws 
	*/
	public Integer queryMaxTaskLevelByType(TradeSetup tradeSetup);

	/** 
	* @Description 保存订单中心设置
	* @param  tradeSetup
	* @return TradeSetup    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月23日 下午5:59:19
	*/
	public TradeSetup saveTradeSetup(TradeSetup tradeSetup);

	/** 
	* @Description 筛选多任务订单中心相关设置集合
	* @param  setup
	* @return List<TradeSetup>    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 上午10:55:49
	*/
	public List<TradeSetup> queryTradeSetupTable(TradeSetupVO setup);

	/** 
	* @Description 查询订单中心单条设置
	* @param  setup
	* @return TradeSetup    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 上午11:11:40
	*/
	public TradeSetup querySingleTradeSetup(TradeSetupVO setup);

	/** 
	* @Description 查询单条订单中心设置用于数据回填
	* @param  setup
	* @return TradeSetup    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 上午11:21:48
	*/
	public TradeSetup showSingleTradeSetup(TradeSetupVO setup);

	/** 
	* @Description 删除订单中心设置
	* @param  setup    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 上午11:31:07
	*/
	public void deleteTradeSetup(TradeSetupVO setup);

	/** 
	* @Description 打开或者关闭订单中心设置
	* @param  setup    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 上午11:44:38
	*/
	public void switchTradeSetup(TradeSetupVO setup);

	/** 
	* @Description 重置任务级别
	* @param  setup    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 上午11:51:21
	*/
	public void resetTradeSetupLevel(TradeSetupVO setup);

	/** 
	* @Description 更新订单中心设置
	* @param  tradeSetup    设定文件 
	* @return void    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月24日 下午1:44:27
	*/
	public void updateTradeSetup(TradeSetup tradeSetup);

	/**
	 * 订单中心根据条件筛选任务名称列表
	 * @Title: queryTradeSetupTaskNames 
	 * @param @param tradeCenterEffectVO
	 * @param @return 设定文件 
	 * @return List<TradeSetup> 返回类型 
	 * @throws
	 */
	public List<TradeSetup> queryTradeSetupTaskNames(TradeCenterEffectVO tradeCenterEffectVO);
	
	/**
	 * 查询订单中心开启设置的所有taskId
	 * @Title: queryTaskIdIsUse 
	 * @param @param uid
	 * @param @param inUse
	 * @param @return 设定文件 
	 * @return List<Long> 返回类型 
	 * @throws
	 */
	public List<Long> queryTaskIdIsUse(Long uid,Boolean inUse);
	
	/**
	 * 查询用户是否开启中差评监控
	 * @Title: queryStatusIsOpen 
	 * @param @param uid 设定文件 
	 * @return boolean 返回类型 
	 * @throws
	 */
	public boolean queryStatusIsOpen(Long uid);
    /**
     * 查询所有的订单中心的设置
     * @return
     */
	public List<TradeSetup> listAllTradeStep();
	/**
	 * 添加授权
	 */
	public void addUserPermitByMySql(final Long uid,final String userName);
}
