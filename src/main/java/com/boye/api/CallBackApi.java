package com.boye.api;

import com.boye.common.utils.HttpClientUtil;

public class CallBackApi {

	public static String sendRQReturnMessage(String url, String param) {
		String findUrl = url + "?" + param;
        String res = HttpClientUtil.httpGet(findUrl, "UTF-8"); 
        System.out.println(res);
        return res;
	}

}
