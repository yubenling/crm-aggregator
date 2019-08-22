package com.kycrm.member.domain.vo.message;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class AppointNumberSendVO implements Serializable {
	private static final long serialVersionUID = -7063171873051693921L;

	private Long uid;// 用户info表id
	private Integer sendMode = 1;// 发送方式 1--手动输入号码,2---批量上传号码
	private List<String> phones;// 手机号码
	private Long phonesId; // ---批量上传号码的记录id
	private String activityName;// 活动名称
	private String autograph;// 短信签名
	private String content;// 短信内容
	private Integer sendDay;// 屏蔽后设置的天数
	private Integer sendTimeType = 1;// 发送类型 1---立即发送 2---定时发送
	private Date sendTime;// 定时发送的时间
	private Long taoBaoShortLinkId;
	private Integer shortLinkType;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Integer getSendMode() {
		return sendMode;
	}

	public void setSendMode(Integer sendMode) {
		this.sendMode = sendMode;
	}

	public List<String> getPhones() {
		return phones;
	}

	public void setPhones(List<String> phones) {
		this.phones = phones;
	}

	public Long getPhonesId() {
		return phonesId;
	}

	public void setPhonesId(Long phonesId) {
		this.phonesId = phonesId;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getAutograph() {
		return autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getSendDay() {
		return sendDay;
	}

	public void setSendDay(Integer sendDay) {
		this.sendDay = sendDay;
	}

	public Integer getSendTimeType() {
		return sendTimeType;
	}

	public void setSendTimeType(Integer sendTimeType) {
		this.sendTimeType = sendTimeType;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
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
