package com.kycrm.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;


public class PinYin4jUtil {
	
	//将汉字转换为拼音
	 public static String hanyu2pinyin(String chineseLanguage){
	        
	       String hanyupinyin = "";//接收转换后的拼音
       		try {
	       			HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
	       			defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);// 输出拼音全部小写
	       			defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);// 不带声调
	       			defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
	       			char[] cl_chars = chineseLanguage.replaceAll("\\s*", "").toCharArray();//去掉空格转换为数组
	    	        for (int i=0; i<cl_chars.length; i++){
	    	            if (String.valueOf(cl_chars[i]).matches("[\u4e00-\u9fa5]+")){// 如果字符是中文,则将中文转为汉语拼音
	    	                hanyupinyin += PinyinHelper.toHanyuPinyinStringArray(cl_chars[i], defaultFormat)[0];
	    	            } else {// 如果字符不是中文,则不转换
	    	                hanyupinyin += cl_chars[i];
	    	            }
	    	        }
    	    } catch (Exception e) {
    	    	return chineseLanguage.replaceAll("\\s*", "");
    	    }
			return hanyupinyin;
	    }
}
