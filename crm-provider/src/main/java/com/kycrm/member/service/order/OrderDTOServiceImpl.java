package com.kycrm.member.service.order;

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
import com.kycrm.member.core.redis.RedisLockService;
import com.kycrm.member.dao.order.IOrderDTODao;
import com.kycrm.member.dao.trade.ITradeDTODao;
import com.kycrm.member.domain.entity.member.TempEntity;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.order.OrderTempEntity;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.vo.order.OrderVO;
import com.kycrm.member.service.trade.TradeErrorMessageService;
import com.kycrm.member.util.RedisLock;
import com.kycrm.util.Constants;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.JayCommonUtil;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;
import com.kycrm.util.thread.MyFixedThreadPool;
import com.taobao.api.SecretException;

/**
 * @author wy
 * @version 创建时间：2018年1月18日 下午2:39:01
 */
@MyDataSource
@Service("orderDTOService")
public class OrderDTOServiceImpl implements IOrderDTOService {

	@Autowired
	private IOrderDTODao orderDao;

	@Resource(name = "redisTemplateLock")
	private StringRedisTemplate redisTemplate;

	private Logger logger = LoggerFactory.getLogger(OrderDTOServiceImpl.class);

	@Autowired
	private TradeErrorMessageService tradeErrorMessageService;

	@Autowired
	private RedisLockService redisLockService;

	@Autowired
	private ITradeDTODao tradeDTODao;

	/**
	 * 根据用户主键id创建对应的子订单表
	 * 
	 * @author: wy
	 * @time: 2018年1月18日 上午11:53:10
	 * @param uid
	 *            用户主键id
	 */
	@Override
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

