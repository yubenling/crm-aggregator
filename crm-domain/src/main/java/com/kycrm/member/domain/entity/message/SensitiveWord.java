package com.kycrm.member.domain.entity.message;

import com.kycrm.member.domain.entity.base.BaseEntity;
/**
 * @Table (name = "crm_antistop_shield") 
 * @MetaData (value = "发短信-敏感词屏蔽")
 */
public class SensitiveWord extends BaseEntity{
	private static final long serialVersionUID = 655572040569758719L;
	
	private String content;//敏感词

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
