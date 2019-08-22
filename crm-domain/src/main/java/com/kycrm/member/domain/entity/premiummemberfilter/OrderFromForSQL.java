package com.kycrm.member.domain.entity.premiummemberfilter;

import java.util.List;

import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterBaseVO;

/**
 * 交易来源
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午1:53:09
 * @Tags
 */
public class OrderFromForSQL extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午1:53:07
	 */
	private static final long serialVersionUID = 1L;

	private List<String> orderFromList;

	public OrderFromForSQL() {
		super();

	}

	public OrderFromForSQL(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public OrderFromForSQL(List<String> orderFromList) {
		super();
		this.orderFromList = orderFromList;
	}

	public List<String> getOrderFromList() {
		return orderFromList;
	}

	public void setOrderFromList(List<String> orderFromList) {
		this.orderFromList = orderFromList;
	}

	@Override
	public String toString() {
		return "OrderFromForSQL [orderFromList=" + orderFromList + "]";
	}

}
