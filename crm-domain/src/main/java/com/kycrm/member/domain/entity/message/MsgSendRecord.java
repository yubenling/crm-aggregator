package com.kycrm.member.domain.entity.message;

import java.math.BigDecimal;
import java.util.Date;

import com.kycrm.member.domain.entity.base.BaseEntity;

/**
 * @Table (name = "CRM_MSGRECORD")
 * @MetaData (value = "短信群发总记录")
 *
 */
public class MsgSendRecord extends BaseEntity {

	private static final long serialVersionUID = 3441870237673104935L;
	/**
	 * @MetaData (value ="卖家昵称")
	 * @Column (name="user_Id")
	 */
	private String userId;
	/**
	 * @MetaData (value ="短信批量发送总数")
	 * @Column (name="total_count")
	 */
	private Integer totalCount;
	/**
	 * @MetaData (value ="短信批量发送成功总数")
	 * @Column (name="succeed_count")
	 */
	private Integer succeedCount;
	/**
	 * @MetaData (value ="短信批量发送失败总数/调用接口失败")
	 * @Column (name="failed_count")
	 */
	private Integer failedCount;
	/**
	 * @MetaData (value ="短信批量发送手机号错误总数")
	 * @Column (name="wrong_count")
	 */
	private Integer wrongCount;
	/**
	 * @MetaData (value ="短信批量发送手机号重复总数")
	 * @Column (name="repeat_count")
	 */
	private Integer repeatCount;
	/**
	 * @MetaData (value ="短信批量发送黑名单总数")
	 * @Column (name="black_count")
	 */
	private Integer blackCount;
	/**
	 * @MetaData (value ="短信批量发送被屏蔽总数")
	 * @Column (name="sheild_count")
	 */
	private Integer sheildCount;
	/**
	 * @MetaData (value ="状态--1:全部成功/2:全部失败/3:部分成功/4:发送中/5:发送完成(该字段作为识别字段)")
	 * @Column (name="status")
	 */
	private String status;
	/**
	 * @MetaData (value ="短信基础/模板内容")
	 * @Column (name="template_content")
	 */
	private String templateContent;
	/**
	 * @MetaData (value ="活动名称")
	 * @Column (name="activity_name")
	 */
	private String activityName;

	/**
	 * @MetaData (value ="创建时间")
	 * @Column (name="send_creat")
	 */
	private Date sendCreat;
	/**
	 * @MetaData (value ="短信类型")
	 * @Column (name="type")
	 */
	private String type;
	/**
	 * @MetaData (value ="是否删除(显示或者隐藏)--true:显示 /false:不显示  默认保存true")
	 * @Column (name="is_show")
	 */
	private Boolean isShow;
	/**
	 * @MetaData (value ="是否已发送--true:已发送/false:未发送,未发送说明是定时任务的总记录待发送")
	 * @Column (name="is_sent")
	 */
	private Boolean isSent;

	/**
	 * ROI
	 */
	private String roiValue;

	/**
	 * 营销类型：1-->基础营销；2->高级营销
	 */
	private String marketingType;

	/**
	 * 本次发送记录的对应的查询条件
	 */
	private Long queryParamId;

	/**
	 * 预售(2018-12-24添加),目前只针对订单；0：所有1：非预售，2：预售
	 */
	private Integer stepType = 0;

	/**
	 * 本次发送记录是否可以查看效果分析:true(可查看1);false(不可查看0)
	 */
	private Boolean effectIsShow = true;

	private Long taoBaoShortLinkId;

	private Integer shortLinkType;

	private Integer pageClickNum;

