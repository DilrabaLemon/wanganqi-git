package com.boye.common.http.query;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class HhlQueryParamBean {
	
	private String merchant_open_id;
	
	private String out_trade_no;
	
	private String timestamp;
	
	private String key;
	
	private String sign;

	public String getMerchant_open_id() {
		return merchant_open_id;
	}

	public void setMerchant_open_id(String merchant_open_id) {
		this.merchant_open_id = merchant_open_id;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
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

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("merchant_open_id=" + merchant_open_id);
		sb.append("&out_trade_no=" + out_trade_no);
		sb.append("&timestamp=" + timestamp);
		System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam);
		System.out.println(sign);
		return sign;
	}
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sign" , sign);
		result.put("merchant_open_id" , merchant_open_id);
		result.put("out_trade_no" , out_trade_no);
		result.put("timestamp" , timestamp);
		return result;
	}
}
