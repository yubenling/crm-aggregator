package com.kycrm.member.domain.entity.member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kycrm.member.domain.entity.base.ShardingTable;
import com.kycrm.member.domain.entity.order.OrderDTO;
import com.kycrm.member.domain.to.ReceiverInfo;
import com.kycrm.member.domain.to.RemarkInfo;

/**
 * @Title: MemberInfoDTO.java
 * @Package com.kycrm.member.domain Copyright: Copyright (c) 2017
 *          Company:北京冰点零度科技有限公司 *
 * @author zlp
 * @date 2017年12月29日 下午4:15:08
 * @version V1.0
 */
public class MemberInfoDTO implements Serializable, ShardingTable {

	private static final long serialVersionUID = -7538644406089477521L;

	/**
	 * 主键id
	 */
	private Long id;

	/**
	 * 分库分表实体对应用户表的主键id<br/>
	 * column:uid
	 */
	private Long uid;

	/**
	 * 所属卖家昵称<br/>
	 * column:user_name
	 */
	private String userName;

	/**
	 * 买家会员ID<br/>
	 * column:buyer_id
	 */
	private String buyerId;

	/**
	 * 会员的等级ID<br/>
	 * column:grade_id
	 */
	private Long gradeId;

	/**
	 * 会员等级名称<br/>
	 * column:grade_name
	 */
	private Long gradeName;

	/**
	 * 买家昵称<br/>
	 * column:buyer_nick
	 */
	private String buyerNick;

	/**
	 * 买家名称/签收人名称（可能会随订单发生变化）<br/>
	 * column:receiver_name
	 */
	private String receiverName;

	/**
	 * 买家邮箱<br/>
	 * column:buyer_email
	 */
	private String buyerEmail;

	/**
	 * 买家支付宝账号<br/>
	 * column:buyer_alipay_no
	 */
	private String buyerAlipayNo;

	/**
	 * 会员状态 1-正常 2-黑名单<br/>
	 * column:status
	 */
	private String status = "1";

	/**
	 * 交易成功笔数<br/>
	 * column:trade_num
	 */
	private Long tradeNum;

	/**
	 * 交易关闭的笔数<br/>
	 * column:close_trade_num
	 */
	private Long closeTradeNum;

	/**
	 * 交易成功的金额<br/>
	 * column:trade_amount
	 */
	private BigDecimal tradeAmount;

	/**
	 * 交易关闭的金额<br/>
	 * column:close_trade_amount
	 */
	private BigDecimal closeTradeAmount;

	/**
	 * 省份<br/>
	 * column:province
	 */
	private String province;

	/**
	 * 城市<br/>
	 * column:city
	 */
	private String city;

	/**
	 * 购买的宝贝件数<br/>
	 * column:item_num
	 */
	private Long itemNum;

	/**
	 * 交易关闭的宝贝件数<br/>
	 * column:close_item_num
	 */
	private Long closeItemNum;

	/**
	 * 平均订单价<br/>
	 * column:avg_trade_price
	 */
	private BigDecimal avgTradePrice;

	/**
	 * 关系来源 1-交易成功 2-未成交 3-卖家主动吸纳<br/>
	 * column:relation_source
	 */
	private Integer relationSource;

	/**
	 * 最后交易时间<br/>
	 * column:last_trade_time
	 */
	private Date lastTradeTime;

	/**
	 * 性别 1-男 2-女<br/>
	 * column:gender
	 */
	private String gender;

	/**
	 * 年龄<br/>
	 * column:age
	 */
	private Integer age;

	/**
	 * 职业<br/>
	 * column:occupation
	 */
	private String occupation;

	/**
	 * 生日<br/>
	 * column:birthday
	 */
	private String birthday;

	/**
	 * QQ号<br/>
	 * column:qq
	 */
	private String qq;

	/**
	 * 微信<br/>
	 * column:we_chat
	 */
	private String weChat;

	/**
	 * 手机号<br/>
	 * column:mobile
	 */
	private String mobile;

	/**
	 * 注册时间<br/>
	 * column:register_date
	 */
	private Date registerDate;

	/**
	 * 评价得分<br/>
	 * column:score
	 */
	private String score;

	/**
	 * 备注<br/>
	 * column:remarkStr
	 */
	private String remarks;

	/**
	 * 备注时间<br/>
	 * column: remark_str_time
	 */
	private Date remarkStrTime;

	/**
	 * 反序列化封装remarkStr,数据库不存在该字段,慎用!
	 */
	private transient List<RemarkInfo> remarkList;

	/**
	 * 会员等级名称<br/>
	 * column:cur_grade_name
	 */
	private String curGradeName;

	/**
	 * 是否退过款<br/>
	 * true:退过/false&null:未<br/>
	 * column:refund_flag
	 */
	private Boolean refundFlag;

