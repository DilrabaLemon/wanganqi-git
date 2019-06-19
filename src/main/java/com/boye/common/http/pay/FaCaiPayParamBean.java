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
public class FaCaiPayParamBean {
	
	private String attach;
	
	private String callbackurl;
	
	private String hrefbackurl;
	
	private String orderid;
	
	private String ordertype;
	
	private String payerIp;
	
	private String merchant;
	
	private BigDecimal value;
	
	private String sign;
	
	private String type;

	private String FullName;
	
	private String IDCard;
	
	private String CardNo;
	
	private String Phone;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		DecimalFormat format = new DecimalFormat("0.00");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("attach", attach);
		signParam.put("callbackurl", callbackurl);
		signParam.put("hrefbackurl", hrefbackurl);
		signParam.put("orderid", orderid);
		signParam.put("ordertype", ordertype);
		signParam.put("payerIp", payerIp);
		signParam.put("merchant", merchant);
		signParam.put("value", format.format(value));
		signParam.put("type", type);
		signParam.put("FullName", FullName);
		signParam.put("IDCard", IDCard);
		signParam.put("CardNo", CardNo);
		signParam.put("Phone", Phone);
		signParam.put("sign", sign);
		return signParam;
	}
	
	public String hasSignGetParam() {
		DecimalFormat format = new DecimalFormat("0.00");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("attach", attach);
		signParam.put("callbackurl", callbackurl);
		signParam.put("hrefbackurl", hrefbackurl);
		signParam.put("orderid", orderid);
		signParam.put("ordertype", ordertype);
		signParam.put("payerIp", payerIp);
		signParam.put("merchant", merchant);
		signParam.put("value", format.format(value));
		signParam.put("type", type);
		signParam.put("FullName", FullName);
		signParam.put("IDCard", IDCard);
		signParam.put("CardNo", CardNo);
		signParam.put("Phone", Phone);
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
		signParam.put("attach", attach);
		signParam.put("callbackurl", callbackurl);
		signParam.put("hrefbackurl", hrefbackurl);
		signParam.put("orderid", orderid);
		signParam.put("ordertype", ordertype);
		signParam.put("payerIp", payerIp);
		signParam.put("merchant", merchant);
		signParam.put("value", format.format(value));
		signParam.put("type", type);
		signParam.put("FullName", FullName);
		signParam.put("IDCard", IDCard);
		signParam.put("CardNo", CardNo);
		signParam.put("Phone", Phone);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry entry : entrySet) {
        	if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		sb.append(key);
		System.out.println(sb);
		sign = MD5.md5Str(sb.toString()).toLowerCase();
		System.out.println(sign);
		return sign;
	}
}
