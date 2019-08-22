package com.kycrm.member.domain.entity.trade;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.kycrm.member.domain.entity.base.BaseEntity;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.taobao.api.internal.mapping.ApiField;
import com.taobao.api.internal.mapping.ApiListField;

public class TradeDTO extends BaseEntity{
	
	private static final long serialVersionUID = -6824764955031861640L;
	/** 
	 * 卖家手工调整金额，精确到2位小数，单位：元。如：200.07，表示：200元7分。来源于订单价格修改，如果有多笔子订单的时候，这个为0，单笔的话则跟[order].adjust_fee一样		
	 */
	@ApiField("adjust_fee")		
	private String adjustFeeString;
	
	
	private BigDecimal adjustFee;
	
	/** 
	 * 买家的支付宝id号，在UIC中有记录，买家支付宝的唯一标示，不因为买家更换Email账号而改变。		
	 */
	@ApiField("alipay_id")			
	private Long alipayId;					
	
	/** 
	 * 支付宝交易号，如：2009112081173831		
	 */
	@ApiField("alipay_no")	
	private String alipayNo;				
	
	/** 
	 * 付款时使用的支付宝积分的额度,单位分，比如返回1，则为1分钱		
	 */
	@ApiField("alipay_point")			
	private Long alipayPoint;					
	
	/** 
	 * 交易中剩余的确认收货金额（这个金额会随着子订单确认收货而不断减少，交易成功后会变为零）。精确到2位小数单位:元。如:200.07，表示:200元7分		
	 */
	@ApiField("available_confirm_fee")		
	private String availableConfirmFeeString;
	
	private BigDecimal availableConfirmFee;					
	
	/** 
	 * 买家支付宝账号		
	 */
	@ApiField("buyer_alipay_no")			
	private String buyerAlipayNo;					
	
	/** 
	 * 买家下单的地区		
	 */
	@ApiField("buyer_area")			
	private String buyerArea;					
	
	/** 
	 * 买家货到付款服务费。精确到2位小数单位:元。如:12.07，表示:12元7分		
	 */
	@ApiField("buyer_cod_fee")			
	private String buyerCodFee;					
	
	/** 
	 * 买家邮件地址		
	 */
	@ApiField("buyer_email")			
	private String buyerEmail;					
	
	/** 
	 * 买家昵称		
	 */
	@ApiField("buyer_nick")			
	private String buyerNick;					
	
	/** 
	 * 买家获得积分,返点的积分。格式:100单位:个。返点的积分要交易成功之后才能获得。		
	 */
	@ApiField("buyer_obtain_point_fee")			
	private Long buyerObtainPointFee;					
	
	/** 
	 * 买家是否已评价。可选值:true(已评价),false(未评价)。如买家只评价未打分，此字段仍返回false		
	 */
	@ApiField("buyer_rate")			
	private Boolean buyerRate;					
	
	/** 
	 * 货到付款服务费。精确到2位小数单位:元。如:12.07，表示:12元7分。		
	 */
	@ApiField("cod_fee")		
	private String codFeeString;
	
	private BigDecimal codFee;					
	
	/** 
	 * 货到付款物流状态。初始状态NEW_CREATED,接单成功ACCEPTED_BY_COMPANY,接单失败REJECTED_BY_COMPANY,接单超时RECIEVE_TIMEOUT,揽收成功TAKEN_IN_SUCCESS,揽收失败TAKEN_IN_FAILED,揽收超时TAKEN_TIMEOUT,签收成功SIGN_IN,签收失败REJECTED_BY_OTHER_SIDE,订单等待发送给物流公司WAITING_TO_BE_SENT,用户取消物流订单CANCELED		
	
	 */
	@ApiField("cod_status")			
	private String codStatus;					
	
	/** 
	 * 交易佣金。精确到2位小数单位:元。如:200.07，表示:200元7分		
	 */
	@ApiField("commission_fee")			
	private String commissionFeeString;					
	
	private BigDecimal commissionFee;					
	
	/** 
	 * 卖家发货时间。格式:yyyy-MM-ddHH:mm:ss		
	 */
	@ApiField("consign_time")			
	private Date consignTime;					
	
