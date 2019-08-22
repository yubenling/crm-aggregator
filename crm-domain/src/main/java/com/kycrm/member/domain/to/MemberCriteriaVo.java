package com.kycrm.member.domain.to;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MemberCriteriaVo implements Serializable{
	
	/**
	 */
	private static final long serialVersionUID = -883124731955004303L;
	/*用户id*/
	private  String userId;
	/*分组id*/
	private String groupId ;
	/*屏蔽黑名单黑名单*/
	private boolean blackList;
	/*屏蔽中差评*/
	private boolean evaluation;
	/*屏蔽退款*/
	private boolean refund;
	/*屏蔽最近发送*/
	private boolean sheildRecent;
	/*交易金额--最小*/
	private Double minTradePrice ;
	/*交易金额--最大*/
	private Double maxTradePrice;
	/*交易来源*/
	private String customerSource ;
	/*平均客单价--最小*/
	private Double minAvgPrice ;
	/*平均客单价--最大*/
	private Double maxAvgPrice ;
	/*交易量--最小*/
	private Integer minTradeNum ;
	/*交易量--最大*/
	private Integer maxTradeNum ;
	/*地区串*/
	private String region ;
	/*会员昵称*/
	private String buyerName ;
	/*商品id串*/
	private String itemIds ;
	/*最后交易时间--开始*/
	private String tradeStartTime ;
	/*最后交易时间--结束*/
	private String tradeEndTime ;
	/*屏蔽时间*/
	private Integer sendTime =0 ;
	/*单次查询出的总条数*/
	private long totalCount;
	/*会员分组类型*/
	private String groupType;
	
	/*(已发送过滤)  */
	private String lastestSend;
	/*交易次数按几次 */
	private String tradeNumByTimes;
	/*交易时间近几天   */
	private String tradeTimeByDay;
	
	/*交易成功金额*/
	private String tradeAmount;
	
	
	
	/**
	 *查询条件相关属性 
	 * */
	/*商品集合*/
	private List<String> productList;
	/*地区集合*/
	private List<String> areaList;
	/*手机号集合*/
	private List<String> phoneList;
	/*最近发送主键ID集合*/
	private List<Long> msgIdList;
	/*类型*/
	private String type;
	/*屏蔽发送开始时间*/
	private Date sendStartTime;
	/*屏蔽发送结束时间*/
	private Date sendEndTime;
	/*退款状态*/
	private Integer refundStatus;
	/*黑名单状态*/
	private Integer blackStatus;
	/*评价状态*/
	private Integer evaluateStatus;
	/*订单状态*/
	private String orderStatus;
	/*查询的开始行数*/
	private Long startRows;
	/*查询的行数*/
	private Long pageSize;
	
	/*标记改查询msgId是否被填充*/
	private Boolean full;
	 
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}
	
	public Integer getBlackStatus() {
		return blackStatus;
	}
	public void setBlackStatus(Integer blackStatus) {
		this.blackStatus = blackStatus;
	}
	public Integer getEvaluateStatus() {
		return evaluateStatus;
	}
	public void setEvaluateStatus(Integer evaluateStatus) {
		this.evaluateStatus = evaluateStatus;
	}
	public Date getSendStartTime() {
		return sendStartTime;
	}
	public void setSendStartTime(Date sendStartTime) {
		this.sendStartTime = sendStartTime;
	}
	public Date getSendEndTime() {
		return sendEndTime;
	}
	public void setSendEndTime(Date sendEndTime) {
		this.sendEndTime = sendEndTime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
	public List<Long> getMsgIdList() {
		return msgIdList;
	}
	public void setMsgIdList(List<Long> msgIdList) {
		this.msgIdList = msgIdList;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public boolean isBlackList() {
		return blackList;
	}
	public void setBlackList(boolean blackList) {
		this.blackList = blackList;
	}
	public boolean isEvaluation() {
		return evaluation;
	}
	public void setEvaluation(boolean evaluation) {
		this.evaluation = evaluation;
	}
	public boolean isRefund() {
		return refund;
	}
	public void setRefund(boolean refund) {
		this.refund = refund;
	}
	public boolean isSheildRecent() {
		return sheildRecent;
	}
	public void setSheildRecent(boolean sheildRecent) {
		this.sheildRecent = sheildRecent;
	}
	public Double getMinTradePrice() {
		return minTradePrice;
	}
	public void setMinTradePrice(Double minTradePrice) {
		this.minTradePrice = minTradePrice;
	}
	public Double getMaxTradePrice() {
		return maxTradePrice;
	}
	public void setMaxTradePrice(Double maxTradePrice) {
		this.maxTradePrice = maxTradePrice;
	}
	
	public String getCustomerSource() {
		return customerSource;
	}
	public void setCustomerSource(String customerSource) {
		this.customerSource = customerSource;
	}
	public Double getMinAvgPrice() {
		return minAvgPrice;
	}
	public void setMinAvgPrice(Double minAvgPrice) {
		this.minAvgPrice = minAvgPrice;
	}
	public Double getMaxAvgPrice() {
		return maxAvgPrice;
	}
	public void setMaxAvgPrice(Double maxAvgPrice) {
		this.maxAvgPrice = maxAvgPrice;
	}
	public Integer getMinTradeNum() {
		return minTradeNum;
	}
	public void setMinTradeNum(Integer minTradeNum) {
		this.minTradeNum = minTradeNum;
	}
	public Integer getMaxTradeNum() {
		return maxTradeNum;
	}
	public void setMaxTradeNum(Integer maxTradeNum) {
		this.maxTradeNum = maxTradeNum;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getItemIds() {
		return itemIds;
	}
	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}
	public String getTradeStartTime() {
		return tradeStartTime;
	}
	public void setTradeStartTime(String tradeStartTime) {
		this.tradeStartTime = tradeStartTime;
	}
	public String getTradeEndTime() {
		return tradeEndTime;
	}
	public void setTradeEndTime(String tradeEndTime) {
		this.tradeEndTime = tradeEndTime;
	}
	public Integer getSendTime() {
		return sendTime;
	}
	public void setSendTime(Integer sendTime) {
		this.sendTime = sendTime;
	}
	
	public List<String> getProductList() {
		return productList;
	}
	public void setProductList(List<String> productList) {
		this.productList = productList;
	}
	public List<String> getAreaList() {
		return areaList;
	}
	public void setAreaList(List<String> areaList) {
		this.areaList = areaList;
	}
	public List<String> getPhoneList() {
		return phoneList;
	}
	public void setPhoneList(List<String> phoneList) {
		this.phoneList = phoneList;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	
	public Long getStartRows() {
		return startRows;
	}
	public void setStartRows(Long startRows) {
		this.startRows = startRows;
	}
	public Long getPageSize() {
		return pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public String getGroupType() {
		return groupType;
	}
	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}
	
	public String getTradeAmount() {
		return tradeAmount;
	}
	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}
	
	/**
	 * 手机号集合有多个筛选条件在使用
	 * 修改此set方法
	 * **/
	public void appendPhoneList(List<String> phoneList) {
		if(this.phoneList==null){
			this.phoneList = new ArrayList<String>();
			this.phoneList.addAll(phoneList);
		}else{
			this.phoneList = phoneList;
		}
	}
	public String getLastestSend() {
		return lastestSend;
	}
	public void setLastestSend(String lastestSend) {
		this.lastestSend = lastestSend;
	}
	public String getTradeNumByTimes() {
		return tradeNumByTimes;
	}
	public void setTradeNumByTimes(String tradeNumByTimes) {
		this.tradeNumByTimes = tradeNumByTimes;
	}
	public String getTradeTimeByDay() {
		return tradeTimeByDay;
	}
	public void setTradeTimeByDay(String tradeTimeByDay) {
		this.tradeTimeByDay = tradeTimeByDay;
	}
	public Boolean getFull() {
		return full;
	}
	public void setFull(Boolean full) {
		this.full = full;
	}
	@Override
	public String toString() {
		return "MemberCriteriaVo [userId=" + userId + ", groupId=" + groupId + ", blackList=" + blackList
				+ ", evaluation=" + evaluation + ", refund=" + refund + ", sheildRecent=" + sheildRecent
				+ ", minTradePrice=" + minTradePrice + ", maxTradePrice=" + maxTradePrice + ", customerSource="
				+ customerSource + ", minAvgPrice=" + minAvgPrice + ", maxAvgPrice=" + maxAvgPrice + ", minTradeNum="
				+ minTradeNum + ", maxTradeNum=" + maxTradeNum + ", region=" + region + ", buyerName=" + buyerName
				+ ", itemIds=" + itemIds + ", tradeStartTime=" + tradeStartTime + ", tradeEndTime=" + tradeEndTime
				+ ", sendTime=" + sendTime + ", totalCount=" + totalCount + ", groupType=" + groupType
				+ ", lastestSend=" + lastestSend + ", tradeNumByTimes=" + tradeNumByTimes + ", tradeTimeByDay="
				+ tradeTimeByDay + ", tradeAmount=" + tradeAmount + ", productList=" + productList + ", areaList="
				+ areaList + ", phoneList=" + phoneList + ", msgIdList=" + msgIdList + ", type=" + type
				+ ", sendStartTime=" + sendStartTime + ", sendEndTime=" + sendEndTime + ", refundStatus=" + refundStatus
				+ ", blackStatus=" + blackStatus + ", evaluateStatus=" + evaluateStatus + ", orderStatus=" + orderStatus
				+ ", startRows=" + startRows + ", pageSize=" + pageSize + ", full=" + full + "]";
	}
	
	
}
