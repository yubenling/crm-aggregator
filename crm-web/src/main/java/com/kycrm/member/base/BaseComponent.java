package com.kycrm.member.base;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.util.JsonUtil;


/**
 * @Title: Base.java
 * @Package cn.com.commons.base
 * @Description: controller、service、dao 组件基类
 * @version V1.0
 */
public class BaseComponent {
	
	
	/** 获取Logger */
	protected Logger logger(@SuppressWarnings("rawtypes") Class clazz){
		return Logger.getLogger(clazz);
	}
	/** 获取Logger */
	protected Logger logger(){
		return Logger.getLogger(this.getClass());
	}
	/** GUID */
	protected String randomUUID(){
		return UUID.randomUUID().toString();
	}
	/** XSS过滤包装后的 request */
	protected HttpServletRequest request(){
		return Store.getRequest();
	}
	/** session */
	protected HttpSession session(){
		return request().getSession();
	}
	
	/** 获得原始 request */
	protected HttpServletRequest getOrgRequest(){
		XssHttpServletRequestWrapper req = (XssHttpServletRequestWrapper)request();
		return req.getOrgRequest();
	}
	
	/** 获得原始ati */
	protected String getAtiCookie(){
		XssHttpServletRequestWrapper req = (XssHttpServletRequestWrapper)request();
		return req.getAntiCookie();
	}
	
	

	/** 取后缀名 */
	protected String getSuffix(String src){
		src = src.toLowerCase();
		return src.replaceAll(".*\\.(.*?$)", "$1");
	}

	/** 创建目录 */
	protected boolean createDirWhenNotExist(File dir){
		if(!dir.exists()){
			return dir.mkdirs();
		}
		return true;
	}

	/** 获取一个SimpleDateFormat日期 */
	protected String getFormatedDate(Date date, String pattern){
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(date);
	}
}
