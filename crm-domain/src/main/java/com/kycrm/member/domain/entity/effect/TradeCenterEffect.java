package com.kycrm.member.domain.entity.effect;


import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

public class TradeCenterEffect extends BaseEntity {

	
	/** 
	 * @Fields serialVersionUID : TODO（订单中心效果分析）
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String userId;
	
	private String type;
	
	private Long taskId;
	
	private Integer targetOrder;
	
	private Double targetFee;
	
	private Integer earningOrder;
	
	private Double earningFee;

	private Integer smsNum;
	
	private Double smsMoney;
	
	private Integer linkNum;
	
	private Integer customerClick;
	
	private Integer pageClick;
	
	private Date effectTime;
	
	private String effectTimeStr;
	
	private String earningOrderRate;
	
	private String earningMoneyRate;
	
	private String clickRate;
	
	private String roiValue;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	

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
	

	
}
