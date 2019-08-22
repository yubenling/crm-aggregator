package com.kycrm.member.service.trade;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
import com.kycrm.member.domain.entity.effect.EffectStandardRFM;
import com.kycrm.member.domain.entity.effect.RFMDetailChart;
import com.kycrm.member.domain.entity.effect.TradeCenterEffect;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.member.TempEntity;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.entity.trade.TradeTempEntity;
import com.kycrm.member.domain.to.SimplePage;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.domain.vo.trade.TradeMessageVO;
import com.kycrm.member.domain.vo.trade.TradeResultVO;
import com.kycrm.member.domain.vo.trade.TradeVO;
import com.kycrm.member.service.item.IItemService;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.message.ITradeMsgSendService;
import com.kycrm.member.service.trade.thread.TradeSendMessageThread;
import com.kycrm.member.util.RedisLock;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.JayCommonUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.NumberUtils;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;
import com.kycrm.util.thread.MyFixedThreadPool;
import com.taobao.api.SecretException;

/**
 * @author wy
 * @version 创建时间：2018年1月18日 下午2:36:49
 */
@MyDataSource
@Service("tradeDTOService")
public class TradeDTOServiceImpl implements ITradeDTOService {

	@Autowired
	private ITradeDTODao tradeDao;

	@Autowired
	private IOrderDTODao orderDao;

	@Resource(name = "redisTemplateLock")
	private StringRedisTemplate redisTemplate;

	@Autowired
	private TradeErrorMessageService tradeErrorMessageService;

	@Autowired
	private RedisLockService redisLockService;

	@Autowired
	private ITradeMsgSendService tradeMsgSendService;

	@Autowired
	private ISmsBlackListDTOService blackListDTOService;

	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;

	@Autowired
	private IMemberDTOService memberDTOserviceImpl;

	@Autowired
	private IItemService itemService;

	private Logger logger = LoggerFactory.getLogger(TradeDTOServiceImpl.class);

	@Override
	public void doSendSms(Long uid, TradeMessageVO messageVO) {
		TradeSendMessageThread tradeSendMessageThread = new TradeSendMessageThread(tradeMsgSendService, messageVO,
				null);
		new Thread(tradeSendMessageThread).start();
	}

	/**
	 * 根据用户主键id创建对应的主订单表
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
		String userId = String.valueOf(uid);
		List<String> tables = this.tradeDao.isExistsTable(userId);
		if (ValidateUtil.isNotNull(tables)) {
			logger.info("已存在 uid：{} 的trade_dto表格", userId);
			return;
		}
		try {
			this.tradeDao.doCreateTableByNewUser(userId);
			this.tradeDao.addTradeTableIndex(userId);
			logger.info("不存在 uid：{} 的trade_dto表格，并创建", userId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String findTradeStatusByTid(Long uid, Long tid) {
		Map<String, Long> map = new HashMap<String, Long>(5);
		map.put("uid", uid);
		map.put("tid", tid);
		return this.tradeDao.findStatusByTid(map);
	}

	/**
	 * 批量保存订单
	 * 
	 * @author: wy
	 * @time: 2018年1月22日 下午4:31:37
	 * @param uid
	 *            用户主键id
	 * @param tradeList
	 *            要保持的订单集合
	 */
	@Override
	public void batchInsertTradeList(Long uid, List<TradeDTO> tradeList) {
		if (ValidateUtil.isEmpty(uid) || ValidateUtil.isEmpty(tradeList)) {
			return;
		}
		long startTime = System.currentTimeMillis();
		this.logger.debug("批量订单开始更新 uid:" + uid + " 数量：" + tradeList.size());
		int size = tradeList.size();
		if (size > Constants.TRADE_SAVE_MAX_LIMIT) {
			List<List<TradeDTO>> splitList = JayCommonUtil.splitList(tradeList, Constants.TRADE_SAVE_MAX_LIMIT);
			for (List<TradeDTO> list : splitList) {
				this.batchLock(uid, list);
			}
		} else {
			this.batchLock(uid, tradeList);
		}
		this.logger.debug(
				"全部更新完成  uid:" + uid + " 数量：" + tradeList.size() + " 花费时间：" + (System.currentTimeMillis() - startTime));
	}