	@Override
	public void batchInsertOrderList(Long uid, List<OrderDTO> orderList) {
		if (ValidateUtil.isEmpty(uid) || ValidateUtil.isEmpty(orderList)) {
			return;
		}
		long startTime = System.currentTimeMillis();
		this.logger.debug("批量子订单开始更新 uid:" + uid + " 数量：" + orderList.size());
		int size = orderList.size();
		if (size > Constants.TRADE_SAVE_MAX_LIMIT) {
			List<List<OrderDTO>> splitList = JayCommonUtil.splitList(orderList, Constants.TRADE_SAVE_MAX_LIMIT);
			for (List<OrderDTO> list : splitList) {
				this.batchLock(uid, list);
			}
		} else {
			this.batchLock(uid, orderList);
		}
		this.logger.debug(
				"全部更新完成  uid:" + uid + " 数量：" + orderList.size() + " 花费时间：" + (System.currentTimeMillis() - startTime));
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
		RedisLock redisLock = new RedisLock(redisTemplate,
				RedisConstant.RediskeyCacheGroup.ORDERDTO_TABLE_LOCKE_KEY + uid, Constants.TRADE_SAVE_TIME_OUT,
				Constants.TRADE_SAVE_EXPIRE_TIME);
		try {
			if (redisLock.lock()) {
				this.batchInsetOrderListByLimit(uid, orderList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisLock.unlock();
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
	private void batchInsetOrderListByLimit(Long uid, List<OrderDTO> orderList) {
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
			this.logger.debug(
					"区分保存还是更新 uid:" + uid + " 的  " + orderList.size() + "个子订单，花费了  " + (startTime2 - startTime) + "ms");
			this.saveOrderDTOList(uid, newOrderList);
			long startTime3 = System.currentTimeMillis();
			this.logger.debug("保存子订单 uid:" + uid + " 的  " + newOrderList.size() + "个子订单，花费了  "
					+ (startTime3 - startTime2) + "ms");
			this.updateOrderDTOList(uid, updateOrderList);
			this.logger.debug("更新子订单 uid:" + uid + " 的  " + updateOrderList.size() + "个子订单，花费了  "
					+ (System.currentTimeMillis() - startTime3) + "ms");
		} catch (Exception e) {
			e.printStackTrace();
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
			this.logger.error("批量保存子订单异常！！！改为轮询单个保存" + e.getMessage());
			long startTime2 = System.currentTimeMillis();
			for (OrderDTO orderDTO : orderList) {
				this.saveOrderDTOBySingle(uid, orderDTO);
			}
			this.logger.error("保存异常，轮询单个子订单保存花费的时间为 ： " + (System.currentTimeMillis() - startTime2) + "ms");
		}
		this.logger.debug("批量保存子订单花费时间为：  " + (System.currentTimeMillis() - startTime) + "ms");
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
			return;
		}
		long startTime = System.currentTimeMillis();
		try {
			this.doUpdateOrderDTOByList(uid, orderDTOList);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("批量更新子订单异常！！！改为轮询单个更新" + e.getMessage());
			long startTime2 = System.currentTimeMillis();
			for (OrderDTO orderDTO : orderDTOList) {
				this.doUpdateOrderDTOBySingle(uid, orderDTO);
			}
			this.logger.error("更新异常，轮询单个子订单更新花费的时间为 ： " + (System.currentTimeMillis() - startTime2) + "ms");
		} finally {
			this.logger.debug("批量更新子订单花费时间为：  " + (System.currentTimeMillis() - startTime) + "ms");
		}
	}

	@Override
	public String findOrderStatusByOid(Long uid, Long oid) {
		if (ValidateUtil.isEmpty(uid) || ValidateUtil.isEmpty(oid)) {
			return null;
		}
		Map<String, Long> map = new HashMap<String, Long>(5);
		map.put("uid", uid);
		map.put("oid", oid);
		return this.orderDao.findStatusByOid(map);
	}

	private void saveOrderDTOByList(Long uid, List<OrderDTO> orderDTOList) {
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
			MyFixedThreadPool.getMyFixedThreadPool().execute(new Runnable() {
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
	private void doUpdateOrderDTOByList(Long uid, List<OrderDTO> orderDTOList) {
		if (ValidateUtil.isEmpty(orderDTOList) || ValidateUtil.isEmpty(uid)) {
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
			MyFixedThreadPool.getMyFixedThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					tradeErrorMessageService.saveErrorMessage(e.getMessage(), TradeErrorMessageService.IS_ORDER,
							TradeErrorMessageService.UPDATE, JSON.toJSONString(orderDTO));
				}
			});
		}
	}

	@Override
	public Map<String, Object> findOrderDTOPage(Long uid, OrderVO vo, String accessToken) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (null != vo.getBuyerNick() && !"".equals(vo.getBuyerNick())) {
			try {
				String encryptName = EncrptAndDecryptClient.getInstance().encryptData(vo.getBuyerNick(),
						EncrptAndDecryptClient.SEARCH, accessToken);
				vo.setBuyerNick(encryptName);
			} catch (Exception e) {
				logger.error("*********订单查询**********加密出错，**********************" + e.getMessage());
			}
		}

		if (null != vo.getReceiverMobile() && !"".equals(vo.getReceiverMobile())) {
			try {
				String encryptPhone = EncrptAndDecryptClient.getInstance().encryptData(vo.getReceiverMobile(),
						EncrptAndDecryptClient.PHONE, accessToken);
				vo.setReceiverMobile(encryptPhone);
			} catch (Exception e) {
				logger.error("*********订单查询**********加密出错，**********************" + e.getMessage());
			}
		}

		Long totalCount = this.tradeDTODao.countManageTradeDTO(vo);
		if (null == totalCount)
			totalCount = 0L;
		Integer totalPage = (int) Math.ceil(1.0 * totalCount / vo.getCurrentRows());
		List<TradeDTO> list = this.tradeDTODao.listManageTradeDTO(vo);
		for (TradeDTO tradeDTO : list) {
			if (null != tradeDTO.getReceiverMobile() && !"".equals(tradeDTO.getReceiverMobile())) {
				try {
					if (EncrptAndDecryptClient.isEncryptData(tradeDTO.getReceiverMobile(),
							EncrptAndDecryptClient.PHONE)) {
						String decrypt = EncrptAndDecryptClient.getInstance().decrypt(tradeDTO.getReceiverMobile(),
								EncrptAndDecryptClient.PHONE, accessToken);
						tradeDTO.setReceiverMobile(decrypt.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
					} else {
						tradeDTO.setReceiverMobile(
								tradeDTO.getReceiverMobile().replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2"));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (null != tradeDTO.getBuyerNick() && !"".equals(tradeDTO.getBuyerNick())) {
				try {
					if (EncrptAndDecryptClient.isEncryptData(tradeDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
						String decrypt = EncrptAndDecryptClient.getInstance().decrypt(tradeDTO.getBuyerNick(),
								EncrptAndDecryptClient.SEARCH, accessToken);
						tradeDTO.setBuyerNick(decrypt);
					}
				} catch (SecretException e) {
					e.printStackTrace();
				}
			}

			if (null != tradeDTO.getReceiverName() && !"".equals(tradeDTO.getReceiverName())) {
				try {
					if (EncrptAndDecryptClient.isEncryptData(tradeDTO.getReceiverName(),
							EncrptAndDecryptClient.SEARCH)) {
						String decrypt = EncrptAndDecryptClient.getInstance().decrypt(tradeDTO.getReceiverName(),
								EncrptAndDecryptClient.SEARCH, accessToken);
						tradeDTO.setReceiverName(decrypt);
					}
				} catch (SecretException e) {
					e.printStackTrace();
				}
			}
		}
		map.put("totalPage", totalPage);
		map.put("list", list);
		return map;
	}

	/**
	 * 通过oid 查询订单是否存在
	 * 
	 * @author HL
	 * @time 2018年6月1日 下午3:05:24
	 * @param uid
	 * @param newTid
	 * @return
	 */
	@Override
	public Integer findOneOrderDTOByTid(Long uid, String tid) {
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("uid", uid);
		map.put("tid", tid);
		return orderDao.findOneOrderDTOByTid(map);
	}

	/**
	 * tid查询子订单
	 */
	@Override
	public List<OrderDTO> listOrderByTid(Long uid, Long tid) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("tid", tid);
		List<OrderDTO> orderDTOs = orderDao.listOrderByTid(map);
		return orderDTOs;
	}

	/**
	 * tid集合查询子订单
	 */
	@Override
	public List<OrderDTO> listOrderByTids(Long uid, List<Long> tids) {
		if (uid == null || tids == null || tids.isEmpty()) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("tids", tids);
		List<OrderDTO> orderDTOs = this.orderDao.listOrderByTids(map);
		return orderDTOs;
	}

	@Override
	public List<OrderDTO> listOrderByPhone(Long uid, List<String> pagePhones, Date bTime, Date eTime) {
		if (uid == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("phoneList", pagePhones);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		List<OrderDTO> orderDTOs = orderDao.listOrderByPhone(map);
		return orderDTOs;
	}

	@Override
	public List<Long> findOrderDTOByIds(Long uid, List<Long> oids) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("oids", oids);
		return orderDao.findOrderDTOByIds(map);
	}

	@Override
	public List<OrderDTO> listOrderByNick(Long uid, String buyerNick) {
		List<OrderDTO> tradeList = this.orderDao.listTradeByNick(uid, buyerNick);
		return tradeList;
	}

	@Override
	public void updateTableIndex(Long uid) {
	}

	@Override
	public List<Long> getHasOrderButNotFoundTradeList(Long uid) throws Exception {
		return null;
	}

	@Override
	public List<Long> getHasOrderButNotFoundMemberList(Long uid) throws Exception {
		return null;
	}

	@Override
	public void fixColumnLength(Long uid) throws Exception {
	}

	@Override
	public void updateTradeCreatedTime(Long uid) throws Exception {
	}

	@Override
	public List<Long> getMultiOrderTidList(Long uid) throws Exception {
		return null;
	}

	@Override
	public void updateOrderResult(Long uid, OrderDTO orderDTO) {
		// TODO Auto-generated method stub

	}

	@Override
	public Long getCountByCondition(Long uid, Date endDate, String column) throws Exception {
		return null;
	}

	@Override
	public void batchUpdateTradePayTimeHMS(Long uid, List<Long> idList) throws Exception {
	}

	@Override
	public void batchUpdateTradeEndTimeHMS(Long uid, List<Long> idList) throws Exception {
	}

	@Override
	public List<Long> getIdList(Long uid, String column, Long startPoint, Long range) throws Exception {
		return null;
	}

	@Override
	public void batchUpdateWeek(Long uid, List<Long> idList) throws Exception {
	}

	@Override
	public List<TempEntity> getAddItemNumFromOrderTable(Long uid, Set<String> buyerNickList) throws Exception {
		return null;
	}

	@Override
	public void batchUpdateResult(Long uid, List<Long> idList) throws Exception {
	}

	@Override
	public List<Long> getOidList(Long uid, String column, Integer startPoint, Integer range) throws Exception {
		return null;
	}

	@Override
	public List<OrderDTO> getOrderList(Long uid, Integer startPoint, Integer range) throws Exception {
		return null;
	}

	@Override
	public void batchUpdateOrder(Long uid, List<OrderDTO> orderList) throws Exception {
	}
	@Override
	public void updateOrderStatus(Long uid, List<OrderDTO> list) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("list", list);
		orderDao.updateOrderStatus(map);	
	}
	
	@Override
	public List<OrderTempEntity> getAddItemNum(Long uid, Set<String> buyerNickSet) throws Exception {
		return null;
	}

	@Override
	public List<OrderTempEntity> getTradeAmountAndItemNum(Long uid, Set<String> buyerNickSet) throws Exception {
		return null;
	}

	@Override
	public List<OrderTempEntity> getCloseTradeAmountCloseItemNum(Long uid, Set<String> buyerNickSet) throws Exception {
		return null;
	}

	@Override
	public List<OrderTempEntity> getLastTradeTimeFirstPayTimeFirstTradeTimeLastPayTime(Long uid,
			Set<String> buyerNickSet) throws Exception {
		return null;
	}

	@Override
	public List<OrderTempEntity> getFirstTradeFinishTimeLastTradeFinishTime(Long uid, Set<String> buyerNickSet)
			throws Exception {
		return null;
	}

	@Override
	public List<OrderTempEntity> getTbrefundStatusTbOrderStatus(Long uid, Set<String> buyerNickSet) throws Exception {
		return null;
	}

	@Override
	public void batchUpdateOrderStatus(Long uid, List<OrderDTO> orderDTOList) throws Exception {
	}
	
	@Override
	public List<OrderDTO> listRefundStatusByTid(Long uid, Long tid){
		if(uid == null || tid == null){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("tid", tid);
		List<OrderDTO> refundOrderList = orderDao.listRefundOrderByTid(map);
		return refundOrderList;
	}

	@Override
	public Long getCount(Long id) throws Exception {
		return null;
	}
}
