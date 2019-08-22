package com.kycrm.tmc.sysinfo.service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.domain.entity.message.SmsSendInfo;
import com.kycrm.tmc.util.JudgeUserUtil;
import com.kycrm.tmc.util.TaoBaoClientUtil;
import com.kycrm.util.ValidateUtil;


/** 
* @author wy
* @version 创建时间：2017年10月24日 下午6:22:30
*/
@Service
@SuppressWarnings("unused")
public class ShortLinkService {
    @Autowired
    private JudgeUserUtil judgeUserUtil;
    
    @Autowired
    private TransactionOrderService transactionOrderService;
    
    @Autowired
    private HttpService httpService;
    
    private Logger logger = org.slf4j.LoggerFactory.getLogger(ShortLinkService.class);
    
    /**
     * 短链接表名
     */
    private static final String LINK_TABLE_NAME = "crm_short_link";
    
    /**
     * 请求成功
     */
    public static final String HTTP_SUCCESS = "true";
    /**
     * 请求失败
     */
    public static final String HTTP_FAIL = "false";
    
    /**
     * 短链接服务器的地址
     */
    private String shortServerURL = "";
    /**
     * 获取短链接的url
     */
    private String getShortLinkURL ;
    /**
     * 删除短链接的url
     */
    private String removeShortLinkURL ;
    /**
     * 取得短链接效果分析短链接的url
     */
    private String getAllEffectURL ;
    /**
     * 显示具体点击详情短链接的url
     */
    private String showClickDetailURL ;
    
    public ShortLinkService(){
        ResourceBundle  resource=ResourceBundle.getBundle("dev");
        this.shortServerURL = resource.getString("shortServerURL");
        this.getShortLinkURL = this.shortServerURL+"/link/getShortLink";
        this.removeShortLinkURL = this.shortServerURL+"/link/deleteById";
        this.getAllEffectURL = this.shortServerURL+"/link/getAllEffect";
        this.showClickDetailURL = this.shortServerURL+"/link/showClickDetail";
    }
    
    /**
     * 订单中心获取转换后的短链接
     * @author: wy
     * @time: 2017年10月24日 下午6:28:58
     * @param sellerNick 买家昵称
     * @param tid 主订单号
     * @param type 类型
     * @param taskId 任务id
     * @param sendDate 发送时间
     * @return 转换后的短链接加映射主键id
     */
    public String getConvertLinkByTmcTradeCenter(long uid,String sellerNick,Long tid,String type,Long taskId,Date sendDate,SmsSendInfo smsSendInfo){
        if(ValidateUtil.isEmpty(tid) ||ValidateUtil.isEmpty(uid) ||ValidateUtil.isEmpty(type) ||sendDate==null ||smsSendInfo==null){
            return null;
        }
        String sessionKey = this.judgeUserUtil.getUserTokenByRedis(uid);
        String taoBaoLink = TaoBaoClientUtil.creatLink(sessionKey,"LT_TRADE",String.valueOf(tid));
        Map<String,String> map = new HashMap<String,String>(10);
        map.put("taoBaoLink", taoBaoLink);
        map.put("tid", String.valueOf(tid));
        map.put("sendDate", String.valueOf(sendDate.getTime()));
        map.put("type", type);
        map.put("taskId", String.valueOf(taskId));
        map.put("sellerNick", uid + "");
        map.put("uid", String.valueOf(uid));
        String startNum = new SimpleDateFormat("yyMM").format(new Date());
        map.put("tableName", ShortLinkService.LINK_TABLE_NAME+startNum);
        JSONObject resultJSON = launchHttp(this.getShortLinkURL,map);
        if(resultJSON!=null){
            smsSendInfo.setShortLinkId(resultJSON.getLong("id"));
            return resultJSON.getString("link");
        }
        return null;
    }
    /**
     * 根据用户、类型和所选定的时间查出对应的短链接记录统计
     * @author: wy
     * @time: 2017年10月26日 下午12:03:08
     * @param sellerNick 卖家昵称
     * @param type 类型
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return map集合 <br>
     *  key = pageClickNum , value = 链接总点击次数<br>
     *  key = customerClickNum , value = 用户总点击次数<br>
     *  key = customerClickRate , value = 客户点击率  
     */
    public JSONObject getAllEffect(long uid,String type,Long taskId,Date startTime,Date endTime){
        JSONObject resultJSON = new JSONObject();
        resultJSON.put("result", HTTP_FAIL);
        if(ValidateUtil.isEmpty(uid) ||ValidateUtil.isEmpty(type) ||startTime==null ||endTime==null){
            this.logger.debug("获取页面点击数据效果分析时参数错误，卖家昵称：" +uid +",类型："+type);
            return null;
        }
        Map<String,String> param = new HashMap<String,String>(8);
        param.put("sellerNick", String.valueOf(uid));
        param.put("type", type);
        if(ValidateUtil.isNotNull(taskId)){
            param.put("taskId", String.valueOf(taskId));
        }
        param.put("startTime", String.valueOf(startTime.getTime()));
        param.put("endTime", String.valueOf(endTime.getTime()));
        resultJSON = launchHttp(this.getAllEffectURL,param);
        if(resultJSON!=null){
            return resultJSON;
        }
        return null;
    }
    /**
     * 根据主键ID删除链接映射关系
     * @author: wy
     * @time: 2017年10月25日 下午6:02:32
     * @param id 主键ID
     * @return 删除成功返回true 删除失败返回false
     */
    public boolean doRemoveById(Long id){
        if(ValidateUtil.isEmpty(id)){
            return false;
        }
        Map<String,String> param = new HashMap<String,String>(5);
        param.put("id", String.valueOf(id));
        JSONObject resultJSON = launchHttp(this.removeShortLinkURL,param);
        if(resultJSON!=null){
            return true;
        }
        return false;
    }
    
    /**
     * 发送http请求
     * @author: wy
     * @time: 2017年11月27日 下午3:56:04
     * @param url
     * @param param
     * @return
     */
    private JSONObject launchHttp(String url,Map<String,String> param){
        if(ValidateUtil.isEmpty(url) ||ValidateUtil.isEmpty(param)){
            return null;
        }
        String result = null;
        try {
            result = this.httpService.doGet(url, param);
            if(result==null){
                return null;
            }
            JSONObject resultJSON = JSONObject.parseObject(result);
            String flag = resultJSON.getString("result");
            if(HTTP_SUCCESS.equals(flag)){
                return resultJSON;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
   
}
