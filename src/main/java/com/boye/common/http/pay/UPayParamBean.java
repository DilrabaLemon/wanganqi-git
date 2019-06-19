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
public class UPayParamBean {
	
	private String user;
	
	private String osn;
	
	private BigDecimal money;
	
	public Map<String, Object> hasSignParamMap() {
		DecimalFormat format = new DecimalFormat("0.00");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("user", user);
		signParam.put("money", format.format(money));
		signParam.put("osn", osn);
		return signParam;
	}
	
	public String hasSignGetParam() {
		DecimalFormat format = new DecimalFormat("0.00");
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("user", user);
		signParam.put("money", format.format(money));
		signParam.put("osn", osn);
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
		return null;
	}
}
