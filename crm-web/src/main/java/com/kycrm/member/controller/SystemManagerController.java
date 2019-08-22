package com.kycrm.member.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.remoting.RemotingException;
import com.alibaba.dubbo.remoting.TimeoutException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.message.BatchSmsData;
import com.kycrm.member.domain.entity.other.MobileSetting;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.message.AppointNumberSendVO;
import com.kycrm.member.domain.vo.tradecenter.ShortLinkVo;
import com.kycrm.member.service.message.IMultithreadBatchSmsService;
import com.kycrm.member.service.message.ISensitiveWordService;
import com.kycrm.member.service.other.IMobileSettingService;
import com.kycrm.member.service.other.IShortLinkService;
import com.kycrm.member.service.redis.ICacheService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.IpAddressUtil;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MobileRegEx;
import com.kycrm.util.MsgType;
import com.kycrm.util.RedisConstant;
import com.kycrm.util.SmsCalculateUtil;

/**
 * @ClassName: SystemManagerController
 * @Description 系统管理控制层(整合):<br/>
 *              1:后台管理模块<br/>
 *              2:所有模块的验证码<br/>
 *              3:短链接 <br/>
 *              4:店铺名称
 * @author jackstraw_yu
 * @date 2018年1月15日 下午4:15:10
 */
@Controller
@RequestMapping("/systemManage")
public class SystemManagerController extends BaseController {

	private static final Log logger = LogFactory.getLog(SystemManagerController.class);
	/**
	 * 前台验证码标识
	 */
	private final String INDEX_SIGN = "INDEX";
	/**
	 * 分隔符
	 */
	private final String SEPARATOR = "-";
	/**
	 * 手机验证码正则表达式
	 */
	private final Pattern REGEX_CODE = Pattern.compile("([0-9]){6}");

	@Autowired
	private SessionProvider sessionProvider;

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private IMobileSettingService mobileSettingService;

	@Autowired
	private ICacheService cacheService;

	@Autowired
	private IUserAccountService userAccountService;

	@Autowired
	private IMultithreadBatchSmsService multithreadBatchSmsService;

	@Autowired
	private ISensitiveWordService sensitiveWordService;

	@Autowired
	private IShortLinkService shortLinkService;

