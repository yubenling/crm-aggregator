package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 购买商品次数
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:18:11
 * @Tags
 */
public class BuyGoodsTime extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:18:18
	 */
	private static final long serialVersionUID = 1L;

	// 最小购买商品次数
	private Integer minBuyGoodsTime;

	// 最大购买商品次数
	private Integer maxBuyGoodsTime;

	public BuyGoodsTime() {
		super();

	}

	public BuyGoodsTime(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public BuyGoodsTime(Integer minBuyGoodsTime, Integer maxBuyGoodsTime) {
		super();
		this.minBuyGoodsTime = minBuyGoodsTime;
		this.maxBuyGoodsTime = maxBuyGoodsTime;
	}

	public Integer getMinBuyGoodsTime() {
		return minBuyGoodsTime;
	}

	public void setMinBuyGoodsTime(Integer minBuyGoodsTime) {
		this.minBuyGoodsTime = minBuyGoodsTime;
	}

	public Integer getMaxBuyGoodsTime() {
		return maxBuyGoodsTime;
	}

	public void setMaxBuyGoodsTime(Integer maxBuyGoodsTime) {
		this.maxBuyGoodsTime = maxBuyGoodsTime;
	}

	@Override
	public String toString() {
		return "BuyGoodsTime [minBuyGoodsTime=" + minBuyGoodsTime + ", maxBuyGoodsTime=" + maxBuyGoodsTime + "]";
	}

}
