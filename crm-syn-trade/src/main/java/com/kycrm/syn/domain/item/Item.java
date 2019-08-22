package com.kycrm.syn.domain.item;

import java.util.Date;

public class Item {
    private Long id;

    private Long uid;

    private String userName;

    private Long numIid;

    private String approveStatus;

    private String cid;

    private String hasShowcase;

    private String hasDiscount;

    private Date created;

    private Date modified;

    private String sellerCids;

    private String props;

    private String inputPids;

    private String inputStr;

    private Integer num;

    private Date listTime;

    private Date delistTime;

    private Date stuffStatus;

    private String zip;

    private String address;

    private String city;

    private String state;

    private String country;

    private String district;

    private String freightPayer;

    private String url;

    private Byte is3d;

    private Integer score;

    private Byte sellPromise;

    private String title;

    private String type;

    private String itemDesc;

    private Long skuId;

    private String properties;

    private Integer quantity;

    private String skuPrice;

    private String skuCreated;

    private String skuModified;

    private String skuStatus;

    private String skuPropertiesName;

    private Integer skuSpecId;

    private Integer skuWithHoldQuantity;

    private String skuDeliveryTime;

    private String changeProp;

    private String propsName;

    private String promotedService;

    private Byte isLightningConsignment;

    private Integer isFenxiao;

    private Integer auctionPoint;

    private String propertyAlias;

    private String templateId;

    private Long afterSaleId;

    private Byte isXinpin;

    private Integer subStock;

    private String features;

    private String itemWeight;

    private String itemSize;

    private Integer withHoldQuantity;

    private String sellPoint;

    private Integer validThru;

    private String autoFill;

    private String descModules;

    private String customMadeTypeId;

    private String wirelessDesc;

    private String barcode;

    private String newprepay;

    private String price;

    private String postFee;

    private String expressFee;

    private String emsFee;

    private String globalStockType;

    private String globalStockCountry;

    private String largeScreenImageUrl;

    private Integer optlock;

    private String createdby;

    private Date createddate;

    private String lastmodifiedby;

