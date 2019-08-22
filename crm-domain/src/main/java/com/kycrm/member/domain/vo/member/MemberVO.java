package com.kycrm.member.domain.vo.member; 

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/** 
* @ClassName: MemberVO 
* @Description: 会员VO
* @author jackstraw_yu
* @date 2018年2月8日 下午4:40:37 
*/
public class MemberVO implements Serializable{

	private static final long serialVersionUID = 4442400037712217446L;
	
	/**
	 * 主键id
	 */
	private Long id;
	
	/**
	 * 分库分表实体对应用户表的主键id<br/>
	 */
    private Long uid;
    
    /**
	 * 所属卖家昵称<br/>
	 */
	private String userName;
	
	/**
	 * 买家会员ID<br/>
	 */
	private String buyerId;
	
	/**
	 * 会员的等级ID<br/>
	 */
	private Long gradeId;
	
	/**
	 * 会员等级名称<br/>
	 */
	private Long gradeName;
	
	/**
	 * 买家昵称<br/>
	 */
	private String buyerNick;
	
	/**
	 * 买家名称/签收人名称（可能会随订单发生变化）<br/>
	 */
	private String receiverName;
	
	/**
	 * 买家邮箱<br/>
	 */
	private String buyerEmail;	
	
	/**
	 * 买家支付宝账号<br/>
	 */
	private String buyerAlipayNo;	
	
	/**
	 * 会员状态 1-正常 2-黑名单<br/>
	 */
	private String status;
	
	/**
	 * 交易成功笔数<br/>
	 */
	private Long tradeNum;

	/**
	 * 交易关闭的笔数<br/>
	 */
	private Long closeTradeNum;
	
	/**
	 * 交易成功的金额<br/>
	 */
	private BigDecimal tradeAmount;
	
	/**
	 * 交易关闭的金额<br/>
	 */
	private BigDecimal closeTradeAmount;
	
	/**
	 * 省份<br/>
	 */
	private String province;
	
	/**
	 * 城市<br/>
	 */
	private String city;
	
	/**
	 * 购买的宝贝件数<br/>
	 */
	private Long itemNum;
	
	/**
	 * 交易关闭的宝贝件数<br/>
	 */
	private Long closeItemNum;
	
	/**
	 * 平均订单价<br/>
	 */
	private BigDecimal avgTradePrice;
	
	/**
	 * 关系来源 1-交易成功 2-未成交 3-卖家主动吸纳<br/>
	 */
	private Integer relationSource;
	
	/**
	 * 最后交易时间<br/>
	 */
	private Date lastTradeTime;
	
	/**
	 * 性别 1-男 2-女<br/>
	 */
	private String gender;
	
	/**
	 * 年龄<br/>
	 */
	private Integer age;
	
	/**
	 * 职业<br/>
	 */
	private String occupation;
	
	/**
	 * 生日<br/>
	 */
	private String birthday;
	
	/**
	 * QQ号<br/>
	 */
	private String qq;
	
	/**
	 * 微信<br/>
	 */
	private String weChat;
	
	/**
	 * 手机号<br/>
	 */
	private String mobile;
	
	/**
	 * 注册时间<br/>
	 */
	private Date registerDate;
	
	/**
	 * 评价得分<br/>
	 */
	private String score;
	
	/**
	 * 备注<br/>
	 */
	private String remarkStr;
	
	/**
	 * 会员等级名称<br/>
	 */
	private String curGradeName;
	
	/**
	 * 是否退过款<br/>
	 * true:退过/false&null:未<br/>
	 */
	private Boolean refundFlage;
	
	/**
	 * 是否给过中差评 true:是 false:没有<br/>
	 */ 
	private Boolean neutralBadRate;
    
	/**
	 * 是否退订回N<br/>
	 * true:是;false:否
	 */ 
	private Boolean unsubscribe;
	
	/**
	 * 收货人信息<br/>
	 * 收货人名称,收货人地址,手机号
	 */
	private String receiverInfoStr;

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

	public String getRemarkStr() {
		return remarkStr;
	}

	public void setRemarkStr(String remarkStr) {
		this.remarkStr = remarkStr;
	}

	public String getCurGradeName() {
		return curGradeName;
	}

