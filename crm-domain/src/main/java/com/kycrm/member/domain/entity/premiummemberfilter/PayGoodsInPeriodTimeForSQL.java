package com.kycrm.member.domain.entity.premiummemberfilter;

import java.sql.Time;
import java.util.List;

import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterBaseVO;

public class PayGoodsInPeriodTimeForSQL extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午12:15:32
	 */
	private static final long serialVersionUID = 1L;

	// 起始时间
	private Time startTime;

	// 截止时间
	private Time endTime;

	// 起始时间和截止时间比较结果
	private Integer compareResultBetweenStartAndEnd;

	// 商品编号
	private List<String> numIids;

	public PayGoodsInPeriodTimeForSQL() {
		super();

	}

	public PayGoodsInPeriodTimeForSQL(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public PayGoodsInPeriodTimeForSQL(Time startTime, Time endTime, Integer compareResultBetweenStartAndEnd,
			List<String> numIids) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.compareResultBetweenStartAndEnd = compareResultBetweenStartAndEnd;
		this.numIids = numIids;
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

	public List<String> getNumIids() {
		return numIids;
	}

	public void setNumIids(List<String> numIids) {
		this.numIids = numIids;
	}

	@Override
	public String toString() {
		return "PayGoodsInPeriodTimeForSQL [startTime=" + startTime + ", endTime=" + endTime
				+ ", compareResultBetweenStartAndEnd=" + compareResultBetweenStartAndEnd + ", numIids=" + numIids + "]";
	}

}
