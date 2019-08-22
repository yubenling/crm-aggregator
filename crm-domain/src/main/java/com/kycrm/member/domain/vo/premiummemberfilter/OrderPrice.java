package com.kycrm.member.domain.vo.premiummemberfilter;

import java.math.BigDecimal;

/**
 * 订单金额
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午1:39:46
 * @Tags
 */
public class OrderPrice extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午1:39:43
	 */
	private static final long serialVersionUID = 1L;

	// 最小订单金额
	private BigDecimal minOrderPrice;

	// 最大订单金额
	private BigDecimal maxOrderPrice;

	public OrderPrice() {
		super();

	}

	public OrderPrice(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public OrderPrice(BigDecimal minOrderPrice, BigDecimal maxOrderPrice) {
		super();
		this.minOrderPrice = minOrderPrice;
		this.maxOrderPrice = maxOrderPrice;
	}

	public BigDecimal getMinOrderPrice() {
		return minOrderPrice;
	}

	public void setMinOrderPrice(BigDecimal minOrderPrice) {
		this.minOrderPrice = minOrderPrice;
	}

	public BigDecimal getMaxOrderPrice() {
		return maxOrderPrice;
	}

	public void setMaxOrderPrice(BigDecimal maxOrderPrice) {
		this.maxOrderPrice = maxOrderPrice;
	}

	@Override
	public String toString() {
		return "OrderPrice [minOrderPrice=" + minOrderPrice + ", maxOrderPrice=" + maxOrderPrice + "]";
	}

}
