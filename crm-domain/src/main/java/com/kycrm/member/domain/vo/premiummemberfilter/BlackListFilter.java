package com.kycrm.member.domain.vo.premiummemberfilter;

import java.io.Serializable;

/**
 * 过滤黑名单
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:42:30
 * @Tags
 */
public class BlackListFilter implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:43:00
	 */
	private static final long serialVersionUID = 1L;

	// 过滤黑名单
	private String memberStatus;

	public BlackListFilter() {
		super();

	}

	public BlackListFilter(String memberStatus) {
		super();
		this.memberStatus = memberStatus;
	}

	public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

	@Override
	public String toString() {
		return "BlackListFilter [memberStatus=" + memberStatus + "]";
	}

}
