package com.kycrm.member.domain.vo.member;

import java.io.Serializable;
import java.util.Date;

public class SmsReceiveVO implements Serializable{



	/**
	 * 
	 */
	private static final long serialVersionUID = 4427139306368278345L;

	/*
	 * 内容发送者(buyer:买家;seller:卖家)
	 */
	private String role = "buyer";
	
	/*
	 * userId
	 */
	private String userId;
	
	/*
	 * 买家昵称
	 */
	private String buyerNick;
	
	/*
	 * 发送短信时间
	 */
	private Date sendTime;
	
	/*
	 * 短信内容
	 */
	private String content;
	
	/*
	 * 签名
	 */
	private String signVal;
	
	/*
	 * 模板id
	 */
	private String smsTempId;
	
	/*
	 * 手机号
	 */
	private String phone;

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSignVal() {
		return signVal;
	}

	public void setSignVal(String signVal) {
		this.signVal = signVal;
	}

	public String getSmsTempId() {
		return smsTempId;
	}

	public void setSmsTempId(String smsTempId) {
		this.smsTempId = smsTempId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	

}
