package com.kycrm.member.controller;

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
import com.kycrm.member.domain.entity.member.MemberDetailDTO;
import com.kycrm.member.domain.entity.member.MemberReceiveDetail;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.utils.pagination.Pagination;
import com.kycrm.member.domain.vo.member.MemberInformationDetailUpdateVO;
import com.kycrm.member.domain.vo.member.MemberInformationDetailVO;
import com.kycrm.member.domain.vo.member.MemberInformationSearchVO;
import com.kycrm.member.domain.vo.member.MemberOrderVO;
import com.kycrm.member.domain.vo.member.MemberRemarkVO;
import com.kycrm.member.service.marketing.IMarketingMemberFilterService;
import com.kycrm.member.service.member.IMemberReceiveDetailService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.taobao.api.SecretException;

/**
 * 客户管理 - 会员信息
 * 
 * @Author ZhengXiaoChen
 * @Date 2018年7月21日上午11:49:32
 * @Tags
 */
@Controller
@RequestMapping("/memberInfo")
public class MemberInformationController extends BaseController {

	private static final Log logger = LogFactory.getLog(MemberInformationController.class);

	@Autowired
	private SessionProvider sessionProvider;

	// 会员筛选服务
	@Autowired
	private IMarketingMemberFilterService marketingMemberFilterService;

