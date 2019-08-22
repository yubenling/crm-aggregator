package com.kycrm.util;


public class ConstantUtils {
	
	//店铺数据-数据类型
	public static final String DataType = "DataType";
	
	//黑名单类型
	public static final String TypeBlack = "TypeBlack";
	
	//添加来源
	public static final String AddSource = "AddSource";
	
	//所属功能
	public static final String FUNCTIONGENS= "FUNCTIONGENS";
	
	
	//进入首页时添加手机号赠送500条短信
	public static final Integer INDEX_SEND_SMS_NUM= 500;
	
	
	//定时任务的分页处理数据大小
	public static final Long RSP_PAGE_SIZE = 100L;
	//分页显示的条数
	public static final Integer PAGE_SIZE_MAX = 15;
	public static final Integer PAGE_SIZE_MIDDLE = 10;
	public static final Integer PAGE_SIZE_MIN = 5;
	
	//分页处理数据的条数据
	public static final Long PROCESS_PAGE_SIZE_MIN = 500L;
	public static final Long PROCESS_PAGE_SIZE_MIDDLE = 1000L;
	public static final Long PROCESS_PAGE_SIZE_MAX = 10000L;
	//效果分析一次查询的数据
	public static final Integer PROCESS_PAGE_SIZE_OVER = 50000;
	
	//后台设置发送短信
	//即将过期几天(之前的需求:3*24*60*60000l)/(现在的需求:7*24*60*60000l)
	public static final Long EXPIRATION_TIME_SEND = 604800000l;
	//大于多少条
	public static final Long COUNT_SEND = 100l;
	//后台管理设置,默认提醒条数
	public static final Integer MESSAGE_REMINDER = 50; 
	
	
	
	//补充资料发送验证码内容
	public static final String MESSAGE_VALIDATECODE_CONTNET = "【客云CRM】 验证码CODE，"
			+ "有效时间5分钟。您正在进行客云系统登陆，如非本人操作，建议立即更改账户密码！";
	
	/*
	 * 催付效果，短信发送量和当前短信条数（每天9点发送前一天数据）
	 * 短信余额不足报警：剩余条数不足*条开始提醒
	 * 软件过期提醒
	 * 最新促销活动通知
	 * 
	 */
	
	//催付效果，短信发送量和当前短信条数（每天9点发送前一天数据）
 	public static final String MESSAGE_EXPEDITING_CONTNET = 
 			"【客云CRM】 尊敬的USERID您好，截止当前您的短信剩余条数为CURRENTCOUNT条、"
 			+ "昨天的短信发送总数量为SENDCOUNT条、其中催付发送的条数为EXPEDITING条。"
 			+"顺祝商祺!";
 	
 	//短信余额不足报警：剩余条数不足*条开始提醒
 	public static final String MESSAGE_SMSCOUNT_CONTNET =
 			"【客云CRM】 尊敬的USERID截止当前您的短信已不足:COUNT条，为了不影响您的使用，请尽快充值。顺祝商祺！";
 	
 	//软件过期提醒
 	public static final String MESSAGE_SERVICEEXPIRE_CONTNET = 
 			"【客云CRM】 尊敬的USERID您好，"
 		 			+ "您的客云服务到期日期为EXPIRATIONTIME。请您尽快联系您的商务经理或客服，可以免费续订。顺祝商祺！";
 			/*"【客云CRM】 尊敬的USERID您好，"
 			+ "您的客云服务到期日期为EXPIRATIONTIME。顺祝商祺！";*/
}
