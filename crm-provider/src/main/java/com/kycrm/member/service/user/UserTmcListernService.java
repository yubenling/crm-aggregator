package com.kycrm.member.service.user;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.service.tradecenter.ITradeSetupService;
import com.kycrm.member.util.TaoBaoClientUtil;
import com.kycrm.util.TmcInfo;
import com.kycrm.util.ValidateUtil;

/** 
 * 用户tmc消息监听业务处理
* @author wy
* @version 创建时间：2018年1月12日 下午2:33:35
*/
@Service("userTmcListernService")
public class UserTmcListernService {
    @Autowired
    private JudgeUserUtil judgeUserUtil;
    
    @Autowired
    private ITradeSetupService tradeSetupService;
    
    /**
     * 删除用户的消息监听
     * @author: wy
     * @time: 2017年8月23日 下午4:01:18
     * @param sellerNick 消息昵称
     * @param delType 要删除的类型（1--下单关怀）
     * @return 
     */
    public boolean removeUserPermitByTaoBao(Long uid,String sellerName,String delType){
        return this.addUserPermitByMySql(uid, sellerName,null);
    }
    /**
     * 给用户授权，通过查询数据库来添加要接收的tmc消息
     * @author: wy
     * @time: 2017年8月23日 下午3:43:58
     * @param sellerNick 卖家昵称
     * @param addType 要新添加的消息类型（如1--下单关怀），可以为空，为空则根据取出来的值添加消息
     */
    public boolean addUserPermitByMySql(Long uid,String sellerName,String addType){
        if(ValidateUtil.isEmpty(uid)){
            return false;
        }
        List<String> list = this.tradeSetupService.findTypeBySellerNickTmc(uid);
        Set<String> set = new HashSet<String>(28);
        if(ValidateUtil.isNotNull(list)){
            for (String string : list) {
                String s = this.getTopicBySettingType(string);
                if(s!=null){
                    set.add(s);
                }
            }
        }
        if(ValidateUtil.isNotNull(addType)){
            String addTopic = this.getTopicBySettingType(addType);
            if(ValidateUtil.isNotNull(addTopic)){
                set.add(addTopic);
            }
        }
        set.add(TmcInfo.FUWU_ORDERPAID_TOPIC);
        set.add(TmcInfo.FUWU_SERVICE_OPEN_TOPIC);
        String topics = this.getCommaStringByCollection(set);
        String sessionKey = this.judgeUserUtil.getUserTokenByRedis(uid);
        return this.openUserPermit(sessionKey, topics);
    }
    /**
     * 根据用户的sessionKey和具体的Topic来为用户开启消息
     * @author: wy
     * @time: 2017年8月23日 下午2:59:26
     * @param sessionKey 用户的秘钥
     * @param topics 要为用户授权的消息，如果消息为空，则会强制加上服务开通和服务支付两个消息
     * @return
     */
    public boolean openUserPermit(String sessionKey,String topics){
        return TaoBaoClientUtil.doOpenUserPermit(sessionKey,topics);
    } 
    /**
     * 将字符串集合转为由英文逗号连接的字符串
     * @author: wy
     * @time: 2017年8月23日 下午2:43:47
     * @param c
     * @return
     */
    public String getCommaStringByCollection(Collection<? extends String> c){
        if(c == null || c.size()==0){
            return null;
        }
        StringBuffer s = new StringBuffer();
        int i = 0;
        for (String string : c) {
            if(string==null){
                continue;
            }
            if(i==0){
                s.append(string);
                i++;
            }else{
                s.append(",").append(string);
            }
        }
        return s.toString();
    }
    /**
     * 通过对应的状态取得对应的淘宝TMC消息TOPIC,不提供服务开通付款的消息返回
     * @author: wy
     * @time: 2017年8月23日 下午2:40:56
     * @param type
     * @return
     */
    public String getTopicBySettingType(String type){
        if(ValidateUtil.isEmpty(type)){
            return null;
        }
        switch (type) {
        case "1":{ //1-下单关怀 
            return TmcInfo.TRADE_CREATE_TOPIC;
        }
        case "2":{ //2-常规催付 
            return TmcInfo.TRADE_CREATE_TOPIC;
        }
        case "3":{ //3-二次催付
            return TmcInfo.TRADE_CREATE_TOPIC;
        }
        case "4":{ //4-聚划算催付
            return TmcInfo.TRADE_CREATE_TOPIC;
        }
        case "6":{ //6-发货提醒 
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "7":{ //7-到达同城提醒
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "8":{ //8-派件提醒
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "9":{ //9-签收提醒
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "11":{ //11-延时发货提醒
            return TmcInfo.TRADE_BUYERPAY_TOPIC;
        }
        case "12":{ //12-宝贝关怀
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "13":{ //13-付款关怀 
            return TmcInfo.TRADE_BUYERPAY_TOPIC;
        }
        case "14":{ //14-回款提醒
            return TmcInfo.LOGSTIC_DETAIL_TOPIC;
        }
        case "16":{ //16-自动评价
            return TmcInfo.TRADE_SUCCESS_TOPIC;
        }
        case "29":{ //29-买家申请退款
            return TmcInfo.REFUND_CREATED_TOPIC;
        }
        case "30":{ //30-退款成功
            return TmcInfo.REFUND_SUCCESS_TOPIC;
        }
        case "31":{ // 31-等待退货 
            return TmcInfo.REFUND_AGREE_TOPIC;
        }
        case "32":{ //32-拒绝退款
            return TmcInfo.REFUND_REFUSE_TOPIC;
        }
        case "37":{ //37-好评提醒
            return TmcInfo.TRADE_SUCCESS_TOPIC;
        }
        default:
            return null;
        }
    }
}
