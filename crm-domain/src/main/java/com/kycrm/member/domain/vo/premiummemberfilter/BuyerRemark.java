package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 买家评价
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午1:59:41
 * @Tags
 */
public class BuyerRemark extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午1:59:48
	 */
	private static final long serialVersionUID = 1L;

	// 好评
	private boolean good;

	// 中评
	private boolean neutral;

	// 差评
	private boolean bad;

	public BuyerRemark() {
		super();

	}

	public BuyerRemark(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public BuyerRemark(boolean good, boolean neutral, boolean bad) {
		super();
		this.good = good;
		this.neutral = neutral;
		this.bad = bad;
	}

	public boolean isGood() {
		return good;
	}

	public void setGood(boolean good) {
		this.good = good;
	}

	public boolean isNeutral() {
		return neutral;
	}

	public void setNeutral(boolean neutral) {
		this.neutral = neutral;
	}

	public boolean isBad() {
		return bad;
	}

	public void setBad(boolean bad) {
		this.bad = bad;
	}

	@Override
	public String toString() {
		return "BuyerRemark [good=" + good + ", neutral=" + neutral + ", bad=" + bad + "]";
	}

}
