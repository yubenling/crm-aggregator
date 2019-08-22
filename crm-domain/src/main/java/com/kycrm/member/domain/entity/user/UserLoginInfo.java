package com.kycrm.member.domain.entity.user;

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/** 
 * @Title: 用户信息登录表
 * @date 2017年4月20日--上午10:46:10 
 * @param     设定文件 
 * @return 返回类型 
 * @throws 
 */

/**
 * @Table(name = "CRM_UserLoginInfo") 
 * @MetaData(value = "用户信息登录表")
 * @author qy
 *
 */
public class UserLoginInfo extends BaseEntity{
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -7358742471017165219L;

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
    private Date createdDate;

    /**
     * 最后修改者
     */
    private String lastModifiedBy;

    /**
     * 最后修改时间
     */
    private Date lastModifiedDate;
    
	/**
	 * @MetaData (value = "卖家用户名") 
	 * @Column (name = "sellerNick")
	 */
	private String sellerNick;
	
	/**
	 *用户主键id
	 */
	private Long uid;
	
	/**
	 * @MetaData (value = "登录时间") 
	 * @Column (name = "loginTime")
	 */
	private Date loginTime;
	
	/**
	 * @MetaData (value = "登录IP地址") 
	 * @Column (name = "IpAddress")
	 */
	private String ipAddress;

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }
	
	
}
