
/** 
* @Title: UserInfo.java 
* @Package com.kycrm.member.domain 
* Copyright: Copyright (c) 2017 
* Company:北京冰点零度科技有限公司 *
* @author zlp 
* @date 2017年12月29日 下午4:15:08 
* @version V1.0
*/
package com.kycrm.member.domain.entity.user; 

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/** 
 * @ClassName: UserInfo  
 * @author zlp 
 * @date 2017年12月29日 下午4:15:08 *  
 */
public class UserInfo extends BaseEntity{

    private static final long serialVersionUID = 1L;
    
        /**
     * 淘宝用户唯一标识(淘宝昵称可变更，此字段作为用户的唯一标识)
     */
    private String openUid;
    
    /**
     * @MetaData (value = "淘宝账户对应Id") 
     * @Column (name = "taobao_user_id")
     */
    private String taobaoUserId;

    /**
     * @MetaData (value = "淘宝子账户对应Id") 
     * @Column (name = "sub_taobao_user_id")
     */
    private String subtaobaoUserId;

    /**
     * @MetaData (value = "创建时间")
     * @Column (name = "create_Time")
     */
    private Date createTime;

    /**
     * @MetaData (value = "状态") 
     * @Column (name = "STATUS")
     */
    private Integer status;

    /**
     * @MetaData (value = "最后一次登录时间")
     * @Column (name = "last_login_date")
     */
    private Date lastLoginDate;

    /**
     * @MetaData (value = "账户过期时间") 
     * @Column (name = "expiration_time")
     */
    private Date expirationTime;

    /**
     * @MetaData (value = "用户手机号") 
     * @Column (name = "mobile")
     */
    private String mobile;

    /**
     * @MetaData (value = "top分配给用户的key") 
     * @Column (name = "appkey")
     */
    private String appkey;

    /**
     * @MetaData (value = "淘宝账号(昵称)") 
     * @Column (name = "taobao_user_nick", length = 200)
     */
    private String taobaoUserNick;

    /**
     * @MetaData (value = "淘宝子账号") 
     * @Column (name = "sub_taobao_user_nick")
     */
    private String subtaobaoUserNick;

    /**
     * @MetaData (value = "修改时间") 
     * @Column (name = "modify_time")
     */
    private Date modifyTime;

    /**
     * @MetaData (value = "邮件剩余数量") 
     * @Column (name = "email_num")
     */
    private Integer emailNum;

    /**
     * @MetaData (value = "卖家用户的sessionKey") 
     * @Column (name = "access_token")
     */
    private String accessToken;
    
    /**
     * @MetaData (value = "卖家用户的QQ号") 
     * @Column (name = "qq_num")
     */
    private String qqNum;
    
    /**
     * @MetaData (value = "店铺名称（短信签名）") 
     * @Column (name = "shop_name")
     */
    private String shopName;
    
    /**
     * 短信余额
     */
    private Long userAccountSms;
    
    /**
     * 是否赠过送客户500条短信
     * true:是
     * false:没有/且用户不希望在展示首页小红包
     * null:没有
     * @MetaData (value = "是否赠送500条短信") 
     * @Column (name = "has_provide")
     * */
    private Boolean hasProvide;

    /**
     * token过期时间 秒
     */
    private Long expirationSecs;
    
    /**
     * @MetaData (value = "用户等级，分为20个级别")  
     * @Column (name = "level")
     */
    private Long level;
    
    /**
     * @MetaData (value = "用户设置的首页展示4个快接入口用","拼接")
     * @Column (name = "shortcutLinkStr")
     */
    private String shortcutLinkStr;

    
	public String getTaobaoUserId() {
		return taobaoUserId;
	}

	public void setTaobaoUserId(String taobaoUserId) {
		this.taobaoUserId = taobaoUserId;
	}

	public String getSubtaobaoUserId() {
		return subtaobaoUserId;
	}

	public void setSubtaobaoUserId(String subtaobaoUserId) {
		this.subtaobaoUserId = subtaobaoUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getLastLoginDate() {
		return lastLoginDate;
	}

	public void setLastLoginDate(Date lastLoginDate) {
		this.lastLoginDate = lastLoginDate;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAppkey() {
		return appkey;
	}

	public void setAppkey(String appkey) {
		this.appkey = appkey;
	}

	public String getTaobaoUserNick() {
		return taobaoUserNick;
	}

	public void setTaobaoUserNick(String taobaoUserNick) {
		this.taobaoUserNick = taobaoUserNick;
	}

	public String getSubtaobaoUserNick() {
		return subtaobaoUserNick;
	}

	public void setSubtaobaoUserNick(String subtaobaoUserNick) {
		this.subtaobaoUserNick = subtaobaoUserNick;
	}

	public Date getModifyTime() {
		return modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public Integer getEmailNum() {
		return emailNum;
	}

	public void setEmailNum(Integer emailNum) {
		this.emailNum = emailNum;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getQqNum() {
		return qqNum;
	}

	public void setQqNum(String qqNum) {
		this.qqNum = qqNum;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Long getUserAccountSms() {
		return userAccountSms;
	}

	public void setUserAccountSms(Long userAccountSms) {
		this.userAccountSms = userAccountSms;
	}

	public Boolean getHasProvide() {
		return hasProvide;
	}

	public void setHasProvide(Boolean hasProvide) {
		this.hasProvide = hasProvide;
	}

	public Long getExpirationSecs() {
		return expirationSecs;
	}

	public void setExpirationSecs(Long expirationSecs) {
		this.expirationSecs = expirationSecs;
	}

    public String getOpenUid() {
        return openUid;
    }

    public void setOpenUid(String openUid) {
        this.openUid = openUid;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

	public String getShortcutLinkStr() {
		return shortcutLinkStr;
	}

	public void setShortcutLinkStr(String shortcutLinkStr) {
		this.shortcutLinkStr = shortcutLinkStr;
	}
}
