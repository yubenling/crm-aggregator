package com.kycrm.member.domain.vo.multishopbinding;

public class MultiShopBindingReleaseBindingVO extends MultiShopBindingBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年3月28日上午10:17:11
	 */
	private static final long serialVersionUID = 1L;

	private Integer bindingStatus;

	public MultiShopBindingReleaseBindingVO() {
		super();
	}

	public MultiShopBindingReleaseBindingVO(Long id) {
		super(id);
	}

	public MultiShopBindingReleaseBindingVO(Integer bindingStatus) {
		super();
		this.bindingStatus = bindingStatus;
	}

	public Integer getBindingStatus() {
		return bindingStatus;
	}

	public void setBindingStatus(Integer bindingStatus) {
		this.bindingStatus = bindingStatus;
	}

	@Override
	public String toString() {
		return "MultiShopBindingReleaseBindingVO [bindingStatus=" + bindingStatus + "]";
	}

}
