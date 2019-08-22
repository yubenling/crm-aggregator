package com.kycrm.member.domain.entity.item;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

/** 
* @ClassName: Item 
* @Description 商品信息
* @author jackstraw_yu
* @date 2018年1月29日 下午2:15:07 
*/
public class Item implements Serializable {

	private static final long serialVersionUID = -3920083687855728050L;

	/**
	 * 主键id<br/>
	 */
	private Long id;
	
	/**
	 * 卖家昵称<br/>
	 * column:user_name
	 */
	private String userName;
	/**
	 * 废弃or使用userName代替
	 * 卖家nick
	 * column:nick
	 * private String nick;
	 */
	
	/**
	 * 卖家id
	 */
	private Long uid;
	
	/**
	 * 商品数字id<br/>
	 * column:num_iid
	 */
	@JsonSerialize(using=ToStringSerializer.class)
	private Long numIid;

	/**
	 * 商品上传后的状态。onsale出售中，instock库中<br/>
	 * column:approve_status
	 */
	private String approveStatus;

	/**
	 * 商品类目ID<br/>
	 * column:cid
	 */
	private String cid;

	/**
	 * 橱窗推荐,true/false<br/>
	 * column:has_showcase
	 */
	private String hasShowcase;

	/**
	 * 支持会员打折,true/false<br/>
	 * column:has_discount
	 */
	private String hasDiscount;

	/**
	 * 发布时间<br/>
	 * column:created
	 */
	private Date created;

	/**
	 * 商品修改时间<br/>
	 * column:modified
	 */
	private Date modified;

	/**
	 * 商品所属的店铺内卖家自定义类目列表<br/>
	 * column:seller_cids
	 */
	private String sellerCids;

	/**
	 * 商品属性 格式：pid:vid;pid:vid<br/>
	 * column:props
	 */
	private String props;

	/**
	 * 用户自行输入的类目属性ID串。<br/>
	 * 结构：'pid1,pid2,pid3'，如：'20000'（表示品牌） 注：通常一个类目下用户可输入的关键属性不超过1个。<br/>
	 * column:input_pids
	 */
	private String inputPids;

	/**
	 * 用户自行输入的子属性名和属性值，<br/>
	 * 结构:'父属性值;一级子属性名;一级子属性值;二级子属性名;<br/>
	 * 自定义输入值,....',如：'耐克;耐克系列;科比系列;科比系列;2K5'，input_str需要与input_pids一一对应，注：通常一个类目下用户可输入的关键属性不超过1个。所有属性别名加起来不能超过 3999字节。<br/>
	 * column:input_str
	 */
	private String inputStr;

	/**
	 * 商品数量<br/>
	 * column:num
	 */
	private Integer num;

	/**
	 * 上架时间<br/>
	 * column:list_time
	 */
	private Date listTime;

	/**
	 * 下架时间<br/>
	 * column:delist_time
	 */
	private Date delistTime;

	/**
	 * 商品新旧程度(全新:new，闲置:unused，二手：second)
	 * column:stuff_status
	 */
	private String stuffStatus;

	/**
	 * 邮政编码
	 * column:zip
	 */
	private String zip;

	/**
	 * 详细地址，最大256个字节（128个中文）
	 * column:address
	 */
	private String address;

	/**
	 * 所在城市（中文名称）<br/>
	 * column:city
	 */
	private String city;

	/**
	 * 所在省份（中文名称）<br/>
	 * column:state
	 */
	private String state;

	/**
	 * 国家名称<br/>
	 * column:country
	 */
	private String country;

	/**
	 * 区/县（只适用于物流API）<br/>
	 * column:district
	 */
	private String district;

	/**
	 * 运费承担方式,seller（卖家承担），buyer（买家承担）
	 * column:freight_payer
	 */
	private String freightPayer;

	/**
	 * 图片路径
	 * column:url
	 */
	private String url;

	/**
	 * 是否是3D淘宝的商品
	 * column:is_3D
	 */
	private Boolean is3D;