	/**
	 * 是否给过中差评 true:是 false:没有<br/>
	 * column:neutral_bad_rate
	 */
	private Boolean neutralBadRate = false;

	/**
	 * 是否退订回N<br/>
	 * true:是;false:否 column:unsubscribe
	 */
	private Boolean unsubscribe = false;

	/**
	 * 收货人信息<br/>
	 * 收货人名称,收货人地址,手机号 column:receiver_info_str
	 */
	private String receiverInfoStr;

	/**
	 * 最后发送时间
	 */
	private Date lastSendTime;

	/**
	 * 反序列化封装receiverInfoStr,数据库不存在该字段,慎用!
	 */
	private transient List<ReceiverInfo> receiverList;

	/**
	 * 星座
	 */
	private String horoscope;

	/**
	 * 生肖
	 */
	private String zodiac;

	/**
	 * 血型
	 */
	private String bloodType;

	/**
	 * 情感状态
	 */
	private String emotionalState;

	/**
	 * 付款商品数量
	 */
	private Long payItemNum;

	/**
	 * 拍下商品数量
	 */
	private Long addItemNum;

	/**
	 * 货单价
	 */
	private BigDecimal avgGoodsPrice;

	/**
	 * 首次付款时间
	 */
	private Date firstPayTime;

	/**
	 * 二次付款时间
	 */
	private Date secondPayTime;

	/**
	 * 最后付款时间/上次付款时间
	 */
	private Date lastPayTime;

	/**
	 * 总购买次数
	 */
	private Integer buyNumber;

	/**
	 * 总拍下次数
	 */
	private Integer addNumber;

	/**
	 * 拍下未付款次数(使用close_trade_num)
	 */
	private Integer addNotPayNumber;

	/**
	 * 总付款金额
	 */
	private BigDecimal totalPayFee;

	/**
	 * 首次购买商品
	 */
	private Long firstPayItem;

	/**
	 * 最后一次购买商品
	 */
	private Long lastPayItem;

	/**
	 * 退款次数
	 */
	private Integer refundNumber;

	/**
	 * 退款金额
	 */
	private BigDecimal refundFee;

	/**
	 * 退款比例
	 */
	private String refundScale;

	/**
	 * 退款子订单数
	 */
	private Integer refundOrderNum;

	/**
	 * 中差评次数
	 */
	private Integer neutralBadRateNum;

	/**
	 * 版本<br/>
	 * column:optlock
	 */
	private Integer version = 0;

	/**
	 * 创建者<br/>
	 * column:createdBy
	 */
	private String createdBy;

	/**
	 * 创建时间<br/>
	 * column:createdDate
	 */
	protected Date createdDate;

	/**
	 * 最后修改者<br/>
	 * column:lastModifiedBy
	 */
	private String lastModifiedBy;

	/**
	 * 最后修改时间<br/>
	 * column:lastModifiedDate
	 */
	private Date lastModifiedDate;

	/**
	 * 最后发送营销短信时间<br/>
	 * column:last_marketing_time
	 */
	private Date lastMarketingTime;

	/**
	 * 营销短信发送次数<br/>
	 * column:marketing_sms_number
	 */
	private Integer marketingSmsNumber;

	/**
	 * 信用等级<br/>
	 * column:credit_rank
	 */
	private String creditRank;
	/**
	 * 号码号段前三位
	 */
	private String  dnsegThree;  
	/**
	 *运营商 
	 */
	private String   operator;          
	/**
	 * 运营商所在省
	 */
	private String   dnsegProvince ;      
	/**
	 * 运营商所在市
	 */
	private String   dnsegCity ;        
	

	private OrderDTO orderDTO;
	
	/**
	 * 首次交易时间
	 * @return
	 */
	private Date firstTradeTime;
	/**
	 * 首次交易完成时间
	 * @return
	 */
	private Date firstTradeFinishTime;
	/**
	 * 末次交易完成时间
	 * @return
	 */
	private Date lastTradeFinishTime;
	
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getBuyerId() {
		return buyerId;
	}

	public void setBuyerId(String buyerId) {
		this.buyerId = buyerId;
	}

