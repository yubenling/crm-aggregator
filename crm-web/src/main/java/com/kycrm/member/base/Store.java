package com.kycrm.member.base;

import javax.servlet.http.HttpServletRequest;


/**
 * @Title: Store.java
 * @Package cn.com.commons.filter.threadlocal
 * @Description: 储存request 和 session
 * @author zlp
 * @version V1.0
 */
public class Store {
	private static ThreadLocal<HttpServletRequest> request = new ThreadLocal<HttpServletRequest>();
	public static HttpServletRequest getRequest() {
		return request.get();
	}
	public static void setRequest(HttpServletRequest req) {
		request.set(req);
	}
	public static void removeRequest(){
		request.remove();
	}
}
