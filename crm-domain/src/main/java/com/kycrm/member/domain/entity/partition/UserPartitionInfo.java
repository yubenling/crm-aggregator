package com.kycrm.member.domain.entity.partition;

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/** 
 * 用户分库关系表
* @author wy
* @version 创建时间：2018年1月26日 下午3:26:12
*/
public class UserPartitionInfo extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -7316450081718343656L;

	/**
     * 主键id
     */
    private Long id;
    
    /**
     * 创建时间
     */
    private Date created;
    
    /**
     * 最后修改时间
     */
    private Date lastModified;
    
    /**
     * 用户主键id
     */
    private Long uid;
    
    /**
     * 用户昵称
     */
    private String userNick;
    
    /**
     * 对应用户分库的数据库排序，当前有两个数据库1和2
     */
    private Integer tableNo;
    
    /**
     * 默认 true  （true时 ---新用户分库时，用户主键id%当前数据库总数。false时---新用户分库时，直接取默认主键id为1设置的数据库名）
     */
    private Boolean isHash;
    
    /**
     * 指定分库名   id=1的记录才有该值
     */
    private Integer designTableNo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getLastModified() {
        return lastModified;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified;
    }

    public Long getUid() {
        return uid;
    }

    public void setUid(Long uid) {
        this.uid = uid;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public Integer getTableNo() {
        return tableNo;
    }

    public void setTableNo(Integer tableNo) {
        this.tableNo = tableNo;
    }

    public Boolean getIsHash() {
        return isHash;
    }

    public void setIsHash(Boolean isHash) {
        this.isHash = isHash;
    }

    public Integer getDesignTableNo() {
        return designTableNo;
    }

    public void setDesignTableNo(Integer designTableNo) {
        this.designTableNo = designTableNo;
    }

}
