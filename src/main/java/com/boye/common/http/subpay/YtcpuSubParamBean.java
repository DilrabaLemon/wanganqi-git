package com.boye.common.http.subpay;

import java.util.HashMap;
import java.util.Map;
import com.boye.common.utils.MD5;

public class YtcpuSubParamBean {
	
	private String partner_id;
	
	private String channel;
	
	private String out_order_no;
	
	private String amount;
	
	private String body;
	
	private String acc_no;
	
	private String acc_name;
	
	private String acc_type;
	
	private String sign;
	
	private String bank_name;
	
	private String backCity;
	
	private String bank_city;
	
	private String key;
	
	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		
		sb.append("acc_name=" + acc_name);
		sb.append("&acc_no=" + acc_no);
		sb.append("&acc_type=" + acc_type);
		sb.append("&amount=" + amount);
		
		sb.append("&bankCity=" + backCity);
		sb.append("&bank_city=" + bank_city);
		sb.append("&bank_name=" + bank_name);
		sb.append("&body=" + body);
		sb.append("&channel=" + channel);
		
		sb.append("&out_order_no=" + out_order_no);
		sb.append("&partner_id=" + partner_id);
		
		System.out.println(sb);
		return sb;
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("acc_no", acc_no);
		resultMap.put("acc_name", acc_name);
		resultMap.put("acc_type", acc_type);
		resultMap.put("amount", amount);
		
		resultMap.put("bankCity", backCity);
		resultMap.put("bank_city", bank_city);
		resultMap.put("bank_name", bank_name);
		resultMap.put("body", body);
		resultMap.put("channel", channel);
		
		resultMap.put("out_order_no", out_order_no);
		resultMap.put("partner_id", partner_id);
		resultMap.put("sign", sign);
		return resultMap;
	}

	public String generateSign() {
		String signParam = returnParamStr().toString() + "&key=" + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam).toUpperCase();
		System.out.println(sign);
		return sign;
	}

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getOut_order_no() {
		return out_order_no;
	}

	public void setOut_order_no(String out_order_no) {
		this.out_order_no = out_order_no;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getBackCity() {
		return backCity;
	}

	public void setBackCity(String backCity) {
		this.backCity = backCity;
	}

	public String getAcc_no() {
		return acc_no;
	}

	public void setAcc_no(String acc_no) {
		this.acc_no = acc_no;
	}

	public String getAcc_name() {
		return acc_name;
	}

	public void setAcc_name(String acc_name) {
		this.acc_name = acc_name;
	}

	public String getAcc_type() {
		return acc_type;
	}

	public void setAcc_type(String acc_type) {
		this.acc_type = acc_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getBank_city() {
		return bank_city;
	}

	public void setBank_city(String bank_city) {
		this.bank_city = bank_city;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
}
