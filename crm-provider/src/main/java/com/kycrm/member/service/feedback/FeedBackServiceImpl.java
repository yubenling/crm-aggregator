package com.kycrm.member.service.feedback;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.core.annotation.MyDataSource;
import com.kycrm.member.core.annotation.MyDataSourceAspect;
import com.kycrm.member.dao.feedback.IFeedBackDao;
import com.kycrm.member.domain.entity.feedback.FeedBack;

@Service("feedBackService")
@MyDataSource(MyDataSourceAspect.MASTER)
public class FeedBackServiceImpl implements IFeedBackService  {

	
	@Autowired
	private IFeedBackDao feedBackDao;


	/**
	 * 客户反馈信息保存
	 * @author HL
	 * @param orderCenterSetup
	 * @return
	 */
	public Long insertFeedBack(FeedBack feedBack){
		
		return feedBackDao.insertFeedBack(feedBack);
	}
}
