package com.boye.common.http.pay;

import com.boye.common.utils.MD5;
import lombok.Data;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

@Data
public class JjsmPayParamBean {

	private String mchid;

	private String order_sn;

	private Integer fee;

	private String create_ip;

	private String notify_url;

	private String sign;

	private String key;

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("mchid", mchid);
		signParam.put("order_sn", order_sn);
		signParam.put("fee", fee);
		signParam.put("create_ip", create_ip);
		signParam.put("notify_url", notify_url);
		signParam.put("sign", sign);
		return signParam;
	}

	public String hasSignGetParam() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("mchid", mchid);
		signParam.put("order_sn", order_sn);
		signParam.put("fee", fee);
		signParam.put("create_ip", create_ip);
		signParam.put("notify_url", notify_url);
		signParam.put("sign", sign);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Entry entry : entrySet) {
            if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public String generateSign() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("mchid", mchid);
		signParam.put("order_sn", order_sn);
		signParam.put("fee", fee);
		signParam.put("create_ip", create_ip);
		signParam.put("notify_url", notify_url);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Entry entry : entrySet) {
        	if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getValue()).append("&");
        }
		sb.append(key);
		System.out.println(sb);
		sign = MD5.md5Str(sb.toString()).toLowerCase();
		System.out.println(sign);
		return sign;
	}
}
