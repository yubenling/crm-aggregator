package com.kycrm.member.domain.vo.trade;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/**
 * 订单短信群发页面查询数据的包装类
 */
public class TradeVO implements Serializable{

	private static final long serialVersionUID = 1L;
	//卖家id，取代之前的userId
	@JsonSerialize(using=ToStringSerializer.class)
	private Long uid;
	//买家昵称
	private String buyerNick;
	//主订单id
	@JsonSerialize(using=ToStringSerializer.class)
	private Long tid;
	//子订单id
	private Long oid;
	//订单来源
	private String tradeFrom;
	//订单状态
	private String tradeStatus;
	//订单状态为关闭时，有两种情况TRADE_CLOSED,TRADE_CLOSED_BY_TAOBAO
	private List<String> tradeStatusList;
	//订单退款状态
	private String refundStatus;
	//评价状态
	private String rateStatus;
	//卖家是否已评
	private Boolean sellerRate;
	//买家是否已评
	private Boolean buyerRate;
	//存放订单标识的list
	private List<Integer> sellerFlagList;
	//过滤黑名单
	private Integer filterBlack;
	//过滤中差评
	private Integer filterNeutralBad;
	//订单金额前筛选框
	private Double minPayment;
	//订单金额前筛选框
	private Double maxPayment;
	//下单时间前筛选框字符串
	private String minCreatedTimeStr;
	//下单时间后筛选框字符串
	private String maxCreatedTimeStr;
	//下单时间前筛选框
	private Date minCreatedTime;
	//下单时间后筛选框
	private Date maxCreatedTime;
	//发货时间前筛选框字符串
	private String minConsignTimeStr;
	//发货时间后筛选框字符串
	private String maxConsignTimeStr;
	//发货时间前筛选框
	private Date minConsignTime;
	//发货时间后筛选框
	private Date maxConsignTime;
	//确认时间前筛选框字符串
	private String minEndTimeStr;
	//确认时间后筛选框字符串
	private String maxEndTimeStr;
	//确认时间前筛选框
	private Date minEndTime;
	//确认时间后筛选框
	private Date maxEndTime;
	//指定商品发送或不发送0,1
	private Integer itemIsSend;
	//商品id的字符串
//	private String numIidStr;
	//存放商品id的list
	private List<Long> numIidList;
	//指定地区发送或不发送
	private Integer stateIsSend;
	//存储省份的集合
	private List<String> stateList;
	
	private List<String> cityList;
	//最近发送的idList
	private List<Long> msgIdList;
	//短信过滤天数
	private String smsFileDays;
	//页数
	private Integer pageNo;
	//查询的开始行数
	private Long startRows;
	//查询每页行数
	private Long pageSize;
	//条件查询数据总量
	private Long totalCount;
	//用户昵称
	private String userId;
	//存放不符合条件的订单id的集合
	private List<String> excludeTidList;
	//存放保存在redis的查询条件的key
	private String queryKey;
	//开始付款时间(订单中心效果分析)
	private Date payTimeBegin;
	//结束付款时间(订单中心效果分析)
	private Date payTimeEnd;
	//订单集合(订单中心效果分析)
	private List<String> tidList;
	
	private String cityStr;
	
	private String numIidStr;
	
	private String stateStr;
	
	private String flagStr;
	//拍下订单时段前输入框(字符串)
	private String minCreatedHourStr;
	//拍下订单时段后输入框(字符串)
	private String maxCreatedHourStr;
	//拍下订单时段前输入框(Time)
	private Date minCreatedHour;
	//拍下订单时段后输入框(Time)
	private Date maxCreatedHour;
	
	/*private String[] cityArr;
	
	private String[] numIidArr;
	
	private String[] stateArr;
	
	private String[] flagArr;*/
	
	private Long msgId;
	
	
	private String isTitle;
	
	private String itemTitle;
	
	/**
	 * 订单类型  "step":预售订单；"fixed":一口价
	 */
	private String type;
	
	/**
	 * 预售状态:"预售状态"：FRONT_NOPAID_FINAL_NOPAID-->"定金未付尾款未付";FRONT_PAID_FINAL_NOPAID-->"定金已付尾款未付";FRONT_PAID_FINAL_PAID-->"定金和尾款都付"
	 */
	private String stepTradeStatus;
	
//-------------------------------------------------------
//	private   String tradeStatus ;
//	public List<OrderItem> orderItems;//主订单下，存储子订单的标题，图片路径，单价以及数量的集合
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getBuyerNick() {
		return buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}
	
