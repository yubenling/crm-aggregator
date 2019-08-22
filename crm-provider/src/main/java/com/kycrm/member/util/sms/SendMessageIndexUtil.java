package com.kycrm.member.util.sms;  

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kycrm.member.util.MD5Utils;
import com.kycrm.member.util.MsgUtil;
import com.kycrm.member.util.http.HttpClientUtil;
import com.kycrm.util.DateUtils;

/**
 * 首页红包短信工具类
 * @ClassName: MarketingCenterTestSmsUtil  
 * @author ztk
 * @date 2018年5月30日 下午4:14:50
 */
public class SendMessageIndexUtil {
	
	private static Log logger = LogFactory.getLog(SendMessageIndexUtil.class);

	
	private static String YZM_USERNAME = null;
	private static String YZM_PASSWORD = null;
	public static String URL=null;
	
	static{
		ResourceBundle  resource=ResourceBundle.getBundle("dev");
		YZM_USERNAME= resource.getString("indexUtil.username");
		YZM_PASSWORD= resource.getString("indexUtil.password");
		URL=resource.getString("sms.url");
	}
	//线上账号
	
	/*private final static String YZM_USERNAME = "kyshouye";
	private final static String YZM_PASSWORD = "tu4gxn30";*/
	
	//public static String URL ="http://223.223.187.13:8080/eums/sms/utf8/send.do";
	
