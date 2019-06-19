package com.boye.common.http.pay;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class DingEPayParamBean {
	
	private String userId;
	
	private BigDecimal parvalue;
	
	private Long timestamp;
	
	private String callbackUrl;
	
	private String sign;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("UserId", userId);
		signParam.put("Parvalue", parvalue);
		signParam.put("Timestamp", timestamp);
		signParam.put("CallbackUrl", callbackUrl);
		signParam.put("Sign", sign);
		return signParam;
	}
	
	public String hasSignGetParam() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("UserId", userId);
		signParam.put("Parvalue", parvalue);
		signParam.put("Timestamp", timestamp);
		signParam.put("CallbackUrl", callbackUrl);
		signParam.put("Sign", sign);
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
		StringBuilder sb = new StringBuilder();
		sb.append(parvalue);
		sb.append(userId);
		sb.append(timestamp.toString());
		sb.append(key);
		System.out.println(sb);
		sign = MD5.md5Str(sb.toString());
		System.out.println(sign);
		return sign;
	}
}
