package com.kycrm.member.domain.entity.order;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.kycrm.member.domain.entity.base.BaseEntity;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.member.domain.entity.traderate.TradeRates;
import com.taobao.api.internal.mapping.ApiField;

public class OrderDTO extends BaseEntity{

	private static final long serialVersionUID = -495975644557606780L;


	/**
	 * 套餐的值。如：M8原装电池:便携支架:M8专用座充:莫凡保护袋
	 */
	@ApiField("item_meal_name")
	private String itemMealName;

	/**
	 * 商品图片的绝对路径
	 */
	@ApiField("pic_path")
	private String picPath;

	/**
	 * 卖家昵称
	 */
	@ApiField("seller_nick")
	private String sellerNick;

	/**
	 * 买家昵称
	 */
	@ApiField("buyer_nick")
	private String buyerNick;

	/**
	 * 退款状态。退款状态。可选值 WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意)
	 * WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货)
	 * WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货) SELLER_REFUSE_BUYER(卖家拒绝退款)
	 * CLOSED(退款关闭) SUCCESS(退款成功)
	 */
	@ApiField("refund_status")
	private String refundStatus;

	/**
	 * 商家外部编码(可与商家外部系统对接)。外部商家自己定义的商品Item的id，可以通过taobao.items.custom.
	 * get获取商品的Item的信息
	 */
	@ApiField("outer_iid")
	private String outerIid;

	/**
	 * 订单快照URL
	 */
	@ApiField("snapshot_url")
	private String snapshotUrl;

	/**
	 * 订单快照详细信息
	 */
	@ApiField("snapshot")
	private String snapshot;

	/**
	 * 订单超时到期时间。格式:yyyy-MM-dd HH:mm:ss
	 */
	@ApiField("timeout_action_time")
	private Date timeoutActionTime;

	/**
	 * 买家是否已评价。可选值：true(已评价)，false(未评价)
	 */
	@ApiField("buyer_rate")
	private Boolean buyerRate;

	/**
	 * 卖家是否已评价。可选值：true(已评价)，false(未评价)
	 */
	@ApiField("seller_rate")
	private Boolean sellerRate;

	/**
	 * 卖家类型，可选值为：B（商城商家），C（普通卖家）
	 */
	@ApiField("seller_type")
	private String sellerType;

	/**
	 * 交易商品对应的类目ID
	 */
	@ApiField("cid")
	private Long cid;

	/**
	 * 天猫国际官网直供子订单关税税费
	 */
	@ApiField("sub_order_tax_fee")
	private String subOrderTaxFee;

	/**
	 * 天猫国际官网直供子订单关税税率
	 */
	@ApiField("sub_order_tax_rate")
	private String subOrderTaxRate;

	/**
	 * 子订单预计发货时间
	 */
	@ApiField("estimate_con_time")
	private String estimateConTime;

	/**
	 * 子订单编号
	 */
	@JsonSerialize(using=ToStringSerializer.class)
	@ApiField("oid")
	private Long oid;

	/** 
	 * 订单状态（请关注此状态，如果为TRADE_CLOSED_BY_TAOBAO状态，则不要对此订单进行发货，切记啊！）。
	 * 可选值: TRADE_NO_CREATE_PAY(没有创建支付宝交易) WAIT_BUYER_PAY(等待买家付款) 
	 * WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款) WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货) 
	 * TRADE_BUYER_SIGNED(买家已签收,货到付款专用) TRADE_FINISHED(交易成功) TRADE_CLOSED(付款以后用户退款成功，交易自动关闭) 
	 * TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)PAY_PENDING(国际信用卡支付付款确认中)		
	 */
	@ApiField("status")
	private String status;

	/**
	 * 商品标题
	 */
	@ApiField("title")
	private String title;

	/**
	 * 交易类型
	 */
	@ApiField("type")
	private String type;

	/**
	 * 商品的字符串编号(注意：iid近期即将废弃，请用num_iid参数)
	 */
	@ApiField("iid")
	private String iid;

	/**
	 * 商品价格。精确到2位小数;
	 * 
	 * /**单位:元。如:200.07，表示:200元7分
	 */
	@ApiField("price")
	private String priceString;

	private BigDecimal price;

	/**
	 * 商品数字ID
	 */
	@ApiField("num_iid")
	private Long numIid;

	/**
	 * 套餐ID
	 */
	@ApiField("item_meal_id")
	private Long itemMealId;