	// =================================首页小红包====================================
	/**
	 * @Description: 首页加载后,判断用户: 1,有没有填写过手机号<br/>
	 *               2,是否赠送了客户500条短信<br/>
	 * @param request
	 * @param response
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月18日 下午3:47:40
	 */
	@ResponseBody
	@RequestMapping(value = "/showIndexFrame", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public String showIndexFrame(HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();

		try {
			UserInfo u = userInfoService.findUserInfo(user.getId());
			if (null != u) {
				if (u.getMobile() == null && u.getHasProvide() == null)
					return successReusltMap(null).toJson();
			}
		} catch (Exception e) {
			logger.error("******************首页小红包*************** Exception:" + e.getMessage());
		}
		return failureReusltMap(null).toJson();
	}

	/**
	 * @Description 首页小红包,点击获取验证码接口 <br/>
	 *              预防重复点击发送||一天之内的发送上限?如果用户此次发送验证码的次数超过30次 --不予再次发送
	 * @param request
	 * @param response
	 * @param mobile
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月22日 上午11:49:02
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/indexSecurityCode", method = RequestMethod.POST)
	public String indexSecurityCode(@RequestBody String params, HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		// 装换对象
		Map<String, String> map = new HashMap<String, String>();
		if (null != params && !"".equals(params)) {
			try {
				map = JsonUtil.paramsJsonToObject(params, Map.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		String mobile = null;
		if (map != null && map.size() > 0) {
			mobile = map.get("mobile");
		}
		if (mobile == null || "".equals(mobile.trim()) || !MobileRegEx.validateMobile(mobile))
			return failureReusltMap(ApiResult.MOBILE_WRONG).toJson();

		// 预防重复点击发送||一天之内的发送上限?
		int times = getCodeUseTimes(user.getId());
		if (times >= 30)
			return failureReusltMap(ApiResult.SECURITY_CODE_USE_TOP).toJson();

		// 验证通过开始生成并发送验证码
		String code = produceCode(), content = Constants.MESSAGE_VALIDATECODE_CONTNET;
		content = content.replace("CODE", code);

		// 是否根据成功失败作用户提示?
		boolean result = mobileSettingService.sendSecurityCodeMessage(content, mobile);
		if (result == false)
			return failureReusltMap(ApiResult.SEND_SMS_FAILURE).toJson();

		sessionProvider
				.putStrValueWithExpireTime(
						RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_KEY + this.SEPARATOR + this.INDEX_SIGN
								+ this.SEPARATOR + user.getTaobaoUserNick() + this.SEPARATOR + mobile,
						code, TimeUnit.MINUTES, 5l);
		// 放入使用次数
		putCodeUseTimes(user.getId(), ++times);

		return successReusltMap(ApiResult.SEND_SMS_SUCCESS).toJson();
	}

	/**
	 * @Description: 首页填写小红包: 1,判断用户是否有后台设置,没有后台设置,保存一条初始化后台设置<br/>
	 *               2:保存用户的手机号,qq号,赠送500条短信<br/>
	 * @param request
	 * @param response
	 * @param mobile
	 * @param qqNum
	 * @param code
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月22日 下午2:18:43
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "/saveUserInformation", method = RequestMethod.POST)
	public String saveUserInformation(@RequestBody String params, HttpServletRequest request,
			HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		// 装换对象
		Map<String, String> map = new HashMap<String, String>();
		if (null != params && !"".equals(params)) {
			try {
				map = JsonUtil.paramsJsonToObject(params, Map.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		String mobile = null, code = null, qqNum = null;
		if (map != null & map.size() > 0) {
			mobile = map.get("mobile");
			code = map.get("code");
			qqNum = map.get("qqNum");
		}
		if (mobile == null || "".equals(mobile) || !MobileRegEx.validateMobile(mobile))
			return failureReusltMap(ApiResult.MOBILE_WRONG).toJson();

		if (code == null || "".equals(code) || !this.REGEX_CODE.matcher(code).matches())
			return failureReusltMap(ApiResult.SECURITY_CODE_ERROR).toJson();

		String cacheCode = sessionProvider
				.getStrValue(RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_KEY + this.SEPARATOR + this.INDEX_SIGN
						+ this.SEPARATOR + user.getTaobaoUserNick() + this.SEPARATOR + mobile);

		if (cacheCode == null || !cacheCode.equals(code))
			return failureReusltMap(ApiResult.MOBILE_OR_CODE_ERROR).toJson();

		try {
			MobileSetting ms = new MobileSetting();
			ms.setExpediting(false);
			ms.setMessageRemainder(false);
			ms.setMessageCount(50);
			ms.setServiceExpire(false);
			ms.setActivityNotice(true);
			ms.setMobile(mobile);
			ms.setUid(user.getId());
			ms.setUserId(user.getTaobaoUserNick());
			ms.setCreatedBy(user.getTaobaoUserNick());
			ms.setLastModifiedBy(user.getTaobaoUserNick());

			// 1:保存用户手机号qq号,给用户添加500条短信,初始化后台设置
			mobileSettingService.saveInitMobileSetting(ms, user, qqNum);
		} catch (Exception e) {
			logger.error("******************首页小红包*************** Exception:" + e.getMessage());
			return failureReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}

		// 2:移除验证码
		sessionProvider.removeStrValueByKey(RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_KEY + this.SEPARATOR
				+ this.INDEX_SIGN + this.SEPARATOR + user.getTaobaoUserNick() + this.SEPARATOR + mobile);

		return successReusltMap(ApiResult.OPERATION_SUCCESS).toJson();
	}

	// ===================================测试短信发送==========================================
	@ResponseBody
	@RequestMapping(value = "/testSendSMS", produces = "text/html;charset=UTF-8")
	public String testSendSMS(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();

		AppointNumberSendVO vo = new AppointNumberSendVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, AppointNumberSendVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}

		// 判断电话号码
		if (null == vo.getPhones() || vo.getPhones().size() == 0) {
			return failureReusltMap(ApiResult.MOBILE_IS_NULL).toJson();
		}

		// 判断短信
		if (null == vo.getContent() || "".equals(vo.getContent())) {
			return failureReusltMap(ApiResult.SMS_CONTENT_NULL).toJson();
		}

		// 判断签名
		if (null == vo.getAutograph() || "".equals(vo.getAutograph())) {
			return failureReusltMap(ApiResult.SMS_AUTOGRAPH_NULL).toJson();
		}

		// 获取单条短信发送条数
		Integer deductionNum = SmsCalculateUtil.getActualDeduction(vo.getContent());
		// 查询用户的短信条数
		Long smsNum = userAccountService.findUserAccountSms(user.getId());
		// 判断当前短信条数是否大于发送条数
		if (smsNum <= vo.getPhones().size() * deductionNum) {
			return failureReusltMap(ApiResult.SMS_NOT_ENOUGH).toJson();
		}
		Set<String> set = this.disposePhones(vo.getPhones());
		if (null != set && set.size() > 0) {
			String[] phones = this.setTurnArray(set);
			String ipAddress = IpAddressUtil.getIpAddress(request);

			// 封装数据
			BatchSmsData batchSmsData = new BatchSmsData(phones);
			batchSmsData.setUid(user.getId());
			batchSmsData.setUserId(user.getTaobaoUserNick());
			batchSmsData.setChannel(vo.getAutograph());
			batchSmsData.setAutograph(vo.getAutograph());
			batchSmsData.setContent(vo.getContent());
			batchSmsData.setType(MsgType.MSG_CSFS);
			batchSmsData.setVip(Boolean.FALSE);
			batchSmsData.setIpAdd(ipAddress);

			// 调用发送
			Map<String, Integer> map = multithreadBatchSmsService.batchOperateSms(batchSmsData);

			int success = map.get("succeedNum");
			if (success > 0) {
				return successReusltMap(ApiResult.SEND_SMS_SUCCESS).toJson();
			}
		} else {
			return failureReusltMap(ApiResult.MOBILE_WRONG).toJson();
		}

		return failureReusltMap(ApiResult.SEND_SMS_FAILURE).toJson();
	}

	// =================================店铺名称
	/**
	 * @Description 展示店铺名称
	 * @param request
	 * @param response
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月23日 上午10:27:33
	 */
	@RequestMapping(value = "/showShopName", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String showShopName(HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();

		String shopName = null;
		logger.info("查询的用户id为" + user.getId());
		try {
			shopName = userInfoService.queryShopName(user);
			logger.info("用户为" + user.getId() + "查询用户的店铺名称为" + shopName);
		} catch (Exception e) {
			logger.error("##################### showShopName() Exception:" + e.getMessage());
			// DUBBO 异常?
			if (e.getCause() instanceof TimeoutException)
				return exceptionReusltMap(ApiResult.TIMEOUT_EXCEPTION_TRY_AGAGIN).toJson();
			// DUBBO 异常?
			if (e.getCause() instanceof RemotingException)
				return exceptionReusltMap(ApiResult.REMOTING_EXCEPTION_TRY_AGAIN).toJson();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		return successReusltMap(null).put(ApiResult.API_RESULT, shopName).toJson();
	}

	/**
	 * @Description 修改店铺名称
	 * @param request
	 * @param response
	 * @param shopName
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月23日 上午10:47:30
	 */
	@RequestMapping(value = "/modifyShopName", produces = "text/html;charset=UTF-8", method = RequestMethod.POST)
	@ResponseBody
	public String modifyShopName(HttpServletRequest request, HttpServletResponse response, @RequestBody String params) {
		if (params == null || "".equals(params))
			return failureReusltMap(ApiResult.PARAM_WRONG).toJson();
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();

		JSONObject parseObject = JSON.parseObject(params);
		String param = parseObject.getString("params");
		UserInfo from_Json = JsonUtil.fromJson(param, UserInfo.class);
		user.setShopName(from_Json.getShopName());
		try {
			userInfoService.modiftyShopName(user);
		} catch (Exception e) {
			logger.error("##################### modifyShopName() Exception:" + e.getMessage());
			// DUBBO 异常?
			if (e.getCause() instanceof TimeoutException)
				return exceptionReusltMap(ApiResult.TIMEOUT_EXCEPTION).toJson();
			// DUBBO 异常?
			if (e.getCause() instanceof RemotingException)
				return exceptionReusltMap(ApiResult.REMOTING_EXCEPTION).toJson();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		return successReusltMap(ApiResult.SAVE_SUCCESS).toJson();
	}

	/**
	 * 业务层 是不是缓存店铺名称 ?查询时先去缓存中拿 没有,再去数据库查,在放入数据库 更新是先保存到数据库,在放入缓存
	 * cacheService.putNoTime(RedisConstant.RedisCacheGroup.SHOP_NAME_CACHE,
	 * RedisConstant.RediskeyCacheGroup.SHOP_NAME_KEY+userId,shopName);
	 */

	// =================================短链接

	/**
	 * @Description: 淘宝相关短链接
	 * @param request
	 * @param response
	 * @param type
	 * @param value
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月22日 上午11:55:48
	 */
	@RequestMapping(value = "/shortLink", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String createTaobaoLink(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		ShortLinkVo slo = null;
		try {
			JSONObject parseObject = JSON.parseObject(params);
			String param = parseObject.getString("params");
			slo = JsonUtil.fromJson(param, ShortLinkVo.class);
		} catch (Exception e) {
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		if (slo.getType() == null || "".equals(slo.getType()))
			return failureReusltMap(null).toJson();
		if (!"LT_SHOP".equals(slo.getType())) {
			if (slo.getValue() == null || "".equals(slo.getValue()))
				return failureReusltMap(null).toJson();
		} else {
			// 店铺短链接不需要参数
			slo.setValue(null);
		}
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();

		String token = null;
		try {
			// 获取token用于生成短链接
			token = getSessionkey(user.getId());
		} catch (Exception e) {
			logger.error("##################### createTaobaoLink() Exception:" + e.getMessage());
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		if (token == null)
			return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		try {
			String link = mobileSettingService.getLink(token, slo);
			if (link == null)
				return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
			return successReusltMap(null).put(ApiResult.API_RESULT, link).toJson();
		} catch (Exception e) {
			logger.error("##################### createTaobaoLink() Exception:" + e.getMessage());
			// 此处给失败的提示,只是调用淘宝接口
			return exceptionReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		}
	}

	/**
	 * @Description: 淘宝相关短链接
	 * @param request
	 * @param response
	 * @param type
	 * @param value
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月22日 上午11:55:48
	 */
	@RequestMapping(value = "/marketingShortLink", method = RequestMethod.POST, produces = "text/html;charset=UTF-8")
	public @ResponseBody String marketingShortLink(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		ShortLinkVo slo = null;
		try {
			JSONObject parseObject = JSON.parseObject(params);
			String param = parseObject.getString("params");
			slo = JsonUtil.fromJson(param, ShortLinkVo.class);
		} catch (Exception e) {
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		if (slo.getType() == null || "".equals(slo.getType()))
			return failureReusltMap(null).toJson();
		if (!"LT_SHOP".equals(slo.getType())) {
			if (slo.getValue() == null || "".equals(slo.getValue()))
				return failureReusltMap(null).toJson();
		} else {
			// 店铺短链接不需要参数
			slo.setValue(null);
		}
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();

		String token = user.getAccessToken();
		if (token == null)
			return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		try {
			String link = mobileSettingService.getLink(token, slo);
			logger.info("UID = " + user.getId() + " 获取淘宝短链地址 = " + link);
			if (link == null || "".equals(link) || "null".equals(link.trim())) {
				return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
			} else {
				Map<String, Object> resultMap = this.shortLinkService.getConvertLinkByMarketingCenter(user.getId(),
						slo.getMessageType(), new Date(), link);
				if (resultMap == null || resultMap.size() == 0) {
					return failureReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
				} else {
					logger.info("UID = " + user.getId() + " 获取KYCRM自定义短链地址 = " + resultMap.get("taoBaoShortLink"));
					return successReusltMap(null).put(ApiResult.API_RESULT, resultMap).toJson();
				}
			}
		} catch (Exception e) {
			logger.error("##################### createTaobaoLink() Exception:" + e.getMessage());
			// 此处给失败的提示,只是调用淘宝接口
			return exceptionReusltMap(ApiResult.OPERATE_FAILURE_TRY_AGAIN).toJson();
		}
	}

	/**
	 * 敏感词屏蔽
	 * 
	 * @time 2018年12月11日 下午12:06:18
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sensitiveWord", produces = "text/html;charset=UTF-8")
	public String sensitiveWord(@RequestBody String params, HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();

		AppointNumberSendVO vo = new AppointNumberSendVO();
		if (null != params && !"".equals(params)) {
			try {
				vo = JsonUtil.paramsJsonToObject(params, AppointNumberSendVO.class);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("=================敏感词屏蔽====================json异常：" + e.getMessage());
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		List<String> list = sensitiveWordService.verifySensitiveWord(vo.getContent());
		if (list.size() > 2) {
			return successReusltMap(null).put(ApiResult.API_RESULT, list).toJson();
		}
		return successReusltMap(null).put(ApiResult.API_RESULT, null).toJson();

	}

	// =================================私有方法,为接口服务
	/**
	 * @Description 获取用户的sessionKey
	 * @param userNick
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月19日 下午4:01:33
	 */
	private String getSessionkey(Long uid) {
		String token = cacheService.getJsonStr(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
				RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + uid);
		if (null != token && !"".equals(token))
			return token;
		UserInfo user = userInfoService.findUserInfo(uid);
		if (user != null)
			if (null != user.getAccessToken() && !"".equals(user.getAccessToken())) {
				cacheService.putNoTime(RedisConstant.RedisCacheGroup.USRENICK_TOKEN_CACHE,
						RedisConstant.RediskeyCacheGroup.USRENICK_TOKEN_CACHE_KEY + uid, user.getAccessToken(), false);
				return user.getAccessToken();
			}
		return null;
	}

	/**
	 * @Description: 获取用户一天之内使用发送验证码的次数<br/>
	 *               如果用户此次发送验证码的次数超过30次 --不予再次发送
	 * @param @param
	 *            userNick
	 * @return int 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月22日 上午11:43:09
	 */
	private int getCodeUseTimes(Long uid) {
		String num = sessionProvider
				.getStrValue(RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_TOP_KEY + this.SEPARATOR + uid);
		int times = 0;
		if (num != null && !"".equals(num))
			try {
				times = Integer.valueOf(num.trim());
			} catch (Exception e) {
				times = 0;
			}
		return times;
	}

	/**
	 * @Description: 放入用户一天之内使用发送验证码的次数<br/>
	 *               如果用户此次发送验证码的次数超过30次 --不予再次发送
	 * @param userNick
	 * @param times
	 * @return void 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月22日 上午11:44:09
	 */
	private void putCodeUseTimes(Long uid, int times) {
		sessionProvider.putStrValueWithExpireTime(
				RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_TOP_KEY + this.SEPARATOR + uid, times + "",
				TimeUnit.MILLISECONDS, DateUtils.getMillisOverToday());
	}

	/**
	 * @Description 返回一个6位数的随机验证码<br/>
	 *              所有线程共享一个ThreadLocalRandom实例<br/>
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月22日 下午12:03:38
	 */
	private String produceCode() {
		String code = "";
		ThreadLocalRandom random = ThreadLocalRandom.current();
		for (int i = 0; i < 6; i++) {
			code += random.nextInt(10);
		}
		return code;
	}

	/**
	 * set转string数组
	 * 
	 * @author HL
	 * @time 2018年8月9日 下午2:25:08
	 * @return
	 */
	private String[] setTurnArray(Set<String> correctPhone) {
		String str = correctPhone.toString().replace("[", "").replace("]", "").replace(" ", "");
		String[] array = str.trim().split(",");
		return array;
	}

	/**
	 * 处理电话号码
	 * 
	 * @author HL
	 * @time 2018年8月9日 下午2:25:08
	 * @return
	 */
	private Set<String> disposePhones(List<String> phones) {
		Set<String> phoneSet = new HashSet<String>();// 正确的手机号码
		for (String p : phones) {
			if (MobileRegEx.validateMobile(p)) {
				phoneSet.add(p);
			}
		}
		return phoneSet;
	}
}