	/** 
	 * 交易创建时间。格式:yyyy-MM-ddHH:mm:ss		
	 */
	@ApiField("created")			
	private Date created;					
	
	/** 
	 * 可以使用trade.promotion_details查询系统优惠系统优惠金额（如打折，VIP，满就送等），精确到2位小数，单位：元。如：200.07，表示：200元7分		
	 */
	@ApiField("discount_fee")			
	private String discountFeeString;					
	
	private BigDecimal discountFee;					
	
	/** 
	 * 交易结束时间。交易成功时间(更新交易状态为成功的同时更新)/确认收货时间或者交易关闭时间。格式:yyyy-MM-ddHH:mm:ss		
	 */
	@ApiField("end_time")			
	private Date endTime;					
	
	/** 
	 * 是否包含邮费。与available_confirm_fee同时使用。可选值:true(包含),false(不包含)		
	 */
	@ApiField("has_post_fee")			
	private Boolean hasPostFee;					
	
	/** 
	 * 是否3D交易		
	 */
	@ApiField("is_3D")			
	private Boolean is3D;					
	
	/** 
	 * 表示是否是品牌特卖（常规特卖，不包括特卖惠和特实惠）订单，如果是返回true，如果不是返回false。当此字段与is_force_wlb均为true时，订单强制物流宝发货。		
	 */
	@ApiField("is_brand_sale")			
	private Boolean isBrandSale;					
	
	/** 
	 * 表示订单交易是否含有对应的代销采购单。如果该订单中存在一个对应的代销采购单，那么该值为true反之，该值为false。		
	 */
	@ApiField("is_daixiao")			
	private Boolean isDaixiao;					
	
	/** 
	 * 订单是否强制使用物流宝发货。当此字段与is_brand_sale均为true时，订单强制物流宝发货。此字段为false时，该订单根据流转规则设置可以使用物流宝或者常规方式发货		
	 */
	@ApiField("is_force_wlb")			
	private Boolean isForceWlb;				
	
	/** 
	 * 是否保障速递，如果为true，则为保障速递订单，使用线下联系发货接口发货，如果未false，则该订单非保障速递，根据卖家设置的订单流转规则可使用物流宝或者常规物流发货。		
	 */
	@ApiField("is_lgtype")			
	private Boolean isLgtype;					
	
	/** 
	 * 是否是多次发货的订单如果卖家对订单进行多次发货，则为true否则为false		
	 */
	@ApiField("is_part_consign")			
	private Boolean isPartConsign;					
	
	/** 
	 * 表示订单交易是否网厅订单。如果该订单是网厅订单，那么该值为true反之，该值为false。		
	 */
	@ApiField("is_wt")			
	private Boolean isWt;					
	
	/** 
	 * 交易修改时间(用户对订单的任何修改都会更新此字段)。格式:yyyy-MM-ddHH:mm:ss		
	 */
	@ApiField("modified")			
	private Date modified;					
	
	/** 
	 * 付款时间。格式:yyyy-MM-ddHH:mm:ss。订单的付款时间即为物流订单的创建时间。		
	 */
	@ApiField("pay_time")			
	private Date payTime;					
	
	/** 
	 * 实付金额。精确到2位小数单位:元。如:200.07，表示:200元7分		
	 */
	@ApiField("payment")			
	private String paymentString;		
	
	private BigDecimal payment;				
	
	/** 
	 * 天猫点券卡实付款金额,单位分		
	 */
	@ApiField("pcc_af")			
	private Long pccAf;					
	
	/** 
	 * 买家使用积分,下单时生成，且一直不变。格式:100单位:个.		
	 */
	@ApiField("point_fee")			
	private Long pointFee;					
	
	/** 
	 * 邮费。精确到2位小数单位:元。如:200.07，表示:200元7分		
	 */
	@ApiField("post_fee")			
	private String postFeeString;					
	
	private BigDecimal postFee;					
	
	/** 
	 * 买家实际使用积分（扣除部分退款使用的积分），交易完成后生成（交易成功或关闭），交易未完成时该字段值为0。格式:100单位:个		
	 */
	@ApiField("real_point_fee")			
	private Long realPointFee;				
	
