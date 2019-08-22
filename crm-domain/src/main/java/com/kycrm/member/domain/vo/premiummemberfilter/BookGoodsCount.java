package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 拍下商品数量
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午1:36:29
 * @Tags
 */
public class BookGoodsCount extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午1:35:53
	 */
	private static final long serialVersionUID = 1L;

	// 最小拍下商品数量
	private Integer minBookGoodsCount;

	// 最大拍下商品数量
	private Integer maxBookGoodsCount;

	public BookGoodsCount() {
		super();

	}

	public BookGoodsCount(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public BookGoodsCount(Integer minBookGoodsCount, Integer maxBookGoodsCount) {
		super();
		this.minBookGoodsCount = minBookGoodsCount;
		this.maxBookGoodsCount = maxBookGoodsCount;
	}

	public Integer getMinBookGoodsCount() {
		return minBookGoodsCount;
	}

	public void setMinBookGoodsCount(Integer minBookGoodsCount) {
		this.minBookGoodsCount = minBookGoodsCount;
	}

	public Integer getMaxBookGoodsCount() {
		return maxBookGoodsCount;
	}

	public void setMaxBookGoodsCount(Integer maxBookGoodsCount) {
		this.maxBookGoodsCount = maxBookGoodsCount;
	}

	@Override
	public String toString() {
		return "BookGoodsCount [minBookGoodsCount=" + minBookGoodsCount + ", maxBookGoodsCount=" + maxBookGoodsCount
				+ "]";
	}

}
