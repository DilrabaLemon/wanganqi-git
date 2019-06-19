package com.boye.common.utils;

import java.util.HashMap;
import java.util.Map;

public class ParamUtils {
	
	public static Map<String, Object> getMapByStringParam(String param) {
		String[] strs = param.split("&");
		Map<String, Object> paramMap = new HashMap<String, Object>();
		for (String str: strs) {
			String[] kv = str.split("=");
			if (kv.length == 2) {
				paramMap.put(kv[0], kv[1]);
			}
		}
		return paramMap;
	}
}
