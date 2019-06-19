package com.boye.common.http.subpay;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;
import com.boye.common.utils.RSAutil;

import lombok.Data;

@Data
public class YouPaySubParamBean {
	
	private String bankAccountName;
	
	private String bankAccountNo;
	
	private String bankCityCode;
	
	private String bankCode;
	
	private String bankProvinceCode;
	
	private String bankSiteName;
	
	private String brandNo;
	
	private String orderNo;
	
	private BigDecimal price;
	
	private String serviceType;
	
	private String userName;
	
	private String clientIP;
	
	private String callbackUrl;
	
	private String signType;
	
	private String signature;
	
	private String key;

	public Map<String, Object> hasSignParamMap() {
		DecimalFormat df = new DecimalFormat("0.00#");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("bankAccountName", bankAccountName);
		signParam.put("bankAccountNo", bankAccountNo);
		signParam.put("bankCityCode", bankCityCode);
		signParam.put("bankCode", bankCode);
		signParam.put("bankProvinceCode", bankProvinceCode);
		signParam.put("bankSiteName", bankSiteName);
		signParam.put("price", df.format(price));
		signParam.put("brandNo", brandNo);
		signParam.put("orderNo", orderNo);
		signParam.put("serviceType", serviceType);
		signParam.put("userName", userName);
		signParam.put("clientIP", clientIP);
		signParam.put("callbackUrl", callbackUrl);
		signParam.put("signType", signType);
		signParam.put("signature", signature);
		return signParam;
	}
	
	public String hasSignGetParam() {
		DecimalFormat df = new DecimalFormat("0.00#");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("bankAccountName", bankAccountName);
		signParam.put("bankAccountNo", bankAccountNo);
		signParam.put("bankCityCode", bankCityCode);
		signParam.put("bankCode", bankCode);
		signParam.put("bankProvinceCode", bankProvinceCode);
		signParam.put("bankSiteName", bankSiteName);
		signParam.put("price", df.format(price));
		signParam.put("brandNo", brandNo);
		signParam.put("orderNo", orderNo);
		signParam.put("serviceType", serviceType);
		signParam.put("userName", userName);
		signParam.put("clientIP", clientIP);
		signParam.put("callbackUrl", callbackUrl);
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
		DecimalFormat df = new DecimalFormat("0.00#");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("bankAccountNo", bankAccountNo);
		signParam.put("bankCityCode", bankCityCode);
		signParam.put("bankCode", bankCode);
		signParam.put("bankProvinceCode", bankProvinceCode);
		signParam.put("price", df.format(price));
		signParam.put("brandNo", brandNo);
		signParam.put("orderNo", orderNo);
		signParam.put("serviceType", serviceType);
		signParam.put("userName", userName);
		signParam.put("clientIP", clientIP);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getValue() != null)
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
