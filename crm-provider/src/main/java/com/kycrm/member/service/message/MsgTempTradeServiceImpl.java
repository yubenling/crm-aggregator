package com.kycrm.member.service.message;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.redis.RedisLockService;
import com.kycrm.member.dao.effect.IItemTempTradeDao;
import com.kycrm.member.dao.effect.IItemTempTradeHistoryDao;
import com.kycrm.member.dao.message.IMsgTempTradeDao;
import com.kycrm.member.dao.message.IMsgTempTradeHistoryDao;
import com.kycrm.member.domain.entity.trade.MsgTempTrade;
import com.kycrm.member.domain.entity.trade.MsgTempTradeHistory;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.effect.CustomerDetailVO;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.GetCurrentPageUtil;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.TradesInfo;
import com.taobao.api.SecretException;

@Service("tempTradeService")
@MyDataSource
public class MsgTempTradeServiceImpl implements IMsgTempTradeService {
	
	
	@Autowired      
	private IMsgTempTradeDao tempTradeDao;
	
	@Autowired
	private IOrderDTOService orderService;
	
	@Autowired
	private RedisLockService lockService;
	
	@Autowired      
	private IMsgTempTradeHistoryDao tempTradeHistoryDao;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	@Autowired
	private ICacheService cacheService;
	
	@Autowired
	private IItemTempTradeDao itemTempTradeDao;
	
	@Autowired
	private IItemTempTradeHistoryDao itemTempTradeHistoryDao;
	
