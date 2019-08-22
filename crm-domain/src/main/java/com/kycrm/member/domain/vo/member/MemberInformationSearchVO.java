package com.kycrm.member.domain.vo.member;

public class MemberInformationSearchVO extends MemberFilterVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月21日下午3:09:58
	 */
	private static final long serialVersionUID = 1L;

	// 会员昵称
	private String buyerNick;

	// 客户来源
	private String relationSource;

	// 手机号
	private String mobile;

	// 页码
	private Integer pageNo;

	public MemberInformationSearchVO() {
		super();

	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getRelationSource() {
		return relationSource;
	}

	public void setRelationSource(String relationSource) {
		this.relationSource = relationSource;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return "MemberInformationSearchVO [buyerNick=" + buyerNick + ", relationSource=" + relationSource + ", mobile="
				+ mobile + ", pageNo=" + pageNo + "]";
	}

}
