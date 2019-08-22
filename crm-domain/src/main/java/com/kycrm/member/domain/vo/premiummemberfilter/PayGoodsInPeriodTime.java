package com.kycrm.member.domain.vo.premiummemberfilter;

import java.util.List;

public class PayGoodsInPeriodTime extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午12:15:32
	 */
	private static final long serialVersionUID = 1L;

	// 起始时间
	private String startTime;

	// 截止时间
	private String endTime;

	// 商品编号
	private List<String> numIids;

	public PayGoodsInPeriodTime() {
		super();

	}

	public PayGoodsInPeriodTime(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public PayGoodsInPeriodTime(String startTime, String endTime, List<String> numIids) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.numIids = numIids;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public List<String> getNumIids() {
		return numIids;
	}

	public void setNumIids(List<String> numIids) {
		this.numIids = numIids;
	}

	@Override
	public String toString() {
		return "PayGoodsInPeriodTime [startTime=" + startTime + ", endTime=" + endTime + ", numIids=" + numIids + "]";
	}

}
