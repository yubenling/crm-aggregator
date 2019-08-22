package com.kycrm.member.domain.entity.premiummemberfilter;

import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterBaseVO;

/**
 * 时段内首次购买商品
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:02:25
 * @Tags
 */
public class BuyGoodsInFirstTimeOfPeriodTimeForSQL extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:02:36
	 */
	private static final long serialVersionUID = 1L;

	// 起始时间
	private Date startDate;

	// 截止时间
	private Date endDate;

	// 商品编号
	private List<String> numIids;

	public BuyGoodsInFirstTimeOfPeriodTimeForSQL() {
		super();

	}

	public BuyGoodsInFirstTimeOfPeriodTimeForSQL(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public BuyGoodsInFirstTimeOfPeriodTimeForSQL(Date startDate, Date endDate, List<String> numIids) {
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
		return "BuyGoodsInFirstTimeOfPeriodTimeForSQL [startDate=" + startDate + ", endDate=" + endDate + ", numIids="
				+ numIids + "]";
	}

}
