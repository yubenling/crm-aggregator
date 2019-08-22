package com.kycrm.transferdata.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 旧版类名单实体类
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年9月5日上午10:53:51
 * @Tags
 */
public class SmsBlackList implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年9月5日上午10:53:06
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	// @MetaData(value ="客户昵称")
	// @Column(name="NICK")
	private String nick;

	// @MetaData(value="手机号")
	// @Column(name="PHONE")
	private String phone;

	// @MetaData(value="黑名单类型 1-手机号 2-客户昵称")
	// @Column(name="TYPE")
	private String type;

	// @MetaData(value="添加时间")
	// @Column(name="CREATEDATE")
	private Date createdate;

	// @MetaData(value="添加来源 1-黑名单导入 2- 黑名单添加 3-回复退订")
	// @Column(name="ADD_SOURCE")
	private String addSource;

	// @MetaData(value="备注")
	// @Column(name="REMARKS")
	private String remarks;

	// @MetaData(value="添加用户编号")
	// @Column(name="USERID")
	private String userId;

	// @MetaData(value="内容")
	// @Column(name="CONTENT")
	private String content;

	// @MetaData(value="是否移除黑名单 0-是(不是黑名单) 1-否(是黑名单)")
	// @Column(name="IS_DELETE")
	private String isDelete;

	public SmsBlackList() {
		super();

	}

	public SmsBlackList(Long id, String nick, String phone, String type, Date createdate, String addSource,
			String remarks, String userId, String content, String isDelete) {
		super();
		this.id = id;
		this.nick = nick;
		this.phone = phone;
		this.type = type;
		this.createdate = createdate;
		this.addSource = addSource;
		this.remarks = remarks;
		this.userId = userId;
		this.content = content;
		this.isDelete = isDelete;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getCreatedate() {
		return createdate;
	}

	public void setCreatedate(Date createdate) {
		this.createdate = createdate;
	}

	public String getAddSource() {
		return addSource;
	}

	public void setAddSource(String addSource) {
		this.addSource = addSource;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(String isDelete) {
		this.isDelete = isDelete;
	}

	@Override
	public String toString() {
		return "SmsBlacklist [id=" + id + ", nick=" + nick + ", phone=" + phone + ", type=" + type + ", createdate="
				+ createdate + ", addSource=" + addSource + ", remarks=" + remarks + ", userId=" + userId + ", content="
				+ content + ", isDelete=" + isDelete + "]";
	}

}
