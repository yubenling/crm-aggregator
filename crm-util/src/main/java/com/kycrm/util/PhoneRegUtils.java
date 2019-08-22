package com.kycrm.util;

import java.util.regex.Pattern;

/**
* @ClassName: PhoneRegUtils
* @Description: (手机验证工具)
* @author:jackstraw_yu
* @date 2017年2月21日
*
*/
public  class PhoneRegUtils {
	
	//手机号正则
	//private static  Pattern pattern = Pattern.compile("^1[34578]\\d{9}$");  
	//private static  Pattern pattern = Pattern.compile("^(13[0-9]|15[012356789]|17[3678]|18[0-9]|14[57])[0-9]{8}$"); 
	//private static  Pattern pattern = Pattern.compile("^((13[0-9])|(14[5,7])|(15[^4,\\D])|(17[6-8])|(18[0-9]))\\d{8}$");
	private static  Pattern pattern = Pattern.compile("^(13[0-9]|14[5,7]|15[0-3,5-9]|166|17[3678]|18[0-9]|19[8,9])\\d{8}$");
	//private static Pattern pattern = Pattern.compile("^(13[0-9][0-9]|14[157][0-9]|15[012356789][0-9]|17[1378][0-9]|17[0][0-9]|18[0-9][0-9]|14[57])\\d{7}$");
	
	
	/** 
	* @Description 手机号验证
	* @param  mobile
	* @return boolean    返回类型 
	* @author jackstraw_yu
	* @date 2018年3月5日 下午5:47:58
	*/
	public static boolean phoneValidate(String mobile){
		if(mobile==null || "".equals(mobile.trim())) return false;
		return pattern.matcher(mobile.trim()).matches();
	}
	
	/*public static void main(String[] args) {
		String mobile = "16637686800";
		System.out.println(phoneValidate(mobile));
		String mobile2 = "14837686800";
		System.out.println(phoneValidate(mobile2));
		String mobile3 = "19937686800";
		System.out.println(phoneValidate(mobile3)); 
		String mobile4 = "19837686800";
		System.out.println(phoneValidate(mobile4));
		String mobile5 = "19537686800";
		System.out.println(phoneValidate(mobile5));
		String mobile6 = "15537686800";
		System.out.println(phoneValidate(mobile6));
		String mobile7 = null;
		System.out.println(phoneValidate(mobile7));
	}*/

}
