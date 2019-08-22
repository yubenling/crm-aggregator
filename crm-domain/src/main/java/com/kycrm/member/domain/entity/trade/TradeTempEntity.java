package com.kycrm.member.domain.entity.trade;

import java.io.Serializable;

public class TradeTempEntity implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年1月28日下午4:15:37
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String buyerNick;

	private Long tradeNum;

	private Long closeTradeNum;

	private Integer addNumber;

	private String buyerAlipayNo;

	private String receiverInfoStr;

	public TradeTempEntity() {
		super();

	}

	public TradeTempEntity(Long id, String buyerNick, Long tradeNum, Long closeTradeNum, Integer addNumber,
			String buyerAlipayNo, String receiverInfoStr) {
		super();
		this.id = id;
		this.buyerNick = buyerNick;
		this.tradeNum = tradeNum;
		this.closeTradeNum = closeTradeNum;
		this.addNumber = addNumber;
		this.buyerAlipayNo = buyerAlipayNo;
		this.receiverInfoStr = receiverInfoStr;
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

	public Long getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(Long tradeNum) {
		this.tradeNum = tradeNum;
	}

	public Long getCloseTradeNum() {
		return closeTradeNum;
	}

	public void setCloseTradeNum(Long closeTradeNum) {
		this.closeTradeNum = closeTradeNum;
	}

	public Integer getAddNumber() {
		return addNumber;
	}

	public void setAddNumber(Integer addNumber) {
		this.addNumber = addNumber;
	}

	public String getBuyerAlipayNo() {
		return buyerAlipayNo;
	}

	public void setBuyerAlipayNo(String buyerAlipayNo) {
		this.buyerAlipayNo = buyerAlipayNo;
	}

	public String getReceiverInfoStr() {
		return receiverInfoStr;
	}

	public void setReceiverInfoStr(String receiverInfoStr) {
		this.receiverInfoStr = receiverInfoStr;
	}

}