	/**
	 * 商品的最小库存单位Sku的id.可以通过taobao.item.sku.get获取详细的Sku信息
	 */
	@ApiField("sku_id")
	private String skuId;

	/**
	 * 购买数量。取值范围:大于零的整数
	 */
	@ApiField("num")
	private Long num;

	/**
	 * 外部网店自己定义的Sku编号
	 */
	@ApiField("outer_sku_id")
	private String outerSkuId;

	/**
	 * 子订单来源,WAP(手机);HITAO(嗨淘);TOP(TOP平台);
	 * TAOBAO(普通淘宝);JHS(聚划算);TNR(线下天猫中台新零售)
	 * 一笔订单可能同时有以上多个标记，则以逗号分隔省略......
	 */
	@ApiField("order_from")
	private String orderFrom;

	/**
	 * 应付金额（商品价格 * 商品数量 + 手工调整金额 - 子订单级订单优惠金额）。精确到2位小数;
	 * 
	 * /**单位:元。如:200.07，表示:200元7分
	 */
	@ApiField("total_fee")
	private String totalFeeString;

	private BigDecimal totalFee;

	/**
	 * 子订单实付金额。精确到2位小数，单位:元。如:200.07，表示:200元7分。对于多子订单的交易，计算公式如下：payment = price
	 * * num + adjust_fee - discount_fee
	 * ；单子订单交易，payment与主订单的payment一致，对于退款成功的子订单，由于主订单的优惠分摊金额，会造成该字段可能不为0.00元。
	 * 建议使用退款前的实付金额减去退款单中的实际退款金额计算。 /** 应付金额（商品价格 * 商品数量 + 手工调整金额 -
	 * 子订单级订单优惠金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分
	 */
	@ApiField("payment")
	private String paymentString;

	private BigDecimal payment;

	/**
	 * 子订单级订单优惠金额。精确到2位小数;
	 * 
	 * /**单位:元。如:200.07，表示:200元7分
	 */
	@ApiField("discount_fee")
	private String discountFeeString;

	private BigDecimal discountFee;

	/**
	 * 手工调整金额.格式为:1.01;
	 * 
	 * /**单位:元;
	 * 
	 * /**精确到小数点后两位. /** 子订单级订单优惠金额。精确到2位小数;单位:元。如:200.07，表示:200元7分
	 */
	@ApiField("adjust_fee")
	private String adjustFeeString;

	private BigDecimal adjustFee;

	/**
	 * 订单修改时间，目前只有taobao.trade.ordersku.update会返回此字段。 /**
	 * 手工调整金额.格式为:1.01;单位:元;精确到小数点后两位.
	 */
	@ApiField("modified")
	private Date modified;

	/**
	 * SKU的值。如：机身颜色:黑色;
	 * 
	 * /**手机套餐:官方标配
	 */
	@ApiField("sku_properties_name")
	private String skuPropertiesName;

	/**
	 * 最近退款ID /** SKU的值。如：机身颜色:黑色;手机套餐:官方标配
	 */
	@ApiField("refund_id")
	private Long refundId;

	/**
	 * 是否超卖
	 */
	@ApiField("is_oversold")
	private Boolean isOversold;

	/**
	 * 是否是服务订单，是返回true，否返回false。
	 */
	@ApiField("is_service_order")
	private Boolean isServiceOrder;

	/**
	 * 子订单的交易结束时间说明：子订单有单独的结束时间，与主订单的结束时间可能有所不同，在有退款发起的时候或者是主订单分阶段付款的时候，
	 * 子订单的结束时间会早于主订单的结束时间，所以开放这个字段便于订单结束状态的判断
	 */
	@ApiField("end_time")
	private Date endTime;

	/**
	 * 子订单发货时间，当卖家对订单进行了多次发货，子订单的发货时间和主订单的发货时间可能不一样了，那么就需要以子订单的时间为准。（没有进行多次发货的订单
	 * ，主订单的发货时间和子订单的发货时间都一样）
	 */
	@ApiField("consign_time")
	private String consignTime;

	/**
	 * top动态字段
	 */
	@ApiField("order_attr")
	private String orderAttr;

	/**
	 * 子订单的运送方式（卖家对订单进行多次发货之后，一个主订单下的子订单的运送方式可能不同，用order.
	 * shipping_type来区分子订单的运送方式）
	 */
	@ApiField("shipping_type")
	private String shippingType;