	private BigDecimal successPayment;

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Integer getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}

	public Integer getSucceedCount() {
		return succeedCount;
	}

	public void setSucceedCount(Integer succeedCount) {
		this.succeedCount = succeedCount;
	}

	public Integer getFailedCount() {
		return failedCount;
	}

	public void setFailedCount(Integer failedCount) {
		this.failedCount = failedCount;
	}

	public Integer getWrongCount() {
		return wrongCount;
	}

	public void setWrongCount(Integer wrongCount) {
		this.wrongCount = wrongCount;
	}

	public Integer getRepeatCount() {
		return repeatCount;
	}

	public void setRepeatCount(Integer repeatCount) {
		this.repeatCount = repeatCount;
	}

	public Integer getBlackCount() {
		return blackCount;
	}

	public void setBlackCount(Integer blackCount) {
		this.blackCount = blackCount;
	}

	public Integer getSheildCount() {
		return sheildCount;
	}

	public void setSheildCount(Integer sheildCount) {
		this.sheildCount = sheildCount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTemplateContent() {
		return templateContent;
	}

	public void setTemplateContent(String templateContent) {
		this.templateContent = templateContent;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public Date getSendCreat() {
		return sendCreat;
	}

	public void setSendCreat(Date sendCreat) {
		this.sendCreat = sendCreat;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getIsShow() {
		return isShow;
	}

	public void setIsShow(Boolean isShow) {
		this.isShow = isShow;
	}

	public Boolean getIsSent() {
		return isSent;
	}

	public void setIsSent(Boolean isSent) {
		this.isSent = isSent;
	}

	public String getRoiValue() {
		return roiValue;
	}

	public void setRoiValue(String roiValue) {
		this.roiValue = roiValue;
	}

	public String getMarketingType() {
		return marketingType;
	}

	public void setMarketingType(String marketingType) {
		this.marketingType = marketingType;
	}

	public Long getQueryParamId() {
		return queryParamId;
	}

	public void setQueryParamId(Long queryParamId) {
		this.queryParamId = queryParamId;
	}

	public Integer getStepType() {
		return stepType;
	}

	public void setStepType(Integer stepType) {
		this.stepType = stepType;
	}

	public Boolean getEffectIsShow() {
		return effectIsShow;
	}

	public void setEffectIsShow(Boolean effectIsShow) {
		this.effectIsShow = effectIsShow;
	}

	public Long getTaoBaoShortLinkId() {
		return taoBaoShortLinkId;
	}

	public void setTaoBaoShortLinkId(Long taoBaoShortLinkId) {
		this.taoBaoShortLinkId = taoBaoShortLinkId;
	}

	public Integer getShortLinkType() {
		return shortLinkType;
	}

	public void setShortLinkType(Integer shortLinkType) {
		this.shortLinkType = shortLinkType;
	}

	public Integer getPageClickNum() {
		return pageClickNum;
	}

	public void setPageClickNum(Integer pageClickNum) {
		this.pageClickNum = pageClickNum;
	}

	public BigDecimal getSuccessPayment() {
		return successPayment;
	}

	public void setSuccessPayment(BigDecimal successPayment) {
		this.successPayment = successPayment;
	}

	@Override
	public String toString() {
		return "MsgSendRecord [userId=" + userId + ", totalCount=" + totalCount + ", succeedCount=" + succeedCount
				+ ", failedCount=" + failedCount + ", wrongCount=" + wrongCount + ", repeatCount=" + repeatCount
				+ ", blackCount=" + blackCount + ", sheildCount=" + sheildCount + ", status=" + status
				+ ", templateContent=" + templateContent + ", activityName=" + activityName + ", sendCreat=" + sendCreat
				+ ", type=" + type + ", isShow=" + isShow + ", isSent=" + isSent + ", roiValue=" + roiValue
				+ ", marketingType=" + marketingType + ", queryParamId=" + queryParamId + ", stepType=" + stepType
				+ ", effectIsShow=" + effectIsShow + ", taoBaoShortLinkId=" + taoBaoShortLinkId + ", shortLinkType="
				+ shortLinkType + ", pageClickNum=" + pageClickNum + ", successPayment=" + successPayment + "]";
	}

}
