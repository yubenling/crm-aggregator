package com.kycrm.member.domain.entity.usermanagement;

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * 卖家分组规则
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月10日下午3:50:06
 * @Tags
 */
public class SellerGroupRule extends BaseEntity {

	private static final long serialVersionUID = -8948892853672566543L;

	// 分组ID
	private Long groupId;

	// 买家昵称
	private String buyerNick;

	// 买家姓名
	private String buyerName;

	// 会员等级
	private String memberGrade;

	// 交易时间与未交易时间标识
	private String tradeOrUntradeTime;

	// 交易时间与未交易时间数
	private String tradeTime;

	// 最小交易时间与未交易时间
	private String minTradeTime;

	// 最大交易时间与未交易时间
	private String maxTradeTime;

	// 订单来源
	private String orderFrom;

	// 交易成功次数
	private Long tradeNum;

	// 最小交易次数
	private Long minTradeNum;

	// 最大交易次数
	private Long maxTradeNum;

	// 订单状态
	private String orderStatus;

	// 累计消费金额
	private String accumulatedAmount;

	// 最小累计金额
	private String minAccumulatedAmount;

	// 最大累计金额
	private String maxAccumulatedAmount;

	// 平均客单价
	private String averagePrice;

	// 最小平均客单价
	private String minAveragePrice;

	// 最大平均客单价
	private String maxAveragePrice;

	// 指定分组商品【发送】与【不发送】标识
	private Integer sendOrNotSendForGoods;

	// 选择指定商品或者选择关键字商品标识
	private String specifyGoodsOrKeyCodeGoods;

	// 指定商品编号
	private String numIid;

	// 商品关键字
	private String goodsKeyCode;

	// 关闭交易次数
	private Long closeTradeTime;

	// 最小关闭交易次数
	private Long minCloseTradeTime;

	// 最大关闭交易次数
	private Long maxCloseTradeTime;

	// 地区筛选【发送】与【不发送】标识
	private Integer sendOrNotSendForArea;

	// 省份
	private String province;

	// 城市
	private String city;

	// 交易宝贝件数
	private Long itemNum;

	// 最小交易宝贝件数
	private Long minItemNum;

	// 最大交易宝贝件数
	private Long maxItemNum;

	// 拍下订单时间起始段
	private String orderTimeSectionStart;

	// 拍下订单时间结束段
	private String orderTimeSectionEnd;

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

	// 卖家昵称
	private String userId;

	// 最近交易状态
	private String tradeTimeStatus;

	// 最近交易时间单位
	private String tradeType;

	// 性别
	private String sex;

	// 年龄段
	private String ageRange;

	// 年龄
	private Integer age;

	// 职业
	private String occupation;

	// 生日
	private String birthday;

	// QQ号
	private String qq;

	// 微信号
	private String wechat;

	// 手机号
	private String phone;

	// 手机号段
	private String phoneRange;

	// 邮箱
	private String email;

	// 邮箱类型
	private String emailType;

	// 注册日期
	private Date registerDate;

	// 评价得分
	private String score;

	// 备注
	private String remarks;

	// 乐观锁版本号
	private Integer optlock;

	public SellerGroupRule() {
		super();

	}