	/**
	 * 捆绑的子订单号，表示该子订单要和捆绑的子订单一起发货，用于卖家子订单捆绑发货
	 */
	@ApiField("bind_oid")
	private Long bindOid;

	/**
	 * 子订单发货的快递公司名称
	 */
	@ApiField("logistics_company")
	private String logisticsCompany;

	/**
	 * 子订单所在包裹的运单号
	 */
	@ApiField("invoice_no")
	private String invoiceNo;

	/**
	 * 表示订单交易是否含有对应的代销采购单。如果该订单中存在一个对应的代销采购单，那么该值为true；反之，该值为false。
	 */
	@ApiField("is_daixiao")
	private Boolean isDaixiao;

	/**
	 * 分摊之后的实付金额
	 */
	@ApiField("divide_order_fee")
	private String divideOrderFeeString;

	private BigDecimal divideOrderFee;

	/**
	 * 优惠分摊
	 */
	@ApiField("part_mjz_discount")
	private String partMjzDiscount;

	/**
	 * 对应门票有效期的外部id
	 */
	@ApiField("ticket_outer_id")
	private String ticketOuterId;

	/**
	 * 门票有效期的key
	 */
	@ApiField("ticket_expdate_key")
	private String ticketExpDateKey;

	/**
	 * 发货的仓库编码
	 */
	@ApiField("store_code")
	private String storeCode;

	/**
	 * 子订单是否是www订单
	 */
	@ApiField("is_www")
	private Boolean isWww;

	/**
	 * 支持家装类物流的类型
	 */
	@ApiField("tmser_spu_code")
	private String tmserSpuCode;

	/**
	 * bind_oid字段的升级，支持返回绑定的多个子订单，多个子订单以半角逗号分隔
	 */
	@ApiField("bind_oids")
	private String bindOids;

	/**
	 * 征集预售订单征集状态：1（征集中），2（征集成功），3（征集失败）
	 */
	@ApiField("zhengji_status")
	private String zhengjiStatus;

	/**
	 * 免单资格属性
	 */
	@ApiField("md_qualification")
	private String mdQualification;

	/**
	 * 免单金额
	 */
	@ApiField("md_fee")
	private String mdFeeString;

	private BigDecimal mdFee;

	/**
	 * 定制信息
	 */
	@ApiField("customization")
	private String customization;

	/**
	 * 库存类型：6为在途
	 */
	@ApiField("inv_type")
	private String invType;

	/**
	 * xxx
	 */
	@ApiField("xxx")
	private String xxx;

	/**
	 * 是否发货
	 */
	@ApiField("is_sh_ship")
	private Boolean isShShip;

	/**
	 * 仓储信息
	 */
	@ApiField("shipper")
	private String shipper;

	/**
	 * 订单履行类型，如喵鲜生极速达（jsd）
	 */
	@ApiField("f_type")
	private String fType;

	/**
	 * 订单履行状态，如喵鲜生极速达：分单完成
	 */
	@ApiField("f_status")
	private String fStatus;

	/**
	 * 单履行内容，如喵鲜生极速达：storeId,phone
	 */
	@ApiField("f_term")
	private String fTerm;

	/**
	 * 天猫搭配宝
	 */
	@ApiField("combo_id")
	private String comboId;

	/**
	 * 主商品订单id
	 */
	@ApiField("assembly_rela")
	private String assemblyRela;

	/**
	 * 价格
	 */
	@ApiField("assembly_price")
	private String assemblyPrice;

	/**
	 * assemblyItem
	 */
	@ApiField("assembly_item")
	private String assemblyItem;

	/**
	 * 天猫国际子订单计税优惠金额
	 */
	@ApiField("sub_order_tax_promotion_fee")
	private String subOrderTaxPromotionFee;

	/**
	 * 花呗分期期数
	 */
	@ApiField("fqg_num")
	private Long fqgNum;

	/**
	 * 是否商家承担手续费
	 */
	@ApiField("is_fqg_s_fee")
	private Boolean isFqgSFee;

	// ====================================================================
	// 主订单冗余
	// ====================================================================
	/**
	 * 实付金额。精确到2位小数;单位:元。如:200.07，表示:200元7分 //payment(主订单冗余字段)
	 */
	private BigDecimal tradePayment;

