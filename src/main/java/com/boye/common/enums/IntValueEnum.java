package com.boye.common.enums;

import org.apache.commons.lang3.EnumUtils;

import java.util.List;

/**
 * 整型值枚举接口
 */
public interface IntValueEnum
{
	/**
	 * 获取枚举值
	 * @return
	 */
	public int getValue();

	/**
	 * 获取枚举信息
	 * @return
	 */
	public String getText();

	/**
	 * 获取枚举名称
	 *
	 * @return
	 */
	public String name();

	static <E extends Enum<E>> E valueOf(Class<E> enumClass, int value) {

		List<E> list = EnumUtils.getEnumList(enumClass);
		for (E method : list) {

			if (method instanceof IntValueEnum && value == ((IntValueEnum)method).getValue()) {
				return method;
			}
		}

		return null;
	};
}