	// 会员收货地址服务
	@Autowired
	private IMemberReceiveDetailService memberReceiveDetailService;

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员信息主页面查询(解密)
	 * @Date 2018年7月21日下午2:58:49
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping(value = "/findMemberInfo", method = RequestMethod.POST)
	@ResponseBody
	public String findMember(HttpServletRequest request, HttpServletResponse response, @RequestBody String params) {
		Map<String, Object> resultMap = null;
		try {
			resultMap = new HashMap<String, Object>(3);
			MemberInformationSearchVO memberInfoSearchVO = null;
			if (params != null && !"".equals(params)) {
				memberInfoSearchVO = JsonUtil.paramsJsonToObject(params, MemberInformationSearchVO.class);
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			String contextPath = request.getContextPath();
			UserInfo user = this.sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Long uid = user.getId();
			String accessToken = user.getAccessToken();
			String taobaoUserNick = user.getTaobaoUserNick();
			memberInfoSearchVO.setUid(uid);
			logger.info("用户UID = " + uid + " 本次会员信息筛选条件 : " + memberInfoSearchVO.toString());
			Integer pageNo = memberInfoSearchVO.getPageNo();
			if (pageNo == null) {
				pageNo = 1;
			}
			Pagination pagination = this.marketingMemberFilterService.findMembersByConditionAndPagination(uid,
					taobaoUserNick, accessToken, memberInfoSearchVO, contextPath, pageNo);
			int pageNomax = 0;
			int count = pagination.getTotalCount();
			// 每页显示条数
			int currentRows = 10;
			if (count < currentRows) {
				pageNomax = 1;
			} else {
				if (count % currentRows == 0) {
					pageNomax = count / currentRows;
				} else {
					pageNomax = count / currentRows + 1;
				}
			}
			resultMap.put("pageNoMax", pageNomax);
			resultMap.put("pageNo", pageNo);
			resultMap.put("pagination", pagination);
			return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			logger.error("会员信息主页面查询(解密) " + e);
			resultMap = new HashMap<String, Object>(2);
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
	 * @Description 查询会员详情
	 * @Date 2018年7月25日下午2:54:28
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws SecretException
	 * @ReturnType String
	 */
	@RequestMapping(value = "/findMemberDetail", method = RequestMethod.POST)
	@ResponseBody
	public String findMemberDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		Map<String, Object> resultMap = null;
		try {
			resultMap = new HashMap<String, Object>(1);
			MemberInformationDetailVO memberInformationDetailVO = null;
			if (params != null && !"".equals(params)) {
				memberInformationDetailVO = JsonUtil.paramsJsonToObject(params, MemberInformationDetailVO.class);
			} else {
				return successReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			UserInfo user = this.sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Long uid = user.getId();
			memberInformationDetailVO.setUid(uid);
			logger.info("用户UID = " + uid + " 本次查询会员详情入参 " + memberInformationDetailVO.toString());
			MemberDetailDTO memberDetailDTO = this.marketingMemberFilterService.findMemberInfoDetail(uid,
					user.getTaobaoUserNick(), user.getAccessToken(), memberInformationDetailVO);
			if (memberDetailDTO.getLastTradeTime() != null) {
				memberDetailDTO.setUntradeTime(DateUtils.getDiffDay(memberDetailDTO.getLastTradeTime(), new Date()));
			}
			resultMap.put("memberInformationDetail", memberDetailDTO);
			return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			logger.error("查询会员详情 " + e);
			resultMap = new HashMap<String, Object>(2);
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
	 * @Description 会员详情 - 其他地址
	 * @Date 2018年7月28日上午11:26:21
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping(value = "/findMultiAddress", method = RequestMethod.POST)
	@ResponseBody
	public String findMultiAddress(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		Map<String, Object> resultMap = null;
		try {
			resultMap = new HashMap<String, Object>(1);
			MemberInformationDetailVO memberInformationDetailVO = null;
			if (params != null && !"".equals(params)) {
				memberInformationDetailVO = JsonUtil.paramsJsonToObject(params, MemberInformationDetailVO.class);
			} else {
				return successReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			UserInfo user = this.sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Long uid = user.getId();
			String accessToken = user.getAccessToken();
			String taobaoUserNick = user.getTaobaoUserNick();
			String buyerNick = memberInformationDetailVO.getBuyerNick();
			logger.info("会员详情 - 其他地址 入参 : uid = " + uid + " buyerNick = " + buyerNick);
			if (buyerNick != null && !"".equals(buyerNick)) {
				List<MemberReceiveDetail> memberMultiAddressList = this.memberReceiveDetailService
						.findMemberMultiAddress(uid, accessToken, taobaoUserNick, buyerNick);
				if (memberMultiAddressList.size() == 0) {
					memberMultiAddressList = null;
				}
				resultMap.put("memberMultiAddressList", memberMultiAddressList);
				return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				resultMap.put("message", "会员ID为空");
				return failureReusltMap("2060").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		} catch (Exception e) {
			logger.error("会员详情 - 其他地址 " + e);
			resultMap = new HashMap<String, Object>(2);
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
	 * @Description 更新会员信息
	 * @Date 2018年7月25日下午3:41:09
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping(value = "/updateMemberDetail", method = RequestMethod.POST)
	@ResponseBody
	public String updateMemberInfoDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		try {
			MemberInformationDetailUpdateVO memberInformationDetailUpdateVO = null;
			if (params != null && !"".equals(params)) {
				memberInformationDetailUpdateVO = JsonUtil.paramsJsonToObject(params,
						MemberInformationDetailUpdateVO.class);
			} else {
				return successReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			UserInfo user = this.sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Long uid = user.getId();
			memberInformationDetailUpdateVO.setUid(uid);
			logger.info("用户UID = " + uid + " 本次更新会员详情中的基本信息部分入参 " + memberInformationDetailUpdateVO.toString());
			Integer result = this.marketingMemberFilterService.updateMemberInformationDetail(uid,
					memberInformationDetailUpdateVO);
			if (result > 0) {
				resultMap.put("success", true);
				resultMap.put("info", "更新会员信息成功!");
				logger.error("结果 : 更新会员信息成功");
			} else {
				resultMap.put("success", false);
				resultMap.put("info", "更新会员信息成功失败!");
				logger.error("错误 : 会员详情 - 更新会员信息成功失败");
			}
			return successReusltMap("1014").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			logger.error("更新会员信息 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2058").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 会员详情 - 添加备注
	 * @Date 2018年7月25日下午4:40:57
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping(value = "/addRemark", method = RequestMethod.POST)
	@ResponseBody
	public String addRemark(HttpServletRequest request, HttpServletResponse response, @RequestBody String params) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		try {
			MemberRemarkVO memberRemarkVO = null;
			if (params != null && !"".equals(params)) {
				memberRemarkVO = JsonUtil.paramsJsonToObject(params, MemberRemarkVO.class);
			} else {
				return successReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			UserInfo user = this.sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Long uid = user.getId();
			memberRemarkVO.setUid(uid);
			Integer result = this.marketingMemberFilterService.addMemberRemark(uid, memberRemarkVO);
			if (result > 0) {
				resultMap.put("success", true);
				resultMap.put("info", "添加数据成功!");
				logger.error("结果 : 添加数据成功");
			} else {
				resultMap.put("success", false);
				resultMap.put("info", "添加数据失败!");
				logger.error("错误 : 会员详情 - 添加数据失败");
			}
			return successReusltMap("1000").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			logger.error("会员详情 - 添加备注 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
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
	 * @Description 会员详情 - 修改备注
	 * @Date 2018年7月25日下午4:40:57
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping(value = "/updateRemark", method = RequestMethod.POST)
	@ResponseBody
	public String updateRemark(HttpServletRequest request, HttpServletResponse response, @RequestBody String params) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		try {
			MemberRemarkVO memberRemarkVO = null;
			if (params != null && !"".equals(params)) {
				memberRemarkVO = JsonUtil.paramsJsonToObject(params, MemberRemarkVO.class);
			} else {
				return successReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			UserInfo user = this.sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Long uid = user.getId();
			memberRemarkVO.setUid(uid);
			Integer result = this.marketingMemberFilterService.addMemberRemark(uid, memberRemarkVO);
			if (result > 0) {
				resultMap.put("success", true);
				resultMap.put("info", "修改备注成功!");
				logger.error("结果 : 修改备注成功");
			} else {
				resultMap.put("success", false);
				resultMap.put("info", "修改备注失败!");
				logger.error("错误 : 会员详情 - 修改备注失败");
			}
			return successReusltMap("1001").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			logger.error("会员详情 - 修改备注 " + e);
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
	 * @Description 会员详情 - 删除备注
	 * @Date 2018年7月25日下午4:40:57
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping(value = "/deleteRemark", method = RequestMethod.POST)
	@ResponseBody
	public String deleteRemark(HttpServletRequest request, HttpServletResponse response, @RequestBody String params) {
		Map<String, Object> resultMap = new HashMap<String, Object>(2);
		try {
			MemberRemarkVO memberRemarkVO = null;
			if (params != null && !"".equals(params)) {
				memberRemarkVO = JsonUtil.paramsJsonToObject(params, MemberRemarkVO.class);
			} else {
				return successReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			UserInfo user = this.sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Long uid = user.getId();
			memberRemarkVO.setUid(uid);
			Integer result = this.marketingMemberFilterService.addMemberRemark(uid, memberRemarkVO);
			if (result > 0) {
				resultMap.put("success", true);
				resultMap.put("info", "删除备注成功!");
				logger.error("结果 : 删除备注成功");
			} else {
				resultMap.put("success", false);
				resultMap.put("info", "删除备注失败!");
				logger.error("错误 : 删除备注失败");
			}
			return successReusltMap("1007").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			logger.error("会员详情 - 删除备注 " + e);
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
	 * @Description 会员详情 - 订单信息
	 * @Date 2018年7月25日下午5:22:34
	 * @param request
	 * @param response
	 * @param params
	 * @return
	 * @throws Exception
	 * @ReturnType String
	 */
	@RequestMapping(value = "/findOrderDetail", method = RequestMethod.POST)
	@ResponseBody
	public String findOrderDetail(HttpServletRequest request, HttpServletResponse response,
			@RequestBody String params) {
		Map<String, Object> resultMap = null;
		try {
			resultMap = new HashMap<String, Object>(3);
			MemberOrderVO memberOrderVO = null;
			if (params != null && !"".equals(params)) {
				memberOrderVO = JsonUtil.paramsJsonToObject(params, MemberOrderVO.class);
			} else {
				return successReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			UserInfo user = this.sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
			if (user == null) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			}
			Long uid = user.getId();
			memberOrderVO.setUid(uid);
			String contextPath = request.getContextPath();
			String accessToken = user.getAccessToken();
			String taobaoUserNick = user.getTaobaoUserNick();
			logger.info("用户UID = " + uid + " 本次会员详情-->订单信息筛选条件 : " + memberOrderVO.toString());
			Integer pageNo = memberOrderVO.getPageNo();
			if (pageNo == null) {
				pageNo = 1;
			}
			Pagination pagination = this.marketingMemberFilterService.findMemberOrderByConditionAndPagination(uid,
					memberOrderVO, taobaoUserNick, accessToken, contextPath, pageNo);
			// 会员分组添加成功后，跳转的页面
			int pageNomax = 0;
			int count = pagination.getTotalCount();
			// 每页显示条数
			int currentRows = 10;
			if (count < currentRows) {
				pageNomax = 1;
			} else {
				if (count % currentRows == 0) {
					pageNomax = count / currentRows;
				} else {
					pageNomax = count / currentRows + 1;
				}
			}
			resultMap.put("pageNoMax", pageNomax);
			resultMap.put("pageNo", pageNo);
			resultMap.put("pagination", pagination);
			return successReusltMap("1011").put(ApiResult.API_RESULT, resultMap).toJson();
		} catch (Exception e) {
			resultMap = new HashMap<String, Object>(2);
			logger.error("会员详情 - 订单信息 " + e);
			resultMap.put("success", false);
			resultMap.put("info", e.getMessage());
			if (e instanceof SecretException) {
				return failureReusltMap("4000").put(ApiResult.API_RESULT, resultMap).toJson();
			} else {
				return failureReusltMap("2005").put(ApiResult.API_RESULT, resultMap).toJson();
			}
		}
	}

}
