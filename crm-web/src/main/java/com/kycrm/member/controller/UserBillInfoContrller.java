package com.kycrm.member.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.user.UserBillInfo;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.user.UserBillInfoVO;
import com.kycrm.member.service.user.IUserBillInfoService;
import com.kycrm.member.service.user.IUserPayBillService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.JsonUtil;


@Controller
@RequestMapping(value = "/backstage")
public class UserBillInfoContrller extends BaseController{
	
	private static final Logger logger=LoggerFactory.getLogger(UserBillInfoContrller.class);
	
	
	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private IUserBillInfoService userBillInfoService;
	
	@Autowired
	private IUserPayBillService userPayBillService;
	
	@ResponseBody
	@RequestMapping(value="/selcetBillInfo")
	public String selcetBillInfoList(@RequestBody String params,HttpServletRequest request,HttpServletResponse response){
	    UserInfo user=sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(user==null){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();	
		}
		//转换对象
		UserBillInfoVO ubv=new UserBillInfoVO();
		try {
			if(null!=params&&!"".equals(params)){
				ubv=JsonUtil.paramsJsonToObject(params, UserBillInfoVO.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//需要计算打款记录总和
		Map<String, Object> map=new HashMap<String, Object>();
		UserBillInfo userBillInfo =userBillInfoService.selcetBillInfo(user.getId());
		map.put("userBillInfo", userBillInfo);
		if(ubv!=null&&ubv.getComputerSum()!=null&&ubv.getComputerSum()>0){
		   BigDecimal computerSum=userPayBillService.selectAllPayBillAmont(user);
		   map.put("computerSum", computerSum);
		}
	    return successReusltMap(null).put(ApiResult.API_RESULT, map).toJson();	
	}
    /**
     * 
     * @param params  保存的发票信息
     * @param request
     * @param response
     * @return 是否保存成功
     */
	
	@ResponseBody
	@RequestMapping(value="/saveBillInfo")
	public String saveBillInfo(@RequestBody String params,
			HttpServletRequest request,HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null==user){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		UserBillInfoVO userBillInfoVO =new UserBillInfoVO();
		try {
			if(null!=params&&!"".equals(params)){
				userBillInfoVO=JsonUtil.paramsJsonToObject(params, UserBillInfoVO.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		Integer i=userBillInfoService.saveBillInfo(user,userBillInfoVO);
		if(i>0){
			return successReusltMap(ApiResult.SAVE_SUCCESS).toJson();
		}else{
			return failureReusltMap(ApiResult.SAVE_FAILURE).toJson();
		}
	}
	
	/**
	 * 修改发票信息
	 * @param params 要修改的发票信息
	 * @param request
	 * @param response
	 * @return 是否修改成功
	 */
	@ResponseBody
	@RequestMapping(value="/updateBillInfo")
	public String updateBillInfo(@RequestBody String params,
			HttpServletRequest request,HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null==user){
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		}
		UserBillInfoVO userBillInfoVO =new UserBillInfoVO();
		try {
			if(null!=params&&!"".equals(params)){
				userBillInfoVO=JsonUtil.paramsJsonToObject(params, UserBillInfoVO.class);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		//判断对象时候存在
		if(null==userBillInfoVO){
			return failureReusltMap(ApiResult.PARAM_LACK).toJson();
		}
		Integer updateCount=userBillInfoService.updateBillInfo(user,userBillInfoVO);
		if(updateCount>0){
			return successReusltMap(ApiResult.UPDATE_SUCCESS).toJson();
		}	
		return failureReusltMap(ApiResult.UPDATE_FAILURE).toJson();
	}

}
