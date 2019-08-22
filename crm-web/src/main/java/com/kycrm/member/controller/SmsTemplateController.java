package com.kycrm.member.controller;

import java.util.HashMap;
import java.util.HashSet;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.domain.PubChannelDTO;
import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.message.SmsTemplate;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.domain.vo.tradecenter.TradeSetupVO;
import com.kycrm.member.service.message.ISmsTemplateService;
import com.kycrm.member.service.user.IUserInfoService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.JsonUtil;
/**
 * 短信模板
 * @ClassName: SmsTemplateController  
 * @author ztk
 * @date 2018年6月20日 上午10:23:06
 */
@Controller
@RequestMapping(value="/template")
public class SmsTemplateController extends BaseController {
	
	private static final Logger logger=LoggerFactory.getLogger(SmsTemplateController.class);
	@Autowired
	private ISmsTemplateService templateService;
	
	@Autowired
	private SessionProvider sessionProvider;
	
	@Autowired
	private IUserInfoService userInfoService;

	/**
	 * saveTemplate(添加短语库)
	 * @Title: saveTemplate 
	 * @param @param request
	 * @param @param response
	 * @param @param params
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/saveSmsTemplate",method=RequestMethod.POST)
	@ResponseBody
	public String saveTemplate(HttpServletRequest request,HttpServletResponse response,@RequestBody String params){
		SmsTemplate smsTemplate = null;
		try {
			smsTemplate = parseJsonToVO(params);
		} catch (Exception e) {
			e.printStackTrace();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		//查询模板名称是否重复
		if(smsTemplate.getName()!=null && !"".equals(smsTemplate.getName())){
			boolean result = this.findTemName(smsTemplate);
			if(!result){
				logger.error("************用户"+userInfo.getTaobaoUserNick()+"另存为短语库失败,模板名称重复!***********");
				return  failureReusltMap(ApiResult.OPERATE_EXCEPTION).toJson();
			}
		}
		if(userInfo == null || userInfo.getId() == null || smsTemplate == null){
			return failureReusltMap(ApiResult.PARAM_LACK).toJson();
		}
		try {
			templateService.saveSingleTemplate(userInfo.getId(), smsTemplate);
		} catch (Exception e) {
			e.printStackTrace();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		return successReusltMap(ApiResult.SAVE_SUCCESS).toJson();
	}
	
	/**
	 * 另存为短语库，查询模板名称是否重复
	 * @param smsTem
	 * @return
	 */
	private boolean findTemName(SmsTemplate smsTem){
		boolean result = true;
		int count = templateService.findSmsTemName(smsTem);
		if(count!=0){
			result = false;
		}
		return result;
	}

	/**
	 * listTemplate(引用短语库)
	 * @Title: listTemplate 
	 * @param @param request
	 * @param @param response
	 * @param @param params
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/listTemplate",method=RequestMethod.POST)
	@ResponseBody
	public String listTemplate(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		SmsTemplate smsTemplate =null;
		try {
		    smsTemplate = parseJsonToVO(params);
		} catch (Exception e) {
			e.printStackTrace();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || userInfo.getId() == null || smsTemplate == null||smsTemplate.getCustomerType()==null){
			return failureReusltMap(ApiResult.PARAM_LACK).toJson();
		}
		Map<String,Object> map=new HashMap<String, Object>();
		List<SmsTemplate> templateList = templateService.listSmsTemplate(userInfo.getId(), smsTemplate.getType(), smsTemplate.getCustomerType(),smsTemplate.getSubType());
		map.put("templateList", templateList);
		if(smsTemplate.getCustomerType().equals("system")&&"33,34".contains(smsTemplate.getType())){
             if(smsTemplate.getSubType()==null||smsTemplate.getSubType().equals("")){
            	 List<SmsTemplate> sub_Type_List=templateService.findSubType(smsTemplate.getType());
            	 map.put("subTypeList", new HashSet<SmsTemplate>(sub_Type_List));	 
             }else{
            	 map.put("subTypeList", null);
             }
		}
		return successReusltMap(null).put(ApiResult.API_RESULT, map) .toJson();
	}
	
	/**
	 * deleteTemplate(删除模板)
	 * @Title: deleteTemplate 
	 * @param @param request
	 * @param @param response
	 * @param @param params
	 * @param @return 设定文件 
	 * @return String 返回类型 
	 * @throws
	 */
	@RequestMapping(value="/delSmsTemplate",method=RequestMethod.POST)
	@ResponseBody
	public String deleteTemplate(HttpServletRequest request,HttpServletResponse response,
			@RequestBody String params){
		SmsTemplate smsTemplate = null;
		try {
			smsTemplate =parseJsonToVO(params);
		} catch (Exception e) {
			e.printStackTrace();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
		}
		UserInfo userInfo = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(userInfo == null || userInfo.getId() == null || smsTemplate == null){
			return failureReusltMap(ApiResult.PARAM_LACK).toJson();
		}
		try {
			templateService.deleteTemplateById(smsTemplate.getId());
		} catch (Exception e) {
			e.printStackTrace();
			return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION).toJson();
		}
		return successReusltMap(ApiResult.DEL_SUCCESS).toJson();
	}
	/**
	 * 将接收的json参数转换为SmsTemplate 对象
	 * @param params
	 * @return
	 */
	private SmsTemplate parseJsonToVO(String params){
		SmsTemplate smsTemplate = null;
		if(params!=null){
			JSONObject parseObject = JSON.parseObject(params);
			String param = parseObject.getString("params");
			if(param==null || "".equals(param)) return smsTemplate;
			smsTemplate = JsonUtil.fromJson(param,SmsTemplate.class);
		}
		return smsTemplate;
	}
}
