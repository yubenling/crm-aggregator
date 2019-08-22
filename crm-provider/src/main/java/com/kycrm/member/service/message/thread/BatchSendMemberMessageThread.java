package com.kycrm.member.service.message.thread;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kycrm.member.component.IMessageQueue;
import com.kycrm.member.domain.entity.member.MemberInfoDTO;
import com.kycrm.member.domain.entity.message.BatchSmsData;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.message.SmsRecordDTO;
import com.kycrm.member.domain.entity.message.SmsSendInfo;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.message.BatchSendBaseFilterMemberMessageVO;
import com.kycrm.member.domain.vo.message.BatchSendMemberMessageBaseVO;
import com.kycrm.member.domain.vo.message.BatchSendPremiumFilterMemberMessageVO;
import com.kycrm.member.domain.vo.message.SendMsgVo;
import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterVO;
import com.kycrm.member.service.marketing.IMarketingMemberFilterService;
import com.kycrm.member.service.marketing.IPremiumMarketingMemberFilterService;
import com.kycrm.member.service.message.IMsgSendRecordService;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.message.ISmsSendInfoScheduleService;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.EncrptAndDecryptClient;
import com.kycrm.util.FilterMobileUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MsgType;

public class BatchSendMemberMessageThread implements Runnable {

	private static final Log logger = LogFactory.getLog(BatchSendMemberMessageThread.class);

	private BatchSendMemberMessageBaseVO batchSendMemberMessageBaseVO;

	private boolean isSendScheduleMessage;

	private IMarketingMemberFilterService marketingMemberFilterService;

	private IPremiumMarketingMemberFilterService premiumMarketingMemberFilterService;

	private ISmsRecordDTOService smsRecordDTOService;

	private IMsgSendRecordService msgSendRecordService;

	private ISmsBlackListDTOService smsBlackListDTOService;

	private ISmsSendInfoScheduleService smsSendInfoScheduleService;

	private IMessageQueue messageQueue;

	// 每批次容量
	private static final Integer BATCH_SIZE = 500;

	public BatchSendMemberMessageThread(BatchSendMemberMessageBaseVO batchSendMemberMessageBaseVO,
			boolean isSendScheduleMessage, IMarketingMemberFilterService marketingMemberFilterService,
			IPremiumMarketingMemberFilterService premiumMarketingMemberFilterService,
			ISmsRecordDTOService smsRecordDTOService, IMsgSendRecordService msgSendRecordService,
			ISmsBlackListDTOService smsBlackListDTOService, ISmsSendInfoScheduleService smsSendInfoScheduleService,
			IMessageQueue messageQueue) {
		super();
		this.batchSendMemberMessageBaseVO = batchSendMemberMessageBaseVO;
		this.isSendScheduleMessage = isSendScheduleMessage;
		this.marketingMemberFilterService = marketingMemberFilterService;
		this.premiumMarketingMemberFilterService = premiumMarketingMemberFilterService;
		this.smsRecordDTOService = smsRecordDTOService;
		this.msgSendRecordService = msgSendRecordService;
		this.smsBlackListDTOService = smsBlackListDTOService;
		this.smsSendInfoScheduleService = smsSendInfoScheduleService;
		this.messageQueue = messageQueue;
	}

	@Override
	public void run() {
		// 用户信息
		UserInfo user = batchSendMemberMessageBaseVO.getUser();
		// 发送短信入参
		SendMsgVo sendMsgVo = batchSendMemberMessageBaseVO.getSendMsgVo();
		// 满足筛选条件的会员总数
		Long memberCount = batchSendMemberMessageBaseVO.getMemberCount();
		// 本次发送短信的uuid
		String uuid = batchSendMemberMessageBaseVO.getUuid();
		// 屏蔽天数
		Integer shieldDay = batchSendMemberMessageBaseVO.getShieldDay();
		// 是否为定时短信
		Boolean schedule = sendMsgVo.getSchedule();
		Long filterRecordId = batchSendMemberMessageBaseVO.getFilterRecordId();
		Long uid = user.getId();
		String taobaoUserNick = user.getTaobaoUserNick();
		String accessToken = user.getAccessToken();
		try {
			int batchCount = this.getBatchCount(memberCount.intValue());
			Long msgId = null;
			Long limitId = null;
			if (isSendScheduleMessage) {
				msgId = batchSendMemberMessageBaseVO.getSendMsgVo().getMsgId();
			} else {
				msgId = 0L;
			}
			// 获取用户黑名单列表
			List<String> blackPhoneList = this.getBlackPhoneList(user, uuid);
			final FilterMobileUtil filterMobileUtil = new FilterMobileUtil();
			Map<String, Long> resultMap = null;
			if (schedule) {
				this.sendScheduledMessage(user, shieldDay, sendMsgVo, accessToken, uid, taobaoUserNick, uuid,
						batchSendMemberMessageBaseVO, filterRecordId);
			} else {
				for (int i = batchCount - 1; i >= 0; i--) {
					logger.info("UID = " + user.getId() + " TAOBAO_USER_NICK = " + user.getTaobaoUserNick() + " UUID = "
							+ uuid + " 第 " + (i + 1) + " 批次短信提交");
					if ((i == batchCount - 1) && !isSendScheduleMessage) {
						resultMap = this.send(filterMobileUtil, i, 0, BATCH_SIZE, blackPhoneList);// 获取短信总记录表ID
						if (resultMap != null) {
							if (resultMap.containsKey("msgId")) {
								msgId = resultMap.get("msgId");
							} else {
								throw new Exception("短信总记录表保存失败");
							}
							if (resultMap.containsKey("limitId")) {
								limitId = resultMap.get("limitId");
							} else {
								throw new Exception("未获取到当前批次的最小ID");
							}
						}
						logger.info("UID = " + user.getId() + " TAOBAO_USER_NICK = " + user.getTaobaoUserNick()
								+ " UUID = " + uuid + " MSGID = " + msgId);
					} else {
						if ((i == batchCount - 1) && isSendScheduleMessage) {
							// 更新短信总记录中定时短信的发送总数
							this.updateMsgSendRecord(user, msgId, batchSendMemberMessageBaseVO.getMemberCount());
						}
						limitId = this.send(filterMobileUtil, msgId, limitId, i, batchCount, 0, BATCH_SIZE,
								blackPhoneList);
					}
				}
			}
			// 提交短信任务完成就认为是发送完成
			// 需要更新短信总记录表的发送状态
			MsgSendRecord msg = new MsgSendRecord();
			msg.setId(msgId);
			msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
			this.msgSendRecordService.updateMsgRecordByMsgId(msg);
		} catch (Exception e) {
			e.printStackTrace();
			if (schedule) {
				logger.error("UID = " + uid + " TAOBAO_USER_NICK = " + taobaoUserNick + " UUID = " + uuid
						+ " 会员【定时】短信群发 批量发送出错 " + e);
			} else {
				logger.error("UID = " + uid + " TAOBAO_USER_NICK = " + taobaoUserNick + " UUID = " + uuid
						+ " 会员【及时】短信群发 批量发送出错 " + e);
			}
		}
	}

