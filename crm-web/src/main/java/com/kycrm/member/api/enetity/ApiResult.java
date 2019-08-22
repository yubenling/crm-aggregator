package com.kycrm.member.api.enetity;

import java.util.Locale;
import java.util.ResourceBundle;

/** 
* @ClassName: ApiResult 
* @Description 接口返回结果
* @author jackstraw_yu
* @date 2018年1月18日 下午6:24:31 
*/
public enum ApiResult {

	/**
	 * 操作成功
	 */
	Success(100,true),
	
	/**
	 * 操作失败
	 */
	Failure(101,false),
	
	/**
	 * 操作异常
	 */
	Exception(102,false);
	
	/**
	 * 状态码
	 */
	private Integer code;
    
	/**
	 * 状态
	 */
	private Boolean status;

	private ApiResult(Integer code, Boolean status) {
		this.code = code;
		this.status = status;
	}
	public Integer getCode() {
		return code;
	}
	public void setCode(Integer code) {
		this.code = code;
	}
	public Boolean getStatus() {
		return status;
	}
	public void setStatus(Boolean status) {
		this.status = status;
	}

	private static final ResourceBundle RESULT_MSG = ResourceBundle.getBundle("api_msg", Locale.CHINA);
	
	
	/** 
	* @Description 获取接口执行结果提示语句 
	* @param  key
	* @return String    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月18日 下午2:24:21
	*/
	public static final String obtainResultMsg(String key){
		return RESULT_MSG.getString(key);
	}

	/**
	 * 接口返回状态码
	 */
	public static final String API_RESULT_CODE = "code";
	/**
	 * 接口返回状态
	 */
	public static final String API_RESULT_STATUS = "status";
	/**
	 * 接口返回操作提示
	 */
	public static final String API_RESULT_MSG = "msg";
	/**
	 * 接口返回结果
	 */
	public static final String API_RESULT = "result";
	
	
	/**
	 * 默认提示语句,为"";视情况使用
	 */
	public static final String DEAFULT_OPERATE_MASG = "";

	
//===================================华丽的分割线===================================
	//为方便后期添加提示语句
	//操作成功 1000 起 
	//操作失败 2000起 
	//异常3000起
	//其他4000起
//====================================操作成功=====================================
	/**
	 * 提示语:保存成功！<br/>
	 * 备注:无
	 */
	public static final String SAVE_SUCCESS = "1000";
	/**
	 * 提示语:修改成功！<br/>
	 * 备注:无
	 */
	public static final String UPDATE_SUCCESS = "1001";
	/**
	 * 提示语:保存设置成功！<br/>
	 * 备注:无
	 */
	public static final String SAVE_SET_SUCCESS = "1002";
	/**
	 * 提示语:删除设置成功！<br/>
	 * 备注:无
	 */
	public static final String DEL_SET_SUCCESS = "1003";
	/**
	 * 提示语:发送成功！<br/>
	 * 备注:无
	 */
	public static final String SEND_SMS_SUCCESS = "1004";
	/**
	 * 提示语:上传成功！<br/>
	 * 备注:无
	 */
	public static final String IMPORT_SUCCESS= "1005";
	
