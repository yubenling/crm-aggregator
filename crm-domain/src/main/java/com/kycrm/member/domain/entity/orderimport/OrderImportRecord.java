package com.kycrm.member.domain.entity.orderimport;

import com.kycrm.member.domain.entity.base.BaseEntity;


/**
 * @Table(name = "CRM_ORDER_HISTORY_IMPORT")
 * @MetaData(value ="历史订单导入记录")
 * @author HL
 * @time 2018年5月24日 下午2:51:20
 */

public class OrderImportRecord extends BaseEntity{
	private static final long serialVersionUID = 1L;

	/**
	 * @MetaData(value="用户编号")
	 * @Column(name="user_id")
	 */
	private String userId;
	
	/**
	 * @MetaData(value="导入的订单文件名称")
	 * @Column(name="order_name")
	 */
	private String orderName;
	
	/**
	 * @MetaData(value="总行数")
	 * @Column(name="order_number")
	 */
	private Integer orderNumber;
	
	/**
	 * @MetaData(value="商品文件名称")
	 * @Column(name="commodity_name")
	 */
	private String commodityName;
	/**
	 * @MetaData(value="总行数")
	 * @Column(name="commodity_number")
	 */
	private Integer commodityNumber;
	
	/**
	 * @MetaData(value="处理状态    0 --导入完成   1--导入中  ")
	 * @Column(name="state")
	 */
	private String state;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getOrderName() {
		return orderName;
	}

	public void setOrderName(String orderName) {
		this.orderName = orderName;
	}

	public Integer getOrderNumber() {
		return orderNumber;
	}

	public void setOrderNumber(Integer orderNumber) {
		this.orderNumber = orderNumber;
	}

	public String getCommodityName() {
		return commodityName;
	}

	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}

	public Integer getCommodityNumber() {
		return commodityNumber;
	}

	public void setCommodityNumber(Integer commodityNumber) {
		this.commodityNumber = commodityNumber;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
}
