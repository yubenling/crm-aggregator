package com.kycrm.syn.service.mongo.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;



//@MetaData(value = "买家会员信息")
public class OldMemberDTO  extends BaseMongoDTO implements Serializable{

	
	/**
	 *
	 */
	private static final long serialVersionUID = -7227944584478182586L;

	//@MetaData("主键")
	private String _id;
	
	//@MetaData(value="买家会员ID")
	private Long buyerId;
	
	//@MetaData(value="会员的等级ID")
	private Long gradeId;
	
	//@MetaData(value="卖家ID")
	private String userId;
	
	//@MetaData(value="买家昵称")
	private String buyerNick;
	
	//@MetaData(value="会员状态 1-正常 2-黑名单")
	private String status;
	
	//@MetaData(value="交易成功笔数")
	private Long tradeCount;
	
	//@MetaData(value="交易成功的金额")
	private Double tradeAmount;
	
	//@MetaData(value="交易关闭的金额")
	private Double closeTradeAmount;
	
	//@MetaData(value="交易关闭的笔数")
	private Long closeTradeCount;
	
	//@MetaData(value="省份")
	private String province;
	
	//@MetaData(value="城市")
	private String city;
	
	//@MetaData(value="购买的宝贝件数")
	private Integer itemNum;
	
	//@MetaData(value="平均客单价")
	private Double avgPrice;
	
	//@MetaData(value="关系来源 1-交易成功 2-未成交 3-卖家主动吸纳")
	private Integer relationSource;
	
	//@MetaData(value="最后交易时间")
	private Date lastTradeTimeUTC;
	
	//@MetaData(value="最后交易时间")
	private Long lastTradeTime;
	
	//@MetaData(value="交易关闭的宝贝件数")
	private Integer itemCloseCount;
	
	//@MetaData(value="性别 1-男 2-女")
	private String sex;
	
	//@MetaData(value="年龄")
	private Long age;
	
	//@MetaData(value="职业")
	private String occupation;
	
	//@MetaData(value="生日")
	private String birthday;
	
	//@MetaData(value="QQ号")
	private String qq;

	//@MetaData(value="微信")
	private String wechat;
	
	//@MetaData(value="手机号")
	private String phone;
	
	//@MetaData(value="手机号段")
	private String phoneRange;
	
	//@MetaData(value="邮箱")
	private String email;
	
	//@MetaData(value="邮箱类型")
	private String emailType;
	
	//@MetaData(value="注册时间")
	private Date registerDateUTC;
	//@MetaData(value="注册时间")
	private Long registerDate;

	//@MetaData(value="评价得分")
	private String score;
	
	//@MetaData(value="备注")
	private String remarks;
	
	//@MetaData(value="备注时间")
	private Date remarksTime;
	
	//@MetaData(value="买家自定义分组等级设置条件表id")
	private Long member_level_setting_id;
	
	
	//@MetaData(value="短信黑名单Id")
	private Long smsBlackListId;

	
	//@MetaData(value="会员等级名称")
	private String curGradeName;


	//@MetaData(value="会员识别码")
	private String memberInfoCode;
	
	
	//@MetaData("商品数字编号")
	private String NUM_IID;
	
	//@MetaData("最近订单状态")
	private String lastOrderStatus;
	
	//@MetaData("购买订单的id")
	private String tid;

	
	//@MetaData(value="会员姓名&收货人姓名")
	private String receiverName;
	/**
	 * 交易来源
	 */
	private String tradeFrom;
	//@MetaData("最近短信发送Id")
	private  Long msgId  ;
	
	//@MetaData("最近短信发送Id数组")
	private  List<Long> msgIdList  ;
	
	//@MetaData("最近短信发送时间")
	private  Long lastSendSmsTime  ;
	
	//@MetaData("退款状态       1表示是 0 表示不是")
	private  Integer refund_status  ;
	//@MetaData("黑名单状态")
	private Integer blackStatus;
	
