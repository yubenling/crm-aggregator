package com.kycrm.member.controller;

import java.util.HashMap;
import java.util.Map;
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
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.other.MobileSetting;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.utils.pagination.Pagination;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingApplyVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingConfirmVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingDeleteRecordVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingReleaseBindingVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingSearchVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingSendMessageRecordVO;
import com.kycrm.member.domain.vo.multishopbinding.MultiShopBindingSendMessageVO;
import com.kycrm.member.service.multishopbinding.IMultiShopBindingSendMessageRecordService;
import com.kycrm.member.service.multishopbinding.IMultiShopBindingService;
import com.kycrm.member.service.other.IMobileSettingService;
import com.kycrm.member.service.user.IUserAccountService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MobileRegEx;
import com.kycrm.util.RedisConstant;
import com.taobao.api.SecretException;

/**
 * 多店铺绑定
 * 
 * @Author ZhengXiaoChen
 * @Date 2019年3月26日上午10:55:09
 * @Tags
 */
@Controller
@RequestMapping("/multiShopBinding")
public class MultiShopBindingController extends BaseController {

	private static final Log logger = LogFactory.getLog(MarketingMemberFilterController.class);

	@Autowired
	private SessionProvider sessionProvider;

	@Autowired
	private IMobileSettingService mobileSettingService;

	@Autowired
	private IMultiShopBindingService multiShopBindingService;

	@Autowired
	private IUserInfoService userInfoService;

	@Autowired
	private IUserAccountService userAccountService;

	@Autowired
	private IMultiShopBindingSendMessageRecordService multiShopBindingSendMessageRecordService;

	// 后台验证码标识
	private final String BACK_SIGN = "MULTI_SHOP_BINDING";

	// 分隔符
	private final String SEPARATOR = "-";

