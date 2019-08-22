package com.kycrm.member.domain.entity.user;

import com.kycrm.member.domain.entity.base.BaseEntity;
/**
 * 
 * @author ybl
 * 
 */
public class UserBillInfo extends BaseEntity{

	
	private static final long serialVersionUID = 1L;
	/**
	 * 发票抬头
	 */
	private String billHead;
	/**
	 * 公司税号
	 */
	private String companyDutyNum;
	/**
	 * 公司注册地址
	 */
	private String registerAddress;
	/**
	 * 公司电话
	 */
	private String companyPhone;
	/**
	 * 公司银行
	 */
	private String companyBank;
	/**
	 * 公司卡号
	 */
	private String companyCard;
	/**
	 * 收货人
	 */
	private String receiverName;
	/**
	 * 收货手机
	 */
	private String receiverPhone;
	/**
	 * 收货地址
	 */
	private String receiverAddress;
	
	
	
	
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
