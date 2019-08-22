package com.kycrm.tmc.manage;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.util.Constants;
import com.kycrm.util.OrderSettingInfo;
import com.kycrm.util.TmcInfo;


/** 
 * 服务tmc消息管理类
* @author wy
* @version 创建时间：2017年9月5日 下午5:13:20
*/
@Service
@SuppressWarnings("unused")
public class FuwuTmcManageService {
	private Logger logger = LoggerFactory.getLogger(FuwuTmcManageService.class);
	@Autowired
	private IUserInfoService userInfoService;
	
	public void doHandle(String str){
		this.logger.info("服务tmc开始处理-->"+str);
    	JSONObject json = JSONObject.parseObject(str);
    	String topic = json.getString(OrderSettingInfo.TOPIC);
    	if(TmcInfo.FUWU_ORDERPAID_TOPIC.equals(topic)){
    	    this.logger.info("开始充值 " + json);
    		this.fuwuOrderPaid(json);
    	}else if(TmcInfo.FUWU_SERVICE_OPEN_TOPIC.equals(topic)){
//    		this.addUser(json);
    	}else{
    		this.logger.info("错误的消息！！！ "+str);
    	}
	}
	
	
	
	 /**
     * 服务支付消息
     * @author: wy
     * @time: 2017年9月5日 下午4:53:44
     * @param content
     */
    private void fuwuOrderPaid(JSONObject content){
        this.logger.info("准备充值： " + content);
    	String articleCode = content.getString("article_code");
    	if (Constants.TOP_APP_CODE.equals(articleCode)) {
    		try {
    			addUser(content);
			} catch (Exception e) {
              logger.info("修改用户过期时间出错");
			}
		} else {
			addUserRecharge(content);
		}
    }
    
    private void addUserRecharge(JSONObject json) {
        this.userInfoService.addUserRechargeByTmc(json.toJSONString());
	}
    
    /**
	 * 判断用户是否存在，不存在则添加新用户；如果存在则更新用户应用到期时间
	 * 
	 * @param message
	 */
    private void addUser(JSONObject json) {
    	
    	logger.info("更新用户信息"+json);
    	if (json != null ) {
			// 根据用户昵称查询用户是否存在
			UserInfo ui =userInfoService.findUserInfoByTaobaoUserNick(json.getString("nick"));

			SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
			// 判断用户是否存在，不存在则添加新用户，存在则更新用户应用的到期时间
			if(ui!=null){
				// 更新用户应用到期时间
			    Map<String, Object> map = new HashMap<String, Object>();
			    map.put("id", ui.getId());
				try {
				   map.put("expirationTime",dateFormat.parse(json.getString("order_cycle_end")));
				} catch (Exception e) {
					e.printStackTrace();
				}
				userInfoService.updateUserInfoExpirationTime(map);
			}
    	
//		// String
//		// content="{'refund_fee':'0','biz_type':2,'order_cycle':'2','article_name':'客云CRM','article_code':'FW_GOODS-1952286','order_id':310022087290045,'create':'2017-03-10 11:20:03','order_status':3,'pay_status':1,'biz_order_id':310022087290045,'fee':'1500.0','prom_fee':'0.0','order_cycle_end':'2017-05-10 00:00:00','total_pay_fee':'1500.0','item_code':'FW_GOODS-1952286-1','nick':'哈数据库等哈','item_name':'经典版','version_no':1,'order_cycle_start':'2017-04-10 00:00:00','outer_trade_code':''}";
//		if (json != null ) {
//			// 根据用户昵称查询用户是否存在
//			UserInfoService userInfoService = SpringContextUtil
//					.getBean("userInfoService");
//			UserInfo ui = null;
//			ui = userInfoService.findUserInfo(json.getString("nick"));
//
//			SimpleDateFormat dateFormat = new SimpleDateFormat(
//					"yyyy-MM-dd HH:mm:ss");
//			// 判断用户是否存在，不存在则添加新用户，存在则更新用户应用的到期时间
//			if (ui == null) {
//				ui = new UserInfo();
//				// ui.setTaobaoUserId(json.getString("taobao_user_id"));
//				ui.setTaobaoUserNick(json.getString("nick"));
//				ui.setCreateTime(new Date());
//				try {
//					ui.setExpirationTime(dateFormat.parse(json
//							.getString("order_cycle_end")));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				ui.setSmsNum(0);
//				ui.setEmailNum(0);
//				ui.setStatus(0);
//				ui.setLastLoginDate(new Date());
//				userInfoService.addUserInfo(ui);
//			} else {
//				// 更新用户应用到期时间
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("taobao_user_nick", ui.getTaobaoUserNick());
//				try {
//					map.put("expiration_time",
//							dateFormat.parse(json.getString("order_cycle_end")));
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
//				ui.setLastLoginDate(new Date());
//				userInfoService.updateUserInfo(map);
//			}
		}
	}
    
}
