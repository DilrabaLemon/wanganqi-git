package com.boye.common.http.query;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.boye.common.utils.MD5;
import com.google.gson.Gson;

public class NewQuickQueryBalanceParamBean {
	
	private String version;
	private String orgNo;
	private String custId;
	private String sign;
	
	//特殊
	private String key; //密钥

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getOrgNo() {
		return orgNo;
	}

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}

	public String getCustId() {
		return custId;
	}

	public void setCustId(String custId) {
		this.custId = custId;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("version", version);
		result.put("orgNo", orgNo);
		result.put("custId", custId);
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
//	
	public Map<String, Object> returnParamStr() {
		Map<String, Object> result = new TreeMap<String, Object>();
		if (version != null) result.put("version" , version);
		if (orgNo != null) result.put("orgNo" , orgNo);
		if (custId != null) result.put("custId" , custId);
		return result;
	}
	
	public String hasSignParam() {
		return null;
	}
	
	public String notSignParam() {
		return null;
	}
	
}