	/**
	 * 提示语:反馈成功！<br/>
	 * 备注:无
	 */
	public static final String FEEDBACK_SUCCESS= "1006";
	/**
	 * 提示语:删除成功！<br/>
	 * 备注:无
	 */
	public static final String DEL_SUCCESS = "1007";
	/**
	 * 提示语:添加成功！<br/>
	 * 备注:无
	 */
	public static final String INSERT_SUCCESS = "1008";
	/**
	 * 提示语:上传处理中！<br/>
	 * 备注:无
	 */
	public static final String IMPORT_DISPOSE = "1009";
	/**
	 * 提示语:定时成功！<br/>
	 * 备注:无
	 */
	public static final String SEND_TIMING_SUCCESS = "1010";
	/**
	 * 提示语:操作成功！<br/>
	 * 备注:无
	 */
	public static final String OPERATION_SUCCESS = "1011";
	/**
	 * 提示语:定时中！<br/>
	 * 备注:无
	 */
	public static final String SMS_TIMING_CENTRE = "1012";
	/**
	 * 提示语:发送中！<br/>
	 * 备注:无
	 */
	public static final String SMS_SEND_CENTRE = "1013";
//====================================操作失败=====================================
	/**
	 * 提示语:保存失败！<br/>
	 * 备注:无
	 */
	public static final String SAVE_FAILURE = "2000";
	/**
	 * 提示语:保存失败，请重新操作或联系系统管理员！<br/>
	 * 备注:无
	 */
	public static final String SAVE_FAILURE_TRY_AGAIN = "2001";
	/**
	 * 提示语:修改失败！<br/>
	 * 备注:无
	 */
	public static final String UPDATE_FAILURE = "2002";
	/**
	 * 提示语:修改失败，请重新操作或联系系统管理员！<br/>
	 * 备注:无
	 */
	public static final String UPDATE_FAILURE_TRY_AGAIN = "2003";
	/**
	 * 提示语:保存设置失败！<br/>
	 * 备注:无
	 */
	public static final String SAVE_SET_FAILURE = "2004";
	/**
	 * 提示语:操作失败，请重新操作或联系系统管理员！<br/>
	 * 备注:无
	 */
	public static final String OPERATE_FAILURE_TRY_AGAIN = "2005";
	/**
	 * 提示语:参数错误！<br/>
	 * 备注:无
	 */
	public static final String PARAM_WRONG = "2006";
	/**
	 * 提示语:参数缺失！<br/>
	 * 备注:无
	 */
	public static final String PARAM_LACK = "2007";
	/**
	 * 提示语:手机号为空！<br/>
	 * 备注:无
	 */
	public static final String MOBILE_IS_NULL = "2008";
	/**
	 * 提示语:手机号错误！<br/>
	 * 备注:无
	 */
	public static final String MOBILE_WRONG = "2009";
	/**
	 * 提示语:您今天使用该软件发送验证码的次数过多，请明日再次使用，谢谢！<br/>
	 * 备注:无
	 */
	public static final String SECURITY_CODE_USE_TOP= "2010";
	/**
	 * 提示语:验证码为空或错误！<br/>
	 * 备注:无
	 */
	public static final String SECURITY_CODE_ERROR = "2011";
	/**
	 * 提示语:验证码已过期或手机号错误！<br/>
	 * 备注:无
	 */
	public static final String MOBILE_OR_CODE_ERROR = "2012";
	/**
	 * 提示语:验证码已过期！<br/>
	 * 备注:无
	 */
	public static final String SECURITY_CODE_EXPIRY = "2013";
	/**
	 * 提示语:设置类型错误！<br/>
	 * 备注:无
	 */
	public static final String SET_TYPE_ERROR = "2014";
	/**
	 * 提示语:设置状态为空！<br/>
	 * 备注:无
	 */
	public static final String SET_STATUS_NULL = "2015";
	/**
	 * 提示语:短信内容为空！<br/>
	 * 备注:无
	 */
	public static final String SMS_CONTENT_NULL= "2016";
	/**
	 * 提示语:短信内容不能大于500字！<br/>
	 * 备注:无
	 */
	public static final String SMS_CONTENT_TOO_LONG = "2017";
	/**
	 * 提示语:必选项不能为空！<br/>
	 * 备注:无
	 */
	public static final String OPTION_NO_SELECT = "2018";
	/**
	 * 提示语:黑名单评价内容不能大于500字！<br/>
	 * 备注:无
	 */
	public static final String EVALUATE_BLACK_CONTENT_TOO_LONG = "2019";
	/**
	 * 提示语:通知号码填写错误！<br/>
	 * 备注:无
	 */
	public static final String INFORM_MOBILE_ERROR = "2020";
	/**
	 * 提示语:执行时间为空或错误！<br/>
	 * 备注:无
	 */
	public static final String EXECUTE_TIME_ERROR = "2021";
	/**
	 * 提示语:发送短信订单范围不能为空！<br/>
	 * 备注:无
	 */
	public static final String TRDE_BLOCK_NULL = "2022";
	/**
	 * 提示语:通知时间项参数错误！<br/>
	 * 备注:无
	 */
	public static final String INFORM_TIME_OPTION_PARAM_ERROR = "2023";
	/**
	 * 提示语:设置数量达到上限！<br/>
	 * 备注:无
	 */
	public static final String SET_TOO_MANY= "2024";
	/**
	 * 提示语:该类型设置已经存在！<br/>
	 * 备注:无
	 */
	public static final String THIS_TYPE_SET_EXIST = "2025";
	/**
	 * 提示语:该设置的任务名称不能为空！<br/>
	 * 备注:无
	 */
	public static final String SET_NAME_NULL = "2026";
	/**
	 * 提示语:该设置的任务名称已存在！<br/>
	 * 备注:无
	 */
	public static final String THIS_SET_NAME_EXIST = "2027";
	/**
	 * 提示语:商品数量填写错误！<br/>
	 * 备注:无
	 */
	public static final String PRODUCT_NUM_ERROR = "2028";
	/**
	 * 提示语:支付金额填写错误！<br/>
	 * 备注:无
	 */
	public static final String PAY_MENT_ERROR = "2029";
	/**
	 * 提示语:通知时间填写错误！<br/>
	 * 备注:无
	 */
	public static final String INFORM_TIME_ERROR = "2030";
	/**
	 * 提示语:排除时间填写错误！<br/>
	 * 备注:无
	 */
	public static final String EXCEPT_TIME_ERRO = "2031";
	/**
	 * 提示语:短信签名为空！<br/>
	 * 备注:无
	 */
	public static final String SMS_AUTOGRAPH_NULL = "2032";
	
