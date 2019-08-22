package com.kycrm.member.domain.entity.tradecenter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

/** 
* @author wy
* @version 创建时间：2018年1月4日 下午4:10:00
*/
public class TradeSetup implements Serializable,Comparable<TradeSetup>{

    private static final long serialVersionUID = 6120845944295133347L;

    /**
     * 发送时间:
     * 下单关怀:没有发送时间(符合条件,立即发送??);
     * 常规催付,二次催付,聚划算催付(多久发送);
     * 付款关怀:"付款后立即发送付款关怀";
     * 发货提醒:"订单状态变为卖家已发货是发送提醒";
     * 延时发货提醒:"付款后,请选择***未发货进行安抚"(多久发送);
     * 到达同城提醒:"物流显示到达客户所在城市时发送";
     * 派件提醒:"物流显示派送时发送";
     * 签收提醒:"物流显示签收时发送";
     * 宝贝关怀:"物流显示签收后,请选择***发送宝贝关怀"(多久发送);
     * 回款提醒:"收货后,请输入***请选择**(timeUnit)*发送回款提醒"(多久发送);
     * 退款关怀:"买家发起退款申请后发送";
     * =========
     * 好评管理:"确认收货后请选择***未进行评价"
     * 中差评安抚:"接收到买家中差评时"
     * =======
     * 自动评价/中差评监控
     * */
    
    /**
     * 主键
     */
    private Long id;
    
    /**
     * 用户昵称<br/>
     * column:user_Name
     */
    private String userId;
    
    /**
     * 用户主键id<br/>
     * column:uid
     */
    private Long uid;
    
    /**
	 * 设置类型:<br/>
	 * 5-预售催付,10-疑难件提醒 (废弃!)<br/>
	 * 1-下单关怀 ,2-常规催付,3-二次催付,4-聚划算催付 ,6-发货提醒,7-到达同城提醒 ,8-派件提醒<br/> 
	 * 9-签收提醒,11-延时发货提醒,12-宝贝关怀 ,13-付款关怀 ,14-回款提醒 ,15-退款关怀<br/>  
	 * 16-自动评价 ,17-批量评价 ,18-评价记录 ,19-中差管理 ,20-中差评监控 ,21-中差评安抚 ,22-中差评统计<br/> 
	 * 23-中差评原因 ,24中差评原因设置 ,25-中差评原因分析 ,26-手动订单提醒, 27-优秀催付案例 <br/> 
	 * 28-效果统计,29-买家申请退款 ,30-退款成功 31-等待退货, 32-拒绝退款 ,33-会员短信群发 <br/> 
	 * 34-指定号码群发 ,35-订单短信群发 ,36-会员互动<br/>
	 * column:type
	 */
    private String type;
    
    /**
     * 设置&任务级别<br/>
     * column:task_level
     */
    private Integer taskLevel;

    /**
     * 设置&任务名称<br/>
     * column:task_name
     */
    private String taskName;
    
    /**
	 * 设置状态:<br/>
	 * true:开启;
	 * false:关闭<br/>
	 * column:status
	 * */
    private Boolean status;
    
    /**
     * 软删除,保存到数据库时默认true<br/>
     * json序列化时,忽略该字段<br/>
     * false:已删除,此数据无效;
     * true:使用中<br/>
     * column:in_use
     * */
    @JsonIgnore
    private Boolean inUse = true;
    
    /**
	 * 任务执行类型:<br/>
	 * true:持续开启(全时段执行,忽略minExecuteTime&maxExecuteTime 内容);
	 * false:指定开启时段(根据minExecuteTime&maxExecuteTime执行)<br/>
	 * column:execute_type
     * */
    private Boolean executeType;
    
    /**
	 * 任务执行开始时间<br/>
	 * column:min_execute_time
     */
    private Date minExecuteTime;
    
    /**
	 * 任务执行结束时间<br/>
	 * column:max_execute_time
     */
    private Date maxExecuteTime;
    
    /**
	 * 开始通知时段<br/>
	 * column:min_inform_time
     */
    private String minInformTime;
    
