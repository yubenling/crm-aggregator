package com.kycrm.member.domain.vo.usermanagement;

import java.io.Serializable;

/**
 * 会员分组查询条件实体
 * @Author ZhengXiaoChen
 * @Date 2018年7月16日上午11:24:21
 * @Tags
 */
public class SellerGroupSearchVO implements Serializable {

	/**
	 * @Author ZhengXiaoChen
	 * @Date 2018年7月16日上午11:23:50
	 */
	private static final long serialVersionUID = 1L;

	private String groupType;

	private Integer pageNo;

	public SellerGroupSearchVO() {
		super();
	}

	public SellerGroupSearchVO(String memberType, Integer pageNo) {
		super();
		this.groupType = memberType;
		this.pageNo = pageNo;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String memberType) {
		this.groupType = memberType;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	@Override
	public String toString() {
		return "SellerGroupSearcher [groupType=" + groupType + ", pageNo=" + pageNo + "]";
	}

}
