package com.boye.common.http.pay;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.boye.common.utils.MD5;
import com.google.gson.Gson;

public class YsH5ParamBean {
	
	private String sign_type;
	private String signature;
	private String biz_content;
	
	private String version;
	private String mch_id;
	private String out_order_no;
	private String pay_platform;
	private String pay_type;
	private int payment_fee;
	private String cur_type;
	private String body;
	private String notify_url;
	private String bill_create_ip;
	private String remark;
	private String cashier_id;
	private String auth_code;
	private String openid;
	private String pay_limit;
	private String scene_info;
	private String appid;
	
	//特殊
	private String key; //密钥
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

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

	public String getPay_platform() {
		return pay_platform;
	}

	public void setPay_platform(String pay_platform) {
		this.pay_platform = pay_platform;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public int getPayment_fee() {
		return payment_fee;
	}

	public void setPayment_fee(int payment_fee) {
		this.payment_fee = payment_fee;
	}

	public String getCur_type() {
		return cur_type;
	}

	public void setCur_type(String cur_type) {
		this.cur_type = cur_type;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getBill_create_ip() {
		return bill_create_ip;
	}

	public void setBill_create_ip(String bill_create_ip) {
		this.bill_create_ip = bill_create_ip;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCashier_id() {
		return cashier_id;
	}

	public void setCashier_id(String cashier_id) {
		this.cashier_id = cashier_id;
	}

	public String getAuth_code() {
		return auth_code;
	}

	public void setAuth_code(String auth_code) {
		this.auth_code = auth_code;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getPay_limit() {
		return pay_limit;
	}

	public void setPay_limit(String pay_limit) {
		this.pay_limit = pay_limit;
	}

	public String getScene_info() {
		return scene_info;
	}

	public void setScene_info(String scene_info) {
		this.scene_info = scene_info;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
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
		param.put("version", version);
		param.put("mch_id", mch_id);
		param.put("out_order_no", out_order_no);
		param.put("pay_platform", pay_platform);
		param.put("pay_type", pay_type);
		param.put("payment_fee", payment_fee);
		param.put("cur_type", cur_type);
		param.put("body", body);
		param.put("notify_url", notify_url);
		param.put("bill_create_ip", bill_create_ip);
		param.put("remark", remark);
		param.put("cashier_id", cashier_id);
		param.put("auth_code", auth_code);
		param.put("openid", openid);
		param.put("pay_limit", pay_limit);
		param.put("scene_info", scene_info);
		param.put("appid", appid);
		
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
