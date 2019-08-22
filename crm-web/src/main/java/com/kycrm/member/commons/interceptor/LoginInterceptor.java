package com.kycrm.member.commons.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.kycrm.member.commons.exception.SessionOutException;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.utils.RequestUtil;

public class LoginInterceptor implements HandlerInterceptor {

	private static Logger logger = LoggerFactory.getLogger(LoginInterceptor.class);

	private static final String AJAX_REQUEST_HEADER_NAME = "x-requested-with";
	private static final String AJAX_REQUEST_HEADER_VALUE = "XMLHttpRequest";
	private static final String REDIRECT_PATH = "https://login.taobao.com/member/login.jhtml";

	@Autowired
	private SessionProvider sessionProvider;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// String result =
		// sessionProvider.getAttributeForUserName(RequestUtil.getCSESSIONID(request,
		// response));
		// UserInfo result =
		// sessionProvider.getAttributeForUser(RequestUtil.getCSESSIONID(request,
		// response),UserInfo.class);
		// if(result==null /*|| "".equals(result)*/){
		// //判断是否是ajax请求
		// //是 : 在响应位置添加登出
		// if (request.getHeader(AJAX_REQUEST_HEADER_NAME) != null
		// &&
		// request.getHeader(AJAX_REQUEST_HEADER_NAME).equalsIgnoreCase(AJAX_REQUEST_HEADER_VALUE)){
		// logger.info("-------------------------------------an ajax reuqest has
		// occurred with sessionOut !");
		// //返回到配置文件中定义的路径
		// throw new SessionOutException("login timeOut !");
		// }else{
		// response.sendRedirect(REDIRECT_PATH);
		// return false;
		// }
		// }
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

}
