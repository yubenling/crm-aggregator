package com.kycrm.member.domain.vo.message;

import java.io.Serializable;

import com.kycrm.member.domain.entity.user.UserInfo;

public class BatchSendMemberMessageBaseVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年12月11日下午4:02:50
	 */
	private static final long serialVersionUID = 1L;

	private UserInfo user;

	private String uuid;

	private Long memberCount;

	private Long filterRecordId;

	private SendMsgVo sendMsgVo;

	private Integer memberFilterType;

	private Integer shieldDay;

	public BatchSendMemberMessageBaseVO() {
		super();

	}

	public BatchSendMemberMessageBaseVO(UserInfo user, String uuid, Long memberCount, Long filterRecordId,
			SendMsgVo sendMsgVo, Integer memberFilterType, Integer shieldDay) {
		super();
		this.user = user;
		this.uuid = uuid;
		this.memberCount = memberCount;
		this.filterRecordId = filterRecordId;
		this.sendMsgVo = sendMsgVo;
		this.memberFilterType = memberFilterType;
		this.shieldDay = shieldDay;
	}

	public UserInfo getUser() {
		return user;
	}

	public void setUser(UserInfo user) {
		this.user = user;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Long getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Long memberCount) {
		this.memberCount = memberCount;
	}

	public Long getFilterRecordId() {
		return filterRecordId;
	}

	public void setFilterRecordId(Long filterRecordId) {
		this.filterRecordId = filterRecordId;
	}

	public SendMsgVo getSendMsgVo() {
		return sendMsgVo;
	}

	public void setSendMsgVo(SendMsgVo sendMsgVo) {
		this.sendMsgVo = sendMsgVo;
	}

	public Integer getMemberFilterType() {
		return memberFilterType;
	}

	public void setMemberFilterType(Integer memberFilterType) {
		this.memberFilterType = memberFilterType;
	}

	public Integer getShieldDay() {
		return shieldDay;
	}

	public void setShieldDay(Integer shieldDay) {
		this.shieldDay = shieldDay;
	}

	@Override
	public String toString() {
		return "BatchSendMemberMessageBaseVO [user=" + user + ", uuid=" + uuid + ", memberCount=" + memberCount
				+ ", filterRecordId=" + filterRecordId + ", sendMsgVo=" + sendMsgVo + ", memberFilterType="
				+ memberFilterType + ", shieldDay=" + shieldDay + "]";
	}

}