	//@MetaData("中差评状态")
	private Integer commentStatus;
	
	//@MetaData("临时之前购买订单的tid")
	private List<String> tempTid;
	
	
	//@MetaData("节点标识")
	private  Long nodeFlag  ;
	
	//@MetaData("临时交易来源")
	private List<String> tempTradeFrom;
	//@MetaData("临时地区")
	private List<String> tempProvince;
	//@MetaData("临时商品")
	private List<String> tempProduct;
	
	//@MetaData("产生会员的tid 请勿操作")
	private List<String> successTidList;

	
	
 
	public List<String> getSuccessTidList() {
		return successTidList;
	}

	public void setSuccessTidList(List<String> successTidList) {
		this.successTidList = successTidList;
	}

	public List<String> getTempTradeFrom() {
		return tempTradeFrom;
	}

	public void setTempTradeFrom(List<String> tempTradeFrom) {
		this.tempTradeFrom = tempTradeFrom;
	}

	public List<String> getTempProvince() {
		return tempProvince;
	}

	public void setTempProvince(List<String> tempProvince) {
		this.tempProvince = tempProvince;
	}

	public List<String> getTempProduct() {
		return tempProduct;
	}

	public void setTempProduct(List<String> tempProduct) {
		this.tempProduct = tempProduct;
	}

	public Long getNodeFlag() {
		return nodeFlag;
	}

	public void setNodeFlag(Long nodeFlag) {
		this.nodeFlag = nodeFlag;
	}

	public Integer getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(Integer refund_status) {
		this.refund_status = refund_status;
	}

	

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public List<Long> getMsgIdList() {
		return msgIdList;
	}

	public void setMsgIdList(List<Long> msgIdList) {
		this.msgIdList = msgIdList;
	}

	public Long getLastSendSmsTime() {
		return lastSendSmsTime;
	}

	public void setLastSendSmsTime(Long lastSendSmsTime) {
		this.lastSendSmsTime = lastSendSmsTime;
	}

	public List<String> getTempTid() {
		return tempTid;
	}

	public void setTempTid(List<String> tempTid) {
		this.tempTid = tempTid;
	}

	
	public String getTradeFrom() {
		return tradeFrom;
	}

	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getLastOrderStatus() {
		return lastOrderStatus;
	}

	public void setLastOrderStatus(String lastOrderStatus) {
		this.lastOrderStatus = lastOrderStatus;
	}

	public Integer getBlackStatus() {
		return blackStatus;
	}

	public void setBlackStatus(Integer blackStatus) {
		this.blackStatus = blackStatus;
	}

	public Integer getCommentStatus() {
		return commentStatus;
	}

	public void setCommentStatus(Integer commentStatus) {
		this.commentStatus = commentStatus;
	}

	public String getNUM_IID() {
		return NUM_IID;
	}

	public void setNUM_IID(String nUM_IID) {
		NUM_IID = nUM_IID;
	}

	public void setMemberInfoCode(String memberInfoCode) {
		this.memberInfoCode = memberInfoCode;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Long getBuyerId() {
		return buyerId;
	}


	public void setBuyerId(Long buyerId) {
		this.buyerId = buyerId;
	}


	public Long getGradeId() {
		return gradeId;
	}


	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getBuyerNick() {
		return buyerNick;
	}


	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}


	public String getStatus() {
		return status;
	}


	public void setStatus(String status) {
		this.status = status;
	}


	public Long getTradeCount() {
		return tradeCount;
	}


	public void setTradeCount(Long tradeCount) {
		this.tradeCount = tradeCount;
	}


	 


	public Long getCloseTradeCount() {
		return closeTradeCount;
	}


	public void setCloseTradeCount(Long closeTradeCount) {
		this.closeTradeCount = closeTradeCount;
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


	public Integer getItemNum() {
		return itemNum;
	}


	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}

	


	public Double getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(Double avgPrice) {
		this.avgPrice = avgPrice;
	}

	public Integer getRelationSource() {
		return relationSource;
	}


