package com.kycrm.member.domain.entity.premiummemberfilter;

import java.sql.Time;

import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterBaseVO;

/**
 * 付款时段
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:14:44
 * @Tags
 */
public class PayTimeForSQL extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:14:49
	 */
	private static final long serialVersionUID = 1L;

	// 起始时间段
	private Time startTime;

	// 截止时间段
	private Time endTime;

	// 起始时间和截止时间比较结果
	private Integer compareResultBetweenStartAndEnd;

	public PayTimeForSQL() {
		super();

	}

	public PayTimeForSQL(Time startTime, Time endTime, Integer compareResultBetweenStartAndEnd) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.compareResultBetweenStartAndEnd = compareResultBetweenStartAndEnd;
	}

	public Time getStartTime() {
		return startTime;
	}

	public void setStartTime(Time startTime) {
		this.startTime = startTime;
	}

	public Time getEndTime() {
		return endTime;
	}

	public void setEndTime(Time endTime) {
		this.endTime = endTime;
	}

	public Integer getCompareResultBetweenStartAndEnd() {
		return compareResultBetweenStartAndEnd;
	}

	public void setCompareResultBetweenStartAndEnd(Integer compareResultBetweenStartAndEnd) {
		this.compareResultBetweenStartAndEnd = compareResultBetweenStartAndEnd;
	}

	@Override
	public String toString() {
		return "PayTimeForSQL [startTime=" + startTime + ", endTime=" + endTime + ", compareResultBetweenStartAndEnd="
				+ compareResultBetweenStartAndEnd + "]";
	}

}
