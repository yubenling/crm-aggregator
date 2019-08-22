package com.kycrm.syn.core.handle.log;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.kycrm.member.domain.entity.eco.log.LogAccessDTO;
import com.kycrm.member.domain.entity.http.HttpResult;
import com.kycrm.syn.core.http.HttpService;
import com.kycrm.util.JsonUtil;


/**
 * @version V1.0
 */
@SuppressWarnings("rawtypes")
public class LogAccessHandler extends LogAccessForwardHandler {

    private static final Log logger = LogFactory.getLog(LogAccessHandler.class);

    public static String LOG_APP_KEY;
    public static String LOG_APP_SECRET;
    public static String URL;
    public static String TOP_APP_KEY;
    public static String TOP_APP_NAME;

    @Autowired
    private HttpService httpService;

    public LogAccessHandler() {
        ResourceBundle resource = ResourceBundle.getBundle("application");
        LogAccessHandler.LOG_APP_KEY = resource.getString("log.app.key");
        LogAccessHandler.LOG_APP_SECRET = resource.getString("log.app.secret");
        LogAccessHandler.URL = resource.getString("log.access.baseUrl");
        LogAccessHandler.TOP_APP_KEY = resource.getString("top.app.key");
        LogAccessHandler.TOP_APP_NAME = resource.getString("top.app.name");
    }

    /**
     * 上传登录日志
     */
    @Override
    protected String handleLoginLogType(Map map) {
        Map<String, String> paramMap = new TreeMap<String, String>();
        LogAccessDTO logAccessDTO = (LogAccessDTO) map.get(LogAccessDTO.class.getName());
        paramMap.put("time", String.valueOf(System.currentTimeMillis()));
        paramMap.put("appKey", LOG_APP_KEY);
        paramMap.put("ati", logAccessDTO.getAti());
        paramMap.put("userId", logAccessDTO.getUserId());
        paramMap.put("userIp", logAccessDTO.getUserIp());
        paramMap.put("tid", logAccessDTO.getTid());
        paramMap.put("appName", TOP_APP_NAME);
        paramMap.put("loginResult", logAccessDTO.getLoginResult());
        paramMap.put("loginMessage", logAccessDTO.getLoginMessage());
        paramMap = buildStrParam(paramMap);
        String jsonResult = sendRequestToTaoBao(paramMap, URL + "/login");
        return jsonResult;
    }

    /**
     * 上传订单访问日志
     */
    @Override
    protected String handleOrderLogType(Map map) {
        Map<String, String> paramMap = new TreeMap<String, String>();
        LogAccessDTO logAccessDTO = (LogAccessDTO) map.get(LogAccessDTO.class.getName());
        paramMap.put("time", String.valueOf(System.currentTimeMillis()));
        paramMap.put("appKey", LOG_APP_KEY);
        paramMap.put("ati", logAccessDTO.getAti());
        paramMap.put("userId", logAccessDTO.getUserId());
        paramMap.put("userIp", logAccessDTO.getUserIp());
        paramMap.put("topAppKey", TOP_APP_KEY);
        paramMap.put("appName", TOP_APP_NAME);
        paramMap.put("url", logAccessDTO.getUrl());
        paramMap.put("tradeIds", logAccessDTO.getTradeIds());
        paramMap.put("operation", logAccessDTO.getOperation());
        paramMap = buildStrParam(paramMap);
        String jsonResult = sendRequestToTaoBao(paramMap, URL + "/order");
        return jsonResult;
    }

    /**
     * 上传 发送第三方订单 日志
     */
    @Override
    protected String handleSendOrderLogType(Map map) {
        Map<String, String> paramMap = new TreeMap<String, String>();
        LogAccessDTO logAccessDTO = (LogAccessDTO) map.get(LogAccessDTO.class.getName());
        paramMap.put("time", String.valueOf(System.currentTimeMillis()));
        paramMap.put("appKey", LOG_APP_KEY);
        paramMap.put("ati", logAccessDTO.getAti());
        paramMap.put("userId", logAccessDTO.getUserId());
        paramMap.put("userIp", logAccessDTO.getUserIp());
        paramMap.put("topAppKey", TOP_APP_KEY);
        paramMap.put("appName", TOP_APP_NAME);
        paramMap.put("url", logAccessDTO.getUrl());
        paramMap.put("serverIp", logAccessDTO.getServerIp());
        paramMap.put("tradeIds", logAccessDTO.getTradeIds());
        paramMap.put("sendTo", logAccessDTO.getSendTo());
        paramMap = buildStrParam(paramMap);
        String jsonResult = sendRequestToTaoBao(paramMap, URL + "/sendOrder");
        return jsonResult;

    }

