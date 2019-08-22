package com.kycrm.member.domain.vo.premiummemberfilter;

import java.util.List;

/**
 * 时段内最后一次购买商品
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:06:02
 * @Tags
 */
public class BuyGoodsInLastTimeOfPeriodTime extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午2:06:05
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

	// 商品编号
	private List<String> numIids;

	public BuyGoodsInLastTimeOfPeriodTime() {
		super();

	}

	public BuyGoodsInLastTimeOfPeriodTime(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public BuyGoodsInLastTimeOfPeriodTime(boolean relativeOrAbsolute, String startDate, String endDate, Integer startPoint,
			Integer endPoint, List<String> numIids) {
		super();
		this.relativeOrAbsolute = relativeOrAbsolute;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startPoint = startPoint;
		this.endPoint = endPoint;
		this.numIids = numIids;
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

	public List<String> getNumIids() {
		return numIids;
	}

	public void setNumIids(List<String> numIids) {
		this.numIids = numIids;
	}

	@Override
	public String toString() {
		return "BuyGoodsInLastTimeOfPeriodTime [relativeOrAbsolute=" + relativeOrAbsolute + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", startPoint=" + startPoint + ", endPoint=" + endPoint + ", numIids="
				+ numIids + "]";
	}

}
