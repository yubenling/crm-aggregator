
/** 
* @Title: LoginController.java 
* @Package com.kycrm.member.controller 
* Copyright: Copyright (c) 2017 
* Company:北京冰点零度科技有限公司 *
* @author zlp 
* @date 2017年12月29日 下午6:10:22 
* @version V1.0
*/
package com.kycrm.member.controller;

import java.net.URLDecoder;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alipay.api.internal.util.WebUtils;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserLoginInfo;
import com.kycrm.member.service.effect.IItemDetailService;
import com.kycrm.member.service.effect.IMarketingCenterEffectService;
import com.kycrm.member.service.item.IItemService;
import com.kycrm.member.service.login.ILoginService;
import com.kycrm.member.service.member.IMemberDTOService;
import com.kycrm.member.service.message.IMsgTempTradeHistoryService;
import com.kycrm.member.service.message.IMsgTempTradeService;
import com.kycrm.member.service.message.ISmsRecordDTOService;
import com.kycrm.member.service.order.IOrderDTOService;
import com.kycrm.member.service.other.ITaskNodeService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.syn.IRefundService;
import com.kycrm.member.service.trade.ITradeDTOService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.GetIpAddress;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.thread.MyFixedThreadPool;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.JushitaJdpUserAddRequest;
import com.taobao.api.request.ShopGetRequest;
import com.taobao.api.request.TmcUserGetRequest;
import com.taobao.api.request.VasSubscribeGetRequest;
import com.taobao.api.response.JushitaJdpUserAddResponse;
import com.taobao.api.response.ShopGetResponse;
import com.taobao.api.response.TmcUserGetResponse;
import com.taobao.api.response.VasSubscribeGetResponse;

/**
 * @ClassName: LoginController
 * @Description 此接口为服务跳转接口<br/>
 *              为方便大家测试和项目后期发版，<br/>
 *              麻烦需要在此类中添加测试方法和注入的小伙伴将内容写在分割下下面，<br/>
 *              需要提交代码的时候或者情况下(eg:添加的测试方法可能接下来还要用)<br/>
 *              将自己添加的内容使用ctrl+/注释掉
 * @author zlp
 * @date 2018年1月26日 下午4:26:58
 */