    /**
     * 上传 数据库 SQL 日志
     */
    @Override
    protected String handleAccessDBLogType(Map map) {
        Map<String, String> paramMap = new TreeMap<String, String>();
        LogAccessDTO logAccessDTO = (LogAccessDTO) map.get(LogAccessDTO.class.getName());
        paramMap.put("time", String.valueOf(System.currentTimeMillis()));
        paramMap.put("appKey", LOG_APP_KEY);
        paramMap.put("ati", logAccessDTO.getAti());
        paramMap.put("userId", logAccessDTO.getUserId());
        paramMap.put("userIp", logAccessDTO.getUserIp());
        paramMap.put("topAppKey", TOP_APP_KEY);
        paramMap.put("appName", TOP_APP_NAME);
        paramMap.put("url", logAccessDTO.getUrl());
        paramMap.put("serverIp", logAccessDTO.getServerIp());
        paramMap.put("db", logAccessDTO.getDb());
        paramMap.put("sql", logAccessDTO.getSql());
        paramMap = buildStrParam(paramMap);
        String jsonResult = sendRequestToTaoBao(paramMap, URL + "/sql");
        return jsonResult;
    }

    @Override
    protected String handleBatchLogType(Map map) {
        Map<String, String> paramMap = new TreeMap<String, String>();
        LogAccessDTO logAccessDTO = (LogAccessDTO) map.get(LogAccessDTO.class.getName());
        paramMap.put("time", String.valueOf(System.currentTimeMillis()));
        paramMap.put("appKey", LOG_APP_KEY);
        paramMap.put("topAppKey", TOP_APP_KEY);
        paramMap.put("data", logAccessDTO.getData()); 
        paramMap.put("method", logAccessDTO.getMethod());
        paramMap = buildStrParam(paramMap);
        String jsonResult = sendRequestToTaoBao(paramMap, URL + "/batchLog");
        return jsonResult;
    }

    @Override
    protected void log(Map map) {
        String httpResult = (String) map.get(HttpResult.class.getName());
        logger.info("日志上传结果："+httpResult);
    }

    private String sendRequestToTaoBao(Map<String, String> paramMap, String url) {
        logger.info("url=" + url + " 参数=" + paramMap);
        try {  
            HttpResult httpResult = httpService.doPost(url, paramMap);
            if (null != httpResult) {
                String json = JsonUtil.toJson(httpResult);
                return json;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Map<String, String> buildStrParam(Map<String, String> paramMap) {
        String sign = getSignature(LOG_APP_SECRET, paramMap);
        paramMap.put("sign", sign);
        return paramMap;
    }

    public static String getSignature(String appSecret, Map<String, String> paramMap) {
        try {
            if (paramMap == null) {
                return "";
            }
            StringBuilder combineString = new StringBuilder();
            combineString.append(appSecret);
            Set<Entry<String, String>> entrySet = paramMap.entrySet();
            for (Entry<String, String> entry : entrySet) {
                combineString.append(entry.getKey() + entry.getValue());
            }
            combineString.append(appSecret);
            byte[] bytesOfMessage = combineString.toString().getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] thedigest = md.digest(bytesOfMessage);
            String signature = bytesToHexString(thedigest);
            return signature;
        } catch (Exception e) {
            return "";
        }
    }

    private static String bytesToHexString(byte[] src) {
        try {
            StringBuilder stringBuilder = new StringBuilder("");
            if (src == null || src.length <= 0) {
                return null;
            }
            for (int i = 0; i < src.length; i++) {
                int v = src[i] & 0xFF;
                String hv = Integer.toHexString(v);
                if (hv.length() < 2) {
                    stringBuilder.append(0);
                }
                stringBuilder.append(hv);
            }
            return stringBuilder.toString();
        } catch (Exception e) {
            return null;
        }
    }

}
