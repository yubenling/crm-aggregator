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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.base.BaseController;
import com.kycrm.member.component.SessionProvider;
import com.kycrm.member.domain.entity.other.MobileSetting;
import com.kycrm.member.domain.entity.user.UserInfo;
import com.kycrm.member.service.other.IMobileSettingService;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.Constants;
import com.kycrm.util.DateUtils;
import com.kycrm.util.JsonUtil;
import com.kycrm.util.MobileRegEx;
import com.kycrm.util.RedisConstant;

@Controller
@RequestMapping("/backstage")
public class MobileSettingController extends BaseController{
	private static final Log logger = LogFactory.getLog(MobileSettingController.class);
	@Autowired
	private IMobileSettingService mobileSettingService;
	
	@Autowired
	private SessionProvider sessionProvider;
	/**
	 * 后台验证码标识
	 */
	private  final String BACK_SIGN = "BACK";
	/**
	 * 分隔符
	 */
	private  final String SEPARATOR= "-";
	
	/**
	 * 手机验证码正则表达式
	 * */
	private  final Pattern REGEX_CODE = Pattern.compile("([0-9]){6}");
	
	/** 
	* @Description 查询用户的后台管理设置,针对接口调用
	* @param @param request
	* @param @param response
	* @param @return    设定文件 
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月22日 下午5:24:37
	*/
	@ResponseBody
	@RequestMapping(value="/queryMobileSetting",method=RequestMethod.POST)
	public String queryMobileSetting(HttpServletRequest request,HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		MobileSetting ms =null;
		try {
			ms = mobileSettingService.findMobileSetting(user.getId());
		} catch (Exception e) {
			logger.error("******************手机号绑定*************** Exception:"+e.getMessage());
		}
		return successReusltMap(null).put(ApiResult.API_RESULT, ms).toJson();
	}
	
	
	/** 
	* @Description  后台设置,修改手机号,获取后台设置的验证:<br/>
	* 后台设置获取验证码<br/>
	* 如果用户此次发送验证码的次数超过30次 --不予再次发送<br/>
	* 1:第一次保存时获取验证码<br/>
	* 2:修改后台设置并修改设置中的手机号时获取验证码
	* @param request
	* @param response
	* @param mobile
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月22日 下午5:30:58
	*/
	@ResponseBody
	@RequestMapping(value="/securityCode")
	public String backstageSecurityCode(@RequestBody String params,
			HttpServletRequest request,HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		MobileSetting ms = new MobileSetting();
		if (null != params && !"".equals(params)) {
			try {
				ms = JsonUtil.paramsJsonToObject(params, MobileSetting.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		
		if (ms.getMobile() == null
				|| "".equals(ms.getMobile().trim())
				|| !MobileRegEx.validateMobile(ms.getMobile()))
			return failureReusltMap(ApiResult.MOBILE_WRONG).toJson();
		
		MobileSetting setting = mobileSettingService.findMobileSetting(user.getId());
		if(setting !=null && setting.getMobile().equals(ms.getMobile()))
			return failureReusltMap(ApiResult.MOBILE_NO_CHANGE).toJson();
		
		int times = getCodeUseTimes(user.getId());
		if(times>=30)
			return failureReusltMap(ApiResult.SECURITY_CODE_USE_TOP).toJson();
		
		String code = produceCode(),content = Constants.MESSAGE_VALIDATECODE_CONTNET;
		content = content.replace("CODE", code);
		
		//是否根据成功失败作用户提示?
		boolean result = mobileSettingService.sendSecurityCodeMessage(content,ms.getMobile());
		if(result == false)
			return failureReusltMap(ApiResult.SEND_SMS_FAILURE).toJson();
		
		sessionProvider.putStrValueWithExpireTime(
				 RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_KEY+this.SEPARATOR+this.BACK_SIGN+this.SEPARATOR+user.getId()+this.SEPARATOR+ms.getMobile(),
				 code, TimeUnit.MINUTES, 5l);
		
		//放入使用次数
		putCodeUseTimes(user.getId(),++times);
		
		return successReusltMap(ApiResult.SEND_SMS_SUCCESS).toJson();
	}
	
	
	/** 
	* @Description 保存后者或者更新后台设置
	* 1:保存用户后台设置<br/>
	* 1.1:保存后天设置,更新用户手机号,赠送短信标记<br/>
	* 1.2:赠送用户500条短信<br/>
	* 2:更新用户后台设置<br/>
	* @param  request
	* @param  response
	* @param  mobileSetting
	* @param  lastMobile
	* @param  code
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月22日 下午5:37:32
	*/
	@ResponseBody
	@RequestMapping(value="/saveMobileSetting",method=RequestMethod.POST)
	public String saveMobileSetting(@RequestBody String params,
			HttpServletRequest request,HttpServletResponse response){
		UserInfo user = sessionProvider.getDefaultAttribute(RequestUtil.getCSESSIONID(request, response));
		if(null == user)
			return failureReusltMap(ApiResult.USER_IS_NULL).toJson();
		
		MobileSetting ms = new MobileSetting();
		if (null != params && !"".equals(params)) {
			try {
				ms = JsonUtil.paramsJsonToObject(params, MobileSetting.class);
			} catch (Exception e) {
				e.printStackTrace();
				return exceptionReusltMap(ApiResult.OPERATE_EXCEPTION_TRY_AGAIN).toJson();
			}
		}
		ms.setUid(user.getId());
		
		//1取出之前使用手机号存储的验证码
		//2..如果oldPNum 不存在 说明走的第一次保存,直接判断验证码是否相等;
		//2..如果存在说明是修改,判断手机号是否被修改过,没有修改该过,直接保存
		//2...............................,修改过,验证验证码是否相等;
		Map<String, Object> map = validateUserOperation(user.getId(),ms.getMobile(),ms.getCode());
		if(!(Boolean)map.get("status"))
			return failureReusltMap((String)map.get("msgKey")).toJson();

		try {
			if(ms.getId()==null){
				ms.setCreatedBy(user.getTaobaoUserNick());
				ms.setLastModifiedBy(user.getTaobaoUserNick());
				ms.setUserId(user.getTaobaoUserNick());
				mobileSettingService.saveInitMobileSetting(ms,user,null);
			}else{
				mobileSettingService.updateMobileSetting(ms,user);
			}
		} catch (Exception e) {
			logger.error("******************手机号绑定*************** Exception:"+e.getMessage());
			return failureReusltMap(ApiResult.SAVE_SET_FAILURE).toJson();
		}
		
		sessionProvider.removeStrValueByKey(
				RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_KEY
				+this.SEPARATOR+this.BACK_SIGN+this.SEPARATOR+user.getId()+this.SEPARATOR+ms.getMobile());
		
		return successReusltMap(ApiResult.SAVE_SET_SUCCESS).toJson();
	}
	
	/** 
	* @Description: 获取用户一天之内使用发送验证码的次数<br/>
	* 如果用户此次发送验证码的次数超过30次 --不予再次发送
	* @param @param userNick
	* @return int    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月22日 上午11:43:09
	*/
	private int getCodeUseTimes(Long uid){
		 String num = sessionProvider.getStrValue(
				 		RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_TOP_KEY+this.SEPARATOR+uid);
		 int times = 0;
		 if(num!=null&&!"".equals(num))
			 try{
				times =  Integer.valueOf(num.trim());
			 }catch (Exception e) {
				times =0;
			 }
		 return times;
	}
	
	 /** 
	    * @Description	返回一个6位数的随机验证码<br/>
	    * 所有线程共享一个ThreadLocalRandom实例<br/> 
	    * @return String    返回类型 
	    * @author jackstraw_yu
	    * @date 2018年1月22日 下午12:03:38
	    */
	    private String produceCode(){
			String code = "";
			ThreadLocalRandom random = ThreadLocalRandom.current();
			for(int i =0;i<6;i++){
				code += random.nextInt(10);
			}
			return code;
		}
	    
	     
	     /** 
	 	* @Description: 放入用户一天之内使用发送验证码的次数<br/>
	 	* 如果用户此次发送验证码的次数超过30次 --不予再次发送
	 	* @param  userNick
	 	* @param  times 
	 	* @return void    返回类型 
	 	* @author jackstraw_yu
	 	* @date 2018年1月22日 上午11:44:09
	 	*/
	 	private void putCodeUseTimes(Long uid,int times){
	 		sessionProvider.putStrValueWithExpireTime(
	 				 RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_TOP_KEY+this.SEPARATOR+uid,
	 				 times+"",TimeUnit.MILLISECONDS,DateUtils.getMillisOverToday());
	 	}
	 	
	 	 /** 
	     * @Description 后台管理验证用户操作
	     * @param  userNick
	     * @param  newNum
	     * @param  oldNum
	     * @param  code
	     * @return Map<String,Object>    返回类型 
	     * @author jackstraw_yu
	     * @date 2018年1月22日 下午6:05:37
	     */
	     private Map<String,Object> validateUserOperation(Long uid,String mobile,String code){
		 		//1取出之前使用手机号存储的验证码
		 		//2..如果oldNum 不存在 说明走的第一次保存,直接判断验证码是否相等;
		 		//3..如果存在说明是修改,判断手机号是否被修改过,没有修改该过,直接保存
		 		//4...............................,修改过,验证验证码是否相等;
	    	 	MobileSetting ms = mobileSettingService.findMobileSetting(uid);
	    	 	if(ms==null || !ms.getMobile().equals(mobile)){
	    	 		if(mobile == null || "".equals(mobile)){
		 				return resultMap(false,ApiResult.MOBILE_IS_NULL);//手机号不能为空!
		 			}else if(!MobileRegEx.validateMobile(mobile)){
		 				return resultMap(false,ApiResult.MOBILE_WRONG);//手机号不正确!
		 			}
		 			
		 			if(code ==null || "".equals(code)){
		 				return resultMap(false,ApiResult.SECURITY_CODE_ERROR);//验证码不能为空!
		 			}else if( !this.REGEX_CODE.matcher(code).matches()){
		 				return resultMap(false,ApiResult.SECURITY_CODE_ERROR);//验证码填写错误!
		 			}
		 			
		 			String cacheCode = sessionProvider.getStrValue(
		 					RedisConstant.RediskeyCacheGroup.VALIDATE_CODE_KEY
		 					+this.SEPARATOR+this.BACK_SIGN+this.SEPARATOR+uid+this.SEPARATOR+mobile);
		 			
		 			if(cacheCode==null || "".equals(cacheCode)){
		 				return resultMap(false,ApiResult.SECURITY_CODE_EXPIRY);//验证码已过期!
		 			}else if(!cacheCode.equals(code)){
		 				return resultMap(false,ApiResult.MOBILE_OR_CODE_ERROR);//手机号或者验证码错误!
		 			}
	    	 	}
	     	return resultMap(true,null);
	 	}
	     
	     
	     /** 
	 	* 返回结果集
	 	* @param  status 状态值:true/false
	 	* @param  message 提示信息
	 	* @return Map<String,Object>    返回类型 
	 	* @author jackstraw_yu
	 	* @date 2017年11月15日 下午3:38:29
	 	*/
	 	private Map<String,Object> resultMap(Boolean status,String msgKey){
	 		Map<String,Object> map = new HashMap<String,Object>();
	 		map.put("status", status);
	 		map.put("msgKey", msgKey);
	 		return map;
	 	}
}
