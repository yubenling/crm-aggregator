package com.kycrm.util;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.BigDecimalConverter;
import org.apache.commons.beanutils.converters.BooleanConverter;
import org.apache.commons.beanutils.converters.DateConverter;
import org.apache.commons.beanutils.converters.DoubleConverter;
import org.apache.commons.beanutils.converters.IntegerConverter;
import org.apache.commons.beanutils.converters.LongConverter;
import org.apache.commons.beanutils.converters.ShortConverter;

/** 
* @ClassName: MyBeanUtils 
* @Description BeanUtils 注册,避免source转target时 部分为null属性给默认值
* @author jackstraw_yu
* @date 2018年1月23日 下午5:50:03 
*  
*/
public class MyBeanUtils extends BeanUtils{

	private MyBeanUtils(){} 
	static { 
		//注册sql.date的转换器，即允许BeanUtils.copyProperties时的源目标的sql类型的值允许为空 
		ConvertUtils.register(new org.apache.commons.beanutils.converters.SqlDateConverter(null), java.sql.Date.class); 
		ConvertUtils.register(new org.apache.commons.beanutils.converters.SqlDateConverter(null), java.util.Date.class);  
		ConvertUtils.register(new org.apache.commons.beanutils.converters.SqlTimestampConverter(null), java.sql.Timestamp.class); 
		//注册util.date的转换器，即允许BeanUtils.copyProperties时的源目标的util类型的值允许为空 
		ConvertUtils.register(new DateConverter(null), java.util.Date.class);
		
		//避免对基本数据类型对应的整型进行初始化
		ConvertUtils.register(new BooleanConverter(null), Boolean.class);  
		ConvertUtils.register(new LongConverter(null), Long.class);  
	    ConvertUtils.register(new ShortConverter(null), Short.class);  
	    ConvertUtils.register(new IntegerConverter(null), Integer.class);  
	    ConvertUtils.register(new DoubleConverter(null), Double.class);  
	    ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class); 
	
	} 

	public static void copyProperties(Object target, Object source) 
	throws InvocationTargetException, IllegalAccessException { 
		// 支持对日期copy 
		BeanUtils.copyProperties(target, source); 
	} 
}
