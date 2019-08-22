package com.kycrm.member.api.enetity;

import java.util.Locale;
import java.util.ResourceBundle;

import com.kycrm.member.api.exception.KycrmApiException;

public class ApiCallResult {
    public static final ResourceBundle res = ResourceBundle.getBundle("api_msg", Locale.CHINA);

    /**
     * 标识操作结果类型
     */
    public enum OPERATION_RESULT_TYPE {
        /**
         * @MetaData (value = "成功", comments = "操作处理成功。前端一般是绿色的短暂气泡提示")
         */
        success,
        /**
         * @MetaData (value = "失败", comments = "操作处理失败。前端一般是红色的长时间或需要用户主动关闭的气泡提示")
         */
        failure,

    }

    // 全局成功标识代码
    public final static String SUCCESS = "100000";//操作成功
    /**
     * 用户登录已超时
     */
    public final static String FAIL_INVALID_TOKEN = "100001";//用户登录已超时
    public final static String FAIL_UNKNOWN_METHOD = "100002";//登陆异常

	public final static String FAIL_LOGIN_ILLEGALOPERATION = "100003";//非法操作
	public final static String FAIL_LOGIN_ERROE = "100004";//用户名或密码错误
	public final static String SAFEPWD_NULL = "100005";//交易密码为空
	public final static String SAFEPWD_ERROR = "100006";//交易密码错误
	public final static String MOBILE_NULL = "100007";////手机号为空
	public final static String USER_NAME_NULL = "100008";//用户名为空
	public final static String LOGIN_PWD_NULL = "100009";//登陆密码为空
	public final static String LOGIN_ACOUNT_ERROR = "100065";//手机号格式错误
    
	public final static String WINE_TAB_EXIST = "100010";//该标签已经被添加
	public final static String DELETE_ONLY_BY_SELF = "100011";//酒标签仅限本人删除
	public final static String PICTURE_ONT_ALLOWED_TO_STRANGER = "100012";//相册不对陌生人开放
	public final static String OVER_WINE_TAB_COUNT = "100013";//最多可设置50个酒标签
	public final static String UPLOAD_HEAD_PIC_FAIL = "100014";//上传头像失败
	public final static String POST_DYNAMIN_PIC_FAIL = "100015";//上传动态图片失败

	public final static String GOLD_COUNT_LESS = "100016";  //金币不足
	public final static String SMS_CODE_NULL = "100017";	//短信验证码为空
	public final static String SMS_CODE_ERROR = "100018";	//短信验证码错误
	public final static String ONE_SMS_FAIL = "100019";		//一分钟以内只能获取一次
	public final static String MOBILE_IS_ERROR = "100020";	//手机号输入错误
	public final static String MOBILE_IS_UESRED = "100021";	//该手机号已被注册
	
	public final static String MOBILE_OR_PWD_ERROR = "100022";	//手机号或密码错误
	public final static String OLD_LOGIN_PWD_ERROR = "100023";	//原始登录密码错误
	public final static String TWO_PWD_NOT_EQUEAL = "100024";	//两次密码输入不相同
	public final static String USERNAME_IS_UESRED = "100025";	//用户名已存在
	public final static String MOBILE_IS_NOT_REGISTER = "100026"; //该手机号未注册过
	public final static String OPERATE_FAIL = "100027"; //操作失败
	
	public final static String OLD_SAFE_PWD_ERROR = "100028";	//原始交易密码错误
	public final static String LOGIN_PWD_ERROR = "100029";	//登录密码错误
	
	public final static String NICKNAME_NULL = "100030";	//昵称为空
	public final static String HEADURL_NULL = "100031";	//头像地址为空
	public final static String OPENID_NULL = "100032";	//openId为空
	public final static String FEEDBACK_FAIL = "100033";	//反馈信息提交失败
	public final static String SELF_ONT_FOCUS_ON_SELF = "100034";	//自己不允许关注自己
	
