package com.boye.common.http.pay;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.boye.common.utils.RSAutil;

import lombok.Data;

@Data
public class YouPayParamBean {
	
	private String brandNo;
	
	private String orderNo;
	
	private BigDecimal price;
	
	private String callbackUrl;
	
	private String frontUrl;
	
	private String serviceType;
	
	private String userName;
	
	private String clientIP;
	
	private String signature;
	
	private String signType;
	
	private String key;
	
	private String pkey;
	
	public Map<String, Object> hasSignParamMap() {
		DecimalFormat df = new DecimalFormat("0.00#");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("brandNo", brandNo);
		signParam.put("orderNo", orderNo);
		signParam.put("callbackUrl", callbackUrl);
		signParam.put("frontUrl", frontUrl);
		signParam.put("price", df.format(price));
		signParam.put("serviceType", serviceType);
		signParam.put("userName", userName);
		signParam.put("clientIP", clientIP);
		signParam.put("signType", signType);
		signParam.put("signature", signature);
		return signParam;
	}
	
	public String hasSignGetParam() {
		DecimalFormat df = new DecimalFormat("0.00#");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("brandNo", brandNo);
		signParam.put("orderNo", orderNo);
		signParam.put("callbackUrl", callbackUrl);
		signParam.put("frontUrl", frontUrl);
		signParam.put("price", df.format(price));
		signParam.put("serviceType", serviceType);
		signParam.put("userName", userName);
		signParam.put("clientIP", clientIP);
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
		signParam.put("price", price);
		signParam.put("serviceType", serviceType);
		signParam.put("userName", userName);
		signParam.put("clientIP", clientIP);
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
			PublicKey publicKey = RSAutil.getPublicKey(pkey);
			System.out.println(signature);
			System.out.println(RSAutil.verify(sb.toString(), publicKey, signature));
			return signature;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return signature;
	}

}
