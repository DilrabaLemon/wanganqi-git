package com.boye.common.http.pay;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.boye.common.utils.MD5;
import com.google.gson.Gson;

public class QwebankParamBean {
	
	private String accessToken;
	private String outTradeNo;
	private Long money;
	
	private String type;
	private String body;
	private String detail;
	private String notifyUrl;
	private String productId;
	private String successUrl;
	
	//特殊
	
	private String key; //密钥

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public Long getMoney() {
		return money;
	}

	public void setMoney(Long money) {
		this.money = money;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		
//		result.put("sign_type", sign_type);
//		result.put("signature", signature);
//		result.put("biz_content", biz_content);
		
		return result;
	}
	
	public String generateSign() {
		return null;
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
