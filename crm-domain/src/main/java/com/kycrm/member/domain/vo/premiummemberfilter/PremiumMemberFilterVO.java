package com.kycrm.member.domain.vo.premiummemberfilter;

import java.io.Serializable;
import java.util.List;

/**
 * 高级会员筛选入参
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年10月29日下午2:56:05
 * @Tags
 */
public class PremiumMemberFilterVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年10月29日上午11:47:04
	 */
	private static final long serialVersionUID = 1L;

	// 分库分表实体对应用户表的主键id
	private Long uid;

	// 【购物属性】付款商品名称集合【1】
	private List<PayGoodsName> payGoodsNameList;

	// 【购物属性】时段内购买某商品【2】
	private List<PayGoodsInPeriodTime> payGoodsInPeriodTimeList;

	// 【购物属性】付款商品数量【3】
	private List<PayGoodsCount> payGoodsCountList;

	// 【购物属性】拍下商品数量【4】
	private List<BookGoodsCount> bookGoodsCountList;

	// 【购物属性】商品价格【5】
	private List<GoodsPrice> goodsPriceList;

	// 【购物属性】订单金额【6】
	private List<OrderPrice> orderPriceList;

	// 【购物属性】收货地区【7】
	private List<ReceiveGoodsArea> receiveGoodsAreaList;

	// 【购物属性】时段内订单状态【8】
	private List<OrderStatusInPeriodTime> orderStatusInPeriodTimeList;

	// 【购物属性】订单发货时间【9】
	private List<OrderSentDate> orderSentDateList;

	// 【购物属性】交易来源【10】
	private List<OrderFrom> orderFromList;

	// 【购物属性】卖家备注【11】
	private List<SellerFlag> sellerFlagList;

	// 【购物属性】买家评价【12】
	private List<BuyerRemark> buyerRemarkList;

	// 【RFM属性】时段内首次购买商品【13】
	private List<BuyGoodsInFirstTimeOfPeriodTime> buyGoodsInFirstTimeOfPeriodTimeList;

	// 【RFM属性】时段内最后一次购买商品【14】
	private List<BuyGoodsInLastTimeOfPeriodTime> buyGoodsInLastTimeOfPeriodTimeList;

	// 【RFM属性】拍下时间【15】
	private List<BookDate> bookDateList;

	// 【RFM属性】付款时间【16】
	private List<PayDate> payDateList;

	// 【RFM属性】拍下时段【17】
	private List<BookTime> bookTimeList;

	// 【RFM属性】付款时段【18】
	private List<PayTime> payTimeList;

	// 【RFM属性】购买次数【19】
	private List<BuyGoodsTime> buyGoodsTimeList;

	// 【RFM属性】拍下次数【20】
	private List<BookGoodsTime> bookGoodsTimeList;

	// 【RFM属性】拍下金额【21】
	private List<BookPrice> bookPriceList;

	// 【RFM属性】 拍下未付款次数【22】
	private List<BookButNonPaymentTime> bookButNonPaymentTimeList;

	// 【RFM属性】付款金额【23】
	private List<PayPrice> payPriceList;

	// 【RFM属性】星期几付款【24】
	private List<PayInDayOfWeek> payInDayOfWeekList;

	// 【会员属性】手机号运营商【25】
	private List<MobileManufacturer> mobileManufacturerList;

	// 【会员属性】手机号归属地【26】
	private List<MobileLocation> mobileLocationList;

	// 【会员属性】手机号段【27】
	private List<MobileSectionNumber> mobileSectionNumberList;

	// 【会员属性】过滤黑名单【28】
	private boolean filterBlackList;

	// 【会员属性】优先发送支付宝手机号【29】
	private boolean sendPriority;

	// 【分组筛选】会员分组编号【30】
	private Long memberGroupId;

	// 【分组筛选】商品分组编号【31】
	private Long goodsGroupId;

	// 【发送短信】屏蔽N天内发送过的号码不再发送
	private String sentFilter;

	// 【发送短信】发送短信时配合limit使用的ID
	private Long limitId;

	public PremiumMemberFilterVO() {
		super();

	}

	public PremiumMemberFilterVO(Long uid, List<PayGoodsName> payGoodsNameList,
			List<PayGoodsInPeriodTime> payGoodsInPeriodTimeList, List<PayGoodsCount> payGoodsCountList,
			List<BookGoodsCount> bookGoodsCountList, List<GoodsPrice> goodsPriceList, List<OrderPrice> orderPriceList,
			List<ReceiveGoodsArea> receiveGoodsAreaList, List<OrderStatusInPeriodTime> orderStatusInPeriodTimeList,
			List<OrderSentDate> orderSentDateList, List<OrderFrom> orderFromList, List<SellerFlag> sellerFlagList,
			List<BuyerRemark> buyerRemarkList,
			List<BuyGoodsInFirstTimeOfPeriodTime> buyGoodsInFirstTimeOfPeriodTimeList,
			List<BuyGoodsInLastTimeOfPeriodTime> buyGoodsInLastTimeOfPeriodTimeList, List<BookDate> bookDateList,
			List<PayDate> payDateList, List<BookTime> bookTimeList, List<PayTime> payTimeList,
			List<BuyGoodsTime> buyGoodsTimeList, List<BookGoodsTime> bookGoodsTimeList, List<BookPrice> bookPriceList,
			List<BookButNonPaymentTime> bookButNonPaymentTimeList, List<PayPrice> payPriceList,
			List<PayInDayOfWeek> payInDayOfWeekList, List<MobileManufacturer> mobileManufacturerList,
			List<MobileLocation> mobileLocationList, List<MobileSectionNumber> mobileSectionNumberList,
			boolean filterBlackList, boolean sendPriority, Long memberGroupId, Long goodsGroupId, String sentFilter,
			Long limitId) {
		super();
		this.uid = uid;
		this.payGoodsNameList = payGoodsNameList;
		this.payGoodsInPeriodTimeList = payGoodsInPeriodTimeList;
		this.payGoodsCountList = payGoodsCountList;
		this.bookGoodsCountList = bookGoodsCountList;
		this.goodsPriceList = goodsPriceList;
		this.orderPriceList = orderPriceList;
		this.receiveGoodsAreaList = receiveGoodsAreaList;
		this.orderStatusInPeriodTimeList = orderStatusInPeriodTimeList;
		this.orderSentDateList = orderSentDateList;
		this.orderFromList = orderFromList;
		this.sellerFlagList = sellerFlagList;
		this.buyerRemarkList = buyerRemarkList;
		this.buyGoodsInFirstTimeOfPeriodTimeList = buyGoodsInFirstTimeOfPeriodTimeList;
		this.buyGoodsInLastTimeOfPeriodTimeList = buyGoodsInLastTimeOfPeriodTimeList;
		this.bookDateList = bookDateList;
		this.payDateList = payDateList;
		this.bookTimeList = bookTimeList;
		this.payTimeList = payTimeList;
		this.buyGoodsTimeList = buyGoodsTimeList;
		this.bookGoodsTimeList = bookGoodsTimeList;
		this.bookPriceList = bookPriceList;
		this.bookButNonPaymentTimeList = bookButNonPaymentTimeList;
		this.payPriceList = payPriceList;
		this.payInDayOfWeekList = payInDayOfWeekList;
		this.mobileManufacturerList = mobileManufacturerList;
		this.mobileLocationList = mobileLocationList;
		this.mobileSectionNumberList = mobileSectionNumberList;
		this.filterBlackList = filterBlackList;
		this.sendPriority = sendPriority;
		this.memberGroupId = memberGroupId;
		this.goodsGroupId = goodsGroupId;
		this.sentFilter = sentFilter;
		this.limitId = limitId;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public List<PayGoodsName> getPayGoodsNameList() {
		return payGoodsNameList;
	}

	public void setPayGoodsNameList(List<PayGoodsName> payGoodsNameList) {
		this.payGoodsNameList = payGoodsNameList;
	}

	public List<PayGoodsInPeriodTime> getPayGoodsInPeriodTimeList() {
		return payGoodsInPeriodTimeList;
	}

	public void setPayGoodsInPeriodTimeList(List<PayGoodsInPeriodTime> payGoodsInPeriodTimeList) {
		this.payGoodsInPeriodTimeList = payGoodsInPeriodTimeList;
	}

	public List<PayGoodsCount> getPayGoodsCountList() {
		return payGoodsCountList;
	}

	public void setPayGoodsCountList(List<PayGoodsCount> payGoodsCountList) {
		this.payGoodsCountList = payGoodsCountList;
	}

	public List<BookGoodsCount> getBookGoodsCountList() {
		return bookGoodsCountList;
	}

	public void setBookGoodsCountList(List<BookGoodsCount> bookGoodsCountList) {
		this.bookGoodsCountList = bookGoodsCountList;
	}

	public List<GoodsPrice> getGoodsPriceList() {
		return goodsPriceList;
	}

	public void setGoodsPriceList(List<GoodsPrice> goodsPriceList) {
		this.goodsPriceList = goodsPriceList;
	}

	public List<OrderPrice> getOrderPriceList() {
		return orderPriceList;
	}

	public void setOrderPriceList(List<OrderPrice> orderPriceList) {
		this.orderPriceList = orderPriceList;
	}

	public List<ReceiveGoodsArea> getReceiveGoodsAreaList() {
		return receiveGoodsAreaList;
	}

	public void setReceiveGoodsAreaList(List<ReceiveGoodsArea> receiveGoodsAreaList) {
		this.receiveGoodsAreaList = receiveGoodsAreaList;
	}

	public List<OrderStatusInPeriodTime> getOrderStatusInPeriodTimeList() {
		return orderStatusInPeriodTimeList;
	}

	public void setOrderStatusInPeriodTimeList(List<OrderStatusInPeriodTime> orderStatusInPeriodTimeList) {
		this.orderStatusInPeriodTimeList = orderStatusInPeriodTimeList;
	}

	public List<OrderSentDate> getOrderSentDateList() {
		return orderSentDateList;
	}

	public void setOrderSentDateList(List<OrderSentDate> orderSentDateList) {
		this.orderSentDateList = orderSentDateList;
	}

	public List<OrderFrom> getOrderFromList() {
		return orderFromList;
	}

	public void setOrderFromList(List<OrderFrom> orderFromList) {
		this.orderFromList = orderFromList;
	}

	public List<SellerFlag> getSellerFlagList() {
		return sellerFlagList;
	}

	public void setSellerFlagList(List<SellerFlag> sellerFlagList) {
		this.sellerFlagList = sellerFlagList;
	}

	public List<BuyerRemark> getBuyerRemarkList() {
		return buyerRemarkList;
	}

	public void setBuyerRemarkList(List<BuyerRemark> buyerRemarkList) {
		this.buyerRemarkList = buyerRemarkList;
	}

	public List<BuyGoodsInFirstTimeOfPeriodTime> getBuyGoodsInFirstTimeOfPeriodTimeList() {
		return buyGoodsInFirstTimeOfPeriodTimeList;
	}

	public void setBuyGoodsInFirstTimeOfPeriodTimeList(
			List<BuyGoodsInFirstTimeOfPeriodTime> buyGoodsInFirstTimeOfPeriodTimeList) {
		this.buyGoodsInFirstTimeOfPeriodTimeList = buyGoodsInFirstTimeOfPeriodTimeList;
	}

	public List<BuyGoodsInLastTimeOfPeriodTime> getBuyGoodsInLastTimeOfPeriodTimeList() {
		return buyGoodsInLastTimeOfPeriodTimeList;
	}

	public void setBuyGoodsInLastTimeOfPeriodTimeList(
			List<BuyGoodsInLastTimeOfPeriodTime> buyGoodsInLastTimeOfPeriodTimeList) {
		this.buyGoodsInLastTimeOfPeriodTimeList = buyGoodsInLastTimeOfPeriodTimeList;
	}

	public List<BookDate> getBookDateList() {
		return bookDateList;
	}

	public void setBookDateList(List<BookDate> bookDateList) {
		this.bookDateList = bookDateList;
	}

	public List<PayDate> getPayDateList() {
		return payDateList;
	}

	public void setPayDateList(List<PayDate> payDateList) {
		this.payDateList = payDateList;
	}

	public List<BookTime> getBookTimeList() {
		return bookTimeList;
	}

	public void setBookTimeList(List<BookTime> bookTimeList) {
		this.bookTimeList = bookTimeList;
	}

	public List<PayTime> getPayTimeList() {
		return payTimeList;
	}

	public void setPayTimeList(List<PayTime> payTimeList) {
		this.payTimeList = payTimeList;
	}

	public List<BuyGoodsTime> getBuyGoodsTimeList() {
		return buyGoodsTimeList;
	}

	public void setBuyGoodsTimeList(List<BuyGoodsTime> buyGoodsTimeList) {
		this.buyGoodsTimeList = buyGoodsTimeList;
	}

	public List<BookGoodsTime> getBookGoodsTimeList() {
		return bookGoodsTimeList;
	}

	public void setBookGoodsTimeList(List<BookGoodsTime> bookGoodsTimeList) {
		this.bookGoodsTimeList = bookGoodsTimeList;
	}

	public List<BookPrice> getBookPriceList() {
		return bookPriceList;
	}

	public void setBookPriceList(List<BookPrice> bookPriceList) {
		this.bookPriceList = bookPriceList;
	}

	public List<BookButNonPaymentTime> getBookButNonPaymentTimeList() {
		return bookButNonPaymentTimeList;
	}

	public void setBookButNonPaymentTimeList(List<BookButNonPaymentTime> bookButNonPaymentTimeList) {
		this.bookButNonPaymentTimeList = bookButNonPaymentTimeList;
	}

	public List<PayPrice> getPayPriceList() {
		return payPriceList;
	}

	public void setPayPriceList(List<PayPrice> payPriceList) {
		this.payPriceList = payPriceList;
	}

	public List<PayInDayOfWeek> getPayInDayOfWeekList() {
		return payInDayOfWeekList;
	}

	public void setPayInDayOfWeekList(List<PayInDayOfWeek> payInDayOfWeekList) {
		this.payInDayOfWeekList = payInDayOfWeekList;
	}

	public List<MobileManufacturer> getMobileManufacturerList() {
		return mobileManufacturerList;
	}

	public void setMobileManufacturerList(List<MobileManufacturer> mobileManufacturerList) {
		this.mobileManufacturerList = mobileManufacturerList;
	}

	public List<MobileLocation> getMobileLocationList() {
		return mobileLocationList;
	}

	public void setMobileLocationList(List<MobileLocation> mobileLocationList) {
		this.mobileLocationList = mobileLocationList;
	}

	public List<MobileSectionNumber> getMobileSectionNumberList() {
		return mobileSectionNumberList;
	}

	public void setMobileSectionNumberList(List<MobileSectionNumber> mobileSectionNumberList) {
		this.mobileSectionNumberList = mobileSectionNumberList;
	}

	public boolean isFilterBlackList() {
		return filterBlackList;
	}

	public void setFilterBlackList(boolean filterBlackList) {
		this.filterBlackList = filterBlackList;
	}

	public boolean isSendPriority() {
		return sendPriority;
	}

	public void setSendPriority(boolean sendPriority) {
		this.sendPriority = sendPriority;
	}

	public Long getMemberGroupId() {
		return memberGroupId;
	}

	public void setMemberGroupId(Long memberGroupId) {
		this.memberGroupId = memberGroupId;
	}

	public Long getGoodsGroupId() {
		return goodsGroupId;
	}

	public void setGoodsGroupId(Long goodsGroupId) {
		this.goodsGroupId = goodsGroupId;
	}

	public String getSentFilter() {
		return sentFilter;
	}

	public void setSentFilter(String sentFilter) {
		this.sentFilter = sentFilter;
	}

	public Long getLimitId() {
		return limitId;
	}

	public void setLimitId(Long limitId) {
		this.limitId = limitId;
	}

	@Override
	public String toString() {
		return "PremiumMemberFilterVO [uid=" + uid + ", payGoodsNameList=" + payGoodsNameList
				+ ", payGoodsInPeriodTimeList=" + payGoodsInPeriodTimeList + ", payGoodsCountList=" + payGoodsCountList
				+ ", bookGoodsCountList=" + bookGoodsCountList + ", goodsPriceList=" + goodsPriceList
				+ ", orderPriceList=" + orderPriceList + ", receiveGoodsAreaList=" + receiveGoodsAreaList
				+ ", orderStatusInPeriodTimeList=" + orderStatusInPeriodTimeList + ", orderSentDateList="
				+ orderSentDateList + ", orderFromList=" + orderFromList + ", sellerFlagList=" + sellerFlagList
				+ ", buyerRemarkList=" + buyerRemarkList + ", buyGoodsInFirstTimeOfPeriodTimeList="
				+ buyGoodsInFirstTimeOfPeriodTimeList + ", buyGoodsInLastTimeOfPeriodTimeList="
				+ buyGoodsInLastTimeOfPeriodTimeList + ", bookDateList=" + bookDateList + ", payDateList=" + payDateList
				+ ", bookTimeList=" + bookTimeList + ", payTimeList=" + payTimeList + ", buyGoodsTimeList="
				+ buyGoodsTimeList + ", bookGoodsTimeList=" + bookGoodsTimeList + ", bookPriceList=" + bookPriceList
				+ ", bookButNonPaymentTimeList=" + bookButNonPaymentTimeList + ", payPriceList=" + payPriceList
				+ ", payInDayOfWeekList=" + payInDayOfWeekList + ", mobileManufacturerList=" + mobileManufacturerList
				+ ", mobileLocationList=" + mobileLocationList + ", mobileSectionNumberList=" + mobileSectionNumberList
				+ ", filterBlackList=" + filterBlackList + ", sendPriority=" + sendPriority + ", memberGroupId="
				+ memberGroupId + ", goodsGroupId=" + goodsGroupId + ", sentFilter=" + sentFilter + ", limitId="
				+ limitId + "]";
	}

}
