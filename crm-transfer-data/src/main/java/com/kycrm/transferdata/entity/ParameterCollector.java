package com.kycrm.transferdata.entity;

import java.io.Serializable;
import java.util.Date;

public class ParameterCollector implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年9月5日上午11:17:50
	 */
	private static final long serialVersionUID = 1L;

	// 基础参数 - 迁移数据MongoDB中CollectionName前缀【外部传入】
	private String prefix;
	private String uid;
	// 基础参数 - 每个线程处理的数据量【外部传入】
	private Integer pageSize;
	// 基础参数 - 迁移数据的起始时间【外部传入】
	private Date startDate;
	// 基础参数 - 迁移数据的截止时间【外部传入】
	private Date endDate;

	public ParameterCollector() {
		super();

	}

	public ParameterCollector(String prefix, String uid, Integer pageSize, Date startDate, Date endDate) {
		super();
		this.prefix = prefix;
		this.uid = uid;
		this.pageSize = pageSize;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Override
	public String toString() {
		return "ParameterCollector [prefix=" + prefix + ", uid=" + uid + ", pageSize=" + pageSize + ", startDate="
				+ startDate + ", endDate=" + endDate + "]";
	}

}