	/**
	 * 加锁
	 * 
	 * @author: wy
	 * @time: 2018年2月28日 下午2:06:29
	 * @param uid
	 * @param tradeList
	 */
	private void batchLock(Long uid, List<TradeDTO> tradeList) {
		RedisLock redisLock = new RedisLock(redisTemplate,
				RedisConstant.RediskeyCacheGroup.TRADEDTO_TABLE_LOCKE_KEY + uid, Constants.TRADE_SAVE_TIME_OUT,
				Constants.TRADE_SAVE_EXPIRE_TIME);
		try {
			if (redisLock.lock()) {
				this.batchInsetTradeListByLimit(uid, tradeList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisLock.unlock();
		}
	}

	/**
	 * 批量保存订单
	 * 
	 * @author: wy
	 * @time: 2018年1月22日 下午4:31:51
	 * @param uid
	 *            用户主键id
	 * @param tradeList
	 *            要保持的订单集合
	 */
	private void batchInsetTradeListByLimit(Long uid, List<TradeDTO> tradeList) {
		try {

			long startTime = System.currentTimeMillis();
			// List<TradeDTO> newTradeList = new
			// ArrayList<TradeDTO>(Constants.TRADE_SAVE_MAX_LIMIT/2);
			// List<TradeDTO> updateTradeList = new
			// ArrayList<TradeDTO>(Constants.TRADE_SAVE_MAX_LIMIT);
			// for (TradeDTO tradeDTO : tradeList) {
			// String status = this.findTradeStatusByTid(uid,
			// tradeDTO.getTid());
			// if (ValidateUtil.isEmpty(status)) {
			// newTradeList.add(tradeDTO);
			// } else {
			// updateTradeList.add(tradeDTO);
			// }
			// }
			long startTime2 = System.currentTimeMillis();
			this.logger.info(
					"区分保存还是更新  :" + uid + " 的  " + tradeList.size() + "个订单，花费了  " + (startTime2 - startTime) + "ms");
			saveBatchTradeDTOByList(uid, tradeList);
			long startTime3 = System.currentTimeMillis();
			this.logger.info(
					"保存订单     :" + uid + " 的  " + tradeList.size() + "个订单，花费了  " + (startTime3 - startTime2) + "ms");
			// this.updateBatchTradeDTOByList(uid, updateTradeList);
			// this.logger.info("更新订单 :"+uid+" 的
			// "+updateTradeList.size()+"个订单，花费了 " +
			// (System.currentTimeMillis()-startTime3)+"ms");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 批量保存订单，如果批处理失败，则改为轮询 单个订单保存
	 * 
	 * @author: wy
	 * @time: 2018年1月22日 下午4:51:15
	 */
	private void saveBatchTradeDTOByList(long uid, List<TradeDTO> tradeList) throws InterruptedException {
		if (ValidateUtil.isEmpty(tradeList)) {
			return;
		}
		long startTime = System.currentTimeMillis();
		try {
			this.saveTradeDTOList(uid, tradeList);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("批量保存订单异常！！！改为轮询单个保存" + e.getMessage());
			long startTime2 = System.currentTimeMillis();
			for (TradeDTO tradeDTO : tradeList) {
				this.saveTradeDTOBySingle(uid, tradeDTO);
			}
			this.logger.error("保存异常，轮询单个订单保存花费的时间为 ： " + (System.currentTimeMillis() - startTime2) + "ms");
		}
		this.logger.debug("保存订单  :" + uid + " 的  " + tradeList.size() + "个订单，花费了  "
				+ (System.currentTimeMillis() - startTime) + "ms");
	}

	/**
	 * 批量更新订单信息
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 下午12:16:28
	 * @param uid
	 * @param tradeList
	 */
	private void updateBatchTradeDTOByList(long uid, List<TradeDTO> tradeList) {
		if (ValidateUtil.isEmpty(tradeList)) {
			return;
		}
		long startTime = System.currentTimeMillis();
		try {
			this.doUpdateTradeDTOByList(uid, tradeList);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("批量更新订单异常！！！改为轮询单个更新" + e.getMessage());
			long startTime2 = System.currentTimeMillis();
			for (TradeDTO tradeDTO : tradeList) {
				this.doUpdateTradeDTOBySingle(uid, tradeDTO);
			}
			this.logger.error("更新异常，轮询单个订单更新花费的时间为 ： " + (System.currentTimeMillis() - startTime2) + "ms");
		} finally {
			this.logger.debug("批量更新订单花费时间为：  " + (System.currentTimeMillis() - startTime) + "ms");
		}
	}

	/**
	 * 批量保存订单
	 * 
	 * @author: wy
	 * @time: 2018年1月22日 下午4:45:33
	 * @param uid
	 * @param tradeDTOList
	 */
	private void saveTradeDTOList(Long uid, List<TradeDTO> tradeDTOList) {
		if (ValidateUtil.isEmpty(tradeDTOList)) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("list", tradeDTOList);
		this.tradeDao.doCreateTradeDTOByList(map);
	}

	/**
	 * 保存单个订单内容
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 上午11:26:24
	 * @param uid
	 *            用户表主键id
	 * @param tradeDTO
	 */
	private void saveTradeDTOBySingle(Long uid, TradeDTO tradeDTO) {
		if (tradeDTO == null) {
			return;
		}
		try {
			String status = this.findTradeStatusByTid(uid, tradeDTO.getTid());
			if (ValidateUtil.isEmpty(status)) {
				tradeDTO.setUid(uid);
				this.tradeDao.doCreateTradeDTOByBySingle(tradeDTO);
			}
		} catch (Exception e) {
			e.printStackTrace();
			MyFixedThreadPool.getMyFixedThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					tradeErrorMessageService.saveErrorMessage(e.getMessage(), TradeErrorMessageService.IS_TRADE,
							TradeErrorMessageService.SAVE, JSON.toJSONString(tradeDTO));
				}
			});
		}
	}

	/**
	 * 批量更新订单
	 * 
	 * @author: wy
	 * @time: 2018年1月22日 下午4:45:33
	 * @param uid
	 * @param tradeDTOList
	 */
	private void doUpdateTradeDTOByList(Long uid, List<TradeDTO> tradeDTOList) {
		if (ValidateUtil.isEmpty(tradeDTOList)) {
			return;
		}
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("list", tradeDTOList);
		this.tradeDao.doUpdateTradeDTOByList(map);
	}

	/**
	 * 更新单个订单内容
	 * 
	 * @author: wy
	 * @time: 2018年1月23日 上午11:26:24
	 * @param uid
	 *            用户表主键id
	 * @param tradeDTO
	 */
	private void doUpdateTradeDTOBySingle(Long uid, TradeDTO tradeDTO) {
		try {
			if (tradeDTO == null) {
				return;
			}
			tradeDTO.setUid(uid);
			this.tradeDao.doUpdateTradeDTOBySingle(tradeDTO);
		} catch (Exception e) {
			e.printStackTrace();
			MyFixedThreadPool.getMyFixedThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					tradeErrorMessageService.saveErrorMessage(e.getMessage(), TradeErrorMessageService.IS_TRADE,
							TradeErrorMessageService.UPDATE, JSON.toJSONString(tradeDTO));
				}
			});
		}
	}

	/**
	 * 订单短信群发筛选全部订单
	 * 
	 * @throws Exception
	 */
	@Override
	public List<TradeResultVO> listMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey, String queryKey)
			throws Exception {
		if (sessionKey == null || "".equals(sessionKey) || uid == null) {
			return null;
		}
		logger.info("入参maxId:" + tradeVO.getStartRows());
		tradeVO.setUid(uid);
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		if (tradeVO.getBuyerNick() != null && !"".equals(tradeVO.getBuyerNick())) {
			try {
				if (!EncrptAndDecryptClient.isEncryptData(tradeVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
					tradeVO.setBuyerNick(
							decryptClient.encrypt(tradeVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH, sessionKey));
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
		}
		Long l1 = System.currentTimeMillis();
		if (tradeVO.getItemTitle() != null && !"".equals(tradeVO.getItemTitle())) {
			List<Long> numIidList = this.itemService.fuzzilyFindNumIidByGoodsKeyCode(uid, tradeVO.getItemTitle());
			tradeVO.setNumIidList(numIidList);
			tradeVO.setIsTitle(null);
			tradeVO.setItemTitle(null);
		}
		List<OrderDTO> orderDTOs = orderDao.listMarketingCenterOrder(tradeVO);
		Long l2 = System.currentTimeMillis();
		logger.info("此次从数据库查询订单时间：" + (l2 - l1) + "ms");
		/*
		 * if((tradeVO.getNumIidStr() != null &&
		 * !"".equals(tradeVO.getNumIidStr())) || (tradeVO.getRefundStatus() !=
		 * null && !"".equals(tradeVO.getRefundStatus()))){ orderDTOs =
		 * orderDao.listOrderDTOs(tradeVO); tradeNum =
		 * orderDao.countOrderDTO(tradeVO); }else { orderDTOs =
		 * orderDao.listMarketingCenterOrder(tradeVO); tradeNum =
		 * orderDao.countMarketingCenterOrder(tradeVO); }
		 */
		Long tradeNum = 0L;
		if (tradeVO.getPageSize() != null && tradeVO.getPageSize() != 1L) {
			tradeNum = orderDao.countMarketingCenterOrder(tradeVO);
			tradeVO.setTotalCount(tradeNum);
		}
		if (orderDTOs != null && !orderDTOs.isEmpty()) {
			logger.info("此次查询orders的个数为:" + orderDTOs.size() + "tradeNum:" + tradeNum);
		} else {
			logger.info("此次查询orders的个数为:kong ,tradeNum:" + tradeNum);
		}
		if (queryKey != null) {
			redisLockService.putStringValueWithExpireTime(
					RedisConstant.RediskeyCacheGroup.ORDER_BATCH_SEND_DATA_KEY + "-" + queryKey + "-" + uid,
					JsonUtil.toJson(tradeVO), TimeUnit.HOURS, 1L);
		}

		Map<Long, TradeResultVO> resultMap = new HashMap<>();
		if (orderDTOs != null && !orderDTOs.isEmpty()) {
			Long maxId = 0L;
			for (OrderDTO orderDTO : orderDTOs) {
				try {
					if (orderDTO != null) {
						if (orderDTO.getTid() > maxId) {
							maxId = orderDTO.getTid();
						}
						if (orderDTO.getBuyerNick() != null && !"".equals(orderDTO.getBuyerNick())) {
							if (EncrptAndDecryptClient.isEncryptData(orderDTO.getBuyerNick(),
									EncrptAndDecryptClient.SEARCH)) {
								orderDTO.setBuyerNick(decryptClient.decryptData(orderDTO.getBuyerNick(),
										EncrptAndDecryptClient.SEARCH, sessionKey));
							}
						}
						if (orderDTO.getReceiverMobile() != null && !"".equals(orderDTO.getReceiverMobile())) {
							if (EncrptAndDecryptClient.isEncryptData(orderDTO.getReceiverMobile(),
									EncrptAndDecryptClient.PHONE)) {
								orderDTO.setReceiverMobile(decryptClient.decryptData(orderDTO.getReceiverMobile(),
										EncrptAndDecryptClient.PHONE, sessionKey));
							}
						}
						if (orderDTO.getReceiverName() != null || !"".equals(orderDTO.getReceiverName())) {
							if (EncrptAndDecryptClient.isEncryptData(orderDTO.getReceiverName(),
									EncrptAndDecryptClient.SEARCH)) {
								orderDTO.setReceiverName(decryptClient.decryptData(orderDTO.getReceiverName(),
										EncrptAndDecryptClient.SEARCH, sessionKey));
							}
						}
					}
				} catch (SecretException e) {
					e.printStackTrace();
				}
				if (resultMap.containsKey(orderDTO.getTid())) {
					TradeResultVO tradeResultVO = resultMap.get(orderDTO.getTid());
					tradeResultVO.getOrderDTOs().add(orderDTO);
					// tradeResultVO.setItemNum(itemNum);
					resultMap.put(orderDTO.getTid(), tradeResultVO);
				} else {
					TradeResultVO resultVO = new TradeResultVO();
					List<OrderDTO> resultOrderDTOs = new ArrayList<>();
					resultVO.setBuyerNick(orderDTO.getBuyerNick());
					resultVO.setCreated(orderDTO.getTradeCreated());
					resultVO.setOrderDTOs(resultOrderDTOs);
					resultVO.setReceiverMobile(orderDTO.getReceiverMobile());
					resultVO.setTid(orderDTO.getTid());
					resultVO.setTradeFrom(orderDTO.getTradeFrom());
					resultVO.setTradeNum(orderDTO.getNum());
					resultVO.setTradePayment(orderDTO.getTradePayment().toString());
					resultVO.setTradeStatus(orderDTO.getTradeStatus());
					resultVO.setItemNum(orderDTO.getNum());
					resultVO.setItemPrice(orderDTO.getPrice().toString());
					resultVO.setItemTitle(orderDTO.getTitle());
					resultVO.setReceiverName(orderDTO.getReceiverName());
					resultVO.setSellerFlag(orderDTO.getSellerFlag() + "");
					resultMap.put(orderDTO.getTid(), resultVO);
				}
			}
			tradeVO.setStartRows(maxId);
			logger.info("!!!!!!!!!!!!!!!!!!!!maxTid:" + maxId);
			// return orderDTOs;
			if (resultMap != null && !resultMap.isEmpty()) {
				List<TradeResultVO> resultVOs = new ArrayList<>();
				Set<Entry<Long, TradeResultVO>> entrySet = resultMap.entrySet();
				for (Entry<Long, TradeResultVO> entry : entrySet) {
					TradeResultVO resultVO = entry.getValue();
					resultVOs.add(resultVO);
				}
				logger.info("拼接成TradeVO对象耗时：" + (System.currentTimeMillis() - l2) + "ms");
				return resultVOs;
			}
		}
		return null;
	}

	/**
	 * 订单短信群发筛选全部订单个数
	 * 
	 * @throws Exception
	 */
	@Override
	public Long countMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey, String queryKey) throws Exception {
		if (sessionKey == null || "".equals(sessionKey) || uid == null) {
			return null;
		}
		tradeVO.setUid(uid);
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		if (tradeVO.getBuyerNick() != null && !"".equals(tradeVO.getBuyerNick())) {
			try {
				if (!EncrptAndDecryptClient.isEncryptData(tradeVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
					tradeVO.setBuyerNick(
							decryptClient.encrypt(tradeVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH, sessionKey));
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
		}
		if (tradeVO.getItemTitle() != null && !"".equals(tradeVO.getItemTitle())) {
			List<Long> numIidList = this.itemService.fuzzilyFindNumIidByGoodsKeyCode(uid, tradeVO.getItemTitle());
			tradeVO.setNumIidList(numIidList);
			tradeVO.setIsTitle(null);
			tradeVO.setItemTitle(null);
		}
		Long tradeNum = orderDao.countMarketingCenterOrder(tradeVO);
		/*
		 * if((tradeVO.getNumIidStr() != null &&
		 * !"".equals(tradeVO.getNumIidStr())) || (tradeVO.getRefundStatus() !=
		 * null && !"".equals(tradeVO.getRefundStatus()))){ tradeNum =
		 * orderDao.countOrderDTO(tradeVO); }else { tradeNum =
		 * orderDao.countMarketingCenterOrder(tradeVO); }
		 */
		return tradeNum;
	}

	/**
	 * 查询时间段内每天的下单金额
	 */
	@Override
	public List<Double> listDaysPayment(Long uid, Date endTime) {
		if (uid == null) {
			return null;
		}
		Date beginTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(6, endTime));
		Map<String, Double> deafaultMap = new LinkedHashMap<String, Double>();
		for (int i = 0; i < 6; i++) {
			String nDaysAgo = DateUtils.dateToString(DateUtils.nDaysAgo(i, endTime));
			deafaultMap.put(nDaysAgo, 0.00);
		}
		TradeVO tradeVO = new TradeVO();
		tradeVO.setUid(uid);
		tradeVO.setMinCreatedTime(beginTime);
		tradeVO.setMaxCreatedTime(endTime);
		List<TradeVO> TradeVOList = tradeDao.listDaysPayment(tradeVO);
		if (TradeVOList != null && !TradeVOList.isEmpty()) {
			for (TradeVO resultVO : TradeVOList) {
				if (deafaultMap.containsKey(resultVO.getMinCreatedTimeStr())) {
					deafaultMap.put(resultVO.getMinCreatedTimeStr(), resultVO.getMinPayment());
				}
			}
		}
		List<Double> values = new ArrayList<>();
		Set<Entry<String, Double>> entrySet = deafaultMap.entrySet();
		for (Entry<String, Double> entry : entrySet) {
			values.add(entry.getValue() == null ? 0.00 : entry.getValue());
		}
		return values;
	}

	/**
	 * 订单中心根据订单id查询催付订单数、金额以及回款的订单数、金额
	 */
	@Override
	public TradeCenterEffect sumEarningOrder(Long uid, List<String> tidList, List<String> statusList, Date beginTime,
			Date endTime) {
		if (uid == null || tidList == null || tidList.isEmpty()) {
			return null;
		}
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("uid", uid);
		queryMap.put("statusList", statusList);
		List<Long> tids = new ArrayList<>();
		for (String tidStr : tidList) {
			if (tidStr == null || "".equals(tidStr)) {
				continue;
			}
			tids.add(Long.parseLong(tidStr));
		}
		queryMap.put("tidList", tids);
		if (statusList == null || statusList.isEmpty()) {
			queryMap.put("statusList", null);
		}
		queryMap.put("beginTime", beginTime);
		queryMap.put("endTime", endTime);
		TradeDTO tradeDTO = this.tradeDao.sumTradeCenterEffect(queryMap);
		TradeCenterEffect resultEffect = new TradeCenterEffect();
		resultEffect.setTargetFee(0.0);
		resultEffect.setTargetOrder(0);
		if (tradeDTO != null) {
			resultEffect.setTargetFee(tradeDTO.getPayment() == null ? 0.0 : tradeDTO.getPayment().doubleValue());
			resultEffect.setTargetOrder(tradeDTO.getTid() == null ? 0 : tradeDTO.getTid().intValue());
		}

		return resultEffect;
	}

	/**
	 * 订单中心效果分析查询状态变化的主订单
	 */
	@Override
	public List<TradeDTO> listTradeByStatus(Long uid, TradeVO tradeVO, String sessionKey) {
		if (tradeVO == null || tradeVO.getUid() == null) {
			return null;
		}
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		List<TradeDTO> tradeList = tradeDao.listTradeByStatus(tradeVO);
		if (tradeList != null && !tradeList.isEmpty()) {
			for (int i = 0; i < tradeList.size(); i++) {
				try {
					TradeDTO tradeDTO = tradeList.get(i);
					if (EncrptAndDecryptClient.isEncryptData(tradeDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
						tradeDTO.setBuyerNick(decryptClient.decryptData(tradeDTO.getBuyerNick(),
								EncrptAndDecryptClient.SEARCH, sessionKey));
					}
					if (EncrptAndDecryptClient.isEncryptData(tradeDTO.getReceiverMobile(),
							EncrptAndDecryptClient.PHONE)) {
						tradeDTO.setReceiverMobile(decryptClient.decryptData(tradeDTO.getReceiverMobile(),
								EncrptAndDecryptClient.PHONE, sessionKey));
					}
				} catch (SecretException e) {
					e.printStackTrace();
				}
			}
		}
		return tradeList;
	}

	/**
	 * 根据手机号匹配查询新创建的订单
	 */
	@Override
	public List<TradeDTO> listNewTradeByPhones(Long uid, TradeVO tradeVO, String sessionKey) {
		List<TradeDTO> tradeDTOs = tradeDao.listNewTradeByPhones(tradeVO);
		return tradeDTOs;
	}

	/**
	 * @Description 客户管理查询出该会员的相关订单信息 <br/>
	 *              此处需要封装主订单-子订单 一对多
	 * @param uid
	 * @param map
	 * @return SimplePage 返回类型
	 * @author jackstraw_yu
	 * @date 2018年3月1日 下午1:44:58
	 */
	public SimplePage queryMemberTradePage(Long uid, Map<String, Object> map) {
		// long count = this.queryMemberTradeCount(uid,map);
		// List<TradeDTO> datas = this.queryMemberTradeList(uid,map);
		// if(datas!=null && !datas.isEmpty())
		// this.fillTradeWithOrder(uid,datas);
		// SimplePage page = new
		// SimplePage(datas,(Integer)map.get("pageNo"),count,(Integer)map.get("pageSize"));
		// return page;
		return null;
	}

	/**
	 * 通过订单查询出tradeDto是否存在
	 * 
	 * @author HL
	 * @time 2018年6月1日 下午1:50:30
	 * @param userId
	 * @param newTid
	 * @return
	 */
	@Override
	public Integer findOneTradeDTOByTid(Long uid, String tid) {
		Map<String, Object> map = new HashMap<String, Object>(5);
		map.put("uid", uid);
		map.put("tid", tid);
		return tradeDao.findOneTradeDTOByTid(map);
	}

	/**
	 * 通过订单号匹配订单
	 */
	@Override
	public List<TradeDTO> listTradeByTids(Long uid, List<String> tidList) {
		if (uid == null || tidList == null || tidList.isEmpty()) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("tidList", tidList);
		List<TradeDTO> tradeList = this.tradeDao.listTradeByTids(map);
		return tradeList;
	}

	/**
	 * 下载订单数据
	 */
	@Override
	public List<TradeResultVO> exportTradeDTO(Long uid, String queryKey, String sessionKey) {
		if (queryKey == null || "".equals(queryKey)) {
			return null;
		}
		TradeVO tradeVO = redisLockService.getValue(
				RedisConstant.RediskeyCacheGroup.ORDER_BATCH_SEND_DATA_KEY + "-" + queryKey + "-" + uid, TradeVO.class);
		tradeVO.setStartRows(null);
		tradeVO.setPageSize(null);
		List<OrderDTO> orderDTOs = null;
		if ((tradeVO.getNumIidStr() != null && !"".equals(tradeVO.getNumIidStr()))
				|| (tradeVO.getRefundStatus() != null && !"".equals(tradeVO.getRefundStatus()))) {
			orderDTOs = orderDao.listOrderDTOs(tradeVO);
		} else {
			orderDTOs = tradeDao.listMarketingCenterOrder(tradeVO);
		}
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		Map<Long, TradeResultVO> resultMap = new HashMap<>();
		if (orderDTOs != null && !orderDTOs.isEmpty()) {
			for (OrderDTO orderDTO : orderDTOs) {
				try {
					if (orderDTO != null && orderDTO.getBuyerNick() != null && !"".equals(orderDTO.getBuyerNick())) {
						if (EncrptAndDecryptClient.isEncryptData(orderDTO.getBuyerNick(),
								EncrptAndDecryptClient.SEARCH)) {
							orderDTO.setBuyerNick(decryptClient.decryptData(orderDTO.getBuyerNick(),
									EncrptAndDecryptClient.SEARCH, sessionKey));
						}
					}
					if (orderDTO != null && orderDTO.getReceiverMobile() != null
							&& !"".equals(orderDTO.getReceiverMobile())) {
						if (EncrptAndDecryptClient.isEncryptData(orderDTO.getReceiverMobile(),
								EncrptAndDecryptClient.PHONE)) {
							orderDTO.setReceiverMobile(decryptClient.decryptData(orderDTO.getReceiverMobile(),
									EncrptAndDecryptClient.PHONE, sessionKey));
						}
					}
				} catch (SecretException e) {
					e.printStackTrace();
				}
				if (resultMap.containsKey(orderDTO.getTid())) {
					TradeResultVO tradeResultVO = resultMap.get(orderDTO.getTid());
					tradeResultVO.getOrderDTOs().add(orderDTO);
					resultMap.put(orderDTO.getTid(), tradeResultVO);
				} else {
					TradeResultVO resultVO = new TradeResultVO();
					List<OrderDTO> resultOrderDTOs = new ArrayList<>();
					resultOrderDTOs.add(orderDTO);
					resultVO.setBuyerNick(orderDTO.getBuyerNick());
					resultVO.setCreated(orderDTO.getTradeCreated());
					resultVO.setOrderDTOs(resultOrderDTOs);
					resultVO.setReceiverMobile(orderDTO.getReceiverMobile());
					resultVO.setTid(orderDTO.getTid());
					resultVO.setTradeFrom(orderDTO.getTradeFrom());
					resultVO.setTradeNum(orderDTO.getTradeNum());
					resultVO.setTradePayment(orderDTO.getTradePayment().toString());
					resultVO.setTradeStatus(orderDTO.getTradeStatus());
					resultMap.put(orderDTO.getTid(), resultVO);
				}
			}
			// return orderDTOs;
			if (resultMap != null && !resultMap.isEmpty()) {
				ArrayList<TradeResultVO> values = (ArrayList<TradeResultVO>) resultMap.values();
				return values;
			}
		}
		return null;
	}

	/**
	 * 根据手机号查询订单号，不需要加解密
	 */
	@Override
	public List<TradeDTO> listTradeByPhone(Long uid, List<String> phones, Date bTime, Date eTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("phones", phones);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		List<TradeDTO> tradeDTOs = tradeDao.listTradeByPhones(map);

		return tradeDTOs;
	}

	@Override
	public TradeDTO findTradeDTOByTid(Long uid, Long tid) {
		return null;
	}

	@Override
	public void updateTableIndex(Long uid) {
	}

	@Override
	public List<Long> getHasTradeButNotFoundOrderList(Long uid) throws Exception {
		return null;
	}

	@Override
	public List<Long> getHasTradeButNotFoundMemberList(Long uid) throws Exception {
		return null;
	}

	@Override
	public List<TradeDTO> getDirtyData(Long uid, String sellerNick) throws Exception {
		return null;
	}

	@Override
	public void deleteDirtyDataBySellerNick(Long uid, String sellerNick) throws Exception {
	}

	@Override
	public List<Long> getTidWhetherTradeNumIsNullOrEqualsZero(Long uid) throws Exception {
		return null;
	}

	@Override
	public List<TempEntity> getAddNumberFromTradeTable(Long uid, Set<String> buyerNickList) throws Exception {
		return null;
	}

	@Override
	public List<TempEntity> getBuyNumberFromTradeTable(Long uid, Set<String> keySet) throws Exception {
		return null;
	}

	@Override
	public List<TempEntity> getFirstPayTimeFromTradeTable(Long uid, Set<String> buyerNickList) throws Exception {
		return null;
	}

	/**
	 * 查询RFM标准分析购买次数和最后购买时间对应的会员数
	 */
	@Override
	public Long countMemberAmountByTimes(Long uid, Integer buyTimes, Date bTime, Date eTime) throws Exception {
		if (uid == null || buyTimes == null || bTime == null || eTime == null) {
			return 0L;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("buyTimes", buyTimes);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		Long memberAmount = this.tradeDao.countMemberAmountByTimes(map);
		return memberAmount == null ? 0 : memberAmount;
	}

	/**
	 * 查询RFM标准分析购买次数和最后购买时间对应的会员数
	 */
	@Override
	public List<TradeDTO> listEffectRFMByTime(Long uid, Date bTime, Date eTime) throws Exception {
		if (uid == null || bTime == null || eTime == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		List<TradeDTO> trades = this.tradeDao.listEffectRFMByTime(map);
		return trades;
	}

	/**
	 * 查询RFM标准分析购买次数和最后购买时间对应的累计消费金额以及平均客单价
	 */
	@Override
	public Double sumPaidAmountByTimes(Long uid, Integer buyTimes, Date bTime, Date eTime) throws Exception {
		if (uid == null || buyTimes == null || bTime == null || eTime == null) {
			return 0.0;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("buyTimes", buyTimes);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		BigDecimal paidAmount = this.tradeDao.sumPaidAmountByTimes(map);
		return paidAmount == null ? 0.0 : paidAmount.doubleValue();
	}

	/**
	 * 计算RFM详情数据列表
	 */
	@Override
	public Map<String, Object> listRFMDetailChart(Long uid, String dateType, Integer days, Date bTime, Date eTime)
			throws Exception {
		Long l1 = System.currentTimeMillis();
		if (uid == null || bTime == null || eTime == null || days == null || days == 0) {
			return null;
		}
		if (dateType == null || "".equals(dateType)) {
			dateType = "day";
		}
		Map<String, Object> resultMap = new LinkedHashMap<>();
		Map<String, String> tempMap = new LinkedHashMap<>();
		if ("day".equals(dateType) || "day" == dateType) {
			for (int i = 0; i < days; i++) {
				tempMap.put(DateUtils.dateToString(DateUtils.nDaysAfter(i, bTime)), "0,0.0,0");
			}
		} else if ("month".equals(dateType) || "month" == dateType) {
			for (int i = 0; i < days + 1; i++) {
				tempMap.put(DateUtils.dateToStringYM(DateUtils.nDaysAfter(i * 30, bTime)), "0,0.0,0");
			}
		}
		Long l2 = System.currentTimeMillis();
		logger.info("111111111111111111111111耗时：" + (l2 - l1) + "ms");
		logger.info("tempMap0:" + JsonUtil.toJson(tempMap));
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);

		// 下单客户数的list
		// List<TradeVO> memberCreateAmount =
		// this.tradeDao.listCreateCustomerAmount(map);
		// 购买次数和消费金额的list
		Long l3 = System.currentTimeMillis();
		List<TradeVO> memberPaidData = this.tradeDao.listPaidData(map);
		Long l4 = System.currentTimeMillis();
		logger.info("222222222222222222222222222耗时：" + (l4 - l3) + "ms");
		// if (memberCreateAmount != null && !memberCreateAmount.isEmpty()) {
		// for (TradeVO tradeVO : memberCreateAmount) {
		// logger.info("minCreatedTimeStr1:" + tradeVO.getMinCreatedTimeStr() +
		// ",biuerNick1:" + tradeVO.getBuyerNick()
		// + ",dateType:" + dateType + ",bTime:" + bTime + ",eTime:" + eTime);
		// if (tempMap.containsKey(tradeVO.getMinCreatedTimeStr())) {
		// tempMap.put(tradeVO.getMinCreatedTimeStr(), tradeVO.getBuyerNick());
		// logger.info("containKey:" + tradeVO.getMinCreatedTimeStr() + ",is
		// true");
		// }
		// }
		// }
		if (memberPaidData != null && !memberPaidData.isEmpty()) {
			for (TradeVO tradeVO : memberPaidData) {
				Long tid = tradeVO.getTid() == null ? 0L : tradeVO.getTid();
				String paymentStr = tradeVO.getBuyerNick() == null ? "0.0" : tradeVO.getBuyerNick();
				Integer paidNickNum = tradeVO.getPageNo() == null ? 0 : tradeVO.getPageNo();
				if (tempMap.containsKey(tradeVO.getMinCreatedTimeStr())) {
					tempMap.put(tradeVO.getMinCreatedTimeStr(), tid + "," + paymentStr + "," + paidNickNum);
				}
			}
		}
		Long l5 = System.currentTimeMillis();
		logger.info("333333333333333333333333333耗时：" + (l5 - l4) + "ms");
		logger.info("tempMap2:" + JsonUtil.toJson(tempMap));
		if (tempMap != null && !tempMap.isEmpty()) {
			List<String> dateList = new ArrayList<String>();
			List<Integer> createMemberNum = new ArrayList<Integer>();
			List<Integer> paidNum = new ArrayList<Integer>();
			List<Double> paidFee = new ArrayList<Double>();
			List<Double> avgPriceList = new ArrayList<Double>();
			Set<Entry<String, String>> entrySet = tempMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				dateList.add(entry.getKey());
				String detailStr = entry.getValue();
				String[] detailArr = detailStr.split(",");
				if (detailArr != null && detailArr.length >= 3) {
					createMemberNum.add(detailArr[2] == null ? 0 : NumberUtils.stringToInteger(detailArr[2]));
					paidNum.add(detailArr[0] == null ? 0 : NumberUtils.stringToInteger(detailArr[0]));
					paidFee.add(detailArr[1] == null ? 0.0 : NumberUtils.stringToDouble(detailArr[1]));
					Double avgPrice = Integer.parseInt(detailArr[2]) == 0 ? 0.0
							: (NumberUtils.getTwoDouble(
									new BigDecimal(detailArr[1]).doubleValue() / Integer.parseInt(detailArr[2])));
					avgPriceList.add(avgPrice == null ? 0.0 : avgPrice);
				}
			}
			resultMap.put("dateList", dateList);
			resultMap.put("createMemberNum", createMemberNum);
			resultMap.put("paidNum", paidNum);
			resultMap.put("paidFee", paidFee);
			resultMap.put("avgPriceList", avgPriceList);
		}
		Long l6 = System.currentTimeMillis();
		logger.info("44444444444444444444444444444444耗时：" + (l6 - l5) + "ms");

		logger.info("dateType:" + dateType + ",days:" + days + ",总耗时：" + (l6 - l1) + "ms");
		return resultMap;
	}

	/**
	 * 计算标准RFM中会员数的数据
	 */
	@Override
	public List<EffectStandardRFM> listCustomerRFMs(Long uid, Date startNodeDate) throws Exception {
		List<EffectStandardRFM> results = new ArrayList<>();
		// 计算近30天有交易客户数
		EffectStandardRFM standardCusRFMLtam = new EffectStandardRFM();
		standardCusRFMLtam.setUid(uid);
		standardCusRFMLtam.setTimeScope("ltam");
		standardCusRFMLtam.setEffectType(0);
		// 计算近30天有交易消费金额
		EffectStandardRFM standardPaidRFMLtam = new EffectStandardRFM();
		standardPaidRFMLtam.setUid(uid);
		standardPaidRFMLtam.setTimeScope("ltam");
		standardPaidRFMLtam.setEffectType(1);
		// 计算近30天有交易平均客单价
		EffectStandardRFM standardAvgRFMLtam = new EffectStandardRFM();
		standardAvgRFMLtam.setUid(uid);
		standardAvgRFMLtam.setEffectType(2);
		standardAvgRFMLtam.setTimeScope("ltam");

		Date ltamBTime = DateUtils.addDate(startNodeDate, -30), ltamETime = startNodeDate;
		double ltemAvgPrice = this.assembleCusStandardRFM(standardCusRFMLtam, standardPaidRFMLtam, standardAvgRFMLtam,
				ltamBTime, ltamETime);

		// 计算近30 - 90天有交易客户数
		EffectStandardRFM standardCusRFMAmttm = new EffectStandardRFM();
		standardCusRFMAmttm.setUid(uid);
		standardCusRFMAmttm.setTimeScope("amttm");
		standardCusRFMAmttm.setEffectType(0);
		// 计算近30 - 90天有交易消费金额
		EffectStandardRFM standardPaidRFMAmttm = new EffectStandardRFM();
		standardPaidRFMAmttm.setUid(uid);
		standardPaidRFMAmttm.setTimeScope("amttm");
		standardPaidRFMAmttm.setEffectType(1);
		// 计算近30 - 90天有交易平均客单价
		EffectStandardRFM standardAvgRFMAmttm = new EffectStandardRFM();
		standardAvgRFMAmttm.setUid(uid);
		standardAvgRFMAmttm.setEffectType(2);
		standardAvgRFMAmttm.setTimeScope("amttm");

		Date amttmBTime = DateUtils.addDate(startNodeDate, -90), amttmETime = DateUtils.addDate(startNodeDate, -30);
		double amttmAvgPrice = this.assembleCusStandardRFM(standardCusRFMAmttm, standardPaidRFMAmttm,
				standardAvgRFMAmttm, amttmBTime, amttmETime);

		// 计算近90 - 180天有交易客户数
		EffectStandardRFM standardCusRFMTmtsm = new EffectStandardRFM();
		standardCusRFMTmtsm.setUid(uid);
		standardCusRFMTmtsm.setTimeScope("tmtsm");
		standardCusRFMTmtsm.setEffectType(0);
		// 计算近90 - 180天有交易消费金额
		EffectStandardRFM standardPaidRFMTmtsm = new EffectStandardRFM();
		standardPaidRFMTmtsm.setUid(uid);
		standardPaidRFMTmtsm.setTimeScope("tmtsm");
		standardPaidRFMTmtsm.setEffectType(1);
		// 计算近90 - 180天有交易平均客单价
		EffectStandardRFM standardAvgRFMTmtsm = new EffectStandardRFM();
		standardAvgRFMTmtsm.setUid(uid);
		standardAvgRFMTmtsm.setEffectType(2);
		standardAvgRFMTmtsm.setTimeScope("tmtsm");

		Date tmtsmBTime = DateUtils.addDate(startNodeDate, -180), tmtsmETime = DateUtils.addDate(startNodeDate, -90);
		double tmtsmAvgPrice = this.assembleCusStandardRFM(standardCusRFMTmtsm, standardPaidRFMTmtsm,
				standardAvgRFMTmtsm, tmtsmBTime, tmtsmETime);

		// 计算近180 - 360天有交易客户数
		EffectStandardRFM standardCusRFMSmtay = new EffectStandardRFM();
		standardCusRFMSmtay.setUid(uid);
		standardCusRFMSmtay.setTimeScope("smtay");
		standardCusRFMSmtay.setEffectType(0);
		// 计算近180 - 360天有交易消费金额
		EffectStandardRFM standardPaidRFMSmtay = new EffectStandardRFM();
		standardPaidRFMSmtay.setUid(uid);
		standardPaidRFMSmtay.setTimeScope("smtay");
		standardPaidRFMSmtay.setEffectType(1);
		// 计算近180 - 360天有交易平均客单价
		EffectStandardRFM standardAvgRFMSmtay = new EffectStandardRFM();
		standardAvgRFMSmtay.setUid(uid);
		standardAvgRFMSmtay.setEffectType(2);
		standardAvgRFMSmtay.setTimeScope("smtay");

		Date smtayBTime = DateUtils.addDate(startNodeDate, -360), smtayETime = DateUtils.addDate(startNodeDate, -180);
		double smtayAvgPrice = this.assembleCusStandardRFM(standardCusRFMSmtay, standardPaidRFMSmtay,
				standardAvgRFMSmtay, smtayBTime, smtayETime);

		// 计算360天前有交易客户数
		EffectStandardRFM standardCusRFMMtay = new EffectStandardRFM();
		standardCusRFMMtay.setUid(uid);
		standardCusRFMMtay.setTimeScope("mtay");
		standardCusRFMMtay.setEffectType(0);
		// 计算360天前有交易消费金额
		EffectStandardRFM standardPaidRFMMtay = new EffectStandardRFM();
		standardPaidRFMMtay.setUid(uid);
		standardPaidRFMMtay.setTimeScope("mtay");
		standardPaidRFMMtay.setEffectType(1);
		// 计算360天前有交易平均客单价
		EffectStandardRFM standardAvgRFMMtay = new EffectStandardRFM();
		standardAvgRFMMtay.setUid(uid);
		standardAvgRFMMtay.setEffectType(2);
		standardAvgRFMMtay.setTimeScope("mtay");

		Date mtayBTime = DateUtils.addDate(startNodeDate, -360);
		double mtayAvgPrice = this.assembleCusStandardRFM(standardCusRFMMtay, standardPaidRFMMtay, standardAvgRFMMtay,
				null, mtayBTime);

		double totalAvgPrice = ltemAvgPrice + amttmAvgPrice + tmtsmAvgPrice + smtayAvgPrice + mtayAvgPrice;
		this.assembleStandardRFMAvgPrice(standardAvgRFMLtam, totalAvgPrice);
		this.assembleStandardRFMAvgPrice(standardAvgRFMAmttm, totalAvgPrice);
		this.assembleStandardRFMAvgPrice(standardAvgRFMTmtsm, totalAvgPrice);
		this.assembleStandardRFMAvgPrice(standardAvgRFMSmtay, totalAvgPrice);
		this.assembleStandardRFMAvgPrice(standardAvgRFMMtay, totalAvgPrice);

		results.add(standardCusRFMLtam);
		results.add(standardPaidRFMLtam);
		results.add(standardAvgRFMLtam);

		results.add(standardCusRFMAmttm);
		results.add(standardPaidRFMAmttm);
		results.add(standardAvgRFMAmttm);

		results.add(standardCusRFMTmtsm);
		results.add(standardPaidRFMTmtsm);
		results.add(standardAvgRFMTmtsm);

		results.add(standardCusRFMSmtay);
		results.add(standardPaidRFMSmtay);
		results.add(standardAvgRFMSmtay);

		results.add(standardCusRFMMtay);
		results.add(standardPaidRFMMtay);
		results.add(standardAvgRFMMtay);
		return results;
	}

	public double assembleCusStandardRFM(EffectStandardRFM standardCusRFM, EffectStandardRFM standardPaidRFM,
			EffectStandardRFM standardAvgRFM, Date bTime, Date eTime) throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", standardCusRFM.getUid());
		logger.info("11111111111111111111111111111111");
		// 计算购买一次的会员数
		Long memberAmountOnce = 0L;
		// 计算购买二次的会员数
		Long memberAmountTwice = 0L;
		// 计算购买三次的会员数
		Long memberAmountThrice = 0L;
		// 计算购买四次的会员数
		Long memberAmountQuartic = 0L;
		// 计算购买五次的会员数
		Long memberAmountQuintic = 0L;
		// 计算购买一次的消费金额
		double paidAmountOnce = 0.0;
		// 计算购买二次的消费金额
		double paidAmountTwice = 0.0;
		// 计算购买三次的消费金额
		double paidAmountThrice = 0.0;
		// 计算购买四次的消费金额
		double paidAmountQuartic = 0.0;
		// 计算购买五次的消费金额
		double paidAmountQuintic = 0.0;
		MemberInfoDTO memberInfoOnce = memberDTOserviceImpl.queryStandardRFM(standardCusRFM.getUid(), 1, bTime, eTime);
		MemberInfoDTO memberInfoTwice = memberDTOserviceImpl.queryStandardRFM(standardCusRFM.getUid(), 2, bTime, eTime);
		MemberInfoDTO memberInfoThrice = memberDTOserviceImpl.queryStandardRFM(standardCusRFM.getUid(), 3, bTime,
				eTime);
		MemberInfoDTO memberInfoQuartic = memberDTOserviceImpl.queryStandardRFM(standardCusRFM.getUid(), 4, bTime,
				eTime);
		MemberInfoDTO memberInfoQuintic = memberDTOserviceImpl.queryStandardRFM(standardCusRFM.getUid(), 5, bTime,
				eTime);
		if (memberInfoOnce != null) {
			logger.info("2222222222222222222222222");
			memberAmountOnce = (long) NumberUtils.stringToInteger(memberInfoOnce.getBuyerNick());
			paidAmountOnce = memberInfoOnce.getTradeAmount() == null ? 0.0
					: memberInfoOnce.getTradeAmount().doubleValue();
			logger.info("uid:" + standardCusRFM.getUid() + ",30天内购买1次的会员数：" + memberAmountOnce + ",金额：" + paidAmountOnce
					+ ",查询条件，bTime:" + DateUtils.dateToStringHMS(bTime) + ",ETime:" + DateUtils.dateToStringHMS(eTime));
		} else {
			logger.info("3333333333333333333");
			logger.info("memberInfoOnce为NULL" + ",查询条件，bTime:" + bTime + ",ETime:" + eTime + ",uid:"
					+ standardCusRFM.getUid());
		}
		logger.info("uid :" + standardCusRFM.getUid() + ",memberAmountOnce:" + memberAmountOnce + ",paidAmountOnce"
				+ paidAmountOnce);
		if (memberInfoTwice != null) {
			memberAmountTwice = (long) NumberUtils.stringToInteger(memberInfoTwice.getBuyerNick());
			paidAmountTwice = memberInfoTwice.getTradeAmount() == null ? 0.0
					: memberInfoTwice.getTradeAmount().doubleValue();
			logger.info("uid:" + standardCusRFM.getUid() + ",30天内购买2次的会员数：" + memberAmountTwice + ",金额："
					+ paidAmountTwice + ",查询条件，bTime:" + DateUtils.dateToStringHMS(bTime) + ",ETime:"
					+ DateUtils.dateToStringHMS(eTime));
		}
		if (memberInfoThrice != null) {
			memberAmountThrice = (long) NumberUtils.stringToInteger(memberInfoThrice.getBuyerNick());
			paidAmountThrice = memberInfoThrice.getTradeAmount() == null ? 0.0
					: memberInfoThrice.getTradeAmount().doubleValue();
			logger.info("uid:" + standardCusRFM.getUid() + ",30天内购买3次的会员数：" + memberAmountThrice + ",金额："
					+ paidAmountThrice + ",查询条件，bTime:" + DateUtils.dateToStringHMS(bTime) + ",ETime:"
					+ DateUtils.dateToStringHMS(eTime));
		}
		if (memberInfoQuartic != null) {
			memberAmountQuartic = (long) NumberUtils.stringToInteger(memberInfoQuartic.getBuyerNick());
			paidAmountQuartic = memberInfoQuartic.getTradeAmount() == null ? 0.0
					: memberInfoQuartic.getTradeAmount().doubleValue();
			logger.info("uid:" + standardCusRFM.getUid() + ",30天内购买4次的会员数：" + memberAmountQuartic + ",金额："
					+ paidAmountQuartic + ",查询条件，bTime:" + DateUtils.dateToStringHMS(bTime) + ",ETime:"
					+ DateUtils.dateToStringHMS(eTime));
		}
		if (memberInfoQuintic != null) {
			memberAmountQuintic = (long) NumberUtils.stringToInteger(memberInfoQuintic.getBuyerNick());
			paidAmountQuintic = memberInfoQuintic.getTradeAmount() == null ? 0.0
					: memberInfoQuintic.getTradeAmount().doubleValue();
			logger.info("uid:" + standardCusRFM.getUid() + ",30天内购买5次的会员数：" + memberAmountQuintic + ",金额："
					+ paidAmountQuintic + ",查询条件，bTime:" + DateUtils.dateToStringHMS(bTime) + ",ETime:"
					+ DateUtils.dateToStringHMS(eTime));
		}
		standardCusRFM.setBuyOnceData(memberAmountOnce + "");

		standardCusRFM.setBuyTwiceData(memberAmountTwice + "");

		standardCusRFM.setBuyThriceData(memberAmountThrice + "");

		standardCusRFM.setBuyQuarticData(memberAmountQuartic + "");

		standardCusRFM.setBuyQuinticData(memberAmountQuintic + "");

		standardPaidRFM.setBuyOnceData(NumberUtils.getTwoDouble(paidAmountOnce) + "");

		standardPaidRFM.setBuyTwiceData(NumberUtils.getTwoDouble(paidAmountTwice) + "");

		standardPaidRFM.setBuyThriceData(NumberUtils.getTwoDouble(paidAmountThrice) + "");

		standardPaidRFM.setBuyQuarticData(NumberUtils.getTwoDouble(paidAmountQuartic) + "");

		standardPaidRFM.setBuyQuinticData(NumberUtils.getTwoDouble(paidAmountQuintic) + "");

		/*
		 * 计算平均客单价
		 */
		// 购买一次的客单价
		double avgPriceOnce = memberAmountOnce == 0 ? 0.0 : NumberUtils.getTwoDouble(paidAmountOnce / memberAmountOnce);
		// 购买二次的客单价
		double avgPriceTwice = memberAmountTwice == 0 ? 0.0
				: NumberUtils.getTwoDouble(paidAmountTwice / memberAmountTwice);
		// 购买三次的客单价
		double avgPriceThrice = memberAmountThrice == 0 ? 0.0
				: NumberUtils.getTwoDouble(paidAmountThrice / memberAmountThrice);
		// 购买四次的客单价
		double avgPriceQuartic = memberAmountQuartic == 0 ? 0.0
				: NumberUtils.getTwoDouble(paidAmountQuartic / memberAmountQuartic);
		// 购买五次以上的客单价
		double avgPriceQuintic = memberAmountQuintic == 0 ? 0.0
				: NumberUtils.getTwoDouble(paidAmountQuintic / memberAmountQuintic);
		// 客单价总和(没有实际意思，一点都没，直接加起来算比例)
		double totalAvgPrice = avgPriceOnce + avgPriceTwice + avgPriceThrice + avgPriceQuartic + avgPriceQuintic;
		// 购买一次的客单价
		standardAvgRFM.setBuyOnceData(avgPriceOnce + "");
		// 购买二次的客单价
		standardAvgRFM.setBuyTwiceData(avgPriceTwice + "");
		// 购买三次的客单价
		standardAvgRFM.setBuyThriceData(avgPriceThrice + "");
		// 购买四次的客单价
		standardAvgRFM.setBuyQuarticData(avgPriceQuartic + "");
		// 购买五次以上的客单价
		standardAvgRFM.setBuyQuinticData(avgPriceQuintic + "");
		return totalAvgPrice;
	}

	public void assembleStandardRFMAvgPrice(EffectStandardRFM standardAvgRFM, double totalAvgPrice) {
		// 购买一次的客单价
		double ratioAvgOnce = 0.0;
		if (totalAvgPrice != 0) {
			ratioAvgOnce = NumberUtils.stringToDouble(standardAvgRFM.getBuyOnceData()) / totalAvgPrice;
		}
		standardAvgRFM.setBuyOnceRatio(NumberUtils.getTwoDouble(ratioAvgOnce * 100) + "%");
		// 购买二次的客单价
		double ratioAvgTwice = 0.0;
		if (totalAvgPrice != 0) {
			ratioAvgTwice = NumberUtils.stringToDouble(standardAvgRFM.getBuyTwiceData()) / totalAvgPrice;
		}
		standardAvgRFM.setBuyTwiceRatio(NumberUtils.getTwoDouble(ratioAvgTwice * 100) + "%");
		// 购买三次的客单价
		double ratioAvgThrice = 0.0;
		if (totalAvgPrice != 0) {
			ratioAvgThrice = NumberUtils.stringToDouble(standardAvgRFM.getBuyThriceData()) / totalAvgPrice;
		}
		standardAvgRFM.setBuyThriceRatio(NumberUtils.getTwoDouble(ratioAvgThrice * 100) + "%");
		// 购买四次的客单价
		double ratioAvgQuartic = 0.0;
		if (totalAvgPrice != 0) {
			ratioAvgQuartic = NumberUtils.stringToDouble(standardAvgRFM.getBuyQuarticData()) / totalAvgPrice;
		}
		standardAvgRFM.setBuyQuarticRatio(NumberUtils.getTwoDouble(ratioAvgQuartic * 100) + "%");
		// 购买五次以上的客单价
		double ratioAvgQuintic = 0.0;
		if (totalAvgPrice != 0) {
			ratioAvgQuintic = NumberUtils.stringToDouble(standardAvgRFM.getBuyQuinticData()) / totalAvgPrice;
		}
		standardAvgRFM.setBuyQuinticRatio(NumberUtils.getTwoDouble(ratioAvgQuintic * 100) + "%");
	}

	@Override
	public Long countUnPaidMember(Long uid) {
		if (uid == null) {
			return 0L;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		Long unPaidCount = 0L;
		try {
			unPaidCount = this.tradeDao.countUnPaidMember(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unPaidCount;
	}

	/**
	 * 订单短信群发筛选全部订单个数
	 */
	@Override
	public Long countMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey) {
		if (sessionKey == null || uid == null) {
			return null;
		}
		tradeVO.setUid(uid);
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		if (tradeVO.getBuyerNick() != null && !"".equals(tradeVO.getBuyerNick())) {
			try {
				if (!EncrptAndDecryptClient.isEncryptData(tradeVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
					tradeVO.setBuyerNick(
							decryptClient.encrypt(tradeVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH, sessionKey));
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
		}
		try {
			if (tradeVO.getItemTitle() != null && !"".equals(tradeVO.getItemTitle())) {
				List<Long> numIidList = this.itemService.fuzzilyFindNumIidByGoodsKeyCode(uid, tradeVO.getItemTitle());
				tradeVO.setNumIidList(numIidList);
				tradeVO.setIsTitle(null);
				tradeVO.setItemTitle(null);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Long tradeNum = orderDao.countMarketingCenterOrder(tradeVO);
		/*
		 * if((tradeVO.getNumIidStr() != null &&
		 * !"".equals(tradeVO.getNumIidStr())) || (tradeVO.getRefundStatus() !=
		 * null && !"".equals(tradeVO.getRefundStatus()))){ tradeNum =
		 * orderDao.countOrderDTO(tradeVO); }else { tradeNum =
		 * orderDao.countMarketingCenterOrder(tradeVO); }
		 */
		return tradeNum;
	}

	/**
	 * 订单短信群发筛选全部订单
	 */
	@Override
	public List<TradeResultVO> listMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey) {
		if (sessionKey == null || uid == null) {
			return null;
		}
		logger.info("入参maxId:" + tradeVO.getStartRows());
		tradeVO.setUid(uid);
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		if (tradeVO.getBuyerNick() != null && !"".equals(tradeVO.getBuyerNick())) {
			try {
				if (!EncrptAndDecryptClient.isEncryptData(tradeVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)) {
					tradeVO.setBuyerNick(
							decryptClient.encrypt(tradeVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH, sessionKey));
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
		}
		try {
			if (tradeVO.getItemTitle() != null && !"".equals(tradeVO.getItemTitle())) {
				List<Long> numIidList = this.itemService.fuzzilyFindNumIidByGoodsKeyCode(uid, tradeVO.getItemTitle());
				tradeVO.setNumIidList(numIidList);
				tradeVO.setIsTitle(null);
				tradeVO.setItemTitle(null);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		Long tradeNum = orderDao.countMarketingCenterOrder(tradeVO);
		List<OrderDTO> orderDTOs = orderDao.listMarketingCenterOrder(tradeVO);
		/*
		 * if((tradeVO.getNumIidStr() != null &&
		 * !"".equals(tradeVO.getNumIidStr())) || (tradeVO.getRefundStatus() !=
		 * null && !"".equals(tradeVO.getRefundStatus()))){ orderDTOs =
		 * orderDao.listOrderDTOs(tradeVO); tradeNum =
		 * orderDao.countOrderDTO(tradeVO); }else { orderDTOs =
		 * orderDao.listMarketingCenterOrder(tradeVO); tradeNum =
		 * orderDao.countMarketingCenterOrder(tradeVO); }
		 */
		if (orderDTOs != null && !orderDTOs.isEmpty()) {
			logger.info("此次查询orders的个数为:" + orderDTOs.size() + "tradeNum:" + tradeNum);
		} else {
			logger.info("此次查询orders的个数为:kong ,tradeNum:" + tradeNum);
		}
		tradeVO.setTotalCount(tradeNum);
		Map<Long, TradeResultVO> resultMap = new HashMap<>();
		if (orderDTOs != null && !orderDTOs.isEmpty()) {
			Long maxId = 0L;
			for (OrderDTO orderDTO : orderDTOs) {
				if (orderDTO.getTid() > maxId) {
					maxId = orderDTO.getTid();
				}
				try {
					if (orderDTO != null) {
						if (orderDTO.getBuyerNick() != null && !"".equals(orderDTO.getBuyerNick())) {
							if (EncrptAndDecryptClient.isEncryptData(orderDTO.getBuyerNick(),
									EncrptAndDecryptClient.SEARCH)) {
								orderDTO.setBuyerNick(decryptClient.decryptData(orderDTO.getBuyerNick(),
										EncrptAndDecryptClient.SEARCH, sessionKey));
							}
						}
						if (orderDTO.getReceiverMobile() != null && !"".equals(orderDTO.getReceiverMobile())) {
							if (EncrptAndDecryptClient.isEncryptData(orderDTO.getReceiverMobile(),
									EncrptAndDecryptClient.PHONE)) {
								orderDTO.setReceiverMobile(decryptClient.decryptData(orderDTO.getReceiverMobile(),
										EncrptAndDecryptClient.PHONE, sessionKey));
							}
						}
						if (orderDTO.getReceiverName() != null || !"".equals(orderDTO.getReceiverName())) {
							if (EncrptAndDecryptClient.isEncryptData(orderDTO.getReceiverName(),
									EncrptAndDecryptClient.SEARCH)) {
								orderDTO.setReceiverName(decryptClient.decryptData(orderDTO.getReceiverName(),
										EncrptAndDecryptClient.SEARCH, sessionKey));
							}
						}
					}
				} catch (SecretException e) {
					e.printStackTrace();
				}
				if (resultMap.containsKey(orderDTO.getTid())) {
					TradeResultVO tradeResultVO = resultMap.get(orderDTO.getTid());
					tradeResultVO.getOrderDTOs().add(orderDTO);
					// tradeResultVO.setItemNum(itemNum);
					resultMap.put(orderDTO.getTid(), tradeResultVO);
				} else {
					TradeResultVO resultVO = new TradeResultVO();
					List<OrderDTO> resultOrderDTOs = new ArrayList<>();
					resultVO.setBuyerNick(orderDTO.getBuyerNick());
					resultVO.setCreated(orderDTO.getTradeCreated());
					resultVO.setOrderDTOs(resultOrderDTOs);
					resultVO.setReceiverMobile(orderDTO.getReceiverMobile());
					resultVO.setTid(orderDTO.getTid());
					resultVO.setTradeFrom(orderDTO.getTradeFrom());
					resultVO.setTradeNum(orderDTO.getNum());
					resultVO.setTradePayment(orderDTO.getTradePayment().toString());
					resultVO.setTradeStatus(orderDTO.getTradeStatus());
					resultVO.setItemNum(orderDTO.getNum());
					resultVO.setItemPrice(orderDTO.getPrice().toString());
					resultVO.setItemTitle(orderDTO.getTitle());
					resultVO.setReceiverName(orderDTO.getReceiverName());
					resultMap.put(orderDTO.getTid(), resultVO);
				}
			}
			tradeVO.setStartRows(maxId);
			logger.info("!!!!!!!!!!!!!!!!!!!!maxTid:" + maxId);
			if (resultMap != null && !resultMap.isEmpty()) {
				List<TradeResultVO> resultVOs = new ArrayList<>();
				Set<Entry<Long, TradeResultVO>> entrySet = resultMap.entrySet();
				for (Entry<Long, TradeResultVO> entry : entrySet) {
					TradeResultVO resultVO = entry.getValue();
					resultVOs.add(resultVO);
				}
				return resultVOs;
			}
		}
		return null;
	}

	/**
	 * 根据订单查询订单
	 */
	@Override
	public List<TradeDTO> listStepTradeByTids(Long uid, List<String> tids, Date bTime, Date eTime) {
		if (uid == null || tids == null || tids.isEmpty()) {
			return null;
		}
		List<String> splitTids = new ArrayList<>();
		for (String tidStr : tids) {
			if (tidStr == null || "".equals(tidStr)) {
				continue;
			}
			String[] tidArr = tidStr.split(",");
			for (String tid : tidArr) {
				if (tid == null || "".equals(tid)) {
					continue;
				}
				splitTids.add(tid);
			}
		}
		if (splitTids.isEmpty()) {
			logger.info("splitTids为NULL");
			return null;
		}
		logger.info("splitTidsbu 为NULL,个数为:" + splitTids.size());
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("tids", splitTids);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		List<TradeDTO> tradeDTOs = null;
		try {
			tradeDTOs = this.tradeDao.listStepTradeByTids(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tradeDTOs;
	}

	/**
	 * 根据订单号查询对应订单总金额
	 * 
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> sumPaymentByTids(Long uid, MsgSendRecord msgSendRecord) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		if (uid == null || msgSendRecord == null) {
			resultMap.put("payment", 0.0);
			resultMap.put("tids", 0);
			return resultMap;
		}
		SmsRecordVO smsRecordVO = new SmsRecordVO();
		smsRecordVO.setUid(uid);
		smsRecordVO.setStatus(2);
		smsRecordVO.setType(msgSendRecord.getType());
		smsRecordVO.setMsgId(msgSendRecord.getId());
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		Double resultPayment = 0.0;
		Set<String> tidSet = new HashSet<>();
		if (msgSendRecord.getSucceedCount() != null) {
			if (msgSendRecord.getSucceedCount() > Constants.PROCESS_PAGE_SIZE_OVER) {
				Integer totalSuccessPhone = smsRecordDTOService.countSuccessRecordByMsgId(uid, msgSendRecord.getId());
				int start = 0, end = 0;
				if (totalSuccessPhone % Constants.PROCESS_PAGE_SIZE_OVER == 0) {
					end = totalSuccessPhone / Constants.PROCESS_PAGE_SIZE_OVER;
				} else {
					end = (totalSuccessPhone + Constants.PROCESS_PAGE_SIZE_OVER) / Constants.PROCESS_PAGE_SIZE_OVER;
				}
				while (start < end) {
					start++;
					smsRecordVO.setCurrentRows(Constants.PROCESS_PAGE_SIZE_OVER);
					smsRecordVO.setPageNo(start);
					List<String> tids = smsRecordDTOService.queryTidList(uid, smsRecordVO);
					if (tids == null || tids.isEmpty()) {
						continue;
					}
					List<String> splitTids = new ArrayList<>();
					for (String tidStr : tids) {
						if (tidStr == null || "".equals(tidStr)) {
							continue;
						}
						String[] tidArr = tidStr.split(",");
						if (tidArr != null && tidArr.length > 0) {
							for (String tid : tidArr) {
								if (tid != null && !"".equals(tid)) {
									splitTids.add(tid);
								}
							}
						}
					}
					if (splitTids == null || splitTids.isEmpty()) {
						continue;
					}
					map.put("tids", splitTids);
					BigDecimal payment = tradeDao.sumPaymentByTids(map);
					resultPayment += payment == null ? 0.0 : payment.doubleValue();
					tidSet.addAll(tids);
				}
			} else {
				List<String> tids = smsRecordDTOService.queryTidList(uid, smsRecordVO);
				List<String> splitTids = new ArrayList<>();
				for (String tidStr : tids) {
					if (tidStr == null || "".equals(tidStr)) {
						continue;
					}
					String[] tidArr = tidStr.split(",");
					if (tidArr != null && tidArr.length > 0) {
						for (String tid : tidArr) {
							if (tid != null && !"".equals(tid)) {
								splitTids.add(tid);
							}
						}
					}
				}
				if (splitTids == null || splitTids.isEmpty()) {
					return null;
				}
				map.put("tids", splitTids);
				BigDecimal payment = tradeDao.sumPaymentByTids(map);
				resultPayment += payment == null ? 0.0 : payment.doubleValue();
				tidSet.addAll(tids);
			}
		}
		resultMap.put("payment", resultPayment);
		resultMap.put("tids", tidSet.size());
		return resultMap;
	}

	@Override
	public List<TradeTempEntity> getTradeNumByBuyerNick(Long uid, Set<String> buyerNickSet) throws Exception {
		return null;
	}

	@Override
	public List<TradeTempEntity> getCloseTradeNum(Long uid, Set<String> buyerNickSet) throws Exception {
		return null;
	}

	@Override
	public List<TradeTempEntity> getAddNumber(Long uid, Set<String> buyerNickSet) throws Exception {
		return null;
	}

	@Override
	public List<TradeTempEntity> getbuyerAlipayNo(Long uid, Set<String> buyerNickSet) throws Exception {
		return null;
	}

	@Override
	public List<TradeTempEntity> getReceiverInfoStr(Long uid, Set<String> buyerNickSet) throws Exception {
		return null;
	}

	@Override
	public RFMDetailChart listRFMDetailChartData(Long uid, Integer memberType, String dateType, Integer dateNum,
			Date bTime, Date eTime) {
		if (uid == null || bTime == null || eTime == null || dateNum == null || dateNum == 0) {
			return null;
		}
		RFMDetailChart resultDetailChart = new RFMDetailChart();
		resultDetailChart.setUid(uid);
		resultDetailChart.setMemberType(memberType);
		resultDetailChart.setDateType(dateType);
		resultDetailChart.setDateNum(dateNum);
		resultDetailChart.setLastModifiedDate(new Date());
		resultDetailChart.setLastModifiedBy(uid + "");
		resultDetailChart.setCreatedBy(uid + "");
		resultDetailChart.setCreatedDate(new Date());
		if (dateType == null || "".equals(dateType)) {
			dateType = "day";
		}
		Map<String, String> tempMap = new LinkedHashMap<>();
		if ("day".equals(dateType) || "day" == dateType) {
			for (int i = 0; i < dateNum; i++) {
				tempMap.put(DateUtils.dateToString(DateUtils.nDaysAfter(i, bTime)), "0,0.0,0");
			}
		} else if ("month".equals(dateType) || "month" == dateType) {
			for (int i = 0; i < dateNum + 1; i++) {
				tempMap.put(DateUtils.dateToStringYM(DateUtils.nDaysAfter(i * 30, bTime)), "0,0.0,0");
			}
		}
		logger.info("tempMap0:" + JsonUtil.toJson(tempMap));
		Map<String, Object> map = new LinkedHashMap<>();
		if (memberType == 0) {
			map.put("tradeNum", 5);
		} else if (memberType == 1) {
			map.put("tradeNum", 1);
		} else if (memberType == 2) {
			map.put("tradeNum", 2);
		}
		map.put("uid", uid);
		map.put("dateType", dateType);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		List<TradeVO> memberPaidData = null;
		try {
			memberPaidData = this.tradeDao.listPaidData(map);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (memberPaidData != null && !memberPaidData.isEmpty()) {
			for (TradeVO tradeVO : memberPaidData) {
				Long tid = tradeVO.getTid() == null ? 0L : tradeVO.getTid();
				String paymentStr = tradeVO.getBuyerNick() == null ? "0.0" : tradeVO.getBuyerNick();
				Integer paidNickNum = tradeVO.getPageNo() == null ? 0 : tradeVO.getPageNo();
				if (tempMap.containsKey(tradeVO.getMinCreatedTimeStr())) {
					tempMap.put(tradeVO.getMinCreatedTimeStr(), tid + "," + paymentStr + "," + paidNickNum);
				}
			}
		}
		String dateStr = "";
		String memberNumStr = "";
		String tradeNumStr = "";
		String tradeAmountStr = "";
		String avgPriceStr = "";
		if (tempMap != null) {
			Set<Entry<String, String>> entrySet = tempMap.entrySet();
			for (Entry<String, String> entry : entrySet) {
				String date = entry.getKey();
				String value = entry.getValue();
				String[] dataArr = value.split(",");
				String tradeNum = dataArr[0];
				String tradeAmount = dataArr[1];
				String memberNum = dataArr[2];
				Double avgPrice = Integer.parseInt(memberNum) == 0 ? 0.0
						: (NumberUtils
								.getTwoDouble(new BigDecimal(tradeAmount).doubleValue() / Integer.parseInt(memberNum)));
				dateStr += date + ",";
				memberNumStr += memberNum + ",";
				tradeNumStr += tradeNum + ",";
				avgPriceStr += avgPrice + ",";
				tradeAmountStr += tradeAmount + ",";
			}
		}
		logger.info("uid:" + uid + ",本次保存数据库的购买次数：" + tradeNumStr + ",客户数：" + memberNumStr);
		resultDetailChart.setAvgPriceStr(avgPriceStr);
		resultDetailChart.setDateStr(dateStr);
		resultDetailChart.setTradeAmountStr(tradeAmountStr);
		resultDetailChart.setTradeNumStr(tradeNumStr);
		resultDetailChart.setMemberNumStr(memberNumStr);
		return resultDetailChart;
	}

	@Override
	public Long getCount(Long id) throws Exception {
		return null;
	}

	@Override
	public void truncateTable(Long uid) throws Exception {
	}

	@Override
	public List<RFMDetailChart> listRFMDetailChart(Long uid) {
		Long l1 = System.currentTimeMillis();
		Date eTime = new Date();
		Date bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(6, eTime));
		RFMDetailChart servenDayDataAll = this.listRFMDetailChartData(uid, 0, "day", 7, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(14, eTime));
		RFMDetailChart fifteenDayDataAll = this.listRFMDetailChartData(uid, 0, "day", 15, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(29, eTime));
		RFMDetailChart thirtyDayDataAll = this.listRFMDetailChartData(uid, 0, "day", 30, bTime, eTime);
		logger.info("RFMCHART4444444444444444444444444444444444");
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(180, eTime));
		RFMDetailChart sixMonthDataAll = this.listRFMDetailChartData(uid, 0, "month", 6, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(365, eTime));
		RFMDetailChart yearDataAll = this.listRFMDetailChartData(uid, 0, "month", 12, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(730, eTime));
		RFMDetailChart twoYearDataAll = this.listRFMDetailChartData(uid, 0, "month", 24, bTime, eTime);
		logger.info("RFMCHART55555555555555555555555555555555555");
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(6, eTime));
		RFMDetailChart servenDayDataNew = this.listRFMDetailChartData(uid, 1, "day", 7, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(14, eTime));
		RFMDetailChart fifteenDayDataNew = this.listRFMDetailChartData(uid, 1, "day", 15, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(29, eTime));
		RFMDetailChart thirtyDayDataNew = this.listRFMDetailChartData(uid, 1, "day", 30, bTime, eTime);
		logger.info("RFMCHART66666666666666666666666666666666666");
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(180, eTime));
		RFMDetailChart sixMonthDataNew = this.listRFMDetailChartData(uid, 1, "month", 6, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(365, eTime));
		RFMDetailChart yearDataNew = this.listRFMDetailChartData(uid, 1, "month", 12, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(730, eTime));
		RFMDetailChart twoYearDataNew = this.listRFMDetailChartData(uid, 1, "month", 24, bTime, eTime);

		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(6, eTime));
		RFMDetailChart servenDayDataOld = this.listRFMDetailChartData(uid, 2, "day", 7, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(14, eTime));
		RFMDetailChart fifteenDayDataOld = this.listRFMDetailChartData(uid, 2, "day", 15, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(29, eTime));
		RFMDetailChart thirtyDayDataOld = this.listRFMDetailChartData(uid, 2, "day", 30, bTime, eTime);

		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(180, eTime));
		RFMDetailChart sixMonthDataOld = this.listRFMDetailChartData(uid, 2, "month", 6, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(365, eTime));
		RFMDetailChart yearDataOld = this.listRFMDetailChartData(uid, 2, "month", 12, bTime, eTime);
		bTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(730, eTime));
		RFMDetailChart twoYearDataOld = this.listRFMDetailChartData(uid, 2, "month", 24, bTime, eTime);
		Long l2 = System.currentTimeMillis();
		logger.info("uid:" + uid + ",执行RFM详细分析数据计算完毕，耗时：" + (l2 - l1) + "ms,开始保存数据库");
		List<RFMDetailChart> rfmDetailCharts = new ArrayList<>();
		rfmDetailCharts.add(servenDayDataAll);
		rfmDetailCharts.add(servenDayDataNew);
		rfmDetailCharts.add(servenDayDataOld);
		rfmDetailCharts.add(fifteenDayDataAll);
		rfmDetailCharts.add(fifteenDayDataNew);
		rfmDetailCharts.add(fifteenDayDataOld);
		rfmDetailCharts.add(thirtyDayDataAll);
		rfmDetailCharts.add(thirtyDayDataNew);
		rfmDetailCharts.add(thirtyDayDataOld);
		rfmDetailCharts.add(sixMonthDataAll);
		rfmDetailCharts.add(sixMonthDataNew);
		rfmDetailCharts.add(sixMonthDataOld);
		rfmDetailCharts.add(yearDataAll);
		rfmDetailCharts.add(yearDataNew);
		rfmDetailCharts.add(yearDataOld);
		rfmDetailCharts.add(twoYearDataAll);
		rfmDetailCharts.add(twoYearDataNew);
		rfmDetailCharts.add(twoYearDataOld);
		return rfmDetailCharts;
	}

}
