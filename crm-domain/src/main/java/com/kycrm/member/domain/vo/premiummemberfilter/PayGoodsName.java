package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 付款商品名称
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午12:13:23
 * @Tags
 */
public class PayGoodsName extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午12:12:35
	 */
	private static final long serialVersionUID = 1L;

	// 第一个商品名称
	private String firstGoodsKeyCode;

	// 第二个商品名称
	private String secondGoodsKeyCode;

	//和|或
	private String andOrOrForGoods;

	public PayGoodsName() {
		super();

	}

	public PayGoodsName(String firstGoodsKeyCode, String secondGoodsKeyCode, String andOrOrForGoods) {
		super();
		this.firstGoodsKeyCode = firstGoodsKeyCode;
		this.secondGoodsKeyCode = secondGoodsKeyCode;
		this.andOrOrForGoods = andOrOrForGoods;
	}

	public String getFirstGoodsKeyCode() {
		return firstGoodsKeyCode;
	}

	public void setFirstGoodsKeyCode(String firstGoodsKeyCode) {
		this.firstGoodsKeyCode = firstGoodsKeyCode;
	}

	public String getSecondGoodsKeyCode() {
		return secondGoodsKeyCode;
	}

	public void setSecondGoodsKeyCode(String secondGoodsKeyCode) {
		this.secondGoodsKeyCode = secondGoodsKeyCode;
	}

	public String getAndOrOrForGoods() {
		return andOrOrForGoods;
	}

	public void setAndOrOrForGoods(String andOrOrForGoods) {
		this.andOrOrForGoods = andOrOrForGoods;
	}

	@Override
	public String toString() {
		return "PayGoodsName [firstGoodsKeyCode=" + firstGoodsKeyCode + ", secondGoodsKeyCode=" + secondGoodsKeyCode
				+ ", andOrOrForGoods=" + andOrOrForGoods + "]";
	}

}