	@Override
	public Boolean doCreateTable(Long uid){
		if (uid == null) {
			return false;
		}
		List<String> tables = tempTradeDao.tableIsExist(uid);
		if (tables != null && !tables.isEmpty()) {
			return true;
		}
		try {
			tempTradeDao.doCreateTable(uid);
			tempTradeDao.addMsgTempTradeTableIndex(uid);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * 筛选15天的数据，存放到历史表中
	 */
	@Override
	public List<MsgTempTrade> listFifteenTrade(Long uid,Date fifteenAgoTime,int startRows,int pageSize){
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("uid", uid);
		map.put("fifteenAgoTime", fifteenAgoTime);
		map.put("startRows", startRows);
		map.put("pageSize", pageSize);
//		List<MsgTempTrade> tempTrades = new ArrayList<MsgTempTrade>();
//		while(true){
			List<MsgTempTrade> limitTempTrade = tempTradeDao.listFifteenTrade(map);
//			if(limitTempTrade != null){
//				tempTrades.addAll(limitTempTrade);
//			}else {
//				break;
//			}
//		}
		return limitTempTrade;
	}
	
	/**
	 * 根据msgId删除对应记录
	 */
	@Override
	public void deleteDataByMsgId(Long uid, Long msgId){
		if(uid == null || msgId == null){
			return;
		}
		this.tempTradeDao.deleteDataByMsgId(uid, msgId);
	}

	/**
	 * 客户详情集合
	 */
	@Override
	public Map<String, Object> limitCustomerDetail(Long uid,CustomerDetailVO customerDetailVO,UserInfo userInfo){
		if(uid == null || customerDetailVO == null){
			return null;
		}
		if(customerDetailVO.getPageNo() == null){
			customerDetailVO.setPageNo(1);
		}
		CustomerDetailVO queryVo = lockService.getValue(uid + "-effectParam", CustomerDetailVO.class);
		if(queryVo == null || queryVo.getMsgId() == null){
			throw new RuntimeException("客户详情查询时，从redis取出查询条件为空");
		}
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		String token = userInfo.getAccessToken();
		Integer startRows = (customerDetailVO.getPageNo() - 1) * ConstantUtils.PAGE_SIZE_MIDDLE;
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("startRows", startRows);
		map.put("pageSize", ConstantUtils.PAGE_SIZE_MIDDLE);
		map.put("tradeFrom", customerDetailVO.getOrderSource());
		map.put("tradeStatus", customerDetailVO.getBuyerType());
		if(customerDetailVO.getBuyerNick() != null && !"".equals(customerDetailVO.getBuyerNick())){
			try {
				if(!EncrptAndDecryptClient.isEncryptData(customerDetailVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)){
					String buyerNick = decryptClient.encryptData(customerDetailVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH, token);
					map.put("buyerNick", buyerNick);
				}else {
					map.put("buyerNick", customerDetailVO.getBuyerNick());
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
		}
		if(customerDetailVO.getPhone() != null && !"".equals(customerDetailVO.getPhone())){
			try {
				if(!EncrptAndDecryptClient.isEncryptData(customerDetailVO.getPhone(), EncrptAndDecryptClient.PHONE)){
					String phone = decryptClient.encryptData(customerDetailVO.getPhone(), EncrptAndDecryptClient.PHONE, token);
					map.put("receiverMobile", phone);
				}else {
					map.put("receiverMobile", customerDetailVO.getPhone());
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
		}
		map.put("msgId", queryVo.getMsgId());
		map.put("bTime", queryVo.getbTime());
		map.put("eTime", queryVo.geteTime());
		if(customerDetailVO.getSortData() != null && !"".equals(customerDetailVO.getSortData())){
			String sortType = customerDetailVO.getSortType();
			if("payment" == sortType || "payment".equals(sortType)){
				map.put("payment", "payment");
			}else if("tradeNum" == sortType || "tradeNum".equals(sortType)){
				map.put("tradeNum", "tradeNum");
			}else if("itemNum" == sortType || "itemNum".equals(sortType)){
				map.put("itemNum", "itemNum");
			}else {
				map.put("id", "id");
			}
			String sortData = customerDetailVO.getSortData();
			if(sortData != null && !"".equals(sortData)){
				if("ASC" == sortData || "ASC".equals(sortData)){
					map.put("sortType", "sortType");
				}else{
					map.put("sortType", null);
				}
			}else {
				map.put("id", null);
			}
		}
		Map<String, Object> resultMap = new HashMap<>();
		Integer totalCount = null;
		Boolean isHistory = this.cacheService.get(RedisConstant.RediskeyCacheGroup.MSG_IS_HISTORY_DATA,
				"msgId-" + queryVo.getMsgId(), Boolean.class);
		if(isHistory == null){
			isHistory = false;
		}
		
		if(!isHistory){
			if(customerDetailVO.getItemId() != null){
				Long itemId = customerDetailVO.getItemId();
				List<Long> tids = itemTempTradeDao.listTidByItemId(uid, itemId);
				map.put("tids", tids);
			}
			List<MsgTempTrade> customerDetails = tempTradeDao.listCustomerDetail(map);
			totalCount = tempTradeDao.countCustomerDetail(map);
			if(customerDetails != null && !customerDetails.isEmpty()){
				for (MsgTempTrade msgTempTrade : customerDetails) {
					if(msgTempTrade.getBuyerNick() != null && !"".equals(msgTempTrade.getBuyerNick())){
						try {
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getBuyerNick(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setBuyerNick(decryptClient.decryptData(msgTempTrade.getBuyerNick(), EncrptAndDecryptClient.SEARCH, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverMobile(), EncrptAndDecryptClient.PHONE)){
								msgTempTrade.setReceiverMobile(decryptClient.decryptData(msgTempTrade.getReceiverMobile(), EncrptAndDecryptClient.PHONE, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverAddress(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setReceiverAddress(decryptClient.decryptData(msgTempTrade.getReceiverAddress(), EncrptAndDecryptClient.SEARCH, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverName(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setReceiverName(decryptClient.decryptData(msgTempTrade.getReceiverName(), EncrptAndDecryptClient.SEARCH, token));
							}
						} catch (SecretException e) {
							e.printStackTrace();
						}
					}
				}
			}
			resultMap.put("data", customerDetails);
		}else {
			if(customerDetailVO.getItemId() != null){
				Long itemId = customerDetailVO.getItemId();
				List<Long> tids = itemTempTradeHistoryDao.listTidByItemId(uid, itemId);
				map.put("tids", tids);
			}
			List<MsgTempTradeHistory> customerHistory = tempTradeHistoryDao.listCustomerDetail(map);
			totalCount = tempTradeHistoryDao.countCustomerDetail(map);
			if(customerHistory != null && !customerHistory.isEmpty()){
				for (MsgTempTradeHistory msgTempTrade : customerHistory) {
					if(msgTempTrade.getBuyerNick() != null && !"".equals(msgTempTrade.getBuyerNick())){
						try {
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getBuyerNick(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setBuyerNick(decryptClient.decryptData(msgTempTrade.getBuyerNick(), EncrptAndDecryptClient.SEARCH, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverMobile(), EncrptAndDecryptClient.PHONE)){
								msgTempTrade.setReceiverMobile(decryptClient.decryptData(msgTempTrade.getReceiverMobile(), EncrptAndDecryptClient.PHONE, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverAddress(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setReceiverAddress(decryptClient.decryptData(msgTempTrade.getReceiverAddress(), EncrptAndDecryptClient.SEARCH, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverName(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setReceiverName(decryptClient.decryptData(msgTempTrade.getReceiverName(), EncrptAndDecryptClient.SEARCH, token));
							}
						} catch (SecretException e) {
							e.printStackTrace();
						}
					}
				}
			}
			resultMap.put("data", customerHistory);
		}
		int totalPage = GetCurrentPageUtil.getTotalPage(totalCount, ConstantUtils.PAGE_SIZE_MIDDLE);
		resultMap.put("totalPage", totalPage);
		resultMap.put("pageNo", customerDetailVO.getPageNo());
		return resultMap;
	}
	
	@Override
	public List<MsgTempTrade> listTempTradeByMsgId(Long uid, Long msgId){
		if(uid == null || msgId == null){
			return null;
		}
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("msgId", msgId);
		List<MsgTempTrade> tempTrades = this.tempTradeDao.listTempTradeByMsgId(map);
		return tempTrades;
	}

	/**
	 * 首页昨日营销回款金额
	 */
	@Override
	public Double sumPaymentByDate(Long uid, Date bTime, Date eTime){
		if(uid == null || bTime == null || eTime == null){
			return 0.0;
		}
		Map<String, Object> queryMap = new HashMap<>();
		queryMap.put("uid", uid);
		queryMap.put("bTime", bTime);
		queryMap.put("eTime", eTime);
		MsgTempTrade resultTrade = this.tempTradeDao.sumPaymentByDate(queryMap);
		if(resultTrade != null && resultTrade.getPayment() != null){
			return resultTrade.getPayment().doubleValue();
		}
		return 0.0;
	}
	
	/**
	 * 预售客户详情集合
	 */
	@Override
	public Map<String, Object> limitStepCustomerDetail(Long uid,CustomerDetailVO customerDetailVO,UserInfo userInfo){
		if(uid == null || customerDetailVO == null){
			return null;
		}
		if(customerDetailVO.getPageNo() == null){
			customerDetailVO.setPageNo(1);
		}
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		String token = userInfo.getAccessToken();
		CustomerDetailVO queryVo = lockService.getValue(uid + "-effectParam", CustomerDetailVO.class);
		if(queryVo == null || queryVo.getMsgId() == null){
			throw new RuntimeException("客户详情查询时，从redis取出查询条件为空");
		}
		Integer startRows = (customerDetailVO.getPageNo() - 1) * ConstantUtils.PAGE_SIZE_MIDDLE;
		Map<String, Object> map = new HashMap<>();
		map.put("uid", uid);
		map.put("startRows", startRows);
		map.put("pageSize", ConstantUtils.PAGE_SIZE_MIDDLE);
		if("paidFinal".equals(customerDetailVO.getBuyerType())){
			map.put("stepTradeStatus", TradesInfo.FRONT_PAID_FINAL_PAID);
		}/*else {
			map.put("tradeFrom", customerDetailVO.getOrderSource());
		}*/
		map.put("tradeFrom", customerDetailVO.getOrderSource());
		map.put("tradeStatus", customerDetailVO.getBuyerType());
		List<String> noStatusList = new ArrayList<String>();
		if("success".equals(customerDetailVO.getBuyerType())){
			noStatusList.add("refund");
			map.put("noStatusList", noStatusList);
		}else if ("paidFinal".equals(customerDetailVO.getBuyerType())) {
			noStatusList.add("refund");
			map.put("noStatusList", noStatusList);
		}else if ("waitPay".equals(customerDetailVO.getBuyerType())) {
			noStatusList.add("refund");noStatusList.add("success");
			noStatusList.add("paidFinal");
			map.put("noStatusList", noStatusList);
		}else if ("refund".equals(customerDetailVO.getBuyerType())) {
			map.put("noStatusList", null);
		}
		if(customerDetailVO.getBuyerNick() != null && !"".equals(customerDetailVO.getBuyerNick())){
			try {
				if(!EncrptAndDecryptClient.isEncryptData(customerDetailVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)){
					String buyerNick = decryptClient.encryptData(customerDetailVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH, token);
					map.put("buyerNick", buyerNick);
				}else {
					map.put("buyerNick", customerDetailVO.getBuyerNick());
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
		}
		if(customerDetailVO.getPhone() != null && !"".equals(customerDetailVO.getPhone())){
			try {
				if(!EncrptAndDecryptClient.isEncryptData(customerDetailVO.getPhone(), EncrptAndDecryptClient.PHONE)){
					String phone = decryptClient.encryptData(customerDetailVO.getPhone(), EncrptAndDecryptClient.PHONE, token);
					map.put("receiverMobile", phone);
				}else {
					map.put("receiverMobile", customerDetailVO.getPhone());
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
		}
		map.put("msgId", queryVo.getMsgId());
		map.put("bTime", queryVo.getbTime());
		if(customerDetailVO.geteTime() != null){
			map.put("eTime", customerDetailVO.geteTime());
		}else {
			map.put("eTime", queryVo.geteTime());
		}
		if(customerDetailVO.getSortData() != null && !"".equals(customerDetailVO.getSortData())){
			String sortType = customerDetailVO.getSortType();
			if("payment" == sortType || "payment".equals(sortType)){
				map.put("payment", "payment");
			}else if("tradeNum" == sortType || "tradeNum".equals(sortType)){
				map.put("tradeNum", "tradeNum");
			}else if("itemNum" == sortType || "itemNum".equals(sortType)){
				map.put("itemNum", "itemNum");
			}else {
				map.put("id", "id");
			}
			String sortData = customerDetailVO.getSortData();
			if(sortData != null && !"".equals(sortData)){
				if("ASC" == sortData || "ASC".equals(sortData)){
					map.put("sortType", "sortType");
				}else{
					map.put("sortType", null);
				}
			}else {
				map.put("id", null);
			}
		}
		Map<String, Object> resultMap = new HashMap<>();
		Integer totalCount = null;
		Boolean isHistory = this.cacheService.get(RedisConstant.RediskeyCacheGroup.MSG_IS_HISTORY_DATA,
				"msgId-" + queryVo.getMsgId(), Boolean.class);
		if(isHistory == null){
			isHistory = false;
		}
		
		if(!isHistory){
			if(customerDetailVO.getItemId() != null){
				Long itemId = customerDetailVO.getItemId();
				List<Long> tids = itemTempTradeDao.listTidByItemId(uid, itemId);
				map.put("tids", tids);
			}
			List<MsgTempTrade> customerDetails = tempTradeDao.listStepCustomerDetail(map);
			totalCount = tempTradeDao.countStepCustomerDetail(map);
			if(customerDetails != null && !customerDetails.isEmpty()){
				for (MsgTempTrade msgTempTrade : customerDetails) {
					if(msgTempTrade.getBuyerNick() != null && !"".equals(msgTempTrade.getBuyerNick())){
						try {
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getBuyerNick(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setBuyerNick(decryptClient.decryptData(msgTempTrade.getBuyerNick(), EncrptAndDecryptClient.SEARCH, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverMobile(), EncrptAndDecryptClient.PHONE)){
								msgTempTrade.setReceiverMobile(decryptClient.decryptData(msgTempTrade.getReceiverMobile(), EncrptAndDecryptClient.PHONE, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverAddress(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setReceiverAddress(decryptClient.decryptData(msgTempTrade.getReceiverAddress(), EncrptAndDecryptClient.SEARCH, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverName(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setReceiverName(decryptClient.decryptData(msgTempTrade.getReceiverName(), EncrptAndDecryptClient.SEARCH, token));
							}
						} catch (SecretException e) {
							e.printStackTrace();
						}
					}
				}
			}
			resultMap.put("data", customerDetails);
		}else {
			if(customerDetailVO.getItemId() != null){
				Long itemId = customerDetailVO.getItemId();
				List<Long> tids = itemTempTradeHistoryDao.listTidByItemId(uid, itemId);
				map.put("tids", tids);
			}
			List<MsgTempTradeHistory> customerHistory = tempTradeHistoryDao.listStepCustomerDetail(map);
			totalCount = tempTradeHistoryDao.countStepCustomerDetail(map);
			if(customerHistory != null && !customerHistory.isEmpty()){
				for (MsgTempTradeHistory msgTempTrade : customerHistory) {
					if(msgTempTrade.getBuyerNick() != null && !"".equals(msgTempTrade.getBuyerNick())){
						try {
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getBuyerNick(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setBuyerNick(decryptClient.decryptData(msgTempTrade.getBuyerNick(), EncrptAndDecryptClient.SEARCH, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverMobile(), EncrptAndDecryptClient.PHONE)){
								msgTempTrade.setReceiverMobile(decryptClient.decryptData(msgTempTrade.getReceiverMobile(), EncrptAndDecryptClient.PHONE, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverAddress(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setReceiverAddress(decryptClient.decryptData(msgTempTrade.getReceiverAddress(), EncrptAndDecryptClient.SEARCH, token));
							}
							if(EncrptAndDecryptClient.isEncryptData(msgTempTrade.getReceiverName(), EncrptAndDecryptClient.SEARCH)){
								msgTempTrade.setReceiverName(decryptClient.decryptData(msgTempTrade.getReceiverName(), EncrptAndDecryptClient.SEARCH, token));
							}
						} catch (SecretException e) {
							e.printStackTrace();
						}
					}
				}
			}
			resultMap.put("data", customerHistory);
		}
		int totalPage = GetCurrentPageUtil.getTotalPage(totalCount, ConstantUtils.PAGE_SIZE_MIDDLE);
		resultMap.put("totalPage", totalPage);
		resultMap.put("pageNo", customerDetailVO.getPageNo());
		return resultMap;
	}
	
}
