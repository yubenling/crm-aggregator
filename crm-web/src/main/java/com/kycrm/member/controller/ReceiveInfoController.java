package com.kycrm.member.controller;

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.api.exception.KycrmApiException;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.member.MemberReceiveDetail;
import com.kycrm.member.domain.entity.message.BatchSmsData;
import com.kycrm.member.domain.entity.message.SmsBlackListDTO;
import com.kycrm.member.domain.entity.message.SmsReceiveInfo;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.member.MemberInfoVO;
import com.kycrm.member.domain.vo.message.SmsRecordVO;
import com.kycrm.member.domain.vo.receive.ReceiveInfoVO;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.member.IMemberReceiveDetailService;
import com.kycrm.member.service.message.IMultithreadBatchSmsService;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.message.ISmsReceiveInfoService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.member.utils.SyncJudgeRequestUtil;
import com.kycrm.util.ConstantUtils;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.GetCurrentPageUtil;
import com.kycrm.util.GetIpAddress;
import com.kycrm.util.JsonUtil;
import com.taobao.api.SecretException;

/**
 * 买家回复信息Controller
 * 
 * @ClassName: ReceiveInfoController
 * @author ztk
 * @date 2018年1月18日 下午4:02:37 *
 */
@Controller
@RequestMapping("/receiver")
public class ReceiveInfoController extends BaseController {
	
	private Logger logger = LoggerFactory.getLogger(ReceiveInfoController.class);

	@Autowired
	private ISmsReceiveInfoService receiveInfoService;

	@Autowired
	private SessionProvider sessionProvider;

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;

	@Autowired
	private IUserAccountService userAccountService;

	@Autowired
	private ISmsBlackListDTOService blackListService;

	@Autowired
	private IMultithreadBatchSmsService multithreadBatchSmsService;
	
	@Autowired
	private IMemberDTOService memberDTOSerivce;
	
	@Autowired
	private IMemberReceiveDetailService receiverDetailService;
	
	@Autowired
	private IOrderDTOService orderService;
	
	@Autowired
	private SyncJudgeRequestUtil syncJudgeRequestUtil;
	
