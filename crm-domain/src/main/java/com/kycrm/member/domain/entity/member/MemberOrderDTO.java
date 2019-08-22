package com.kycrm.member.domain.entity.member;

/**
 * 客户信息详情 - 订单信息
 * @Author ZhengXiaoChen
 * @Date 2018年7月26日下午8:25:51
 * @Tags
 */
public class MemberOrderDTO extends MemberInfoDTO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月26日下午8:24:41
	 */
	private static final long serialVersionUID = 1L;

	// 订单号
	private String tradeId;

	// 交易时间
	private String tradeTime;

	// 订单状态
	private String orderStatus;

	public MemberOrderDTO() {
		super();

	}

	public MemberOrderDTO(String tradeId, String tradeTime, String orderStatus) {
		super();
		this.tradeId = tradeId;
		this.tradeTime = tradeTime;
		this.orderStatus = orderStatus;
	}

	public String getTradeId() {
		return tradeId;
	}

	public void setTradeId(String tradeId) {
		this.tradeId = tradeId;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	@Override
	public String toString() {
		return "MemberOrderDTO [tradeId=" + tradeId + ", tradeTime=" + tradeTime + ", orderStatus=" + orderStatus + "]";
	}

}
