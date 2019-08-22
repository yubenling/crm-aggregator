package com.kycrm.member.controller;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.effect.TradeCenterEffect;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.effect.ReturnOrderDetailVO;
import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;
import com.kycrm.member.domain.vo.trade.TradeVO;
import com.kycrm.member.exception.KycrmApiException;
import com.kycrm.member.service.effect.ITradeCenterEffectService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.other.IShortLinkService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.tradecenter.ITradeSetupService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.DateUtils;
import com.kycrm.util.GetCurrentPageUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MsgType;
import com.kycrm.util.NumberUtils;
import com.kycrm.util.TradesInfo;

/**
 * 订单中心效果分析Controller
 * @ClassName: TradeCenterEffectController  
 * @author ztk
 * @date 2018年1月18日 下午4:02:58 *
 */
@RequestMapping("")
@Controller
public class TradeCenterEffectController extends BaseController {
	

	@Autowired
	private ITradeSetupService tradeSetupService;

	@Autowired
	private ITradeCenterEffectService effectPictureService;
	
	@Autowired
	private ITradeDTOService tradeDTOService;
	
	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;
	
	@Autowired
	private IShortLinkService shortLinkService;
	
	
	/**
	 * 
	 * getTaskNameByType(物流提醒效果分析页面根据类型查询任务名称) 
	 * @Title: getTaskNameByType * @Description: TODO 
	 * @param @param request
	 * @param @param model
	 * @param @param params
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/tradeCenter/getTaskNames")
	@ResponseBody
	public String getTaskNameByType(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || userInfo.getId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		TradeCenterEffectVO tradeCenterEffectVO = null;
		try {
			JSONObject jsonObject = JSONObject.parseObject(params);
			tradeCenterEffectVO = JSON.toJavaObject(jsonObject, TradeCenterEffectVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if(tradeCenterEffectVO == null){
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		tradeCenterEffectVO.setUid(userInfo.getId());
		List<TradeSetup> taskNames = tradeSetupService.queryTradeSetupTaskNames(tradeCenterEffectVO);
		return rsMap(100, "操作成功").put("status", true).put("data",taskNames).toJson();
	}
	
	/**
	 * 订单中心效果分析列表展示，按照日期倒序
	 * @Title: tradeCenterEffectIndex 
	 * @param  设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/tradeCenter/effectIndex")
	@ResponseBody
	public String tradeCenterEffectIndex(@RequestBody String params,HttpServletRequest request,
			HttpServletResponse response){
		TradeCenterEffectVO trCenterEffectVO = null;
		try {
			trCenterEffectVO = JsonUtil.paramsJsonToObject(params, TradeCenterEffectVO.class);
		} catch (KycrmApiException e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if(trCenterEffectVO == null){
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo ==null || userInfo.getId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		String dayNum = trCenterEffectVO.getDayNum();
		if(dayNum == null || "".equals(dayNum) || "0".equals(dayNum)){
			dayNum = "7";
		}
		Date startEffectTime = DateUtils.getStartTimeOfDay(DateUtils.nDaysAgo(Integer.parseInt(dayNum) - 1, new Date()));
		Date endEffectTime = new Date();
		trCenterEffectVO.setUserId(userInfo.getTaobaoUserNick());
		trCenterEffectVO.setBeginTime(startEffectTime);
		trCenterEffectVO.setEndTime(endEffectTime);
		trCenterEffectVO.setUid(userInfo.getId());
		List<TradeCenterEffect> tradeCenterEffectList = effectPictureService.aggregateTradeCenterList(trCenterEffectVO);
		List<Date> daysList = new ArrayList<Date>();//天数
		List<Double> orderFeeList = new ArrayList<Double>();//回款订单金额
		List<Integer> orderNumList = new ArrayList<Integer>();//回款订单数
		List<Double> smsMoneyList = new ArrayList<Double>();//短信金额
		List<Double> targetFeeList = new ArrayList<Double>();//催付金额
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		//计算汇总数据
		int smsNumAll = 0;//短信消费条数
		double smsNumMoneyAll = 0.00;//总消费金额
		int totalOrderAll = 0;//催付订单数
		double totalMoneyAll = 0.00;//催付金额
		double successMoneyAll = 0.00;//回款金额
		int successOrderAll = 0;//回款订单数
		int totalCustomerClick = 0;//客户点击量
		int totalPageClick = 0;//页面点击量
		int totalLink = 0;//客户总链接量
		String totalClickRate = "0.00%";//客户总点击率
		String roiValueAll = "0:0";//汇总数据的roi
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		if(tradeCenterEffectList != null && !tradeCenterEffectList.isEmpty()){
			for (int i = 0; i < tradeCenterEffectList.size(); i++) {
				TradeCenterEffect tradeCenterEffect = tradeCenterEffectList.get(i);
				if(tradeCenterEffect != null){
					daysList.add(tradeCenterEffect.getEffectTime());
					targetFeeList.add(Double.valueOf(decimalFormat.format(NumberUtils.getResult(tradeCenterEffect.getTargetFee()))));
					smsMoneyList.add(NumberUtils.getResult(tradeCenterEffect.getSmsMoney()));
					orderNumList.add(NumberUtils.getResult(tradeCenterEffect.getEarningOrder()));
					orderFeeList.add(Double.valueOf(decimalFormat.format(NumberUtils.getResult(tradeCenterEffect.getEarningFee()))));
					smsNumAll += NumberUtils.getResult(tradeCenterEffect.getSmsNum());
					totalOrderAll += NumberUtils.getResult(tradeCenterEffect.getTargetOrder());
					totalMoneyAll += NumberUtils.getResult(tradeCenterEffect.getTargetFee());
					successMoneyAll += NumberUtils.getResult(tradeCenterEffect.getEarningFee());
					successOrderAll += NumberUtils.getResult(tradeCenterEffect.getEarningOrder());
					totalCustomerClick += NumberUtils.getResult(tradeCenterEffect.getCustomerClick());
					totalPageClick += NumberUtils.getResult(tradeCenterEffect.getPageClick());
					totalLink += NumberUtils.getResult(tradeCenterEffect.getLinkNum());
					double earningsOrderRate = NumberUtils.getResult(tradeCenterEffect.getTargetOrder()) == 0? 0.0 : (double)NumberUtils.getResult(tradeCenterEffect.getEarningOrder()) / (double)NumberUtils.getResult(tradeCenterEffect.getTargetOrder());
					double earningsMoneyRate = NumberUtils.getResult(tradeCenterEffect.getTargetFee()) == 0? 0.0 : NumberUtils.getResult(tradeCenterEffect.getEarningFee()) / NumberUtils.getResult(tradeCenterEffect.getTargetFee());
					double customerClickRate = NumberUtils.getResult(tradeCenterEffect.getLinkNum()) == 0 ? 0.0:(double)NumberUtils.getResult(tradeCenterEffect.getCustomerClick()) / (double)NumberUtils.getResult(tradeCenterEffect.getLinkNum());
					String roiValue = creatROI(tradeCenterEffect.getSmsMoney(), tradeCenterEffect.getEarningFee());
					tradeCenterEffect.setEarningOrderRate(decimalFormat.format(earningsOrderRate * 100) + "%");
					tradeCenterEffect.setEarningMoneyRate(decimalFormat.format(earningsMoneyRate * 100) + "%");
					tradeCenterEffect.setClickRate(decimalFormat.format(customerClickRate * 100) + "%");
					tradeCenterEffect.setRoiValue(roiValue);
					tradeCenterEffect.setEffectTimeStr(DateUtils.dateToString(tradeCenterEffect.getEffectTime(), DateUtils.DEFAULT_DATE_FORMAT));
					tradeCenterEffect.setSmsMoney(NumberUtils.getTwoDouble(NumberUtils.getResult(tradeCenterEffect.getSmsMoney())));
					tradeCenterEffect.setCustomerClick(NumberUtils.getResult(tradeCenterEffect.getCustomerClick()));
					tradeCenterEffect.setPageClick(NumberUtils.getResult(tradeCenterEffect.getPageClick()));
					tradeCenterEffect.setLinkNum(NumberUtils.getResult(tradeCenterEffect.getLinkNum()));
					tradeCenterEffect.setTargetFee(Double.valueOf(decimalFormat.format(NumberUtils.getResult(tradeCenterEffect.getTargetFee()))));
					tradeCenterEffect.setEarningFee(Double.valueOf(decimalFormat.format(NumberUtils.getResult(tradeCenterEffect.getEarningFee()))));
				}
			}
		}
		Double earningsOrderRateAll = totalOrderAll == 0 ? 0.00 :  ((double)successOrderAll / (double)totalOrderAll);
		Double earningsMoneyRateAll = NumberUtils.getResult(totalMoneyAll) == 0? 0.00 : NumberUtils.getResult(successMoneyAll) / NumberUtils.getResult(totalMoneyAll);
		smsNumMoneyAll = NumberUtils.getResult(smsNumAll * 0.05);
		roiValueAll = creatROI(smsNumMoneyAll, successMoneyAll);
		totalClickRate = decimalFormat.format((totalLink == 0? 0.0:(double)totalCustomerClick / (double)totalLink) * 100) + "%";
		List<TradeSetup> taskNames = tradeSetupService.queryTradeSetupTaskNames(trCenterEffectVO);
		resultMap.put("taskNames", taskNames);
		resultMap.put("effects",tradeCenterEffectList);
		resultMap.put("smsNumAll", smsNumAll);
		resultMap.put("smsNumMoneyAll", NumberUtils.getTwoDouble(smsNumMoneyAll));
		resultMap.put("totalOrderAll", totalOrderAll);
		resultMap.put("totalMoneyAll",NumberUtils.getTwoDouble(totalMoneyAll));
		resultMap.put("successMoneyAll", NumberUtils.getTwoDouble(successMoneyAll));
		resultMap.put("successOrderAll", successOrderAll);
		resultMap.put("totalCustomerClick", totalCustomerClick);
		resultMap.put("totalPageClick",totalPageClick);
		resultMap.put("totalLink", totalLink);
		resultMap.put("totalClickRate", totalClickRate);
		resultMap.put("roiValueAll", roiValueAll);
		resultMap.put("earningsOrderRateAll", decimalFormat.format(earningsOrderRateAll * 100) + "%");
		resultMap.put("earningsMoneyRateAll", decimalFormat.format(earningsMoneyRateAll * 100) + "%");
		resultMap.put("daysList", daysList);
		resultMap.put("orderFeeList", orderFeeList);
		resultMap.put("orderNumList", orderNumList);
		resultMap.put("smsMoneyList", smsMoneyList);
		resultMap.put("targetFeeList", targetFeeList);
		//计算今日数据
		int smsNum = 0;//短信消费条数
		double smsMoneyDouble = 0.0;
		int totalOrder = 0;//催付订单数
		double totalMoney = 0.00;//催付金额
		double successMoney = 0.00;//回款金额
		int successOrder = 0;//回款订单数
		int customerClickNum = 0;//客户点击量
		int pageClickNum = 0;//页面点击量
		int totalClickNum = 0;//短信内链接量
		double customerClickRate = 0.0;//客户点击率
		double earningsOrderRate = 0.0;
		double earningsMoneyRate = 0.0;
		String RIO = "0:0";
		TradeCenterEffectVO tradeCenterEffectVO = new TradeCenterEffectVO();
		tradeCenterEffectVO.setUserId(userInfo.getTaobaoUserNick());
		tradeCenterEffectVO.setUid(userInfo.getId());
		tradeCenterEffectVO.setTaskId(trCenterEffectVO.getId());
		tradeCenterEffectVO.setType(trCenterEffectVO.getType());
		tradeCenterEffectVO.setEffectTime(DateUtils.getStartTimeOfDay(new Date()));
		TradeCenterEffect todayEffect = effectPictureService.queryTradeEffect(tradeCenterEffectVO);
		if(todayEffect != null){
			smsNum = NumberUtils.getResult(todayEffect.getSmsNum());
			smsMoneyDouble = smsNum * 0.05;
			totalOrder = NumberUtils.getResult(todayEffect.getTargetOrder());
			totalMoney = NumberUtils.getResult(todayEffect.getTargetFee());
			successOrder = NumberUtils.getResult(todayEffect.getEarningOrder());
			successMoney = NumberUtils.getResult(todayEffect.getEarningFee());
			customerClickNum = NumberUtils.getResult(todayEffect.getCustomerClick());
			pageClickNum = NumberUtils.getResult(todayEffect.getPageClick());
			totalClickNum = NumberUtils.getResult(todayEffect.getLinkNum());
			customerClickRate = totalClickNum == 0? 0.00 : ((double)customerClickNum / (double)totalClickNum);
			earningsOrderRate = totalOrder == 0 ? 0.00 : ((double)successOrder / (double)totalOrder);
			earningsMoneyRate = totalMoney == 0? 0.00 : successMoney / totalMoney;
			RIO = creatROI(smsMoneyDouble, successMoney);
		}
		resultMap.put("smsNum", smsNum);
		resultMap.put("smsMoneyDouble", NumberUtils.getTwoDouble(smsMoneyDouble));
		resultMap.put("totalOrder", totalOrder);
		resultMap.put("totalMoney", NumberUtils.getTwoDouble(totalMoney));
		resultMap.put("earningsMoney", NumberUtils.getTwoDouble(successMoney));
		resultMap.put("earningsOrder", successOrder);
		resultMap.put("customerClickNum", customerClickNum);
		resultMap.put("pageClickNum", pageClickNum);
		resultMap.put("totalClickNum", totalClickNum);
		resultMap.put("customerClickRate", decimalFormat.format(customerClickRate * 100) + "%");
		resultMap.put("earningsOrderRate", decimalFormat.format(earningsOrderRate * 100) + "%");
		resultMap.put("earningsMoneyRate", decimalFormat.format(earningsMoneyRate * 100) + "%");
		resultMap.put("RIO", RIO);
		return resultMap.toJson();
	}
	
	
	/**
	 * 根据日期查询催付订单详细信息
	 * ztk2017年9月25日下午4:52:30
	 */
	@RequestMapping("/tradeCenter/dayDetail")
	@ResponseBody 
	public String findEarningOrderDetails(HttpServletRequest request,HttpServletResponse response,@RequestBody String params
			){
		TradeCenterEffectVO tradeCenterEffectVO = null;
		try {
			tradeCenterEffectVO = JsonUtil.paramsJsonToObject(params, TradeCenterEffectVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if(tradeCenterEffectVO == null){
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		String dateStr = tradeCenterEffectVO.getEffectTimeStr();
		String type = tradeCenterEffectVO.getType();
		Integer pageNo = tradeCenterEffectVO.getPageNo();
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		tradeCenterEffectVO.setUid(userInfo.getId());
		Long taskId = tradeCenterEffectVO.getId();
		if(pageNo == null || pageNo == 0){
			pageNo = 1;
			tradeCenterEffectVO.setPageNo(pageNo);
		}
		Date startTime = DateUtils.getStartTimeOfDay(DateUtils.parseDate(dateStr, DateUtils.DEFAULT_DATE_FORMAT));
		Date endTime = DateUtils.getEndTimeOfDay(DateUtils.parseDate(dateStr, DateUtils.DEFAULT_DATE_FORMAT));
		TradeVO tradeVO = new TradeVO();
		tradeVO.setUid(userInfo.getId());
		if(MsgType.MSG_CGCF.equals(type) || MsgType.MSG_ECCF.equals(type) || MsgType.MSG_JHSCF.equals(type)){
			if(startTime != null && endTime != null){
				tradeVO.setPayTimeBegin(startTime);
				tradeVO.setPayTimeEnd(endTime);
			}
		}else if(MsgType.MSG_HKTX.equals(type)){
			List<String> statusList = new ArrayList<String>();
			statusList.add(TradesInfo.TRADE_FINISHED);
			statusList.add(TradesInfo.TRADE_CLOSED);
			if(startTime != null && endTime != null){
				tradeVO.setMinEndTime(startTime);
				tradeVO.setMaxEndTime(endTime);
			}
			tradeVO.setTradeStatusList(statusList);
		}else {
			List<String> tidList = smsRecordDTOService.tradeCenterEffectRecordTid( userInfo.getId(),type,startTime, endTime, taskId);
			tradeVO.setTidList(tidList);
		}
		List<TradeDTO> tradeList = tradeDTOService.listTradeByStatus(userInfo.getId(),tradeVO,userInfo.getAccessToken());
		List<ReturnOrderDetailVO> successList = new ArrayList<ReturnOrderDetailVO>();
		if(tradeList != null && !tradeList.isEmpty()){
			for (int i = 0; i < tradeList.size(); i++) {
				TradeDTO trade = tradeList.get(i);
				if(trade != null && trade.getTid() != null){
					//如果是常规催付，查询该订单是否发过二次催付的短信，若发过，则不计入常规催付
					if(MsgType.MSG_CGCF.equals(type)){
						Date ECCF_sendTime = findRecordByQuery(trade.getTid(), userInfo.getId(), MsgType.MSG_ECCF);
						if(ECCF_sendTime != null){
							continue;
						}
					}
					ReturnOrderDetailVO earningDetail = new ReturnOrderDetailVO();
					Date sendTime = findRecordByQuery(trade.getTid(), userInfo.getId(), type);
					if(sendTime != null){
						try {
							earningDetail.setBuyerNick(trade.getBuyerNick());
							earningDetail.setEarningFee(trade.getPayment().doubleValue());
							earningDetail.setOrderId(trade.getTid());
							earningDetail.setPayTime(trade.getPayTime());
							earningDetail.setRecNum(trade.getReceiverMobile());
							earningDetail.setSendTime(sendTime);
							successList.add(earningDetail);
						} catch (KycrmApiException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		List<ReturnOrderDetailVO> resultList = new ArrayList<ReturnOrderDetailVO>();
		int totalPage = 0;
		if(successList != null && !successList.isEmpty()){
			totalPage = GetCurrentPageUtil.getTotalPage(successList.size(), ConstantUtils.PAGE_SIZE_MIN);
			if(pageNo > (successList.size() / ConstantUtils.PAGE_SIZE_MIN) + 1){
				return rsMap(101, "操作失败").put("status", false).toJson();
			}else if(ConstantUtils.PAGE_SIZE_MIN * pageNo <= successList.size()){
				resultList = successList.subList(ConstantUtils.PAGE_SIZE_MIN * (pageNo - 1), ConstantUtils.PAGE_SIZE_MIN * (pageNo));
			}else{
				resultList = successList.subList(ConstantUtils.PAGE_SIZE_MIN * (pageNo - 1), successList.size());
			}
		}
		return rsMap(100, "操作成功").put("status", true).put("data", resultList).put("totalPage", totalPage).put("pageNo", tradeCenterEffectVO.getPageNo()).toJson();
	}
	
	/**
	 * 客户页面点击量
	 * ztk2017年10月31日上午11:09:15
	 */
	@RequestMapping("/tradeCenter/clickDetail")
	@ResponseBody
	public String findClickDetail(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		TradeCenterEffectVO tradeCenterEffectVO = null;
		try {
			tradeCenterEffectVO = JsonUtil.paramsJsonToObject(params, TradeCenterEffectVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if(tradeCenterEffectVO == null){
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || userInfo.getId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		tradeCenterEffectVO.setUserId(userInfo.getTaobaoUserNick());
		tradeCenterEffectVO.setUid(userInfo.getId());
		String type = tradeCenterEffectVO.getType();
		String dateStr = tradeCenterEffectVO.getEffectTimeStr();
		Integer pageNo = tradeCenterEffectVO.getPageNo() == null? 1 : tradeCenterEffectVO.getPageNo();
		Integer currentRows = ConstantUtils.PAGE_SIZE_MIN;
		Long taskId = tradeCenterEffectVO.getId();
		Boolean forUser = tradeCenterEffectVO.getForUser();
		String isUserPage = "";
		if(forUser != null && forUser == true){
			isUserPage = "true";
		}
		Date startTime = DateUtils.getStartTimeOfDay(DateUtils.parseDate(dateStr, DateUtils.DEFAULT_DATE_FORMAT));
		Date endTime = DateUtils.getEndTimeOfDay(DateUtils.parseDate(dateStr, DateUtils.DEFAULT_DATE_FORMAT));
		List<Map<String, Object>> succesList = new ArrayList<>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		int totalPage = 0;
		if(succesList != null && !succesList.isEmpty()){
			totalPage = GetCurrentPageUtil.getTotalPage(succesList.size(), currentRows);
			if(pageNo > (succesList.size() / currentRows) + 1){
				return rsMap(101, "操作失败").put("status", false).toJson();
			}else if(currentRows * pageNo <= succesList.size()){
				resultList = succesList.subList(currentRows * (pageNo - 1), currentRows * (pageNo));
			}else{
				resultList = succesList.subList(currentRows * (pageNo - 1), succesList.size());
			}
		}
		JSONObject jsonObject = shortLinkService.showCustomerClickDetail(userInfo.getId(), type, taskId, startTime.getTime(), endTime.getTime(), pageNo, currentRows, isUserPage);
		if(jsonObject != null){
			jsonObject.put("rc", 100);
			jsonObject.put("message", "操作成功");     
			jsonObject.put("status", true);
			jsonObject.put("pageNo", tradeCenterEffectVO.getPageNo());
			String jsonString = jsonObject.toJSONString();
			return jsonString;
		}else {
			return rsMap(100, "操作成功").put("status", true).put("data", resultList).put("totalPage", totalPage).put("pageNo", tradeCenterEffectVO.getPageNo()).toJson();
		}
	}
	
	/**
	 * 计算ROI
	 * ZTK2017年7月7日上午9:54:08
	 */
	private String creatROI(Double small,Double big){
		double result = NumberUtils.getResult(small);
		double result2 = NumberUtils.getResult(big);
		if(result != 0.00){
			double roi = result2 / result;
			return "1:" + NumberUtils.getTwoDouble(roi);
		}else {
			return NumberUtils.getTwoDouble(result) + ":0";
		}
	}
	
	/**
	 * 根据type和orderId查询发送时间
	 * ztk2017年9月26日上午11:53:58
	 */
	public Date findRecordByQuery(Long tid,Long uid,String type){
		SmsRecordDTO smsRecordDTO = new SmsRecordDTO();
		smsRecordDTO.setUid(uid);
		smsRecordDTO.setStatus(2);
		smsRecordDTO.setType(type);
		if(tid != null && !"".equals(tid)){
			smsRecordDTO.setOrderId(tid + "");
		}else {
			return null;
		}
		Date sendTime = smsRecordDTOService.queryTidSendTime(uid,smsRecordDTO);
		return sendTime;
	}
}