	/**
	 * 商品所属卖家的信用等级数，<br/>
	 * 1表示1心，2表示2心……，只有调用商品搜索:taobao.items.get和taobao.items.search的时候才能返回<br/>
	 * column:score
	 */
	private Integer score;

	/**
	 * 是否承诺退换货服务!<br/>
	 * column:sell_promise
	 */
	private Boolean sellPromise;
	
	/**
	 * 商品标题,不能超过60字节<br/>
	 * column:title
	 */
	private String title;
	
	/**
	 * 商品类型(fixed:一口价;auction:拍卖)注：取消团购<br/>
	 * column:type
	 */
	private String type;
	
	/**
	 * 商品描述, 字数要大于5个字符，小于25000个字符<br/>
	 * column:item_desc
	 */
	private String itemDesc;
	
	/**
	 * sku的id<br/>
	 * column:sku_id
	 */
	private Long skuId;

	/**
	 * sku的销售属性组合字符串（颜色，大小，等等，可通过类目API获取某类目下的销售属性）,格式是p1:v1;p2:v2<br/>
	 * column:properties
	 */
	private String properties;

	/**
	 * 属于这个sku的商品的数量<br/>
	 * column:quantity
	 */
	private Integer quantity;

	/**
	 * 属于这个sku的商品的价格 取值范围:0-100000000;精确到2位小数;单位:元。如:200.07，表示:200元7分。<br/>
	 * column:sku_price
	 */
	private String skuPrice;

	/**
	 * sku创建日期 时间格式：yyyy-MM-dd HH:mm:ss<br/>
	 * column:sku_created
	 */
	private String skuCreated;

	/**
	 * sku最后修改日期 时间格式：yyyy-MM-dd HH:mm:ss<br/>
	 * column:sku_modified
	 */
	private String skuModified;

	/**
	 * sku状态。 normal:正常 ；delete:删除<br/>
	 * column:sku_status
	 */
	private String skuStatus;

	/**
	 * sku所对应的销售属性的中文名字串，格式如：pid1:vid1:pid_name1:vid_name1;pid2:vid2:pid_name2:vid_name2……<br/>
	 * column:sku_properties_name
	 */
	private String skuPropertiesName;

	/**
	 * 表示SKu上的产品规格信息<br/>
	 * column:sku_spec_id
	 */
	private Integer skuSpecId;

	/**
	 * 商品在付款减库存的状态下，该sku上未付款的订单数量<br/>
	 * 与 withHoldQuantity重复 所以采用skuWithHoldQuantity
	 * column:sku_with_hold_quantity
	 */
	private Integer skuWithHoldQuantity;

	/**
	 * sku级别发货时间<br/>
	 * column:sku_delivery_time
	 */
	private String skuDeliveryTime;

	/**
	 * 基础色数据<br/>
	 * column:change_prop
	 */
	private String changeProp;

	/**
	 * 商品属性名称。标识着props内容里面的pid和vid所对应的名称。<br/>
	 * 格式为：pid1:vid1:pid_name1:vid_name1;pid2:vid2:pid_name2:vid_name2……<br/>
	 * column:props_name
	 */
	private String propsName;

	/**
	 * 消保类型，多个类型以,分割。可取以下值：2：假一赔三；4：7天无理由退换货<br/>
	 * column:promoted_service
	 */
	private String promotedService;

	/**
	 * 是否24小时闪电发货<br/>
	 * column:is_lightning_consignment
	 */
	private Boolean isLightningConsignment;

	/**
	 * 非分销商品：0，代销：1，经销：2<br/>
	 * column:is_fenxiao
	 */
	private Integer isFenxiao;

	/**
	 * 商品的积分返点比例。如:5,表示:返点比例0.5%商品的积分返点比例。如:5,表示:返点比例0.5%<br/>
	 * column:auction_point
	 */
	private Integer auctionPoint;

	/**
	 * 属性值别名,比如颜色的自定义名称<br/>
	 * column:property_alias
	 */
	private String propertyAlias;

	/**
	 * 页面模板id<br/>
	 * column:template_id
	 */
	private String templateId;

