package com.kycrm.member.domain.vo.order;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class OrdersVo implements Serializable {

	/**
	 * 订单短信群发页面查询数据的包装类
	 */
	private static final long serialVersionUID = 1L;
	private	String stepTradeStatus;//分段付费
	private String receiverDistrict;//所属地区
	private String refundStatus;//退款状态
	private String orderFrom;//订单来源
	private String status;//状态
	private Long numIid;//
	private String oid;//子订单id
	private String tid;//父订单id
	
	private String buyerNick;
	private String startOrderDate;//前发货时间输入框
	private String endOrderDate;//后发货时间输入框
	private String consignTimeBefore;//前发货时间输入框
	private String consignTimeAfter;//后发货时间输入框
	private String paymentBefore;//前订单金额输入框
	private String paymentAfter;//后订单金额输入框
	private String endTimeBefore;//前确认时间输入框
	private String endTimeAfter;//后确认时间输入框
	private String rateStatus;//评价状态
	private String step01;
	private String step02;
	private Integer sellerFlag;//卖家旗帜
	private String receiverState;//省份
	private String sellerFlagStr;//表示卖家标识的字符串
	private Date dateEndTimeBefor;
	private Date dateEndTimeAfter;
	
	//订单时间查询---会员信息
	private String startTime;//开始时间
	private String endTime;//结束时间
	
	
	private List<String> orderList;//存放不符合条件的订单id的集合
	//存放订单标识的list
	private List<Integer> sellerFlagList;
	//存放商品id的list
	private List<Long> numIidList;
	// 最近发送的idList
	private   List<Long> MsgIdList;
	//总记录id
	private long msgId;
	//订单短信群发前台参数
	private   String showMore ;
	private   String numIidStr ;
	private   String tradeStatus ;
	private   String smsFile;//短信过滤天数
	
	private String  userId; //用户 昵称
	
	private String label;//标签页
	
	/*单次查询出的总条数*/
	private long totalCount;  // 条件查询数据总量
	
	public List<OrderItem> orderItems;//主订单下，存储子订单的标题，图片路径，单价以及数量的集合
	private int startRows;
	private int currentRows;
	//订单发送所需参数
	/*查询的开始行数*/
	private Long startRow;
	/*查询的行数*/
	private Long pageSize;
	

	
	
	
	
	public Date getDateEndTimeBefor() {
		return dateEndTimeBefor;
	}
	public void setDateEndTimeBefor(Date dateEndTimeBefor) {
		this.dateEndTimeBefor = dateEndTimeBefor;
	}
	public Date getDateEndTimeAfter() {
		return dateEndTimeAfter;
	}
	public void setDateEndTimeAfter(Date dateEndTimeAfter) {
		this.dateEndTimeAfter = dateEndTimeAfter;
	}
	public Long getStartRow() {
		return startRow;
	}
	public void setStartRow(Long startRow) {
		this.startRow = startRow;
	}
	public Long getPageSize() {
		return pageSize;
	}
	public void setPageSize(Long pageSize) {
		this.pageSize = pageSize;
	}
	public long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<Long> getMsgIdList() {
		return MsgIdList;
	}
	public void setMsgIdList(List<Long> msgIdList) {
		MsgIdList = msgIdList;
	}
	public String getSmsFile() {
		return smsFile;
	}
	public void setSmsFile(String smsFile) {
		this.smsFile = smsFile;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public String getNumIidStr() {
		return numIidStr;
	}
	public void setNumIidStr(String numIidStr) {
		this.numIidStr = numIidStr;
	}
	public String getShowMore() {
		return showMore;
	}
	public void setShowMore(String showMore) {
		this.showMore = showMore;
	}
	public String getTid() {
		return tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

	public List<OrderItem> getOrderItems() {
		return orderItems;
	}
	public void setOrderItems(List<OrderItem> orderItems) {
		this.orderItems = orderItems;
	}
	public String getReceiverDistrict() {
		return receiverDistrict;
	}
	public String getStepTradeStatus() {
		return stepTradeStatus;
	}
	public void setStepTradeStatus(String stepTradeStatus) {
		this.stepTradeStatus = stepTradeStatus;
	}
	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public String getOrderFrom() {
		return orderFrom;
	}
	public void setOrderFrom(String orderFrom) {
		this.orderFrom = orderFrom;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getNumIid() {
		return numIid;
	}
	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}
	public String getOid() {
		return oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}
	public String getBuyerNick() {
		return buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getSellerFlagStr() {
		return sellerFlagStr;
	}
	public void setSellerFlagStr(String sellerFlagStr) {
		this.sellerFlagStr = sellerFlagStr;
	}
	private List<String> stateList;//存储省份的集合
	public List<String> getStateList() {
		return stateList;
	}
	public void setStateList(List<String> stateList) {
		this.stateList = stateList;
	}
	public String getReceiverState() {
		return receiverState;
	}
	public void setReceiverState(String receiverState) {
		this.receiverState = receiverState;
	}
	public Integer getSellerFlag() {
		return sellerFlag;
	}
	public void setSellerFlag(Integer sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	public int getStartRows() {
		return startRows;
	}
	public void setStartRows(int startRows) {
		this.startRows = startRows;
	}
	public int getCurrentRows() {
		return currentRows;
	}
	public void setCurrentRows(int currentRows) {
		this.currentRows = currentRows;
	}
	public String getStartOrderDate() {
		return startOrderDate;
	}
	public void setStartOrderDate(String startOrderDate) {
		this.startOrderDate = startOrderDate;
	}
	public String getEndOrderDate() {
		return endOrderDate;
	}
	public void setEndOrderDate(String endOrderDate) {
		this.endOrderDate = endOrderDate;
	}
	public String getConsignTimeBefore() {
		return consignTimeBefore;
	}
	public void setConsignTimeBefore(String consignTimeBefore) {
		this.consignTimeBefore = consignTimeBefore;
	}
	public String getConsignTimeAfter() {
		return consignTimeAfter;
	}
	public void setConsignTimeAfter(String consignTimeAfter) {
		this.consignTimeAfter = consignTimeAfter;
	}
	public String getPaymentBefore() {
		return paymentBefore;
	}
	public void setPaymentBefore(String paymentBefore) {
		this.paymentBefore = paymentBefore;
	}
	public String getPaymentAfter() {
		return paymentAfter;
	}
	public void setPaymentAfter(String paymentAfter) {
		this.paymentAfter = paymentAfter;
	}
	public String getEndTimeBefore() {
		return endTimeBefore;
	}
	public void setEndTimeBefore(String endTimeBefore) {
		this.endTimeBefore = endTimeBefore;
	}
	public String getEndTimeAfter() {
		return endTimeAfter;
	}
	public void setEndTimeAfter(String endTimeAfter) {
		this.endTimeAfter = endTimeAfter;
	}
	public String getRateStatus() {
		return rateStatus;
	}
	public void setRateStatus(String rateStatus) {
		this.rateStatus = rateStatus;
	}
	public String getStep01() {
		return step01;
	}
	public void setStep01(String step01) {
		this.step01 = step01;
	}
	public String getStep02() {
		return step02;
	}
	public void setStep02(String step02) {
		this.step02 = step02;
	}

	public List<String> getOrderList() {
		return orderList;
	}
	public void setOrderList(List<String> orderList) {
		this.orderList = orderList;
	}

	public List<Integer> getSellerFlagList() {
		return sellerFlagList;
	}
	public void setSellerFlagList(List<Integer> sellerFlagList) {
		this.sellerFlagList = sellerFlagList;
	}

	public List<Long> getNumIidList() {
		return numIidList;
	}
	public void setNumIidList(List<Long> numIidList) {
		this.numIidList = numIidList;
	}
	public long getMsgId() {
		return msgId;
	}
	public void setMsgId(long msgId) {
		this.msgId = msgId;
	}
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	
	
}
