package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 拍下未付款次数
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:27:28
 * @Tags
 */
public class BookButNonPaymentTime extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:27:26
	 */
	private static final long serialVersionUID = 1L;

	// 最小拍下未付款次数
	private Integer minBookButNonPaymentTime;

	// 最大拍下未付款次数
	private Integer maxBookButNonPaymentTime;

	public BookButNonPaymentTime() {
		super();

	}

	public BookButNonPaymentTime(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public BookButNonPaymentTime(Integer minBookButNonPaymentTime, Integer maxBookButNonPaymentTime) {
		super();
		this.minBookButNonPaymentTime = minBookButNonPaymentTime;
		this.maxBookButNonPaymentTime = maxBookButNonPaymentTime;
	}

	public Integer getMinBookButNonPaymentTime() {
		return minBookButNonPaymentTime;
	}

	public void setMinBookButNonPaymentTime(Integer minBookButNonPaymentTime) {
		this.minBookButNonPaymentTime = minBookButNonPaymentTime;
	}

	public Integer getMaxBookButNonPaymentTime() {
		return maxBookButNonPaymentTime;
	}

	public void setMaxBookButNonPaymentTime(Integer maxBookButNonPaymentTime) {
		this.maxBookButNonPaymentTime = maxBookButNonPaymentTime;
	}

	@Override
	public String toString() {
		return "BookButNonPaymentTime [minBookButNonPaymentTime=" + minBookButNonPaymentTime
				+ ", maxBookButNonPaymentTime=" + maxBookButNonPaymentTime + "]";
	}

}