	/**
	 * 售后服务ID,该字段仅在taobao.item.get接口中返回<br/>
	 * column:after_sale_id
	 */
	private Long afterSaleId;

	/**
	 * 标示商品是否为新品。值含义：true-是，false-否。<br/>
	 * column:is_xinpin
	 */
	private Boolean isXinpin;

	/**
	 * 商品是否支持拍下减库存:1支持;2取消支持(付款减库存);0(默认)不更改 集市卖家默认拍下减库存; 商城卖家默认付款减库存<br/>
	 * column:sub_stock
	 */
	private Integer subStock;

	/**
	 * 宝贝特征值，只有在Top支持的特征值才能保存到宝贝上<br/>
	 * column:features
	 */
	private String features;

	/**
	 * 商品的重量，用于按重量计费的运费模板。注意：单位为kg<br/>
	 * column:item_weight
	 */
	private String itemWeight;

	/**
	 * 表示商品的体积，用于按体积计费的运费模板。该值的单位为立方米（m3）。<br/>
	 * 该值支持两种格式的设置：格式1：bulk:3,单位为立方米(m3),表示直接设置为商品的体积。<br/>
	 * 格式2：weight:10;breadth:10;height:10，单位为米（m）<br/>
	 * column:item_size
	 */
	private String itemSize;

	/**
	 * 预扣库存，即付款减库存的商品现在有多少处于未付款状态的订单<br/>
	 * column:with_hold_quantity 被删
	 * 废弃 与 skuWithHoldQuantity重复 所以采用 skuWithHoldQuantity
	 */
	private Integer withHoldQuantity;

	/**
	 * 商品卖点信息，天猫商家使用字段，最长150个字符<br/>
	 * column:sell_point
	 */
	private String sellPoint;

	/**
	 * 有效期,7或者14（默认是7天）<br/>
	 * column:valid_thru
	 */
	private Integer validThru;

	/**商家外部编码(可与商家外部系统对接)。需要授权才能获取。
	* column:outer_id*/
	private String outerId;

	/**
	 * 代充商品类型。在代充商品的类目下，不传表示不标记商品类型（交易搜索中就不能通过标记搜到相关的交易了）。<br/>
	 * 可选类型： no_mark(不做类型标记) time_card(点卡软件代充) fee_card(话费软件代充)
	 * column:auto_fill
	 */
	private String autoFill;

	/**
	 * 商品描述模块化，模块列表，由List转化成jsonArray存入，后端逻辑验证通过，拼装成模块内容+锚点导航后存入desc中。<br/>
	 * 数据结构具体参见Item_Desc_Module
	 * column:desc_modules
	 */
	private String descModules;

	/**
	 * 定制工具Id<br/>
	 * column:custom_made_type_id
	 */
	private String customMadeTypeId;

	/**
	 * 无线的宝贝描述<br/>
	 * column:wireless_desc
	 */
	private String wirelessDesc;

	/**
	 * 商品级别的条形码<br/>
	 * column:barcode
	 */
	private String barcode;

	/**
	 * 是否为新消保法中的7天无理由退货<br/>
	 * column:newprepay
	 */
	private String newprepay;

	/**
	 * 商品价格，格式：5.00；单位：元；精确到：分<br/>
	 * column:price
	 */
	private String price;

	/**
	 * 平邮费用,格式：5.00；单位：元；精确到：分<br/>
	 * column:post_fee
	 */
	private String postFee;

	/**
	 * 快递费用,格式：5.00；单位：元；精确到：分<br/>
	 * column:express_fee
	 */
	private String expressFee;

	/**
	 * ems费用,格式：5.00；单位：元；精确到：分<br/>
	 * column:ems_fee
	 */
	private String emsFee;

	/**
	 * 全球购商品采购地信息（库存类型），有两种库存类型：现货和代购;参数值为1时代表现货，值为2时代表代购<br/>
	 * column:global_stock_type
	 */
	private String globalStockType;

	/**
	 * 全球购商品采购地信息（地区/国家），代表全球购商品的产地信息。<br/>
	 * column:global_stock_country
	 */
	private String globalStockCountry;