	/**
	 * 卖家是否已评价。可选值:true(已评价),false(未评价)//seller_rate(主订单冗余字段)
	 */
	private Boolean tradeSellerRate;

	/**
	 * 收货人的姓名//receiver_name(主订单冗余字段)
	 */
	private String receiverName;

	/**
	 * 收货人的所在省份// receiver_state(主订单冗余字段)
	 */
	private String receiverState;

	/**
	 * 收货人的详细地址// receiver_address(主订单冗余字段)
	 */
	private String receiverAddress;

	/**
	 * 收货人的邮编//receiver_zip(主订单冗余字段)
	 */
	private String receiverZip;

	/**
	 * 收货人的手机号码//receiver_mobile(主订单冗余字段)
	 */
	private String receiverMobile;

	/**
	 * 收货人的电话号码//receiver_phone(主订单冗余字段)
	 */
	private String receiverPhone;

	/**
	 * 卖家发货时间。格式:yyyy-MM-ddHH:mm:ss//consign_time(主订单冗余字段)
	 */
	private Date tradeConsignTime;

	/**
	 * 卖家实际收到的支付宝打款金额（由于子订单可以部分确认收货，这个金额会随着子订单的确认收货而不断增加，交易成功后等于买家实付款减去退款金额）。
	 * 精确到2位小数;单位:元。如:200.07，表示:200元7分//received_payment(主订单冗余字段)
	 */
	private BigDecimal receivedPayment;

	/**
	 * 收货人国籍//receiver_country(主订单冗余字段)
	 */
	private String receiverCountry;

	/**
	 * 收货人街道地址//receiver_town(主订单冗余字段)
	 */
	private String receiverTown;

	/**
	 * 门店自提，总店发货，分店取货的门店自提订单标识 //shop_pick(主订单冗余字段)
	 */
	private String shopPick;

	/**
	 * 交易编号(父订单的交易编号)//tid(主订单冗余字段)
	 */
	@JsonSerialize(using=ToStringSerializer.class)
	private Long tid;

	/**
	 * 商品购买数量。取值范围：大于零的整数,对于一个trade对应多个order的时候（一笔主订单，对应多笔子订单），num=0，
	 * num是一个跟商品关联的属性，一笔订单对应多比子订单的时候，主订单上的num无意义。//num(主订单冗余字段)
	 */
	private Long tradeNum;

	/**
	 * 交易状态。可选值:*TRADE_NO_CREATE_PAY(没有创建支付宝交易)*WAIT_BUYER_PAY(等待买家付款)*
	 * SELLER_CONSIGNED_PART(卖家部分发货)*WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款)*
	 * WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货)*TRADE_BUYER_SIGNED(买家已签收,
	 * 货到付款专用)*TRADE_FINISHED(交易成功)*TRADE_CLOSED(付款以后用户退款成功，交易自动关闭)*
	 * TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)*PAY_PENDING(国际信用卡支付付款确认中)*
	 * WAIT_PRE_AUTH_CONFIRM(0元购合约中)*PAID_FORBID_CONSIGN(拼团中订单，已付款但禁止发货)//status
	 * (主订单冗余字段)
	 */
	private String tradeStatus;

	/**
	 * 交易标题，以店铺名作为此标题的值。注:taobao.trades.get接口返回的Trade中的title是商品名称//title(
	 * 主订单冗余字段)
	 */
	private String tradeTitle;

	/**
	 * 交易类型列表，同时查询多种交易类型可用逗号分隔。默认同时查询guarantee_trade,auto_delivery,ec,
	 * cod的4种交易类型的数据可选值fixed(一口价)auction(拍卖)guarantee_trade(一口价、拍卖)auto_delivery
	 * (自动发货)independent_simple_trade(旺店入门版交易)independent_shop_trade(旺店标准版交易)ec(
	 * 直冲)cod(货到付款)fenxiao(分销)game_equipment(游戏装备)shopex_trade(ShopEX交易)
	 * netcn_trade(万网交易)external_trade(统一外部交易)o2o_offlinetrade（O2O交易）step(万人团)
	 * nopaid(无付款订单)pre_auth_type(预授权0元购机交易)//type(主订单冗余字段)
	 */
	private String tradeType;

	/**
	 * 商品金额（商品价格乘以数量的总金额）。精确到2位小数;单位:元。如:200.07，表示:200元7分//total_fee(主订单冗余字段)
	 */
	private BigDecimal tradeTotalFee;

