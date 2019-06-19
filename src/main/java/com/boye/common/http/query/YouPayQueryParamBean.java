package com.boye.common.http.query;

import java.security.PrivateKey;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.RSAutil;

import lombok.Data;

@Data
public class YouPayQueryParamBean {
	
	private String brandNo;
	
	private String orderNo;
	
	private String signature;
	
	private String signType;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("brandNo", brandNo);
		signParam.put("orderNo", orderNo);
		signParam.put("signType", signType);
		signParam.put("signature", signature);
		return signParam;
	}
	
	public String hasSignGetParam() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("brandNo", brandNo);
		signParam.put("orderNo", orderNo);
		signParam.put("signType", signType);
		signParam.put("signature", signature);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	public String generateSign() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("brandNo", brandNo);
		signParam.put("orderNo", orderNo);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
        	if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
        PrivateKey privateKey;
		try {
			privateKey = RSAutil.getPrivateKey(key);
			signature = RSAutil.sign(sb.toString(),privateKey);
			System.out.println(signature);
			return signature;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}

}
