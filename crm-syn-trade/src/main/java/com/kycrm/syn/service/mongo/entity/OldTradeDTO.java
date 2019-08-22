package com.kycrm.syn.service.mongo.entity;

/*import java.math.String;*/
import java.io.Serializable;
import java.util.Date;
import java.util.List;


import org.springframework.data.annotation.Transient;

import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;


public class OldTradeDTO extends BaseMongoDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8276637572844079065L;
	//@MetaData("主键")
	private String _id;
	
	//@MetaData("交易编号")
	@ApiField("tid")
	private String tid;
	
	//@MetaData("卖家昵称")
	@ApiField("seller_nick")
	private String sellerNick;
	
	//@MetaData("商品图片绝对路径")
	@ApiField("pic_path")
	private String picPath;
	
	//@MetaData("实付金额")
	private Double payment;
	
	//@MetaData("实付金额")
	@ApiField("payment")
	@Transient
	private String paymentMoney;
	
	//@MetaData("卖家是否已评价")
	@ApiField("seller_rate")
	private Boolean sellerRate = Boolean.FALSE;
	
	//@MetaData("邮费")
	@ApiField("post_fee")
	private String postFee;
	
	//@MetaData("收货人姓名")
	@ApiField("receiver_name")
	private String receiverName;
	
	//@MetaData("收货人所在省份")
	@ApiField("receiver_state")
	private String receiverState;
	
	//@MetaData("收货人详细地址")
	@ApiField("receiver_address")
	private String receiverAddress;
	
	//@MetaData("收货人邮编")
	@ApiField("receiver_zip")
	private String receiverZip;
	
	//@MetaData("收货人手机号")
	@ApiField("receiver_mobile")
	private String receiverMobile;
	
	//@MetaData("收货人电话号码")
	@ApiField("receiver_phone")
	private String receiverPhone;
	
	//@MetaData("卖家发货时间淘宝")
	@ApiField("consign_time")
	private Date consignTimeUTC;
	
	//@MetaData("卖家发货时间本地")
	private Long consignTime;
	
	//@MetaData("卖家实际收到的支付宝打款金额")
	private Double receivedPayment;
	
	//@MetaData("卖家实际收到的支付宝打款金额")
	@ApiField("received_payment")
	@Transient
	private String receivedPaymentMoney;
	
	//@MetaData("商家的预计发货时间")
	@ApiField("est_con_time")
	private String estConTime;
	
	//@MetaData("发票类型 1-电子发票 2-纸质发票")
	@ApiField("invoice_kind")
	private String invoiceKind;
	
	//@MetaData("收货人国籍")
	@ApiField("receiver_country")
	private String receiverCountry;
	
	//@MetaData("收货人街道地址")
	@ApiField("receiver_town")
	private String receiverTown;
	
	//@MetaData("天猫国际官网直供主订单关税税费")
	@ApiField("order_tax_fee")
	private Integer orderTaxFee;
	
	//@MetaData("返红包的金额")
	@ApiField("paid_coupon_fee")
	private Double paidCouponFee;
	
	//@MetaData("门店自提，总店发货，分店取货的门店自提订单标识")
	@ApiField("shop_pick")
	private String shopPick;
	
	//@MetaData("商品购买数量")
	private Long num;
	
	//@MetaData("商品购买数量")
	@ApiField("num")
	@Transient
	private String numGoods;
	
	//@MetaData("商品数字编号本地的 用户crm取值")
	private String NUM_IID;
	
	//@MetaData("商品数字编号淘宝的")
	@ApiField("num_iid")
	@Transient
	private Long numIid;
	
	//@MetaData("交易状态 1-没有创建支付宝交易 2-等待买家付款 3-卖家部分发货 4-等待卖家发货，即买家已付款 5-等待买家确认收货，及卖家已发货 6-买家已签收，货到付款专用 7-交易成功")
	@ApiField("status")
	private String status;
	//@MetaData("交易标题")
	@ApiField("title")
	private String title;
	
	//@MetaData("交易类型列表  import--导入的订单")
	@ApiField("type")
	private String type;
	
	//@MetaData("商品价格")
	private Double price;
	//@MetaData("商品价格")
	@ApiField("price")
	@Transient
	private String priceMoney;
	
	//@MetaData("优惠金额")
	@ApiField("discount_fee")
	private String discountFee;
	
	//@MetaData("是否包含邮费")
	@ApiField("has_post_fee")
	private Boolean hasPostFee = Boolean.FALSE;
	
	//@MetaData("商品金额")
	private Double totalFee;
	//@MetaData("商品金额")
	@ApiField("total_fee")
	@Transient
	private String totalFeeMoney;
	
	//@MetaData("交易创建时间淘宝")
	@ApiField("created")
	private Date createdUTC;
	
	//@MetaData("交易创建时间淘宝本地")
	private Long created;
	
	//@MetaData("付款时间淘宝")
	@ApiField("pay_time")
	private Date payTimeUTC;
	//@MetaData("付款时间本地")
	private Long payTime;
	
	//@MetaData("交易修改时间淘宝")
	@ApiField("modified")
	private Date modifiedUTC;
	//@MetaData("交易修改时间本地")
	private Long modified;
	
	//@MetaData("交易结束时间淘宝")
	@ApiField("end_time")
	private Date endTimeUTC;
	
	//@MetaData("交易结束时间本地")
	private Long endTime;
	
	//@MetaData("买家留言")
	@ApiField("has_buyer_message")
	private String buyerMessage;
	
	//@MetaData("买家备注")
	@ApiField("buyer_memo")
	private String buyerMemo;
	
	//@MetaData("买家备注旗帜")
	@ApiField("buyer_flag")
	private Integer buyerFlag;
	
	//@MetaData("卖家备注")
	@ApiField("seller_memo")
	private String sellerMemo;
	
	//@MetaData("卖家备注旗帜")
	@ApiField("seller_flag")
	private String sellerFlag = "0";
	
	//@MetaData("发票抬头")
	@ApiField("invoice_name")
	private String invoiceName;
	
	//@MetaData("发票类型")
	@ApiField("invoice_type")
	private String invoiceType;
	
	//@MetaData("买家昵称")
	@ApiField("buyer_nick")
	private String buyerNick;
	
	//@MetaData("top动态字段")
	@ApiField("trade_attr")
	private String tradeAttr;
	
	//@MetaData("使用信用卡支付金额数")
	@ApiField("credit_card_fee")
	private String creditCardFee;
	
	//@MetaData("分阶段付款的状态")
	@ApiField("step_trade_status")
	private String stepTradeStatus;
	
	//@MetaData("分阶段付款的已付款金额")
	@ApiField("step_paid_fee")
	private String stepPaidFee;
	
	//@MetaData("订单异常给用户的描述")
	@ApiField("mark_desc")
	private String markDesc;
	
	//@MetaData("创建交易的物流方式")
	@ApiField("shipping_type")
	private String shippingType;
	
	//@MetaData("买家货到付款服务费")
	@ApiField("buyer_cod_fee")
	private String buyerCodFee;
	
	//@MetaData("卖家手工调整金额")
	@ApiField("adjust_fee")
	private String adjustFee;
	
	//@MetaData("交易内部来源")
	@ApiField("trade_from")
	private String tradeFrom;
	
	//@MetaData("买家是否已评价")
	@ApiField("buyer_rate")
	private Boolean buyerRate = Boolean.FALSE;
	
	//@MetaData("收货人的所在城市")
	@ApiField("receiver_city")
	private String receiverCity;
	
	//@MetaData("收货人所在地区")
	@ApiField("receiver_district")
	private String receiverDistrict;
	
	//@MetaData("导购宝")
	@ApiField("o2o")
	private String o2o;
	
	//@MetaData("导购员ID")
	@ApiField("o2o_guide_id")
	private String o2oGuideId;
	
	//@MetaData("导购员门店ID")
	@ApiField("o2o_shop_id")
	private String o2oShopId;
	
	//@MetaData("导购员名称")
	@ApiField("o2o_guide_name")
	private String o2oGuideName;
	
	//@MetaData("导购员门店名称")
	@ApiField("o2o_shop_name")
	private String o2oShopName;
	
	//@MetaData("导购宝提货方式 1-店内提货 2-线上发货")
	@ApiField("o2o_delivery")
	private String o2oDelivery;
	
	//@MetaData("天猫电子凭证家装")
	@ApiField("eticket_service_addr")
	private String eticketServiceAddr;
	
	//@MetaData("处方药为审核状态")
	@ApiField("rx_audit_status")
	private String rxAuditStatus;
	
	//@MetaData("时间段")
	@ApiField("es_range")
	private String esRange;
	
	//@MetaData("时间")
	@ApiField("es_date")
	private String esDate;
	
	//@MetaData("时间")
	@ApiField("os_date")
	private String osDate;
	
	//@MetaData("时间段")
	@ApiField("os_range")
	private String osRange;
	
	//@MetaData("订单中使用红包付款的金额")
	@ApiField("coupon_fee")
	private Integer couponFee;
	
	//@MetaData("是否邮关订单")
	@ApiField("post_gate_declare")
	private Boolean postGateDeclare = Boolean.FALSE;
	
	//@MetaData("是否为跨境订单")
	@ApiField("cross_bonded_declare")
	private Boolean crossBondedDeclare = Boolean.FALSE;
	
	//@MetaData("渠道商品相关字段")
	@ApiField("omnichannel_param")
	private String omnichannelParam;
	
	//@MetaData("组合商品")
	@ApiField("assembly")
	private String assembly;
	
	//@MetaData("top拦截标识 0-不拦截 1-拦截")
	@ApiField("top_hold")
	private Integer topHold;
	
	//@MetaData("星盘标识字段")
	@ApiField("omni_attr")
	private String omniAttr;
	
	//@MetaData("星盘业务字段")
	@ApiField("omni_param")
	private String omniParam;
	
	//@MetaData("是否屏蔽发货")
	@ApiField("is_sh_ship")
	private Boolean isShShip =Boolean.FALSE;
	
	//@MetaData("抢单状态 1-未处理待分发 2-抢单中 3-已发货")
	@ApiField("o2o_snatch_status")
	private String o2oSnatchStatus;
	
	//@MetaData("垂直市场")
	@ApiField("market")
	private String market;
	
	//@MetaData("电子凭证扫码购")
	@ApiField("et_type")
	private String etType;
	
	//@MetaData("扫码购关联门店")
	@ApiField("et_shop_id")
	private Long etShopId;
	
	//@MetaData("门店预约自提订单标")
	@ApiField("obs")
	private String obs;
	
	/**
	 * 买家邮件地址
	 */
	//@MetaData("用户的邮箱地址")
	@ApiField("buyer_email")
	private String buyerEmail;
	
	/**
	 * 买家支付宝账号
	 */
	@ApiField("buyer_alipay_no")
	private String buyer_alipay_no;
	
	@ApiField("alipay_id")
	private Long alipayId;
	
	/**
	 * 支付宝交易号，如：2009112081173831
	 */
	@ApiField("alipay_no")
	private String alipayNo;
	
	/**
	 * 创建交易接口成功后，返回的支付url
	 */
	@ApiField("alipay_url")
	private String alipayUrl;
	
	/**
	 * 区域id，代表订单下单的区位码，区位码是通过省市区转换而来，通过区位码能精确到区内的划分，比如310012是杭州市西湖区华星路
	 */
	@ApiField("area_id")
	private String areaId;
	
	/**
	 * 物流到货时效截单时间，格式 HH:mm
	 */
	@ApiField("arrive_cut_time")
	private String arriveCutTime;
	
	
	/**
	 * 物流到货时效，单位天
	 */
	@ApiField("arrive_interval")
	private Long arriveInterval;

	/**
	 * 同步到卖家库的时间，taobao.trades.sold.incrementv.get接口返回此字段
	 */
	@ApiField("async_modified")
	private Date asyncModified;
	
	/**
	 * 买家下单的地区
	 */
	@ApiField("buyer_area")
	private String buyerArea;
	
	/**
	 * 买家下单的IP信息，仅供taobao.trade.fullinfo.get查询返回。需要对返回结果进行Base64解码。
	 */
	@ApiField("buyer_ip")
	private String buyerIp;
	
	/**
	 * 买家获得积分,返点的积分。格式:100;单位:个。返点的积分要交易成功之后才能获得。
	 */
	@ApiField("buyer_obtain_point_fee")
	private Long buyerObtainPointFee;

	//@MetaData("最近短信发送Id")
	private  Long msgId  ;
	//@MetaData("最近短信发送时间")
	private  Long lastSendSmsTime  ;
	
	//@MetaData("节点标识")
	private  Long nodeFlag  ;
 
	@ApiListField("orders")
	@ApiField("order")
	private  List<OldOrdersDTO>  orders;
	

	
	public Long getNodeFlag() {
		return nodeFlag;
	}

	public void setNodeFlag(Long nodeFlag) {
		this.nodeFlag = nodeFlag;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public Long getLastSendSmsTime() {
		return lastSendSmsTime;
	}

	public void setLastSendSmsTime(Long lastSendSmsTime) {
		this.lastSendSmsTime = lastSendSmsTime;
	}

	/**
	 * 标识是否退款:退款推送为true,没有推送为false
	 * */
	private boolean refundFlag;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getSellerNick() {
		return sellerNick;
	}

	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public Double getPayment() {
		return payment;
	}

	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public String getPaymentMoney() {
		return paymentMoney;
	}

	public void setPaymentMoney(String paymentMoney) {
		this.paymentMoney = paymentMoney;
	}

	public Boolean getSellerRate() {
		return sellerRate;
	}

	public void setSellerRate(Boolean sellerRate) {
		this.sellerRate = sellerRate;
	}

	public String getPostFee() {
		return postFee;
	}

	public void setPostFee(String postFee) {
		this.postFee = postFee;
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

	public String getReceiverZip() {
		return receiverZip;
	}

	public void setReceiverZip(String receiverZip) {
		this.receiverZip = receiverZip;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
 

	

	public String getEstConTime() {
		return estConTime;
	}

	public void setEstConTime(String estConTime) {
		this.estConTime = estConTime;
	}

	public String getInvoiceKind() {
		return invoiceKind;
	}

	public void setInvoiceKind(String invoiceKind) {
		this.invoiceKind = invoiceKind;
	}

	public String getReceiverCountry() {
		return receiverCountry;
	}

	public void setReceiverCountry(String receiverCountry) {
		this.receiverCountry = receiverCountry;
	}

	public String getReceiverTown() {
		return receiverTown;
	}

	public void setReceiverTown(String receiverTown) {
		this.receiverTown = receiverTown;
	}

	public Integer getOrderTaxFee() {
		return orderTaxFee;
	}

	public void setOrderTaxFee(Integer orderTaxFee) {
		this.orderTaxFee = orderTaxFee;
	}

	public Double getPaidCouponFee() {
		return paidCouponFee;
	}

	public void setPaidCouponFee(Double paidCouponFee) {
		this.paidCouponFee = paidCouponFee;
	}

	public String getShopPick() {
		return shopPick;
	}

	public void setShopPick(String shopPick) {
		this.shopPick = shopPick;
	}

	 

	public String getNUM_IID() {
		return NUM_IID;
	}

	public void setNUM_IID(String nUM_IID) {
		NUM_IID = nUM_IID;
	}

	public Long getNumIid() {
		return numIid;
	}

	public void setNumIid(Long numIid) {
		this.numIid = numIid;
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

 

	public String getDiscountFee() {
		return discountFee;
	}

	public void setDiscountFee(String discountFee) {
		this.discountFee = discountFee;
	}

	public Boolean getHasPostFee() {
		return hasPostFee;
	}

	public void setHasPostFee(Boolean hasPostFee) {
		this.hasPostFee = hasPostFee;
	}

	

	
	 

	public String getReceivedPaymentMoney() {
		return receivedPaymentMoney;
	}

	public void setReceivedPaymentMoney(String receivedPaymentMoney) {
		this.receivedPaymentMoney = receivedPaymentMoney;
	}

	public Long getNum() {
		return num;
	}

	public void setNum(Long num) {
		this.num = num;
	}

	public String getNumGoods() {
		return numGoods;
	}

	public void setNumGoods(String numGoods) {
		this.numGoods = numGoods;
	}

	 

	public String getPriceMoney() {
		return priceMoney;
	}

	public void setPriceMoney(String priceMoney) {
		this.priceMoney = priceMoney;
	}

	public Double getReceivedPayment() {
		return receivedPayment;
	}

	public void setReceivedPayment(Double receivedPayment) {
		this.receivedPayment = receivedPayment;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(Double totalFee) {
		this.totalFee = totalFee;
	}

	public String getTotalFeeMoney() {
		return totalFeeMoney;
	}

	public void setTotalFeeMoney(String totalFeeMoney) {
		this.totalFeeMoney = totalFeeMoney;
	}

	public Date getConsignTimeUTC() {
		return consignTimeUTC;
	}

	public void setConsignTimeUTC(Date consignTimeUTC) {
		this.consignTimeUTC = consignTimeUTC;
	}

	public Long getConsignTime() {
		return consignTime;
	}

	public void setConsignTime(Long consignTime) {
		this.consignTime = consignTime;
	}

	public Date getCreatedUTC() {
		return createdUTC;
	}

	public void setCreatedUTC(Date createdUTC) {
		this.createdUTC = createdUTC;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Date getPayTimeUTC() {
		return payTimeUTC;
	}

	public void setPayTimeUTC(Date payTimeUTC) {
		this.payTimeUTC = payTimeUTC;
	}

	public Long getPayTime() {
		return payTime;
	}

	public void setPayTime(Long payTime) {
		this.payTime = payTime;
	}

	public Date getModifiedUTC() {
		return modifiedUTC;
	}

	public void setModifiedUTC(Date modifiedUTC) {
		this.modifiedUTC = modifiedUTC;
	}

	public Long getModified() {
		return modified;
	}

	public void setModified(Long modified) {
		this.modified = modified;
	}

	public Date getEndTimeUTC() {
		return endTimeUTC;
	}

	public void setEndTimeUTC(Date endTimeUTC) {
		this.endTimeUTC = endTimeUTC;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public String getBuyerMessage() {
		return buyerMessage;
	}

	public void setBuyerMessage(String buyerMessage) {
		this.buyerMessage = buyerMessage;
	}

	public String getBuyerMemo() {
		return buyerMemo;
	}

	public void setBuyerMemo(String buyerMemo) {
		this.buyerMemo = buyerMemo;
	}

	public Integer getBuyerFlag() {
		return buyerFlag;
	}

	public void setBuyerFlag(Integer buyerFlag) {
		this.buyerFlag = buyerFlag;
	}

	public String getSellerMemo() {
		return sellerMemo;
	}

	public void setSellerMemo(String sellerMemo) {
		this.sellerMemo = sellerMemo;
	}

	public String getSellerFlag() {
		return sellerFlag;
	}

	public void setSellerFlag(String sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	public String getInvoiceName() {
		return invoiceName;
	}

	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	public String getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getTradeAttr() {
		return tradeAttr;
	}

	public void setTradeAttr(String tradeAttr) {
		this.tradeAttr = tradeAttr;
	}

	public String getCreditCardFee() {
		return creditCardFee;
	}

	public void setCreditCardFee(String creditCardFee) {
		this.creditCardFee = creditCardFee;
	}

	public String getStepTradeStatus() {
		return stepTradeStatus;
	}

	public void setStepTradeStatus(String stepTradeStatus) {
		this.stepTradeStatus = stepTradeStatus;
	}

	public String getStepPaidFee() {
		return stepPaidFee;
	}

	public void setStepPaidFee(String stepPaidFee) {
		this.stepPaidFee = stepPaidFee;
	}

	public String getMarkDesc() {
		return markDesc;
	}

	public void setMarkDesc(String markDesc) {
		this.markDesc = markDesc;
	}

	public String getShippingType() {
		return shippingType;
	}

	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}

	public String getBuyerCodFee() {
		return buyerCodFee;
	}

	public void setBuyerCodFee(String buyerCodFee) {
		this.buyerCodFee = buyerCodFee;
	}

	public String getAdjustFee() {
		return adjustFee;
	}

	public void setAdjustFee(String adjustFee) {
		this.adjustFee = adjustFee;
	}

	public String getTradeFrom() {
		return tradeFrom;
	}

	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

	public Boolean getBuyerRate() {
		return buyerRate;
	}

	public void setBuyerRate(Boolean buyerRate) {
		this.buyerRate = buyerRate;
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

	public String getO2o() {
		return o2o;
	}

	public void setO2o(String o2o) {
		this.o2o = o2o;
	}

	public String getO2oGuideId() {
		return o2oGuideId;
	}

	public void setO2oGuideId(String o2oGuideId) {
		this.o2oGuideId = o2oGuideId;
	}

	public String getO2oShopId() {
		return o2oShopId;
	}

	public void setO2oShopId(String o2oShopId) {
		this.o2oShopId = o2oShopId;
	}

	public String getO2oGuideName() {
		return o2oGuideName;
	}

	public void setO2oGuideName(String o2oGuideName) {
		this.o2oGuideName = o2oGuideName;
	}

	public String getO2oShopName() {
		return o2oShopName;
	}

	public void setO2oShopName(String o2oShopName) {
		this.o2oShopName = o2oShopName;
	}

	public String getO2oDelivery() {
		return o2oDelivery;
	}

	public void setO2oDelivery(String o2oDelivery) {
		this.o2oDelivery = o2oDelivery;
	}

	public String getEticketServiceAddr() {
		return eticketServiceAddr;
	}

	public void setEticketServiceAddr(String eticketServiceAddr) {
		this.eticketServiceAddr = eticketServiceAddr;
	}

	public String getRxAuditStatus() {
		return rxAuditStatus;
	}

	public void setRxAuditStatus(String rxAuditStatus) {
		this.rxAuditStatus = rxAuditStatus;
	}

	public String getEsRange() {
		return esRange;
	}

	public void setEsRange(String esRange) {
		this.esRange = esRange;
	}

	public String getEsDate() {
		return esDate;
	}

	public void setEsDate(String esDate) {
		this.esDate = esDate;
	}

	public String getOsDate() {
		return osDate;
	}

	public void setOsDate(String osDate) {
		this.osDate = osDate;
	}

	public String getOsRange() {
		return osRange;
	}

	public void setOsRange(String osRange) {
		this.osRange = osRange;
	}

	public Integer getCouponFee() {
		return couponFee;
	}

	public void setCouponFee(Integer couponFee) {
		this.couponFee = couponFee;
	}

	public Boolean getPostGateDeclare() {
		return postGateDeclare;
	}

	public void setPostGateDeclare(Boolean postGateDeclare) {
		this.postGateDeclare = postGateDeclare;
	}

	public Boolean getCrossBondedDeclare() {
		return crossBondedDeclare;
	}

	public void setCrossBondedDeclare(Boolean crossBondedDeclare) {
		this.crossBondedDeclare = crossBondedDeclare;
	}

	public String getOmnichannelParam() {
		return omnichannelParam;
	}

	public void setOmnichannelParam(String omnichannelParam) {
		this.omnichannelParam = omnichannelParam;
	}

	public String getAssembly() {
		return assembly;
	}

	public void setAssembly(String assembly) {
		this.assembly = assembly;
	}

	public Integer getTopHold() {
		return topHold;
	}

	public void setTopHold(Integer topHold) {
		this.topHold = topHold;
	}

	public String getOmniAttr() {
		return omniAttr;
	}

	public void setOmniAttr(String omniAttr) {
		this.omniAttr = omniAttr;
	}

	public String getOmniParam() {
		return omniParam;
	}

	public void setOmniParam(String omniParam) {
		this.omniParam = omniParam;
	}

	public Boolean getIsShShip() {
		return isShShip;
	}

	public void setIsShShip(Boolean isShShip) {
		this.isShShip = isShShip;
	}

	public String getO2oSnatchStatus() {
		return o2oSnatchStatus;
	}

	public void setO2oSnatchStatus(String o2oSnatchStatus) {
		this.o2oSnatchStatus = o2oSnatchStatus;
	}

	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getEtType() {
		return etType;
	}

	public void setEtType(String etType) {
		this.etType = etType;
	}

	public Long getEtShopId() {
		return etShopId;
	}

	public void setEtShopId(Long etShopId) {
		this.etShopId = etShopId;
	}

	public String getObs() {
		return obs;
	}

	public void setObs(String obs) {
		this.obs = obs;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	public String getBuyer_alipay_no() {
		return buyer_alipay_no;
	}

	public void setBuyer_alipay_no(String buyer_alipay_no) {
		this.buyer_alipay_no = buyer_alipay_no;
	}

	public List<OldOrdersDTO> getOrders() {
		return orders;
	}

	public void setOrders(List<OldOrdersDTO> orders) {
		this.orders = orders;
	}

	

	public boolean isRefundFlag() {
		return refundFlag;
	}

	public void setRefundFlag(boolean refundFlag) {
		this.refundFlag = refundFlag;
	}
	

	public Long getAlipayId() {
		return alipayId;
	}

	public void setAlipayId(Long alipayId) {
		this.alipayId = alipayId;
	}

	public String getAlipayNo() {
		return alipayNo;
	}

	public void setAlipayNo(String alipayNo) {
		this.alipayNo = alipayNo;
	}

	public String getAlipayUrl() {
		return alipayUrl;
	}

	public void setAlipayUrl(String alipayUrl) {
		this.alipayUrl = alipayUrl;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getArriveCutTime() {
		return arriveCutTime;
	}

	public void setArriveCutTime(String arriveCutTime) {
		this.arriveCutTime = arriveCutTime;
	}

	public Long getArriveInterval() {
		return arriveInterval;
	}

	public void setArriveInterval(Long arriveInterval) {
		this.arriveInterval = arriveInterval;
	}

	public Date getAsyncModified() {
		return asyncModified;
	}

	public void setAsyncModified(Date asyncModified) {
		this.asyncModified = asyncModified;
	}

	public String getBuyerArea() {
		return buyerArea;
	}

	public void setBuyerArea(String buyerArea) {
		this.buyerArea = buyerArea;
	}

	public String getBuyerIp() {
		return buyerIp;
	}

	public void setBuyerIp(String buyerIp) {
		this.buyerIp = buyerIp;
	}

	public Long getBuyerObtainPointFee() {
		return buyerObtainPointFee;
	}

	public void setBuyerObtainPointFee(Long buyerObtainPointFee) {
		this.buyerObtainPointFee = buyerObtainPointFee;
	}

	@Override
	public String toString() {
		return "TradeDTO [_id=" + _id + ", tid=" + tid + ", sellerNick=" + sellerNick + ", picPath=" + picPath
				+ ", payment=" + payment + ", paymentMoney=" + paymentMoney + ", sellerRate=" + sellerRate
				+ ", postFee=" + postFee + ", receiverName=" + receiverName + ", receiverState=" + receiverState
				+ ", receiverAddress=" + receiverAddress + ", receiverZip=" + receiverZip + ", receiverMobile="
				+ receiverMobile + ", receiverPhone=" + receiverPhone + ", consignTime=" + consignTime
				+ ", receivedPayment=" + receivedPayment + ", estConTime=" + estConTime + ", invoiceKind=" + invoiceKind
				+ ", receiverCountry=" + receiverCountry + ", receiverTown=" + receiverTown + ", orderTaxFee="
				+ orderTaxFee + ", paidCouponFee=" + paidCouponFee + ", shopPick=" + shopPick + ", num=" + num
				+ ", NUM_IID=" + NUM_IID + ", numIid=" + numIid + ", status=" + status + ", title=" + title + ", type="
				+ type + ", price=" + price + ", discountFee=" + discountFee + ", hasPostFee=" + hasPostFee
				+ ", totalFee=" + totalFee + ", created=" + created + ", payTime=" + payTime + ", modified=" + modified
				+ ", endTime=" + endTime + ", buyerMessage=" + buyerMessage + ", buyerMemo=" + buyerMemo
				+ ", buyerFlag=" + buyerFlag + ", sellerMemo=" + sellerMemo + ", sellerFlag=" + sellerFlag
				+ ", invoiceName=" + invoiceName + ", invoiceType=" + invoiceType + ", buyerNick=" + buyerNick
				+ ", tradeAttr=" + tradeAttr + ", creditCardFee=" + creditCardFee + ", stepTradeStatus="
				+ stepTradeStatus + ", stepPaidFee=" + stepPaidFee + ", markDesc=" + markDesc + ", shippingType="
				+ shippingType + ", buyerCodFee=" + buyerCodFee + ", adjustFee=" + adjustFee + ", tradeFrom="
				+ tradeFrom + ", buyerRate=" + buyerRate + ", receiverCity=" + receiverCity + ", receiverDistrict="
				+ receiverDistrict + ", o2o=" + o2o + ", o2oGuideId=" + o2oGuideId + ", o2oShopId=" + o2oShopId
				+ ", o2oGuideName=" + o2oGuideName + ", o2oShopName=" + o2oShopName + ", o2oDelivery=" + o2oDelivery
				+ ", eticketServiceAddr=" + eticketServiceAddr + ", rxAuditStatus=" + rxAuditStatus + ", esRange="
				+ esRange + ", esDate=" + esDate + ", osDate=" + osDate + ", osRange=" + osRange + ", couponFee="
				+ couponFee + ", postGateDeclare=" + postGateDeclare + ", crossBondedDeclare=" + crossBondedDeclare
				+ ", omnichannelParam=" + omnichannelParam + ", assembly=" + assembly + ", topHold=" + topHold
				+ ", omniAttr=" + omniAttr + ", omniParam=" + omniParam + ", isShShip=" + isShShip
				+ ", o2oSnatchStatus=" + o2oSnatchStatus + ", market=" + market + ", etType=" + etType + ", etShopId="
				+ etShopId + ", obs=" + obs + ", buyerEmail=" + buyerEmail + ", buyer_alipay_no=" + buyer_alipay_no
				+ ", orders=" + orders + ",refundFlag=" + refundFlag + "]";
	}
	
	
	
	

}
