package com.kycrm.member.domain.vo.receive;

import java.io.Serializable;
import java.util.Date;

public class ReceiveInfoVO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 881966205246242386L;
	// 主键ID
	private Long id;
	// 卖家的id(取代之前的userId)
	private Long uid;
	// 卖家编号
	private String userId;
	// 卖家昵称
	private String taobaoNick;
	// 买家昵称
	private String buyerNick;
	// 发送手机号
	private String sendPhone;
	// 内容
	private String content;
	// 接收手机号
	private String receivePhone;
	// 接收时间
	private Date receiveDate;
	// 备注
	private String remarks;
	// 短信状态(0未读，1已读)
	private Integer status;
	// 开始时间
	private String bTime;
	// 结束时间
	private String eTime;
	// 是否包含"N",若包含,给containN赋值"N";不包含,给notContainN赋值"N"
	private String containN;
	// 是否包含"N",若包含,给containN赋值"N";不包含,给notContainN赋值"N"
	private String notContainN;
	// 内容发送者(buyer:买家;seller:卖家)
	private String role = "buyer";
	// 签名
	private String signVal;

	private Integer pageNo;
	
	/*
	 *手机号数组 
	 */
	private String[] phones;
	
	private String[] buyerNicks;

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTaobaoNick() {
		return taobaoNick;
	}

	public void setTaobaoNick(String taobaoNick) {
		this.taobaoNick = taobaoNick;
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

	public String getContainN() {
		return containN;
	}

	public void setContainN(String containN) {
		this.containN = containN;
	}

	public String getNotContainN() {
		return notContainN;
	}

	public void setNotContainN(String notContainN) {
		this.notContainN = notContainN;
	}

	public String getbTime() {
		return bTime;
	}

	public void setbTime(String bTime) {
		this.bTime = bTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getSignVal() {
		return signVal;
	}

	public void setSignVal(String signVal) {
		this.signVal = signVal;
	}

	public String[] getPhones() {
		return phones;
	}

	public void setPhones(String[] phones) {
		this.phones = phones;
	}

	public String[] getBuyerNicks() {
		return buyerNicks;
	}

	public void setBuyerNicks(String[] buyerNicks) {
		this.buyerNicks = buyerNicks;
	}


}
