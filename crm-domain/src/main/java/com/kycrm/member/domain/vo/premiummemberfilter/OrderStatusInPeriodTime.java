package com.kycrm.member.domain.vo.premiummemberfilter;

/**
 * 时段内订单状态
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午1:44:55
 * @Tags
 */
public class OrderStatusInPeriodTime extends PremiumMemberFilterBaseVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日下午1:44:51
	 */
	private static final long serialVersionUID = 1L;

	// 起始时间
	private String startTime;

	// 截止时间
	private String endTime;

	// 等待买家付款
	private boolean waitBuyerPay;

	// 买家已付款
	private boolean waitSellerSendGoods;

	// 交易成功
	private boolean tradeFinished;

	public OrderStatusInPeriodTime() {
		super();

	}

	public OrderStatusInPeriodTime(boolean includeOrExclude, boolean andOrOr) {
		super(includeOrExclude, andOrOr);

	}

	public OrderStatusInPeriodTime(String startTime, String endTime, boolean waitBuyerPay, boolean waitSellerSendGoods,
			boolean tradeFinished) {
		super();
		this.startTime = startTime;
		this.endTime = endTime;
		this.waitBuyerPay = waitBuyerPay;
		this.waitSellerSendGoods = waitSellerSendGoods;
		this.tradeFinished = tradeFinished;
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
		return "OrderStatusInPeriodTime [startTime=" + startTime + ", endTime=" + endTime + ", waitBuyerPay="
				+ waitBuyerPay + ", waitSellerSendGoods=" + waitSellerSendGoods + ", tradeFinished=" + tradeFinished
				+ "]";
	}

}
