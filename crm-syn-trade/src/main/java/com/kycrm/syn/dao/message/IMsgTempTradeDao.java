package com.kycrm.syn.dao.message;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.base.BaseListEntity;
import com.kycrm.member.domain.entity.trade.MsgTempTrade;

public interface IMsgTempTradeDao {

	/**
	 * tableIsExist(查询库中是否有该uid的表)
	 * @Title: tableIsExist 
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return List<String> 返回类型 
	 * @throws
	 */
	public String tableIsExist(@Param("uid") Long uid);
	
	/**
	 * doCreateTable(创建该uid的表)
	 * @Title: doCreateTable 
	 * @param @param uid 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void doCreateTable(@Param("uid") Long uid);
	
	/**
	 * savemsgTempTrade(添加一条记录)
	 * @Title: savemsgTempTrade 
	 * @param @param tradeTemplate
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long saveMsgTempTrade(MsgTempTrade tradeTemplate);
	
	/**
	 * savemsgTempTrade(添加多条记录)
	 * @Title: savemsgTempTrade 
	 * @param @param tradeList 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveMsgTempTradeList(BaseListEntity<MsgTempTrade> tradeList);
	
	/**
	 * aggregateDataByStatus(根据订单来源和订单状态查询效果分析数据)
	 * @Title: aggregateDataByStatus 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Map<String,Object> 返回类型 
	 * @throws
	 */
	public Map<String, Object> aggregateDataByStatus(Map<String, Object> map);
	
	/**
	 * listPayData(计算用户选择时间段内每天成交数据)
	 * @Title: listPayData 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<Map<String,Object>> 返回类型 
	 * @throws
	 */
	public List<Map<String, Object>> listPayData(Map<String, Object> map);
	
	/**
	 * listTempTradeByMsgId(根据msgId查询符合的订单信息)
	 * @Title: listTempTradeByMsgId 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<MsgTempTrade> 返回类型 
	 * @throws
	 */
	public List<MsgTempTrade> listTempTradeByMsgId(Map<String, Object> map);
	
	/**
	 * listFifteenTrade(查询15天之前的订单)
	 * @Title: listFifteenTrade 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<MsgTempTrade> 返回类型 
	 * @throws
	 */
	public List<MsgTempTrade> listFifteenTrade(Map<String, Object> map);
	
	/**
	 * findMaxIdByTime(查询15天之前数据中最大的id)
	 * @Title: findMaxIdByTime 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long findMaxIdByTime(Map<String, Object> map);
	
	/**
	 * deleteDataById(批量删除数据)
	 * @Title: deleteDataById 
	 * @param @param map 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void deleteDataById(Map<String, Object> map);
	
	/**
	 * listCustomerDetail(客户详情列表)
	 * @Title: listCustomerDetail 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<MsgTempTrade> 返回类型 
	 * @throws
	 */
	public List<MsgTempTrade> listCustomerDetail(Map<String, Object> map);
	
	/**
	 * countCustomerDetail(客户详情总条数)
	 * @Title: countCustomerDetail 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Integer 返回类型 
	 * @throws
	 */
	public Integer countCustomerDetail(Map<String, Object> map);
	
	/**
	 * listItemDetail(订单详情)
	 * @Title: listItemDetail 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return List<MsgTempTrade> 返回类型 
	 * @throws
	 */
	public List<MsgTempTrade> listItemDetail(Map<String, Object> map);
	
	/**
	 * isExistMsgTrade(根据msgId,uid,tradeStatus,tid查询是否存在记录)
	 * @Title: isExistMsgTrade 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long isExistMsgTrade(Map<String, Object> map);

	public void addMsgTempTradeTableIndex(Long uid);
}
