package com.kycrm.member.domain.entity.member;

public class MemberDisplayDTO extends MemberInfoDTO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月28日下午2:02:05
	 */
	private static final long serialVersionUID = 1L;

	private Integer tradeSource;

	public MemberDisplayDTO() {
		super();

	}

	public MemberDisplayDTO(Integer tradeSource) {
		super();
		this.tradeSource = tradeSource;
	}

	public Integer getTradeSource() {
		return tradeSource;
	}

	public void setTradeSource(Integer tradeSource) {
		this.tradeSource = tradeSource;
	}

	@Override
	public String toString() {
		return "MemberDisplayDTO [tradeSource=" + tradeSource + "]";
	}

}
