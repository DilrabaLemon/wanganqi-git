package com.boye.common.http.query;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.boye.common.utils.MD5;
import com.google.gson.Gson;

public class YsH5SubQueryParamBean {
	
	private String sign_type;
	private String signature;
	private String biz_content;
	
	private String mch_id;
	private String out_order_no;
	private String order_no;
	
	//特殊
	private String key; //密钥

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getOut_order_no() {
		return out_order_no;
	}

	public void setOut_order_no(String out_order_no) {
		this.out_order_no = out_order_no;
	}

	public String getOrder_no() {
		return order_no;
	}

	public void setOrder_no(String order_no) {
		this.order_no = order_no;
	}

	public String getSign_type() {
		return sign_type;
	}
	
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getBiz_content() {
		return biz_content;
	}

	public void setBiz_content(String biz_content) {
		this.biz_content = biz_content;
	}

	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("sign_type", sign_type);
		result.put("signature", signature);
		result.put("biz_content", biz_content);
		
		return result;
	}
	
	public String generateSign() {
		Map<String, Object> param = new TreeMap<String, Object>();
		Gson gson = new Gson();
		param.put("mch_id", mch_id);
		param.put("out_order_no", out_order_no);
		param.put("order_no", order_no);
		
		biz_content = gson.toJson(param);
		String signParam = "biz_content=" + biz_content + "&key=" + key;
		System.out.println(signParam);
		signature = MD5.md5Str(signParam).toUpperCase();
		System.out.println(signature);
		return signature;
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
