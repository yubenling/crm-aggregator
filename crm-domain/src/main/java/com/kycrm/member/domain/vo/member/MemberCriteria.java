package com.kycrm.member.domain.vo.member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/** 
* @ClassName: MemberCriteria 
* @Description: 客户管理会员搜索VO 
* @author jackstraw_yu
* @date 2018年2月7日 下午5:23:40 
*  
*/
public class MemberCriteria implements Serializable{

	private static final long serialVersionUID = 4803779403872037002L;

	/**
	 * 卖家昵称
	 */
	private String userNick;
	
	/**
	 * 卖家主键
	 */
	private Long uid;
	
	/**
	 * 买家昵称
	 */
	private String buyerNick;
	
	/**
	 * 客户来源:<br/>
	 * 1-交易成功 2-未成交 3-卖家主动吸纳
	 */
	private String relationSource; 
	
	/**
	 * 最后交易时间<br/>
	 * 最小
	 */
	private Date minLastTradeTime;
	
	/**
	 * 最后交易时间<br/>
	 * 最大
	 */
	private Date maxLastTradeTime;
	
	/**
	 * 未购买时间<br/>
	 * 最小
	 */
	private Date minNoneTradeTime;
	
	/**
	 * 未购买时间<br/>
	 * 最大
	 */
	private Date maxNoneTradeTime;
	
	/**
	 * 手机号
	 */
	private String mobile;
	
	/**
	 * 累计消费金额<br/>
	 * 最小
	 */
	private BigDecimal minTradeAmount;
	
	/**
	 * 累计消费金额<br/>
	 * 最大
	 */
	private BigDecimal maxTradeAmount;
	
	/**
	 * 平均订单金额<br/>
	 * 最小
	 */
	private BigDecimal minAvgTradePrice;
	
	/**
	 * 平均订单金额<br/>
	 * 最大
	 */
	private BigDecimal maxAvgTradePrice;
	
	/**
	 * 会员等级
	 */
	private Long gradeId;
	
	/**
	 * 成交次数<br/>
	 * 最小
	 */
	private Long minTradeNum;
	
	/**
	 * 成交次数<br/>
	 * 最大
	 */
	private Long maxTradeNum;

	/**
	 * 页码:1开始起
	 */
	private Integer pageNo;
	
	/**
	 * 每页显示的数量
	 */
	private Integer pageSize;

	
	public MemberCriteria() {
	}
	public MemberCriteria(Integer pageNo, Integer pageSize) {
		this.pageNo = pageNo==null||pageNo.intValue()<=0?1:pageNo;
		this.pageSize = pageSize==null||pageSize.intValue()<=0?5:pageSize;
	}


	public String getUserNick() {
		return userNick;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getRelationSource() {
		return relationSource;
	}

	public void setRelationSource(String relationSource) {
		this.relationSource = relationSource;
	}

	public Date getMinLastTradeTime() {
		return minLastTradeTime;
	}

	public void setMinLastTradeTime(Date minLastTradeTime) {
		this.minLastTradeTime = minLastTradeTime;
	}

	public Date getMaxLastTradeTime() {
		return maxLastTradeTime;
	}

	public void setMaxLastTradeTime(Date maxLastTradeTime) {
		this.maxLastTradeTime = maxLastTradeTime;
	}

	public Date getMinNoneTradeTime() {
		return minNoneTradeTime;
	}

	public void setMinNoneTradeTime(Date minNoneTradeTime) {
		this.minNoneTradeTime = minNoneTradeTime;
	}

	public Date getMaxNoneTradeTime() {
		return maxNoneTradeTime;
	}

	public void setMaxNoneTradeTime(Date maxNoneTradeTime) {
		this.maxNoneTradeTime = maxNoneTradeTime;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public BigDecimal getMinTradeAmount() {
		return minTradeAmount;
	}

	public void setMinTradeAmount(BigDecimal minTradeAmount) {
		this.minTradeAmount = minTradeAmount;
	}

	public BigDecimal getMaxTradeAmount() {
		return maxTradeAmount;
	}

	public void setMaxTradeAmount(BigDecimal maxTradeAmount) {
		this.maxTradeAmount = maxTradeAmount;
	}

	public BigDecimal getMinAvgTradePrice() {
		return minAvgTradePrice;
	}

	public void setMinAvgTradePrice(BigDecimal minAvgTradePrice) {
		this.minAvgTradePrice = minAvgTradePrice;
	}

	public BigDecimal getMaxAvgTradePrice() {
		return maxAvgTradePrice;
	}

	public void setMaxAvgTradePrice(BigDecimal maxAvgTradePrice) {
		this.maxAvgTradePrice = maxAvgTradePrice;
	}

	public Long getGradeId() {
		return gradeId;
	}

	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}

	public Long getMinTradeNum() {
		return minTradeNum;
	}

	public void setMinTradeNum(Long minTradeNum) {
		this.minTradeNum = minTradeNum;
	}

	public Long getMaxTradeNum() {
		return maxTradeNum;
	}

	public void setMaxTradeNum(Long maxTradeNum) {
		this.maxTradeNum = maxTradeNum;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo==null||pageNo.intValue()<=0?1:pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize==null||pageSize.intValue()<=0?5:pageSize;
	}
	
}
