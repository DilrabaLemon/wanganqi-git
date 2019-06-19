package com.boye.common.http.pay;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class NewQuickPayParamBean {
	
	private String version;
	private String orgNo;
	private String custId;
	private String custOrderNo;
	private String tranType;
	private String backUrl;
	private String frontUrl;
	private String payAmt;
	private String cardNo;
	private String bankCode;
	private String goodsName;
	private String orderDesc;
	private String buyIp;
	private String sign;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = returnParamStr();
		result.put("sign", sign);
		return result;
	}
	
	public String generateSign() {
		StringBuffer sb = new StringBuffer();
        Set<Entry<String, Object>> entrySet = returnParamStr().entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getValue() != null)
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
		String signParam = sb.toString() + "key=" + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam);
		return sign;
	}
	
	public Map<String, Object> returnParamStr() {
		Map<String, Object> result = new TreeMap<String, Object>();
		result.put("cardNo" , cardNo);
		if (version != null) result.put("version" , version);
		if (orgNo != null) result.put("orgNo" , orgNo);
		if (custId != null) result.put("custId" , custId);
		if (custOrderNo != null) result.put("custOrderNo" , custOrderNo);
		if (tranType != null) result.put("tranType" , tranType);
		if (backUrl != null) result.put("backUrl" , backUrl);
		if (frontUrl != null) result.put("frontUrl" , frontUrl);
		if (payAmt != null) result.put("payAmt" , payAmt);
		if (bankCode != null) result.put("bankCode" , bankCode);
		if (goodsName != null) result.put("goodsName" , goodsName);
		if (orderDesc != null) result.put("orderDesc" , orderDesc);
		if (buyIp != null) result.put("buyIp" , buyIp);
		return result;
	}
	
	public String hasSignParam() {
		return null;
	}
	
	public String notSignParam() {
		return null;	
	}
}
