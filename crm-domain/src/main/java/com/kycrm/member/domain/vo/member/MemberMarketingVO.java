package com.kycrm.member.domain.vo.member;

import java.io.Serializable;

/**
 * 会员分组-会员营销参数
 * @Author ZhengXiaoChen
 * @Date 2018年7月17日下午2:33:54
 * @Tags
 */
public class MemberMarketingVO implements Serializable{

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月17日下午2:33:51
	 */
	private static final long serialVersionUID = 1L;

	private String groupId;

	private String groupType;

	private String pageNo;

	public MemberMarketingVO() {
		super();

	}

	public MemberMarketingVO(String groupId, String groupType, String pageNo) {
		super();
		this.groupId = groupId;
		this.groupType = groupType;
		this.pageNo = pageNo;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getPageNo() {
		return pageNo;
	}

	public void setPageNo(String pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return "MemberMarketingVO [groupId=" + groupId + ", groupType=" + groupType + ", pageNo=" + pageNo + "]";
	}

}
