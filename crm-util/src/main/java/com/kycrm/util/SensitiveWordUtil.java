package com.kycrm.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SensitiveWordUtil {

	private static final String regulation = "\\d+(\\.\\d+)?";//数字正则表达式
	
	private static final String number = "**";//**代表数字

	public static List<String> contentVerify(String content,String antistop) {
		List<String> list = new ArrayList<>();
		String regex = antistop.replace(number, regulation);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);  
        while (matcher.find()) {
        	list.add(matcher.group());
        }  
        return list;  
    }  
}
