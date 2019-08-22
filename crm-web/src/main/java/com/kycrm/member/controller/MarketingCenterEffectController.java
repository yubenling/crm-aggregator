package com.kycrm.member.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.effect.MarketingCenterEffect;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.effect.CustomerDetailVO;
import com.kycrm.member.domain.vo.effect.PayOrderEffectDetailVO;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.exception.KycrmApiException;
import com.kycrm.member.service.effect.IItemDetailService;
import com.kycrm.member.service.effect.IMarketingCenterEffectService;
import com.kycrm.member.service.message.IMsgSendRecordService;
import com.kycrm.member.service.message.IMsgTempTradeService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.other.IShortLinkService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.DateUtils;
import com.kycrm.util.GetCurrentPageUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.NumberUtils;

/**
 * 营销中心效果分析Controller
 * 
 * @ClassName: MarktingCenterEffectController
 * @author ztk
 * @date 2018年1月18日 下午4:01:52 *
 */
@Controller
@RequestMapping("/effect")
public class MarketingCenterEffectController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(MarketingCenterEffectController.class);

	@Autowired
	private IMsgSendRecordService msgSendRecordService;

	@Autowired
	private SessionProvider sessionProvider;

	@Autowired
	private IMarketingCenterEffectService marketingCenterEffectService;

	@Autowired
	private ITradeDTOService tradeService;

	@Autowired
	private IMsgTempTradeService tempTradeService;

	@Autowired
	private IItemDetailService itemDetailService;

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private IShortLinkService shortLinkService;

	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;

	/**
	 * 
	 * listMsgSendRecord(营销中心效果分析--发送记录) @Title: lsitMsgSendRecord @param
	 * 设定文件 @return String 返回类型 @throws
	 */
	@RequestMapping("/msgList")
	@ResponseBody
	public String listMsgSendRecord(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		SmsRecordVO msgRecordVO = null;
		try {
			msgRecordVO = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
		} catch (KycrmApiException e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (userInfo == null || userInfo.getId() == null || msgRecordVO == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		String bTimeStr = msgRecordVO.getbTime();
		String eTimeStr = msgRecordVO.geteTime();
		Integer pageNo = msgRecordVO.getPageNo();
		if (pageNo == null || pageNo == 0) {
			pageNo = 1;
			msgRecordVO.setPageNo(pageNo);
		}
		Date bTime = null;
		Date eTime = null;
		if (bTimeStr != null && !"".equals(bTimeStr)) {
			bTime = DateUtils.parseTime(bTimeStr);
			msgRecordVO.setBeginTime(bTime);
		}
		if (eTimeStr != null && !"".equals(eTimeStr)) {
			eTime = DateUtils.parseTime(eTimeStr);
			msgRecordVO.setEndTime(eTime);
		}
		List<MsgSendRecord> msgRecord = msgSendRecordService.listEffectSendRecord(userInfo.getId(), msgRecordVO, true);
		for (int i = 0; i < msgRecord.size(); i++) {
			if (msgRecord.get(i).getShortLinkType() != null) {
				if (msgRecord.get(i).getShortLinkType() == 2) {
					JSONObject jsonObject = shortLinkService.getAllEffect(userInfo.getId(), msgRecord.get(i).getType(),
							msgRecord.get(i).getTaoBaoShortLinkId(), msgRecord.get(i).getSendCreat(),
							DateUtils.getEndTimeOfDay(DateUtils.nDaysAfter(10, msgRecord.get(i).getSendCreat())));
					msgRecord.get(i).setPageClickNum(
							jsonObject != null ? Integer.valueOf(jsonObject.get("pageClickNum").toString()) : 0);
				}
			}
			msgRecord.get(i)
					.setRoiValue(creatROI(
							Double.valueOf(msgRecord.get(i).getSucceedCount() == null ? 0
									: msgRecord.get(i).getSucceedCount() * 0.05),
							Double.valueOf(msgRecord.get(i).getSuccessPayment() == null ? new BigDecimal(0).toString()
									: msgRecord.get(i).getSuccessPayment().toString())));
		}
		Integer msgRecordCount = msgSendRecordService.countEffectSendRecord(userInfo.getId(), msgRecordVO, true);
		// 计算总页数
		Integer totalPage = GetCurrentPageUtil.getTotalPage(msgRecordCount, ConstantUtils.PAGE_SIZE_MIDDLE);
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("totalPage", totalPage);
		resultMap.put("data", msgRecord);
		resultMap.put("pageNo", msgRecordVO.getPageNo());
		return resultMap.toJson();
	}

	/**
	 * sumEffectPicture(营销中心效果分析--效果分析综合数据) @Title: sumEffectPicture @param
	 * 设定文件 @return String 返回类型 @throws
	 */
	@ResponseBody
	@RequestMapping("/index")
	public String sumEffectPicture(@RequestBody String params, HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (userInfo == null || userInfo.getId() == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		MarketingCenterEffect effectPicture = null;
		try {
			effectPicture = JsonUtil.paramsJsonToObject(params, MarketingCenterEffect.class);
		} catch (KycrmApiException e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if (effectPicture == null) {
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		if (effectPicture.getTradeFrom() == null || "".equals(effectPicture.getTradeFrom())) {
			effectPicture.setTradeFrom("TOTAL");
		}
		this.cacheService.put("effect", userInfo.getId() + "effect", JsonUtil.toJson(effectPicture), TimeUnit.HOURS,
				1L);
		Long msgId = effectPicture.getMsgId();
		MsgSendRecord msgRecord = msgSendRecordService.queryRecordById(userInfo.getId(), msgId);
		Integer dayNum = effectPicture.getDays();
		Date beginTime = msgRecord.getSendCreat();
		Date endTime = DateUtils.getEndTimeOfDay(DateUtils.nDaysAfter(dayNum, beginTime));
		int messageCount = msgRecord.getTemplateContent().length();
		if (messageCount <= 70) {
			messageCount = 1;
		} else {
			messageCount = (messageCount + 66) / 67;
		}
		Integer totalSendCustomer = msgRecord.getTotalCount();// 群发客户总数
		Integer successSendCustomer = msgRecord.getSucceedCount();// 成功发送客户数
		// Long deduction =
		// smsRecordDTOService.sumDeductionById(userInfo.getId(),
		// msgRecord.getId());
		Integer totalSendNum = msgRecord.getSucceedCount();// 消耗短信条数
		Double totalSendMoney = NumberUtils.getTwoDouble(NumberUtils.getResult(totalSendNum) * 0.05);// 消费短信金额
		// 下单客户数、下单总金额、订单总数、下单商品总数
		int createCustomer = 0;
		double createMoney = 0.00;
		int createOrderNum = 0;
		long createItemNum = 0;
		// 成交客户数、成交总金额、成交订单数、成交商品数
		int successCustomer = 0;
		double successMoney = 0.00;
		int successOrderNum = 0;
		long successItemNum = 0;
		// 未付款客户数、未付款总金额、未付款订单数、未付款商品数
		int waitCustomer = 0;
		double waitMoney = 0.00;
		int waitOrderNum = 0;
		long waitItemNum = 0;
		// 退款客户数、退款总金额、退款订单数、退款商品数
		int refundCustomer = 0;
		double refundMoney = 0.00;
		int refundOrderNum = 0;
		long refundItemNum = 0;
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		MarketingCenterEffect resultPicture = null;
		try {
			resultPicture = marketingCenterEffectService.sumMarketingCenterEffect(userInfo.getId(), msgRecord,
					effectPicture.getTradeFrom(), beginTime, endTime);
		} catch (Exception e) {
			logger.info("marketingCenterEffectService.sumMarketingCenterEffect异常，195行");
			e.printStackTrace();
		}
		if (resultPicture != null) {
			logger.info("resultPicture不为NULL！！！！！！！！！！！！！！！！！！！！！！！");
			createMoney = NumberUtils.getResult(resultPicture.getCreateAmount()).doubleValue();// 下单总金额
			createOrderNum = NumberUtils.getResult(resultPicture.getCreateTradeNum());// 订单总数
			createItemNum = NumberUtils.getResult(resultPicture.getCreateItemNum());// 下单商品总数
			createCustomer = NumberUtils.getResult(resultPicture.getCreateBuyerNum());
			successMoney = NumberUtils.getResult(resultPicture.getPayAmount()).doubleValue();// 成交总金额
			successOrderNum = NumberUtils.getResult(resultPicture.getPayTradeNum());// 成交订单数
			successItemNum = NumberUtils.getResult(resultPicture.getPayItemNum());// 成交商品数
			successCustomer = NumberUtils.getResult(resultPicture.getPayBuyerNum());// 成交客户数
			waitMoney = NumberUtils.getResult(resultPicture.getWaitPayAmount()).doubleValue();// 未付款总金额
			waitOrderNum = NumberUtils.getResult(resultPicture.getWaitPayTradeNum());// 未付款订单数
			waitItemNum = NumberUtils.getResult(resultPicture.getWaitPayItemNum());// 未付款商品数
			waitCustomer = NumberUtils.getResult(resultPicture.getWaitPayBuyerNum());// 未付款客户数
			refundMoney = NumberUtils.getResult(resultPicture.getRefundAmount()).doubleValue();// 退款总金额
			refundOrderNum = NumberUtils.getResult(resultPicture.getRefundTradeNum());// 退款订单数
			refundItemNum = NumberUtils.getResult(resultPicture.getRefundItemNum());// 退款商品数
			refundCustomer = NumberUtils.getResult(resultPicture.getRefundBuyerNum());// 退款客户数
		}
		// 计算每日数据
		List<PayOrderEffectDetailVO> daysDetailVOs = marketingCenterEffectService.listPayData(userInfo.getId(), msgId,
				effectPicture.getTradeFrom(), beginTime, endTime);
		// 填充每日数据，没有的为0
		List<PayOrderEffectDetailVO> resultDetailVOs = new ArrayList<PayOrderEffectDetailVO>();
		List<String> dateList = new ArrayList<String>();
		List<Double> feeList = new ArrayList<Double>();
		// if(daysDetailVOs != null && !daysDetailVOs.isEmpty()){
		// for (PayOrderEffectDetailVO dayDetail : daysDetailVOs) {
		// dateList.add(DateUtils.dateToString(dayDetail.getPayTime()));
		// feeList.add(dayDetail.getPayment().doubleValue());
		// }
		// }

		for (int i = 0; i < dayNum; i++) {
			// 每日的数据，先全部存成0
			PayOrderEffectDetailVO notSetDetail = new PayOrderEffectDetailVO();
			notSetDetail.setCustomerNum(0);
			notSetDetail.setEndTime(DateUtils.dateToString(DateUtils.nDaysAfter(i, msgRecord.getSendCreat())));
			notSetDetail.setItemNum(0);
			notSetDetail.setOrderNum(0);
			notSetDetail.setSuccessCusPrice(0.0);
			notSetDetail.setSuccessItemAverageNum(0.0);
			notSetDetail.setSuccessMoney(0.0);
			notSetDetail.setSuccessOrderRate(0.0);
			resultDetailVOs.add(notSetDetail);
			dateList.add(DateUtils.dateToString(DateUtils.nDaysAfter(i, msgRecord.getSendCreat())));
			feeList.add(0.0);
		}
		if (daysDetailVOs != null && !daysDetailVOs.isEmpty()) {
			for (int i = 0; i < daysDetailVOs.size(); i++) {
				PayOrderEffectDetailVO daysEffectDetail = daysDetailVOs.get(i);
				if (daysEffectDetail != null) {
					int effectDay = DateUtils
							.getDiffDay(msgRecord.getSendCreat(), DateUtils
									.getEndTimeOfDay(DateUtils.convertStringToDate(daysEffectDetail.getPayTime())))
							.intValue();
					if (resultDetailVOs.size() > effectDay) {
						PayOrderEffectDetailVO finishedDetail = new PayOrderEffectDetailVO();
						int succCustomer = NumberUtils.getResult(daysEffectDetail.getCustomerNum());
						int succItemNum = (int) NumberUtils.getResult(daysEffectDetail.getItemNum());
						double succMoney = NumberUtils.getResult(daysEffectDetail.getSuccessMoney());
						int succorderNum = NumberUtils.getResult(daysEffectDetail.getTids());
						finishedDetail.setCustomerNum(succCustomer);
						finishedDetail.setItemNum(succItemNum);
						finishedDetail.setSuccessMoney(succMoney);
						finishedDetail.setOrderNum(succorderNum);
						finishedDetail.setSuccessCusPrice(
								succCustomer == 0 ? 0.00 : NumberUtils.getTwoDouble(succMoney / (double) succCustomer));
						finishedDetail.setSuccessItemAverageNum(succCustomer == 0 ? 0.00
								: NumberUtils.getTwoDouble((double) succItemNum / (double) succCustomer));
						finishedDetail.setSuccessOrderRate(createCustomer == 0 ? 0.00
								: NumberUtils.getTwoDouble((double) succCustomer / (double) createCustomer));
						finishedDetail.setEndTime(daysEffectDetail.getPayTime());
						resultDetailVOs.set(effectDay, finishedDetail);
						feeList.set(effectDay, NumberUtils.getTwoDouble(succMoney));
					}
				}
			}
		}
		// 未下单客户数
		int failOrderCustomer = NumberUtils.getResult(msgRecord.getSucceedCount())
				- NumberUtils.getResult(createCustomer);
		resultMap.put("totalCustomer", createCustomer);
		resultMap.put("totalMoney", createMoney);
		resultMap.put("totalOrderNum", createOrderNum);
		resultMap.put("totalItemNum", createItemNum);
		resultMap.put("successCustomer", successCustomer);
		resultMap.put("successMoney", successMoney);
		resultMap.put("successOrderNum", successOrderNum);
		resultMap.put("successItemNum", successItemNum);
		resultMap.put("waitCustomer", waitCustomer);
		resultMap.put("waitMoney", waitMoney);
		resultMap.put("waitOrderNum", waitOrderNum);
		resultMap.put("waitItemNum", waitItemNum);
		resultMap.put("refundCustomer", refundCustomer);
		resultMap.put("refundMoney", refundMoney);
		resultMap.put("refundOrderNum", refundOrderNum);
		resultMap.put("refundItemNum", refundItemNum);
		resultMap.put("failOrderCustomer", failOrderCustomer);
		resultMap.put("totalSendCustomer", totalSendCustomer);
		resultMap.put("successSendCustomer", successSendCustomer);
		resultMap.put("totalSendNum", totalSendNum);
		resultMap.put("totalSendMoney", totalSendMoney);
		Double smsSuccessRate = NumberUtils.getResult(totalSendCustomer) == 0 ? 0.00
				: ((double) NumberUtils.getResult(successSendCustomer)
						/ (double) NumberUtils.getResult(totalSendCustomer));
		String ROIValue = creatROI(totalSendMoney, successMoney);
		double orderCusPrice = NumberUtils.getResult(createCustomer) == 0 ? 0.00
				: NumberUtils.getResult(createMoney) / NumberUtils.getResult(createCustomer);// 订单客单价
		double orderItemAverageNum = NumberUtils.getResult(createOrderNum) == 0 ? 0.00
				: (double) NumberUtils.getResult(createItemNum) / (double) NumberUtils.getResult(createOrderNum);// 平均订单内商品数
		double placeOrderRate = NumberUtils.getResult(successSendCustomer) == 0 ? 0.00
				: ((double) NumberUtils.getResult(createCustomer)
						/ (double) NumberUtils.getResult(successSendCustomer));// 下订单率
		resultMap.put("smsSuccessRate", NumberUtils.getTwoDouble(NumberUtils.getResult(smsSuccessRate) * 100));
		resultMap.put("ROI", ROIValue);
		resultMap.put("orderCusPrice", NumberUtils.getTwoDouble(orderCusPrice));
		resultMap.put("orderItemAverageNum", NumberUtils.getTwoDouble(orderItemAverageNum));
		resultMap.put("placeOrderRate", NumberUtils.getTwoDouble(NumberUtils.getResult(placeOrderRate) * 100));
		double successCusPrice = NumberUtils.getResult(successCustomer) == 0 ? 0.00
				: NumberUtils.getResult(successMoney) / NumberUtils.getResult(successCustomer);// 成交客单价
		double successItemAverageNum = NumberUtils.getResult(successOrderNum) == 0 ? 0.00
				: (double) NumberUtils.getResult(successItemNum) / (double) NumberUtils.getResult(successOrderNum);// 平均成交商品数
		double successOrderRate = NumberUtils.getResult(createCustomer) == 0 ? 0.00
				: ((double) NumberUtils.getResult(successCustomer) / (double) NumberUtils.getResult(createCustomer));// 成交率
		resultMap.put("successCusPrice", NumberUtils.getTwoDouble(successCusPrice));
		resultMap.put("successItemAverageNum", NumberUtils.getTwoDouble(successItemAverageNum));
		resultMap.put("successOrderRate", NumberUtils.getTwoDouble(NumberUtils.getResult(successOrderRate) * 100));
		double waitCusPrice = NumberUtils.getResult(waitCustomer) == 0 ? 0.00
				: NumberUtils.getResult(waitMoney) / NumberUtils.getResult(waitCustomer);// 未付款客单价
		double waitItemAverageNum = NumberUtils.getResult(waitOrderNum) == 0 ? 0.00
				: (double) NumberUtils.getResult(waitItemNum) / (double) NumberUtils.getResult(waitOrderNum);// 平均未付款商品数
		double waitOrderRate = NumberUtils.getResult(createCustomer) == 0 ? 0.00
				: ((double) NumberUtils.getResult(waitCustomer) / (double) NumberUtils.getResult(createCustomer));// 未付款率
		resultMap.put("waitCusPrice", NumberUtils.getTwoDouble(waitCusPrice));
		resultMap.put("waitItemAverageNum", NumberUtils.getTwoDouble(waitItemAverageNum));
		resultMap.put("waitOrderRate", NumberUtils.getTwoDouble(NumberUtils.getResult(waitOrderRate) * 100));
		double failCusPrice = NumberUtils.getResult(refundCustomer) == 0 ? 0.00
				: NumberUtils.getResult(refundMoney) / NumberUtils.getResult(refundCustomer);// 退款客单价
		double failItemAverageNum = NumberUtils.getResult(refundOrderNum) == 0 ? 0.00
				: (double) (NumberUtils.getResult(refundItemNum) / (double) NumberUtils.getResult(refundOrderNum));// 平均退款商品数
		double failOrderRate = NumberUtils.getResult(createCustomer) == 0 ? 0.00
				: ((double) NumberUtils.getResult(refundCustomer) / (double) NumberUtils.getResult(createCustomer));// 退款率
		resultMap.put("refundCusPrice", NumberUtils.getTwoDouble(failCusPrice));
		resultMap.put("refundItemAverageNum", NumberUtils.getTwoDouble(failItemAverageNum));
		resultMap.put("refundOrderRate", NumberUtils.getTwoDouble(NumberUtils.getResult(failOrderRate) * 100));
		// 每日数据表格与折线图X轴(时间)和Y轴(金额)
		resultMap.put("dataDetail", resultDetailVOs);
		resultMap.put("dateList", dateList);
		resultMap.put("feeList", feeList);
		if (msgRecord.getShortLinkType() != null) {
			if (msgRecord.getShortLinkType() == 1) {
				resultMap.put("customerClickNum", null);
				resultMap.put("pageClickNum", null);
				resultMap.put("totalClickNum", null);
				resultMap.put("customerClickRate", null);
			} else if (msgRecord.getShortLinkType() == 2) {
				JSONObject jsonObject = shortLinkService.getAllEffect(userInfo.getId(), msgRecord.getType(),
						msgRecord.getTaoBaoShortLinkId(), beginTime, endTime);
				// 客户及页面点击信息
				resultMap.put("customerClickNum", jsonObject != null ? jsonObject.get("customerClickNum") : 0);
				resultMap.put("pageClickNum", jsonObject != null ? jsonObject.get("pageClickNum") : 0);
				resultMap.put("totalClickNum", jsonObject != null ? jsonObject.get("totalClickNum") : 0);
				resultMap.put("customerClickRate", "0");
			} else {
				return null;
			}
		} else {
			resultMap.put("customerClickNum", null);
			resultMap.put("pageClickNum", null);
			resultMap.put("totalClickNum", null);
			resultMap.put("customerClickRate", null);
		}
		// 查询条件回显
		resultMap.put("beginTime", DateUtils.dateToStringHMS(beginTime));
		resultMap.put("endTime", DateUtils.dateToStringHMS(endTime));
		return resultMap.toJson();
	}

	/**
	 * listCustomerDetail(营销中心效果分析--客户详情数据) @Title: listCustomerDetail @param
	 * 设定文件 @return void 返回类型 @throws
	 */
	@RequestMapping("/customerDetail")
	@ResponseBody
	public String listCustomerDetail(@RequestBody String params, HttpServletResponse response,
			HttpServletRequest request) {
		CustomerDetailVO customerVO = null;
		try {
			customerVO = JsonUtil.paramsJsonToObject(params, CustomerDetailVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if (customerVO == null || customerVO.getStepType() == null) {
			return rsMap(101, "请求参数为空").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (userInfo == null || userInfo.getId() == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		Map<String, Object> detailMap = null;
		try {
			if (customerVO.getStepType() == 2) {
				if (customerVO.geteTimeStr() != null && !"".equals(customerVO.geteTimeStr())) {
					customerVO.seteTime(DateUtils.convertDate(customerVO.geteTimeStr()));
				}
				detailMap = tempTradeService.limitStepCustomerDetail(userInfo.getId(), customerVO, userInfo);
			} else {
				detailMap = tempTradeService.limitCustomerDetail(userInfo.getId(), customerVO, userInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("data", null);
		resultMap.put("totalPage", null);
		resultMap.put("pageNo", null);
		if (detailMap != null) {
			resultMap.put("data", detailMap.get("data"));
			resultMap.put("totalPage", detailMap.get("totalPage"));
			resultMap.put("pageNo", detailMap.get("pageNo"));
		}
		return resultMap.toJson();
	}

	/**
	 * listItemDetail(营销中心效果分析--商品详情数据) @Title: listItemDetail @param
	 * 设定文件 @return void 返回类型 @throws
	 */
	@RequestMapping("/itemDetail")
	@ResponseBody
	public String listItemDetail(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (userInfo == null || userInfo.getId() == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		CustomerDetailVO itemVO = null;
		try {
			itemVO = JsonUtil.paramsJsonToObject(params, CustomerDetailVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if (itemVO == null || itemVO.getStepType() == null) {
			return rsMap(101, "请求参数为空").put("status", false).toJson();
		}
		Map<String, Object> detailMap = null;
		try {
			if (itemVO.getStepType() == 2) {
				if (itemVO.geteTimeStr() != null && !"".equals(itemVO.geteTimeStr())) {
					itemVO.seteTime(DateUtils.convertDate(itemVO.geteTimeStr()));
				}
				detailMap = itemDetailService.limitStepItemDetail(userInfo.getId(), itemVO);
			} else {
				detailMap = itemDetailService.limitItemDetail(userInfo.getId(), itemVO);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		if (detailMap != null) {
			resultMap.put("data", detailMap.get("data")).put("totalPage", detailMap.get("totalPage")).put("pageNo",
					detailMap.get("pageNo"));
		} else {
			resultMap.put("data", null).put("totalPage", null).put("pageNo", null);
		}
		return resultMap.toJson();
	}

	/**
	 * 计算ROI ZTK2017年7月7日上午9:54:08
	 */
	private String creatROI(Double small, Double big) {
		double result = NumberUtils.getResult(small);
		double result2 = NumberUtils.getResult(big);
		if (result != 0.00) {
			double roi = result2 / result;
			return "1:" + NumberUtils.getTwoDouble(roi);
		} else {
			return result + ":0";
		}
	}

	/**
	 * crm_trade_dto crm_order_dto crm_member_info_dto crm_sms_record_dto
	 * crm_msg_temp_trade crm_msg_temp_trade_history crm_item_temp_trade
	 * crm_item_temp_trade_history crm_marketing_center_effect crm_blacklist_dto
	 */

	/**
	 * sumEffectPicture(营销中心效果分析--效果分析综合数据) @Title: sumEffectPicture @param
	 * 设定文件 @return String 返回类型 @throws
	 */
	@ResponseBody
	@RequestMapping("/stepIndex")
	public String sumStepEffectPicture(@RequestBody String params, HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (userInfo == null || userInfo.getId() == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		MarketingCenterEffect effectPicture = null;
		try {
			effectPicture = JsonUtil.paramsJsonToObject(params, MarketingCenterEffect.class);
		} catch (KycrmApiException e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if (effectPicture == null) {
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		if (effectPicture.getTradeFrom() == null || "".equals(effectPicture.getTradeFrom())) {
			effectPicture.setTradeFrom("TOTAL");
		}
		this.cacheService.put("effect", userInfo.getId() + "effect", JsonUtil.toJson(effectPicture), TimeUnit.HOURS,
				1L);
		Long msgId = effectPicture.getMsgId();
		MsgSendRecord msgRecord = msgSendRecordService.queryRecordById(userInfo.getId(), msgId);
		Date beginTime = msgRecord.getSendCreat();
		Date endTime = null;

		Date effectTime = effectPicture.getEffectTime();
		if (effectTime != null) {
			endTime = effectTime;
		} else {
			endTime = new Date();
		}
		Long dayNum = DateUtils.getDiffDay(beginTime, endTime);
		int messageCount = msgRecord.getTemplateContent().length();
		if (messageCount <= 70) {
			messageCount = 1;
		} else {
			messageCount = (messageCount + 66) / 67;
		}
		Integer totalSendCustomer = msgRecord.getTotalCount();// 群发客户总数
		Integer successSendCustomer = msgRecord.getSucceedCount();// 成功发送客户数
		Long deduction = smsRecordDTOService.sumDeductionById(userInfo.getId(), msgRecord.getId());
		Integer totalSendNum = deduction.intValue();// 消耗短信条数
		Double totalSendMoney = NumberUtils.getTwoDouble(NumberUtils.getResult(totalSendNum) * 0.05);// 消费短信金额
		// 下单客户数、下单总金额、订单总数、下单商品总数
		int paidFrontCustomer = successSendCustomer;
		double paidFrontFee = 0.00;
		int paidFrontTrade = 0;
		long paidFrontItem = 0;
		// 成交客户数、成交总金额、成交订单数、成交商品数
		int paidTailCustomer = 0;
		double paidTailFee = 0.00;
		int paidTailTrade = 0;
		long paidTailItem = 0;
		// 未付款客户数、未付款总金额、未付款订单数、未付款商品数
		/* int waitCustomer = 0; */double waitPayFee = 0.00;
		int waitPayTrade = 0;
		long waitPayItem = 0;
		// 退款客户数、退款总金额、退款订单数、退款商品数
		/* int refundCustomer = 0; */double refundFee = 0.00;
		int refundTrade = 0;
		long refundItem = 0;
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		Long l1 = System.currentTimeMillis();
		MarketingCenterEffect resultPicture = null;
		try {
			resultPicture = marketingCenterEffectService.sumMarketingStepCenterEffect(userInfo.getId(), msgRecord,
					effectPicture.getTradeFrom(), beginTime, endTime);
		} catch (Exception e) {
			logger.info("marketingCenterEffectService.sumMarketingCenterEffect异常，195行");
			e.printStackTrace();
		}
		if (resultPicture != null) {
			logger.info("resultPicture不为NULL！！！！！！！！！！！！！！！！！！！！！！！");
			paidFrontFee = NumberUtils
					.getTwoDouble(NumberUtils.getResult(resultPicture.getCreateAmount()).doubleValue());// 下单总金额
			paidFrontTrade = NumberUtils.getResult(resultPicture.getCreateTradeNum());// 订单总数
			paidFrontItem = NumberUtils.getResult(resultPicture.getCreateItemNum());// 下单商品总数
			// paidFrontCustomer =
			// NumberUtils.getResult(resultPicture.getCreateBuyerNum());
			paidTailFee = NumberUtils.getTwoDouble(NumberUtils.getResult(resultPicture.getPayAmount()).doubleValue());// 成交总金额
			paidTailTrade = NumberUtils.getResult(resultPicture.getPayTradeNum());// 成交订单数
			paidTailItem = NumberUtils.getResult(resultPicture.getPayItemNum());// 成交商品数
			paidTailCustomer = NumberUtils.getResult(resultPicture.getPayBuyerNum());// 成交客户数
			waitPayFee = NumberUtils
					.getTwoDouble(NumberUtils.getResult(resultPicture.getWaitPayAmount()).doubleValue());// 未付款总金额
			waitPayTrade = NumberUtils.getResult(resultPicture.getWaitPayTradeNum());// 未付款订单数
			waitPayItem = NumberUtils.getResult(resultPicture.getWaitPayItemNum());// 未付款商品数
			// waitCustomer =
			// NumberUtils.getResult(resultPicture.getWaitPayBuyerNum());//未付款客户数
			refundFee = NumberUtils.getTwoDouble(NumberUtils.getResult(resultPicture.getRefundAmount()).doubleValue());// 退款总金额
			refundTrade = NumberUtils.getResult(resultPicture.getRefundTradeNum());// 退款订单数
			refundItem = NumberUtils.getResult(resultPicture.getRefundItemNum());// 退款商品数
			// refundCustomer =
			// NumberUtils.getResult(resultPicture.getRefundBuyerNum());//退款客户数
		}
		Long l2 = System.currentTimeMillis();

		// 填充每日数据，没有的为0
		List<PayOrderEffectDetailVO> resultDetailVOs = null;
		List<String> dateList = null;
		List<Double> feeList = null;
		try {
			logger.info("查询上部数据耗时：" + (l2 - l1) + "ms");
			// 计算每日数据
			List<PayOrderEffectDetailVO> daysDetailVOs = marketingCenterEffectService.listStepPayData(userInfo.getId(),
					msgId, effectPicture.getTradeFrom(), beginTime, endTime);
			List<PayOrderEffectDetailVO> daysPayFrontVOs = marketingCenterEffectService
					.listStepPayFrontData(userInfo.getId(), msgId, effectPicture.getTradeFrom(), beginTime, endTime);
			Long l3 = System.currentTimeMillis();
			logger.info("查询每日数据耗时：" + (l3 - l2) + "ms，开始填充每日数据");
			resultDetailVOs = new ArrayList<PayOrderEffectDetailVO>();
			dateList = new ArrayList<String>();
			feeList = new ArrayList<Double>();

			for (int i = 0; i < dayNum; i++) {
				// 每日的数据，先全部存成0
				PayOrderEffectDetailVO notSetDetail = new PayOrderEffectDetailVO();
				notSetDetail.setEndTime(DateUtils.dateToString(DateUtils.nDaysAfter(i, msgRecord.getSendCreat())));
				notSetDetail.setCustomerNum(0);
				notSetDetail.setPaidFrontFee(0.0);
				notSetDetail.setPaidFrontTrade(0);
				notSetDetail.setPaidTailFee(0.0);
				notSetDetail.setPaidTailTrade(0);
				notSetDetail.setSuccessItemAverageNum(0.0);
				notSetDetail.setStepPaidRatio("0.0");
				// notSetDetail.setItemNum(0);
				// notSetDetail.setOrderNum(0);
				// notSetDetail.setSuccessCusPrice(0.0);
				// notSetDetail.setSuccessMoney(0.0);
				// notSetDetail.setSuccessOrderRate(0.0);
				resultDetailVOs.add(notSetDetail);
				dateList.add(DateUtils.dateToString(DateUtils.nDaysAfter(i, msgRecord.getSendCreat())));
				feeList.add(0.0);
			}
			if (daysDetailVOs != null && !daysDetailVOs.isEmpty()) {
				for (int i = 0; i < daysDetailVOs.size(); i++) {
					PayOrderEffectDetailVO daysEffectDetail = daysDetailVOs.get(i);
					if (daysEffectDetail != null) {
						int effectDay = DateUtils
								.getDiffDay(msgRecord.getSendCreat(),
										DateUtils.getEndTimeOfDay(
												DateUtils.convertStringToDate(daysEffectDetail.getPayTime())))
								.intValue();
						if (resultDetailVOs.size() > effectDay) {
							PayOrderEffectDetailVO finishedDetail = new PayOrderEffectDetailVO();
							int succCustomer = NumberUtils.getResult(daysEffectDetail.getCustomerNum());
							int succItemNum = (int) NumberUtils.getResult(daysEffectDetail.getItemNum());
							finishedDetail.setEndTime(daysEffectDetail.getPayTime());
							finishedDetail.setCustomerNum(succCustomer);
							finishedDetail.setPaidFrontFee(daysEffectDetail.getPaidFrontFee());
							finishedDetail.setPaidFrontTrade(daysEffectDetail.getPaidFrontTrade());
							finishedDetail.setPaidTailFee(NumberUtils.getTwoDouble(daysEffectDetail.getPaidTailFee()));
							finishedDetail.setPaidTailTrade(daysEffectDetail.getPaidTailTrade());
							finishedDetail.setSuccessItemAverageNum(succCustomer == 0 ? 0.00
									: NumberUtils.getTwoDouble((double) succItemNum / (double) succCustomer));
							finishedDetail
									.setStepPaidRatio(
											paidFrontTrade == 0 ? "0.0"
													: NumberUtils.getTwoDouble((double) NumberUtils
															.getResult(daysEffectDetail.getPaidFrontTrade())
															/ paidFrontTrade) + "");
							finishedDetail.setPaidFrontFee(0.0);
							finishedDetail.setPaidFrontTrade(0);
							// finishedDetail.setSuccessCusPrice(succCustomer ==
							// 0? 0.00 : NumberUtils.getTwoDouble(succMoney /
							// (double)succCustomer));
							// double succMoney =
							// NumberUtils.getResult(daysEffectDetail.getSuccessMoney());
							// int succorderNum =
							// NumberUtils.getResult(daysEffectDetail.getTids());
							// finishedDetail.setItemNum(succItemNum);
							// finishedDetail.setSuccessMoney(succMoney);
							// finishedDetail.setOrderNum(succorderNum);
							// finishedDetail.setSuccessOrderRate(paidFrontCustomer
							// == 0? 0.00 :
							// NumberUtils.getTwoDouble((double)succCustomer /
							// (double)paidFrontCustomer));
							resultDetailVOs.set(effectDay, finishedDetail);
							feeList.set(effectDay, NumberUtils.getTwoDouble(daysEffectDetail.getPayment() == null ? 0.0
									: daysEffectDetail.getPayment().doubleValue()));
						}
					}
				}
			}
			if (daysPayFrontVOs != null && !daysPayFrontVOs.isEmpty()) {
				for (int i = 0; i < daysDetailVOs.size(); i++) {
					PayOrderEffectDetailVO daysEffectDetail = daysDetailVOs.get(i);
					if (daysEffectDetail != null) {
						int effectDay = DateUtils
								.getDiffDay(msgRecord.getSendCreat(),
										DateUtils.getEndTimeOfDay(
												DateUtils.convertStringToDate(daysEffectDetail.getPayTime())))
								.intValue();
						if (resultDetailVOs.size() > effectDay) {
							PayOrderEffectDetailVO finishedDetail = resultDetailVOs.get(effectDay);
							finishedDetail.setPaidFrontFee(daysEffectDetail.getPaidFrontFee() == null ? 0.0
									: daysEffectDetail.getPaidFrontFee());
							finishedDetail.setPaidFrontTrade(daysEffectDetail.getPaidFrontTrade() == null ? 0
									: daysEffectDetail.getPaidFrontTrade());
							// finishedDetail.setSuccessCusPrice(succCustomer ==
							// 0? 0.00 : NumberUtils.getTwoDouble(succMoney /
							// (double)succCustomer));
							// double succMoney =
							// NumberUtils.getResult(daysEffectDetail.getSuccessMoney());
							// int succorderNum =
							// NumberUtils.getResult(daysEffectDetail.getTids());
							// finishedDetail.setItemNum(succItemNum);
							// finishedDetail.setSuccessMoney(succMoney);
							// finishedDetail.setOrderNum(succorderNum);
							// finishedDetail.setSuccessOrderRate(paidFrontCustomer
							// == 0? 0.00 :
							// NumberUtils.getTwoDouble((double)succCustomer /
							// (double)paidFrontCustomer));
							resultDetailVOs.set(effectDay, finishedDetail);
						}
					}
				}
			}
			Long l4 = System.currentTimeMillis();
			logger.info("填充每日数据耗时" + (l4 - l3) + "ms");
		} catch (Exception e1) {
			logger.info("yichangyichangyichangyichangyichangyichangyichangyichang ： " + e1.getMessage());
			e1.printStackTrace();
		}
		// 未下单客户数
		// int failOrderCustomer =
		// NumberUtils.getResult(msgRecord.getSucceedCount()) -
		// NumberUtils.getResult(paidFrontCustomer);
		resultMap.put("paidFrontCustomer", paidFrontCustomer);
		resultMap.put("paidFrontFee", paidFrontFee);
		resultMap.put("paidFrontTrade", paidFrontTrade);
		resultMap.put("paidFrontItem", paidFrontItem);
		resultMap.put("paidTailCustomer", paidTailCustomer);
		resultMap.put("paidTailFee", paidTailFee);
		resultMap.put("paidTailTrade", paidTailTrade);
		resultMap.put("paidTailItem", paidTailItem);
		// resultMap.put("waitCustomer", waitCustomer);
		resultMap.put("waitPayFee", waitPayFee);
		resultMap.put("waitPayTrade", waitPayTrade);
		resultMap.put("waitPayItem", waitPayItem);
		// resultMap.put("refundCustomer", refundCustomer);
		resultMap.put("refundFee", refundFee);
		resultMap.put("refundTrade", refundTrade);
		resultMap.put("refundItem", refundItem);
		// resultMap.put("failOrderCustomer", failOrderCustomer);
		resultMap.put("totalSendCustomer", totalSendCustomer);
		resultMap.put("successSendCustomer", successSendCustomer);
		resultMap.put("totalSendNum", totalSendNum);
		resultMap.put("totalSendMoney", totalSendMoney);
		Double smsSuccessRate = NumberUtils.getResult(totalSendCustomer) == 0 ? 0.00
				: ((double) NumberUtils.getResult(successSendCustomer)
						/ (double) NumberUtils.getResult(totalSendCustomer));
		String ROIValue = creatROI(totalSendMoney, paidTailFee);
		// double orderCusPrice = NumberUtils.getResult(paidFrontCustomer) == 0
		// ? 0.00 : NumberUtils.getResult(paidFrontFee) /
		// NumberUtils.getResult(paidFrontCustomer);//订单客单价
		double paidFrontItemAvg = NumberUtils.getResult(paidFrontTrade) == 0 ? 0.00
				: (double) NumberUtils.getResult(paidFrontItem) / (double) NumberUtils.getResult(paidFrontTrade);// 平均订单内商品数
		// double placeOrderRate = NumberUtils.getResult(successSendCustomer) ==
		// 0? 0.00 : ((double)NumberUtils.getResult(paidFrontCustomer) /
		// (double)NumberUtils.getResult(successSendCustomer));//下订单率
		resultMap.put("smsSuccessRate", NumberUtils.getTwoDouble(NumberUtils.getResult(smsSuccessRate) * 100));
		resultMap.put("ROI", ROIValue);
		// resultMap.put("orderCusPrice",
		// NumberUtils.getTwoDouble(orderCusPrice));
		resultMap.put("paidFrontItemAvg", NumberUtils.getTwoDouble(paidFrontItemAvg));
		// resultMap.put("placeOrderRate",
		// NumberUtils.getTwoDouble(NumberUtils.getResult(placeOrderRate) *
		// 100));
		double successCusPrice = NumberUtils.getResult(paidTailCustomer) == 0 ? 0.00
				: NumberUtils.getResult(paidTailFee) / NumberUtils.getResult(paidTailCustomer);// 成交客单价
		double paidTailItemAvg = NumberUtils.getResult(paidTailTrade) == 0 ? 0.00
				: (double) NumberUtils.getResult(paidTailItem) / (double) NumberUtils.getResult(paidTailTrade);// 平均成交商品数
		// double successOrderRate = NumberUtils.getResult(paidFrontCustomer) ==
		// 0 ? 0.00 : ((double)NumberUtils.getResult(paidTailCustomer) /
		// (double)NumberUtils.getResult(paidFrontCustomer));//成交率
		resultMap.put("successCusPrice", NumberUtils.getTwoDouble(successCusPrice));
		resultMap.put("paidTailItemAvg", NumberUtils.getTwoDouble(paidTailItemAvg));
		// resultMap.put("successOrderRate",
		// NumberUtils.getTwoDouble(NumberUtils.getResult(successOrderRate) *
		// 100));
		// double waitCusPrice = NumberUtils.getResult(waitCustomer) == 0? 0.00
		// : NumberUtils.getResult(waitMoney) /
		// NumberUtils.getResult(waitCustomer);//未付款客单价
		double waitPayItemAvg = NumberUtils.getResult(waitPayTrade) == 0 ? 0.00
				: (double) NumberUtils.getResult(waitPayItem) / (double) NumberUtils.getResult(waitPayTrade);// 平均未付款商品数
		// double waitOrderRate = NumberUtils.getResult(paidFrontCustomer) == 0
		// ? 0.00 : ((double)NumberUtils.getResult(waitCustomer) /
		// (double)NumberUtils.getResult(paidFrontCustomer));//未付款率
		// resultMap.put("waitCusPrice",
		// NumberUtils.getTwoDouble(waitCusPrice));
		resultMap.put("waitPayItemAvg", NumberUtils.getTwoDouble(waitPayItemAvg));
		// resultMap.put("waitOrderRate",
		// NumberUtils.getTwoDouble(NumberUtils.getResult(waitOrderRate) *
		// 100));
		// double failCusPrice = NumberUtils.getResult(refundCustomer) == 0?
		// 0.00 : NumberUtils.getResult(refundMoney) /
		// NumberUtils.getResult(refundCustomer);//退款客单价
		double refundItemAvg = NumberUtils.getResult(refundTrade) == 0 ? 0.00
				: (double) (NumberUtils.getResult(refundItem) / (double) NumberUtils.getResult(refundTrade));// 平均退款商品数
		// double failOrderRate = NumberUtils.getResult(paidFrontCustomer) == 0?
		// 0.00 : ((double)NumberUtils.getResult(refundCustomer) /
		// (double)NumberUtils.getResult(paidFrontCustomer));//退款率
		// resultMap.put("refundCusPrice",
		// NumberUtils.getTwoDouble(failCusPrice));
		resultMap.put("refundItemAvg", NumberUtils.getTwoDouble(refundItemAvg));
		// resultMap.put("refundOrderRate",
		// NumberUtils.getTwoDouble(NumberUtils.getResult(failOrderRate) *
		// 100));
		// 金额占比
		Map<String, Object> tidMap = null;
		try {
			tidMap = tradeService.sumPaymentByTids(userInfo.getId(), msgRecord);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Double totalPayment = 0.0;
		Integer tids = 0;
		if (tidMap != null) {
			// Double paymentStr = ;
			totalPayment = (Double) tidMap.get("payment");
			tids = (Integer) tidMap.get("tids");
		}
		// 应付订单总金额
		double paidFrontFeeRatio = totalPayment == 0.0 ? 0.0 : paidFrontFee / totalPayment;
		double paidTailFeeRatio = totalPayment == 0.0 ? 0.0 : paidTailFee / totalPayment;
		double waitPayFeeRatio = totalPayment == 0.0 ? 0.0 : waitPayFee / totalPayment;
		double refundFeeRatio = totalPayment == 0.0 ? 0.0 : refundFee / totalPayment;
		resultMap.put("paidFrontFeeRatio", NumberUtils.getTwoDouble(paidFrontFeeRatio * 100) + "");
		resultMap.put("paidTailFeeRatio", NumberUtils.getTwoDouble(paidTailFeeRatio * 100) + "");
		resultMap.put("waitPayFeeRatio", NumberUtils.getTwoDouble(waitPayFeeRatio * 100) + "");
		resultMap.put("refundFeeRatio", NumberUtils.getTwoDouble(refundFeeRatio * 100) + "");
		// 列表预售订单成交率占比
		Double totalStepPaidRatio = tids == 0 ? 0.0 : (double) paidTailTrade / tids;
		String totalStepPaidRatioStr = NumberUtils.getTwoDouble(totalStepPaidRatio * 100) + "";
		resultMap.put("totalStepPaidRatio", totalStepPaidRatioStr);
		// 每日数据表格与折线图X轴(时间)和Y轴(金额)
		resultMap.put("dataDetail", resultDetailVOs);
		resultMap.put("dateList", dateList);
		resultMap.put("feeList", feeList);
		if (msgRecord.getShortLinkType() != null) {
			if (msgRecord.getShortLinkType() == 1) {
				resultMap.put("customerClickNum", null);
				resultMap.put("pageClickNum", null);
				resultMap.put("totalClickNum", null);
				resultMap.put("customerClickRate", null);
			} else {
				JSONObject jsonObject = shortLinkService.getAllEffect(userInfo.getId(), msgRecord.getType(),
						msgRecord.getTaoBaoShortLinkId(), beginTime, endTime);
				// 客户及页面点击信息
				resultMap.put("customerClickNum", jsonObject != null ? jsonObject.get("customerClickNum") : 0);
				resultMap.put("pageClickNum", jsonObject != null ? jsonObject.get("pageClickNum") : 0);
				resultMap.put("totalClickNum", jsonObject != null ? jsonObject.get("totalClickNum") : 0);
				resultMap.put("customerClickRate", "0");
			}
		} else {
			resultMap.put("customerClickNum", null);
			resultMap.put("pageClickNum", null);
			resultMap.put("totalClickNum", null);
			resultMap.put("customerClickRate", null);
		}
		// 查询条件回显
		resultMap.put("beginTime", DateUtils.dateToStringHMS(beginTime));
		resultMap.put("endTime", DateUtils.dateToStringHMS(endTime));
		return resultMap.toJson();
	}

}
