package com.kycrm.member.domain.vo.trade;

import java.io.Serializable;
import java.util.Date;

public class TradeMessageVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * 主订单id
	 */
	private Long tid;

	/*
	 * 子订单id
	 */
	private Long oid;

	/*
	 * 用户主键id
	 */
	private Long uid;

	/*
	 * 卖家昵称
	 */
	private String userId;

	/**
	 * 短信总批次id
	 */
	private Long msgId;

	/*
	 * 发送内容
	 */
	private String content;

	/*
	 * 发送签名
	 */
	private String signVal;

	/*
	 * 定时发送还是立即发送0:立即发送；1:定时发送
	 */
	private String sendSchedule;

	/*
	 * 是否定时
	 */
	private Boolean schedule;

	/*
	 * 定时发送时间
	 */
	private Date sendTime;

	/*
	 * 定时发送时间字符串
	 */
	private String sendTimeStr;

	/*
	 * 查询条件key
	 */
	private String queryKey;

	/*
	 * 发送总订单数
	 */
	private Integer totalCount;

	/*
	 * 活动名称
	 */
	private String activityName;

	/*
	 * 发送类型，默认35，订单短信群发
	 */
	private String msgType = "35";

	private String autograph;

	/*
	 * 
	 */
	private String ipAddress;

	private Integer shiledDay;

	/**
	 * 订单筛选条件
	 * 
	 * @return
	 */
	private TradeVO tradeVo;

	/**
	 * 是否定时，且定时条件从数据库中取出
	 */
	private Boolean isDBQueryParam;

	private Long taoBaoShortLinkId;

	private Integer shortLinkType;

	public Boolean getIsDBQueryParam() {
		return isDBQueryParam;
	}

	public void setIsDBQueryParam(Boolean isDBQueryParam) {
		this.isDBQueryParam = isDBQueryParam;
	}

	public TradeVO getTradeVo() {
		return tradeVo;
	}

	public void setTradeVo(TradeVO tradeVo) {
		this.tradeVo = tradeVo;
	}

	public Integer getShiledDay() {
		return shiledDay;
	}

	public void setShiledDay(Integer shiledDay) {
		this.shiledDay = shiledDay;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
		this.oid = oid;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSignVal() {
		return signVal;
	}

	public void setSignVal(String signVal) {
		this.signVal = signVal;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getSendTimeStr() {
		return sendTimeStr;
	}

	public void setSendTimeStr(String sendTimeStr) {
		this.sendTimeStr = sendTimeStr;
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getSendSchedule() {
		return sendSchedule;
	}

	public void setSendSchedule(String sendSchedule) {
		this.sendSchedule = sendSchedule;
	}

	public Boolean getSchedule() {
		return schedule;
	}

	public void setSchedule(Boolean schedule) {
		this.schedule = schedule;
	}

	public String getAutograph() {
		return autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

}
