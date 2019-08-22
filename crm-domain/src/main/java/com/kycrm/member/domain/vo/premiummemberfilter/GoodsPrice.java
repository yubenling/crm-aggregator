package com.kycrm.member.domain.vo.premiummemberfilter;

import java.math.BigDecimal;

/**
 * 商品价格
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午1:37:08
 * @Tags
 */
public class GoodsPrice extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午1:37:05
	 */
	private static final long serialVersionUID = 1L;

	// 最小商品价格
	private BigDecimal minGoodsPrice;

	// 最大商品价格
	private BigDecimal maxGoodsPrice;

	public GoodsPrice(BigDecimal minGoodsPrice, BigDecimal maxGoodsPrice) {
		super();
		this.minGoodsPrice = minGoodsPrice;
		this.maxGoodsPrice = maxGoodsPrice;
	}

	public GoodsPrice() {
		super();

	}

	public GoodsPrice(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public BigDecimal getMinGoodsPrice() {
		return minGoodsPrice;
	}

	public void setMinGoodsPrice(BigDecimal minGoodsPrice) {
		this.minGoodsPrice = minGoodsPrice;
	}

	public BigDecimal getMaxGoodsPrice() {
		return maxGoodsPrice;
	}

	public void setMaxGoodsPrice(BigDecimal maxGoodsPrice) {
		this.maxGoodsPrice = maxGoodsPrice;
	}

	@Override
	public String toString() {
		return "GoodsPrice [minGoodsPrice=" + minGoodsPrice + ", maxGoodsPrice=" + maxGoodsPrice + "]";
	}

}
