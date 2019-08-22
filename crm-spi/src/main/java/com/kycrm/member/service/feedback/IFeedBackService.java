package com.kycrm.member.service.feedback;

import com.kycrm.member.domain.entity.feedback.FeedBack;

public interface IFeedBackService  {


	/**
	 * 客户反馈信息保存
	 * @author HL
	 * @param orderCenterSetup
	 * @return
	 */
	public Long insertFeedBack(FeedBack feedBack);
}
