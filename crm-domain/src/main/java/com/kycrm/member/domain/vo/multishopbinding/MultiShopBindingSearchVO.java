package com.kycrm.member.domain.vo.multishopbinding;

import java.io.Serializable;

public class MultiShopBindingSearchVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年3月26日下午5:56:03
	 */
	private static final long serialVersionUID = 1L;

	// 1: 绑定店铺 2: 主动申请 3: 接收申请
	private Integer menuNumber;

	private Integer pageNo;

	public MultiShopBindingSearchVO() {
		super();
	}

	public MultiShopBindingSearchVO(Integer menuNumber, Integer pageNo) {
		super();
		this.menuNumber = menuNumber;
		this.pageNo = pageNo;
	}

	public Integer getMenuNumber() {
		return menuNumber;
	}

	public void setMenuNumber(Integer menuNumber) {
		this.menuNumber = menuNumber;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return "MultiShopBindingSearchVO [menuNumber=" + menuNumber + ", pageNo=" + pageNo + "]";
	}

}
