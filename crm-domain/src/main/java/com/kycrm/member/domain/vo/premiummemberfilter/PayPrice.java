package com.kycrm.member.domain.vo.premiummemberfilter;

import java.math.BigDecimal;

/**
 * 付款金额
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:29:44
 * @Tags
 */
public class PayPrice extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:29:50
	 */
	private static final long serialVersionUID = 1L;

	// 最小付款金额
	private BigDecimal minPayPrice;

	// 最大付款金额
	private BigDecimal maxPayPrice;

	public PayPrice() {
		super();

	}

	public PayPrice(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public PayPrice(BigDecimal minPayPrice, BigDecimal maxPayPrice) {
		super();
		this.minPayPrice = minPayPrice;
		this.maxPayPrice = maxPayPrice;
	}

	public BigDecimal getMinPayPrice() {
		return minPayPrice;
	}

	public void setMinPayPrice(BigDecimal minPayPrice) {
		this.minPayPrice = minPayPrice;
	}

	public BigDecimal getMaxPayPrice() {
		return maxPayPrice;
	}

	public void setMaxPayPrice(BigDecimal maxPayPrice) {
		this.maxPayPrice = maxPayPrice;
	}

	@Override
	public String toString() {
		return "PayPrice [minPayPrice=" + minPayPrice + ", maxPayPrice=" + maxPayPrice + "]";
	}

}