	/** 
	 * 卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额）。精确到2位小数单位:元。如:200.07，表示:200元7分		
	 */
	@ApiField("received_payment")			
	private String receivedPaymentString;					
	
	private BigDecimal receivedPayment;					
	
	/** 
	 * 收货人的详细地址		
	 */
	@ApiField("receiver_address")			
	private String receiverAddress;					
	
	/** 
	 * 收货人的所在城市<br/>注：因为国家对于城市和地区的划分的有：省直辖市和省直辖县级行政区（区级别的）划分的，淘宝这边根据这个差异保存在不同字段里面比如：广东广州：广州属于一个直辖市是放在的receiver_city的字段里面而河南济源：济源属于省直辖县级行政区划分，是区级别的，放在了receiver_district里面<br/>建议：程序依赖于城市字段做物流等判断的操作，最好加一个判断逻辑：如果返回值里面只有receiver_district参数，该参数作为城市		
	 */
	@ApiField("receiver_city")			
	private String receiverCity;				
	
	/** 
	 * 收货人的所在地区<br/>注：因为国家对于城市和地区的划分的有：省直辖市和省直辖县级行政区（区级别的）划分的，淘宝这边根据这个差异保存在不同字段里面比如：广东广州：广州属于一个直辖市是放在的receiver_city的字段里面而河南济源：济源属于省直辖县级行政区划分，是区级别的，放在了receiver_district里面<br/>建议：程序依赖于城市字段做物流等判断的操作，最好加一个判断逻辑：如果返回值里面只有receiver_district参数，该参数作为城市		
	 */
	@ApiField("receiver_district")			
	private String receiverDistrict;					
	
	/** 
	 * 收货人的手机号码		
	 */
	@ApiField("receiver_mobile")			
	private String receiverMobile;					
	
	/**
	 * 收货人固定电话
	 */
	@ApiField("receiver_phone")
    private String receiverPhone;
	
	
	/** 
	 * 收货人的姓名		
	 */
	@ApiField("receiver_name")			
	private String receiverName;					
	
	/** 
	 * 收货人的所在省份		
	 */
	@ApiField("receiver_state")			
	private String receiverState;					
	
	/** 
	 * 收货人的邮编		
	 */
	@ApiField("receiver_zip")			
	private String receiverZip;					
	
	/** 
	 * 卖家支付宝账号		
	 */
	@ApiField("seller_alipay_no")			
	private String sellerAlipayNo;					
	
	/** 
	 * 卖家是否可以对订单进行评价		
	 */
	@ApiField("seller_can_rate")			
	private Boolean sellerCanRate;					
	
	/** 
	 * 卖家货到付款服务费。精确到2位小数单位:元。如:12.07，表示:12元7分。卖家不承担服务费的订单：未发货的订单获取服务费为0，发货后就能获取到正确值。		
	 */
	@ApiField("seller_cod_fee")			
	private String sellerCodFeeString;					
	
	
	private BigDecimal sellerCodFee;					
	
	/** 
	 * 卖家邮件地址		
	 */
	@ApiField("seller_email")			
	private String sellerEmail;					
	
	/** 
	 * 卖家备注旗帜（与淘宝网上订单的卖家备注旗帜对应，只有卖家才能查看该字段）红、黄、绿、蓝、紫分别对应1、2、3、4、5		
	 */
	@ApiField("seller_flag")			
	private Long sellerFlag;				
	
	/** 
	 * 卖家手机		
	 */
	@ApiField("seller_mobile")			
	private String sellerMobile;					
	
	/** 
	 * 卖家姓名		
	 */
	@ApiField("seller_name")			
	private String sellerName;					
	
	/** 
	 * 卖家昵称		
	 */
	@ApiField("seller_nick")			
	private String sellerNick;					
	
	/** 
	 * 卖家电话		
	 */
	@ApiField("seller_phone")			
	private String sellerPhone;					
	
	/** 
	 * 卖家是否已评价。可选值:true(已评价),false(未评价)		
	 */
	@ApiField("seller_rate")			
	private Boolean sellerRate;					
	
