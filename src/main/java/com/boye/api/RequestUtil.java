package com.boye.api;

import java.util.TreeMap;

public class RequestUtil {
	public static String getParamSrc(TreeMap<String, String> paramsMap) {
		StringBuffer paramstr = new StringBuffer();
		for (String pkey : paramsMap.keySet()) {
			String pvalue = paramsMap.get(pkey);
			if (null != pvalue && "" != pvalue) {
				paramstr.append(pkey + "=" + pvalue + "&");
			}
		}
		String result = paramstr.substring(0, paramstr.length() - 1);
		return result;
	}
}
