package com.boye.common.http.query;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class HhlQuickSubQueryParamBean {
	
    private String cash_number;   //商户号
    private String merchant_open_id; //订单号
    private String timestamp;  //订单时间
    private String sign;   //签名信息
    private String key; //密钥

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cash_number" , cash_number);
		result.put("merchant_open_id" , merchant_open_id);
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
		sb.append("cash_number=" + cash_number);
		sb.append("&merchant_open_id=" + merchant_open_id);
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
