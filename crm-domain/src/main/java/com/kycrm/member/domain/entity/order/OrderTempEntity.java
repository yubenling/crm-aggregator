package com.kycrm.member.domain.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OrderTempEntity implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年1月28日下午3:50:58
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String buyerNick;

	private BigDecimal tradeAmount;

	private Long itemNum;

	private Long addItemNum;

	private BigDecimal closeTradeAmount;

	private Long closeItemNum;

	private Date lastTradeTime;

	private Date firstPayTime;

	private Date firstTradeTime;

	private Date lastPayTime;

	private Date firstTradeFinishTime;

	private Date lastTradeFinishTime;

	private String tbrefundStatus;

	private String tborderStatus;

	public OrderTempEntity() {
		super();
	}

	public OrderTempEntity(Long id, String buyerNick, BigDecimal tradeAmount, Long itemNum, Long addItemNum,
			BigDecimal closeTradeAmount, Long closeItemNum, Date lastTradeTime, Date firstPayTime, Date firstTradeTime,
			Date lastPayTime, Date firstTradeFinishTime, Date lastTradeFinishTime, String tbrefundStatus,
			String tborderStatus) {
		super();
		this.id = id;
		this.buyerNick = buyerNick;
		this.tradeAmount = tradeAmount;
		this.itemNum = itemNum;
		this.addItemNum = addItemNum;
		this.closeTradeAmount = closeTradeAmount;
		this.closeItemNum = closeItemNum;
		this.lastTradeTime = lastTradeTime;
		this.firstPayTime = firstPayTime;
		this.firstTradeTime = firstTradeTime;
		this.lastPayTime = lastPayTime;
		this.firstTradeFinishTime = firstTradeFinishTime;
		this.lastTradeFinishTime = lastTradeFinishTime;
		this.tbrefundStatus = tbrefundStatus;
		this.tborderStatus = tborderStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public Long getItemNum() {
		return itemNum;
	}

	public void setItemNum(Long itemNum) {
		this.itemNum = itemNum;
	}

	public Long getAddItemNum() {
		return addItemNum;
	}

	public void setAddItemNum(Long addItemNum) {
		this.addItemNum = addItemNum;
	}

	public BigDecimal getCloseTradeAmount() {
		return closeTradeAmount;
	}

	public void setCloseTradeAmount(BigDecimal closeTradeAmount) {
		this.closeTradeAmount = closeTradeAmount;
	}

	public Long getCloseItemNum() {
		return closeItemNum;
	}

	public void setCloseItemNum(Long closeItemNum) {
		this.closeItemNum = closeItemNum;
	}

	public Date getLastTradeTime() {
		return lastTradeTime;
	}

	public void setLastTradeTime(Date lastTradeTime) {
		this.lastTradeTime = lastTradeTime;
	}

	public Date getFirstPayTime() {
		return firstPayTime;
	}

	public void setFirstPayTime(Date firstPayTime) {
		this.firstPayTime = firstPayTime;
	}

	public Date getFirstTradeTime() {
		return firstTradeTime;
	}

	public void setFirstTradeTime(Date firstTradeTime) {
		this.firstTradeTime = firstTradeTime;
	}

	public Date getLastPayTime() {
		return lastPayTime;
	}

	public void setLastPayTime(Date lastPayTime) {
		this.lastPayTime = lastPayTime;
	}

	public Date getFirstTradeFinishTime() {
		return firstTradeFinishTime;
	}

	public void setFirstTradeFinishTime(Date firstTradeFinishTime) {
		this.firstTradeFinishTime = firstTradeFinishTime;
	}

	public Date getLastTradeFinishTime() {
		return lastTradeFinishTime;
	}

	public void setLastTradeFinishTime(Date lastTradeFinishTime) {
		this.lastTradeFinishTime = lastTradeFinishTime;
	}

	public String getTbrefundStatus() {
		return tbrefundStatus;
	}

	public void setTbrefundStatus(String tbrefundStatus) {
		this.tbrefundStatus = tbrefundStatus;
	}

	public String getTborderStatus() {
		return tborderStatus;
	}

	public void setTborderStatus(String tborderStatus) {
		this.tborderStatus = tborderStatus;
	}

	@Override
	public String toString() {
		return "OrderTempEntity [id=" + id + ", buyerNick=" + buyerNick + ", tradeAmount=" + tradeAmount + ", itemNum="
				+ itemNum + ", addItemNum=" + addItemNum + ", closeTradeAmount=" + closeTradeAmount + ", closeItemNum="
				+ closeItemNum + ", lastTradeTime=" + lastTradeTime + ", firstPayTime=" + firstPayTime
				+ ", firstTradeTime=" + firstTradeTime + ", lastPayTime=" + lastPayTime + ", firstTradeFinishTime="
				+ firstTradeFinishTime + ", lastTradeFinishTime=" + lastTradeFinishTime + ", tbrefundStatus="
				+ tbrefundStatus + ", tborderStatus=" + tborderStatus + "]";
	}

}