	public void setRelationSource(Integer relationSource) {
		this.relationSource = relationSource;
	}


	 


	public Integer getItemCloseCount() {
		return itemCloseCount;
	}


	public void setItemCloseCount(Integer itemCloseCount) {
		this.itemCloseCount = itemCloseCount;
	}


	public String getSex() {
		return sex;
	}


	public void setSex(String sex) {
		this.sex = sex;
	}

	

	public Double getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(Double tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public Double getCloseTradeAmount() {
		return closeTradeAmount;
	}

	public void setCloseTradeAmount(Double closeTradeAmount) {
		this.closeTradeAmount = closeTradeAmount;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
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


	public Date getLastTradeTimeUTC() {
		return lastTradeTimeUTC;
	}

	public void setLastTradeTimeUTC(Date lastTradeTimeUTC) {
		this.lastTradeTimeUTC = lastTradeTimeUTC;
	}

	public Long getLastTradeTime() {
		return lastTradeTime;
	}

	public void setLastTradeTime(Long lastTradeTime) {
		this.lastTradeTime = lastTradeTime;
	}

	public Date getRegisterDateUTC() {
		return registerDateUTC;
	}

	public void setRegisterDateUTC(Date registerDateUTC) {
		this.registerDateUTC = registerDateUTC;
	}

	public Long getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Long registerDate) {
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


	public Long getMember_level_setting_id() {
		return member_level_setting_id;
	}


	public void setMember_level_setting_id(Long member_level_setting_id) {
		this.member_level_setting_id = member_level_setting_id;
	}


	public Long getSmsBlackListId() {
		return smsBlackListId;
	}


	public void setSmsBlackListId(Long smsBlackListId) {
		this.smsBlackListId = smsBlackListId;
	}


	public String getCurGradeName() {
		return curGradeName;
	}


	public void setCurGradeName(String curGradeName) {
		this.curGradeName = curGradeName;
	}


	public String getMemberInfoCode() {
		return memberInfoCode;
	}


	public void setMemberInfoCode(String buyerNick ,String userId) {
		buyerNick = buyerNick==null?"":buyerNick;
		userId = userId==null?"":userId;
		this.memberInfoCode = buyerNick+"&"+userId;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	
	public Date getRemarksTime() {
		return remarksTime;
	}

	public void setRemarksTime(Date remarksTime) {
		this.remarksTime = remarksTime;
	}

	@Override
	public String toString() {
		return "MemberDTO [_id=" + _id + ", buyerId=" + buyerId + ", gradeId=" + gradeId + ", userId=" + userId
				+ ", buyerNick=" + buyerNick + ", status=" + status + ", tradeCount=" + tradeCount + ", tradeAmount="
				+ tradeAmount + ", closeTradeAmount=" + closeTradeAmount + ", closeTradeCount=" + closeTradeCount
				+ ", province=" + province + ", city=" + city + ", itemNum=" + itemNum + ", avgPrice=" + avgPrice
				+ ", relationSource=" + relationSource + ", lastTradeTime=" + lastTradeTime + ", itemCloseCount="
				+ itemCloseCount + ", sex=" + sex + ", age=" + age + ", occupation=" + occupation + ", birthday="
				+ birthday + ", qq=" + qq + ", wechat=" + wechat + ", phone=" + phone + ", phoneRange=" + phoneRange
				+ ", email=" + email + ", emailType=" + emailType + ", registerDate=" + registerDate + ", score="
				+ score + ", remarks=" + remarks + ", member_level_setting_id=" + member_level_setting_id
				+ ", smsBlackListId=" + smsBlackListId + ", curGradeName=" + curGradeName + ", memberInfoCode="
				+ memberInfoCode + ", NUM_IID=" + NUM_IID + ", blackStauts=" + blackStatus + ", commentStatus="
				+ commentStatus + ",  receiverName=" + receiverName + ",remarksTime=" + remarksTime +"]";
	}
}