	public Long getTid() {
		return tid;
	}
	public void setTid(Long tid) {
		this.tid = tid;
	}
	public Long getOid() {
		return oid;
	}
	public void setOid(Long oid) {
		this.oid = oid;
	}
	public String getTradeFrom() {
		return tradeFrom;
	}
	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}
	public String getTradeStatus() {
		return tradeStatus;
	}
	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}
	public List<String> getTradeStatusList() {
		return tradeStatusList;
	}
	public void setTradeStatusList(List<String> tradeStatusList) {
		this.tradeStatusList = tradeStatusList;
	}
	public String getRefundStatus() {
		return refundStatus;
	}
	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}
	public String getRateStatus() {
		return rateStatus;
	}
	public void setRateStatus(String rateStatus) {
		this.rateStatus = rateStatus;
	}
	public Boolean getSellerRate() {
		return sellerRate;
	}
	public void setSellerRate(Boolean sellerRate) {
		this.sellerRate = sellerRate;
	}
	public Boolean getBuyerRate() {
		return buyerRate;
	}
	public void setBuyerRate(Boolean buyerRate) {
		this.buyerRate = buyerRate;
	}
	public List<Integer> getSellerFlagList() {
		return sellerFlagList;
	}
	public void setSellerFlagList(List<Integer> sellerFlagList) {
		this.sellerFlagList = sellerFlagList;
	}
	public Integer getFilterBlack() {
		return filterBlack;
	}
	public void setFilterBlack(Integer filterBlack) {
		this.filterBlack = filterBlack;
	}
	public Integer getFilterNeutralBad() {
		return filterNeutralBad;
	}
	public void setFilterNeutralBad(Integer filterNeutralBad) {
		this.filterNeutralBad = filterNeutralBad;
	}
	public Double getMinPayment() {
		return minPayment;
	}
	public void setMinPayment(Double minPayment) {
		this.minPayment = minPayment;
	}
	public Double getMaxPayment() {
		return maxPayment;
	}
	public void setMaxPayment(Double maxPayment) {
		this.maxPayment = maxPayment;
	}
	public String getMinCreatedTimeStr() {
		return minCreatedTimeStr;
	}
	public void setMinCreatedTimeStr(String minCreatedTimeStr) {
		this.minCreatedTimeStr = minCreatedTimeStr;
	}
	public String getMaxCreatedTimeStr() {
		return maxCreatedTimeStr;
	}
	public void setMaxCreatedTimeStr(String maxCreatedTimeStr) {
		this.maxCreatedTimeStr = maxCreatedTimeStr;
	}
	public Date getMinCreatedTime() {
		return minCreatedTime;
	}
	public void setMinCreatedTime(Date minCreatedTime) {
		this.minCreatedTime = minCreatedTime;
	}
	public Date getMaxCreatedTime() {
		return maxCreatedTime;
	}
	public void setMaxCreatedTime(Date maxCreatedTime) {
		this.maxCreatedTime = maxCreatedTime;
	}
	public String getMinConsignTimeStr() {
		return minConsignTimeStr;
	}
	public void setMinConsignTimeStr(String minConsignTimeStr) {
		this.minConsignTimeStr = minConsignTimeStr;
	}
	public String getMaxConsignTimeStr() {
		return maxConsignTimeStr;
	}
	public void setMaxConsignTimeStr(String maxConsignTimeStr) {
		this.maxConsignTimeStr = maxConsignTimeStr;
	}
	public Date getMinConsignTime() {
		return minConsignTime;
	}
	public void setMinConsignTime(Date minConsignTime) {
		this.minConsignTime = minConsignTime;
	}
	public Date getMaxConsignTime() {
		return maxConsignTime;
	}
	public void setMaxConsignTime(Date maxConsignTime) {
		this.maxConsignTime = maxConsignTime;
	}
	public String getMinEndTimeStr() {
		return minEndTimeStr;
	}
	public void setMinEndTimeStr(String minEndTimeStr) {
		this.minEndTimeStr = minEndTimeStr;
	}
	public String getMaxEndTimeStr() {
		return maxEndTimeStr;
	}
	public void setMaxEndTimeStr(String maxEndTimeStr) {
		this.maxEndTimeStr = maxEndTimeStr;
	}
	public Date getMinEndTime() {
		return minEndTime;
	}
	public void setMinEndTime(Date minEndTime) {
		this.minEndTime = minEndTime;
	}
	public Date getMaxEndTime() {
		return maxEndTime;
	}
	public void setMaxEndTime(Date maxEndTime) {
		this.maxEndTime = maxEndTime;
	}
	public Integer getItemIsSend() {
		return itemIsSend;
	}
	public void setItemIsSend(Integer itemIsSend) {
		this.itemIsSend = itemIsSend;
	}
	public List<Long> getNumIidList() {
		return numIidList;
	}
	public void setNumIidList(List<Long> numIidList) {
		this.numIidList = numIidList;
	}
	public Integer getStateIsSend() {
		return stateIsSend;
	}
	public void setStateIsSend(Integer stateIsSend) {
		this.stateIsSend = stateIsSend;
	}
	public List<String> getStateList() {
		return stateList;
	}
	public void setStateList(List<String> stateList) {
		this.stateList = stateList;
	}
	public List<Long> getMsgIdList() {
		return msgIdList;
	}
	public void setMsgIdList(List<Long> msgIdList) {
		this.msgIdList = msgIdList;
	}
	public String getSmsFileDays() {
		return smsFileDays;
	}
	public void setSmsFileDays(String smsFileDays) {
		this.smsFileDays = smsFileDays;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
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
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public List<String> getExcludeTidList() {
		return excludeTidList;
	}
	public void setExcludeTidList(List<String> excludeTidList) {
		this.excludeTidList = excludeTidList;
	}
	public Date getPayTimeBegin() {
		return payTimeBegin;
	}
	public void setPayTimeBegin(Date payTimeBegin) {
		this.payTimeBegin = payTimeBegin;
	}
	public Date getPayTimeEnd() {
		return payTimeEnd;
	}
	public void setPayTimeEnd(Date payTimeEnd) {
		this.payTimeEnd = payTimeEnd;
	}
	public List<String> getTidList() {
		return tidList;
	}
	public void setTidList(List<String> tidList) {
		this.tidList = tidList;
	}
	public String getQueryKey() {
		return queryKey;
	}
	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}
	public String getCityStr() {
		return cityStr;
	}
	public void setCityStr(String cityStr) {
		this.cityStr = cityStr;
	}
	public List<String> getCityList() {
		return cityList;
	}
	public void setCityList(List<String> cityList) {
		this.cityList = cityList;
	}
	public Long getMsgId() {
		return msgId;
	}
	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}
	public Long getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Long totalCount) {
		this.totalCount = totalCount;
	}
	public String getNumIidStr() {
		return numIidStr;
	}
	public void setNumIidStr(String numIidStr) {
		this.numIidStr = numIidStr;
	}
	public String getStateStr() {
		return stateStr;
	}
	public void setStateStr(String stateStr) {
		this.stateStr = stateStr;
	}
	public String getFlagStr() {
		return flagStr;
	}
	public void setFlagStr(String flagStr) {
		this.flagStr = flagStr;
	}
	public String getMinCreatedHourStr() {
		return minCreatedHourStr;
	}
	public void setMinCreatedHourStr(String minCreatedHourStr) {
		this.minCreatedHourStr = minCreatedHourStr;
	}
	public String getMaxCreatedHourStr() {
		return maxCreatedHourStr;
	}
	public void setMaxCreatedHourStr(String maxCreatedHourStr) {
		this.maxCreatedHourStr = maxCreatedHourStr;
	}
	public Date getMinCreatedHour() {
		return minCreatedHour;
	}
	public void setMinCreatedHour(Date minCreatedHour) {
		this.minCreatedHour = minCreatedHour;
	}
	public Date getMaxCreatedHour() {
		return maxCreatedHour;
	}
	public void setMaxCreatedHour(Date maxCreatedHour) {
		this.maxCreatedHour = maxCreatedHour;
	}
	public String getIsTitle() {
		return isTitle;
	}
	public void setIsTitle(String isTitle) {
		this.isTitle = isTitle;
	}
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public String getStepTradeStatus() {
		return stepTradeStatus;
	}
	public void setStepTradeStatus(String stepTradeStatus) {
		this.stepTradeStatus = stepTradeStatus;
	}
	
}