package com.kycrm.member.domain.entity.message;

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * @Table(name = "CRM_SMS_SEND_INFO")
 * @MetaData (value = "短信发送详情临时表")
 */
public class SmsSendInfo extends BaseEntity {
	private static final long serialVersionUID = 6799034695519339104L;

	/**
	 * @MetaData (value="用户ID")
	 * @Column (name="user_id",nullable = false)
	 */
	private String userId;

	/**
	 * @MetaData (value="接收人手机号")
	 * @Column (name="PHONE")x
	 */
	private String phone;

	/**
	 * @MetaData (value="接收人昵称")
	 * @Column (name="NICKNAME")
	 */
	private String nickname;

	/**
	 * @MetaData (value="短息内容")
	 * @Column (name="CONTENT")
	 */
	private String content;

	/**
	 * @MetaData (value="短信类型")
	 * @Column (name="TYPE")
	 */
	private String type;

	/**
	 * @MetaData (value="发送时间")
	 * @Column (name="send_time")
	 */
	private Date sendTime;

	/*
	 * 发送成功--1 手机号码不正确--2 短信余额不足--3 重复被屏蔽--4 黑名单--5 等待被发送--6 初次短信判断标记--99
	 * 同一买家一天只催付一次--7 同一买家已付款1小时不催--8 屏蔽黑名单用户--9 未设置短信提示语--10
	 */
	/**
	 * @MetaData (value="短信发送状态")
	 * @Column (name="status")
	 */
	private Integer status;

	/**
	 * @MetaData (value="实际扣除短信条数")
	 * @Column (name="actual_deduction")
	 */
	private Integer actualDeduction;

	/**
	 * @MetaData (value="短信渠道")
	 * @Column (name="channel")
	 */
	private String channel;

	/**
	 * @MetaData (value="短信定时开始时间比如10:00，12:25，不可以为空")
	 * @Column (name="startSend")
	 */
	private Date startSend;

	/**
	 * @MetaData (value="短信定时结束小时，如果发送时已经超出了结束延后次日发送，可以为空")
	 * @Column (name="endSend")
	 */
	private Date endSend;

	/**
	 * @MetaData (value="超时次日发送短信")
	 * @Column (name="delayDate")
	 */
	private Boolean delayDate;

	/**
	 * @MetaData (value="短信被定时执行次数，反复延期不能超过五次")
	 * @Column (name="flag")
	 */
	private int flag;

	/**
	 * @MetaData (value="要发送的短信主订单号")
	 * @Column (name="tid")
	 */
	private Long tid;

	/**
	 * @MetaData (value="总记录id")
	 * @Column (name="msg_id")
	 */
	private Long msgId;

	/**
	 * @MetaData (value="子订单号，自动评价内容专属")
	 * @Column (name="oid")
	 */
	private Long oid;

	/**
	 * @MetaData (value="评价类型，自动评价内容专属")
	 * @Column (name="rate_type")
	 */
	private String rateType;

	/**
	 * true:过滤 false:不过滤
	 */
	/**
	 * @MetaData (value="同一买家一天只提醒一次")
	 * @Column (name="filter_once")
	 */
	private Boolean filterOnce;

	/**
	 * 只针对催付有效 true:过滤 false:不过滤
	 * 
	 * @MetaData (value="同一买家有付过款的不发送")
	 * @Column (name="filter_hassent")
	 */
	private Boolean filterHassent;

	/**
	 * @MetaData (value="中差评监控手机号码")
	 * @Column (name="inform_Mobile")
	 */
	private String informMobile;

	/**
	 * @MetaData (value="任务名称")
	 * @Column (name="taskId")
	 */
	private Long taskId;

	/**
	 * @MetaData (value="屏蔽天数")
	 * @Column (name="shield_day")
	 */
	private Integer shieldDay;

	/**
	 * @MetaData(value="筛选类型【1:基础筛选;2:高级筛选】")
	 * @Column(name="member_filter_type")
	 */
	private Integer memberFilterType;

	/**
	 * @MetaData(value="筛选条件JSON格式")
	 * @Column(name="member_filter_condition")
	 */
	private String memberFilterCondition;

	public Integer getShieldDay() {
		return shieldDay;
	}

	public void setShieldDay(Integer shieldDay) {
		this.shieldDay = shieldDay;
	}

	/* 测试发送短信时使用 */
	private Boolean isVip;

	private Long shortLinkId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getActualDeduction() {
		return actualDeduction;
	}

	public void setActualDeduction(Integer actualDeduction) {
		this.actualDeduction = actualDeduction;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Date getStartSend() {
		return startSend;
	}

	public void setStartSend(Date startSend) {
		this.startSend = startSend;
	}

	public Date getEndSend() {
		return endSend;
	}

	public void setEndSend(Date endSend) {
		this.endSend = endSend;
	}

	public Boolean getDelayDate() {
		return delayDate;
	}

	public void setDelayDate(Boolean delayDate) {
		this.delayDate = delayDate;
	}

	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public String getRateType() {
		return rateType;
	}

	public void setRateType(String rateType) {
		this.rateType = rateType;
	}

	public Boolean getFilterOnce() {
		return filterOnce;
	}

	public void setFilterOnce(Boolean filterOnce) {
		this.filterOnce = filterOnce;
	}

	public Boolean getFilterHassent() {
		return filterHassent;
	}

	public void setFilterHassent(Boolean filterHassent) {
		this.filterHassent = filterHassent;
	}

	public String getInformMobile() {
		return informMobile;
	}

	public void setInformMobile(String informMobile) {
		this.informMobile = informMobile;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Boolean getIsVip() {
		return isVip;
	}

	public void setIsVip(Boolean isVip) {
		this.isVip = isVip;
	}

	public Long getShortLinkId() {
		return shortLinkId;
	}

	public void setShortLinkId(Long shortLinkId) {
		this.shortLinkId = shortLinkId;
	}

	public Integer getMemberFilterType() {
		return memberFilterType;
	}

	public void setMemberFilterType(Integer memberFilterType) {
		this.memberFilterType = memberFilterType;
	}

	public String getMemberFilterCondition() {
		return memberFilterCondition;
	}

	public void setMemberFilterCondition(String memberFilterCondition) {
		this.memberFilterCondition = memberFilterCondition;
	}

	@Override
	public String toString() {
		return "SmsSendInfo [userId=" + userId + ", phone=" + phone + ", nickname=" + nickname + ", content=" + content
				+ ", type=" + type + ", sendTime=" + sendTime + ", status=" + status + ", actualDeduction="
				+ actualDeduction + ", channel=" + channel + ", startSend=" + startSend + ", endSend=" + endSend
				+ ", delayDate=" + delayDate + ", flag=" + flag + ", tid=" + tid + ", msgId=" + msgId + ", oid=" + oid
				+ ", rateType=" + rateType + ", filterOnce=" + filterOnce + ", filterHassent=" + filterHassent
				+ ", informMobile=" + informMobile + ", taskId=" + taskId + ", shieldDay=" + shieldDay
				+ ", memberFilterType=" + memberFilterType + ", memberFilterCondition=" + memberFilterCondition
				+ ", isVip=" + isVip + ", shortLinkId=" + shortLinkId + "]";
	}

}
