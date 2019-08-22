package com.kycrm.member.domain.entity.premiummemberfilter;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterBaseVO;

/**
 * 时段内最后一次购买商品
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:06:02
 * @Tags
 */
public class BuyGoodsInLastTimeOfPeriodTimeForSQL extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:06:05
	 */
	private static final long serialVersionUID = 1L;

	// 起始时间
	private Date startDate;

	// 截止时间
	private Date endDate;

	// 商品编号
	private List<String> numIids;

	public BuyGoodsInLastTimeOfPeriodTimeForSQL() {
		super();

	}

	public BuyGoodsInLastTimeOfPeriodTimeForSQL(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public BuyGoodsInLastTimeOfPeriodTimeForSQL(Date startDate, Date endDate, List<String> numIids) {
		super();
		this.startDate = startDate;
		this.endDate = endDate;
		this.numIids = numIids;
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

	public List<String> getNumIids() {
		return numIids;
	}

	public void setNumIids(List<String> numIids) {
		this.numIids = numIids;
	}

	@Override
	public String toString() {
		return "BuyGoodsInLastTimeOfPeriodTimeForSQL [startDate=" + startDate + ", endDate=" + endDate + ", numIids="
				+ numIids + "]";
	}

}
