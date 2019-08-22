package com.kycrm.member.domain.entity.item;

import java.io.Serializable;
import java.util.Date;

public class CommodityGrouping implements Serializable {

	private static final long serialVersionUID = 8322917664138285369L;
	
	/**
	 * 乐观锁版本
	 */
    private Integer version = 0;
	
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 用户主键id
     */
    private Long uid;


	/**
	 * 卖家ID
	 */
	private String userId;
	
	/**
	 * 分组名称
	 */
	private String groupName;
	
	/**
	 * 商品数量
	 */
	private Integer commodityNum;
	
	/**
	 * 备注
	 */
	private String remark;
	
	/**
	 * 创建时间
	 */
	private Date cTime;
	
	/**
	 * 分组时选择的关键字
	 */
	private String title;
	
	/**
	 * 当为1时，表示按照关键字搜索来分组
	 */
	private String isTitle;
	
	/**
	 * 修改时间
	 */
	private Date modifyTime;
	
	private String createdBy;

    protected Date createdDate;

    private String lastModifiedBy;

    private Date lastModifiedDate;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public Integer getCommodityNum() {
		return commodityNum;
	}

	public void setCommodityNum(Integer commodityNum) {
		this.commodityNum = commodityNum;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getcTime() {
		return cTime;
	}

	public void setcTime(Date cTime) {
		this.cTime = cTime;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIsTitle() {
		return isTitle;
	}

	public void setIsTitle(String isTitle) {
		this.isTitle = isTitle;
	}
	
	
    
}
