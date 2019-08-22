package com.kycrm.member.service.synctrade.trade;

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
import com.kycrm.member.core.queue.MemberCalculateQueue;
import com.kycrm.member.core.queue.UpdateMemberQueueService;
import com.kycrm.member.dao.syntrade.member.IMemberDTODaosyn;
import com.kycrm.member.dao.syntrade.order.IOrderDTODaosyn;
import com.kycrm.member.dao.syntrade.trade.ITradeDTODaosyn;
import com.kycrm.member.dao.syntrade.trade.ITradeLostDaosyn;
import com.kycrm.member.domain.entity.effect.EffectStandardRFM;
import com.kycrm.member.domain.entity.effect.TradeCenterEffect;
import com.kycrm.member.domain.entity.member.TempEntity;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.entity.trade.TradeTempEntity;
import com.kycrm.member.domain.to.SimplePage;
import com.kycrm.member.domain.vo.trade.TradeMessageVO;
import com.kycrm.member.domain.vo.trade.TradeResultVO;
import com.kycrm.member.domain.vo.trade.TradeVO;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.trade.TradeErrorMessageService;
import com.kycrm.member.util.RedisDistributionLock;
import com.kycrm.member.util.thread.MyFixedThreadPool;
import com.kycrm.util.Constants;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.ValidateUtil;

/**
 * @author wy
 * @version 创建时间：2018年1月18日 下午2:36:49
 */
@MyDataSource
@Service
public class TradeDTOServiceImplsyn /*implements ITradeDTOService*/ {

	@Autowired
	private ITradeDTODaosyn tradeDao;

	@Autowired
	private ITradeLostDaosyn tradeLostDao;
	
	@Autowired
	private IOrderDTODaosyn orderDTODao;
	
	@Autowired
	private IMemberDTODaosyn memberDTODao;

	@Resource(name = "redisTemplateLock")
	private StringRedisTemplate redisTemplate;
	@Autowired
	private MemberCalculateQueue memberCalculateQueue;

	@Autowired
	private TradeErrorMessageService tradeErrorMessageService;

	private Logger logger = LoggerFactory.getLogger(TradeDTOServiceImplsyn.class);

