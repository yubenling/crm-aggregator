package com.kycrm.member.domain.vo.multishopbinding;

import java.io.Serializable;

public class MultiShopBindingSendMessageRecordVO extends MultiShopBindingBaseVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年3月27日下午12:20:25
	 */
	private static final long serialVersionUID = 1L;

	// 1: 绑定店铺 2: 主动申请 3: 接收申请
	private Integer menuNumber;

	private Integer pageNo;

	public MultiShopBindingSendMessageRecordVO() {
		super();
	}

	public MultiShopBindingSendMessageRecordVO(Integer menuNumber, Integer pageNo) {
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
		return "MultiShopBindingSendMessageRecordVO [menuNumber=" + menuNumber + ", pageNo=" + pageNo + "]";
	}

}
