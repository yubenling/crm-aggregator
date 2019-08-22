package com.kycrm.member.dao.feedback;

import com.kycrm.member.domain.entity.feedback.FeedBack;

public interface IFeedBackDao{

	/**
	 * 客户反馈信息保存
	 * @author HL
	 * @param orderCenterSetup
	 * @return
	 */
	public Long insertFeedBack(FeedBack feedBack);
}