	/**
	 * 
	 * sendMessage:(这里用一句话描述这个方法的作用). <br/> 
	 * @author zlp
	 * @param phone     手机号码（多个号码用“逗号”分开，GET方式最多50个号码，POST方式最多1000个号码）
	 * @param content  短信内容  最多500个字符。
	 * @param ext   扩展号码（视通道是否支持扩展，可以为空或不填）
	 * @param reference  参考信息（最多50个字符，在推送状态报告时，推送给合作方，本参数不参与任何下行控制，仅为方便合作方业务处理，可以为空或不填，不能含有半角的逗号和分号
	 * @return
	 */
	public static String sendMessage(String[] phone ,String content,String ext,String reference){
		try {
			if(null!=phone&&phone.length>0&&!"".equals(content)&&null!=content){
				Map<String, Object> sendMap = buildRequestPara(phone,content,ext,reference);
				String response = HttpClientUtil.doPost(URL, sendMap);
				logger.info("resonse"+content);
				if(null!=response&&!"".equals(response)){
					String[] split = response.split(":");
					if("success".equals(split[0])){
						return  MsgUtil.returnSuccess(split[1]);
					}else{
						return  MsgUtil.returnError(split[1], convertMessage(split[1]));
					}
				}
			}else{
				return  MsgUtil.returnError("9999", "手机号或内容为空");
			}
		} catch (Exception e) {
                return  MsgUtil.returnError("9999", "接口异常");
		}
		
	    return  MsgUtil.returnError("9999", "接口调用异常"); 
	}
	public static String sendMessage(String phone ,String content,String ext,String reference){
		 try {
			if(null!=phone&&!"".equals(content)&&null!=content){
				Map<String, Object> sendMap = buildStrRequestPara(phone,content,ext,reference);
				String response = HttpClientUtil.doPost(URL, sendMap);
				if(null!=response&&!"".equals(response)){
					String[] split = response.split(":");
					if("success".equals(split[0])){
						return  MsgUtil.returnSuccess(split[1]);
					}else{
						return  MsgUtil.returnError(split[1], convertMessage(split[1]));
					}
				}
			}else{
				return  MsgUtil.returnError("9999", "手机号或内容为空");
			}
		} catch (Exception e) {
			return  MsgUtil.returnError("9999", "接口异常");
		}
		return  MsgUtil.returnError("9999", "接口调用异常"); 
	}
	private static Map<String, Object> buildRequestPara(String[] phones,String content,String ext,String reference) {
		Map<String, Object> sendMap = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer("");
		for (String str : phones) { sb.append(str); }
		String phone = sb.toString();
		String orderNum = DateUtils.getOrderNum();
		sendMap.put("name",YZM_USERNAME);// 帐号
		sendMap.put("seed",orderNum);//当前时间，格式：YYYYMMDD HHMISS，例如：20130806102030。客户时间早于或晚于网关时间超过30分钟，则网关拒绝提交。
		sendMap.put("key",MD5Utils.sign(MD5Utils.sign(YZM_PASSWORD,"","UTF-8").toLowerCase()+orderNum,"","UTF-8").toLowerCase());//签名
		sendMap.put("dest",phone);// 手机号码（多个号码用“逗号”分开，GET方式最多50个号码，POST方式最多1000个号码）
		sendMap.put("content",content);//  短信内容  最多500个字符。
		sendMap.put("ext",ext);//扩展号码（视通道是否支持扩展，可以为空或不填）
		sendMap.put("reference",reference);// 参考信息（最多50个字符，在推送状态报告时，推送给合作方，本参数不参与任何下行控制，仅为方便合作方业务处理，可以为空或不填，不能含有半角的逗号和分号
		return paraFilter(sendMap) ;
	}
	private static Map<String, Object> buildStrRequestPara(String phone,String content,String ext,String reference) {
		Map<String, Object> sendMap = new HashMap<String, Object>();
		String orderNum = DateUtils.getOrderNum();
		sendMap.put("name",YZM_USERNAME);// 帐号
		sendMap.put("seed",orderNum);//当前时间，格式：YYYYMMDD HHMISS，例如：20130806102030。客户时间早于或晚于网关时间超过30分钟，则网关拒绝提交。
		sendMap.put("key",MD5Utils.sign(MD5Utils.sign(YZM_PASSWORD,"","UTF-8").toLowerCase()+orderNum,"","UTF-8").toLowerCase());//签名
		sendMap.put("dest",phone);// 手机号码（多个号码用“逗号”分开，GET方式最多50个号码，POST方式最多1000个号码）
		sendMap.put("content",content);//  短信内容  最多500个字符。
		sendMap.put("ext",ext);//扩展号码（视通道是否支持扩展，可以为空或不填）
		sendMap.put("reference",reference);// 参考信息（最多50个字符，在推送状态报告时，推送给合作方，本参数不参与任何下行控制，仅为方便合作方业务处理，可以为空或不填，不能含有半角的逗号和分号
		return paraFilter(sendMap) ;
	}
    /** 
     * 除去数组中的空值和签名参数
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    public static Map<String, Object> paraFilter(Map<String, Object> sArray) {
        Map<String, Object> result = new HashMap<String, Object>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }
        for (String key : sArray.keySet()) {
        	Object value = sArray.get(key);
            if (value == null || value.equals("")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }
    
    public static String convertMessage(String msg) {
    	switch (msg){
        case "101": return "缺少name参数";case "102": return "缺少seed参数";
        case "103": return "缺少key参数";case "104": return "缺少dest参数";
        case "105": return "缺少content参数";  case "106": return "seed错误";
        case "107": return "key错误";case "108": return "ext错误";
        case "109": return "内容超长";case "110": return "模板未备案";
        case "111": return "发送内容无签名";case "201": return "无对应账户";
        case "202": return "账户暂停";case "203": return "账户删除";
        case "204": return "账户IP没备案";case "205": return "账户无余额";
        case "206": return "密码错误";case "301": return "无对应产品";
        case "302": return "产品暂停";case "303": return "产品删除";
        case "304": return "产品不在服务时间";case "305": return "无匹配通道";
        case "306": return "通道暂停"; case "307": return "通道已删除";
        case "308": return "通道不在服务时间";case "309": return "未提供短信服务";
        case "310": return "未提供彩信服务";case "401": return "屏蔽词";
        case "500": return "查询间隔太短";case "999": return "其他错误";
        default:
            return "";
    	}
    }
	
}
