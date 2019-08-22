package com.kycrm.member.domain.entity.effect;

import java.math.BigDecimal;
import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

public class MarketingCenterEffect extends BaseEntity {

	private static final long serialVersionUID = 1L;
	
	public MarketingCenterEffect(){
		
	}
	
	public MarketingCenterEffect(Long uid,String orderSource,Date effectTime,Integer days,Long msgId,Date sendTime){
		this.userId = uid + "";
		this.tradeFrom = orderSource;
		this.effectTime = effectTime;
		this.days = days;
		this.msgId = msgId;
		this.sendTime = sendTime;
	}
	
	
	/**
	 * 卖家昵称
	 */
	private String userId;
	
	/**
	 * 总记录id
	 */
	private Long msgId;
	
	/**
	 * 总记录发送时间
	 */
	private Date sendTime;
	
	/**
	 * 订单来源
	 */
	private String tradeFrom;
	
	/**
	 * 订单状态(客户类型)
	 */
	private String tradeStatus;
	
	/**
	 * 下单总金额/支付定金总金额
	 */
	private BigDecimal createAmount;
	
	/**
	 * 下单客户数/支付定金客户数
	 */
	private Integer createBuyerNum;
	
	/**
	 * 总下单数/支付定金订单数
	 */
	private Integer createTradeNum;
	
	/**
	 * 下单商品数/支付定金商品数
	 */
	private Long createItemNum;
	
	/**
	 * 付款金额/付尾款总金额
	 */
	private BigDecimal payAmount;
	
	/**
	 * 付款客户数/付尾款客户数
	 */
	private Integer payBuyerNum;
	
	/**
	 * 付款订单数/付尾款订单数
	 */
	private Integer payTradeNum;
	
	/**
	 * 付款商品数/付尾款商品数
	 */
	private Long payItemNum;
	
	/**
	 * 未付款金额/未付尾款总金额
	 */
	private BigDecimal waitPayAmount;
	
	/**
	 * 未付款客户/为付尾款客户数
	 */
	private Integer waitPayBuyerNum;
	
	/**
	 * 未付款订单数/未付尾款订单数
	 */
	private Integer waitPayTradeNum;
	
	/**
	 * 未付款商品数/未付尾款商品数
	 */
	private Long waitPayItemNum;
	
	/**
	 * 退款金额/退款总金额
	 */
	private BigDecimal refundAmount;
	
	/**
	 * 退款客户数/退款客户数
	 */
	private Integer refundBuyerNum;
	
	/**
	 * 退款订单数/退款订单数
	 */
	private Integer refundTradeNum;
	
	/**
	 * 退款商品数/退款商品数
	 */
	private Long refundItemNum;
	
	/**
	 * 分析日期(只保存发送短信之后的15天的分析数据，每天存一条记录)
	 */
	private Date effectTime;
	
	/**
	 * 针对每个批次的短信，只保存15天，days记录保存到第几天
	 */
	private Integer days;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getTradeFrom() {
		return tradeFrom;
	}

	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

	public BigDecimal getCreateAmount() {
		return createAmount;
	}

	public void setCreateAmount(BigDecimal createAmount) {
		this.createAmount = createAmount;
	}

	public Integer getCreateBuyerNum() {
		return createBuyerNum;
	}

	public void setCreateBuyerNum(Integer createBuyerNum) {
		this.createBuyerNum = createBuyerNum;
	}

	public Integer getCreateTradeNum() {
		return createTradeNum;
	}

	public void setCreateTradeNum(Integer createTradeNum) {
		this.createTradeNum = createTradeNum;
	}

	public Long getCreateItemNum() {
		return createItemNum;
	}

	public void setCreateItemNum(Long createItemNum) {
		this.createItemNum = createItemNum;
	}

	public BigDecimal getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}

	public Integer getPayBuyerNum() {
		return payBuyerNum;
	}

	public void setPayBuyerNum(Integer payBuyerNum) {
		this.payBuyerNum = payBuyerNum;
	}

	public Integer getPayTradeNum() {
		return payTradeNum;
	}

	public void setPayTradeNum(Integer payTradeNum) {
		this.payTradeNum = payTradeNum;
	}

	public Long getPayItemNum() {
		return payItemNum;
	}

	public void setPayItemNum(Long payItemNum) {
		this.payItemNum = payItemNum;
	}

	

	public BigDecimal getWaitPayAmount() {
		return waitPayAmount;
	}

	public void setWaitPayAmount(BigDecimal waitPayAmount) {
		this.waitPayAmount = waitPayAmount;
	}

	public Integer getWaitPayBuyerNum() {
		return waitPayBuyerNum;
	}

	public void setWaitPayBuyerNum(Integer waitPayBuyerNum) {
		this.waitPayBuyerNum = waitPayBuyerNum;
	}

	public Integer getWaitPayTradeNum() {
		return waitPayTradeNum;
	}

	public void setWaitPayTradeNum(Integer waitPayTradeNum) {
		this.waitPayTradeNum = waitPayTradeNum;
	}

	public Long getWaitPayItemNum() {
		return waitPayItemNum;
	}

	public void setWaitPayItemNum(Long waitPayItemNum) {
		this.waitPayItemNum = waitPayItemNum;
	}

	public BigDecimal getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(BigDecimal refundAmount) {
		this.refundAmount = refundAmount;
	}

	public Integer getRefundBuyerNum() {
		return refundBuyerNum;
	}

	public void setRefundBuyerNum(Integer refundBuyerNum) {
		this.refundBuyerNum = refundBuyerNum;
	}

	public Integer getRefundTradeNum() {
		return refundTradeNum;
	}

	public void setRefundTradeNum(Integer refundTradeNum) {
		this.refundTradeNum = refundTradeNum;
	}

	public Long getRefundItemNum() {
		return refundItemNum;
	}

	public void setRefundItemNum(Long refundItemNum) {
		this.refundItemNum = refundItemNum;
	}

	public Date getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(Date effectTime) {
		this.effectTime = effectTime;
	}

	public Integer getDays() {
		return days;
	}

	public void setDays(Integer days) {
		this.days = days;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}



	
}