	/**
	 * 根据用户主键id创建对应的主订单表
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
	
	public void batchInsertTradeList(Long uid, List<TradeDTO> tradeList) {
		if (ValidateUtil.isEmpty(uid) || ValidateUtil.isEmpty(tradeList)) {
			this.logger.debug("batchInsertTradeList方法的" + "uid = " + uid + ", tradeList = " + tradeList);
			return;
		}
		long startTime = System.currentTimeMillis();
		this.logger.debug("trade批量订单开始更新 uid:" + uid + " 数量：" + tradeList.size());
		// int size = tradeList.size();
		// if (size > Constants.TRADE_SAVE_MAX_LIMIT) {
		// List<List<TradeDTO>> splitList = JayCommonUtil.splitList(tradeList,
		// Constants.TRADE_SAVE_MAX_LIMIT);
		// for (List<TradeDTO> list : splitList) {
		// // this.batchLock(uid, list);
		// this.batchInsetTradeListByLimit(uid, list);
		// }
		// } else {
		// this.batchLock(uid, tradeList);
		memberCalculateQueue.putTradeListToQueue(tradeList);
		try {

			this.batchInsetTradeListByLimit(uid, tradeList);
			// this.saveBatchTradeDTOByList(uid, tradeList);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("**** trade批量保存出错 ", e);
		}

		tradeList.clear();
		// }
		this.logger.debug("**** trade全部更新完成  uid:{} 数量:{} 花费时间:{}ms", uid, tradeList.size(),
				System.currentTimeMillis() - startTime);
	}

	/**
	 * 加锁{由于效率太慢，去掉加锁操作}
	 * 
	 * @author: wy
	 * @time: 2018年2月28日 下午2:06:29
	 * @param uid
	 * @param tradeList
	 */
	private void batchLock(Long uid, List<TradeDTO> tradeList) {
		// RedisLock redisLock = new RedisLock(redisTemplate,
		// RedisConstant.RediskeyCacheGroup.TRADEDTO_TABLE_LOCKE_KEY + uid,
		// Constants.TRADE_SAVE_TIME_OUT,
		// Constants.TRADE_SAVE_EXPIRE_TIME);
		RedisDistributionLock redisDistributionLock = new RedisDistributionLock(redisTemplate);
		long lockTimeOut = 0L;
		try {
			lockTimeOut = redisDistributionLock.lock(RedisConstant.RediskeyCacheGroup.TRADEDTO_TABLE_LOCKE_KEY + uid);
			if (lockTimeOut > 0) {
				this.batchInsetTradeListByLimit(uid, tradeList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisDistributionLock.unlock(RedisConstant.RediskeyCacheGroup.TRADEDTO_TABLE_LOCKE_KEY + uid, lockTimeOut);
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
	private void batchInsetTradeListByLimit(Long uid, List<TradeDTO> tradeList) throws Exception {
		try {
			long startTime = System.currentTimeMillis();
			List<TradeDTO> newTradeList = new ArrayList<TradeDTO>(Constants.TRADE_SAVE_MAX_LIMIT / 2);
			List<TradeDTO> updateTradeList = new ArrayList<TradeDTO>(Constants.TRADE_SAVE_MAX_LIMIT);
			for (TradeDTO tradeDTO : tradeList) {
				String status = this.findTradeStatusByTid(uid, tradeDTO.getTid());
				if (ValidateUtil.isEmpty(status)) {
					newTradeList.add(tradeDTO);
				} else {
					updateTradeList.add(tradeDTO);
				}
			}
			long startTime2 = System.currentTimeMillis();
			logger.debug("trade 分拣新旧订单  uid:{}的 {}个订单，耗时{}ms", uid, tradeList.size(), startTime2 - startTime);
			if (newTradeList.size() > 0) {
				this.saveBatchTradeDTOByList(uid, newTradeList);
				// 将订单加入到阻塞队列中，再去修改会员的拍下订单数量和拍下商品数量
				UpdateMemberQueueService.NUM_QUEUE.put(newTradeList);
				logger.info("用户" + uid + "放入更新会员拍下数量的订单为" + newTradeList.size());
			}
			if (updateTradeList.size() > 0)
				this.updateBatchTradeDTOByList(uid, updateTradeList);
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception(e);
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
			logger.info("saveBatchTradeDTOByList方法的tradeList为空");
			return;
		}
		long startTime = System.currentTimeMillis();
		try {
			this.saveTradeDTOList(uid, tradeList);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("trade批量保存订单异常！！！改为轮询单个保存" + e.getMessage());
			long startTime2 = System.currentTimeMillis();
			tradeList.stream().forEach(tradeDTO -> this.saveTradeDTOBySingle(uid, tradeDTO));
			this.logger.error("trade保存异常，轮询单个订单保存耗时 ： {}ms", System.currentTimeMillis() - startTime2);
		}
		this.logger.info("trade insert保存订单  uid:{} 的  {}个订单，耗时  {}ms", uid, tradeList.size(),
				System.currentTimeMillis() - startTime);
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
			logger.info("updateBatchTradeDTOByList方法的tradeList为空");
			return;
		}
		long startTime = System.currentTimeMillis();
		try {
			this.doUpdateTradeDTOByList(uid, tradeList);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error("trade批量update订单异常！！！改为轮询单个更新" + e.getMessage());
			long startTime2 = System.currentTimeMillis();
			for (TradeDTO tradeDTO : tradeList) {
				this.doUpdateTradeDTOBySingle(uid, tradeDTO);
			}
			this.logger.error("trade update更新异常，轮询单个订单更新花费的时间为 ： " + (System.currentTimeMillis() - startTime2) + "ms");
		} finally {
			this.logger.debug("trade批量update订单花费时间为：  " + (System.currentTimeMillis() - startTime) + "ms");
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
	private void saveTradeDTOList(Long uid, List<TradeDTO> tradeDTOList) throws Exception {
		if (ValidateUtil.isEmpty(tradeDTOList)) {
			return;
		}
		// logger.debug("\n**##Dao前{}", JsonUtil.toJson(tradeDTOList));
		Map<String, Object> map = new HashMap<String, Object>(2);
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
			MyFixedThreadPool.getTradeAndOrderFixedThreadPool().execute(new Runnable() {
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
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("list", tradeDTOList);
		this.tradeDao.doUpdateTradeDTOByList_bak(map);
		// this.tradeDao.doUpdateTradeDTOByList(map);
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
			MyFixedThreadPool.getMemberAndReceiveDetailFixedThreadPool().execute(new Runnable() {
				@Override
				public void run() {
					tradeErrorMessageService.saveErrorMessage(e.getMessage(), TradeErrorMessageService.IS_TRADE,
							TradeErrorMessageService.UPDATE, JSON.toJSONString(tradeDTO));
				}
			});
		}
	}

	
	public TradeDTO findTradeDTOByTid(Long uid, Long tid) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("tid", tid);
		return tradeDao.findTradeDTOByTid(map);
	}


	public List<Double> listDaysPayment(Long uid, Date endTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		orderDTODao.alterTableOrder(map);
		memberDTODao.alterTableMember(map);
		tradeDao.alterTableTrade(map);
		return null;
	}

	
	public TradeCenterEffect sumEarningOrder(Long uid, List<String> tidList, List<String> statusList, Date beginTime,
			Date endTime) {
		return null;
	}


	public SimplePage queryMemberTradePage(Long uid, Map<String, Object> map) {
		return null;
	}

	
	public List<TradeResultVO> listMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey, String queryKey) {
		return null;
	}

	
	public Long countMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey, String queryKey) {
		return null;
	}

	
	public List<TradeDTO> listTradeByStatus(Long uid, TradeVO tradeVO, String sessionKey) {
		return null;
	}


	public List<TradeDTO> listNewTradeByPhones(Long uid, TradeVO tradeVO, String sessionKey) {
		return null;
	}

	
	public Integer findOneTradeDTOByTid(Long uid, String tid) {
		return null;
	}

	
	public List<TradeDTO> listTradeByTids(Long uid, List<String> tidList) {
		return null;
	}

	
	public List<TradeResultVO> exportTradeDTO(Long uid, String queryKey, String sessionKey) {
		return null;
	}

	
	public List<TradeDTO> listTradeByPhone(Long uid, List<String> phones, Date bTime, Date eTime) {
		return null;
	}


	public void updateTableIndex(Long uid) {
		if (ValidateUtil.isEmpty(uid)) {
			return;
		}
		String userId = String.valueOf(uid);
		List<String> tables = this.tradeDao.isExistsTable(userId);
		if (ValidateUtil.isNotNull(tables)) {
			logger.info("已存在 uid：{} 的trade_dto表格", userId);
			return;
		}
		this.tradeDao.updateTableIndex(uid);
	}

	
	public List<Long> getHasTradeButNotFoundOrderList(Long uid) throws Exception {
		return this.tradeLostDao.getHasTradeButNotFoundOrderList(uid);
	}

	
	public List<Long> getHasTradeButNotFoundMemberList(Long uid) throws Exception {
		return this.tradeLostDao.getHasTradeButNotFoundMemberList(uid);
	}

	
	public void deleteDirtyDataBySellerNick(Long uid, String sellerNick) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("uid", uid);
		paramMap.put("sellerNick", sellerNick);
		this.tradeDao.deleteDirtyDataBySellerNick(paramMap);
	}

	
	public List<TradeDTO> getDirtyData(Long uid, String sellerNick) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(2);
		paramMap.put("uid", uid);
		paramMap.put("sellerNick", sellerNick);
		return this.tradeDao.getDirtyData(paramMap);
	}

	
	public List<Long> getTidWhetherTradeNumIsNullOrEqualsZero(Long uid) throws Exception {
		Map<String, Object> paramMap = new HashMap<String, Object>(1);
		paramMap.put("uid", uid);
		return this.tradeDao.getTidWhetherTradeNumIsNullOrEqualsZero(paramMap);
	}

	
	public List<TempEntity> getAddNumberFromTradeTable(Long uid, Set<String> buyerNickList) throws Exception {
		return this.tradeDao.getAddNumberFromTradeTable(uid, buyerNickList);
	}

	
	public List<TempEntity> getBuyNumberFromTradeTable(Long uid, Set<String> buyerNickList) throws Exception {
		return this.tradeDao.getBuyNumberFromTradeTable(uid, buyerNickList);
	}


	public List<TempEntity> getFirstPayTimeFromTradeTable(Long uid, Set<String> buyerNickList) throws Exception {
		return this.tradeDao.getFirstPayTimeFromTradeTable(uid, buyerNickList);
	}

	public void doSendSms(Long uid, TradeMessageVO messageVO) {
	}

	
	public Long countMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey) {
		return null;
	}

	
	public List<TradeResultVO> listMarketingCenterOrder(Long uid, TradeVO tradeVO, String sessionKey) {
		return null;
	}

	
	public Map<String, Object> listRFMDetailChart(Long uid, String dateType, Integer days, Date bTime, Date eTime)
			throws Exception {
		return null;
	}

	
	public Long countMemberAmountByTimes(Long uid, Integer buyTimes, Date bTime, Date eTime) throws Exception {
		return null;
	}

	
	public Double sumPaidAmountByTimes(Long uid, Integer buyTimes, Date bTime, Date eTime) throws Exception {
		return null;
	}

	
	public List<TradeDTO> listEffectRFMByTime(Long uid, Date bTime, Date eTime) throws Exception {
		return null;
	}

