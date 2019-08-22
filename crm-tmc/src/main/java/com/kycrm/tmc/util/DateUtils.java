package com.kycrm.tmc.util;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class DateUtils {

    public final static String DEFAULT_TIMEZONE = "GMT+8";

    public final static String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

    public final static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    public final static String SHORT_TIME_FORMAT = "yyyy-MM-dd HH:mm";

    public final static String FULL_SEQ_FORMAT = "yyyyMMddHHmmssSSS";

    public final static String[] MULTI_FORMAT = { DEFAULT_DATE_FORMAT, ISO_DATE_TIME_FORMAT, DEFAULT_TIME_FORMAT, SHORT_TIME_FORMAT, "yyyy-MM" };

    public final static String FORMAT_YYYY = "yyyy";

    public final static String FORMAT_YYYYMM = "yyyyMM";

    public final static String FORMAT_YYYYMMDD = "yyyyMMdd";

    public final static String FORMAT_YYYYMMDDHH = "yyyyMMddHH";
    
    public final static String FORMAT_YYYYMMDDHHMMSS = "yyyyMMddHHmmss";

    public static String formatDate(Date date) {
        if (date == null) {
            return "";
        }
        return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(date);
    }

    public static String formatDate(Date date, String format) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(format).format(date);
    }

    public static Integer formatDateToInt(Date date, String format) {
        if (date == null) {
            return null;
        }
        return Integer.valueOf(new SimpleDateFormat(format).format(date));
    }

    public static Long formatDateToLong(Date date, String format) {
        if (date == null) {
            return null;
        }
        return Long.valueOf(new SimpleDateFormat(format).format(date));
    }
    
    public static Long dateToLong(Date date) {
        if (date == null) {
            return null;
        }
        return date.getTime();
    }
    

    public static String formatTime(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(DEFAULT_TIME_FORMAT).format(date);
    }

    public static String formatShortTime(Date date) {
        if (date == null) {
            return null;
        }
        return new SimpleDateFormat(SHORT_TIME_FORMAT).format(date);
    }


    public static Date parseDate(String date, String format) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseTime(String date, String format) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(format).parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static Date parseDate(String date) {
        return parseDate(date, DEFAULT_DATE_FORMAT);
    }

    public static Date parseTime(String date) {
        return parseTime(date, DEFAULT_TIME_FORMAT);
    }


    public static String getHumanDisplayForTimediff(Long diffMillis) {
        if (diffMillis == null) {
            return "";
        }
        long day = diffMillis / (24 * 60 * 60 * 1000);
        long hour = (diffMillis / (60 * 60 * 1000) - day * 24);
        long min = ((diffMillis / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long se = (diffMillis / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        StringBuilder sb = new StringBuilder();
        if (day > 0) {
            sb.append(day + "D");
        }
        DecimalFormat df = new DecimalFormat("00");
        sb.append(df.format(hour) + ":");
        sb.append(df.format(min) + ":");
        sb.append(df.format(se));
        return sb.toString();
    }

    /**
     * 把类似2014-01-01 ~ 2014-01-30格式的单一字符串转换为两个元素数组
     */
    public static Date[] parseBetweenDates(String date) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        date = date.replace("～", "~");
        Date[] dates = new Date[2];
        String[] values = date.split("~");
        dates[0] = parseMultiFormatDate(values[0].trim());
        dates[1] = parseMultiFormatDate(values[1].trim());
        return dates;
    }

    public static Date parseMultiFormatDate(String date) {
        try {
            return org.apache.commons.lang3.time.DateUtils.parseDate(date, MULTI_FORMAT);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *@Title:getDiffDay
     *@Description:获取日期相差天数
     *@param:@param beginDate  字符串类型开始日期
     *@param:@param endDate    字符串类型结束日期
     *@param:@return
     *@return:Long             日期相差天数
     *@author:谢
     *@thorws:
     */
    public static Long getDiffDay(String beginDate, String endDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Long checkday = 0L;
        //开始结束相差天数
        try {
            checkday = (formatter.parse(endDate).getTime() - formatter.parse(beginDate).getTime()) / (1000 * 24 * 60 * 60);
        } catch (ParseException e) {

            e.printStackTrace();
            checkday = null;
        }
        return checkday;
    }

    /**
    *@Title:getDiffDay
    *@Description:获取日期相差天数
    *@param:@param beginDate Date类型开始日期
    *@param:@param endDate   Date类型结束日期
    *@param:@return
    *@return:Long            相差天数
    *@author: 谢
    *@thorws:
    */
    public static Long getDiffDay(Date beginDate, Date endDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strBeginDate = format.format(beginDate);

        String strEndDate = format.format(endDate);
        return getDiffDay(strBeginDate, strEndDate);
    }

    /**
     * N天之后
     * @param n
     * @param date
     * @return
     */
    public static Date nDaysAfter(Integer n, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + n);
        return cal.getTime();
    }

    /**
     * N天之前
     * @param n
     * @param date
     * @return
     */
    public static Date nDaysAgo(Integer n, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - n);
        return cal.getTime();
    }

    public static Integer getWeekOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return Integer.valueOf(formatDate(date, FORMAT_YYYY) + c.get(Calendar.WEEK_OF_YEAR));
    }
    
    /**
	 * 增加小时
	 * @param date
	 * @param add
	 * @return
	 */
	public static Date addHour(Date date,int add){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, add);
		return cal.getTime();
	}
	/**
	 * 
	 * @param sourceDate
	 * @param days
	 * @return
	 * @description
	 * 加天
	 */
	public static Date addDate(Date sourceDate, int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sourceDate);
		calendar.add(Calendar.DATE, days);
		return calendar.getTime();
	}
	
	/**
	 * 
	 * @param sourceDate
	 * @param months
	 * @return
	 * @description
	 * 加月
	 */
	public static Date addMonth(Date sourceDate,int months)
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sourceDate);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}
	
	
	/**
	 * 
	 * @param sourceDate
	 * @param months
	 * @return
	 * @description
	 * 加分钟
	 */
	public static Date addMinute(Date sourceDate,int minute){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sourceDate);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}
	
	
    public static String dateToString(Date data, String formatType) {  
        return new SimpleDateFormat(formatType).format(data);  
    }  
   
    // long类型转换为String类型  
    // currentTime要转换的long类型的时间  
    // formatType要转换的string类型的时间格式  
    public static String longToString(long currentTime, String formatType)  
            throws ParseException {  
        Date date = longToDate(currentTime, formatType); // long类型转成Date类型  
        String strTime = dateToString(date, formatType); // date类型转成String  
        return strTime;  
    }  
   
    // string类型转换为date类型  
    // strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日  
    // HH时mm分ss秒，  
    // strTime的时间格式必须要与formatType的时间格式相同  
    public static Date stringToDate(String strTime, String formatType)  
            throws ParseException {  
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);  
        Date date = null;  
        date = formatter.parse(strTime);  
        return date;  
    }  
   
    // long转换为Date类型  
    // currentTime要转换的long类型的时间  
    // formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒  
    public static Date longToDate(long currentTime, String formatType)  
            throws ParseException {  
        Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间  
        String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string  
        Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型  
        return date;  
    }  
   
    // string类型转换为long类型  
    // strTime要转换的String类型的时间  
    // formatType时间格式  
    // strTime的时间格式和formatType的时间格式必须相同  
    public static long stringToLong(String strTime, String formatType)  
            throws ParseException {  
        Date date = stringToDate(strTime, formatType); // String类型转成date类型  
        if (date == null) {  
            return 0;  
        } else {  
            long currentTime = dateToLong(date); // date类型转成long类型  
            return currentTime;  
        }  
    }
    
    
    /**
	 * @Description:返回距今指定天数的开始时间结束时间
	 * @author jackstraw_yu
	 * eg:Sun Jul 09 00:00:00 CST 2017
	 *	  Sun Jul 09 23:59:59 CST 2017
	 */
    public static Map<String,Date> caculateDate(int day){
		Map<String, Date> map = new HashMap<String,Date>(5);
		Calendar cal  = new GregorianCalendar();
		cal.add(Calendar.DATE, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date minTime = cal.getTime();
		map.put("minTime",minTime);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date maxTime = cal.getTime();
		map.put("maxTime",maxTime);
		return map;
	}
	
	
	/**
	 * @Description:返回指定相对时间(某天)的开始与结束的绝对时间
	 * @author jackstraw_yu
	 * eg:"2017-07-07"-->"2017-07-07 23:59:59"
	 */
	public static Date composeDateTime(String time,boolean flag){
		Date date = null;
		if(time==null || "".equals(time)) {
            return date;
        }
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if(flag){
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
		}else{
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
		}
		date = cal.getTime();
		return date;
	}
    
	/**
	 * @Description:返回指定相对时间(某天)的开始与结束的绝对时间
	 * @author jackstraw_yu
	 * eg:"2017-07-07"-->"2017-07-07 23:59:59"
	 */
	public static Date composeDateTime(Date time,boolean flag){
		Date date = null;
		if(time==null || "".equals(time)) {
            return date;
        }
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		if(flag){
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
		}else{
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
		}
		date = cal.getTime();
		return date;
	}
    
	/**
	 * @Description:现在到今天结束的毫秒值
	 * @author jackstraw_yu
	 */
	public static long getMillisOverToday(){
		Calendar calendar = Calendar.getInstance();
		long begin = calendar.getTime().getTime();
		calendar.add(Calendar.DATE, +1);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		long end = 0;
		try {
			end = df.parse(df.format(calendar.getTime())).getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return end-begin;
	}
	
	/**
	 * 获得本月第一天0点时间
	 * ZTK2017年7月24日下午5:16:24
	 */
	public static Date getTimesMonthmorning() {
	    Calendar cal = Calendar.getInstance();
	    cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
	    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
	    return  cal.getTime();
	}
	
	/**
     * 获得某天0点
     * ZTK2017年7月3日下午3:15:38
     */
    public static Date getStartTimeOfDay(Date date){
    	Calendar day = Calendar.getInstance();
    	day.setTime(date);
    	day.set(Calendar.HOUR_OF_DAY,0);
    	day.set(Calendar.MINUTE, 0);
    	day.set(Calendar.SECOND, 0);
    	day.set(Calendar.MILLISECOND, 0);
    	return day.getTime();
    }
    
    /**
     * 获得某天23:59:59:000点
     * ZTK2017年7月3日下午3:15:38
     */
    public static Date getEndTimeOfDay(Date date){
    	Calendar day = Calendar.getInstance();
    	day.setTime(date);
    	day.set(Calendar.HOUR_OF_DAY,23);
    	day.set(Calendar.MINUTE, 59);
    	day.set(Calendar.SECOND, 59);
    	day.set(Calendar.MILLISECOND, 0);
    	return day.getTime();
    }
}
