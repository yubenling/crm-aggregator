package com.kycrm.member.controller;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.api.enetity.ApiCallResult;
import com.kycrm.member.api.exception.KycrmApiException;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.domain.entity.tradecenter.TradeSetup;
import com.kycrm.member.domain.vo.effect.TradeCenterEffectVO;

/** 
* @author wy
* @version 创建时间：2018年1月5日 上午11:41:38
*/
@SuppressWarnings("unused")
@Controller
@RequestMapping("/tradeCenter")
public class TradeCenterController extends BaseController{
    /**
     * 免token验证的方法名
     */
    private static enum ignoreTokenMethods{
        findTradeSetupByType;
    }
    private Logger logger = LoggerFactory.getLogger(TradeCenterController.class);
    /**
     * 用户（卖家）淘宝昵称
     */
    private static final String USER_NICK = "userNick";
    /**
     * 用户秘钥
     */
    private static final String TOKEN = "token";
    /**
     * 要执行的方法名
     */
    private static final String METHOD = "method";
    /**
     * 版本
     */
    private static final String VERSION = "version";
    /**
     * 请求方式
     */
    private static final String MODE = "mode";
    /**
     * 头信息<br>
     * userNIck 卖家昵称<br>
     * token 卖家秘钥
     */
    private static final String HEADER = "header";
    /**
     * 参数
     */
    private static final String PARAM = "param";
    public static void main(String[] args) {
        JSONObject reqJson = new JSONObject();
        JSONObject header = new JSONObject();
        header.put(VERSION, "1.0");
        header.put(MODE, "pc");
        header.put(USER_NICK, "哈数据库等哈");
        header.put(TOKEN, "123456");
        JSONObject param = new JSONObject();
        reqJson.put(HEADER, header);
        param.put(METHOD, "findTradeSetupByType");
        reqJson.put(PARAM, param);
        System.out.println(reqJson.toJSONString());
        
    }
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public ApiCallResult apiHandler(@RequestBody String reqrestParam) {
        JSONObject result = null;
        KycrmApiException ex = null;
        try {
            JSONObject reqJson = JSONObject.parseObject(reqrestParam);
            JSONObject header = reqJson.getJSONObject(HEADER);
            String version = header.getString("version");
            String  userNick = header.getString(USER_NICK);
            String mode = header.getString(TOKEN);
            String token = header.getString(TOKEN);
            JSONObject param = reqJson.getJSONObject(PARAM);
            param.put(VERSION, version);
            param.put(USER_NICK, userNick);
            param.put(MODE, mode);
            String method = param.getString(METHOD);
            this.logger.info("执行method:"+method +"================请求参数param:"+param.toJSONString());
            if (validToken(userNick, token, method)) {
                long time1 = System.currentTimeMillis();
                result = invokeMethod(param);
                long time2 = System.currentTimeMillis();
                if(time2-time1 > 1000){
                    logger.info("执行时间" + (time2 - time1) + "ms");
                }
            } else {
                //throw new KycrmApiException(ApiCallResult.FAIL_INVALID_TOKEN);
                throw new KycrmApiException("");
            }
        } catch (KycrmApiException e) {
            e.printStackTrace();
            ex = e;
        } catch (Exception ex1) {
            ex1.printStackTrace();
            //ex = new KycrmApiException(ApiCallResult.FAIL_UNKNOWN);
            ex = new KycrmApiException("");
        }
        return makeReturnResultJson(result, ex);
    }
    /**
     * 返回请求执行代码及结果
     * @author: wy
     * @time: 2018年1月5日 上午11:54:32
     * @return
     */
    private ApiCallResult makeReturnResultJson(JSONObject result, KycrmApiException e) {
//        if (e != null) {
//            return ApiCallResult.buildFailureResult(e);
//        } else {
//            return ApiCallResult.buildSuccessResult(result);
//        }
    	return null;
    }
    /**
     * 通过方法名，反射调用当前类的方法
     * @author: wy
     * @time: 2018年1月5日 下午12:06:13
     * @param params 方法名和参数内容的json集合
     * @return 方法的执行结果
     * @throws KycrmApiException
     */
    private JSONObject invokeMethod(JSONObject params) throws KycrmApiException {
        String method = (String) params.get("method");
        try {
            Method callHandler = this.getClass().getDeclaredMethod(method, JSONObject.class);
            JSONObject returnResult = (JSONObject) callHandler.invoke(this, params);
            return returnResult;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            //throw new KycrmApiException(ApiCallResult.FAIL_UNKNOWN_METHOD);
            throw new KycrmApiException("");
        } catch (Exception e) {
            e.printStackTrace();
            if (e.getCause() instanceof KycrmApiException) {
                throw (KycrmApiException) e.getCause();
            } else {
                //throw new KycrmApiException(ApiCallResult.FAIL_UNKNOWN);
                throw new KycrmApiException("");
            }
        }
    }
    /**
     * 校验用户的token
     * @author: wy
     * @time: 2018年1月5日 下午12:07:55
     * @param uid 用户昵称
     * @param token
     * @param method
     * @return
     * @throws Exception
     */
    private boolean validToken(String sellerNick, String token, String method) throws Exception {
        for (ignoreTokenMethods value : ignoreTokenMethods.values()) {
            if (value.toString().equals(method)) {
                return true;
            }
        }
        //TODO 校验token
        Boolean flag = true;
        if (flag) {
            return true;
        }
        return false;
    }
    
    
    /**
     * 保存订单中心设置
     * @author: wy
     * @time: 2018年1月5日 下午12:14:12
     * @param paramJSON 
     * @return
     */
    private JSONObject saveTradeSetup(JSONObject paramJSON){
        JSONObject result = new JSONObject();
        TradeSetup tradeSetup = paramJSON.getObject("tradeSetup", TradeSetup.class);
        //TODO 保存 校验参数
        result.put("isSave", true);
        return result;
    }
    
