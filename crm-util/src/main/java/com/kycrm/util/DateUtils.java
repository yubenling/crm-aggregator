package com.kycrm.util;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

public class DateUtils {

	public final static String DEFAULT_TIMEZONE = "GMT+8";

	public final static String ISO_DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS";

	public final static String DEFAULT_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

	public final static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

	public final static String SHORT_TIME_FORMAT = "yyyy-MM-dd HH:mm";

	public final static String SHORT_HOUR_FORMAT = "HH:mm:ss";

	public final static String FULL_SEQ_FORMAT = "yyyyMMddHHmmssSSS";

	public final static String[] MULTI_FORMAT = { DEFAULT_DATE_FORMAT, ISO_DATE_TIME_FORMAT, DEFAULT_TIME_FORMAT,
			SHORT_TIME_FORMAT, "yyyy-MM" };

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

	public static String plusOneDay(String date) {
		DateTime dateTime = new DateTime(parseDate(date).getTime());
		return formatDate(dateTime.plusDays(1).toDate());
	}

	public static String plusOneDay(Date date) {
		DateTime dateTime = new DateTime(date.getTime());
		return formatDate(dateTime.plusDays(1).toDate());
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
	 * @Title:getDiffDay
	 * @Description:获取日期相差天数
	 * @param:@param beginDate
	 *                   字符串类型开始日期
	 * @param:@param endDate
	 *                   字符串类型结束日期
	 * @param:@return
	 * @return:Long 日期相差天数
	 * @author:谢
	 * @thorws:
	 */
	public static Long getDiffDay(String beginDate, String endDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		Long checkday = 0l;
		// 开始结束相差天数
		try {
			checkday = (formatter.parse(endDate).getTime() - formatter.parse(beginDate).getTime())
					/ (1000 * 24 * 60 * 60);
		} catch (ParseException e) {

			e.printStackTrace();
			checkday = null;
		}
		return checkday;
	}

	/**
	 * @Title:getDiffDay
	 * @Description:获取日期相差天数
	 * @param:@param beginDate
	 *                   Date类型开始日期
	 * @param:@param endDate
	 *                   Date类型结束日期
	 * @param:@return
	 * @return:Long 相差天数
	 * @author: 谢
	 * @thorws:
	 */
	public static Long getDiffDay(Date beginDate, Date endDate) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String strBeginDate = format.format(beginDate);

		String strEndDate = format.format(endDate);
		return getDiffDay(strBeginDate, strEndDate);
	}

	/**
	 * N天之后
	 * 
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
	 * 
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

	public static Date nDaysStartTime(Integer n, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - n);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date nDaysEndTime(Integer n, Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - n);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}

	public static Integer getWeekOfYear(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return Integer.valueOf(formatDate(date, FORMAT_YYYY) + c.get(Calendar.WEEK_OF_YEAR));
	}

	/**
	 * 增加小时
	 * 
	 * @param date
	 * @param add
	 * @return
	 */
	public static Date addHour(Date date, int add) {
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
	 * @description 加天
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
	 * @description 加月
	 */
	public static Date addMonth(Date sourceDate, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sourceDate);
		calendar.add(Calendar.MONTH, months);
		return calendar.getTime();
	}
	
