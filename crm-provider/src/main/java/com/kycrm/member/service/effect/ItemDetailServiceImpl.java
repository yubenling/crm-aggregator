package com.kycrm.member.service.effect;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.redis.RedisLockService;
import com.kycrm.member.dao.effect.IItemTempTradeDao;
import com.kycrm.member.dao.effect.IItemTempTradeHistoryDao;
import com.kycrm.member.domain.entity.base.BaseListEntity;
import com.kycrm.member.domain.entity.effect.ItemTempTrade;
import com.kycrm.member.domain.entity.effect.ItemTempTradeHistory;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.vo.effect.CustomerDetailVO;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.GetCurrentPageUtil;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.TradesInfo;

@MyDataSource
@Service("itemDetailService")
public class ItemDetailServiceImpl implements IItemDetailService{
	
	private Logger logger = LoggerFactory.getLogger(ItemDetailServiceImpl.class);
	
	@Autowired
	private IItemTempTradeDao itemTmepTradeDao;
	
	@Autowired
	private IItemTempTradeHistoryDao itemTmepTradeHistoryDao;
	
	@Autowired
	private RedisLockService lockService;
	
	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;
	
	@Autowired
	private IOrderDTOService orderDTOService;
	
	@Autowired
	private ICacheService cacheService;
	
