/** 
 * Project Name:s2jh4net 
 * File Name:SmsRecordDTO.java 
 * Package Name:s2jh.biz.shop.crm.manage.entity 
 * Date:2017年5月31日下午4:35:19 
 * Copyright (c) 2017,  All Rights Reserved. 
 * author zlp
*/  
  
package com.kycrm.syn.service.mongo.entity;  

import java.io.Serializable;
import java.util.Date;

import org.springframework.data.annotation.Transient;


/** 
 * ClassName:SmsRecordDTO <br/> 
 * Date:     2017年5月31日 下午4:35:19 <br/> 
 * @author   zlp
 * @version   1.0     
 */
public class OldSmsRecordDTO extends BaseMongoDTO implements Serializable{


	/** 
	 * Project Name:s2jh4net 
	 * File Name:SmsRecordDTO.java 
	 * Package Name:s2jh.biz.shop.crm.manage.entity 
	 * Date:2017年5月31日下午4:36:16 
	 * Copyright (c) 2017,  All Rights Reserved. 
	 */  
	private static final long serialVersionUID = -828978469191722311L;
	

	//@MetaData(value="主键id")
	private String _id;
	
	//@MetaData(value="短信发送流水号")
	private Long bizId;
	
	//@MetaData(value="短信接收号码")
	private String recNum;
	//@MetaData(value="短信错误码")
	private String resultCode;
	
	//@MetaData(value="模板编码")
	private String code;
	
	//@MetaData(value="短信内容")
	private String content;
	
	//@MetaData(value="短信接收时间")
	private Date receiverTime;
	
	//@MetaData(value="发送时间")
	private Date sendTime;
	//@MetaData(value="发送时间")
	private long sendLongTime;
	
	//@MetaData(value="短信类型 设置类型 1-下单关怀 2-常规催付 3-二次催付 4-聚划算催付 5-预售催付 "
//			+ "6-发货提醒 7-到达同城提醒 8-派件提醒 9-签收提醒 10-疑难件提醒 11-延时发货提醒 12-宝贝关怀 13-付款关怀 "
//			+ "14-回款提醒 15-退款关怀 16-自动评价 17-批量评价 18-评价记录 19-中差评查看 20-中差评监控 21-中差评安抚 "
//			+ "22-中差评统计 23-中差评原因 24中差评原因设置 25-中差评原因分析 26-手动订单提醒 27-优秀催付案例 28-效果统计 "
//			+ "29-买家申请退款 30-退款成功 31-等待退货 32-拒绝退款 33-会员短信群发 34-指定号码群发 35-订单短信群发 36-会员互动")
	private String type;
	
	//@MetaData(value="发送状态 1：发送失败，2：发送成功，3：手机号码不正确，4：号码重复， 5 ：黑名单， 6 ：重复被屏蔽 /重复发送")
	private Integer status;

	//@MetaData(value="短信渠道  1--淘宝 2--京东 , 3--天猫 , 4---自定义签名 ")
	private String channel;

	//@MetaData(value="实际扣除短信条数")
	private Integer actualDeduction;

	
	//@MetaData(value="订单编号")
	private String orderId;
	
	//@MetaData(value="买家昵称")
	private String buyerNick;

	//@MetaData(value="收货人昵称")
	private String nickname;
	
	//@MetaData(value="短信签名")
	private String autograph;
	
	//@MetaData(value="操作人")
	private String userId;
	
	//@MetaData(value="总记录id")
	private Long msgId;
	
	//@MetaData(value="订单中心任务Id，可为空")
	private Long taskId;
	
	//@MetaData(value="订单中心任务名称，可为空")
	private String taskName;
	
	//@MetaData(value ="是否删除(显示或者隐藏)--true:显示 /false:不显示  默认保存true")
	private boolean isShow;
	
	//@MetaData(value ="活动名称")
	private String activityName;
	
	//@MetaData(value ="活动名称")
	private String lastModifiedBy;
	
	//@MetaData(value ="数据来源--2表示mongo版本产生的新数据1表示mysql历史数据")
	private String  source;
	
	//@MetaData(value="起始发送时间")
	@Transient
	private Date bTime;
	//@MetaData(value="结束发送时间")
	@Transient
	private Date eTime;
	

	
	
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Date getbTime() {
		return bTime;
	}

	public void setbTime(Date bTime) {
		this.bTime = bTime;
	}

	public Date geteTime() {
		return eTime;
	}

	public void seteTime(Date eTime) {
		this.eTime = eTime;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Long getBizId() {
		return bizId;
	}

	public void setBizId(Long bizId) {
		this.bizId = bizId;
	}

	public String getRecNum() {
		return recNum;
	}

	public void setRecNum(String recNum) {
		this.recNum = recNum;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getReceiverTime() {
		return receiverTime;
	}

	public void setReceiverTime(Date receiverTime) {
		this.receiverTime = receiverTime;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Integer getActualDeduction() {
		return actualDeduction;
	}

	public void setActualDeduction(Integer actualDeduction) {
		this.actualDeduction = actualDeduction;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getAutograph() {
		return autograph;
	}

	public void setAutograph(String autograph) {
		this.autograph = autograph;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public boolean isShow() {
		return isShow;
	}

	public void setShow(boolean isShow) {
		this.isShow = isShow;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public long getSendLongTime() {
		return sendLongTime;
	}

	public void setSendLongTime(long sendLongTime) {
		this.sendLongTime = sendLongTime;
	}

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}
	
    
}
  