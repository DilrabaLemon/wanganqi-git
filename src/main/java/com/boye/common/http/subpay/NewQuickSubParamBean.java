package com.boye.common.http.subpay;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.boye.common.utils.MD5;

public class NewQuickSubParamBean {
	
	private String version;//版本号
	private String orgNo;//机构号
	private String custId;//商户号
	private String custOrdNo;//商户订单号
	private String casType;//交易类型
	private Long casAmt;//金额，正整数，单位为分
	private String deductWay;//手续费扣除方式
	private String callBackUrl;//用于接收开联通的交易结果通知 
	private String accountName;//收款方姓名
	private String cardNo;//收款方卡号	
	private String bankName;//收款方开户行名称
	private String subBankName;//收款方开户行支行名称
	private String accountType;//收款方账户类型
	private String cnapsCode;//联行号 
	
	private String sign;//商户付款订单日期
	private String key;//秘钥
	
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
		String signParam =sb.toString() + "key=" + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam).toUpperCase();
		System.out.println(sign);
		return sign;
	}
	
	public Map<String, Object> returnParamStr() {
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		
		paramMap.put("version", version);
		paramMap.put("orgNo", orgNo);
		paramMap.put("custId", custId);
		if (custOrdNo != null) paramMap.put("custOrdNo", custOrdNo);
		if (casType != null) paramMap.put("casType", casType);
		if (casAmt != null) paramMap.put("casAmt", casAmt);
		if (deductWay != null) paramMap.put("deductWay", deductWay);
		if (accountName != null) paramMap.put("accountName", accountName);
		if (accountType != null) paramMap.put("accountType", accountType);
		if (callBackUrl != null) paramMap.put("callBackUrl", callBackUrl);
		if (bankName != null) paramMap.put("bankName", bankName);
		if (cardNo != null) paramMap.put("cardNo", cardNo);
		if (subBankName != null) paramMap.put("subBankName", subBankName);
		if (cnapsCode != null) paramMap.put("cnapsCode", cnapsCode);
		
		return paramMap;
	}
	
	public String hasSignParam() {
		return null;
	}
	
	public String notSignParam() {
		return null;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
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

	public String getCustOrdNo() {
		return custOrdNo;
	}

	public void setCustOrdNo(String custOrdNo) {
		this.custOrdNo = custOrdNo;
	}

	public String getCasType() {
		return casType;
	}

	public void setCasType(String casType) {
		this.casType = casType;
	}

	public Long getCasAmt() {
		return casAmt;
	}

	public void setCasAmt(Long casAmt) {
		this.casAmt = casAmt;
	}

	public String getDeductWay() {
		return deductWay;
	}

	public void setDeductWay(String deductWay) {
		this.deductWay = deductWay;
	}

	public String getCallBackUrl() {
		return callBackUrl;
	}

	public void setCallBackUrl(String callBackUrl) {
		this.callBackUrl = callBackUrl;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getSubBankName() {
		return subBankName;
	}

	public void setSubBankName(String subBankName) {
		this.subBankName = subBankName;
	}

	public String getCnapsCode() {
		return cnapsCode;
	}

	public void setCnapsCode(String cnapsCode) {
		this.cnapsCode = cnapsCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	
}
