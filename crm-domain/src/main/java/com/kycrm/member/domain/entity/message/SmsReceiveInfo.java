package com.kycrm.member.domain.entity.message;


import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;



public class SmsReceiveInfo extends BaseEntity{

	/** 
	 * @Fields serialVersionUID : 买家回复内容
	 */
	private static final long serialVersionUID = 1L;

	//卖家编号
	private String userId;
	
	//买家昵称
	private String buyerNick;
	
	//发送手机号
	private String sendPhone;
	
	//内容
	private String content;
	
	//接收手机号
	private String receivePhone;
	
	//接收时间
	private Date receiveDate;
	
	//备注
	private String remarks;
	
	//是否显示， true:显示；false：不显是
	private Boolean isShow;
	
	//短信状态(0未读，1已读)
	private Integer status;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getSendPhone() {
		return sendPhone;
	}

	public void setSendPhone(String sendPhone) {
		this.sendPhone = sendPhone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getReceivePhone() {
		return receivePhone;
	}

	public void setReceivePhone(String receivePhone) {
		this.receivePhone = receivePhone;
	}

	public Date getReceiveDate() {
		return receiveDate;
	}

	public void setReceiveDate(Date receiveDate) {
		this.receiveDate = receiveDate;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	
}