	/** 
	 * 创建交易时的物流方式（交易完成前，物流方式有可能改变，但系统里的这个字段一直不变）。可选值：free(卖家包邮),post(平邮),express(快递),ems(EMS),virtual(虚拟发货)，25(次日必达)，26(预约配送)。		
	 */
	@ApiField("shipping_type")			
	private String shippingType;					
	
	/** 
	 * 交易快照地址		
	 */
	@ApiField("snapshot_url")			
	private String snapshotUrl;					
	
	/** 
	 * 订单状态（请关注此状态，如果为TRADE_CLOSED_BY_TAOBAO状态，则不要对此订单进行发货，切记啊！）。
	 * 可选值: TRADE_NO_CREATE_PAY(没有创建支付宝交易) WAIT_BUYER_PAY(等待买家付款) 
	 * WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款) WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货) 
	 * TRADE_BUYER_SIGNED(买家已签收,货到付款专用) TRADE_FINISHED(交易成功) TRADE_CLOSED(付款以后用户退款成功，交易自动关闭) 
	 * TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)PAY_PENDING(国际信用卡支付付款确认中)	SELLER_CONSIGNED_PART(部分发货)	
	 */
	@ApiField("status")			
	private String status;					
	
	/** 
	 * 交易编号(父订单的交易编号)		
	 */
	@ApiField("tid")
	@JsonSerialize(using=ToStringSerializer.class)
	private Long tid;					
	
	/** 
	 * 同tid		
	 */
	@ApiField("tid_str")			
	private String tidStr;					
	
	/** 
	 * 交易标题，以店铺名作为此标题的值。注:taobao.trades.get接口返回的Trade中的title是商品名称		
	 */
	@ApiField("title")			
	private String title;					
	
	/** 
	 * 商品金额（商品价格乘以数量的总金额）。精确到2位小数单位:元。如:200.07，表示:200元7分		
	 */
	@ApiField("total_fee")			
	private String totalFeeString;				
	
	private BigDecimal totalFee;				
	
	/**
	 * 子订单来源,WAP(手机);HITAO(嗨淘);TOP(TOP平台);
	 * TAOBAO(普通淘宝);JHS(聚划算);TNR(线下天猫中台新零售)
	 * 一笔订单可能同时有以上多个标记，则以逗号分隔省略......
	 */
	@ApiField("trade_from")			
	private String tradeFrom;					
	
	/** 
	 * 交易类型列表，同时查询多种交易类型可用逗号分隔。默认同时查询guarantee_trade,auto_delivery,ec,cod的4种交易类型的数据可选值fixed(一口价)auction(拍卖)guarantee_trade(一口价、拍卖)auto_delivery(自动发货)independent_simple_trade(旺店入门版交易)independent_shop_trade(旺店标准版交易)ec(直冲)cod(货到付款)fenxiao(分销)game_equipment(游戏装备)shopex_trade(ShopEX交易)netcn_trade(万网交易)external_trade(统一外部交易)o2o_offlinetrade（O2O交易）step(万人团)nopaid(无付款订单)pre_auth_type(预授权0元购机交易)		
	 */
	@ApiField("type")			
	private String type;					
	
	/**
     * 分阶段付款的订单状态（例如万人团订单等），目前有三返回状态FRONT_NOPAID_FINAL_NOPAID(定金未付尾款未付)，FRONT_PAID_FINAL_NOPAID(定金已付尾款未付)，FRONT_PAID_FINAL_PAID(定金和尾款都付)
     */
	@ApiField("step_trade_status")  
    private String stepTradeStatus;
    
    /**
     * 分阶段付款的已付金额 
     */
	@ApiField("step_paid_fee")  
    private String  stepPaidFee;
	
	//==========================================
	//部分json内容
	//==========================================
	/**
	 * 物流标签
	 */
	@ApiField("service_tags")	
	private String serviceTags;
	
	/**
	 *优惠详情
	 */
	@ApiField("promotion_details")	
	private String promotionDetails;
	
	/**
	 * 营销短信发送id 
	 */
	private Long msgId;
	
	/**
	 * 最近短信发送时间
	 */
	private  Date lastSendSmsTime  ;

	/**
	 * 订单来源   1、拉取sysInfo数据库，同步订单   2、用户导入
	 */
	private Integer tradeSource;
	
