package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 付款时间
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午12:03:53
 * @Tags
 */
public class PayDate extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午12:01:46
	 */
	private static final long serialVersionUID = 1L;

	// 相对时间|绝对时间
	private boolean relativeOrAbsolute;

	// 起始时间
	private String startDate;

	// 截止时间
	private String endDate;

	// 起始天数
	private Integer startPoint;

	// 截止天数
	private Integer endPoint;

	public PayDate() {
		super();

	}

	public PayDate(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public PayDate(boolean relativeOrAbsolute, String startDate, String endDate, Integer startPoint, Integer endPoint) {
		super();
		this.relativeOrAbsolute = relativeOrAbsolute;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
	}

	public boolean isRelativeOrAbsolute() {
		return relativeOrAbsolute;
	}

	public void setRelativeOrAbsolute(boolean relativeOrAbsolute) {
		this.relativeOrAbsolute = relativeOrAbsolute;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public Integer getStartPoint() {
		return startPoint;
	}

	public void setStartPoint(Integer startPoint) {
		this.startPoint = startPoint;
	}

	public Integer getEndPoint() {
		return endPoint;
	}

	public void setEndPoint(Integer endPoint) {
		this.endPoint = endPoint;
	}

	@Override
	public String toString() {
		return "PayDate [relativeOrAbsolute=" + relativeOrAbsolute + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", startPoint=" + startPoint + ", endPoint=" + endPoint + "]";
	}

}
