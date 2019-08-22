package com.kycrm.member.domain.entity.trade;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
/**
 * 存放可以与msgId匹配的订单(所有的历史数据)
 * @ClassName: MsgMatchTrade  
 * @author ztk
 * @date 2018年6月13日 下午3:55:01
 */
public class MsgTempTradeHistory implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Long id;
	
	private Long uid;
	
	/**
	 * 发送记录批次id
	 */
	private Long msgId;
	
	private Long tid;
	
	/**
	 * 使用字符串拼接的商品id
	 */
	private String numIidStr;
	
	/**
	 * 此订单的状态   非预售效果分析的： create:创建 waitPay:未付款 success:付款 refund:退款
	 * 			 预售效果分析的：create:创建 closed订单关闭 waitPay:未付款   success成交  refund退款
	 */
	private String tradeStatus;
	
	/**
	 * 买家昵称
	 */
	private String buyerNick;
	
	/**
	 * 是否退款
	 */
	private Boolean refundFlag;
	
	/**
	 * 实付金额
	 */
	private BigDecimal payment;
	
	/**
	 * 订单来源
	 */
	private String tradeFrom;
	
	/**
	 * 商品数量(不确定)
	 */
	private Long itemNum;
	
	/**
	 * 订单状态List
	 */
	private List<String> tradeStatusList;
	
	/**
	 * 创建时间
	 */
	private Date created;
	
	/**
	 * 订单结束时间
	 */
	private Date endTime;
	
	/**
	 * 订单发生变化时间
	 */
	private Date modifiedTime;
	
	/**
	 * 付款时间
	 */
	private Date payTime;
	
	/**
	 * 商品标题（不确定）
	 */
	private String title;
	
	/**
	 * 收货地址
	 */
	private String receiverAddress;
	
	/**
	 * 收货人手机号
	 */
	private String receiverMobile;
	
	/**
	 * 收货人名字
	 */
	private String receiverName;
	
	/**
	 * 发送总记录创建时间，删除操作时使用
	 */
	private Date msgCreated;
	
	 /**
     * 定金
     */
    private String front;
    
    /**
     * 尾款
     */
    private String tail;
    
    /**
     * 阶段付款的订单状态（例如万人团订单等），目前有三返回状态
     * FRONT_NOPAID_FINAL_NOPAID(定金未付尾款未付)，
     * FRONT_PAID_FINAL_NOPAID(定金已付尾款未付)，
     * FRONT_PAID_FINAL_PAID(定金和尾款都付)
     */
    private String stepTradeStatus;
    
    /**
     * 定金付款时间
     */
    private Date frontPayTime;

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getMsgId() {
		return msgId;
	}

	public void setMsgId(Long msgId) {
		this.msgId = msgId;
	}

	
	public Long getTid() {
		return tid;
	}

	public void setTid(Long tid) {
		this.tid = tid;
	}

	public String getNumIidStr() {
		return numIidStr;
	}

	public void setNumIidStr(String numIidStr) {
		this.numIidStr = numIidStr;
	}

	public String getBuyerNick() {
		return buyerNick;
	}

	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public Boolean getRefundFlag() {
		return refundFlag;
	}

	public void setRefundFlag(Boolean refundFlag) {
		this.refundFlag = refundFlag;
	}

	public BigDecimal getPayment() {
		return payment;
	}

	public void setPayment(BigDecimal payment) {
		this.payment = payment;
	}

	public String getTradeFrom() {
		return tradeFrom;
	}

	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}

	public Long getItemNum() {
		return itemNum;
	}

	public void setItemNum(Long itemNum) {
		this.itemNum = itemNum;
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getModifiedTime() {
		return modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public Date getPayTime() {
		return payTime;
	}

	public void setPayTime(Date payTime) {
		this.payTime = payTime;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress;
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public List<String> getTradeStatusList() {
		return tradeStatusList;
	}

	public void setTradeStatusList(List<String> tradeStatusList) {
		this.tradeStatusList = tradeStatusList;
	}

	public Date getMsgCreated() {
		return msgCreated;
	}

	public void setMsgCreated(Date msgCreated) {
		this.msgCreated = msgCreated;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getTail() {
		return tail;
	}

	public void setTail(String tail) {
		this.tail = tail;
	}

	public String getStepTradeStatus() {
		return stepTradeStatus;
	}

	public void setStepTradeStatus(String stepTradeStatus) {
		this.stepTradeStatus = stepTradeStatus;
	}

	public Date getFrontPayTime() {
		return frontPayTime;
	}

	public void setFrontPayTime(Date frontPayTime) {
		this.frontPayTime = frontPayTime;
	}
	
	

}
