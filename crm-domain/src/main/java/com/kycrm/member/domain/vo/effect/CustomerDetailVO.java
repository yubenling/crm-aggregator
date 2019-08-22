package com.kycrm.member.domain.vo.effect;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class CustomerDetailVO implements Serializable{

	private static final long serialVersionUID = 1L;

	/*
	 * 订单来源(TAOBAO、WAP,WAP、TOTAL)
	 */
	private String orderSource;
	
	private String buyerType;

	/*
	 * 页面设置的排序规则(订单金额payment,订单数tradeNum,商品数num)
	 */
	private String sortData;
	
	/*
	 * 页面排序方式(升序ASC,降序DESC)
	 */
	private String sortType;
	
	private Date sendCreated;
	
	/*
	 * 商品id
	 */
	private Long itemId;
	
	private Long uid;
	
	private Long msgId;
	
	private String tradeStatus;
	
	private String buyerNick;//买家昵称
	private String receiverName;//买家姓名
	private String phone;//电话号
	private String memberlevel;//会员等级
	private Double totalOrderMoney;//订单金额
	private Integer tradeNum;//订单数
	private Integer itemNum;//商品数
	private String receiverAddress;//地址
	private Integer startRows;//起始行数
	private Integer currentRows;//每次查询行数
	private Integer pageNo;//页数
	private Integer totalCount;//总记录数
	
	private Double successMoney;//成交总金额
	private Integer successOrderNum;//成交订单数
	private Integer successItemNum;//成交商品数
	
	private Double waitMoney;//未付款总金额
	private Integer waitOrderNum;//未付款订单数
	private Integer waitItemNum;//未付款商品数
	
	private Double failMoney;//退款总金额
	private Integer failOrderNum;//退款订单数
	private Long failItemNum;//退款商品数
	
	private Date bTime;
	
	private Date eTime;
	
	private List<Long> itemIdList;
	private List<String> orderSourceList;
	
	/**
	 * 查询日期后输入框
	 */
	private String eTimeStr;
	
	/**
	 * 预售状态
	 */
	private String stepTradeStatus;
	
	/**
	 * 是否预售；1：非预售；2：预售
	 */
	private Integer stepType;
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
	public String getMemberlevel() {
		return memberlevel;
	}
	public void setMemberlevel(String memberlevel) {
		this.memberlevel = memberlevel;
	}
	public Double getTotalOrderMoney() {
		return totalOrderMoney;
	}
	public void setTotalOrderMoney(Double totalOrderMoney) {
		this.totalOrderMoney = totalOrderMoney;
	}
	
	public Integer getTradeNum() {
		return tradeNum;
	}
	public void setTradeNum(Integer tradeNum) {
		this.tradeNum = tradeNum;
	}
	public Integer getItemNum() {
		return itemNum;
	}
	public void setItemNum(Integer itemNum) {
		this.itemNum = itemNum;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public Double getSuccessMoney() {
		return successMoney;
	}
	public void setSuccessMoney(Double successMoney) {
		this.successMoney = successMoney;
	}
	public Integer getSuccessOrderNum() {
		return successOrderNum;
	}
	public void setSuccessOrderNum(Integer successOrderNum) {
		this.successOrderNum = successOrderNum;
	}
	public Integer getSuccessItemNum() {
		return successItemNum;
	}
	public void setSuccessItemNum(Integer successItemNum) {
		this.successItemNum = successItemNum;
	}
	public Double getWaitMoney() {
		return waitMoney;
	}
	public void setWaitMoney(Double waitMoney) {
		this.waitMoney = waitMoney;
	}
	public Integer getWaitOrderNum() {
		return waitOrderNum;
	}
	public void setWaitOrderNum(Integer waitOrderNum) {
		this.waitOrderNum = waitOrderNum;
	}
	public Integer getWaitItemNum() {
		return waitItemNum;
	}
	public void setWaitItemNum(Integer waitItemNum) {
		this.waitItemNum = waitItemNum;
	}
	public Double getFailMoney() {
		return failMoney;
	}
	public void setFailMoney(Double failMoney) {
		this.failMoney = failMoney;
	}
	public Integer getFailOrderNum() {
		return failOrderNum;
	}
	public void setFailOrderNum(Integer failOrderNum) {
		this.failOrderNum = failOrderNum;
	}
	public Long getFailItemNum() {
		return failItemNum;
	}
	public void setFailItemNum(Long failItemNum) {
		this.failItemNum = failItemNum;
	}
	public List<Long> getItemIdList() {
		return itemIdList;
	}
	public void setItemIdList(List<Long> itemIdList) {
		this.itemIdList = itemIdList;
	}
	public List<String> getOrderSourceList() {
		return orderSourceList;
	}
	public void setOrderSourceList(List<String> orderSourceList) {
		this.orderSourceList = orderSourceList;
	}
	
	public Long getItemId() {
		return itemId;
	}
	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}
	public Integer getStartRows() {
		return startRows;
	}
	public void setStartRows(Integer startRows) {
		this.startRows = startRows;
	}
	public Integer getCurrentRows() {
		return currentRows;
	}
	public void setCurrentRows(Integer currentRows) {
		this.currentRows = currentRows;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public Date getbTime() {
		return bTime;
	}
	public void setbTime(Date bTime) {
		this.bTime = bTime;
	}
	public Date geteTime() {
		return eTime;
	}
	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}
	public Date getSendCreated() {
		return sendCreated;
	}
	public void setSendCreated(Date sendCreated) {
		this.sendCreated = sendCreated;
	}
	public String getBuyerType() {
		return buyerType;
	}
	public void setBuyerType(String buyerType) {
		this.buyerType = buyerType;
	}
	public String getOrderSource() {
		return orderSource;
	}
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}
	public String getSortData() {
		return sortData;
	}
	public void setSortData(String sortData) {
		this.sortData = sortData;
	}
	public String getSortType() {
		return sortType;
	}
	public void setSortType(String sortType) {
		this.sortType = sortType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getStepTradeStatus() {
		return stepTradeStatus;
	}
	public void setStepTradeStatus(String stepTradeStatus) {
		this.stepTradeStatus = stepTradeStatus;
	}
	public Integer getStepType() {
		return stepType;
	}
	public void setStepType(Integer stepType) {
		this.stepType = stepType;
	}
	public String geteTimeStr() {
		return eTimeStr;
	}
	public void seteTimeStr(String eTimeStr) {
		this.eTimeStr = eTimeStr;
	}
	
}
