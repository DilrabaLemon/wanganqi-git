package com.boye.common.utils;

import com.boye.common.exception.SimpleException;
import com.fasterxml.jackson.core.JsonParser.Feature;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * json工具类
 * @author luofc
 *
 */
public class JsonUtils {

	private static ObjectMapper jsonbuildMapper=null;
	
	
	/**
	 * 返回通用的JSON处理对象
	 * @return
	 */
	public static ObjectMapper buildCommonJsonMapper() {
		if (null==jsonbuildMapper) {
			jsonbuildMapper = new ObjectMapper();
			//定义规则
			// 空属性异常忽略
			jsonbuildMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			// 转义字符-异常忽略
			jsonbuildMapper.configure(Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
			// 空值转换 异常忽略
			jsonbuildMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		}
		return jsonbuildMapper;
	}
	
	
	
	
	/**
	 * 返回JSON格式的字符串
	 * @param object 要转换的对象
	 * @return 返回json字符串
	 */
	public static  String covertObjectToString(Object object) {
		String value = null;
		try {
			value = buildCommonJsonMapper().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			throw new SimpleException("covertObjectToString序列化为json异常", e);
		}

		return value;
	}
}
