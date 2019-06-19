package com.boye.common.http.subpay;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class HhlWAKSubParamBean {
	
	private String acc_no;
	
	private String id_number;
	
	private String tel_no;
	
	private String bank_name;

	private String district;
	
	private String cash_amount;
	
	private String merchant_open_id;
	
	private String name;
	
	private String sign;
	
	private String timestamp;
	
	private String key;
	

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("acc_no" , acc_no);
		result.put("id_number" , id_number);
		result.put("tel_no" , tel_no);
		result.put("district" , district);
		result.put("bank_name" , bank_name);
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
		sb.append("&bank_name=" + bank_name);
		sb.append("&cash_amount=" + cash_amount);
		sb.append("&district=" + district);
		sb.append("&id_number=" + id_number);
		sb.append("&merchant_open_id=" + merchant_open_id);
		sb.append("&name=" + name);
		sb.append("&tel_no=" + tel_no);
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
