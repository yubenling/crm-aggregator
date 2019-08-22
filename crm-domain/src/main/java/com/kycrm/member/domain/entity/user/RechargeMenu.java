package com.kycrm.member.domain.entity.user;

import java.math.BigDecimal;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * @Table(name = "crm_recharge_menu") 
 * @MetaData (value = "收费项目列表")
 */
public class RechargeMenu extends BaseEntity{
	private static final long serialVersionUID = -8775318459243952095L;

	/**
	 * @MetaData (value = "收费项目代码") 
	 * @Column (name = "mid")
	 */
	private String mid;

	/**
	 * @MetaData (value = "父级收费项目代码") 
	 * @Column (name = "super_mid")
	 */
	private String superMid;

	/**
	 * @MetaData (value = "收费项目名称录账号") 
	 * @Column (name = "name")
	 */
	private String name;

	/**
	 * @MetaData (value = "金额") 
	 * @Column (name = "money")
	 */
	private BigDecimal money;

	/**
	 * @MetaData (value = "对应短信数量") 
	 * @Column (name = "num")
	 */
	private Integer num;

	/**
	 * @MetaData (value = "对应短信单价") 
	 * @Column (name = "unit_price")
	 */
	private Double unitPrice;

	
	/**
	 * @MetaData (value = "免费试用:false-否,true-是") 
	 * @Column (name = "is_probational")
	 */
	private boolean isProbational;

	/**
	 * @MetaData (value = ":false-不可订购,true-可订购") 
	 * @Column (name = "status")
	 */
	private boolean status;

	public String getMid() {
		return mid;
	}

	public void setMid(String mid) {
		this.mid = mid;
	}

	public String getSuperMid() {
		return superMid;
	}

	public void setSuperMid(String superMid) {
		this.superMid = superMid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Double getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	public boolean isProbational() {
		return isProbational;
	}

	public void setProbational(boolean isProbational) {
		this.isProbational = isProbational;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
}
