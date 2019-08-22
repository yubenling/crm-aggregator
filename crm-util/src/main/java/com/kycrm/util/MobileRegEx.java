package com.kycrm.util;

import java.util.regex.Pattern;

/** 
* @ClassName: MobileRegEx 
* @Description 手机正则
* @author jackstraw_yu
* @date 2018年1月19日 下午6:31:40 
*/
public final class MobileRegEx {

	private MobileRegEx(){}
	
	//手机号正则
	//private static  Pattern pattern = Pattern.compile("^1[34578]\\d{9}$");  
	private static  Pattern pattern = Pattern.compile("^(1[3-9][0-9])[0-9]{8}$"); 
	//private static Pattern pattern = Pattern.compile("^(13[0-9][0-9]|14[157][0-9]|15[012356789][0-9]|17[0][0-9]|17[1378][0-9]|18[0-9][0-9])\\d{8}$");
	
	/** 
	* @Description 手机号正则 
	* @param  num
	* @return boolean    返回类型 
	* @author jackstraw_yu
	* @date 2018年1月19日 下午6:27:18
	*/
	public static boolean validateMobile(String mobile){
		return pattern.matcher(mobile.trim()).matches();
	}
	
}
