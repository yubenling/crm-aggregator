package com.kycrm.util;

/**
 * 公用常量类。
 * 
 * @author wy
 * @version 创建时间：2017年7月28日 上午11:23:46
 */
public abstract class Constants {
	/**
	 * 淘宝正式环境地址
	 */
	public static final String TAOBAO_URL = "http://gw.api.taobao.com/router/rest";

	/**
	 * 淘宝正式环境换取token地址
	 */
	public static final String TAOBAO_TOKEN_URL = "http://oauth.taobao.com/token";

	/**
	 * 加解密数据使用的服务器路径
	 */
	public static final String TAOBAO_SECRET_URL = "https://eco.taobao.com/router/rest";

	/**
	 * 公网回调地址
	 */
	public static final String TAOBAO_CALLBACK_URL = "http://b.kycrm.com:81/getUserCode/login";

	/**
	 * 创建应用时，TOP颁发的唯一标识，TOP通过App Key来鉴别应用的身份。调用接口时必须传入的参数。
	 */
	public static final String TOP_APP_KEY = "21800702";

	/**
	 * 客云CRM服务应用code
	 */
	public static final String TOP_APP_CODE = "FW_GOODS-1952286";

	/**
	 * App Secret是TOP给应用分配的密钥，开发者需要妥善保存这个密钥，这个密钥用来保证应用来源的可靠性，防止被伪造。
	 */
	public static final String TOP_APP_SECRET = "ef60a0040f9cb676f17526290bc01692";

	/**
	 * 聚石塔rds名称（用于添加同步用户）
	 */
	public static final String RDS_MYSQL_ACCOUNT = "rm-vy14617996v77fs5h";

	/**
	 * 加密数据使用的安全令牌码
	 */
	public static final String SECURITY_TOKEN = "qE8Vqx/iYqZwJ0BfiRN5icpnam51pdwbZMLIzi+CV7c=";

	/**
	 * 加密数据使用的服务器路径
	 */
	public static final String SECURITY_SERVICE_URL = "http://eco.taobao.com/router/rest";

	/**
	 * 购买应用页面地址
	 */
	public static final String TOP_BUY_APP_URL = "redirect:http://fuwu.taobao.com/ser/detail.htm?service_code=FW_GOODS-1952286&selected_item_code=FW_GOODS-1952286-1&redirect=1";

	/**
	 * 扣除短信数量 状态
	 */
	public static final boolean DEL_SMS = true;
	/**
	 * 增加短信数量 状态
	 */
	public static final boolean ADD_SMS = false;

	/**
	 * 用户日志 类型 扣除短信数
	 */
	public final static String DEL_MESSEGE = "扣除短信数";
	/**
	 * 用户日志 类型 增加短信数
	 */
	public final static String ADD_MESSEGE = "增加短信数";
	/**
	 * 用户日志 操作成功
	 */
	public final static String USER_OPERATION_LOG_SUCCESS = "成功";
	/**
	 * 用户日志 操作失败
	 */
	public final static String USER_OPERATION_LOG_FAIL = "失败";
	/**
	 * 用户日志 操作失败
	 */
	public final static String USER_OPERATION_SINGLE = "单条短信扣费";
	/**
	 * 用户日志 操作失败
	 */
	public final static String USER_OPERATION_MORE = "群发短信扣费";

	/**
	 * 使用软件,初次添加手机号赠送500条短信
	 */
	public static final Integer PROVIDER_CUSTOMER_SMS_NUM = 1000;

	/**
	 * 补充资料发送验证码内容
	 */
	public static final String MESSAGE_VALIDATECODE_CONTNET = "【客云CRM】 验证码CODE，"
			+ "有效时间5分钟。您正在进行客云系统登陆，如非本人操作，建议立即更改账户密码！";

	/**
	 * 后台设置发送短信 即将过期几天(之前的需求:3*24*60*60000l)/(现在的需求:7*24*60*60000l)
	 */
	public static final Long EXPIRATION_TIME_SEND = 604800000l;
	/**
	 * 大于多少条
	 */
	public static final Long COUNT_SEND = 100l;
	/**
	 * 后台管理设置,默认提醒条数
	 */
	public static final Integer MESSAGE_REMINDER = 50;

	/**
	 * 催付效果，短信发送量和当前短信条数（每天9点发送前一天数据）
	 */
	public static final String MESSAGE_EXPEDITING_CONTNET = "【客云CRM】 尊敬的USERNAME您好，截止当前您的短信剩余条数为CURRENTCOUNT条、"
			+ "昨天的短信发送总数量为SENDCOUNT条、其中催付发送的条数为EXPEDITING条。" + "顺祝商祺!";

