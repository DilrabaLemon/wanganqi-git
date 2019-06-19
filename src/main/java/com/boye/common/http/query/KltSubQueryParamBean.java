package com.boye.common.http.query;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class KltSubQueryParamBean {
	
	// 报文内容（content）
	private String mchtOrderNo;
	private String paymentBusinessType;
	
	// 报文头（head）
	private String version;
	private String merchantId;
	private String signType;
	private String sign;
	
	// 特殊
	private String key;
	
	public String getMchtOrderNo() {
		return mchtOrderNo;
	}

	public void setMchtOrderNo(String mchtOrderNo) {
		this.mchtOrderNo = mchtOrderNo;
	}

	public String getPaymentBusinessType() {
		return paymentBusinessType;
	}

	public void setPaymentBusinessType(String paymentBusinessType) {
		this.paymentBusinessType = paymentBusinessType;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
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
		sb.append("mchtOrderNo=" + mchtOrderNo);
		sb.append("&merchantId=" + merchantId);
		sb.append("&paymentBusinessType=" + paymentBusinessType);
		sb.append("&signType=" + signType);
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
		Map<String, Object> head = new HashMap<String, Object>();
		Map<String, Object> content = new HashMap<String, Object>();
		
		head.put("version", version);
		head.put("merchantId", merchantId);
		head.put("signType", signType);
		head.put("sign", sign);
		
		content.put("mchtOrderNo", mchtOrderNo);
		content.put("paymentBusinessType", paymentBusinessType);
		
		result.put("head" , head);
		result.put("content" , content);
		return result;
	}
}