	@ApiListField("orders")
    @ApiField("order")
    private  List<OrderDTO>  orders;
	

    /**
     * 收货人国籍
     */
    @ApiField("receiver_country")
    private String receiverCountry;
    
    /**
     * 收货人街道地址
     */
    @ApiField("receiver_town")
    private String receiverTown;
    
    /**
     * 门店自提，总店发货，分店取货的门店自提订单标识
     */
    @ApiField("shop_pick")
    private String shopPick;
    
    /**
     * 商品购买数量
     */
    @ApiField("num")
    private Long num;
	
    /**
     * 买家备注旗帜
     */
    @ApiField("buyer_flag")
    private Long buyerFlag;
    
    /**
     * 该订单是否退款
     */
    @ApiField("refund_flag")
    private Boolean refundFlag;
    
    private List<OrderDTO> orderList;
    
    /**
     * 定金
     * @return
     */
    private String front;
    /**
     * 尾款
     * @return
     */
    private String tail;
	public BigDecimal getAdjustFee() {
		return adjustFee;
	}

	public void setAdjustFee(BigDecimal adjustFee) {
		this.adjustFee = adjustFee;
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


	public Long getAlipayPoint() {
		return alipayPoint;
	}


	public void setAlipayPoint(Long alipayPoint) {
		this.alipayPoint = alipayPoint;
	}


	public BigDecimal getAvailableConfirmFee() {
		return availableConfirmFee;
	}


	public void setAvailableConfirmFee(BigDecimal availableConfirmFee) {
		this.availableConfirmFee = availableConfirmFee;
	}


	public String getBuyerAlipayNo() {
		return buyerAlipayNo;
	}


	public void setBuyerAlipayNo(String buyerAlipayNo) {
		this.buyerAlipayNo = buyerAlipayNo;
	}


	public String getBuyerArea() {
		return buyerArea;
	}


	public void setBuyerArea(String buyerArea) {
		this.buyerArea = buyerArea;
	}


	public String getBuyerCodFee() {
		return buyerCodFee;
	}


	public void setBuyerCodFee(String buyerCodFee) {
		this.buyerCodFee = buyerCodFee;
	}


	public String getBuyerEmail() {
		return buyerEmail;
	}


	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}


