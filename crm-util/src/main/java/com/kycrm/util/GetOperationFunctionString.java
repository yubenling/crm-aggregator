package com.kycrm.util;

/**
 * 取得类型所对应的中文字符
 * @author zhrt2
 *
 */
public class GetOperationFunctionString {
	private GetOperationFunctionString(){};
	/**
	 * 根据类型取得对应的文字信息
	 * @param type
	 * @return
	 */
	public static String getFunctionsByType(String type){
		if(type==null){
			return "未知操作";
		}
		String str = null;
		switch (type) {
		case "1":{
			str = "下单关怀";
			break;
		}
		case "2":{
			str = "常规催付";
			break;
		}
		case "3":{
			str = "二次催付";
			break;
		}
		case "4":{
			str = "聚划算催付";
			break;
		}
		case "5":{
			str = "预售催付";
			break;
		}
		case "6":{
			str = "发货提醒";
			break;
		}
		case "7":{
			str = "到达同城提醒";
			break;
		}
		case "8":{
			str = "派件提醒";
			break;
		}
		case "9":{
			str = "签收提醒";
			break;
		}
		case "10":{
			str = "疑难件提醒";
			break;
		}
		case "11":{
			str = "延时发货提醒";
			break;
		}
		case "12":{
			str = "宝贝关怀";
			break;
		}
		case "13":{
			str = "付款关怀";
			break;
		}
		case "14":{
			str = "回款提醒";
			break;
		}
		case "15":{
			str = "退款关怀";
			break;
		}
		case "16":{
			str = "自动评价";
			break;
		}
		case "17":{
			str = "批量评价";
			break;
		}
		case "18":{
			str = "评价记录";
			break;
		}
		case "19":{
			str = "中差管理";
			break;
		}
		case "20":{
			str = "中差评监控";
			break;
		}
		case "21":{
			str = "中差评安抚";
			break;
		}
		case "22":{
			str = "中差评统计";
			break;
		}
		case "23":{
			str = "中差评原因";
			break;
		}
		case "24":{
			str = "中差评原因设置";
			break;
		}
		case "25":{
			str = "中差评原因分析";
			break;
		}
		case "26":{
			str = "手动订单提醒";
			break;
		}
		case "27":{
			str = "优秀催付案例";
			break;
		}
		case "28":{
			str = "效果统计";
			break;
		}
		case "29":{
			str = "买家申请退款";
			break;
		}
		case "30":{
			str = "退款成功";
			break;
		}
		case "31":{
			str = "等待退货";
			break;
		}
		case "32":{
			str = "拒绝退款";
			break;
		}
		case "33":{
			str = "黑名单管理";
			break;
		}
		default:
			str = "未知操作 ";
			break;
		}
		return str;
	}
}
