package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 付款商品数量
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午1:34:05
 * @Tags
 */
public class PayGoodsCount extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午1:32:38
	 */
	private static final long serialVersionUID = 1L;

	// 最小付款商品数量
	private Integer minPayGoodsCount;

	// 最大付款商品数量
	private Integer maxPayGoodsCount;

	public PayGoodsCount(Integer minPayGoodsCount, Integer maxPayGoodsCount) {
		super();
		this.minPayGoodsCount = minPayGoodsCount;
		this.maxPayGoodsCount = maxPayGoodsCount;
	};

	public PayGoodsCount() {
		super();

	}

	public PayGoodsCount(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public Integer getMinPayGoodsCount() {
		return minPayGoodsCount;
	}

	public void setMinPayGoodsCount(Integer minPayGoodsCount) {
		this.minPayGoodsCount = minPayGoodsCount;
	}

	public Integer getMaxPayGoodsCount() {
		return maxPayGoodsCount;
	}

	public void setMaxPayGoodsCount(Integer maxPayGoodsCount) {
		this.maxPayGoodsCount = maxPayGoodsCount;
	}

	@Override
	public String toString() {
		return "PayGoodsCount [minPayGoodsCount=" + minPayGoodsCount + ", maxPayGoodsCount=" + maxPayGoodsCount + "]";
	}

}
