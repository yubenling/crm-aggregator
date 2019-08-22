package com.kycrm.member.domain.vo.trade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.kycrm.member.domain.entity.order.OrderDTO;

/**
 * 订单短信群发查询结果
 * @ClassName: TradeResultVO  
 * @author ztk
 * @date 2018年1月31日 下午4:19:07
 */
public class TradeResultVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4471051335101278180L;
	//主订单id
	@JsonSerialize(using=ToStringSerializer.class)
	private Long tid;
	//创建时间
	private Date created;
	//订单来源
	private String tradeFrom;
	//买家昵称
	private String buyerNick;
	//收货人手机号
	private String receiverMobile;
	//主订单实付金额
	private String tradePayment;
	//主订单购买商品总数
	private Long tradeNum;
	//订单状态
	private String tradeStatus;
	//卖家标记
	private String sellerFlag;
	
	private String receiverName;
	
	public String getSellerFlag() {
		return sellerFlag;
	}
	public void setSellerFlag(String sellerFlag) {
		this.sellerFlag = sellerFlag;
	}
	
	private String itemTitle;
	
	private Long itemNum;
	
	private String itemPrice;
	
	
	//主订单对应子订单
	private List<OrderDTO> orderDTOs;
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
	}
	public Date getCreated() {
		return created;
	}
	public void setCreated(Date created) {
		this.created = created;
	}
	public String getTradeFrom() {
		return tradeFrom;
	}
	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}
	public String getBuyerNick() {
		return buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}
	public String getReceiverMobile() {
		return receiverMobile;
	}
	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}
	public String getTradePayment() {
		return tradePayment;
	}
	public void setTradePayment(String tradePayment) {
		this.tradePayment = tradePayment;
	}
	public Long getTradeNum() {
		return tradeNum;
	}
	public void setTradeNum(Long tradeNum) {
		this.tradeNum = tradeNum;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public List<OrderDTO> getOrderDTOs() {
		return orderDTOs;
	}
	public void setOrderDTOs(List<OrderDTO> orderDTOs) {
		this.orderDTOs = orderDTOs;
	}
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	public Long getItemNum() {
		return itemNum;
	}
	public void setItemNum(Long itemNum) {
		this.itemNum = itemNum;
	}
	public String getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(String itemPrice) {
		this.itemPrice = itemPrice;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
}