	public void setCurGradeName(String curGradeName) {
		this.curGradeName = curGradeName;
	}

	public Boolean getRefundFlage() {
		return refundFlage;
	}

	public void setRefundFlage(Boolean refundFlage) {
		this.refundFlage = refundFlage;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((age == null) ? 0 : age.hashCode());
		result = prime * result + ((avgTradePrice == null) ? 0 : avgTradePrice.hashCode());
		result = prime * result + ((birthday == null) ? 0 : birthday.hashCode());
		result = prime * result + ((buyerAlipayNo == null) ? 0 : buyerAlipayNo.hashCode());
		result = prime * result + ((buyerEmail == null) ? 0 : buyerEmail.hashCode());
		result = prime * result + ((buyerId == null) ? 0 : buyerId.hashCode());
		result = prime * result + ((buyerNick == null) ? 0 : buyerNick.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((closeItemNum == null) ? 0 : closeItemNum.hashCode());
		result = prime * result + ((closeTradeAmount == null) ? 0 : closeTradeAmount.hashCode());
		result = prime * result + ((closeTradeNum == null) ? 0 : closeTradeNum.hashCode());
		result = prime * result + ((curGradeName == null) ? 0 : curGradeName.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + ((gradeId == null) ? 0 : gradeId.hashCode());
		result = prime * result + ((gradeName == null) ? 0 : gradeName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((itemNum == null) ? 0 : itemNum.hashCode());
		result = prime * result + ((lastTradeTime == null) ? 0 : lastTradeTime.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((neutralBadRate == null) ? 0 : neutralBadRate.hashCode());
		result = prime * result + ((occupation == null) ? 0 : occupation.hashCode());
		result = prime * result + ((province == null) ? 0 : province.hashCode());
		result = prime * result + ((qq == null) ? 0 : qq.hashCode());
		result = prime * result + ((receiverInfoStr == null) ? 0 : receiverInfoStr.hashCode());
		result = prime * result + ((receiverName == null) ? 0 : receiverName.hashCode());
		result = prime * result + ((refundFlage == null) ? 0 : refundFlage.hashCode());
		result = prime * result + ((registerDate == null) ? 0 : registerDate.hashCode());
		result = prime * result + ((relationSource == null) ? 0 : relationSource.hashCode());
		result = prime * result + ((remarkStr == null) ? 0 : remarkStr.hashCode());
		result = prime * result + ((score == null) ? 0 : score.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((tradeAmount == null) ? 0 : tradeAmount.hashCode());
		result = prime * result + ((tradeNum == null) ? 0 : tradeNum.hashCode());
		result = prime * result + ((uid == null) ? 0 : uid.hashCode());
		result = prime * result + ((unsubscribe == null) ? 0 : unsubscribe.hashCode());
		result = prime * result + ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((weChat == null) ? 0 : weChat.hashCode());
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
		MemberVO other = (MemberVO) obj;
		if (age == null) {
			if (other.age != null)
				return false;
		} else if (!age.equals(other.age))
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
		if (curGradeName == null) {
			if (other.curGradeName != null)
				return false;
		} else if (!curGradeName.equals(other.curGradeName))
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
		if (lastTradeTime == null) {
			if (other.lastTradeTime != null)
				return false;
		} else if (!lastTradeTime.equals(other.lastTradeTime))
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
		if (occupation == null) {
			if (other.occupation != null)
				return false;
		} else if (!occupation.equals(other.occupation))
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
		if (receiverName == null) {
			if (other.receiverName != null)
				return false;
		} else if (!receiverName.equals(other.receiverName))
			return false;
		if (refundFlage == null) {
			if (other.refundFlage != null)
				return false;
		} else if (!refundFlage.equals(other.refundFlage))
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
		if (remarkStr == null) {
			if (other.remarkStr != null)
				return false;
		} else if (!remarkStr.equals(other.remarkStr))
			return false;
		if (score == null) {
			if (other.score != null)
				return false;
		} else if (!score.equals(other.score))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
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
		if (weChat == null) {
			if (other.weChat != null)
				return false;
		} else if (!weChat.equals(other.weChat))
			return false;
		return true;
	}

	
}
