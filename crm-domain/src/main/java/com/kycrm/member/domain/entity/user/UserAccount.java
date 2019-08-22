package com.kycrm.member.domain.entity.user;

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;


/** 
* @author wy
* @version 创建时间：2017年11月13日 下午2:40:49
* @Table (name = "CRM_USER_ACCOUNT") 
* @MetaData (value = "用户账户表")
* 用户短信余额表
*/
public class UserAccount extends BaseEntity{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -3453127985426101516L;

	/**
     * @MetaData (value = "淘宝账户对应Id") 
     * @Column (name = "user_id")
     */
    private String userId;
    
    
    /**
     * @MetaData (value = "短信剩余数量") 
     * @Column (name = "sms_num")
     */
    private Long smsNum;
    
    /**
     * 主键
     */
    private Long id;

    /**
     * 乐观锁版本
     */
    private Integer version = 0;

    private String createdBy;

    protected Date createdDate;

    private String lastModifiedBy;

    private Date lastModifiedDate;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getSmsNum() {
        return smsNum;
    }

    public void setSmsNum(Long smsNum) {
        this.smsNum = smsNum;
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
    
    
    
}
