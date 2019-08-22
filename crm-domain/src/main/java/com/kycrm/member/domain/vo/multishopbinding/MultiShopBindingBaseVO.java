package com.kycrm.member.domain.vo.multishopbinding;

import java.io.Serializable;

public class MultiShopBindingBaseVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年3月28日上午10:05:20
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	public MultiShopBindingBaseVO() {
		super();
	}

	public MultiShopBindingBaseVO(Long id) {
		super();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "MultiShopBindingBaseVO [id=" + id + "]";
	}

}