	public SellerGroupRule(Long groupId, String buyerNick, String buyerName, String memberGrade,
			String tradeOrUntradeTime, String tradeTime, String minTradeTime, String maxTradeTime, String orderFrom,
			Long tradeNum, Long minTradeNum, Long maxTradeNum, String orderStatus, String accumulatedAmount,
			String minAccumulatedAmount, String maxAccumulatedAmount, String averagePrice, String minAveragePrice,
			String maxAveragePrice, Integer sendOrNotSendForGoods, String specifyGoodsOrKeyCodeGoods, String numIid,
			String goodsKeyCode, Long closeTradeTime, Long minCloseTradeTime, Long maxCloseTradeTime,
			Integer sendOrNotSendForArea, String province, String city, Long itemNum, Long minItemNum, Long maxItemNum,
			String orderTimeSectionStart, String orderTimeSectionEnd, Integer marketingSmsNumber,
			Integer minMarketingSmsNumber, Integer maxMarketingSmsNumber, String sellerFlag, String sentFilter,
			String memberStatus, String userId, String tradeTimeStatus, String tradeType, String sex, String ageRange,
			Integer age, String occupation, String birthday, String qq, String wechat, String phone, String phoneRange,
			String email, String emailType, Date registerDate, String score, String remarks, Integer optlock) {
		super();
		this.groupId = groupId;
		this.buyerNick = buyerNick;
		this.buyerName = buyerName;
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
		this.accumulatedAmount = accumulatedAmount;
		this.minAccumulatedAmount = minAccumulatedAmount;
		this.maxAccumulatedAmount = maxAccumulatedAmount;
		this.averagePrice = averagePrice;
		this.minAveragePrice = minAveragePrice;
		this.maxAveragePrice = maxAveragePrice;
		this.sendOrNotSendForGoods = sendOrNotSendForGoods;
		this.specifyGoodsOrKeyCodeGoods = specifyGoodsOrKeyCodeGoods;
		this.numIid = numIid;
		this.goodsKeyCode = goodsKeyCode;
		this.closeTradeTime = closeTradeTime;
		this.minCloseTradeTime = minCloseTradeTime;
		this.maxCloseTradeTime = maxCloseTradeTime;
		this.sendOrNotSendForArea = sendOrNotSendForArea;
		this.province = province;
		this.city = city;
		this.itemNum = itemNum;
		this.minItemNum = minItemNum;
		this.maxItemNum = maxItemNum;
		this.orderTimeSectionStart = orderTimeSectionStart;
		this.orderTimeSectionEnd = orderTimeSectionEnd;
		this.marketingSmsNumber = marketingSmsNumber;
		this.minMarketingSmsNumber = minMarketingSmsNumber;
		this.maxMarketingSmsNumber = maxMarketingSmsNumber;
		this.sellerFlag = sellerFlag;
		this.sentFilter = sentFilter;
		this.memberStatus = memberStatus;
		this.userId = userId;
		this.tradeTimeStatus = tradeTimeStatus;
		this.tradeType = tradeType;
		this.sex = sex;
		this.ageRange = ageRange;
		this.age = age;
		this.occupation = occupation;
		this.birthday = birthday;
		this.qq = qq;
		this.wechat = wechat;
		this.phone = phone;
		this.phoneRange = phoneRange;
		this.email = email;
		this.emailType = emailType;
		this.registerDate = registerDate;
		this.score = score;
		this.remarks = remarks;
		this.optlock = optlock;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getBuyerName() {
		return buyerName;
	}

	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getTradeTimeStatus() {
		return tradeTimeStatus;
	}

	public void setTradeTimeStatus(String tradeTimeStatus) {
		this.tradeTimeStatus = tradeTimeStatus;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAgeRange() {
		return ageRange;
	}

	public void setAgeRange(String ageRange) {
		this.ageRange = ageRange;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getBirthday() {
		return birthday;
	}

	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public String getWechat() {
		return wechat;
	}

	public void setWechat(String wechat) {
		this.wechat = wechat;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getPhoneRange() {
		return phoneRange;
	}

	public void setPhoneRange(String phoneRange) {
		this.phoneRange = phoneRange;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmailType() {
		return emailType;
	}

	public void setEmailType(String emailType) {
		this.emailType = emailType;
	}

	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Integer getOptlock() {
		return optlock;
	}

	public void setOptlock(Integer optlock) {
		this.optlock = optlock;
	}

	@Override
	public String toString() {
		return "SellerGroupRule [groupId=" + groupId + ", buyerNick=" + buyerNick + ", buyerName=" + buyerName
				+ ", memberGrade=" + memberGrade + ", tradeOrUntradeTime=" + tradeOrUntradeTime + ", tradeTime="
				+ tradeTime + ", minTradeTime=" + minTradeTime + ", maxTradeTime=" + maxTradeTime + ", orderFrom="
				+ orderFrom + ", tradeNum=" + tradeNum + ", minTradeNum=" + minTradeNum + ", maxTradeNum=" + maxTradeNum
				+ ", orderStatus=" + orderStatus + ", accumulatedAmount=" + accumulatedAmount
				+ ", minAccumulatedAmount=" + minAccumulatedAmount + ", maxAccumulatedAmount=" + maxAccumulatedAmount
				+ ", averagePrice=" + averagePrice + ", minAveragePrice=" + minAveragePrice + ", maxAveragePrice="
				+ maxAveragePrice + ", sendOrNotSendForGoods=" + sendOrNotSendForGoods + ", specifyGoodsOrKeyCodeGoods="
				+ specifyGoodsOrKeyCodeGoods + ", numIid=" + numIid + ", goodsKeyCode=" + goodsKeyCode
				+ ", closeTradeTime=" + closeTradeTime + ", minCloseTradeTime=" + minCloseTradeTime
				+ ", maxCloseTradeTime=" + maxCloseTradeTime + ", sendOrNotSendForArea=" + sendOrNotSendForArea
				+ ", province=" + province + ", city=" + city + ", itemNum=" + itemNum + ", minItemNum=" + minItemNum
				+ ", maxItemNum=" + maxItemNum + ", orderTimeSectionStart=" + orderTimeSectionStart
				+ ", orderTimeSectionEnd=" + orderTimeSectionEnd + ", marketingSmsNumber=" + marketingSmsNumber
				+ ", minMarketingSmsNumber=" + minMarketingSmsNumber + ", maxMarketingSmsNumber="
				+ maxMarketingSmsNumber + ", sellerFlag=" + sellerFlag + ", sentFilter=" + sentFilter
				+ ", memberStatus=" + memberStatus + ", userId=" + userId + ", tradeTimeStatus=" + tradeTimeStatus
				+ ", tradeType=" + tradeType + ", sex=" + sex + ", ageRange=" + ageRange + ", age=" + age
				+ ", occupation=" + occupation + ", birthday=" + birthday + ", qq=" + qq + ", wechat=" + wechat
				+ ", phone=" + phone + ", phoneRange=" + phoneRange + ", email=" + email + ", emailType=" + emailType
				+ ", registerDate=" + registerDate + ", score=" + score + ", remarks=" + remarks + ", optlock="
				+ optlock + "]";
	}

}
