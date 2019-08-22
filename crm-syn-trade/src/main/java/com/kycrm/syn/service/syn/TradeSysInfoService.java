package com.kycrm.syn.service.syn;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TbTrade;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.vo.trade.TradeFullinfoGetResponse;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.trade.ITbTransactionOrderService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.syn.core.redis.CacheService;
import com.kycrm.syn.domain.base.TradeAndOrderDataCollector;
import com.kycrm.syn.queue.MemberQueueService;
import com.kycrm.syn.queue.TradeQueueService;
import com.kycrm.syn.thread.OrderSynThread;
import com.kycrm.syn.thread.TradeSynThread;
import com.kycrm.syn.util.MyFixedThreadPool;
import com.kycrm.syn.util.RedisDistributionLock;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.TradesInfo;
import com.kycrm.util.ValidateUtil;
import com.taobao.api.ApiException;
import com.taobao.api.internal.util.TaobaoUtils;

/**
 * @author wy
 * @version 创建时间：2018年1月29日 下午3:31:50
 */
@Service
public class TradeSysInfoService {

	@Autowired
	private CacheService cacheService;

	@Autowired
	private ITbTransactionOrderService tbTransactionOrderService;

	@Autowired
	private ITradeDTOService tradeDTOService;

	@Autowired
	private IOrderDTOService orderDTOService;

	@Autowired
	private IUserInfoService userInfoService;

	@Resource(name = "redisTemplate")
	private StringRedisTemplate redisTemplate;

	private Logger logger = LoggerFactory.getLogger(TradeSysInfoService.class);

	// 节点同步跨度时间自己调节
	int nodeSynMinute = Constants.TRADE_SYN_SLEEP_MINUTE;

	/**
	 * 相同间隔时间的订单同步只允许一个job执行
	 * 
	 * @author: wy
	 * @time: 2018年1月30日 下午4:16:00
	 */
	public void startSynTradeBySysInfo() {
		String startTimeStr = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
				RedisConstant.RediskeyCacheGroup.TRADE_NODE_START_TIME_KEY);
		Optional<String> optional = Optional.ofNullable(startTimeStr);
		String startTime = optional.orElse(Constants.DEFAULT_NODE_SYNC_TIME);

