package com.kycrm.member.dao.syntrade.trade;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.member.TempEntity;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.entity.trade.TradeTempEntity;

public interface ITradeDTODaosyn {

	/**
	 * 自动创建用户表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:46:14
	 * @param uid
	 *            用户表主键id
	 */
	public void doCreateTableByNewUser(String uid);

	/**
	 * 是否存在短信记录表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:48:10
	 * @param uid
	 * @return
	 */
	public List<String> isExistsTable(String uid);

	/**
	 * 批量保存新的订单
	 * 
	 * @author: wy
	 * @time: 2018年1月22日 上午11:29:21
	 * @param tradeDTO
	 */
	public void doCreateTradeDTOByList(Map<String, Object> map) throws Exception;

	/**
	 * 保存单个短信记录
	 * 
	 * @author: wy
	 * @time: 2018年1月22日 下午2:20:09
	 * @param tradeDTO
	 */
	public void doCreateTradeDTOByBySingle(TradeDTO tradeDTO);

	/**
	 * 通过主订单号找到订单状态
	 * 
	 * @author: wy
	 * @time: 2018年1月22日 下午2:58:27
	 * @param map
	 *            tid 主订单号 uid 用户主键id
	 * @return
	 */
	public String findStatusByTid(Map<String, Long> map);

	/**
	 * 批量更新多个订单
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 上午11:24:54
	 * @param tradeDTOList
	 */
	// public void doUpdateTradeDTOByList(BaseListEntity<TradeDTO>
	// tradeDTOList);
	public void doUpdateTradeDTOByList_bak(Map<String, Object> paramMap);

	/**
	 * 批量更新多个订单
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 上午11:24:54
	 * @param tradeDTOList
	 */
	// public void doUpdateTradeDTOByList(BaseListEntity<TradeDTO>
	// tradeDTOList);
	public void doUpdateTradeDTOByList(Map<String, Object> paramMap);

	/**
	 * 更新单个订单
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 上午11:25:28
	 * @param tradeDTO
	 */
	public void doUpdateTradeDTOBySingle(TradeDTO tradeDTO);

	public TradeDTO findTradeDTOByTid(Map<String, Object> map);

	public void addTradeTableIndex(String userId);

	public void updateTableIndex(Long uid);

	public List<TradeDTO> getDirtyData(Map<String, Object> paramMap) throws Exception;

	public void deleteDirtyDataBySellerNick(Map<String, Object> paramMap) throws Exception;

	public List<Long> getTidWhetherTradeNumIsNullOrEqualsZero(Map<String, Object> paramMap) throws Exception;

	public List<TempEntity> getAddNumberFromTradeTable(@Param("uid") Long uid,
			@Param("buyerNickList") Set<String> buyerNickList) throws Exception;

	public List<TempEntity> getBuyNumberFromTradeTable(@Param("uid") Long uid,
			@Param("buyerNickList") Set<String> buyerNickList) throws Exception;

	public List<TempEntity> getFirstPayTimeFromTradeTable(@Param("uid") Long uid,
			@Param("buyerNickList") Set<String> buyerNickList) throws Exception;

	public List<TradeTempEntity> getTradeNumByBuyerNick(@Param("uid") Long uid,
			@Param("buyerNickSet") Set<String> buyerNickSet) throws Exception;

	public List<TradeTempEntity> getCloseTradeNum(@Param("uid") Long uid,
			@Param("buyerNickSet") Set<String> buyerNickSet) throws Exception;

	public List<TradeTempEntity> getAddNumber(@Param("uid") Long uid, @Param("buyerNickSet") Set<String> buyerNickSet)
			throws Exception;

	public List<TradeTempEntity> getBuyerAlipayNo(@Param("uid") Long uid,
			@Param("buyerNickSet") Set<String> buyerNickSet) throws Exception;

	public List<TradeTempEntity> getReceiverInfoStr(@Param("uid") Long uid,
			@Param("buyerNickSet") Set<String> buyerNickSet) throws Exception;
	
	/**
	 * 更新主订单表字段长度
	 * @param map
	 */
	public void alterTableTrade(Map<String, Object> map);

}