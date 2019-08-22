package com.kycrm.member.domain.vo.member;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.kycrm.member.annotation.Table;

/** 
* @ClassName: MemberMsgCriteria 
* @Description: 会员短信群发条件查询VO类
* @author jackstraw_yu
* @date 2018年3月6日 下午2:33:33 
*  
*/
public class MemberMsgCriteria implements Serializable{

	private static final long serialVersionUID = -1598402621687157714L;

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
	@Table("TRADE_DTO")
	private String tradeFrom;
	
	/**
	 * 订单状态:	
	 */
	@Table("TRADE_DTO")
	private String tradeStatus;
	
	/**
	 * 未交易时间
	 */
	private Integer noneTradeTime;
	
	/**
	 * 未交易时间:	
	 */
	@Table("TRADE_DTO")
	private Date minNoneTradeTime;
	
	/**
	 * 未交易时间:
	 */
	@Table("TRADE_DTO")
	private Date maxNoneTradeTime;
	
	/**
	 * 信用等级:	
	 */
	private Long creditId;
	
	//------商品
	/**
	 * 指定商品的","拼接的字符串
	 */
	private String itemIds;
	
	/**
	 * 指定商品id的集合
	 */
	private List<String> itemIdList;
	
	/**
	 * 指定发送:	
	 */
	private Boolean booitemSend;
	
	/**
	 * 地区筛选:","拼接的字符串
	 */
	private String provinces;
	
	/**
	 * 地区筛选:集合
	 */
	private List<String> provinceList;
	
	/**
	 * 地区筛选:","拼接的字符串
	 */
	private String citys;
	
	/**
	 * 地区筛选:集合
	 */
	private List<String> cityList;
	
	/**
	 * 地区发送:	
	 */
	private Boolean booprovinceSend;
	
	/**
	 * 卖家标记:","拼接的字符串
	 */
	private String sellerFlag;
	
	/**
	 * 卖家标记:集合
	 */
	@Table("TRADE_DTO")
	private List<String> sellerFlagList;
	
	//-----过滤条件
	/**
	 * 黑名单:	
	 */
	private Boolean	filterBlack;
	
	/**
	 * 中差评:	
	 */
	private Boolean	filterneutral;	
	
	/**
	 * 交易时间：近一天，近两天，用1,2代替
	 */
	private Integer tradeTime;
	
	/**
	 * 交易时间:	
	 */
	@Table("TRADE_DTO")
	private Date minTradeTime;
	
	/**
	 * 交易时间:	
	 */
	@Table("TRADE_DTO")
	private Date maxTradeTime;
	
	/**
	 * 成功交易次数：0,1,2,3表示，3表示3次以及以上
	 */
	private Long tradeNum;
	
	/**
	 * 成功交易次数:	
	 */
	private Long minTradeNum;
	
	/**
	 * 成功交易次数:	
	 */
	private Long maxTradeNum;
	
	/**
	 * 累计消费金额：1,100,200,300。1表示1-100（包括1）,100表示100-200（包括100）,200表示200-300（包括200）,300表示300及以上
	 */
	private Integer tradeAmount;
	
	/**
	 * 累计消费金额: 
	 */
	private BigDecimal minTradeAmount;
	
	/**
	 * 累计消费金额:	
	 */
	private BigDecimal maxTradeAmount;
	
	/**
	 * 平均客单价：1,100,200,300。1表示1-100（包括1）,100表示100-200（包括100）,200表示200-300（包括200）,300表示300及以上
	 */
	private Integer avgPrice;

	/**
	 * 平均客单件:	
	 */
	private BigDecimal minAvgPrice;
	
	/**
	 * 平均客单件:	
	 */
	private BigDecimal maxAvgPrice;
	
	/**
	 * 交易关闭次数：0,1,2,3表示，3表示3次以及以上
	 */
	private Integer closeTradeNum;
	
	/**
	 * 交易关闭次数:	
	 */
	private Integer minCloseTradeNum;
	
	/**
	 * 交易关闭次数:
	 */
	private Integer maxCloseTradeNum;
	
	/**
	 * 拍下订单时间段:	
	 */
	@Table("TRADE_DTO")
	private String minTradeCreat;
	
	/**
	 * 拍下订单时间段:	
	 */
	@Table("TRADE_DTO")
	private String maxTradeCreat;

	/**
	 * 参与短信营销次数: 0,1,2,3表示，3表示3次以及以上
	 */
	private Integer joinTimes;
	
	/**
	 * 参与短信营销次数: 
	 */
	private Integer minJoinTimes;
	
	/**
	 * 参与短信营销次数: 
	 */
	private Integer maxJoinTimes;

	
	/**
	 * 已发送过滤:天数1-30,31表示31及以上天数
	 */
	private Integer filterHasSent;
	
	/**
	 * 发送过滤时间
	 */
	private Date minSendTime;
	
	/**
	 * 发送过虑时间
	 */
	private Date maxSendTime;
	/**
	 * 退款状态
	 */
	private String refundStatus;