		// 将从redis取结束时间改为计算
		String endTime = DateUtils.addMinute(DateUtils.parseDate(startTime), nodeSynMinute).toString();
		String totalCountStr = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
				RedisConstant.RediskeyCacheGroup.TOTAL_TRADE_DATA_COUNT_KEY);
		Optional<String> optional2 = Optional.ofNullable(totalCountStr);
		String totalCount = optional2.orElse("0");
		logger.info("Trade节点数据开始时间:{} 同步时长:{}分 历史已处理数据量:{}", startTime, nodeSynMinute, totalCount);
		if (ValidateUtil.isEmpty(startTime) || ValidateUtil.isEmpty(endTime)) {
			return;
		}
		Date startDate = DateUtils.parseTime(startTime);
		Date endDate = DateUtils.addMinute(startDate, nodeSynMinute);
		/*
		 * RedisLock lock = new RedisLock(redisTemplate,
		 * RedisConstant.RedisCacheGroup.TRADE_SYN_LOCK + startDate.getTime() +
		 * "_" + endDate.getTime(), InitManangeTrade.SLEEP_TIME);
		 */
		try {
			// if (lock.lock()) {
			this.startSyncTrade(startDate, endDate, totalCount);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// lock.unlock();
		}
	}

	/**
	 * 分页定时查询订单，同步淘宝推送库的订单数据
	 * 
	 * @author: wy
	 * @time: 2018年1月30日 下午2:21:05
	 */
	private void startSyncTrade(Date startDate, Date endDate, String totalCount) {
		if (startDate == null || endDate == null) {
			return;
		}
		Long rowsCount = this.tbTransactionOrderService.findCountByDate(startDate, endDate);
		Long pageNum = 0L;
		Integer syncAddTime=0;
        Long pageSize = Constants.PROCESS_PAGE_SIZE_MIDDLE;
        if (rowsCount / Constants.PROCESS_PAGE_SIZE_MIDDLE == 0) {
        pageNum = 1L;
		} else if (rowsCount % Constants.PROCESS_PAGE_SIZE_MIDDLE == 0) {
			pageNum = rowsCount / Constants.PROCESS_PAGE_SIZE_MIDDLE;
		} else {
			pageNum = (rowsCount + Constants.PROCESS_PAGE_SIZE_MIDDLE) / Constants.PROCESS_PAGE_SIZE_MIDDLE;
		}
		if (rowsCount == 0) {
			pageNum = 0l;
		}
		if(rowsCount>0){
			syncAddTime=(int)(nodeSynMinute*60/pageNum);
		}
		List<TbTrade> tbTradeList = null;
		int start = 0;
		Date SearchEedDate=null;
		this.logger.info("**** 本次数据总量{}条 共{}页 每页{}条", rowsCount, pageNum, pageSize);
		while (start < pageNum) {
			try {
				if (start == pageNum - 1) {
					 SearchEedDate = endDate;
				}else{
					 SearchEedDate =DateUtils.addSecond(startDate, syncAddTime);	
				}
				logger.info("开始时间"+DateUtils.formatTime(startDate)+"结束时间"+DateUtils.formatTime(SearchEedDate));
				// 分页查询trade数据
				tbTradeList = this.tbTransactionOrderService.findTradeByLimit(startDate, SearchEedDate)
						.stream()
						// TODO 测试过滤 上线注释 filter()
						//.filter(tbTrade ->
					    //tbTrade.getSellerNick().equals("北京冰点零度")
						//|| tbTrade.getSellerNick().equals("溜溜梅旗舰店")
						//|| tbTrade.getSellerNick().equals("小白你什么都没看见哦"))
						.collect(Collectors.toList());
				start++; // 如果有10分钟之内
				// 空 不添加
				if (!ValidateUtil.isEmpty(tbTradeList)) {
					this.logger.info("========>>>>>>订单数据同步， 第{}次分发 本次{}条数据 <<<<<<=======", (start + 1), tbTradeList.size());
					// 放入订单内存队列
					TradeQueueService.TRADE_QUEUE.put(tbTradeList);
					// 放入会员处理内存队列
					MemberQueueService.MENBER_QUEUE.put(tbTradeList);
				}
				startDate=SearchEedDate;
				int size = TradeQueueService.TRADE_QUEUE.size();
				if (size > 10000) {
					int sleep = (int) size / 2000;
					Thread.sleep(Constants.TRADE_SYN_SLEEP_TIME * sleep);
				} else {
					Thread.sleep(Constants.TRADE_SYN_SLEEP_TIME);
				}
			} catch (Exception e) {
				e.printStackTrace();
				continue;
			}

		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date startNode = endDate;
		//startNode = DateUtils.addMinute(startDate, nodeSynMinute);
		logger.info("Trade处理结束，下次同步节点时间：{}", sdf.format(startNode));
		rowsCount = Long.valueOf(totalCount) + rowsCount;
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
				RedisConstant.RediskeyCacheGroup.TRADE_NODE_START_TIME_KEY,
				DateUtils.formatDate(startNode, Constants.DATE_TIME_FORMAT));
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.NODE_DATA_CACHE,
				RedisConstant.RediskeyCacheGroup.TOTAL_TRADE_DATA_COUNT_KEY, rowsCount);
	}

	/**
	 * 订单数据转换，根据用户分类并保存<
	 * 
	 * @author: wy
	 * @time: 2018年1月30日 下午2:20:30
	 * @param tradeList
	 *            要保存的订单集合
	 */
	public void processTbTradeData(List<TbTrade> tbTradeList) throws Exception {
		if (ValidateUtil.isEmpty(tbTradeList))
			return;
		logger.info("订单处理数据为" + tbTradeList.size());
		// 组装数据
		TradeAndOrderDataCollector dataCollector = this.assembleTradeAndOrder(tbTradeList);
		// 异步存储主订单
		this.batchSaveTradeData(dataCollector);
		// 异步存储子订单
		this.batchSaveOrderData(dataCollector);
		dataCollector = null;
	}

	/**
	 * 分离保存Trade和Order
	 * 
	 * @param tradeDTO
	 */
	public TradeAndOrderDataCollector assembleTradeAndOrder(List<TbTrade> tbTradeList) throws Exception {
		TradeAndOrderDataCollector dataCollector = new TradeAndOrderDataCollector();
		Map<Long, List<TradeDTO>> tradeListMap = new HashMap<>(Constants.TRADE_SYN_USER_KEY);
		Map<Long, List<OrderDTO>> orderListMap = new HashMap<>(Constants.TRADE_SYN_USER_KEY);
		List<TradeDTO> tradeDTOlist = new ArrayList<TradeDTO>(Constants.TRADE_SYN_USER_KEY);
		List<OrderDTO> orderList = null;
		List<OrderDTO> orderDTOList = null;
		TbTrade tbTrade = null;
		TradeDTO tradeDTO = null;
		for (int i = 0; i < tbTradeList.size(); i++) {
			tbTrade = tbTradeList.get(i);
			if (tbTrade != null && tbTrade.getJdpResponse() != null && !"".equals(tbTrade.getJdpResponse())) {
				tradeDTO = this.getTradeDTO(tbTrade);
				try {
					// TODO 1、拉取sysInfo数据库，同步订单 2、用户导入
					tradeDTO.setTradeSource(1);
					// 封装trade
					tradeDTO = this.packageTradeDTO(tradeDTO);
					orderList = tradeDTO.getOrders();
					Long num = 0L;
					for (OrderDTO orderDTO : orderList) {
						orderDTO.setTradePayment(tradeDTO.getPayment());
						// 封装order
						orderDTO = this.packageTradeToOrder(tradeDTO, orderDTO);
						num += (orderDTO.getNum() == null ? 0 : orderDTO.getNum());
					}
					//为每个子订单添加trade_num
					if (orderList != null && orderList.size() >= 1) {
						for (OrderDTO orderDTO : orderList) {
							orderDTO.setTradeNum(num);
						}
						tradeDTO.setNum(num);
					}
					Long uid = null;
					if (tradeDTO.getUid() != null) {
						logger.info("从订单中取出的uid为"+tradeDTO.getUid());
						uid =tradeDTO.getUid(); //userInfoService.findUidBySellerNick(tbTrade.getSellerNick());
					}
					if(tbTrade.getSellerNick()!=null&&uid==null){
						uid =userInfoService.findUidBySellerNick(tbTrade.getSellerNick());
					}
					if (uid == null) {
						logger.error("通过sellerNick: {} 没有找到uid", tbTrade.getSellerNick());
						continue;
					}
					if (tradeListMap.containsKey(uid)) {
						tradeListMap.get(uid).add(tradeDTO);
						orderListMap.get(uid).addAll(orderList);
					} else {
						tradeDTOlist = new ArrayList<TradeDTO>();
						tradeDTOlist.add(tradeDTO);
						orderDTOList = new ArrayList<OrderDTO>(orderList.size());
						orderDTOList.addAll(orderList);
						tradeListMap.put(uid, tradeDTOlist);
						orderListMap.put(uid, orderDTOList);
					}
				} catch (Exception e) {
					e.printStackTrace();
					logger.error("分离保存Trade和Order出错 ", e);
					throw new Exception(e);
				}

			}
		}

		dataCollector.setTradeListMap(tradeListMap);
		dataCollector.setOrderListMap(orderListMap);
		return dataCollector;
		// for (TradeDTO tradeDTO : oldTradeDTOList) {
		// try {
		// // TODO 1、拉取sysInfo数据库，同步订单 2、用户导入
		// tradeDTO.setTradeSource(1);
		// // 封装trade
		// tradeDTO = this.packageTradeDTO(tradeDTO);
		// orderList = tradeDTO.getOrders();
		// Long num = 0L;
		// for (OrderDTO orderDTO : orderList) {
		// orderDTO.setTradePayment(tradeDTO.getPayment());
		// // 封装order
		// orderDTO = this.packageTradeToOrder(tradeDTO, orderDTO);
		// num += (orderDTO.getNum() == null ? 0 : orderDTO.getNum());
		// }
		// if (orderList != null && orderList.size() > 1) {
		// tradeDTO.setNum(num);
		// }
		// String sellerNick = tradeDTO.getSellerNick();
		// String uid = tradeDTO.getUid().toString();
		// if (uid == null) {
		// logger.error("通过sellerNick: {} 没有找到uid", sellerNick);
		// }
		//
		// if (tradeListMap.containsKey(uid)) {
		// tradeListMap.get(uid).add(tradeDTO);
		// orderListMap.get(uid).addAll(orderList);
		// } else {
		// tradeDTOlist.add(tradeDTO);
		// orderDTOList = new ArrayList<OrderDTO>(orderList.size());
		// orderDTOList.addAll(orderList);
		// tradeListMap.put(uid, tradeDTOlist);
		// orderListMap.put(uid, orderDTOList);
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// logger.error("分离保存Trade和Order出错 ", e);
		// throw new Exception(e);
		// }
		// }

		// Set<String> keySet = tradeListMap.keySet();
		// logger.info("本批次trade大小" + keySet.size());
		// long startTime1 = System.currentTimeMillis();
		// keySet.stream().forEach(uid ->
		// handleAfterCreateData(tradeListMap.get(uid), orderListMap.get(uid),
		// uid));
		// logger.info("保存本批{}条Trade 耗时{}毫秒", keySet.size(),
		// (System.currentTimeMillis() - startTime1));
	}

	/**
	 * 分用户保存订单记录
	 * 
	 * @author: wy
	 * @time: 2018年1月30日 下午2:04:57
	 * @param tradeDTOList
	 * @param userNickName
	 */
	private void batchSaveTradeData(TradeAndOrderDataCollector dataCollector) {
		try {
			Map<Long, List<TradeDTO>> tradeListMap = dataCollector.getTradeListMap();
			for (Entry<Long, List<TradeDTO>> map : tradeListMap.entrySet()) {
				tradeDTOService.batchInsertTradeList(new Long(map.getKey()), map.getValue());
				// MyFixedThreadPool.getTradeAndOrderFixedThreadPool()
				// .execute(new TradeSynThread(tradeDTOService, map.getKey(),
				// map.getValue()));
				Thread.sleep(500);
			}
			tradeListMap.clear();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("分用户保存订单记录 = ", e);
		}
	}

	private void batchSaveOrderData(TradeAndOrderDataCollector dataCollector) {
		try {
			Map<Long, List<OrderDTO>> orderListMap = dataCollector.getOrderListMap();
			for (Entry<Long, List<OrderDTO>> map : orderListMap.entrySet()) {
				orderDTOService.batchInsertOrderList(new Long(map.getKey()), map.getValue());
				// MyFixedThreadPool.getTradeAndOrderFixedThreadPool()
				// .execute(new OrderSynThread(orderDTOService, map.getKey(),
				// map.getValue()));
				Thread.sleep(500);
			}
			orderListMap.clear();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("分用户保存订单记录 = ", e);
		}
	}

	public void batchProcessTradeByRedisLock(Long uid, List<TbTrade> tbTradeList) throws Exception {
		RedisDistributionLock redisDistributionLock = new RedisDistributionLock(redisTemplate);
		long lockTimeOut = 0L;
		try {
			lockTimeOut = redisDistributionLock.lock(RedisConstant.RediskeyCacheGroup.TRADEDTO_TABLE_LOCKE_KEY + uid);
			if (lockTimeOut > 0) {
				this.processTbTradeData(tbTradeList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			redisDistributionLock.unlock(RedisConstant.RediskeyCacheGroup.TRADEDTO_TABLE_LOCKE_KEY + uid, lockTimeOut);
		}
	}

	/**
	 * 从TbTrade获取TradeDTO
	 * 
	 * @param tbTrade
	 * @return TradeDTO
	 */
	public TradeDTO getTradeDTO(TbTrade tbTrade) {
		TradeFullinfoGetResponse tfgr = null;
		try {
			String jdpResponseStr = tbTrade.getJdpResponse();
			tfgr = TaobaoUtils.parseResponse(jdpResponseStr, TradeFullinfoGetResponse.class);
		} catch (ApiException e) {
			e.printStackTrace();
			logger.error("从TbTrade获取TradeDTO", e);
		}
		TradeDTO tradeDTO = tfgr.getTrade();
		return tradeDTO;
	}

	/**
	 * 补充部分类型无法转换问题
	 * 
	 * @author: wy
	 * @time: 2018年1月30日 上午11:26:47
	 * @param tradeDTO
	 * @return
	 */
	private TradeDTO packageTradeDTO(TradeDTO tradeDTO) throws Exception {
		if (tradeDTO == null) {
			return null;
		}
		if (null != tradeDTO.getAdjustFeeString()) {
			tradeDTO.setAdjustFee(new BigDecimal(tradeDTO.getAdjustFeeString()));
		}
		if (null != tradeDTO.getAvailableConfirmFeeString()) {
			tradeDTO.setAvailableConfirmFee(new BigDecimal(tradeDTO.getAvailableConfirmFeeString()));
		}
		if (null != tradeDTO.getCodFeeString()) {
			tradeDTO.setCodFee(new BigDecimal(tradeDTO.getCodFeeString()));
		}
		if (null != tradeDTO.getCommissionFeeString()) {
			tradeDTO.setCommissionFee(new BigDecimal(tradeDTO.getCommissionFeeString()));
		}
		if (null != tradeDTO.getDiscountFeeString()) {
			tradeDTO.setDiscountFee(new BigDecimal(tradeDTO.getDiscountFeeString()));
		}
		if (null != tradeDTO.getPaymentString()) {
			tradeDTO.setPayment(new BigDecimal(tradeDTO.getPaymentString()));
		}
		if (null != tradeDTO.getPostFeeString()) {
			tradeDTO.setPostFee(new BigDecimal(tradeDTO.getPostFeeString()));
		}
		if (null != tradeDTO.getReceivedPaymentString()) {
			tradeDTO.setReceivedPayment(new BigDecimal(tradeDTO.getReceivedPaymentString()));
		}
		if (null != tradeDTO.getSellerCodFeeString()) {
			tradeDTO.setSellerCodFee(new BigDecimal(tradeDTO.getSellerCodFeeString()));
		}
		if (null != tradeDTO.getTotalFeeString()) {
			tradeDTO.setTotalFee(new BigDecimal(tradeDTO.getTotalFeeString()));
		}
		if (tradeDTO.getTid() != null) {
			tradeDTO.setTidStr(tradeDTO.getTid().toString());
		}
		Long uid = userInfoService.findUidBySellerNick(tradeDTO.getSellerNick());
		if (uid != null) {
			tradeDTO.setUid(uid);
		}
		//添加定金
		if(tradeDTO.getStepTradeStatus()!=null&&tradeDTO.getStepTradeStatus().equals(TradesInfo.FRONT_PAID_FINAL_NOPAID)){
			tradeDTO.setFront(tradeDTO.getStepPaidFee());
		}
		//添加尾款
		if(tradeDTO.getStepTradeStatus()!=null&&tradeDTO.getStepTradeStatus().equals(TradesInfo.FRONT_PAID_FINAL_PAID)){
			tradeDTO.setTail(tradeDTO.getStepPaidFee());
		}
		return tradeDTO;
	}
    /**
     * 计算日期是周几
     * @param tradeDTO
     * @return
     */
	private String dateToWeek(TradeDTO tradeDTO) {
		String[] weekDays = { "1", "2", "3", "4", "5", "6", "7" };
		Calendar cal = Calendar.getInstance(); // 获得一个日历
		Date datet = tradeDTO.getPayTime();
	    cal.setTime(datet);
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
		if (w < 0)
		    w = 0;
		return weekDays[w];
	}

	/**
	 * 增加子订单冗余数据
	 * 
	 * @author: wy
	 * @time: 2018年1月30日 上午11:26:31
	 * @param orderDTO
	 * @param tradeDTO
	 * @return
	 */
	private OrderDTO packageTradeToOrder(TradeDTO tradeDTO, OrderDTO orderDTO) throws Exception {
		String title="";
		if (orderDTO == null) {
			return null;
		}
		if (tradeDTO == null) {
			return orderDTO;
		}
		if(null !=orderDTO.getTitle()){
			title=orderDTO.getTitle();
		}
		Long orderNum = orderDTO.getNum();
		BeanUtils.copyProperties(tradeDTO, orderDTO);
		orderDTO.setNum(orderNum);
		if (tradeDTO.getUid() != null) {
			orderDTO.setUid(tradeDTO.getUid());
		}
		if (ValidateUtil.isNotNull(orderDTO.getRefundId())) {
			tradeDTO.setRefundFlag(true);
		}
		if (null != orderDTO.getAdjustFeeString()) {
			orderDTO.setAdjustFee(new BigDecimal(orderDTO.getAdjustFeeString()));
		}
		if (null != orderDTO.getDiscountFeeString()) {
			orderDTO.setDiscountFee(new BigDecimal(orderDTO.getDiscountFeeString()));
		}
		if (null != orderDTO.getDivideOrderFeeString()) {
			orderDTO.setDivideOrderFee(new BigDecimal(orderDTO.getDivideOrderFeeString()));
		}
		if (null != orderDTO.getMdFeeString()) {
			orderDTO.setMdFee(new BigDecimal(orderDTO.getMdFeeString()));
		}
		if (null != orderDTO.getPaymentString()) {
			orderDTO.setPayment(new BigDecimal(orderDTO.getPaymentString()));
		}
		if (null != orderDTO.getPriceString()) {
			orderDTO.setPrice(new BigDecimal(orderDTO.getPriceString()));
		}
		if (null != orderDTO.getTotalFeeString()) {
			orderDTO.setTotalFee(new BigDecimal(orderDTO.getTotalFeeString()));
		}
		if(null !=title&&!"".equals(title)){
			orderDTO.setTitle(title);
		}
		// TODO 订单导入的时候需要修改
		orderDTO.setTradeSource(1);
      
		orderDTO.setSellerNick(tradeDTO.getSellerNick());
		orderDTO.setBuyerNick(tradeDTO.getBuyerNick());
		orderDTO.setTradePayment(tradeDTO.getPayment());
		orderDTO.setTradeSellerRate(tradeDTO.getSellerRate());
		orderDTO.setReceiverName(tradeDTO.getReceiverName());
		orderDTO.setReceiverState(tradeDTO.getReceiverState());
		orderDTO.setReceiverAddress(tradeDTO.getReceiverAddress());
		orderDTO.setReceiverZip(tradeDTO.getReceiverZip());
		orderDTO.setReceiverMobile(tradeDTO.getReceiverMobile());
		orderDTO.setReceiverPhone(tradeDTO.getReceiverPhone());
		orderDTO.setTradeConsignTime(tradeDTO.getConsignTime());
		orderDTO.setReceiverCountry(tradeDTO.getReceiverCountry());
		orderDTO.setReceiverTown(tradeDTO.getReceiverTown());
		orderDTO.setShopPick(tradeDTO.getShopPick());
		orderDTO.setTid(tradeDTO.getTid());
		orderDTO.setTradeStatus(tradeDTO.getStatus());
		orderDTO.setTradeTitle(tradeDTO.getTitle());
		orderDTO.setTradeType(tradeDTO.getType());
		orderDTO.setTradeTotalFee(tradeDTO.getTotalFee());
		orderDTO.setTradeCreated(tradeDTO.getCreated());
		orderDTO.setTradePayTime(tradeDTO.getPayTime());
		orderDTO.setTradeModified(tradeDTO.getModified());
		orderDTO.setTradeEndTime(tradeDTO.getEndTime());
		orderDTO.setBuyerFlag(tradeDTO.getBuyerFlag());
		orderDTO.setSellerFlag(tradeDTO.getSellerFlag());
		orderDTO.setStepTradeStatus(tradeDTO.getStepTradeStatus());
		if (null != tradeDTO.getStepPaidFee()) {
			orderDTO.setStepPaidFee(new BigDecimal(tradeDTO.getStepPaidFee()));
		}
		orderDTO.setTradeShippingType(tradeDTO.getShippingType());
        //订单创建时间时间段
		if (orderDTO.getTradeCreated() != null) {
			orderDTO.setTradeCreatedTime(DateUtils.formatDate(orderDTO.getTradeCreated(), DateUtils.SHORT_HOUR_FORMAT));
		}
		//订单支付时间时间段
        if(orderDTO.getTradePayTime()  != null){
        	orderDTO.setTradePayTimeHms(DateUtils.formatDate(orderDTO.getTradePayTime(), DateUtils.SHORT_HOUR_FORMAT));	
        }
        //订单结束时间时间段
        if(orderDTO.getTradeEndTime()!=null){
        	orderDTO.setTradeEndTimeHms(DateUtils.formatDate(orderDTO.getTradeEndTime(), DateUtils.SHORT_HOUR_FORMAT));
        }
		if (null != tradeDTO.getBuyerCodFee()) {
			orderDTO.setBuyerCodFee(new BigDecimal(tradeDTO.getBuyerCodFee()));
		}
		if (orderDTO.getCreatedDate() == null) {
			orderDTO.setCreatedDate(tradeDTO.getCreated());
		}
		//在order中添加付款时间时周几
		if(tradeDTO.getPayTime()!=null){
			orderDTO.setWeek(dateToWeek(tradeDTO));
		}
		orderDTO.setTradeAdjustFee(tradeDTO.getAdjustFee());
		orderDTO.setTradeFrom(tradeDTO.getTradeFrom());
		orderDTO.setTradeBuyerRate(tradeDTO.getBuyerRate());
		orderDTO.setReceiverCity(tradeDTO.getReceiverCity());
		orderDTO.setReceiverDistrict(tradeDTO.getReceiverDistrict());
		//默认好评
		orderDTO.setResult(OrderSettingInfo.GOOD_RATE);
		//添加定金
		if(tradeDTO.getFront()!=null){
			orderDTO.setFront(tradeDTO.getFront());
		}
		//添加尾款
		if(tradeDTO.getTail()!=null){
			orderDTO.setTail(tradeDTO.getTail());
		}
		return orderDTO;
	}

}
