package com.kycrm.member.domain.entity.usermanagement;

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * 卖家分组
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月10日下午3:49:53
 * @Tags
 */
public class SellerGroup extends BaseEntity {

	private static final long serialVersionUID = 3994608067073815545L;

	// 卖家昵称
	private String userId;

	// 分组规则ID
	private Long ruleId;

	// 分组名称
	private String groupName;

	// 分组创建时间
	private Date groupCreate;

	// 分组修改时间
	private Date groupModify;

	// 分组状态（1代表正常; 2代表异常）
	private String status;

	// 会员数量
	private String memberCount;

	// 分组类型（1代表默认分组; 2代表用户添加分组）
	private String groupType;

	// 分组说明
	private String remark;

	// 每个分组的查询key值
	private String queryKey;

	public SellerGroup() {
		super();
	}

	public SellerGroup(Long ruleId, String groupName, Date groupCreate, Date groupModify, String status,
			String memberCount, String groupType, String userId, String remark, String queryKey) {
		super();
		this.ruleId = ruleId;
		this.groupName = groupName;
		this.groupCreate = groupCreate;
		this.groupModify = groupModify;
		this.status = status;
		this.memberCount = memberCount;
		this.groupType = groupType;
		this.userId = userId;
		this.remark = remark;
		this.queryKey = queryKey;
	}

	public Long getRuleId() {
		return ruleId;
	}

	public void setRuleId(Long ruleId) {
		this.ruleId = ruleId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Date getGroupCreate() {
		return groupCreate;
	}

	public void setGroupCreate(Date groupCreate) {
		this.groupCreate = groupCreate;
	}

	public Date getGroupModify() {
		return groupModify;
	}

	public void setGroupModify(Date groupModify) {
		this.groupModify = groupModify;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(String memberCount) {
		this.memberCount = memberCount;
	}

	public String getGroupType() {
		return groupType;
	}

	public void setGroupType(String groupType) {
		this.groupType = groupType;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getQueryKey() {
		return queryKey;
	}

	public void setQueryKey(String queryKey) {
		this.queryKey = queryKey;
	}

	@Override
	public String toString() {
		return "SellerGroup [userId=" + userId + ", ruleId=" + ruleId + ", groupName=" + groupName + ", groupCreate="
				+ groupCreate + ", groupModify=" + groupModify + ", status=" + status + ", memberCount=" + memberCount
				+ ", groupType=" + groupType + ", remark=" + remark + "]";
	}

}