	// 手机验证码正则表达式
	private final Pattern REGEX_CODE = Pattern.compile("([0-9]){6}");

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 绑定店铺列表页
	 * @Date 2019年3月26日下午2:47:08
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/findBindingList")
	@ResponseBody
	public String findBindingList(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		Long uid = null;
		try {
			resultMap = new HashMap<String, Object>(4);
			// 查询条件实体
			MultiShopBindingSearchVO multiShopBindingSearchVO = null;
			if (null != params && !"".equals(params)) {
				multiShopBindingSearchVO = JsonUtil.paramsJsonToObject(params, MultiShopBindingSearchVO.class);
			}
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			uid = user.getId();
			if (uid == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Integer pageNo = multiShopBindingSearchVO.getPageNo();
			if (pageNo == null) {
				pageNo = 1;
			}
			// 使用分页工具进行分页列表查询
			String contextPath = request.getContextPath();
			if (multiShopBindingSearchVO.getMenuNumber() == 1) {
				logger.info("用户UID = " + uid + " 使用【多店铺绑定-绑定店铺页签】功能");
			} else if (multiShopBindingSearchVO.getMenuNumber() == 2) {
				logger.info("用户UID = " + uid + " 使用【多店铺绑定-主动申请页签】功能");
			} else if (multiShopBindingSearchVO.getMenuNumber() == 3) {
				logger.info("用户UID = " + uid + " 使用【多店铺绑定-接受申请页签】功能");
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Pagination pagination = this.multiShopBindingService.findBindingList(uid,
					multiShopBindingSearchVO.getMenuNumber(), contextPath, pageNo);
			// 会员分组添加成功后，跳转的页面
			int pageNomax = 0;
			int count = pagination.getTotalCount();
			// 每页显示条数
			int pageSize = 10;
			if (count < pageSize) {
				pageNomax = 1;
			} else {
				if (count % pageSize == 0) {
					pageNomax = count / pageSize;
				} else {
					pageNomax = count / pageSize + 1;
				}
			}
			resultMap.put("pageNoMax", pageNomax);
			resultMap.put("pageNo", pageNo);
			resultMap.put("pagination", pagination);
			return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = new HashMap<String, Object>(2);
			logger.error("用户UID = " + uid + " 使用【多店铺绑定-列表页】出错 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 申请绑定
	 * @Date 2019年3月26日下午2:47:08
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/applyBinding")
	@ResponseBody
	public String applyBinding(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		Long uid = null;
		try {
			resultMap = new HashMap<String, Object>(4);
			// 查询条件实体
			MultiShopBindingApplyVO multiShopBindingVO = null;
			if (null != params && !"".equals(params)) {
				multiShopBindingVO = JsonUtil.paramsJsonToObject(params, MultiShopBindingApplyVO.class);
			}
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			uid = user.getId();
			if (uid == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			multiShopBindingVO.setChildShopUid(uid);
			MobileSetting setting = this.mobileSettingService.findMobileSetting(uid);
			if (setting == null) {
				return failureReusltMap(ApiResult.NOT_FIND_MOBILE).toJson();
			}
			if (setting.getMobile() == null || "".equals(setting.getMobile())) {
				return failureReusltMap(ApiResult.MOBILE_IS_NULL).toJson();
			}
			multiShopBindingVO.setChildShopMobile(setting.getMobile());
			logger.info("用户UID = " + uid + " 使用【多店铺绑定-申请绑定】功能入参 = " + JsonUtil.toJson(multiShopBindingVO));
			Map<String, Object> validateMap = validateUserOperation(uid, multiShopBindingVO.getChildShopMobile(),
					multiShopBindingVO.getValidateCode());
			if (!(Boolean) validateMap.get("status")) {
				return failureReusltMap((String) validateMap.get("msgKey")).toJson();
			}
			// 不能自己绑定自己
			if (user.getTaobaoUserNick().equals(multiShopBindingVO.getFamilyShopName())) {
				return failureReusltMap(ApiResult.SAME_USER).toJson();
			}
			try {
				boolean ifExist = this.multiShopBindingService.checkIfExist(user.getTaobaoUserNick(),
						multiShopBindingVO.getFamilyShopName(), new Integer[] { 0, 1 });
				if (ifExist) {
					return failureReusltMap(ApiResult.ALREADY_BINDING).toJson();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 申请绑定服务调用
			Long result = this.multiShopBindingService.applyBinding(multiShopBindingVO);
			if (result == 1) {
				return successReusltMap("2073").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("4002").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = new HashMap<String, Object>(2);
			logger.error("用户UID = " + uid + " 使用【多店铺绑定-申请绑定】出错 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap(e.getMessage()).put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 解除绑定
	 * @Date 2019年3月26日下午5:15:13
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/releaseBinding")
	@ResponseBody
	public String releaseBinding(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		Long uid = null;
		try {
			resultMap = new HashMap<String, Object>(4);
			// 查询条件实体
			MultiShopBindingReleaseBindingVO releaseBindingVO = null;
			if (null != params && !"".equals(params)) {
				releaseBindingVO = JsonUtil.paramsJsonToObject(params, MultiShopBindingReleaseBindingVO.class);
			}
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			logger.info("用户UID = " + uid + " 使用【多店铺绑定-解除绑定】功能入参 = " + JsonUtil.toJson(releaseBindingVO));
			Long result = this.multiShopBindingService.releaseBinding(releaseBindingVO);
			if (result == 1) {
				return successReusltMap("2074").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("4003").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = new HashMap<String, Object>(2);
			logger.error("用户UID = " + uid + " 使用【多店铺绑定-解除绑定】出错 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 接受或者拒绝绑定
	 * @Date 2019年3月26日下午5:15:13
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/confirm")
	@ResponseBody
	public String confirm(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		Long uid = null;
		try {
			resultMap = new HashMap<String, Object>(4);
			// 查询条件实体
			MultiShopBindingConfirmVO confirmVO = null;
			if (null != params && !"".equals(params)) {
				confirmVO = JsonUtil.paramsJsonToObject(params, MultiShopBindingConfirmVO.class);
			}
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			uid = user.getId();
			logger.info("用户UID = " + uid + " 使用【多店铺绑定-接受或者拒绝绑定】功能入参 = " + JsonUtil.toJson(confirmVO));
			Long result = this.multiShopBindingService.confirm(confirmVO);
			if (result == 1) {
				if (confirmVO.getConfirm() == 1) {
					return successReusltMap("2075").put(ApiResult.API_RESULT, resultMap).toJson();
				} else {
					return successReusltMap("2076").put(ApiResult.API_RESULT, resultMap).toJson();
				}
			} else {
				if (confirmVO.getConfirm() == 1) {
					return failureReusltMap("4004").put(ApiResult.API_RESULT, resultMap).toJson();
				} else {
					return failureReusltMap("4005").put(ApiResult.API_RESULT, resultMap).toJson();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = new HashMap<String, Object>(2);
			logger.error("用户UID = " + uid + " 使用【多店铺绑定-接受或者拒绝绑定】出错 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 查询短信余额
	 * @Date 2019年3月26日下午5:15:13
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/findMessageBalance")
	@ResponseBody
	public String findMessageBalance(HttpServletRequest request, HttpServletResponse response) throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		Long uid = null;
		try {
			resultMap = new HashMap<String, Object>(1);
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			uid = user.getId();
			logger.info("用户UID = " + uid + " 使用【多店铺绑定-查询短信余额】功能");
			Long userAccountSms = this.userAccountService.findUserAccountSms(uid);
			logger.info("用户UID = " + uid + " 使用【多店铺绑定-查询短信余额】功能, 本次查询短信余额是 = " + userAccountSms);
			resultMap.put("userAccountSms", userAccountSms);
			return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = new HashMap<String, Object>(2);
			logger.error("用户UID = " + uid + " 使用【多店铺绑定-查询短信余额】出错 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 赠送短信
	 * @Date 2019年3月26日下午5:45:18
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/sendMessage")
	@ResponseBody
	public String sendMessage(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		Long uid = null;
		try {
			resultMap = new HashMap<String, Object>(4);
			// 查询条件实体
			MultiShopBindingSendMessageVO sendMessageVO = null;
			if (null != params && !"".equals(params)) {
				sendMessageVO = JsonUtil.paramsJsonToObject(params, MultiShopBindingSendMessageVO.class);
			}
			Integer sendMessageCount = sendMessageVO.getSendMessageCount();
			if (sendMessageCount == null || sendMessageCount <= 0) {
				return failureReusltMap("3006").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			uid = user.getId();
			sendMessageVO.setUid(uid);
			MobileSetting setting = this.mobileSettingService.findMobileSetting(uid);
			if (setting == null) {
				return failureReusltMap(ApiResult.NOT_FIND_MOBILE).toJson();
			}
			if (setting.getMobile() == null || "".equals(setting.getMobile())) {
				return failureReusltMap(ApiResult.MOBILE_IS_NULL).toJson();
			}
			sendMessageVO.setOriginMobile(setting.getMobile());
			logger.info("用户UID = " + uid + " 使用【多店铺绑定-赠送短信】功能入参 = " + JsonUtil.toJson(sendMessageVO));
			Map<String, Object> validateMap = validateUserOperation(uid, sendMessageVO.getOriginMobile(),
					sendMessageVO.getValidateCode());
			if (!(Boolean) validateMap.get("status")) {
				return failureReusltMap((String) validateMap.get("msgKey")).toJson();
			}
			Long result = this.multiShopBindingService.sendMessage(sendMessageVO);
			if (result == 1L) {
				return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = new HashMap<String, Object>(2);
			logger.error("用户UID = " + uid + " 使用【多店铺绑定-赠送短信】出错 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap(e.getMessage()).put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 赠送记录分页查询
	 * @Date 2019年3月26日下午2:47:08
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/findSendMessageRecordList")
	@ResponseBody
	public String findSendMessageRecordList(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		Long uid = null;
		try {
			resultMap = new HashMap<String, Object>(4);
			// 查询条件实体
			MultiShopBindingSendMessageRecordVO sendMessageRecordVO = null;
			if (null != params && !"".equals(params)) {
				sendMessageRecordVO = JsonUtil.paramsJsonToObject(params, MultiShopBindingSendMessageRecordVO.class);
			}
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			uid = user.getId();
			if (uid == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Integer pageNo = sendMessageRecordVO.getPageNo();
			if (pageNo == null) {
				sendMessageRecordVO.setPageNo(1);
			}
			// 使用分页工具进行分页列表查询
			String contextPath = request.getContextPath();
			logger.info("用户UID = " + uid + " 使用【多店铺绑定-赠送记录分页查询】功能入参 = " + JsonUtil.toJson(sendMessageRecordVO));
			Pagination pagination = this.multiShopBindingSendMessageRecordService.findSendMessageRecordList(uid,
					contextPath, sendMessageRecordVO);
			// 会员分组添加成功后，跳转的页面
			int pageNomax = 0;
			int count = pagination.getTotalCount();
			// 每页显示条数
			int pageSize = 5;
			if (count < pageSize) {
				pageNomax = 1;
			} else {
				if (count % pageSize == 0) {
					pageNomax = count / pageSize;
				} else {
					pageNomax = count / pageSize + 1;
				}
			}
			resultMap.put("pageNoMax", pageNomax);
			resultMap.put("pageNo", pageNo);
			resultMap.put("pagination", pagination);
			return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = new HashMap<String, Object>(2);
			logger.error("用户UID = " + uid + " 使用【多店铺绑定-列表页】出错 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 删除记录
	 * @Date 2019年3月26日下午5:45:18
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/deleteRecord")
	@ResponseBody
	public String deleteRecord(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws Exception {
		// 返回结果集
		Map<String, Object> resultMap = null;
		Long uid = null;
		try {
			resultMap = new HashMap<String, Object>(4);
			// 查询条件实体
			MultiShopBindingDeleteRecordVO deleteRecord = null;
			if (null != params && !"".equals(params)) {
				deleteRecord = JsonUtil.paramsJsonToObject(params, MultiShopBindingDeleteRecordVO.class);
			}
			// 从Redis中获取uid
			UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			logger.info("用户UID = " + uid + " 使用【多店铺绑定-删除记录】功能入参 = " + JsonUtil.toJson(deleteRecord));
			Long result = this.multiShopBindingService.deleteRecord(deleteRecord);
			if (result == 1L) {
				return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		} catch (Exception e) {
			e.printStackTrace();
			resultMap = new HashMap<String, Object>(2);
			logger.error("用户UID = " + uid + " 使用【多店铺绑定-删除记录】出错 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * @Description 后台设置,修改手机号,获取后台设置的验证:<br/>
	 *              后台设置获取验证码<br/>
	 *              如果用户此次发送验证码的次数超过30次 --不予再次发送<br/>
	 *              1:第一次保存时获取验证码<br/>
	 *              2:修改后台设置并修改设置中的手机号时获取验证码
	 * @param request
	 * @param response
	 * @param mobile
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月22日 下午5:30:58
	 */
	@ResponseBody
	@RequestMapping(value = "/securityCode")
	public String securityCode(HttpServletRequest request, HttpServletResponse response, @RequestBody String params) {
		// 查询条件实体
		MultiShopBindingApplyVO multiShopBindingVO = null;
		if (null != params && !"".equals(params)) {
			multiShopBindingVO = JsonUtil.paramsJsonToObject(params, MultiShopBindingApplyVO.class);
		}
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (null == user) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		Long uid = user.getId();
		UserInfo userInfo = this.userInfoService.findUserInfoByTaobaoUserNick(multiShopBindingVO.getFamilyShopName());
		if (userInfo == null) {
			return failureReusltMap(ApiResult.USER_IS_NOT_EXISTS).toJson();
		}
		// 不能自己绑定自己
		if (user.getTaobaoUserNick().equals(multiShopBindingVO.getFamilyShopName())) {
			return failureReusltMap(ApiResult.SAME_USER).toJson();
		}
		try {
			boolean ifExist = this.multiShopBindingService.checkIfExist(user.getTaobaoUserNick(),
					multiShopBindingVO.getFamilyShopName(), new Integer[] { 0, 1 });
			if (ifExist) {
				logger.info(user.getTaobaoUserNick() + " 和 " + multiShopBindingVO.getFamilyShopName() + " 已绑定, 不能重复绑定");
				return failureReusltMap(ApiResult.ALREADY_BINDING).toJson();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("用户UID = " + uid + " 使用【多店铺绑定-获取短信验证码】功能");
		MobileSetting setting = mobileSettingService.findMobileSetting(user.getId());
		if (setting == null) {
			return failureReusltMap(ApiResult.MOBILE_IS_NULL).toJson();
		}
		if (setting.getMobile() == null || "".equals(setting.getMobile().trim())
				|| !MobileRegEx.validateMobile(setting.getMobile())) {
			return failureReusltMap(ApiResult.MOBILE_WRONG).toJson();
		}
		int times = getCodeUseTimes(user.getId());
		if (times >= 30) {
			return failureReusltMap(ApiResult.SECURITY_CODE_USE_TOP).toJson();
		}
		String code = produceCode(), content = Constants.MESSAGE_VALIDATECODE_CONTNET;
		content = content.replace("CODE", code);
		logger.info("用户UID = " + uid + " 使用【多店铺绑定-获取短信验证码】功能, 本次获取验证码为 = " + code);
		// 是否根据成功失败作用户提示?
		boolean result = mobileSettingService.sendSecurityCodeMessage(content, setting.getMobile());
		if (result == false) {
			return failureReusltMap(ApiResult.SEND_SMS_FAILURE).toJson();
		}
		sessionProvider
				.putStrValueWithExpireTime(
						RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_KEY + this.SEPARATOR + this.BACK_SIGN
								+ this.SEPARATOR + user.getId() + this.SEPARATOR + setting.getMobile(),
						code, TimeUnit.MINUTES, 5l);
		// 放入使用次数
		putCodeUseTimes(user.getId(), ++times);
		return successReusltMap(ApiResult.SEND_SMS_SUCCESS).toJson();
	}

	/**
	 * @Description 后台设置,修改手机号,获取后台设置的验证:<br/>
	 *              后台设置获取验证码<br/>
	 *              如果用户此次发送验证码的次数超过30次 --不予再次发送<br/>
	 *              1:第一次保存时获取验证码<br/>
	 *              2:修改后台设置并修改设置中的手机号时获取验证码
	 * @param request
	 * @param response
	 * @param mobile
	 * @return String 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月22日 下午5:30:58
	 */
	@ResponseBody
	@RequestMapping(value = "/sendMessageSecurityCode")
	public String sendMessageSecurityCode(HttpServletRequest request, HttpServletResponse response) {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if (null == user) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		Long uid = user.getId();
		UserInfo userInfo = this.userInfoService.findUserInfo(uid);
		if (userInfo == null) {
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		logger.info("用户UID = " + uid + " 使用【多店铺绑定-获取短信验证码】功能");
		MobileSetting setting = mobileSettingService.findMobileSetting(user.getId());
		if (setting == null) {
			return failureReusltMap(ApiResult.MOBILE_IS_NULL).toJson();
		}
		if (setting.getMobile() == null || "".equals(setting.getMobile().trim())
				|| !MobileRegEx.validateMobile(setting.getMobile())) {
			return failureReusltMap(ApiResult.MOBILE_WRONG).toJson();
		}
		int times = getCodeUseTimes(user.getId());
		if (times >= 30) {
			return failureReusltMap(ApiResult.SECURITY_CODE_USE_TOP).toJson();
		}
		String code = produceCode(), content = Constants.MESSAGE_VALIDATECODE_CONTNET;
		content = content.replace("CODE", code);
		logger.info("用户UID = " + uid + " 使用【多店铺绑定-获取短信验证码】功能, 本次获取验证码为 = " + code);
		// 是否根据成功失败作用户提示?
		boolean result = mobileSettingService.sendSecurityCodeMessage(content, setting.getMobile());
		if (result == false) {
			return failureReusltMap(ApiResult.SEND_SMS_FAILURE).toJson();
		}
		sessionProvider
				.putStrValueWithExpireTime(
						RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_KEY + this.SEPARATOR + this.BACK_SIGN
								+ this.SEPARATOR + user.getId() + this.SEPARATOR + setting.getMobile(),
						code, TimeUnit.MINUTES, 5l);
		// 放入使用次数
		putCodeUseTimes(user.getId(), ++times);
		return successReusltMap(ApiResult.SEND_SMS_SUCCESS).toJson();
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
	 * @Description 后台管理验证用户操作
	 * @param userNick
	 * @param newNum
	 * @param oldNum
	 * @param code
	 * @return Map<String,Object> 返回类型
	 * @author jackstraw_yu
	 * @date 2018年1月22日 下午6:05:37
	 */
	private Map<String, Object> validateUserOperation(Long uid, String mobile, String code) throws Exception {
		if (mobile == null || "".equals(mobile)) {
			return resultMap(false, ApiResult.MOBILE_IS_NULL);// 手机号不能为空!
		} else if (!MobileRegEx.validateMobile(mobile)) {
			return resultMap(false, ApiResult.MOBILE_WRONG);// 手机号不正确!
		}

		if (code == null || "".equals(code)) {
			return resultMap(false, ApiResult.SECURITY_CODE_ERROR);// 验证码不能为空!
		} else if (!this.REGEX_CODE.matcher(code).matches()) {
			return resultMap(false, ApiResult.SECURITY_CODE_ERROR);// 验证码填写错误!
		}

		String cacheCode = sessionProvider.getStrValue(RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_KEY
				+ this.SEPARATOR + this.BACK_SIGN + this.SEPARATOR + uid + this.SEPARATOR + mobile);

		if (cacheCode == null || "".equals(cacheCode)) {
			return resultMap(false, ApiResult.SECURITY_CODE_EXPIRY);// 验证码已过期!
		} else if (!cacheCode.equals(code)) {
			return resultMap(false, ApiResult.MOBILE_OR_CODE_ERROR);// 手机号或者验证码错误!
		} else {
			sessionProvider.removeStrValueByKey(RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_KEY + this.SEPARATOR
					+ this.BACK_SIGN + this.SEPARATOR + uid + this.SEPARATOR + mobile);
			return resultMap(true, null);
		}
	}

	/**
	 * 返回结果集
	 * 
	 * @param status
	 *            状态值:true/false
	 * @param message
	 *            提示信息
	 * @return Map<String,Object> 返回类型
	 * @author jackstraw_yu
	 * @date 2017年11月15日 下午3:38:29
	 */
	private Map<String, Object> resultMap(Boolean status, String msgKey) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("status", status);
		map.put("msgKey", msgKey);
		return map;
	}

}
