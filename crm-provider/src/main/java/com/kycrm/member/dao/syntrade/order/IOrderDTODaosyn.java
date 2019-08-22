package com.kycrm.member.dao.syntrade.order;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.ibatis.annotations.Param;

import com.kycrm.member.domain.entity.member.TempEntity;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.order.OrderTempEntity;

public interface IOrderDTODaosyn {

	/**
	 * 自动创建用户表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:46:14
	 * @param uid
	 *            用户表主键id
	 */
	public void doCreateTableByNewUser(Long uid);

	/**
	 * 创建索引
	 * 
	 * @author: sungk
	 * @time: 2018年08月27日 上午11:46:14
	 * @param uid
	 *            用户表主键id
	 */
	public void addOrderTableIndex(Long uid);

	/**
	 * 是否存在短信记录表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:48:10
	 * @param uid
	 * @return
	 */
	public List<String> isExistsTable(Long uid);

	/**
	 * 批量保存子订单
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 下午3:15:49
	 * @param orderDTOList
	 */
	public void doCreateOrderDTOByList(Map<String, Object> map);

	/**
	 * 单个保存子订单
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 下午3:17:43
	 * @param orderDTO
	 */
	public void doCreateOrderDTOByBySingle(OrderDTO orderDTO);

	/**
	 * 批量更新子订单
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 下午3:35:05
	 * @param orderDTOList
	 */
	public void doUpdateOrderDTOByList(Map<String, Object> map);

	/**
	 * 更新单个子订单信息
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 下午3:35:50
	 * @param orderDTO
	 */
	public void doUpdateOrderDTOBySingle(OrderDTO orderDTO);

	public String findStatusByOid(Map<String, Long> map);

	public void updateTableIndex(@Param("uid") Long uid) throws Exception;

	public void fixColumnLength1(@Param("uid") Long uid) throws Exception;

	public void fixColumnLength2(@Param("uid") Long uid) throws Exception;

	public void updateTradeCreatedTime(@Param("uid") Long uid) throws Exception;

	public List<Long> getMultiOrderTidList(@Param("uid") Long uid) throws Exception;

	public Long checkColumnIfExist(@Param("uid") Long uid, @Param("columnName") String columnName) throws Exception;

	public void updateOrderResult(Map<String, Object> map);

	public Long getCountByCondition(@Param("uid") Long uid, @Param("endDate") Date endDate,
			@Param("column") String column) throws Exception;

	public List<Long> getIdList(@Param("uid") Long uid, @Param("column") String column,
			@Param("startPoint") Long startPoint, @Param("range") Long range) throws Exception;

	public void batchUpdateTradePayTimeHMS(@Param("uid") Long uid, @Param("idList") List<Long> idList) throws Exception;

	public void batchUpdateTradeEndTimeHMS(@Param("uid") Long uid, @Param("idList") List<Long> idList) throws Exception;

	public void batchUpdateWeek(@Param("uid") Long uid, @Param("idList") List<Long> idList) throws Exception;

	public List<TempEntity> getAddItemNumFromOrderTable(@Param("uid") Long uid,
			@Param("buyerNickList") Set<String> buyerNickList) throws Exception;

	public List<Long> getOidList(@Param("uid") Long uid, @Param("column") String column,
			@Param("startPoint") Integer startPoint, @Param("range") Integer range) throws Exception;

	public void batchUpdateResult(@Param("uid") Long uid, @Param("idList") List<Long> idList) throws Exception;

	public List<OrderDTO> getOrderList(@Param("uid") Long uid, @Param("startPoint") Integer startPoint,
			@Param("range") Integer range) throws Exception;

	public void batchUpdateOrder(@Param("uid") Long uid, @Param("orderList") List<OrderDTO> orderList) throws Exception;

	public List<OrderTempEntity> getTradeAmountAndItemNum(@Param("uid") Long uid,
			@Param("buyerNickSet") Set<String> buyerNickSet) throws Exception;

	public List<OrderTempEntity> getAddItemNum(@Param("uid") Long uid, @Param("buyerNickSet") Set<String> buyerNickSet)
			throws Exception;

	public List<OrderTempEntity> getCloseTradeAmountCloseItemNum(@Param("uid") Long uid,
			@Param("buyerNickSet") Set<String> buyerNickSet) throws Exception;

	public List<OrderTempEntity> getLastTradeTimeFirstPayTimeFirstTradeTimeLastPayTime(@Param("uid") Long uid,
			@Param("buyerNickSet") Set<String> buyerNickSet) throws Exception;

	public List<OrderTempEntity> getFirstTradeFinishTimeLastTradeFinishTime(@Param("uid") Long uid,
			@Param("buyerNickSet") Set<String> buyerNickSet) throws Exception;

	public List<OrderTempEntity> getTbrefundStatusTbOrderStatus(@Param("uid") Long uid,
			@Param("buyerNickSet") Set<String> buyerNickSet) throws Exception;

	public void batchUpdateOrderStatus(@Param("uid") Long uid, @Param("orderDTOList") List<OrderDTO> orderDTOList)
			throws Exception;

	/**
	 * 更新子订单表字段长度
	 * @param map
	 */
	public void alterTableOrder(Map<String, Object> map);
}
