package com.kycrm.member.domain.vo.premiummemberfilter;

import java.math.BigDecimal;

/**
 * 拍下金额
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:25:11
 * @Tags
 */
public class BookPrice extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:25:15
	 */
	private static final long serialVersionUID = 1L;

	// 最小拍下金额
	private BigDecimal minBookPrice;

	// 最大拍下金额
	private BigDecimal maxBookPrice;

	public BookPrice(BigDecimal minBookPrice, BigDecimal maxBookPrice) {
		super();
		this.minBookPrice = minBookPrice;
		this.maxBookPrice = maxBookPrice;
	}

	public BookPrice() {
		super();

	}

	public BookPrice(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public BigDecimal getMinBookPrice() {
		return minBookPrice;
	}

	public void setMinBookPrice(BigDecimal minBookPrice) {
		this.minBookPrice = minBookPrice;
	}

	public BigDecimal getMaxBookPrice() {
		return maxBookPrice;
	}

	public void setMaxBookPrice(BigDecimal maxBookPrice) {
		this.maxBookPrice = maxBookPrice;
	}

	@Override
	public String toString() {
		return "BookPrice [minBookPrice=" + minBookPrice + ", maxBookPrice=" + maxBookPrice + "]";
	}

}
