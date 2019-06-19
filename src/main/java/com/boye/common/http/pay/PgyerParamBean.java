package com.boye.common.http.pay;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;

public class PgyerParamBean {
	
	private String service;
	private String pay_code;
	private String merchant_no;
	private String merchants;
	private String order_no;
	private String amount;
	private String product_name;
	private String type;
	private String notify_url;
	private String sign;
	
	//特殊
	private String key; //密钥

	
	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getPay_code() {
		return pay_code;
	}

	public void setPay_code(String pay_code) {
		this.pay_code = pay_code;
	}

	public String getMerchant_no() {
		return merchant_no;
	}

	public void setMerchant_no(String merchant_no) {
		this.merchant_no = merchant_no;
	}

	public String getMerchants() {
		return merchants;
	}

	public void setMerchants(String merchants) {
		this.merchants = merchants;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getProduct_name() {
		return product_name;
	}

	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
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
		param.put("pay_code", pay_code);
		param.put("merchant_no", merchant_no);
		param.put("merchants", merchants);
		param.put("order_no", order_no);
		param.put("amount", amount);
		param.put("product_name", product_name);
		param.put("type", type);
		param.put("notify_url", notify_url);
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
		result.append("&type=" + type);
		return result.toString();
	}
	
}
