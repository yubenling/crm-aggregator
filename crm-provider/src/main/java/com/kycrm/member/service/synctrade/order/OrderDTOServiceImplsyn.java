package com.kycrm.member.service.synctrade.order;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.dao.syntrade.order.IOrderDTODaosyn;
import com.kycrm.member.dao.syntrade.order.IOrderLostDaosyn;
import com.kycrm.member.domain.entity.member.TempEntity;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.order.OrderTempEntity;
import com.kycrm.member.domain.vo.order.OrderVO;
import com.kycrm.member.service.trade.TradeErrorMessageService;
import com.kycrm.member.util.RedisDistributionLock;
import com.kycrm.member.util.thread.MyFixedThreadPool;
import com.kycrm.util.Constants;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;

/**
 * @author wy
 * @version 创建时间：2018年1月18日 下午2:39:01
 */
@MyDataSource
@Service
public class OrderDTOServiceImplsyn /*implements IOrderDTOService*/ {

	@Autowired
	private IOrderDTODaosyn orderDao;

	@Autowired
	private IOrderLostDaosyn orderLostDao;

	@Resource(name = "redisTemplateLock")
	private StringRedisTemplate redisTemplate;

	private Logger logger = LoggerFactory.getLogger(OrderDTOServiceImplsyn.class);

	@Autowired
	private TradeErrorMessageService tradeErrorMessageService;

