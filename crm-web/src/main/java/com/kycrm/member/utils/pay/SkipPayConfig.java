package com.kycrm.member.utils.pay;

import java.io.Serializable;



public class SkipPayConfig implements Serializable{
		/** 
	 * Project Name:s2jh4net 
	 * File Name:SkipPayConfig.java 
	 * Package Name:s2jh.biz.shop.crm.payment.util 
	 * Date:2017年11月16日下午3:29:29 
	 * Copyright (c) 2017,  All Rights Reserved. 
	 * 
	 */  
	private static final long serialVersionUID = -6390161467241573037L;

	// 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
	public static String app_id  ;
	// 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key ;
	// 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key ;
	// 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String notify_url ;
	// 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
	public static String return_url;
	// 签名方式
	public static String sign_type; 
	// 字符编码格式
	public static String charset;
	// 支付宝网关
	public static String gatewayUrl ;
	public static String getApp_id() {
		return app_id;
	}
	public static void setApp_id(String app_id) {
		SkipPayConfig.app_id = app_id;
	}
	public static String getMerchant_private_key() {
		return merchant_private_key;
	}
	public static void setMerchant_private_key(String merchant_private_key) {
		SkipPayConfig.merchant_private_key = merchant_private_key;
	}
	public static String getAlipay_public_key() {
		return alipay_public_key;
	}
	public static void setAlipay_public_key(String alipay_public_key) {
		SkipPayConfig.alipay_public_key = alipay_public_key;
	}
	public static String getNotify_url() {
		return notify_url;
	}
	public static void setNotify_url(String notify_url) {
		SkipPayConfig.notify_url = notify_url;
	}
	public static String getReturn_url() {
		return return_url;
	}
	public static void setReturn_url(String return_url) {
		SkipPayConfig.return_url = return_url;
	}
	public static String getSign_type() {
		return sign_type;
	}
	public static void setSign_type(String sign_type) {
		SkipPayConfig.sign_type = sign_type;
	}
	public static String getCharset() {
		return charset;
	}
	public static void setCharset(String charset) {
		SkipPayConfig.charset = charset;
	}
	public static String getGatewayUrl() {
		return gatewayUrl;
	}
	public static void setGatewayUrl(String gatewayUrl) {
		SkipPayConfig.gatewayUrl = gatewayUrl;
	}
	
}
