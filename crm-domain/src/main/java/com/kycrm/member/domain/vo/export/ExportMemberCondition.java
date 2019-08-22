package com.kycrm.member.domain.vo.export;

public class ExportMemberCondition extends ExportVO {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2019年3月27日上午10:05:32
	 */
	private static final long serialVersionUID = 1L;

	// 卖家昵称
	private String userId;

	// 分组规则ID
	private Long ruleId;

	// 分组ID
	private Long groupId;

	// 分组名称
	private String groupName;

	// 会员等级
	private String memberGrade;

	// 交易时间与未交易时间标识
	private String tradeOrUntradeTime;

	// 交易时间或未交易时间数
	private String tradeTime;

	// 最小交易时间或未交易时间数
	private String minTradeTime;

	// 最大交易时间或未交易时间数
	private String maxTradeTime;

	// 订单来源
	private String orderFrom;

	// 交易成功次数
	private String tradeNum;

	// 最小交易成功次数
	private String minTradeNum;

	// 最大交易成功次数
	private String maxTradeNum;

	// 订单状态
	private String orderStatus;

	// 关闭交易次数
	private String closeTradeTime;

	// 最小关闭交易次数
	private String minCloseTradeTime;

	// 最大关闭交易次数
	private String maxCloseTradeTime;

	// 累计消费金额
	private String accumulatedAmount;

	// 最小累计消费金额
	private String minAccumulatedAmount;

	// 最大累计消费金额
	private String maxAccumulatedAmount;

	// 指定分组商品【发送】与【不发送】标识
	private String sendOrNotSendForGoods;

	// 选择指定商品或者选择关键字商品标识
	private String specifyGoodsOrKeyCodeGoods;

	// 指定分组商品
	private String numIid;

	// 商品关键字
	private String goodsKeyCode;

	// 交易宝贝件数
	private String itemNum;

	// 最小交易宝贝件数
	private String minItemNum;

	// 最大交易宝贝件数
	private String maxItemNum;

	// 地区筛选【发送】与【不发送】标识
	private String sendOrNotSendForArea;

	// 地区筛选【省份】
	private String province;

	// 地区筛选【城市】
	private String city;

	// 平均客单价
	private String averagePrice;

	// 最小平均客单价
	private String minAveragePrice;

	// 最大平均客单价
	private String maxAveragePrice;

	// 拍下订单时间起始段
	private String orderTimeSectionStart;

	// 拍下订单时间结束段
	private String orderTimeSectionEnd;

	// 参与短信营销活动次数
	private String marketingSmsNumber;

	// 最小参与短信营销活动次数
	private String minMarketingSmsNumber;

	// 最大参与短信营销活动次数
	private String maxMarketingSmsNumber;

	// 卖家标记
	private String sellerFlag;

	// 已发送过滤(根据last_marketing_time推算)
	private String sentFilter;

	// 黑名单
	private String memberStatus;

	public ExportMemberCondition() {
		super();
	}

	public ExportMemberCondition(Long uid) {
		super(uid);
	}

