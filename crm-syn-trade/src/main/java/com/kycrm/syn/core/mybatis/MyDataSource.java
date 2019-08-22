package com.kycrm.syn.core.mybatis;
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
    
    String value() default "";
}