	/**
	 * 门店大屏图<br/>
	 * column:large_screen_image_url
	 */
	private String largeScreenImageUrl;
	
	private String subTitle;

	/**
     * 版本<br/>
     * column:optlock
     */
    private Integer version = 0;

    /**
     * 创建者<br/>
     * column:createdBy
     */
    private String createdBy;

    /**
     * 创建时间<br/>
     * column:createdDate
     */
    protected Date createdDate;

    /**
     * 最后修改者<br/>
     * column:lastModifiedBy
     */
    private String lastModifiedBy;

    /**
     * 最后修改时间<br/>
     * column:lastModifiedDate
     */
    private Date lastModifiedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	public Long getNumIid() {
		return numIid;
	}

	public void setNumIid(Long numIid) {
		this.numIid = numIid;
	}

	public String getApproveStatus() {
		return approveStatus;
	}

	public void setApproveStatus(String approveStatus) {
		this.approveStatus = approveStatus;
	}

	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getHasShowcase() {
		return hasShowcase;
	}

	public void setHasShowcase(String hasShowcase) {
		this.hasShowcase = hasShowcase;
	}

	public String getHasDiscount() {
		return hasDiscount;
	}

	public void setHasDiscount(String hasDiscount) {
		this.hasDiscount = hasDiscount;
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

	public String getSellerCids() {
		return sellerCids;
	}

	public void setSellerCids(String sellerCids) {
		this.sellerCids = sellerCids;
	}

	public String getProps() {
		return props;
	}

	public void setProps(String props) {
		this.props = props;
	}

	public String getInputPids() {
		return inputPids;
	}

	public void setInputPids(String inputPids) {
		this.inputPids = inputPids;
	}

	public String getInputStr() {
		return inputStr;
	}

	public void setInputStr(String inputStr) {
		this.inputStr = inputStr;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public Date getListTime() {
		return listTime;
	}

	public void setListTime(Date listTime) {
		this.listTime = listTime;
	}

	public Date getDelistTime() {
		return delistTime;
	}

	public void setDelistTime(Date delistTime) {
		this.delistTime = delistTime;
	}

	public String getStuffStatus() {
		return stuffStatus;
	}

	public void setStuffStatus(String stuffStatus) {
		this.stuffStatus = stuffStatus;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getFreightPayer() {
		return freightPayer;
	}

	public void setFreightPayer(String freightPayer) {
		this.freightPayer = freightPayer;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getIs3D() {
		return is3D;
	}


	public void setIs3D(Boolean is3d) {
		is3D = is3d;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public Boolean getSellPromise() {
		return sellPromise;
	}

	public void setSellPromise(Boolean sellPromise) {
		this.sellPromise = sellPromise;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getItemDesc() {
		return itemDesc;
	}

	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
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

	public String getSkuPrice() {
		return skuPrice;
	}

	public void setSkuPrice(String skuPrice) {
		this.skuPrice = skuPrice;
	}

	public String getSkuCreated() {
		return skuCreated;
	}

	public void setSkuCreated(String skuCreated) {
		this.skuCreated = skuCreated;
	}

	public String getSkuModified() {
		return skuModified;
	}

	public void setSkuModified(String skuModified) {
		this.skuModified = skuModified;
	}

	public String getSkuStatus() {
		return skuStatus;
	}

	public void setSkuStatus(String skuStatus) {
		this.skuStatus = skuStatus;
	}

	public String getSkuPropertiesName() {
		return skuPropertiesName;
	}

	public void setSkuPropertiesName(String skuPropertiesName) {
		this.skuPropertiesName = skuPropertiesName;
	}

	public Integer getSkuSpecId() {
		return skuSpecId;
	}

	public void setSkuSpecId(Integer skuSpecId) {
		this.skuSpecId = skuSpecId;
	}

	public Integer getSkuWithHoldQuantity() {
		return skuWithHoldQuantity;
	}

	public void setSkuWithHoldQuantity(Integer skuWithHoldQuantity) {
		this.skuWithHoldQuantity = skuWithHoldQuantity;
	}

	public String getSkuDeliveryTime() {
		return skuDeliveryTime;
	}

	public void setSkuDeliveryTime(String skuDeliveryTime) {
		this.skuDeliveryTime = skuDeliveryTime;
	}

	public String getChangeProp() {
		return changeProp;
	}

	public void setChangeProp(String changeProp) {
		this.changeProp = changeProp;
	}

	public String getPropsName() {
		return propsName;
	}

	public void setPropsName(String propsName) {
		this.propsName = propsName;
	}

	public String getPromotedService() {
		return promotedService;
	}

	public void setPromotedService(String promotedService) {
		this.promotedService = promotedService;
	}

	public Boolean getIsLightningConsignment() {
		return isLightningConsignment;
	}

	public void setIsLightningConsignment(Boolean isLightningConsignment) {
		this.isLightningConsignment = isLightningConsignment;
	}

	public Integer getIsFenxiao() {
		return isFenxiao;
	}

	public void setIsFenxiao(Integer isFenxiao) {
		this.isFenxiao = isFenxiao;
	}

	public Integer getAuctionPoint() {
		return auctionPoint;
	}

	public void setAuctionPoint(Integer auctionPoint) {
		this.auctionPoint = auctionPoint;
	}

	public String getPropertyAlias() {
		return propertyAlias;
	}

	public void setPropertyAlias(String propertyAlias) {
		this.propertyAlias = propertyAlias;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public Long getAfterSaleId() {
		return afterSaleId;
	}

	public void setAfterSaleId(Long afterSaleId) {
		this.afterSaleId = afterSaleId;
	}

	public Boolean getIsXinpin() {
		return isXinpin;
	}

	public void setIsXinpin(Boolean isXinpin) {
		this.isXinpin = isXinpin;
	}

	public Integer getSubStock() {
		return subStock;
	}

	public void setSubStock(Integer subStock) {
		this.subStock = subStock;
	}

	public String getFeatures() {
		return features;
	}

	public void setFeatures(String features) {
		this.features = features;
	}

	public String getItemWeight() {
		return itemWeight;
	}

	public void setItemWeight(String itemWeight) {
		this.itemWeight = itemWeight;
	}

	public String getItemSize() {
		return itemSize;
	}

	public void setItemSize(String itemSize) {
		this.itemSize = itemSize;
	}

	public String getSellPoint() {
		return sellPoint;
	}

	public void setSellPoint(String sellPoint) {
		this.sellPoint = sellPoint;
	}

	public Integer getValidThru() {
		return validThru;
	}

	public void setValidThru(Integer validThru) {
		this.validThru = validThru;
	}

	public String getAutoFill() {
		return autoFill;
	}

	public void setAutoFill(String autoFill) {
		this.autoFill = autoFill;
	}

	public String getDescModules() {
		return descModules;
	}

	public void setDescModules(String descModules) {
		this.descModules = descModules;
	}

	public String getCustomMadeTypeId() {
		return customMadeTypeId;
	}

	public void setCustomMadeTypeId(String customMadeTypeId) {
		this.customMadeTypeId = customMadeTypeId;
	}

	public String getWirelessDesc() {
		return wirelessDesc;
	}

	public void setWirelessDesc(String wirelessDesc) {
		this.wirelessDesc = wirelessDesc;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public String getNewprepay() {
		return newprepay;
	}

	public void setNewprepay(String newprepay) {
		this.newprepay = newprepay;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getPostFee() {
		return postFee;
	}

	public void setPostFee(String postFee) {
		this.postFee = postFee;
	}

	public String getExpressFee() {
		return expressFee;
	}

	public void setExpressFee(String expressFee) {
		this.expressFee = expressFee;
	}

	public String getEmsFee() {
		return emsFee;
	}

	public void setEmsFee(String emsFee) {
		this.emsFee = emsFee;
	}

	public String getGlobalStockType() {
		return globalStockType;
	}

	public void setGlobalStockType(String globalStockType) {
		this.globalStockType = globalStockType;
	}

	public String getGlobalStockCountry() {
		return globalStockCountry;
	}

	public void setGlobalStockCountry(String globalStockCountry) {
		this.globalStockCountry = globalStockCountry;
	}

	public String getLargeScreenImageUrl() {
		return largeScreenImageUrl;
	}

	public void setLargeScreenImageUrl(String largeScreenImageUrl) {
		this.largeScreenImageUrl = largeScreenImageUrl;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getLastModifiedBy() {
		return lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public Date getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
    
	public Integer getWithHoldQuantity() {
		return withHoldQuantity;
	}

	public void setWithHoldQuantity(Integer withHoldQuantity) {
		this.withHoldQuantity = withHoldQuantity;
	}

	public String getOuterId() {
		return outerId;
	}

	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", userName=" + userName + ", uid=" + uid
				+ ", numIid=" + numIid + ", approveStatus=" + approveStatus
				+ ", cid=" + cid + ", hasShowcase=" + hasShowcase
				+ ", hasDiscount=" + hasDiscount + ", created=" + created
				+ ", modified=" + modified + ", sellerCids=" + sellerCids
				+ ", props=" + props + ", inputPids=" + inputPids
				+ ", inputStr=" + inputStr + ", num=" + num + ", listTime="
				+ listTime + ", delistTime=" + delistTime + ", stuffStatus="
				+ stuffStatus + ", zip=" + zip + ", address=" + address
				+ ", city=" + city + ", state=" + state + ", country="
				+ country + ", district=" + district + ", freightPayer="
				+ freightPayer + ", url=" + url + ", is3D=" + is3D + ", score="
				+ score + ", sellPromise=" + sellPromise + ", title=" + title
				+ ", type=" + type + ", itemDesc=" + itemDesc + ", skuId="
				+ skuId + ", properties=" + properties + ", quantity="
				+ quantity + ", skuPrice=" + skuPrice + ", skuCreated="
				+ skuCreated + ", skuModified=" + skuModified + ", skuStatus="
				+ skuStatus + ", skuPropertiesName=" + skuPropertiesName
				+ ", skuSpecId=" + skuSpecId + ", skuWithHoldQuantity="
				+ skuWithHoldQuantity + ", skuDeliveryTime=" + skuDeliveryTime
				+ ", changeProp=" + changeProp + ", propsName=" + propsName
				+ ", promotedService=" + promotedService
				+ ", isLightningConsignment=" + isLightningConsignment
				+ ", isFenxiao=" + isFenxiao + ", auctionPoint=" + auctionPoint
				+ ", propertyAlias=" + propertyAlias + ", templateId="
				+ templateId + ", afterSaleId=" + afterSaleId + ", isXinpin="
				+ isXinpin + ", subStock=" + subStock + ", features="
				+ features + ", itemWeight=" + itemWeight + ", itemSize="
				+ itemSize + ", withHoldQuantity=" + withHoldQuantity
				+ ", sellPoint=" + sellPoint + ", validThru=" + validThru
				+ ", outerId=" + outerId + ", autoFill=" + autoFill
				+ ", descModules=" + descModules + ", customMadeTypeId="
				+ customMadeTypeId + ", wirelessDesc=" + wirelessDesc
				+ ", barcode=" + barcode + ", newprepay=" + newprepay
				+ ", price=" + price + ", postFee=" + postFee + ", expressFee="
				+ expressFee + ", emsFee=" + emsFee + ", globalStockType="
				+ globalStockType + ", globalStockCountry="
				+ globalStockCountry + ", largeScreenImageUrl="
				+ largeScreenImageUrl + ", subTitle=" + subTitle + ", version="
				+ version + ", createdBy=" + createdBy + ", createdDate="
				+ createdDate + ", lastModifiedBy=" + lastModifiedBy
				+ ", lastModifiedDate=" + lastModifiedDate + "]";
	}

	
	
    
	/*
	 * private Integer itemCount;(老版迁移后，注释掉)//满足查询条件的商品数目，作为分页的依据
	 * 下面的属性在原来的实体中被注释掉。。。
	 * @MetaData(value="商品ID")
	 * 
	 * @Column(name="COMMODITY_ID") private String commodityId;
	 * 
	 * 
	 * @MetaData(value="淘宝ID")
	 * 
	 * @Column(name="USER_ID") private String userId;
	 * 
	 * @MetaData(value="父ID")
	 * 
	 * @Column(name="PARENT_ID") private Long parentId;
	 * 
	 * @MetaData(value="品牌ID")
	 * 
	 * @Column(name="BRAND_ID") private String brandId;
	 * 
	 * @MetaData(value="淘宝昵称")
	 * 
	 * @Column(name="USER_NICK") private String userNick;
	 * 
	 * @MetaData(value="商品名称")
	 * 
	 * @Column(name="NAME") private String name;
	 * 
	 * @MetaData(value="前台商品名称")
	 * 
	 * @Column(name="TITLE") private String title;
	 * 
	 * @MetaData(value="商家编码")
	 * 
	 * @Column(name="ITEM_CODE") private String itemCode;
	 * 
	 * @MetaData(value="是否sku")
	 * 
	 * @Column(name="IS_SKU") private Boolean isSku = Boolean.FALSE;
	 * 
	 * @MetaData(value="标记")
	 * 
	 * @Column(name="FLAG") private String flag;
	 * 
	 * @MetaData(value="商品类型 1-普通类型 2-组合商品 2-分销商")
	 * 
	 * @Column(name="TYPE") private String type;
	 * 
	 * @MetaData(value="商品备注")
	 * 
	 * @Column(name="REMARK") private String remark;
	 * 
	 * @MetaData(value="状态 1-有效 2-锁定")
	 * 
	 * @Column(name="STATUS") private String status;
	 * 
	 * @MetaData(value="发布版本号")
	 * 
	 * @Column(name="PUBLISH_VERSION") private Long publishVersion;
	 * 
	 * @MetaData(value="创建人")
	 * 
	 * @Column(name="CREATOR") private String creator;
	 * 
	 * @MetaData(value="创建时间")
	 * 
	 * @Column(name="GMT_CREATE") private Date gmtCreate;
	 * 
	 * @MetaData(value="最后修改人")
	 * 
	 * @Column(name="LAST_MODIFIER") private String lastModifier;
	 * 
	 * @MetaData(value="修改日期")
	 * 
	 * @Column(name="GMT_MODIFIED") private Date gmtModified;
	 * 
	 * @MetaData(value="属性")
	 * 
	 * @Column(name="PROPERTIES") private String properties;
	 * 
	 * @MetaData(value="是否易碎")
	 * 
	 * @Column(name="IS_FRIABLE") private Boolean isFriable;
	 * 
	 * @MetaData(value="是否危险品")
	 * 
	 * @Column(name="IS_DANGEROUS") private Boolean isDangerous = Boolean.FALSE;
	 * 
	 * @MetaData(value="颜色")
	 * 
	 * @Column(name="COLOR") private String color;
	 * 
	 * @MetaData(value="重量")
	 * 
	 * @Column(name="WEIGHT") private Long weight;
	 * 
	 * @MetaData(value="长度")
	 * 
	 * @Column(name="LENGTH") private Long length;
	 * 
	 * @MetaData(value="宽度")
	 * 
	 * @Column(name="WIDTH") private Long width;
	 * 
	 * @MetaData(value="高度")
	 * 
	 * @Column(name="HEIGHT") private Long height;
	 * 
	 * @MetaData(value="立方mm")
	 * 
	 * @Column(name="VOLUME") private Long volume;
	 * 
	 * @MetaData(value="货类")
	 * 
	 * @Column(name="GOODS_CAT") private String goodsCat;
	 * 
	 * @MetaData(value="计价货类")
	 * 
	 * @Column(name="PRICING_CAT") private String pricingCat;
	 * 
	 * @MetaData(value="包装材料")
	 * 
	 * @Column(name="PACKAGE_MATERIAL") private String packageMaterial;
	 * 
	 * @MetaData(value="价格")
	 * 
	 * @Column(name="PRICE") private Long price;
	 */
	
	


	
	
}