	public final static String GOLDCOUNT_ISNOT = "100035";//余额不足无法开通会员，请充值
	public final static String VIP_IS_NOT = "100036";//您还不是VIP会员，请先购买才能享有VIP特权
	public final static String ASSETS_NO_OPEN = "100037";//账户未开通
	
	public final static String PRESENT_COUNT_NULL = "100037";	//您的该礼物已经用完
	public final static String ROOM_ANCHOR_COUNT_FULL = "100038";	//酒局人数已满
	public final static String REPEAT_ENTER = "100039";	//重复进入
	
	public final static String ENTER_ROOM_PWD_ERROR = "100040";	//进入酒局密码错误
	/**
	 * 全局未知错误标识代码
	 */
	public final static String FAIL_UNKNOWN = "999999";
	
	public final static String FAILPARMETERERROR = "100068";  //参数为空
	public final static String FAILSYSYDBERROR = "100066";  //查询库错误|数据错误
	
	
	
	public final static String OLD_SAFE_PWD_ERROR_NULL = "100041";//未設置原始交易密碼
	public final static String NO_SEND_TO_SELF = "100042";//不能送自己礼物哟

	public final static String TUHAO_NOT_KICK = "100043";//土豪会员不能被踢出
	public final static String MASTER_TO_KICK = "100044";//只有房主能踢人
	public final static String MASTER_TO_CO = "100045";//只有房主能开麦关麦
	
	public final static String CREATE_ROOM_FAIL = "100046";//创建酒局失败
	
	public final static String NOT_SET_SAFEPWD = "100047";//您还未设置交易密码
	
	public final static String DYNAMIC_ONLY_DELETE_BY_SELF = "100048";//仅本人能删除本人动态
	public final static String ROOM_REMOVED = "100049";//房间不存在

	public final static String TO_WATCH_CHALLENGE = "100050";//请点击开始观战
	public final static String CHALLENGE_STARTED = "100051";//挑战已经开始，不能再加入
	public final static String MASTER_TO_DONE = "100052";//只有裁判能进行排名操作
	
	public final static String ONLY_JUDGE_CAN_START_CHALLENGE = "100053";//只有裁判能开始比赛
	
	public final static String USER_HAS_EXITED = "100054";//该用户已经退出
	public final static String MASTER_TO_DROP = "100055";//只有裁判能解散结局
	public final static String CHALLENGE_ENDED = "100056";//挑战圈已经解散
	
	public final static String ROOM_ANCHOR_LEFT = "100057";//有主播离开酒局
	public final static String MANAGER_ALLOWED_OPERATE = "100058";//只有管理员能操作
	
	public final static String ID_IS_BLOCKED = "100059";//该账号已被封，不能登录
	public final static String ROOM_GAME_STARTED = "100060";//酒局中游戏已经开始
	public final static String NOT_LOGIN = "100061";//请登录
	
	public final static String SPONSOR_OPERATE = "100062";//您非创建者，不能操作
	
	public final static String CHALLENGE_NOT_STARTED = "100063";//挑战圈未开始
	
	public final static String NICKNAME_LENGTH = "100064";//用户昵称大于12位

	public final static String EXTENSROOM = "100067";  //此人已存在酒局中
	
	public final static String EXTENSCART = "100069";  //此人已存在购物车
	
	public final static String  GOODSNOTEXTEN = "100070";  //商品不存在
	
	public final static String  CARTNOTEXTEN = "100071";  //购物车商品为空

	public final static String  CARTNOTNULL = "100072";  //购物车商品数量为1不能减去

	public final static String  ADDRESSNOTNULL = "100073";  //收货地址不能为空

	public final static String  ORDERPRODUCTNOTNULL = "100074";  //订单商品不能为空

	public final static String  ORDERGOODSNOTEXTEN = "100075";  //订单商品数量不能为0
	
	public final static String  ORDERNOTEXTEN = "100076";  //订单不存在
	
