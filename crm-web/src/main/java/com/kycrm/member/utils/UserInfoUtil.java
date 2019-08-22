package com.kycrm.member.utils;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.user.UserInfo;

/**
 * 通用用户信息工具类
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月21日下午2:18:51
 * @Tags
 */
@Component
public class UserInfoUtil {

	@Autowired
	private SessionProvider sessionProvider;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取用户信息
	 * @Date 2018年7月11日上午11:31:06
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @ReturnType Long
	 */
	public Long getUid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserInfo user = this.sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		Long uid = null;
		if (user != null && user.getId() != null) {
			uid = user.getId();
		} else {
			throw new Exception("错误 : 卖家ID不能为空");
		}
		return uid;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 获取taobao_user_nick
	 * @Date 2018年7月20日下午5:06:30
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	public String getTaoBaoUserNick(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserInfo user = this.sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		String taobaoUserNick = user.getTaobaoUserNick();
		return taobaoUserNick;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 每次搜索生成一个key
	 * @Date 2018年7月21日上午11:16:58
	 * @return
	 * @ReturnType String
	 */
	public String getKey() {
		long id = System.currentTimeMillis();
		String key = id + "-";
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			key += random.nextInt(10);
		}
		return key;
	}
}