    private Date lastmodifieddate;

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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
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
        this.approveStatus = approveStatus == null ? null : approveStatus.trim();
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid == null ? null : cid.trim();
    }

    public String getHasShowcase() {
        return hasShowcase;
    }

    public void setHasShowcase(String hasShowcase) {
        this.hasShowcase = hasShowcase == null ? null : hasShowcase.trim();
    }

    public String getHasDiscount() {
        return hasDiscount;
    }

    public void setHasDiscount(String hasDiscount) {
        this.hasDiscount = hasDiscount == null ? null : hasDiscount.trim();
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
        this.sellerCids = sellerCids == null ? null : sellerCids.trim();
    }

    public String getProps() {
        return props;
    }

    public void setProps(String props) {
        this.props = props == null ? null : props.trim();
    }

    public String getInputPids() {
        return inputPids;
    }

    public void setInputPids(String inputPids) {
        this.inputPids = inputPids == null ? null : inputPids.trim();
    }

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr == null ? null : inputStr.trim();
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

    public Date getStuffStatus() {
        return stuffStatus;
    }

    public void setStuffStatus(Date stuffStatus) {
        this.stuffStatus = stuffStatus;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip == null ? null : zip.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public String getFreightPayer() {
        return freightPayer;
    }

    public void setFreightPayer(String freightPayer) {
        this.freightPayer = freightPayer == null ? null : freightPayer.trim();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Byte getIs3d() {
        return is3d;
    }

    public void setIs3d(Byte is3d) {
        this.is3d = is3d;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Byte getSellPromise() {
        return sellPromise;
    }

    public void setSellPromise(Byte sellPromise) {
        this.sellPromise = sellPromise;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc == null ? null : itemDesc.trim();
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
        this.properties = properties == null ? null : properties.trim();
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
        this.skuPrice = skuPrice == null ? null : skuPrice.trim();
    }

    public String getSkuCreated() {
        return skuCreated;
    }

    public void setSkuCreated(String skuCreated) {
        this.skuCreated = skuCreated == null ? null : skuCreated.trim();
    }

    public String getSkuModified() {
        return skuModified;
    }

    public void setSkuModified(String skuModified) {
        this.skuModified = skuModified == null ? null : skuModified.trim();
    }

    public String getSkuStatus() {
        return skuStatus;
    }

    public void setSkuStatus(String skuStatus) {
        this.skuStatus = skuStatus == null ? null : skuStatus.trim();
    }

    public String getSkuPropertiesName() {
        return skuPropertiesName;
    }

    public void setSkuPropertiesName(String skuPropertiesName) {
        this.skuPropertiesName = skuPropertiesName == null ? null : skuPropertiesName.trim();
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
        this.skuDeliveryTime = skuDeliveryTime == null ? null : skuDeliveryTime.trim();
    }

    public String getChangeProp() {
        return changeProp;
    }

    public void setChangeProp(String changeProp) {
        this.changeProp = changeProp == null ? null : changeProp.trim();
    }

    public String getPropsName() {
        return propsName;
    }

    public void setPropsName(String propsName) {
        this.propsName = propsName == null ? null : propsName.trim();
    }

    public String getPromotedService() {
        return promotedService;
    }

    public void setPromotedService(String promotedService) {
        this.promotedService = promotedService == null ? null : promotedService.trim();
    }

    public Byte getIsLightningConsignment() {
        return isLightningConsignment;
    }

    public void setIsLightningConsignment(Byte isLightningConsignment) {
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
        this.propertyAlias = propertyAlias == null ? null : propertyAlias.trim();
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId == null ? null : templateId.trim();
    }

    public Long getAfterSaleId() {
        return afterSaleId;
    }

    public void setAfterSaleId(Long afterSaleId) {
        this.afterSaleId = afterSaleId;
    }

    public Byte getIsXinpin() {
        return isXinpin;
    }

    public void setIsXinpin(Byte isXinpin) {
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
        this.features = features == null ? null : features.trim();
    }

    public String getItemWeight() {
        return itemWeight;
    }

    public void setItemWeight(String itemWeight) {
        this.itemWeight = itemWeight == null ? null : itemWeight.trim();
    }

    public String getItemSize() {
        return itemSize;
    }

    public void setItemSize(String itemSize) {
        this.itemSize = itemSize == null ? null : itemSize.trim();
    }

    public Integer getWithHoldQuantity() {
        return withHoldQuantity;
    }

    public void setWithHoldQuantity(Integer withHoldQuantity) {
        this.withHoldQuantity = withHoldQuantity;
    }

    public String getSellPoint() {
        return sellPoint;
    }

    public void setSellPoint(String sellPoint) {
        this.sellPoint = sellPoint == null ? null : sellPoint.trim();
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
        this.autoFill = autoFill == null ? null : autoFill.trim();
    }

    public String getDescModules() {
        return descModules;
    }

    public void setDescModules(String descModules) {
        this.descModules = descModules == null ? null : descModules.trim();
    }

    public String getCustomMadeTypeId() {
        return customMadeTypeId;
    }

    public void setCustomMadeTypeId(String customMadeTypeId) {
        this.customMadeTypeId = customMadeTypeId == null ? null : customMadeTypeId.trim();
    }

    public String getWirelessDesc() {
        return wirelessDesc;
    }

    public void setWirelessDesc(String wirelessDesc) {
        this.wirelessDesc = wirelessDesc == null ? null : wirelessDesc.trim();
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode == null ? null : barcode.trim();
    }

    public String getNewprepay() {
        return newprepay;
    }

    public void setNewprepay(String newprepay) {
        this.newprepay = newprepay == null ? null : newprepay.trim();
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price == null ? null : price.trim();
    }

    public String getPostFee() {
        return postFee;
    }

    public void setPostFee(String postFee) {
        this.postFee = postFee == null ? null : postFee.trim();
    }

    public String getExpressFee() {
        return expressFee;
    }

    public void setExpressFee(String expressFee) {
        this.expressFee = expressFee == null ? null : expressFee.trim();
    }

    public String getEmsFee() {
        return emsFee;
    }

    public void setEmsFee(String emsFee) {
        this.emsFee = emsFee == null ? null : emsFee.trim();
    }

    public String getGlobalStockType() {
        return globalStockType;
    }

    public void setGlobalStockType(String globalStockType) {
        this.globalStockType = globalStockType == null ? null : globalStockType.trim();
    }

    public String getGlobalStockCountry() {
        return globalStockCountry;
    }

    public void setGlobalStockCountry(String globalStockCountry) {
        this.globalStockCountry = globalStockCountry == null ? null : globalStockCountry.trim();
    }

    public String getLargeScreenImageUrl() {
        return largeScreenImageUrl;
    }

    public void setLargeScreenImageUrl(String largeScreenImageUrl) {
        this.largeScreenImageUrl = largeScreenImageUrl == null ? null : largeScreenImageUrl.trim();
    }

    public Integer getOptlock() {
        return optlock;
    }

    public void setOptlock(Integer optlock) {
        this.optlock = optlock;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby == null ? null : createdby.trim();
    }

    public Date getCreateddate() {
        return createddate;
    }

    public void setCreateddate(Date createddate) {
        this.createddate = createddate;
    }

    public String getLastmodifiedby() {
        return lastmodifiedby;
    }

    public void setLastmodifiedby(String lastmodifiedby) {
        this.lastmodifiedby = lastmodifiedby == null ? null : lastmodifiedby.trim();
    }

    public Date getLastmodifieddate() {
        return lastmodifieddate;
    }

    public void setLastmodifieddate(Date lastmodifieddate) {
        this.lastmodifieddate = lastmodifieddate;
    }
}