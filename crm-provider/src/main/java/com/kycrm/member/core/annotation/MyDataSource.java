package com.kycrm.member.core.annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/** 
* @author wy
* @version 创建时间：2018年1月10日 上午11:40:19
*/
@Target({ ElementType.METHOD,ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME) 
public @interface MyDataSource {
    
	/**
	 * 
	 * @Author ZhengXiaoChen
	 * @Description 指定数据库库号
	 * @Date 2018年7月31日下午6:44:28
	 * @return
	 * @ReturnType String
	 */
    String value() default "";
}
