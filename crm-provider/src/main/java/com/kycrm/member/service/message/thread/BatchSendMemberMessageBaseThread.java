package com.kycrm.member.service.message.thread;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kycrm.member.component.IMessageQueue;
import com.kycrm.member.domain.entity.message.MsgSendRecord;
import com.kycrm.member.domain.entity.message.SmsSendInfo;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.BatchSendMemberMessageBaseVO;
import com.kycrm.member.domain.vo.message.SendMsgVo;
import com.kycrm.member.service.marketing.IMarketingMemberFilterService;
import com.kycrm.member.service.marketing.IPremiumMarketingMemberFilterService;
import com.kycrm.member.service.message.IMsgSendRecordService;
import com.kycrm.member.service.message.ISmsBlackListDTOService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.message.ISmsSendInfoScheduleService;
import com.kycrm.util.DateUtils;
import com.kycrm.util.FilterMobileUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MsgType;

@Deprecated
public class BatchSendMemberMessageBaseThread implements Runnable {

	private static final Log logger = LogFactory.getLog(BatchSendMemberMessageBaseThread.class);

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

	// 线程池大小
	private static final Integer THREAD_POOL_SIZE = 5;

	public BatchSendMemberMessageBaseThread(BatchSendMemberMessageBaseVO batchSendMemberMessageBaseVO,
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
		ExecutorService threadpool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
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
			Future<Long> future = null;
			int batchCount = this.getBatchCount(memberCount.intValue());
			Long msgId = null;
			if (isSendScheduleMessage) {
				msgId = batchSendMemberMessageBaseVO.getSendMsgVo().getMsgId();
			} else {
				msgId = 0L;
			}
			// 获取用户黑名单列表
			List<String> blackPhoneList = this.getBlackPhoneList(user, uuid);
			final FilterMobileUtil filterMobileUtil = new FilterMobileUtil();
			if (schedule) {
				this.sendScheduledMessage(user, shieldDay, sendMsgVo, accessToken, uid, taobaoUserNick, uuid,
						batchSendMemberMessageBaseVO, filterRecordId);
			} else {
				for (int i = batchCount - 1; i >= 0; i--) {
					logger.info("UID = " + user.getId() + " TAOBAO_USER_NICK = " + user.getTaobaoUserNick() + " UUID = "
							+ uuid + " 第 " + (i + 1) + " 批次短信提交");
					if ((i == batchCount - 1) && !isSendScheduleMessage) {
						future = threadpool.submit(new BatchSendMemberMessageCallable(i, i * BATCH_SIZE, BATCH_SIZE,
								batchSendMemberMessageBaseVO, blackPhoneList, filterMobileUtil,
								marketingMemberFilterService, premiumMarketingMemberFilterService, smsRecordDTOService,
								msgSendRecordService, messageQueue));
						msgId = future.get();// 获取短信总记录表ID
						logger.info("UID = " + user.getId() + " TAOBAO_USER_NICK = " + user.getTaobaoUserNick()
								+ " UUID = " + uuid + " MSGID = " + msgId);
					} else {
						if ((i == batchCount - 1) && isSendScheduleMessage) {
							// 更新短信总记录中定时短信的发送总数
							this.updateMsgSendRecord(user, msgId, batchSendMemberMessageBaseVO.getMemberCount());
						}
						threadpool.execute(new BatchSendMemberMessageRunnable(i, batchCount, i * BATCH_SIZE, BATCH_SIZE,
								msgId, batchSendMemberMessageBaseVO, blackPhoneList, filterMobileUtil,
								marketingMemberFilterService, premiumMarketingMemberFilterService, smsRecordDTOService,
								msgSendRecordService, messageQueue));
					}
				}
			}
			// 提交短信任务完成就认为是发送完成
			// 需要更新短信总记录表的发送状态
			MsgSendRecord msg = new MsgSendRecord();
			msg.setId(msgId);
			msg.setStatus(MsgType.MSG_STATUS_SENDOVER);
			this.msgSendRecordService.updateMsgRecordByMsgId(msg);
			threadpool.shutdown();
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
	 * @Description 封装总记录并保存
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

}
