package com.kycrm.member.domain.vo.effect;

import java.io.Serializable;
import java.util.Date;
/**
 * 订单中心效果分析回款详情VO
 * @ClassName: ReturnOrderDetail  
 * @author ztk
 * @date 2018年1月30日 上午10:31:48
 */
public class ReturnOrderDetailVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6631388822871523112L;
	//回款订单号
	private Long orderId;
	//催付时间
	private Date sendTime;
	//买家昵称
	private String buyerNick;
	//手机号
	private String recNum;
	//回款订单金额
	private Double earningFee;
	//付款时间
	private Date payTime;
	//点击时间
	private Date clickTime;
	
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getBuyerNick() {
		return buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}
	public String getRecNum() {
		return recNum;
	}
	public void setRecNum(String recNum) {
		this.recNum = recNum;
	}
	public Double getEarningFee() {
		return earningFee;
	}
	public void setEarningFee(Double earningFee) {
		this.earningFee = earningFee;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public Date getClickTime() {
		return clickTime;
	}
	public void setClickTime(Date clickTime) {
		this.clickTime = clickTime;
	}
	
}
