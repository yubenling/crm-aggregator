package com.kycrm.util;

public abstract class SendMessageStatusInfo {
	public static final String SEND_SYSTEM_SCHEDULE = "system";
	/**
	 * 短信发送成功
	 */
	public static final String SEND_SUCCESS = "100";
	/**
	 * 验证失败
	 */
	public static final String AUTH_FAIL = "101";
	/**
	 * 短信余额不足
	 */
	public static final String INSUFFICIENT_BALANCE = "102";
	
	/**
	 * 发送失败
	 */
	public static final String SEND_FAIL = "103";
	/**
	 * 非法字符
	 */
	public static final String FORBIDDEN_CHARACTER = "104";
	/**
	 * 内容过多
	 */
	public static final String MEMORY_ERROR = "105";
	/**
	 * 号码过多
	 */
	public static final String TOO_MUCH_PHONE = "106";
	/**
	 * 频率过快
	 */
	public static final String UPDATE_TOO_FAST = "107";
	/**
	 * 号码内容为空或用了UTF-8，不支持，改成GBK即可
	 */
	public static final String USE_GBK = "108";
	/**
	 * 账号冻结
	 */
	public static final String ACCOUNT_LOCK = "109";
	/**
	 * 禁止频繁单条发送
	 */
	public static final String SINGLE_UPDATE_TOO_FAST = "110";
	/**
	 * 系统暂定发送
	 */
	public static final String SYSTEM_WAIT_SEND = "111";
	/**
	 * 子号不正确
	 */
	public static final String PHONE_ERROR = "112";
	/**
	 * 非法信息拒绝验证码炸弹或签名黑名单或双签名
	 */
	public static final String BLACKLIST = "120";
	/**
	 * 新加的 短信发送记录表crm_sms_send_record   status字段    状态码
	 */
	public static final Integer RECORDSUCESS = 2;
	public static final Integer RECORDFAIL = 1;
	/**
	 * 新加的 短信发送记录表crm_sms_send_inof   status字段  状态码
	 */
	public static final Integer INFOSUCCESS = 1;
	public static final Integer INFOFAIL = 0;
	/**
	 * 扣除短信数量  状态
	 */
	public static final boolean  DEL_SMS = true;
	/**
	 * 增加短信数量 状态
	 */
	public static final boolean  ADD_SMS = false;
}
