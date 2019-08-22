package com.kycrm.member.domain.entity.feedback;

import com.kycrm.member.domain.entity.base.BaseEntity;


/**@Table(name = "CRM_FEEDBACK")
 * @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
 */
public class FeedBack extends BaseEntity{
private static final long serialVersionUID = 4386721832098019347L;
	

	/**
	 * @MetaData(value="卖家用户Id")
	 * @Column(name="USER_ID")
	 */
	private String userId;
	
	/** 
	 * @MetaData(value="反馈内容")
	 * @Column(name="FEEDBACK_CONTENT")
	 */
	private String feedbackContent;
	
	/**
	 * @MetaData(value="反馈图片")
	 * @Column(name="FEEDBACK_IMAGE")
	 */
	private String feedbackImage;
	
	/**
	 * @MetaData(value="联系方式")
	 * @Column(name="CONTACT_MODE")
	 */
	private String contactMode;

	/**
	 * @MetaData(value="是否阅读反馈 true ,false")
	 * @Column(name="FEEDBACK_READ")
	 */
	private Boolean feedbackRead;


	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFeedbackContent() {
		return feedbackContent;
	}

	public void setFeedbackContent(String feedbackContent) {
		this.feedbackContent = feedbackContent;
	}

	public String getFeedbackImage() {
		return feedbackImage;
	}

	public void setFeedbackImage(String feedbackImage) {
		this.feedbackImage = feedbackImage;
	}

	public String getContactMode() {
		return contactMode;
	}

	public void setContactMode(String contactMode) {
		this.contactMode = contactMode;
	}

	public Boolean getFeedbackRead() {
		return feedbackRead;
	}

	public void setFeedbackRead(Boolean feedbackRead) {
		this.feedbackRead = feedbackRead;
	}
}
