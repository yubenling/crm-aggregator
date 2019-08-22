package com.kycrm.member.domain.vo.user;

import java.io.Serializable;
import java.util.Date;

public class UserPayBillVO  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**分页页码*/
	private Integer pageNo = 1;
	/**起始行数*/
	private Integer startRows;
	/**每页显示条数*/
	private Integer currentRows = 5;
	/**打款开始时间*/
	private Date payStartTime;
	/**
	 * 打款开始时间串
	 */
	private String payStartTimeStr;
	/**
	 * 打款结束时间串
	 */
	private String payEndTimeStr;
	/**打款结束时间*/
	private Date payEndTime;
	/**打款记录状态*/
	private Integer billStatus;
	/**用户id*/
	private Long  uid;
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
	public Date getPayStartTime() {
		return payStartTime;
	}
	public void setPayStartTime(Date payStartTime) {
		this.payStartTime = payStartTime;
	}
	public String getPayStartTimeStr() {
		return payStartTimeStr;
	}
	public void setPayStartTimeStr(String payStartTimeStr) {
		this.payStartTimeStr = payStartTimeStr;
	}
	public String getPayEndTimeStr() {
		return payEndTimeStr;
	}
	public void setPayEndTimeStr(String payEndTimeStr) {
		this.payEndTimeStr = payEndTimeStr;
	}
	public Date getPayEndTime() {
		return payEndTime;
	}
	public void setPayEndTime(Date payEndTime) {
		this.payEndTime = payEndTime;
	}
	public Integer getBillStatus() {
		return billStatus;
	}
	public void setBillStatus(Integer billStatus) {
		this.billStatus = billStatus;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	
}
