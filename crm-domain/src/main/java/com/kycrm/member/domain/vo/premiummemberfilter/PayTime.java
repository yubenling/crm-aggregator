package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 付款时段
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:14:44
 * @Tags
 */
public class PayTime extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:14:49
	 */
	private static final long serialVersionUID = 1L;

	// 起始时间段
	private String startTime;

	// 截止时间段
	private String endTime;

	public PayTime() {
		super();

	}

	public PayTime(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public PayTime(String startTime, String endTime) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
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

	@Override
	public String toString() {
		return "PayTime [startTime=" + startTime + ", endTime=" + endTime + "]";
	}

}