	/**
	 * 短信余额不足报警：剩余条数不足*条开始提醒
	 */
	public static final String MESSAGE_SMSCOUNT_CONTNET = "【客云CRM】 尊敬的USERNAME截止当前您的短信已不足:COUNT条，为了不影响您的使用，请尽快充值。顺祝商祺！";

	/**
	 * 软件过期提醒
	 */
	public static final String MESSAGE_SERVICEEXPIRE_CONTNET = "【客云CRM】 尊敬的USERNAME您好，"
			+ "您的客云服务到期日期为EXPIRATIONTIME。请您尽快联系您的商务经理或客服，可以免费续订。顺祝商祺！";

	/**
	 * 日期时间格式 yyyy-MM-dd HH:mm:ss
	 */
	public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	/**
	 * 订单保存超时时间
	 */
	public static final int TRADE_SAVE_TIME_OUT = 120000;

	/**
	 * 订单保存过期时间
	 */
	public static final int TRADE_SAVE_EXPIRE_TIME = 60000;

	/**
	 * 订单一次最大更新数量限制
	 */
	public static final int TRADE_SAVE_MAX_LIMIT = 1000;
	/**
	 * 订单一次最大查询数量
	 */
	public static final int TRADE_SEARCH_MAX_LIMTI = 100;
	/**
	 * 店铺数据-数据类型
	 */
	public static final String DataType = "DataType";

	/**
	 * 黑名单类型
	 */
	public static final String TypeBlack = "TypeBlack";

	/**
	 * 添加来源
	 */
	public static final String AddSource = "AddSource";

	/**
	 * 所属功能
	 */
	public static final String FUNCTIONGENS = "FUNCTIONGENS";

	/**
	 * 进入首页时添加手机号赠送500条短信
	 */
	public static final Integer INDEX_SEND_SMS_NUM = 500;

	/**
	 * 定时任务的分页处理数据大小
	 */
	public static final Long RSP_PAGE_SIZE = 100L;
	/**
	 * 分页显示的条数 15
	 */
	public static final Integer PAGE_SIZE_MAX = 15;
	/**
	 * 分页显示的条数 10
	 */
	public static final Integer PAGE_SIZE_MIDDLE = 10;
	/**
	 * 分页显示的条数 5
	 */
	public static final Integer PAGE_SIZE_MIN = 5;

	/**
	 * 分页处理数据的条数据 500
	 */
	public static final Long PROCESS_PAGE_SIZE_MIN = 500L;
	/**
	 * 分页处理数据的条数据 2000
	 */
	public static final Long PROCESS_PAGE_SIZE_MIDDLE = 2000L;
	/**
	 * 分页处理数据的条数据 10000
	 */
	public static final Long PROCESS_PAGE_SIZE_MAX = 10000L;
	/**
	 * 效果分析一次查询的数据 50000
	 */
	public static final Integer PROCESS_PAGE_SIZE_OVER = 50000;
	/**
	 * 订单同步队列长度 20000
	 */
	public static final Integer TRADE_SYN_QUEUE_SIZE = 10000;
	/**
	 * 订单每次分页间隔休眠时间 10 * 1000ms
	 */
	public static final int TRADE_SYN_SLEEP_TIME = 10000;
	/**
	 * 一次分页查询，Map集合key的初始化用户数
	 */
	public static final int TRADE_SYN_USER_KEY = 1000;

	/**
	 * trade每10分钟同步一次
	 */
	public static final int TRADE_SYN_SLEEP_MINUTE = 10;

	/**
	 * item每20分钟同步一次
	 */
	public static final int ITEM_SYN_TIME_SLEEP_MINUTE = 10;

	/**
	 * rate 每30分钟同步一次
	 */
	public static final int RATE_SYN_TIME_SLEEP_MINUTE = 30;

	/**
	 * rate 每次同步时长 120分钟
	 */
	public static final int RATE_SYN_TIME_MINUTE = 60;
	/**
	 * REFUND 同步时间
	 */
	public static final int REFUND_SYN_TIME_SLEEP_MINUTE = 10;
	/**
	 * 默认同步节点时间
	 */
	public static final String DEFAULT_NODE_SYNC_TIME = "2019-01-15 00:00:00";
	/**
	 * 默认创建处理Trade的线程数，与CPU处理的内核数相同
	 */
	public static final int TRADE_THREAD_NUM = 64;
	/**
	 * 默认创建处理item的线程数
	 */
	public static final int ITEM_THREAD_NUM = 32;

	public static final String SMSSEPARATOR = "@^";
	public static final String SMSSEPARATOR_S="@\\^";

	public static final Integer SCHEDULESEND_PAGESIZE = 1;
    /**
     * 退款队列的长度
     */
	public static final int REFUND_QUEUE_SIZE = 10000;

	
}
