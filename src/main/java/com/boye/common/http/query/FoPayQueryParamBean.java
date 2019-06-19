package com.boye.common.http.query;

import java.text.DecimalFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class FoPayQueryParamBean {
	
	private String company_no;
	
	private String format;
	
	private String scharset;
	
	private String sign_type;
	
	private String sign;
	
	private String keyword;
	
	private String page;
	
	private String page_size;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("company_no", company_no);
		signParam.put("format", format);
		signParam.put("scharset", scharset);
		signParam.put("keyword", keyword);
		signParam.put("page", page);
		signParam.put("page_size", page_size);
		signParam.put("sign", sign);
		signParam.put("sign_type", sign_type);
		return signParam;
	}
	
	public String hasSignGetParam() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("company_no", company_no);
		signParam.put("format", format);
		signParam.put("scharset", scharset);
		signParam.put("keyword", keyword);
		signParam.put("page", page);
		signParam.put("page_size", page_size);
		signParam.put("sign", sign);
		signParam.put("sign_type", sign_type);
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
		signParam.put("company_no", company_no);
		signParam.put("format", format);
		signParam.put("scharset", scharset);
		signParam.put("keyword", keyword);
		signParam.put("page", page);
		signParam.put("page_size", page_size);
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
