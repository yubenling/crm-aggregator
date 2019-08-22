package com.kycrm.member.util;

import com.kycrm.util.JsonUtil;

/**
 * @类名称：MsgUtil 
 * @类描述：消息工具类 
 * @创建时间：2017年03月16日 下午9:20:54 
 * @author zlp
 * @version V1.0
 */
public class MsgUtil {
	
	
	/**
	 * @名称：returnSuccess 
	 * @描述：返回成功 
	 * @param obj
	 * @return
	*/
	public static String returnSuccess(Object obj){
		ReturnMessage msg=new ReturnMessage();
		msg.setReturnCode("100");
		msg.setMsgDesc("提交成功");
		msg.setResult(obj);
		return JsonUtil.toJson(msg);
	}
	/**
	 * @名称：returnError 
	 * @描述：返回失败 
	 * @param code 错误码
	 * @return
	*/
	public static String returnError(String code,String errmsg){
		ReturnMessage msg=new ReturnMessage();
		msg.setReturnCode(code);
		msg.setMsgDesc("提交成功");
		msg.setResult("");
		return JsonUtil.toJson(msg);
	}

}
