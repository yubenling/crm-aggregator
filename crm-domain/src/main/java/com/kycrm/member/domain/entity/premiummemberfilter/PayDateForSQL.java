package com.kycrm.member.domain.entity.premiummemberfilter;

import java.util.Date;

import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterBaseVO;

/**
 * 付款时间
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午12:03:53
 * @Tags
 */
public class PayDateForSQL extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午12:01:46
	 */
	private static final long serialVersionUID = 1L;

	// 起始时间
	private Date startDate;

	// 截止时间
	private Date endDate;

	public PayDateForSQL() {
		super();

	}

	public PayDateForSQL(Date startDate, Date endDate) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "PayDateForSQL [startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