	@Override
	public Boolean doCreateTable(Long uid){
		if (uid == null) {
			return false;
		}
		List<String> tables = this.itemTmepTradeDao.tableIsExist(uid);
		if (tables != null && !tables.isEmpty()) {
			return true;
		}
		try {
			this.itemTmepTradeDao.doCreateTable(uid);
			this.itemTmepTradeDao.addItemTempTradeTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
	/**
	 * limitItemDetail(效果分析商品详情的分页统计)
	 */
	@Override
	public Map<String, Object> limitItemDetail(Long uid,CustomerDetailVO itemVO){
		if(uid == null || itemVO == null){
			return null;
		}
		if(itemVO.getPageNo() == null || itemVO.getPageNo() <= 0){
			itemVO.setPageNo(1);
		}
		CustomerDetailVO queryVO = lockService.getValue(uid + "-effectParam", CustomerDetailVO.class);
		if(queryVO == null || queryVO.getMsgId() == null){
			throw new RuntimeException("从redis取出查询条件为空");
		}
		this.doCreateTable(uid);
		this.doCreateItemHistory(uid);
		Integer startRows = (itemVO.getPageNo() - 1) * ConstantUtils.PAGE_SIZE_MIDDLE;
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("uid", uid);
		queryMap.put("msgId", queryVO.getMsgId());
		queryMap.put("tradeFrom", itemVO.getOrderSource());
		queryMap.put("tradeStatus", itemVO.getBuyerType());
		queryMap.put("bTime", queryVO.getbTime());
		queryMap.put("eTime", queryVO.geteTime());
		queryMap.put("itemId", itemVO.getItemId());
		queryMap.put("startRows", startRows);
		queryMap.put("pageSize", ConstantUtils.PAGE_SIZE_MIDDLE);
		if(itemVO.getSortData() != null && !"".equals(itemVO.getSortData())){
			String sortType = itemVO.getSortType();
			if("price" == sortType || "price".equals(sortType)){
				queryMap.put("price", "price");
			}else if("buyerNum" == sortType || "buyerNum".equals(sortType)){
				queryMap.put("buyerNum", "buyerNum");
			}else if("payment" == sortType || "payment".equals(sortType)){
				queryMap.put("payment", "payment");
			}else if("tradeNum" == sortType || "tradeNum".equals(sortType)){
				queryMap.put("tradeNum", "tradeNum");
			}else if("itemNum" == sortType || "itemNum".equals(sortType)){
				queryMap.put("itemNum", "itemNum");
			}else {
				queryMap.put("id", "id");
			}
			String sortData = itemVO.getSortData();
			if(sortData != null && !"".equals(sortData)){
				if("ASC" == sortData || "ASC".equals(sortData)){
					queryMap.put("sortType", "sortType");
				}else{
					queryMap.put("sortType", null);
				}
			}else {
				queryMap.put("id", null);
			}
		}
		Integer itemCount = null;
		Map<String, Object> resultMap = new HashMap<>();
		Boolean isHistory = this.cacheService.get(RedisConstant.RediskeyCacheGroup.MSG_IS_HISTORY_DATA,
				"msgId-" + queryVO.getMsgId(), Boolean.class);
		if(isHistory == null){
			isHistory = false;
		}
		if(isHistory){
			List<ItemTempTradeHistory> itemDetailHistories = itemTmepTradeHistoryDao.listItemDetail(queryMap);
			itemCount = itemTmepTradeHistoryDao.countItemDetail(queryMap);
			if(itemDetailHistories != null && !itemDetailHistories.isEmpty()){
				for (ItemTempTradeHistory itemTempTradeHistory : itemDetailHistories) {
					if(itemTempTradeHistory.getBuyerNick() == null || "".equals(itemTempTradeHistory.getBuyerNick())){
						itemTempTradeHistory.setBuyerNick(0 + "");
					}
				}
			}
			resultMap.put("data", itemDetailHistories);
		}else {
			List<ItemTempTrade> itemDetails = itemTmepTradeDao.listItemDetail(queryMap);
			itemCount = itemTmepTradeDao.countItemDetail(queryMap);
			if(itemDetails != null && !itemDetails.isEmpty()){
				for (ItemTempTrade itemTempTrade : itemDetails) {
					if(itemTempTrade.getBuyerNick() == null || "".equals(itemTempTrade.getBuyerNick())){
						itemTempTrade.setBuyerNick(0 + "");
					}
				}
			}
			resultMap.put("data", itemDetails);
		}
		resultMap.put("totalPage", GetCurrentPageUtil.getTotalPage(itemCount, ConstantUtils.PAGE_SIZE_MIDDLE));
		resultMap.put("pageNo", itemVO.getPageNo());
		resultMap.put("itemCount", itemCount);
		return resultMap;
	}
	
	/**
	 * 添加到itemTempTrade
	 */
	@Override
	public void saveItemDetail(Long uid,MsgSendRecord msg, List<TradeDTO> tradeDTOs) throws Exception{
		List<OrderDTO> orderDTOs = this.queryOrderByTid(uid, msg, tradeDTOs);
		if(orderDTOs == null || orderDTOs.isEmpty()){
			logger.info("效果分析同步临时订单库，符合发送记录的订单为空~~~~~~~~!~~~~~~~~");
			return;
		}
		logger.info("效果分析同步临时订单库，符合发送记录的订单:uid:" + uid + "个数为：" + orderDTOs.size());
		Boolean createStatus = this.doCreateTable(uid);
		if(false == createStatus){
			return;
		}
		Boolean createTable = this.doCreateItemHistory(uid);
		if(false == createTable){
			return;
		}
		List<ItemTempTrade> itemTempTrades = new ArrayList<>();
		for (OrderDTO orderDTO : orderDTOs) {
			if(orderDTO.getTradeCreated() != null && orderDTO.getTradeCreated().getTime() < msg.getSendCreat().getTime()){
				continue;
			}
			ItemTempTrade itemTempTrade = new ItemTempTrade();
			itemTempTrade.setBuyerNick(orderDTO.getBuyerNick());
			itemTempTrade.setCreated(orderDTO.getCreatedDate());
			itemTempTrade.setItemId(orderDTO.getNumIid());
			itemTempTrade.setMsgId(msg.getId());
			itemTempTrade.setNum(orderDTO.getNum());
			itemTempTrade.setPayment(orderDTO.getPayment());
			itemTempTrade.setPrice(orderDTO.getPrice());
			itemTempTrade.setTitle(orderDTO.getTitle());
			itemTempTrade.setTradeFrom(orderDTO.getOrderFrom());
			itemTempTrade.setMsgCreated(msg.getSendCreat());
			itemTempTrade.setTid(orderDTO.getTid());
			itemTempTrade.setUid(uid);
			//判断订单状态
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
			map.put("uid", itemTempTrade.getUid());
			map.put("msgId", itemTempTrade.getMsgId());
			map.put("itemId", itemTempTrade.getItemId());
			map.put("tid", orderDTO.getTid());
			if(orderDTO.getTbrefundStatus() != null && "SUCCESS".equals(orderDTO.getTbrefundStatus())){
//				if(TradesInfo.TRADE_FINISHED.equals(orderDTO.getStatus())){
//					createFinishedItemTrade(uid, map, itemTempTrade, itemTempTrades);
//				}else {
					createRefundItemTrade(uid, map, itemTempTrade, itemTempTrades);
//				}
			}else {
				if(payStatusList.contains(orderDTO.getStatus())){
					createFinishedItemTrade(uid, map, itemTempTrade, itemTempTrades);
				}else if(waitStatusList.contains(orderDTO.getStatus())){
					createWaitPayItemTrade(uid, map, itemTempTrade, itemTempTrades);
				}else {
					itemTempTrade.setTradeStatus("create");
					itemTempTrade.setUid(uid);
					itemTempTrades.add(itemTempTrade);
				}
			}
		}
		
		int start = 0, end = 0;
		int totalSize = itemTempTrades.size();
		int pageSize = ConstantUtils.PROCESS_PAGE_SIZE_MAX.intValue();
		if(totalSize % pageSize == 0){
			end = totalSize / pageSize;
		}else {
			end = (totalSize + pageSize) / pageSize;
		}
		BaseListEntity<ItemTempTrade> entityList = null;
		Date nowDate = new Date();
		while(start < end){
			entityList = new BaseListEntity<ItemTempTrade>();
			if(start == (end - 1)){
				entityList.setEntityList(itemTempTrades.subList(start * pageSize, itemTempTrades.size()));
			}else {
				entityList.setEntityList(itemTempTrades.subList(start * pageSize, (start + 1) * pageSize));
			}
			entityList.setUid(uid);
			try {
				if(DateUtils.getDiffDay(msg.getSendCreat(), nowDate) < 15){
					itemTmepTradeDao.saveItemDetails(entityList);
					itemTmepTradeHistoryDao.saveItemDetails(entityList);
				}else {
					itemTmepTradeHistoryDao.saveItemDetails(entityList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			start ++;
		}
	}
	
	/**
	 * createRefundItemTrade(创建等待付款的临时订单，用于效果分析商品详情查询)
	 * @Title: createRefundItemTrade 
	 * @param @param uid
	 * @param @param map
	 * @param @param itemTempTrade
	 * @param @param itemTempTrades 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void createRefundItemTrade(Long uid, Map<String, Object> map,
			ItemTempTrade itemTempTrade, List<ItemTempTrade> itemTempTrades){
		itemTempTrade.setTradeStatus("refund");
		map.put("tradeStatus", "refund");
		List<Long> refundIds = itemTmepTradeDao.isExistItemTrade(map);
		if(refundIds == null || refundIds.isEmpty()){
			ItemTempTrade refundTempTrade = this.createItemTempTrade(uid, itemTempTrade, "refund");
			itemTempTrades.add(refundTempTrade);
			map.put("tradeStatus", "success");
			List<Long> ids = itemTmepTradeDao.isExistItemTrade(map);
			if(ids == null || ids.isEmpty()){
				ItemTempTrade successItemTrade = this.createItemTempTrade(uid, itemTempTrade, "success");
				if(successItemTrade != null){
					itemTempTrades.add(successItemTrade);
				}
				map.put("tradeStatus", "waitPay");
				List<Long> waitPayIds = itemTmepTradeDao.isExistItemTrade(map);
				if(waitPayIds == null || waitPayIds.isEmpty()){
					ItemTempTrade waitPayItemTrade = this.createItemTempTrade(uid, itemTempTrade, "waitPay");
					if(waitPayItemTrade != null){
						itemTempTrades.add(waitPayItemTrade);
					}
					map.put("tradeStatus", "create");
					List<Long> createIds = itemTmepTradeDao.isExistItemTrade(map);
					if(createIds == null || createIds.isEmpty()){
						ItemTempTrade createItemTrade = this.createItemTempTrade(uid, itemTempTrade, "create");
						if(createItemTrade != null){
							itemTempTrades.add(createItemTrade);
						}
					}
				}
			}
		}
	}
	
	/**
	 * createWaitPayItemTrade(创建等待付款的临时订单，用于效果分析商品详情查询)
	 * @Title: createWaitPayItemTrade 
	 * @param @param uid
	 * @param @param map
	 * @param @param itemTempTrade
	 * @param @param itemTempTrades 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void createWaitPayItemTrade(Long uid, Map<String, Object> map,
			ItemTempTrade itemTempTrade, List<ItemTempTrade> itemTempTrades){
		itemTempTrade.setTradeStatus("waitPay");
		map.put("tradeStatus", "waitPay");
		List<Long> waitPayIds = itemTmepTradeDao.isExistItemTrade(map);
		if(waitPayIds == null || waitPayIds.isEmpty()){
			ItemTempTrade waitPayTempTrade = this.createItemTempTrade(uid, itemTempTrade, "waitPay");
			itemTempTrades.add(waitPayTempTrade);
			map.put("tradeStatus", "create");
			List<Long> createIds = itemTmepTradeDao.isExistItemTrade(map);
			if(createIds == null || createIds.isEmpty()){
				ItemTempTrade createItemTrade = this.createItemTempTrade(uid, itemTempTrade, "create");
				if(createItemTrade != null){
					itemTempTrades.add(createItemTrade);
				}
			}
			
		}
	}
	
	/**
	 * createFinishedItemTrade(创建成功的临时订单，用于效果分析商品详情查询)
	 * @Title: createFinishedItemTrade 
	 * @param @param uid
	 * @param @param map
	 * @param @param itemTempTrade
	 * @param @param itemTempTrades 设定文件 
	 * @return void 返回类型 
	 * @throws
	 */
	public void createFinishedItemTrade(Long uid, Map<String, Object> map,
			ItemTempTrade itemTempTrade, List<ItemTempTrade> itemTempTrades){
		itemTempTrade.setTradeStatus("success");
		map.put("tradeStatus", "success");
		List<Long> successIds = itemTmepTradeDao.isExistItemTrade(map);
		if(successIds == null || successIds.isEmpty()){
			ItemTempTrade successTempTrade = this.createItemTempTrade(uid, itemTempTrade, "success");
			itemTempTrades.add(successTempTrade);
			map.put("tradeStatus", "waitPay");
			List<Long> waitPayIds = itemTmepTradeDao.isExistItemTrade(map);
			if(waitPayIds == null || waitPayIds.isEmpty()){
				ItemTempTrade waitPayItemTrade = this.createItemTempTrade(uid, itemTempTrade, "waitPay");
				if(waitPayItemTrade != null){
					itemTempTrades.add(waitPayItemTrade);
				}
				map.put("tradeStatus", "create");
				List<Long> createIds = itemTmepTradeDao.isExistItemTrade(map);
				if(createIds == null ||createIds.isEmpty()){
					ItemTempTrade createItemTrade = this.createItemTempTrade(uid, itemTempTrade, "create");
					if(createItemTrade != null){
						itemTempTrades.add(createItemTrade);
					}
				}
			}
			
		}
	}
	
	public ItemTempTrade createItemTempTrade(Long uid,ItemTempTrade itemTempTrade,String status){
		if(itemTempTrade == null){
			return null;
		}
		ItemTempTrade itemTrade = new ItemTempTrade();
		itemTrade.setBuyerNick(itemTempTrade.getBuyerNick());
		itemTrade.setCreated(itemTempTrade.getCreated());
		itemTrade.setItemId(itemTempTrade.getItemId());
		itemTrade.setMsgId(itemTempTrade.getMsgId());
		itemTrade.setNum(itemTempTrade.getNum());
		itemTrade.setPayment(itemTempTrade.getPayment());
		itemTrade.setPrice(itemTempTrade.getPrice());
		itemTrade.setTitle(itemTempTrade.getTitle());
		itemTrade.setTradeFrom(itemTempTrade.getTradeFrom());
		itemTrade.setTradeStatus(status);
		itemTrade.setUid(uid);
		itemTrade.setTid(itemTempTrade.getTid());
		itemTrade.setMsgCreated(itemTempTrade.getMsgCreated());
		itemTrade.setStepTradeStatus(itemTempTrade.getStepTradeStatus());
		return itemTrade;
	}
	
	/**
	 * 删除15天之前的临时商品订单表数据
	 */
	@Override
	public void deleteItemTempTrade(Long uid,Date fifteenAgoDate){
		if(uid == null || fifteenAgoDate == null){
			return;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("fifteenAgoDate", fifteenAgoDate);
		while(true){
			Long maxId = itemTmepTradeDao.findMaxIdByTime(map);
			if(maxId != null){
				itemTmepTradeDao.deleteDataById(maxId);
			}else {
				return;
			}
		}
		
	}

	/**
	 * 创建表itemTempTradeHistory
	 */
	@Override
	public Boolean doCreateItemHistory(Long uid) {
		if (uid == null) {
			return false;
		}
		List<String> isExist = itemTmepTradeHistoryDao.tableIsExist(uid);
		if (isExist != null && !isExist.isEmpty()) {
			return true;
		}
		try {
			itemTmepTradeHistoryDao.doCreateTable(uid);
			this.itemTmepTradeHistoryDao.addItemTempTradeHistoryTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public List<OrderDTO> queryOrderByMsgRecord(Long uid, MsgSendRecord msgSendRecord,Date bTime,Date eTime){
		List<OrderDTO> orderDTOs = new ArrayList<OrderDTO>();
		SmsRecordVO smsRecordVO = new SmsRecordVO();
		smsRecordVO.setUid(uid);
		smsRecordVO.setStatus(2);
		smsRecordVO.setType(msgSendRecord.getType());
		smsRecordVO.setMsgId(msgSendRecord.getId());
		if(msgSendRecord.getSucceedCount() != null){
			if(msgSendRecord.getSucceedCount() > Constants.PROCESS_PAGE_SIZE_OVER){
				Integer totalSuccessPhone = smsRecordDTOService.
						countSuccessRecordByMsgId(uid,msgSendRecord.getId());
				int start = 0, end = 0;
				if(totalSuccessPhone % Constants.PROCESS_PAGE_SIZE_OVER == 0){
					end = totalSuccessPhone / Constants.PROCESS_PAGE_SIZE_OVER;
				}else {
					end = (totalSuccessPhone + Constants.PROCESS_PAGE_SIZE_OVER) / Constants.PROCESS_PAGE_SIZE_OVER;
				}
				while(start < end){
					start ++;
					smsRecordVO.setCurrentRows(Constants.PROCESS_PAGE_SIZE_OVER);
					smsRecordVO.setPageNo(start);
					List<String> pagePhoneList = smsRecordDTOService.queryPhoneList(uid, smsRecordVO);
					if(pagePhoneList == null || pagePhoneList.isEmpty()){
						continue;
					}
					List<OrderDTO> orderDTOList = orderDTOService.listOrderByPhone(uid, pagePhoneList, bTime,eTime);
					orderDTOs.addAll(orderDTOList);
					
				}
			}else {
				List<String> phoneList = smsRecordDTOService.queryPhoneList(uid, smsRecordVO);
				if(phoneList == null || phoneList.isEmpty()){
					return null;
				}
				orderDTOs = orderDTOService.listOrderByPhone(uid, phoneList, bTime,eTime);
			}
		}
		return orderDTOs;
	}
	
	@Override
	public List<ItemTempTrade> listItemDetail(Long uid,CustomerDetailVO itemVO,Boolean isHistory){
		if(uid == null || itemVO == null){
			return null;
		}
		if(itemVO.getPageNo() == null || itemVO.getPageNo() <= 0){
			itemVO.setPageNo(1);
		}
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("uid", uid);
		queryMap.put("msgId", itemVO.getMsgId());
		queryMap.put("tradeStatus", "success");
		queryMap.put("startRows", itemVO.getStartRows());
		queryMap.put("pageSize", itemVO.getCurrentRows());
		List<ItemTempTrade> itemDetails = itemTmepTradeDao.listItemDetail(queryMap);
		return itemDetails;
	}
	
	
	public List<OrderDTO> queryOrderByTid(Long uid, MsgSendRecord msgSendRecord,List<TradeDTO> tradeDTOs){
		if(uid == null || tradeDTOs == null || tradeDTOs.isEmpty()){
			return null;
		}
		List<OrderDTO> orderDTOs = new ArrayList<>();
		int start = 0, end = 0;
		int totalSize = tradeDTOs.size();
		int pageSize = ConstantUtils.PROCESS_PAGE_SIZE_MAX.intValue();
		if(totalSize % pageSize == 0){
			end = totalSize / pageSize;
		}else {
			end = (totalSize + pageSize) / pageSize;
		}
		while(start < end){
			List<TradeDTO> queryTradeDTOs = new ArrayList<>();
			List<Long> tids = new ArrayList<>();
			if((start + 1) == end){
				queryTradeDTOs = tradeDTOs.subList(start * pageSize, tradeDTOs.size());
			}else {
				queryTradeDTOs = tradeDTOs.subList(start * pageSize, (start + 1) * pageSize);
			}
			for (TradeDTO tradeDTO : queryTradeDTOs) {
				if(tradeDTO.getTid() == null){
					continue;
				}
				tids.add(tradeDTO.getTid());
			}
			
			List<OrderDTO> limitOrderDTOs = orderDTOService.listOrderByTids(uid, tids);
			orderDTOs.addAll(limitOrderDTOs);
			start ++;
		}
		return orderDTOs;
	}
	
	/**
	 * deleteDataByMsgId(根据msgId删除对应记录)
	 */
	@Override
	public void deleteDataByMsgId(Long uid, Long msgId){
		if(uid == null || msgId == null){
			return;
		}
		this.itemTmepTradeDao.deleteDataByMsgId(uid, msgId);
	}
	
	@Override
	public void saveStepItemDetail(Long uid,MsgSendRecord msg, List<TradeDTO> tradeDTOs) throws Exception{
		List<OrderDTO> orderDTOs = this.queryOrderByTid(uid, msg, tradeDTOs);
		if(orderDTOs == null || orderDTOs.isEmpty()){
			logger.info("效果分析同步临时订单库，符合发送记录的订单为空~~~~~~~~!~~~~~~~~");
			return;
		}
		logger.info("效果分析同步临时订单库，符合发送记录的订单:uid:" + uid + "个数为：" + orderDTOs.size());
		Boolean createStatus = this.doCreateTable(uid);
		if(false == createStatus){
			return;
		}
		Boolean createTable = this.doCreateItemHistory(uid);
		if(false == createTable){
			return;
		}
		List<ItemTempTrade> itemTempTrades = new ArrayList<>();
		for (OrderDTO orderDTO : orderDTOs) {
//			if(orderDTO.getTradeCreated() != null && orderDTO.getTradeCreated().getTime() < msg.getSendCreat().getTime()){
//				continue;
//			}
			ItemTempTrade itemTempTrade = new ItemTempTrade();
			itemTempTrade.setBuyerNick(orderDTO.getBuyerNick());
			itemTempTrade.setCreated(orderDTO.getCreatedDate());
			itemTempTrade.setItemId(orderDTO.getNumIid());
			itemTempTrade.setMsgId(msg.getId());
			itemTempTrade.setNum(orderDTO.getNum());
			itemTempTrade.setPayment(orderDTO.getPayment());
			itemTempTrade.setPrice(orderDTO.getPrice());
			itemTempTrade.setTitle(orderDTO.getTitle());
			itemTempTrade.setTradeFrom(orderDTO.getOrderFrom());
			itemTempTrade.setMsgCreated(msg.getSendCreat());
			itemTempTrade.setTid(orderDTO.getTid());
			itemTempTrade.setUid(uid);
			itemTempTrade.setStepTradeStatus(orderDTO.getStepTradeStatus());
			//判断订单状态
			List<String> payStatusList = new ArrayList<String>();
			payStatusList.add(TradesInfo.TRADE_BUYER_SIGNED);
			payStatusList.add(TradesInfo.WAIT_SELLER_SEND_GOODS);
			payStatusList.add(TradesInfo.WAIT_BUYER_CONFIRM_GOODS);
			Map<String, Object> map = new HashMap<>();
			map.put("uid", itemTempTrade.getUid());
			map.put("msgId", itemTempTrade.getMsgId());
			map.put("itemId", itemTempTrade.getItemId());
			map.put("tid", orderDTO.getTid());
			if(TradesInfo.TRADE_CLOSED_BY_TAOBAO.equals(orderDTO.getTradeStatus())){
				map.put("tradeStatus", "closed");
				List<Long> closedIds = itemTmepTradeDao.isExistItemTrade(map);
				if(closedIds == null || closedIds.isEmpty()){
					ItemTempTrade closedTemp = this.createItemTempTrade(uid, itemTempTrade, "closed");
					itemTempTrades.add(closedTemp);
				}
			}else if(TradesInfo.TRADE_FINISHED.equals(orderDTO.getTradeStatus())){
				createStepFinishedTempTrade(itemTempTrade, map, uid, itemTempTrades);
			}else {
				if(orderDTO.getTbrefundStatus() != null && "SUCCESS".equals(orderDTO.getTbrefundStatus())){
					itemTempTrade.setTradeStatus("refund");
					map.put("tradeStatus", "refund");
					List<Long> refundIds = itemTmepTradeDao.isExistItemTrade(map);
					if(refundIds == null || refundIds.isEmpty()){
						ItemTempTrade refundTempTrade = this.createItemTempTrade(uid, itemTempTrade, "refund");
						itemTempTrades.add(refundTempTrade);
						if(TradesInfo.FRONT_PAID_FINAL_PAID.equals(orderDTO.getStepTradeStatus())){
							createStepPaidFinalTmepTrade(uid, itemTempTrade, map, itemTempTrades);
						}else {
							createStepWaitPayTempTrade(uid, itemTempTrade, map, itemTempTrades);
						}
					}
				}else {
					if(payStatusList.contains(orderDTO.getStatus())){
						createStepPaidFinalTmepTrade(uid, itemTempTrade, map, itemTempTrades);
					}else if(TradesInfo.WAIT_BUYER_PAY.contains(orderDTO.getStatus())){
						createStepWaitPayTempTrade(uid, itemTempTrade, map, itemTempTrades);
					}else {
						itemTempTrade.setTradeStatus("create");
						itemTempTrade.setUid(uid);
						itemTempTrades.add(itemTempTrade);
					}
				}
			}
		}
		
		int start = 0, end = 0;
		int totalSize = itemTempTrades.size();
		int pageSize = ConstantUtils.PROCESS_PAGE_SIZE_MAX.intValue();
		if(totalSize % pageSize == 0){
			end = totalSize / pageSize;
		}else {
			end = (totalSize + pageSize) / pageSize;
		}
		BaseListEntity<ItemTempTrade> entityList = null;
		Date nowDate = new Date();
		while(start < end){
			entityList = new BaseListEntity<ItemTempTrade>();
			if(start == (end - 1)){
				entityList.setEntityList(itemTempTrades.subList(start * pageSize, itemTempTrades.size()));
			}else {
				entityList.setEntityList(itemTempTrades.subList(start * pageSize, (start + 1) * pageSize));
			}
			entityList.setUid(uid);
			try {
				if(DateUtils.getDiffDay(msg.getSendCreat(), nowDate) < 15){
					itemTmepTradeDao.saveItemDetails(entityList);
					itemTmepTradeHistoryDao.saveItemDetails(entityList);
				}else {
					itemTmepTradeHistoryDao.saveItemDetails(entityList);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			start ++;
		}
	}
	
	
	/*public ItemTempTrade createNewItemTempTrade(ItemTempTrade oldItemTempTrade){
	if(oldItemTempTrade == null){
		logger.info("oldItemTempTrade为NULL");
		return null;
	}
	ItemTempTrade itemTempTrade = new ItemTempTrade();
	itemTempTrade.setBuyerNick(oldItemTempTrade.getBuyerNick());
	itemTempTrade.setCreated(oldItemTempTrade.getCreated());
	itemTempTrade.setItemId(oldItemTempTrade.getItemId());
	itemTempTrade.setMsgCreated(oldItemTempTrade.getMsgCreated());
	itemTempTrade.setMsgId(oldItemTempTrade.getMsgId());
	itemTempTrade.setNum(oldItemTempTrade.getNum());
	itemTempTrade.setPayment(oldItemTempTrade.getPayment());
	itemTempTrade.setPrice(oldItemTempTrade.getPrice());
	itemTempTrade.setStepTradeStatus(oldItemTempTrade.getStepTradeStatus());
	itemTempTrade.setTid(oldItemTempTrade.getTid());
	itemTempTrade.setTitle(oldItemTempTrade.getTitle());
	itemTempTrade.setTradeFrom(oldItemTempTrade.getTradeFrom());
	itemTempTrade.setTradeStatus(oldItemTempTrade.getTradeStatus());
	itemTempTrade.setUid(oldItemTempTrade.getUid());
	return itemTempTrade;
}*/

/**
 * createStepWaitPayTempTrade(订单状态为等待付款，创建等待付款状态以及历史状态的临时订单)
 * @Title: createStepWaitPayTempTrade 
 * @param @param uid
 * @param @param itemTempTrade
 * @param @param map
 * @param @param itemTempTrades 设定文件 
 * @return void 返回类型 
 * @throws
 */
public void createStepWaitPayTempTrade(Long uid, ItemTempTrade itemTempTrade,
		Map<String, Object> map, List<ItemTempTrade> itemTempTrades){
	itemTempTrade.setTradeStatus("waitPay");
	map.put("tradeStatus", "waitPay");
	List<Long> waitPayIds = itemTmepTradeDao.isExistItemTrade(map);
	if(waitPayIds == null || waitPayIds.isEmpty()){
		ItemTempTrade waitPayTempTrade = this.createItemTempTrade(uid, itemTempTrade, "waitPay");
		itemTempTrades.add(waitPayTempTrade);
		map.put("tradeStatus", "create");
		List<Long> createIds = itemTmepTradeDao.isExistItemTrade(map);
		if(createIds == null || createIds.isEmpty()){
			ItemTempTrade createItemTrade = this.createItemTempTrade(uid, itemTempTrade, "create");
			if(createItemTrade != null){
				itemTempTrades.add(createItemTrade);
			}
		}
		
	}
}

/**
 * createStepPaidFinalTmepTrade(订单状态为付款完成，创建付款完成状态以及历史状态的临时订单)
 * @Title: createStepPaidFinalTmepTrade 
 * @param  设定文件 
 * @return void 返回类型 
 * @throws
 */
public void createStepPaidFinalTmepTrade(Long uid, ItemTempTrade itemTempTrade,
		Map<String, Object> map, List<ItemTempTrade> itemTempTrades){
	itemTempTrade.setTradeStatus("paidFinal");
	map.put("tradeStatus", "paidFinal");
	List<Long> successIds = itemTmepTradeDao.isExistItemTrade(map);
	if(successIds == null || successIds.isEmpty()){
		ItemTempTrade successTempTrade = this.createItemTempTrade(uid, itemTempTrade, "paidFinal");
		itemTempTrades.add(successTempTrade);
		map.put("tradeStatus", "waitPay");
		List<Long> waitPayIds = itemTmepTradeDao.isExistItemTrade(map);
		if(waitPayIds == null || waitPayIds.isEmpty()){
			ItemTempTrade waitPayItemTrade = this.createItemTempTrade(uid, itemTempTrade, "waitPay");
			if(waitPayItemTrade != null){
				itemTempTrades.add(waitPayItemTrade);
			}
			map.put("tradeStatus", "create");
			List<Long> createIds = itemTmepTradeDao.isExistItemTrade(map);
			if(createIds == null ||createIds.isEmpty()){
				ItemTempTrade createItemTrade = this.createItemTempTrade(uid, itemTempTrade, "create");
				if(createItemTrade != null){
					itemTempTrades.add(createItemTrade);
				}
			}
		}
		
	}
}


/**
 * createStepFinishedTempTrade(订单状态为成交，创建成交状态以及历史状态的临时订单)
 * @Title: createStepFinishedTempTrade 
 * @param @param itemTempTrade
 * @param @param map
 * @param @param uid
 * @param @param itemTempTrades 设定文件 
 * @return void 返回类型 
 * @throws
 */
public void createStepFinishedTempTrade(ItemTempTrade itemTempTrade, Map<String, Object> map,
		Long uid, List<ItemTempTrade> itemTempTrades){
	itemTempTrade.setTradeStatus("success");
	map.put("tradeStatus", "success");
	List<Long> successIds = itemTmepTradeDao.isExistItemTrade(map);
	if(successIds == null || successIds.isEmpty()){
		ItemTempTrade successTempTrade = this.createItemTempTrade(uid, itemTempTrade, "success");
		itemTempTrades.add(successTempTrade);
		map.put("tradeStatus", "waitPay");
		List<Long> waitPayIds = itemTmepTradeDao.isExistItemTrade(map);
		if(waitPayIds == null || waitPayIds.isEmpty()){
			ItemTempTrade waitPayItemTrade = this.createItemTempTrade(uid, itemTempTrade, "waitPay");
			if(waitPayItemTrade != null){
				itemTempTrades.add(waitPayItemTrade);
			}
			map.put("tradeStatus", "create");
			List<Long> createIds = itemTmepTradeDao.isExistItemTrade(map);
			if(createIds == null ||createIds.isEmpty()){
				ItemTempTrade createItemTrade = this.createItemTempTrade(uid, itemTempTrade, "create");
				if(createItemTrade != null){
					itemTempTrades.add(createItemTrade);
				}
			}
		}
		
	}
}
	
	
	/**
	 * limitStepItemDetail(预售效果分析商品详情的分页统计)
	 */
	@Override
	public Map<String, Object> limitStepItemDetail(Long uid,CustomerDetailVO itemVO){
		if(uid == null || itemVO == null){
			return null;
		}
		if(itemVO.getPageNo() == null || itemVO.getPageNo() <= 0){
			itemVO.setPageNo(1);
		}
		CustomerDetailVO queryVO = lockService.getValue(uid + "-effectParam", CustomerDetailVO.class);
		if(queryVO == null || queryVO.getMsgId() == null){
			throw new RuntimeException("从redis取出查询条件为空");
		}
		this.doCreateTable(uid);
		this.doCreateItemHistory(uid);
		Integer startRows = (itemVO.getPageNo() - 1) * ConstantUtils.PAGE_SIZE_MIDDLE;
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("uid", uid);
		queryMap.put("msgId", queryVO.getMsgId());
		queryMap.put("tradeFrom", itemVO.getOrderSource());
		queryMap.put("tradeStatus", itemVO.getBuyerType());
		if("paidFinal".equals(queryVO.getOrderSource())){
			queryMap.put("stepTradeStatus", TradesInfo.FRONT_PAID_FINAL_PAID);
		}/*else {
			queryMap.put("tradeFrom", queryVO.getOrderSource());
		}*/
		List<String> noStatusList = new ArrayList<String>();
		if("success".equals(itemVO.getBuyerType())){
			noStatusList.add("refund");
			queryMap.put("noStatusList", noStatusList);
		}else if ("paidFinal".equals(itemVO.getBuyerType())) {
			noStatusList.add("refund");
			queryMap.put("noStatusList", noStatusList);
		}else if ("waitPay".equals(itemVO.getBuyerType())) {
			noStatusList.add("refund");noStatusList.add("success");
			noStatusList.add("paidFinal");
			queryMap.put("noStatusList", noStatusList);
		}else if ("refund".equals(itemVO.getBuyerType())) {
			queryMap.put("noStatusList", null);
		}
		queryMap.put("bTime", queryVO.getbTime());
		if(itemVO.geteTime() != null){
			queryMap.put("eTime", queryVO.geteTime());
		}else {
			queryMap.put("eTime", queryVO.geteTime());
		}
		queryMap.put("itemId", itemVO.getItemId());
		queryMap.put("startRows", startRows);
		queryMap.put("pageSize", ConstantUtils.PAGE_SIZE_MIDDLE);
		if(itemVO.getSortData() != null && !"".equals(itemVO.getSortData())){
			String sortType = itemVO.getSortType();
			if("price" == sortType || "price".equals(sortType)){
				queryMap.put("price", "price");
			}else if("buyerNum" == sortType || "buyerNum".equals(sortType)){
				queryMap.put("buyerNum", "buyerNum");
			}else if("payment" == sortType || "payment".equals(sortType)){
				queryMap.put("payment", "payment");
			}else if("tradeNum" == sortType || "tradeNum".equals(sortType)){
				queryMap.put("tradeNum", "tradeNum");
			}else if("itemNum" == sortType || "itemNum".equals(sortType)){
				queryMap.put("itemNum", "itemNum");
			}else {
				queryMap.put("id", "id");
			}
			String sortData = itemVO.getSortData();
			if(sortData != null && !"".equals(sortData)){
				if("ASC" == sortData || "ASC".equals(sortData)){
					queryMap.put("sortType", "sortType");
				}else{
					queryMap.put("sortType", null);
				}
			}else {
				queryMap.put("id", null);
			}
		}
		Integer itemCount = null;
		Map<String, Object> resultMap = new HashMap<>();
		Boolean isHistory = this.cacheService.get(RedisConstant.RediskeyCacheGroup.MSG_IS_HISTORY_DATA,
				"msgId-" + queryVO.getMsgId(), Boolean.class);
		if(isHistory == null){
			isHistory = false;
		}
		if(isHistory){
			List<ItemTempTradeHistory> itemDetailHistories = itemTmepTradeHistoryDao.listStepItemDetail(queryMap);
			itemCount = itemTmepTradeHistoryDao.countItemDetail(queryMap);
			if(itemDetailHistories != null && !itemDetailHistories.isEmpty()){
				for (ItemTempTradeHistory itemTempTradeHistory : itemDetailHistories) {
					if(itemTempTradeHistory.getBuyerNick() == null || "".equals(itemTempTradeHistory.getBuyerNick())){
						itemTempTradeHistory.setBuyerNick(0 + "");
					}
				}
			}
			resultMap.put("data", itemDetailHistories);
		}else {
			List<ItemTempTrade> itemDetails = itemTmepTradeDao.listStepItemDetail(queryMap);
			itemCount = itemTmepTradeDao.countItemDetail(queryMap);
			if(itemDetails != null && !itemDetails.isEmpty()){
				for (ItemTempTrade itemTempTrade : itemDetails) {
					if(itemTempTrade.getBuyerNick() == null || "".equals(itemTempTrade.getBuyerNick())){
						itemTempTrade.setBuyerNick(0 + "");
					}
				}
			}
			resultMap.put("data", itemDetails);
		}
		resultMap.put("totalPage", GetCurrentPageUtil.getTotalPage(itemCount, ConstantUtils.PAGE_SIZE_MIDDLE));
		resultMap.put("pageNo", itemVO.getPageNo());
		resultMap.put("itemCount", itemCount);
		return resultMap;
	}
}