    /**
	 * 结束通知时段<br/>
	 * column:max_inform_time
     */
    private String maxInformTime;
    
    /**
     * 开始排除通知时段一<br/>
     * column:min_primary_inform_time
     */
    private String minPrimaryInformTime;
    
    /**
     * 结束排除通知时段一<br/>
     * column:max_primary_inform_time
     */
    private String maxPrimaryInformTime;
    
    /**
     * 开始排除通知时段二<br/>
     * column:min_middle_inform_time
     */
    private String minMiddleInformTime;
    
    /**
     * 结束排除通知时段二<br/>
     * column:max_middle_inform_time
     */
    private String maxMiddleInformTime;
    
    /**
     * 开始排除通知时段三 <br/>
     * column:min_senior_inform_time
     */
    private String minSeniorInformTime;
    
    /**
     * 结束排除通知时段三 <br/>
     * column:max_senior_inform_time
     */
    private String maxSeniorInformTime;
    
    /**
	 * 超出时间段执行:<br/>
	 * true:超出时间不发送;
	 * false: 超出时间次日发送<br/>
	 * column:time_out_inform
     * */
    private Boolean timeOutInform;
    
    /**
	 * 发送时间类型:<br/>
	 * 分:1;
	 * 时:2;
	 * 天:3<br/>
	 * column:time_Type
     * */
    private Integer timeType;
    
    /**
     * 发送时间具体值 
     * column:remind_time
     */
    private Integer remindTime;
    
    /**
	 * 同一买家一天只提醒一次:<br/>
	 * true:过滤;
	 * false:不过滤<br/>
	 * column:filter_once
     * */
    private Boolean filterOnce;
    
    /**
	 * 黑名单不发送:<br/>
	 * true:过滤;
	 * false:不过滤<br/>
	 * column:filter_black
     * */
    private Boolean filterBlack;
    
    /**
	 * 同一买家有付过款的不发送(只针对催付有效):<br/>
	 * true:过滤;
	 * false:不过滤<br/>
	 * column:filter_hassent
     * */
    private Boolean filterHassent;
    
    /**
	 * 订单发送范围/订单流转阻塞:<br/>
	 * true:开启新任务后产生的订单;
	 * false:订单状态流转至此处的订单(此状态时忽略chosenTime内容)<br/>
	 * column:trade_block
     * */
    private Boolean tradeBlock;
    
    /**
     * 订单开启时间(为订单发送范围服务)<br/>
     * column:chosen_time
     * */
    private Date chosenTime;
    
    /**
	 * 标记类型:<br/>
	 * 顺序:未标记,红黄青蓝紫<br/>
	 * 未标记0;红:"1";黄:"2";青:"3";蓝:"4";紫:"5"<br/>
	 * 备注:如有多个使用,隔开<br/>
	 * column:seller_flag
     * */
    private String sellerFlag;
    
    /**
	 * 订单来源:<br/>
	 * PC端:TAOBAO;手机:WAP;聚划算:JHS<br/>
	 * 备注:如有多个使用,隔开<br/>
	 * column:trade_from
     * */
    private String tradeFrom;
    
    /**
	 * 会员等级:<br/>
	 * 初次下单用户:"1";店铺客户:"2";普通会员:"3";高级会员:"4";VIP会员:"5";至尊VIP会员:"6"<br/>
	 * 备注:如有多个使用,隔开<br/>
	 * column:member_level
     * */
    private String memberLevel;
    
    /**
     * 最小商品数量 <br/>
     * column:min_product_num
     */
    private Integer minProductNum;
    
    /**
     * 最大商品数量 <br/>
     * column:max_product_num
     */
    private Integer maxProductNum;
    
    /**
     * 最小支付金额 <br/>
     * column:min_payment
     */
    private BigDecimal minPayment;
    
    /**
     * 最大支付金额<br/> 
     *  column:max_payment
     */
    private BigDecimal maxPayment;
    
    /**
	 * 省:<br/>
	 * 备注:如有多个使用,隔开
	 * column:province
     * */
    private String province;
    
