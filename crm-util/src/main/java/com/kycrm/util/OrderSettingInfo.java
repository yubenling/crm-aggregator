package com.kycrm.util;

public abstract class OrderSettingInfo {
	/**
	 * 相同内容一个手机号码最大发送次数
	 */
	public static final int  MAX_CONTENT_SAME_COUNT = 3;
	/**
	 * 一个手机号码最大发送次数
	 */
	public static final int MAX_CONTENT_DIFFERENT_COUNT = 20;	
	/**
	 * 好评
	 */
	public static final String GOOD_RATE = "good";
	/**
	 * 中评
	 */
	public static final String NEUTRAL_RATE = "neutral";
	/**
	 * 差评
	 */
	public static final String BAD_RATE = "bad";
	/**
	 * tmc消息主题
	 */
	public static final String TOPIC = "topic";
	/**
	 * tmc消息的内容
	 */
	public static final String CONTENT= "content";
	/**
	 * redis设置前置常量
	 */
	public static final String TRADE_SETUP = "TRADE_SETUP_";
	/**
	 * 用户关怀设置开启
	 */
	public static final String ORDER_SETUP_OPEN="0";
	/**
	 * 用户关怀设置关闭
	 */
	public static final String ORDER_SETUP_CLOSE="1";
	/**
	 * 下单关怀
	 */
	public static final String CREATE_ORDER = "1";
	/**
	 * 常规催付，第一次催付（下完单后未付钱）
	 */
	public static final String FIRST_PUSH_PAYMENT = "2";
	/**
	 * 第二次催付（下完单后未付钱）
	 */
	public static final String SECOND_PUSH_PAYMENT = "3";
	/**
	 * 聚划算催付
	 */
	public static final String PREFERENTIAL_PUSH_PAYMENT = "4";
	/**
	 * 预售催付
	 */
	public static final String ADVANCE_PUSH_PAYMENT = "5";
	/**
	 * 发货提醒
	 */
	public static final String SHIPMENT_TO_REMIND = "6";
	/**
	 * 签收提醒
	 */
	public static final String REMIND_SIGNFOR = "9";
	/**
	 * 宝贝关怀
	 */
	public static final String COWRY_CARE = "12";
	/**
	 * 自动评价
	 */
	public static final String AUTO_RATE = "16";
	/**
	 * 手动订单提醒
	 */
	public static final String ORDER_MANUAL_REMIND = "26";
	/**
	 * 回款提醒
	 */
	public static final String RETURNED_PAYEMNT = "14";
	/**
	 * 付款关怀
	 */
	public static final String PAYMENT_CINCERN = "13";
	/**
	 * 退款创建
	 */
	public static final String REFUND_CREATED = "29";
	/**
	 * 退款同意，等待买家退货
	 */
	public static final String REFUND_AGREE = "31";
	/**
	 * 退款成功
	 */
	public static final String REFUND_SUCCESS = "30";
	/**
	 * 退款拒绝
	 */
	public static final String REFUND_REFUSE = "32";
	/**
	 * 中差评监控,锁 
	 */
	public static final String APPRAISE_MONITORING_ORDER = "20";
	/**
	 * 中差评安抚，锁
	 */
	public static final String APPRAISE_PACIFY_ORDER="21";
	/**
	 * 到达同城提醒
	 */
	public static final String ARRIVAL_LOCAL_REMIND = "7";
	/**
	 * 派件提醒
	 */
	public static final String SEND_GOODS_REMIND = "8";
	/**
	 * 疑难件提醒
	 */
	public static final String ABNORMAL_GOODS_REMIND = "10";
	/**
	 * 延时发货提醒
	 */
	public static final String DELAY_SEND_REMIND = "11";
	/**
	 * 好评提醒
	 */
	public static final String GOOD_VALUTION_REMIND = "37";
	/**
	 * 单条短信发送类型
	 */
	public static final String PAYMENT_SMS_TYPE = "2,3,4,5";
	/**
	 * tmc处理超时时间
	 */
	public static final int TMC_OVER_TIME = 5000;
}
