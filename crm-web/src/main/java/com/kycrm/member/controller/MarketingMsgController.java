package com.kycrm.member.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserOperationLog;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.exception.KycrmApiException;
import com.kycrm.member.service.message.IMsgSendRecordService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.message.ISmsSendInfoScheduleService;
import com.kycrm.member.service.user.IUserOperationLogService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.DateUtils;
import com.kycrm.util.GetCurrentPageUtil;
import com.kycrm.util.JsonUtil;

@Controller
@RequestMapping("/msgSend")
public class MarketingMsgController extends BaseController{

	private Logger logger = LoggerFactory.getLogger(MarketingMsgController.class);
	
	@Autowired
	private IMsgSendRecordService msgRecordService;
	
	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private ISmsRecordDTOService recordDTOService;
	
	@Autowired
	private ISmsSendInfoScheduleService sendScheduleService;
	
	@Autowired
	private IUserOperationLogService userOperationLogService;
	
	@Autowired
	private ISmsSendInfoScheduleService smsSendInfoScheduleService;
	@RequestMapping("/index")
	public String index(){
		return "marketingCenter/index";
	}

	
	/**
	 * listMsgRecord(查看所有总记录批次)
	 * @Title: listMsgRecord 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/msgList")
	@ResponseBody
	public String listMsgRecord(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		SmsRecordVO msgRecordVO = null;
		try {
			msgRecordVO = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
		} catch (KycrmApiException e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || userInfo.getId() == null || msgRecordVO == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		String minSendTime = msgRecordVO.getMinSendTime();
		String maxSendTime = msgRecordVO.getMaxSendTime();
		Integer pageNo = msgRecordVO.getPageNo();
		if(pageNo == null || pageNo == 0){
			pageNo = 1;
			msgRecordVO.setPageNo(pageNo);
		}
		String method = msgRecordVO.getMethod();
		Date bTime = null;
		Date eTime = null;
		if(minSendTime != null && !"".equals(minSendTime)){
			bTime = DateUtils.parseTime(minSendTime);
			msgRecordVO.setBeginTime(bTime);
		}
		if(maxSendTime != null && !"".equals(maxSendTime)){
			eTime = DateUtils.parseTime(maxSendTime);
			msgRecordVO.setEndTime(eTime);
		}
		Boolean isSend = true;
		if("0" == method || "0".equals(method)){
			isSend = false;
		}
		List<MsgSendRecord> msgRecord = msgRecordService.listEffectSendRecord(userInfo.getId(),msgRecordVO,isSend);
		Integer msgRecordCount = msgRecordService.countEffectSendRecord(userInfo.getId(),msgRecordVO,isSend);
		
		Integer totalPage = GetCurrentPageUtil.getTotalPage(msgRecordCount, ConstantUtils.PAGE_SIZE_MIDDLE);
		
		ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("totalPage", totalPage);
		resultMap.put("data", msgRecord);
		resultMap.put("pageNo", pageNo);
		return resultMap.toJson();
	}
	
	/**
	 * listMsgDetail(查看发送总记录详情)
	 * @Title: listMsgDetail 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/smsDetail")
	@ResponseBody
	public String listMsgDetail(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		SmsRecordVO vo = null;
		try {
			vo = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if(userInfo == null || vo == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		Map<String, Object> detailMap = recordDTOService.pageRecordDetail(userInfo.getId(),vo, userInfo);
		ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true);
		if(detailMap != null){
			@SuppressWarnings("unchecked")
			ArrayList<SmsRecordDTO> recordList = (ArrayList<SmsRecordDTO>) detailMap.get("recordList");
			Integer totalCount = (Integer) detailMap.get("recordCount");
			resultMap.put("data", recordList);
			resultMap.put("pageNo", vo.getPageNo());
			int totalPage = GetCurrentPageUtil.getTotalPage(totalCount, ConstantUtils.PAGE_SIZE_MIN);
			resultMap.put("totalPage", totalPage);
		}
		MsgSendRecord msgRecord = msgRecordService.queryRecordById(userInfo.getId(),vo.getMsgId());
		/*
			"actualNum":50,//实际扣费                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
		 */
		if(msgRecord != null){
			Long deduction = recordDTOService.sumDeductionById(userInfo.getId(), vo.getMsgId());
			resultMap.put("totalNum", msgRecord.getTotalCount()==null?0:msgRecord.getTotalCount());
			resultMap.put("successNum", msgRecord.getSucceedCount()==null?0:msgRecord.getSucceedCount());
			resultMap.put("actualNum", deduction);
			resultMap.put("repeatNum", msgRecord.getRepeatCount()==null?0:msgRecord.getRepeatCount());
			resultMap.put("wrongNum", msgRecord.getWrongCount()==null?0:msgRecord.getWrongCount());
			resultMap.put("blackNum", msgRecord.getBlackCount()==null?0:msgRecord.getBlackCount());
			resultMap.put("filterRepeat", msgRecord.getSheildCount()==null?0:msgRecord.getSheildCount());
			resultMap.put("failedNum", msgRecord.getFailedCount()==null?0:msgRecord.getFailedCount());
		}else {
			resultMap.put("totalNum", 0);
			resultMap.put("successNum", 0);
			resultMap.put("actualNum", 0);
			resultMap.put("repeatNum", 0);
			resultMap.put("wrongNum", 0);
			resultMap.put("blackNum", 0);
			resultMap.put("filterRepeat", 0);
			resultMap.put("failedNum", 0);
		}
		return resultMap.toJson();
	}
	
	/**
	 * deleteMsgRecord(删除单条群发记录)
	 * @Title: deleteMsgRecord 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@ResponseBody
	@RequestMapping("/deleteRecord")
	public String deleteMsgRecord(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		SmsRecordVO vo = null;
		try {
			vo = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if(userInfo == null || vo == null || vo.getMsgId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		Boolean isSuccess = msgRecordService.updateMsgIsShow(userInfo.getId(), vo.getMsgId(), false);
		if(isSuccess){
			UserOperationLog userLog = new UserOperationLog();
			userLog.setUserId(userInfo.getTaobaoUserNick());
			userLog.setUid(userInfo.getId());
			userLog.setFunctions("MSG_ID:" + vo.getMsgId());
			userLog.setType("删除发送总记录");
			userLog.setDate(new Date());
			userLog.setState("1");
			userLog.setIpAdd(RequestUtil.getRequestorIpAddress(request));
			userLog.setRemark("删除短信总记录,ID为：" + vo.getMsgId());
			try {
				userOperationLogService.insert(userLog);
				logger.info("删除单挑发送记录保存日志成功：{}", "SAVE_SUCCESS");
			} catch (Exception e) {
				logger.info("删除单挑发送记录保存日志失败：{}", e.getMessage());
				e.printStackTrace();
			}
			return rsMap(100, "删除成功").put("status", true).toJson();
		}
		return rsMap(101, "删除失败!").put("status", false).toJson();
	}
	
	
	/**
	 * deleteScheduleMsg(取消定时)
	 * @Title: deleteScheduleMsg 
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/cancelScheduleSend")
	@ResponseBody
	public String deleteScheduleMsg(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		SmsRecordVO vo = null;
		try {
			vo = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if(userInfo == null || vo == null || vo.getMsgId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
//		Boolean flag = false;
//		if(vo.getMethod() != null && !"".equals(vo.getMethod())){
//			if("0".equals(vo.getMethod() == null)){
		MsgSendRecord msgRecord = msgRecordService.queryRecordById(userInfo.getId(), vo.getMsgId());
		if((msgRecord.getSendCreat().getTime() - System.currentTimeMillis()) < 180000){
			return rsMap(101, "取消失败！定时发送时间与当前时间相差小于3分钟!").put("status", false).toJson();
		}
		Boolean flag = msgRecordService.updateMsgIsShow(userInfo.getId(), vo.getMsgId(), false);
//			}else if("1".equals(vo.getMethod() == null)){
//		Boolean flag = msgRecordService.updateMsgIsShow(userInfo.getId(), vo.getMsgId(), true);
//			}
//		}
		if(flag == true){
			if((msgRecord.getSendCreat().getTime() - System.currentTimeMillis()) < 180000){
				msgRecordService.updateMsgIsShow(userInfo.getId(), vo.getMsgId(), true);
				return rsMap(101, "取消失败！定时发送时间与当前时间相差小于3分钟!").put("status", false).toJson();
			}
			Boolean scheduleStatus = sendScheduleService.deleteScheduleByMsgId(vo.getMsgId());
			UserOperationLog userLog = new UserOperationLog();
			userLog.setUserId(userInfo.getTaobaoUserNick());
			userLog.setUid(userInfo.getId());
			userLog.setFunctions("MSG_ID:" + vo.getMsgId());
			userLog.setType("取消定时记录");
			userLog.setDate(new Date());
			userLog.setRemark("取消定时总记录,ID为：" + vo.getMsgId());
			userLog.setIpAdd(RequestUtil.getRequestorIpAddress(request));
			if(scheduleStatus){
				userLog.setState("1");
				try {
					userOperationLogService.insert(userLog);
					logger.info("取消定时发送保存日志成功：{}", "SAVE_SUCCESS");
				} catch (Exception e) {
					logger.info("取消定时发送保存日志失败：{}", e.getMessage());
					e.printStackTrace();
				}
				return rsMap(100, "设置成功").put("status", true).toJson();
			}else {
				userLog.setState("2");
				try {
					userOperationLogService.insert(userLog);
					logger.info("取消定时发送保存日志成功：{}", "SAVE_SUCCESS");
				} catch (Exception e) {
					logger.info("取消定时发送保存日志失败：{}", e.getMessage());
					e.printStackTrace();
				}
				return rsMap(101, "设置异常！请重新操作").put("status", false).toJson();
			}
		}else {
			return rsMap(101, "设置异常！请重新操作").put("status", false).toJson();
		}
		
	}
	
	/**
	 * 修改定时时间
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 */
	@RequestMapping("/updateScheduleSend")
	@ResponseBody
	public String updateScheduleSend(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		SmsRecordVO vo = null;
		SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			vo = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("对象转换异常");
			return failureReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		if(vo.getbTime()!=null&&vo.getbTime()!=""){
			try {
				vo.setBeginTime(simpleDateFormat.parse(vo.getbTime()));
			} catch (ParseException e) {
				e.printStackTrace();
				logger.info("开始时间解析错误");
				return failureReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		MsgSendRecord msgRecord = msgRecordService.queryRecordById(userInfo.getId(), vo.getMsgId());
		if((msgRecord.getSendCreat().getTime() - System.currentTimeMillis()) < 180000){
			return failureReusltMap(ApiResult.CACLE_FAILED).toJson();
		}
        if(vo.getBeginTime()!=null){
    		if((vo.getBeginTime().getTime() - System.currentTimeMillis()) < 180000){
    			return failureReusltMap(ApiResult.CACLE_FAILED).toJson();
    		}
        	try {
        		//修改总记录表中的时间
        		msgRecordService.updateMsgBeginSendCreat(userInfo.getUid(),vo);
        		//修改定时表中的时间
        		smsSendInfoScheduleService.updateSmsSendScheduleStrartTime(vo);
			} catch (Exception e) {
				e.printStackTrace();
				logger.info("定时修改异常");
				return	failureReusltMap(ApiResult.SAVE_FAILURE).toJson();
			}
        }
		return successReusltMap(ApiResult.SAVE_SUCCESS).toJson();
	}
	
}
