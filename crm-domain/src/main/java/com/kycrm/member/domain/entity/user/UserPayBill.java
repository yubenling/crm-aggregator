package com.kycrm.member.domain.entity.user;

import java.math.BigDecimal;
import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * 用户打款记录
 * @author wufan
 *
 */
public class UserPayBill extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @MetaData (value = "订单编号") 
	 * @Column (name = "order_id")
	 */
	private String orderId;
	/**
	 * 打款时间
	 * 数据库  pay_time
	 */
	private Date payTime;
	/**
	 * 打款金额
	 * 数据库  pay_amount
	 */
	private BigDecimal payAmount;
	/**
	 * 打款银行
	 * 数据库  bank
	 */
	private String bank;
	/**
	 * 是否已经开过发票 默认false
	 * 数据库  bill_status 0没开 1 申请中 2 完成
	 */
	private Integer billStatus;
	 /**
	  * 打款途径(线上充值，公对公，私对公)
	  * 数据库  type
	  */
	private String type;
	/**
	 * 打款记录备注（简约版，在充值的选择中使用）
	 * 数据库  remark
	 */
    private String remark;
    /**
     * 关联发票记录表
     * @return
     */
    private Long billRecordId;
    /**
     * 打款是否完成
     * @return
     */
    private Integer isFinish;
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public Date getPayTime() {
		return payTime;
	}
	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}
	public BigDecimal getPayAmount() {
		return payAmount;
	}
	public void setPayAmount(BigDecimal payAmount) {
		this.payAmount = payAmount;
	}
	public String getBank() {
		return bank;
	}
	public void setBank(String bank) {
		this.bank = bank;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}
	public Long getBillRecordId() {
		return billRecordId;
	}
	public void setBillRecordId(Long billRecordId) {
		this.billRecordId = billRecordId;
	}
	public Integer getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(Integer isFinish) {
		this.isFinish = isFinish;
	}
	
    
}
