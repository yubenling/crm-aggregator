package com.kycrm.member.domain.vo.message;

/** 
 * Project Name:s2jh4net 
 * File Name:SendMsgVo.java 
 * Package Name:s2jh.biz.shop.crm.manage.entity 
 * Date:2017年5月27日下午8:19:30 
 * Copyright (c) 2017,  All Rights Reserved. 
 * author zlp
*/
import java.io.Serializable;

/**
 * ClassName:SendMsgVo <br/>
 * Date: 2017年5月27日 下午8:19:30 <br/>
 * 
 * @author zlp
 * @version 1.0
 */
public class SendMsgVo implements Serializable {

	private static final long serialVersionUID = -6652268350905001750L;

	// 下面这些参数，都可以在会员群发页面得到
	private Long uid; // 我加了一个uid
	private String content; // {内容，前台获取}
	private String msgType; // {短信类型，静态设置，是会员群发短信，还是什么短信}
	private String autograph; // {签名，也是太传递过来}
	private String userId; // userid
							// {买家用户名，request中可以获取(会员短信群发:验证用户短信条数与用户余额)的使用使用}
	private String ipAddress; // {ip地址，前台获取}
	private String activityName; // {活动名，前台传递过来}
	private String sendTime; // {发送时间} 如果是定时发送，才会有这个值
	private String sendType; // 是定时发送，还是立即发送 2代表的应该是定时发送 1代表立即发送
	private Long totalCount; // 会员的数量
	private long msgId; // {这一批短信保存在总记录表的id}
	private Long taoBaoShortLinkId;
	private Integer shortLinkType;// 短链类型 1：淘宝短链 2：客云短链

	// 通过该字段区分是定时发送还是立即发送
	// 使用整型,在发送时候必须赋值
	// true:定时发送,false:立即发送
	private Boolean schedule;

	private Integer memberFilterType;// 会员筛选类型【1：基础会员筛选；2：高级会员筛选】
	private String queryKey;// 这个值用户取出redis中的会员筛选key

	// 已发送过滤
	private String sentFilter;

	// 订单短信群发参数
	private String smsTempId;
	private String send_time_type;
	private String type;
	private String unsubscribeMSGVal;
	private String signVal;

	public SendMsgVo() {
		super();

	}

	public SendMsgVo(Long uid, String content, String msgType, String autograph, String userId, String ipAddress,
			String activityName, String sendTime, String sendType, Long totalCount, long msgId, Long taoBaoShortLinkId,
			Integer shortLinkType, Boolean schedule, Integer memberFilterType, String queryKey, String sentFilter,
			String smsTempId, String send_time_type, String type, String unsubscribeMSGVal, String signVal) {
		super();
		this.uid = uid;
		this.content = content;
		this.msgType = msgType;
		this.autograph = autograph;
		this.userId = userId;
		this.ipAddress = ipAddress;
		this.activityName = activityName;
		this.sendTime = sendTime;
		this.sendType = sendType;
		this.totalCount = totalCount;
		this.msgId = msgId;
		this.taoBaoShortLinkId = taoBaoShortLinkId;
		this.shortLinkType = shortLinkType;
		this.schedule = schedule;
		this.memberFilterType = memberFilterType;
		this.queryKey = queryKey;
		this.sentFilter = sentFilter;
		this.smsTempId = smsTempId;
		this.send_time_type = send_time_type;
		this.type = type;
		this.unsubscribeMSGVal = unsubscribeMSGVal;
		this.signVal = signVal;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public String getAutograph() {
		return autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getSendTime() {
		return sendTime;
	}

	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendType() {
		return sendType;
	}

	public void setSendType(String sendType) {
		this.sendType = sendType;
	}

	public Long getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}

	public long getMsgId() {
		return msgId;
	}

	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}

	public Long getTaoBaoShortLinkId() {
		return taoBaoShortLinkId;
	}

	public void setTaoBaoShortLinkId(Long taoBaoShortLinkId) {
		this.taoBaoShortLinkId = taoBaoShortLinkId;
	}

	public Integer getShortLinkType() {
		return shortLinkType;
	}

	public void setShortLinkType(Integer shortLinkType) {
		this.shortLinkType = shortLinkType;
	}

	public Boolean getSchedule() {
		return schedule;
	}

	public void setSchedule(Boolean schedule) {
		this.schedule = schedule;
	}

	public Integer getMemberFilterType() {
		return memberFilterType;
	}

	public void setMemberFilterType(Integer memberFilterType) {
		this.memberFilterType = memberFilterType;
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public String getSentFilter() {
		return sentFilter;
	}

	public void setSentFilter(String sentFilter) {
		this.sentFilter = sentFilter;
	}

	public String getSmsTempId() {
		return smsTempId;
	}

	public void setSmsTempId(String smsTempId) {
		this.smsTempId = smsTempId;
	}

	public String getSend_time_type() {
		return send_time_type;
	}

	public void setSend_time_type(String send_time_type) {
		this.send_time_type = send_time_type;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUnsubscribeMSGVal() {
		return unsubscribeMSGVal;
	}

	public void setUnsubscribeMSGVal(String unsubscribeMSGVal) {
		this.unsubscribeMSGVal = unsubscribeMSGVal;
	}

	public String getSignVal() {
		return signVal;
	}

	public void setSignVal(String signVal) {
		this.signVal = signVal;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		return "SendMsgVo [uid=" + uid + ", content=" + content + ", msgType=" + msgType + ", autograph=" + autograph
				+ ", userId=" + userId + ", ipAddress=" + ipAddress + ", activityName=" + activityName + ", sendTime="
				+ sendTime + ", sendType=" + sendType + ", totalCount=" + totalCount + ", msgId=" + msgId
				+ ", taoBaoShortLinkId=" + taoBaoShortLinkId + ", shortLinkType=" + shortLinkType + ", schedule="
				+ schedule + ", memberFilterType=" + memberFilterType + ", queryKey=" + queryKey + ", sentFilter="
				+ sentFilter + ", smsTempId=" + smsTempId + ", send_time_type=" + send_time_type + ", type=" + type
				+ ", unsubscribeMSGVal=" + unsubscribeMSGVal + ", signVal=" + signVal + "]";
	}

}
