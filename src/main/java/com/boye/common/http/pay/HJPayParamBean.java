package com.boye.common.http.pay;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class HJPayParamBean {
	
	private String version;
	
	private String merchant_number;
	
	private BigDecimal cash;
	
	private String server_url;
	
	private String brower_url;
	
	private String order_id;
	
	private String order_time;
	
	private String pay_type;
	
	private String sign;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		DecimalFormat format = new DecimalFormat("0.00");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("version", version);
		signParam.put("merchant_number", merchant_number);
		signParam.put("cash", format.format(cash));
		signParam.put("server_url", server_url);
		signParam.put("brower_url", brower_url);
		signParam.put("order_id", order_id);
		signParam.put("order_time", order_time);
		signParam.put("pay_type", pay_type);
		signParam.put("sign", sign);
		return signParam;
	}
	
	public String hasSignGetParam() {
		DecimalFormat format = new DecimalFormat("0.00");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("version", version);
		signParam.put("merchant_number", merchant_number);
		signParam.put("cash", format.format(cash));
		signParam.put("server_url", server_url);
		signParam.put("brower_url", brower_url);
		signParam.put("order_id", order_id);
		signParam.put("order_time", order_time);
		signParam.put("pay_type", pay_type);
		signParam.put("sign", sign);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry entry : entrySet) {
            if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	public String generateSign() {
		DecimalFormat format = new DecimalFormat("0.00");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("version", version);
		signParam.put("merchant_number", merchant_number);
		signParam.put("cash", format.format(cash));
		signParam.put("server_url", server_url);
		signParam.put("brower_url", brower_url);
		signParam.put("order_id", order_id);
		signParam.put("order_time", order_time);
		signParam.put("pay_type", pay_type);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry entry : entrySet) {
        	if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
		sb.append("key=" + key);
		System.out.println(sb);
		sign = MD5.md5Str(sb.toString()).toLowerCase();
		System.out.println(sign);
		return sign;
	}
}
