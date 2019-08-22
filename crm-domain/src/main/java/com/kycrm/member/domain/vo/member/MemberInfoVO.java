
/** 
* @Title: UserInfo.java 
* @Package com.kycrm.member.domain 
* Copyright: Copyright (c) 2017 
* Company:北京冰点零度科技有限公司 *
* @author zlp 
* @date 2017年12月29日 下午4:15:08 
* @version V1.0
*/
   package com.kycrm.member.domain.vo.member; 

import java.io.Serializable;
import java.util.Date;

/** 
 * @ClassName: MemberInfoVO
 * @author zlp 
 * @date 2017年12月29日 下午4:15:08 *  
 */
public class MemberInfoVO implements Serializable{
	
	/** 
	* @Fields serialVersionUID : （用一句话描述这个变量表示什么）
	*/
	private static final long serialVersionUID = -7273041575629430461L;

	private Long id;
	
	private Long uid;

	private String userId;
	
	private String taobao_user_nick;
	
	private String buyerNick;
	
	private Date beginTime;
	
	private Date endTime;

	/** 
	 * getter method 
	 * @return the id 
	 */
	
	public Long getId() {
		return id;
	}

	/**
	 * setter method
	 * @param id the id to set
	 */
	
	public void setId(Long id) {
		this.id = id;
	}

	/** 
	 * getter method 
	 * @return the userId 
	 */
	
	public String getUserId() {
		return userId;
	}

	/**
	 * setter method
	 * @param userId the userId to set
	 */
	
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/** 
	 * getter method 
	 * @return the taobao_user_nick 
	 */
	
	public String getTaobao_user_nick() {
		return taobao_user_nick;
	}

	/**
	 * setter method
	 * @param taobao_user_nick the taobao_user_nick to set
	 */
	
	public void setTaobao_user_nick(String taobao_user_nick) {
		this.taobao_user_nick = taobao_user_nick;
	}

	/** 
	 * getter method 
	 * @return the buyerNick 
	 */
	
	public String getBuyerNick() {
		return buyerNick;
	}

	/**
	 * setter method
	 * @param buyerNick the buyerNick to set
	 */
	
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Date getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(Date beginTime) {
		this.beginTime = beginTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}
	
	
}
