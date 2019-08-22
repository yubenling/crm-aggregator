package com.kycrm.member.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.message.BatchSmsData;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.message.SmsSendInfo;
import com.kycrm.member.domain.entity.message.SmsSendListImport;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.AppointNumberSendVO;
import com.kycrm.member.domain.vo.message.SmsSendListImportVO;
import com.kycrm.member.service.message.IMsgSendRecordService;
import com.kycrm.member.service.message.IMultithreadBatchSmsService;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.message.ISmsSendInfoScheduleService;
import com.kycrm.member.service.message.ISmsSendListImportService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.service.vip.IVipUserService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.ExcelImportUtil;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.IpAddressUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MobileRegEx;
import com.kycrm.util.MsgType;
import com.kycrm.util.SmsCalculateUtil;
import com.kycrm.util.TxtImportUtil;
import com.kycrm.util.thread.MyFixedThreadPool;

@Controller
@RequestMapping(value = "marketing")
public class AppointNumberSendController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(AppointNumberSendController.class);
	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private SessionProvider sessionProvider;
	@Autowired
	private IUserAccountService userAccountService;
	@Autowired
	private ISmsSendListImportService smsSendListImportService;
	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;
	@Autowired
	private IMsgSendRecordService msgSendRecordService;
	@Autowired
	private ISmsBlackListDTOService smsBlackListDTOService;
	@Autowired
	private IVipUserService vipUserService;
	@Autowired
	private ISmsSendInfoScheduleService smsSendInfoScheduleService;
	@Autowired
	private IMultithreadBatchSmsService multithreadBatchSmsService;
	
	//导入文件格式
	private final String IS_TXT = "txt";
	private final String IS_XLSX = "xlsx";
	private final String IS_XLS = "xls";
	/**
	 * 指定号码群发，批量发送短信
	 * @author HL
	 * @time 2018年2月8日 下午2:58:35 
	 * @param params
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sendSms",method = RequestMethod.POST)
	public String sendSms(@RequestBody String params,
			HttpServletRequest request, HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		AppointNumberSendVO vo = new AppointNumberSendVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, AppointNumberSendVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("=================指定号码群发====================json异常：" + e.getMessage());
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		vo.setUid(user.getId());
		
		
		if(vo.getSendTimeType()==2){
			if(null != vo.getSendTime()){
				long millis = System.currentTimeMillis();
				millis +=30*60*1000;
				Date SystemDate = new Date(millis);
				if(vo.getSendTime().before(SystemDate)){
					return failureReusltMap(ApiResult.TIMING_TIME_GT30MINUTE).toJson();
				}
			}else{
				return failureReusltMap(ApiResult.TIMING_TIME_ISNULL).toJson();
			}
		}
		
		// 发送方式 1--手动输入号码,2---批量上传号码
		if(vo.getSendMode()==2){
				//通过id查询上传号码，
				List<String> sendPhones = smsSendListImportService.findImportPhoneByteById(vo.getPhonesId());
				vo.setPhones(sendPhones);
		}else{
			List<String> phones = checkoutPhones(vo.getPhones());
			vo.setPhones(phones);
		}

		
		//判断电话号码
		if(null == vo.getPhones() || vo.getPhones().size() == 0){
			return failureReusltMap(ApiResult.MOBILE_IS_NULL).toJson();
		}

		//判断短信
		if(null == vo.getContent() || "".equals(vo.getContent())){
			return failureReusltMap(ApiResult.SMS_CONTENT_NULL).toJson();
		}
	
		//判断签名
		if(null == vo.getAutograph() || "".equals(vo.getAutograph())){
			return failureReusltMap(ApiResult.SMS_AUTOGRAPH_NULL).toJson();
		}
	
		if (vo.getSendTimeType()==1 || vo.getSendTimeType()==2) {
		}else{
			return failureReusltMap(ApiResult.SEND_SMS_TYPEERROR).toJson();
		}
		
		//获取单条短信发送条数
		Integer deductionNum = SmsCalculateUtil.getActualDeduction(vo.getContent());
		// 查询用户的短信条数
		long smsNum = userAccountService.findUserAccountSms(user.getId());
		//判断当前短信条数是否大于发送条数
		if(smsNum <= vo.getPhones().size()*deductionNum){
			return failureReusltMap(ApiResult.SMS_NOT_ENOUGH).toJson();
		}
		
		if(null != vo.getPhones() && vo.getPhones().size()>0){
			try {
				
				// 保存总记录数据
				Long msgRecordId = saveMsgSendRecord(user, vo.getPhones().size(),0, 
						0, 0,0,vo);
				
				//异步执行短信发送或定时
				String ipAddress = IpAddressUtil.getIpAddress(request);
				this.executeSmsSend(vo,user,msgRecordId,deductionNum,ipAddress);
				
				if(vo.getSendTimeType()==2){
					return successReusltMap(ApiResult.SMS_TIMING_CENTRE).toJson();
				}else{
					return successReusltMap(ApiResult.SMS_SEND_CENTRE).toJson();
				}
			} catch (Exception e) {
				logger.error("=================指定号码群发====================短信发送异常" + e.getMessage());
				if (vo.getSendTimeType()==1) {
					//短信发送异常
					return failureReusltMap(ApiResult.SEND_SMS_FAILURE).toJson();
				} else {
					//短信定时异常
					return failureReusltMap(ApiResult.TIMING_SMS_FAILURE).toJson();
				}
			}
		}else{
			return failureReusltMap(ApiResult.MOBILE_BLACK_SHIELD).toJson();
		}
	}

	/**
	 * 指定号码群发-电话号码上传
	 * @author HL
	 * @time 2018年2月27日 下午5:32:54 
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/uploadPhones", method = RequestMethod.POST)
	public String uploadPhones(@RequestParam MultipartFile file,
			HttpServletRequest request, HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		try {
			//手机号码
			List<String> list = new ArrayList<String>();
			
			//文件名称
			String filename = file.getOriginalFilename();
			
			if(filename.contains(IS_TXT)){
				list = TxtImportUtil.gainTxtData(file.getInputStream());
			}else if(filename.contains(IS_XLSX) || filename.contains(IS_XLS)) {
				// 读取整个excel文档
				List<List<String>> dataLists = ExcelImportUtil.gainExcelData(file);
				for (List<String> data : dataLists) {
					if(null == data.get(0) || "".equals(data.get(0)))
						continue;
					
					list.add(data.get(0).trim());
				}
			}else{
				return failureReusltMap(ApiResult.IMPORT_FILE_FORMATERROR).toJson();
			}
			
			// 判断文件是否过大
			if (list.size() > 150000)
				return failureReusltMap(ApiResult.IMPORT_FILE_OVERSIZE).toJson();

			if (list != null && list.size() > 0) {
				try {
					if (!MobileRegEx.validateMobile(list.get(0))) {
						list.remove(0);// 删除第一行数据为"手机号"的
					}
				} catch (Exception e) {
					list.remove(0);// 删除第一行数据为"手机号"的
				}
			}
			
			if (null == list || list.size() == 0)
				return failureReusltMap(ApiResult.CONTENT_ERROR_ISNULL).toJson();
				
			
			//创建上传记录
			SmsSendListImport sendList = new SmsSendListImport();
			// 封装数据
			sendList.setUid(user.getId());
			sendList.setFileName(filename);// 上传的文件名称
			sendList.setState(2);// 0--上传完成 1--上传失败 2--上传中
			sendList.setSendNumber(list.size());// 上传的总电话条数
			sendList.setSuccessNumber(0);// 上传成功的电话条数
			sendList.setErrorNumber(0);// 上传失败的电话条数
			sendList.setRepetitionNumber(0);// 上传重复的电话条数
			sendList.setCreatedBy(user.getTaobaoUserNick());
			sendList.setLastModifiedBy(user.getTaobaoUserNick());
			
			// 创建上传记录
			Long importId = smsSendListImportService.saveSmsSendListImport(sendList);
			
			// 开启线程上传数据
			this.importPhoneThread(list, importId, user);

			return successReusltMap(ApiResult.IMPORT_DISPOSE).toJson();
		} catch (Exception e) {
			logger.info("===========指定号码发送===========:"+user.getTaobaoUserNick()+"==导入手机号异常!!!=="+e.getMessage());
		}
		return failureReusltMap(ApiResult.IMPORT_FAILURE).toJson();
	}
	
	/**
	 * 查询到导入的发送电话号码记录
	 * @author HL
	 * @time 2018年3月5日 下午3:44:41 
	 * @return
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/findSmsSendList")
	public String findSmsSendList(@RequestBody String params,
			HttpServletResponse response, HttpServletRequest request){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		SmsSendListImportVO vo = new SmsSendListImportVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, SmsSendListImportVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		vo.setUid(user.getId());
		List<SmsSendListImport> list = smsSendListImportService.findSmsSendLists(vo);
		return successReusltMap(null).put(ApiResult.API_RESULT, list).toJson();
	}
	
	/**
	 * 删除导入的数据
	 * @throws IOException
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteSmsSendlist")
	public String deleteSmsSendlist(@RequestBody String params,
			HttpServletResponse response, HttpServletRequest request){
		SmsSendListImportVO vo = new SmsSendListImportVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, SmsSendListImportVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		boolean result = smsSendListImportService.deleteSmsSendListById(vo.getRecordId());
		if(result){
			return successReusltMap(ApiResult.DEL_SUCCESS).toJson();
		}
		return failureReusltMap(ApiResult.DEL_FAILURE).toJson();

	}
	
	
	/*
	 * 验证是不是号码
	 */
	private List<String> checkoutPhones(List<String> phones) {
		List<String> newPhones = new ArrayList<String>();// 重新封装电话号码
		for (String p : phones) {
			String phone = p.trim();
			if (MobileRegEx.validateMobile(phone)) {
				newPhones.add(phone);
			}
		}
		return newPhones;
	}
		
	/*
	 * 异步执行短信发送或定时
	 */
	private void executeSmsSend(AppointNumberSendVO vo, UserInfo user,
			Long msgRecordId, Integer deductionNum, String ipAddress) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {
				Map<String, Collection<String>> removePhone = disposePhones(vo.getPhones());
				Set<String> correctPhone =  (Set<String>) removePhone.get("phoneSet");// 正确的手机号码
				List<String> wrongPhone = (List<String>) removePhone.get("wrongNums");// 错误的手机号码
				List<String> repeatPhone = (List<String>) removePhone.get("repeatNums");// 重复的手机号码
				List<String> blackPhone = filtrateBlackPhone(correctPhone,user);// 黑名单的电话号码
				List<String> shieldPhone = filtrateShieldPhones(vo.getSendDay(),correctPhone,user);// 屏蔽的电话号码
				
				//更新总记录
				MsgSendRecord msg = new MsgSendRecord();
				msg.setId(msgRecordId);
				msg.setWrongCount(wrongPhone.size());
				msg.setRepeatCount(repeatPhone.size());
				msg.setBlackCount(blackPhone.size());
				msg.setSheildCount(shieldPhone.size());
				msgSendRecordService.updateMsgRecordByMsgId(msg);
				
				if(correctPhone != null && correctPhone.size() > 0){
					String[] phonesArr = setTurnArray(correctPhone);
					// 立即发送:调用短信接口发送数据
					if (vo.getSendTimeType()==1) {
						threadSendSms(phonesArr, user, vo, msgRecordId,ipAddress);
					} else if (vo.getSendTimeType()==2) {
						threadTimingSms(phonesArr, user, vo, msgRecordId, deductionNum);
					}
				}else{
					msg = new MsgSendRecord();
					msg.setId(msgRecordId);
					msg.setIsSent(Boolean.TRUE);
					msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
					msgSendRecordService.updateMsgRecordByMsgId(msg);
				}
				
				//将错误手机号码进行保存
				threadSaveErrorPhone(user, msgRecordId,
						wrongPhone, repeatPhone, blackPhone, shieldPhone, vo);
				
			}
		});
	}
	
	/**
	 * 立即发送短信
	 * @author HL
	 * @time 2018年8月4日 下午1:00:45 
	 * @param vo
	 */
	public void threadSendSms(String[] phones, UserInfo user,
			AppointNumberSendVO vo, Long msgRecordId, String ipAddress) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {
				
				/*查询当前用户是否为vip*/
				boolean isVip = vipUserService.findVipUserIfExist(user.getId());

				// 封装数据
				BatchSmsData batchSmsData = new BatchSmsData(phones);
				batchSmsData.setUid(user.getId());
				batchSmsData.setUserId(user.getTaobaoUserNick());
				batchSmsData.setChannel(vo.getAutograph());
				batchSmsData.setAutograph(vo.getAutograph());
				batchSmsData.setContent(vo.getContent());
				batchSmsData.setType(MsgType.MSG_ZDHMQF);
				batchSmsData.setVip(isVip);
				batchSmsData.setIpAdd(ipAddress);
				batchSmsData.setMsgId(msgRecordId);

				//调用发送
				Map<String, Integer> map = multithreadBatchSmsService.batchOperateSms(batchSmsData);
				
				int success = map.get("succeedNum");
				int fail = map.get("failedNum");
				
				//更新总记录
				MsgSendRecord msg = new MsgSendRecord();
				msg.setId(msgRecordId);
				msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
				msg.setSucceedCount(success);
				msg.setFailedCount(fail);
				msgSendRecordService.updateMsgRecordByMsgId(msg);
			}
		});
	}
	
	
	/**
	 * 定时保存短信
	 * @author HL
	 * @time 2018年8月4日 下午1:00:45 
	 * @param vo
	 */
	public void threadTimingSms(String[] phones, UserInfo user,
			AppointNumberSendVO vo, Long msgRecordId, Integer deductionNum) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {
				// 定时发送:保存到数据库,以10万长度为节点 拆封保存定时
				List<String> asList = Arrays.asList(phones);
		    	int dataSize = asList.size();
		    	int start = 0,end =0,node = 100000;
		    	List<String> subList = null;
		    	if(dataSize/node==0){
		    		end = 1;
		    	}else if(dataSize%node==0){
		    		end = dataSize/node;
		    	}else{
		    		end = (dataSize+node)/node;
		    	}
		    	while (start<end) {
		    		if(start==(end-1)){
		    			subList = asList.subList(start*node, dataSize);
		    		}else{
		    			subList = asList.subList(start*node, (start+1)*node);
		    		}
		    		start++;
		    		
		    		//加密电话号码
	    			List<String> encryptListPhone = encryptListPhone(subList, user);
	    			if(null != encryptListPhone && encryptListPhone.size()>0){
	    				insertScheduleSend(encryptListPhone,user.getId(),user.getTaobaoUserNick(),deductionNum,msgRecordId,vo);
	    			}else{
	    				insertScheduleSend(subList,user.getId(),user.getTaobaoUserNick(),deductionNum,msgRecordId,vo);
	    			}
		    	}
		    	//更新总记录
				MsgSendRecord msg = new MsgSendRecord();
				msg.setId(msgRecordId);
				msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
				msgSendRecordService.updateMsgRecordByMsgId(msg);
			}
		});
	}

	/**
	 * 封装总记录并保存
	 * @author HL
	 * @time 2018年2月26日 下午2:23:54 
	 * @return
	 */
	private Long saveMsgSendRecord(UserInfo user, Integer totalCount,
			int wrongPhone, int repeatPhone, int blackPhone, int shieldPhone,
			AppointNumberSendVO vo) {
		// 保存总记录返回id
		MsgSendRecord msg = new MsgSendRecord();
		msg.setUid(user.getId());
		msg.setUserId(user.getTaobaoUserNick());
		msg.setTotalCount(totalCount);
		msg.setSucceedCount(0);
		msg.setFailedCount(0);
		msg.setWrongCount(wrongPhone);
		msg.setRepeatCount(repeatPhone);
		msg.setBlackCount(blackPhone);
		msg.setSheildCount(shieldPhone);
		msg.setTemplateContent(vo.getContent());
		msg.setActivityName(vo.getActivityName());
		msg.setMarketingType("1");
		msg.setType(MsgType.MSG_ZDHMQF);
		msg.setIsShow(Boolean.TRUE);
		msg.setStatus(MsgType.MSG_STATUS_SENDING);
		msg.setTaoBaoShortLinkId(vo.getTaoBaoShortLinkId());
		msg.setShortLinkType(vo.getShortLinkType());
		msg.setIsSent(vo.getSendTimeType()==1 ? Boolean.TRUE : Boolean.FALSE);
		msg.setSendCreat(vo.getSendTimeType()==1 ? new Date() : vo
				.getSendTime());
		msg.setCreatedBy(user.getTaobaoUserNick());
		msg.setLastModifiedBy(user.getTaobaoUserNick());
		Long msgRecordId = msgSendRecordService.saveMsgSendRecord(msg);
		return msgRecordId;
	}

	
	/**
	 * 批量插入定时数据
	 * @author Administrator_HL
	 * @data 2017年6月12日 下午2:40:23
	 */
	private void insertScheduleSend(List<String> phoneList, Long uid,
			String taobaoUserNick, Integer deductionNum,
			Long msgRecordId, AppointNumberSendVO vo){
		String phones = StringUtils.join(phoneList.toArray(), ",");
		// 创建SmsSendInfo封装数据调用短信发送方法
		SmsSendInfo smsSendInfo = new SmsSendInfo();
		smsSendInfo.setUid(uid);
		smsSendInfo.setPhone(phones);// 接收人手机号
		smsSendInfo.setUserId(taobaoUserNick);// 用户ID
		smsSendInfo.setType(MsgType.MSG_ZDHMQF);// 短信类型
		smsSendInfo.setActualDeduction(deductionNum);// 实际扣除短信条数
		smsSendInfo.setContent(vo.getContent());// 短信内容
		smsSendInfo.setStartSend(vo.getSendTime());// 短信定时开始时间
		smsSendInfo.setEndSend(null);// 短信定时结束小时
		smsSendInfo.setChannel(vo.getAutograph());// 短信渠道
		smsSendInfo.setCreatedDate(new Date());// 创建时间
		smsSendInfo.setMsgId(msgRecordId);// 总记录id
		smsSendInfo.setShieldDay(vo.getSendDay());
		smsSendInfoScheduleService.doAutoCreate(smsSendInfo);
	}
	
	
	
	/**
	 * 去除重复/错误手机号 
	 * @author HL
	 * @time 2018年8月3日 下午2:39:07 
	 * @param phones
	 * @return
	 */
	private Map<String, Collection<String>> disposePhones(List<String> phones) {
		Map<String, Collection<String>> mapPhones = new HashMap<String, Collection<String>>();

		// 将数据循环放入Set中,去重
		Set<String> phoneSet = new HashSet<String>();// 正确的手机号码
		List<String> wrongNums = new ArrayList<String>();// 错误的手机号码
		List<String> repeatNums = new ArrayList<String>();// 重复的手机号码
		List<String> newPhones = new ArrayList<String>();// 重新封装电话号码
		
		if (null != phones && phones.size()>0) {
			for (String p : phones) {
				String phone = p.trim();
				if(null == phone || "".equals(phone))
					continue;
				
				if (MobileRegEx.validateMobile(phone)) {
					phoneSet.add(phone);
				} else {
					wrongNums.add(phone);
				}
				
				newPhones.add(phone);
			}

			// 将phones赋值给repeatNums
			repeatNums.addAll(newPhones);
			// 移除正确的号码和错误的号码 repeatNums内都是剩余重复的的手机号
			for (String string : phoneSet) {
				repeatNums.remove(string);
			}
			for (String string : wrongNums) {
				repeatNums.remove(string);
			}

		}
		mapPhones.put("phoneSet", phoneSet);
		mapPhones.put("wrongNums", wrongNums);
		mapPhones.put("repeatNums", repeatNums);
		return mapPhones;
	}
	
	/**
	 * 处理黑名单号码
	 * @author HL
	 * @time 2018年8月3日 下午2:38:03 
	 * @param correctPhone
	 * @param user
	 * @return
	 */
	private List<String> filtrateBlackPhone(Set<String> correctPhone,
			UserInfo user) {
		List<String> blackPhones = new ArrayList<String>();
		if (correctPhone != null && correctPhone.size() > 0) {
			blackPhones.addAll(correctPhone);
			List<String> list = smsBlackListDTOService.findBlackPhones(user.getId(),user);
			if(null != list && list.size()>0){
				correctPhone.removeAll(list);
			}
			blackPhones.removeAll(correctPhone);
		}
		return blackPhones;
	}
	
	
	/**
	 * 通过屏蔽后设置的天数 获取已经发送过的用户信息
	 */
	private List<String> filtrateShieldPhones(Integer sendDay,
			Set<String> correctPhone, UserInfo user) {
		List<String> shieldPhone = new ArrayList<String>();// 屏蔽的电话号码
		if (null != sendDay && sendDay > 0) {
			if (null != correctPhone && correctPhone.size() > 0) {
				
				List<String> newPhone = new ArrayList<String>(correctPhone);// Set转List
				int dataSize = newPhone.size();//总条数
				int start = 0,//开始次数
					end =0,//结束次数
					node = 10000;//每次处理多少条
				if(dataSize%node==0){
					end=dataSize/node;
				}else{
					end=(dataSize+node)/node;
				}

				while (start<end) {
					List<String> subList = new ArrayList<String>();
					if(start==(end-1)){
						subList = newPhone.subList(start*node, dataSize);
					}else{
						subList = newPhone.subList(start*node, (start+1)*node);
					}
					start++;
					try {
						//加密号码
						List<String> phone = encryptListPhone(subList, user);
						if(null != phone && phone.size()>0){
							//获取最近发送号码
							List<String> list = smsRecordDTOService.findSmsRecordDTOShieldDay(user.getId(), sendDay,user,phone);
							shieldPhone.addAll(list);
						}else{
							logger.error("*****指定号码发送*****屏蔽天数*****加密失败无法屏蔽*****");
						}
						
					} catch (Exception e) {
						logger.error("*****指定号码发送*****屏蔽天数*****异常*****"+e.getMessage());
						continue;
					}
				}
				
				correctPhone.removeAll(shieldPhone);//删除最近发送号码
			}
		}
		return shieldPhone;
	}
	
	/**
	 * set转string数组
	 * @author HL
	 * @time 2018年6月13日 下午4:32:00 
	 * @param correctPhone
	 * @return
	 */
	private String[] setTurnArray(Set<String> correctPhone) {
		String str = correctPhone.toString().replace("[", "").replace("]", "").replace(" ", "");
		String[] array = str.trim().split(",");
		return array;
	}
	
	
	/**
	 * 加密电话号码！！！
	 */
	private List<String> encryptListPhone(List<String> phones,UserInfo user) {
			List<String> list = new ArrayList<String>();
			if(null!= phones && phones.size()>0){
				String newToken = user.getAccessToken();
				for (String phone : phones) {
					try {
						if(!EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)){
							String encryptData = EncrptAndDecryptClient.getInstance().encryptData(phone, EncrptAndDecryptClient.PHONE,newToken);
							list.add(encryptData);
						}else{
							list.add(phone);
						}
					} catch (Exception e) {
						try {
							newToken = userInfoService.findUserTokenById(user.getId());
							if(!EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)){
								String encryptData = EncrptAndDecryptClient.getInstance().encryptData(phone, EncrptAndDecryptClient.PHONE,newToken);
								list.add(encryptData);
							}else{
								list.add(phone);
							}
						} catch (Exception e1) {
							logger.error("=========指定号码群发======="+user.getTaobaoUserNick()+"=========加密电话号码失败"+e1.getMessage());
							return list;
						}
					}
				}
			}
			return list;
		}

	/**
	 * 指定号码群发将错误的手机号码保存
	 */
	public void threadSaveErrorPhone(UserInfo user, Long msgRecordId, List<String> wrongPhone,
			List<String> repeatPhone, List<String> blackPhone, List<String> shieldPhone, AppointNumberSendVO vo) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {
				SmsRecordDTO sms = new SmsRecordDTO();
				sms.setUid(user.getId());
				sms.setUserId(user.getTaobaoUserNick());
				sms.setActivityName(vo.getActivityName());
				sms.setContent(vo.getContent());
				sms.setType(MsgType.MSG_ZDHMQF);
				sms.setActualDeduction(0);
				sms.setAutograph(vo.getAutograph());
				sms.setChannel(vo.getAutograph());
				sms.setSendTime(new Date());
				sms.setMsgId(msgRecordId);
				sms.setSource("2");
				sms.setShow(true);

				// 错误的手机号码
				if (wrongPhone != null && wrongPhone.size() > 0) {
					sms.setStatus(3);
					for (String phone : wrongPhone) {
						String encryptPhone = encryptPhone(phone, user);
						sms.setRecNum(encryptPhone);
						smsRecordDTOService.saveSmsRecordBySingle(user.getId(), sms);
					}
				}
				// 重复的手机号码
				if (repeatPhone != null && repeatPhone.size() > 0) {
					sms.setStatus(4);
					for (String phone : repeatPhone) {
						String encryptPhone = encryptPhone(phone, user);
						sms.setRecNum(encryptPhone);
						smsRecordDTOService.saveSmsRecordBySingle(user.getId(), sms);
					}
				}
				// 黑名单的电话号码
				if (blackPhone != null && blackPhone.size() > 0) {
					sms.setStatus(5);
					for (String phone : blackPhone) {
						String encryptPhone = encryptPhone(phone, user);
						sms.setRecNum(encryptPhone);
						smsRecordDTOService.saveSmsRecordBySingle(user.getId(), sms);
					}
				}
				// 屏蔽的电话号码
				if (shieldPhone != null && shieldPhone.size() > 0) {
					sms.setStatus(6);
					for (String phone : shieldPhone) {
						String encryptPhone = encryptPhone(phone, user);
						sms.setRecNum(encryptPhone);
						smsRecordDTOService.saveSmsRecordBySingle(user.getId(), sms);
					}
				}
			}
		});

	}
	
	/**
	 * 单条加密手机号码
	 * @time 2018年8月3日 下午3:48:43
	 * @return
	 */
	private String encryptPhone(String phone, UserInfo user) {
		if (null != phone && !"".equals(phone)) {
			String newToken = user.getAccessToken();
			try {
				if (!EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)) {
					String encryptData = EncrptAndDecryptClient.getInstance().encryptData(phone,
							EncrptAndDecryptClient.PHONE, newToken);
					return encryptData;
				} else {
					return phone;
				}
			} catch (Exception e) {
				try {
					newToken = userInfoService.findUserTokenById(user.getId());
					if (!EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)) {
						String encryptData = EncrptAndDecryptClient.getInstance().encryptData(phone,
								EncrptAndDecryptClient.PHONE, newToken);
						return encryptData;
					} else {
						return phone;
					}
				} catch (Exception e1) {
					logger.error("=========指定号码群发=======" + user.getTaobaoUserNick() + "=========加密电话号码失败"
							+ e1.getMessage());
				}
			}
		}
		return null;
	}
	
	
	/**
	 * 异步处理上传号码 
	 */
	public void importPhoneThread(List<String> list, Long importId,
			UserInfo user) {
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {
				logger.info("=========指定号码群发=======导入手机号："
						+ user.getTaobaoUserNick() + "===开始处理。。。。。");
				
				// 创建修改记录对象============
				SmsSendListImport sendList = new SmsSendListImport();
				sendList.setId(importId);

				try {
					
					// 接收成功上传的电话号码============
					Set<String> phoneNums = new HashSet<String>();
					// 上传失败的电话条数
					int errorNumber = 0;

					// 定义变量循环次数
					int i = 0;
					for (String phone : list) {
						String data = phone.trim();
						if (MobileRegEx.validateMobile(data)) {
							phoneNums.add(data);
						} else {
							errorNumber++;
						}
						if (i % 1000 == 0 && i != 0) {
							sendList.setSuccessNumber(phoneNums.size());
							sendList.setErrorNumber(errorNumber);
							smsSendListImportService
									.updateSmsSendListImportById(sendList);
						}
						i++;
					}

					// 记录重复的数据
					int repetitionNum = list.size() - phoneNums.size()
							- errorNumber;

					// 将号码处理并保存
					if (null != phoneNums && phoneNums.size() > 0) {
						sendList.setState(0);
						sendList.setSuccessNumber(phoneNums.size());
						sendList.setErrorNumber(errorNumber);
						sendList.setRepetitionNumber(repetitionNum);// 上传重复的电话条数
						// 加密电话号码
						String phones = StringUtils.join(phoneNums, ",");
						try {
							byte[] compress = GzipUtil.compress(phones);
							sendList.setImportPhoneByte(compress);
						} catch (Exception e) {
							sendList.setState(1);
							sendList.setSuccessNumber(0);
							sendList.setRepetitionNumber(0);
							sendList.setErrorNumber(list.size());
							sendList.setImportPhoneByte(null);
						}
					} else {
						sendList.setState(1);
						sendList.setSuccessNumber(0);
						sendList.setRepetitionNumber(0);
						sendList.setErrorNumber(list.size());
						sendList.setImportPhoneByte(null);
					}

					smsSendListImportService
							.updateSmsSendListImportById(sendList);
					logger.info("=========指定号码群发=======导入手机号："
							+ user.getTaobaoUserNick() + "===处理完成√√√√√");
				} catch (Exception e) {
					sendList.setState(1);
					sendList.setSuccessNumber(0);
					sendList.setRepetitionNumber(0);
					sendList.setErrorNumber(list.size());
					sendList.setImportPhoneByte(null);
					smsSendListImportService.updateSmsSendListImportById(sendList);
					logger.error("=========指定号码群发=======导入手机号失败："
							+ user.getTaobaoUserNick() + "=========："
							+ e.getMessage());
				}
			}
		});
	}
	
	/**
	 * 将线上导入过的号码解密压缩保存到byte字段里--执行一次
	 * @time 2018年9月20日 上午10:54:52 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/importPhoneToByte")
	public String disposeImportPhoneToByte(Long uid){
		MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
			@Override
			public void run() {
				smsSendListImportService.disposeImportPhoneToByte(uid);
			}
		});
		return "正在压缩crm_sms_sendlist_import的电话号码";
	}
}
