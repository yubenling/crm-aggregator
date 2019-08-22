package com.kycrm.util;
/** 
* @author wy
* @version 创建时间：2017年8月23日 下午1:57:36
*/
public abstract class TmcInfo {
	/** 服务订单支付消息 */
	public static final String FUWU_ORDERPAID_TOPIC = "taobao_fuwu_OrderPaid";
	
	/** 服务开通消息 */
	public static final String FUWU_SERVICE_OPEN_TOPIC = "taobao_fuwu_ServiceOpen";
	
	/** 订单创建消息 */
	public static final String TRADE_CREATE_TOPIC = "taobao_trade_TradeCreate";

	/** 订单变更消息 */
	public static final String TRADE_CHANGE_TOPIC = "taobao_trade_TradeChanged";
	
	/** 订单支付消息 */
	public static final String TRADE_BUYERPAY_TOPIC = "taobao_trade_TradeBuyerPay";
	
	/** 订单交易成功消息 */
	public static final String TRADE_SUCCESS_TOPIC = "taobao_trade_TradeSuccess";
	
	/** 订单交易关闭消息 */
	public static final String TRADE_CLOSE_TOPIC = "taobao_trade_TradeClose";
	
	/** 退款成功消息 */
	public static final String REFUND_SUCCESS_TOPIC = "taobao_refund_RefundSuccess";
	
	/** 退款创建消息 */
	public static final String REFUND_CREATED_TOPIC = "taobao_refund_RefundCreated";
	
	/** 退款拒绝消息 */
	public static final String REFUND_REFUSE_TOPIC = "taobao_refund_RefundSellerRefuseAgreement";
	
	/** 卖家同意退款消息 */
	public static final String REFUND_AGREE_TOPIC = "taobao_refund_RefundSellerAgreeAgreement";
	
	/** 物流消息 */
	public static final String LOGSTIC_DETAIL_TOPIC = "taobao_logistics_LogsticDetailTrace";
	
	/** 交易评价消息 */
	public static final String TRADE_RATED_TOPIC = "taobao_trade_TradeRated";
	
}