@SuppressWarnings("unused")
@Controller
@RequestMapping(value = "/login")
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(LoginController.class);

	private TaobaoClient client = new DefaultTaobaoClient(Constants.TAOBAO_URL, Constants.TOP_APP_KEY,
			Constants.TOP_APP_SECRET);

	@Autowired
	private IUserInfoService userInfoService;
	@Autowired
	private SessionProvider sessionProvider;
	@Autowired
	private ITaskNodeService taskNodeService;
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private ILoginService loginService;
	@Autowired
	private IMemberDTOService memberDTOService;
	@Autowired
	private ITradeDTOService tradeDTOService;
	@Autowired
	private IOrderDTOService orderDTOService;
	@Autowired
	private ISmsRecordDTOService smsRecordDTOService;
	@Autowired
	private IMsgTempTradeService msgTempTradeService;
	@Autowired
	private IMsgTempTradeHistoryService msgTempTradeHistoryService;
	@Autowired
	private IItemDetailService itemDetailService;
	@Autowired
	private IMarketingCenterEffectService marketingCenterEffectService;
	@Autowired
	private IUserAccountService userAccountService;
	@Autowired
	private IItemService itemService;
	@Autowired
	private IRefundService refundService;

	private static String ENVIORMENT = "";
	private static String ONLINE_URL = "";

	static {
		ResourceBundle resource = ResourceBundle.getBundle("env");
		ENVIORMENT = resource.getString("enviorment");
		ONLINE_URL = resource.getString("online.url");
	}

	/**
	 * testLogin(测试登陆输入用户名，TOKEN) @Title: testLogin @param @return 设定文件 @return
	 * String 返回类型 @throws
	 */
	@RequestMapping(value = "/testLogin", produces = "text/html;charset=UTF-8")
	public String testLogin() {
		return "testlogin";
	}

	/**
	 * @Description 测试登录,生成cookie令牌
	 * @param request
	 * @param response
	 * @param userNick
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月26日 下午4:21:40
	 */
	@RequestMapping(value = "/testIndex", produces = "text/html;charset=UTF-8")
	public String loginIndex(HttpServletRequest request, HttpServletResponse response, String token, Long uid) {
		// 1,校验字符串
		if (uid == null) {
			return "404";
		}
		// 2,判断数据库是否存在该用户
		UserInfo userInfo = userInfoService.findUserInfo(uid);
		if (userInfo == null)
			return "404";
		userInfo.setId(uid);
		if (token != null && !"".equals(token)) {
			userInfo.setAccessToken(token);
			cacheService.putNoTime(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
					RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + uid, token, false);
		}
		logger.info("userinfo=============:" + com.alibaba.fastjson.JSONObject.toJSONString(userInfo));
		sessionProvider.setAttributeForUser(RequestUtil.getCSESSIONID(request, response), userInfo);
		// test登陆时 创建表格
		loginService.updateTable(uid, userInfo.getTaobaoUserNick());
		String osName = System.getProperty("os.name");
		if (osName.startsWith("Windows")) {
			return "index";
		} else {
			if ("test".equals(ENVIORMENT)) {
				return "redirect:http://oss2.kycrm.com/newIndex/index.html";
			} else {
				return "redirect:" + ONLINE_URL;
			}
		}
	}

	@RequestMapping(value = "/login", produces = "text/html;charset=UTF-8")
	public String toIndex(HttpServletRequest request, HttpServletResponse response, Model model, String code) {
		String url = Constants.TOP_BUY_APP_URL;
		logger.info("================================" + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ "<=>开始根据买家code生成token================================");
		if (code != null && !"".equals(code.trim())) {
			try {
				url = this.getToken(request, response, code, model);
			} catch (Exception e) {
				logger.error("用户登录时生成token并获取url时异常!!^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
				url = Constants.TOP_BUY_APP_URL;
			}
		}
		return url;
	}

	/**
	 * 根据买家的code生成token（即sessionKey）
	 * 
	 * @param request
	 * @param code
	 * @throws ParseException
	 */
	@SuppressWarnings("deprecation")
	private String getToken(HttpServletRequest request, HttpServletResponse response, String code, Model model) {
		// 将用户信息放入session中
		HttpSession session = request.getSession();
		/***
		 * 每次从淘宝跳转到 本项目中 的code都不一致 所以此处原有的代码暂时先删除
		 **/
		Map<String, String> props = new HashMap<String, String>();
		props.put("grant_type", "authorization_code");
		// 测试时，需把test参数换成自己应用对应的值
		props.put("code", code);
		props.put("client_id", Constants.TOP_APP_KEY);
		props.put("client_secret", Constants.TOP_APP_SECRET);
		props.put("redirect_uri", Constants.TAOBAO_CALLBACK_URL);
		props.put("view", "web");

		String json = null;
		try {
			json = WebUtils.doPost(Constants.TAOBAO_TOKEN_URL, props, 30000, 30000);
		} catch (Exception e) {
			logger.error("WebUtils.doPost异常!!^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
			return Constants.TOP_BUY_APP_URL;
		}
		String access_token = null, taobao_user_nick = null, taobao_user_id = null, expires_in = null;
		logger.debug("------------------- 用户登录请求的json原数据  -------------------------" + json);
		if (json != null && !"".equals(json)) {
			JSONObject json_test = JSONObject.fromObject(json);
			access_token = json_test.getString("access_token");
			logger.info("json_test = " + json_test);
			taobao_user_nick = json_test.getString("taobao_user_nick");
			taobao_user_id = json_test.getString("taobao_user_id");
			expires_in = json_test.getString("expires_in");
			try {
				taobao_user_nick = URLDecoder.decode(taobao_user_nick, "utf-8");
			} catch (Exception e) {
				logger.error("URLDecoder.decode编码异常!!^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
				return Constants.TOP_BUY_APP_URL;
			}
			logger.info("================================ access_token：" + access_token + " taobao_user_nick:"
					+ taobao_user_nick + " taobao_user_id:" + taobao_user_id);
		}
		logger.info("================================" + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ "<=>用户：" + taobao_user_nick + "准备开始登录================================");
		// 如果用户名为空，跳转到登录页面
		if (taobao_user_nick == null || "".equals(taobao_user_nick))
			return Constants.TOP_BUY_APP_URL;
		// 判断用户表是否存在用户信息
		logger.info("================================" + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ "<=>用户：" + taobao_user_nick
				+ "================================开始从数据库中获取用户的信息================================");
		UserInfo user = userInfoService.findUserInfo(taobao_user_nick);
		logger.info("================================" + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ "<=>用户：" + taobao_user_nick
				+ "================================获取用户数据完成，开始判断用户到期时间！================================");
		// 判断如果为空则添加用户信息
		Date exTime = null;
		Long uid = null;
		if (user == null) {
			// 启动一个线程，同步新用户一天的订单数据
			// asyncHandleData(access_token, taobao_user_nick);
			logger.info("================================"
					+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户：" + taobao_user_nick
					+ "================================获取用户的应用到期时间！！！================================");
			// 查询用户应用的到期时间
			exTime = this.getUserOrderInfo(taobao_user_nick);
			uid = this.addNewUser(taobao_user_nick, taobao_user_id, access_token, exTime, expires_in);
			if (uid == null) {
				logger.info("================================"
						+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户：" + taobao_user_nick
						+ "================================添加新用户信息失败！=======uid=" + uid);
				return Constants.TAOBAO_URL;
			}

			user = userInfoService.findUserInfo(taobao_user_nick);
			if (uid == null) {
				uid = user.getId();
			}
			userAccountService.doCreateUserAccountByUser(user.getId(), taobao_user_nick, 0L);
			logger.info("================================"
					+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户："
					+ com.alibaba.fastjson.JSONObject.toJSONString(user) + "==============" + taobao_user_nick
					+ "================================添加新用户信息完成！================================");

			// 创建每个用户的新表 // 订单表(字段√ 主键↑ 索引√ 建表√建索引√)【1】
			tradeDTOService.doCreateTableByNewUser(user.getId());

			logger.info("==================tradeDTOService.doCreateTableByNewUser=============="
					+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户："
					+ com.alibaba.fastjson.JSONObject.toJSONString(user) + "==============" + taobao_user_nick
					+ "================================添加新用户信息完成！================================");
			// 子订单表(字段√ 主键↑索引√ 建表√建索引√)【2】
			orderDTOService.doCreateTableByNewUser(user.getId());
			// 会员表(字段√ 主键↑ 索引√ 建表√建索引√)【3】
			memberDTOService.doCreateTableByNewUser(user.getId());
			// 会有多地址表(字段√主键↑ 索引√ 建表√建索引√)【4】
			memberDTOService.doCreateMemberReceiveDetailTableByNewUser(user.getId());
			// 评价表(字段√ 主键↑索引√ 建表√建索引√)【5】
			memberDTOService.doCreateTradeRatesTableByNewUser(user.getId());
			// 卖家会员表(字段√ 主键↑ 索引√ 建表√建索引√)【6】
			memberDTOService.doCreateMemberItemAmountTableByNewUser(user.getId());
			// 黑名单表(字段√ 主键↑ 索引√建表√建索引√)【7】
			memberDTOService.doCreateSmsBlacklistTableByNewUser(user.getId());
			// 短信记录表(字段√ 主键↑ 索引√ 建表√建索引√)【8】
			smsRecordDTOService.doCreateTableByNewUser(user.getId());
			// 存放可以与msg匹配的订单表(字段√ 主键↑ 索引√ 建表√建索引√)【9】
			msgTempTradeService.doCreateTable(user.getId());
			// 存放可以与msg匹配的订单历史表(字段√ 主键↑ 索引√ 建表√建索引√)【10】
			msgTempTradeHistoryService.doCreateTable(user.getId());
			// 用于计算商品详情的临时订单表(字段√ 主键↑ 索引√ 建表√建索引√)【11】
			itemDetailService.doCreateTable(user.getId()); //
			// 用于计算商品详情的临时订单历史表(字段√ 主键↑ 索引√ 建表√建索引√)【12】
			itemDetailService.doCreateItemHistory(user.getId());
			// 效果分析结果表(字段√主键↑ 索引√ 建表√建索引√)【13】
			marketingCenterEffectService.doCreateTable(user.getId());
			// 商品表(字段√主键↑ 索引√ 建表√建索引√)【14】
			itemService.doCreateTable(user.getId());
			// 创建会员筛选记录表【15】
			memberDTOService.doCreatePremiumFilterRecordTable(user.getId());
			// 创建退款表
			refundService.doCreateTableByNewUser(user.getId());
			logger.info("================================"
					+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户："
					+ com.alibaba.fastjson.JSONObject.toJSONString(user) + "==============" + taobao_user_nick
					+ "================================添加新用户信息完成！================================");
		} else {
			uid = user.getId();

			logger.info("uid:::::::::::::::::::::::::::::" + uid);
			logger.info(com.alibaba.fastjson.JSONObject.toJSONString(user));

			if (null != user.getStatus() && user.getStatus().equals("1")) {
				return Constants.TOP_BUY_APP_URL;
			}
			logger.info("================================"
					+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户：" + taobao_user_nick
					+ "================================用户信息不为空，判断用户过期时间================================");
			// 判断用户的过期时间
			int expirationTimeStatus = -1;
			exTime = this.getUserOrderInfo(taobao_user_nick);
			if (exTime != null) {
				expirationTimeStatus = exTime.compareTo(new Date());
			} else {
				// 如果查询为空则判断数据库中存储的时间是否过期
				logger.info("================================"
						+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户：" + uid
						+ "======================" + taobao_user_nick
						+ "================================重新获取用户的应用到期时间！！！================================");
				exTime = user.getExpirationTime();
				expirationTimeStatus = exTime.compareTo(new Date());
			}

			if (expirationTimeStatus >= 0) {
				// 更新用户token到数据库
				this.updateUserInfo(user.getId(), access_token, taobao_user_nick, exTime, expires_in);
			} else {
				logger.info("================================"
						+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户：" + taobao_user_nick
						+ "================================用户已过期，跳转到订购页面================================");
				return Constants.TOP_BUY_APP_URL;
			}
		}
		user.setAccessToken(access_token);
		sessionProvider.setAttributeForUser(RequestUtil.getCSESSIONID(request, response), user);
		// 将新用户添加到聚石塔同步列表
		this.addJstUser(access_token);
		if (!getTmcUserTopic(taobao_user_nick)) {
			this.userInfoService.addUserPermitByMySql(uid, null);
		}
		// 根据卖家昵称获取店铺名称(短信签名)
		logger.info("================================" + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ "<=>用户：" + taobao_user_nick + "================================");
		String ShopName = null;
		if (user.getShopName() != null && !"".equals(user.getShopName())) {
			ShopName = user.getShopName();
		} else {
			ShopName = this.getShopName(taobao_user_nick);
		}
		session.setAttribute("taobao_user_id", taobao_user_id);
		session.setAttribute("taobao_user_nick", taobao_user_nick);
		session.setAttribute("access_token", access_token);
		session.setAttribute("ShopName", ShopName);

		// 将用户 的access_token->sessionKey放到redis中方便后期加密解密好获取
		// ????????

		// 获取页面数据
		// this.getTradeData(model,taobao_user_nick);
		logger.info("================================" + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ "<=>用户：" + taobao_user_nick + "id::::::::::::" + uid
				+ "================================用户验证成功,跳转首页!================================");

		UserLoginInfo userLoginInfo = new UserLoginInfo();
		if (uid == null) {
			userLoginInfo.setUid(user.getId());
		} else {
			userLoginInfo.setUid(uid);
		}
		userLoginInfo.setSellerNick(taobao_user_nick);
		userLoginInfo.setIpAddress(GetIpAddress.getIpAddress(request));
		userLoginInfo.setLoginTime(new Date());
		userLoginInfo.setCreatedBy(uid + "");
		userLoginInfo.setCreatedDate(new Date());
		userLoginInfo.setLastModifiedBy(uid + "");
		userLoginInfo.setLastModifiedDate(new Date());
		try {
			userInfoService.doCreateInfoByLogin(userLoginInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:" + ONLINE_URL;
	}

	/**
	 * 根据用户昵称和应用服务code获取用户应用的到期时间
	 * 
	 * @param taobao_user_nick
	 * @return
	 */
	private Date getUserOrderInfo(String taobao_user_nick) {
		VasSubscribeGetRequest req = new VasSubscribeGetRequest();
		req.setArticleCode(Constants.TOP_APP_CODE);
		req.setNick(taobao_user_nick);
		VasSubscribeGetResponse rsp = null;
		try {
			rsp = client.execute(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rsp.getArticleUserSubscribes() != null) {
			int timeCount = rsp.getArticleUserSubscribes().size();
			return rsp.getArticleUserSubscribes().get(timeCount - 1).getDeadline();
		}
		return null;
	}

	/**
	 * @Description:更新用户的access_token
	 * @author jackstraw_yu
	 */
	private void updateUserInfo(Long uid, String access_token, String taobao_user_nick, Date exTime,
			String expires_in) {
		if (uid == null) {
			return;
		}
		logger.info("================================" + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ "<=>用户：" + taobao_user_nick
				+ "================================更新用户的access_token================================");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("access_token", access_token);
		logger.info("access_token = " + access_token);
		map.put("taobao_user_nick", taobao_user_nick);
		map.put("expiration_time", exTime);
		map.put("expires_in", expires_in);
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
				RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + uid, access_token, false);
		cacheService.putNoTime(RedisConstant.RedisCacheGroup.SELLER_EXPIRATION_TIME,
				RedisConstant.RedisCacheGroup.SELLER_EXPIRATION_TIME + uid,
				Long.parseLong(expires_in) * 1000 + exTime.getTime(), false);
		try {
			userInfoService.updateUserInfoByLogin(uid, taobao_user_nick, access_token, exTime, expires_in, new Date(),
					null);
			// userInfoService.updateUserInfo(taobao_user_nick, access_token,
			// exTime, expires_in);
			logger.info("================================"
					+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户：" + taobao_user_nick
					+ "================================更新用户信息成功！！！================================");
		} catch (Exception e) {
			logger.error("^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^"
					+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户：" + taobao_user_nick
					+ "更新用户信息失败！！！^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		}
	}

	/**
	 * @Description:保存新的用户信息到数据库
	 * @author jackstraw_yu
	 */
	private Long addNewUser(String taobao_user_nick, String taobao_user_id, String access_token, Date exTime,
			String expires_in) {
		logger.info("================================" + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ "<=>用户：" + taobao_user_nick
				+ "================================用户信息为空，添加新用户至数据库中！！！================================");
		/*
		 * UserInfo ui = new UserInfo(); ui.setTaobaoUserNick(taobao_user_nick);
		 * ui.setCreateTime(new Date()); ui.setLastLoginDate(new Date());
		 * ui.setStatus(0); ui.setTaobaoUserId(taobao_user_id);
		 * ui.setAccessToken(access_token); ui.setExpirationTime(exTime);
		 * ui.setExpirationSecs(expires_in == null ? 0 :
		 * Long.parseLong(expires_in));
		 */

		try {
			Long uid = userInfoService.addUserInfo(taobao_user_nick, taobao_user_id, access_token, exTime, expires_in);
			cacheService.putNoTime(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
					RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + uid, access_token, false);
			cacheService.putNoTime(RedisConstant.RedisCacheGroup.SELLER_EXPIRATION_TIME,
					RedisConstant.RedisCacheGroup.SELLER_EXPIRATION_TIME + taobao_user_nick,
					Long.parseLong(expires_in) * 1000 + exTime.getTime(), false);
			logger.info("================================"
					+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户：" + taobao_user_nick
					+ "================================添加新用户成功！！！！================================");
			return uid;
		} catch (Exception e) {
			logger.error("^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^"
					+ DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT) + "<=>用户：" + taobao_user_nick
					+ "添加新用户失败！！！！^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^|^_^");
		}
		return null;
	}

	private void addJstUser(String token) {
		logger.info("================================" + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ "<=>添加用户到聚石塔数据同步列表================================");
		JushitaJdpUserAddRequest req = new JushitaJdpUserAddRequest();
		req.setRdsName(Constants.RDS_MYSQL_ACCOUNT);
		req.setTopics("item,trade");
		req.setHistoryDays(90L);
		JushitaJdpUserAddResponse rsp = null;
		try {
			rsp = client.execute(req, token);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据用户昵称查询用户是否添加到tmc同步列表中
	 * 
	 * @param taobao_user_nick
	 * @return
	 */
	public boolean getTmcUserTopic(String taobao_user_nick) {
		logger.info("================================" + DateUtils.formatDate(new Date(), DateUtils.DEFAULT_TIME_FORMAT)
				+ "<=>查询用户是否在tmc同步列表中================================");
		TmcUserGetRequest req = new TmcUserGetRequest();
		req.setFields("user_nick,topics,user_id,is_valid,created,modified");
		req.setNick(taobao_user_nick);
		req.setUserPlatform("tbUIC");
		TmcUserGetResponse rsp = null;
		try {
			rsp = client.execute(req);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		if (rsp.getTmcUser() != null && rsp.getTmcUser().getTopics() != null
				&& rsp.getTmcUser().getTopics().size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 创建人：邱洋
	 * 
	 * @Title: 根据卖家昵称获取店铺名称 @date 2017年1月23日--下午2:28:58 @return String @throws
	 */
	private String getShopName(String taobao_user_nick) {
		ShopGetRequest req = new ShopGetRequest();
		req.setFields("sid,cid,title,nick,desc,bulletin,pic_path,created,modified");
		req.setNick(taobao_user_nick);
		ShopGetResponse rsp = null;
		try {
			rsp = client.execute(req);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (rsp.getShop() != null) {
			return rsp.getShop().getTitle();
		} else {
			return null;
		}

	}

	/**
	 * 创建人：邱洋
	 * 
	 * @Title: 开启一个线程同步新用户一天之内的订单数据 @date 2017年4月7日--上午10:45:43 @return
	 *         void @throws
	 */
	// private void asyncHandleData(final String access_token, final String
	// userId) {
	// MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
	// @Override
	// public void run() {
	// firstGetUserTrade.firstGetUserTrade(access_token, userId);
	// }
	// });
	// }

	/**
	 * 用户建表 doCreateTable(这里用一句话描述这个方法的作用) @Title: doCreateTable @param @param
	 * uid 设定文件 @return void 返回类型 @throws
	 */
	// private void doCreateTable(final Long uid) {
	// MyFixedThreadPool.getMyFixedThreadPool().execute(new Thread() {
	// @Override
	// public void run() {
	//
	// }
	// });
	// }
	// ================================测试方法分割线=====================================================
	// ================================测试方法分割线=====================================================
	// ================================测试方法分割线=====================================================
	// ================================测试方法分割线=====================================================

	// @Autowired
	// private IMemberInfoService memberInfoService;
	//
	// @RequestMapping(value="/login",produces="text/html;charset=UTF-8")
	// @ResponseBody
	// public String login(String userNick){
	// UserInfo userInfo = new UserInfo();
	//// userInfo.setName(userNick);
	//// UserInfo findUserByParam = userInfoService.findUserByParam(userInfo);
	// MemberInfo m = new MemberInfo();
	//// m.setTaobao_user_nick(findUserByParam.getName());
	//// m.setUserId(String.valueOf(findUserByParam.getId()));
	// MemberInfo findMemberByParam = memberInfoService.findMemberByParam(m);
	//
	// return findMemberByParam.getBuyerNick()+" --";
	// }
	//
	// @RequestMapping(value="/test",produces="text/html;charset=UTF-8")
	// @ResponseBody
	// public String test(String userNick){
	//// userInfo.setName(userNick);
	//// m.setTaobao_user_nick(findUserByParam.getName());
	//
	// return this.memberInfoService.findUserByNick(userNick, userNick);
	// }
	//
}
