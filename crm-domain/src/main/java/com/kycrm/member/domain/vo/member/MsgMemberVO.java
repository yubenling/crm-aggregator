package com.kycrm.member.domain.vo.member;

import java.io.Serializable;
import java.math.BigDecimal;

/** 
* @ClassName: MemberMsgCriteria 
* @Description: 会员短信群发条件查询VO类
*  
*/
public class MsgMemberVO implements Serializable{


	/**
	 * 
	 */
	private static final long serialVersionUID = -4478219678215819419L;

	/**
	 * 用户id
	 */
	private Long uid;
	
	/**
	 * 用户昵称
	 */
	private String userNick;
	
	/**
	 * 会员分组:
	 */
	private Long memberGroupId;

	//------订单相关的
	/**
	 * 订单来源:	
	 */
	private String tradeFrom;
	
	/**
	 * 订单状态:	
	 */
	private String tradeStatus;
	
	/**
	 * 未交易时间:	
	 */
	
	private String minNoneTradeTime;
	
	/**
	 * 未交易时间:
	 */
	
	private String maxNoneTradeTime;
	
	/**
	 * 卖家标记:	
	 */
	
	private String sellerFlag;
	
	/**
	 * 交易时间:	
	 */
	
	private String minTradeTime;
	
	/**
	 * 交易时间:	
	 */
	
	private String maxTradeTime;
	
	/**
	 * 拍下订单时间段:	
	 */
	
	private String minTradeCreat;
	
	/**
	 * 拍下订单时间段:	
	 */
	
	private String maxTradeCreat;

	//-------会员
	
	/**
	 * 信用等级:	
	 */
	private Long creditId;
	
	/**
	 * 地区筛选:	
	 */
	private String provinces;
	
	/**
	 * 地区发送:	
	 */
	private Integer provinceSend;
	
	/**
	 * 参与短信营销次数: 
	 */
	private Integer minJoinTimes;
	
	/**
	 * 参与短信营销次数: 
	 */
	private Integer maxJoinTimes;

	/**
	 * 成功交易次数:	
	 */
	private Integer minTradeNum;
	
	/**
	 * 成功交易次数:	
	 */
	private Integer maxTradeNum;

	/**
	 * 累计消费金额: 
	 */
	private BigDecimal minTradeAmount;
	
	/**
	 * 累计消费金额:	
	 */
	private BigDecimal maxTradeAmount;

	/**
	 * 平均客单件:	
	 */
	private BigDecimal minAvgPrice;
	
	/**
	 * 平均客单件:	
	 */
	private BigDecimal maxAvgPrice;

	/**
	 * 交易关闭次数:	
	 */
	private Integer minCloseTradeNum;
	
	/**
	 * 交易关闭次数:
	 */
	private Integer maxCloseTradeNum;

	//------商品
	/**
	 * 指定商品:	
	 */
	private String itemIds;
	
	/**
	 * 指定发送:	
	 */
	private Integer BooitemSend;

	//-----过滤条件
	
	/**
	 * 黑名单:	
	 */
	private Integer	filterBlack;
	
	/**
	 * 中差评:	
	 */
	private Integer	filterneutral;

	/**
	 * 已发送过滤:
	 */
	private Integer filterHasSent;

	//=======辅助功能属性
	/**
	 * 是否关联订单表进行查询,有值的时候进行
	 */
	private String queryTable;
	
	/**
	 * 其实行数
	 */
	private Integer startRows;
	
	/**
	 * 每页显示的条数
	 */
	private Integer PageSize;

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

	public Long getMemberGroupId() {
		return memberGroupId;
	}

	public void setMemberGroupId(Long memberGroupId) {
		this.memberGroupId = memberGroupId;
	}

	public String getTradeFrom() {
		return tradeFrom;
	}

	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getMinNoneTradeTime() {
		return minNoneTradeTime;
	}

	public void setMinNoneTradeTime(String minNoneTradeTime) {
		this.minNoneTradeTime = minNoneTradeTime;
	}

	public String getMaxNoneTradeTime() {
		return maxNoneTradeTime;
	}

	public void setMaxNoneTradeTime(String maxNoneTradeTime) {
		this.maxNoneTradeTime = maxNoneTradeTime;
	}

