package com.kycrm.member.utils.pay;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;

public class PayHelperUtil  {
	
	/**
	 * @Description: 支付宝---跳转支付回调参数 
	 * @author HL
	 * @throws Exception 
	 * @date 2017年11月16日 下午4:49:05
	 */
	public static String buildSkipPay(String outTradeNo,Double totalAmount,String subject ) throws Exception{
		String submitForm="";
			JSONObject  json = new JSONObject();
			AlipayClient  aClient = new DefaultAlipayClient(SkipPayConfig.gatewayUrl, SkipPayConfig.app_id, SkipPayConfig.merchant_private_key, "json", 
					SkipPayConfig.charset, SkipPayConfig.alipay_public_key, SkipPayConfig.sign_type);
			AlipayTradePagePayRequest apRequest = new AlipayTradePagePayRequest();
			apRequest.setReturnUrl(SkipPayConfig.return_url);
			apRequest.setNotifyUrl(SkipPayConfig.notify_url);
			json.put("out_trade_no", outTradeNo);
			json.put("total_amount", totalAmount+"");
			json.put("subject", subject);
			json.put("product_code", "FAST_INSTANT_TRADE_PAY");
			apRequest.setBizContent(json.toJSONString());
			submitForm = aClient.pageExecute(apRequest).getBody();
		 return submitForm;
	 }
	
	/**
	 * @Description: 支付宝---二维码支付回调参数
	 * @author HL
	 * @date 2017年11月16日 下午4:51:26
	 */
	public static AlipayF2FPrecreateResult buildQRcodePay(String outTradeNo,Double totalAmount,String subject ){
				// 创建扫码支付请求builder，设置请求参数
				AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
				    .setSubject(subject).setTotalAmount(totalAmount+"").setOutTradeNo(outTradeNo)
				    .setStoreId(QRcodePayConfigUtil.storeId).setTimeoutExpress(QRcodePayConfigUtil.timeoutExpress)
				    .setNotifyUrl(SkipPayConfig.notify_url);//支付宝服务器主动通知商户服务器里指定的页面http路径,根据需要设置
				return QRcodePayConfigUtil.tradeService.tradePrecreate(builder);
	 }
}