	/**
	 * 提示语:发送失败！<br/>
	 * 备注:无
	 */
	public static final String SEND_SMS_FAILURE = "2033";
	/**
	 * 提示语:短信余额不足！<br/>
	 * 备注:无
	 */
	public static final String SMS_NOT_ENOUGH= "2034";
	/**
	 * 提示语:手机号：错误/黑名单/被屏蔽！<br/>
	 * 备注:无
	 */
	public static final String MOBILE_BLACK_SHIELD= "2035";
	/**
	 * 提示语:定时失败！<br/>
	 * 备注:无
	 */
	public static final String TIMING_SMS_FAILURE= "2036";
	/**
	 * 提示语:上传失败！<br/>
	 * 备注:无
	 */
	public static final String IMPORT_FAILURE= "2037";
	/**
	 * 提示语:反馈失败！<br/>
	 * 备注:无
	 */
	public static final String FEEDBACK_FAILURE= "2038";
	/**
	 * 提示语:上传文件过大！<br/>
	 * 备注:无
	 */
	public static final String IMPORT_FILE_OVERSIZE= "2039";
	/**
	 * 提示语:删除失败！<br/>
	 * 备注:无
	 */
	public static final String DEL_FAILURE = "2040";
	/**
	 * 提示语:请选择数据！<br/>
	 * 备注:无
	 */
	public static final String ID_IS_NULL = "2041";
	/**
	 * 提示语:添加失败！<br/>
	 * 备注:无
	 */
	public static final String INSERT_FAILURE = "2042";
	/**
	 * 提示语:内容错误或空！<br/>
	 * 备注:无
	 */
	public static final String CONTENT_ERROR_ISNULL = "2043";
	/**
	 * 提示语:上传文件格式错误！<br/>
	 * 备注:无
	 */
	public static final String IMPORT_FILE_FORMATERROR = "2044";
	/**
	 * 提示语:定时时间不能为空！<br/>
	 * 备注:无
	 */
	public static final String TIMING_TIME_ISNULL= "2045";
	/**
	 * 提示语:定时时间必须大于30分钟！<br/>
	 * 备注:无
	 */
	public static final String TIMING_TIME_GT30MINUTE= "2046";
	/**
	 * 提示语:发送类型错误！<br/>
	 * 备注:无
	 */
	public static final String SEND_SMS_TYPEERROR= "2047";
	/**
	 * 提示语: 店铺名称为空
	 * 备注：无
	 */
	public static final String SHOPNAME_IS_NULL="2048";
	/**
	 * 提示语: 充值失败！
	 * 备注：无
	 */
	public static final String RECHARGE_FAILURE="2049";
	/**
	 * 提示语: 手机号码未改变不能获取验证码！
	 * 备注：无
	 */
	public static final String MOBILE_NO_CHANGE="2050";
	/**
	 * 提示语: 上传文件不存在！
	 * 备注：无
	 */
	public static final String IMPORT_FILE_ISNULL="2051";
	/**
	 * 提示语: 文件错误，请同时上传订单及宝贝两个文件！
	 * 备注：无
	 */
	public static final String IMPORT_ORDERANDITEM_ISNULL="2052";
	/**
	 * 提示语: 请不要修改文件名字！
	 * 备注：无
	 */
	public static final String IMPORT_NAME_ERROR="2053";
	/**
	 * 提示语: 请不要修改文件内容！
	 * 备注：无
	 */
	public static final String IMPORT_CONTENT_ERROR="2054";
	