	@Autowired
	private IUserInfoService userInfoService;
	
	
	
	
	@RequestMapping(value="/refund",method=RequestMethod.GET,produces="text/html;charset=UTF-8")
	@ResponseBody
	public String testGetSellerRefund(String name,String src,String dest,String content,
			String time,String reference) throws ParseException{
		logger.debug("请求来啦~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		logger.debug("name:" + name + "  src:" + src + "   dest:" + dest + "   content:" + content + "   time:" + time
				+ "   reference:" + reference);
		if(!"".equals(src)){
			String receive = src+content;
			boolean result = syncJudgeRequestUtil.canExecute(receive);
			//初始化加密
			EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
			try {
				if(result){
					if(reference != null && !"".equals(reference)){
						Long uid = Long.parseLong(reference);
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						SmsReceiveInfo smsReceiveInfo = new SmsReceiveInfo();
						if(content == null){
							content = "";
						}
						String encodeContent = URLEncoder.encode(content, "utf-8");
						smsReceiveInfo.setContent(encodeContent);//回复内容
						smsReceiveInfo.setRemarks(dest);//备注
						smsReceiveInfo.setCreatedDate(new Date());
						String sessionKey = userInfoService.findUserTokenById(uid);
						if(EncrptAndDecryptClient.isEncryptData(src, EncrptAndDecryptClient.PHONE)){
							smsReceiveInfo.setSendPhone(src);//发送手机号
						}else {
							smsReceiveInfo.setSendPhone(decryptClient.encryptData(src, EncrptAndDecryptClient.PHONE, sessionKey));//发送手机号
						}
						smsReceiveInfo.setReceivePhone(dest);//接受号码
						smsReceiveInfo.setStatus(0);//是否已读 0:未读 1::已读
						logger.debug("1111~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						if(time != null){
							smsReceiveInfo.setReceiveDate(dateFormat.parse(time));//回复时间
						}
						smsReceiveInfo.setLastModifiedDate(new Date());
						//通过用户号码src查询买家和卖家(SmsSendRecord)
						/*MemberInfo memberInfo = memberInfoService.findMemberInfoByPhone(src);*/
						//SmsSendRecord sendRecord = smsSendRecordService.findRecordByPhone(src);
						MemberInfoDTO memberDTO = null;
						List<MemberInfoDTO> members = null;
						if(src != null && !"".equals(src) && reference != null && !"".equals(reference)){
							if(EncrptAndDecryptClient.isEncryptData(src, EncrptAndDecryptClient.PHONE)){
								members = memberDTOSerivce.listMemberByPhone(uid, src);
							}else {
								String mobile = decryptClient.encrypt(src, EncrptAndDecryptClient.PHONE, sessionKey);
								members = memberDTOSerivce.listMemberByPhone(uid, mobile);
							}
						}else {
							return rsMap(101, "src&reference为空").put("status", false).put("data", "src&reference为空").toJson();
						}
						if(members != null && members.size()>0){
							memberDTO = anylsistMemberList(members);
						}
						logger.debug("22222~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						if(memberDTO != null){
							smsReceiveInfo.setUid(uid);
							smsReceiveInfo.setUserId(uid + "");
							if(EncrptAndDecryptClient.isEncryptData(memberDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)){
								smsReceiveInfo.setLastModifiedBy(memberDTO.getBuyerNick());
								smsReceiveInfo.setBuyerNick(memberDTO.getBuyerNick());//买家昵称
							}else {
								smsReceiveInfo.setBuyerNick(decryptClient.encryptData(memberDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH, sessionKey));//发送手机号
								smsReceiveInfo.setLastModifiedBy(decryptClient.encryptData(memberDTO.getBuyerNick(), EncrptAndDecryptClient.SEARCH, sessionKey));//发送手机号
							}
							logger.debug("333~~~~~~memberDTO不为空~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						}else {
							smsReceiveInfo.setUid(uid);
							smsReceiveInfo.setLastModifiedBy(uid+"");
							smsReceiveInfo.setUserId(uid+"");
							smsReceiveInfo.setBuyerNick("");
						}
						logger.debug("444~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
						receiveInfoService.saveReceiverInfo(smsReceiveInfo);
						logger.debug("smsReceiveInfoService 保存成功" + smsReceiveInfo.getSendPhone() + smsReceiveInfo.getContent());
						//客户回复退订回N 的短信,标记会员为黑名单
						if(encodeContent != null){
							String smsContent = encodeContent.trim().replaceAll("\\s", "").toUpperCase();
							if(smsContent.equals("N") || smsContent.equals("TD") || smsContent.equals("Ｎ")){
								logger.info("555有回复退订!~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
								// 黑名单状态 (blackStatus) 1表示是 0 表示不是
//								VipMemberService.updateMemberBlackStatus(memberDTO, 1);
//								Boolean updateResult = memberDTOSerivce.updateMemberStatus(uid, smsReceiveInfo.getBuyerNick(), "2");
//								logger.info("666更新黑名单状态为：~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~" + updateResult);
								//在黑名单列表中插入一条数据
								SmsBlackListDTO blackListDTO = new SmsBlackListDTO();
								blackListDTO.setAddSource("3");
								blackListDTO.setCreatedBy(uid+"");
								blackListDTO.setCreatedDate(new Date());
								blackListDTO.setLastModifiedBy(uid+"");
								blackListDTO.setLastModifiedDate(new Date());
								blackListDTO.setNickOrPhone(smsReceiveInfo.getSendPhone());
								blackListDTO.setPhone(smsReceiveInfo.getSendPhone());
								blackListDTO.setType("1");
								blackListDTO.setUid(uid);
								blackListService.saveBlackListDTO(uid, blackListDTO);
							}
						}
					}
				}else{
					logger.debug("消息推送重复了哦"+receive);	
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				return "error";
			}
		}
		return "success";
	}
	
	
	/** 
	* 分析会员,获取一个 会员集合中id最小的
	* @param  members
	* @return MemberDTO    返回类型 
	* @author jackstraw_yu
	* @date 2017年11月16日 下午6:05:59
	*/
	private MemberInfoDTO anylsistMemberList(List<MemberInfoDTO> members){
		MemberInfoDTO member =null;
		long timeStamp = 0,shift=0;
		if(members==null || members.isEmpty()){
			return null;
		}else if(members.size()==1){
			return members.get(0);
		}else{
			member = members.get(0);
			timeStamp = member.getId();
			for (MemberInfoDTO memberDTO : members) {
				shift = memberDTO.getId();
				if(timeStamp > shift ){
					member = memberDTO;
					timeStamp = shift;
				}
			}
		}
		return member;
	}

	/**
	 * 客户回复列表 ztk2018年1月11日下午5:03:14
	 */
	@RequestMapping("/receive/list")
	@ResponseBody
	public String listReceiveInfo(@RequestBody String params, HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		ReceiveInfoVO receiveInfoVO = null;
		try {
			receiveInfoVO = JsonUtil.paramsJsonToObject(params, ReceiveInfoVO.class);
		} catch (KycrmApiException e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if (userInfo == null || userInfo.getId() == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		Integer pageNo = receiveInfoVO.getPageNo();
		if (pageNo == null) {
			pageNo = 1;
		}
		List<SmsReceiveInfo> receiveInfoList = receiveInfoService.listReceiveInfoLimit(userInfo.getId(), receiveInfoVO,userInfo);
		Long totalSize = receiveInfoService.countReceiveInfo(userInfo.getId(), receiveInfoVO, userInfo);
		Long totalPage = GetCurrentPageUtil.getTotalPage(totalSize, ConstantUtils.PAGE_SIZE_MIDDLE.longValue());
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("data", receiveInfoList);
		resultMap.put("totalPage", totalPage);
		resultMap.put("pageNo", pageNo);
		return resultMap.toJson();
	}

	/**
	 * 点击回复时，查询会员互动记录 @Title: listSmsReceiveDetail @param @return 设定文件 @return
	 * String 返回类型 @throws
	 */
	@RequestMapping(value = "/receive/history", produces = "text/plain;charset=UTF-8")
	@ResponseBody
	public String listReceiveHistory(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		Long uid = null;
		if (userInfo != null && userInfo.getId() != null) {
			uid = userInfo.getId();
		}
		SmsRecordVO smsRecordVO = null;
		try {
			smsRecordVO = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
		} catch (Exception e1) {
			return rsMap(102, "操作失败,请重新操作或联系管理员!").put("status", false).toJson();
		}
		if (uid == null || smsRecordVO == null || smsRecordVO.getRecNum() == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		smsRecordDTOService.doCreateTableByNewUser(uid);
		List<SmsRecordDTO> smsRecordDTOs = smsRecordDTOService.listSmsRecordByPhone(uid, userInfo.getAccessToken(),
				smsRecordVO.getRecNum());
		List<ReceiveInfoVO> receiveInfoVOs = new ArrayList<>();
		if (smsRecordDTOs != null && !smsRecordDTOs.isEmpty()) {
			for (int i = 0; i < smsRecordDTOs.size(); i++) {
				SmsRecordDTO recordDTO = smsRecordDTOs.get(i);
				ReceiveInfoVO receiveInfoVo = new ReceiveInfoVO();
				receiveInfoVo.setContent(recordDTO.getContent());
				receiveInfoVo.setReceiveDate(recordDTO.getSendTime());
				receiveInfoVo.setRole("seller");
				receiveInfoVo.setSendPhone(recordDTO.getRecNum());
				receiveInfoVOs.add(receiveInfoVo);
			}
		}
		List<ReceiveInfoVO> receiveVOs = receiveInfoService.listReceiveInfoByPhone(uid, userInfo.getAccessToken(),
				smsRecordVO.getRecNum());
		receiveInfoVOs.addAll(receiveVOs);
		receiveInfoVOs.removeAll(Collections.singleton(null));
		if(receiveInfoVOs != null && !receiveInfoVOs.isEmpty()){
			try {
				Collections.sort(receiveInfoVOs, new Comparator<ReceiveInfoVO>() {
					@Override
					public int compare(ReceiveInfoVO o1, ReceiveInfoVO o2) {
						return o1.getReceiveDate().compareTo(o2.getReceiveDate());
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return rsMap(100, "操作成功").put("status", true).put("data", receiveInfoVOs).toJson();
	}

	/**
	 * queryMemberDetail(点击回复时查询会员信息以及历史交易订单) @Title:
	 * queryMemberDetail @param @param request @param @param
	 * response @param @param params @param @return 设定文件 @return String
	 * 返回类型 @throws
	 */
	@RequestMapping("/receive/detail")
	@ResponseBody
	public String queryMemberDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		MemberInfoVO memberVO = null;
		try {
			memberVO = JsonUtil.paramsJsonToObject(params, MemberInfoVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "格式化参数失败，请重新操作").put("status", false).toJson();
		}
		if(userInfo == null || memberVO == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
//		memberDTOSerivce.doCreateTableByNewUser(userInfo.getId());
		EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
		try {
			if(!EncrptAndDecryptClient.isEncryptData(memberVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH)){
				String buyerNick = decryptClient.encryptData(memberVO.getBuyerNick(), EncrptAndDecryptClient.SEARCH, userInfo.getAccessToken());
				memberVO.setBuyerNick(buyerNick);
			}
		} catch (SecretException e) {
			e.printStackTrace();
		}
		MemberInfoDTO memberInfo = memberDTOSerivce.findMemberByParam(userInfo.getId(), memberVO.getBuyerNick());
		if(memberInfo == null){
			return rsMap(101, "根据"+memberVO.getBuyerNick()+"查询的Member对象为空").put("status", false).toJson();
		}
		//最后收货地址
		MemberReceiveDetail lastDetail = receiverDetailService.findLastDetail(userInfo.getId());
		ResultMap<String,Object> resultMap = rsMap(100, "操作成功").put("status", true);
		if(memberInfo.getBuyerNick() != null && !"".equals(memberInfo.getBuyerNick())){
			String buyerNick = memberInfo.getBuyerNick();
			try {
				if(EncrptAndDecryptClient.isEncryptData(buyerNick, EncrptAndDecryptClient.SEARCH)){
					buyerNick = decryptClient.decryptData(buyerNick, EncrptAndDecryptClient.SEARCH, userInfo.getAccessToken());
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
			resultMap.put("buyerNick", buyerNick);
		}
		if(memberInfo.getMobile() != null && !"".equals(memberInfo.getMobile())){
			String mobile = memberInfo.getMobile();
			try {
				if(EncrptAndDecryptClient.isEncryptData(mobile, EncrptAndDecryptClient.PHONE)){
					mobile = decryptClient.decryptData(mobile, EncrptAndDecryptClient.PHONE, userInfo.getAccessToken());
				}
				resultMap.put("mobile", mobile);
			} catch (SecretException e) {
				e.printStackTrace();
			}
		}
		resultMap.put("receiverAddress", null);
		if(lastDetail != null){
			String receiverAddress = lastDetail.getReceiverAddress();
			try {
				if(EncrptAndDecryptClient.isEncryptData(receiverAddress, EncrptAndDecryptClient.SEARCH)){
					receiverAddress = decryptClient.decryptData(receiverAddress, EncrptAndDecryptClient.SEARCH, userInfo.getAccessToken());
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
			resultMap.put("receiverAddress", receiverAddress);
		}
		resultMap.put("tradeNum", memberInfo.getTradeNum());
		resultMap.put("gradeName", memberInfo.getGradeName());
		resultMap.put("totalPayFee", memberInfo.getTradeAmount());
		resultMap.put("lastTradeTime", memberInfo.getLastTradeTime());
		//购买历史
		List<OrderDTO> orderDTOs = orderService.listOrderByNick(userInfo.getId(), memberVO.getBuyerNick());
		resultMap.put("tradeList", orderDTOs);
		return resultMap.toJson();
	}

	/**
	 * 发送短信 ZTK2018年3月1日下午9:24:54
	 */
	@ResponseBody
	@RequestMapping(value = "/receiveSendSms", produces = "text/plain;charset=UTF-8")
	public String orderSmsSend(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (userInfo == null || userInfo.getId() == null) {
			return rsMap(102, "操作失败,请重新操作或联系管理员!").put("status", false).toJson();
		}
		ReceiveInfoVO receiveVO = null;
		try {
			receiveVO = JsonUtil.paramsJsonToObject(params, ReceiveInfoVO.class);
		} catch (Exception e1) {
			e1.printStackTrace();
			return rsMap(102, "解析params失败，请重新操作").put("status", false).toJson();
		}
		if(receiveVO == null){
			return rsMap(102, "解析params失败，请重新操作").put("status", false).toJson();
		}
		String signVal = receiveVO.getSignVal();
		String content = receiveVO.getContent();
		String[] phones = receiveVO.getPhones();
		String[] buyerNicks = receiveVO.getBuyerNicks();
		Map<String, String> map = new HashMap<String, String>();
		for(int i =0; i < phones.length; i ++){
			map.put(phones[i], buyerNicks[i]);
		}
		// 判断短信签名
		if (signVal != null && !"".equals(signVal)) {
			String signValRe = signVal.replace("【", "").replace("】", "").replace(" ", "");
			if (signValRe == null || "".equals(signValRe)) {
				return rsMap(101, "操作失败,短信签名不能为空或者空格").put("status", false).toJson();
			}
		}
		List<String> phoneList = new ArrayList<>();
		List<String> blackPhones = new ArrayList<>();
		for (String phone : phones) {
			// 初始化加密
			EncrptAndDecryptClient decryptClient = EncrptAndDecryptClient.getInstance();
			String sessionKey = userInfo.getAccessToken();
			String encrptPhone = null;
			try {
				if (EncrptAndDecryptClient.isEncryptData(phone, EncrptAndDecryptClient.PHONE)) {
					encrptPhone = phone;
				}else {
					encrptPhone = decryptClient.encryptData(phone, EncrptAndDecryptClient.PHONE, sessionKey);
				}
			} catch (SecretException e) {
				e.printStackTrace();
			}
			boolean isBlack = blackListService.phoneIsBlack(userInfo.getId(), phone);
			if (isBlack) {
				blackPhones.add(encrptPhone);
				map.remove(phone);
			}else{
				phoneList.add(phone + Constants.SMSSEPARATOR + map.get(phone));
			}
		}
		if(phoneList == null || phoneList.isEmpty()){
			return rsMap(101, "操作失败,该手机号或买家已加入黑名单中").put("status", false).toJson();
		}
		// 计算短信长度
		int contentCount = content.length();
		if (contentCount <= 70) {
			contentCount = 1;
		} else {
			contentCount = (contentCount + 66) / 67;
		}
		// 判断短信数量
		Long smsNum = userAccountService.findUserAccountSms(userInfo.getId());
		if ((contentCount * phoneList.size()) > smsNum) {
			return rsMap(101, "操作失败,短信数量不足请充值").put("status", false).toJson();
		}
		try {
//			for (String phone : blackPhones) {
//				memberDTOSerivce.
//			}
			
			BatchSmsData batchSmsData = new BatchSmsData(phoneList.toArray(new String[phoneList.size()]));
			batchSmsData.setIpAdd(GetIpAddress.getIpAddress(request));
			batchSmsData.setUserId(userInfo.getTaobaoUserNick());
			batchSmsData.setUid(userInfo.getId());
			batchSmsData.setActualDeduction(contentCount);
			batchSmsData.setAutograph(signVal);
			batchSmsData.setChannel(signVal);
			batchSmsData.setType("36");// 会员互动
			batchSmsData.setContent(content);
			multithreadBatchSmsService.batchOperateSms(batchSmsData);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(102, "系统错误，请重新发送或联系管理员！").put("status", false).toJson();
		}
		return rsMap(100, "操作成功").put("status", true).toJson();
	}

	

	/**
	 * 卖家回复内容查询 ztk2018年1月11日下午5:03:14
	 */
	@RequestMapping("/sellerSend/list")
	@ResponseBody
	public String listSellerRevert(@RequestBody String params, HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (userInfo == null || userInfo.getId() == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		SmsRecordVO smsRecordVO = null;
		try {
			smsRecordVO = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
		} catch (KycrmApiException e) {
			e.printStackTrace();
			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
		}
		if (smsRecordVO == null || userInfo == null || userInfo.getId() == null) {
			return rsMap(101, "操作失败").put("status", false).toJson();
		}
		Date beginTime = null;
		Date endTime = null;
		if(smsRecordVO.getbTime() != null && !"".equals(smsRecordVO.getbTime())){
			beginTime = DateUtils.convertDate(smsRecordVO.getbTime());
		}
		if(smsRecordVO.geteTime() != null && !"".equals(smsRecordVO.geteTime())){
			endTime = DateUtils.convertDate(smsRecordVO.geteTime());
		}
		smsRecordVO.setBeginTime(beginTime);
		smsRecordVO.setEndTime(endTime);
		List<SmsRecordDTO> sellerRevertList = smsRecordDTOService.listSellerRevert(userInfo.getId(), smsRecordVO,
				userInfo.getAccessToken());
		Integer totalSize = smsRecordDTOService.countSellerRevert(userInfo.getId(),smsRecordVO, userInfo.getAccessToken());
		long totalPage = GetCurrentPageUtil.getTotalPage(totalSize, ConstantUtils.PAGE_SIZE_MIDDLE);
		ResultMap<String, Object> resultMap = rsMap(100, "操作成功").put("status", true);
		resultMap.put("data", sellerRevertList);
		resultMap.put("totalPage", totalPage);
		resultMap.put("pageNo", smsRecordVO.getPageNo());
		return resultMap.toJson();
	}

	

	/**
	 * @Author ZhengXiaoChen
	 * @Description 删除买家回复记录
	 * @Date 2018年7月31日下午4:53:45
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/receive/deleteRecord")
	@ResponseBody
	public String deleteRecord(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws Exception {
		ReceiveInfoVO receiveInfoVO = null;
		if (params != null && !"".equals(params)) {
			receiveInfoVO = JsonUtil.paramsJsonToObject(params, ReceiveInfoVO.class);
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || receiveInfoVO == null || receiveInfoVO.getId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		Boolean isDelete = this.receiveInfoService.deleteRecord(userInfo.getId(), receiveInfoVO.getId());
		if (isDelete) {
			return rsMap(100, "删除数据成功!").put("status", true).toJson();
		} else {
			return rsMap(101, "删除数据失败!").put("status", false).toJson();
		}
	}


	/**
	 * deleteSellerRecord(删除卖家回复记录)
	 * @Title: deleteSellerRecord 
	 * @param @param request
	 * @param @param response
	 * @param @param params
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping("/rmSellRecord")
	@ResponseBody
	public String deleteSellerRecord(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || userInfo.getId() == null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		SmsRecordVO recordVO = null;
		try {
			recordVO = JsonUtil.paramsJsonToObject(params, SmsRecordVO.class);
		} catch (Exception e) {
			e.printStackTrace();
			return rsMap(101, "格式化参数失败").put("status", false).toJson();
		}
		if(recordVO == null || recordVO.getId() == null){
			return rsMap(101, "参数为空，请重新操作").put("status", false).toJson();
		}
		
		Boolean rmStatus = smsRecordDTOService.removeRecordById(userInfo.getId(), recordVO.getId());
		if(rmStatus){
			return rsMap(100, "删除成功").put("status", true).toJson();
		}else {
			return rsMap(101, "删除失败").put("status", false).toJson();
		}
	}
	
	
	
	
	
	
	
	
	
	/*
	 * 新版没有，万一以后有了呢，先放着吧-----------------------------------------------------------------------
	 */
	/**
	 * 导出买家回复内容 @Title: exportReceiveInfo @param 设定文件 @return void 返回类型 @throws
	 */
	/*@RequestMapping("/receiveInfo/export")
	@ResponseBody
	public void exportReceiveInfo(HttpServletRequest request, HttpServletResponse response) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		ReceiveInfoVO receiveInfoVO = cacheService.get("", "", ReceiveInfoVO.class);
		List<SmsReceiveInfo> receiveInfoList = receiveInfoService.listReceiveInfoLimit(userInfo.getId(), receiveInfoVO);
		// .循环每一条数据将数据放入数组在将数组放入list
		List<Object[]> dataList = new ArrayList<Object[]>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String status = "";
		if (receiveInfoList != null && receiveInfoList.size() > 0) {
			for (int x = 1, i = 0; i < receiveInfoList.size(); x++, i++) {
				if (receiveInfoList.get(i) != null && receiveInfoList.get(i).getStatus() != null
						&& receiveInfoList.get(i).getStatus() == 1) {
					status = "已读";
				} else if (receiveInfoList.get(i) != null && receiveInfoList.get(i).getStatus() != null
						&& receiveInfoList.get(i).getStatus() == 0) {
					status = "未读";
				} else {
					status = "未读";
				}
				// 将数据放入数组放入数组
				// 添加数据到list
				dataList.add(new Object[] { x + "", receiveInfoList.get(i).getBuyerNick() + "",
						receiveInfoList.get(i).getSendPhone() + "", receiveInfoList.get(i).getContent() + "",
						status + "", dateFormat.format(receiveInfoList.get(i).getReceiveDate()) + "" });
			}
		}
		// 表头列名
		String[] rowName = new String[] { "序号", "买家昵称", "手机号", "短信内容", "是否已读", "发送时间" };
		try {
			// 使用工具生产Excel表格
			HSSFWorkbook workbook = ExcelExportUtils.export("买家回复内容记录", rowName, dataList);
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment;filename=document.xls");
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}*/
	
	/**
	 * 导出卖家回复内容 @Title: exportReceiveInfo @param 设定文件 @return void 返回类型 @throws
	 */
	/*@RequestMapping("/sellerSend/export")
	@ResponseBody
	public void exportSellerRevert(HttpServletRequest request, HttpServletResponse response) {
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		ReceiveInfoVO receiveInfoVO = cacheService.get("", "", ReceiveInfoVO.class);
		List<SmsReceiveInfo> receiveInfoList = receiveInfoService.listReceiveInfoLimit(userInfo.getId(), receiveInfoVO);
		// .循环每一条数据将数据放入数组在将数组放入list
		List<Object[]> dataList = new ArrayList<Object[]>();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String status = "";
		if (receiveInfoList != null && receiveInfoList.size() > 0) {
			for (int x = 1, i = 0; i < receiveInfoList.size(); x++, i++) {
				if (receiveInfoList.get(i) != null && receiveInfoList.get(i).getStatus() != null
						&& receiveInfoList.get(i).getStatus() == 1) {
					status = "已读";
				} else if (receiveInfoList.get(i) != null && receiveInfoList.get(i).getStatus() != null
						&& receiveInfoList.get(i).getStatus() == 0) {
					status = "未读";
				} else {
					status = "未读";
				}
				// 将数据放入数组放入数组
				// 添加数据到list
				dataList.add(new Object[] { x + "", receiveInfoList.get(i).getBuyerNick() + "",
						receiveInfoList.get(i).getSendPhone() + "", receiveInfoList.get(i).getContent() + "",
						status + "", dateFormat.format(receiveInfoList.get(i).getReceiveDate()) + "" });
			}
		}
		// 表头列名
		String[] rowName = new String[] { "序号", "买家昵称", "手机号", "短信内容", "是否已读", "发送时间" };
		try {
			// 使用工具生产Excel表格
			HSSFWorkbook workbook = ExcelExportUtils.export("买家回复内容记录", rowName, dataList);
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment;filename=document.xls");
			workbook.write(response.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return;
	}*/
}
