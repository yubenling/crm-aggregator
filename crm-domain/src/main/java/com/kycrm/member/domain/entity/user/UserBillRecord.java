package com.kycrm.member.domain.entity.user;

import java.math.BigDecimal;
import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * 用户发票记录表
 * @author ybl
 *
 */
public class UserBillRecord extends BaseEntity{

	/**
	 * 默认的序列号
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 申请金额
	 * apply_price
	 */
	private BigDecimal applyPrice;
	/**
	 * 申请时间
	 * apply_time
	 */
	private Date  applyTime;
	/**
	 * 发票类型 专票 普票
	 * bill_type
	 */
	private String billType;
	/**
	 * 发票抬头
	 * bill_head
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
	 * 开户银行
	 */
	private String companyBank;
	/**
	 * 银行卡号
	 */
	private String companyCard;
	/**
	 * 收货姓名
	 */
	private String receiverName;
	/**
	 * 联系方式
	 */
	private String receiverPhone;
	/**
	 * 收货地址
	 */
	private String receiverAddress;
	/**
	 * 申请状态   1 受理中 2 成功 3 撤销(直接删除得了)
	 * apply_status
	 */
	private Integer applyStatus;
	/**
	 * 物流运行商
	 * logistical_operator
	 */
	private String logisticalOperator;
	/**
	 * 物流单号
	 * logistical_order_id
	 */
	private String logisticalOrderId;
	
	
	public BigDecimal getApplyPrice() {
		return applyPrice;
	}
	public void setApplyPrice(BigDecimal applyPrice) {
		this.applyPrice = applyPrice;
	}
	public Date getApplyTime() {
		return applyTime;
	}
	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}
	public String getBillType() {
		return billType;
	}
	public void setBillType(String billType) {
		this.billType = billType;
	}
	public String getBillHead() {
		return billHead;
	}
	public void setBillHead(String billHead) {
		this.billHead = billHead;
	}
	public Integer getApplyStatus() {
		return applyStatus;
	}
	public void setApplyStatus(Integer applyStatus) {
		this.applyStatus = applyStatus;
	}
	public String getLogisticalOperator() {
		return logisticalOperator;
	}
	public void setLogisticalOperator(String logisticalOperator) {
		this.logisticalOperator = logisticalOperator;
	}
	public String getLogisticalOrderId() {
		return logisticalOrderId;
	}
	public void setLogisticalOrderId(String logisticalOrderId) {
		this.logisticalOrderId = logisticalOrderId;
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
