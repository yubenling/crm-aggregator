package com.kycrm.member.base;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.log4j.Logger;

/**
 */
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private static Logger logger = Logger.getLogger(XssHttpServletRequestWrapper.class);
	private HttpServletRequest orgRequest;
	
	
	public XssHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		orgRequest = request;
	}
	
	public HttpServletRequest getOrgRequest() {
		return orgRequest;
	}
	
	@Override
	public String getParameter(String name) {
		String parameter = super.getParameter(name);
		return xssEncode(parameter);
	}
	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> parameterMap = super.getParameterMap();
		if(parameterMap != null){
			for (String[] values : parameterMap.values()) {
				for (int i = 0; values != null && i < values.length; i++) {
					values[i] = xssEncode(values[i]);
				}
			}
		}
		return parameterMap;
	}
	
	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		for (int i = 0; values != null && i < values.length; i++) {
			values[i] = xssEncode(values[i]);
		}
		return values;
	}
	
	@Override
	public Enumeration<String> getHeaders(String name) {
		Enumeration<String> headers = super.getHeaders(name);
    	ArrayList<String> list = new ArrayList<String>();
    	if(headers != null){
    		while(headers.hasMoreElements()){
        		String element = headers.nextElement();
        		list.add(xssEncode(element));
        	}
    	}
		return Collections.enumeration(list);
	}
	@Override
	public Cookie[] getCookies() {
		Cookie[] cookies = super.getCookies();
		for (int i = 0; cookies != null && i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if(cookie != null){
				cookie.setValue(xssEncode(cookie.getValue()));
			}
		}
		return cookies;
	}
	
	public String getAntiCookie() {
		Cookie[] cookies = super.getCookies();
		String ati="";
		for (int i = 0; cookies != null && i < cookies.length; i++) {
			Cookie cookie = cookies[i];
			if(cookie != null && "_ati".equals(cookie.getName())){
				ati=xssEncode(cookie.getValue());
			}
		}
		return ati;
	}
	
	private String xssEncode(String value){
		if(value == null){
			return null;
		} else {
			String origin = value;
			logger.debug("XSS Filter 过滤处理：\"" + origin + "\" --> \"" + value + "\"");
			return value;
		}
	}
	
	@Override
	public String getRemoteAddr() {
		return super.getHeader("X-Forwarded-For");
	}

}