	/**
	 * 交易创建时间。格式:yyyy-MM-ddHH:mm:ss//created(主订单冗余字段)
	 */
	private Date tradeCreated;

	/**
	 * 交易创建时段，无日期，格式:HH:mm:ss
	 */
	private String tradeCreatedTime;

	/**
	 * 付款时间。格式:yyyy-MM-ddHH:mm:ss。订单的付款时间即为物流订单的创建时间。//pay_time(主订单冗余字段)
	 */
	private Date tradePayTime;

	/**
	 * 付款时间，无日期。格式:HH:mm:ss。订单的付款时间即为物流订单的创建时间。
	 */
	private String tradePayTimeHms;

	/**
	 * 交易修改时间(用户对订单的任何修改都会更新此字段)。格式:yyyy-MM-ddHH:mm:ss//modified(主订单冗余字段)
	 */
	private Date tradeModified;

	/**
	 * 交易结束时间。交易成功时间(更新交易状态为成功的同时更新)/确认收货时间或者交易关闭时间。格式:yyyy-MM-ddHH:mm:ss//
	 * end_time(主订单冗余字段)
	 */
	private Date tradeEndTime;

	/**
	 * 买家备注旗帜（与淘宝网上订单的买家备注旗帜对应，只有买家才能查看该字段）红、黄、绿、蓝、紫分别对应1、2、3、4、5//buyer_flag(
	 * 主订单冗余字段)
	 */
	private Long buyerFlag;

	/**
	 * 卖家备注旗帜（与淘宝网上订单的卖家备注旗帜对应，只有卖家才能查看该字段）红、黄、绿、蓝、紫分别对应1、2、3、4、5//seller_flag(
	 * 主订单冗余字段)
	 */
	private Long sellerFlag;

	/**
	 * 分阶段付款的订单状态（例如万人团订单等），目前有三返回状态FRONT_NOPAID_FINAL_NOPAID(定金未付尾款未付)，
	 * FRONT_PAID_FINAL_NOPAID(定金已付尾款未付)，FRONT_PAID_FINAL_PAID(定金和尾款都付)//
	 * step_trade_status(主订单冗余字段)
	 */
	private String stepTradeStatus;

	/**
	 * 分阶段付款的已付金额（万人团订单已付金额）// step_paid_fee(主订单冗余字段)
	 */
	private BigDecimal stepPaidFee;

	/**
	 * 创建交易时的物流方式（交易完成前，物流方式有可能改变，但系统里的这个字段一直不变）。可选值：free(卖家包邮),post(平邮),express
	 * (快递),ems(EMS),virtual(虚拟发货)，25(次日必达)，26(预约配送)。//shipping_type(主订单冗余字段)
	 */
	private String tradeShippingType;

	/**
	 * 买家货到付款服务费。精确到2位小数;单位:元。如:12.07，表示:12元7分//buyer_cod_fee(主订单冗余字段)
	 */
	private BigDecimal buyerCodFee;

	/**
	 * 卖家手工调整金额，精确到2位小数，单位：元。如：200.07，表示：200元7分。来源于订单价格修改，如果有多笔子订单的时候，这个为0，
	 * 单笔的话则跟[order].adjust_fee一样//adjust_fee(主订单冗余字段)
	 */
	private BigDecimal tradeAdjustFee;

	/**
	 * 交易内部来源。WAP(手机);HITAO(嗨淘);TOP(TOP平台);TAOBAO(普通淘宝);JHS(聚划算)一笔订单可能同时有以上多个标记，
	 * 则以逗号分隔//trade_from(主订单冗余字段)
	 */
	private String tradeFrom;

	/**
	 * 买家是否已评价。可选值:true(已评价),false(未评价)。如买家只评价未打分，此字段仍返回false
	 * //buyer_rate(主订单冗余字段)
	 */
	private Boolean tradeBuyerRate;

	/**
	 * 收货人的所在城市//receiver_city(主订单冗余字段)
	 */
	private String receiverCity;

	/**
	 * 收货人的所在地区//receiver_district(主订单冗余字段)
	 *
	 */
	private String receiverDistrict;

	/**
	 * 订单来源 1、拉取sysInfo数据库，同步订单 2、用户导入
	 */
	private Integer tradeSource;

	/**
	 * 营销短信发送id
	 */
	private Long msgId;

	private TradeDTO trade;

