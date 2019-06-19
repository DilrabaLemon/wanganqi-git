package com.boye.common.http.pay;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class HhlPayParamBean {
	
	private String merchant_open_id;
	
	private String merchant_order_number;
	
	private String notify_url;
	
	private Integer pay_type;
	
	private Long cope_pay_amount;
	
	private String pay_wap_mark;
	
	private String subject;
	
	private String timestamp;
	
	private String sign;
	
	private String md5_key;
	
//	  $data = array();
//    $data['pid'] = $_REQUEST['pid'];
//    $data['type'] = $_REQUEST['type'];
//    $data['out_trade_no'] = $_REQUEST['out_trade_no'];
//    $data['notify_url'] = $_REQUEST['notify_url'];
//    $data['return_url'] = $_REQUEST['return_url'];
//    $data['name'] = $_REQUEST['name'];
//    $data['money'] = $_REQUEST['money'];
//    if(isset($_REQUEST['sitename'])){
//        $data['sitename'] = $_REQUEST['sitename'];
//    }else{
//        $data['sitename'] = '';
//    }
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cope_pay_amount" , cope_pay_amount);
		result.put("merchant_open_id" , merchant_open_id);
		result.put("merchant_order_number" , merchant_order_number);
		result.put("notify_url" , notify_url);
		result.put("pay_type" , pay_type);
		result.put("pay_wap_mark" , pay_wap_mark);
		result.put("subject" , subject);
		result.put("timestamp" , timestamp);
		result.put("sign", sign);
		return result;
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + md5_key;
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
		sb.append("&pay_type=" + pay_type);
		sb.append("&pay_wap_mark=" + pay_wap_mark);
		sb.append("&subject=" + subject);
		sb.append("&timestamp=" + timestamp);
		System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		result.append("&sign=" + sign);
		return result.toString();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}

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

	public Integer getPay_type() {
		return pay_type;
	}

	public void setPay_type(Integer pay_type) {
		this.pay_type = pay_type;
	}

	public Long getCope_pay_amount() {
		return cope_pay_amount;
	}

	public void setCope_pay_amount(Long cope_pay_amount) {
		this.cope_pay_amount = cope_pay_amount;
	}

	public String getPay_wap_mark() {
		return pay_wap_mark;
	}

	public void setPay_wap_mark(String pay_wap_mark) {
		this.pay_wap_mark = pay_wap_mark;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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

	public String getMd5_key() {
		return md5_key;
	}

	public void setMd5_key(String md5_key) {
		this.md5_key = md5_key;
	}
	
}
