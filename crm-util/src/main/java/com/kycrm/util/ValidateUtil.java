package com.kycrm.util;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** 
* @author wy
* @version 创建时间：2017年7月27日 上午10:33:06
*/
public class ValidateUtil {
	private ValidateUtil(){}
	/**
	 * 判断字符串是否为空
	 * @author: wy
	 * @time: 2017年7月27日 上午10:35:08
	 */
	public static boolean isEmpty(String str){
		if ( str==null || "".equals(str) || "null".equalsIgnoreCase(str))
			return true;
		return false;
	}
	/**
	 * 判断字符串不为空
	 * @author: wy
	 * @time: 2017年7月27日 上午10:35:23
	 */
	public static boolean isNotNull(String str){
		return !isEmpty(str);
	}
	/**
	 * 判断double是否为空
	 * @author: wy
	 * @time: 2017年7月27日 上午10:37:24
	 */
	public static boolean isEmpty(Double d){
		if(d==null || d.doubleValue() == 0.0)
			return true;
		return false;
	}
	/**
	 * 判断double不为空
	 * @author: wy
	 * @time: 2017年7月27日 上午11:28:13
	 */
	public static boolean isNotNull(Double d){
		return !isEmpty(d);
	}
	/**
	 * 判断Integer是否为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:07:48
	 */
	public static boolean isEmpty(Integer i){
		if(i == null || i.intValue() == 0)
			return true;
		return false;
	}
	/**
	 * 判断Integer不为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:08:05
	 */
	public static boolean isNotNull(Integer i){
		return !isEmpty(i);
	}
	/**
	 * 判断Long是否为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:08:20
	 */
	public static boolean isEmpty(Long l){
		if(l == null || l.longValue()== 0L)
			return true;
		return false;
	}
	/**
	 * 判断Long不为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:08:35
	 */
	public static boolean isNotNull(Long l){
		return !isEmpty(l);
	}
	/**
	 * 判断List是否为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:08:49
	 */
	public static boolean isEmpty(List<?> list){
		if(list == null || list.size() == 0)
			return true;
		return false;
	}
	/**
	 * 判断List不为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:09:01
	 */
	public static boolean isNotNull(List<?> list){
		return !isEmpty(list);
	}
	/**
	 * 判断Set是否为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:09:13
	 */
	public static boolean isEmpty(Set<?> set){
		if(set == null || set.size() == 0)
			return true;
		return false;
	}
	/**
	 * 判断Set不为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:09:26
	 */
	public static boolean isNotNull(Set<?> set){
		return !isEmpty(set);
	}
	/**
	 * 判断map是否为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:09:45
	 */
	public static boolean isEmpty(Map<?,?> map){
		if(map == null || map.size() == 0 || map.keySet().size()==0)
			return true;
		return false;
	}
	/**
	 * 判断map不为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:10:00
	 */
	public static boolean isNotNull(Map<?,?> map){
		return !isEmpty(map);
	}
	/**
	 * 判断StringBuffer是否为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:10:15
	 */
	public static boolean isEmpty(StringBuffer str) {
		if (str == null || str.length() <= 0)
			return true;
		return false;
	}
	/**
	 * 判断StringBuffer不为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:10:28
	 */
	public static boolean isNotNull(StringBuffer str){
		return !isEmpty(str);
	}
	/**
	 * 判断byte数字是否空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:10:41
	 */
	public static boolean isEmpty(byte [] b) {
		if (b == null || b.length <= 0)
			return true;
		return false;
	}
	/**
	 * 判断byte数组不为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:10:57
	 */
	public static boolean isNotNull(byte[] b){
		return !isEmpty(b);
	}
	/**
	 * 判断Object是否为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:11:10
	 */
	public static boolean isEmpty(Object obj){
		if(obj == null)
			return true;
		if(obj instanceof String)
			return isEmpty((String)obj);
		if(obj instanceof Double)
			return isEmpty((Double)obj);
		if(obj instanceof Integer)
			return isEmpty((Integer)obj);
		if(obj instanceof Long)
			return isEmpty((Long)obj);
		if(obj instanceof Map)
			return isEmpty((Map<?,?>)obj);
		if(obj instanceof Set)
			return isEmpty((Set<?>)obj);
		if(obj instanceof List)
			return isEmpty((List<?>)obj);
		return true;
	}
	/**
	 * 判断Object不为空
	 * @author: wy
	 * @time: 2017年7月27日 下午3:11:28
	 */
	public static boolean isNotNull(Object obj){
		return !isEmpty(obj);
	}
	/**
	 * String转Long
	 * @author: wy
	 * @time: 2017年7月27日 下午3:11:43
	 */
	public static Long getLong(String str){
		if(!isIntegerOrLong(str))
			return null;
		return Long.parseLong(str);
	}
	/**
	 * String转Integer
	 * @author: wy
	 * @time: 2017年7月27日 下午3:11:59
	 */
	public static Integer getInteger(String str){
		if(!isIntegerOrLong(str))
			return null;
		return Integer.parseInt(str);
	}
	/**
	 * String转Double
	 * @author: wy
	 * @time: 2017年7月27日 下午3:12:13
	 */
	public static Double getDouble(String str){
		if(!isDouble(str))
			return null;
		return Double.parseDouble(str);
	}
	/**
	 * String转BigDecimal
	 * @author: wy
	 * @time: 2017年7月27日 下午3:12:24
	 */
	public static BigDecimal getBigDecimal(String str){
		if(!isDouble(str))
			return null;
		return new BigDecimal(str);
	}
	/**
	 * 根据出生日期算出年龄
	 * @author: wy
	 * @time: 2017年7月27日 下午3:15:54
	 * @param birthDate 出生日期
	 */
	public static int getAge(Date birthDate){
		if (birthDate == null)
		   throw new RuntimeException("出生日期不能为null");
		int age = 0;
		Date now = new Date();
		SimpleDateFormat format_y = new SimpleDateFormat("yyyy");
		SimpleDateFormat format_M = new SimpleDateFormat("MM");
		String birth_year =format_y.format(birthDate);
		String this_year =format_y.format(now);
		String birth_month =format_M.format(birthDate);
		String this_month =format_M.format(now);
		// 初步，估算
		age = Integer.parseInt(this_year) - Integer.parseInt(birth_year);
		// 如果未到出生月份，则age - 1
		if (this_month.compareTo(birth_month) < 0)
		   age -= 1;
		if (age <0)
		   age = 0;
		return age;
	}
	/**
	 * 获取系统操作时间
	 * @param
	 * @return String
	 */
	public static String getSysOptDate() {
		Calendar date = Calendar.getInstance();
		Date sysDate = date.getTime();
		String optDate = dateToString(sysDate, "yyyy-MM-dd HH:mm:ss");
		return optDate;
	}
	/**
	 * 字符串转换为Date类型
	 * @param dteValue 日期对象
	 * @param strFormat 要转型的格式 yyyy-MM-dd HH:mm:ss SSS
	 * @return Sring 对应的日期字符串或者null
	 */
	public static String dateToString(Date dteValue, String strFormat) {
		if (isEmpty(dteValue) || isEmpty(strFormat))
			return null;
		SimpleDateFormat clsFormat = new SimpleDateFormat(strFormat);
		return clsFormat.format(dteValue);
	}
	/**
	 * 字符串转日期
	 * @author: wy
	 * @time: 2017年7月27日 下午3:25:30
	 * @param dateValue 日期字符串
	 * @param format 要转型的格式 yyyy-MM-dd HH:mm:ss SSS
	 * @return 日期类型
	 * @throws ParseException 转型异常
	 */
	public static Date stringToDate(String dateValue,String format) throws ParseException{
		if(isEmpty(dateValue) || isEmpty(format))
			return null;
		SimpleDateFormat clsFormat = new SimpleDateFormat(format);
		return clsFormat.parse(dateValue);
	}
	/**
	 * 判断字符串是否为数字且为整型
	 * @author: wy
	 * @time: 2017年7月27日 下午3:12:40
	 */
	public static boolean isIntegerOrLong(String str){
//		if(isEmpty(str))
//			return false;
//		return str.matches("\\d+");
		if (isEmpty(str)) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
	}
	/**
	 * 判断字符串是否为数字且为小数
	 * @author: wy
	 * @time: 2017年7月27日 下午3:13:12
	 */
	public static boolean isDouble(String str){
		if(isEmpty(str))
			return false;
		return str.matches("\\d+(\\.\\d+)?");
	}
}
