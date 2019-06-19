package com.boye.common.http.query;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class QuickQueryParamBean {

	private String insCode;
	
	private String insMerchantCode;
	
	private String hpMerCode;
	
	private String orderNo;
	
	private String transDate;
	
	private String transSeq;
	
	private String productType;
	
	private String paymentType;
	
	private String nonceStr;
	
	private String signature;
	
	private String key;

	public String getInsCode() {
		return insCode;
	}

	public void setInsCode(String insCode) {
		this.insCode = insCode;
	}

	public String getInsMerchantCode() {
		return insMerchantCode;
	}

	public void setInsMerchantCode(String insMerchantCode) {
		this.insMerchantCode = insMerchantCode;
	}

	public String getHpMerCode() {
		return hpMerCode;
	}

	public void setHpMerCode(String hpMerCode) {
		this.hpMerCode = hpMerCode;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getTransDate() {
		return transDate;
	}

	public void setTransDate(String transDate) {
		this.transDate = transDate;
	}

	public String getTransSeq() {
		return transSeq;
	}

	public void setTransSeq(String transSeq) {
		this.transSeq = transSeq;
	}

	public String getProductType() {
		return productType;
	}

	public void setProductType(String productType) {
		this.productType = productType;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append(insCode);
		sb.append(insMerchantCode);
		sb.append(hpMerCode);
		sb.append(orderNo);
		sb.append(transDate);
		sb.append(transSeq);
		sb.append(productType);
		sb.append(paymentType);
		sb.append(nonceStr);
		return sb;
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + key;
		System.out.println(signParam);
		signature = MD5.md5Str(signParam).toUpperCase();
		System.out.println(signature);
		return signature;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("insCode" , insCode);
		result.put("insMerchantCode" , insMerchantCode);
		result.put("hpMerCode" , hpMerCode);
		result.put("orderNo" , orderNo);
		result.put("transDate" , transDate);
		result.put("transSeq" , transSeq);
		result.put("productType" , productType);
		result.put("paymentType" , paymentType);
		result.put("nonceStr" , nonceStr);
		result.put("signature" , signature);
		return result;
	}
}
