package com.kycrm.member.domain.vo.premiummemberfilter;

import java.util.List;

/**
 * 卖家备注
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午1:55:16
 * @Tags
 */
public class SellerFlag extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午1:55:14
	 */
	private static final long serialVersionUID = 1L;

	// 卖家备注
	private List<String> sellerFlag;

	public SellerFlag() {
		super();

	}

	public SellerFlag(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public SellerFlag(List<String> sellerFlag) {
		super();
		this.sellerFlag = sellerFlag;
	}

	public List<String> getSellerFlag() {
		return sellerFlag;
	}

	public void setSellerFlag(List<String> sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	@Override
	public String toString() {
		return "SellerFlag [sellerFlag=" + sellerFlag + "]";
	}

}
