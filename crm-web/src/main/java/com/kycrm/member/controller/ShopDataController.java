package com.kycrm.member.controller;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.message.MessageBill;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.multishopbinding.MultiShopBindingSendMessageRecordDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserRecharge;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.multishopbinding.IMultiShopBindingSendMessageRecordService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserRechargeService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.DateUtils;
import com.kycrm.util.ExcelExportUtils;
import com.kycrm.util.GetCurrentPageUtil;
import com.kycrm.util.JsonUtil;

/**
 * 店铺数据--账单
 * 
 * @ClassName: ShopDataController
 * @author ztk
 * @date 2018年9月15日 下午2:58:55
 */
@Controller
@RequestMapping("/shopData")
public class ShopDataController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(ShopDataController.class);

	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;

	// 充值记录
	@Autowired
	private IUserRechargeService userRechargeService;

	@Autowired
	private IUserAccountService userAccountService;

	// 获赠|赠送记录
	@Autowired
	private IMultiShopBindingSendMessageRecordService multiShopBindingSendMessageRecordService;

	@Autowired
	private SessionProvider sessionProvider;

	@RequestMapping("/index")
	public String index() {
		return "bsAdmin/index";
	}

	/**
	 * listReport(店铺数据，账单列表) @Title: listReport @param @param
	 * response @param @param request @param @param params @param @return
	 * 设定文件 @return String 返回类型 @throws
	 */
	@RequestMapping(value = "/reportData", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reportData(HttpServletResponse response, HttpServletRequest request, @RequestBody String params) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (userInfo == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		SmsRecordVO reportVO = null;
		try {
			reportVO = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
			logger.info("UID = " + userInfo.getId() + " 短信账单饼图入参 = " + JsonUtil.toJson(reportVO));
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败，请重新操作或联系系统管理员").put("status", false).toJson();
		}
		if (reportVO == null) {
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		String dateType = reportVO.getDateType();
		Integer pageNo = reportVO.getPageNo();
		String bTimeStr = reportVO.getbTime();
		String eTimeStr = reportVO.geteTime();
		if (dateType == null || "".equals(dateType)) {
			dateType = "day";
		}
		if (pageNo == null) {
			pageNo = 1;
		}
		Date bTime = null, eTime = null;
		if ("day".equals(dateType)) {
			if (bTimeStr != null && !"".equals(bTimeStr)) {
				bTime = DateUtils.getStartTimeOfDay(DateUtils.convertStringToDate(bTimeStr));
			}
			if (eTimeStr != null && !"".equals(eTimeStr)) {
				eTime = DateUtils.getEndTimeOfDay(DateUtils.convertStringToDate(eTimeStr));
			}
		} else if ("month".equals(dateType)) {
			if (bTimeStr != null && !"".equals(bTimeStr) && bTimeStr.length() >= 7) {
				bTime = DateUtils.getFirstDayOfMonth(Integer.parseInt(bTimeStr.substring(0, 4)),
						Integer.parseInt(bTimeStr.substring(5, 7)));
			}
			if (eTimeStr != null && !"".equals(eTimeStr) && eTimeStr.length() >= 7) {
				eTime = DateUtils.getLastDayOfMonth(Integer.parseInt(eTimeStr.substring(0, 4)),
						Integer.parseInt(eTimeStr.substring(5, 7)));
			}
		} else if ("year".equals(dateType)) {
			if (bTimeStr != null && !"".equals(bTimeStr) && bTimeStr.length() >= 4) {
				bTime = DateUtils.getYearFirst(Integer.parseInt(bTimeStr.substring(0, 4)));
			}
			if (eTimeStr != null && !"".equals(eTimeStr) && eTimeStr.length() >= 4) {
				eTime = DateUtils.getYearLast(Integer.parseInt(eTimeStr.substring(0, 4)));
			}
		}
		Long rechargeRecordCount = 0L;
		Long messageSendCount = 0L;
		Long singleSendCount = 0L;
		Long singleReceiveCount = 0L;
		Long totalReceiveCount = 0L;
		try {
			// 充值条数
			rechargeRecordCount = this.userRechargeService.findRechargeRecordCount(userInfo.getId(), dateType, bTime,
					eTime);
			// 短信发送条数
			messageSendCount = this.smsRecordDTOService.sumReportSmsNum(userInfo.getId(), dateType, bTime, eTime);
			// 赠送条数
			singleSendCount = this.multiShopBindingSendMessageRecordService.findSingleSendCount(userInfo.getId(),
					dateType, bTime, eTime);
			// 获赠条数
			singleReceiveCount = this.multiShopBindingSendMessageRecordService.findSingleReceiveCount(userInfo.getId(),
					dateType, bTime, eTime);
			Long rechargeByOA = this.userRechargeService.findRechargeRecordCountByType(userInfo.getId(), bTime, eTime,
					"5");
			rechargeByOA = rechargeByOA == null ? 0L : rechargeByOA;
			totalReceiveCount = singleReceiveCount + rechargeByOA;
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("rechargeRecordCount", rechargeRecordCount == null ? 0L : rechargeRecordCount);
		resultMap.put("messageSendCount", messageSendCount == null ? 0L : messageSendCount);
		resultMap.put("singleSendCount", singleSendCount == null ? 0L : singleSendCount);
		resultMap.put("singleReceiveCount", totalReceiveCount == null ? 0L : totalReceiveCount);
		return resultMap.toJson();
	}

	/**
	 * listReport(店铺数据，账单列表) @Title: listReport @param @param
	 * response @param @param request @param @param params @param @return
	 * 设定文件 @return String 返回类型 @throws
	 */
	@RequestMapping(value = "/reportList", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	@ResponseBody
	public String reportList(HttpServletResponse response, HttpServletRequest request, @RequestBody String params) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (userInfo == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		SmsRecordVO reportVO = null;
		try {
			reportVO = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
			logger.info("UID = " + userInfo.getId() + " 短信账单列表入参 = " + JsonUtil.toJson(reportVO));
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败，请重新操作或联系系统管理员").put("status", false).toJson();
		}
		if (reportVO == null) {
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		String dateType = reportVO.getDateType();
		Integer pageNo = reportVO.getPageNo();
		String bTimeStr = reportVO.getbTime();
		String eTimeStr = reportVO.geteTime();
		if (dateType == null || "".equals(dateType)) {
			dateType = "day";
		}
		if (pageNo == null) {
			pageNo = 1;
		}
		Date bTime = null, eTime = null;
		if ("day".equals(dateType)) {
			if (bTimeStr != null && !"".equals(bTimeStr)) {
				bTime = DateUtils.getStartTimeOfDay(DateUtils.convertStringToDate(bTimeStr));
			} else {
				bTime = DateUtils.addDate(new Date(), -9);
			}
			if (eTimeStr != null && !"".equals(eTimeStr)) {
				eTime = DateUtils.getEndTimeOfDay(DateUtils.convertStringToDate(eTimeStr));
			} else {
				eTime = new Date();
			}
		} else if ("month".equals(dateType)) {
			if (bTimeStr != null && !"".equals(bTimeStr) && bTimeStr.length() >= 7) {
				bTime = DateUtils.getFirstDayOfMonth(Integer.parseInt(bTimeStr.substring(0, 4)),
						Integer.parseInt(bTimeStr.substring(5, 7)));
			} else {
				bTime = DateUtils.addMonth(new Date(), -9);
			}
			if (eTimeStr != null && !"".equals(eTimeStr) && eTimeStr.length() >= 7) {
				eTime = DateUtils.getLastDayOfMonth(Integer.parseInt(eTimeStr.substring(0, 4)),
						Integer.parseInt(eTimeStr.substring(5, 7)));
			} else {
				eTime = new Date();
			}
		} else if ("year".equals(dateType)) {
			if ((bTimeStr != null && !"".equals(bTimeStr) && bTimeStr.length() >= 4)
					&& (eTimeStr == null || "".equals(eTimeStr))) {
				bTime = DateUtils.getYearFirst(Integer.parseInt(bTimeStr.substring(0, 4)));
				eTime = new Date(System.currentTimeMillis());
			} else if ((bTimeStr == null || "".equals(bTimeStr))
					&& (eTimeStr != null && !"".equals(eTimeStr) && eTimeStr.length() >= 4)) {
				bTime = userInfo.getCreateTime();
				eTime = DateUtils.getYearLast(Integer.parseInt(eTimeStr.substring(0, 4)));
			} else if ((bTimeStr != null && !"".equals(bTimeStr) && bTimeStr.length() >= 4)
					&& (eTimeStr != null && !"".equals(eTimeStr) && eTimeStr.length() >= 4)) {
				bTime = DateUtils.getYearFirst(Integer.parseInt(bTimeStr.substring(0, 4)));
				eTime = DateUtils.getYearLast(Integer.parseInt(eTimeStr.substring(0, 4)));
			} else {
				bTime = userInfo.getCreateTime();
				eTime = new Date(System.currentTimeMillis());
			}
		}
		int totalPage = 0;
		Long totalSmsNum = 0L;
		List<MessageBill> messageBillList = new ArrayList<MessageBill>();
		try {
			Date createTime = userInfo.getCreateTime();
			Integer dateRange = 10;
			Integer reportSize = this.getReportSize(dateType, bTimeStr, eTimeStr, createTime, bTime, eTime);
			List<Date> dateList = this.assembleDate(dateType, bTime, eTime, pageNo, dateRange, reportSize);
			if (pageNo > 1) {
				bTime = dateList.get(dateList.size() - 1);
				eTime = dateList.get(0);
			}
			List<String> dateStrList = new ArrayList<String>();
			for (int i = 0; i < dateList.size(); i++) {
				if ("day".equals(dateType)) {
					dateStrList.add(DateUtils.formatDate(dateList.get(i), "yyyy-MM-dd"));
				} else if ("month".equals(dateType)) {
					dateStrList.add(DateUtils.formatDate(dateList.get(i), "yyyy-MM"));
				} else {
					dateStrList.add(DateUtils.formatDate(dateList.get(i), "yyyy"));
				}
			}
			// 充值条数
			List<UserRecharge> userRechargeList = this.getUserRechargeList(userInfo.getId(), dateStrList, dateType,
					bTime, eTime);
			// 短信发送条数
			List<SmsRecordDTO> smsRecordList = this.getSmsRecordList(userInfo.getId(), dateStrList, dateType, bTime,
					eTime);
			// 赠送条数
			List<MultiShopBindingSendMessageRecordDTO> sendCountList = this.getSendCountList(userInfo.getId(),
					dateStrList, dateType, bTime, eTime);
			// 获赠条数
			List<MultiShopBindingSendMessageRecordDTO> receiveCountList = this.getReceiveCountList(userInfo.getId(),
					dateStrList, dateType, bTime, eTime);
			// 当前剩余条数
			Long userAccountSms = this.userAccountService.findUserAccountSms(userInfo.getId());
			MessageBill messageBill = null;
			for (int m = 0; m < dateList.size(); m++) {
				messageBill = new MessageBill();
				if ("day".equals(dateType)) {
					messageBill.setSendDate(DateUtils.formatDate(dateList.get(m), "yyyy-MM-dd"));
				} else if ("month".equals(dateType)) {
					messageBill.setSendDate(DateUtils.formatDate(dateList.get(m), "yyyy-MM"));
				} else {
					messageBill.setSendDate(DateUtils.formatDate(dateList.get(m), "yyyy"));
				}
				Integer rechargeNum = userRechargeList.get(m).getRechargeNum() == null ? 0
						: userRechargeList.get(m).getRechargeNum();
				Integer recriveCount = receiveCountList.get(m).getSendMessageCount() == null ? 0
						: receiveCountList.get(m).getSendMessageCount();
				// 增加短信条数 = 充值条数 + 获赠条数
				messageBill.setAddMessageCount(rechargeNum + recriveCount);
				Integer actualDeduction = smsRecordList.get(m).getActualDeduction() == null ? 0
						: smsRecordList.get(m).getActualDeduction();
				Integer sendMessageCount = sendCountList.get(m).getSendMessageCount() == null ? 0
						: sendCountList.get(m).getSendMessageCount();
				// 扣除短信条数=发送条数 + 赠送条数
				messageBill.setDeductMessageCount(actualDeduction + sendMessageCount);
				// 剩余短信条数= 当前短信条数 - 扣除短信条数
				messageBill.setRestMessageCount(userAccountSms.intValue() - messageBill.getDeductMessageCount());
				messageBillList.add(messageBill);
			}
			totalPage = GetCurrentPageUtil.getTotalPage(reportSize, ConstantUtils.PAGE_SIZE_MIDDLE);
			totalSmsNum = smsRecordDTOService.sumReportSmsNum(userInfo.getId(), dateType, bTime, eTime);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true).put("data", messageBillList);
		resultMap.put("pageNo", pageNo == null ? 0L : pageNo);
		resultMap.put("totalPage", totalPage);
		resultMap.put("totalNum", totalSmsNum == null ? 0L : totalSmsNum);
		return resultMap.toJson();
	}

	private List<Date> assembleDate(String dateType, Date bTime, Date eTime, Integer pageNo, Integer dateRange,
			Integer reportSize) {
		List<Date> dateList = new ArrayList<Date>();
		Integer baseDate = 0;
		Integer reportRange = 0;
		if (pageNo == 1) {
			baseDate = pageNo * dateRange - dateRange;
			reportRange = reportSize < dateRange ? reportSize : pageNo * dateRange;
		} else {
			baseDate = pageNo * dateRange - dateRange;
			reportRange = (reportSize - (pageNo * dateRange - dateRange)) < dateRange ? reportSize : pageNo * dateRange;
		}
		if ("day".equals(dateType)) {
			for (int i = baseDate; i < reportRange; i++) {
				if (i == baseDate) {
					dateList.add(DateUtils.getEndTimeOfDay(DateUtils.addDate(eTime, -i)));
				} else {
					dateList.add(DateUtils.getStartTimeOfDay(DateUtils.addDate(eTime, -i)));
				}
			}
		} else if ("month".equals(dateType)) {
			for (int i = baseDate; i < reportRange; i++) {
				Date date = DateUtils.addMonth(eTime, -i);
				String bTimeStr = DateUtils.formatDate(date, "yyyy-MM-dd");
				if (i == baseDate) {
					dateList.add(DateUtils.getLastDayOfMonth(Integer.parseInt(bTimeStr.substring(0, 4)),
							Integer.parseInt(bTimeStr.substring(5, 7))));
				} else {
					dateList.add(DateUtils.getFirstDayOfMonth(Integer.parseInt(bTimeStr.substring(0, 4)),
							Integer.parseInt(bTimeStr.substring(5, 7))));
				}
			}
		} else {
			for (int i = baseDate; i < reportRange; i++) {
				if (i == baseDate) {
					dateList.add(DateUtils.getLastDayOfYear(i, eTime));
				} else {
					dateList.add(DateUtils.getFirstDayOfYear(i, eTime));
				}
			}
		}
		return dateList;
	}

	private List<UserRecharge> getUserRechargeList(Long uid, List<String> dateStrList, String dateType, Date bTime,
			Date eTime) throws Exception {
		Map<String, UserRecharge> userRechargeMap = this.userRechargeService.findRechargeRecordCountByDate(uid,
				dateType, bTime, eTime);
		UserRecharge userRecharge = null;
		List<UserRecharge> userRechargeResultList = new ArrayList<UserRecharge>(10);
		if (userRechargeMap.size() == 0) {
			for (int m = 0; m < dateStrList.size(); m++) {
				userRecharge = new UserRecharge();
				userRecharge.setRechargeDateStr(dateStrList.get(m));
				userRecharge.setRechargeNum(0);
				userRechargeResultList.add(userRecharge);
			}
		} else {
			for (int m = 0; m < dateStrList.size(); m++) {
				if (!userRechargeMap.containsKey(dateStrList.get(m))) {
					userRecharge = new UserRecharge();
					userRecharge.setRechargeDateStr(dateStrList.get(m));
					userRecharge.setRechargeNum(0);
					userRechargeResultList.add(userRecharge);
				} else {
					userRechargeResultList.add(userRechargeMap.get(dateStrList.get(m)));
				}
			}
		}
		return userRechargeResultList;
	}

	private List<SmsRecordDTO> getSmsRecordList(Long uid, List<String> dateStrList, String dateType, Date bTime,
			Date eTime) throws Exception {
		Map<String, SmsRecordDTO> smsRecordMap = this.smsRecordDTOService.sumReportSmsNumByDate(uid, dateType, bTime,
				eTime);
		SmsRecordDTO smsRecordDTO = null;
		List<SmsRecordDTO> smsRecordResultList = new ArrayList<SmsRecordDTO>();
		if (smsRecordMap.size() == 0) {
			for (int m = 0; m < dateStrList.size(); m++) {
				smsRecordDTO = new SmsRecordDTO();
				smsRecordDTO.setDisplayDate(dateStrList.get(m));
				smsRecordDTO.setActualDeduction(0);
				smsRecordResultList.add(smsRecordDTO);
			}
		} else {
			for (int m = 0; m < dateStrList.size(); m++) {
				if (!smsRecordMap.containsKey(dateStrList.get(m))) {
					smsRecordDTO = new SmsRecordDTO();
					smsRecordDTO.setDisplayDate(dateStrList.get(m));
					smsRecordDTO.setActualDeduction(0);
					smsRecordResultList.add(smsRecordDTO);
				} else {
					smsRecordResultList.add(smsRecordMap.get(dateStrList.get(m)));
				}
			}
		}
		return smsRecordResultList;
	}

	private List<MultiShopBindingSendMessageRecordDTO> getSendCountList(Long uid, List<String> dateStrList,
			String dateType, Date bTime, Date eTime) throws Exception {
		Map<String, MultiShopBindingSendMessageRecordDTO> sendCountMap = this.multiShopBindingSendMessageRecordService
				.findSingleSendCountByDate(uid, dateType, bTime, eTime);
		MultiShopBindingSendMessageRecordDTO sendMessageRecord = null;
		List<MultiShopBindingSendMessageRecordDTO> sendCountResultList = new ArrayList<MultiShopBindingSendMessageRecordDTO>();
		if (sendCountMap.size() == 0) {
			for (int m = 0; m < dateStrList.size(); m++) {
				sendMessageRecord = new MultiShopBindingSendMessageRecordDTO();
				sendMessageRecord.setSendDateStr(dateStrList.get(m));
				sendMessageRecord.setSendMessageCount(0);
				sendCountResultList.add(sendMessageRecord);
			}
		} else {
			for (int m = 0; m < dateStrList.size(); m++) {
				if (!sendCountMap.containsKey(dateStrList.get(m))) {
					sendMessageRecord = new MultiShopBindingSendMessageRecordDTO();
					sendMessageRecord.setSendDateStr(dateStrList.get(m));
					sendMessageRecord.setSendMessageCount(0);
					sendCountResultList.add(sendMessageRecord);
				} else {
					sendCountResultList.add(sendCountMap.get(dateStrList.get(m)));
				}
			}
		}
		return sendCountResultList;
	}

	private List<MultiShopBindingSendMessageRecordDTO> getReceiveCountList(Long uid, List<String> dateStrList,
			String dateType, Date bTime, Date eTime) throws Exception {
		Map<String, MultiShopBindingSendMessageRecordDTO> receiveCountMap = this.multiShopBindingSendMessageRecordService
				.findSingleReceiveCountByDate(uid, dateType, bTime, eTime);
		MultiShopBindingSendMessageRecordDTO receiveMessageRecord = null;
		List<MultiShopBindingSendMessageRecordDTO> receiveCountResultList = new ArrayList<MultiShopBindingSendMessageRecordDTO>();
		if (receiveCountMap.size() == 0) {
			for (int m = 0; m < dateStrList.size(); m++) {
				receiveMessageRecord = new MultiShopBindingSendMessageRecordDTO();
				receiveMessageRecord.setSendDateStr(dateStrList.get(m));
				receiveMessageRecord.setSendMessageCount(0);
				receiveCountResultList.add(receiveMessageRecord);
			}
		} else {
			for (int m = 0; m < dateStrList.size(); m++) {
				if (!receiveCountMap.containsKey(dateStrList.get(m))) {
					receiveMessageRecord = new MultiShopBindingSendMessageRecordDTO();
					receiveMessageRecord.setSendDateStr(dateStrList.get(m));
					receiveMessageRecord.setSendMessageCount(0);
					receiveCountResultList.add(receiveMessageRecord);
				} else {
					receiveCountResultList.add(receiveCountMap.get(dateStrList.get(m)));
				}
			}
		}
		return receiveCountResultList;
	}

	/**
	 * downloadReport(这里用一句话描述这个方法的作用) @Title: downloadReport @param @return
	 * 设定文件 @return String 返回类型 @throws
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping(value = "/download", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String downloadReport(HttpServletRequest request, HttpServletResponse response, @RequestBody String params) {

		if (params != null && !"".equals(params)) {
			params = "{" + (URLDecoder.decode(params).replace("=", ":")) + "}";
		}
		SmsRecordVO reportVO = null;
		try {
			reportVO = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败，请重新操作或联系系统管理员").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (reportVO == null || userInfo == null || userInfo.getId() == null) {
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		String dateType = reportVO.getDateType();
		String bTimeStr = reportVO.getbTime();
		String eTimeStr = reportVO.geteTime();
		if (dateType == null || "".equals(dateType)) {
			dateType = "day";
		}
		Date bTime = null, eTime = null;
		if ("day".equals(dateType)) {
			if (bTimeStr != null && !"".equals(bTimeStr)) {
				bTime = DateUtils.getStartTimeOfDay(DateUtils.convertStringToDate(bTimeStr));
			}
			if (eTimeStr != null && !"".equals(eTimeStr)) {
				eTime = DateUtils.getEndTimeOfDay(DateUtils.convertStringToDate(eTimeStr));
			} else {
				eTime = new Date(System.currentTimeMillis());
			}
		} else if ("month".equals(dateType)) {
			if (bTimeStr != null && !"".equals(bTimeStr) && bTimeStr.length() >= 7) {
				bTime = DateUtils.getFirstDayOfMonth(Integer.parseInt(bTimeStr.substring(0, 4)),
						Integer.parseInt(bTimeStr.substring(5, 7)));
			}
			if (eTimeStr != null && !"".equals(eTimeStr) && eTimeStr.length() >= 7) {
				eTime = DateUtils.getLastDayOfMonth(Integer.parseInt(eTimeStr.substring(0, 4)),
						Integer.parseInt(eTimeStr.substring(5, 7)));
			} else {
				eTime = new Date(System.currentTimeMillis());
			}
		} else if ("year".equals(dateType)) {
			if (bTimeStr != null && !"".equals(bTimeStr) && bTimeStr.length() >= 4) {
				bTime = DateUtils.getYearFirst(Integer.parseInt(bTimeStr.substring(0, 4)));
			}
			if (eTimeStr != null && !"".equals(eTimeStr) && eTimeStr.length() >= 4) {
				eTime = DateUtils.getYearLast(Integer.parseInt(eTimeStr.substring(0, 4)));
			} else {
				eTime = new Date(System.currentTimeMillis());
			}
		}
		try {
			List<SmsRecordDTO> reportList = smsRecordDTOService.listReportList(userInfo.getId(), dateType, bTime,
					eTime);
			long smsNum = smsRecordDTOService.sumReportSmsNum(userInfo.getId(), dateType, bTime, eTime);
			List<Object[]> resultList = new ArrayList<>();
			if (reportList != null && !reportList.isEmpty()) {
				for (int i = 0; i < reportList.size(); i++) {
					resultList.add(
							new Object[] { reportList.get(i).getContent(), reportList.get(i).getActualDeduction() });
				}
				resultList.add(new Object[] { "合计", smsNum });
			}
			String[] rowNames = new String[] { "时间", "扣除短信条数(条)" };
			String filename = new String("短信账单报表.xls".getBytes(), "ISO-8859-1");
			// 使用工具生产Excel表格
			HSSFWorkbook workbook = ExcelExportUtils.export("短信账单报表", rowNames, resultList);
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment;filename=" + filename);
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private Integer getReportSize(String dateType, String bTimeStr, String eTimeStr, Date createTime, Date bTime,
			Date eTime) throws Exception {
		Integer reportSize = 0;
		if ("day".equals(dateType)) {
			if ((bTimeStr == null || "".equals(bTimeStr)) && (eTimeStr != null && !"".equals(eTimeStr))) {
				reportSize = DateUtils.getDiffDay(createTime, eTime).intValue() + 1;
			} else if ((bTimeStr != null && !"".equals(bTimeStr)) && (eTimeStr == null || "".equals(eTimeStr))) {
				reportSize = DateUtils.getDiffDay(bTime, new Date()).intValue() + 1;
			} else if ((bTimeStr != null && !"".equals(bTimeStr)) && (eTimeStr != null && !"".equals(eTimeStr))) {
				reportSize = DateUtils.getDiffDay(bTime, eTime).intValue() + 1;
			} else {
				reportSize = DateUtils.getDiffDay(createTime, new Date()).intValue() + 1;
			}
		} else if ("month".equals(dateType)) {
			if ((bTimeStr == null || "".equals(bTimeStr)) && (eTimeStr != null && !"".equals(eTimeStr))) {
				reportSize = DateUtils.getMonthDiff(createTime, eTime);
			} else if ((bTimeStr != null && !"".equals(bTimeStr)) && (eTimeStr == null || "".equals(eTimeStr))) {
				reportSize = DateUtils.getMonthDiff(bTime, new Date());
			} else if ((bTimeStr != null && !"".equals(bTimeStr)) && (eTimeStr != null && !"".equals(eTimeStr))) {
				reportSize = DateUtils.getMonthDiff(bTime, eTime);
			} else {
				reportSize = DateUtils.getMonthDiff(createTime, new Date());
			}
		} else {
			int currentYear = Calendar.getInstance().get(Calendar.YEAR);
			if (bTimeStr.equals(currentYear + "")) {
				reportSize = 1;
			} else {
				if ((bTimeStr == null || "".equals(bTimeStr)) && (eTimeStr != null && !"".equals(eTimeStr))) {
					reportSize = DateUtils.getYearDiff(createTime, eTime) + 1;
				} else if ((bTimeStr != null && !"".equals(bTimeStr)) && (eTimeStr == null || "".equals(eTimeStr))) {
					reportSize = DateUtils.getYearDiff(bTime, new Date()) + 1;
				} else if ((bTimeStr != null && !"".equals(bTimeStr)) && (eTimeStr != null && !"".equals(eTimeStr))) {
					reportSize = DateUtils.getYearDiff(bTime, eTime) + 1;
				} else {
					reportSize = DateUtils.getYearDiff(createTime, new Date()) + 1;
				}
			}
		}
		return reportSize;
	}
}
