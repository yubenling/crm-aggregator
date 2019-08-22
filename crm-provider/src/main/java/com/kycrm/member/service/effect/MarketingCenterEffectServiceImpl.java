package com.kycrm.member.service.effect;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.redis.RedisLockService;
import com.kycrm.member.dao.effect.IMarketingCenterEffectDao;
import com.kycrm.member.dao.message.IMsgTempTradeDao;
import com.kycrm.member.dao.message.IMsgTempTradeHistoryDao;
import com.kycrm.member.dao.trade.ITradeDTODao;
import com.kycrm.member.domain.entity.base.BaseListEntity;
import com.kycrm.member.domain.entity.effect.MarketingCenterEffect;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.MsgTempTrade;
import com.kycrm.member.domain.entity.trade.MsgTempTradeHistory;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.vo.effect.CustomerDetailVO;
import com.kycrm.member.domain.vo.effect.PayOrderEffectDetailVO;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.domain.vo.trade.TradeVO;
import com.kycrm.member.service.message.IMsgSendRecordService;
import com.kycrm.member.service.message.IMsgTempTradeHistoryService;
import com.kycrm.member.service.message.IMsgTempTradeService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.NumberUtils;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.TradesInfo;

@MyDataSource
@Service("marketingCenterEffectService")
public class MarketingCenterEffectServiceImpl implements IMarketingCenterEffectService {

	private Logger logger = LoggerFactory.getLogger(MarketingCenterEffectServiceImpl.class);

	@Autowired
	private IMarketingCenterEffectDao marketingCenterEffectDao;

	@Autowired
	private IMsgSendRecordService msgSendRecordService;

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;

	@Autowired
	private ITradeDTOService tradeDTOService;

	@Autowired
	private IMsgTempTradeDao msgTempTradeDao;

	@Autowired
	private ITradeDTODao tradeDTODao;

	@Autowired
	private IMsgTempTradeService tempTradeService;

	@Autowired
	private IMsgTempTradeHistoryDao tempTradeHistoryDao;

	@Autowired
	private RedisLockService lockService;

	@Autowired
	private IMsgTempTradeHistoryService tempTradeHistoryService;

	@Autowired
	private IItemDetailService itemDetailService;

	@Autowired
	private IOrderDTOService orderDTOService;

	/**
	 * 创建表
	 */
	public Boolean doCreateTable(Long uid) {
		if (uid == null) {
			return false;
		}
		List<String> tables = this.marketingCenterEffectDao.tableIsExist(uid);
		if (tables != null && !tables.isEmpty()) {
			return true;
		}
		try {
			this.marketingCenterEffectDao.doCreateTable(uid);
			this.marketingCenterEffectDao.addMarketingCenterEffectTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 保存一条记录
	 */
	@Override
	public void saveMarktingCenterEffect(Long uid, MarketingCenterEffect effect) {
		effect.setUid(uid);
		marketingCenterEffectDao.saveMarktingCenterEffect(effect);
	}

	/**
	 * 根据条件更新一条记录
	 */
	@Override
	public void updateMarktingEffectByParam(Long uid, MarketingCenterEffect effect) {
		effect.setUid(uid);
		marketingCenterEffectDao.updateMarktingEffectByParam(effect);
	}

	/**
	 * 根据userId、msgId、days查询统计分析数据
	 */
	@Override
	public MarketingCenterEffect findEffectByParam(Long uid, Long msgId, Integer days, String tradeFrom) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("msgId", msgId);
		map.put("days", days);
		map.put("tradeFrom", tradeFrom);
		MarketingCenterEffect marketingCenterEffect = marketingCenterEffectDao.findEffectByParam(map);
		return marketingCenterEffect;
	}

	/**
	 * 根据时间查询营销付款金额
	 */
	@Override
	public double sumPayFeeByTime(Long uid, Date bTime, Date eTime, Long msgId) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		map.put("msgId", msgId);
		Double payFee = marketingCenterEffectDao.findSuccessPayFeeByTime(map);
		return NumberUtils.getResult(payFee);
	}

	/**
	 * 首页查询每小时下单数
	 */
	@Override
	public List<TradeDTO> queryTradeNumByHour(Long uid, Date beginTime, Date endTime) {
		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("uid", uid);
		queryMap.put("bTime", beginTime);
		queryMap.put("eTime", endTime);
		List<TradeDTO> trades = tradeDTODao.queryTradeNumByHour(queryMap);
		return trades;
	}

