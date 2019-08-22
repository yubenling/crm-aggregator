package com.kycrm.member.domain.vo.premiummemberfilter;

import java.util.List;

/**
 * 收货地区
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午1:41:46
 * @Tags
 */
public class ReceiveGoodsArea extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午1:42:11
	 */
	private static final long serialVersionUID = 1L;

	// 收货省份
	private List<String> receiveGoodsProvince;

	// 收货城市
	private List<String> receiveGoodsCity;

	public ReceiveGoodsArea() {
		super();

	}

	public ReceiveGoodsArea(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public ReceiveGoodsArea(List<String> receiveGoodsProvince, List<String> receiveGoodsCity) {
		super();
		this.receiveGoodsProvince = receiveGoodsProvince;
		this.receiveGoodsCity = receiveGoodsCity;
	}

	public List<String> getReceiveGoodsProvince() {
		return receiveGoodsProvince;
	}

	public void setReceiveGoodsProvince(List<String> receiveGoodsProvince) {
		this.receiveGoodsProvince = receiveGoodsProvince;
	}

	public List<String> getReceiveGoodsCity() {
		return receiveGoodsCity;
	}

	public void setReceiveGoodsCity(List<String> receiveGoodsCity) {
		this.receiveGoodsCity = receiveGoodsCity;
	}

	@Override
	public String toString() {
		return "ReceiveGoodsArea [receiveGoodsProvince=" + receiveGoodsProvince + ", receiveGoodsCity="
				+ receiveGoodsCity + "]";
	}

}
