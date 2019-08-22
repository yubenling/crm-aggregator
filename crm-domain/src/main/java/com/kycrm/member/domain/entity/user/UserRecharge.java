package com.kycrm.member.domain.entity.user;

import java.io.Serializable;
import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * @Table(name = "CRM_USER_RECHARGE_RECORD")
 * @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
 * @MetaData (value = "用户充值记录表")
 */
public class UserRecharge extends BaseEntity implements Serializable {
	private static final long serialVersionUID = -2102063905871911009L;

	/**
	 * @MetaData (value = "用户ID")
	 * @Column (name = "user_nick", nullable = false)
	 */
	private String userNick;

	/**
	 * @MetaData (value = "充值金额")
	 * @Column (name = "recharge_price", nullable = false)
	 */
	private double rechargePrice;

	/**
	 * @MetaData (value = "充值类型 1-支付宝 2-微信 3-银行卡4-oa系统充值5-OA补短信 6-店铺获赠")
	 * @Column (name = "recharge_type")
	 */
	private String rechargeType;

	/**
	 * @MetaData (value = "单价")
	 * @Column (name = "unit_price")
	 */
	private String unitPrice;

	/**
	 * @MetaData (value = "充值时间")
	 * @Column (name = "recharge_date")
	 */
	private Date rechargeDate;

	private String rechargeDateStr;

	/**
	 * @MetaData (value = "充值状态 1-成功 2-失败 3-待付款")
	 * @Column (name = "status")
	 */
	private String status;

	/**
	 * @MetaData (value = "备注")
	 * @Column (name = "remarks")
	 */
	private String remarks;

	/**
	 * @MetaData (value = "订单编号")
	 * @Column (name = "order_id")
	 */
	private String orderId;

	/**
	 * @MetaData (value = "充值数量")
	 * @Column (name = "recharge_num")
	 */
	private Integer rechargeNum;

	/**
	 * @MetaData (value = "充值后短信数量")
	 * @Column (name = "recharge_later_num")
	 */
	private Long rechargeLaterNum;
	/**
	 * 充值之前短信条数
	 * @return
	 */
	private Integer rechargeBeforeNum;

    /**
     * 所属打款记录id
     * @return
     */
    private String pid;
	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public double getRechargePrice() {
		return rechargePrice;
	}

	public void setRechargePrice(double rechargePrice) {
		this.rechargePrice = rechargePrice;
	}

	public String getRechargeType() {
		return rechargeType;
	}

	public void setRechargeType(String rechargeType) {
		this.rechargeType = rechargeType;
	}

	public String getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(String unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Date getRechargeDate() {
		return rechargeDate;
	}

	public void setRechargeDate(Date rechargeDate) {
		this.rechargeDate = rechargeDate;
	}

	public String getRechargeDateStr() {
		return rechargeDateStr;
	}

	public void setRechargeDateStr(String rechargeDateStr) {
		this.rechargeDateStr = rechargeDateStr;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public Integer getRechargeNum() {
		return rechargeNum;
	}

	public void setRechargeNum(Integer rechargeNum) {
		this.rechargeNum = rechargeNum;
	}

	public Long getRechargeLaterNum() {
		return rechargeLaterNum;
	}

	public void setRechargeLaterNum(Long rechargeLaterNum) {
		this.rechargeLaterNum = rechargeLaterNum;
	}

	public Integer getRechargeBeforeNum() {
		return rechargeBeforeNum;
	}

	public void setRechargeBeforeNum(Integer rechargeBeforeNum) {
		this.rechargeBeforeNum = rechargeBeforeNum;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}
	
}