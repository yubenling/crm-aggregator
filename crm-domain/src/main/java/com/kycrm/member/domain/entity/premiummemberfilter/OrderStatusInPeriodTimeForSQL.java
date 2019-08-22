package com.kycrm.member.domain.entity.premiummemberfilter;

import java.sql.Time;

import com.kycrm.member.domain.vo.premiummemberfilter.PremiumMemberFilterBaseVO;

/**
 * 时段内订单状态
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午1:44:55
 * @Tags
 */
public class OrderStatusInPeriodTimeForSQL extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午1:44:51
	 */
	private static final long serialVersionUID = 1L;

	// 起始时间
	private Time startTime;

	// 截止时间
	private Time endTime;

	// 起始时间和截止时间比较结果
	private Integer compareResultBetweenStartAndEnd;

	// 等待买家付款
	private boolean waitBuyerPay;

	// 买家已付款
	private boolean waitSellerSendGoods;

	// 交易成功
	private boolean tradeFinished;

	public OrderStatusInPeriodTimeForSQL() {
		super();

	}

	public OrderStatusInPeriodTimeForSQL(Time startTime, Time endTime, Integer compareResultBetweenStartAndEnd,
			boolean waitBuyerPay, boolean waitSellerSendGoods, boolean tradeFinished) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.compareResultBetweenStartAndEnd = compareResultBetweenStartAndEnd;
		this.waitBuyerPay = waitBuyerPay;
		this.waitSellerSendGoods = waitSellerSendGoods;
		this.tradeFinished = tradeFinished;
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

	public boolean isWaitBuyerPay() {
		return waitBuyerPay;
	}

	public void setWaitBuyerPay(boolean waitBuyerPay) {
		this.waitBuyerPay = waitBuyerPay;
	}

	public boolean isWaitSellerSendGoods() {
		return waitSellerSendGoods;
	}

	public void setWaitSellerSendGoods(boolean waitSellerSendGoods) {
		this.waitSellerSendGoods = waitSellerSendGoods;
	}

	public boolean isTradeFinished() {
		return tradeFinished;
	}

	public void setTradeFinished(boolean tradeFinished) {
		this.tradeFinished = tradeFinished;
	}

	@Override
	public String toString() {
		return "OrderStatusInPeriodTimeForSQL [startTime=" + startTime + ", endTime=" + endTime
				+ ", compareResultBetweenStartAndEnd=" + compareResultBetweenStartAndEnd + ", waitBuyerPay="
				+ waitBuyerPay + ", waitSellerSendGoods=" + waitSellerSendGoods + ", tradeFinished=" + tradeFinished
				+ "]";
	}

}
