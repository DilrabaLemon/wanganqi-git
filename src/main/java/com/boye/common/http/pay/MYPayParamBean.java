package com.boye.common.http.pay;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class MYPayParamBean {
	
	private String version;
	
	private String orgNo;
	
	private String custId;
	
	private String custOrderNo;
	
	private BigDecimal payAmt;
	
	private String tranType;
	
	private String backUrl;
	
	private String frontUrl;
	
	private String bankCode;
	
	private String goodsName;
	
	private String orderDesc;
	
	private String buyIp;
	
	private String sign;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("version", version);
		signParam.put("orgNo", orgNo);
		signParam.put("custId", custId);
		signParam.put("custOrderNo", custOrderNo);
		signParam.put("payAmt", payAmt.multiply(BigDecimal.valueOf(100)).intValue());
		signParam.put("tranType", tranType);
		signParam.put("backUrl", backUrl);
		signParam.put("frontUrl", frontUrl);
		signParam.put("bankCode", bankCode);
		signParam.put("goodsName", goodsName);
		signParam.put("orderDesc", orderDesc);
		signParam.put("buyIp", buyIp);
		signParam.put("sign", sign);
		return signParam;
	}
	
	public String hasSignGetParam() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("version", version);
		signParam.put("orgNo", orgNo);
		signParam.put("custId", custId);
		signParam.put("custOrderNo", custOrderNo);
		signParam.put("payAmt", payAmt.multiply(BigDecimal.valueOf(100)).intValue());
		signParam.put("tranType", tranType);
		signParam.put("backUrl", backUrl);
		signParam.put("frontUrl", frontUrl);
		signParam.put("bankCode", bankCode);
		signParam.put("goodsName", goodsName);
		signParam.put("orderDesc", orderDesc);
		signParam.put("buyIp", buyIp);
		signParam.put("sign", sign);
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
		signParam.put("version", version);
		signParam.put("orgNo", orgNo);
		signParam.put("custId", custId);
		signParam.put("custOrderNo", custOrderNo);
		signParam.put("payAmt", payAmt.multiply(BigDecimal.valueOf(100)).intValue());
		signParam.put("tranType", tranType);
		signParam.put("backUrl", backUrl);
		signParam.put("frontUrl", frontUrl);
		signParam.put("bankCode", bankCode);
		signParam.put("goodsName", goodsName);
		signParam.put("orderDesc", orderDesc);
		signParam.put("buyIp", buyIp);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
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
