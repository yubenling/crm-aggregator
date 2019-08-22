package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 拍下次数
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:23:28
 * @Tags
 */
public class BookGoodsTime extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:23:36
	 */
	private static final long serialVersionUID = 1L;

	// 最小拍下次数
	private Integer minBookGoodsTime;

	// 最大拍下次数
	private Integer maxBookGoodsTime;

	public BookGoodsTime() {
		super();

	}

	public BookGoodsTime(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public BookGoodsTime(Integer minBookGoodsTime, Integer maxBookGoodsTime) {
		super();
		this.minBookGoodsTime = minBookGoodsTime;
		this.maxBookGoodsTime = maxBookGoodsTime;
	}

	public Integer getMinBookGoodsTime() {
		return minBookGoodsTime;
	}

	public void setMinBookGoodsTime(Integer minBookGoodsTime) {
		this.minBookGoodsTime = minBookGoodsTime;
	}

	public Integer getMaxBookGoodsTime() {
		return maxBookGoodsTime;
	}

	public void setMaxBookGoodsTime(Integer maxBookGoodsTime) {
		this.maxBookGoodsTime = maxBookGoodsTime;
	}

	@Override
	public String toString() {
		return "BookGoodsTime [minBookGoodsTime=" + minBookGoodsTime + ", maxBookGoodsTime=" + maxBookGoodsTime + "]";
	}

}
