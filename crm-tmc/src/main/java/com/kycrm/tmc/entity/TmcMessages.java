package com.kycrm.tmc.entity;

import java.util.Date;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.taobao.api.domain.Trade;


/** 
 * tmc判断过程中间类
* @author wy
* @version 创建时间：2017年8月9日 下午2:04:45
*/
public class TmcMessages {
    public TmcMessages(){
        this.flag = true;
        this.sendSchedule = false;
        this.sendTime = new Date();
    }
    /** 
     * 主订单实体类对象，不可缺少
     */
    private Trade trade;
    
    /**
     * 流程处理标记字段，全部为true才发送短信
     */
    private Boolean flag ;
    
    /** 
     * 本次tmc消息处理的卖家的信息 ，不可缺少
    */
    private UserInfo user;
    
    /**
     * tmc对应卖家的基本设置信息，不可缺少
     */
    private TradeSetup tradeSetup;
    
    /**
     * 订单号tid
     */
    private Long tid;
    
    /**
     * 当前tmc走的类型，参考OrderSetup中的type成员
     */
    private String settingType;

    /**
     * 是否定时发送，true 定时短信（催付，签收） false，非定时短信（下单，付款）
     */
    private Boolean sendSchedule;
    
    /**
     * 开始时间，正常为接收到消息的当前系统时间，如果是催付，则是下单的时间，如果是签收，则是签收的时间
     */
    private Date sendTime;
    
    /**
     * 用户设置短信提醒结束时间
     */
    private Date sellerEndDate;
    
    /**
     * 接收到的tmc消息
     */
    private JSONObject tmcContent;

    public Trade getTrade() {
        return trade;
    }

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public TradeSetup getTradeSetup() {
        return tradeSetup;
    }

    public void setTradeSetup(TradeSetup tradeSetup) {
        this.tradeSetup = tradeSetup;
    }

    public Long getTid() {
        return tid;
    }

    public void setTid(Long tid) {
        this.tid = tid;
    }

    public String getSettingType() {
        return settingType;
    }

    public void setSettingType(String settingType) {
        this.settingType = settingType;
    }

    public Boolean getSendSchedule() {
        return sendSchedule;
    }

    public void setSendSchedule(Boolean sendSchedule) {
        this.sendSchedule = sendSchedule;
    }

    public Date getSendTime() {
        return sendTime;
    }

    public void setSendTime(Date sendTime) {
        this.sendTime = sendTime;
    }

    public Date getSellerEndDate() {
        return sellerEndDate;
    }

    public void setSellerEndDate(Date sellerEndDate) {
        this.sellerEndDate = sellerEndDate;
    }

    public JSONObject getTmcContent() {
        return tmcContent;
    }

    public void setTmcContent(JSONObject tmcContent) {
        this.tmcContent = tmcContent;
    }
    
    
}