	/**
	 * 根据用户主键id创建对应的子订单表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:53:10
	 * @param uid
	 *            用户主键id
	 */
	public void doCreateTableByNewUser(Long uid) {
		if (ValidateUtil.isEmpty(uid)) {
			return;
		}
		List<String> tables = this.orderDao.isExistsTable(uid);
		if (ValidateUtil.isNotNull(tables)) {
			return;
		}
		try {
			this.orderDao.doCreateTableByNewUser(uid);
			this.orderDao.addOrderTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public void batchInsertOrderList(Long uid, List<OrderDTO> orderList) throws Exception {
		if (ValidateUtil.isEmpty(uid) || ValidateUtil.isEmpty(orderList)) {
			logger.info("batchInsertOrderList方法的uid或者orderList为空");
			return;
		}
		long startTime = System.currentTimeMillis();
		// this.logger.debug("批量order开始更新 uid:" + uid + " 数量：" +
		// orderList.size());
		// int size = orderList.size();
		// if (size > Constants.TRADE_SAVE_MAX_LIMIT) {
		// List<List<OrderDTO>> splitList = JayCommonUtil.splitList(orderList,
		// Constants.TRADE_SAVE_MAX_LIMIT);
		// for (List<OrderDTO> list : splitList) {
		// // this.batchLock(uid, list);
		// this.batchInsetOrderListByLimit(uid, orderList);
		// }
		// } else {
		// this.batchLock(uid, orderList);

		try {
			this.batchInsetOrderListByLimit(uid, orderList);
			// this.saveOrderDTOList(uid, orderList);

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("batchInsertOrderList方法异常 = ", e);
		}

		orderList.clear();
		// }
		this.logger.debug("全部order更新完成  uid:" + uid + " 数量：" + orderList.size() + " 耗时："
				+ (System.currentTimeMillis() - startTime) + "ms");
	}

	/**
	 * 加锁
	 * 
	 * @author: wy
	 * @time: 2018年2月28日 下午2:10:04
	 * @param uid
	 * @param orderList
	 */
	private void batchLock(Long uid, List<OrderDTO> orderList) {
		// RedisLock redisLock = new RedisLock(redisTemplate,
		// RedisConstant.RediskeyCacheGroup.ORDERDTO_TABLE_LOCKE_KEY +
		// uid,Constants.TRADE_SAVE_TIME_OUT,Constants.TRADE_SAVE_EXPIRE_TIME);
		RedisDistributionLock redisDistributionLock = new RedisDistributionLock(redisTemplate);
		long lockTimeOut = 0L;
		try {
			lockTimeOut = redisDistributionLock.lock(RedisConstant.RediskeyCacheGroup.TRADEDTO_TABLE_LOCKE_KEY + uid);
			if (lockTimeOut > 0) {
				this.batchInsetOrderListByLimit(uid, orderList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisDistributionLock.unlock(RedisConstant.RediskeyCacheGroup.TRADEDTO_TABLE_LOCKE_KEY + uid, lockTimeOut);
		}
	}

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
	private void batchInsetOrderListByLimit(Long uid, List<OrderDTO> orderList) throws Exception {
		try {
			long startTime = System.currentTimeMillis();
			List<OrderDTO> newOrderList = new ArrayList<OrderDTO>(Constants.TRADE_SAVE_MAX_LIMIT / 2);
			List<OrderDTO> updateOrderList = new ArrayList<OrderDTO>(Constants.TRADE_SAVE_MAX_LIMIT);
			for (OrderDTO orderDTO : orderList) {
				String status = this.findOrderStatusByOid(uid, orderDTO.getOid());
				if (ValidateUtil.isEmpty(status)) {
					newOrderList.add(orderDTO);
				} else {
					updateOrderList.add(orderDTO);
				}
			}
			long startTime2 = System.currentTimeMillis();
			logger.debug("order分拣新旧订单 uid:{}的  {}个子订单，耗时：{}ms", uid, orderList.size(), startTime2 - startTime);

			this.saveOrderDTOList(uid, newOrderList);
			long startTime3 = System.currentTimeMillis();
			logger.debug("insert order子订单 uid:{} 的  {}个子订单，耗时: {}ms", uid, newOrderList.size(),
					(startTime3 - startTime2));

			this.updateOrderDTOList(uid, updateOrderList);
			logger.debug("update order子订单  uid:" + uid + " 的  " + updateOrderList.size() + "个子订单，耗时：  "
					+ (System.currentTimeMillis() - startTime3) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
		}
	}

	/**
	 * 保存子订单集合
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 下午4:08:54
	 * @param uid
	 * @param orderList
	 * @throws InterruptedException
	 */
	private void saveOrderDTOList(Long uid, List<OrderDTO> orderList) {
		if (ValidateUtil.isEmpty(orderList)) {
			return;
		}
		long startTime = System.currentTimeMillis();
		try {
			this.saveOrderDTOByList(uid, orderList);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("批量保存order子订单异常！！！改为轮询单个保存" + e.getMessage());
			long startTime2 = System.currentTimeMillis();
			for (OrderDTO orderDTO : orderList) {
				this.saveOrderDTOBySingle(uid, orderDTO);
			}
			this.logger.error("保存异常，轮询单个order子订单保存花费的时间为 ： " + (System.currentTimeMillis() - startTime2) + "ms");
		}
		this.logger.debug("批量保存order子订单花费时间为：  " + (System.currentTimeMillis() - startTime) + "ms");
	}

	/**
	 * 更新子子订单集合
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 下午4:11:28
	 * @param uid
	 * @param orderDTOList
	 */
	private void updateOrderDTOList(Long uid, List<OrderDTO> orderDTOList) {
		if (ValidateUtil.isEmpty(orderDTOList)) {
			logger.info("updateOrderDTOList方法的orderDTOList为空");
			return;
		}
		long startTime = System.currentTimeMillis();
		try {
			this.doUpdateOrderDTOByList(uid, orderDTOList);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("批量更新order子订单异常！！！改为轮询单个更新" + e.getMessage());
			long startTime2 = System.currentTimeMillis();
			for (OrderDTO orderDTO : orderDTOList) {
				this.doUpdateOrderDTOBySingle(uid, orderDTO);
			}
			this.logger.error("order更新异常，轮询单个子订单更新花费的时间为 ： " + (System.currentTimeMillis() - startTime2) + "ms");
		} finally {
			this.logger.debug("批量更新order子订单花费时间为：  " + (System.currentTimeMillis() - startTime) + "ms");
		}
	}

	
	public String findOrderStatusByOid(Long uid, Long oid) {
		if (ValidateUtil.isEmpty(uid) || ValidateUtil.isEmpty(oid)) {
			return null;
		}
		Map<String, Long> map = new HashMap<String, Long>(5);
		map.put("uid", uid);
		map.put("oid", oid);
		return this.orderDao.findStatusByOid(map);
	}

	private void saveOrderDTOByList(Long uid, List<OrderDTO> orderDTOList) throws Exception {

		if (ValidateUtil.isEmpty(orderDTOList) || ValidateUtil.isEmpty(uid)) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("list", orderDTOList);
		this.orderDao.doCreateOrderDTOByList(map);
	}

	/**
	 * 保存单个子订单
	 * 
	 * @author: wy
	 * @time: 2018年1月25日 下午3:51:10
	 * @param uid
	 * @param orderDTO
	 */
	private void saveOrderDTOBySingle(Long uid, OrderDTO orderDTO) {
		if (ValidateUtil.isEmpty(uid) || orderDTO == null) {
			return;
		}
		try {
			String status = this.findOrderStatusByOid(uid, orderDTO.getOid());
			if (ValidateUtil.isEmpty(status)) {
				orderDTO.setUid(uid);
				this.orderDao.doCreateOrderDTOByBySingle(orderDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MyFixedThreadPool.getTradeAndOrderFixedThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					tradeErrorMessageService.saveErrorMessage(e.getMessage(), TradeErrorMessageService.IS_ORDER,
							TradeErrorMessageService.SAVE, JSON.toJSONString(orderDTO));
				}
			});
		}
	}

	/**
	 * 批量更新子订单
	 * 
	 * @author: wy
	 * @time: 2018年1月25日 下午3:53:02
	 * @param uid
	 * @param orderDTOList
	 */
	private void doUpdateOrderDTOByList(Long uid, List<OrderDTO> orderDTOList) throws Exception {
		if (ValidateUtil.isEmpty(orderDTOList) || ValidateUtil.isEmpty(uid)) {
			logger.info("doUpdateOrderDTOByList方法的orderDTOList或者uid为空");
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("list", orderDTOList);
		this.orderDao.doUpdateOrderDTOByList(map);
	}

	/**
	 * 更新单个子订单
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 下午4:11:19
	 * @param uid
	 * @param orderDTO
	 */
	private void doUpdateOrderDTOBySingle(Long uid, OrderDTO orderDTO) {
		try {
			if (ValidateUtil.isEmpty(uid) || orderDTO == null) {
				return;
			}
			orderDTO.setUid(uid);
			this.orderDao.doUpdateOrderDTOBySingle(orderDTO);
		} catch (Exception e) {
			e.printStackTrace();
			MyFixedThreadPool.getTradeAndOrderFixedThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					tradeErrorMessageService.saveErrorMessage(e.getMessage(), TradeErrorMessageService.IS_ORDER,
							TradeErrorMessageService.UPDATE, JSON.toJSONString(orderDTO));
				}
			});
		}
	}

	
	public Integer findOneOrderDTOByTid(Long uid, String newTid) {
		return null;
	}

	
	public Map<String, Object> findOrderDTOPage(Long uid, OrderVO odVo, String accessToken) {
		return null;
	}

	
	public List<OrderDTO> listOrderByTid(Long uid, Long tid) {
		return null;
	}

	
	public List<OrderDTO> listOrderByPhone(Long uid, List<String> pagePhones, Date bTime, Date eTime) {
		return null;
	}

	
	public List<OrderDTO> listOrderByNick(Long uid, String buyerNick) {
		return null;
	}


	public List<Long> findOrderDTOByIds(Long uid, List<Long> oids) {
		return null;
	}


	public List<OrderDTO> listOrderByTids(Long uid, List<Long> tids) {
		return null;
	}

	
	public void updateTableIndex(Long uid) throws Exception {
		if (ValidateUtil.isEmpty(uid)) {
			return;
		}
		List<String> tables = this.orderDao.isExistsTable(uid);
		if (ValidateUtil.isNotNull(tables)) {
			return;
		}
		this.orderDao.updateTableIndex(uid);
	}

	
	public List<Long> getHasOrderButNotFoundTradeList(Long uid) throws Exception {
		return this.orderLostDao.getHasOrderButNotFoundTradeList(uid);
	}

	
	public List<Long> getHasOrderButNotFoundMemberList(Long uid) throws Exception {
		return this.orderLostDao.getHasOrderButNotFoundMemberList(uid);
	}


	public void fixColumnLength(Long uid) throws Exception {
		this.orderDao.fixColumnLength1(uid);
	}

	
	public void updateTradeCreatedTime(Long uid) throws Exception {
		this.orderDao.updateTradeCreatedTime(uid);
	}

	
	public List<Long> getMultiOrderTidList(Long uid) throws Exception {
		return this.orderDao.getMultiOrderTidList(uid);
	}

	
	public void updateOrderResult(Long uid, OrderDTO orderDTO) {
		if (uid == null || orderDTO.getOid() == null || orderDTO.getResult() == null) {
			logger.info(
					"更新子订单评价结果参数为空  uid-->" + uid + "子订单号--->" + orderDTO.getOid() + "订单评价结果为" + orderDTO.getResult());
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("result", orderDTO.getResult());
		map.put("oid", orderDTO.getOid());
		orderDao.updateOrderResult(map);
	}

	
	public Long getCountByCondition(Long uid, Date endDate, String column) throws Exception {
		return this.orderDao.getCountByCondition(uid, endDate, column);
	}

	
	public void batchUpdateTradePayTimeHMS(Long uid, List<Long> idList) throws Exception {
		this.orderDao.batchUpdateTradePayTimeHMS(uid, idList);
	}

	public void batchUpdateTradeEndTimeHMS(Long uid, List<Long> idList) throws Exception {
		this.orderDao.batchUpdateTradeEndTimeHMS(uid, idList);
	}


	public List<Long> getIdList(Long uid, String column, Long startPoint, Long range) throws Exception {
		return this.orderDao.getIdList(uid, column, startPoint, range);
	}

	
	public void batchUpdateWeek(Long uid, List<Long> idList) throws Exception {
		this.orderDao.batchUpdateWeek(uid, idList);
	}

	
	public List<TempEntity> getAddItemNumFromOrderTable(Long uid, Set<String> buyerNickList) throws Exception {
		return this.orderDao.getAddItemNumFromOrderTable(uid, buyerNickList);
	}

	
	public void batchUpdateResult(Long uid, List<Long> idList) throws Exception {
		this.orderDao.batchUpdateResult(uid, idList);
	}

	
	public List<Long> getOidList(Long uid, String column, Integer startPoint, Integer range) throws Exception {
		return this.orderDao.getOidList(uid, column, startPoint, range);
	}

	
	public List<OrderDTO> getOrderList(Long uid, Integer startPoint, Integer range) throws Exception {
		return this.orderDao.getOrderList(uid, startPoint, range);
	}

	
	public void batchUpdateOrder(Long uid, List<OrderDTO> orderList) throws Exception {
		this.orderDao.batchUpdateOrder(uid, orderList);
	}

	
	public void updateOrderStatus(Long uid, List<OrderDTO> list) {

	}

	
	public List<OrderTempEntity> getTradeAmountAndItemNum(Long uid, Set<String> buyerNickSet) throws Exception {
		return this.orderDao.getTradeAmountAndItemNum(uid, buyerNickSet);
	}

	public List<OrderTempEntity> getAddItemNum(Long uid, Set<String> buyerNickSet) throws Exception {
		return this.orderDao.getAddItemNum(uid, buyerNickSet);
	}

	
	public List<OrderTempEntity> getCloseTradeAmountCloseItemNum(Long uid, Set<String> buyerNickSet) throws Exception {
		return this.orderDao.getCloseTradeAmountCloseItemNum(uid, buyerNickSet);
	}

	
	public List<OrderTempEntity> getLastTradeTimeFirstPayTimeFirstTradeTimeLastPayTime(Long uid,
			Set<String> buyerNickSet) throws Exception {
		return this.orderDao.getLastTradeTimeFirstPayTimeFirstTradeTimeLastPayTime(uid, buyerNickSet);
	}

	
	public List<OrderTempEntity> getFirstTradeFinishTimeLastTradeFinishTime(Long uid, Set<String> buyerNickSet)
			throws Exception {
		return this.orderDao.getFirstTradeFinishTimeLastTradeFinishTime(uid, buyerNickSet);
	}

	
	public List<OrderTempEntity> getTbrefundStatusTbOrderStatus(Long uid, Set<String> buyerNickSet) throws Exception {
		return this.orderDao.getTbrefundStatusTbOrderStatus(uid, buyerNickSet);
	}

	
	public void batchUpdateOrderStatus(Long uid, List<OrderDTO> orderDTOList) throws Exception {
		this.orderDao.batchUpdateOrderStatus(uid, orderDTOList);
	}

	
	public List<OrderDTO> listRefundStatusByTid(Long uid, Long tid) {
		// TODO Auto-generated method stub
		return null;
	}

}
