package com.kycrm.member.domain.vo.effect;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


public class TradeCenterEffectVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5958837639223002974L;

	private Long id;
	
	//卖家的id(取代之前的userId)
	private Long uid;
	
	//卖家昵称
	private String userId;
	
	//订单id
	private String tid;
	
	//短信类型(2常规催付、3二次催付、4聚划算催付)
	private String type;
	
	//活动名称")
	private Long taskId;
	
	//发送短信订单数")
	private Integer targetOrder;
	
	//发送短信订单数总金额
	private Double targetFee;
	
	//付款订单数
	private Integer earningOrder;
	
	//付款订单总金额
	private Double earningFee;

	//发送短信条数
	private Integer smsNum;
	
	//发送短信金额
	private Double smsMoney;
	
	//短信内链接数
	private Integer linkNum;
	
	//客户点击数
	private Integer customerClick;
	
	//页面点击数
	private Integer pageClick;
	
	//分析时间
	private Date effectTime;
	
	//分析详情时间
	private String effectTimeStr;
	
	//付款订单率
	private String earningOrderRate;
	
	//付款订单金额率
	private String earningMoneyRate;
	
	//客户点击率
	private String clickRate;
	
	//ROI
	private String roiValue;
	
	//开始时间
	private Date beginTime;
	
	//结束时间
	private Date endTime;
	
	//客户选择的分析天数
	private String dayNum;
	
	//任务名称
	private String taskName;
	
	//选中的页数
	private Integer pageNo;
	
	//true:客户点击;false：页面点击
	private Boolean forUser;
	
	private List<String> typeList;
	
	private String createdBy;
	
	private Date createdDate;
	
	private String lastModifiedBy;
	
	private Date lastModifiedDate;
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Integer getTargetOrder() {
		return targetOrder;
	}

	public void setTargetOrder(Integer targetOrder) {
		this.targetOrder = targetOrder;
	}

	public Double getTargetFee() {
		return targetFee;
	}

	public void setTargetFee(Double targetFee) {
		this.targetFee = targetFee;
	}

	public Integer getEarningOrder() {
		return earningOrder;
	}

	public void setEarningOrder(Integer earningOrder) {
		this.earningOrder = earningOrder;
	}

	public Double getEarningFee() {
		return earningFee;
	}

	public void setEarningFee(Double earningFee) {
		this.earningFee = earningFee;
	}

	public Integer getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(Integer smsNum) {
		this.smsNum = smsNum;
	}

	public Double getSmsMoney() {
		return smsMoney;
	}

	public void setSmsMoney(Double smsMoney) {
		this.smsMoney = smsMoney;
	}

	public Integer getLinkNum() {
		return linkNum;
	}

	public void setLinkNum(Integer linkNum) {
		this.linkNum = linkNum;
	}

	public Integer getCustomerClick() {
		return customerClick;
	}

	public void setCustomerClick(Integer customerClick) {
		this.customerClick = customerClick;
	}

	public Integer getPageClick() {
		return pageClick;
	}

	public void setPageClick(Integer pageClick) {
		this.pageClick = pageClick;
	}

	public Date getEffectTime() {
		return effectTime;
	}

	public void setEffectTime(Date effectTime) {
		this.effectTime = effectTime;
	}

	public String getEffectTimeStr() {
		return effectTimeStr;
	}

	public void setEffectTimeStr(String effectTimeStr) {
		this.effectTimeStr = effectTimeStr;
	}

	public String getEarningOrderRate() {
		return earningOrderRate;
	}

	public void setEarningOrderRate(String earningOrderRate) {
		this.earningOrderRate = earningOrderRate;
	}

	public String getEarningMoneyRate() {
		return earningMoneyRate;
	}

	public void setEarningMoneyRate(String earningMoneyRate) {
		this.earningMoneyRate = earningMoneyRate;
	}

	public String getClickRate() {
		return clickRate;
	}

	public void setClickRate(String clickRate) {
		this.clickRate = clickRate;
	}

	public String getRoiValue() {
		return roiValue;
	}

	public void setRoiValue(String roiValue) {
		this.roiValue = roiValue;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDayNum() {
		return dayNum;
	}

	public void setDayNum(String dayNum) {
		this.dayNum = dayNum;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Boolean getForUser() {
		return forUser;
	}

	public void setForUser(Boolean forUser) {
		this.forUser = forUser;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getTid() {
		return tid;
	}

	public void setTid(String tid) {
		this.tid = tid;
	}

	public List<String> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<String> typeList) {
		this.typeList = typeList;
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
	
	
}
