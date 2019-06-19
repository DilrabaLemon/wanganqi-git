package com.boye.common.http.pay;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class HhlWYParamBean {
	
	private String merchant_open_id;   //商户号
	private String merchant_order_number; //订单号
	private String timestamp;  //订单时间
	private String subject; //订单名称
	private String cope_pay_amount; //订单金额
	private String paymentChannel;
	private String pay_wap_mark;    //产品类型
	private String notify_url;  //后台通知地址URL
	private String sign;   //签名信息
	private String key; //密钥
	public String getMerchant_open_id() {
		return merchant_open_id;
	}
	public void setMerchant_open_id(String merchant_open_id) {
		this.merchant_open_id = merchant_open_id;
	}
	public String getMerchant_order_number() {
		return merchant_order_number;
	}
	public void setMerchant_order_number(String merchant_order_number) {
		this.merchant_order_number = merchant_order_number;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getCope_pay_amount() {
		return cope_pay_amount;
	}
	public void setCope_pay_amount(String cope_pay_amount) {
		this.cope_pay_amount = cope_pay_amount;
	}
	public String getPaymentChannel() {
		return paymentChannel;
	}
	public void setPaymentChannel(String paymentChannel) {
		this.paymentChannel = paymentChannel;
	}
	public String getPay_wap_mark() {
		return pay_wap_mark;
	}
	public void setPay_wap_mark(String pay_wap_mark) {
		this.pay_wap_mark = pay_wap_mark;
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
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cope_pay_amount" , cope_pay_amount);
		result.put("merchant_open_id" , merchant_open_id);
		result.put("merchant_order_number" , merchant_order_number);
		result.put("notify_url" , notify_url);
		result.put("paymentChannel" , paymentChannel);
		result.put("pay_wap_mark" , pay_wap_mark);
		result.put("subject" , subject);
		result.put("timestamp" , timestamp);
		result.put("sign", sign);
		return result;
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam);
		return sign;
	}
	
	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("cope_pay_amount=" + cope_pay_amount);
		sb.append("&merchant_open_id=" + merchant_open_id);
		sb.append("&merchant_order_number=" + merchant_order_number);
		sb.append("&notify_url=" + notify_url);
		sb.append("&pay_wap_mark=" + pay_wap_mark);
		sb.append("&paymentChannel=" + paymentChannel);
		sb.append("&subject=" + subject);
		sb.append("&timestamp=" + timestamp);
		System.out.println(sb);
		return sb;
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
