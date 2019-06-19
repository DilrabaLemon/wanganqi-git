package com.boye.common;

import com.boye.base.entity.BaseEntity;

public class ParamUtils {
	
	public static boolean paramCheckNull(String... params) {
		for(String str: params) {
			str = str == null ? null : str.trim();
			if (str == null || str.isEmpty()) return true;
		}
		return false;
	}
	
	public static boolean paramCheckNull(BaseEntity... params) {
		for(BaseEntity obj: params) {
			if (obj == null || obj.paramIsNull()) return true;
		}
		return false;
	}

	public static boolean paramCheckNull(Integer... params) {
		for(Integer param: params) {
			if (param == null) return true;
		}
		return false;
	}

	public static boolean paramCheckNull(Double... params) {
		for(Double param: params) {
			if (param == null) return true;
		}
		return false;
	}
	
	public static boolean paramCheckNull(Long... params) {
		for(Long param: params) {
			if (param == null) return true;
		}
		return false;
	}

//	public static boolean paramCheckNull(QueryBean... params) {
//		for(QueryBean obj: params) {
//			if (obj == null || obj.paramIsNull()) return true;
//		}
//		return false;
//	}
}
