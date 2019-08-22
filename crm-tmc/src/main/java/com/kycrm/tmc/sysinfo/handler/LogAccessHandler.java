package com.kycrm.tmc.sysinfo.handler;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Map;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kycrm.member.domain.entity.eco.log.LogAccessDTO;
import com.kycrm.tmc.sysinfo.entity.HttpResult;
import com.kycrm.tmc.sysinfo.service.HttpService;
import com.kycrm.util.JsonUtil;

/**
 * @version V1.0
 */
@Service("logAccessHandler")
public class LogAccessHandler extends  LogAccessForwardHandler {

	private static final Logger logger = Logger.getLogger(LogAccessHandler.class);

    private String logAppKey;
    private String logAppSecret;
    private String url;
    private String topAppKey;
    private String appName;
    
    @Autowired
    private HttpService httpService;
    
    public LogAccessHandler(){
        ResourceBundle  resource=ResourceBundle.getBundle("dev");
        this.logAppKey = resource.getString("log.app.key");
        this.logAppSecret = resource.getString("log.app.secret");
        this.url = resource.getString("log.access.baseUrl");
        this.topAppKey = resource.getString("top.app.key");
        this.appName = resource.getString("top.app.name");
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    protected String handleLoginLogType(Map map) {
        Map<String, String> paramMap = new TreeMap<String, String>();
        LogAccessDTO logAccessDTO = (LogAccessDTO) map.get(LogAccessDTO.class.getName());
        paramMap.put("time", String.valueOf(System.currentTimeMillis()));
        paramMap.put("appKey", logAppKey);
        paramMap.put("ati",logAccessDTO.getAti());
        paramMap.put("userId",logAccessDTO.getUserId());
        paramMap.put("userIp", logAccessDTO.getUserIp());
        paramMap.put("tid", logAccessDTO.getTid());
        paramMap.put("appName", appName);
        paramMap.put("loginResult", logAccessDTO.getLoginResult());
        paramMap.put("loginMessage", logAccessDTO.getLoginMessage());
        paramMap = buildStrParam(paramMap);
        String jsonResult= sendRequestToTaoBao(paramMap,url+"/login");
        return jsonResult;
    }
    

    @SuppressWarnings("rawtypes")
    @Override
    protected String handleOrderLogType(Map map) {
        Map<String, String> paramMap = new TreeMap<String, String>();
        LogAccessDTO logAccessDTO = (LogAccessDTO) map.get(LogAccessDTO.class.getName());
        paramMap.put("time", String.valueOf(System.currentTimeMillis()));
        paramMap.put("appKey", logAppKey);
        paramMap.put("ati",logAccessDTO.getAti());
        paramMap.put("userId",logAccessDTO.getUserId());
        paramMap.put("userIp", logAccessDTO.getUserIp());
        paramMap.put("topAppKey",topAppKey);
        paramMap.put("appName",appName);
        paramMap.put("url", logAccessDTO.getUrl());
        paramMap.put("serverIp",logAccessDTO.getServerIp());
        paramMap.put("tradeIds",logAccessDTO.getTradeIds());
        paramMap.put("operation",logAccessDTO.getOperation());
        paramMap = buildStrParam(paramMap);
        String jsonResult= sendRequestToTaoBao(paramMap,url+"/order");
        return jsonResult;
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    protected String handleSendOrderLogType(Map map) {
        Map<String, String> paramMap = new TreeMap<String, String>();
        LogAccessDTO logAccessDTO = (LogAccessDTO) map.get(LogAccessDTO.class.getName());
        paramMap.put("time", String.valueOf(System.currentTimeMillis()));
        paramMap.put("appKey", logAppKey);
        paramMap.put("ati",logAccessDTO.getAti());
        paramMap.put("userId",logAccessDTO.getUserId());
        paramMap.put("userIp", logAccessDTO.getUserIp());
        paramMap.put("topAppKey",topAppKey);
        paramMap.put("appName",appName);
        paramMap.put("url", logAccessDTO.getUrl());
        paramMap.put("serverIp",logAccessDTO.getServerIp());
        paramMap.put("tradeIds",logAccessDTO.getTradeIds());
        paramMap.put("sendTo",logAccessDTO.getSendTo());
        paramMap = buildStrParam(paramMap);
        String jsonResult= sendRequestToTaoBao(paramMap,url+"/sendOrder");
        return jsonResult;
        
    }
    
    @SuppressWarnings("rawtypes")
    @Override
    protected String handleAccessDBLogType(Map map) {
        Map<String, String> paramMap = new TreeMap<String, String>();
        LogAccessDTO logAccessDTO = (LogAccessDTO) map.get(LogAccessDTO.class.getName());
        paramMap.put("time", String.valueOf(System.currentTimeMillis()));
        paramMap.put("appKey", logAppKey);
        paramMap.put("ati",logAccessDTO.getAti());
        paramMap.put("userId",logAccessDTO.getUserId());
        paramMap.put("userIp", logAccessDTO.getUserIp());
        paramMap.put("topAppKey",topAppKey);
        paramMap.put("appName",appName);
        paramMap.put("url", logAccessDTO.getUrl());
        paramMap.put("serverIp",logAccessDTO.getServerIp());
        paramMap.put("db",logAccessDTO.getDb());
        paramMap.put("sql", logAccessDTO.getSql());
        paramMap = buildStrParam(paramMap);
        String jsonResult= sendRequestToTaoBao(paramMap,url+"/sql");
        return jsonResult;
    }

	@Override
	@SuppressWarnings("rawtypes")
	protected void log(Map map) {
		String httpResult = (String) map.get(HttpResult.class.getName());
		logger.info(JsonUtil.fromJson(httpResult, HttpResult.class));
	}
	
	private  String  sendRequestToTaoBao(Map<String, String> paramMap,String url) {
		try {
			HttpResult httpResult = httpService.doPost(url, paramMap);
			if(null!=httpResult){
				String json = JsonUtil.toJson(httpResult);
				return json;
			}
		} catch (IOException e) {
			return null;
		}
		return null;
	}
	
	private  Map<String, String> buildStrParam(Map<String, String> paramMap){
		String sign = getSignature(logAppSecret,paramMap);
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
			for (Entry<String, String> entry: entrySet){
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