    /**
     * 市 <br/>
     * column:city
     */
    private String city;
    
    /**
	 * 指定商品:<br/>
	 * true:指定商品;
	 * false:排除指定商品
	 * column:product_type
     * */
    private Boolean productType;

    /**
	 * 商品ID串:<br/>
	 * 备注:如有多个使用,隔开<br/>
	 * column:products
     * */
    private String products;
    
    /**
     * 短信模板内容 <br/>
     * column:sms_content
     */
    private String  smsContent;
    
    /**
	 * 延时评价:<br/>
	 * true:延时评价;
	 * false:立即评价(忽略:会员延时延时时间的内容)<br/>
	 * column:delay_evaluate
     * */
    private Boolean delayEvaluate;
    
    /**
	 * 延时天数:<br/>
	 * 备注:延时几天评价,注意:单位是天!!<br/>
	 * column:delay_date
     * */
    private Integer delayDate;
    
    /**
	 * 评价类型:<br/>
	 * 好:"good";中:"neutral";差:"bad"
	 * column:evaluate_type
     * */
    private String evaluateType;
    
    /**
	 * 黑名单时的自动评价:<br/>
	 * true:黑名单用户不自动评价 (忽略:delayEvaluate,delayDate,evaluateType 内容);
	 * false:当客户是黑名单是给予指定评价<br/>
	 * column:evaluate_black
     * */
    private Boolean evaluateBlack;
    
    
    /**
	 * 黑名单评价类型:<br/>
	 * 好:"good";中:"neutral";差:"bad"<br/>
	 * column:evaluate_black_type
     * */
    private String evaluateBlackType;
    
    /**
	 * 黑名单评价内容<br/>
	 * 备注:黑名单时的自动评价(功能)<br/>
	 * column:evaluate_black_content
     * */
    private String evaluateBlackContent;
    
    /**
	 * 中评通知我:<br/>
	 * true:买家评价中评时通知卖家;
	 * false:反之<br/>
	 * 备注:仅针对中差评监控有效,其他类型忽略(功能)<br/>
	 * column:neutral_evaluate_inform
     * */
    private Boolean neutralEvaluateInform;
    
    /**
	 * 差评通知我:<br/>
	 * true:买家评价差评时通知卖家;
	 * false:反之<br/>
	 * 备注:仅针对中差评监控有效,其他类型忽略(功能)<br/>
	 * column:bad_evaluate_inform
     * */
    private Boolean badEvaluateInform;
    
    /**
	 * 中差评-通知号码<br/>
	 * 备注:通知手机号码按顺序排列最多添加五个,用逗号隔开<br/>
	 * column:inform_mobile
     * **/
    private String informMobile;

