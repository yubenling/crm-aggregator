package com.kycrm.member.domain.vo.usermanagement;

import java.io.Serializable;

public class UpdateSellerGroupCountVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月16日下午3:58:06
	 */
	private static final long serialVersionUID = 1L;

	private Long groupId;

	public UpdateSellerGroupCountVO() {
		super();
	}

	public UpdateSellerGroupCountVO(Long groupId) {
		super();
		this.groupId = groupId;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "UpdateSellerGroupCountVO [groupId=" + groupId + "]";
	}

}
