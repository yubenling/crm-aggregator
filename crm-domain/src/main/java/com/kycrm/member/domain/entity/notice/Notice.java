package com.kycrm.member.domain.entity.notice;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * @Table (name = "crm_notice") 
 * @MetaData (value = "首页-公告")
 */
public class Notice extends BaseEntity{
	private static final long serialVersionUID = -2457904811671049151L;

	private String title;//标题
	
	private String content;//内容

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