	private TradeRates tradeRates;
	/**
	 * 星期几(付款时间时周一) 周日 1 ，周一 2 ，周二 3 周三  4 。。。。周六 7（新添加）
	 * @return
	 */
	private String week;
	/**
	 * 评价结果,可选值:good(好评),neutral(中评),bad(差评),(已删中差评)iSDelete（新添加）
	 * @return
	 */
	private String result;
	/**
	 * 格式 HH:mm:ss
	 * @return
	 */
	private String tradeEndTimeHms;
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
    /**
     * 退款订单状态
     */
	private String tbrefundStatus;
	/**
	 * 退款子订单状态
	 */
	private String tborderStatus;
	
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

	public String getOuterIid() {
		return outerIid;
	}

	public void setOuterIid(String outerIid) {
		this.outerIid = outerIid;
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

	public Long getCid() {
		return cid;
	}

	public void setCid(Long cid) {
		this.cid = cid;
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

	public String getEstimateConTime() {
		return estimateConTime;
	}

	public void setEstimateConTime(String estimateConTime) {
		this.estimateConTime = estimateConTime;
	}

	public Long getOid() {
		return oid;
	}

	public void setOid(Long oid) {
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

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
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

	public BigDecimal getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(BigDecimal totalFee) {
		this.totalFee = totalFee;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public BigDecimal getDiscountFee() {
		return discountFee;
	}

	public void setDiscountFee(BigDecimal discountFee) {
		this.discountFee = discountFee;
	}

	public BigDecimal getAdjustFee() {
		return adjustFee;
	}

	public void setAdjustFee(BigDecimal adjustFee) {
		this.adjustFee = adjustFee;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public String getSkuPropertiesName() {
		return skuPropertiesName;
	}

	public void setSkuPropertiesName(String skuPropertiesName) {
		this.skuPropertiesName = skuPropertiesName;
	}

	public Long getRefundId() {
		return refundId;
	}

	public void setRefundId(Long refundId) {
		this.refundId = refundId;
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

	public Long getBindOid() {
		return bindOid;
	}

	public void setBindOid(Long bindOid) {
		this.bindOid = bindOid;
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

	public Boolean getIsDaixiao() {
		return isDaixiao;
	}

	public void setIsDaixiao(Boolean isDaixiao) {
		this.isDaixiao = isDaixiao;
	}

	public BigDecimal getDivideOrderFee() {
		return divideOrderFee;
	}

	public void setDivideOrderFee(BigDecimal divideOrderFee) {
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

	public String getTicketExpDateKey() {
		return ticketExpDateKey;
	}

	public void setTicketExpDateKey(String ticketExpDateKey) {
		this.ticketExpDateKey = ticketExpDateKey;
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

	public BigDecimal getMdFee() {
		return mdFee;
	}

	public void setMdFee(BigDecimal mdFee) {
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

	public String getXxx() {
		return xxx;
	}

	public void setXxx(String xxx) {
		this.xxx = xxx;
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

	public String getfTerm() {
		return fTerm;
	}

	public void setfTerm(String fTerm) {
		this.fTerm = fTerm;
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

	public String getSubOrderTaxPromotionFee() {
		return subOrderTaxPromotionFee;
	}

	public void setSubOrderTaxPromotionFee(String subOrderTaxPromotionFee) {
		this.subOrderTaxPromotionFee = subOrderTaxPromotionFee;
	}

	public Long getFqgNum() {
		return fqgNum;
	}

	public void setFqgNum(Long fqgNum) {
		this.fqgNum = fqgNum;
	}

	public Boolean getIsFqgSFee() {
		return isFqgSFee;
	}

	public void setIsFqgSFee(Boolean isFqgSFee) {
		this.isFqgSFee = isFqgSFee;
	}

	public BigDecimal getTradePayment() {
		return tradePayment;
	}

	public void setTradePayment(BigDecimal tradePayment) {
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

	public Date getTradeConsignTime() {
		return tradeConsignTime;
	}

	public void setTradeConsignTime(Date tradeConsignTime) {
		this.tradeConsignTime = tradeConsignTime;
	}

	public BigDecimal getReceivedPayment() {
		return receivedPayment;
	}

	public void setReceivedPayment(BigDecimal receivedPayment) {
		this.receivedPayment = receivedPayment;
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

	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
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

	public String getTradeTitle() {
		return tradeTitle;
	}

	public void setTradeTitle(String tradeTitle) {
		this.tradeTitle = tradeTitle;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public BigDecimal getTradeTotalFee() {
		return tradeTotalFee;
	}

	public void setTradeTotalFee(BigDecimal tradeTotalFee) {
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

	public Long getBuyerFlag() {
		return buyerFlag;
	}

	public void setBuyerFlag(Long buyerFlag) {
		this.buyerFlag = buyerFlag;
	}

	public Long getSellerFlag() {
		return sellerFlag;
	}

	public void setSellerFlag(Long sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	public String getStepTradeStatus() {
		return stepTradeStatus;
	}

	public void setStepTradeStatus(String stepTradeStatus) {
		this.stepTradeStatus = stepTradeStatus;
	}

	public BigDecimal getStepPaidFee() {
		return stepPaidFee;
	}

	public void setStepPaidFee(BigDecimal stepPaidFee) {
		this.stepPaidFee = stepPaidFee;
	}

	public String getTradeShippingType() {
		return tradeShippingType;
	}

	public void setTradeShippingType(String tradeShippingType) {
		this.tradeShippingType = tradeShippingType;
	}

	public BigDecimal getBuyerCodFee() {
		return buyerCodFee;
	}

	public void setBuyerCodFee(BigDecimal buyerCodFee) {
		this.buyerCodFee = buyerCodFee;
	}

	public BigDecimal getTradeAdjustFee() {
		return tradeAdjustFee;
	}

	public void setTradeAdjustFee(BigDecimal tradeAdjustFee) {
		this.tradeAdjustFee = tradeAdjustFee;
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

	public Integer getTradeSource() {
		return tradeSource;
	}

	public void setTradeSource(Integer tradeSource) {
		this.tradeSource = tradeSource;
	}

	public String getPriceString() {
		return priceString;
	}

	public void setPriceString(String priceString) {
		this.priceString = priceString;
	}

	public String getTotalFeeString() {
		return totalFeeString;
	}

	public void setTotalFeeString(String totalFeeString) {
		this.totalFeeString = totalFeeString;
	}

	public String getPaymentString() {
		return paymentString;
	}

	public void setPaymentString(String paymentString) {
		this.paymentString = paymentString;
	}

	public String getDiscountFeeString() {
		return discountFeeString;
	}

	public void setDiscountFeeString(String discountFeeString) {
		this.discountFeeString = discountFeeString;
	}

	public String getAdjustFeeString() {
		return adjustFeeString;
	}

	public void setAdjustFeeString(String adjustFeeString) {
		this.adjustFeeString = adjustFeeString;
	}

	public String getDivideOrderFeeString() {
		return divideOrderFeeString;
	}

	public void setDivideOrderFeeString(String divideOrderFeeString) {
		this.divideOrderFeeString = divideOrderFeeString;
	}

	public String getMdFeeString() {
		return mdFeeString;
	}

	public void setMdFeeString(String mdFeeString) {
		this.mdFeeString = mdFeeString;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public TradeDTO getTrade() {
		return trade;
	}

	public void setTrade(TradeDTO trade) {
		this.trade = trade;
	}

	public TradeRates getTradeRates() {
		return tradeRates;
	}

	public void setTradeRates(TradeRates tradeRates) {
		this.tradeRates = tradeRates;
	}

	
	public String getTradeCreatedTime() {
		return tradeCreatedTime;
	}

	public void setTradeCreatedTime(String tradeCreatedTime) {
		this.tradeCreatedTime = tradeCreatedTime;
	}

	public String getTradePayTimeHms() {
		return tradePayTimeHms;
	}

	public void setTradePayTimeHms(String tradePayTimeHms) {
		this.tradePayTimeHms = tradePayTimeHms;
	}

	public String getWeek() {
		return week;
	}

	public void setWeek(String week) {
		this.week = week;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getTradeEndTimeHms() {
		return tradeEndTimeHms;
	}

	public void setTradeEndTimeHms(String tradeEndTimeHms) {
		this.tradeEndTimeHms = tradeEndTimeHms;
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

	public String getTbrefundStatus() {
		return tbrefundStatus;
	}

	public void setTbrefundStatus(String tbrefundStatus) {
		this.tbrefundStatus = tbrefundStatus;
	}

	public String getTborderStatus() {
		return tborderStatus;
	}

	public void setTborderStatus(String tborderStatus) {
		this.tborderStatus = tborderStatus;
	}  
}