	public ExportMemberCondition(String userId, Long ruleId, Long groupId, String groupName, String memberGrade,
			String tradeOrUntradeTime, String tradeTime, String minTradeTime, String maxTradeTime, String orderFrom,
			String tradeNum, String minTradeNum, String maxTradeNum, String orderStatus, String closeTradeTime,
			String minCloseTradeTime, String maxCloseTradeTime, String accumulatedAmount, String minAccumulatedAmount,
			String maxAccumulatedAmount, String sendOrNotSendForGoods, String specifyGoodsOrKeyCodeGoods, String numIid,
			String goodsKeyCode, String itemNum, String minItemNum, String maxItemNum, String sendOrNotSendForArea,
			String province, String city, String averagePrice, String minAveragePrice, String maxAveragePrice,
			String orderTimeSectionStart, String orderTimeSectionEnd, String marketingSmsNumber,
			String minMarketingSmsNumber, String maxMarketingSmsNumber, String sellerFlag, String sentFilter,
			String memberStatus) {
		super();
		this.userId = userId;
		this.ruleId = ruleId;
		this.groupId = groupId;
		this.groupName = groupName;
		this.memberGrade = memberGrade;
		this.tradeOrUntradeTime = tradeOrUntradeTime;
		this.tradeTime = tradeTime;
		this.minTradeTime = minTradeTime;
		this.maxTradeTime = maxTradeTime;
		this.orderFrom = orderFrom;
		this.tradeNum = tradeNum;
		this.minTradeNum = minTradeNum;
		this.maxTradeNum = maxTradeNum;
		this.orderStatus = orderStatus;
		this.closeTradeTime = closeTradeTime;
		this.minCloseTradeTime = minCloseTradeTime;
		this.maxCloseTradeTime = maxCloseTradeTime;
		this.accumulatedAmount = accumulatedAmount;
		this.minAccumulatedAmount = minAccumulatedAmount;
		this.maxAccumulatedAmount = maxAccumulatedAmount;
		this.sendOrNotSendForGoods = sendOrNotSendForGoods;
		this.specifyGoodsOrKeyCodeGoods = specifyGoodsOrKeyCodeGoods;
		this.numIid = numIid;
		this.goodsKeyCode = goodsKeyCode;
		this.itemNum = itemNum;
		this.minItemNum = minItemNum;
		this.maxItemNum = maxItemNum;
		this.sendOrNotSendForArea = sendOrNotSendForArea;
		this.province = province;
		this.city = city;
		this.averagePrice = averagePrice;
		this.minAveragePrice = minAveragePrice;
		this.maxAveragePrice = maxAveragePrice;
		this.orderTimeSectionStart = orderTimeSectionStart;
		this.orderTimeSectionEnd = orderTimeSectionEnd;
		this.marketingSmsNumber = marketingSmsNumber;
		this.minMarketingSmsNumber = minMarketingSmsNumber;
		this.maxMarketingSmsNumber = maxMarketingSmsNumber;
		this.sellerFlag = sellerFlag;
		this.sentFilter = sentFilter;
		this.memberStatus = memberStatus;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getMemberGrade() {
		return memberGrade;
	}

	public void setMemberGrade(String memberGrade) {
		this.memberGrade = memberGrade;
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

	public String getOrderFrom() {
		return orderFrom;
	}

	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}

	public String getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(String tradeNum) {
		this.tradeNum = tradeNum;
	}

	public String getMinTradeNum() {
		return minTradeNum;
	}

	public void setMinTradeNum(String minTradeNum) {
		this.minTradeNum = minTradeNum;
	}

	public String getMaxTradeNum() {
		return maxTradeNum;
	}

	public void setMaxTradeNum(String maxTradeNum) {
		this.maxTradeNum = maxTradeNum;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getCloseTradeTime() {
		return closeTradeTime;
	}

	public void setCloseTradeTime(String closeTradeTime) {
		this.closeTradeTime = closeTradeTime;
	}

	public String getMinCloseTradeTime() {
		return minCloseTradeTime;
	}

	public void setMinCloseTradeTime(String minCloseTradeTime) {
		this.minCloseTradeTime = minCloseTradeTime;
	}

	public String getMaxCloseTradeTime() {
		return maxCloseTradeTime;
	}

	public void setMaxCloseTradeTime(String maxCloseTradeTime) {
		this.maxCloseTradeTime = maxCloseTradeTime;
	}

	public String getAccumulatedAmount() {
		return accumulatedAmount;
	}

	public void setAccumulatedAmount(String accumulatedAmount) {
		this.accumulatedAmount = accumulatedAmount;
	}

	public String getMinAccumulatedAmount() {
		return minAccumulatedAmount;
	}

	public void setMinAccumulatedAmount(String minAccumulatedAmount) {
		this.minAccumulatedAmount = minAccumulatedAmount;
	}

	public String getMaxAccumulatedAmount() {
		return maxAccumulatedAmount;
	}

	public void setMaxAccumulatedAmount(String maxAccumulatedAmount) {
		this.maxAccumulatedAmount = maxAccumulatedAmount;
	}

	public String getSendOrNotSendForGoods() {
		return sendOrNotSendForGoods;
	}

	public void setSendOrNotSendForGoods(String sendOrNotSendForGoods) {
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

	public String getItemNum() {
		return itemNum;
	}

	public void setItemNum(String itemNum) {
		this.itemNum = itemNum;
	}

	public String getMinItemNum() {
		return minItemNum;
	}

	public void setMinItemNum(String minItemNum) {
		this.minItemNum = minItemNum;
	}

	public String getMaxItemNum() {
		return maxItemNum;
	}

	public void setMaxItemNum(String maxItemNum) {
		this.maxItemNum = maxItemNum;
	}

	public String getSendOrNotSendForArea() {
		return sendOrNotSendForArea;
	}

	public void setSendOrNotSendForArea(String sendOrNotSendForArea) {
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

	public String getAveragePrice() {
		return averagePrice;
	}

	public void setAveragePrice(String averagePrice) {
		this.averagePrice = averagePrice;
	}

	public String getMinAveragePrice() {
		return minAveragePrice;
	}

	public void setMinAveragePrice(String minAveragePrice) {
		this.minAveragePrice = minAveragePrice;
	}

	public String getMaxAveragePrice() {
		return maxAveragePrice;
	}

	public void setMaxAveragePrice(String maxAveragePrice) {
		this.maxAveragePrice = maxAveragePrice;
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

	public String getMarketingSmsNumber() {
		return marketingSmsNumber;
	}

	public void setMarketingSmsNumber(String marketingSmsNumber) {
		this.marketingSmsNumber = marketingSmsNumber;
	}

	public String getMinMarketingSmsNumber() {
		return minMarketingSmsNumber;
	}

	public void setMinMarketingSmsNumber(String minMarketingSmsNumber) {
		this.minMarketingSmsNumber = minMarketingSmsNumber;
	}

	public String getMaxMarketingSmsNumber() {
		return maxMarketingSmsNumber;
	}

	public void setMaxMarketingSmsNumber(String maxMarketingSmsNumber) {
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

	@Override
	public String toString() {
		return "ExportMemberCondition [userId=" + userId + ", ruleId=" + ruleId + ", groupId=" + groupId
				+ ", groupName=" + groupName + ", memberGrade=" + memberGrade + ", tradeOrUntradeTime="
				+ tradeOrUntradeTime + ", tradeTime=" + tradeTime + ", minTradeTime=" + minTradeTime + ", maxTradeTime="
				+ maxTradeTime + ", orderFrom=" + orderFrom + ", tradeNum=" + tradeNum + ", minTradeNum=" + minTradeNum
				+ ", maxTradeNum=" + maxTradeNum + ", orderStatus=" + orderStatus + ", closeTradeTime=" + closeTradeTime
				+ ", minCloseTradeTime=" + minCloseTradeTime + ", maxCloseTradeTime=" + maxCloseTradeTime
				+ ", accumulatedAmount=" + accumulatedAmount + ", minAccumulatedAmount=" + minAccumulatedAmount
				+ ", maxAccumulatedAmount=" + maxAccumulatedAmount + ", sendOrNotSendForGoods=" + sendOrNotSendForGoods
				+ ", specifyGoodsOrKeyCodeGoods=" + specifyGoodsOrKeyCodeGoods + ", numIid=" + numIid
				+ ", goodsKeyCode=" + goodsKeyCode + ", itemNum=" + itemNum + ", minItemNum=" + minItemNum
				+ ", maxItemNum=" + maxItemNum + ", sendOrNotSendForArea=" + sendOrNotSendForArea + ", province="
				+ province + ", city=" + city + ", averagePrice=" + averagePrice + ", minAveragePrice="
				+ minAveragePrice + ", maxAveragePrice=" + maxAveragePrice + ", orderTimeSectionStart="
				+ orderTimeSectionStart + ", orderTimeSectionEnd=" + orderTimeSectionEnd + ", marketingSmsNumber="
				+ marketingSmsNumber + ", minMarketingSmsNumber=" + minMarketingSmsNumber + ", maxMarketingSmsNumber="
				+ maxMarketingSmsNumber + ", sellerFlag=" + sellerFlag + ", sentFilter=" + sentFilter
				+ ", memberStatus=" + memberStatus + "]";
	}

}
