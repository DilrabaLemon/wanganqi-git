package com.boye.common.http.subpay;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class HhlYSFSubParamBean {
	
	private String acc_no;
	
	private String cash_amount;
	
	private String merchant_open_id;
	
	private String name;
	
	private String sign;
	
	private String timestamp;
	
	private String key;
	

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cash_amount" , cash_amount);
		result.put("merchant_open_id" , merchant_open_id);
		result.put("acc_no" , acc_no);
		result.put("name" , name);
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
		sb.append("&cash_amount=" + cash_amount);
		sb.append("&merchant_open_id=" + merchant_open_id);
		sb.append("&name=" + name);
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


}