	public List<EffectStandardRFM> listCustomerRFMs(Long uid, Date startNodeDate) throws Exception {
		return null;
	}

	
	public Long countUnPaidMember(Long uid) {
		return null;
	}

	
	public List<TradeDTO> listStepTradeByTids(Long uid, List<String> tids, Date bTime, Date eTime) {
		return null;
	}

	
	public Map<String, Object> sumPaymentByTids(Long uid, MsgSendRecord msgSendRecord) throws Exception {
		return null;
	}

	
	public List<TradeTempEntity> getTradeNumByBuyerNick(Long uid, Set<String> buyerNickSet) throws Exception {
		return this.tradeDao.getTradeNumByBuyerNick(uid, buyerNickSet);
	}

	
	public List<TradeTempEntity> getCloseTradeNum(Long uid, Set<String> buyerNickSet) throws Exception {
		return this.tradeDao.getCloseTradeNum(uid, buyerNickSet);
	}

	public List<TradeTempEntity> getAddNumber(Long uid, Set<String> buyerNickSet) throws Exception {
		return this.tradeDao.getAddNumber(uid, buyerNickSet);
	}

	
	public List<TradeTempEntity> getbuyerAlipayNo(Long uid, Set<String> buyerNickSet) throws Exception {
		return this.tradeDao.getBuyerAlipayNo(uid, buyerNickSet);
	}

	
	public List<TradeTempEntity> getReceiverInfoStr(Long uid, Set<String> buyerNickSet) throws Exception {
		return this.tradeDao.getReceiverInfoStr(uid, buyerNickSet);
	}
}
