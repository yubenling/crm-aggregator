package com.kycrm.member.domain.entity.vip;

import java.io.Serializable;
import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * @Table(name = "CRM_VIP_USER")
@MetaData(value = "vip用户免审表")
 * @author zhrt2
 *
 */
public class VipUser extends BaseEntity{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -839190618942130922L;

	/**
     * 主键
     */
    private Long id;

    /**
     * 乐观锁版本
     */
    private Integer version = 0;

    private String createdBy;

    private Date createdDate;

    private String lastModifiedBy;

    private Date lastModifiedDate;
    
    /**
	 * @MetaData (value="用户表id") 
	 * @Column (name="uid")
	 */
	private Long uid;
    
	/**
	 * @MetaData (value = "vip用户名称") 
	 * @Column (name = "VIP_USER_NAME")
	 */
	private String vipUserNick;
	
	/**
	 * @MetaData (value="vip录入用户ID") 
	 * @Column (name="USER_ID")
	 */
	private String userId;

	
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getVipUserNick() {
        return vipUserNick;
    }

    public void setVipUserNick(String vipUserNick) {
        this.vipUserNick = vipUserNick;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
	
	
}
