package com.kycrm.member.domain.entity.order;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.traderate.TradeRates;

/**
 * 客户信息详情 - 订单信息
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月27日上午11:37:15
 * @Tags
 */
public class MemberInformationDetailOrderInfoDTO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月27日上午11:35:10
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 订单号
	 */
	private String tid;

	/**
	 * 交易时间
	 */
	private Date tradeTime;

	/**
	 * 订单状态
	 */
	private String orderStatus;

	/**
	 * 商品标题
	 */
	private String title;

	/**
	 * 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
	 */
	// payment(主订单冗余字段)
	private BigDecimal tradePayment;

	/**
	 * 商品购买数量。取值范围：大于零的整数,对于一个trade对应多个order的时候（一笔主订单，对应多笔子订单），num=0，
	 * num是一个跟商品关联的属性，一笔订单对应多比子订单的时候，主订单上的num无意义。
	 */
	// num(主订单冗余字段)
	private Long tradeNum;

	/**
	 * 订单状态（请关注此状态，如果为TRADE_CLOSED_BY_TAOBAO状态，则不要对此订单进行发货，切记啊！）。可选值:
	 */
	private String status;

	/**
	 * 卖家备注旗帜（与淘宝网上订单的卖家备注旗帜对应，只有卖家才能查看该字段）红、黄、绿、蓝、紫分别对应1、2、3、4、5
	 */
	// seller_flag(主订单冗余字段)
	private Long sellerFlag;

	private TradeRates tradeRates;

	private List<OrderDTO> orderList = new ArrayList<OrderDTO>();

	public MemberInformationDetailOrderInfoDTO() {
		super();

	}

	public MemberInformationDetailOrderInfoDTO(String tid, Date tradeTime, String orderStatus, String title,
			BigDecimal tradePayment, Long tradeNum, String status, Long sellerFlag, TradeRates tradeRates,
			List<OrderDTO> orderList) {
		super();
		this.tid = tid;
		this.tradeTime = tradeTime;
		this.orderStatus = orderStatus;
		this.title = title;
		this.tradePayment = tradePayment;
		this.tradeNum = tradeNum;
		this.status = status;
		this.sellerFlag = sellerFlag;
		this.tradeRates = tradeRates;
		this.orderList = orderList;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Date getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Date tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public BigDecimal getTradePayment() {
		return tradePayment;
	}

	public void setTradePayment(BigDecimal tradePayment) {
		this.tradePayment = tradePayment;
	}

	public Long getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(Long tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getSellerFlag() {
		return sellerFlag;
	}

	public void setSellerFlag(Long sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	public TradeRates getTradeRates() {
		return tradeRates;
	}

	public void setTradeRates(TradeRates tradeRates) {
		this.tradeRates = tradeRates;
	}

	public List<OrderDTO> getOrderList() {
		return orderList;
	}

	public void setOrderList(List<OrderDTO> orderList) {
		this.orderList = orderList;
	}

	@Override
	public String toString() {
		return "MemberInformationDetailOrderInfoDTO [tid=" + tid + ", tradeTime=" + tradeTime + ", orderStatus="
				+ orderStatus + ", title=" + title + ", tradePayment=" + tradePayment + ", tradeNum=" + tradeNum
				+ ", status=" + status + ", sellerFlag=" + sellerFlag + ", tradeRates=" + tradeRates + ", orderList="
				+ orderList + "]";
	}

}
