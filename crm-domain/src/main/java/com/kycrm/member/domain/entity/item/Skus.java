package com.kycrm.member.domain.entity.item;


import java.util.Date;

//import javax.persistence.Access;
//import javax.persistence.AccessType;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Table;
//
//import lab.s2jh.core.annotation.MetaData;
//import lab.s2jh.core.entity.BaseNativeEntity;
////import lombok.Getter;
////import lombok.Setter;
//import lombok.experimental.Accessors;

//import org.hibernate.annotations.Cache;
//import org.hibernate.annotations.CacheConcurrencyStrategy;

//@Accessors(chain = true)
//@Access(AccessType.FIELD)
//@Entity
//@Table(name = "crm_Sku")
//@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
//@MetaData(value = "商品sku信息")
public class Skus{

	private static final long serialVersionUID = -4873661082081060206L;
	
//	@MetaData(value="商品级别的条形码")
//	@Column(name="barcode")
	private String barcode;
	
//	@MetaData(value="基础色数据")
//	@Column(name="change_prop")
	private String changeProp;
	
//	@MetaData(value="sku创建日期 时间格式：yyyy-MM-dd HH:mm:ss")
//	@Column(name="created")
	private String created;
	
//	@MetaData(value="扩展sku的id")
//	@Column(name="extra_id")
	private Long extraId;
	
//	@MetaData(value="")
//	@Column(name="gmt_modified")
	private Date gmtModified;
	
//	@MetaData(value="sku所属商品Id")
//	@Column(name="iid")
	private String iid;
	
//	@MetaData(value="扩展sku的备注信息")
//	@Column(name="memo")
	private String memo;
	
//	@MetaData(value="sku最后修改日期 时间格式：yyyy-MM-dd HH:mm:ss")
//	@Column(name="modified")
	private Date modified;
	
//	@MetaData(value="sku所属商品数字Id")
//	@Column(name="num_iid")
	private Long numIid;
	
//	@MetaData(value="商家设置的外部id")
//	@Column(name="outer_id")
	private String outerId;
	
//	@MetaData(value = "商品价格，格式：5.00；单位：元；精确到：分")
//	@Column(name = "price")
	private String price;
	
//	@MetaData(value = "sku的销售属性组合字符串（颜色，大小，等等，可通过类目API获取某类目下的销售属性）,格式是p1:v1;p2:v2")
//	@Column(name = "properties")
	private String properties;
	
//	@MetaData(value = "属于这个sku的商品的数量")
//	@Column(name = "quantity")
	private Integer quantity;
	
//	@MetaData(value="sku级别发货时间")
//	@Column(name="sku_delivery_time")
	private String skuDeliveryTime;
	
//	@MetaData(value = "sku的id")
//	@Column(name = "sku_id")
	private Long skuId;
	
//	@MetaData(value="SKu上的产品规格信息")
//	@Column(name="sku_spec_id")
	private Long skuSpecId;
	
//	@MetaData(value="sku状态")
//	@Column(name="status")
	private String status;
	
//	@MetaData(value="商品在付款减库存的状态下，该sku上未付款的订单数量")
//	@Column(name="with_hold_quantity")
	private Long withHoldQuantity;
	
	

	@Override
	public String toString() {
		return "Skus [barcode=" + barcode + ", changeProp=" + changeProp
				+ ", created=" + created + ", extraId=" + extraId
				+ ", gmtModified=" + gmtModified + ", iid=" + iid + ", memo="
				+ memo + ", modified=" + modified + ", numIid=" + numIid
				+ ", outerId=" + outerId + ", price=" + price + ", properties="
				+ properties + ", quantity=" + quantity + ", skuDeliveryTime="
				+ skuDeliveryTime + ", skuId=" + skuId + ", skuSpecId="
				+ skuSpecId + ", status=" + status + ", withHoldQuantity="
				+ withHoldQuantity + "]";
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getChangeProp() {
		return changeProp;
	}

	public void setChangeProp(String changeProp) {
		this.changeProp = changeProp;
	}

	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	public Long getExtraId() {
		return extraId;
	}

	public void setExtraId(Long extraId) {
		this.extraId = extraId;
	}

	public Date getGmtModified() {
		return gmtModified;
	}

	public void setGmtModified(Date gmtModified) {
		this.gmtModified = gmtModified;
	}

	public String getIid() {
		return iid;
	}

	public void setIid(String iid) {
		this.iid = iid;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Date getModified() {
		return modified;
	}

	public void setModified(Date modified) {
		this.modified = modified;
	}

	public Long getNumIid() {
		return numIid;
	}

	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}

	public String getOuterId() {
		return outerId;
	}

	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getProperties() {
		return properties;
	}

	public void setProperties(String properties) {
		this.properties = properties;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getSkuDeliveryTime() {
		return skuDeliveryTime;
	}

	public void setSkuDeliveryTime(String skuDeliveryTime) {
		this.skuDeliveryTime = skuDeliveryTime;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Long getSkuSpecId() {
		return skuSpecId;
	}

	public void setSkuSpecId(Long skuSpecId) {
		this.skuSpecId = skuSpecId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getWithHoldQuantity() {
		return withHoldQuantity;
	}

	public void setWithHoldQuantity(Long withHoldQuantity) {
		this.withHoldQuantity = withHoldQuantity;
	}
	
	
	
}
