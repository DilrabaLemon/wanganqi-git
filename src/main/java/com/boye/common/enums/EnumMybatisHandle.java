package com.boye.common.enums;

public interface EnumMybatisHandle {
	
	/**
	 * 获取枚举名称
	 * 
	 * @return
	 */
	default String getName() {
		return this.getClass().getSimpleName();
	};

}
