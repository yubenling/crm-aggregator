package com.kycrm.tmc.sysinfo.entity;

import java.util.Date;

/**
 * @Table(name = "jdp_tb_trade") 
 * MetaData(value = "淘宝-主交易订单信息")
 *
 */
public class TbTransactionOrder {

    /**
     * 主键
     */
    private Long id;

	/**
	 * @MetaData (value="交易编号") 
	 * @Column (name="tid")
	 */
	private Long tid;
	
	/**
	 * @MetaData (value="状态") 
	 * @Column (name="status")
	 */
	private String status;
	
	/**
	 * @MetaData (value="类型") 
	 * @Column (name="type")
	 */
	private String type;
	
	/**
	 * @MetaData (value="卖家昵称") 
	 * @Column (name="seller_nick")
	 */
	private String sellerNick;
	
	/**
	 * @MetaData (value="买家昵称") 
	 * @Column (name="buyer_nick")
	 */
	private String buyerNick;
	
	/**
	 * @MetaData (value="创建时间") 
	 * @Column (name="created")
	 */
	private Date created;
	
	/**
	 * @MetaData (value="修改时间") 
	 * @Column (name="modified")
	 */
	private Date modified;
	
	/**
	 * @MetaData (value="哈希值") 
	 * @Column (name="jdp_hashcode")
	 */
	private String jdpHashcode;      
	
	/**
	 * @MetaData (value="淘宝回复内容") 
	 * @Column (name="jdp_response",length = 16777216)
	 */
	private String jdpResponse;   
	         
	/**
	 * @MetaData (value="淘宝修改时间") 
	 * @Column (name="jdp_created")
	 */
	private Date jdpCreated;
	
	/**
	 * @MetaData (value="淘宝修改时间") 
	 * @Column (name="jdp_modified")
	 */
	private Date jdpModified;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSellerNick() {
        return sellerNick;
    }

    public void setSellerNick(String sellerNick) {
        this.sellerNick = sellerNick;
    }

    public String getBuyerNick() {
        return buyerNick;
    }

    public void setBuyerNick(String buyerNick) {
        this.buyerNick = buyerNick;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getJdpHashcode() {
        return jdpHashcode;
    }

    public void setJdpHashcode(String jdpHashcode) {
        this.jdpHashcode = jdpHashcode;
    }

    public String getJdpResponse() {
        return jdpResponse;
    }

    public void setJdpResponse(String jdpResponse) {
        this.jdpResponse = jdpResponse;
    }

    public Date getJdpCreated() {
        return jdpCreated;
    }

    public void setJdpCreated(Date jdpCreated) {
        this.jdpCreated = jdpCreated;
    }

    public Date getJdpModified() {
        return jdpModified;
    }

    public void setJdpModified(Date jdpModified) {
        this.jdpModified = jdpModified;
    }

}
