package com.kycrm.member.domain.vo.multishopbinding;

public class MultiShopBindingConfirmVO extends MultiShopBindingBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年3月28日上午10:08:05
	 */
	private static final long serialVersionUID = 1L;

	//1:接受 0:拒绝
	private Integer confirm;

	public MultiShopBindingConfirmVO() {
		super();
	}

	public MultiShopBindingConfirmVO(Long id) {
		super(id);
	}

	public MultiShopBindingConfirmVO(Integer confirm) {
		super();
		this.confirm = confirm;
	}

	public Integer getConfirm() {
		return confirm;
	}

	public void setConfirm(Integer confirm) {
		this.confirm = confirm;
	}

	@Override
	public String toString() {
		return "MultiShopBindingConfirmVO [confirm=" + confirm + "]";
	}

}
