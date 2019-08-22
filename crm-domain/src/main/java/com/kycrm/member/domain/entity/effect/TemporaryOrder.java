package com.kycrm.member.domain.entity.effect;

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

public class TemporaryOrder extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	
	/**
	 * 商品图片的绝对路径	
	 */
	private String picPath;
	
	/**
	 * 卖家昵称	
	 */
	private String sellerNick;
	
	/**
	 * 买家昵称	
	 */
	private String buyerNick;
	
	/**
	 * 退款状态。退款状态。可选值 WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)	
	 */
	private	String refundStatus;
	
	/**
	 * 订单超时到期时间。格式:yyyy-MM-dd HH:mm:ss	
	 */
	private	Date timeoutActionTime;
	
	/**
	 * 买家是否已评价。可选值：true(已评价)，false(未评价)	
	 */
	private Boolean buyerRate;
	
	/**
	 * 卖家是否已评价。可选值：true(已评价)，false(未评价)	
	 */
	private Boolean sellerRate;
	
	/**
	 * 卖家类型，可选值为：B（商城商家），C（普通卖家）	
	 */
	private String sellerType;
	
	/**
	 * 交易商品对应的类目ID	
	 */
	private String cid;
	
	/**
	 * 子订单编号	
	 */
	private String oid;
	
	/**
	 * 订单状态（请关注此状态，如果为TRADE_CLOSED_BY_TAOBAO状态，则不要对此订单进行发货，切记啊！）。可选值:	
	 */
	private String status;
	
	/**
	 * 商品标题	
	 */
	private String title;

	/**
	 * 交易类型	
	 */
	private String type;
	
	/**
	 * 商品的字符串编号(注意：iid近期即将废弃，请用num_iid参数)	
	 */
	private String iid;
	
	/**
	 * 商品价格。精确到2位小数;单位:元。如:200.07，表示:200元7分	
	 */
	private String price;
	
	/**
	 * 商品数字ID	
	 */
	private String numIid;
	
	/**
	 * 购买数量。取值范围:大于零的整数	
	 */
	private Integer num;
	
	/**
	 * 子订单来源,如jhs(聚划算)、taobao(淘宝)、wap(无线)	
	 */
	private String orderFrom;
	
	/**
	 * 应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分	
	 */
	private String totalFee;
	
	/**
	 * 子订单实付金额。精确到2位小数，单位:元。如:200.07，表示:200元7分。对于多子订单的交易，计算公式如下：payment = price * num + adjust_fee - discount_fee ；单子订单交易，payment与主订单的payment一致，对于退款成功的子订单，由于主订单的优惠分摊金额，会造成该字段可能不为0.00元。建议使用退款前的实付金额减去退款单中的实际退款金额计算。	
	 */
	private String payment;
	
	/**
	 * 订单修改时间，目前只有taobao.trade.ordersku.update会返回此字段。	
	 */
	private Date modified;
	
	/**
	 * 最近退款ID	
	 */
	private String refundId;
	
	/**
	 * 子订单的交易结束时间说明：子订单有单独的结束时间，与主订单的结束时间可能有所不同，在有退款发起的时候或者是主订单分阶段付款的时候，子订单的结束时间会早于主订单的结束时间，所以开放这个字段便于订单结束状态的判断	
	 */
	private Date endTime;
	
	/**
	 * 子订单发货时间，当卖家对订单进行了多次发货，子订单的发货时间和主订单的发货时间可能不一样了，那么就需要以子订单的时间为准。（没有进行多次发货的订单，主订单的发货时间和子订单的发货时间都一样）	
	 */
	private String consignTime;
	
	/**
	 * 是否发货	
	 */
	private Boolean isShShip;
	
	/**
	 * 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分	
	 */
	private String tradePayment;
	
	/**
	 * 卖家是否已评价。可选值:true(已评价),false(未评价)	
	 */
	private Boolean tradeSellerRate;
	
	/**
	 * 收货人的姓名	
	 */
	private String receiverName;
	
	/**
	 * 收货人的所在省份	
	 */
	private String receiverState;
	
	/**
	 * 收货人的详细地址	
	 */
	private String receiverAddress;
	
	/**
	 * 收货人的手机号码	
	 */
	private String receiverMobile;
	
	/**
	 * 卖家发货时间。格式:yyyy-MM-ddHH:mm:ss	
	 */
	private Date tradeConsignTime;
	
	/**
	 * 交易编号(父订单的交易编号)	
	 */
	private String tid;
	
	/**
	 * 商品购买数量。取值范围：大于零的整数,对于一个trade对应多个order的时候（一笔主订单，对应多笔子订单），num=0，num是一个跟商品关联的属性，一笔订单对应多比子订单的时候，主订单上的num无意义。	
	 */
	private Integer tradeNum;
	
	/**
	 * 交易状态。可选值:*TRADE_NO_CREATE_PAY(没有创建支付宝交易)*WAIT_BUYER_PAY(等待买家付款)*SELLER_CONSIGNED_PART(卖家部分发货)*WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款)*WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货)*TRADE_BUYER_SIGNED(买家已签收,货到付款专用)*TRADE_FINISHED(交易成功)*TRADE_CLOSED(付款以后用户退款成功，交易自动关闭)*TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)*PAY_PENDING(国际信用卡支付付款确认中)*WAIT_PRE_AUTH_CONFIRM(0元购合约中)*PAID_FORBID_CONSIGN(拼团中订单，已付款但禁止发货)	
	 */
	private String tradeStatus;
	
	/**
	 * 商品金额（商品价格乘以数量的总金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分	
	 */
	private String tradeTotalFee;
	
	/**
	 * 交易创建时间。格式:yyyy-MM-ddHH:mm:ss	
	 */
	private Date tradeCreated;
	
	/**
	 * 付款时间。格式:yyyy-MM-ddHH:mm:ss。订单的付款时间即为物流订单的创建时间。	
	 */
	private Date tradePayTime;
	
	/**
	 * 交易修改时间(用户对订单的任何修改都会更新此字段)。格式:yyyy-MM-ddHH:mm:ss	
	 */
	private Date tradeModified;
	
	/**
	 * 交易结束时间。交易成功时间(更新交易状态为成功的同时更新)/确认收货时间或者交易关闭时间。格式:yyyy-MM-ddHH:mm:ss	
	 */
	private Date tradeEndTime;
	
	/**
	 * 买家备注旗帜（与淘宝网上订单的买家备注旗帜对应，只有买家才能查看该字段）红、黄、绿、蓝、紫分别对应1、2、3、4、5	
	 */
	private String buyerFlag;
	
	/**
	 * 卖家备注旗帜（与淘宝网上订单的卖家备注旗帜对应，只有卖家才能查看该字段）红、黄、绿、蓝、紫分别对应1、2、3、4、5	
	 */
	private String sellerFlag;
	
	/**
	 * 交易内部来源。WAP(手机);HITAO(嗨淘);TOP(TOP平台);TAOBAO(普通淘宝);JHS(聚划算)一笔订单可能同时有以上多个标记，则以逗号分隔	
	 */
	private String tradeFrom;
	
	/**
	 * 买家是否已评价。可选值:true(已评价),false(未评价)。如买家只评价未打分，此字段仍返回false	
	 */
	private Boolean tradeBuyerRate;
	
	/**
	 * 收货人的所在城市	
	 */
	private String receiverCity;
	
	/**
	 * 收货人的所在地区	
	 */
	private String receiverDistrict;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getSellerNick() {
		return sellerNick;
	}

	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Date getTimeoutActionTime() {
		return timeoutActionTime;
	}

	public void setTimeoutActionTime(Date timeoutActionTime) {
		this.timeoutActionTime = timeoutActionTime;
	}

	public Boolean getBuyerRate() {
		return buyerRate;
	}

	public void setBuyerRate(Boolean buyerRate) {
		this.buyerRate = buyerRate;
	}

	public Boolean getSellerRate() {
		return sellerRate;
	}

	public void setSellerRate(Boolean sellerRate) {
		this.sellerRate = sellerRate;
	}

	public String getSellerType() {
		return sellerType;
	}

	public void setSellerType(String sellerType) {
		this.sellerType = sellerType;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getOid() {
		return oid;
	}

	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getNumIid() {
		return numIid;
	}

	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getRefundId() {
		return refundId;
	}

	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getConsignTime() {
		return consignTime;
	}

	public void setConsignTime(String consignTime) {
		this.consignTime = consignTime;
	}

	public Boolean getIsShShip() {
		return isShShip;
	}

	public void setIsShShip(Boolean isShShip) {
		this.isShShip = isShShip;
	}

	public String getTradePayment() {
		return tradePayment;
	}

	public void setTradePayment(String tradePayment) {
		this.tradePayment = tradePayment;
	}

	public Boolean getTradeSellerRate() {
		return tradeSellerRate;
	}

	public void setTradeSellerRate(Boolean tradeSellerRate) {
		this.tradeSellerRate = tradeSellerRate;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverState() {
		return receiverState;
	}

	public void setReceiverState(String receiverState) {
		this.receiverState = receiverState;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public Date getTradeConsignTime() {
		return tradeConsignTime;
	}

	public void setTradeConsignTime(Date tradeConsignTime) {
		this.tradeConsignTime = tradeConsignTime;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public Integer getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(Integer tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getTradeTotalFee() {
		return tradeTotalFee;
	}

	public void setTradeTotalFee(String tradeTotalFee) {
		this.tradeTotalFee = tradeTotalFee;
	}

	public Date getTradeCreated() {
		return tradeCreated;
	}

	public void setTradeCreated(Date tradeCreated) {
		this.tradeCreated = tradeCreated;
	}

	public Date getTradePayTime() {
		return tradePayTime;
	}

	public void setTradePayTime(Date tradePayTime) {
		this.tradePayTime = tradePayTime;
	}

	public Date getTradeModified() {
		return tradeModified;
	}

	public void setTradeModified(Date tradeModified) {
		this.tradeModified = tradeModified;
	}

	public Date getTradeEndTime() {
		return tradeEndTime;
	}

	public void setTradeEndTime(Date tradeEndTime) {
		this.tradeEndTime = tradeEndTime;
	}

	public String getBuyerFlag() {
		return buyerFlag;
	}

	public void setBuyerFlag(String buyerFlag) {
		this.buyerFlag = buyerFlag;
	}

	public String getSellerFlag() {
		return sellerFlag;
	}

	public void setSellerFlag(String sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	public String getTradeFrom() {
		return tradeFrom;
	}

	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

	public Boolean getTradeBuyerRate() {
		return tradeBuyerRate;
	}

	public void setTradeBuyerRate(Boolean tradeBuyerRate) {
		this.tradeBuyerRate = tradeBuyerRate;
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}

	public String getReceiverDistrict() {
		return receiverDistrict;
	}

	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}

	
}
