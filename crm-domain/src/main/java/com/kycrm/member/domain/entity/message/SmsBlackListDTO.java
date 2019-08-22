package com.kycrm.member.domain.entity.message;

import com.kycrm.member.domain.entity.base.BaseEntity;

/** 
* @author wy
* @version 创建时间：2018年1月9日 下午5:18:01
* @Table(name = "crm_blacklist_dto") 
* @MetaData(value = "短信黑名单")
*/
public class SmsBlackListDTO extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = -5112127923434699003L;

	/**
     * @MetaData(value ="客户昵称/手机号码") 
     * @Column(name="nick_or_phone")
     */
    private String nickOrPhone;
    
    /**
     * @MetaData(value ="客户昵称") 
     * @Column(name="nick")
     */
    private String nick;
    
    /**
     * @MetaData(value ="手机号码") 
     * @Column(name="phone")
     */
    private String phone;
    
    /**
     * @MetaData(value="添加来源 1-单个添加 2-批量添加 3-退订回N/TD5-退款") 
     * @Column(name="add_source")
     */
    private String addSource;
    
    /**
     * @MetaData(value="黑名单类型 1-手机号 2-客户昵称") 
     * @Column(name="type")
     */
    private String type;
    

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getNickOrPhone() {
		return nickOrPhone;
	}

	public void setNickOrPhone(String nickOrPhone) {
		this.nickOrPhone = nickOrPhone;
	}

	public String getAddSource() {
		return addSource;
	}

	public void setAddSource(String addSource) {
		this.addSource = addSource;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
}
