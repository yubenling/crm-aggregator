package com.kycrm.member.domain.vo.usermanagement;

import java.io.Serializable;

public class SellerGroupUpdateSearchVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月26日上午11:41:57
	 */
	private static final long serialVersionUID = 1L;

	//分组ID
	private String groupId;

	public SellerGroupUpdateSearchVO() {
		super();

	}

	public SellerGroupUpdateSearchVO(String groupId) {
		super();
		this.groupId = groupId;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "SellerGroupUpdateSearchVO [groupId=" + groupId + "]";
	}

}