    private JSONObject findTradeSetupByType(JSONObject paramJSON){
        System.out.println("数据进来了。。。。。"+paramJSON);
        JSONObject result = new JSONObject();
        String type = paramJSON.getString("type");
        String userNick = paramJSON.getString("userNick");
        //TODO 查询用户对应类型的订单中心设置
        result.put("tradeSetup", "查询出来的订单中心设置集合");
        return result;
    }
    
    /**
     * 物流提醒效果分析页面根据类型查询任务名称
     * ztk2018年1月11日下午5:58:23
     * @param <OrderReminderEffectVo>
     */
//	@RequestMapping("/getTaskNames")
//	@ResponseBody
//	public <OrderReminderEffectVo> String getTaskNameByType(HttpServletRequest request,Model model,@RequestBody String params){
//		String userId = (String) request.getSession().getAttribute("taobao_user_nick");
//		TradeCenterEffectVO tradeCenterEffectVO = null;
//		try {
//			JSONObject jsonObject = JSONObject.parseObject(params);
//			tradeCenterEffectVO = JSON.toJavaObject(jsonObject, TradeCenterEffectVO.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status", false).toJson();
//		}
//		if(tradeCenterEffectVO == null){
//			return rsMap(101, "操作失败").put("status", false).toJson();
//		}
//		/*tradeCenterEffectVO.setUserId(userId);*/
//		//List<OrderReminderEffectVo> effectVos = tradeSetupService.queryTradeSetupTaskNames(tradeCenterEffectVO);
//		return rsMap(100, "操作成功").put("status", true).put("data",null).toJson();
//	}
    
    /**
     * 订单中心效果分析
     * ztk2018年1月11日下午5:48:49
     */
//    @RequestMapping("effectIndex")
//    @ResponseBody
//    public String effectIndex(@RequestBody String params,HttpServletRequest request){
//    	String userId = (String) request.getSession().getAttribute("taobao_user_nick");
//    	TradeCenterEffectVO tradeCenterEffectVO = null;
//    	try {
//			JSONObject jsonObject = JSONObject.parseObject(params);
//			tradeCenterEffectVO = JSON.toJavaObject(jsonObject, TradeCenterEffectVO.class);
//		} catch (KycrmApiException e) {
//			e.printStackTrace();
//			return rsMap(102, "操作失败,请重新操作或联系系统管理员!").put("status",false).toJson();
//		}
//    	if(tradeCenterEffectVO == null || userId == null){
//    		return rsMap(101, "操作失败!").put("status",false).toJson();
//    	}
//    	
//    	
//    	
//    	
//    	return rsMap(100, "操作成功!").put("status",true).toJson();
//    }
}
