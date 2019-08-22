package com.kycrm.member.domain.entity.other;

import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;


/** 
* @ClassName: MobileSetting 
* @Description: 后台管理设置
* @author jackstraw_yu
* @date 2018年1月22日 下午4:08:51 
*  
*/
public class MobileSetting extends BaseEntity {

	private static final long serialVersionUID = -7543268052255564651L;

	/**
	 * 卖家昵称<br/>
	 * column:user_id
	 */
	private String userId;
	
	/**
	 * 催付效果(true:开启,false:关闭)<br/> 
	 * column:expediting
	 */
	private Boolean expediting;
	
	/**
	 * 短信余额不足提醒(true:开启,false:关闭)<br/>  
	 * column:message_remainder
	 */
	private Boolean messageRemainder;
	
	/**
	 * 短信余额不足提醒    <br/>
	 * column:message_count
	 */
	private Integer messageCount;
	
	/**
	 * 软件过期提醒(true:开启,false:关闭)<br/>    
	 * column:service_expire
	 */
	private Boolean serviceExpire;
	
	/**
	 * 最新活动促销(true:开启,false:关闭)<br/>    
	 * column:activity_notice
	 */
	private Boolean activityNotice;
	
	/**
	 * 手机号  <br/>   
	 * column:mobile
	 */
	private String mobile;
	
	/**
	 * 短信余额查询提醒-是否发送过发送(true:发送过,false:没有发送)<br/>   
	 * column:flag
	 */
	private Boolean flag = false;
	
	/**
	 * 短信余额查询提醒-第一次发送时间 <br/>   
	 * column:start_time
	 */
	private Date startTime;
	
	/**
	 * 短信余额查询提醒-最后时间:与第一次发送的间隔一个月<br/>  
	 * column:end_time
	 */
	private Date endTime;
	
//=================下面的字段,其他业务请勿随便调用,后果自负,有坑!author:jackstraw_yu
	/**
	 * 用户服务过期时间,关联用户表时用到
	 * 其他地方请勿随便调用
	 * */
	private transient Date expirationTime;
	/**
	 * 短信余额
	 * */
	private transient Long smsNum;
	/**
	 * 验证码
	 */
	private String code;
	
	/**
	 * 验证码
	 */
	private String qqNum;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getExpediting() {
		return expediting;
	}

	public void setExpediting(Boolean expediting) {
		this.expediting = expediting;
	}

	public Boolean getMessageRemainder() {
		return messageRemainder;
	}

	public void setMessageRemainder(Boolean messageRemainder) {
		this.messageRemainder = messageRemainder;
	}

	public Integer getMessageCount() {
		return messageCount;
	}

	public void setMessageCount(Integer messageCount) {
		this.messageCount = messageCount;
	}

	public Boolean getServiceExpire() {
		return serviceExpire;
	}

	public void setServiceExpire(Boolean serviceExpire) {
		this.serviceExpire = serviceExpire;
	}

	public Boolean getActivityNotice() {
		return activityNotice;
	}

	public void setActivityNotice(Boolean activityNotice) {
		this.activityNotice = activityNotice;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Boolean getFlag() {
		return flag;
	}

	public void setFlag(Boolean flag) {
		this.flag = flag;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(Date expirationTime) {
		this.expirationTime = expirationTime;
	}

	public Long getSmsNum() {
		return smsNum;
	}

	public void setSmsNum(Long smsNum) {
		this.smsNum = smsNum;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getQqNum() {
		return qqNum;
	}

	public void setQqNum(String qqNum) {
		this.qqNum = qqNum;
	}
}
