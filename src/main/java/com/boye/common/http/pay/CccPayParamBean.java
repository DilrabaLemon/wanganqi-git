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
public class CccPayParamBean {
	
	private String mchid;
	
	private String pay_type;
	
	private String notify_url;
	
	private BigDecimal amount;
	
	private String return_url;
	
	private String trade_out_no;
	
	private String noncestr;
	
	private String sign;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("mchid", mchid);
		signParam.put("pay_type", pay_type);
		signParam.put("notify_url", notify_url);
		signParam.put("return_url", return_url);
		signParam.put("trade_out_no", trade_out_no);
		signParam.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
		signParam.put("noncestr", noncestr);
		signParam.put("sign", sign);
		return signParam;
	}
	
	public String hasSignGetParam() {
		Map<String, Object> signParam = hasSignParamMap();
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
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("mchid", mchid);
		signParam.put("pay_type", pay_type);
		signParam.put("notify_url", notify_url);
		signParam.put("return_url", return_url);
		signParam.put("trade_out_no", trade_out_no);
		signParam.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue());
		signParam.put("noncestr", noncestr);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry entry : entrySet) {
        	if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
		sb.append("key=" + key);
		System.out.println(sb);
		sign = MD5.md5Str(sb.toString()).toUpperCase();
		System.out.println(sign);
		return sign;
	}
}
