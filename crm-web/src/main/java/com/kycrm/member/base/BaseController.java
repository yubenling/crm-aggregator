package com.kycrm.member.base;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.kycrm.member.api.enetity.ApiResult;
import com.kycrm.member.utils.RequestUtil;
import com.kycrm.util.JsonUtil;

/**   
 * @Title: BaseController.java
 * @Description: controller 类 基类
 * @author zlp
 * @date 2017年5月27日 上午9:50:17
 * @version V1.0
 */
public class BaseController {
	
	
	/** 返回结果Map */
	protected ResultMap<String, Object> rsMap(int rc, String msg){
		ResultMap<String, Object> rs = new ResultMap<String, Object>();
		return rs.put(ApiResult.API_RESULT_CODE, rc).put(ApiResult.API_RESULT_MSG, msg);
	}
	
	
	/** 
	* @Description 重载一个方法,方便自定义的提示语句使用
	* @param  code
	* @param  status
	* @param  message
	* @return ResultMap<String,Object>    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月22日 下午6:22:25
	*/
	protected ResultMap<String, Object> rsMap(Integer code,Boolean status,String msgKey){
		ResultMap<String, Object> rs = new ResultMap<String, Object>();
		rs.put(ApiResult.API_RESULT_CODE, code)
		  .put(ApiResult.API_RESULT_STATUS, status);
		if(msgKey!=null && !"".equals(msgKey))
			rs.put(ApiResult.API_RESULT_MSG, ApiResult.obtainResultMsg(msgKey));
		return rs;
	}
	
	
	/** 
	* @Description 成功时返回结果
	* @param  msgKey
	* @return ResultMap<String,Object>    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月18日 下午5:42:13
	*/
	protected ResultMap<String, Object> successReusltMap(String msgKey){
		ResultMap<String, Object> rs = new ResultMap<String, Object>();
		return rs.put(ApiResult.API_RESULT_CODE, ApiResult.Success.getCode())
				 .put(ApiResult.API_RESULT_STATUS,  ApiResult.Success.getStatus())
				 .put(ApiResult.API_RESULT_MSG, 
						 msgKey==null||"".equals(msgKey.trim())?ApiResult.DEAFULT_OPERATE_MASG
								 								:ApiResult.obtainResultMsg(msgKey));
	}
	
	/** 
	* @Description 失败时返回结果
	* @param  msgKey
	* @return ResultMap<String,Object>    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月18日 下午5:42:37
	*/
	protected ResultMap<String, Object> failureReusltMap(String msgKey){
		ResultMap<String, Object> rs = new ResultMap<String, Object>();
		return rs.put(ApiResult.API_RESULT_CODE, ApiResult.Failure.getCode())
				 .put(ApiResult.API_RESULT_STATUS,  ApiResult.Failure.getStatus())
				 .put(ApiResult.API_RESULT_MSG, 
						 msgKey==null||"".equals(msgKey.trim())?ApiResult.DEAFULT_OPERATE_MASG
								 								:ApiResult.obtainResultMsg(msgKey));
	}
	
	/** 
	* @Description 异常时返回结果
	* @param  msgKey
	* @return ResultMap<String,Object>    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月18日 下午5:43:15
	*/
	protected ResultMap<String, Object> exceptionReusltMap(String msgKey){
		ResultMap<String, Object> rs = new ResultMap<>();
		return rs.put(ApiResult.API_RESULT_CODE, ApiResult.Exception.getCode())
				 .put(ApiResult.API_RESULT_STATUS,  ApiResult.Exception.getStatus())
				 .put(ApiResult.API_RESULT_MSG, 
						 msgKey==null||"".equals(msgKey.trim())?ApiResult.DEAFULT_OPERATE_MASG
								 								:ApiResult.obtainResultMsg(msgKey));
	}
	
	
	public static class ResultMap<K,V> {
		private LinkedHashMap<K,V> map = new LinkedHashMap<>();
		
		public ResultMap<K,V> put(K k, V v){
			map.put(k,v);
			return this;
		}
		public Map<K,V> build(){
			return map;
		}
		public String toJson(){
			return JsonUtil.toJson(map);
		}
		public String toJson(String callback){
			return callback + "(" + JsonUtil.toJson(map) + ")";
		}
		@Override
		public String toString() {
			return this.toJson();
		}
	}
	
	/** 对象转JSON字符串 */
	protected String toJson(Object obj){
		return JsonUtil.toJson(obj);
	}
	
	
	/** 
	* @Description: 获取cookeid
	* @param     设定文件 
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年3月8日 下午4:11:49
	*/
	protected String requestToken(){
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		String csessionid = RequestUtil.getCSESSIONID(request);
		return csessionid;
	}

}
