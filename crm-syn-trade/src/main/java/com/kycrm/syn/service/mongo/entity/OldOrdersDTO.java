package com.kycrm.syn.service.mongo.entity;

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Transient;

import com.taobao.api.internal.mapping.ApiField;

 
public class OldOrdersDTO extends BaseMongoDTO implements Serializable{

	
	private static final long serialVersionUID = -3758420426943761452L;

	//@MetaData("子订单ID")
	@ApiField("oid")
	private String oid;
	
	//@MetaData("父订单ID")
	private String tid;
	
	//@MetaData("交易商品所对应的类目ID")
	@ApiField("cid")
	private Long cid;
	
	//@MetaData("商品数字ID")
	@ApiField("num_iid")
	private Long numIid;
	
	//@MetaData("套餐ID")
	@ApiField("item_meal_id")
	private Long itemMealId;
	
	//@MetaData("商品的最小库存sku的ID")
	@ApiField("sku_id")
	private String skuId;
	
	//@MetaData("最近退款ID")
	@ApiField("refund_id")
	private Long refundId;
	
	//@MetaData("捆绑的子订单号")
	@ApiField("bind_oid")
	private Long bindOid;
	
	//@MetaData("套餐值")
	@ApiField("item_meal_name")
	private String itemMealName;
	
	//@MetaData("商品图片的绝对路径")
	@ApiField("pic_path")
	private String picPath;
	
	//@MetaData("卖家昵称")
	@ApiField("seller_nick")
	private String sellerNick;
	
	//@MetaData("买家昵称")
	@ApiField("buyer_nick")
	private String buyerNick;
	
	//@MetaData("退款状态 WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意) WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货) WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)   1-买家申请退款，等待卖家同意 2-卖家同意退款，等待买家退货3-买家已经退货，等待卖家确认收货 4- 卖家拒绝退款 5-退款关闭 6-退款成功")
	@ApiField("refund_status")
	private String refundStatus;
	
	//@MetaData("商家外部编码")
	@ApiField("outer_iid")
	private String outeriid;
	
	//@MetaData("订单快照URL")
	@ApiField("snapshot_url")
	private String snapshotUrl;
	
	//@MetaData("订单快照详细信息")
	@ApiField("snapshot")
	private String snapshot;
	
	//@MetaData("订单超时到期时间淘宝")
	@ApiField("timeout_action_time")
	private Date timeoutActionTimeUTC;
	//@MetaData("订单超时到期时间本地")
	private Long timeoutActionTime;
	
	//@MetaData("买家是否已评价")
	@ApiField("buyer_rate")
	private Boolean buyerRate;
	
	//@MetaData("卖家是否已评价")
	@ApiField("seller_rate")
	private Boolean sellerRate;
	
	//@MetaData("卖家类型 1-商城商家  2-普通卖家")
	@ApiField("seller_type")
	private String sellerType;
	
	//@MetaData("天猫子订单税费")
	@ApiField("sub_order_tax_fee")
	private String subOrderTaxFee;
	
	//@MetaData("天猫子订单税率")
	@ApiField("sub_order_tax_rate")
	private String subOrderTaxRate;
	
	//@MetaData("订单状态")
	/**
	 * 订单状态（请关注此状态，如果为TRADE_CLOSED_BY_TAOBAO状态，则不要对此订单进行发货，切记啊！）。可选值: 
	 * <ul>
	 * <li>TRADE_NO_CREATE_PAY(没有创建支付宝交易) </li>
	 * <li>WAIT_BUYER_PAY(等待买家付款) </li>
	 * <li>WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款)</li>
	 * <li>WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货) </li>
	 * <li>TRADE_BUYER_SIGNED(买家已签收,货到付款专用) </li>
	 * <li>TRADE_FINISHED(交易成功) </li>
	 * <li>TRADE_CLOSED(付款以后用户退款成功，交易自动关闭) </li>
	 * <li>TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)</li>
	 * <li>PAY_PENDING(国际信用卡支付付款确认中)</li>
	 * </ul>
	 */
	@ApiField("status")
	private String status;
	
	//@MetaData("商品标题")
	@ApiField("title")
	private String title;
	
	//@MetaData("交易类型     import--导入的订单")
	@ApiField("type")
	private String type;
	
	//@MetaData("商品字符串编号")
	@ApiField("iid")
	private String iid;
	
	//@MetaData("商品价格")
	private Double price;
	//@MetaData("商品价格")
	@ApiField("price")
	@Transient
	private String priceMoney;
	
