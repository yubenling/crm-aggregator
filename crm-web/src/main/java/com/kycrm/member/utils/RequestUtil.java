package com.kycrm.member.utils;

import java.util.UUID;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @ClassName: RequestUtil
 * @Description: 工具类,生成SESSION-ID保存到cookie中
 * @author jackstraw_yu
 * @date 2018年1月15日 下午1:49:43
 */
public class RequestUtil {

	/**
	 * cookie名称
	 */
	private static final String COOKIE_NAME = "KYSESSIONID";

	private static final String ATI_NAME = "_ati";

	/**
	 * cookie 过期时间: (1)0 : 不记录cookie<br/>
	 * -1 : 会话级cookie，关闭浏览器失效<br/>
	 * 60*60 : 过期时间为1小时
	 */
	private static final Integer COOKIE_DEFAULT_EXP = -1;

	/**
	 * cookie path
	 */
	private static final String COOKIE_DEFAULT_PATH = "/";

	/**
	 * 
	 */
	private static final String COOKIE_DEFAULT_SEPARATOR = "-";

	/**
	 * @Description: 生成令牌,session-id
	 * @param request
	 * @param response
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月15日 下午1:50:40
	 */
	public static String getCSESSIONID(HttpServletRequest request, HttpServletResponse response) {
		// 获取Cookie
		Cookie[] cookies = request.getCookies();
		// 获取Cookie中的令牌
		// 1:有,直接返回此令牌
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (COOKIE_NAME.equals(cookie.getName()))
					return cookie.getValue();
			}
		}
		// 2.1:没, 创建令牌、
		// 2.2:创建Cookie 并保存令牌到Cookie 中 把Cookie写回浏览器中
		String csesssionid = UUID.randomUUID().toString().replaceAll(COOKIE_DEFAULT_SEPARATOR, "");
		Cookie cookie = new Cookie(COOKIE_NAME, csesssionid);
		cookie.setMaxAge(COOKIE_DEFAULT_EXP);
		cookie.setPath(COOKIE_DEFAULT_PATH);
		// 跨域相关,暂不打开
//		cookie.setDomain("*.kycrm.com");
		response.addCookie(cookie);
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "Authentication");
		return csesssionid;
	}

	/**
	 * @Description: 获取令牌
	 * @param request
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年3月8日 上午11:47:43
	 */
	public static String getCSESSIONID(HttpServletRequest request) {
		// 获取Cookie
		Cookie[] cookies = request.getCookies();
		// 获取Cookie中的令牌
		// 1:有,直接返回此令牌
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (COOKIE_NAME.equals(cookie.getName()))
					return cookie.getValue();
			}
		}
		return null;
	}

	/**
	 * @Description 获取孔明锁cookieName:_ati的值
	 * @param request
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月2日 下午4:58:32
	 */
	public static String getAtiValue(HttpServletRequest request) {
		// 获取Cookie
		Cookie[] cookies = request.getCookies();
		// 获取Cookie中的令牌
		// 1:有,直接返回此令牌
		if (null != cookies && cookies.length > 0) {
			for (Cookie cookie : cookies) {
				if (ATI_NAME.equals(cookie.getName()))
					if (cookie.getValue() != null && !"".equals(cookie.getValue().trim()))
						return cookie.getValue();
			}
		}
		return null;
	}

	/**
	 * @Description 获取请求者的ip
	 * @param request
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年2月2日 下午5:07:36
	 */
	public static String getRequestorIpAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip == null || "".equals(ip.trim()) ? null : ip;
	}
}