	@SuppressWarnings("unchecked")
	private Map<String, Long> send(FilterMobileUtil filterMobileUtil, int batch, int startRows, int pageSize,
			List<String> blackPhoneList) throws Exception {
		long startTime = System.currentTimeMillis();
		Map<String, Object> memberInfoResultMap = null;
		Long msgId = 0L;
		UserInfo user = batchSendMemberMessageBaseVO.getUser();
		SendMsgVo sendMsgVo = batchSendMemberMessageBaseVO.getSendMsgVo();
		Long memberCount = batchSendMemberMessageBaseVO.getMemberCount();
		String uuid = batchSendMemberMessageBaseVO.getUuid();
		Long filterRecordId = batchSendMemberMessageBaseVO.getFilterRecordId();
		Integer memberFilterType = batchSendMemberMessageBaseVO.getMemberFilterType();
		MemberFilterVO memberFilterVO = null;
		PremiumMemberFilterVO premiumMemberFilterVO = null;
		byte[] compress = null;
		if (memberFilterType == 1) {
			BatchSendBaseFilterMemberMessageVO batchSendBaseFilterMemberMessageVO = (BatchSendBaseFilterMemberMessageVO) batchSendMemberMessageBaseVO;
			memberFilterVO = batchSendBaseFilterMemberMessageVO.getMemberFilterVO();
		} else {
			BatchSendPremiumFilterMemberMessageVO batchSendPremiumFilterMemberMessageVO = (BatchSendPremiumFilterMemberMessageVO) batchSendMemberMessageBaseVO;
			premiumMemberFilterVO = batchSendPremiumFilterMemberMessageVO.getPremiumMemberFilterVO();
			memberFilterVO = batchSendPremiumFilterMemberMessageVO.getMemberFilterVO();
			compress = batchSendPremiumFilterMemberMessageVO.getCompress();
		}
		Long uid = user.getId();
		String taoBaoUserNick = user.getTaobaoUserNick();
		String accessToken = user.getAccessToken();
		try {
			if (memberFilterType == 1) {// 基础筛选
				memberInfoResultMap = this.marketingMemberFilterService.findMembersByCondition(uid, taoBaoUserNick,
						accessToken, memberFilterVO, startRows, pageSize, false, true);
			} else {// 高级筛选
				memberInfoResultMap = this.premiumMarketingMemberFilterService.findMembersByCondition(uid,
						taoBaoUserNick, accessToken, premiumMemberFilterVO, memberFilterVO, compress, startRows,
						pageSize, false, true);
			}
			List<MemberInfoDTO> memberInfoList = (List<MemberInfoDTO>) memberInfoResultMap.get("memberInfoList");
			// 本次查询结果的最大ID
			int index = memberInfoList.size() - 1;
			Long limitId = memberInfoList.get(index).getId();
			int batchMemberCount = memberInfoList.size();
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 本批次数据库查询出的会员数量 = " + batchMemberCount);
			Map<String, Collection<MemberInfoDTO>> removePhone = filterMobileUtil.disposePhones(memberInfoList);
			Set<MemberInfoDTO> correctPhone = (Set<MemberInfoDTO>) removePhone.get("phoneSet");
			int correctPhoneSize = correctPhone.size();// 正确的手机号码数量
			List<MemberInfoDTO> wrongNums = (List<MemberInfoDTO>) removePhone.get("wrongNums");
			int wrongPhoneSize = wrongNums.size();// 错误的手机号码数量
			List<MemberInfoDTO> repeatNums = (List<MemberInfoDTO>) removePhone.get("repeatNums");
			int repeatPhoneSize = repeatNums.size();// 重复的手机号码数量
			Map<String, Object> map = null;
			int blackPhoneSize = 0;
			List<MemberInfoDTO> blackPhones = new ArrayList<MemberInfoDTO>();
			if (blackPhoneList != null && blackPhoneList.size() > 0) {
				map = this.filtrateBlackPhone(correctPhone, blackPhoneList);
				if (map.containsKey("blackPhones")) {
					blackPhones = (List<MemberInfoDTO>) map.get("blackPhones");
					blackPhoneSize = blackPhones.size();// 黑名单的电话号码数量
				}
				if (map.containsKey("correctPhones")) {
					correctPhone = (Set<MemberInfoDTO>) map.get("correctPhones");
					correctPhoneSize = correctPhone.size();// 正确的电话号码数量
				}
			}
			Integer totalSendCount = batchMemberCount - wrongPhoneSize - repeatPhoneSize - blackPhoneSize;
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 会员总数 = " + batchMemberCount);
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 正确的手机号码数量 = " + correctPhoneSize);
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 错误的手机号码数量 = " + wrongPhoneSize);
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 重复的手机号码数量 = " + repeatPhoneSize);
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 黑名单的电话号码数量 = " + blackPhoneSize);
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 本批次总数 = " + totalSendCount);
			if (batchMemberCount != (totalSendCount + wrongPhoneSize + repeatPhoneSize + blackPhoneSize)) {
				logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
						+ " 批次 " + "发送短信数量有问题");
			}
			msgId = this.saveMsgSendRecord(user, memberCount, filterRecordId, wrongPhoneSize, repeatPhoneSize,
					blackPhoneSize, 0, sendMsgVo);
			logger.info("UUID = " + uuid + " 第 " + batch + " 批次 " + "短信会员群发 短信总记录表ID = " + msgId);
			sendMsgVo.setMsgId(msgId);
			// 存储错误，重复，黑名单号码
			this.batchSaveSmsRecordDTO(uid, sendMsgVo, accessToken, msgId, wrongNums, repeatNums, blackPhones);
			long endTime = System.currentTimeMillis();
			logger.info("UID = " + user.getId() + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 "
					+ batch + " 批次 " + " 准备阶段耗时 = " + (endTime - startTime) + " ms");
			this.sendMessageImmediately(new ArrayList<MemberInfoDTO>(correctPhone), sendMsgVo, accessToken, uid,
					taoBaoUserNick, uuid, batch);
			this.addCorrectPhoneForGlobalFilter(filterMobileUtil, correctPhone, batch);
			Map<String, Long> resultMap = new HashMap<String, Long>(2);
			resultMap.put("msgId", msgId);
			resultMap.put("limitId", limitId);
			return resultMap;
		} catch (Exception e) {
			logger.error("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 会员短信群发 批量发送出错 " + e);
			throw new Exception(e);
		}
	}

	@SuppressWarnings("unchecked")
	private Long send(FilterMobileUtil filterMobileUtil, Long msgId, Long limitId, int batch, int batchCount,
			int startRows, int pageSize, List<String> blackPhoneList) throws Exception {
		long startTime = System.currentTimeMillis();
		Map<String, Object> memberInfoResultMap = null;
		UserInfo user = batchSendMemberMessageBaseVO.getUser();
		SendMsgVo sendMsgVo = batchSendMemberMessageBaseVO.getSendMsgVo();
		String uuid = batchSendMemberMessageBaseVO.getUuid();
		Integer memberFilterType = batchSendMemberMessageBaseVO.getMemberFilterType();
		MemberFilterVO memberFilterVO = null;
		PremiumMemberFilterVO premiumMemberFilterVO = null;
		byte[] compress = null;
		if (memberFilterType == 1) {
			BatchSendBaseFilterMemberMessageVO batchSendBaseFilterMemberMessageVO = (BatchSendBaseFilterMemberMessageVO) batchSendMemberMessageBaseVO;
			memberFilterVO = batchSendBaseFilterMemberMessageVO.getMemberFilterVO();
			memberFilterVO.setLimitId(limitId);
		} else {
			BatchSendPremiumFilterMemberMessageVO batchSendPremiumFilterMemberMessageVO = (BatchSendPremiumFilterMemberMessageVO) batchSendMemberMessageBaseVO;
			premiumMemberFilterVO = batchSendPremiumFilterMemberMessageVO.getPremiumMemberFilterVO();
			premiumMemberFilterVO.setLimitId(limitId);
			memberFilterVO = batchSendPremiumFilterMemberMessageVO.getMemberFilterVO();
			compress = batchSendPremiumFilterMemberMessageVO.getCompress();
		}
		Long uid = user.getId();
		String taoBaoUserNick = user.getTaobaoUserNick();
		String accessToken = user.getAccessToken();
		Boolean schedule = sendMsgVo.getSchedule();
		try {
			if (memberFilterType == 1) {
				memberInfoResultMap = this.marketingMemberFilterService.findMembersByCondition(uid, taoBaoUserNick,
						accessToken, memberFilterVO, startRows, pageSize, false, true);
			} else {
				memberInfoResultMap = this.premiumMarketingMemberFilterService.findMembersByCondition(uid,
						taoBaoUserNick, accessToken, premiumMemberFilterVO, memberFilterVO, compress, startRows,
						pageSize, false, true);
			}
			List<MemberInfoDTO> memberInfoList = (List<MemberInfoDTO>) memberInfoResultMap.get("memberInfoList");
			int index = memberInfoList.size() - 1;
			Long nextLimitId = memberInfoList.get(index).getId();
			int batchMemberCount = memberInfoList.size();
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 本批次数据库查询出的会员数量 = " + batchMemberCount);
			Map<String, Collection<MemberInfoDTO>> removePhone = filterMobileUtil.disposePhones(memberInfoList);
			Set<MemberInfoDTO> correctPhone = (Set<MemberInfoDTO>) removePhone.get("phoneSet");
			int correctPhoneSize = correctPhone.size();// 正确的手机号码数量
			List<MemberInfoDTO> wrongNums = (List<MemberInfoDTO>) removePhone.get("wrongNums");
			int wrongPhoneSize = wrongNums.size();// 错误的手机号码数量
			List<MemberInfoDTO> repeatNums = (List<MemberInfoDTO>) removePhone.get("repeatNums");
			int repeatPhoneSize = repeatNums.size();// 重复的手机号码数量
			Map<String, Object> map = null;
			int blackPhoneSize = 0;
			List<MemberInfoDTO> blackPhones = new ArrayList<MemberInfoDTO>();
			if (blackPhoneList != null && blackPhoneList.size() > 0) {
				map = this.filtrateBlackPhone(correctPhone, blackPhoneList);
				if (map.containsKey("blackPhones")) {
					blackPhones = (List<MemberInfoDTO>) map.get("blackPhones");
					blackPhoneSize = blackPhones.size();// 黑名单的电话号码数量
				}
				if (map.containsKey("correctPhones")) {
					correctPhone = (Set<MemberInfoDTO>) map.get("correctPhones");
					correctPhoneSize = correctPhone.size();// 正确的电话号码数量
				}
			}
			Integer totalSendCount = batchMemberCount - wrongPhoneSize - repeatPhoneSize - blackPhoneSize;
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 会员总数 = " + batchMemberCount);
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " msgId = " + msgId);
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 正确的手机号码数量 = " + correctPhoneSize);
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 错误的手机号码数量 = " + wrongPhoneSize);
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 重复的手机号码数量 = " + repeatPhoneSize);
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 黑名单的电话号码数量 = " + blackPhoneSize);
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 会员短信群发 本批次总数 = " + totalSendCount);
			if (batchMemberCount != (totalSendCount + wrongPhoneSize + repeatPhoneSize + blackPhoneSize)) {
				logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
						+ " 批次 " + "发送短信数量有问题");
			}
			sendMsgVo.setMsgId(msgId);
			// 存储错误，重复，黑名单号码
			this.batchSaveSmsRecordDTO(uid, sendMsgVo, accessToken, msgId, wrongNums, repeatNums, blackPhones);
			long endTime = System.currentTimeMillis();
			logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 " + batch
					+ " 批次 " + " 准备阶段耗时 = " + (endTime - startTime) + " ms");
			this.sendMessageImmediately(new ArrayList<MemberInfoDTO>(correctPhone), sendMsgVo, accessToken, uid,
					taoBaoUserNick, uuid, batch);
			// 更新短信总记录表
			this.updateMsgSendRecord(user, wrongPhoneSize, repeatPhoneSize, blackPhoneSize, 0, sendMsgVo, schedule,
					uuid, batch, batchCount);
			this.addCorrectPhoneForGlobalFilter(filterMobileUtil, correctPhone, batch);
			return nextLimitId;
		} catch (Exception e) {
			if (schedule) {
				logger.error("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 "
						+ batch + " 批次 " + " 会员【定时】短信群发 批量发送出错 " + e);
			} else {
				logger.error("UID = " + uid + " TAOBAO_USER_NICK = " + taoBaoUserNick + " UUID = " + uuid + " 第 "
						+ batch + " 批次 " + " 会员【及时】短信群发 批量发送出错 " + e);
			}
			throw new Exception(e);
		}
	}

	private void addCorrectPhoneForGlobalFilter(FilterMobileUtil filterMobileUtil, Set<MemberInfoDTO> correctPhone,
			int batch) throws Exception {
		Set<String> correctPhoneSet = new HashSet<String>();
		for (MemberInfoDTO memberInfoDTO : correctPhone) {
			correctPhoneSet.add(memberInfoDTO.getMobile());
		}
		filterMobileUtil.addNewData(correctPhoneSet, batch);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 计算发送短信的批次数
	 * @Date 2018年9月3日下午3:38:27
	 * @param memberInfoListSize
	 * @return
	 * @throws Exception
	 * @ReturnType int
	 */
	private int getBatchCount(int memberInfoListSize) throws Exception {
		int batchCount = 0;
		if (memberInfoListSize % BATCH_SIZE == 0) {
			batchCount = memberInfoListSize / BATCH_SIZE;
		} else {
			batchCount = memberInfoListSize / BATCH_SIZE + 1;
		}
		return batchCount;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取指定用户黑名单列表
	 * @Date 2018年11月8日下午12:16:03
	 * @param correctPhones
	 * @param user
	 * @return
	 * @throws Exception
	 * @ReturnType List<String>
	 */
	private List<String> getBlackPhoneList(UserInfo user, String uuid) throws Exception {
		if (user != null && user.getId() != null) {
			List<String> list = smsBlackListDTOService.findBlackPhones(user.getId(), user);
			logger.info("UID = " + user.getId() + " TAOBAO_USER_NICK = " + user.getTaobaoUserNick() + " UUID = " + uuid
					+ " 黑名单号码数据库数量 = " + list.size());
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 定时短信总记录
	 * @Date 2018年8月28日下午2:41:42
	 * @param user
	 * @param filterRecordId
	 * @param sendMsgVo
	 * @return
	 * @ReturnType Long
	 */
	private Long saveMsgSendRecord(UserInfo user, Long filterRecordId, SendMsgVo sendMsgVo) throws Exception {
		// 保存总记录返回id
		MsgSendRecord msg = new MsgSendRecord();
		msg.setUid(user.getId());
		msg.setUserId(user.getTaobaoUserNick());
		msg.setMarketingType(sendMsgVo.getMemberFilterType().toString());
		msg.setQueryParamId(filterRecordId);
		msg.setSucceedCount(0);
		msg.setFailedCount(0);
		msg.setTemplateContent(sendMsgVo.getContent());
		msg.setActivityName(sendMsgVo.getActivityName());
		msg.setType(MsgType.MSG_HYDXQF);
		msg.setIsShow(true);
		msg.setStatus(MsgType.MSG_STATUS_SENDING);
		msg.setIsSent(false);
		msg.setShortLinkType(sendMsgVo.getShortLinkType());
		msg.setSendCreat(DateUtils.parseDate(sendMsgVo.getSendTime(), DateUtils.DEFAULT_TIME_FORMAT));
		return msgSendRecordService.saveMsgSendRecord(msg);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 更新短信总记录表
	 * @Date 2018年9月15日上午11:48:20
	 * @param user
	 * @param sendMsgVo
	 * @ReturnType void
	 */
	private void updateMsgSendRecord(UserInfo user, SendMsgVo sendMsgVo) throws Exception {
		MsgSendRecord msg = new MsgSendRecord();
		msg.setId(sendMsgVo.getMsgId());
		msg.setUid(user.getId());
		msg.setUserId(user.getTaobaoUserNick());
		msg.setType(MsgType.MSG_HYDXQF);
		msg.setIsShow(true);
		msg.setIsSent(false);
		msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
		msgSendRecordService.updateMsgRecordForBatchSend(msg);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 更新短信总记录表
	 * @Date 2018年9月15日上午11:48:20
	 * @param user
	 * @param totalCount
	 * @param wrongPhoneSize
	 * @param repeatPhoneSize
	 * @param blackPhoneSize
	 * @param shieldPhone
	 * @param sendMsgVo
	 * @ReturnType void
	 */
	private void updateMsgSendRecord(UserInfo user, Long msgId, Long totalCount) throws Exception {
		MsgSendRecord msg = new MsgSendRecord();
		msg.setId(msgId);
		msg.setUid(user.getId());
		msg.setTotalCount(totalCount.intValue());
		msgSendRecordService.updateMsgRecordForBatchSend(msg);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 更新短信总记录表
	 * @Date 2018年9月15日上午11:48:20
	 * @param user
	 * @param wrongPhoneSize
	 * @param repeatPhoneSize
	 * @param blackPhoneSize
	 * @param shieldPhone
	 * @param sendMsgVo
	 * @ReturnType void
	 */
	private void updateMsgSendRecord(UserInfo user, int wrongPhoneSize, int repeatPhoneSize, int blackPhoneSize,
			int shieldPhone, SendMsgVo sendMsgVo, boolean schedule, String uuid, int batch, int batchCount)
			throws Exception {
		MsgSendRecord msg = new MsgSendRecord();
		msg.setId(sendMsgVo.getMsgId());
		msg.setUid(user.getId());
		msg.setUserId(user.getTaobaoUserNick());
		msg.setWrongCount(wrongPhoneSize);
		msg.setRepeatCount(repeatPhoneSize);
		msg.setBlackCount(blackPhoneSize);
		msg.setSheildCount(shieldPhone);
		msg.setTemplateContent(sendMsgVo.getContent());
		msg.setActivityName(sendMsgVo.getActivityName());
		msg.setType(MsgType.MSG_HYDXQF);
		msg.setIsShow(true);
		msg.setIsSent(sendMsgVo.getSchedule() ? false : true);
		msg.setSendCreat(sendMsgVo.getSchedule() ? DateUtils.parseDate(sendMsgVo.getSendTime(), "yyyy-MM-dd HH:mm:ss")
				: new Date());
		if (batch == batchCount) {
			msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
		}
		logger.info("UID = " + user.getId() + " TAOBAO_USER_NICK = " + user.getTaobaoUserNick() + " UUID = " + uuid
				+ " 第 " + batch + " 批次 " + " 更新短信总记录表 = " + msg.toString());
		msgSendRecordService.updateMsgRecordForBatchSend(msg);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 短信群发 - 定时发送
	 * @Date 2018年8月27日下午2:34:35
	 * @param memberInfoList
	 * @param shieldDay
	 * @param sendMsgVo
	 * @param accessToken
	 * @ReturnType Map<String, Integer>
	 */
	private void sendScheduledMessage(UserInfo user, Integer shieldDay, SendMsgVo sendMsgVo, String accessToken,
			Long uid, String taobaoUserNick, String uuid, BatchSendMemberMessageBaseVO batchSendMemberMessageBaseVO,
			Long filterRecordId) throws Exception {
		Long msgId = this.saveMsgSendRecord(user, filterRecordId, sendMsgVo);
		batchSendMemberMessageBaseVO.getSendMsgVo().setMsgId(msgId);
		SmsSendInfo smsSendInfo = new SmsSendInfo();
		try {
			smsSendInfo.setUid(sendMsgVo.getUid());
			smsSendInfo.setUserId(sendMsgVo.getUserId());
			smsSendInfo.setMsgId(msgId);
			smsSendInfo.setType(sendMsgVo.getMsgType());
			smsSendInfo.setContent(sendMsgVo.getContent());
			Date startTime = DateUtils.parseDate(sendMsgVo.getSendTime(), DateUtils.DEFAULT_TIME_FORMAT);
			Date endTime = DateUtils.addDate(startTime, 1);
			smsSendInfo.setStartSend(startTime);
			smsSendInfo.setEndSend(endTime);
			smsSendInfo.setChannel(sendMsgVo.getAutograph());
			smsSendInfo.setShieldDay(shieldDay);
			smsSendInfo.setCreatedDate(new Date());
			smsSendInfo.setLastModifiedDate(new Date());
			smsSendInfo.setMemberFilterType(sendMsgVo.getMemberFilterType());
			smsSendInfo.setMemberFilterCondition(JsonUtil.toJson(batchSendMemberMessageBaseVO));
			boolean result = smsSendInfoScheduleService.doAutoCreate(smsSendInfo);
			if (result) {
				this.updateMsgSendRecord(user, sendMsgVo);
			} else {
				throw new Exception("保存【会员定时短信】失败");
			}
		} catch (Exception e) {
			logger.error("UID = " + uid + " TAOBAO_USER_NICK = " + taobaoUserNick + " UUID = " + uuid
					+ " 会员短信群发-定时发送:保存单笔定时数据失败!!", e);
			throw new Exception(e);
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 短信群发 - 立即发送
	 * @Date 2018年8月27日下午2:34:58
	 * @param memberInfoList
	 * @param sendMsgVo
	 * @param accessToken
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Integer>
	 */
	private void sendMessageImmediately(List<MemberInfoDTO> memberInfoList, SendMsgVo sendMsgVo, String accessToken,
			Long uid, String taobaoUserNick, String uuid, int batch) throws Exception {
		long s = System.currentTimeMillis();
		BatchSmsData batchSmsData = null;
		int memberInfoListSize = memberInfoList.size();
		if (sendMsgVo.getContent().contains("{买家昵称}") || sendMsgVo.getContent().contains("{买家姓名}")) {
			batchSmsData = new BatchSmsData();
			batchSmsData.setUid(sendMsgVo.getUid());
			batchSmsData.setType(sendMsgVo.getMsgType());
			batchSmsData.setIpAdd(sendMsgVo.getIpAddress());
			batchSmsData.setChannel(sendMsgVo.getAutograph());
			batchSmsData.setAutograph(sendMsgVo.getAutograph());
			batchSmsData.setUserId(sendMsgVo.getUserId());
			batchSmsData.setContent(sendMsgVo.getContent());
			batchSmsData.setMsgId(sendMsgVo.getMsgId());
			batchSmsData.setTotal(sendMsgVo.getTotalCount().intValue());
			List<String> phoneAndContent = new ArrayList<String>(memberInfoListSize);
			for (int j = 0; j < memberInfoListSize; j++) {
				String smsContent = getNewSmsContent(sendMsgVo.getContent(), memberInfoList.get(j));
				phoneAndContent.add(memberInfoList.get(j).getMobile() + Constants.SMSSEPARATOR + smsContent
						+ Constants.SMSSEPARATOR + EncrptAndDecryptClient.getInstance().encryptData(
								memberInfoList.get(j).getBuyerNick(), EncrptAndDecryptClient.SEARCH, accessToken));
			}
			batchSmsData.setContentArr(phoneAndContent.toArray(new String[phoneAndContent.size()]));
			batchSmsData.setTotal(phoneAndContent.size());
			this.messageQueue.putSmsDataToQueue(batchSmsData);
		} else {
			List<String> phones = new ArrayList<String>(memberInfoListSize);
			for (int j = 0; j < memberInfoListSize; j++) {
				phones.add(memberInfoList.get(j).getMobile() + Constants.SMSSEPARATOR
						+ EncrptAndDecryptClient.getInstance().encryptData(memberInfoList.get(j).getBuyerNick(),
								EncrptAndDecryptClient.SEARCH, accessToken));
			}
			batchSmsData = new BatchSmsData(phones.toArray(new String[phones.size()]));// 构造方法将电话号设置进去了
			batchSmsData.setUid(sendMsgVo.getUid());
			batchSmsData.setType(sendMsgVo.getMsgType());
			batchSmsData.setIpAdd(sendMsgVo.getIpAddress());
			batchSmsData.setChannel(sendMsgVo.getAutograph());
			batchSmsData.setAutograph(sendMsgVo.getAutograph());
			batchSmsData.setUserId(sendMsgVo.getUserId());
			batchSmsData.setContent(sendMsgVo.getContent());
			batchSmsData.setMsgId(sendMsgVo.getMsgId());
			this.messageQueue.putSmsDataToQueue(batchSmsData);
		}
		logger.info("UID = " + uid + " TAOBAO_USER_NICK = " + taobaoUserNick + " UUID = " + uuid + " 第 " + batch
				+ " 批次 " + " 会员短信群发-立即发送:批量发送时间开销:" + (System.currentTimeMillis() - s) + "millis");
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 处理黑名单号码
	 * @Date 2018年11月8日下午12:29:06
	 * @param correctPhones
	 * @param blackPhoneList
	 * @return
	 * @throws Exception
	 * @ReturnType Map<String,Object>
	 */
	private Map<String, Object> filtrateBlackPhone(Set<MemberInfoDTO> correctPhones, List<String> blackPhoneList)
			throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		List<MemberInfoDTO> blackPhones = new ArrayList<MemberInfoDTO>();
		if (correctPhones != null && correctPhones.size() > 0) {
			if (null != blackPhoneList && blackPhoneList.size() > 0) {
				for (int i = 0; i < blackPhoneList.size(); i++) {
					String phone = blackPhoneList.get(i);
					for (MemberInfoDTO member : correctPhones) {
						if (phone.equals(member.getMobile())) {
							blackPhones.add(member);
						}
					}
				}
				correctPhones.removeAll(blackPhones);
				map.put("correctPhones", correctPhones);
			} else {
				map.put("correctPhones", correctPhones);
			}
			map.put("blackPhones", blackPhones);
		}
		return map;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 立即发送总记录
	 * @Date 2018年8月28日下午2:41:42
	 * @param user
	 * @param totalCount
	 * @param wrongPhone
	 * @param repeatPhone
	 * @param blackPhone
	 * @param shieldPhone
	 * @param vo
	 * @return
	 * @ReturnType Long
	 */
	private Long saveMsgSendRecord(UserInfo user, Long memberCount, Long filterRecordId, int wrongPhone,
			int repeatPhone, int blackPhone, int shieldPhone, SendMsgVo sendMsgVo) throws Exception {
		// 保存总记录返回id
		MsgSendRecord msg = new MsgSendRecord();
		msg.setUid(user.getId());
		msg.setUserId(user.getTaobaoUserNick());
		msg.setMarketingType(sendMsgVo.getMemberFilterType().toString());
		msg.setQueryParamId(filterRecordId);
		msg.setTotalCount(memberCount.intValue());
		msg.setSucceedCount(0);
		msg.setFailedCount(0);
		msg.setWrongCount(wrongPhone);
		msg.setRepeatCount(repeatPhone);
		msg.setBlackCount(blackPhone);
		msg.setSheildCount(shieldPhone);
		msg.setTemplateContent(sendMsgVo.getContent());
		msg.setActivityName(sendMsgVo.getActivityName());
		msg.setType(MsgType.MSG_HYDXQF);
		msg.setIsShow(true);
		msg.setTaoBaoShortLinkId(sendMsgVo.getTaoBaoShortLinkId());
		msg.setShortLinkType(sendMsgVo.getShortLinkType());
		msg.setStatus(sendMsgVo.getSchedule() ? MsgType.MSG_STATUS_SENDING : MsgType.MSG_STATUS_SENDOVER);
		msg.setIsSent(sendMsgVo.getSchedule() ? false : true);
		msg.setSendCreat(sendMsgVo.getSchedule() ? DateUtils.parseDate(sendMsgVo.getSendTime(), "yyyy-MM-dd HH:mm:ss")
				: new Date());
		return msgSendRecordService.saveMsgSendRecord(msg);
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 批量保存错误，重复，黑名单
	 * @Date 2018年9月13日下午2:34:59
	 * @param sendMsgVo
	 * @param msgId
	 * @param wrongNums
	 * @param repeatNums
	 * @param blackPhones
	 * @throws Exception
	 * @ReturnType void
	 */
	private void batchSaveSmsRecordDTO(Long uid, SendMsgVo sendMsgVo, String accessToken, Long msgId,
			List<MemberInfoDTO> wrongNums, List<MemberInfoDTO> repeatNums, List<MemberInfoDTO> blackPhones)
			throws Exception {
		int wrongNumsSize = 0;
		if (wrongNums != null && wrongNums.size() > 0) {
			wrongNumsSize = wrongNums.size();
		}
		int repeatNumsSize = 0;
		if (repeatNums != null && repeatNums.size() > 0) {
			repeatNumsSize = repeatNums.size();
		}
		int blackPhonesSize = 0;
		if (blackPhones != null && blackPhones.size() > 0) {
			blackPhonesSize = blackPhones.size();
		}
		List<SmsRecordDTO> smsRecordDTOList = new ArrayList<SmsRecordDTO>(
				wrongNumsSize + repeatNumsSize + blackPhonesSize);
		SmsRecordDTO smsRecordDTO = null;
		MemberInfoDTO memberInfoDTO = null;
		String activityName = sendMsgVo.getActivityName();
		String content = sendMsgVo.getContent();
		String autograph = sendMsgVo.getAutograph();
		for (int i = 0; i < wrongNums.size(); i++) {
			smsRecordDTO = new SmsRecordDTO();
			memberInfoDTO = wrongNums.get(i);
			smsRecordDTO.setUid(memberInfoDTO.getUid());
			smsRecordDTO.setUserId(memberInfoDTO.getUserName());
			smsRecordDTO.setType(MsgType.MSG_HYDXQF);
			smsRecordDTO.setRecNum(EncrptAndDecryptClient.getInstance().encryptData(memberInfoDTO.getMobile(),
					EncrptAndDecryptClient.PHONE, accessToken));
			smsRecordDTO.setBuyerNick(EncrptAndDecryptClient.getInstance().encryptData(memberInfoDTO.getBuyerNick(),
					EncrptAndDecryptClient.SEARCH, accessToken));
			smsRecordDTO.setContent(getNewSmsContent(content, memberInfoDTO));
			smsRecordDTO.setActualDeduction(0);
			smsRecordDTO.setNickname(EncrptAndDecryptClient.getInstance().encryptData(memberInfoDTO.getReceiverName(),
					EncrptAndDecryptClient.SEARCH, accessToken));
			smsRecordDTO.setAutograph(autograph);
			smsRecordDTO.setChannel(autograph);
			smsRecordDTO.setSource("2");
			smsRecordDTO.setStatus(3);// 手机号码不正确
			smsRecordDTO.setMsgId(msgId);
			smsRecordDTO.setIsShow(true);
			smsRecordDTO.setActivityName(activityName);
			smsRecordDTO.setCreatedDate(new Date());
			smsRecordDTOList.add(smsRecordDTO);
		}
		for (int i = 0; i < repeatNums.size(); i++) {
			smsRecordDTO = new SmsRecordDTO();
			memberInfoDTO = repeatNums.get(i);
			smsRecordDTO.setUid(memberInfoDTO.getUid());
			smsRecordDTO.setUserId(memberInfoDTO.getUserName());
			smsRecordDTO.setType(MsgType.MSG_HYDXQF);
			smsRecordDTO.setRecNum(EncrptAndDecryptClient.getInstance().encryptData(memberInfoDTO.getMobile(),
					EncrptAndDecryptClient.PHONE, accessToken));
			smsRecordDTO.setBuyerNick(EncrptAndDecryptClient.getInstance().encryptData(memberInfoDTO.getBuyerNick(),
					EncrptAndDecryptClient.SEARCH, accessToken));
			smsRecordDTO.setContent(getNewSmsContent(content, memberInfoDTO));
			smsRecordDTO.setActualDeduction(0);
			smsRecordDTO.setNickname(EncrptAndDecryptClient.getInstance().encryptData(memberInfoDTO.getReceiverName(),
					EncrptAndDecryptClient.SEARCH, accessToken));
			smsRecordDTO.setAutograph(autograph);
			smsRecordDTO.setChannel(autograph);
			smsRecordDTO.setSource("2");
			smsRecordDTO.setStatus(4);// 手机号码重复
			smsRecordDTO.setMsgId(msgId);
			smsRecordDTO.setIsShow(true);
			smsRecordDTO.setActivityName(activityName);
			smsRecordDTO.setCreatedDate(new Date());
			smsRecordDTOList.add(smsRecordDTO);
		}
		for (int i = 0; i < blackPhones.size(); i++) {
			smsRecordDTO = new SmsRecordDTO();
			memberInfoDTO = blackPhones.get(i);
			smsRecordDTO.setUid(memberInfoDTO.getUid());
			smsRecordDTO.setUserId(memberInfoDTO.getUserName());
			smsRecordDTO.setType(MsgType.MSG_HYDXQF);
			smsRecordDTO.setRecNum(EncrptAndDecryptClient.getInstance().encryptData(memberInfoDTO.getMobile(),
					EncrptAndDecryptClient.PHONE, accessToken));
			smsRecordDTO.setBuyerNick(EncrptAndDecryptClient.getInstance().encryptData(memberInfoDTO.getBuyerNick(),
					EncrptAndDecryptClient.SEARCH, accessToken));
			smsRecordDTO.setContent(getNewSmsContent(content, memberInfoDTO));
			smsRecordDTO.setActualDeduction(0);
			smsRecordDTO.setNickname(EncrptAndDecryptClient.getInstance().encryptData(memberInfoDTO.getReceiverName(),
					EncrptAndDecryptClient.SEARCH, accessToken));
			smsRecordDTO.setAutograph(autograph);
			smsRecordDTO.setChannel(autograph);
			smsRecordDTO.setSource("2");
			smsRecordDTO.setStatus(5);// 手机号码黑名单
			smsRecordDTO.setMsgId(msgId);
			smsRecordDTO.setIsShow(true);
			smsRecordDTO.setActivityName(activityName);
			smsRecordDTO.setCreatedDate(new Date());
			smsRecordDTOList.add(smsRecordDTO);
		}
		this.smsRecordDTOService.doCreaterSmsRecordDTOByList(uid, smsRecordDTOList);
	}

	/**
	 * 拼凑短信内容（不对短链接进行拼凑）
	 * 
	 * @author: wy
	 * @time: 2017年9月4日 下午12:13:21
	 * @param oldSms
	 *            未拼凑的短信内容
	 * @param trade
	 *            订单
	 * @param userInfo
	 *            用户的签名
	 * @return 拼凑好的短信内容
	 */
	private String getNewSmsContent(String oldSms, MemberInfoDTO memberInfo_DTO) throws Exception {
		oldSms = oldSms.replaceAll("\\{买家昵称\\}", Matcher.quoteReplacement(memberInfo_DTO.getBuyerNick()));
		oldSms = oldSms.replaceAll("\\{买家姓名\\}", Matcher.quoteReplacement(memberInfo_DTO.getReceiverName()));
		return oldSms;
	}

}
