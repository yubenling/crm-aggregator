package com.kycrm.member.domain.entity.user;

import com.kycrm.member.domain.entity.base.BaseEntity;


/**
 * 用户昵称主键  reids 缓存实体类
 * @author zhrt2
 *
 */
public class UserTableCache  extends BaseEntity{

	/** 
	 * Project Name:s2jh4net 
	 * File Name:UserTableDTO.java 
	 * Package Name:s2jh.biz.shop.crm.manage.entity 
	 * Date:2017年5月31日下午3:34:07 
	 * Copyright (c) 2017,  All Rights Reserved. 
	 * 
	 */  
	private static final long serialVersionUID = -6648700920058450701L;

	 
	private String userNickName;
	
	private String userId;
	
	private Long dataCount;
	

	public String getUserNickName() {
		return userNickName;
	}

	public void setUserNickName(String userNickName) {
		this.userNickName = userNickName;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getDataCount() {
		return dataCount;
	}

	public void setDataCount(Long dataCount) {
		this.dataCount = dataCount;
	}
}
