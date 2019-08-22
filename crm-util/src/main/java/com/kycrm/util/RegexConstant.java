package com.kycrm.util;

public final class RegexConstant {

	//匹配整数
	public static final String INTEGER = "\\d+";
	
	//匹配整数和小数（保留小数点后两位）
	public static final String INTEGER_OR_DECIMAL = "^\\d+(\\.\\d{1,2})?$";
	
	//匹配时分秒
	public static final String TIME = "^(([0-1]{1}[0-9]{1})|2[0-3])(:[0-5]{1}[0-9]{1}){2}$";
	
	public static void main(String[] args) {
		
	}
}
