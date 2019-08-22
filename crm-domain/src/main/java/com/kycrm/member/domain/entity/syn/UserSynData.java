package com.kycrm.member.domain.entity.syn;

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/** 
* @author wy
* @version 创建时间：2018年2月6日 下午2:03:19
*/
public class UserSynData extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 7715186886468750327L;
	/**
     * 用户主键id
     */
    private Long uid;
    /**
     * 用户昵称
     */
    private String userNick;
    
    /**
     * 短信记录是否同步结束
     */
    private Boolean isSmsRecordEnd;
    /**
     * 订单是否同步结束
     */
    private Boolean isTradeEnd;
    /**
     * 会员是否同步结束
     */
    private Boolean isMemberEnd;
    /**
     * 短信记录已同步到的记录数
     */
    private Long smsRecordStartNum;
    /**
     * 订单已同步到的记录数
     */
    private Long tradeStartNum;
    /**
     * 会员已同步到的记录数
     */
    private Long memberStartNum;
    /**
     * 创建时间
     */
    private Date createdDate;
    
    /**
     * 主键id
     */
    private Long id;
    
    /**
     * 最后修改时间
     */
    private Date lastModifedDate;

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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getLastModifedDate() {
        return lastModifedDate;
    }

    public void setLastModifedDate(Date lastModifedDate) {
        this.lastModifedDate = lastModifedDate;
    }

    public Boolean getIsSmsRecordEnd() {
        return isSmsRecordEnd;
    }

    public void setIsSmsRecordEnd(Boolean isSmsRecordEnd) {
        this.isSmsRecordEnd = isSmsRecordEnd;
    }

    public Boolean getIsTradeEnd() {
        return isTradeEnd;
    }

    public void setIsTradeEnd(Boolean isTradeEnd) {
        this.isTradeEnd = isTradeEnd;
    }

    public Boolean getIsMemberEnd() {
        return isMemberEnd;
    }

    public void setIsMemberEnd(Boolean isMemberEnd) {
        this.isMemberEnd = isMemberEnd;
    }

    public Long getSmsRecordStartNum() {
        return smsRecordStartNum;
    }

    public void setSmsRecordStartNum(Long smsRecordStartNum) {
        this.smsRecordStartNum = smsRecordStartNum;
    }

    public Long getTradeStartNum() {
        return tradeStartNum;
    }

    public void setTradeStartNum(Long tradeStartNum) {
        this.tradeStartNum = tradeStartNum;
    }

    public Long getMemberStartNum() {
        return memberStartNum;
    }

    public void setMemberStartNum(Long memberStartNum) {
        this.memberStartNum = memberStartNum;
    }
    
}
