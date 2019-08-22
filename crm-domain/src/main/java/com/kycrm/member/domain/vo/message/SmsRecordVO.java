package com.kycrm.member.domain.vo.message;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**	
* @Title: SmsRecordDTOVo
* @Description: (订单中心,发送记录查询vo类)
*/
public class SmsRecordVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8408589076974757932L;
	
	private Long id;

	/**用户info表id*/
	private Long uid;
	
	/**短信类型 设置类型 1-下单关怀 2-常规催付 3-二次催付 4-聚划算催付 5-预售催付 "
			+ "6-发货提醒 7-到达同城提醒 8-派件提醒 9-签收提醒 10-疑难件提醒 11-延时发货提醒 12-宝贝关怀 13-付款关怀 "
			+ "14-回款提醒 15-退款关怀 16-自动评价 17-批量评价 18-评价记录 19-中差评查看 20-中差评监控 21-中差评安抚 "
			+ "22-中差评统计 23-中差评原因 24中差评原因设置 25-中差评原因分析 26-手动订单提醒 27-优秀催付案例 28-效果统计 "
			+ "29-买家申请退款 30-退款成功 31-等待退货 32-拒绝退款 33-会员短信群发 34-指定号码群发 35-订单短信群发 36-会员互动*/
	private List<String> typeList;
	
	/**发送状态 1：发送失败，2：发送成功，3：手机号码不正确，4：号码重复， 5 ：黑名单， 6 ：重复被屏蔽 /重复发送*/
	private Integer status;
	
	/**发送开始时间*/
	private Date beginTime;
	
	/**发送结束时间*/
	private Date endTime;
	
	/**短信接收号码*/
	private String recNum;
	
	/**订单编号*/
	private String orderId;
	
	/**买家昵称*/
	private String buyerNick;
	
	/**短信类型*/
	private String type;
	
	/**分页页码*/
	private Integer pageNo = 1;
	
	/**起始行数*/
	private Integer startRows;
	
	/**每页显示条数*/
	private Integer currentRows = 10;
	
	/**买家昵称、短信接收号码、订单编号*/
    private String parameters;
	
	/**订单中心任务名称，可为空*/
	private String taskName;
	
	/**发送总记录id*/
	private Long msgId;
	
	/**
	 * 开始日期字符串
	 */
	private String bTime;
	
	/**
	 * 结束日期字符串
	 */
	private String eTime;
	
	/**
	 * 日期类型(day,month,year)短信账单用
	 */
	private String dateType;
	
	/**
	 * 活动名称(发送总记录可用)
	 */
	private String activityName;
	
	
	private String minSendTime;
	
	private String maxSendTime;
	
	private String method;
	
	
	private String marketingType;
	
	/**
	 * 是否预售;1:非预售；2：预售
	 */
	private Integer stepType;

	public String getMarketingType() {
		return marketingType;
	}

	public void setMarketingType(String marketingType) {
		this.marketingType = marketingType;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public List<String> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<String> typeList) {
		this.typeList = typeList;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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

	public String getRecNum() {
		return recNum;
	}

	public void setRecNum(String recNum) {
		this.recNum = recNum;
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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		if(null==pageNo || pageNo<1)
			pageNo = 1;
		this.pageNo = pageNo;
		this.setStartRows(pageNo);
	}

	public Integer getStartRows() {
		return startRows;
	}

	public void setStartRows(Integer pageNum) {
		if(pageNum != null){
			this.startRows = (pageNum-1)*this.currentRows;
		}else {
			this.startRows = pageNum;
		}
	}

	public Integer getCurrentRows() {
		return currentRows;
	}

	public void setCurrentRows(Integer currentRows) {
		this.currentRows = currentRows;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	public String getbTime() {
		return bTime;
	}

	public void setbTime(String bTime) {
		this.bTime = bTime;
	}

	public String geteTime() {
		return eTime;
	}

	public void seteTime(String eTime) {
		this.eTime = eTime;
	}

	public String getDateType() {
		return dateType;
	}

	public void setDateType(String dateType) {
		this.dateType = dateType;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getMinSendTime() {
		return minSendTime;
	}

	public void setMinSendTime(String minSendTime) {
		this.minSendTime = minSendTime;
	}

	public String getMaxSendTime() {
		return maxSendTime;
	}

	public void setMaxSendTime(String maxSendTime) {
		this.maxSendTime = maxSendTime;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public Integer getStepType() {
		return stepType;
	}

	public void setStepType(Integer stepType) {
		this.stepType = stepType;
	}
	
}
