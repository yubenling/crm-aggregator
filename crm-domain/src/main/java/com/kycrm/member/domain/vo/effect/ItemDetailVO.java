package com.kycrm.member.domain.vo.effect;

import java.util.Set;


public class ItemDetailVO {

	/**
	 * 商品id
	 */
	private Long itemId;
	
	/**
	 * 商品名称
	 */
	private String itemTitle;
	
	/**
	 * 商品价格
	 */
	private Double itemPrice;
	
	/**
	 * 选择该商品的客户数
	 */
	private Long buyerNum;
	
	/**
	 * 订单金额
	 */
	private Double tradeFee;
	
	/**
	 * 订单数
	 */
	private Integer tradeNum;
	
	/**
	 * 商品数
	 */
	private Long itemNum;
	
	
	private Set<String> buyerNickSet;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	public String getItemTitle() {
		return itemTitle;
	}

	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}

	public Double getItemPrice() {
		return itemPrice;
	}

	public void setItemPrice(Double itemPrice) {
		this.itemPrice = itemPrice;
	}

	public Long getBuyerNum() {
		return buyerNum;
	}

	public void setBuyerNum(Long buyerNum) {
		this.buyerNum = buyerNum;
	}

	public Double getTradeFee() {
		return tradeFee;
	}

	public void setTradeFee(Double tradeFee) {
		this.tradeFee = tradeFee;
	}

	public Integer getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(Integer tradeNum) {
		this.tradeNum = tradeNum;
	}

	public Long getItemNum() {
		return itemNum;
	}

	public void setItemNum(Long itemNum) {
		this.itemNum = itemNum;
	}

	public Set<String> getBuyerNickSet() {
		return buyerNickSet;
	}

	public void setBuyerNickSet(Set<String> buyerNickSet) {
		this.buyerNickSet = buyerNickSet;
	}
	
	
	
}
