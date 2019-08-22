package com.kycrm.member.domain.vo.usermanagement;

import java.io.Serializable;

/**
 * 会员分组删除操作实体
 * @Author ZhengXiaoChen
 * @Date 2018年7月16日上午11:37:11
 * @Tags
 */
public class SellerGroupDeleteVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月16日上午11:37:08
	 */
	private static final long serialVersionUID = 1L;

	private String  groupId;
	
	private String ruleId;

	public SellerGroupDeleteVO() {
		super();
	}

	public SellerGroupDeleteVO(String groupId, String ruleId) {
		super();
		this.groupId = groupId;
		this.ruleId = ruleId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getRuleId() {
		return ruleId;
	}

	public void setRuleId(String ruleId) {
		this.ruleId = ruleId;
	}

	@Override
	public String toString() {
		return "SellerGroupDeleteVO [groupId=" + groupId + ", ruleId=" + ruleId + "]";
	}
	
	
	
}
