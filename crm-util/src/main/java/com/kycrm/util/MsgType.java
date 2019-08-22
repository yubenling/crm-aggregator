package com.kycrm.util;

/**
 * @ClassName: MsgType
 * @Description: (短信类型的常量类)
 * @author jackstraw_yu
 * @date 2017年4月21日 下午5:20:45
 * 
 */
public class MsgType {

	private MsgType() {
	}

	/****
	 * 项目中所有短信类型 "短信类型 设置类型 1-下单关怀 2-常规催付 3-二次催付 4-聚划算催付 5-预售催付 6-发货提醒 7-到达同城提醒
	 * 8-派件提醒 9-签收提醒 10-疑难件提醒 11-延时发货提醒 12-宝贝关怀 13-付款关怀 14-回款提醒 15-退款关怀 16-自动评价
	 * 17-批量评价 18-评价记录 19-中差评查看 20-中差评监控 21-中差评安抚 22-中差评统计 23-中差评原因 24中差评原因设置 "
	 * 25-中差评原因分析 26-手动订单提醒 27-优秀催付案例 28-效果统计 29-买家申请退款 30-退款成功 31-等待退货 32-拒绝退款
	 * 33-会员短信群发 34-指定号码群发 35-订单短信群发 36-会员互动"
	 */

	// 1-下单关怀
	public static final String MSG_XDGH = "1";
	// 2-常规催付
	public static final String MSG_CGCF = "2";
	// 3-二次催付
	public static final String MSG_ECCF = "3";
	// 4-聚划算催付
	public static final String MSG_JHSCF = "4";
	// 5-预售催付
	public static final String MSG_YSCF = "5";
	// 6-发货提醒
	public static final String MSG_FHTX = "6";
	// 7-到达同城提醒
	public static final String MSG_DDTCTX = "7";
	// 8-派件提醒
	public static final String MSG_PJTX = "8";
	// 9-签收提醒
	public static final String MSG_QSTX = "9";
	// 10-疑难件提醒
	public static final String MSG_YNJTX = "10";
	// 11-延时发货提醒
	public static final String MSG_YSFHTX = "11";
	// 12-宝贝关怀
	public static final String MSG_BBGH = "12";
	// 13-付款关怀
	public static final String MSG_FKGH = "13";
	// 14-回款提醒
	public static final String MSG_HKTX = "14";
	// 15-退款关怀
	public static final String MSG_TKGH = "15";
	// 16-自动评价
	public static final String MSG_ZDPJ = "16";
	// 17-批量评价
	public static final String MSG_PLPJ = "17";
	// 18-评价记录
	public static final String MSG_PJJL = "18";
	// 19-中差评查看
	public static final String MSG_ZCPCK = "19";
	// 20-中差评监控
	public static final String MSG_ZCPJK = "20";
	// 21-中差评安抚
	public static final String MSG_ZCPAF = "21";
	// 22-中差评统计
	public static final String MSG_ZCPTJ = "22";
	// 23-中差评原因
	public static final String MSG_ZCPYY = "23";
	// 24中差评原因设置
	public static final String MSG_ZCPYYSZ = "24";
	// 25-中差评原因分析
	public static final String MSG_ZCPYYFX = "25";
	// 26-手动订单提醒
	public static final String MSG_SDDDTX = "26";
	// 27-优秀催付案例
	public static final String MSG_YXCFAL = "27";
	// 28-效果统计
	public static final String MSG_XGTJ = "28";
	// 29-买家申请退款
	public static final String MSG_MJSQTK = "29";
	// 30-退款成功
	public static final String MSG_TKCG = "30";
	// 31-等待退货
	public static final String MSG_DDTH = "31";
	// 32-拒绝退款
	public static final String MSG_JJTK = "32";
	// 33-会员短信群发
	public static final String MSG_HYDXQF = "33";
	// 34-指定号码群发
	public static final String MSG_ZDHMQF = "34";
	// 35-订单短信群发
	public static final String MSG_DDDXQF = "35";
	// 36-会员互动
	public static final String MSG_HYHD = "36";
	// 37-好评提醒
	public static final String MSG_HPTX = "37";

	// 多店铺赠送短信
	public static final String MSG_MULTI_SHOP_SEND = "98";
	// 99-测试发送
	public static final String MSG_CSFS = "99";

	/**
	 * 总记录 SMS_MSGRECORD
	 */
	// 1:全部失败(废弃)
	// public static final String MSG_STATUS_ALLSUCCEED = "1";
	// //2:全部成功(废弃)
	// public static final String MSG_STATUS_ALLFAILED = "2";
	// //3:部分成功(废弃)
	// public static final String MSG_STATUS_PARTSUCCEED = "3";
	// 4:发送中/保存中
	public static final String MSG_STATUS_SENDING = "4";
	// 5:发送完成/保存完成
	public static final String MSG_STATUS_SENDOVER = "5";

	/**
	 * 子记录 SMS_SEND_RECORD
	 */
	// "发送状态 1：发送失败，2：发送成功，3：手机号码不正确，4：号码重复， 5 ：黑名单， 6 ：重复被屏蔽 /重复发送"
	// 1：发送失败
	public static final Integer SMS_STATUS_FAILED = 1;
	// 2：发送成功
	public static final Integer SMS_STATUS_SUCCEED = 2;
	// 3：手机号码不正确，
	public static final Integer SMS_STATUS_WRONGNUM = 3;
	// 4：号码重复
	public static final Integer SMS_STATUS_REPEATNUM = 4;
	// 5 ：黑名单
	public static final Integer SMS_STATUS_BLAKLIST = 5;
	// 6 ：重复被屏蔽 /重复发送
	public static final Integer SMS_STATUS_REPEATESENT = 6;

	/**
	 * 系统发送 SMS_SEND_INFO
	 **/
	// //系统发送的短信类别
	// //1验证码
	// public static final String SYSTEM_SMS_VALIDATECODE = "validate_code";
	// //2催付发送数量，短信发送量和当前短信条数(后台管理)
	// public static final String SYSTEM_SMS_EXPEDITING =
	// "expedite_count_notice";
	// //3 短信余额不足报警：剩余条数不足*条开始提醒
	// public static final String SYSTEM_SMS_SMSCOUNT = "sms_count_notice";
	// //4软件过期提醒
	// public static final String SYSTEM_SMS_SERVICEEXPIRE =
	// "service_expire_notice";
	// //5最新促销活动通知
	// public static final String SYSTEM_SMS_ACTIVITY = "activity_notice";
	//
	// //定义一个发送者
	// public static final String SYSTEM_ROLE_ADMIN = "admin";

}
