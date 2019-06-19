package com.boye.common.http.pay;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class YtcpuPayParamBean {
	
	private String partner_id;
	
	private String amount;
	
	private String pay_type;
	
	private String out_order_no;
	
	private String body;
	
	private String notify_url;
	
	private String return_url;
	
	private String bank_code;
	
	private String bank_card_no;
	
	private String ip;
	
	private String sign;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("amount" , amount);
		if (bank_card_no != null) result.put("bank_card_no" , bank_card_no);
		if (bank_code != null) result.put("bank_code" , bank_code);
		result.put("body" , body);
		if (ip != null) result.put("ip" , ip);
		result.put("notify_url" , notify_url);
		result.put("out_order_no" , out_order_no);
		result.put("partner_id" , partner_id);
		result.put("pay_type" , pay_type);
		if (return_url != null) result.put("return_url" , return_url);
		result.put("sign", sign);
		return result;
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + "&key=" + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam).toUpperCase();
		System.out.println(sign);
		return sign;
	}
	
	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("amount=" + amount);
		if (bank_card_no != null) sb.append("&bank_card_no=" + bank_card_no);
		if (bank_code != null) sb.append("&bank_code=" + bank_code);
		sb.append("&body=" + body);
		if (ip != null) sb.append("&ip=" + ip);
		sb.append("&notify_url=" + notify_url);
		sb.append("&out_order_no=" + out_order_no);
		sb.append("&partner_id=" + partner_id);
		sb.append("&pay_type=" + pay_type);
		if (return_url != null) sb.append("&return_url=" + return_url);
		System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		result.append("&sign=" + sign);
//		result.append("&sign_type=" + sign_type);
		return result.toString().toUpperCase();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public String getOut_order_no() {
		return out_order_no;
	}

	public void setOut_order_no(String out_order_no) {
		this.out_order_no = out_order_no;
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

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getBank_card_no() {
		return bank_card_no;
	}

	public void setBank_card_no(String bank_card_no) {
		this.bank_card_no = bank_card_no;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
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
	
}
