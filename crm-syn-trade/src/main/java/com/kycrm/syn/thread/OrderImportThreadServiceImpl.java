package com.kycrm.syn.thread;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TbTrade;
import com.kycrm.member.domain.entity.trade.TradeDTO;
import com.kycrm.util.DateUtils;

public class OrderImportThreadServiceImpl /*implements IOrderImportThreadService*/ {
	private static final Logger logger = LoggerFactory.getLogger(OrderImportThreadServiceImpl.class);

//	@Override
//	public void orderImportDataDispose(List<String[]> datasList, Map<String, Integer> headerMap, String userNick,
//			Long historyImportId, Map<String, List<String>> itemTitleMap, Long userId) {
//	}
//	/**
//	 * 将poi的数组list转换需要的TradeList
//	 * @param arrList
//	 * @param headerMap
//	 * @param userId
//	 * @param itemTitleMap
//	 * @return
//	 */
//	@Override
//	public List<TbTrade> importArrListToTradeList(List<String[]> arrList, Map<String, Integer> headerMap, Long userId,
//			Map<String, List<String>> itemTitleMap) {
//		return arrList.parallelStream().map(x -> {
//			return importArrayTradeConvert(x, headerMap, userId, itemTitleMap);
//		}).collect(Collectors.toList());
//	}
//	/**
//	 * poi数组实体转换trade实体
//	 */
//	@Override
//	public TbTrade importArrayTradeConvert(String[] dataArr,
//			Map<String, Integer> headerMap, Long userId,
//			/*Long historyImportId,*/ Map<String,List<String>> itemTitleMap) {
//		
//			// 创建TradeDTO对象封装参数
//			TradeDTO trade = new TradeDTO();
//			// 创建OrdersDTO对象封装数据
//			OrderDTO order = new OrderDTO();
//			TbTrade tbTrade = new TbTrade();
//
//			String tid = ImportUtils.getValFromArray(headerMap, dataArr, "订单编号");
//			String newTid = "";
//			if (tid != null && !tid.equals("")) {
//				newTid = ImportUtils.getTid(tid);
//				boolean judgeOrderId = ImportUtils.judgeOrderId(newTid);
//				if(judgeOrderId){
//					trade.setTid(Long.parseLong(newTid));
//					order.setOid(Long.parseLong(newTid));
//					order.setTid(Long.parseLong(newTid));
//				}else{
//					logger.error("失败----订单编号格式错误："+tid);
////					continue;
//				}
//			}else{
//				logger.error("失败----订单编号为空！！！");
////				continue;
//			}
//			String buyerNick = ImportUtils.getValFromArray(headerMap, dataArr, "买家会员名");
//			if (buyerNick != null && !buyerNick.equals("")) {
//				trade.setBuyerNick(buyerNick);
//				order.setBuyerNick(buyerNick);
//			}
//			String price = ImportUtils.getValFromArray(headerMap, dataArr, "买家应付货款");
////			if (price != null && !price.equals("")) {
////				trade.setPrice(new BigDecimal(price).doubleValue());
////				order.setPrice(new BigDecimal(price).doubleValue());
////			}
//			String postFee = ImportUtils.getValFromArray(headerMap, dataArr, "买家应付邮费");
//			if (postFee != null && !postFee.equals("")) {
//				trade.setPostFee(new BigDecimal(postFee));
//
//			}
//			String totalFee = ImportUtils.getValFromArray(headerMap, dataArr, "总金额");
//			if (totalFee != null && !totalFee.equals("")) {
//				trade.setTotalFee(new BigDecimal(totalFee));
//				order.setTotalFee(new BigDecimal(totalFee));
//			}
//			String payment = ImportUtils.getValFromArray(headerMap, dataArr, "买家实际支付金额");
//			if (payment != null && !payment.equals("")) {
////				double parseDouble = Double.parseDouble(payment);
//				trade.setPayment(new BigDecimal(payment));
//				order.setPayment(new BigDecimal(payment));
//			}
//			String status = ImportUtils.getValFromArray(headerMap, dataArr, "订单状态");
//			String cause = ImportUtils.getValFromArray(headerMap, dataArr, "订单关闭原因");
//			if (status != null && !status.equals("")) {
//				String getdeal = ImportUtils.getdeal(status,cause);
//				trade.setStatus(getdeal);
//				order.setStatus(getdeal);
//			}
//			String buyerMessage = ImportUtils.getValFromArray(headerMap, dataArr, "买家留言");
////			if (buyerMessage != null && !buyerMessage.equals("")) {
////				trade.setBuyerMessage(buyerMessage);
////			}
//			String receiverName = ImportUtils.getValFromArray(headerMap, dataArr, "收货人姓名");
//			if (receiverName != null && !receiverName.equals("")) {
//				trade.setReceiverName(receiverName);
//				order.setReceiverName(receiverName);
//			}
//			String receiverAddress = ImportUtils.getValFromArray(headerMap, dataArr, "收货地址");
//			if (receiverAddress != null && !receiverAddress.equals("")) {
//				String city = ImportUtils.getCity(receiverAddress);
//				String provinces = ImportUtils.getProvinces(receiverAddress);
//				trade.setReceiverAddress(receiverAddress);
//				trade.setReceiverCity(city);
//				trade.setReceiverState(provinces);
//				order.setReceiverCity(city);
//			}
//			String receiverPhone = ImportUtils.getValFromArray(headerMap, dataArr, "联系电话");
//			if (receiverPhone != null && !receiverPhone.equals("")) {
//				trade.setReceiverPhone(ImportUtils.getPhone(receiverPhone));
//			}
//			String receiverMobile = ImportUtils.getValFromArray(headerMap, dataArr, "联系手机");
//			if (receiverMobile != null && !receiverMobile.equals("")) {
//				String phone = ImportUtils.getPhone(receiverMobile);
//				trade.setReceiverMobile(phone);
//				order.setReceiverMobile(phone);
//			}
//			String created = ImportUtils.getValFromArray(headerMap, dataArr, "订单创建时间");
//			Date createDate = DateUtils.parseDate(created);
//			if (created != null && !created.equals("")) {
//				try {
////					trade.setCreatedUTC(ImportUtils.timeFormat(created, ImportUtils.DATE_FORMAT_ONE));
//					trade.setCreated(createDate);
////					order.setCreatedUTC(ImportUtils.timeFormat(created, ImportUtils.DATE_FORMAT_ONE));
//					order.setCreatedDate(createDate);
//				} catch (Exception e) {
//					try {
////						trade.setCreatedUTC(ImportUtils.timeFormat(created, ImportUtils.DATE_FORMAT_TWO));
//						trade.setCreated(createDate);
////						order.setCreatedUTC(ImportUtils.timeFormat(created, ImportUtils.DATE_FORMAT_TWO));
//						order.setCreatedDate(createDate);
//					} catch (Exception e1) {
//						logger.error("失败----订单创建时间格式错误："+created+"，正确格式：yyyy-MM-dd HH:mm:ss");
////						continue;
//					}
//				}
//			}
//			String payTime = ImportUtils.getValFromArray(headerMap, dataArr, "订单付款时间");
//			Date payTimeDate = DateUtils.parseDate(payTime);
//			if (payTime != null && !payTime.equals("")) {
////				try {
////					trade.setPayTimeUTC(ImportUtils.timeFormat(payTime, ImportUtils.DATE_FORMAT_ONE));
//					trade.setPayTime(payTimeDate);
////				} catch (ParseException e) {
////					try {
////						trade.setPayTimeUTC(ImportUtils.timeFormat(payTime, ImportUtils.DATE_FORMAT_TWO));
////						trade.setPayTime(payTimeDate);
////					} catch (ParseException e1) {
////						logger.error("失败----订单付款时间格式错误："+payTime+"，正确格式：yyyy-MM-dd HH:mm:ss");
////						continue;
////					}
////				}
//			}
//			String omnichannelParam = ImportUtils.getValFromArray(headerMap, dataArr, "宝贝标题");
//			if (omnichannelParam != null && !omnichannelParam.equals("")) {
//				order.setTitle(omnichannelParam);
//				trade.setTitle(omnichannelParam);
//				if (null != itemTitleMap && itemTitleMap.size() > 0
//						&& itemTitleMap.containsKey(omnichannelParam.trim())) {
////						trade.setNUM_IID(itemTitleMap.get(omnichannelParam.trim()).get(0));
//						order.setNumIid(Long.parseLong(itemTitleMap.get(omnichannelParam.trim()).get(0)));
//					}
//			}
//			String num = ImportUtils.getValFromArray(headerMap, dataArr, "宝贝总数量");
//			if (num != null && !num.equals("")) {
//				trade.setNum(Long.parseLong(num));
//				order.setNum(Long.parseLong(num));
//			}
//			String sellerNick = ImportUtils.getValFromArray(headerMap, dataArr, "店铺名称");
//			if (sellerNick != null && !sellerNick.equals("")) {
//				trade.setTitle(sellerNick);
//			}
//			// 设置userId  emmmmm 这里原来就是这么写的
//			trade.setSellerNick(userId.toString());
//			order.setSellerNick(userId.toString());
//
//			// 给type字段复制import--标识是导入的订单数据
//			trade.setType("import");
//			trade.setTradeFrom("import");
//			order.setType("import");
//			order.setOrderFrom("import");
//			trade.setSellerFlag(0l);
//			order.setSellerFlag(0l);
////			trade.setNodeFlag(1l);
//			
////		}
//		return null;

 
//	}
	
}
