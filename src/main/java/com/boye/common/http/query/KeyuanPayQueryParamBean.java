package com.boye.common.http.query;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.base.constant.Constants;
import com.boye.common.utils.SignTools;

import lombok.Data;

@Data
public class KeyuanPayQueryParamBean {
	
	private String method;
	
	private String app_key;
	
	private String sign_time;
	
	private String sign_method;
	
	private String charge_id;
	
	private String client_id;
	
	private String sign;
	
	private String key;
	
	private String token;
	
	public Map<String, String> hasSignParamMap() {
		Map<String, String> signParam = new TreeMap<String, String>();
		signParam.put("method", method);
		signParam.put("app_key", app_key);
		signParam.put("sign_method", sign_method);
		signParam.put("sign_time", sign_time);
		signParam.put("client_id", client_id);
		signParam.put("charge_id", charge_id);
		signParam.put("sign", sign);
		return signParam;
	}
	
	public String hasSignGetParam() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("method", method);
		signParam.put("app_key", app_key);
		signParam.put("sign_time", sign_time);
		signParam.put("client_id", client_id);
		signParam.put("charge_id", charge_id);
		signParam.put("sign", sign);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getValue() != null)
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	public String generateSign() throws UnsupportedEncodingException {
		Map<String, String> signParam = new TreeMap<String, String>();
		signParam.put("method", method);
		signParam.put("app_key", app_key);
		signParam.put("sign_method", sign_method);
		signParam.put("sign_time", sign_time);
		signParam.put("charge_id", charge_id);
		signParam.put("client_id", client_id);
		String sign = sign(getHeaders(), null, signParam, "POST", "/router");
		System.out.println(sign);
		return sign;
	}
	
	private String sign(Map<String, String> headerParams, Map<String, String> getParams, Map<String, String> postParams, String method, String path) {
        //header数据拼接字符串
        String mixHeaderParams = SignTools.mixHeaderParams(headerParams);
        //get数据拼接字符串
        String mixGetParams = SignTools.mixRequestParams(getParams);
        //post数据拼接字符串
        String mixPostParams = SignTools.mixRequestParams(postParams);
        //签名拼接字符串
        String mixAllParams = key + Constants.SEPARATOR
                + method + Constants.SEPARATOR
                + urlencode(path) + Constants.SEPARATOR
                + urlencode(mixHeaderParams) + Constants.SEPARATOR
                + urlencode(mixGetParams) + Constants.SEPARATOR
                + urlencode(mixPostParams) + Constants.SEPARATOR
                + key;
        //加密签名
        System.out.println(mixAllParams);
        try {
            return SignTools.byte2hex(SignTools.encryptMD5(mixAllParams), true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
    private String urlencode(String path) {
		String sb = null;
		try {
			sb = URLEncoder.encode(path, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sb;
	}

	public Map<String, String> getHeaders() {
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("User-Agent", "PrismSDK/JAVA");
        if (token != null) {
            headers.put("Authorization", token);
        }
        return headers;
    }

}
