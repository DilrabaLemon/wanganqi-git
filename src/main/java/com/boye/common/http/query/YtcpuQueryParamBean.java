package com.boye.common.http.query;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class YtcpuQueryParamBean {
	
	private String partner_id;
	
	private String out_order_no;
	
	private String sign;
	
	private String key;

	public String getPartner_id() {
		return partner_id;
	}

	public void setPartner_id(String partner_id) {
		this.partner_id = partner_id;
	}

	public String getOut_order_no() {
		return out_order_no;
	}

	public void setOut_order_no(String out_order_no) {
		this.out_order_no = out_order_no;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("out_order_no=" + out_order_no);
		sb.append("&partner_id=" + partner_id);
		return sb;
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + "&key=" + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam).toUpperCase();
		System.out.println(sign);
		return sign;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		result.append("&sign=" + sign);
		return result.toString();
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sign" , sign);
		result.put("out_order_no" , out_order_no);
		result.put("partner_id" , partner_id);
		return result;
	}
}
