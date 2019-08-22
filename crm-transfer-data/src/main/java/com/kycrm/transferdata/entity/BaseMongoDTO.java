package com.kycrm.transferdata.entity;

import java.io.Serializable;
import java.util.Date;



 
public class BaseMongoDTO  implements Serializable{

	
    /** 
	 * Project Name:s2jh4net 
	 * File Name:BaseMongoDTO.java 
	 * Package Name:s2jh.biz.shop.crm.manage.entity 
	 * Date:2017年6月6日下午2:56:28 
	 * Copyright (c) 2017,  All Rights Reserved. 
	 */  
	private static final long serialVersionUID = -3360096502419524364L;

	private String createdBy;

	private Date createdDate;
	
	private Date lastModifiedDate;
	
	private Long timestampId;

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Long getTimestampId() {
		return timestampId;
	}

	public void setTimestampId(Long timestampId) {
		this.timestampId = timestampId;
	}
	
	
    
}
