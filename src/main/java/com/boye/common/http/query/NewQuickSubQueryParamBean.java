package com.boye.common.http.query;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class NewQuickSubQueryParamBean {
	
	// 报文内容（content）
	private String orgNo;
	private String custId;
	private String version;
	private String casOrdNo;
	private String custOrdNo;
	private String sign;
	
	// 特殊
	private String key;
	
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getCasOrdNo() {
		return casOrdNo;
	}

	public void setCasOrdNo(String casOrdNo) {
		this.casOrdNo = casOrdNo;
	}

	public String getCustOrdNo() {
		return custOrdNo;
	}

	public void setCustOrdNo(String custOrdNo) {
		this.custOrdNo = custOrdNo;
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
//		sb.append("casOrdNo=" + casOrdNo);
		sb.append("custId=" + custId);
		sb.append("&custOrdNo=" + custOrdNo);
		sb.append("&orgNo=" + orgNo);
		sb.append("&version=" + version);
		System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + "&key=" + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam).toUpperCase();
		System.out.println(sign);
		return sign;
	}
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("sign" , sign);
		result.put("orgNo" , orgNo);
		result.put("custId" , custId);
		result.put("version" , version);
//		result.put("casOrdNo" , casOrdNo);
		result.put("custOrdNo" , custOrdNo);
		
		return result;
	}
}
