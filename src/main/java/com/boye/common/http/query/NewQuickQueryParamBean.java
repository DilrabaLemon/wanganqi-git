package com.boye.common.http.query;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class NewQuickQueryParamBean {
	
	private String version;
	private String orgNo;
	private String custId;
	private String custOrderNo;
	private String key;
	
	private String sign;

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

	public String getCustOrderNo() {
		return custOrderNo;
	}

	public void setCustOrderNo(String custOrderNo) {
		this.custOrderNo = custOrderNo;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("custId =" + custId );
		sb.append("&custOrderNo=" + custOrderNo);
		sb.append("&orgNo=" + orgNo);
		sb.append("&version=" + version);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam);
		System.out.println(sign);
		return sign;
	}
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sign" , sign);
		result.put("version" , version);
		result.put("orgNo" , orgNo);
		result.put("custId" , custId);
		result.put("custOrderNo" , custOrderNo);
		return result;
	}
}