	public Long getGradeId() {
		return gradeId;
	}

	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}

	public Long getGradeName() {
		return gradeName;
	}

	public void setGradeName(Long gradeName) {
		this.gradeName = gradeName;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getBuyerEmail() {
		return buyerEmail;
	}

	public void setBuyerEmail(String buyerEmail) {
		this.buyerEmail = buyerEmail;
	}

	public String getBuyerAlipayNo() {
		return buyerAlipayNo;
	}

	public void setBuyerAlipayNo(String buyerAlipayNo) {
		this.buyerAlipayNo = buyerAlipayNo;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(Long tradeNum) {
		this.tradeNum = tradeNum;
	}

	public Long getCloseTradeNum() {
		return closeTradeNum;
	}

	public void setCloseTradeNum(Long closeTradeNum) {
		this.closeTradeNum = closeTradeNum;
	}

	public BigDecimal getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(BigDecimal tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public BigDecimal getCloseTradeAmount() {
		return closeTradeAmount;
	}

	public void setCloseTradeAmount(BigDecimal closeTradeAmount) {
		this.closeTradeAmount = closeTradeAmount;
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

	public Long getCloseItemNum() {
		return closeItemNum;
	}

	public void setCloseItemNum(Long closeItemNum) {
		this.closeItemNum = closeItemNum;
	}

	public BigDecimal getAvgTradePrice() {
		return avgTradePrice;
	}

	public void setAvgTradePrice(BigDecimal avgTradePrice) {
		this.avgTradePrice = avgTradePrice;
	}

	public Integer getRelationSource() {
		return relationSource;
	}

	public void setRelationSource(Integer relationSource) {
		this.relationSource = relationSource;
	}

	public Date getLastTradeTime() {
		return lastTradeTime;
	}

	public void setLastTradeTime(Date lastTradeTime) {
		this.lastTradeTime = lastTradeTime;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
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

	public String getWeChat() {
		return weChat;
	}

	public void setWeChat(String weChat) {
		this.weChat = weChat;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
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

	public List<RemarkInfo> getRemarkList() {
		return remarkList;
	}

	public void setRemarkList(List<RemarkInfo> remarkList) {
		this.remarkList = remarkList;
	}

	public String getCurGradeName() {
		return curGradeName;
	}

	public void setCurGradeName(String curGradeName) {
		this.curGradeName = curGradeName;
	}

	public Boolean getRefundFlag() {
		return refundFlag;
	}

	public void setRefundFlag(Boolean refundFlag) {
		this.refundFlag = refundFlag;
	}

	public Boolean getNeutralBadRate() {
		return neutralBadRate;
	}

	public void setNeutralBadRate(Boolean neutralBadRate) {
		this.neutralBadRate = neutralBadRate;
	}

	public Boolean getUnsubscribe() {
		return unsubscribe;
	}

	public void setUnsubscribe(Boolean unsubscribe) {
		this.unsubscribe = unsubscribe;
	}

	public String getReceiverInfoStr() {
		return receiverInfoStr;
	}

	public void setReceiverInfoStr(String receiverInfoStr) {
		this.receiverInfoStr = receiverInfoStr;
	}

	public List<ReceiverInfo> getReceiverList() {
		return receiverList;
	}

	public void setReceiverList(List<ReceiverInfo> receiverList) {
		this.receiverList = receiverList;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Date getLastSendTime() {
		return lastSendTime;
	}

	public void setLastSendTime(Date lastSendTime) {
		this.lastSendTime = lastSendTime;
	}

	public OrderDTO getOrderDTO() {
		return orderDTO;
	}

	public void setOrderDTO(OrderDTO orderDTO) {
		this.orderDTO = orderDTO;
	}

	public Date getLastMarketingTime() {
		return lastMarketingTime;
	}

	public void setLastMarketingTime(Date lastMarketingTime) {
		this.lastMarketingTime = lastMarketingTime;
	}

	public Integer getMarketingSmsNumber() {
		return marketingSmsNumber;
	}

	public void setMarketingSmsNumber(Integer marketingSmsNumber) {
		this.marketingSmsNumber = marketingSmsNumber;
	}

	public String getCreditRank() {
		return creditRank;
	}

	public void setCreditRank(String creditRank) {
		this.creditRank = creditRank;
	}

	public String getHoroscope() {
		return horoscope;
	}

	public void setHoroscope(String horoscope) {
		this.horoscope = horoscope;
	}

	public String getZodiac() {
		return zodiac;
	}

	public void setZodiac(String zodiac) {
		this.zodiac = zodiac;
	}

	public String getBloodType() {
		return bloodType;
	}

	public void setBloodType(String bloodType) {
		this.bloodType = bloodType;
	}

	public String getEmotionalState() {
		return emotionalState;
	}

	public void setEmotionalState(String emotionalState) {
		this.emotionalState = emotionalState;
	}

	public Long getPayItemNum() {
		return payItemNum;
	}

	public void setPayItemNum(Long payItemNum) {
		this.payItemNum = payItemNum;
	}

	public Long getAddItemNum() {
		return addItemNum;
	}

	public void setAddItemNum(Long addItemNum) {
		this.addItemNum = addItemNum;
	}

	public BigDecimal getAvgGoodsPrice() {
		return avgGoodsPrice;
	}

	public void setAvgGoodsPrice(BigDecimal avgGoodsPrice) {
		this.avgGoodsPrice = avgGoodsPrice;
	}

	public Date getFirstPayTime() {
		return firstPayTime;
	}

	public void setFirstPayTime(Date firstPayTime) {
		this.firstPayTime = firstPayTime;
	}

	public Date getSecondPayTime() {
		return secondPayTime;
	}

	public void setSecondPayTime(Date secondPayTime) {
		this.secondPayTime = secondPayTime;
	}

	public Date getLastPayTime() {
		return lastPayTime;
	}

	public void setLastPayTime(Date lastPayTime) {
		this.lastPayTime = lastPayTime;
	}

	public Integer getBuyNumber() {
		return buyNumber;
	}

	public void setBuyNumber(Integer buyNumber) {
		this.buyNumber = buyNumber;
	}

	public Integer getAddNumber() {
		return addNumber;
	}

	public void setAddNumber(Integer addNumber) {
		this.addNumber = addNumber;
	}

	public Integer getAddNotPayNumber() {
		return addNotPayNumber;
	}

	public void setAddNotPayNumber(Integer addNotPayNumber) {
		this.addNotPayNumber = addNotPayNumber;
	}

	public BigDecimal getTotalPayFee() {
		return totalPayFee;
	}

	public void setTotalPayFee(BigDecimal totalPayFee) {
		this.totalPayFee = totalPayFee;
	}

	public Long getFirstPayItem() {
		return firstPayItem;
	}

	public void setFirstPayItem(Long firstPayItem) {
		this.firstPayItem = firstPayItem;
	}

	public Long getLastPayItem() {
		return lastPayItem;
	}

	public void setLastPayItem(Long lastPayItem) {
		this.lastPayItem = lastPayItem;
	}

	public Integer getRefundNumber() {
		return refundNumber;
	}

	public void setRefundNumber(Integer refundNumber) {
		this.refundNumber = refundNumber;
	}

	public BigDecimal getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(BigDecimal refundFee) {
		this.refundFee = refundFee;
	}

	public String getRefundScale() {
		return refundScale;
	}

	public void setRefundScale(String refundScale) {
		this.refundScale = refundScale;
	}

	public Integer getRefundOrderNum() {
		return refundOrderNum;
	}

	public void setRefundOrderNum(Integer refundOrderNum) {
		this.refundOrderNum = refundOrderNum;
	}

	public Integer getNeutralBadRateNum() {
		return neutralBadRateNum;
	}

	public void setNeutralBadRateNum(Integer neutralBadRateNum) {
		this.neutralBadRateNum = neutralBadRateNum;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public Date getRemarkStrTime() {
		return remarkStrTime;
	}

	public void setRemarkStrTime(Date remarkStrTime) {
		this.remarkStrTime = remarkStrTime;
	}
    
	public String getDnsegThree() {
		return dnsegThree;
	}

	public void setDnsegThree(String dnsegThree) {
		this.dnsegThree = dnsegThree;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getDnsegProvince() {
		return dnsegProvince;
	}

	public void setDnsegProvince(String dnsegProvince) {
		this.dnsegProvince = dnsegProvince;
	}

	public String getDnsegCity() {
		return dnsegCity;
	}

	public void setDnsegCity(String dnsegCity) {
		this.dnsegCity = dnsegCity;
	}
    
	public Date getFirstTradeTime() {
		return firstTradeTime;
	}

	public void setFirstTradeTime(Date firstTradeTime) {
		this.firstTradeTime = firstTradeTime;
	}

	public Date getFirstTradeFinishTime() {
		return firstTradeFinishTime;
	}

	public void setFirstTradeFinishTime(Date firstTradeFinishTime) {
		this.firstTradeFinishTime = firstTradeFinishTime;
	}

	public Date getLastTradeFinishTime() {
		return lastTradeFinishTime;
	}

	public void setLastTradeFinishTime(Date lastTradeFinishTime) {
		this.lastTradeFinishTime = lastTradeFinishTime;
	}

	@Override
	public String toString() {
		return "MemberInfoDTO [id=" + id + ", uid=" + uid + ", userName="
				+ userName + ", buyerId=" + buyerId + ", gradeId=" + gradeId
				+ ", gradeName=" + gradeName + ", buyerNick=" + buyerNick
				+ ", receiverName=" + receiverName + ", buyerEmail="
				+ buyerEmail + ", buyerAlipayNo=" + buyerAlipayNo + ", status="
				+ status + ", tradeNum=" + tradeNum + ", closeTradeNum="
				+ closeTradeNum + ", tradeAmount=" + tradeAmount
				+ ", closeTradeAmount=" + closeTradeAmount + ", province="
				+ province + ", city=" + city + ", itemNum=" + itemNum
				+ ", closeItemNum=" + closeItemNum + ", avgTradePrice="
				+ avgTradePrice + ", relationSource=" + relationSource
				+ ", lastTradeTime=" + lastTradeTime + ", gender=" + gender
				+ ", age=" + age + ", occupation=" + occupation + ", birthday="
				+ birthday + ", qq=" + qq + ", weChat=" + weChat + ", mobile="
				+ mobile + ", registerDate=" + registerDate + ", score="
				+ score + ", remarks=" + remarks + ", remarkStrTime="
				+ remarkStrTime + ", curGradeName=" + curGradeName
				+ ", refundFlag=" + refundFlag + ", neutralBadRate="
				+ neutralBadRate + ", unsubscribe=" + unsubscribe
				+ ", receiverInfoStr=" + receiverInfoStr + ", lastSendTime="
				+ lastSendTime + ", horoscope=" + horoscope + ", zodiac="
				+ zodiac + ", bloodType=" + bloodType + ", emotionalState="
				+ emotionalState + ", payItemNum=" + payItemNum
				+ ", addItemNum=" + addItemNum + ", avgGoodsPrice="
				+ avgGoodsPrice + ", firstPayTime=" + firstPayTime
				+ ", secondPayTime=" + secondPayTime + ", lastPayTime="
				+ lastPayTime + ", buyNumber=" + buyNumber + ", addNumber="
				+ addNumber + ", addNotPayNumber=" + addNotPayNumber
				+ ", totalPayFee=" + totalPayFee + ", firstPayItem="
				+ firstPayItem + ", lastPayItem=" + lastPayItem
				+ ", refundNumber=" + refundNumber + ", refundFee=" + refundFee
				+ ", refundScale=" + refundScale + ", refundOrderNum="
				+ refundOrderNum + ", neutralBadRateNum=" + neutralBadRateNum
				+ ", version=" + version + ", createdBy=" + createdBy
				+ ", createdDate=" + createdDate + ", lastModifiedBy="
				+ lastModifiedBy + ", lastModifiedDate=" + lastModifiedDate
				+ ", lastMarketingTime=" + lastMarketingTime
				+ ", marketingSmsNumber=" + marketingSmsNumber
				+ ", creditRank=" + creditRank + ", dnsegThree=" + dnsegThree
				+ ", operator=" + operator + ", dnsegProvince=" + dnsegProvince
				+ ", dnsegCity=" + dnsegCity + ", orderDTO=" + orderDTO
				+ ", firstTradeTime=" + firstTradeTime
				+ ", firstTradeFinishTime=" + firstTradeFinishTime
				+ ", lastTradeFinishTime=" + lastTradeFinishTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((addItemNum == null) ? 0 : addItemNum.hashCode());
		result = prime * result
				+ ((addNotPayNumber == null) ? 0 : addNotPayNumber.hashCode());
		result = prime * result
				+ ((addNumber == null) ? 0 : addNumber.hashCode());
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result
				+ ((avgGoodsPrice == null) ? 0 : avgGoodsPrice.hashCode());
		result = prime * result
				+ ((avgTradePrice == null) ? 0 : avgTradePrice.hashCode());
		result = prime * result
				+ ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result
				+ ((bloodType == null) ? 0 : bloodType.hashCode());
		result = prime * result
				+ ((buyNumber == null) ? 0 : buyNumber.hashCode());
		result = prime * result
				+ ((buyerAlipayNo == null) ? 0 : buyerAlipayNo.hashCode());
		result = prime * result
				+ ((buyerEmail == null) ? 0 : buyerEmail.hashCode());
		result = prime * result + ((buyerId == null) ? 0 : buyerId.hashCode());
		result = prime * result
				+ ((buyerNick == null) ? 0 : buyerNick.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result
				+ ((closeItemNum == null) ? 0 : closeItemNum.hashCode());
		result = prime
				* result
				+ ((closeTradeAmount == null) ? 0 : closeTradeAmount.hashCode());
		result = prime * result
				+ ((closeTradeNum == null) ? 0 : closeTradeNum.hashCode());
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((creditRank == null) ? 0 : creditRank.hashCode());
		result = prime * result
				+ ((curGradeName == null) ? 0 : curGradeName.hashCode());
		result = prime * result
				+ ((dnsegCity == null) ? 0 : dnsegCity.hashCode());
		result = prime * result
				+ ((dnsegProvince == null) ? 0 : dnsegProvince.hashCode());
		result = prime * result
				+ ((dnsegThree == null) ? 0 : dnsegThree.hashCode());
		result = prime * result
				+ ((emotionalState == null) ? 0 : emotionalState.hashCode());
		result = prime * result
				+ ((firstPayItem == null) ? 0 : firstPayItem.hashCode());
		result = prime * result
				+ ((firstPayTime == null) ? 0 : firstPayTime.hashCode());
		result = prime
				* result
				+ ((firstTradeFinishTime == null) ? 0 : firstTradeFinishTime
						.hashCode());
		result = prime * result
				+ ((firstTradeTime == null) ? 0 : firstTradeTime.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((gradeId == null) ? 0 : gradeId.hashCode());
		result = prime * result
				+ ((gradeName == null) ? 0 : gradeName.hashCode());
		result = prime * result
				+ ((horoscope == null) ? 0 : horoscope.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((itemNum == null) ? 0 : itemNum.hashCode());
		result = prime
				* result
				+ ((lastMarketingTime == null) ? 0 : lastMarketingTime
						.hashCode());
		result = prime * result
				+ ((lastModifiedBy == null) ? 0 : lastModifiedBy.hashCode());
		result = prime
				* result
				+ ((lastModifiedDate == null) ? 0 : lastModifiedDate.hashCode());
		result = prime * result
				+ ((lastPayItem == null) ? 0 : lastPayItem.hashCode());
		result = prime * result
				+ ((lastPayTime == null) ? 0 : lastPayTime.hashCode());
		result = prime * result
				+ ((lastSendTime == null) ? 0 : lastSendTime.hashCode());
		result = prime
				* result
				+ ((lastTradeFinishTime == null) ? 0 : lastTradeFinishTime
						.hashCode());
		result = prime * result
				+ ((lastTradeTime == null) ? 0 : lastTradeTime.hashCode());
		result = prime
				* result
				+ ((marketingSmsNumber == null) ? 0 : marketingSmsNumber
						.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result
				+ ((neutralBadRate == null) ? 0 : neutralBadRate.hashCode());
		result = prime
				* result
				+ ((neutralBadRateNum == null) ? 0 : neutralBadRateNum
						.hashCode());
		result = prime * result
				+ ((occupation == null) ? 0 : occupation.hashCode());
		result = prime * result
				+ ((operator == null) ? 0 : operator.hashCode());
		result = prime * result
				+ ((orderDTO == null) ? 0 : orderDTO.hashCode());
		result = prime * result
				+ ((payItemNum == null) ? 0 : payItemNum.hashCode());
		result = prime * result
				+ ((province == null) ? 0 : province.hashCode());
		result = prime * result + ((qq == null) ? 0 : qq.hashCode());
		result = prime * result
				+ ((receiverInfoStr == null) ? 0 : receiverInfoStr.hashCode());
		result = prime * result
				+ ((receiverList == null) ? 0 : receiverList.hashCode());
		result = prime * result
				+ ((receiverName == null) ? 0 : receiverName.hashCode());
		result = prime * result
				+ ((refundFee == null) ? 0 : refundFee.hashCode());
		result = prime * result
				+ ((refundFlag == null) ? 0 : refundFlag.hashCode());
		result = prime * result
				+ ((refundNumber == null) ? 0 : refundNumber.hashCode());
		result = prime * result
				+ ((refundOrderNum == null) ? 0 : refundOrderNum.hashCode());
		result = prime * result
				+ ((refundScale == null) ? 0 : refundScale.hashCode());
		result = prime * result
				+ ((registerDate == null) ? 0 : registerDate.hashCode());
		result = prime * result
				+ ((relationSource == null) ? 0 : relationSource.hashCode());
		result = prime * result
				+ ((remarkList == null) ? 0 : remarkList.hashCode());
		result = prime * result
				+ ((remarkStrTime == null) ? 0 : remarkStrTime.hashCode());
		result = prime * result + ((remarks == null) ? 0 : remarks.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result
				+ ((secondPayTime == null) ? 0 : secondPayTime.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((totalPayFee == null) ? 0 : totalPayFee.hashCode());
		result = prime * result
				+ ((tradeAmount == null) ? 0 : tradeAmount.hashCode());
		result = prime * result
				+ ((tradeNum == null) ? 0 : tradeNum.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result
				+ ((unsubscribe == null) ? 0 : unsubscribe.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((version == null) ? 0 : version.hashCode());
		result = prime * result + ((weChat == null) ? 0 : weChat.hashCode());
		result = prime * result + ((zodiac == null) ? 0 : zodiac.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MemberInfoDTO other = (MemberInfoDTO) obj;
		if (addItemNum == null) {
			if (other.addItemNum != null)
				return false;
		} else if (!addItemNum.equals(other.addItemNum))
			return false;
		if (addNotPayNumber == null) {
			if (other.addNotPayNumber != null)
				return false;
		} else if (!addNotPayNumber.equals(other.addNotPayNumber))
			return false;
		if (addNumber == null) {
			if (other.addNumber != null)
				return false;
		} else if (!addNumber.equals(other.addNumber))
			return false;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
			return false;
		if (avgGoodsPrice == null) {
			if (other.avgGoodsPrice != null)
				return false;
		} else if (!avgGoodsPrice.equals(other.avgGoodsPrice))
			return false;
		if (avgTradePrice == null) {
			if (other.avgTradePrice != null)
				return false;
		} else if (!avgTradePrice.equals(other.avgTradePrice))
			return false;
		if (birthday == null) {
			if (other.birthday != null)
				return false;
		} else if (!birthday.equals(other.birthday))
			return false;
		if (bloodType == null) {
			if (other.bloodType != null)
				return false;
		} else if (!bloodType.equals(other.bloodType))
			return false;
		if (buyNumber == null) {
			if (other.buyNumber != null)
				return false;
		} else if (!buyNumber.equals(other.buyNumber))
			return false;
		if (buyerAlipayNo == null) {
			if (other.buyerAlipayNo != null)
				return false;
		} else if (!buyerAlipayNo.equals(other.buyerAlipayNo))
			return false;
		if (buyerEmail == null) {
			if (other.buyerEmail != null)
				return false;
		} else if (!buyerEmail.equals(other.buyerEmail))
			return false;
		if (buyerId == null) {
			if (other.buyerId != null)
				return false;
		} else if (!buyerId.equals(other.buyerId))
			return false;
		if (buyerNick == null) {
			if (other.buyerNick != null)
				return false;
		} else if (!buyerNick.equals(other.buyerNick))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (closeItemNum == null) {
			if (other.closeItemNum != null)
				return false;
		} else if (!closeItemNum.equals(other.closeItemNum))
			return false;
		if (closeTradeAmount == null) {
			if (other.closeTradeAmount != null)
				return false;
		} else if (!closeTradeAmount.equals(other.closeTradeAmount))
			return false;
		if (closeTradeNum == null) {
			if (other.closeTradeNum != null)
				return false;
		} else if (!closeTradeNum.equals(other.closeTradeNum))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (creditRank == null) {
			if (other.creditRank != null)
				return false;
		} else if (!creditRank.equals(other.creditRank))
			return false;
		if (curGradeName == null) {
			if (other.curGradeName != null)
				return false;
		} else if (!curGradeName.equals(other.curGradeName))
			return false;
		if (dnsegCity == null) {
			if (other.dnsegCity != null)
				return false;
		} else if (!dnsegCity.equals(other.dnsegCity))
			return false;
		if (dnsegProvince == null) {
			if (other.dnsegProvince != null)
				return false;
		} else if (!dnsegProvince.equals(other.dnsegProvince))
			return false;
		if (dnsegThree == null) {
			if (other.dnsegThree != null)
				return false;
		} else if (!dnsegThree.equals(other.dnsegThree))
			return false;
		if (emotionalState == null) {
			if (other.emotionalState != null)
				return false;
		} else if (!emotionalState.equals(other.emotionalState))
			return false;
		if (firstPayItem == null) {
			if (other.firstPayItem != null)
				return false;
		} else if (!firstPayItem.equals(other.firstPayItem))
			return false;
		if (firstPayTime == null) {
			if (other.firstPayTime != null)
				return false;
		} else if (!firstPayTime.equals(other.firstPayTime))
			return false;
		if (firstTradeFinishTime == null) {
			if (other.firstTradeFinishTime != null)
				return false;
		} else if (!firstTradeFinishTime.equals(other.firstTradeFinishTime))
			return false;
		if (firstTradeTime == null) {
			if (other.firstTradeTime != null)
				return false;
		} else if (!firstTradeTime.equals(other.firstTradeTime))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (gradeId == null) {
			if (other.gradeId != null)
				return false;
		} else if (!gradeId.equals(other.gradeId))
			return false;
		if (gradeName == null) {
			if (other.gradeName != null)
				return false;
		} else if (!gradeName.equals(other.gradeName))
			return false;
		if (horoscope == null) {
			if (other.horoscope != null)
				return false;
		} else if (!horoscope.equals(other.horoscope))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (itemNum == null) {
			if (other.itemNum != null)
				return false;
		} else if (!itemNum.equals(other.itemNum))
			return false;
		if (lastMarketingTime == null) {
			if (other.lastMarketingTime != null)
				return false;
		} else if (!lastMarketingTime.equals(other.lastMarketingTime))
			return false;
		if (lastModifiedBy == null) {
			if (other.lastModifiedBy != null)
				return false;
		} else if (!lastModifiedBy.equals(other.lastModifiedBy))
			return false;
		if (lastModifiedDate == null) {
			if (other.lastModifiedDate != null)
				return false;
		} else if (!lastModifiedDate.equals(other.lastModifiedDate))
			return false;
		if (lastPayItem == null) {
			if (other.lastPayItem != null)
				return false;
		} else if (!lastPayItem.equals(other.lastPayItem))
			return false;
		if (lastPayTime == null) {
			if (other.lastPayTime != null)
				return false;
		} else if (!lastPayTime.equals(other.lastPayTime))
			return false;
		if (lastSendTime == null) {
			if (other.lastSendTime != null)
				return false;
		} else if (!lastSendTime.equals(other.lastSendTime))
			return false;
		if (lastTradeFinishTime == null) {
			if (other.lastTradeFinishTime != null)
				return false;
		} else if (!lastTradeFinishTime.equals(other.lastTradeFinishTime))
			return false;
		if (lastTradeTime == null) {
			if (other.lastTradeTime != null)
				return false;
		} else if (!lastTradeTime.equals(other.lastTradeTime))
			return false;
		if (marketingSmsNumber == null) {
			if (other.marketingSmsNumber != null)
				return false;
		} else if (!marketingSmsNumber.equals(other.marketingSmsNumber))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (neutralBadRate == null) {
			if (other.neutralBadRate != null)
				return false;
		} else if (!neutralBadRate.equals(other.neutralBadRate))
			return false;
		if (neutralBadRateNum == null) {
			if (other.neutralBadRateNum != null)
				return false;
		} else if (!neutralBadRateNum.equals(other.neutralBadRateNum))
			return false;
		if (occupation == null) {
			if (other.occupation != null)
				return false;
		} else if (!occupation.equals(other.occupation))
			return false;
		if (operator == null) {
			if (other.operator != null)
				return false;
		} else if (!operator.equals(other.operator))
			return false;
		if (orderDTO == null) {
			if (other.orderDTO != null)
				return false;
		} else if (!orderDTO.equals(other.orderDTO))
			return false;
		if (payItemNum == null) {
			if (other.payItemNum != null)
				return false;
		} else if (!payItemNum.equals(other.payItemNum))
			return false;
		if (province == null) {
			if (other.province != null)
				return false;
		} else if (!province.equals(other.province))
			return false;
		if (qq == null) {
			if (other.qq != null)
				return false;
		} else if (!qq.equals(other.qq))
			return false;
		if (receiverInfoStr == null) {
			if (other.receiverInfoStr != null)
				return false;
		} else if (!receiverInfoStr.equals(other.receiverInfoStr))
			return false;
		if (receiverList == null) {
			if (other.receiverList != null)
				return false;
		} else if (!receiverList.equals(other.receiverList))
			return false;
		if (receiverName == null) {
			if (other.receiverName != null)
				return false;
		} else if (!receiverName.equals(other.receiverName))
			return false;
		if (refundFee == null) {
			if (other.refundFee != null)
				return false;
		} else if (!refundFee.equals(other.refundFee))
			return false;
		if (refundFlag == null) {
			if (other.refundFlag != null)
				return false;
		} else if (!refundFlag.equals(other.refundFlag))
			return false;
		if (refundNumber == null) {
			if (other.refundNumber != null)
				return false;
		} else if (!refundNumber.equals(other.refundNumber))
			return false;
		if (refundOrderNum == null) {
			if (other.refundOrderNum != null)
				return false;
		} else if (!refundOrderNum.equals(other.refundOrderNum))
			return false;
		if (refundScale == null) {
			if (other.refundScale != null)
				return false;
		} else if (!refundScale.equals(other.refundScale))
			return false;
		if (registerDate == null) {
			if (other.registerDate != null)
				return false;
		} else if (!registerDate.equals(other.registerDate))
			return false;
		if (relationSource == null) {
			if (other.relationSource != null)
				return false;
		} else if (!relationSource.equals(other.relationSource))
			return false;
		if (remarkList == null) {
			if (other.remarkList != null)
				return false;
		} else if (!remarkList.equals(other.remarkList))
			return false;
		if (remarkStrTime == null) {
			if (other.remarkStrTime != null)
				return false;
		} else if (!remarkStrTime.equals(other.remarkStrTime))
			return false;
		if (remarks == null) {
			if (other.remarks != null)
				return false;
		} else if (!remarks.equals(other.remarks))
			return false;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		if (secondPayTime == null) {
			if (other.secondPayTime != null)
				return false;
		} else if (!secondPayTime.equals(other.secondPayTime))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (totalPayFee == null) {
			if (other.totalPayFee != null)
				return false;
		} else if (!totalPayFee.equals(other.totalPayFee))
			return false;
		if (tradeAmount == null) {
			if (other.tradeAmount != null)
				return false;
		} else if (!tradeAmount.equals(other.tradeAmount))
			return false;
		if (tradeNum == null) {
			if (other.tradeNum != null)
				return false;
		} else if (!tradeNum.equals(other.tradeNum))
			return false;
		if (uid == null) {
			if (other.uid != null)
				return false;
		} else if (!uid.equals(other.uid))
			return false;
		if (unsubscribe == null) {
			if (other.unsubscribe != null)
				return false;
		} else if (!unsubscribe.equals(other.unsubscribe))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (version == null) {
			if (other.version != null)
				return false;
		} else if (!version.equals(other.version))
			return false;
		if (weChat == null) {
			if (other.weChat != null)
				return false;
		} else if (!weChat.equals(other.weChat))
			return false;
		if (zodiac == null) {
			if (other.zodiac != null)
				return false;
		} else if (!zodiac.equals(other.zodiac))
			return false;
		return true;
	}
    
	

}
