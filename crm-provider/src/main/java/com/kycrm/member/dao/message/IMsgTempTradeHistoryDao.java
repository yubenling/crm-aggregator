package com.kycrm.member.dao.message;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.base.BaseListEntity;
import com.kycrm.member.domain.entity.trade.MsgTempTrade;
import com.kycrm.member.domain.entity.trade.MsgTempTradeHistory;

public interface IMsgTempTradeHistoryDao {
	
	/**
	 * tableIsExist(分表是否存在该用户的表)
	 * @Title: tableIsExist 
	 * @param @param uid
	 * @param @return 设定文件 
	 * @return List<String> 返回类型 
	 * @throws
	 */
	public List<String> tableIsExist(@Param("uid") Long uid);
	
	/**
	 * doCreateTable(创建该用户的表)
	 * @Title: doCreateTable 
	 * @param @param uid 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void doCreateTable(@Param("uid") Long uid);
	/**
	 * savemsgTempTradeHistory(添加一条记录)
	 * @Title: savemsgTempTrade 
	 * @param @param tradeTemplate
	 * @param @return 设定文件 
	 * @return Long 返回类型 
	 * @throws
	 */
	public Long saveMsgTempTradeHistory(MsgTempTrade tradeTemplate);
	
	/**
	 * savemsgTempTradeHistory(添加多条记录)
	 * @Title: savemsgTempTrade 
	 * @param @param tradeList 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void saveMsgTempTradeHistoryList(BaseListEntity<MsgTempTrade> tradeList);
	
	/**
	 * aggregateDataByStatus(根据订单来源和订单状态查询效果分析数据)
	 * @Title: aggregateDataByStatus 
	 * @param @param map
	 * @param @return 设定文件 
	 * @return Map<String,Object> 返回类型 
	 * @throws
	 */
	public MsgTempTradeHistory aggregateDataByStatus(Map<String, Object> map);
	
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
	public List<MsgTempTradeHistory> listTempTradeByMsgId(Map<String, Object> map);
	
	
	public List<MsgTempTradeHistory> listCustomerDetail(Map<String, Object> map);
	
	
	public Integer countCustomerDetail(Map<String, Object> map);
	
	public void addMsgTempTradeHistoryTableIndex(Long uid);
	
	/**
	 * aggregateStepDataByStatus(预售效果分析  根据订单来源和订单状态查询效果分析数据)
	 * @Title: aggregateStepDataByStatus 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return MsgTempTrade 返回类型 
	 * @throws
	 */
	public MsgTempTrade aggregateStepDataByStatus(Map<String, Object> map) throws Exception;
	
	/**
	 * listStepPayData(预售效果分析  计算用户选择时间段内每天成交数据)
	 * @Title: listStepPayData 
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return List<Map<String,Object>> 返回类型 
	 * @throws
	 */
	public List<Map<String, Object>> listStepPayData(Map<String, Object> map) throws Exception;
	
	
	/**
	 * listStepPayData(预售效果分析  计算用户选择时间段内每天成交数据)
	 * @Title: listStepPayData 
	 * @param @return
	 * @param @throws Exception 设定文件 
	 * @return List<Map<String,Object>> 返回类型 
	 * @throws
	 */
	public List<Map<String, Object>> listStepPayFrontData(Map<String, Object> map) throws Exception;
	
	public List<MsgTempTradeHistory> listStepCustomerDetail(Map<String, Object> map);
	
	
	public Integer countStepCustomerDetail(Map<String, Object> map);
}