	//@MetaData("购买数量")
	@ApiField("num")
	private Long num;
	
	//@MetaData("外部网店自己定义的sku编号")
	@ApiField("outer_sku_id")
	private String outerSkuId;
	
	//@MetaData("子订单来源")
	@ApiField("order_from")
	private String orderFrom;
	
	//@MetaData("应付金额")
	private Double totalFee;
	//@MetaData("应付金额")
	@ApiField("total_fee")
	@Transient
	private String totalFeeMoney;
	
	//@MetaData("子订单应付金额本地")
	private Double payment;
	
	//@MetaData("子订单应付金额淘宝")
	@ApiField("payment")
	private String paymentMoney;
	
	//@MetaData("子订单优惠金额")
	@ApiField("discount_fee")
	private String discountFee;
	
	//@MetaData("手工调整金额")
	@ApiField("adjust_fee")
	private String adjustFee;
	
	//@MetaData("订单修改时间淘宝")
	@ApiField("modified")
	private Date modifiedUTC;
	//@MetaData("订单修改时间本地")
	private Long modified;
	
	//@MetaData("sku的值")
	@ApiField("sku_properties_name")
	private String skuPropertiesName;
	
	//@MetaData("是否超卖")
	@ApiField("is_oversold")
	private Boolean isOversold = Boolean.FALSE;
	
	//@MetaData("是否为服务订单")
	@ApiField("is_service_order")
	private Boolean isServiceOrder = Boolean.FALSE;
	
	//@MetaData("子订单交易结束时间淘宝")
	@ApiField("end_time")
	private Date endTimeUTC;
	//@MetaData("子订单交易结束时间本地")
	private Long endTime;
	
	//@MetaData("子订单发货时间")
	@ApiField("consign_time")
	private String consignTime;
	
	//@MetaData("top动态字段")
	@ApiField("order_attr")
	private String orderAttr;
	
	//@MetaData("子订单运送方式")
	@ApiField("shipping_type")
	private String shippingType;
	
	//@MetaData("子订单发货的快递公司")
	@ApiField("logistics_company")
	private String logisticsCompany;
	
	//@MetaData("子订单包裹的运单号")
	@ApiField("invoice_no")
    private String invoiceNo;
	
	//@MetaData("订单是否有相应的代销采购单")
	@ApiField("is_daixiao")
	private Boolean isdaixiao = Boolean.FALSE;
	
	//@MetaData("分摊之后的实付金额")
	@ApiField("divide_order_fee")
	private String divideOrderFee;
	
	//@MetaData("优惠分摊")
	@ApiField("part_mjz_discount")
	private String partMjzDiscount;
	
	//@MetaData("对用门票有效的外部ID")
	@ApiField("ticket_outer_id")
	private String ticketOuterId;
	
	//@MetaData("门票有效期的key")
	@ApiField("ticket_expdate_key")
	private String ticketExpdateKey;
	
	//@MetaData("发货的仓库编码")
	@ApiField("store_code")
	private String storeCode;
	
	//@MetaData("订单是否是www订单")
	@ApiField("is_www")
	private Boolean isWww = Boolean.FALSE;
	
	//@MetaData("支持家装类的物流")
	@ApiField("tmser_spu_code")
	private String tmserSpuCode;
	
	//@MetaData("bind_oid字段的升级")
	@ApiField("bind_oids")
	private String bindOids;
	
	//@MetaData("预售订单征集状态 1-征集中 2-征集成功 3-征集失败")
	@ApiField("zhengji_status")
	private String zhengjiStatus;
	
	//@MetaData("免单资格属性")
	@ApiField("md_qualification")
	private String mdQualification;
	
	//@MetaData("免单金额")
	@ApiField("md_fee")
	private String mdFee;
	
	//@MetaData("定制信息")
	@ApiField("customization")
	private String customization;
	
	//@MetaData("库存类型")
	@ApiField("inv_type")
	private String invType;
	
	//@MetaData("是否发货 1- 是 2-否")
	@ApiField("is_sh_ship")
	private Boolean isShShip=Boolean.FALSE;
	
	//@MetaData("仓储信息")
	@ApiField("shipper")
	private String shipper;
	
	//@MetaData("订单履行类型")
	@ApiField("f_type")
	private String fType;
	
	//@MetaData("订单履行状态")
	@ApiField("f_status")
	private String fStatus;
	
	//@MetaData("订单履行内容")
	@ApiField("f_term")
	private String FTERM;
	
	//@MetaData("天猫搭配包")
	@ApiField("combo_id")
	private String comboId;
	