	public String getSellerFlag() {
		return sellerFlag;
	}

	public void setSellerFlag(String sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	public String getMinTradeTime() {
		return minTradeTime;
	}

	public void setMinTradeTime(String minTradeTime) {
		this.minTradeTime = minTradeTime;
	}

	public String getMaxTradeTime() {
		return maxTradeTime;
	}

	public void setMaxTradeTime(String maxTradeTime) {
		this.maxTradeTime = maxTradeTime;
	}

	public String getMinTradeCreat() {
		return minTradeCreat;
	}

	public void setMinTradeCreat(String minTradeCreat) {
		this.minTradeCreat = minTradeCreat;
	}

	public String getMaxTradeCreat() {
		return maxTradeCreat;
	}

	public void setMaxTradeCreat(String maxTradeCreat) {
		this.maxTradeCreat = maxTradeCreat;
	}

	public Long getCreditId() {
		return creditId;
	}

	public void setCreditId(Long creditId) {
		this.creditId = creditId;
	}

	public String getProvinces() {
		return provinces;
	}

	public void setProvinces(String provinces) {
		this.provinces = provinces;
	}

	public Integer getProvinceSend() {
		return provinceSend;
	}

	public void setProvinceSend(Integer provinceSend) {
		this.provinceSend = provinceSend;
	}

	public Integer getMinJoinTimes() {
		return minJoinTimes;
	}

	public void setMinJoinTimes(Integer minJoinTimes) {
		this.minJoinTimes = minJoinTimes;
	}

	public Integer getMaxJoinTimes() {
		return maxJoinTimes;
	}

	public void setMaxJoinTimes(Integer maxJoinTimes) {
		this.maxJoinTimes = maxJoinTimes;
	}

	public Integer getMinTradeNum() {
		return minTradeNum;
	}

	public void setMinTradeNum(Integer minTradeNum) {
		this.minTradeNum = minTradeNum;
	}

	public Integer getMaxTradeNum() {
		return maxTradeNum;
	}

	public void setMaxTradeNum(Integer maxTradeNum) {
		this.maxTradeNum = maxTradeNum;
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

	public BigDecimal getMinAvgPrice() {
		return minAvgPrice;
	}

	public void setMinAvgPrice(BigDecimal minAvgPrice) {
		this.minAvgPrice = minAvgPrice;
	}

	public BigDecimal getMaxAvgPrice() {
		return maxAvgPrice;
	}

	public void setMaxAvgPrice(BigDecimal maxAvgPrice) {
		this.maxAvgPrice = maxAvgPrice;
	}

	public Integer getMinCloseTradeNum() {
		return minCloseTradeNum;
	}

	public void setMinCloseTradeNum(Integer minCloseTradeNum) {
		this.minCloseTradeNum = minCloseTradeNum;
	}

	public Integer getMaxCloseTradeNum() {
		return maxCloseTradeNum;
	}

	public void setMaxCloseTradeNum(Integer maxCloseTradeNum) {
		this.maxCloseTradeNum = maxCloseTradeNum;
	}

	public String getItemIds() {
		return itemIds;
	}

	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}

	public Integer getBooitemSend() {
		return BooitemSend;
	}

	public void setBooitemSend(Integer booitemSend) {
		BooitemSend = booitemSend;
	}

	public Integer getFilterBlack() {
		return filterBlack;
	}

	public void setFilterBlack(Integer filterBlack) {
		this.filterBlack = filterBlack;
	}

	public Integer getFilterneutral() {
		return filterneutral;
	}

	public void setFilterneutral(Integer filterneutral) {
		this.filterneutral = filterneutral;
	}

	public Integer getFilterHasSent() {
		return filterHasSent;
	}

	public void setFilterHasSent(Integer filterHasSent) {
		this.filterHasSent = filterHasSent;
	}

	public String getQueryTable() {
		return queryTable;
	}

	public void setQueryTable(String queryTable) {
		this.queryTable = queryTable;
	}

	public Integer getStartRows() {
		return startRows;
	}

	public void setStartRows(Integer startRows) {
		this.startRows = startRows;
	}

	public Integer getPageSize() {
		return PageSize;
	}

	public void setPageSize(Integer pageSize) {
		PageSize = pageSize;
	}

}
