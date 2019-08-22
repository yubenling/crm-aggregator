package com.kycrm.member.domain.entity.refund;

import java.math.BigDecimal;
import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;


public class RefundDTO extends BaseEntity{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
     * 退款id
     */
	private Long refundId;
	/*
	 * 订单的退款状态
	 */
	private String status;
	/*
	 * 卖家昵称
	 */
	private String sellerNick;
	/*
	 * 订单id
	 */
	private Long tid;
	/*
	 * 子订单id
	 */
	private Long oid;
	/*
	 * 创建时间
	 */
	private Date created;
	/*
	 * 修改时间
	 */
	private Date modified;
	/*
	 * 
	 */
	private Long advanceStatus;
	/*
	 * 支付宝号
	 */
	private String alipayNo;
	/*
	 * 属性
	 */
	private String attribute;
	/**
	 * 不知道是啥
	 */
	private Long csStatus;
	/*
	 * 描述
	 */
	private String desc;
	/*
	 * 商品状态
	 */
	private String goodStatus;
	/*
	 * 商品是否退回
	 */
	private Boolean hasGoodReturn;
	/*
	 * 数量
	 */
	private Long num;
	/*
	 * 商品id
	 */
	private Long numIid;
	/*
	 * 操作约束
	 */
	private String operationContraint;
	/*
	 * 子订单状态
	 */
	private String orderStatus;
	/**
	 * 外部id
	 */
	private String outerId;
	/*
	 * 支付金额
	 */
	private BigDecimal payment;
	/*
	 * 单价
	 */
	private BigDecimal price;
	/*
	 * 理由
	 */
	private String reason;
	/*
	 * 退款费用
	 */
	private BigDecimal refundFee;
	/*
	 * 退款解析
	 */
	private String refundPhase;
	/*
	 * 退款版本
	 */
	private Long refundVersion;
	/*
	 * sku
	 */
	private String sku;
	/*
	 * 标题
	 */
	private String title;
	/*
	 * 总费用
	 */
	private BigDecimal totalFee;
	public Long getRefundId() {
		return refundId;
	}
	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSellerNick() {
		return sellerNick;
	}
	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
	}
	public Long getOid() {
		return oid;
	}
	public void setOid(Long oid) {
		this.oid = oid;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public Date getModified() {
		return modified;
	}
	public void setModified(Date modified) {
		this.modified = modified;
	}
	public Long getAdvanceStatus() {
		return advanceStatus;
	}
	public void setAdvanceStatus(Long advanceStatus) {
		this.advanceStatus = advanceStatus;
	}
	public String getAlipayNo() {
		return alipayNo;
	}
	public void setAlipayNo(String alipayNo) {
		this.alipayNo = alipayNo;
	}
	public String getAttribute() {
		return attribute;
	}
	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}
	public Long getCsStatus() {
		return csStatus;
	}
	public void setCsStatus(Long csStatus) {
		this.csStatus = csStatus;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getGoodStatus() {
		return goodStatus;
	}
	public void setGoodStatus(String goodStatus) {
		this.goodStatus = goodStatus;
	}
	public Boolean getHasGoodReturn() {
		return hasGoodReturn;
	}
	public void setHasGoodReturn(Boolean hasGoodReturn) {
		this.hasGoodReturn = hasGoodReturn;
	}
	public Long getNum() {
		return num;
	}
	public void setNum(Long num) {
		this.num = num;
	}
	public Long getNumIid() {
		return numIid;
	}
	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}
	public String getOperationContraint() {
		return operationContraint;
	}
	public void setOperationContraint(String operationContraint) {
		this.operationContraint = operationContraint;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOuterId() {
		return outerId;
	}
	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}
	public BigDecimal getPayment() {
		return payment;
	}
	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public BigDecimal getRefundFee() {
		return refundFee;
	}
	public void setRefundFee(BigDecimal refundFee) {
		this.refundFee = refundFee;
	}
	public String getRefundPhase() {
		return refundPhase;
	}
	public void setRefundPhase(String refundPhase) {
		this.refundPhase = refundPhase;
	}
	public Long getRefundVersion() {
		return refundVersion;
	}
	public void setRefundVersion(Long refundVersion) {
		this.refundVersion = refundVersion;
	}
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public BigDecimal getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}
	
	

}
