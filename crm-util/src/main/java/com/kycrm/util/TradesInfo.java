package com.kycrm.util;


//淘宝交易成功返回状态值
public abstract class TradesInfo {
	
	/**
	 * 订单来源  ---  全部
	 */
	public final static String  ORDER_FROM_TOTAL = "TOTAL";
	/**
	 * 订单来源  ---  聚划算
	 */
	public final static String  ORDER_FROM_JHS = "JHS";
	/**
	 * 订单来源  ---  淘宝
	 */
	public final static String  ORDER_FROM_TAOBAO = "TAOBAO";
	/**
	 * 订单来源   ---  无线
	 */
	public final static String  ORDER_FROM_WAP = "WAP";
	/**
	 * 没有创建支付宝交易
	 */
	public final static String TRADE_NO_CREATE_PAY = "TRADE_NO_CREATE_PAY";				
	/**
	 * 付款以前，卖家或买家主动关闭交易
	 */
	public final static String TRADE_CLOSED_BY_TAOBAO = "TRADE_CLOSED_BY_TAOBAO";	
	/**
	 * 国际信用卡支付付款确认中
	 */
	public final static String PAY_PENDING = "PAY_PENDING";		
	/**
	 * 等待买家付款
	 */
	public final static String WAIT_BUYER_PAY = "WAIT_BUYER_PAY";	
	/**
	 * 等待卖家发货,即:买家已付款
	 */
	public final static String WAIT_SELLER_SEND_GOODS = "WAIT_SELLER_SEND_GOODS";	
	/**
	 * 买家已签收,货到付款专用 
	 */
	public final static String TRADE_BUYER_SIGNED = "TRADE_BUYER_SIGNED";			
	/**
	 * 交易成功 
	 */
	public final static String TRADE_FINISHED = "TRADE_FINISHED";						
	/**
	 * 等待买家确认收货,即:卖家已发货 
	 */
	public final static String WAIT_BUYER_CONFIRM_GOODS = "WAIT_BUYER_CONFIRM_GOODS";	
	/**
	 * 付款以后用户退款成功，交易自动关闭
	 */
	public final static String TRADE_CLOSED = "TRADE_CLOSED";		
	/**
	 * 买家申请退款
	 */
	public final static String REFUND_CREATED="REFUND_CREATED";
	/**
	 * 退款成功
	 */
	public final static String REFUND_SUCCESS = "REFUND_SUCCESS";
	/**
	 * 物流签收的JSON数据
	 */
	public final static String SINGED_JSON_INFO = "1";
	/**
	 * 货到付款的JSON数据
	 */
	public final static String CHANGE_JSON_INFO = "2";
	/**
	 * 卖家拒绝退款
	 */
	public final static String REFUND_SELLER_REFUSE_AGREEMENT = "REFUND_SELLER_REFUSE_AGREEMENT";
	/**
	 * 卖家部分发货
	 */
	public final static String SELLER_CONSIGNED_PART = "SELLER_CONSIGNED_PART";
	public final static String WAITING_REFUND = "WAITING_REFUND";//等待退货
	public final static String LOGISTICSCONSIGN = "LOGISTICSCONSIGN";//卖家发货
	public final static String LOGISTICS_SENT_CITY="LOGISTICS_SENT_CITY";//同城提醒
	public final static String LOGISTICS_SENT_SCAN="LOGISTICS_SENT_SCAN";//派件提醒
	public final static String LOGISTICS_SIGNED = "LOGISTICS_SIGNED";//	签收成功
	/**
	 * 0元购合约中
	 */
	public final static String WAIT_PRE_AUTH_CONFIRM = "WAIT_PRE_AUTH_CONFIRM";
	
	/**
	 * 拼团中订单，已付款但禁止发货
	 */
	public final static String PAID_FORBID_CONSIGN = "PAID_FORBID_CONSIGN";
	
	
	public final static String DELAY_REMIND = "DELAY_REMIND";//延迟发货
	public final static String TRADE_RATED = "TRADE_RATED";//评价变更
	
	public final static String PUZZLE_REMIND = "PUZZLE_REMIND";//疑难件提醒
	/**
	 * 定金未付尾款未付
	 */
	public final static String FRONT_NOPAID_FINAL_NOPAID = "FRONT_NOPAID_FINAL_NOPAID";
	/**
	 * 定金已付尾款未付
	 */
	public final static String FRONT_PAID_FINAL_NOPAID = "FRONT_PAID_FINAL_NOPAID";
	/**
	 * 定金和尾款都付
	 */
	public final static String  FRONT_PAID_FINAL_PAID = "FRONT_PAID_FINAL_PAID";
}
