package com.boye.common.http.pay;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class HhlQuickParamBean {
	
    private String merchant_open_id;   //商户号
    private String merchant_order_number; //订单号
    private String timestamp;  //订单时间
    private String subject; //订单名称
    private String cope_pay_amount; //订单金额
    private String name;    //姓名
	private String id_number;   //身份证号
    private String acc_no;   //卡号
    private String tel_no;  //手机号
    private String pay_wap_mark;    //产品类型
    private String pay_type; //支付类型
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
//
//	public String getId_number() {
//		return id_number;
//	}
//
//	public void setId_number(String id_number) {
//		this.id_number = id_number;
//	}

	public String getAcc_no() {
		return acc_no;
	}

	public void setAcc_no(String acc_no) {
		this.acc_no = acc_no;
	}

	public String getTel_no() {
		return tel_no;
	}

	public void setTel_no(String tel_no) {
		this.tel_no = tel_no;
	}

	public String getPay_wap_mark() {
		return pay_wap_mark;
	}

	public void setPay_wap_mark(String pay_wap_mark) {
		this.pay_wap_mark = pay_wap_mark;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
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
	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cope_pay_amount" , cope_pay_amount);
		result.put("name" , name);
		result.put("id_number" , id_number);
		result.put("acc_no" , acc_no);
		result.put("tel_no" , tel_no);
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
		String signParam = returnParamStr().toString() + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam);
		return sign;
	}
	
	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("acc_no=" + acc_no);
		sb.append("&cope_pay_amount=" + cope_pay_amount);
		sb.append("&id_number="+id_number);
		sb.append("&merchant_open_id=" + merchant_open_id);
		sb.append("&merchant_order_number=" + merchant_order_number);
		sb.append("&name=" + name);
		sb.append("&notify_url=" + notify_url);
		sb.append("&pay_type=" + pay_type);
		sb.append("&pay_wap_mark=" + pay_wap_mark);
		sb.append("&subject=" + subject);
		sb.append("&tel_no=" + tel_no);
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
