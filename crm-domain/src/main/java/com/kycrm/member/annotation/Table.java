package com.kycrm.member.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

/** 
* @ClassName: Table 
* @Description: 注解
* @author jackstraw_yu
* @date 2018年3月6日 下午4:15:50 
*/
@Retention(RetentionPolicy.RUNTIME)  
@Target(ElementType.FIELD)
public @interface Table {

	String value() default "";
}
