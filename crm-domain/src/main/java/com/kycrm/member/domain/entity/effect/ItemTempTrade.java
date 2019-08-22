package com.kycrm.member.domain.entity.effect;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.kycrm.member.domain.entity.base.ShardingTable;

public class ItemTempTrade implements Serializable,ShardingTable {

	private static final long serialVersionUID = 1L;
	
	
	private Long id;

	//uid
	private Long uid;
	
	//msgid
	private Long msgId;
	
	//主订单编号
	private Long tid;
	
	//商品id
	private Long itemId;
	
	//商品标题
	private String title;
	
	//商品单价
	private BigDecimal price;
	
	//订单总金额
	private BigDecimal payment;
	
	private String buyerNick;
	
	//商品购买数量
	private Long num;
	
	//订单来源
	private String tradeFrom;
	
	/**
	 * 此订单的状态   非预售效果分析的： create:创建 waitPay:未付款 success:付款 refund:退款
	 * 			 预售效果分析的：create:创建 closed订单关闭 waitPay:未付款   success成交  refund退款
	 */
	private String tradeStatus;
	
	//订单创建时间
	private Date created;
	
	//msg记录创建时间
	private Date msgCreated;
	
	/**
     * 阶段付款的订单状态（例如万人团订单等），目前有三返回状态
     * FRONT_NOPAID_FINAL_NOPAID(定金未付尾款未付)，
     * FRONT_PAID_FINAL_NOPAID(定金已付尾款未付)，
     * FRONT_PAID_FINAL_PAID(定金和尾款都付)
     */
    private String stepTradeStatus;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}


	
	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public String getTradeFrom() {
		return tradeFrom;
	}

	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public Date getMsgCreated() {
		return msgCreated;
	}

	public void setMsgCreated(Date msgCreated) {
		this.msgCreated = msgCreated;
	}

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStepTradeStatus() {
		return stepTradeStatus;
	}

	public void setStepTradeStatus(String stepTradeStatus) {
		this.stepTradeStatus = stepTradeStatus;
	}
	
	
}
