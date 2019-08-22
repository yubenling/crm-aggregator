package com.kycrm.member.service.order;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kycrm.member.domain.entity.member.TempEntity;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.order.OrderTempEntity;
import com.kycrm.member.domain.vo.order.OrderVO;

/**
 * @author wy
 * @version 创建时间：2018年1月18日 下午2:38:13
 */
public interface IOrderDTOService {

	/**
	 * 根据用户主键id创建对应的短信记录表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:53:10
	 * @param uid
	 *            用户主键id
	 */
	public void doCreateTableByNewUser(Long uid);

	/**
	 * 根据子订单号查询子订单的状态
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 下午3:39:18
	 * @param uid
	 * @param oid
	 * @return
	 */
	public String findOrderStatusByOid(Long uid, Long oid);

	/**
	 * 批量保存子订单
	 * 
	 * @author: wy
	 * @time: 2018年1月22日 下午4:31:37
	 * @param uid
	 *            用户主键id
	 * @param tradeList
	 *            要保持的子订单集合
	 */
	public void batchInsertOrderList(Long uid, List<OrderDTO> orderList) throws Exception;

	/**
	 * 查询订单分页数据
	 * 
	 * @author HL
	 * @time 2018年2月1日 下午2:44:39
	 * @param odVo
	 * @return
	 */
	public Map<String, Object> findOrderDTOPage(Long uid, OrderVO odVo, String accessToken);

	/**
	 * 通过oid 查询订单是否存在
	 * 
	 * @author HL
	 * @time 2018年6月1日 下午3:05:24
	 * @param uid
	 * @param newTid
	 * @return
	 */
	public Integer findOneOrderDTOByTid(Long uid, String newTid);

	/**
	 * listOrderByTid(tid查询子订单) @Title: listOrderByTid @param @return
	 * 设定文件 @return List<OrderDTO> 返回类型 @throws
	 */
	public List<OrderDTO> listOrderByTid(Long uid, Long tid);

	/**
	 * listOrderByPhone(手机号查订单) @Title: listOrderByPhone @param @param
	 * uid @param @param pagePhones @param @param bTime @param @param
	 * eTime @param @return 设定文件 @return List<OrderDTO> 返回类型 @throws
	 */
	List<OrderDTO> listOrderByPhone(Long uid, List<String> pagePhones, Date bTime, Date eTime);

	/**
	 * 通过oid查询oid是否存在-------次方法有坑，请勿调用
	 * 
	 * @author HL
	 * @time 2018年8月24日 下午6:34:55
	 * @param id
	 * @param oids
	 * @return
	 */
	public List<Long> findOrderDTOByIds(Long uid, List<Long> oids);

	/**
	 * listOrderByNick(根据昵称查询购买历史记录) @Title: listOrderByNick @param @param
	 * uid @param @param buyerNick @param @return 设定文件 @return List
	 * <OrderDTO> 返回类型 @throws
	 */
	List<OrderDTO> listOrderByNick(Long uid, String buyerNick);

	/**
	 * listOrderByTids(根据tid的集合查询子订单) @Title: listOrderByTids @param @param
	 * uid @param @param tids @param @return 设定文件 @return List
	 * <OrderDTO> 返回类型 @throws
	 */
	List<OrderDTO> listOrderByTids(Long uid, List<Long> tids);

	public void updateTableIndex(Long uid) throws Exception;

	public List<Long> getHasOrderButNotFoundTradeList(Long uid) throws Exception;

	public List<Long> getHasOrderButNotFoundMemberList(Long uid) throws Exception;

	public void fixColumnLength(Long uid) throws Exception;

	public void updateTradeCreatedTime(Long uid) throws Exception;

	public List<Long> getMultiOrderTidList(Long uid) throws Exception;

	public void updateOrderResult(Long uid, OrderDTO orderDTO);

	public Long getCountByCondition(Long uid, Date endDate, String column) throws Exception;

	public void batchUpdateTradePayTimeHMS(Long uid, List<Long> idList) throws Exception;

	public void batchUpdateTradeEndTimeHMS(Long uid, List<Long> idList) throws Exception;

	public List<Long> getIdList(Long uid, String column, Long startPoint, Long range) throws Exception;

	public void batchUpdateWeek(Long uid, List<Long> idList) throws Exception;

	public List<TempEntity> getAddItemNumFromOrderTable(Long uid, Set<String> buyerNickList) throws Exception;

	public void batchUpdateResult(Long uid, List<Long> idList) throws Exception;

	public List<Long> getOidList(Long uid, String column, Integer startPoint, Integer range) throws Exception;

	public List<OrderDTO> getOrderList(Long uid, Integer startPoint, Integer range) throws Exception;

	public void batchUpdateOrder(Long uid, List<OrderDTO> orderList) throws Exception;

	public void updateOrderStatus(Long uid, List<OrderDTO> list);

	public List<OrderTempEntity> getTradeAmountAndItemNum(Long uid, Set<String> buyerNickSet) throws Exception;

	public List<OrderTempEntity> getAddItemNum(Long uid, Set<String> buyerNickSet) throws Exception;

	public List<OrderTempEntity> getCloseTradeAmountCloseItemNum(Long uid, Set<String> buyerNickSet) throws Exception;

	public List<OrderTempEntity> getLastTradeTimeFirstPayTimeFirstTradeTimeLastPayTime(Long uid,
			Set<String> buyerNickSet) throws Exception;

	public List<OrderTempEntity> getFirstTradeFinishTimeLastTradeFinishTime(Long uid, Set<String> buyerNickSet)
			throws Exception;

	public List<OrderTempEntity> getTbrefundStatusTbOrderStatus(Long uid, Set<String> buyerNickSet) throws Exception;

	public void batchUpdateOrderStatus(Long uid, List<OrderDTO> orderDTOList) throws Exception;

	List<OrderDTO> listRefundStatusByTid(Long uid, Long tid);

	public Long getCount(Long uid) throws Exception;
}
