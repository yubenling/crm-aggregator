package com.kycrm.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

public class OrderImportUtil {
	public static final String ORDER_LIST = "ExportOrderList.csv";
	//时间格式
	public static final String DATE_FORMAT_ONE = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_TWO = "yyyy/MM/dd HH:mm";
	
	
	/**
	 * 订单头信息
	 */
	private static Set<String> orderHeaders = new HashSet<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("订单编号");add("买家会员名");add("买家支付宝账号");add("买家应付货款");add("打款商家金额");
			add("买家应付邮费");add("买家支付积分");add("总金额");add("返点积分");add("买家实际支付金额");
			add("买家实际支付积分");add("订单状态");add("收货人姓名");add("收货地址");add("宝贝标题");
			add("联系电话");add("联系手机");add("订单创建时间");add("订单付款时间");
			add("宝贝总数量");add("店铺名称");add("确认收货时间");
		}
	};
	/**
	 * 商品头信息
	 */
	private static Set<String> itemHeaders = new HashSet<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("订单编号");add("标题");add("价格");add("购买数量");
			add("外部系统编号");add("商品属性");add("套餐信息");add("备注");
			add("订单状态");add("商家编码");
		}
	};
	
	/**
	 * 上传文件名字
	 */
	private static Set<String> fileName = new HashSet<String>(){
		private static final long serialVersionUID = 1L;
		{
			add("ExportItemlList.csv");add("ExportOrderDetailList.csv");add("ExportOrderList.csv");
		}
	};
	
	
	public static Set<String> getItemHeaders() {
		return itemHeaders;
	}

	public static Set<String> getOrderHeaders() {
		return orderHeaders;
	}

	public static Set<String> getFileName() {
		return fileName;
	}

	/**
	 * 获取数据
	 * @return
	 */
	public static String getValFromArray(Map<String, Integer> headerMap,
			String[] dataArr, String header) {
		if (headerMap.containsKey(header)) {
			return dataArr[headerMap.get(header)];
		}
		return "";
	}
	
	/**
	 * 获取数据索引
	 * @return
	 */
	public static Integer getIndexFromArray(Map<String, Integer> headerMap,
			String[] dataArr, String header) {
		if (headerMap.containsKey(header)) {
			return headerMap.get(header);
		}
		return null;
	}
	
	/**
	 * 封装表头信息
	 */
	public static Map<String, Integer> makeHeaderMap(String[] headerArr) {
		Map<String, Integer> headerMap = new HashMap<String, Integer>();
		for (int i = 0; i < headerArr.length; i++) {
			headerMap.put(headerArr[i].trim(), i);
		}
		return headerMap;
	}
	
	
	/**
	 * 处理订单编号
	 */
	public static String disposeTid(String tid) {
		try {
			tid = tid.trim();
			if(tid.startsWith("=")){
				tid = tid.substring(2, tid.length()-1);
			}
			
			Pattern p = Pattern.compile("[0-9]+");
			boolean matches = p.matcher(tid).matches();
			if(matches)
				return tid;
		} catch (Exception e) {
		}
		return "";
	}
	
	/**
	 * 获取城市 
	 */
	public static String getCity(String receiverAddress) {
		String[] cityList = receiverAddress.split(" ");
		try {
			return cityList[1];
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * 获取省份 
	 */
	public static String getProvinces(String receiverAddress) {
			String[] cityList = receiverAddress.split(" ");
			try {
				return cityList[0];
			} catch (Exception e) {
				return null;
			}
	}
	
	/**
	 * 电话号码的处理
	 */
	public static String disposePhone(String phone) {
		String newPhone = "";
		if (phone.indexOf("'") != -1) {
			newPhone = phone.substring(1, phone.length());
		} else {
			newPhone = phone;
		}
		return newPhone;
	}
	
	
	/**
	 * @throws ParseException 
	 */
	public static Date timeFormat(String time,String format) throws ParseException{
		// 时间格式化
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.parse(time);
	}
	
	/**
	 * 获取交易的状态转换
	 * @param cause 
	 */
	public static String getdeal(String dealStatus, String cause) {
		String status = "";
		if (dealStatus.equals("等待买家付款")) {
			status = "WAIT_BUYER_PAY";
		}
		if (dealStatus.equals("买家已付款，等待卖家发货")) {
			status = "WAIT_SELLER_SEND_GOODS";
		}
		if (dealStatus.equals("等待卖家发货")) {
			status = "WAIT_SELLER_SEND_GOODS";
		}
		if (dealStatus.equals("卖家部分发货")) {
			status = "SELLER_CONSIGNED_PART";
		}
		if (dealStatus.equals("等待买家确认收货")) {
			status = "WAIT_BUYER_CONFIRM_GOODS";
		}
		if (dealStatus.equals("卖家已发货，等待买家确认")) {
			status = "WAIT_BUYER_CONFIRM_GOODS";
		}
		if (dealStatus.equals("买家已签收")) {
			status = "TRADE_BUYER_SIGNED";
		}
		if (dealStatus.equals("交易成功")) {
			status = "TRADE_FINISHED";
		}
		if (dealStatus.equals("交易关闭")) {
			status = "TRADE_CLOSED";
			if("买家未付款".equals(cause)){
				status = "TRADE_CLOSED_BY_TAOBAO";
			}
		}
		if (dealStatus.equals("交易被淘宝关闭")) {
			status = "TRADE_CLOSED_BY_TAOBAO";
		}
		if (dealStatus.equals("没有创建外部交易")) {
			status = "TRADE_NO_CREATE_PAY";
		}
		if (dealStatus.equals("余额宝0元购合约中")) {
			status = "WAIT_PRE_AUTH_CONFIRM";
		}
		if (dealStatus.equals("外卡支付付款确认中")) {
			status = "PAY_PENDING";
		}
		if (dealStatus.equals("所有买家未付款的交易")) {
			status = "ALL_WAIT_PAY";
		}
		if (dealStatus.equals("所有关闭的交易")) {
			status = "ALL_CLOSED";
		}
		return status;
	}
}