	//=======辅助功能属性
	/**
	 * 是否关联订单表进行查询,有值的时候进行
	 */
	private String queryTable;
	
	/**
	 * 起始行数
	 */
	private Long startRows;
	
	/**
	 * 每页显示的条数
	 */
	private Long PageSize;
	
	private Integer itemSend;
	
	private Integer provinceSend;
	
	/**
	 * 黑名单:	
	 */
	private Integer	filterBlackInt;
	
	/**
	 * 中差评:	
	 */
	private Integer	filterneutralInt;

	public Integer getFilterBlackInt() {
		return filterBlackInt;
	}

	public void setFilterBlackInt(Integer filterBlackInt) {
		this.filterBlackInt = filterBlackInt;
	}

	public Integer getFilterneutralInt() {
		return filterneutralInt;
	}

	public void setFilterneutralInt(Integer filterneutralInt) {
		this.filterneutralInt = filterneutralInt;
	}

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


	public String getSellerFlag() {
		return sellerFlag;
	}

	public void setSellerFlag(String sellerFlag) {
		this.sellerFlag = sellerFlag;
	}

	public List<String> getSellerFlagList() {
		return sellerFlagList;
	}

	public void setSellerFlagList(List<String> sellerFlagList) {
		this.sellerFlagList = sellerFlagList;
	}

	public Date getMinTradeTime() {
		return minTradeTime;
	}

	public void setMinTradeTime(Date minTradeTime) {
		this.minTradeTime = minTradeTime;
	}

	public Date getMaxTradeTime() {
		return maxTradeTime;
	}

	public void setMaxTradeTime(Date maxTradeTime) {
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

	public List<String> getProvinceList() {
		return provinceList;
	}

	public void setProvinceList(List<String> provinceList) {
		this.provinceList = provinceList;
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

	public List<String> getItemIdList() {
		return itemIdList;
	}

	public void setItemIdList(List<String> itemIdList) {
		this.itemIdList = itemIdList;
	}


	public Boolean getFilterBlack() {
		return filterBlack;
	}

	public void setFilterBlack(Boolean filterBlack) {
		this.filterBlack = filterBlack;
	}

	public Boolean getFilterneutral() {
		return filterneutral;
	}

	public void setFilterneutral(Boolean filterneutral) {
		this.filterneutral = filterneutral;
	}


	public Integer getTradeTime() {
		return tradeTime;
	}

	public void setTradeTime(Integer tradeTime) {
		this.tradeTime = tradeTime;
	}


	public Long getTradeNum() {
		return tradeNum;
	}

	public void setTradeNum(Long tradeNum) {
		this.tradeNum = tradeNum;
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

	public Integer getTradeAmount() {
		return tradeAmount;
	}

	public void setTradeAmount(Integer tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

	public Integer getAvgPrice() {
		return avgPrice;
	}

	public void setAvgPrice(Integer avgPrice) {
		this.avgPrice = avgPrice;
	}

	public Integer getCloseTradeNum() {
		return closeTradeNum;
	}

	public void setCloseTradeNum(Integer closeTradeNum) {
		this.closeTradeNum = closeTradeNum;
	}

	public Integer getJoinTimes() {
		return joinTimes;
	}

	public void setJoinTimes(Integer joinTimes) {
		this.joinTimes = joinTimes;
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

	public Long getStartRows() {
		return startRows;
	}

	public void setStartRows(Long startRows) {
		this.startRows = startRows;
	}

	public Long getPageSize() {
		return PageSize;
	}

	public void setPageSize(Long pageSize) {
		PageSize = pageSize;
	}

	public String getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(String refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Date getMinSendTime() {
		return minSendTime;
	}

	public void setMinSendTime(Date minSendTime) {
		this.minSendTime = minSendTime;
	}

	public Date getMaxSendTime() {
		return maxSendTime;
	}

	public void setMaxSendTime(Date maxSendTime) {
		this.maxSendTime = maxSendTime;
	}

	public Boolean getBooitemSend() {
		return booitemSend;
	}

	public void setBooitemSend(Boolean booitemSend) {
		this.booitemSend = booitemSend;
	}

	public Boolean getBooprovinceSend() {
		return booprovinceSend;
	}

	public void setBooprovinceSend(Boolean booprovinceSend) {
		this.booprovinceSend = booprovinceSend;
	}

	public Integer getItemSend() {
		return itemSend;
	}

	public void setItemSend(Integer itemSend) {
		this.itemSend = itemSend;
	}

	public Integer getProvinceSend() {
		return provinceSend;
	}

	public void setProvinceSend(Integer provinceSend) {
		this.provinceSend = provinceSend;
	}

	public String getCitys() {
		return citys;
	}

	public void setCitys(String citys) {
		this.citys = citys;
	}

	public List<String> getCityList() {
		return cityList;
	}

	public void setCityList(List<String> cityList) {
		this.cityList = cityList;
	}

	public Integer getNoneTradeTime() {
		return noneTradeTime;
	}

	public void setNoneTradeTime(Integer noneTradeTime) {
		this.noneTradeTime = noneTradeTime;
	}
	
}
