package com.kycrm.transferdata.thread;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.entity.trade.TbTrade;
import com.kycrm.member.domain.vo.trade.TradeFullinfoGetResponse;
import com.kycrm.member.service.trade.IMongoHistroyTradeService;
import com.kycrm.transferdata.entity.Orders;
import com.kycrm.transferdata.entity.OrdersDTO;
import com.kycrm.transferdata.entity.TradeDTO;
import com.kycrm.transferdata.trade.service.ITradeTransferService;
import com.kycrm.util.GzipUtil;
import com.kycrm.util.JsonUtil;
import com.taobao.api.internal.util.TaobaoUtils;

public class TradeGetLostDataThread implements Callable<Integer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(TradeGetLostDataThread.class);

	private Long uid;

	private String collectionName;

	// 订单迁移数据服务
	private ITradeTransferService tradeTransferService;

	// 迁移订单Dubbo服务
	private IMongoHistroyTradeService mongoHistroyTradeService;

	private List<Long> tidList;

	private int pageSize = 1000;

	private Boolean isNeedToSynchornizeMember;

	private DecimalFormat decimalFormat = new DecimalFormat("0.##");

	public TradeGetLostDataThread(Long uid, String collectionName, ITradeTransferService tradeTransferService,
			IMongoHistroyTradeService mongoHistroyTradeService, List<Long> tidList, boolean isNeedToSynchornizeMember) {
		super();
		this.uid = uid;
		this.collectionName = collectionName;
		this.tradeTransferService = tradeTransferService;
		this.mongoHistroyTradeService = mongoHistroyTradeService;
		this.tidList = tidList;
		this.isNeedToSynchornizeMember = isNeedToSynchornizeMember;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Integer call() throws Exception {
		Integer transferCount = 0;
		List<String> tidStrList = new ArrayList<String>();
		for (int i = 0; i < tidList.size(); i++) {
			tidStrList.add(tidList.get(i).toString().trim());
		}
		List<TradeDTO> oldTradeDTOList = this.tradeTransferService.getTradeByTidList(collectionName, tidStrList);
		LOGGER.info("collectionName = " + collectionName + " UID = " + uid + " oldTradeDTOList = "
				+ oldTradeDTOList.size());
		if (oldTradeDTOList.size() != 0) {
			// 将要存入MySQL的Trade数据
			List<TbTrade> tbTradeList = new ArrayList<TbTrade>(oldTradeDTOList.size());
			com.kycrm.member.domain.entity.trade.TradeDTO newTradeDTO = null;
			TradeFullinfoGetResponse taobaoResponse = null;
			TbTrade tbTrade = null;
			JSONObject jsonObject = null;
			List list = null;
			Orders orders = null;
			Object object = null;
			JSONObject trade = null;
			JSONObject order = null;
			byte[] compress = null;
			for (int i = 0; i < oldTradeDTOList.size(); i++) {
				tbTrade = new TbTrade();
				newTradeDTO = this.assembleTrade(oldTradeDTOList.get(i));
				tbTrade.setTid(newTradeDTO.getTid());
				tbTrade.setStatus(newTradeDTO.getStatus());
				tbTrade.setSellerNick(newTradeDTO.getSellerNick());
				tbTrade.setBuyerNick(newTradeDTO.getBuyerNick());
				tbTrade.setCreated(newTradeDTO.getCreated());
				taobaoResponse = new TradeFullinfoGetResponse();
				taobaoResponse.setTrade(newTradeDTO);
				String json = TaobaoUtils.objectToJson(taobaoResponse);
				jsonObject = JSONObject.parseObject(json);
				trade = jsonObject.getJSONObject("trade");
				trade = this.replaceKeyAndValue(trade);
				list = (List) jsonObject.getJSONObject("trade").getJSONArray("orders");
				for (int j = 0; j < list.size(); j++) {
					order = (JSONObject) list.get(j);
					if (order != null) {
						order = this.replaceKeyAndValue(order);
					}
				}
				orders = new Orders();
				orders.setOrder(list);
				object = JSONObject.toJSON(orders);
				jsonObject.getJSONObject("trade").put("orders", object);
				String jdpResponse = "{\"trade_fullinfo_get_response\":" + jsonObject.toJSONString() + "}";
				tbTrade.setJdpResponse(jdpResponse);
				tbTradeList.add(tbTrade);
				transferCount++;
				if (tbTradeList.size() != pageSize && transferCount != oldTradeDTOList.size()) {
					continue;
				} else {
					String jsonList = JsonUtil.toJson(tbTradeList);
					compress = GzipUtil.compress(jsonList);
					this.mongoHistroyTradeService.synTradeAndOrderMongoDate(uid, compress);
					if (isNeedToSynchornizeMember) {
						this.mongoHistroyTradeService.synMemberAndReceiveDetailMongoDate(uid, compress);
					}
					LOGGER.info("订单记录迁移数据 用户ID = " + uid + " collectionName = " + collectionName + " 迁移数据量 = "
							+ tbTradeList.size());
					tbTradeList.clear();
					Thread.sleep(200);
				}
			}
		}
		return transferCount;
	}

	/**
	 *
	 * @Author ZhengXiaoChen
	 * @Description 组装新版TradeDTO对象
	 * @Date 2018年8月22日上午11:58:34
	 * @param oldTradeDTO
	 * @return
	 * @throws Exception
	 * @ReturnType com.kycrm.member.domain.entity.trade.TradeDTO
	 */
	private com.kycrm.member.domain.entity.trade.TradeDTO assembleTrade(TradeDTO oldTradeDTO) throws Exception {
		com.kycrm.member.domain.entity.trade.TradeDTO newTradeDTO = new com.kycrm.member.domain.entity.trade.TradeDTO();
		String tid = oldTradeDTO.getTid();
		if (tid != null && !"".equals(tid)) {
			newTradeDTO.setTid(Long.valueOf(tid));
		}
		newTradeDTO.setTidStr(oldTradeDTO.getTid());
		newTradeDTO.setSellerNick(oldTradeDTO.getSellerNick());
		String adjustFee = oldTradeDTO.getAdjustFee();
		if (adjustFee != null && !"".equals(adjustFee)) {
			newTradeDTO.setAdjustFeeString(adjustFee);
		}
		Double payment = oldTradeDTO.getPayment();
		if (payment != null) {
			String paymentString = "";
			if (payment == 0D) {
				paymentString = "0.00";
				newTradeDTO.setPaymentString(new BigDecimal(paymentString).toString());
			} else {
				newTradeDTO.setPaymentString(new BigDecimal(this.decimalFormat.format(payment)).toString());
			}
		}
		newTradeDTO.setSellerRate(oldTradeDTO.getSellerRate());
		String postFee = oldTradeDTO.getPostFee();
		if (postFee != null && !"".equals(postFee)) {
			if ("0".equals(postFee)) {
				postFee = "0.00";
			}
			newTradeDTO.setPostFeeString(new BigDecimal(postFee).toString());
		}
		newTradeDTO.setReceiverName(oldTradeDTO.getReceiverName());
		newTradeDTO.setReceiverState(oldTradeDTO.getReceiverState());
		newTradeDTO.setReceiverAddress(oldTradeDTO.getReceiverAddress());
		newTradeDTO.setReceiverZip(oldTradeDTO.getReceiverZip());
		newTradeDTO.setReceiverMobile(oldTradeDTO.getReceiverMobile());
		newTradeDTO.setReceiverPhone(oldTradeDTO.getReceiverPhone());
		newTradeDTO.setConsignTime(oldTradeDTO.getConsignTimeUTC());
		Double receivedPayment = oldTradeDTO.getReceivedPayment();
		if (receivedPayment != null) {
			String receivedPaymentString = "";
			if (receivedPayment == 0D) {
				receivedPaymentString = "0.00";
				newTradeDTO.setReceivedPaymentString(new BigDecimal(receivedPaymentString).toString());
			} else {
				newTradeDTO.setReceivedPaymentString(new BigDecimal(receivedPayment).toString());
			}
		}
		newTradeDTO.setReceiverCountry(oldTradeDTO.getReceiverCountry());
		newTradeDTO.setReceiverTown(oldTradeDTO.getReceiverTown());
		newTradeDTO.setShopPick(oldTradeDTO.getShopPick());
		newTradeDTO.setNum(oldTradeDTO.getNum());
		newTradeDTO.setStatus(oldTradeDTO.getStatus());
		newTradeDTO.setTitle(oldTradeDTO.getTitle());
		newTradeDTO.setType(oldTradeDTO.getType());
		String discountFee = oldTradeDTO.getDiscountFee();
		if (discountFee != null && !"".equals(discountFee)) {
			newTradeDTO.setDiscountFeeString(new BigDecimal(discountFee).toString());
		}
		newTradeDTO.setHasPostFee(oldTradeDTO.getHasPostFee());
		Double totalFee = oldTradeDTO.getTotalFee();
		if (totalFee != null) {
			String totalFeeString = "";
			if (totalFee == 0D) {
				totalFeeString = "0.00";
				newTradeDTO.setTotalFeeString(new BigDecimal(totalFeeString).toString());
			} else {
				newTradeDTO.setTotalFeeString(new BigDecimal(this.decimalFormat.format(totalFee)).toString());
			}
		}
		newTradeDTO.setCreated(oldTradeDTO.getCreatedUTC());
		newTradeDTO.setPayTime(oldTradeDTO.getPayTimeUTC());
		newTradeDTO.setModified(oldTradeDTO.getModifiedUTC());
		newTradeDTO.setEndTime(oldTradeDTO.getEndTimeUTC());
		Integer buyerFlag = oldTradeDTO.getBuyerFlag();
		if (buyerFlag != null) {
			newTradeDTO.setBuyerFlag(Long.valueOf(buyerFlag));
		}
		String sellerFlag = oldTradeDTO.getSellerFlag();
		if (sellerFlag != null && !"".equals(sellerFlag)) {
			newTradeDTO.setSellerFlag(Long.valueOf(sellerFlag));
		}
		newTradeDTO.setBuyerNick(oldTradeDTO.getBuyerNick());
		newTradeDTO.setStepTradeStatus(oldTradeDTO.getStepTradeStatus());
		newTradeDTO.setStepPaidFee(oldTradeDTO.getStepPaidFee());
		newTradeDTO.setShippingType(oldTradeDTO.getShippingType());
		newTradeDTO.setBuyerCodFee(oldTradeDTO.getBuyerCodFee());
		newTradeDTO.setTradeFrom(oldTradeDTO.getTradeFrom());
		newTradeDTO.setBuyerRate(oldTradeDTO.getBuyerRate());
		newTradeDTO.setReceiverCity(oldTradeDTO.getReceiverCity());
		newTradeDTO.setReceiverDistrict(oldTradeDTO.getReceiverDistrict());
		newTradeDTO.setBuyerEmail(oldTradeDTO.getBuyerEmail());
		newTradeDTO.setBuyerAlipayNo(oldTradeDTO.getBuyer_alipay_no());
		newTradeDTO.setAlipayId(oldTradeDTO.getAlipayId());
		newTradeDTO.setAlipayNo(oldTradeDTO.getAlipayNo());
		newTradeDTO.setBuyerArea(oldTradeDTO.getBuyerArea());
		newTradeDTO.setBuyerObtainPointFee(oldTradeDTO.getBuyerObtainPointFee());
		newTradeDTO.setMsgId(oldTradeDTO.getMsgId());
		Long lastSendSmsTime = oldTradeDTO.getLastSendSmsTime();
		if (lastSendSmsTime != null) {
			newTradeDTO.setLastSendSmsTime(new Date(lastSendSmsTime));
		}
		newTradeDTO.setRefundFlag(oldTradeDTO.isRefundFlag());
		newTradeDTO.setOrders(this.assembleOrder(oldTradeDTO.getOrders()));
		return newTradeDTO;
	}

	/**
	 *
	 * @Author ZhengXiaoChen
	 * @Description 组装新版OrderDTO
	 * @Date 2018年8月22日下午12:24:50
	 * @param orders
	 * @return
	 * @throws Exception
	 * @ReturnType List<OrderDTO>
	 */
	private List<OrderDTO> assembleOrder(List<OrdersDTO> oldOrders) throws Exception {
		List<OrderDTO> newOrderList = new ArrayList<OrderDTO>(oldOrders.size());
		OrdersDTO oldOrdersDTO = null;
		OrderDTO newOrderDTO = null;
		if (oldOrders != null && oldOrders.size() > 0) {
			for (int i = 0; i < oldOrders.size(); i++) {
				oldOrdersDTO = oldOrders.get(i);
				newOrderDTO = new OrderDTO();
				String oid = oldOrdersDTO.getOid();
				if (oid != null && !"".equals(oid)) {
					newOrderDTO.setOid(Long.valueOf(oid));
				}
				String tid = oldOrdersDTO.getTid();
				if (tid != null && !"".equals(tid)) {
					newOrderDTO.setTid(Long.valueOf(tid));
				}
				newOrderDTO.setCid(oldOrdersDTO.getCid());
				newOrderDTO.setNumIid(oldOrdersDTO.getNumIid());
				newOrderDTO.setItemMealId(oldOrdersDTO.getItemMealId());
				newOrderDTO.setSkuId(oldOrdersDTO.getSkuId());
				newOrderDTO.setRefundId(oldOrdersDTO.getRefundId());
				newOrderDTO.setBindOid(oldOrdersDTO.getBindOid());
				newOrderDTO.setItemMealName(oldOrdersDTO.getItemMealName());
				newOrderDTO.setPicPath(oldOrdersDTO.getPicPath());
				newOrderDTO.setSellerNick(oldOrdersDTO.getSellerNick());
				newOrderDTO.setBuyerNick(oldOrdersDTO.getBuyerNick());
				newOrderDTO.setRefundStatus(
						oldOrdersDTO.getRefundStatus() == null ? "NO_REFUND" : oldOrdersDTO.getRefundStatus());
				newOrderDTO.setOuterIid(oldOrdersDTO.getOuteriid());
				newOrderDTO.setSnapshotUrl(oldOrdersDTO.getSnapshotUrl());
				newOrderDTO.setSnapshot(oldOrdersDTO.getSnapshot());
				newOrderDTO.setTimeoutActionTime(oldOrdersDTO.getTimeoutActionTimeUTC());
				newOrderDTO.setBuyerRate(oldOrdersDTO.getBuyerRate());
				newOrderDTO.setSellerRate(oldOrdersDTO.getSellerRate());
				newOrderDTO.setSellerType(oldOrdersDTO.getSellerType());
				newOrderDTO.setSubOrderTaxFee(oldOrdersDTO.getSubOrderTaxFee());
				newOrderDTO.setSubOrderTaxRate(oldOrdersDTO.getSubOrderTaxRate());
				newOrderDTO.setStatus(oldOrdersDTO.getStatus());
				newOrderDTO.setTitle(oldOrdersDTO.getTitle());
				newOrderDTO.setType(oldOrdersDTO.getType());
				newOrderDTO.setIid(oldOrdersDTO.getIid());
				Double price = oldOrdersDTO.getPrice();
				if (price != null) {
					String priceString = "";
					if (price == 0D) {
						priceString = "0.00";
						newOrderDTO.setPriceString(new BigDecimal(priceString).toString());
					} else {
						newOrderDTO.setPriceString(new BigDecimal(this.decimalFormat.format(price)).toString());
					}
				}
				newOrderDTO.setNum(oldOrdersDTO.getNum());
				newOrderDTO.setOuterSkuId(oldOrdersDTO.getOuterSkuId());
				newOrderDTO.setOrderFrom(oldOrdersDTO.getOrderFrom());
				Double totalFee = oldOrdersDTO.getTotalFee();
				if (totalFee != null) {
					String totalFeeString = "";
					if (totalFee == 0D) {
						totalFeeString = "0.00";
						newOrderDTO.setTotalFeeString(new BigDecimal(totalFeeString).toString());
					} else {
						newOrderDTO.setTotalFeeString(new BigDecimal(this.decimalFormat.format(totalFee)).toString());
					}
				}
				Double payment = oldOrdersDTO.getPayment();
				if (payment != null) {
					String paymentString = "";
					if (payment == 0D) {
						paymentString = "0.00";
						newOrderDTO.setPaymentString(new BigDecimal(paymentString).toString());
					} else {
						newOrderDTO.setPaymentString(new BigDecimal(this.decimalFormat.format(payment)).toString());
					}
				}
				String discountFee = oldOrdersDTO.getDiscountFee();
				if (discountFee != null && !"".equals(discountFee)) {
					newOrderDTO.setDiscountFeeString(new BigDecimal(discountFee).toString());
				}
				String adjustFee = oldOrdersDTO.getAdjustFee();
				if (adjustFee != null && !"".equals(adjustFee)) {
					newOrderDTO.setAdjustFeeString(new BigDecimal(adjustFee).toString());
				}
				newOrderDTO.setModified(oldOrdersDTO.getModifiedUTC());
				newOrderDTO.setSkuPropertiesName(oldOrdersDTO.getSkuPropertiesName());
				newOrderDTO.setIsOversold(oldOrdersDTO.getIsOversold());
				newOrderDTO.setIsServiceOrder(oldOrdersDTO.getIsServiceOrder());
				newOrderDTO.setEndTime(oldOrdersDTO.getEndTimeUTC());
				newOrderDTO.setConsignTime(oldOrdersDTO.getConsignTime());
				newOrderDTO.setOrderAttr(oldOrdersDTO.getOrderAttr());
				newOrderDTO.setShippingType(oldOrdersDTO.getShippingType());
				newOrderDTO.setLogisticsCompany(oldOrdersDTO.getLogisticsCompany());
				newOrderDTO.setInvoiceNo(oldOrdersDTO.getInvoiceNo());
				newOrderDTO.setIsDaixiao(oldOrdersDTO.getIsdaixiao());
				String divideOrderFee = oldOrdersDTO.getDivideOrderFee();
				if (divideOrderFee != null && !"".equals(divideOrderFee)) {
					newOrderDTO.setDivideOrderFeeString(new BigDecimal(divideOrderFee).toString());
				}
				newOrderDTO.setPartMjzDiscount(oldOrdersDTO.getPartMjzDiscount());
				newOrderDTO.setTicketOuterId(oldOrdersDTO.getTicketOuterId());
				newOrderDTO.setTicketExpDateKey(oldOrdersDTO.getTicketExpdateKey());
				newOrderDTO.setStoreCode(oldOrdersDTO.getStoreCode());
				newOrderDTO.setIsWww(oldOrdersDTO.getIsWww());
				newOrderDTO.setTmserSpuCode(oldOrdersDTO.getTmserSpuCode());
				newOrderDTO.setBindOids(oldOrdersDTO.getBindOids());
				newOrderDTO.setZhengjiStatus(oldOrdersDTO.getZhengjiStatus());
				newOrderDTO.setMdQualification(oldOrdersDTO.getMdQualification());
				String mdFee = oldOrdersDTO.getMdFee();
				if (mdFee != null && !"".equals(mdFee)) {
					newOrderDTO.setMdFeeString(new BigDecimal(mdFee).toString());
				}
				newOrderDTO.setCustomization(oldOrdersDTO.getCustomization());
				newOrderDTO.setInvType(oldOrdersDTO.getInvType());
				newOrderDTO.setIsShShip(oldOrdersDTO.getIsShShip());
				newOrderDTO.setShipper(oldOrdersDTO.getShipper());
				newOrderDTO.setfType(oldOrdersDTO.getfType());
				newOrderDTO.setfStatus(oldOrdersDTO.getfStatus());
				newOrderDTO.setfTerm(oldOrdersDTO.getFTERM());
				newOrderDTO.setComboId(oldOrdersDTO.getComboId());
				newOrderDTO.setAssemblyRela(oldOrdersDTO.getAssemblyRela());
				newOrderDTO.setAssemblyPrice(oldOrdersDTO.getAssemblyPrice());
				newOrderDTO.setAssemblyItem(oldOrdersDTO.getAssemblyItem());
				newOrderDTO.setReceiverDistrict(oldOrdersDTO.getReceiverDistrict());
				newOrderDTO.setReceiverCity(oldOrdersDTO.getReceiverCity());
				newOrderDTO.setStepTradeStatus(oldOrdersDTO.getStepTradeStatus());
				newOrderDTO.setTradeCreated(oldOrdersDTO.getCreatedUTC());
				newOrderDTO.setReceiverName(oldOrdersDTO.getReceiverName());
				newOrderDTO.setReceiverMobile(oldOrdersDTO.getReceiverMobile());
				Integer buyerFlag = oldOrdersDTO.getBuyerFlag();
				if (buyerFlag != null) {
					newOrderDTO.setBuyerFlag(Long.valueOf(buyerFlag));
				}
				Integer sellerFlag = oldOrdersDTO.getSellerFlag();
				if (sellerFlag != null) {
					newOrderDTO.setSellerFlag(Long.valueOf(sellerFlag));
				}
				newOrderList.add(newOrderDTO);
			}
		}
		return newOrderList;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 替换字段
	 * @Date 2018年9月18日下午4:07:01
	 * @param jsonObject
	 * @return
	 * @throws Exception
	 * @ReturnType JSONObject
	 */
	private JSONObject replaceKeyAndValue(JSONObject jsonObject) throws Exception {
		if (jsonObject.containsKey("post_fee_string")) {
			jsonObject.put("post_fee", jsonObject.get("post_fee_string").toString());
			jsonObject.remove("post_fee_string");
		}
		if (jsonObject.containsKey("adjust_fee_string")) {
			jsonObject.put("adjust_fee", jsonObject.get("adjust_fee_string").toString());
			jsonObject.remove("adjust_fee_string");
		}
		if (jsonObject.containsKey("discount_fee_string")) {
			jsonObject.put("discount_fee", jsonObject.get("discount_fee_string").toString());
			jsonObject.remove("discount_fee_string");
		}
		if (jsonObject.containsKey("payment_string")) {
			jsonObject.put("payment", jsonObject.get("payment_string").toString());
			jsonObject.remove("payment_string");
		}
		if (jsonObject.containsKey("received_payment_string")) {
			jsonObject.put("received_payment", jsonObject.get("received_payment_string").toString());
			jsonObject.remove("received_payment_string");
		}
		if (jsonObject.containsKey("total_fee_string")) {
			jsonObject.put("total_fee", jsonObject.get("total_fee_string").toString());
			jsonObject.remove("total_fee_string");
		}
		if (jsonObject.containsKey("divide_order_fee_string")) {
			jsonObject.put("divide_order_fee", jsonObject.get("divide_order_fee_string").toString());
			jsonObject.remove("divide_order_fee_string");
		}
		if (jsonObject.containsKey("price_string")) {
			jsonObject.put("price", jsonObject.get("price_string").toString());
			jsonObject.remove("price_string");
		}
		return jsonObject;
	}
}