	/**
	 * 营销中心效果分析汇总数据
	 */
	@Override
	public MarketingCenterEffect findEffectByDays(Long uid, Long msgId, String tradeFrom, Integer days) {
		if (uid == null && msgId == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("msgId", msgId);
		map.put("tradeFrom", tradeFrom);
		map.put("days", days);
		MarketingCenterEffect resultPicture = marketingCenterEffectDao.findEffectByDays(map);
		return resultPicture;
	}

	/**
	 * 营销中心效果分析汇总数据真实客户数据
	 */
	@Override
	public MarketingCenterEffect findRealBuyerNum(Long uid, Long msgId, int i, String orderSource) {
		MarketingCenterEffect effectPicture = marketingCenterEffectDao.findRealBuyerNum();
		return effectPicture;
	}

	/**
	 * 营销中心效果分析每日数据的集合
	 */
	@Override
	public List<MarketingCenterEffect> listEffectPicturesByDay(Long uid, MarketingCenterEffect effectPicture) {
		Map<String, Object> map = new HashMap<>();
		// TODO
		return marketingCenterEffectDao.listEffectPictures(map);
	}

	/**
	 * 根据发送总记录查询所有匹配的订单 @Title: queryTradeByMsgRecord @param @return
	 * 设定文件 @return List<TradeDTO> 返回类型 @throws
	 */
	@Override
	public List<TradeDTO> queryTradeByMsgRecord(Long uid, MsgSendRecord msgSendRecord, Date bTime, Date eTime) {
		List<TradeDTO> tradeList = new ArrayList<TradeDTO>();
		SmsRecordVO smsRecordVO = new SmsRecordVO();
		smsRecordVO.setUid(uid);
		smsRecordVO.setStatus(2);
		smsRecordVO.setType(msgSendRecord.getType());
		smsRecordVO.setMsgId(msgSendRecord.getId());
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
					List<TradeDTO> tradeDTOs = null;
					if (msgSendRecord.getStepType() == 2) {
						List<String> tids = smsRecordDTOService.queryTidList(uid, smsRecordVO);
						if (tids == null || tids.isEmpty()) {
							continue;
						}
						tradeDTOs = tradeDTOService.listStepTradeByTids(uid, tids, bTime, eTime);
					} else {
						List<String> pagePhoneList = smsRecordDTOService.queryPhoneList(uid, smsRecordVO);
						if (pagePhoneList == null || pagePhoneList.isEmpty()) {
							continue;
						}
						tradeDTOs = tradeDTOService.listTradeByPhone(uid, pagePhoneList, bTime, eTime);
					}
					tradeList.addAll(tradeDTOs);
				}
			} else {
				if (msgSendRecord.getStepType() == 2) {
					List<String> tids = smsRecordDTOService.queryTidList(uid, smsRecordVO);
					logger.info("msgId:" + msgSendRecord.getId() + "查询的手机号个数为：" + tids.size());
					if (tids == null || tids.isEmpty()) {
						logger.info("msgId:" + msgSendRecord.getId() + "查询的tids个数为：NUll");
						return null;
					}
					tradeList = tradeDTOService.listStepTradeByTids(uid, tids, bTime, eTime);
				} else {
					List<String> phoneList = smsRecordDTOService.queryPhoneList(uid, smsRecordVO);
					if (phoneList == null || phoneList.isEmpty()) {
						return null;
					}
					tradeList = tradeDTOService.listTradeByPhone(uid, phoneList, bTime, eTime);
				}
			}
		}
		return tradeList;
	}

	/**
	 * 根据成功发送手机号查询创建的订单 @Title: findTradeByPhones @param @return 设定文件 @return
	 * List<TradeDTO> 返回类型 @throws
	 */
	public List<TradeDTO> findTradeByPhones(MsgSendRecord msgSendRecord, Date startTime, Date endTime,
			List<String> phoneList) {
		if (phoneList != null && !phoneList.isEmpty()) {
			List<TradeDTO> tradeDTOList = new ArrayList<TradeDTO>();
			TradeVO tradeVO = new TradeVO();
			tradeVO.setUid(msgSendRecord.getUid());
			tradeVO.setMinCreatedTime(startTime);
			tradeVO.setMaxCreatedTime(endTime);
			if (phoneList.size() > Constants.PROCESS_PAGE_SIZE_OVER) {
				// TODO
			} else {
				tradeDTOList = tradeDTOService.listNewTradeByPhones(msgSendRecord.getUid(), tradeVO, null);
			}
			return tradeDTOList;
		}
		return null;
	}

	/**
	 * 插入或更新记录 @Title: saveOrUpdateEffect @param 设定文件 @return void 返回类型 @throws
	 */
	public void saveOrUpdateEffect(MarketingCenterEffect marketingCenterEffect) {
		if (marketingCenterEffect == null || marketingCenterEffect.getUid() == null) {
			return;
		}
		Long uid = marketingCenterEffect.getUid();
		Long msgId = marketingCenterEffect.getMsgId();
		Integer days = marketingCenterEffect.getDays();
		String tradeFrom = marketingCenterEffect.getTradeFrom();
		MarketingCenterEffect existEffect = this.findEffectByParam(uid, msgId, days, tradeFrom);
		if (existEffect != null) {
			// TODO
			this.updateMarktingEffectByParam(uid, marketingCenterEffect);
		} else {
			this.saveMarktingCenterEffect(uid, marketingCenterEffect);
		}
	}

	/**
	 * 处理发送总记录--营销效果分析
	 */
	@Override
	public void handleData(Long uid, MsgSendRecord msgRecord) {
		if (uid == null || msgRecord == null) {
			return;
		}
		long l1 = System.currentTimeMillis();
		Boolean createStatus = this.doCreateTable(uid);
		if (false == createStatus) {
			return;
		}
		// 封装条件，筛选msgId的tempTrade
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("msgId", msgRecord.getId());
		List<MsgTempTradeHistory> tempTrades = tempTradeHistoryDao.listTempTradeByMsgId(map);
		if (tempTrades == null || tempTrades.isEmpty()) {
			return;
		}
		for (int i = 0; i < 15; i++) {
			Date bTime = msgRecord.getSendCreat();
			Date eTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAfter(i, bTime));
			// 创建最终结果对象,WAP,TAOBAO,TOTAL
			MarketingCenterEffect effectWAP = new MarketingCenterEffect(uid, TradesInfo.ORDER_FROM_WAP, eTime, i + 1,
					msgRecord.getId(), msgRecord.getSendCreat());
			MarketingCenterEffect effectTB = new MarketingCenterEffect(uid, TradesInfo.ORDER_FROM_TAOBAO, eTime, i + 1,
					msgRecord.getId(), msgRecord.getSendCreat());
			MarketingCenterEffect effectALL = new MarketingCenterEffect(uid, TradesInfo.ORDER_FROM_TOTAL, eTime, i + 1,
					msgRecord.getId(), msgRecord.getSendCreat());
			effectWAP.setUid(uid);
			effectTB.setUid(uid);
			effectALL.setUid(uid);
			// 定义TOTAL数据
			Set<Long> createTidSet = new HashSet<>(), waitPayTidSet = new HashSet<>(), payTidSet = new HashSet<>(),
					refundTidSet = new HashSet<>();
			Set<String> createBuyerSet = new HashSet<>(), waitBuyerTidSet = new HashSet<>(),
					payBuyerSet = new HashSet<>(), refundBuyerSet = new HashSet<>();
			BigDecimal createFee = new BigDecimal(0.00), waitPayFee = new BigDecimal(0.00),
					payFee = new BigDecimal(0.00), refundFee = new BigDecimal(0.00);
			Long createItemNum = 0L, waitPayItemNum = 0L, payItemNum = 0L, refundItemNum = 0L;

			List<MsgTempTradeHistory> wapTempTrade = new ArrayList<>();
			List<MsgTempTradeHistory> taobaoTempTrade = new ArrayList<>();
			// 开始处理数据
			for (MsgTempTradeHistory tempTrade : tempTrades) {
				if (tempTrade != null && tempTrade.getTradeFrom() != null) {
					long tradeCreatedLong = tempTrade.getCreated().getTime();
					long bTimeLong = bTime.getTime();
					long eTimeLong = eTime.getTime();
					if (tradeCreatedLong > bTimeLong && tradeCreatedLong < eTimeLong) {
						if (tempTrade.getTradeFrom().contains("WAP")) {
							wapTempTrade.add(tempTrade);
						} else if (tempTrade.getTradeFrom().contains("TAOBAO")) {
							taobaoTempTrade.add(tempTrade);
						}
						if ("create".equals(tempTrade.getTradeStatus())) {
							createTidSet.add(tempTrade.getTid());
							createBuyerSet.add(tempTrade.getBuyerNick());
							createItemNum += NumberUtils.getResult(tempTrade.getItemNum());
							createFee = createFee.add(NumberUtils.getResult(tempTrade.getPayment()));
						} else if ("waitPay".equals(tempTrade.getTradeStatus())) {
							waitPayTidSet.add(tempTrade.getTid());
							waitBuyerTidSet.add(tempTrade.getBuyerNick());
							waitPayItemNum += NumberUtils.getResult(tempTrade.getItemNum());
							waitPayFee = waitPayFee.add(NumberUtils.getResult(tempTrade.getPayment()));
						} else if ("success".equals(tempTrade.getTradeStatus())) {
							payTidSet.add(tempTrade.getTid());
							payBuyerSet.add(tempTrade.getBuyerNick());
							payItemNum += NumberUtils.getResult(tempTrade.getItemNum());
							payFee = payFee.add(NumberUtils.getResult(tempTrade.getPayment()));
						} else if ("refund".equals(tempTrade.getTradeStatus())) {
							refundTidSet.add(tempTrade.getTid());
							refundBuyerSet.add(tempTrade.getBuyerNick());
							refundItemNum += NumberUtils.getResult(tempTrade.getItemNum());
							refundFee = refundFee.add(NumberUtils.getResult(tempTrade.getPayment()));
						}
					}

				}
			}
			effectALL.setCreateAmount(createFee);
			effectALL.setCreateBuyerNum(createBuyerSet.size());
			effectALL.setCreateItemNum(createItemNum);
			effectALL.setCreateTradeNum(createTidSet.size());
			effectALL.setWaitPayAmount(waitPayFee);
			effectALL.setWaitPayBuyerNum(waitBuyerTidSet.size());
			effectALL.setWaitPayItemNum(waitPayItemNum);
			effectALL.setWaitPayTradeNum(waitPayTidSet.size());
			effectALL.setPayAmount(payFee);
			effectALL.setPayBuyerNum(payBuyerSet.size());
			effectALL.setPayItemNum(payItemNum);
			effectALL.setPayTradeNum(payTidSet.size());
			effectALL.setRefundAmount(refundFee);
			effectALL.setRefundBuyerNum(refundBuyerSet.size());
			effectALL.setRefundItemNum(refundItemNum);
			effectALL.setRefundTradeNum(refundTidSet.size());
			effectALL.setCreatedBy(uid + "");
			effectALL.setCreatedDate(new Date());
			effectALL.setLastModifiedBy(uid + "");
			effectALL.setLastModifiedDate(new Date());
			this.checkTradeFrom(wapTempTrade, effectWAP);
			this.checkTradeFrom(taobaoTempTrade, effectTB);
			this.saveOrUpdateEffect(effectWAP);
			this.saveOrUpdateEffect(effectTB);
			this.saveOrUpdateEffect(effectALL);
		}
		this.cacheService.putNoTime(RedisConstant.RediskeyCacheGroup.MSG_IS_HISTORY_DATA, "msgId-" + msgRecord.getId(),
				true, false);
		long l2 = System.currentTimeMillis();
		// 删除msgTemptrade中的数据
		// tempTradeService.deleteFifteenAgoTrade(msgRecord.getUid(),
		// DateUtils.getTimeByDay(new Date(), -16));
		// 删除itemTemptrade中的数据
		// itemDetailService.deleteItemTempTrade(msgRecord.getUid(),
		// DateUtils.getTimeByDay(new Date(), -16));
		if ((l2 - l1) > 10000) {
			logger.info("+_+	+_++_++_++_++_++_++_++_++_++_++_++_+" + msgRecord.getId() + "的查询时间超过10s " + (l2 - l1)
					+ "ms");
		}
	}

	public void checkTradeFrom(List<MsgTempTradeHistory> tempTrades, MarketingCenterEffect effect) {
		Set<Long> createTidSet = new HashSet<>(), waitPayTidSet = new HashSet<>(), payTidSet = new HashSet<>(),
				refundTidSet = new HashSet<>();
		Set<String> createBuyerSet = new HashSet<>(), waitBuyerTidSet = new HashSet<>(), payBuyerSet = new HashSet<>(),
				refundBuyerSet = new HashSet<>();
		BigDecimal createFee = new BigDecimal(0.00), waitPayFee = new BigDecimal(0.00), payFee = new BigDecimal(0.00),
				refundFee = new BigDecimal(0.00);
		Long createItemNum = 0L, waitPayItemNum = 0L, payItemNum = 0L, refundItemNum = 0L;
		for (MsgTempTradeHistory tempTrade : tempTrades) {
			if (tempTrade != null) {
				if ("create".equals(tempTrade.getTradeStatus())) {
					createTidSet.add(tempTrade.getTid());
					createBuyerSet.add(tempTrade.getBuyerNick());
					createItemNum += NumberUtils.getResult(tempTrade.getItemNum());
					createFee = createFee.add(NumberUtils.getResult(tempTrade.getPayment()));
				} else if ("waitPay".equals(tempTrade.getTradeStatus())) {
					waitPayTidSet.add(tempTrade.getTid());
					waitBuyerTidSet.add(tempTrade.getBuyerNick());
					waitPayItemNum += NumberUtils.getResult(tempTrade.getItemNum());
					waitPayFee = waitPayFee.add(NumberUtils.getResult(tempTrade.getPayment()));
				} else if ("success".equals(tempTrade.getTradeStatus())) {
					payTidSet.add(tempTrade.getTid());
					payBuyerSet.add(tempTrade.getBuyerNick());
					payItemNum += NumberUtils.getResult(tempTrade.getItemNum());
					payFee = payFee.add(NumberUtils.getResult(tempTrade.getPayment()));
				} else if ("refund".equals(tempTrade.getTradeStatus())) {
					refundTidSet.add(tempTrade.getTid());
					refundBuyerSet.add(tempTrade.getBuyerNick());
					refundItemNum += NumberUtils.getResult(tempTrade.getItemNum());
					refundFee = refundFee.add(NumberUtils.getResult(tempTrade.getPayment()));
				}
			}
		}
		effect.setCreateAmount(createFee);
		effect.setCreateBuyerNum(createBuyerSet.size());
		effect.setCreateItemNum(createItemNum);
		effect.setCreateTradeNum(createTidSet.size());
		effect.setWaitPayAmount(waitPayFee);
		effect.setWaitPayBuyerNum(waitBuyerTidSet.size());
		effect.setWaitPayItemNum(waitPayItemNum);
		effect.setWaitPayTradeNum(waitPayTidSet.size());
		effect.setPayAmount(payFee);
		effect.setPayBuyerNum(payBuyerSet.size());
		effect.setPayItemNum(payItemNum);
		effect.setPayTradeNum(payTidSet.size());
		effect.setRefundAmount(refundFee);
		effect.setRefundBuyerNum(refundBuyerSet.size());
		effect.setRefundItemNum(refundItemNum);
		effect.setRefundTradeNum(refundTidSet.size());
		effect.setCreatedBy(effect.getUid() + "");
		effect.setCreatedDate(new Date());
		effect.setLastModifiedBy(effect.getUid() + "");
		effect.setLastModifiedDate(new Date());
	}

	/**
	 * 效果分析主页面的计算
	 */
	@Override
	public MarketingCenterEffect sumMarketingCenterEffect(Long uid, MsgSendRecord msgRecord, String tradeFrom,
			Date bTime, Date eTime) throws Exception {
		MarketingCenterEffect resultData = new MarketingCenterEffect();

		if (msgRecord == null || msgRecord.getSendCreat() == null) {
			return null;
		}
		if (bTime == null || eTime == null) {
			throw new RuntimeException("时间参数为空，请重新操作或登陆");
		}
		Boolean isHistory = this.cacheService.get(RedisConstant.RediskeyCacheGroup.MSG_IS_HISTORY_DATA,
				"msgId-" + msgRecord.getId(), Boolean.class);
		if (isHistory == null) {
			isHistory = false;
		}
		CustomerDetailVO cacheParam = new CustomerDetailVO();
		cacheParam.setMsgId(msgRecord.getId());
		cacheParam.setbTime(bTime);
		cacheParam.seteTime(eTime);
		cacheParam.setSendCreated(msgRecord.getSendCreat());
		this.doCreateTable(uid);
		lockService.putStringValueWithExpireTime(uid + "-effectParam", JsonUtil.toJson(cacheParam), TimeUnit.HOURS, 1L);
		if (isHistory) {
			resultData = this.findEffectByDays(uid, msgRecord.getId(), tradeFrom,
					DateUtils.getDiffDay(msgRecord.getSendCreat(), eTime).intValue() + 1);
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("uid", uid);
			map.put("msgId", msgRecord.getId());
			map.put("tradeFrom", tradeFrom);
			map.put("bTime", bTime);
			map.put("eTime", eTime);
			map.put("tradeStatus", "create");
			map.put("createType", "created");
			map.put("waitType", null);
			map.put("payType", null);
			map.put("refundType", null);
			if ("TOTAL" == tradeFrom || "TOTAL".equals(tradeFrom)) {
				map.put("tradeFrom", null);
			}
			List<String> tables = msgTempTradeDao.tableIsExist(uid);
			if (tables != null && !tables.isEmpty()) {
			} else {
				msgTempTradeDao.doCreateTable(uid);
			}
			MsgTempTrade createData = msgTempTradeDao.aggregateDataByStatus(map);
			lockService.putStringValueWithExpireTime(uid + "-effectMap", JsonUtil.toJson(map), TimeUnit.HOURS, 1L);
			if (createData != null) {
				String buyerNickStr = createData.getBuyerNick();
				if (buyerNickStr != null && !"".equals(buyerNickStr)) {
					Integer buyerCount = Integer.parseInt(buyerNickStr);
					resultData.setCreateBuyerNum(buyerCount);
				}
				Integer tidCount = createData.getTid() == null ? 0 : createData.getTid().intValue();
				Long itemNum = createData.getItemNum();
				BigDecimal payment = createData.getPayment();
				resultData.setCreateTradeNum(tidCount);
				resultData.setCreateItemNum(itemNum);
				resultData.setCreateAmount(payment);
			}
			map.put("tradeStatus", "success");
			map.put("createType", null);
			map.put("waitType", null);
			map.put("payType", "pay");
			map.put("refundType", null);
			MsgTempTrade successData = msgTempTradeDao.aggregateDataByStatus(map);
			if (successData != null) {
				String buyerNickStr = successData.getBuyerNick();
				if (buyerNickStr != null && !"".equals(buyerNickStr)) {
					resultData.setPayBuyerNum(Integer.parseInt(buyerNickStr));
				}
				Integer tidCount = successData.getTid() == null ? 0 : successData.getTid().intValue();
				Long itemNum = successData.getItemNum();
				BigDecimal payment = successData.getPayment();
				resultData.setPayTradeNum(tidCount);
				resultData.setPayItemNum(itemNum);
				resultData.setPayAmount(payment);
			}
			map.put("tradeStatus", "waitPay");
			map.put("createType", null);
			map.put("waitType", "wait");
			map.put("payType", null);
			map.put("refundType", null);
			MsgTempTrade waitPayData = msgTempTradeDao.aggregateDataByStatus(map);
			if (waitPayData != null) {
				String buyerNickStr = waitPayData.getBuyerNick();
				if (buyerNickStr != null && !"".equals(buyerNickStr)) {
					resultData.setWaitPayBuyerNum(Integer.parseInt(buyerNickStr));
				}
				Integer tidCount = waitPayData.getTid() == null ? 0 : waitPayData.getTid().intValue();
				Long itemNum = waitPayData.getItemNum();
				BigDecimal payment = waitPayData.getPayment();
				resultData.setWaitPayTradeNum(tidCount);
				resultData.setWaitPayItemNum(itemNum);
				resultData.setWaitPayAmount(payment);
			}
			map.put("tradeStatus", "refund");
			map.put("createType", null);
			map.put("waitType", null);
			map.put("payType", null);
			map.put("refundType", "refund");
			MsgTempTrade refundData = msgTempTradeDao.aggregateDataByStatus(map);
			if (refundData != null) {
				String buyerNickStr = refundData.getBuyerNick();
				if (buyerNickStr != null && !"".equals(buyerNickStr)) {
					resultData.setRefundBuyerNum(Integer.parseInt(buyerNickStr));
				}
				Integer tidCount = refundData.getTid() == null ? 0 : refundData.getTid().intValue();
				Long itemNum = refundData.getItemNum();
				BigDecimal payment = refundData.getPayment();
				resultData.setRefundTradeNum(tidCount);
				resultData.setRefundItemNum(itemNum);
				resultData.setRefundAmount(payment);
			}
		}
		return resultData;
	}

	/**
	 * 计算用户选择时间段内每天成交数据
	 */
	@Override
	public List<PayOrderEffectDetailVO> listPayData(Long uid, Long msgId, String tradeFrom, Date bTime, Date eTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("msgId", msgId);
		map.put("tradeFrom", tradeFrom);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		map.put("tradeStatus", "success");
		if ("TOTAL" == tradeFrom || "TOTAL".equals(tradeFrom)) {
			map.put("tradeFrom", null);
		}
		Boolean isHistory = this.cacheService.get(RedisConstant.RediskeyCacheGroup.MSG_IS_HISTORY_DATA,
				"msgId-" + msgId, Boolean.class);
		if (isHistory == null) {
			isHistory = false;
		}
		List<Map<String, Object>> payDataList = null;
		if (isHistory) {
			payDataList = tempTradeHistoryDao.listPayData(map);
		} else {
			payDataList = msgTempTradeDao.listPayData(map);
		}
		List<PayOrderEffectDetailVO> resultVOList = null;
		if (payDataList != null && !payDataList.isEmpty()) {
			resultVOList = new ArrayList<>();
			for (Map<String, Object> dataMap : payDataList) {
				if (dataMap != null) {
					PayOrderEffectDetailVO resultVO = new PayOrderEffectDetailVO();
					try {
						BeanUtils.populate(resultVO, dataMap);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					resultVOList.add(resultVO);
				}
			}
		}
		return resultVOList;
	}

	/**
	 * synchSaveMsgTempTrade(每小时将符合最近15天发送记录的订单存放到MsgTempTrade中) @Title:
	 * synchSaveMsgTempTrade @param 设定文件 @return void 返回类型 @throws
	 */
	@Override
	public void synchSaveMsgTempTrade(Long uid, List<TradeDTO> tradeByMsg, MsgSendRecord msg) throws Exception {
		if (tradeByMsg == null || tradeByMsg.isEmpty()) {
			logger.info("效果分析同步临时订单库，符合发送记录的订单为空~~~~~~！~~~~~~");
			return;
		}
		logger.info("效果分析同步临时订单库，符合发送记录的订单:uid:" + uid + "个数为：" + tradeByMsg.size());
		Boolean createStatus = tempTradeService.doCreateTable(uid);
		if (false == createStatus) {
			return;
		}
		Boolean createTable = tempTradeHistoryService.doCreateTable(uid);
		if (false == createTable) {
			return;
		}
		List<MsgTempTrade> tempTrades = new ArrayList<>();
		MsgTempTrade tempTrade = new MsgTempTrade();
		for (TradeDTO trade : tradeByMsg) {
			if (trade.getCreated().getTime() < msg.getSendCreat().getTime()) {
				continue;
			}
			tempTrade.setBuyerNick(trade.getBuyerNick());
			tempTrade.setCreated(trade.getCreated());
			tempTrade.setEndTime(trade.getEndTime() == null ? null : trade.getEndTime());
			tempTrade.setModifiedTime(trade.getModified());
			tempTrade.setMsgCreated(msg.getSendCreat());
			tempTrade.setMsgId(msg.getId());
			// 拼接商品字符串，","拼接
			tempTrade.setItemNum(trade.getNum());
			tempTrade.setPayment(trade.getPayment());
			tempTrade.setPayTime(trade.getPayTime());
			tempTrade.setReceiverAddress(trade.getReceiverAddress());
			tempTrade.setReceiverMobile(trade.getReceiverMobile());
			tempTrade.setReceiverName(trade.getReceiverName());
			tempTrade.setRefundFlag(trade.getRefundFlag());
			tempTrade.setTid(trade.getTid());
			tempTrade.setTradeFrom(trade.getTradeFrom());
			tempTrade.setFront(trade.getFront());
			tempTrade.setTail(trade.getTail());
			tempTrade.setStepTradeStatus(trade.getStepTradeStatus());
			// 判断订单状态
			List<String> payStatusList = new ArrayList<String>();
			payStatusList.add(TradesInfo.TRADE_FINISHED);
			payStatusList.add(TradesInfo.WAIT_SELLER_SEND_GOODS);
			payStatusList.add(TradesInfo.TRADE_CLOSED);
			payStatusList.add(TradesInfo.WAIT_BUYER_CONFIRM_GOODS);
			payStatusList.add(TradesInfo.TRADE_BUYER_SIGNED);
			List<String> waitStatusList = new ArrayList<String>();
			waitStatusList.add(TradesInfo.WAIT_BUYER_PAY);
			waitStatusList.add(TradesInfo.TRADE_CLOSED_BY_TAOBAO);
			Map<String, Object> map = new HashMap<>();
			map.put("uid", uid);
			map.put("msgId", msg.getId());
			map.put("tid", trade.getTid());
			logger.info("msgId:" + msg.getId() + "的定时数据，订单号：" + trade.getTid() + "未发生退款。");
			if (trade.getRefundFlag() != null && true == trade.getRefundFlag()) {
				List<OrderDTO> refundOrderList = orderDTOService.listRefundStatusByTid(uid, trade.getTid());
				logger.info("msgId:" + msg.getId() + "的定时数据，订单号：" + trade.getTid() + "发生退款，refundOrderList。size:"
						+ refundOrderList.size());
				if (refundOrderList != null && !refundOrderList.isEmpty()) {
					BigDecimal refundPayment = new BigDecimal(0);
					Long refundNum = 0L;
					for (OrderDTO orderDTO : refundOrderList) {
						if (orderDTO.getTbrefundStatus() != null && !"".equals(orderDTO.getTbrefundStatus())
								&& "SUCCESS".equals(orderDTO.getTbrefundStatus())) {
							refundNum += orderDTO.getNum();
							refundPayment = NumberUtils.add(refundPayment, orderDTO.getPayment());
						}
					}
					tempTrade.setTradeStatus("refund");
					map.put("tradeStatus", "refund");
					List<Long> refundIds = msgTempTradeDao.isExistMsgTrade(map);
					if (refundIds == null || refundIds.isEmpty()) {
						MsgTempTrade refundTempTrade = createNewRefundTempTrade(tempTrade, refundPayment, refundNum);
						MsgTempTrade refundTemp = this.createMsgTempTrade(trade.getUid(), refundTempTrade, "refund");
						tempTrades.add(refundTemp);
						createFinishedTempTrade(trade, tempTrade, map, tempTrades);
					}
				} else {
					createFinishedTempTrade(trade, tempTrade, map, tempTrades);
				}
			} else {
				if (payStatusList.contains(trade.getStatus())) {
					createFinishedTempTrade(trade, tempTrade, map, tempTrades);
				} else if (waitStatusList.contains(trade.getStatus())) {
					createWaitPayTempTrade(trade, tempTrade, map, tempTrades);
				} else {
					tempTrade.setTradeStatus("create");
					tempTrade.setUid(uid);
					tempTrades.add(tempTrade);
				}
			}
		}
		System.err.println("msgTempTrade总个数：" + tempTrades.size() + "个");
		int totalSize = tempTrades.size();
		int pageSize = ConstantUtils.PROCESS_PAGE_SIZE_MAX.intValue();
		int start = 0, end = 0;
		if (totalSize % pageSize == 0) {
			end = totalSize / pageSize;
		} else {
			end = (totalSize + pageSize) / pageSize;
		}
		BaseListEntity<MsgTempTrade> entityList = null;
		Date nowDate = new Date();
		BigDecimal successPayment = new BigDecimal(0);
		while (start < end) {
			entityList = new BaseListEntity<>();
			if (start == (end - 1)) {
				entityList.setEntityList(tempTrades.subList(start * pageSize, tempTrades.size()));
			} else {
				entityList.setEntityList(tempTrades.subList(start * pageSize, (start + 1) * pageSize));
			}
			entityList.setUid(uid);
			if (DateUtils.getDiffDay(msg.getSendCreat(), nowDate) < 15) {
				msgTempTradeDao.saveMsgTempTradeList(entityList);
				tempTradeHistoryDao.saveMsgTempTradeHistoryList(entityList);
			} else {
				tempTradeHistoryDao.saveMsgTempTradeHistoryList(entityList);
			}
			for (int i = 0; i < tempTrades.size(); i++) {
				successPayment.add(tempTrades.get(i).getPayment());
			}
			start++;
		}
		// 在短信总记录更新存储该对应批次短信实际产生的付款金额
		// 在效果分析时计算ROI使用
		this.msgSendRecordService.updateROIById(msg.getId(), successPayment, msg.getQueryParamId().toString());
	}

	/**
	 * createWaitPayTempTrade(保存状态为交易成功的临时订单) @Title:
	 * createWaitPayTempTrade @param @param trade @param @param
	 * tempTrade @param @param map @param @param tempTrades 设定文件 @return void
	 * 返回类型 @throws
	 */
	public void createWaitPayTempTrade(TradeDTO trade, MsgTempTrade tempTrade, Map<String, Object> map,
			List<MsgTempTrade> tempTrades) {
		tempTrade.setTradeStatus("waitPay");
		map.put("tradeStatus", "waitPay");
		List<Long> waitPayIds = msgTempTradeDao.isExistMsgTrade(map);
		if (waitPayIds == null || waitPayIds.isEmpty()) {
			MsgTempTrade waitPayTempTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "waitPay");
			tempTrades.add(waitPayTempTrade);
			map.put("tradeStatus", "create");
			List<Long> createIds = msgTempTradeDao.isExistMsgTrade(map);
			if (createIds == null || createIds.isEmpty()) {
				MsgTempTrade createTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "create");
				tempTrades.add(createTrade);
			}
		}
	}

	/**
	 * createFinishedTempTrade(保存状态为交易成功的临时订单) @Title:
	 * createFinishedTempTrade @param @param trade @param @param
	 * tempTrade @param @param map @param @param tempTrades 设定文件 @return void
	 * 返回类型 @throws
	 */
	public void createFinishedTempTrade(TradeDTO trade, MsgTempTrade tempTrade, Map<String, Object> map,
			List<MsgTempTrade> tempTrades) {
		tempTrade.setTradeStatus("success");
		map.put("tradeStatus", "success");
		List<Long> payIds = msgTempTradeDao.isExistMsgTrade(map);
		if (payIds == null || payIds.isEmpty()) {
			MsgTempTrade successTempTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "success");
			tempTrades.add(successTempTrade);
			map.put("tradeStatus", "waitPay");
			List<Long> waitPayIds = msgTempTradeDao.isExistMsgTrade(map);
			if (waitPayIds == null || waitPayIds.isEmpty()) {
				MsgTempTrade waitPayTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "waitPay");
				tempTrades.add(waitPayTrade);
				map.put("tradeStatus", "create");
				List<Long> createIds = msgTempTradeDao.isExistMsgTrade(map);
				if (createIds == null || createIds.isEmpty()) {
					MsgTempTrade createTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "create");
					tempTrades.add(createTrade);
				}
			}
		}
	}

	/**
	 * 创建一个对象
	 */
	public MsgTempTrade createMsgTempTrade(Long uid, MsgTempTrade tempTrade, String status) {
		if (tempTrade == null) {
			return null;
		}
		MsgTempTrade resultTrade = new MsgTempTrade();
		resultTrade.setBuyerNick(tempTrade.getBuyerNick());
		resultTrade.setCreated(tempTrade.getCreated());
		resultTrade.setEndTime(tempTrade.getEndTime());
		resultTrade.setItemNum(tempTrade.getItemNum());
		resultTrade.setPayTime(tempTrade.getPayTime());
		resultTrade.setModifiedTime(new Date());
		resultTrade.setMsgCreated(tempTrade.getMsgCreated());
		resultTrade.setMsgId(tempTrade.getMsgId());
		resultTrade.setNumIidStr(tempTrade.getNumIidStr());
		resultTrade.setPayment(tempTrade.getPayment());
		resultTrade.setReceiverAddress(tempTrade.getReceiverAddress());
		resultTrade.setReceiverMobile(tempTrade.getReceiverMobile());
		resultTrade.setReceiverName(tempTrade.getReceiverName());
		resultTrade.setRefundFlag(tempTrade.getRefundFlag());
		resultTrade.setTid(tempTrade.getTid());
		resultTrade.setTradeFrom(tempTrade.getTradeFrom());
		resultTrade.setTradeStatus(status);
		resultTrade.setFront(tempTrade.getFront());
		resultTrade.setTail(tempTrade.getTail());
		resultTrade.setStepTradeStatus(tempTrade.getStepTradeStatus());
		resultTrade.setUid(tempTrade.getUid());
		resultTrade.setFrontPayTime(tempTrade.getFrontPayTime());
		return resultTrade;
	}

	@Override
	public List<Long> findAllMsgIdByTime(Long uid, Date fifteenAgoTime) {
		if (uid == null || fifteenAgoTime == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("fifteenAgoTime", fifteenAgoTime);
		List<Long> msgIds = this.msgTempTradeDao.findMaxIdByTime(map);
		if (msgIds == null || msgIds.isEmpty()) {
			return null;
		}
		return msgIds;
	}

	/**
	 * 同步订单到临时订单表用于实时计算营销中心效果分析
	 */
	@Override
	public void sychnMarketingData(Long uid, MsgSendRecord msgRecord, Date bTime, Date eTime) throws Exception {
		logger.info("bTime:" + DateUtils.dateToStringHMS(bTime) + ",eTime:" + DateUtils.dateToStringHMS(eTime));
		if (uid == null || msgRecord == null) {
			logger.info("用户：" + uid + "为null会MsgSendRRecord为null，msgRecord：{}", JsonUtil.toJson(msgRecord));
			return;
		}
		// logger.info("用户：" + uid +
		// "不为null会MsgSendRRecord不为null，msgRecord：{}",JsonUtil.toJson(msgRecord));
		// List<TradeDTO> tradeByMsgRecord = this.queryTradeByMsgRecord(uid,
		// msgRecord, bTime, eTime);
		// if(tradeByMsgRecord == null || tradeByMsgRecord.isEmpty()){
		// logger.info("用户：" + uid + "msg_id为：" + msgRecord.getId() +
		// "查询的订单为NULL");
		// return;
		// }
		if (msgRecord.getStepType() == 2) {
			logger.info("用户：" + uid + "不为null会MsgSendRRecord不为null，msgRecord：{}", JsonUtil.toJson(msgRecord));
			List<TradeDTO> tradeByMsgRecord = this.queryTradeByMsgRecord(uid, msgRecord, null, null);
			if (tradeByMsgRecord == null || tradeByMsgRecord.isEmpty()) {
				logger.info("用户：" + uid + "msg_id为：" + msgRecord.getId() + "查询的订单为NULL");
				return;
			}
			logger.info("订单保存临时订单表的job===》用户：" + uid + "发送记录id：" + msgRecord.getId() + "查询出的订单个数为:"
					+ tradeByMsgRecord.size());
			this.synchSaveStepMsgTempTrade(uid, tradeByMsgRecord, msgRecord);
			logger.info("订单保存临时订单表的job===》用户：" + uid + "发送记录id：" + msgRecord.getId() + "保存tempTrade完毕-_-_-_-_-_-_-_");
			this.itemDetailService.saveStepItemDetail(uid, msgRecord, tradeByMsgRecord);
			logger.info(
					"订单保存临时订单表的job===》用户：" + uid + "发送记录id：" + msgRecord.getId() + "保存itemTempTrade完毕-_-_-_-_-_-_-_");
		} else {
			logger.info("用户：" + uid + "不为null会MsgSendRRecord不为null，msgRecord：{}", JsonUtil.toJson(msgRecord));
			List<TradeDTO> tradeByMsgRecord = this.queryTradeByMsgRecord(uid, msgRecord, bTime, eTime);
			if (tradeByMsgRecord == null || tradeByMsgRecord.isEmpty()) {
				logger.info("用户：" + uid + "msg_id为：" + msgRecord.getId() + "查询的订单为NULL");
				return;
			}
			logger.info("订单保存临时订单表的job===》用户：" + uid + "发送记录id：" + msgRecord.getId() + "查询出的订单个数为:"
					+ tradeByMsgRecord.size());
			this.synchSaveMsgTempTrade(uid, tradeByMsgRecord, msgRecord);
			logger.info("订单保存临时订单表的job===》用户：" + uid + "发送记录id：" + msgRecord.getId() + "保存tempTrade完毕-_-_-_-_-_-_-_");
			this.itemDetailService.saveItemDetail(uid, msgRecord, tradeByMsgRecord);
			logger.info(
					"订单保存临时订单表的job===》用户：" + uid + "发送记录id：" + msgRecord.getId() + "保存itemTempTrade完毕-_-_-_-_-_-_-_");
		}
	}

	/**
	 * 一次性定时任务，同步历史订单数据到临时订单表
	 */
	@Override
	public void singleSynchEffectData(Long uid, MsgSendRecord msgRecord, Date bTime, Date eTime) throws Exception {
		if (uid == null || msgRecord == null) {
			return;
		}
		List<TradeDTO> tradeDTOs = this.queryTradeByMsgRecord(uid, msgRecord, bTime, eTime);
		if (tradeDTOs == null || tradeDTOs.isEmpty()) {
			return;
		}
		logger.info("一次性同步历史效果分析数据的job===》用户：" + uid + "发送记录id：" + msgRecord.getId() + "查询出的订单个数为:" + tradeDTOs.size());
		this.synchSaveMsgTempTrade(uid, tradeDTOs, msgRecord);
		logger.info("一次性同步历史效果分析数据的job===》用户：" + uid + "发送记录id：" + msgRecord.getId() + "保存tempTrade完毕-_-_-_-_-_-_-_");
		this.itemDetailService.saveItemDetail(uid, msgRecord, tradeDTOs);
		logger.info(
				"一次性同步历史效果分析数据的job===》用户：" + uid + "发送记录id：" + msgRecord.getId() + "保存itemTempTrade完毕-_-_-_-_-_-_-_");
		if (DateUtils.getDiffDay(msgRecord.getSendCreat(), eTime).intValue() > 15) {
			logger.info("用户：" + uid + ",本次执行msgId：" + msgRecord.getId() + ",保存临时订单完毕,开始执行保存结果表");
			this.handleData(uid, msgRecord);
		} else {
			logger.info("用户：" + uid + ",本次执行msgId：" + msgRecord.getId() + ",保存临时订单完毕！不保存结果表");
		}
	}

	@Override
	public Double sumPaidByDate(Long uid, MsgSendRecord msgRecord, Date bTime, Date eTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("msgId", msgRecord.getId());
		map.put("tradeFrom", null);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		List<String> tables = msgTempTradeDao.tableIsExist(uid);
		if (tables == null || tables.isEmpty()) {
			msgTempTradeDao.doCreateTable(uid);
		}
		map.put("tradeStatus", "success");
		map.put("createType", null);
		map.put("waitType", null);
		map.put("payType", "pay");
		map.put("refundType", null);
		MsgTempTradeHistory paidData = tempTradeHistoryDao.aggregateDataByStatus(map);
		if (paidData != null) {
			BigDecimal payment = paidData.getPayment();
			if (payment != null) {
				return payment.doubleValue();
			} else {
				return 0.0;
			}
		}
		return 0.0;
	}

	/**
	 * 同步预售订单到效果分析临时库
	 */
	@Override
	public void synchSaveStepMsgTempTrade(Long uid, List<TradeDTO> tradeByMsg, MsgSendRecord msg) throws Exception {
		if (tradeByMsg == null || tradeByMsg.isEmpty()) {
			logger.info("效果分析同步临时订单库，符合发送记录的订单为空~~~~~~！~~~~~~");
			return;
		}
		logger.info("效果分析同步临时订单库，符合发送记录的订单:uid:" + uid + "个数为：" + tradeByMsg.size());
		Boolean createStatus = tempTradeService.doCreateTable(uid);
		if (false == createStatus) {
			return;
		}
		Boolean createTable = tempTradeHistoryService.doCreateTable(uid);
		if (false == createTable) {
			return;
		}
		List<MsgTempTrade> tempTrades = new ArrayList<>();
		MsgTempTrade tempTrade = new MsgTempTrade();
		for (TradeDTO trade : tradeByMsg) {
			tempTrade.setBuyerNick(trade.getBuyerNick());
			tempTrade.setCreated(trade.getCreated());
			tempTrade.setEndTime(trade.getEndTime() == null ? null : trade.getEndTime());
			tempTrade.setModifiedTime(trade.getModified());
			tempTrade.setMsgCreated(msg.getSendCreat());
			tempTrade.setMsgId(msg.getId());
			// 拼接商品字符串，","拼接
			tempTrade.setItemNum(trade.getNum());
			tempTrade.setPayment(trade.getPayment());
			tempTrade.setPayTime(trade.getPayTime());
			tempTrade.setReceiverAddress(trade.getReceiverAddress());
			tempTrade.setReceiverMobile(trade.getReceiverMobile());
			tempTrade.setReceiverName(trade.getReceiverName());
			tempTrade.setRefundFlag(trade.getRefundFlag());
			tempTrade.setTid(trade.getTid());
			tempTrade.setTradeFrom(trade.getTradeFrom());
			tempTrade.setFront(trade.getFront());
			tempTrade.setTail(trade.getTail());
			tempTrade.setStepTradeStatus(trade.getStepTradeStatus());
			if (TradesInfo.FRONT_PAID_FINAL_NOPAID.equals(trade.getStepTradeStatus())) {
				tempTrade.setFrontPayTime(trade.getPayTime());
			} else if (TradesInfo.FRONT_NOPAID_FINAL_NOPAID.equals(trade.getStepTradeStatus())) {
				tempTrade.setFrontPayTime(null);
			} else if (TradesInfo.FRONT_PAID_FINAL_PAID.equals(trade.getStepTradeStatus())) {
				tempTrade.setFrontPayTime(trade.getCreated());
			}
			// 判断订单状态
			List<String> payStatusList = new ArrayList<String>();
			payStatusList.add(TradesInfo.TRADE_BUYER_SIGNED);
			payStatusList.add(TradesInfo.WAIT_SELLER_SEND_GOODS);
			payStatusList.add(TradesInfo.WAIT_BUYER_CONFIRM_GOODS);
			Map<String, Object> map = new HashMap<>();
			map.put("uid", uid);
			map.put("msgId", msg.getId());
			map.put("tid", trade.getTid());
			logger.info("msgId:" + msg.getId() + ",tid:" + trade.getTid() + "查询出的订单的状态：" + trade.getStatus() + ",退款装太："
					+ trade.getRefundFlag());
			if (TradesInfo.TRADE_CLOSED_BY_TAOBAO.equals(trade.getStatus())) {
				createStepClosedTempTrade(tempTrade, map, uid, tempTrades);
			} else if (TradesInfo.TRADE_FINISHED.equals(trade.getStatus())) {
				createStepFinishedTempTrade(tempTrade, map, trade, tempTrades);
			} else {
				if (trade.getRefundFlag() != null && true == trade.getRefundFlag()) {
					createStepRefundTempTrade(uid, tempTrade, map, trade, tempTrades);
				} else {
					if (payStatusList.contains(trade.getStatus())) {
						createStepPaidFinalTempTrade(tempTrade, map, trade, tempTrades);
					} else if (TradesInfo.WAIT_BUYER_PAY.contains(trade.getStatus())) {
						createStepWaitPayTempTrade(tempTrade, map, trade, tempTrades);
					} else {
						logger.info("create~~~~~~~~~~~~~~~~~~~~~~~~~!!!!!!!!!!!!!!!!");
						tempTrade.setTradeStatus("create");
						tempTrade.setUid(uid);
						tempTrades.add(tempTrade);
					}
				}
			}
		}
		System.err.println("msgTempTrade总个数：" + tempTrades.size() + "个");
		int totalSize = tempTrades.size();
		logger.info("msgId:" + msg.getId() + "保存到临时库中的订单个数为" + totalSize);
		int pageSize = ConstantUtils.PROCESS_PAGE_SIZE_MAX.intValue();
		int start = 0, end = 0;
		if (totalSize % pageSize == 0) {
			end = totalSize / pageSize;
		} else {
			end = (totalSize + pageSize) / pageSize;
		}
		BaseListEntity<MsgTempTrade> entityList = null;
		Date nowDate = new Date();
		while (start < end) {
			entityList = new BaseListEntity<>();
			if (start == (end - 1)) {
				entityList.setEntityList(tempTrades.subList(start * pageSize, tempTrades.size()));
			} else {
				entityList.setEntityList(tempTrades.subList(start * pageSize, (start + 1) * pageSize));
			}
			entityList.setUid(uid);
			if (DateUtils.getDiffDay(msg.getSendCreat(), nowDate) < 15) {
				msgTempTradeDao.saveMsgTempTradeList(entityList);
				tempTradeHistoryDao.saveMsgTempTradeHistoryList(entityList);
			} else {
				tempTradeHistoryDao.saveMsgTempTradeHistoryList(entityList);
			}
			start++;
		}
	}

	public MsgTempTrade createNewRefundTempTrade(MsgTempTrade oldTempTrade, BigDecimal refundPament, Long itemNum) {
		if (oldTempTrade == null) {
			logger.info("oldTempTrade为空，不执行");
			return null;
		}
		MsgTempTrade msgTempTrade = new MsgTempTrade();
		msgTempTrade.setBuyerNick(oldTempTrade.getBuyerNick());
		msgTempTrade.setCreated(oldTempTrade.getCreated());
		msgTempTrade.setEndTime(oldTempTrade.getEndTime());
		msgTempTrade.setFront(oldTempTrade.getFront());
		msgTempTrade.setFrontPayTime(oldTempTrade.getFrontPayTime());
		msgTempTrade.setItemNum(itemNum);
		msgTempTrade.setModifiedTime(oldTempTrade.getModifiedTime());
		msgTempTrade.setMsgId(oldTempTrade.getMsgId());
		msgTempTrade.setMsgCreated(oldTempTrade.getMsgCreated());
		msgTempTrade.setPayment(refundPament);
		msgTempTrade.setReceiverAddress(oldTempTrade.getReceiverAddress());
		msgTempTrade.setReceiverMobile(oldTempTrade.getReceiverMobile());
		msgTempTrade.setReceiverName(oldTempTrade.getReceiverName());
		msgTempTrade.setRefundFlag(oldTempTrade.getRefundFlag());
		msgTempTrade.setStepTradeStatus(oldTempTrade.getStepTradeStatus());
		msgTempTrade.setTail(oldTempTrade.getTail());
		msgTempTrade.setTid(oldTempTrade.getTid());
		msgTempTrade.setTradeFrom(oldTempTrade.getTradeFrom());
		msgTempTrade.setTradeStatus(oldTempTrade.getTradeStatus());
		msgTempTrade.setTradeStatusList(oldTempTrade.getTradeStatusList());
		msgTempTrade.setUid(oldTempTrade.getUid());
		return msgTempTrade;
	}

	/**
	 * createStepClosedTempTrade(订单状态为付款前关闭，创建付款前关闭的临时订单) @Title:
	 * createStepClosedTempTrade @param @param tempTrade @param @param
	 * map @param @param uid @param @param tempTrades 设定文件 @return void
	 * 返回类型 @throws
	 */
	public void createStepClosedTempTrade(MsgTempTrade tempTrade, Map<String, Object> map, Long uid,
			List<MsgTempTrade> tempTrades) {
		logger.info("TradesInfo.TRADE_CLOSED_BY_TAOBAO");
		map.put("tradeStatus", "closed");
		List<Long> closedIds = msgTempTradeDao.isExistMsgTrade(map);
		if (closedIds == null || closedIds.isEmpty()) {
			MsgTempTrade closedTemp = this.createMsgTempTrade(uid, tempTrade, "closed");
			tempTrades.add(closedTemp);
			map.put("tradeStatus", "create");
			List<Long> createIds = msgTempTradeDao.isExistMsgTrade(map);
			if (createIds == null || createIds.isEmpty()) {
				MsgTempTrade createTemp = this.createMsgTempTrade(uid, tempTrade, "create");
				tempTrades.add(createTemp);
			}
		}
	}

	/**
	 * createStepRefundTempTrade(订单状态为退款，创建退款状态以及历史状态的临时订单) @Title:
	 * createRefundTempTrade @param @param uid @param @param
	 * tempTrade @param @param map @param @param trade @param @param tempTrades
	 * 设定文件 @return void 返回类型 @throws
	 */
	public void createStepRefundTempTrade(Long uid, MsgTempTrade tempTrade, Map<String, Object> map, TradeDTO trade,
			List<MsgTempTrade> tempTrades) {
		logger.info("trade.getRefundFlag()");
		List<OrderDTO> refundOrderList = orderDTOService.listRefundStatusByTid(uid, trade.getTid());
		if (refundOrderList != null && !refundOrderList.isEmpty()) {
			BigDecimal refundPament = new BigDecimal(0);
			Long itemNum = 0L;
			for (OrderDTO orderDTO : refundOrderList) {
				if (orderDTO.getRefundStatus() != null && !"".equals(orderDTO.getRefundStatus())
						&& "SUCCESS".equals(orderDTO.getRefundStatus())) {
					refundPament = NumberUtils.add(refundPament, orderDTO.getPayment());
					itemNum += orderDTO.getNum();
				}
			}
			tempTrade.setTradeStatus("refund");
			map.put("tradeStatus", "refund");
			List<Long> refundIds = msgTempTradeDao.isExistMsgTrade(map);
			if (refundIds == null || refundIds.isEmpty()) {
				MsgTempTrade refundTempTrade = createNewRefundTempTrade(tempTrade, refundPament, itemNum);
				MsgTempTrade refundTemp = this.createMsgTempTrade(trade.getUid(), refundTempTrade, "refund");
				tempTrades.add(refundTemp);
				if (TradesInfo.FRONT_PAID_FINAL_PAID.equals(trade.getStepTradeStatus())) {
					map.put("tradeStatus", "paidFinal");
					List<Long> ids = msgTempTradeDao.isExistMsgTrade(map);
					if (ids == null || ids.isEmpty()) {
						MsgTempTrade successTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "paidFinal");
						tempTrades.add(successTrade);
						map.put("tradeStatus", "waitPay");
						List<Long> waitPayIds = msgTempTradeDao.isExistMsgTrade(map);
						if (waitPayIds == null || waitPayIds.isEmpty()) {
							MsgTempTrade waitPayTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "waitPay");
							tempTrades.add(waitPayTrade);
							map.put("tradeStatus", "create");
							List<Long> createIds = msgTempTradeDao.isExistMsgTrade(map);
							if (createIds == null || createIds.isEmpty()) {
								MsgTempTrade createTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "create");
								tempTrades.add(createTrade);
							}
						}
					}
				} else {
					map.put("tradeStatus", "waitPay");
					List<Long> waitPayIds = msgTempTradeDao.isExistMsgTrade(map);
					if (waitPayIds == null || waitPayIds.isEmpty()) {
						MsgTempTrade waitPayTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "waitPay");
						tempTrades.add(waitPayTrade);
						map.put("tradeStatus", "create");
						List<Long> createIds = msgTempTradeDao.isExistMsgTrade(map);
						if (createIds == null || createIds.isEmpty()) {
							MsgTempTrade createTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "create");
							tempTrades.add(createTrade);
						}
					}
				}
			}
		} else {
		}

	}

	/**
	 * createFinishedTrade(订单状态为成交，创建成交状态以及历史状态的临时订单) @Title:
	 * createFinishedTrade @param @param tempTrade @param @param
	 * map @param @param trade @param @param tempTrades 设定文件 @return void
	 * 返回类型 @throws
	 */
	public void createStepFinishedTempTrade(MsgTempTrade tempTrade, Map<String, Object> map, TradeDTO trade,
			List<MsgTempTrade> tempTrades) {
		logger.info(TradesInfo.TRADE_FINISHED);
		tempTrade.setTradeStatus("success");
		map.put("tradeStatus", "success");
		List<Long> payIds = msgTempTradeDao.isExistMsgTrade(map);
		if (payIds == null || payIds.isEmpty()) {
			MsgTempTrade successTempTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "success");
			tempTrades.add(successTempTrade);
			map.put("tradeStatus", "paidFinal");
			List<Long> paidFinalIds = msgTempTradeDao.isExistMsgTrade(map);
			if (paidFinalIds == null || paidFinalIds.isEmpty()) {
				MsgTempTrade paidFinalTempTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "paidFinal");
				tempTrades.add(paidFinalTempTrade);
				map.put("tradeStatus", "waitPay");
				List<Long> waitPayIds = msgTempTradeDao.isExistMsgTrade(map);
				if (waitPayIds == null || waitPayIds.isEmpty()) {
					MsgTempTrade waitPayTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "waitPay");
					tempTrades.add(waitPayTrade);
					map.put("tradeStatus", "create");
					List<Long> createIds = msgTempTradeDao.isExistMsgTrade(map);
					if (createIds == null || createIds.isEmpty()) {
						MsgTempTrade createTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "create");
						tempTrades.add(createTrade);
					}
				}
			}
		}

	}

	/**
	 * createPaidFinalTrade(订单状态为付完尾款，创建付完尾款状态以及历史状态的临时订单) @Title:
	 * createPaidFinalTrade @param @param tempTrade @param @param
	 * map @param @param trade @param @param tempTrades 设定文件 @return void
	 * 返回类型 @throws
	 */
	public void createStepPaidFinalTempTrade(MsgTempTrade tempTrade, Map<String, Object> map, TradeDTO trade,
			List<MsgTempTrade> tempTrades) {
		logger.info("paidFinal");
		tempTrade.setTradeStatus("paidFinal");
		map.put("tradeStatus", "paidFinal");
		List<Long> payIds = msgTempTradeDao.isExistMsgTrade(map);
		if (payIds == null || payIds.isEmpty()) {
			MsgTempTrade successTempTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "paidFinal");
			tempTrades.add(successTempTrade);
			map.put("tradeStatus", "waitPay");
			List<Long> waitPayIds = msgTempTradeDao.isExistMsgTrade(map);
			if (waitPayIds == null || waitPayIds.isEmpty()) {
				MsgTempTrade waitPayTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "waitPay");
				tempTrades.add(waitPayTrade);
				map.put("tradeStatus", "create");
				List<Long> createIds = msgTempTradeDao.isExistMsgTrade(map);
				if (createIds == null || createIds.isEmpty()) {
					MsgTempTrade createTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "create");
					tempTrades.add(createTrade);
				}
			}
		}
	}

	/**
	 * createWaitPayTempTrade(订单状态为等待付款，创建等待付款状态以及历史状态的临时订单) @Title:
	 * createWaitPayTempTrade @param @param tempTrade @param @param
	 * map @param @param trade @param @param tempTrades 设定文件 @return void
	 * 返回类型 @throws
	 */
	public void createStepWaitPayTempTrade(MsgTempTrade tempTrade, Map<String, Object> map, TradeDTO trade,
			List<MsgTempTrade> tempTrades) {
		logger.info("TradesInfo.WAIT_BUYER_PAY111111111111111111111111");
		tempTrade.setTradeStatus("waitPay");
		map.put("tradeStatus", "waitPay");
		List<Long> waitPayIds = msgTempTradeDao.isExistMsgTrade(map);
		if (waitPayIds == null || waitPayIds.isEmpty()) {
			MsgTempTrade waitPayTempTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "waitPay");
			tempTrades.add(waitPayTempTrade);
			map.put("tradeStatus", "create");
			List<Long> createIds = msgTempTradeDao.isExistMsgTrade(map);
			if (createIds == null || createIds.isEmpty()) {
				MsgTempTrade createTrade = this.createMsgTempTrade(trade.getUid(), tempTrade, "create");
				tempTrades.add(createTrade);
			}
		}
	}

	/**
	 * 处理发送总记录--预售效果分析
	 */
	@Override
	public void handleStepData(Long uid, MsgSendRecord msgRecord) {
		if (uid == null || msgRecord == null) {
			return;
		}
		long l1 = System.currentTimeMillis();
		Boolean createStatus = this.doCreateTable(uid);
		if (false == createStatus) {
			return;
		}
		// 封装条件，筛选msgId的tempTrade
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("msgId", msgRecord.getId());
		List<MsgTempTradeHistory> tempTrades = tempTradeHistoryDao.listTempTradeByMsgId(map);
		if (tempTrades == null || tempTrades.isEmpty()) {
			return;
		}
		for (int i = 0; i < 15; i++) {
			Date bTime = msgRecord.getSendCreat();
			Date eTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAfter(i, bTime));
			// 创建最终结果对象,WAP,TAOBAO,TOTAL
			MarketingCenterEffect effectWAP = new MarketingCenterEffect(uid, TradesInfo.ORDER_FROM_WAP, eTime, i + 1,
					msgRecord.getId(), msgRecord.getSendCreat());
			MarketingCenterEffect effectTB = new MarketingCenterEffect(uid, TradesInfo.ORDER_FROM_TAOBAO, eTime, i + 1,
					msgRecord.getId(), msgRecord.getSendCreat());
			MarketingCenterEffect effectALL = new MarketingCenterEffect(uid, TradesInfo.ORDER_FROM_TOTAL, eTime, i + 1,
					msgRecord.getId(), msgRecord.getSendCreat());
			effectWAP.setUid(uid);
			effectTB.setUid(uid);
			effectALL.setUid(uid);
			// 定义TOTAL数据
			Set<Long> createTidSet = new HashSet<>(), waitPayTidSet = new HashSet<>(), payTidSet = new HashSet<>(),
					refundTidSet = new HashSet<>();
			Set<String> createBuyerSet = new HashSet<>(), waitBuyerTidSet = new HashSet<>(),
					payBuyerSet = new HashSet<>(), refundBuyerSet = new HashSet<>();
			BigDecimal createFee = new BigDecimal(0.00), waitPayFee = new BigDecimal(0.00),
					payFee = new BigDecimal(0.00), refundFee = new BigDecimal(0.00);
			Long createItemNum = 0L, waitPayItemNum = 0L, payItemNum = 0L, refundItemNum = 0L;

			List<MsgTempTradeHistory> wapTempTrade = new ArrayList<>();
			List<MsgTempTradeHistory> taobaoTempTrade = new ArrayList<>();
			// 开始处理数据
			for (MsgTempTradeHistory tempTrade : tempTrades) {
				if (tempTrade != null && tempTrade.getTradeFrom() != null) {
					long tradeCreatedLong = tempTrade.getCreated().getTime();
					long bTimeLong = bTime.getTime();
					long eTimeLong = eTime.getTime();
					if (tradeCreatedLong > bTimeLong && tradeCreatedLong < eTimeLong) {
						if (tempTrade.getTradeFrom().contains("WAP")) {
							wapTempTrade.add(tempTrade);
						} else if (tempTrade.getTradeFrom().contains("TAOBAO")) {
							taobaoTempTrade.add(tempTrade);
						}
						if ("create".equals(tempTrade.getTradeStatus())
								|| "waitPay".equals(tempTrade.getTradeStatus())) {
							createTidSet.add(tempTrade.getTid());
							// createBuyerSet.add(tempTrade.getBuyerNick());
							createItemNum += NumberUtils.getResult(tempTrade.getItemNum());
							createFee = createFee.add(new BigDecimal(NumberUtils.getResult(tempTrade.getTail())));
						} else if ("closed".equals(tempTrade.getTradeStatus())) {
							waitPayTidSet.add(tempTrade.getTid());
							// waitBuyerTidSet.add(tempTrade.getBuyerNick());
							waitPayItemNum += NumberUtils.getResult(tempTrade.getItemNum());
							waitPayFee = waitPayFee.add(new BigDecimal(NumberUtils.getResult(tempTrade.getTail())))
									.add(new BigDecimal(NumberUtils.getResult(tempTrade.getFront())));
						} else if ("success".equals(tempTrade.getTradeStatus())) {
							payTidSet.add(tempTrade.getTid());
							// payBuyerSet.add(tempTrade.getBuyerNick());
							payItemNum += NumberUtils.getResult(tempTrade.getItemNum());
							payFee = payFee.add(NumberUtils.getResult(tempTrade.getPayment()));
						} else if ("refund".equals(tempTrade.getTradeStatus())) {
							refundTidSet.add(tempTrade.getTid());
							refundBuyerSet.add(tempTrade.getBuyerNick());
							refundItemNum += NumberUtils.getResult(tempTrade.getItemNum());
							refundFee = refundFee.add(new BigDecimal(NumberUtils.getResult(tempTrade.getTail())))
									.add(new BigDecimal(NumberUtils.getResult(tempTrade.getFront())));
						}
					}

				}
			}
			effectALL.setCreateAmount(createFee);
			effectALL.setCreateBuyerNum(createBuyerSet.size());
			effectALL.setCreateItemNum(createItemNum);
			effectALL.setCreateTradeNum(createTidSet.size());
			effectALL.setWaitPayAmount(waitPayFee);
			effectALL.setWaitPayBuyerNum(waitBuyerTidSet.size());
			effectALL.setWaitPayItemNum(waitPayItemNum);
			effectALL.setWaitPayTradeNum(waitPayTidSet.size());
			effectALL.setPayAmount(payFee);
			effectALL.setPayBuyerNum(payBuyerSet.size());
			effectALL.setPayItemNum(payItemNum);
			effectALL.setPayTradeNum(payTidSet.size());
			effectALL.setRefundAmount(refundFee);
			effectALL.setRefundBuyerNum(refundBuyerSet.size());
			effectALL.setRefundItemNum(refundItemNum);
			effectALL.setRefundTradeNum(refundTidSet.size());
			effectALL.setCreatedBy(uid + "");
			effectALL.setCreatedDate(new Date());
			effectALL.setLastModifiedBy(uid + "");
			effectALL.setLastModifiedDate(new Date());
			this.checkTradeFrom(wapTempTrade, effectWAP);
			this.checkTradeFrom(taobaoTempTrade, effectTB);
			this.saveOrUpdateEffect(effectWAP);
			this.saveOrUpdateEffect(effectTB);
			this.saveOrUpdateEffect(effectALL);
		}
		this.cacheService.putNoTime(RedisConstant.RediskeyCacheGroup.MSG_IS_HISTORY_DATA, "msgId-" + msgRecord.getId(),
				true, false);
		long l2 = System.currentTimeMillis();
		// 删除msgTemptrade中的数据
		// tempTradeService.deleteFifteenAgoTrade(msgRecord.getUid(),
		// DateUtils.getTimeByDay(new Date(), -16));
		// 删除itemTemptrade中的数据
		// itemDetailService.deleteItemTempTrade(msgRecord.getUid(),
		// DateUtils.getTimeByDay(new Date(), -16));
		if ((l2 - l1) > 10000) {
			logger.info("+_+	+_++_++_++_++_++_++_++_++_++_++_++_+" + msgRecord.getId() + "的查询时间超过10s " + (l2 - l1)
					+ "ms");
		}
	}

	public void checkStepTradeFrom(List<MsgTempTradeHistory> tempTrades, MarketingCenterEffect effect) {
		Set<Long> createTidSet = new HashSet<>(), waitPayTidSet = new HashSet<>(), payTidSet = new HashSet<>(),
				refundTidSet = new HashSet<>();
		Set<String> createBuyerSet = new HashSet<>(), waitBuyerTidSet = new HashSet<>(), payBuyerSet = new HashSet<>(),
				refundBuyerSet = new HashSet<>();
		BigDecimal createFee = new BigDecimal(0.00), waitPayFee = new BigDecimal(0.00), payFee = new BigDecimal(0.00),
				refundFee = new BigDecimal(0.00);
		Long createItemNum = 0L, waitPayItemNum = 0L, payItemNum = 0L, refundItemNum = 0L;
		for (MsgTempTradeHistory tempTrade : tempTrades) {
			if (tempTrade != null) {
				if ("create".equals(tempTrade.getTradeStatus()) || "waitPay".equals(tempTrade.getTradeStatus())) {
					createTidSet.add(tempTrade.getTid());
					createBuyerSet.add(tempTrade.getBuyerNick());
					createItemNum += NumberUtils.getResult(tempTrade.getItemNum());
					createFee = createFee.add(new BigDecimal(NumberUtils.getResult(tempTrade.getTail())));
				} else if ("closed".equals(tempTrade.getTradeStatus())) {
					waitPayTidSet.add(tempTrade.getTid());
					waitBuyerTidSet.add(tempTrade.getBuyerNick());
					waitPayItemNum += NumberUtils.getResult(tempTrade.getItemNum());
					waitPayFee = waitPayFee.add(new BigDecimal(NumberUtils.getResult(tempTrade.getTail())))
							.add(new BigDecimal(NumberUtils.getResult(tempTrade.getFront())));
				} else if ("success".equals(tempTrade.getTradeStatus())) {
					payTidSet.add(tempTrade.getTid());
					payBuyerSet.add(tempTrade.getBuyerNick());
					payItemNum += NumberUtils.getResult(tempTrade.getItemNum());
					payFee = payFee.add(NumberUtils.getResult(tempTrade.getPayment()));
				} else if ("refund".equals(tempTrade.getTradeStatus())) {
					refundTidSet.add(tempTrade.getTid());
					refundBuyerSet.add(tempTrade.getBuyerNick());
					refundItemNum += NumberUtils.getResult(tempTrade.getItemNum());
					refundFee = refundFee.add(new BigDecimal(NumberUtils.getResult(tempTrade.getTail())))
							.add(new BigDecimal(NumberUtils.getResult(tempTrade.getFront())));
				}
			}
		}
		effect.setCreateAmount(createFee);
		effect.setCreateBuyerNum(createBuyerSet.size());
		effect.setCreateItemNum(createItemNum);
		effect.setCreateTradeNum(createTidSet.size());
		effect.setWaitPayAmount(waitPayFee);
		effect.setWaitPayBuyerNum(waitBuyerTidSet.size());
		effect.setWaitPayItemNum(waitPayItemNum);
		effect.setWaitPayTradeNum(waitPayTidSet.size());
		effect.setPayAmount(payFee);
		effect.setPayBuyerNum(payBuyerSet.size());
		effect.setPayItemNum(payItemNum);
		effect.setPayTradeNum(payTidSet.size());
		effect.setRefundAmount(refundFee);
		effect.setRefundBuyerNum(refundBuyerSet.size());
		effect.setRefundItemNum(refundItemNum);
		effect.setRefundTradeNum(refundTidSet.size());
		effect.setCreatedBy(effect.getUid() + "");
		effect.setCreatedDate(new Date());
		effect.setLastModifiedBy(effect.getUid() + "");
		effect.setLastModifiedDate(new Date());
	}

	/**
	 * 预售效果分析主页面的计算
	 */
	@Override
	public MarketingCenterEffect sumMarketingStepCenterEffect(Long uid, MsgSendRecord msgRecord, String tradeFrom,
			Date bTime, Date eTime) throws Exception {
		MarketingCenterEffect resultData = new MarketingCenterEffect();

		if (msgRecord == null || msgRecord.getSendCreat() == null) {
			return null;
		}
		if (bTime == null || eTime == null) {
			throw new RuntimeException("时间参数为空，请重新操作或登陆");
		}
		Boolean isHistory = this.cacheService.get(RedisConstant.RediskeyCacheGroup.MSG_IS_HISTORY_DATA,
				"msgId-" + msgRecord.getId(), Boolean.class);
		if (isHistory == null) {
			isHistory = false;
		}
		CustomerDetailVO cacheParam = new CustomerDetailVO();
		cacheParam.setMsgId(msgRecord.getId());
		cacheParam.setbTime(bTime);
		cacheParam.seteTime(eTime);
		cacheParam.setSendCreated(msgRecord.getSendCreat());
		this.doCreateTable(uid);
		lockService.putStringValueWithExpireTime(uid + "-effectParam", JsonUtil.toJson(cacheParam), TimeUnit.HOURS, 1L);
		if (isHistory) {
			resultData = this.findEffectByDays(uid, msgRecord.getId(), tradeFrom,
					DateUtils.getDiffDay(msgRecord.getSendCreat(), eTime).intValue() + 1);
		} else {
			Map<String, Object> map = new HashMap<>();
			map.put("uid", uid);
			map.put("msgId", msgRecord.getId());
			map.put("tradeFrom", tradeFrom);
			map.put("bTime", bTime);
			map.put("eTime", eTime);
			map.put("tradeStatus", "create");
			List<String> stepStatusList = new ArrayList<>();
			stepStatusList.add(TradesInfo.FRONT_PAID_FINAL_NOPAID);
			stepStatusList.add(TradesInfo.FRONT_PAID_FINAL_PAID);
			map.put("stepTradeStatusList", stepStatusList);
			map.put("stepTradeStatus", null);
			map.put("createType", "created");
			map.put("waitType", null);
			map.put("payType", null);
			map.put("refundType", null);
			if ("TOTAL" == tradeFrom || "TOTAL".equals(tradeFrom)) {
				map.put("tradeFrom", null);
			}
			List<String> tables = msgTempTradeDao.tableIsExist(uid);
			if (tables != null && !tables.isEmpty()) {
			} else {
				msgTempTradeDao.doCreateTable(uid);
			}
			MsgTempTrade createData = msgTempTradeDao.aggregateStepDataByStatus(map);
			lockService.putStringValueWithExpireTime(uid + "-effectMap", JsonUtil.toJson(map), TimeUnit.HOURS, 1L);
			if (createData != null) {
				String buyerNickStr = createData.getBuyerNick();
				if (buyerNickStr != null && !"".equals(buyerNickStr)) {
					Integer buyerCount = Integer.parseInt(buyerNickStr);
					resultData.setCreateBuyerNum(buyerCount);
				}
				Integer tidCount = createData.getTid() == null ? 0 : createData.getTid().intValue();
				Long itemNum = createData.getItemNum();
				String front = createData.getFront();
				resultData.setCreateTradeNum(tidCount);
				resultData.setCreateItemNum(itemNum);
				resultData.setCreateAmount(new BigDecimal(NumberUtils.getResult(front)));
			}
			List<String> paidStatusList = new ArrayList<>();
			paidStatusList.add("success");
			paidStatusList.add("paidFinal");
			map.put("tradeStatus", null);
			map.put("tradeStatusList", paidStatusList);
			map.put("createType", null);
			map.put("waitType", null);
			map.put("payType", "pay");
			map.put("refundType", null);
			map.put("stepTradeStatus", TradesInfo.FRONT_PAID_FINAL_PAID);
			map.put("stepStatusList", null);
			MsgTempTrade successData = msgTempTradeDao.aggregateStepDataByStatus(map);
			if (successData != null) {
				String buyerNickStr = successData.getBuyerNick();
				if (buyerNickStr != null && !"".equals(buyerNickStr)) {
					resultData.setPayBuyerNum(Integer.parseInt(buyerNickStr));
				}
				Integer tidCount = successData.getTid() == null ? 0 : successData.getTid().intValue();
				Long itemNum = successData.getItemNum();
				String tail = successData.getTail();
				resultData.setPayTradeNum(tidCount);
				resultData.setPayItemNum(itemNum);
				resultData.setPayAmount(new BigDecimal(NumberUtils.getResult(tail)));
			}
			map.put("tradeStatus", "closed");
			map.put("tradeStatusList", null);
			map.put("createType", null);
			map.put("waitType", "wait");
			map.put("payType", null);
			map.put("refundType", null);
			map.put("stepTradeStatus", TradesInfo.FRONT_PAID_FINAL_NOPAID);
			map.put("stepStatusList", null);
			MsgTempTrade frontNoPaidPayData = msgTempTradeDao.aggregateStepDataByStatus(map);
			map.put("stepTradeStatus", TradesInfo.FRONT_NOPAID_FINAL_NOPAID);
			map.put("stepStatusList", null);
			MsgTempTrade frontTailNoPaidPayData = msgTempTradeDao.aggregateStepDataByStatus(map);
			if (frontNoPaidPayData != null) {
				String buyerNickStr = frontNoPaidPayData.getBuyerNick();
				if (buyerNickStr != null && !"".equals(buyerNickStr)) {
					resultData.setWaitPayBuyerNum(Integer.parseInt(buyerNickStr));
				}
				Integer tidCount = frontNoPaidPayData.getTid() == null ? 0 : frontNoPaidPayData.getTid().intValue();
				Long itemNum = frontNoPaidPayData.getItemNum();
				String tail = frontNoPaidPayData.getTail();
				resultData.setWaitPayTradeNum(tidCount);
				resultData.setWaitPayItemNum(itemNum);
				resultData.setWaitPayAmount(new BigDecimal(NumberUtils.getResult(tail)));
			}
			if (frontTailNoPaidPayData != null) {
				String buyerNickStr = frontNoPaidPayData.getBuyerNick();
				if (buyerNickStr != null && !"".equals(buyerNickStr)) {
					resultData.setWaitPayBuyerNum(Integer.parseInt(buyerNickStr));
				}
				Integer tidCount = frontNoPaidPayData.getTid() == null ? 0 : frontNoPaidPayData.getTid().intValue();
				Long itemNum = frontNoPaidPayData.getItemNum();
				String tail = frontNoPaidPayData.getTail();
				resultData.setWaitPayTradeNum(tidCount);
				resultData.setWaitPayItemNum(itemNum);
				resultData.setWaitPayAmount(NumberUtils.add(new BigDecimal(NumberUtils.getResult(tail)),
						NumberUtils.getResult(resultData.getWaitPayAmount())));
			}
			map.put("tradeStatus", "refund");
			map.put("createType", null);
			map.put("waitType", null);
			map.put("payType", null);
			map.put("refundType", "refund");
			map.put("stepTradeStatus", null);
			map.put("stepStatusList", null);
			MsgTempTrade refundData = msgTempTradeDao.aggregateStepDataByStatus(map);
			if (refundData != null) {
				String buyerNickStr = refundData.getBuyerNick();
				if (buyerNickStr != null && !"".equals(buyerNickStr)) {
					resultData.setRefundBuyerNum(Integer.parseInt(buyerNickStr));
				}
				Integer tidCount = refundData.getTid() == null ? 0 : refundData.getTid().intValue();
				Long itemNum = refundData.getItemNum();
				BigDecimal payment = refundData.getPayment();
				resultData.setRefundTradeNum(tidCount);
				resultData.setRefundItemNum(itemNum);
				resultData.setRefundAmount(payment);
			}
		}
		return resultData;
	}

	/**
	 * 计算预售用户选择时间段内每天成交数据
	 */
	@Override
	public List<PayOrderEffectDetailVO> listStepPayData(Long uid, Long msgId, String tradeFrom, Date bTime,
			Date eTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("msgId", msgId);
		map.put("tradeFrom", tradeFrom);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		List<String> paidStatusList = new ArrayList<>();
		paidStatusList.add("success");
		paidStatusList.add("paidFinal");
		map.put("tradeStatusList", paidStatusList);
		map.put("stepTradeStatus", TradesInfo.FRONT_PAID_FINAL_PAID);
		if ("TOTAL" == tradeFrom || "TOTAL".equals(tradeFrom)) {
			map.put("tradeFrom", null);
		}
		Boolean isHistory = this.cacheService.get(RedisConstant.RediskeyCacheGroup.MSG_IS_HISTORY_DATA,
				"msgId-" + msgId, Boolean.class);
		if (isHistory == null) {
			isHistory = false;
		}
		List<Map<String, Object>> payDataList = null;
		try {
			if (isHistory) {
				payDataList = tempTradeHistoryDao.listStepPayData(map);
			} else {
				payDataList = msgTempTradeDao.listStepPayData(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<PayOrderEffectDetailVO> resultVOList = null;
		if (payDataList != null && !payDataList.isEmpty()) {
			resultVOList = new ArrayList<>();
			for (Map<String, Object> dataMap : payDataList) {
				if (dataMap != null) {
					Object paidTailFeeObj = dataMap.get("paidTailFee");
					double paidTailFee = 0.0;
					if (paidTailFeeObj instanceof String) {
						String paidTailFeeStr = (String) paidTailFeeObj;
						if (paidTailFeeStr != null && !"".equals(paidTailFeeStr)) {
							paidTailFee = Double.parseDouble(paidTailFeeStr);
						}
					} else if (paidTailFeeObj instanceof Double) {
						paidTailFee = (double) paidTailFeeObj;
					}
					dataMap.put("paidTailFee", paidTailFee);
					PayOrderEffectDetailVO resultVO = new PayOrderEffectDetailVO();
					try {
						BeanUtils.populate(resultVO, dataMap);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					resultVOList.add(resultVO);
				}
			}
		}
		return resultVOList;
	}

	/**
	 * 计算预售用户选择时间段内每天成交数据
	 */
	@Override
	public List<PayOrderEffectDetailVO> listStepPayFrontData(Long uid, Long msgId, String tradeFrom, Date bTime,
			Date eTime) {
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("msgId", msgId);
		map.put("tradeFrom", tradeFrom);
		map.put("bTime", bTime);
		map.put("eTime", eTime);
		List<String> paidStatusList = new ArrayList<>();
		paidStatusList.add("create");
		map.put("tradeStatusList", paidStatusList);
		map.put("stepTradeStatus", TradesInfo.FRONT_NOPAID_FINAL_NOPAID);
		if ("TOTAL" == tradeFrom || "TOTAL".equals(tradeFrom)) {
			map.put("tradeFrom", null);
		}
		Boolean isHistory = this.cacheService.get(RedisConstant.RediskeyCacheGroup.MSG_IS_HISTORY_DATA,
				"msgId-" + msgId, Boolean.class);
		if (isHistory == null) {
			isHistory = false;
		}
		List<Map<String, Object>> payDataList = null;
		try {
			if (isHistory) {
				payDataList = tempTradeHistoryDao.listStepPayFrontData(map);
			} else {
				payDataList = msgTempTradeDao.listStepPayFrontData(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<PayOrderEffectDetailVO> resultVOList = null;
		if (payDataList != null && !payDataList.isEmpty()) {
			resultVOList = new ArrayList<>();
			for (Map<String, Object> dataMap : payDataList) {
				if (dataMap != null) {
					Object paidFrontFeeObj = dataMap.get("paidFrontFee");
					double paidFrontFee = 0.0;
					if (paidFrontFeeObj instanceof String) {
						String paidFrontFeeStr = (String) paidFrontFeeObj;
						if (paidFrontFeeStr != null && !"".equals(paidFrontFeeStr)) {
							paidFrontFee = Double.parseDouble(paidFrontFeeStr);
						}
					} else if (paidFrontFeeObj instanceof Double) {
						paidFrontFee = (double) paidFrontFeeObj;
					}
					dataMap.put("paidFrontFee", paidFrontFee);
					PayOrderEffectDetailVO resultVO = new PayOrderEffectDetailVO();
					try {
						BeanUtils.populate(resultVO, dataMap);
					} catch (IllegalAccessException | InvocationTargetException e) {
						e.printStackTrace();
					}
					resultVOList.add(resultVO);
				}
			}
		}
		return resultVOList;
	}

}
