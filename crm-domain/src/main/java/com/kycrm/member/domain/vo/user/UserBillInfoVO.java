package com.kycrm.member.domain.vo.user;

import java.io.Serializable;

public class UserBillInfoVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id; //用户接受更新id
	private Integer computerSum; //0 不需要 1 需要
	private Long   uid;
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
	public Integer getComputerSum() {
		return computerSum;
	}
	public void setComputerSum(Integer computerSum) {
		this.computerSum = computerSum;
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

}
