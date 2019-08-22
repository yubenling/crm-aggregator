package com.kycrm.member.domain.vo.member;

import java.io.Serializable;

/**
 * 会员信息 - 客户信息详情 - 备注
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月25日下午4:41:32
 * @Tags
 */
public class MemberRemarkVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月25日下午4:41:59
	 */
	private static final long serialVersionUID = 1L;

	private Long uid;

	private String memberId;

	private String remark;

	public MemberRemarkVO() {
		super();

	}

	public MemberRemarkVO(Long uid, String memberId, String remark) {
		super();
		this.uid = uid;
		this.memberId = memberId;
		this.remark = remark;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	@Override
	public String toString() {
		return "MemberRemarkVO [uid=" + uid + ", memberId=" + memberId + ", remark=" + remark + "]";
	}

}
