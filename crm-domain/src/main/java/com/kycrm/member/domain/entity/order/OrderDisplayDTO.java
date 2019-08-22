package com.kycrm.member.domain.entity.order;

import java.math.BigDecimal;

import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.entity.traderate.TradeRates;

/**
 * 会员信息 - 会员详情 - 订单信息
 * 【注】：此类仅为了前端页面输出方便而创建
 * @Author ZhengXiaoChen
 * @Date 2018年7月28日下午12:54:25
 * @Tags
 */
public class OrderDisplayDTO extends TradeDTO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月28日下午12:37:18
	 */
	private static final long serialVersionUID = 1L;

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

	public OrderDisplayDTO() {
		super();

	}

	public OrderDisplayDTO(String title, BigDecimal tradePayment, Long tradeNum, String status, Long sellerFlag,
			TradeRates tradeRates) {
		super();
		this.title = title;
		this.tradePayment = tradePayment;
		this.tradeNum = tradeNum;
		this.status = status;
		this.sellerFlag = sellerFlag;
		this.tradeRates = tradeRates;
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

	@Override
	public String toString() {
		return "TradeDisplayDTO [title=" + title + ", tradePayment=" + tradePayment + ", tradeNum=" + tradeNum
				+ ", status=" + status + ", sellerFlag=" + sellerFlag + ", tradeRates=" + tradeRates + "]";
	}

}