	public static Date addYear(Date sourceDate, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sourceDate);
		calendar.add(Calendar.YEAR, year);
		return calendar.getTime();
	}

	/**
	 * 
	 * @param sourceDate
	 * @param months
	 * @return
	 * @description 加分钟
	 */
	public static Date addMinute(Date sourceDate, int minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sourceDate);
		calendar.add(Calendar.MINUTE, minute);
		return calendar.getTime();
	}

	/**
	 * 加秒
	 * 
	 * @param sourceDate
	 * @param second
	 * @return
	 */
	public static Date addSecond(Date sourceDate, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sourceDate);
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}

	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}

	// long类型转换为String类型
	// currentTime要转换的long类型的时间
	// formatType要转换的string类型的时间格式
	public static String longToString(long currentTime, String formatType) throws ParseException {
		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
		String strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}

	// string类型转换为date类型
	// strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日
	// HH时mm分ss秒，
	// strTime的时间格式必须要与formatType的时间格式相同
	public static Date stringToDate(String strTime, String formatType) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		date = formatter.parse(strTime);
		return date;
	}

	// long转换为Date类型
	// currentTime要转换的long类型的时间
	// formatType要转换的时间格式yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	public static Date longToDate(long currentTime, String formatType) throws ParseException {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}

	// string类型转换为long类型
	// strTime要转换的String类型的时间
	// formatType时间格式
	// strTime的时间格式和formatType的时间格式必须相同
	public static long stringToLong(String strTime, String formatType) throws ParseException {
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
	 * @author jackstraw_yu eg:Sun Jul 09 00:00:00 CST 2017 Sun Jul 09 23:59:59
	 *         CST 2017
	 */
	public static Map<String, Date> caculateDate(int day) {
		Map<String, Date> map = new HashMap<String, Date>();
		Calendar cal = new GregorianCalendar();
		cal.add(Calendar.DATE, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		Date minTime = cal.getTime();
		map.put("minTime", minTime);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		Date maxTime = cal.getTime();
		map.put("maxTime", maxTime);
		return map;
	}

	/**
	 * @Description:返回指定相对时间(某天)的开始与结束的绝对时间
	 * @author jackstraw_yu eg:"2017-07-07"-->"2017-07-07 23:59:59"
	 */
	public static Date composeDateTime(String time, boolean flag) {
		Date date = null;
		if (time == null || "".equals(time))
			return date;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			return date;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		if (flag) {
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
		} else {
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			cal.set(Calendar.SECOND, 59);
		}
		date = cal.getTime();
		return date;
	}

	/**
	 * @Description:返回指定相对时间(某天)的开始与结束的绝对时间
	 * @author jackstraw_yu eg:"2017-07-07"-->"2017-07-07 23:59:59"
	 */
	public static Date composeDateTime(Date time, boolean flag) {
		Date date = null;
		if (time == null || "".equals(time))
			return date;
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		if (flag) {
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
		} else {
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
	public static long getMillisOverToday() {
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
		return end - begin;
	}

	/**
	 * 获得本月第一天0点时间 ZTK2017年7月24日下午5:16:24
	 */
	public static Date getTimesMonthmorning() {
		Calendar cal = Calendar.getInstance();
		cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	/**
	 * 获得某天0点 ZTK2017年7月3日下午3:15:38
	 */
	public static Date getStartTimeOfDay(Date date) {
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		day.set(Calendar.HOUR_OF_DAY, 0);
		day.set(Calendar.MINUTE, 0);
		day.set(Calendar.SECOND, 0);
		day.set(Calendar.MILLISECOND, 0);
		return day.getTime();
	}

	/**
	 * 获得某天23:59:59:000点 ZTK2017年7月3日下午3:15:38
	 */
	public static Date getEndTimeOfDay(Date date) {
		Calendar day = Calendar.getInstance();
		day.setTime(date);
		day.set(Calendar.HOUR_OF_DAY, 23);
		day.set(Calendar.MINUTE, 59);
		day.set(Calendar.SECOND, 59);
		day.set(Calendar.MILLISECOND, 0);
		return day.getTime();
	}

	// 精确的时间格式化
	public static final String DETAIL_DATE_FORMAT = "yyyyMMddHHmmss";

	/**
	 * 返回系统当前时间(精确到毫秒),作为一个唯一的订单编号
	 * 
	 * @return 以yyyyMMddHHmmss为格式的当前系统时间
	 */
	public static String getOrderNum() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat(DETAIL_DATE_FORMAT);
		return df.format(date);
	}

	/**
	 * 获得某个时间几个月后或几个月前的时间(返回的是字符串型的时间，输入的是String day, int x)
	 * 
	 * @param day
	 *            当前时间字符串类型
	 * @param x
	 *            正数为x个月之后，负数为x个月之前
	 * @return
	 */
	public static String getTimeByMoth(String day, int x) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
		// 引号里面个格式也可以是 HH:mm:ss或者HH:mm等等，很随意的，不过在主函数调用时，要和输入的变量day格式一致

		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, x);// 24小时制
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}

	/**
	 * 获得某个时间几分钟后或几分钟前的时间(返回的是字符串型的时间，输入的是String day, int x)
	 * 
	 * @param day
	 * @param x
	 * @return
	 */
	public static String getTimeByMinut(String day, int x) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
		// 引号里面个格式也可以是 HH:mm:ss或者HH:mm等等，很随意的，不过在主函数调用时，要和输入的变量day格式一致

		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, x);// 24小时制
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}

	/**
	 * 获得某个时间几小时后或几小时前的时间(返回的是字符串型的时间，输入的是String day, int x)
	 * 
	 * @param day
	 * @param x
	 * @return
	 */
	public static String getTimeByHour(String day, int x) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
		// 引号里面个格式也可以是 HH:mm:ss或者HH:mm等等，很随意的，不过在主函数调用时，要和输入的变量day格式一致

		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, x);// 24小时制
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}

	/**
	 * 获得某个时间几天后或几天前的时间(返回的是字符串型的时间，输入的是String day, int x)
	 * 
	 * @param day
	 * @param x
	 * @return
	 */
	public static String getTimeByDay(String day, int x) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
		// 引号里面个格式也可以是 HH:mm:ss或者HH:mm等等，很随意的，不过在主函数调用时，要和输入的变量day格式一致

		Date date = null;
		try {
			date = format.parse(day);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (date == null)
			return "";
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, x);// 24小时制
		date = cal.getTime();
		cal = null;
		return format.format(date);
	}

	/**
	 * 获得某个时间几天后或几天前的时间(返回的是字符串型的时间，输入的是Date day, int x)
	 * 
	 * @param day
	 * @param x
	 * @return
	 */
	public static Date getTimeByDay(Date day, int x) {
		if (day == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(day);
		cal.add(Calendar.DATE, x);// 24小时制
		day = cal.getTime();
		cal = null;
		return day;
	}

	/**
	 * 定义一个日期的工具转换类("yyyy-MM-dd HH:mm:ss")
	 * 
	 * @param time
	 * @return
	 */
	public static Date convertDate(String time) {

		Date Ctime = null;// 结束时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (time != null && !"".equals(time)) {
			try {
				Ctime = dateFormat.parse(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return Ctime;

	}

	/**
	 * 获取现在时间
	 * 
	 * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
	 */
	public static Date getNowDate() {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(currentTime);
		Date parse = null;
		try {
			parse = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return parse;
	}

	/**
	 * 将yyyy-MM-dd格式的string转成date
	 * 
	 * @param date
	 * @return
	 */
	public static Date convertStringToDate(String date) {
		Date d = null;

		if (date != null && !date.equals("")) {
			try {
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
				d = f.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	/**
	 * 将yyyy-MM-dd格式的string转成date
	 * 
	 * @param date
	 * @return
	 */
	public static Date convertStringToDateNoSS(String date) {
		Date d = null;

		if (date != null && !date.equals("")) {
			try {
				SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				d = f.parse(date);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return d;
	}

	/**
	 * 判断当前时间是否在某个时间段
	 * 
	 * @param strDateBegin
	 * @param strDateEnd
	 * @return
	 */
	public static boolean isInDate(String strDateBegin, String strDateEnd) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
		String strDate = sdf.format(new Date());// 当前时间
		// 截取当前时间时分秒 转成整型
		int tempDate = Integer
				.parseInt(strDate.substring(11, 13) + strDate.substring(14, 16) + strDate.substring(17, 19));
		// 截取开始时间时分秒 转成整型
		int tempDateBegin = Integer
				.parseInt(strDateBegin.substring(0, 2) + strDateBegin.substring(3, 5) + strDateBegin.substring(6, 8));
		// 截取结束时间时分秒 转成整型
		int tempDateEnd = Integer
				.parseInt(strDateEnd.substring(0, 2) + strDateEnd.substring(3, 5) + strDateEnd.substring(6, 8));

		if ((tempDate >= tempDateBegin && tempDate <= tempDateEnd)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 时间转成string类型(yyyy-MM-dd HH:mm:ss) ZTK2017年7月14日上午11:59:44
	 */
	public static String dateToStringHMS(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 24小时制
		if (date != null) {
			String dateStr = format.format(date);
			return dateStr;
		}
		return null;
	}

	/**
	 * 时间转成string类型(yyyy-MM-dd) ZTK2017年7月14日上午11:59:44
	 */
	public static String dateToString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");// 无小时制
		if (date != null) {
			String dateStr = format.format(date);
			return dateStr;
		}
		return null;
	}

	/**
	 * 时间转成string类型(yyyy-MM) ZTK2017年7月14日上午11:59:44
	 */
	public static String dateToStringYM(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");// 只有年月
		if (date != null) {
			String dateStr = format.format(date);
			return dateStr;
		}
		return null;
	}

	/**
	 * 获取当年的第一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date getCurrYearFirst() {
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearFirst(currentYear);
	}

	/**
	 * 获取当年的最后一天
	 * 
	 * @param year
	 * @return
	 */
	public static Date getCurrYearLast() {
		Calendar currCal = Calendar.getInstance();
		int currentYear = currCal.get(Calendar.YEAR);
		return getYearLast(currentYear);
	}

	/**
	 * 获取某年第一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date getYearFirst(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		Date currYearFirst = calendar.getTime();
		return currYearFirst;
	}

	/**
	 * 获取某年最后一天日期
	 * 
	 * @param year
	 *            年份
	 * @return Date
	 */
	public static Date getYearLast(int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.clear();
		calendar.set(Calendar.YEAR, year);
		calendar.roll(Calendar.DAY_OF_YEAR, -1);
		Date currYearLast = calendar.getTime();

		return currYearLast;
	}

	/**
	 * 获得该月第一天
	 * 
	 * @param year
	 * @param month
	 * @return
	 */
	public static Date getFirstDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		// 格式化日期
		Date firstDayOfMonth = cal.getTime();
		return firstDayOfMonth;
	}

	public static Date getFirstDayOfMonth(int diff, Date date) {
		Calendar cal = Calendar.getInstance();
		String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
		String[] splits = dateStr.split("-");
		Integer year = new Integer(splits[0]);
		Integer month = new Integer(splits[1]);
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1 - diff);
		// 获取某月最小天数
		int firstDay = cal.getActualMinimum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		// 格式化日期
		Date firstDayOfMonth = cal.getTime();
		return firstDayOfMonth;
	}

	public static Date getFirstDayOfYear(int diff, Date date) {
		Calendar cal = Calendar.getInstance();
		String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
		String[] splits = dateStr.split("-");
		Integer year = new Integer(splits[0]);
		// 设置年份
		cal.set(Calendar.YEAR, year - diff);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		// 格式化日期
		Date firstDayOfYear = cal.getTime();
		return firstDayOfYear;
	}

	/**
	 * 获取某月的最后一天 @Title:getLastDayOfMonth @Description: @param:@param
	 * year @param:@param month @param:@return @return:String @throws
	 */
	public static Date getLastDayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1);
		// 获取某月最大天数
		int lastDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最大天数
		cal.set(Calendar.DAY_OF_MONTH, lastDay);
		// 格式化日期
		Date lastDayOfMonth = cal.getTime();

		return lastDayOfMonth;
	}

	public static Date getLastDayOfMonth(int diff, Date date) {
		Calendar cal = Calendar.getInstance();
		String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
		String[] splits = dateStr.split("-");
		Integer year = new Integer(splits[0]);
		Integer month = new Integer(splits[1]);
		// 设置年份
		cal.set(Calendar.YEAR, year);
		// 设置月份
		cal.set(Calendar.MONTH, month - 1 - diff);
		// 获取某月最小天数
		int firstDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 设置日历中月份的最小天数
		cal.set(Calendar.DAY_OF_MONTH, firstDay);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		// 格式化日期
		Date lastDayOfMonth = cal.getTime();
		return lastDayOfMonth;
	}

	public static Date getLastDayOfYear(int diff, Date date) {
		Calendar cal = Calendar.getInstance();
		String dateStr = DateUtils.formatDate(date, "yyyy-MM-dd");
		String[] splits = dateStr.split("-");
		Integer year = new Integer(splits[0]);
		// 设置年份
		cal.set(Calendar.YEAR, year - diff);
		cal.set(Calendar.MONTH, 11);
		cal.set(Calendar.DATE, 31);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		// 格式化日期
		Date lastDayOfYear = cal.getTime();
		return lastDayOfYear;
	}

	/**
	 * 获取两个日期相差的月数
	 */
	public static int getMonthDiff(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(d1);
		c2.setTime(d2);
		int year1 = c1.get(Calendar.YEAR);
		int year2 = c2.get(Calendar.YEAR);
		int month1 = c1.get(Calendar.MONTH);
		int month2 = c2.get(Calendar.MONTH);
		int day1 = c1.get(Calendar.DAY_OF_MONTH);
		int day2 = c2.get(Calendar.DAY_OF_MONTH);
		// 获取年的差值
		int yearInterval = year1 - year2;
		// 如果 d1的 月-日 小于 d2的 月-日 那么 yearInterval-- 这样就得到了相差的年数
		if (month1 < month2 || month1 == month2 && day1 < day2)
			yearInterval--;
		// 获取月数差值
		int monthInterval = (month1 + 12) - month2;
		if (day1 < day2)
			monthInterval--;
		monthInterval %= 12;
		int monthsDiff = Math.abs(yearInterval * 12 + monthInterval);
		return monthsDiff;
	}

	public static int getYearDiff(Date d1, Date d2) {
		Integer year1 = Integer.valueOf(DateUtils.formatDate(d1, "yyyy"));
		Integer year2 = Integer.valueOf(DateUtils.formatDate(d2, "yyyy"));
		return (year2 - year1) == 0 ? 1 : (year2 - year1);
	}

	public static void main(String[] args) {
		System.out.println(DateUtils.getLastDayOfYear(1, new Date()));
	}

}
