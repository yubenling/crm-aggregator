package com.kycrm.member.domain.vo.effect;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 营销中心效果分析首页的每日数据VO
 * @ClassName: PayOrderEffectDetail  
 * @author ztk
 * @date 2018年1月25日 下午4:19:40
 */
public class PayOrderEffectDetailVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1018939165920218636L;
	/**
	 * 日期
	 */
	private String endTime;
	
	/**
	 * 付款客户数
	 */
	private Integer customerNum;
	
	/**
	 * 付款金额
	 */
	private Double successMoney;

	/**
	 * 付款订单数
	 */
	private Integer orderNum;

	/**
	 * 付款客单价
	 */
	private Double successCusPrice;
	
	/**
	 * 付款商品数
	 */
	private Integer itemNum;
	
	/**
	 * 平均购买商品件数
	 */
	private Double successItemAverageNum;
	
	/**
	 * 付款率
	 */
	private Double successOrderRate;
	
	/**
	 * 付款金额
	 */
	private BigDecimal payment;
	
	/**
	 * 订单数
	 */
	private Integer tids;
	
	/**
	 * 付款时间
	 */
	private String payTime;
	
	/**
	 * 定金支付总金额
	 */
	private Double paidFrontFee;
	
	/**
	 * 付定金订单数
	 */
	private Integer paidFrontTrade;
	
	/**
	 * 尾款支付总金额
	 */
	private Double paidTailFee;
	
	/**
	 * 付尾款订单数
	 */
	private Integer paidTailTrade;
	
	/**
	 * 预售订单成交率
	 */
	private String stepPaidRatio;
	
	
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getCustomerNum() {
		return customerNum;
	}
	public void setCustomerNum(Integer customerNum) {
		this.customerNum = customerNum;
	}
	public Double getSuccessMoney() {
		return successMoney;
	}
	public void setSuccessMoney(Double successMoney) {
		this.successMoney = successMoney;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public Double getSuccessCusPrice() {
		return successCusPrice;
	}
	public void setSuccessCusPrice(Double successCusPrice) {
		this.successCusPrice = successCusPrice;
	}
	public Integer getItemNum() {
		return itemNum;
	}
	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}
	public Double getSuccessItemAverageNum() {
		return successItemAverageNum;
	}
	public void setSuccessItemAverageNum(Double successItemAverageNum) {
		this.successItemAverageNum = successItemAverageNum;
	}
	public Double getSuccessOrderRate() {
		return successOrderRate;
	}
	public void setSuccessOrderRate(Double successOrderRate) {
		this.successOrderRate = successOrderRate;
	}
	public BigDecimal getPayment() {
		return payment;
	}
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
	public Integer getTids() {
		return tids;
	}
	public void setTids(Integer tids) {
		this.tids = tids;
	}
	public String getPayTime() {
		return payTime;
	}
	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}
	public Double getPaidFrontFee() {
		return paidFrontFee;
	}
	public void setPaidFrontFee(Double paidFrontFee) {
		this.paidFrontFee = paidFrontFee;
	}
	public Integer getPaidFrontTrade() {
		return paidFrontTrade;
	}
	public void setPaidFrontTrade(Integer paidFrontTrade) {
		this.paidFrontTrade = paidFrontTrade;
	}
	public Double getPaidTailFee() {
		return paidTailFee;
	}
	public void setPaidTailFee(Double paidTailFee) {
		this.paidTailFee = paidTailFee;
	}
	public Integer getPaidTailTrade() {
		return paidTailTrade;
	}
	public void setPaidTailTrade(Integer paidTailTrade) {
		this.paidTailTrade = paidTailTrade;
	}
	public String getStepPaidRatio() {
		return stepPaidRatio;
	}
	public void setStepPaidRatio(String stepPaidRatio) {
		this.stepPaidRatio = stepPaidRatio;
	}
	
	
}
