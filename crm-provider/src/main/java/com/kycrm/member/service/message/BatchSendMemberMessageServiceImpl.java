package com.kycrm.member.service.message;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.component.IMessageQueue;
import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.BatchSendBaseFilterMemberMessageVO;
import com.kycrm.member.domain.vo.message.BatchSendMemberMessageBaseVO;
import com.kycrm.member.domain.vo.message.BatchSendPremiumFilterMemberMessageVO;
import com.kycrm.member.service.marketing.IMarketingMemberFilterService;
import com.kycrm.member.service.marketing.IPremiumMarketingMemberFilterService;
import com.kycrm.member.service.message.thread.BatchSendMemberMessageThread;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.util.DateUtils;
import com.kycrm.util.RedisConstant;

@MyDataSource
@Service("batchSendMemberMessageService")
public class BatchSendMemberMessageServiceImpl implements IBatchSendMemberMessageService {

	private static final Log logger = LogFactory.getLog(BatchSendMemberMessageServiceImpl.class);

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private IMarketingMemberFilterService marketingMemberFilterService;

	@Autowired
	private IPremiumMarketingMemberFilterService premiumMarketingMemberFilterService;

	@Autowired
	private ISmsSendInfoScheduleService smsSendInfoScheduleService;

	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;

	@Autowired
	private IMsgSendRecordService msgSendRecordService;

	@Autowired
	private ISmsBlackListDTOService smsBlackListDTOService;

	@Autowired
	private IMessageQueue messageQueue;

	@Override
	public void batchSendMemberMessageBaseMethod(Long uid, BatchSendMemberMessageBaseVO batchSendMemberMessageBaseVO,
			boolean isSendScheduleMessage) throws Exception {
		if (isSendScheduleMessage) {
			// 更新用户的accessToken
			String accessToken = this.cacheService.hget(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
					RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + uid);
			UserInfo user = batchSendMemberMessageBaseVO.getUser();
			user.setAccessToken(accessToken);
			batchSendMemberMessageBaseVO.setUser(user);
			Integer memberFilterType = batchSendMemberMessageBaseVO.getMemberFilterType();
			Long memberCount = null;
			if (memberFilterType == 1) {
				BatchSendBaseFilterMemberMessageVO baseFilterMemberMessageVO = (BatchSendBaseFilterMemberMessageVO) batchSendMemberMessageBaseVO;
				memberCount = this.marketingMemberFilterService.findMemberCountByCondition(uid,
						baseFilterMemberMessageVO.getMemberFilterVO());
			} else {
				BatchSendPremiumFilterMemberMessageVO premiumFilterMemberMessageVO = (BatchSendPremiumFilterMemberMessageVO) batchSendMemberMessageBaseVO;
				memberCount = this.premiumMarketingMemberFilterService.findMembersCountByCondition(uid,
						premiumFilterMemberMessageVO.getPremiumMemberFilterVO(),
						premiumFilterMemberMessageVO.getMemberFilterVO(), premiumFilterMemberMessageVO.getCompress());
			}
			batchSendMemberMessageBaseVO.setMemberCount(memberCount);
		}
		// 异步发送会员短信
		Thread thread = new Thread(
				new BatchSendMemberMessageThread(batchSendMemberMessageBaseVO, isSendScheduleMessage,
						marketingMemberFilterService, premiumMarketingMemberFilterService, smsRecordDTOService,
						msgSendRecordService, smsBlackListDTOService, smsSendInfoScheduleService, messageQueue));
		thread.start();
		if (isSendScheduleMessage) {
			logger.info("UID = " + uid + " 在 " + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
					+ " 开始发送定时短信");
		} else {
			if (batchSendMemberMessageBaseVO.getSendMsgVo().getSchedule()) {
				logger.info("UID = " + uid + " 在 " + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ " 开始存储定时短信条件");
			} else {
				logger.info("UID = " + uid + " 在 " + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
						+ " 开始发送及时短信");
			}
		}
	}

}
