package com.kycrm.member.domain.to;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 会员等级设置
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月12日上午11:32:48
 * @Tags
 */
public class MemberLevelSetting implements Serializable {

	private static final long serialVersionUID = 7635333658152914902L;

	private Long id;

	// 用户ID
	private String userId;

	// 交易额度
	private BigDecimal tradingVolume;

	// 交易笔数
	private Integer turnover;

	// 折扣率
	private String discount;

	// 创建时间
	private Date ctime;

	// 会员等级 0-店铺会员 1-普通会员 2-高级会员 3-vip会员 4-至尊vip会员
	private String memberlevel;

	// 是否启用 0-是 1-否
	private String enabled;

	// 是否归类 0-是 1-否
	private String isSortOut;

	// 开启状态 0-开启 1-关闭
	private String status;

	// 分组编号
	private String groupId;

	// 是否设置达到某一会员等级的交易量和交易额
	private String hierarchy;

	public MemberLevelSetting() {
		super();
	}

	public MemberLevelSetting(Long id, String userId, BigDecimal tradingVolume, Integer turnover, String discount,
			Date ctime, String memberlevel, String enabled, String isSortOut, String status, String groupId,
			String hierarchy) {
		super();
		this.id = id;
		this.userId = userId;
		this.tradingVolume = tradingVolume;
		this.turnover = turnover;
		this.discount = discount;
		this.ctime = ctime;
		this.memberlevel = memberlevel;
		this.enabled = enabled;
		this.isSortOut = isSortOut;
		this.status = status;
		this.groupId = groupId;
		this.hierarchy = hierarchy;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public BigDecimal getTradingVolume() {
		return tradingVolume;
	}

	public void setTradingVolume(BigDecimal tradingVolume) {
		this.tradingVolume = tradingVolume;
	}

	public Integer getTurnover() {
		return turnover;
	}

	public void setTurnover(Integer turnover) {
		this.turnover = turnover;
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount;
	}

	public Date getCtime() {
		return ctime;
	}

	public void setCtime(Date ctime) {
		this.ctime = ctime;
	}

	public String getMemberlevel() {
		return memberlevel;
	}

	public void setMemberlevel(String memberlevel) {
		this.memberlevel = memberlevel;
	}

	public String getEnabled() {
		return enabled;
	}

	public void setEnabled(String enabled) {
		this.enabled = enabled;
	}

	public String getIsSortOut() {
		return isSortOut;
	}

	public void setIsSortOut(String isSortOut) {
		this.isSortOut = isSortOut;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getHierarchy() {
		return hierarchy;
	}

	public void setHierarchy(String hierarchy) {
		this.hierarchy = hierarchy;
	}

	@Override
	public String toString() {
		return "MemberLevelSetting [userId=" + userId + ", tradingVolume=" + tradingVolume + ", turnover=" + turnover
				+ ", discount=" + discount + ", ctime=" + ctime + ", memberlevel=" + memberlevel + ", enabled="
				+ enabled + ", isSortOut=" + isSortOut + ", status=" + status + ", groupId=" + groupId + ", hierarchy="
				+ hierarchy + "]";
	}

}
