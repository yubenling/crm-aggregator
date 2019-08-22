package com.kycrm.member.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.entity.user.UserOperationLog;
import com.kycrm.member.domain.entity.usermanagement.SellerGroup;
import com.kycrm.member.domain.entity.usermanagement.SellerGroupRule;
import com.kycrm.member.domain.utils.pagination.Pagination;
import com.kycrm.member.domain.vo.member.MemberFilterVO;
import com.kycrm.member.domain.vo.usermanagement.SellerGroupAndRuleInsertOrUpdateVO;
import com.kycrm.member.domain.vo.usermanagement.SellerGroupDeleteVO;
import com.kycrm.member.domain.vo.usermanagement.SellerGroupSearchVO;
import com.kycrm.member.domain.vo.usermanagement.SellerGroupUpdateSearchVO;
import com.kycrm.member.domain.vo.usermanagement.UpdateSellerGroupCountVO;
import com.kycrm.member.service.marketing.IMarketingMemberFilterService;
import com.kycrm.member.service.user.IUserOperationLogService;
import com.kycrm.member.service.usermanagement.ISellerGroupRuleService;
import com.kycrm.member.service.usermanagement.ISellerGroupService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.member.utils.UserInfoUtil;
import com.kycrm.member.utils.thread.AsynchronizeUpdateMemberCountRunnable;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.RegexConstant;
import com.taobao.api.SecretException;

/**
 * 客户管理 - 会员分组
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月20日下午12:05:20
 * @Tags
 */
@Controller
@RequestMapping("/memberGroup")
public class MemberGroupController extends BaseController {

	private static final Log logger = LogFactory.getLog(MemberGroupController.class);

	@Autowired
	private UserInfoUtil userInfoUtil;

	@Autowired
	private SessionProvider sessionProvider;

	// 用户分组服务
	@Autowired
	private ISellerGroupService sellerGroupService;

	// 用户分组规则服务
	@Autowired
	private ISellerGroupRuleService sellerGroupRuleService;

	// 会员筛选服务
	@Autowired
	private IMarketingMemberFilterService marketingMemberFilterService;

	// 用户操作日志服务
	@Autowired
	private IUserOperationLogService userOperationLogService;

