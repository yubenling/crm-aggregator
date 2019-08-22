package com.kycrm.member.domain.vo.user;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class UserBillRecordVO implements Serializable{
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long  id;
	/**分页页码*/
	private Integer pageNo = 1;
	/**起始行数*/
	private Integer startRows;
	/**每页显示条数*/
	private Integer currentRows = 5;
	/**
	 * 用户id
	 */
	private Long uid;
	/**
	 * 申请开始时间
	 */
	private Date applyStartTime;
	/**
	 * 申请开始时间串
	 */
	private String applyStartTimeStr;
	/**
	 * 申请结束时间
	 */
	private Date applyEndTime;
	/**
	 * 申请结束时间串
	 */
	private String applyEndTimeStr;
	/**
	 * 发票类型
	 */
	private String billType;
	/**
	 * 累计金额(该发票所有打款记录的累计总额)
	 */
	private BigDecimal applyPrice;
	/**
	 * 发票状态()
	 */
	private String billStatus;
	
	//申请发票记录  1 表示是一键申请全部记录发票 2 表示一个或者多个记录
	private String applyStyle;
	//申请发票的订单号集合，以,分隔
	private String orderIdList;
	private String userName;
	private String billHead;
	private String companyDutyNum;
	private String registerAddress;
	private String companyPhone;
	private String companyBank;
	private String companyCard;
	private String receiverName;
	private String receiverPhone;
	private String receiverAddress;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
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
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public Date getApplyStartTime() {
		return applyStartTime;
	}
	public void setApplyStartTime(Date applyStartTime) {
		this.applyStartTime = applyStartTime;
	}
	public String getApplyStartTimeStr() {
		return applyStartTimeStr;
	}
	public void setApplyStartTimeStr(String applyStartTimeStr) {
		this.applyStartTimeStr = applyStartTimeStr;
	}
	public Date getApplyEndTime() {
		return applyEndTime;
	}
	public void setApplyEndTime(Date applyEndTime) {
		this.applyEndTime = applyEndTime;
	}
	public String getApplyEndTimeStr() {
		return applyEndTimeStr;
	}
	public void setApplyEndTimeStr(String applyEndTimeStr) {
		this.applyEndTimeStr = applyEndTimeStr;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(String billStatus) {
		this.billStatus = billStatus;
	}
	public String getApplyStyle() {
		return applyStyle;
	}
	public void setApplyStyle(String applyStyle) {
		this.applyStyle = applyStyle;
	}
	public String getOrderIdList() {
		return orderIdList;
	}
	public void setOrderIdList(String orderIdList) {
		this.orderIdList = orderIdList;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getBillHead() {
		return billHead;
	}
	public void setBillHead(String billHead) {
		this.billHead = billHead;
	}
	public String getCompanyDutyNum() {
		return companyDutyNum;
	}
	public void setCompanyDutyNum(String companyDutyNum) {
		this.companyDutyNum = companyDutyNum;
	}
	public String getRegisterAddress() {
		return registerAddress;
	}
	public void setRegisterAddress(String registerAddress) {
		this.registerAddress = registerAddress;
	}
	public String getCompanyPhone() {
		return companyPhone;
	}
	public void setCompanyPhone(String companyPhone) {
		this.companyPhone = companyPhone;
	}
	public String getCompanyBank() {
		return companyBank;
	}
	public void setCompanyBank(String companyBank) {
		this.companyBank = companyBank;
	}
	public String getCompanyCard() {
		return companyCard;
	}
	public void setCompanyCard(String companyCard) {
		this.companyCard = companyCard;
	}
	public String getReceiverName() {
		return receiverName;
	}
	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}
	public String getReceiverPhone() {
		return receiverPhone;
	}
	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}
	public String getReceiverAddress() {
		return receiverAddress;
	}
	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}
	public BigDecimal getApplyPrice() {
		return applyPrice;
	}
	public void setApplyPrice(BigDecimal applyPrice) {
		this.applyPrice = applyPrice;
	}
	
}