	public String getBuyerNick() {
		return buyerNick;
	}


	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}


	public Long getBuyerObtainPointFee() {
		return buyerObtainPointFee;
	}


	public void setBuyerObtainPointFee(Long buyerObtainPointFee) {
		this.buyerObtainPointFee = buyerObtainPointFee;
	}


	public Boolean getBuyerRate() {
		return buyerRate;
	}


	public void setBuyerRate(Boolean buyerRate) {
		this.buyerRate = buyerRate;
	}


	public BigDecimal getCodFee() {
		return codFee;
	}


	public void setCodFee(BigDecimal codFee) {
		this.codFee = codFee;
	}


	public String getCodStatus() {
		return codStatus;
	}


	public void setCodStatus(String codStatus) {
		this.codStatus = codStatus;
	}


	public BigDecimal getCommissionFee() {
		return commissionFee;
	}


	public void setCommissionFee(BigDecimal commissionFee) {
		this.commissionFee = commissionFee;
	}


	public Date getConsignTime() {
		return consignTime;
	}


	public void setConsignTime(Date consignTime) {
		this.consignTime = consignTime;
	}


	public Date getCreated() {
		return created;
	}


	public void setCreated(Date created) {
		this.created = created;
	}


	public BigDecimal getDiscountFee() {
		return discountFee;
	}


	public void setDiscountFee(BigDecimal discountFee) {
		this.discountFee = discountFee;
	}


	public Date getEndTime() {
		return endTime;
	}


	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}


	public Boolean getHasPostFee() {
		return hasPostFee;
	}


	public void setHasPostFee(Boolean hasPostFee) {
		this.hasPostFee = hasPostFee;
	}


	public Boolean getIs3D() {
		return is3D;
	}


	public void setIs3D(Boolean is3d) {
		is3D = is3d;
	}


	public Boolean getIsBrandSale() {
		return isBrandSale;
	}


	public void setIsBrandSale(Boolean isBrandSale) {
		this.isBrandSale = isBrandSale;
	}


	public Boolean getIsDaixiao() {
		return isDaixiao;
	}


	public void setIsDaixiao(Boolean isDaixiao) {
		this.isDaixiao = isDaixiao;
	}


	public Boolean getIsForceWlb() {
		return isForceWlb;
	}


	public void setIsForceWlb(Boolean isForceWlb) {
		this.isForceWlb = isForceWlb;
	}


	public Boolean getIsLgtype() {
		return isLgtype;
	}


	public void setIsLgtype(Boolean isLgtype) {
		this.isLgtype = isLgtype;
	}


	public Boolean getIsPartConsign() {
		return isPartConsign;
	}


	public void setIsPartConsign(Boolean isPartConsign) {
		this.isPartConsign = isPartConsign;
	}


	public Boolean getIsWt() {
		return isWt;
	}


	public void setIsWt(Boolean isWt) {
		this.isWt = isWt;
	}


	public Date getModified() {
		return modified;
	}


	public void setModified(Date modified) {
		this.modified = modified;
	}


	public Date getPayTime() {
		return payTime;
	}


	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}


	public BigDecimal getPayment() {
		return payment;
	}


	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}


	public Long getPccAf() {
		return pccAf;
	}


	public void setPccAf(Long pccAf) {
		this.pccAf = pccAf;
	}


	public Long getPointFee() {
		return pointFee;
	}


	public void setPointFee(Long pointFee) {
		this.pointFee = pointFee;
	}


	public BigDecimal getPostFee() {
		return postFee;
	}


	public void setPostFee(BigDecimal postFee) {
		this.postFee = postFee;
	}


	public Long getRealPointFee() {
		return realPointFee;
	}


	public void setRealPointFee(Long realPointFee) {
		this.realPointFee = realPointFee;
	}


	public BigDecimal getReceivedPayment() {
		return receivedPayment;
	}


	public void setReceivedPayment(BigDecimal receivedPayment) {
		this.receivedPayment = receivedPayment;
	}


	public String getReceiverAddress() {
		return receiverAddress;
	}


	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
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


	public String getReceiverMobile() {
		return receiverMobile;
	}


	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
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


	public String getReceiverZip() {
		return receiverZip;
	}


	public void setReceiverZip(String receiverZip) {
		this.receiverZip = receiverZip;
	}


	public String getSellerAlipayNo() {
		return sellerAlipayNo;
	}


	public void setSellerAlipayNo(String sellerAlipayNo) {
		this.sellerAlipayNo = sellerAlipayNo;
	}


	public Boolean getSellerCanRate() {
		return sellerCanRate;
	}


	public void setSellerCanRate(Boolean sellerCanRate) {
		this.sellerCanRate = sellerCanRate;
	}


	public BigDecimal getSellerCodFee() {
		return sellerCodFee;
	}


	public void setSellerCodFee(BigDecimal sellerCodFee) {
		this.sellerCodFee = sellerCodFee;
	}


	public String getSellerEmail() {
		return sellerEmail;
	}


	public void setSellerEmail(String sellerEmail) {
		this.sellerEmail = sellerEmail;
	}


	public Long getSellerFlag() {
		return sellerFlag;
	}


	public void setSellerFlag(Long sellerFlag) {
		this.sellerFlag = sellerFlag;
	}


	public String getSellerMobile() {
		return sellerMobile;
	}


	public void setSellerMobile(String sellerMobile) {
		this.sellerMobile = sellerMobile;
	}


	public String getSellerName() {
		return sellerName;
	}


	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
	}


	public String getSellerNick() {
		return sellerNick;
	}


	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}


	public String getSellerPhone() {
		return sellerPhone;
	}


	public void setSellerPhone(String sellerPhone) {
		this.sellerPhone = sellerPhone;
	}


	public Boolean getSellerRate() {
		return sellerRate;
	}


	public void setSellerRate(Boolean sellerRate) {
		this.sellerRate = sellerRate;
	}


	public String getShippingType() {
		return shippingType;
	}


	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}


	public String getSnapshotUrl() {
		return snapshotUrl;
	}


	public void setSnapshotUrl(String snapshotUrl) {
		this.snapshotUrl = snapshotUrl;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Long getTid() {
		return tid;
	}


	public void setTid(Long tid) {
		this.tid = tid;
	}


	public String getTidStr() {
		return tidStr;
	}


	public void setTidStr(String tidStr) {
		this.tidStr = tidStr;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public BigDecimal getTotalFee() {
		return totalFee;
	}


	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}


	public String getTradeFrom() {
		return tradeFrom;
	}


	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public String getServiceTags() {
		return serviceTags;
	}


	public void setServiceTags(String serviceTags) {
		this.serviceTags = serviceTags;
	}


	public String getPromotionDetails() {
		return promotionDetails;
	}


	public void setPromotionDetails(String promotionDetails) {
		this.promotionDetails = promotionDetails;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

    public Date getLastSendSmsTime() {
        return lastSendSmsTime;
    }

    public void setLastSendSmsTime(Date lastSendSmsTime) {
        this.lastSendSmsTime = lastSendSmsTime;
    }

    public Integer getTradeSource() {
        return tradeSource;
    }

    public void setTradeSource(Integer tradeSource) {
        this.tradeSource = tradeSource;
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



    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public List<OrderDTO> getOrders() {
        return orders;
    }


    public void setOrders(List<OrderDTO> orders) {
        this.orders = orders;
    }


    public String getAdjustFeeString() {
        return adjustFeeString;
    }


    public void setAdjustFeeString(String adjustFeeString) {
        this.adjustFeeString = adjustFeeString;
    }


    public String getAvailableConfirmFeeString() {
        return availableConfirmFeeString;
    }


    public void setAvailableConfirmFeeString(String availableConfirmFeeString) {
        this.availableConfirmFeeString = availableConfirmFeeString;
    }


    public String getCodFeeString() {
        return codFeeString;
    }


    public void setCodFeeString(String codFeeString) {
        this.codFeeString = codFeeString;
    }


    public String getCommissionFeeString() {
        return commissionFeeString;
    }


    public void setCommissionFeeString(String commissionFeeString) {
        this.commissionFeeString = commissionFeeString;
    }


    public String getDiscountFeeString() {
        return discountFeeString;
    }


    public void setDiscountFeeString(String discountFeeString) {
        this.discountFeeString = discountFeeString;
    }


    public String getPaymentString() {
        return paymentString;
    }


    public void setPaymentString(String paymentString) {
        this.paymentString = paymentString;
    }


    public String getPostFeeString() {
        return postFeeString;
    }


    public void setPostFeeString(String postFeeString) {
        this.postFeeString = postFeeString;
    }


    public String getReceivedPaymentString() {
        return receivedPaymentString;
    }


    public void setReceivedPaymentString(String receivedPaymentString) {
        this.receivedPaymentString = receivedPaymentString;
    }


    public String getSellerCodFeeString() {
        return sellerCodFeeString;
    }


    public void setSellerCodFeeString(String sellerCodFeeString) {
        this.sellerCodFeeString = sellerCodFeeString;
    }


    public String getTotalFeeString() {
        return totalFeeString;
    }


    public void setTotalFeeString(String totalFeeString) {
        this.totalFeeString = totalFeeString;
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


    public String getShopPick() {
        return shopPick;
    }


    public void setShopPick(String shopPick) {
        this.shopPick = shopPick;
    }


    public Long getNum() {
        return num;
    }


    public void setNum(Long num) {
        this.num = num;
    }


    public Long getBuyerFlag() {
        return buyerFlag;
    }


    public void setBuyerFlag(Long buyerFlag) {
        this.buyerFlag = buyerFlag;
    }


    public Boolean getRefundFlag() {
        return refundFlag;
    }


    public void setRefundFlag(Boolean refundFlag) {
        this.refundFlag = refundFlag;
    }


	public List<OrderDTO> getOrderList() {
		return orderList;
	}


	public void setOrderList(List<OrderDTO> orderList) {
		this.orderList = orderList;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getTail() {
		return tail;
	}

	public void setTail(String tail) {
		this.tail = tail;
	}
	
}
