package com.boye.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

	public String value() default "";
	
	/**
	 * 标记忽略字段
	 * @return
	 */
	public boolean ignore() default false;
	
	/**
	 * 是否忽略空值
	 * @return
	 */
	public boolean ignoreNull() default true;
}
