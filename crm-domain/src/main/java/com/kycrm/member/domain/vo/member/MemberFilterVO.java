package com.kycrm.member.domain.vo.member;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 营销中心-会员筛选条件
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月17日上午11:38:36
 * @Tags
 */
public class MemberFilterVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月17日上午11:38:30
	 */
	private static final long serialVersionUID = 1L;

	// 分库分表实体对应用户表的主键id
	private Long uid;

	// 会员分组ID
	private Long groupId;

	// 1: 一键营销 2: 立即营销
	private String marketingType;

	// 【交易时间】或【未交易时间】标识
	private String tradeOrUntradeTime;

	// 最后交易时间
	private String tradeTime;

	// 最小最后交易时间
	private String minTradeTime;

	// 最大最后交易时间
	private String maxTradeTime;

	// 最近交易【一键营销, 立即营销使用】
	private String recentTrade;

	// 订单来源
	private String orderFrom;

	// 交易成功次数
	private Long tradeNum;

	// 最小交易成功次数
	private Long minTradeNum;

	// 最大交易成功次数
	private Long maxTradeNum;

	// 订单状态
	private String orderStatus;

	// 累计消费金额
	private Double accumulatedAmount;

	// 最小累计消费金额
	private Double minAccumulatedAmount;

	// 最大累计消费金额
	private Double maxAccumulatedAmount;

	// 平均客单价
	private BigDecimal averagePrice;

	// 最小平均客单价
	private BigDecimal minAveragePrice;

	// 最大平均客单价
	private BigDecimal maxAveragePrice;

	// 关闭交易次数
	private Long closeTradeTime;

	// 最小关闭交易次数
	private Long minCloseTradeTime;

	// 最大关闭交易次数
	private Long maxCloseTradeTime;

	// 交易宝贝件数
	private Long itemNum;

	// 最小交易宝贝件数
	private Long minItemNum;

	// 最大交易宝贝件数
	private Long maxItemNum;

	// 指定商品【发送】与【不发送】标识
	private Integer sendOrNotSendForGoods;

	// 指定商品或者商品关键字标识
	private String specifyGoodsOrKeyCodeGoods;

	// 指定商品编号
	private String numIid;

	// 商品关键字
	private String goodsKeyCode;

	// 拍下订单时间起始段
	private String orderTimeSectionStart;

	// 拍下订单时间结束段
	private String orderTimeSectionEnd;

	// 地区筛选 【发送】与【不发送】标识
	private Integer sendOrNotSendForArea;

	// 地区筛选 - 省份
	private String province;

	// 地区筛选 - 城市
	private String city;

	// 参与短信营销活动次数
	private Integer marketingSmsNumber;

	// 最小参与短信营销活动次数
	private Integer minMarketingSmsNumber;

	// 最大参与短信营销活动次数
	private Integer maxMarketingSmsNumber;

	// 卖家标记
	private String sellerFlag;

	// 已发送过滤(根据last_marketing_time推算)
	private String sentFilter;

	// 黑名单
	private String memberStatus;

	// 中差评
	private String neutralBadRate;

	// 发送短信时配合limit使用的ID
	private Long limitId;

	public MemberFilterVO() {
		super();

	}

	public MemberFilterVO(Long uid, Long groupId, String marketingType, String tradeOrUntradeTime, String tradeTime,
			String minTradeTime, String maxTradeTime, String recentTrade, String orderFrom, Long tradeNum,
			Long minTradeNum, Long maxTradeNum, String orderStatus, Double accumulatedAmount,
			Double minAccumulatedAmount, Double maxAccumulatedAmount, BigDecimal averagePrice,
			BigDecimal minAveragePrice, BigDecimal maxAveragePrice, Long closeTradeTime, Long minCloseTradeTime,
			Long maxCloseTradeTime, Long itemNum, Long minItemNum, Long maxItemNum, Integer sendOrNotSendForGoods,
			String specifyGoodsOrKeyCodeGoods, String numIid, String goodsKeyCode, String orderTimeSectionStart,
			String orderTimeSectionEnd, Integer sendOrNotSendForArea, String province, String city,
			Integer marketingSmsNumber, Integer minMarketingSmsNumber, Integer maxMarketingSmsNumber, String sellerFlag,
			String sentFilter, String memberStatus, String neutralBadRate, Long limitId) {
		super();
		this.uid = uid;
		this.groupId = groupId;
		this.marketingType = marketingType;
		this.tradeOrUntradeTime = tradeOrUntradeTime;
		this.tradeTime = tradeTime;
		this.minTradeTime = minTradeTime;
		this.maxTradeTime = maxTradeTime;
		this.recentTrade = recentTrade;
		this.orderFrom = orderFrom;
		this.tradeNum = tradeNum;
		this.minTradeNum = minTradeNum;
		this.maxTradeNum = maxTradeNum;
		this.orderStatus = orderStatus;
		this.accumulatedAmount = accumulatedAmount;
		this.minAccumulatedAmount = minAccumulatedAmount;
		this.maxAccumulatedAmount = maxAccumulatedAmount;
		this.averagePrice = averagePrice;
		this.minAveragePrice = minAveragePrice;
		this.maxAveragePrice = maxAveragePrice;
		this.closeTradeTime = closeTradeTime;
		this.minCloseTradeTime = minCloseTradeTime;
		this.maxCloseTradeTime = maxCloseTradeTime;
		this.itemNum = itemNum;
		this.minItemNum = minItemNum;
		this.maxItemNum = maxItemNum;
		this.sendOrNotSendForGoods = sendOrNotSendForGoods;
		this.specifyGoodsOrKeyCodeGoods = specifyGoodsOrKeyCodeGoods;
		this.numIid = numIid;
		this.goodsKeyCode = goodsKeyCode;
		this.orderTimeSectionStart = orderTimeSectionStart;
		this.orderTimeSectionEnd = orderTimeSectionEnd;
		this.sendOrNotSendForArea = sendOrNotSendForArea;
		this.province = province;
		this.city = city;
		this.marketingSmsNumber = marketingSmsNumber;
		this.minMarketingSmsNumber = minMarketingSmsNumber;
		this.maxMarketingSmsNumber = maxMarketingSmsNumber;
		this.sellerFlag = sellerFlag;
		this.sentFilter = sentFilter;
		this.memberStatus = memberStatus;
		this.neutralBadRate = neutralBadRate;
		this.limitId = limitId;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getMarketingType() {
		return marketingType;
	}

	public void setMarketingType(String marketingType) {
		this.marketingType = marketingType;
	}

	public String getTradeOrUntradeTime() {
		return tradeOrUntradeTime;
	}

	public void setTradeOrUntradeTime(String tradeOrUntradeTime) {
		this.tradeOrUntradeTime = tradeOrUntradeTime;
	}

	public String getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(String tradeTime) {
		this.tradeTime = tradeTime;
	}

	public String getMinTradeTime() {
		return minTradeTime;
	}

	public void setMinTradeTime(String minTradeTime) {
		this.minTradeTime = minTradeTime;
	}

	public String getMaxTradeTime() {
		return maxTradeTime;
	}

	public void setMaxTradeTime(String maxTradeTime) {
		this.maxTradeTime = maxTradeTime;
	}

	public String getRecentTrade() {
		return recentTrade;
	}

	public void setRecentTrade(String recentTrade) {
		this.recentTrade = recentTrade;
	}

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public Long getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(Long tradeNum) {
		this.tradeNum = tradeNum;
	}

	public Long getMinTradeNum() {
		return minTradeNum;
	}

	public void setMinTradeNum(Long minTradeNum) {
		this.minTradeNum = minTradeNum;
	}

	public Long getMaxTradeNum() {
		return maxTradeNum;
	}

	public void setMaxTradeNum(Long maxTradeNum) {
		this.maxTradeNum = maxTradeNum;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Double getAccumulatedAmount() {
		return accumulatedAmount;
	}

	public void setAccumulatedAmount(Double accumulatedAmount) {
		this.accumulatedAmount = accumulatedAmount;
	}

	public Double getMinAccumulatedAmount() {
		return minAccumulatedAmount;
	}

	public void setMinAccumulatedAmount(Double minAccumulatedAmount) {
		this.minAccumulatedAmount = minAccumulatedAmount;
	}

	public Double getMaxAccumulatedAmount() {
		return maxAccumulatedAmount;
	}

	public void setMaxAccumulatedAmount(Double maxAccumulatedAmount) {
		this.maxAccumulatedAmount = maxAccumulatedAmount;
	}

	public BigDecimal getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(BigDecimal averagePrice) {
		this.averagePrice = averagePrice;
	}

	public BigDecimal getMinAveragePrice() {
		return minAveragePrice;
	}

	public void setMinAveragePrice(BigDecimal minAveragePrice) {
		this.minAveragePrice = minAveragePrice;
	}

	public BigDecimal getMaxAveragePrice() {
		return maxAveragePrice;
	}

	public void setMaxAveragePrice(BigDecimal maxAveragePrice) {
		this.maxAveragePrice = maxAveragePrice;
	}

	public Long getCloseTradeTime() {
		return closeTradeTime;
	}

	public void setCloseTradeTime(Long closeTradeTime) {
		this.closeTradeTime = closeTradeTime;
	}

	public Long getMinCloseTradeTime() {
		return minCloseTradeTime;
	}

	public void setMinCloseTradeTime(Long minCloseTradeTime) {
		this.minCloseTradeTime = minCloseTradeTime;
	}

	public Long getMaxCloseTradeTime() {
		return maxCloseTradeTime;
	}

	public void setMaxCloseTradeTime(Long maxCloseTradeTime) {
		this.maxCloseTradeTime = maxCloseTradeTime;
	}

	public Long getItemNum() {
		return itemNum;
	}

	public void setItemNum(Long itemNum) {
		this.itemNum = itemNum;
	}

	public Long getMinItemNum() {
		return minItemNum;
	}

	public void setMinItemNum(Long minItemNum) {
		this.minItemNum = minItemNum;
	}

	public Long getMaxItemNum() {
		return maxItemNum;
	}

	public void setMaxItemNum(Long maxItemNum) {
		this.maxItemNum = maxItemNum;
	}

	public Integer getSendOrNotSendForGoods() {
		return sendOrNotSendForGoods;
	}

	public void setSendOrNotSendForGoods(Integer sendOrNotSendForGoods) {
		this.sendOrNotSendForGoods = sendOrNotSendForGoods;
	}

	public String getSpecifyGoodsOrKeyCodeGoods() {
		return specifyGoodsOrKeyCodeGoods;
	}

	public void setSpecifyGoodsOrKeyCodeGoods(String specifyGoodsOrKeyCodeGoods) {
		this.specifyGoodsOrKeyCodeGoods = specifyGoodsOrKeyCodeGoods;
	}

	public String getNumIid() {
		return numIid;
	}

	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}

	public String getGoodsKeyCode() {
		return goodsKeyCode;
	}

	public void setGoodsKeyCode(String goodsKeyCode) {
		this.goodsKeyCode = goodsKeyCode;
	}

	public String getOrderTimeSectionStart() {
		return orderTimeSectionStart;
	}

	public void setOrderTimeSectionStart(String orderTimeSectionStart) {
		this.orderTimeSectionStart = orderTimeSectionStart;
	}

	public String getOrderTimeSectionEnd() {
		return orderTimeSectionEnd;
	}

	public void setOrderTimeSectionEnd(String orderTimeSectionEnd) {
		this.orderTimeSectionEnd = orderTimeSectionEnd;
	}

	public Integer getSendOrNotSendForArea() {
		return sendOrNotSendForArea;
	}

	public void setSendOrNotSendForArea(Integer sendOrNotSendForArea) {
		this.sendOrNotSendForArea = sendOrNotSendForArea;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public Integer getMarketingSmsNumber() {
		return marketingSmsNumber;
	}

	public void setMarketingSmsNumber(Integer marketingSmsNumber) {
		this.marketingSmsNumber = marketingSmsNumber;
	}

	public Integer getMinMarketingSmsNumber() {
		return minMarketingSmsNumber;
	}

	public void setMinMarketingSmsNumber(Integer minMarketingSmsNumber) {
		this.minMarketingSmsNumber = minMarketingSmsNumber;
	}

	public Integer getMaxMarketingSmsNumber() {
		return maxMarketingSmsNumber;
	}

	public void setMaxMarketingSmsNumber(Integer maxMarketingSmsNumber) {
		this.maxMarketingSmsNumber = maxMarketingSmsNumber;
	}

	public String getSellerFlag() {
		return sellerFlag;
	}

	public void setSellerFlag(String sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	public String getSentFilter() {
		return sentFilter;
	}

	public void setSentFilter(String sentFilter) {
		this.sentFilter = sentFilter;
	}

	public String getMemberStatus() {
		return memberStatus;
	}

	public void setMemberStatus(String memberStatus) {
		this.memberStatus = memberStatus;
	}

	public String getNeutralBadRate() {
		return neutralBadRate;
	}

	public void setNeutralBadRate(String neutralBadRate) {
		this.neutralBadRate = neutralBadRate;
	}

	public Long getLimitId() {
		return limitId;
	}

	public void setLimitId(Long limitId) {
		this.limitId = limitId;
	}

	@Override
	public String toString() {
		return "MemberFilterVO [uid=" + uid + ", groupId=" + groupId + ", marketingType=" + marketingType
				+ ", tradeOrUntradeTime=" + tradeOrUntradeTime + ", tradeTime=" + tradeTime + ", minTradeTime="
				+ minTradeTime + ", maxTradeTime=" + maxTradeTime + ", recentTrade=" + recentTrade + ", orderFrom="
				+ orderFrom + ", tradeNum=" + tradeNum + ", minTradeNum=" + minTradeNum + ", maxTradeNum=" + maxTradeNum
				+ ", orderStatus=" + orderStatus + ", accumulatedAmount=" + accumulatedAmount
				+ ", minAccumulatedAmount=" + minAccumulatedAmount + ", maxAccumulatedAmount=" + maxAccumulatedAmount
				+ ", averagePrice=" + averagePrice + ", minAveragePrice=" + minAveragePrice + ", maxAveragePrice="
				+ maxAveragePrice + ", closeTradeTime=" + closeTradeTime + ", minCloseTradeTime=" + minCloseTradeTime
				+ ", maxCloseTradeTime=" + maxCloseTradeTime + ", itemNum=" + itemNum + ", minItemNum=" + minItemNum
				+ ", maxItemNum=" + maxItemNum + ", sendOrNotSendForGoods=" + sendOrNotSendForGoods
				+ ", specifyGoodsOrKeyCodeGoods=" + specifyGoodsOrKeyCodeGoods + ", numIid=" + numIid
				+ ", goodsKeyCode=" + goodsKeyCode + ", orderTimeSectionStart=" + orderTimeSectionStart
				+ ", orderTimeSectionEnd=" + orderTimeSectionEnd + ", sendOrNotSendForArea=" + sendOrNotSendForArea
				+ ", province=" + province + ", city=" + city + ", marketingSmsNumber=" + marketingSmsNumber
				+ ", minMarketingSmsNumber=" + minMarketingSmsNumber + ", maxMarketingSmsNumber="
				+ maxMarketingSmsNumber + ", sellerFlag=" + sellerFlag + ", sentFilter=" + sentFilter
				+ ", memberStatus=" + memberStatus + ", neutralBadRate=" + neutralBadRate + ", limitId=" + limitId
				+ "]";
	}

}
