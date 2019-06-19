package com.boye.common.http.query;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class HLPayQueryParamBean {
	
	private String order_id;
	
	private String merchant_id;
	
	private String sign;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("merchant_id", merchant_id);
		signParam.put("order_id", order_id);
		signParam.put("sign", sign);
		return signParam;
	}
	
	public String hasSignGetParam() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("merchant_id", merchant_id);
		signParam.put("order_id", order_id);
		signParam.put("sign", sign);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry entry : entrySet) {
            if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	public String generateSign() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("merchant_id", merchant_id);
		signParam.put("order_id", order_id);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry entry : entrySet) {
        	if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
		sb.append(key);
		System.out.println(sb);
		sign = MD5.md5Str(sb.toString()).toUpperCase();
		System.out.println(sign);
		return sign;
	}

}