	/**
	 * 提示语: 请关闭"二次催付"！
	 * 备注：无
	 */
	public static final String CLOSE_TWICE_PAYMENT="2068";
	
	/**
	 * 提示语: 请开启"常规催付"！
	 * 备注：无
	 */
	public static final String OPEN_GENERAL_PAYMENT="2069";
	
	/**
	 * 提示语: 修改失败！定时发送时间与当前时间相差小于3分钟!
	 * 备注 ：无
	 */
	public static final String CACLE_FAILED="2070";
	
	public static final String NOT_FIND_MOBILE = "2072";

	
//====================================操作异常=====================================
	/**
	 * 提示语:服务调用失败，请联系系统管理员！<br/>
	 * 备注:rpc(dubbo)远程调用失败,多为代码问题/服务宕机!<br/>
	 * com.alibaba.dubbo.remoting.RemotingException
	 */
	public static final String REMOTING_EXCEPTION = "3000";
	/**
	 * 提示语:服务调用失败，请重新操作或联系系统管理员！<br/>
	 * 备注:rpc(dubbo)远程调用失败,多为代码问题/服务宕机!<br/>
	 * com.alibaba.dubbo.remoting.RemotingException
	 */
	public static final String REMOTING_EXCEPTION_TRY_AGAIN = "3001";
	
	/**
	 * 提示语:服务调用超时，请联系系统管理员！<br/>
	 * 备注:rpc(dubbo)服务调用超时,此情况下,用户的增删改,不建议提示用户重试或者重新操作!<br/>
	 * com.alibaba.dubbo.remoting.TimeoutException
	 */
	public static final String TIMEOUT_EXCEPTION= "3002";
	/**
	 * 提示语:服务调用超时，请联系系统管理员或重新操作！<br/>
	 * 备注:rpc(dubbo)服务调用超时,此情况下,用户的增删改,用户的查询操作可以视情况而定,提示用户重试或者重新操作!<br/>
	 * com.alibaba.dubbo.remoting.TimeoutException
	 */
	public static final String TIMEOUT_EXCEPTION_TRY_AGAGIN= "3003";
	/**
	 * 提示语:操作异常！<br/>
	 * 备注:无
	 */
	public static final String OPERATE_EXCEPTION = "3004";
	
	/**
	 * 提示语:操作异常，请重新操作或者联系系统管理员！<br/>
	 * 备注:无
	 */
	public static final String OPERATE_EXCEPTION_TRY_AGAIN = "3005";
//====================================其他=====================================
	/**
	 * 提示语:用户为空！<br/>
	 * 备注:无
	 */
	public static final String USER_IS_NULL = "4000";
	
	public static final String SAME_USER = "4017";
	
	public static final String ALREADY_BINDING = "4018";
	
	public static final String USER_IS_NOT_EXISTS = "4019";
	
	
}
