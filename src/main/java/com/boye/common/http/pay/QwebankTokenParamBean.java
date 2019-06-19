package com.boye.common.http.pay;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.boye.base.constant.EncryptionUtils;
import com.boye.common.utils.MD5;
import com.google.gson.Gson;

public class QwebankTokenParamBean {
	
	private String merchantNo;
	private String key;
	private String nonce;
	private Long timestamp;
	private String sign;
	private String token;
	
	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getNonce() {
		return nonce;
	}

	public void setNonce(String nonce) {
		this.nonce = nonce;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("merchantNo", merchantNo);
		result.put("key", key);
		result.put("nonce", nonce);
		result.put("timestamp", timestamp);
		result.put("sign", sign);
		result.put("token", token);
		
		return result;
	}
	
	public String generateSign() {
		StringBuffer sb = new StringBuffer();
		sb.append("merchantNo=" + merchantNo);
		sb.append("&nonce=" + nonce);
		sb.append("&timestamp=" + timestamp);
		if (token != null) sb.append("&token=" + token);
		
		String signParam =sb.toString() + "&key=" + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam).toUpperCase();
		System.out.println(sign);
		return sign;
	}
//	
	public StringBuffer returnParamStr() {
		return null;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString().toUpperCase();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}
	
}
