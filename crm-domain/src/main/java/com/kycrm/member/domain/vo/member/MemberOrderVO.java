package com.kycrm.member.domain.vo.member;

import java.io.Serializable;

/**
 * 会员信息 - 客户信息详情 - 订单信息
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月25日下午5:27:56
 * @Tags
 */
public class MemberOrderVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月25日下午5:23:50
	 */
	private static final long serialVersionUID = 1L;

	private Long uid;

	// 买家昵称
	private String buyerNick;

	// 最小交易时间
	private String minTradeTime;

	// 最大交易时间
	private String maxTradeTime;

	// 订单状态
	private String orderStatus;
	
	//页码
	private Integer pageNo;

	public MemberOrderVO() {
		super();

	}

	public MemberOrderVO(Long uid, String buyerNick, String minTradeTime, String maxTradeTime, String orderStatus,
			Integer pageNo) {
		super();
		this.uid = uid;
		this.buyerNick = buyerNick;
		this.minTradeTime = minTradeTime;
		this.maxTradeTime = maxTradeTime;
		this.orderStatus = orderStatus;
		this.pageNo = pageNo;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getMinTradeTime() {
		return minTradeTime;
	}

	public void setMinTradeTime(String minTradeTime) {
		this.minTradeTime = minTradeTime;
	}

	public String getMaxTradeTime() {
		return maxTradeTime;
	}

	public void setMaxTradeTime(String maxTradeTime) {
		this.maxTradeTime = maxTradeTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return "MemberOrderVO [uid=" + uid + ", buyerNick=" + buyerNick + ", minTradeTime=" + minTradeTime
				+ ", maxTradeTime=" + maxTradeTime + ", orderStatus=" + orderStatus + ", pageNo=" + pageNo + "]";
	}

}
