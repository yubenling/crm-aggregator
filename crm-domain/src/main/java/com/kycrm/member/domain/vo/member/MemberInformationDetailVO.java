package com.kycrm.member.domain.vo.member;

/**
 * 客户信息 - 客户信息详情
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月25日下午2:58:16
 * @Tags
 */
public class MemberInformationDetailVO extends MemberFilterVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月25日下午2:56:54
	 */
	private static final long serialVersionUID = 1L;

	private String memberId;

	private String buyerNick;

	public MemberInformationDetailVO() {
		super();

	}

	public MemberInformationDetailVO(String memberId, String buyerNick) {
		super();
		this.memberId = memberId;
		this.buyerNick = buyerNick;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	@Override
	public String toString() {
		return "MemberInformationDetailVO [memberId=" + memberId + ", buyerNick=" + buyerNick + "]";
	}

}
