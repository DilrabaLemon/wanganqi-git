package com.boye.common.enums;

/**
 * 字符串值枚举接口
 */

public interface StringValueTextEnum extends EnumMybatisHandle{

	/**
	 * 获取枚举值
	 * 
	 * @return
	 */
	String getValue();

	/**
	 * 获取枚举信息
	 * 
	 * @return
	 */
	String getText();
	
	String name();
	
	
}
