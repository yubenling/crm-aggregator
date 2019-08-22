package com.kycrm.member.domain.entity.member;

public class MemberDetailDTO extends MemberInfoDTO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年8月13日下午5:37:21
	 */
	private static final long serialVersionUID = 1L;

	private Long untradeTime;

	public MemberDetailDTO(Long untradeTime) {
		super();
		this.untradeTime = untradeTime;
	}

	public MemberDetailDTO() {
		super();

	}

	public Long getUntradeTime() {
		return untradeTime;
	}

	public void setUntradeTime(Long untradeTime) {
		this.untradeTime = untradeTime;
	}

	@Override
	public String toString() {
		return "MemberDetailDTO [untradeTime=" + untradeTime + "]";
	}

}