    /**
     * 乐观锁版本<br/>
     * column:optlcok
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
    private Date createdDate;

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
    
    
    public int compareTo(TradeSetup tradeSetup) {
        //以优先等级为准，等级数越小，等级越高，例如1级为最高级，最优先。如果优先等级相同以最后修改时间较早为准。  (循环的时候因为排序原因从小到大，所以此处倒回来排)
        if(tradeSetup == null)
            return 1;
        if(this.getTaskLevel() ==null)
            return 1;
        if(tradeSetup.getTaskLevel()==null)
            return -1;
        if(this.getTaskLevel()<tradeSetup.getTaskLevel()){
            return -1;
        }else if(this.getTaskLevel()>tradeSetup.getTaskLevel()){
            return 1;
        }else{
            if(this.getLastModifiedDate()==null){
                return 1;
            }else if(tradeSetup.getLastModifiedDate()==null){
                return -1;
            }else{
                if(this.getLastModifiedDate().getTime()<tradeSetup.getLastModifiedDate().getTime()){
                    return -1;
                }else if(this.getLastModifiedDate().getTime()>tradeSetup.getLastModifiedDate().getTime()){
                    return 1;
                }else{
                    if(this.getId()==null){
                        return 1;
                    }else if(tradeSetup.getId()==null){
                        return -1;
                    }else if(this.getId()>tradeSetup.getId()){
                        return -1;
                    }else if(this.getId()<tradeSetup.getId()){
                        return 1;
                    }
                }
            }
        }
        return 0;
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


	public Long getUid() {
		return uid;
	}


	public void setUid(Long uid) {
		this.uid = uid;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}


	public Integer getTaskLevel() {
		return taskLevel;
	}


	public void setTaskLevel(Integer taskLevel) {
		this.taskLevel = taskLevel;
	}


	public String getTaskName() {
		return taskName;
	}


	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}


	public Boolean getStatus() {
		return status;
	}


	public void setStatus(Boolean status) {
		this.status = status;
	}


	public Boolean getInUse() {
		return inUse;
	}


	public void setInUse(Boolean inUse) {
		this.inUse = inUse;
	}


	public Boolean getExecuteType() {
		return executeType;
	}


	public void setExecuteType(Boolean executeType) {
		this.executeType = executeType;
	}


	public Date getMinExecuteTime() {
		return minExecuteTime;
	}


	public void setMinExecuteTime(Date minExecuteTime) {
		this.minExecuteTime = minExecuteTime;
	}


	public Date getMaxExecuteTime() {
		return maxExecuteTime;
	}


	public void setMaxExecuteTime(Date maxExecuteTime) {
		this.maxExecuteTime = maxExecuteTime;
	}


	public String getMinInformTime() {
		return minInformTime;
	}


	public void setMinInformTime(String minInformTime) {
		this.minInformTime = minInformTime;
	}


	public String getMaxInformTime() {
		return maxInformTime;
	}


	public void setMaxInformTime(String maxInformTime) {
		this.maxInformTime = maxInformTime;
	}


	public String getMinPrimaryInformTime() {
		return minPrimaryInformTime;
	}


	public void setMinPrimaryInformTime(String minPrimaryInformTime) {
		this.minPrimaryInformTime = minPrimaryInformTime;
	}


	public String getMaxPrimaryInformTime() {
		return maxPrimaryInformTime;
	}


	public void setMaxPrimaryInformTime(String maxPrimaryInformTime) {
		this.maxPrimaryInformTime = maxPrimaryInformTime;
	}


	public String getMinMiddleInformTime() {
		return minMiddleInformTime;
	}


	public void setMinMiddleInformTime(String minMiddleInformTime) {
		this.minMiddleInformTime = minMiddleInformTime;
	}


	public String getMaxMiddleInformTime() {
		return maxMiddleInformTime;
	}


	public void setMaxMiddleInformTime(String maxMiddleInformTime) {
		this.maxMiddleInformTime = maxMiddleInformTime;
	}


	public String getMinSeniorInformTime() {
		return minSeniorInformTime;
	}


	public void setMinSeniorInformTime(String minSeniorInformTime) {
		this.minSeniorInformTime = minSeniorInformTime;
	}


	public String getMaxSeniorInformTime() {
		return maxSeniorInformTime;
	}


	public void setMaxSeniorInformTime(String maxSeniorInformTime) {
		this.maxSeniorInformTime = maxSeniorInformTime;
	}


	public Boolean getTimeOutInform() {
		return timeOutInform;
	}


	public void setTimeOutInform(Boolean timeOutInform) {
		this.timeOutInform = timeOutInform;
	}


	public Integer getTimeType() {
		return timeType;
	}


	public void setTimeType(Integer timeType) {
		this.timeType = timeType;
	}


	public Integer getRemindTime() {
		return remindTime;
	}


	public void setRemindTime(Integer remindTime) {
		this.remindTime = remindTime;
	}


	public Boolean getFilterOnce() {
		return filterOnce;
	}


	public void setFilterOnce(Boolean filterOnce) {
		this.filterOnce = filterOnce;
	}


	public Boolean getFilterBlack() {
		return filterBlack;
	}


	public void setFilterBlack(Boolean filterBlack) {
		this.filterBlack = filterBlack;
	}


	public Boolean getFilterHassent() {
		return filterHassent;
	}


	public void setFilterHassent(Boolean filterHassent) {
		this.filterHassent = filterHassent;
	}


	public Boolean getTradeBlock() {
		return tradeBlock;
	}


	public void setTradeBlock(Boolean tradeBlock) {
		this.tradeBlock = tradeBlock;
	}


	public Date getChosenTime() {
		return chosenTime;
	}


	public void setChosenTime(Date chosenTime) {
		this.chosenTime = chosenTime;
	}


	public String getSellerFlag() {
		return sellerFlag;
	}


	public void setSellerFlag(String sellerFlag) {
		this.sellerFlag = sellerFlag;
	}


	public String getTradeFrom() {
		return tradeFrom;
	}


	public void setTradeFrom(String tradeFrom) {
		this.tradeFrom = tradeFrom;
	}


	public String getMemberLevel() {
		return memberLevel;
	}


	public void setMemberLevel(String memberLevel) {
		this.memberLevel = memberLevel;
	}


	public Integer getMinProductNum() {
		return minProductNum;
	}


	public void setMinProductNum(Integer minProductNum) {
		this.minProductNum = minProductNum;
	}


	public Integer getMaxProductNum() {
		return maxProductNum;
	}


	public void setMaxProductNum(Integer maxProductNum) {
		this.maxProductNum = maxProductNum;
	}


	public BigDecimal getMinPayment() {
		return minPayment;
	}


	public void setMinPayment(BigDecimal minPayment) {
		this.minPayment = minPayment;
	}


	public BigDecimal getMaxPayment() {
		return maxPayment;
	}


	public void setMaxPayment(BigDecimal maxPayment) {
		this.maxPayment = maxPayment;
	}


	public String getProvince() {
		return province;
	}


	public void setProvince(String province) {
		this.province = province;
	}


	public String getCity() {
		return city;
	}


	public void setCity(String city) {
		this.city = city;
	}


	public Boolean getProductType() {
		return productType;
	}


	public void setProductType(Boolean productType) {
		this.productType = productType;
	}


	public String getProducts() {
		return products;
	}


	public void setProducts(String products) {
		this.products = products;
	}


	public String getSmsContent() {
		return smsContent;
	}


	public void setSmsContent(String smsContent) {
		this.smsContent = smsContent;
	}


	public Boolean getDelayEvaluate() {
		return delayEvaluate;
	}


	public void setDelayEvaluate(Boolean delayEvaluate) {
		this.delayEvaluate = delayEvaluate;
	}


	public Integer getDelayDate() {
		return delayDate;
	}


	public void setDelayDate(Integer delayDate) {
		this.delayDate = delayDate;
	}


	public String getEvaluateType() {
		return evaluateType;
	}


	public void setEvaluateType(String evaluateType) {
		this.evaluateType = evaluateType;
	}


	public Boolean getEvaluateBlack() {
		return evaluateBlack;
	}


	public void setEvaluateBlack(Boolean evaluateBlack) {
		this.evaluateBlack = evaluateBlack;
	}


	public String getEvaluateBlackType() {
		return evaluateBlackType;
	}


	public void setEvaluateBlackType(String evaluateBlackType) {
		this.evaluateBlackType = evaluateBlackType;
	}


	public String getEvaluateBlackContent() {
		return evaluateBlackContent;
	}


	public void setEvaluateBlackContent(String evaluateBlackContent) {
		this.evaluateBlackContent = evaluateBlackContent;
	}


	public Boolean getNeutralEvaluateInform() {
		return neutralEvaluateInform;
	}


	public void setNeutralEvaluateInform(Boolean neutralEvaluateInform) {
		this.neutralEvaluateInform = neutralEvaluateInform;
	}


	public Boolean getBadEvaluateInform() {
		return badEvaluateInform;
	}


	public void setBadEvaluateInform(Boolean badEvaluateInform) {
		this.badEvaluateInform = badEvaluateInform;
	}


	public String getInformMobile() {
		return informMobile;
	}


	public void setInformMobile(String informMobile) {
		this.informMobile = informMobile;
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
    
}