	/**
	 *
	 * @Author ZhengXiaoChen
	 * @Description 添加会员分组
	 * @Date 2018年7月10日下午4:32:10
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @ReturnType String
	 */
	@RequestMapping(value = "/addMemberGroup", method = RequestMethod.POST)
	@ResponseBody
	public String addMemberGroup(HttpServletRequest request, HttpServletResponse response, @RequestBody String params) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		Long uid = null;
		Long groupId = null;
		Long ruleId = null;
		try {
			SellerGroupAndRuleInsertOrUpdateVO sellerGroupInsertVO = null;
			if (params != null && !"".equals(params)) {
				sellerGroupInsertVO = JsonUtil.paramsJsonToObject(params, SellerGroupAndRuleInsertOrUpdateVO.class);
				logger.info("创建会员分组接口入参：" + JsonUtil.toJson(sellerGroupInsertVO));
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			// 从Redis中获取uid
			uid = this.getUid(request, response);
			// 卖家编号
			String taoBaoUserNick = this.userInfoUtil.getTaoBaoUserNick(request, response);
			String groupName = sellerGroupInsertVO.getGroupName();
			if (groupName == null || "".equals(groupName)) {
				resultMap.put("success", false);
				resultMap.put("info", "会员分组名称为空!");
				logger.error("错误 : 会员分组名称为空!");
				return failureReusltMap("2057").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				// 查询是否存在该会员分组
				Integer result = this.sellerGroupService.existenceSellerGroupInfo(uid,
						sellerGroupInsertVO.getGroupName());
				if (result != null && result > 0) {
					resultMap.put("success", false);
					resultMap.put("info", "会员分组名称重复!");
					logger.error("错误 : 会员分组名称重复");
					return failureReusltMap("2056").put(ApiResult.API_RESULT, resultMap).toJson();
				}
			}
			// 组装会员分组对象
			SellerGroup sellerGroup = this.assembleSellerGroup(sellerGroupInsertVO, taoBaoUserNick, uid);
			logger.info("创建会员分组：" + JsonUtil.toJson(sellerGroup));
			// 会员分组ID
			groupId = this.sellerGroupService.addSellerGroup(sellerGroup);
			// -----记录用户添加会员分组日志----- //
			boolean statement = false;
			if (groupId != null) {
				statement = true;
			} else {
				statement = false;
			}
			this.userOperationLogService
					.insert(this.assembleUserOperationLog(uid, statement, "添加会员分组", "会员分组", "添加", request));
			// -----记录用户添加会员分组日志----- //
			// 异步更新会员数量
			MemberFilterVO memberFilterVO = this.assembleMemberFilterCondition(sellerGroupInsertVO, uid);
			Thread thread = new Thread(new AsynchronizeUpdateMemberCountRunnable(uid, groupId, memberFilterVO,
					sellerGroupService, marketingMemberFilterService));
			thread.start();
			// 组装会员分组规则对象
			SellerGroupRule sellerGroupRule = this.assembleSellerGroupRule(sellerGroupInsertVO, taoBaoUserNick, groupId,
					uid);
			logger.info("创建会员分组规则：" + JsonUtil.toJson(sellerGroupRule));
			// 添加分组规则条件，并反回主键id
			ruleId = sellerGroupRuleService.addSellerGroupRule(sellerGroupRule);
			// -----记录用户添加会员分组规则日志-----//
			if (ruleId != null) {
				statement = true;
			} else {
				statement = false;
				resultMap.put("success", false);
				resultMap.put("info", "添加会员分组规则失败!");
				logger.error("错误 : 添加会员分组规则!");
				Map<String, Object> paramMap = new HashMap<String, Object>(2);
				paramMap.put("uid", uid);
				paramMap.put("id", groupId);
				this.sellerGroupService.deleteSellerGroup(paramMap);
				return failureReusltMap("2055").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			this.userOperationLogService
					.insert(this.assembleUserOperationLog(uid, statement, "添加会员分组规则", "会员分组规则", "添加", request));
			// -----记录用户添加会员分组规则日志-----//
			sellerGroup.setId(groupId);
			sellerGroup.setRuleId(ruleId);
			sellerGroup.setMemberCount(null);
			int operateResult = sellerGroupService.updateSellerGroup(sellerGroup);
			// 添加修改分组规则操作日志
			if (operateResult == 1) {
				resultMap.put("success", true);
				resultMap.put("info", "添加数据成功!");
				logger.error("结果 : 添加数据成功");
				return successReusltMap("1000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				resultMap.put("success", false);
				resultMap.put("info", "添加数据失败!");
				logger.error("错误 : 添加数据失败");
				return failureReusltMap("2000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		} catch (Exception e) {
			logger.error("创建分组发送错误 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (groupId != null) {
				Map<String, Object> paramMap = new HashMap<String, Object>(2);
				paramMap.put("uid", uid);
				paramMap.put("id", groupId);
				try {
					this.sellerGroupService.deleteSellerGroup(paramMap);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2001").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 *
	 * @Author ZhengXiaoChen
	 * @Description 修改会员分组及分组规则
	 * @Date 2018年7月10日下午4:31:48
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @ReturnType String
	 */
	@RequestMapping(value = "/updateMemberGroup", method = RequestMethod.POST, produces = { "text/html;charset=UTF-8;",
			"application/json;" })
	@ResponseBody
	public String updateMemberGroup(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		try {
			SellerGroupAndRuleInsertOrUpdateVO sellerGroupUpdateVO = null;
			logger.info("修改会员分组及分组规则入参：" + params);
			if (params != null && !"".equals(params)) {
				sellerGroupUpdateVO = JsonUtil.paramsJsonToObject(params, SellerGroupAndRuleInsertOrUpdateVO.class);
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			// 从Redis中获取uid
			Long uid = this.getUid(request, response);
			// 卖家编号
			String taoBaoUserNick = this.userInfoUtil.getTaoBaoUserNick(request, response);
			// 分组ID
			Long groupId = sellerGroupUpdateVO.getGroupId();
			// 分组名称
			String groupName = sellerGroupUpdateVO.getGroupName();
			// 组装分组规则对象
			SellerGroupRule sellerGroupRule = this.assembleSellerGroupRule(sellerGroupUpdateVO, taoBaoUserNick, groupId,
					uid);
			logger.info("修改会员分组规则：" + JsonUtil.toJson(sellerGroupRule));
			if (groupName == null || "".equals(groupName)) {
				resultMap.put("success", false);
				resultMap.put("info", "会员分组名称为空");
				return failureReusltMap("2057").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				// 查询是否存在该会员分组
				Integer result = this.sellerGroupService.existenceSellerGroupForUpdate(uid,
						sellerGroupUpdateVO.getGroupId(), sellerGroupUpdateVO.getGroupName());
				if (result != null && result > 0) {
					resultMap.put("success", false);
					resultMap.put("info", "会员分组名称重复!");
					logger.error("错误 : 会员分组名称重复");
					return failureReusltMap("2056").put(ApiResult.API_RESULT, resultMap).toJson();
				}
			}
			// 修改用户分组规则
			int result = this.sellerGroupRuleService.updateSellerGroupRule(sellerGroupRule);
			boolean statement = false;
			if (result > 0) {
				statement = true;
			} else {
				statement = false;
			}
			// 组装用户操作日志对象
			UserOperationLog userOperationLog = this.assembleUserOperationLog(uid, statement, "修改会员分组规则", "会员分组规则",
					"修改", request);
			this.userOperationLogService.insert(userOperationLog);
			SellerGroup sellerGroup = new SellerGroup();
			sellerGroup.setId(groupId);
			sellerGroup.setMemberCount("更新中");
			sellerGroup.setGroupName(groupName);
			sellerGroup.setRemark(sellerGroupUpdateVO.getRemarks());
			int operateResult = this.sellerGroupService.updateSellerGroup(sellerGroup);
			// 异步更新会员数量
			MemberFilterVO memberFilterVO = this.assembleMemberFilterCondition(sellerGroupUpdateVO, uid);
			Thread thread = new Thread(new AsynchronizeUpdateMemberCountRunnable(uid, groupId, memberFilterVO,
					sellerGroupService, marketingMemberFilterService));
			thread.start();
			if (operateResult > 0) {
				resultMap.put("success", true);
				resultMap.put("info", "修改数据成功!");
				logger.error("结果 : 修改数据成功");
				return successReusltMap("1001").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				resultMap.put("success", true);
				resultMap.put("info", "修改数据失败!");
				logger.error("结果 : 修改数据失败");
				return failureReusltMap("2002").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		} catch (Exception e) {
			logger.error("修改会员分组及分组规则 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2003").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 *
	 * @Author ZhengXiaoChen
	 * @Description 更新会员分组的数量
	 * @Date 2018年7月10日下午4:31:34
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @ReturnType String
	 */
	@RequestMapping(value = "/updateMemberGroupCount", method = RequestMethod.POST)
	@ResponseBody
	public String updateMemberGroupCount(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		try {
			UpdateSellerGroupCountVO updateSellerGroupCountVO = null;
			if (params != null && !"".equals(params)) {
				updateSellerGroupCountVO = JsonUtil.paramsJsonToObject(params, UpdateSellerGroupCountVO.class);
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			// 从Redis中获取uid
			Long uid = this.getUid(request, response);
			// 分组编号
			Long groupId = updateSellerGroupCountVO.getGroupId();
			// 获取分组规则对象
			SellerGroupRule sellerGroupRule = this.sellerGroupRuleService.findSellerGroupRule(uid, groupId);
			if (sellerGroupRule != null) {
				SellerGroup sellerGroup = new SellerGroup();
				sellerGroup.setId(groupId);
				sellerGroup.setUserId(this.userInfoUtil.getTaoBaoUserNick(request, response));
				sellerGroup.setUid(uid);
				sellerGroup.setMemberCount("更新中");
				sellerGroup.setLastModifiedDate(new Date());
				int result = this.sellerGroupService.updateSellerGroup(sellerGroup);
				// 更新会员分组会员数量
				boolean statement = false;
				if (result > 0) {
					statement = true;
				} else {
					statement = false;
				}
				this.userOperationLogService
						.insert(this.assembleUserOperationLog(uid, statement, "更新会员分组数据", "会员分组", "更新会员数量", request));
				// 组装会员筛选条件
				MemberFilterVO memberFilterVO = this.assembleMemberFilterCondition(sellerGroupRule, uid);
				Thread thread = new Thread(new AsynchronizeUpdateMemberCountRunnable(uid, groupId, memberFilterVO,
						sellerGroupService, marketingMemberFilterService));
				thread.start();
				resultMap.put("memberCount", "更新中");
				resultMap.put("modifyTime", DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss"));
				return successReusltMap("1016").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				resultMap.put("success", false);
				resultMap.put("info", "未找到指定会员分组规则");
				return failureReusltMap("2058").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		} catch (Exception e) {
			logger.error("更新会员分组的数量 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2059").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 *
	 * @Author ZhengXiaoChen
	 * @Description 根据分组编号删除分组
	 * @Date 2018年7月10日下午4:32:20
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @ReturnType String
	 */
	@RequestMapping(value = "/deleteMemberGroup", method = RequestMethod.POST)
	@ResponseBody
	public String deleteMemberGroup(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		try {
			SellerGroupDeleteVO sellerGroupDeleteVO = null;
			if (params != null && !"".equals(params)) {
				sellerGroupDeleteVO = JsonUtil.paramsJsonToObject(params, SellerGroupDeleteVO.class);
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			// 从Redis中获取uid
			Long uid = this.getUid(request, response);
			Map<String, Object> paramMap = new HashMap<String, Object>(2);
			paramMap.put("uid", uid);
			paramMap.put("id", Long.valueOf(sellerGroupDeleteVO.getGroupId()));
			int deleteSellerGroupResult = sellerGroupService.deleteSellerGroup(paramMap);
			boolean statement = false;
			if (deleteSellerGroupResult == 1) {
				resultMap.put("success", true);
				resultMap.put("info", "删除数据成功!");
				statement = true;
			} else {
				statement = false;
			}
			// 记录用户操作日志
			userOperationLogService
					.insert(this.assembleUserOperationLog(uid, statement, "删除会员分组", "会员分组", "删除", request));
			paramMap.remove("id");
			paramMap.put("ruleId", Long.valueOf(sellerGroupDeleteVO.getRuleId()));
			int deleteSellerGroupRuleResult = sellerGroupRuleService.delSellerGroupRule(paramMap);
			if (deleteSellerGroupRuleResult == 1) {
				resultMap.put("success", true);
				resultMap.put("info", "删除数据成功!");
				statement = true;
			} else {
				statement = false;
			}
			// 记录用户操作日志
			userOperationLogService
					.insert(this.assembleUserOperationLog(uid, statement, "删除会员分组规则", "会员分组", "删除", request));
			return successReusltMap("1007").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			logger.error("更新会员分组的数量 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2040").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 跳转到会员分组页面,并查询卖家所有用户分组(分页)
	 * @Date 2018年7月10日下午4:32:30
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @ReturnType String
	 */
	@RequestMapping(value = "/findMemberGroup", method = RequestMethod.POST)
	@ResponseBody
	public String findMemberGroup(HttpServletRequest request, HttpServletResponse response, @RequestBody String params)
			throws Exception {
		Map<String, Object> resultMap = new HashMap<String, Object>(4);
		try {
			SellerGroupSearchVO sellerGroupSearchVO = null;
			if (params != null && !"".equals(params)) {
				sellerGroupSearchVO = JsonUtil.paramsJsonToObject(params, SellerGroupSearchVO.class);
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			// 从Redis中获取uid
			Long uid = this.getUid(request, response);
			// 使用分页工具进行分页列表查询
			String contextPath = request.getContextPath();
			String groupType = sellerGroupSearchVO.getGroupType();
			Integer pageNo = sellerGroupSearchVO.getPageNo();
			if (pageNo == null) {
				pageNo = 1;
			}
			logger.info("会员分组列表 " + " 请求时间 = " + DateUtils.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss") + " uid = "
					+ uid + " groupType = " + groupType + " pageNo = " + pageNo);
			// 分页查询客户分组
			Pagination pagination = null;
			// 分页查询客户分组
			pagination = sellerGroupService.findAllSellerGroup(contextPath, pageNo, uid, groupType);
			resultMap.put("groupType", groupType);
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
			logger.error("跳转到会员分组页面,并查询卖家所有用户分组(分页) " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2006").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 *
	 * @Author ZhengXiaoChen
	 * @Description 根据分组编号查询分组信息
	 * @Date 2018年7月10日下午4:31:58
	 * @param groupId
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping(value = "/findGroupInfoByGroupId", method = RequestMethod.POST)
	@ResponseBody
	public String findGroupInfoByGroupId(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		Map<String, Object> resultMap = null;
		try {
			resultMap = new HashMap<String, Object>(41);
			SellerGroupUpdateSearchVO sellerGroupUpdateSearchVO = null;
			if (params != null && !"".equals(sellerGroupUpdateSearchVO)) {
				sellerGroupUpdateSearchVO = JsonUtil.paramsJsonToObject(params, SellerGroupUpdateSearchVO.class);
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			// 从Redis中获取uid
			Long uid = this.getUid(request, response);
			SellerGroup sellerGroup = sellerGroupService.findSellerGroup(uid, sellerGroupUpdateSearchVO.getGroupId());
			List<SellerGroup> sellerGroupList = sellerGroupService.findSellerGroupListByUid(uid);
			SellerGroupRule sellerGroupRule = null;
			if (sellerGroup != null && sellerGroup.getId() != null) {
				sellerGroupRule = sellerGroupRuleService.findSellerGroupRule(uid, sellerGroup.getId());
			}
			// 指定UID下的所有分组列表【1】
			resultMap.put("sellerGroupList", sellerGroupList);
			// 分组ID【2】
			resultMap.put("groupId", sellerGroup.getId() == null ? null : sellerGroup.getId());
			// 分组规则ID【3】
			resultMap.put("ruleId", sellerGroup.getRuleId() == null ? null : sellerGroup.getRuleId());
			// 分组名称【4】
			resultMap.put("groupName", sellerGroup.getGroupName() == null ? null : sellerGroup.getGroupName());
			// 会员等级【5】
			resultMap.put("memberGrade",
					sellerGroupRule.getMemberGrade() == null ? null : sellerGroupRule.getMemberGrade());
			// 交易时间与未交易时间标识【6】
			resultMap.put("tradeOrUntradeTime",
					sellerGroupRule.getTradeOrUntradeTime() == null ? null : sellerGroupRule.getTradeOrUntradeTime());
			// 交易时间或未交易时间数【7】
			resultMap.put("tradeTime", sellerGroupRule.getTradeTime() == null ? null : sellerGroupRule.getTradeTime());
			// 最小交易时间或未交易时间【8】
			resultMap.put("minTradeTime",
					sellerGroupRule.getMinTradeTime() == null ? null : sellerGroupRule.getMinTradeTime());
			// 最大交易时间或未交易时间【9】
			resultMap.put("maxTradeTime",
					sellerGroupRule.getMaxTradeTime() == null ? null : sellerGroupRule.getMaxTradeTime());
			// 订单来源【10】
			resultMap.put("orderFrom", sellerGroupRule.getOrderFrom() == null ? null : sellerGroupRule.getOrderFrom());
			// 交易次数【11】
			resultMap.put("tradeNum", sellerGroupRule.getTradeNum() == null ? null : sellerGroupRule.getTradeNum());
			// 最小交易次数【12】
			resultMap.put("minTradeNum",
					sellerGroupRule.getMinTradeNum() == null ? null : sellerGroupRule.getMinTradeNum());
			// 最大交易次数【13】
			resultMap.put("maxTradeNum",
					sellerGroupRule.getMaxTradeNum() == null ? null : sellerGroupRule.getMaxTradeNum());
			// 订单状态【14】
			resultMap.put("orderStatus",
					sellerGroupRule.getOrderStatus() == null ? null : sellerGroupRule.getOrderStatus());
			// 交易关闭次数【15】
			resultMap.put("closeTradeTime",
					sellerGroupRule.getCloseTradeTime() == null ? null : sellerGroupRule.getCloseTradeTime());
			// 最小交易关闭次数【16】
			resultMap.put("minCloseTradeTime",
					sellerGroupRule.getMinCloseTradeTime() == null ? null : sellerGroupRule.getMinCloseTradeTime());
			// 最大交易关闭次数【17】
			resultMap.put("maxCloseTradeTime",
					sellerGroupRule.getMaxCloseTradeTime() == null ? null : sellerGroupRule.getMaxCloseTradeTime());
			// 累计消费金额【18】
			resultMap.put("accumulatedAmount",
					sellerGroupRule.getAccumulatedAmount() == null ? null : sellerGroupRule.getAccumulatedAmount());
			// 最小累计消费金额【19】
			resultMap.put("minAccumulatedAmount", sellerGroupRule.getMinAccumulatedAmount() == null ? null
					: sellerGroupRule.getMinAccumulatedAmount());
			// 最大累计消费金额【20】
			resultMap.put("maxAccumulatedAmount", sellerGroupRule.getMaxAccumulatedAmount() == null ? null
					: sellerGroupRule.getMaxAccumulatedAmount());
			// 指定商品编号【发送】与【不发送】标识【21】
			resultMap.put("sendOrNotSendForGoods", sellerGroupRule.getSendOrNotSendForGoods() == null ? null
					: sellerGroupRule.getSendOrNotSendForGoods());
			// 指定商品或者商品关键字标识 【22】
			resultMap.put("specifyGoodsOrKeyCodeGoods", sellerGroupRule.getSpecifyGoodsOrKeyCodeGoods());
			// 指定商品编号【23】
			resultMap.put("numIid", sellerGroupRule.getNumIid() == null ? null : sellerGroupRule.getNumIid());
			// 商品关键字【24】
			resultMap.put("goodsKeyCode", sellerGroupRule.getGoodsKeyCode());
			// 交易宝贝数量【25】
			resultMap.put("itemNum", sellerGroupRule.getItemNum() == null ? null : sellerGroupRule.getItemNum());
			// 最小交易宝贝数量【26】
			resultMap.put("minItemNum",
					sellerGroupRule.getMinItemNum() == null ? null : sellerGroupRule.getMinItemNum());
			// 最大交易宝贝数量【27】
			resultMap.put("maxItemNum",
					sellerGroupRule.getMaxItemNum() == null ? null : sellerGroupRule.getMaxItemNum());
			// 地区筛选【发送】与【不发送】标识【28】
			resultMap.put("sendOrNotSendForArea", sellerGroupRule.getSendOrNotSendForArea() == null ? null
					: sellerGroupRule.getSendOrNotSendForArea());
			// 地区筛选 - 省份【29】
			resultMap.put("province", sellerGroupRule.getProvince() == null ? null : sellerGroupRule.getProvince());
			// 地区筛选 - 城市【30】
			resultMap.put("city", sellerGroupRule.getCity() == null ? null : sellerGroupRule.getCity());
			// 平均订单金额【31】
			resultMap.put("averagePrice",
					sellerGroupRule.getAveragePrice() == null ? null : sellerGroupRule.getAveragePrice());
			// 最小平均订单金额【32】
			resultMap.put("minAveragePrice",
					sellerGroupRule.getMinAveragePrice() == null ? null : sellerGroupRule.getMinAveragePrice());
			// 最大平均订单金额【33】
			resultMap.put("maxAveragePrice",
					sellerGroupRule.getMaxAveragePrice() == null ? null : sellerGroupRule.getMaxAveragePrice());
			// 拍下订单时段起始【34】
			resultMap.put("orderTimeSectionStart", sellerGroupRule.getOrderTimeSectionStart() == null ? null
					: sellerGroupRule.getOrderTimeSectionStart());
			// 拍下订单时段结束【35】
			resultMap.put("orderTimeSectionEnd",
					sellerGroupRule.getOrderTimeSectionEnd() == null ? null : sellerGroupRule.getOrderTimeSectionEnd());
			// 参与短信营销活动次数【36】
			resultMap.put("marketingSmsNumber",
					sellerGroupRule.getMarketingSmsNumber() == null ? null : sellerGroupRule.getMarketingSmsNumber());
			// 最小参与短信营销活动次数【37】
			resultMap.put("minMarketingSmsNumber", sellerGroupRule.getMinMarketingSmsNumber() == null ? null
					: sellerGroupRule.getMinMarketingSmsNumber());
			// 最大参与短信营销活动次数【38】
			resultMap.put("maxMarketingSmsNumber", sellerGroupRule.getMaxMarketingSmsNumber() == null ? null
					: sellerGroupRule.getMaxMarketingSmsNumber());
			// 卖家标记【39】
			resultMap.put("sellerFlag",
					sellerGroupRule.getSellerFlag() == null ? null : sellerGroupRule.getSellerFlag());
			// 已发送过滤【40】
			resultMap.put("sentFilter",
					sellerGroupRule.getSentFilter() == null ? null : sellerGroupRule.getSentFilter());
			// 黑名单【41】
			resultMap.put("memberStatus",
					sellerGroupRule.getMemberStatus() == null ? null : sellerGroupRule.getMemberStatus());
			// 分组说明【42】
			resultMap.put("remark", sellerGroup.getRemark() == null ? null : sellerGroup.getRemark());
			return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			resultMap = new HashMap<String, Object>(2);
			logger.error("根据分组编号查询分组信息 " + e);
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
	 * @Description 根据用户UID查询所有分组
	 * @Date 2018年8月13日下午5:12:51
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping("/getAllSellerGroupListByUid")
	@ResponseBody
	public String getAllSellerGroupListByUid(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Map<String, Object> resultMap = null;
		try {
			resultMap = new HashMap<String, Object>(1);
			// 从Redis中获取uid
			Long uid = this.getUid(request, response);
			List<SellerGroup> sellerGroupList = sellerGroupService.findSellerGroupListByUid(uid);
			resultMap.put("sellerGroupList", sellerGroupList);
			return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			resultMap = new HashMap<String, Object>(2);
			logger.error("根据用户UID查询所有分组 " + e);
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
	 * @Description 组装会员筛选条件对象
	 * @Date 2018年7月20日下午3:26:58
	 * @return
	 * @throws Exception
	 * @ReturnType MarketingMemberFilterVO
	 */
	private <T> MemberFilterVO assembleMemberFilterCondition(T t, Long uid) throws Exception {
		MemberFilterVO memberFilterVO = new MemberFilterVO();
		boolean isSellerGroupAndRuleInsertOrUpdate = false;
		SellerGroupRule sellerGroupRule = null;
		SellerGroupAndRuleInsertOrUpdateVO sellerGroupInsertVO = null;
		if (t instanceof SellerGroupAndRuleInsertOrUpdateVO) {
			isSellerGroupAndRuleInsertOrUpdate = true;
			sellerGroupInsertVO = (SellerGroupAndRuleInsertOrUpdateVO) t;
		} else {
			sellerGroupRule = (SellerGroupRule) t;
		}
		memberFilterVO.setUid(uid);

		// 订单来源
		memberFilterVO.setOrderFrom(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupInsertVO.getOrderFrom()
				: sellerGroupRule.getOrderFrom());

		// 交易时间与未交易时间标识
		memberFilterVO.setTradeOrUntradeTime(isSellerGroupAndRuleInsertOrUpdate
				? sellerGroupInsertVO.getTradeOrUntradeTime() : sellerGroupRule.getTradeOrUntradeTime());

		// 最近交易时间或未交易时间数
		memberFilterVO.setTradeTime(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupInsertVO.getTradeTime()
				: sellerGroupRule.getTradeTime());

		// 最小交易时间或未交易时间
		memberFilterVO.setMinTradeTime(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupInsertVO.getMinTradeTime()
				: sellerGroupRule.getMinTradeTime());

		// 最大交易时间或未交易时间
		memberFilterVO.setMaxTradeTime(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupInsertVO.getMaxTradeTime()
				: sellerGroupRule.getMaxTradeTime());

		// 订单状态
		memberFilterVO.setOrderStatus(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupInsertVO.getOrderStatus()
				: sellerGroupRule.getOrderStatus());

		// 交易成功次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String tradeNum = sellerGroupInsertVO.getTradeNum();
			if (tradeNum != null && !"".equals(tradeNum) && tradeNum.matches(RegexConstant.INTEGER)) {
				memberFilterVO.setTradeNum(Long.valueOf(tradeNum));
			}
		} else {
			memberFilterVO.setTradeNum(sellerGroupRule.getTradeNum());
		}

		// 最小交易成功次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String minTradeNum = sellerGroupInsertVO.getMinTradeNum();
			if (minTradeNum != null && !"".equals(minTradeNum)) {
				if (minTradeNum.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMinTradeNum(Long.valueOf(minTradeNum));
				} else {
					throw new Exception("错误 : 最小交易成功次数只能为整数");
				}
			}
		} else {
			memberFilterVO.setMinTradeNum(sellerGroupRule.getMinTradeNum());
		}

		// 最大交易成功次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String maxTradeNum = sellerGroupInsertVO.getMaxTradeNum();
			if (maxTradeNum != null && !"".equals(maxTradeNum)) {
				if (maxTradeNum.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMaxTradeNum(Long.valueOf(maxTradeNum));
				} else {
					throw new Exception("错误 : 最大交易成功次数只能为整数");
				}
			}
		} else {
			memberFilterVO.setMaxTradeNum(sellerGroupRule.getMaxTradeNum());
		}

		// 关闭交易次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String closeTradeTime = sellerGroupInsertVO.getCloseTradeTime();
			if (closeTradeTime != null && !"".equals(closeTradeTime)) {
				memberFilterVO.setCloseTradeTime(Long.valueOf(closeTradeTime));
			}
		} else {
			memberFilterVO.setCloseTradeTime(sellerGroupRule.getCloseTradeTime());
		}

		// 最小关闭交易次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String minCloseTradeTime = sellerGroupInsertVO.getMinCloseTradeTime();
			if (minCloseTradeTime != null && !"".equals(minCloseTradeTime)) {
				if (minCloseTradeTime.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMinCloseTradeTime(Long.valueOf(minCloseTradeTime));
				} else {
					throw new Exception("错误 :  最小关闭交易次数只能是整数");
				}
			}
		} else {
			memberFilterVO.setMinCloseTradeTime(sellerGroupRule.getMinCloseTradeTime());
		}

		// 最大关闭交易次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String maxCloseTradeTime = sellerGroupInsertVO.getMaxCloseTradeTime();
			if (maxCloseTradeTime != null && !"".equals(maxCloseTradeTime)) {
				if (maxCloseTradeTime.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMaxCloseTradeTime(Long.valueOf(maxCloseTradeTime));
				} else {
					throw new Exception("错误 :  最大关闭交易次数只能是整数");
				}
			}
		} else {
			memberFilterVO.setMaxCloseTradeTime(sellerGroupRule.getMaxCloseTradeTime());
		}

		// 累计消费金额
		String accumulatedAmount = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			accumulatedAmount = sellerGroupInsertVO.getAccumulatedAmount();
		} else {
			accumulatedAmount = sellerGroupRule.getAccumulatedAmount();
		}
		if (accumulatedAmount != null && !"".equals(accumulatedAmount)) {
			memberFilterVO.setAccumulatedAmount(Double.valueOf(accumulatedAmount));
		}

		// 最小累计交易金额
		String minAccumulatedAmount = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			minAccumulatedAmount = sellerGroupInsertVO.getMinAccumulatedAmount();
		} else {
			minAccumulatedAmount = sellerGroupRule.getMinAccumulatedAmount();
		}
		if (minAccumulatedAmount != null && !"".equals(minAccumulatedAmount)) {
			if (minAccumulatedAmount.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
				memberFilterVO.setMinAccumulatedAmount(new Double(minAccumulatedAmount));
			} else {
				throw new Exception("错误 : 最小累计交易金额只能为整数或小数,若为小数,只能保留小数点后两位");
			}
		}

		// 最大累计消费金额
		String maxAccumulatedAmount = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			maxAccumulatedAmount = sellerGroupInsertVO.getMaxAccumulatedAmount();
		} else {
			maxAccumulatedAmount = sellerGroupRule.getMaxAccumulatedAmount();
		}
		if (maxAccumulatedAmount != null && !"".equals(maxAccumulatedAmount)) {
			if (maxAccumulatedAmount.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
				memberFilterVO.setMaxAccumulatedAmount(new Double(maxAccumulatedAmount));
			} else {
				throw new Exception("错误 : 最大累计交易金额只能为整数或小数,若为小数,只能保留小数点后两位");
			}
		}

		// 指定分组商品【发送】与【不发送】标识
		memberFilterVO.setSendOrNotSendForGoods(
				isSellerGroupAndRuleInsertOrUpdate ? Integer.valueOf(sellerGroupInsertVO.getSendOrNotSendForGoods())
						: sellerGroupRule.getSendOrNotSendForGoods());

		// 指定商品或者商品关键字标识
		String specifyGoodsOrKeyCodeGoods = isSellerGroupAndRuleInsertOrUpdate
				? sellerGroupInsertVO.getSpecifyGoodsOrKeyCodeGoods() : sellerGroupRule.getSpecifyGoodsOrKeyCodeGoods();
		memberFilterVO.setSpecifyGoodsOrKeyCodeGoods(specifyGoodsOrKeyCodeGoods);
		if (specifyGoodsOrKeyCodeGoods != null && !"".equals(specifyGoodsOrKeyCodeGoods)) {
			if ("1".equals(specifyGoodsOrKeyCodeGoods)) {
				// 指定分组商品
				memberFilterVO.setNumIid(isSellerGroupAndRuleInsertOrUpdate ? sellerGroupInsertVO.getNumIid()
						: sellerGroupRule.getNumIid());
			} else {
				// 商品关键字
				memberFilterVO.setGoodsKeyCode(isSellerGroupAndRuleInsertOrUpdate
						? sellerGroupInsertVO.getGoodsKeyCode() : sellerGroupRule.getGoodsKeyCode());
			}
		}

		// 交易宝贝件数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String itemNum = sellerGroupInsertVO.getItemNum();
			if (itemNum != null && !"".equals(itemNum)) {
				if (itemNum.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setItemNum(Long.valueOf(itemNum));
				} else {
					throw new Exception("错误 : 交易宝贝件数只能为整数");
				}
			}
		} else {
			memberFilterVO.setItemNum(sellerGroupRule.getItemNum());
		}

		// 最小交易宝贝件数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String minItemNum = sellerGroupInsertVO.getMinItemNum();
			if (minItemNum != null && !"".equals(minItemNum)) {
				if (minItemNum.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMinItemNum(Long.valueOf(minItemNum));
				} else {
					throw new Exception("错误 : 最小交易宝贝件数只能为整数");
				}
			}
		} else {
			memberFilterVO.setMinItemNum(sellerGroupRule.getMinItemNum());
		}

		// 最大交易宝贝件数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String maxItemNum = sellerGroupInsertVO.getMaxItemNum();
			if (maxItemNum != null && !"".equals(maxItemNum)) {
				if (maxItemNum.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMaxItemNum(Long.valueOf(maxItemNum));
				} else {
					throw new Exception("错误 : 最大交易宝贝件数只能为整数");
				}
			}
		} else {
			memberFilterVO.setMaxItemNum(sellerGroupRule.getMaxItemNum());
		}

		// 地区筛选【发送】与【不发送】标识
		memberFilterVO.setSendOrNotSendForArea(
				isSellerGroupAndRuleInsertOrUpdate ? Integer.valueOf(sellerGroupInsertVO.getSendOrNotSendForArea())
						: sellerGroupRule.getSendOrNotSendForArea());

		// 地区筛选【省份】
		memberFilterVO.setProvince(
				isSellerGroupAndRuleInsertOrUpdate ? sellerGroupInsertVO.getProvince() : sellerGroupRule.getProvince());

		// 地区筛选【城市】
		memberFilterVO.setCity(
				isSellerGroupAndRuleInsertOrUpdate ? sellerGroupInsertVO.getCity() : sellerGroupRule.getCity());

		// 平均订单金额
		String averagePrice = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			averagePrice = sellerGroupInsertVO.getAveragePrice();
		} else {
			averagePrice = sellerGroupRule.getAveragePrice();
		}
		if (averagePrice != null && !"".equals(averagePrice)) {
			memberFilterVO.setAveragePrice(new BigDecimal(averagePrice));
		}

		// 最小平均订单金额
		String minAveragePrice = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			minAveragePrice = sellerGroupInsertVO.getMinAveragePrice();
		} else {
			minAveragePrice = sellerGroupRule.getMinAveragePrice();
		}
		if (minAveragePrice != null && !"".equals(minAveragePrice)) {
			if (minAveragePrice.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
				memberFilterVO.setMinAveragePrice(new BigDecimal(minAveragePrice));
			} else {
				throw new Exception("错误 : 最小平均订单金额只能为整数或小数,若为小数,只能保留小数点后两位");
			}
		}

		// 最大平均订单金额
		String maxAveragePrice = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			maxAveragePrice = sellerGroupInsertVO.getMaxAveragePrice();
		} else {
			maxAveragePrice = sellerGroupRule.getMaxAveragePrice();
		}
		if (maxAveragePrice != null && !"".equals(maxAveragePrice)) {
			if (maxAveragePrice.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
				memberFilterVO.setMaxAveragePrice(new BigDecimal(maxAveragePrice));
			} else {
				throw new Exception("错误 : 最大平均订单金额只能为整数或小数,若为小数,只能保留小数点后两位");
			}
		}

		// 拍下订单起始时段
		String orderTimeSectionStart = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			orderTimeSectionStart = sellerGroupInsertVO.getOrderTimeSectionStart();
		} else {
			orderTimeSectionStart = sellerGroupRule.getOrderTimeSectionStart();
		}
		if (orderTimeSectionStart != null && !"".equals(orderTimeSectionStart)
				&& orderTimeSectionStart.matches(RegexConstant.TIME)) {
			memberFilterVO.setOrderTimeSectionStart(orderTimeSectionStart);
		}

		// 拍下订单截止时段
		String orderTimeSectionEnd = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			orderTimeSectionEnd = sellerGroupInsertVO.getOrderTimeSectionEnd();
		} else {
			orderTimeSectionEnd = sellerGroupRule.getOrderTimeSectionEnd();
		}
		if (orderTimeSectionEnd != null && !"".equals(orderTimeSectionEnd)
				&& orderTimeSectionEnd.matches(RegexConstant.TIME)) {
			memberFilterVO.setOrderTimeSectionEnd(orderTimeSectionEnd);
		}

		// 参与短信营销活动次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String marketingSmsNumber = sellerGroupInsertVO.getMarketingSmsNumber();
			if (marketingSmsNumber != null && !"".equals(marketingSmsNumber)) {
				memberFilterVO.setMarketingSmsNumber(Integer.valueOf(marketingSmsNumber));
			}
		} else {
			Integer marketingSmsNumber = sellerGroupRule.getMarketingSmsNumber();
			if (marketingSmsNumber != null) {
				memberFilterVO.setMarketingSmsNumber(marketingSmsNumber);
			}
		}

		// 最小参与短信营销活动次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String minMarketingSmsNumber = sellerGroupInsertVO.getMinMarketingSmsNumber();
			if (minMarketingSmsNumber != null && !"".equals(minMarketingSmsNumber)) {
				if (minMarketingSmsNumber.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMinMarketingSmsNumber(Integer.valueOf(minMarketingSmsNumber));
				} else {
					throw new Exception("错误 : 最小参与短信营销活动次数只能为整数");
				}
			}
		} else {
			Integer minMarketingSmsNumber = sellerGroupRule.getMinMarketingSmsNumber();
			if (minMarketingSmsNumber != null) {
				memberFilterVO.setMinMarketingSmsNumber(minMarketingSmsNumber);
			}
		}

		// 最大参与短信营销活动次数
		if (isSellerGroupAndRuleInsertOrUpdate) {
			String maxMarketingSmsNumber = sellerGroupInsertVO.getMaxMarketingSmsNumber();
			if (maxMarketingSmsNumber != null && !"".equals(maxMarketingSmsNumber)) {
				if (maxMarketingSmsNumber.matches(RegexConstant.INTEGER)) {
					memberFilterVO.setMaxMarketingSmsNumber(Integer.valueOf(maxMarketingSmsNumber));
				} else {
					throw new Exception("错误 : 最大参与短信营销活动次数只能为整数");
				}
			}
		} else {
			Integer maxMarketingSmsNumber = sellerGroupRule.getMaxMarketingSmsNumber();
			if (maxMarketingSmsNumber != null) {
				memberFilterVO.setMaxMarketingSmsNumber(maxMarketingSmsNumber);
			}
		}

		// 卖家标记
		String sellerFlag = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			sellerFlag = sellerGroupInsertVO.getSellerFlag();
		} else {
			sellerFlag = sellerGroupRule.getSellerFlag();
		}
		if (sellerFlag != null && !"".equals(sellerFlag)) {
			memberFilterVO.setSellerFlag(sellerFlag);
		}

		// 已发送过滤
		String sentFilter = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			sentFilter = sellerGroupInsertVO.getSentFilter();
		} else {
			sentFilter = sellerGroupRule.getSentFilter();
		}
		if (sentFilter != null && !"".equals(sentFilter)) {
			memberFilterVO.setSentFilter(sentFilter);
		}

		// 黑名单
		String memberStatus = null;
		if (isSellerGroupAndRuleInsertOrUpdate) {
			memberStatus = sellerGroupInsertVO.getMemberStatus();
		} else {
			memberStatus = sellerGroupRule.getMemberStatus();
		}
		if (memberStatus != null && !"".equals(memberStatus)) {
			memberFilterVO.setMemberStatus(memberStatus);
		}

		return memberFilterVO;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 组装会员分组对象
	 * @Date 2018年7月20日下午3:59:31
	 * @param sellerGroupInsertVO
	 * @param taoBaoUserNick
	 * @param uid
	 * @return
	 * @throws Exception
	 * @ReturnType SellerGroup
	 */
	private SellerGroup assembleSellerGroup(SellerGroupAndRuleInsertOrUpdateVO sellerGroupInsertVO,
			String taoBaoUserNick, Long uid) throws Exception {
		// 创建会员分组
		SellerGroup sellerGroup = new SellerGroup();
		sellerGroup.setUserId(taoBaoUserNick);
		String groupName = sellerGroupInsertVO.getGroupName();
		if (groupName == null || "".equals(groupName)) {
			throw new Exception("错误：分组名称不能为空");
		}
		sellerGroup.setGroupName(groupName);
		sellerGroup.setRemark(sellerGroupInsertVO.getRemarks());
		// 创建分组默认状态为1（1代表正常）
		sellerGroup.setStatus("1");
		sellerGroup.setMemberCount("更新中");
		// 创建分组默认分组类型为2（2代表用户添加分组）
		sellerGroup.setGroupType("2");
		sellerGroup.setGroupCreate(new Date());
		sellerGroup.setCreatedBy(taoBaoUserNick);
		sellerGroup.setCreatedDate(new Date());
		sellerGroup.setUid(uid);
		sellerGroup.setLastModifiedDate(new Date());
		return sellerGroup;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 组装会员分组规则对象
	 * @Date 2018年7月20日下午3:50:23
	 * @param sellerGroupInsertVO
	 * @return
	 * @throws Exception
	 * @ReturnType SellerGroupRule
	 */
	private SellerGroupRule assembleSellerGroupRule(SellerGroupAndRuleInsertOrUpdateVO sellerGroupInsertVO,
			String taoBaoUserNick, Long groupId, Long uid) throws Exception {
		// 将数分组规则据添加到对象里
		SellerGroupRule sellerGroupRule = new SellerGroupRule();
		// 分组ID
		if (groupId != null) {
			sellerGroupRule.setGroupId(groupId);
		} else {
			throw new Exception("错误 : 分组ID不能为空");
		}
		// 分组规则ID
		sellerGroupRule.setId(sellerGroupInsertVO.getRuleId());
		// 卖家主键ID
		sellerGroupRule.setUid(uid);
		// 会员等级
		if (sellerGroupInsertVO.getMemberGrade() == null || "".equals(sellerGroupInsertVO.getMemberGrade())) {
			sellerGroupRule.setMemberGrade(null);
		} else {
			sellerGroupRule.setMemberGrade(sellerGroupInsertVO.getMemberGrade());
		}
		// 交易时间与未交易时间标识
		String tradeOrUntradeTime = sellerGroupInsertVO.getTradeOrUntradeTime();
		if (tradeOrUntradeTime != null && !"".equals(tradeOrUntradeTime)) {
			sellerGroupRule.setTradeOrUntradeTime(tradeOrUntradeTime);
		} else {
			throw new Exception("错误: 交易时间或未交易时间标识为空");
		}
		// 交易或未交易时间
		String tradeTime = sellerGroupInsertVO.getTradeTime();
		if (tradeTime != null && !"".equals(tradeTime) && tradeTime.matches(RegexConstant.INTEGER)) {
			// 最近交易时间数（只能为正整数）
			sellerGroupRule.setTradeTime(tradeTime);
		} else {
			sellerGroupRule.setTradeTime(null);
			// 最小交易或未交易时间
			String minTradeTime = sellerGroupInsertVO.getMinTradeTime();
			if (minTradeTime != null && !"".equals(minTradeTime)) {
				sellerGroupRule.setMinTradeTime(minTradeTime);
			}
			// 最大交易或未交易时间
			String maxTradeTime = sellerGroupInsertVO.getMaxTradeTime();
			if (maxTradeTime != null && !"".equals(maxTradeTime)) {
				sellerGroupRule.setMaxTradeTime(maxTradeTime);
			}
		}
		// 订单来源
		String orderFrom = sellerGroupInsertVO.getOrderFrom();
		if (orderFrom != null) {
			if (!"".equals(orderFrom)) {
				sellerGroupRule.setOrderFrom(orderFrom);
			} else {
				sellerGroupRule.setOrderFrom(null);
			}
		}
		// 交易次数
		String tradeNum = sellerGroupInsertVO.getTradeNum();
		if (tradeNum != null && !"".equals(tradeNum) && tradeNum.matches(RegexConstant.INTEGER)) {
			sellerGroupRule.setTradeNum(Long.valueOf(tradeNum));
		} else {
			sellerGroupRule.setTradeNum(null);
			// 最小交易次数
			String minTradeNum = sellerGroupInsertVO.getMinTradeNum();
			// 最大交易次数
			String maxTradeNum = sellerGroupInsertVO.getMaxTradeNum();
			if ((minTradeNum != null && !"".equals(minTradeNum) && minTradeNum.matches(RegexConstant.INTEGER))
					&& (maxTradeNum != null && !"".equals(maxTradeNum) && maxTradeNum.matches(RegexConstant.INTEGER))) {
				Long min = Long.valueOf(minTradeNum);
				Long max = Long.valueOf(maxTradeNum);
				if (min > max) {
					throw new Exception("错误 : 最小交易次数不能大于最大交易次数");
				} else {
					sellerGroupRule.setMinTradeNum(Long.valueOf(minTradeNum));
					sellerGroupRule.setMaxTradeNum(Long.valueOf(maxTradeNum));
				}
			} else {
				if (minTradeNum != null && !"".equals(minTradeNum) && minTradeNum.matches(RegexConstant.INTEGER)) {
					sellerGroupRule.setMinTradeNum(Long.valueOf(minTradeNum));
				}
				if (maxTradeNum != null && !"".equals(maxTradeNum) && maxTradeNum.matches(RegexConstant.INTEGER)) {
					sellerGroupRule.setMaxTradeNum(Long.valueOf(maxTradeNum));
				}
			}
		}
		// 订单状态
		String orderStatus = sellerGroupInsertVO.getOrderStatus();
		if (orderStatus == null || "".equals(orderStatus)) {
			sellerGroupRule.setOrderStatus(null);
		} else {
			sellerGroupRule.setOrderStatus(orderStatus);
		}
		// 交易关闭次数
		String closeTradeTime = sellerGroupInsertVO.getCloseTradeTime();
		if (closeTradeTime != null && !"".equals(closeTradeTime) && closeTradeTime.matches(RegexConstant.INTEGER)) {
			sellerGroupRule.setCloseTradeTime(Long.valueOf(closeTradeTime));
		} else {
			sellerGroupRule.setCloseTradeTime(null);
			// 最小交易关闭次数
			String minCloseTradeTime = sellerGroupInsertVO.getMinCloseTradeTime();
			// 最大交易关闭次数
			String maxCloseTradeTime = sellerGroupInsertVO.getMaxCloseTradeTime();
			if ((minCloseTradeTime != null && !"".equals(minCloseTradeTime)
					&& minCloseTradeTime.matches(RegexConstant.INTEGER))
					&& (maxCloseTradeTime != null && !"".equals(maxCloseTradeTime)
							&& maxCloseTradeTime.matches(RegexConstant.INTEGER))) {
				Long min = Long.valueOf(minCloseTradeTime);
				Long max = Long.valueOf(maxCloseTradeTime);
				if (min.compareTo(max) == 1) {
					throw new Exception("错误 : 最小交易关闭次数不能大于最大交易关闭次数");
				} else {
					sellerGroupRule.setMinCloseTradeTime(Long.valueOf(minCloseTradeTime));
					sellerGroupRule.setMaxCloseTradeTime(Long.valueOf(maxCloseTradeTime));
				}
			} else {
				if (minCloseTradeTime != null && !"".equals(minCloseTradeTime)
						&& minCloseTradeTime.matches(RegexConstant.INTEGER)) {
					sellerGroupRule.setMinCloseTradeTime(Long.valueOf(minCloseTradeTime));
				}
				if (maxCloseTradeTime != null && !"".equals(maxCloseTradeTime)
						&& maxCloseTradeTime.matches(RegexConstant.INTEGER)) {
					sellerGroupRule.setMaxCloseTradeTime(Long.valueOf(maxCloseTradeTime));
				}
			}
		}
		// 累计消费金额
		String accumulatedAmount = sellerGroupInsertVO.getAccumulatedAmount();
		if (accumulatedAmount != null && !"".equals(accumulatedAmount)) {
			if ("1".equals(accumulatedAmount)) {
				sellerGroupRule.setAccumulatedAmount("1");
			} else if ("2".equals(accumulatedAmount)) {
				sellerGroupRule.setAccumulatedAmount("2");
			} else if ("3".equals(accumulatedAmount)) {
				sellerGroupRule.setAccumulatedAmount("3");
			} else {
				sellerGroupRule.setAccumulatedAmount("4");
			}
		} else {
			sellerGroupRule.setAccumulatedAmount(null);
			// 最小累计消费金额
			String minAccumulatedAmount = sellerGroupInsertVO.getMinAccumulatedAmount();
			// 最大累计消费金额
			String maxAccumulatedAmount = sellerGroupInsertVO.getMaxAccumulatedAmount();
			if ((minAccumulatedAmount != null && !"".equals(minAccumulatedAmount)
					&& minAccumulatedAmount.matches(RegexConstant.INTEGER_OR_DECIMAL))
					&& (maxAccumulatedAmount != null && !"".equals(maxAccumulatedAmount)
							&& maxAccumulatedAmount.matches(RegexConstant.INTEGER_OR_DECIMAL))) {
				Double min = Double.valueOf(minAccumulatedAmount);
				Double max = Double.valueOf(maxAccumulatedAmount);
				if (min.compareTo(max) == 1) {
					throw new Exception("最小累计消费金额不能大于最大累计消费金额");
				} else {
					sellerGroupRule.setMinAccumulatedAmount(minAccumulatedAmount);
					sellerGroupRule.setMaxAccumulatedAmount(maxAccumulatedAmount);
				}
			} else {
				if (minAccumulatedAmount != null && !"".equals(minAccumulatedAmount)
						&& minAccumulatedAmount.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
					sellerGroupRule.setMinAccumulatedAmount(minAccumulatedAmount);
				}
				if (maxAccumulatedAmount != null && !"".equals(maxAccumulatedAmount)
						&& maxAccumulatedAmount.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
					sellerGroupRule.setMaxAccumulatedAmount(maxAccumulatedAmount);
				}
			}
		}
		// 指定分组商品【发送】与【不发送】标识
		String sendOrNotSendForGoods = sellerGroupInsertVO.getSendOrNotSendForGoods();
		if (sendOrNotSendForGoods != null && !"".equals(sendOrNotSendForGoods)) {
			sellerGroupRule.setSendOrNotSendForGoods(Integer.valueOf(sellerGroupInsertVO.getSendOrNotSendForGoods()));
		} else {
			throw new Exception("错误: 指定商品【发送】与【不发送】标识为空");
		}
		// 选择指定商品或者选择关键字商品标识
		String specifyGoodsOrKeyCodeGoods = sellerGroupInsertVO.getSpecifyGoodsOrKeyCodeGoods();
		if (specifyGoodsOrKeyCodeGoods != null && !"".equals(specifyGoodsOrKeyCodeGoods)) {
			sellerGroupRule.setSpecifyGoodsOrKeyCodeGoods(specifyGoodsOrKeyCodeGoods);
			if ("1".equals(specifyGoodsOrKeyCodeGoods)) {
				// 商品编号
				sellerGroupRule.setNumIid(sellerGroupInsertVO.getNumIid());
			} else {
				// 商品关键字
				sellerGroupRule.setGoodsKeyCode(sellerGroupInsertVO.getGoodsKeyCode());
			}
		}
		// 交易宝贝件数
		String itemNum = sellerGroupInsertVO.getItemNum();
		if (itemNum != null && !"".equals(itemNum) && itemNum.matches(RegexConstant.INTEGER)) {
			sellerGroupRule.setItemNum(Long.valueOf(itemNum));
		} else {
			sellerGroupRule.setItemNum(null);
			// 最小交易宝贝件数
			String minItemNum = sellerGroupInsertVO.getMinItemNum();
			// 最大交易宝贝件数
			String maxItemNum = sellerGroupInsertVO.getMaxItemNum();
			if ((minItemNum != null && !"".equals(minItemNum) && minItemNum.matches(RegexConstant.INTEGER))
					&& (maxItemNum != null && !"".equals(maxItemNum) && maxItemNum.matches(RegexConstant.INTEGER))) {
				Long min = Long.valueOf(minItemNum);
				Long max = Long.valueOf(maxItemNum);
				if (min.compareTo(max) == 1) {
					throw new Exception("错误 : 最小交易宝贝件数不能大于最大交易宝贝件数");
				} else {
					sellerGroupRule.setMinItemNum(Long.valueOf(minItemNum));
					sellerGroupRule.setMaxItemNum(Long.valueOf(maxItemNum));
				}
			} else {
				if (minItemNum != null && !"".equals(minItemNum) && minItemNum.matches(RegexConstant.INTEGER)) {
					sellerGroupRule.setMinItemNum(Long.valueOf(minItemNum));
				}
				if (maxItemNum != null && !"".equals(maxItemNum) && maxItemNum.matches(RegexConstant.INTEGER)) {
					sellerGroupRule.setMaxItemNum(Long.valueOf(maxItemNum));
				}
			}
		}
		// 地区筛选【发送】与【不发送】标识
		String sendOrNotSendForArea = sellerGroupInsertVO.getSendOrNotSendForArea();
		if (sendOrNotSendForArea != null && !"".equals(sendOrNotSendForArea)) {
			sellerGroupRule.setSendOrNotSendForArea(Integer.valueOf(sellerGroupInsertVO.getSendOrNotSendForArea()));
		}
		// 省份
		sellerGroupRule.setProvince(sellerGroupInsertVO.getProvince());
		// 城市
		sellerGroupRule.setCity(sellerGroupInsertVO.getCity());
		// 平均客单价
		String averagePrice = sellerGroupInsertVO.getAveragePrice();
		if (averagePrice != null && !"".equals(averagePrice)) {
			sellerGroupRule.setAveragePrice(averagePrice);
		} else {
			sellerGroupRule.setAveragePrice(null);
			// 最小平均客单价
			String minAveragePrice = sellerGroupInsertVO.getMinAveragePrice();
			// 最大平均客单价
			String maxAveragePrice = sellerGroupInsertVO.getMaxAveragePrice();
			if ((minAveragePrice != null && !"".equals(minAveragePrice)
					&& minAveragePrice.matches(RegexConstant.INTEGER_OR_DECIMAL))
					&& (maxAveragePrice != null && !"".equals(maxAveragePrice)
							&& maxAveragePrice.matches(RegexConstant.INTEGER_OR_DECIMAL))) {
				Double min = Double.valueOf(minAveragePrice);
				Double max = Double.valueOf(maxAveragePrice);
				if (min.compareTo(max) == 1) {
					throw new Exception("错误:  最小平均客单价不能最大平均客单价");
				} else {
					sellerGroupRule.setMinAveragePrice(minAveragePrice);
					sellerGroupRule.setMaxAveragePrice(maxAveragePrice);
				}
			} else {
				if (minAveragePrice != null && !"".equals(minAveragePrice)
						&& minAveragePrice.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
					sellerGroupRule.setMinAveragePrice(minAveragePrice);
				}
				if (maxAveragePrice != null && !"".equals(maxAveragePrice)
						&& maxAveragePrice.matches(RegexConstant.INTEGER_OR_DECIMAL)) {
					sellerGroupRule.setMaxAveragePrice(maxAveragePrice);
				}
			}
		}
		// 拍下订单时间起始段
		String orderTimeSectionStart = sellerGroupInsertVO.getOrderTimeSectionStart();
		if (orderTimeSectionStart != null && !"".equals(orderTimeSectionStart)
				&& orderTimeSectionStart.matches(RegexConstant.TIME)) {
			sellerGroupRule.setOrderTimeSectionStart(orderTimeSectionStart);
		} else {
			sellerGroupRule.setOrderTimeSectionStart(null);
		}
		// 拍下订单时间结束段
		String orderTimeSectionEnd = sellerGroupInsertVO.getOrderTimeSectionEnd();
		if (orderTimeSectionEnd != null && !"".equals(orderTimeSectionEnd)
				&& orderTimeSectionEnd.matches(RegexConstant.TIME)) {
			sellerGroupRule.setOrderTimeSectionEnd(orderTimeSectionEnd);
		} else {
			sellerGroupRule.setOrderTimeSectionEnd(null);
		}
		// 参与短信营销活动次数
		String marketingSmsNumber = sellerGroupInsertVO.getMarketingSmsNumber();
		if (marketingSmsNumber != null && !"".equals(marketingSmsNumber)
				&& marketingSmsNumber.matches(RegexConstant.INTEGER)) {
			sellerGroupRule.setMarketingSmsNumber(Integer.valueOf(marketingSmsNumber));
		} else {
			sellerGroupRule.setMarketingSmsNumber(null);
			// 最小参与短信营销活动次数
			String minMarketingSmsNumber = sellerGroupInsertVO.getMinMarketingSmsNumber();
			// 最大参与短信营销活动次数
			String maxMarketingSmsNumber = sellerGroupInsertVO.getMaxMarketingSmsNumber();
			if ((minMarketingSmsNumber != null && !"".equals(minMarketingSmsNumber)
					&& minMarketingSmsNumber.matches(RegexConstant.INTEGER))
					&& (maxMarketingSmsNumber != null && !"".equals(maxMarketingSmsNumber)
							&& maxMarketingSmsNumber.matches(RegexConstant.INTEGER))) {
				Integer min = Integer.valueOf(minMarketingSmsNumber);
				Integer max = Integer.valueOf(maxMarketingSmsNumber);
				if (min.compareTo(max) == 1) {
					throw new Exception("错误 : 最小参与短信营销活动次数不能大于最大参与短信营销活动次数");
				} else {
					sellerGroupRule.setMinMarketingSmsNumber(min);
					sellerGroupRule.setMaxMarketingSmsNumber(max);
				}
			} else {
				if (minMarketingSmsNumber != null && !"".equals(minMarketingSmsNumber)
						&& minMarketingSmsNumber.matches(RegexConstant.INTEGER)) {
					sellerGroupRule.setMinMarketingSmsNumber(Integer.valueOf(minMarketingSmsNumber));
				}
				if (maxMarketingSmsNumber != null && !"".equals(maxMarketingSmsNumber)
						&& maxMarketingSmsNumber.matches(RegexConstant.INTEGER)) {
					sellerGroupRule.setMaxMarketingSmsNumber(Integer.valueOf(maxMarketingSmsNumber));
				}
			}
		}
		// 卖家标记
		String sellerFlag = sellerGroupInsertVO.getSellerFlag();
		if (sellerFlag == null || "".equals(sellerFlag)) {
			sellerGroupRule.setSellerFlag(null);
		} else {
			sellerGroupRule.setSellerFlag(sellerFlag);
		}
		// 已发送过滤
		String sentFilter = sellerGroupInsertVO.getSentFilter();
		if (sentFilter == null || "".equals(sentFilter)) {
			sellerGroupRule.setSentFilter(null);
		} else {
			sellerGroupRule.setSentFilter(sentFilter);
		}
		// 黑名单
		String memberStatus = sellerGroupInsertVO.getMemberStatus();
		if (memberStatus != null && !"".equals(memberStatus)) {
			sellerGroupRule.setMemberStatus(memberStatus);
		}
		// 备注
		sellerGroupRule.setRemarks(sellerGroupInsertVO.getRemarks());
		// 卖家昵称
		sellerGroupRule.setUserId(taoBaoUserNick);
		sellerGroupRule.setCreatedDate(new Date());
		sellerGroupRule.setRegisterDate(new Date());
		return sellerGroupRule;
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 组装用户操作日志对象
	 * @Date 2018年7月20日下午12:13:52
	 * @return
	 * @throws Exception
	 * @ReturnType UserOperationLog
	 */
	private UserOperationLog assembleUserOperationLog(Long uid, boolean statement, String remark, String functions,
			String type, HttpServletRequest request) throws Exception {
		UserOperationLog userOperationLog = new UserOperationLog();
		userOperationLog.setUid(uid);
		// 标记状态
		userOperationLog.setState(statement ? "成功" : "失败");
		// 备注
		userOperationLog.setRemark(remark);
		// 功能分类
		userOperationLog.setFunctions(functions);
		// 操作类型
		userOperationLog.setType(type);
		// 操作日期
		userOperationLog.setDate(new Date());
		// 用户IP地址
		userOperationLog.setIpAdd(this.getIPAddress(request));
		return userOperationLog;
	}

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
	private Long getUid(HttpServletRequest request, HttpServletResponse response) throws Exception {
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		Long uid = null;
		if (user != null && user.getId() != null) {
			uid = user.getId();
		} else {
			throw new Exception("卖家UID为空");
		}
		return uid;
	}

	private String getIPAddress(HttpServletRequest request) {
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
		return ip;
	}

}
