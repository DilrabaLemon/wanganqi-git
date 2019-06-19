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
public class BePayParamBean {
	
	private String mchId;
	
	private String appId;
	
	private String productId;
	
	private String currency;
	
	private BigDecimal amount;
	
	private String subject;
	
	private String body;
	
	private String mchOrderNo;
	
	private String notifyUrl;
	
	private String sign;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("mchId", mchId);
		signParam.put("appId", appId);
		signParam.put("mchOrderNo", mchOrderNo);
		signParam.put("productId", productId);
		signParam.put("currency", currency);
		signParam.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
		signParam.put("notifyUrl", notifyUrl);
		signParam.put("subject", subject);
		signParam.put("body", body);
		signParam.put("sign", sign);
		return signParam;
	}
	
	public String hasSignGetParam() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("mchId", mchId);
		signParam.put("appId", appId);
		signParam.put("mchOrderNo", mchOrderNo);
		signParam.put("productId", productId);
		signParam.put("currency", currency);
		signParam.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
		signParam.put("notifyUrl", notifyUrl);
		signParam.put("subject", subject);
		signParam.put("body", body);
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
		signParam.put("mchId", mchId);
		signParam.put("appId", appId);
		signParam.put("mchOrderNo", mchOrderNo);
		signParam.put("productId", productId);
		signParam.put("currency", currency);
		signParam.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
		signParam.put("notifyUrl", notifyUrl);
		signParam.put("subject", subject);
		signParam.put("body", body);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry entry : entrySet) {
        	if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
		sb.append("key=" + key);
		System.out.println(sb);
		sign = MD5.md5Str(sb.toString()).toUpperCase();
		System.out.println(sign);
		return sign;
	}
}