	//@MetaData("主商品订单ID")
	@ApiField("assembly_rela")
	private String assemblyRela;
	
	//@MetaData("价格")
	@ApiField("assembly_price")
	private String assemblyPrice;
	
	//@MetaData("assemblyItem")
	@ApiField("assembly_item")
	private String assemblyItem;

	//@MetaData("收货人所在地区")
	private String receiverDistrict;
	
	//@MetaData("收货人所在城市")
	private String receiverCity;
	
	//@MetaData("分段付款状态")
	private	String stepTradeStatus;
	
	//@MetaData("订单创建时间")
	private Date createdUTC;
	//@MetaData("订单创建时间")
	private Long created;
	
	//@MetaData("收货人姓名")
	private String receiverName;
	
	//@MetaData("收货人手机")
	private String receiverMobile;
	
	//@MetaData("买家备注旗帜0-灰代表全部，1-红，2-黄，3-绿，4-蓝，5-紫")
	private Integer buyerFlag;
	
	//@MetaData("卖家备注旗帜0-灰代表全部，1-红，2-黄，3-绿，4-蓝，5-紫")
	private Integer sellerFlag = 0;
	

	
	public String getPaymentMoney() {
		return paymentMoney;
	}



	public void setPaymentMoney(String paymentMoney) {
		this.paymentMoney = paymentMoney;
	}



	public String getOid() {
		return oid;
	}



	public void setOid(String oid) {
		this.oid = oid;
	}



	public String getTid() {
		return tid;
	}



	public void setTid(String tid) {
		this.tid = tid;
	}



	public Long getCid() {
		return cid;
	}



	public void setCid(Long cid) {
		this.cid = cid;
	}



