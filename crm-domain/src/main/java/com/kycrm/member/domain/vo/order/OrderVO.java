package com.kycrm.member.domain.vo.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


public class OrderVO implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3651548921675840760L;

	/**用户info表id*/
	private Long uid;
	
	/**买家昵称*/
	private String buyerNick;
	
	/**订单编号*/
	private String orderId;
	
	/**手机号码*/
	private String receiverMobile;
	
	/**下单时间*/
	private Date b_tradeCreated;
	
	/**下单时间*/
	private Date e_tradeCreated;
	
	/**发货时间*/
	private Date b_tradeConsignTime;
	
	/**发货时间*/
	private Date e_tradeConsignTime;
	
	/**确认时间*/
	private Date b_tradeEndTime;
	
	/**确认时间*/
	private Date e_tradeEndTime;
	
	/**订单金额*/
	private BigDecimal b_tradePayment ;
	
	/**订单金额*/
	private BigDecimal e_tradePayment ;
	
	/**订单来源*/
	private String orderFrom;

	/**订单状态*/
	private List<String> tradeStatus;
	
	/**分页页码*/
	private int pageNo = 1;
	
	/**起始行数*/
	private int startRows;
	
	/**每页显示条数*/
	private int currentRows = 10;

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
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

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public Date getB_tradeCreated() {
		return b_tradeCreated;
	}

	public void setB_tradeCreated(Date b_tradeCreated) {
		this.b_tradeCreated = b_tradeCreated;
	}

	public Date getE_tradeCreated() {
		return e_tradeCreated;
	}

	public void setE_tradeCreated(Date e_tradeCreated) {
		this.e_tradeCreated = e_tradeCreated;
	}

	public Date getB_tradeConsignTime() {
		return b_tradeConsignTime;
	}

	public void setB_tradeConsignTime(Date b_tradeConsignTime) {
		this.b_tradeConsignTime = b_tradeConsignTime;
	}

	public Date getE_tradeConsignTime() {
		return e_tradeConsignTime;
	}

	public void setE_tradeConsignTime(Date e_tradeConsignTime) {
		this.e_tradeConsignTime = e_tradeConsignTime;
	}

	public Date getB_tradeEndTime() {
		return b_tradeEndTime;
	}

	public void setB_tradeEndTime(Date b_tradeEndTime) {
		this.b_tradeEndTime = b_tradeEndTime;
	}

	public Date getE_tradeEndTime() {
		return e_tradeEndTime;
	}

	public void setE_tradeEndTime(Date e_tradeEndTime) {
		this.e_tradeEndTime = e_tradeEndTime;
	}

	public BigDecimal getB_tradePayment() {
		return b_tradePayment;
	}

	public void setB_tradePayment(BigDecimal b_tradePayment) {
		this.b_tradePayment = b_tradePayment;
	}

	public BigDecimal getE_tradePayment() {
		return e_tradePayment;
	}

	public void setE_tradePayment(BigDecimal e_tradePayment) {
		this.e_tradePayment = e_tradePayment;
	}

	public List<String> getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(List<String> tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public int getPageNo() {
		return pageNo;
	}

	public void setPageNo(int pageNo) {
		if(pageNo<1)
			pageNo = 1;
		this.pageNo = pageNo;
		this.setStartRows(pageNo);
	}

	public int getStartRows() {
		return startRows;
	}

	public void setStartRows(int pageNum) {
		this.startRows = (pageNum-1)*this.currentRows;
	}

	public int getCurrentRows() {
		return currentRows;
	}

	public void setCurrentRows(int currentRows) {
		this.currentRows = currentRows;
	}
	
	
}
