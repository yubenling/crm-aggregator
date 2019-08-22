package com.kycrm.member.domain.vo.tradecenter;

import java.io.Serializable;
import java.util.Date;

//import lombok.Getter;
//import lombok.Setter;
//
//@Getter
//@Setter
public class OrderReminderEffectVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2001306363524526963L;
	private Long id;
	private String userId;
	//催付短信条数
	private Integer smsNum;
	//短信消费金额
	private Double smsMoney;
	//催付订单数
	private Integer targetOrder;
	//催付订单金额
	private Double targetOrderFee;
	//回款订单数
	private Integer earningsOrder;
	//回款金额
	private Double earningOrderFee;
	//订单回款率
	private String earningsOrderRate;
	//回款金额率
	private String earningsMoneyRate;
	//ROI
	private String roi;
	//客户点击量
	private Integer customerClickNum;
	//客户点击率
	private String customerClickRate;
	//页面点击量
	private Integer pageClickNum;
	//分析日期
	private String effectDay;
	//类型(2常规催付、3二次催付、4聚划算催付)
	private String type;
	//分析天数
	private String dayNum;
	//任务名称
	private String taskName;
	//详情日期
	private String dateStr;
	//选中的页数
	private Integer pageNo;
	//总连接量
	private Integer totalLink;
	//搜索时间start
	private Date startEffectTime;
	//搜索时间end
	private Date endEffectTime;
	//true:客户点击
	//false：页面点击
	private Boolean forUser;
	//任务id
	private Long taskId;
	
	
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public Integer getTargetOrder() {
		return targetOrder;
	}
	public void setTargetOrder(Integer targetOrder) {
		this.targetOrder = targetOrder;
	}
	public Double getTargetOrderFee() {
		return targetOrderFee;
	}
	public void setTargetOrderFee(Double targetOrderFee) {
		this.targetOrderFee = targetOrderFee;
	}
	public Integer getEarningsOrder() {
		return earningsOrder;
	}
	public void setEarningsOrder(Integer earningsOrder) {
		this.earningsOrder = earningsOrder;
	}
	public Double getEarningOrderFee() {
		return earningOrderFee;
	}
	public void setEarningOrderFee(Double earningOrderFee) {
		this.earningOrderFee = earningOrderFee;
	}
	public String getEarningsOrderRate() {
		return earningsOrderRate;
	}
	public void setEarningsOrderRate(String earningsOrderRate) {
		this.earningsOrderRate = earningsOrderRate;
	}
	public String getEarningsMoneyRate() {
		return earningsMoneyRate;
	}
	public void setEarningsMoneyRate(String earningsMoneyRate) {
		this.earningsMoneyRate = earningsMoneyRate;
	}
	public String getRoi() {
		return roi;
	}
	public void setRoi(String roi) {
		this.roi = roi;
	}
	public Integer getCustomerClickNum() {
		return customerClickNum;
	}
	public void setCustomerClickNum(Integer customerClickNum) {
		this.customerClickNum = customerClickNum;
	}
	public String getCustomerClickRate() {
		return customerClickRate;
	}
	public void setCustomerClickRate(String customerClickRate) {
		this.customerClickRate = customerClickRate;
	}
	public Integer getPageClickNum() {
		return pageClickNum;
	}
	public void setPageClickNum(Integer pageClickNum) {
		this.pageClickNum = pageClickNum;
	}
	public String getEffectDay() {
		return effectDay;
	}
	public void setEffectDay(String effectDay) {
		this.effectDay = effectDay;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
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
	public String getDateStr() {
		return dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public Integer getTotalLink() {
		return totalLink;
	}
	public void setTotalLink(Integer totalLink) {
		this.totalLink = totalLink;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getStartEffectTime() {
		return startEffectTime;
	}
	public void setStartEffectTime(Date startEffectTime) {
		this.startEffectTime = startEffectTime;
	}
	public Date getEndEffectTime() {
		return endEffectTime;
	}
	public void setEndEffectTime(Date endEffectTime) {
		this.endEffectTime = endEffectTime;
	}
	public Boolean getForUser() {
		return forUser;
	}
	public void setForUser(Boolean forUser) {
		this.forUser = forUser;
	}
	public Long getTaskId() {
		return taskId;
	}
	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}
	
	
}
