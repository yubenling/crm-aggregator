package com.kycrm.member.domain.entity.trade;

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/** 
 * 订单保存更新错误日志
* @author wy
* @version 创建时间：2018年1月26日 上午11:59:13
*/
public class TradeErrorMessage extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -6691539313667610575L;

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
     * 要修改的json内容
     */
    private String jdpResponse;
    
    /**
     * true ： 主订单，false ： 子订单
     */
    private boolean isTrade;
    
    /**
     * 错误的日志
     */
    private String errorMessage;

    /**
     * 操作类型
     */
    private String type;
    
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

    public String getJdpResponse() {
        return jdpResponse;
    }

    public void setJdpResponse(String jdpResponse) {
        this.jdpResponse = jdpResponse;
    }

    public boolean getIsTrade() {
        return isTrade;
    }

    public void setIsTrade(boolean isTrade) {
        this.isTrade = isTrade;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    
}