	public Long getNumIid() {
		return numIid;
	}



	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}



	public Long getItemMealId() {
		return itemMealId;
	}



	public void setItemMealId(Long itemMealId) {
		this.itemMealId = itemMealId;
	}



	public String getSkuId() {
		return skuId;
	}



	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}



	public Long getRefundId() {
		return refundId;
	}



	public void setRefundId(Long refundId) {
		this.refundId = refundId;
	}



	public Long getBindOid() {
		return bindOid;
	}



	public void setBindOid(Long bindOid) {
		this.bindOid = bindOid;
	}



	public String getItemMealName() {
		return itemMealName;
	}



	public void setItemMealName(String itemMealName) {
		this.itemMealName = itemMealName;
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



	public String getOuteriid() {
		return outeriid;
	}



	public void setOuteriid(String outeriid) {
		this.outeriid = outeriid;
	}



	public String getSnapshotUrl() {
		return snapshotUrl;
	}



	public void setSnapshotUrl(String snapshotUrl) {
		this.snapshotUrl = snapshotUrl;
	}



	public String getSnapshot() {
		return snapshot;
	}



	public void setSnapshot(String snapshot) {
		this.snapshot = snapshot;
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



	public String getSubOrderTaxFee() {
		return subOrderTaxFee;
	}



	public void setSubOrderTaxFee(String subOrderTaxFee) {
		this.subOrderTaxFee = subOrderTaxFee;
	}



	public String getSubOrderTaxRate() {
		return subOrderTaxRate;
	}



	public void setSubOrderTaxRate(String subOrderTaxRate) {
		this.subOrderTaxRate = subOrderTaxRate;
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



 


	public Long getNum() {
		return num;
	}



	public void setNum(Long num) {
		this.num = num;
	}



	public String getOuterSkuId() {
		return outerSkuId;
	}



	public void setOuterSkuId(String outerSkuId) {
		this.outerSkuId = outerSkuId;
	}



	public String getOrderFrom() {
		return orderFrom;
	}



	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}



	 


	public String getPriceMoney() {
		return priceMoney;
	}



	public void setPriceMoney(String priceMoney) {
		this.priceMoney = priceMoney;
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



	public Double getPayment() {
		return payment;
	}



	public void setPayment(Double payment) {
		this.payment = payment;
	}



	public String getDiscountFee() {
		return discountFee;
	}



	public void setDiscountFee(String discountFee) {
		this.discountFee = discountFee;
	}



	public String getAdjustFee() {
		return adjustFee;
	}



	public void setAdjustFee(String adjustFee) {
		this.adjustFee = adjustFee;
	}


 


	public String getSkuPropertiesName() {
		return skuPropertiesName;
	}



	public void setSkuPropertiesName(String skuPropertiesName) {
		this.skuPropertiesName = skuPropertiesName;
	}



	public Boolean getIsOversold() {
		return isOversold;
	}



	public void setIsOversold(Boolean isOversold) {
		this.isOversold = isOversold;
	}



	public Boolean getIsServiceOrder() {
		return isServiceOrder;
	}



	public void setIsServiceOrder(Boolean isServiceOrder) {
		this.isServiceOrder = isServiceOrder;
	}


 
	public String getConsignTime() {
		return consignTime;
	}



	public void setConsignTime(String consignTime) {
		this.consignTime = consignTime;
	}



	public String getOrderAttr() {
		return orderAttr;
	}



	public void setOrderAttr(String orderAttr) {
		this.orderAttr = orderAttr;
	}



	public String getShippingType() {
		return shippingType;
	}



	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}



	public String getLogisticsCompany() {
		return logisticsCompany;
	}



	public void setLogisticsCompany(String logisticsCompany) {
		this.logisticsCompany = logisticsCompany;
	}



	public String getInvoiceNo() {
		return invoiceNo;
	}



	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}



	public Boolean getIsdaixiao() {
		return isdaixiao;
	}



	public void setIsdaixiao(Boolean isdaixiao) {
		this.isdaixiao = isdaixiao;
	}



	public String getDivideOrderFee() {
		return divideOrderFee;
	}



	public void setDivideOrderFee(String divideOrderFee) {
		this.divideOrderFee = divideOrderFee;
	}



	public String getPartMjzDiscount() {
		return partMjzDiscount;
	}



	public void setPartMjzDiscount(String partMjzDiscount) {
		this.partMjzDiscount = partMjzDiscount;
	}



	public String getTicketOuterId() {
		return ticketOuterId;
	}



	public void setTicketOuterId(String ticketOuterId) {
		this.ticketOuterId = ticketOuterId;
	}



	public String getTicketExpdateKey() {
		return ticketExpdateKey;
	}



	public void setTicketExpdateKey(String ticketExpdateKey) {
		this.ticketExpdateKey = ticketExpdateKey;
	}



	public String getStoreCode() {
		return storeCode;
	}



	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}



	public Boolean getIsWww() {
		return isWww;
	}



	public void setIsWww(Boolean isWww) {
		this.isWww = isWww;
	}



	public String getTmserSpuCode() {
		return tmserSpuCode;
	}



	public void setTmserSpuCode(String tmserSpuCode) {
		this.tmserSpuCode = tmserSpuCode;
	}



	public String getBindOids() {
		return bindOids;
	}



	public void setBindOids(String bindOids) {
		this.bindOids = bindOids;
	}



	public String getZhengjiStatus() {
		return zhengjiStatus;
	}



	public void setZhengjiStatus(String zhengjiStatus) {
		this.zhengjiStatus = zhengjiStatus;
	}



	public String getMdQualification() {
		return mdQualification;
	}



	public void setMdQualification(String mdQualification) {
		this.mdQualification = mdQualification;
	}



	public String getMdFee() {
		return mdFee;
	}



	public void setMdFee(String mdFee) {
		this.mdFee = mdFee;
	}



	public String getCustomization() {
		return customization;
	}



	public void setCustomization(String customization) {
		this.customization = customization;
	}



	public String getInvType() {
		return invType;
	}



	public void setInvType(String invType) {
		this.invType = invType;
	}



	public Boolean getIsShShip() {
		return isShShip;
	}



	public void setIsShShip(Boolean isShShip) {
		this.isShShip = isShShip;
	}



	public String getShipper() {
		return shipper;
	}



	public void setShipper(String shipper) {
		this.shipper = shipper;
	}



	public String getfType() {
		return fType;
	}



	public void setfType(String fType) {
		this.fType = fType;
	}



	public String getfStatus() {
		return fStatus;
	}



	public void setfStatus(String fStatus) {
		this.fStatus = fStatus;
	}



	public String getFTERM() {
		return FTERM;
	}



	public void setFTERM(String fTERM) {
		FTERM = fTERM;
	}



	public String getComboId() {
		return comboId;
	}



	public void setComboId(String comboId) {
		this.comboId = comboId;
	}



	public String getAssemblyRela() {
		return assemblyRela;
	}



	public void setAssemblyRela(String assemblyRela) {
		this.assemblyRela = assemblyRela;
	}



	public String getAssemblyPrice() {
		return assemblyPrice;
	}



	public void setAssemblyPrice(String assemblyPrice) {
		this.assemblyPrice = assemblyPrice;
	}



	public String getAssemblyItem() {
		return assemblyItem;
	}



	public void setAssemblyItem(String assemblyItem) {
		this.assemblyItem = assemblyItem;
	}



	public String getReceiverDistrict() {
		return receiverDistrict;
	}



	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}



	public String getReceiverCity() {
		return receiverCity;
	}



	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity;
	}



	public String getStepTradeStatus() {
		return stepTradeStatus;
	}



	public void setStepTradeStatus(String stepTradeStatus) {
		this.stepTradeStatus = stepTradeStatus;
	}



	 


	public String getReceiverName() {
		return receiverName;
	}



	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}



	public String getReceiverMobile() {
		return receiverMobile;
	}



	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}



	public Integer getBuyerFlag() {
		return buyerFlag;
	}



	public void setBuyerFlag(Integer buyerFlag) {
		this.buyerFlag = buyerFlag;
	}



	public Integer getSellerFlag() {
		return sellerFlag;
	}



	public void setSellerFlag(Integer sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	
	


	public Date getTimeoutActionTimeUTC() {
		return timeoutActionTimeUTC;
	}



	public void setTimeoutActionTimeUTC(Date timeoutActionTimeUTC) {
		this.timeoutActionTimeUTC = timeoutActionTimeUTC;
	}



	public Long getTimeoutActionTime() {
		return timeoutActionTime;
	}



	public void setTimeoutActionTime(Long timeoutActionTime) {
		this.timeoutActionTime = timeoutActionTime;
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



	@Override
	public String toString() {
		return "Orders [oid=" + oid + ", tid=" + tid + ", cid=" + cid
				+ ", numIid=" + numIid + ", itemMealId=" + itemMealId
				+ ", skuId=" + skuId + ", refundId=" + refundId + ", bindOid="
				+ bindOid + ", itemMealName=" + itemMealName + ", picPath="
				+ picPath + ", sellerNick=" + sellerNick + ", buyerNick="
				+ buyerNick + ", refundStatus=" + refundStatus + ", outeriid="
				+ outeriid + ", snapshotUrl=" + snapshotUrl + ", snapshot="
				+ snapshot + ", timeoutActionTime=" + timeoutActionTime
				+ ", buyerRate=" + buyerRate + ", sellerRate=" + sellerRate
				+ ", sellerType=" + sellerType + ", subOrderTaxFee="
				+ subOrderTaxFee + ", subOrderTaxRate=" + subOrderTaxRate
				+ ", status=" + status + ", title=" + title + ", type=" + type
				+ ", iid=" + iid + ", price=" + price + ", num=" + num
				+ ", outerSkuId=" + outerSkuId + ", orderFrom=" + orderFrom
				+ ", totalFee=" + totalFee + ", payment=" + payment
				+ ", discountFee=" + discountFee + ", adjustFee=" + adjustFee
				+ ", modified=" + modified + ", skuPropertiesName="
				+ skuPropertiesName + ", isOversold=" + isOversold
				+ ", isServiceOrder=" + isServiceOrder + ", endTime=" + endTime
				+ ", consignTime=" + consignTime + ", orderAttr=" + orderAttr
				+ ", shippingType=" + shippingType + ", logisticsCompany="
				+ logisticsCompany + ", invoiceNo=" + invoiceNo
				+ ", isdaixiao=" + isdaixiao + ", divideOrderFee="
				+ divideOrderFee + ", partMjzDiscount=" + partMjzDiscount
				+ ", ticketOuterId=" + ticketOuterId + ", ticketExpdateKey="
				+ ticketExpdateKey + ", storeCode=" + storeCode + ", isWww="
				+ isWww + ", tmserSpuCode=" + tmserSpuCode + ", bindOids="
				+ bindOids + ", zhengjiStatus=" + zhengjiStatus
				+ ", mdQualification=" + mdQualification + ", mdFee=" + mdFee
				+ ", customization=" + customization + ", invType=" + invType
				+ ", isShShip=" + isShShip + ", shipper=" + shipper
				+ ", fType=" + fType + ", fStatus=" + fStatus + ", FTERM="
				+ FTERM + ", comboId=" + comboId + ", assemblyRela="
				+ assemblyRela + ", assemblyPrice=" + assemblyPrice
				+ ", assemblyItem=" + assemblyItem + ", receiverDistrict="
				+ receiverDistrict + ", receiverCity=" + receiverCity
				+ ", stepTradeStatus=" + stepTradeStatus + ", created="
				+ created + ", receiverName=" + receiverName
				+ ", receiverMobile=" + receiverMobile + ", buyerFlag="
				+ buyerFlag + ", sellerFlag=" + sellerFlag +  "]";
	}

	
	

}