	public final static String FAILED_UPDATE_COWRYTYPE ="100080";//将宝贝从可提取更新到可兑换状态失败
	
	public final static String FAILED_EXCHANGE_PRESENT ="100081";//兑换礼物失败
	
	public final static String TOTAL_EXCHANGE_NOTEQULE ="100082";//金币数计算与传过来的不同
	
	public final static String CANNOT_EXCHANGE ="100083";//要兑换的礼物数大于拥有的礼物数
	
	public final static String FAIL_ALIPAYMENT ="100084";//支付宝支付失败
	
	public final static String FAIL_GOLD_PAY = "100085";//金币支付失败
	
	public final static String FAIL_WXPAYMENT = "100086";//微信支付失败
	
	public final static String FAIL_ALIPAY_REFUND ="100087";//支付宝退款失败
	
	public final static String FAIL_GOLDPAY_REFUND ="100088";//金币退款失败
	
	public final static String FAIL_WXPAY_REFUND ="100089";//微信退款失败
	
	public final static String NOT_EQUAL_GOLD ="100090";//金币数不同
	
	public final static String CANNOT_REFUND ="100091";//不能退款

	public final static String ORDER_REFUND ="100092";//订单正在退款处理呦.
	
	
	
	public final static String  CHALLENGEWAITSTART = "100077";  //比赛暂未开始,不能排名
	

	/**
     * 返回success或failure操作标识
     */
    private String type;

    /**
     * 成功：100000，其他标识错误
     */
    private String code;

    /**
     * 国际化处理的返回JSON消息正文，一般用于提供failure错误消息
     */
    private String message;

    /**
     * 补充的业务数据
     */
    private Object result;

    public static ApiCallResult buildSuccessResult(String message, Object data) {
        return new ApiCallResult(OPERATION_RESULT_TYPE.success, message, data,SUCCESS);
    }

    public static ApiCallResult buildSuccessResult() {
        return new ApiCallResult(OPERATION_RESULT_TYPE.success, res.getString(SUCCESS),SUCCESS);
    }

    public static ApiCallResult buildSuccessResult(String message) {
        return new ApiCallResult(OPERATION_RESULT_TYPE.success, message,SUCCESS);
    }

    public static ApiCallResult buildSuccessResult(Object data) {
        return new ApiCallResult(OPERATION_RESULT_TYPE.success, res.getString(SUCCESS), data,SUCCESS);
    }

    public static ApiCallResult buildFailureResult(String code, String message) {
        return new ApiCallResult(OPERATION_RESULT_TYPE.failure, message,code);
    }

    public static ApiCallResult buildFailureResult(String code, String message, Object data) {
        return new ApiCallResult(OPERATION_RESULT_TYPE.failure, message, data,code);
    }

    public static ApiCallResult buildFailureResult(String code, Object data) {
        return new ApiCallResult(OPERATION_RESULT_TYPE.failure, res.getString(code), data,code);
    }

    public static ApiCallResult buildFailureResult(String code) {
        return new ApiCallResult(OPERATION_RESULT_TYPE.failure, res.getString(code),code);
    }

    public static ApiCallResult buildFailureResult(KycrmApiException e) {
        String errMsg=res.getString(e.getCode());
        if(e.getAppendMsg()!=null ||!("".equals(e.getAppendMsg()))){
            errMsg+=e.getAppendMsg();
        }
        return new ApiCallResult(OPERATION_RESULT_TYPE.failure, errMsg,e.getCode());
    }

    public ApiCallResult(OPERATION_RESULT_TYPE type, String message) {
        this.type = type.name();
        this.message = message;
    }

    public ApiCallResult(OPERATION_RESULT_TYPE type, String message, Object data) {
        this.type = type.name();
        this.message = message;
        this.result = data;
    }

    public ApiCallResult(OPERATION_RESULT_TYPE type, String message, Object data,String code) {
        this.type = type.name();
        this.message = message;
        this.result = data;
        this.code = code;
    }
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
    
    
}