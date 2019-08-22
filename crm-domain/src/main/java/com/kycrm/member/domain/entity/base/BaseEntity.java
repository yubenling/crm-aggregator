package com.kycrm.member.domain.entity.base;

import java.io.Serializable;
import java.util.Date;

public abstract class BaseEntity implements Serializable{
    
    private static final long serialVersionUID = 693468696296687126L;

    /**
     * 主键id
     */
    private Long id;
    
	/**
	 * 版本
	 */
	private Integer version = 0;

	/**
	 * 创建者
	 */
    private String createdBy;

    /**
	 * 创建时间
	 */
    protected Date createdDate;

    /**
	 * 最后修改者
	 */
    private String lastModifiedBy;

    /**
	 * 最后修改时间
	 */
    private Date lastModifiedDate;
    
    /**
     * 分库分表实体对应用户表的主键id
     */
    private Long uid;
    
    private Integer optlock;

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

	public Integer getOptlock() {
		return optlock;
	}

	public void setOptlock(Integer optlock) {
		this.optlock = optlock;
	}

    
}
