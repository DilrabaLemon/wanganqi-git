package com.boye.common.http.subpay;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class QuickSubParamBean {
	
	private String insCode;//机构号
	
	private String insMerchantCode;//机构商户编号
	
	private String hpMerCode;//瀚银商户号
	
	private String orderNo;//商户付款订单号
	
	private String orderDate;//商户付款订单日期
	
	private String orderTime;//商户付款付款时间
	
	private String currencyCode;//订单币种
	
	private String orderAmount;//订单金额	
	
	private String orderType;//订单类型
	
	private String certType;//证件类型
	
	private String accountType;//账户类型
	
	private String accountName;//账户名
	
	private String accountNumber;//卡号
	
	private String certNumber;//证件号
	
	private String mainBankName;//总行名称

	private String mainBankCode;//总行号
	
	private String openBranchBankName;//开户行名称
	
	private String mobile;//手机号
	
	private String attach;//商户私有域
	
	private String nonceStr;//随机参数
	
	private String signature;//签名
	
	private String signKey;//秘钥
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("insCode", insCode);
		result.put("insMerchantCode", insMerchantCode);
		result.put("currencyCode", currencyCode);
		result.put("hpMerCode", hpMerCode);
		result.put("orderNo", orderNo);
		result.put("orderDate", orderDate);
		result.put("orderTime", orderTime);
		result.put("orderType", orderType);
		result.put("currencyCode", currencyCode);
		result.put("orderAmount", orderAmount);
		result.put("certType", certType);
		result.put("accountType", accountType);
		result.put("accountName", accountName);
		result.put("accountNumber", accountNumber);
		result.put("mainBankName", mainBankName);
		result.put("mainBankCode", mainBankCode);
		result.put("openBranchBankName", openBranchBankName);
		result.put("mobile", mobile);
		result.put("attach", attach);
		result.put("certNumber", certNumber);
		result.put("nonceStr", nonceStr);
		result.put("signature", signature);
		return result;
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + signKey;
		System.out.println(signParam);
		signature = MD5.md5Str(signParam).toUpperCase();
		System.out.println(signature);
		return signature;
	}
	
	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append(insCode);
		sb.append(insMerchantCode);
		sb.append(hpMerCode);
		sb.append(orderNo);
		sb.append(orderDate);
		sb.append(orderTime);
		sb.append(currencyCode);
		sb.append(orderAmount);
		sb.append(orderType);
		sb.append(accountType);
		sb.append(accountName);
		sb.append(accountNumber);
		sb.append(nonceStr);
		System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		result.append("&sign=" + signature);
		return result.toString().toUpperCase();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}

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

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getCurrencyCode() {
		return currencyCode;
	}

	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}

	public String getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(String orderAmount) {
		this.orderAmount = orderAmount;
	}

	public String getSignKey() {
		return signKey;
	}

	public void setSignKey(String signKey) {
		this.signKey = signKey;
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

	public String getOrderDate() {
		return orderDate;
	}

	public void setOrderDate(String orderDate) {
		this.orderDate = orderDate;
	}

	public String getOrderType() {
		return orderType;
	}

	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getCertType() {
		return certType;
	}

	public void setCertType(String certType) {
		this.certType = certType;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getCertNumber() {
		return certNumber;
	}

	public void setCertNumber(String certNumber) {
		this.certNumber = certNumber;
	}

	public String getMainBankName() {
		return mainBankName;
	}

	public void setMainBankName(String mainBankName) {
		this.mainBankName = mainBankName;
	}

	public String getMainBankCode() {
		return mainBankCode;
	}

	public void setMainBankCode(String mainBankCode) {
		this.mainBankCode = mainBankCode;
	}

	public String getOpenBranchBankName() {
		return openBranchBankName;
	}

	public void setOpenBranchBankName(String openBranchBankName) {
		this.openBranchBankName = openBranchBankName;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAttach() {
		return attach;
	}

	public void setAttach(String attach) {
		this.attach = attach;
	}
}
