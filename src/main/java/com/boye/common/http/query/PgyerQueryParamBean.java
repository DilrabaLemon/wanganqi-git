package com.boye.common.http.query;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;

public class PgyerQueryParamBean {
	
	private String service;
	private String merchant_no;
	private String merchants;
	private String order_no;
	private String sign;
	
	//特殊
	private String key; //密钥

	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getMerchant_no() {
		return merchant_no;
	}

	public void setMerchant_no(String merchant_no) {
		this.merchant_no = merchant_no;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getMerchants() {
		return merchants;
	}

	public void setMerchants(String merchants) {
		this.merchants = merchants;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = returnParamStr();
		result.put("sign", sign);
		return result;
	}
	
	public String generateSign() {
		StringBuffer sb = new StringBuffer();
        Set<Entry<String, Object>> entrySet = returnParamStr().entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getValue() != null)
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
		String signParam = sb.toString() + "key=" + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam);
		System.out.println(sign);
		return sign;
	}
	
	public Map<String, Object> returnParamStr() {
		Map<String, Object> param = new TreeMap<String, Object>();
		param.put("service", service);
		param.put("merchant_no", merchant_no);
		param.put("merchants", merchants);
		param.put("order_no", order_no);
		return param;
	}
	
	public String hasSignParam() {
		return null;
	}
	
	public String notSignParam() {
		StringBuffer result = new StringBuffer();
//		result.append("appId=" + appId);
//		result.append("&amount=" + amount);
//		result.append("&platOrderNo=" + platOrderNo);
//		result.append("&url=" + url);
		return result.toString();
	}
	
}
