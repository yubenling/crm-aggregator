package com.kycrm.member.domain.vo.payment;

import java.io.Serializable;
/**	
* @Title: AliPayVo
* @Description: (充值vo类)
*/
public class AliPayVO implements Serializable {
	private static final long serialVersionUID = -5721478514581007370L;
	
	/**
	 * @param totalAmount (必填) 订单总金额，单位为元，不能超过1亿元
	 */
	private Double totalAmount;
	
	/**
	 * rechargeNum //必填) 短信充值条数
	 */
	private Integer rechargeNum;
	
	/**
	 * 充值记录订单id
	 */
	private String payTrade;
	
	
	/**
	 * 支付宝返回状态
	 */
	private String tradeStatus;
	
	
	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getPayTrade() {
		return payTrade;
	}

	public void setPayTrade(String payTrade) {
		this.payTrade = payTrade;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public Integer getRechargeNum() {
		return rechargeNum;
	}

	public void setRechargeNum(Integer rechargeNum) {
		this.rechargeNum = rechargeNum;
	}
	
	
}
