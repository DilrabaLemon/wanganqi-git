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
public class HLPayParamBean {
	//汇联支付
	
	private String order_id;
	
	private String merchant_id;
	
	private BigDecimal order_amt;
	
	private String card_type;
	
	private String return_url;
	
	private String bg_url;
	
	private String biz_code;
	
	private String bank_code;
	
	private String extra;
	
	private String sign;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("order_id", order_id);
		signParam.put("merchant_id", merchant_id);
		signParam.put("order_amt", order_amt.multiply(BigDecimal.valueOf(100)).intValue() + "");
		signParam.put("card_type", card_type);
		signParam.put("return_url", return_url);
		signParam.put("bg_url", bg_url);
		signParam.put("biz_code", biz_code);
		signParam.put("bank_code", bank_code);
		signParam.put("extra", extra);
		signParam.put("sign", sign);
		return signParam;
	}
	
	public String hasSignGetParam() {
//		DecimalFormat format = new DecimalFormat("0.00");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("order_id", order_id);
		signParam.put("merchant_id", merchant_id);
		signParam.put("order_amt", order_amt.multiply(BigDecimal.valueOf(100)).intValue() + "");
		signParam.put("card_type", card_type);
		signParam.put("return_url", return_url);
		signParam.put("bg_url", bg_url);
		signParam.put("biz_code", biz_code);
		signParam.put("bank_code", bank_code);
		signParam.put("extra", extra);
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
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("order_id", order_id);
		signParam.put("merchant_id", merchant_id);
		signParam.put("order_amt", order_amt.multiply(BigDecimal.valueOf(100)).intValue() + "");
		signParam.put("card_type", card_type);
		signParam.put("return_url", return_url);
		signParam.put("bg_url", bg_url);
		signParam.put("biz_code", biz_code);
//		signParam.put("bank_code", bank_code);
//		signParam.put("extra", extra);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry entry : entrySet) {
        	if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
		sb.append(key);
		System.out.println(sb);
		sign = MD5.md5Str(sb.toString()).toUpperCase();
		System.out.println(sign);
		return sign;
	}
}
