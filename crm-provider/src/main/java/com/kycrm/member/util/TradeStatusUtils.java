package com.kycrm.member.util;

/**
 * 订单的状态
 * 
 * @author Administrator 订单状态： TRADE_CLOSED_BY_TAOBAO 状态，则不要对此订单进行发货，切记啊！）
 *         TRADE_NO_CREATE_PAY(没有创建支付宝交易) WAIT_BUYER_PAY(等待买家付款)
 *         WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款)
 *         WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货)
 *         TRADE_BUYER_SIGNED(买家已签收,货到付款专用) TRADE_FINISHED(交易成功)
 *         TRADE_CLOSED(付款以后用户退款成功，交易自动关闭)
 *         TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易) PAY_PENDING(国际信用卡支付付款确认中)
 *         退款状态： WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意)
 *         WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货)
 *         WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货)
 *         SELLER_REFUSE_BUYER(卖家拒绝退款) CLOSED(退款关闭) SUCCESS(退款成功)'
 */
public class TradeStatusUtils {

	/**
	 * TRADE_NO_CREATE_PAY(没有创建支付宝交易)
	 */
	public static final String TRADE_NO_CREATE_PAY = "TRADE_NO_CREATE_PAY";

	/**
	 * WAIT_BUYER_PAY(等待买家付款)
	 */
	public static final String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

	/**
	 * WAIT_SELLER_SEND_GOODS(等待卖家发货,即:买家已付款)
	 */
	public static final String WAIT_SELLER_SEND_GOODS = "WAIT_SELLER_SEND_GOODS";

	/**
	 * 
	 */
	public static final String SELLER_CONSIGNED_PART = "SELLER_CONSIGNED_PART";

	/**
	 * WAIT_BUYER_CONFIRM_GOODS(等待买家确认收货,即:卖家已发货)
	 */
	public static final String WAIT_BUYER_CONFIRM_GOODS = "WAIT_BUYER_CONFIRM_GOODS";

	/**
	 * TRADE_BUYER_SIGNED(买家已签收,货到付款专用)
	 */
	public static final String TRADE_BUYER_SIGNED = "TRADE_BUYER_SIGNED";

	/**
	 * TRADE_FINISHED(交易成功)
	 */
	public static final String TRADE_FINISHED = "TRADE_FINISHED";

	/**
	 * TRADE_CLOSED(付款以后用户退款成功，交易自动关闭)
	 */
	public static final String TRADE_CLOSED = "TRADE_CLOSED";

	/**
	 * TRADE_CLOSED_BY_TAOBAO(付款以前，卖家或买家主动关闭交易)
	 */
	public static final String TRADE_CLOSED_BY_TAOBAO = "TRADE_CLOSED_BY_TAOBAO";

	/**
	 * PAY_PENDING(国际信用卡支付付款确认中)
	 */
	public static final String PAY_PENDING = "PAY_PENDING";

	/**
	 * WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意)
	 */
	public static final String REFUND_WAIT_SELLER_AGREE = "WAIT_SELLER_AGREE";

	/**
	 * WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货)
	 */
	public static final String REFUND_WAIT_BUYER_RETURN_GOODS = "WAIT_BUYER_RETURN_GOODS";

	/**
	 * WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货)
	 */
	public static final String REFUND_WAIT_SELLER_CONFIRM_GOODS = "WAIT_SELLER_CONFIRM_GOODS";

	/**
	 * SELLER_REFUSE_BUYER(卖家拒绝退款)
	 */
	public static final String REFUND_SELLER_REFUSE_BUYER = "SELLER_REFUSE_BUYER";

	/**
	 * CLOSED(退款关闭)
	 */
	public static final String REFUND_CLOSED = "CLOSED";

	/**
	 * SUCCESS(退款成功)'
	 */
	public static final String REFUND_SUCCESS = "SUCCESS";

}
