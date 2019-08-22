package com.kycrm.transferdata.entity;

import java.io.Serializable;
import java.util.List;

import com.kycrm.member.domain.entity.order.OrderDTO;

public class Orders implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年9月18日下午1:57:13
	 */
	private static final long serialVersionUID = 1L;

	private List<OrderDTO> order;

	public List<OrderDTO> getOrder() {
		return order;
	}

	public void setOrder(List<OrderDTO> order) {
		this.order = order;
	}

}
