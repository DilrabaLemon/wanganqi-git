package com.boye.common.http.subpay;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.boye.common.utils.MD5;

public class KltSubParamBean {
	
	private String version;//版本号
	private String merchantId;//商户号
	private String transactType;//交易类型
	private String signType;//签名类型，0：数字证书 1：md5
	private String mchtOrderNo;//商户订单号
	private String orderDateTime;//商户订单时间，yyyyMMddHHmmss
	private String accountNo;//收款方账号	
	private String accountName;//收款方姓名
	private String accountType;//收款方账户类型
	private String bankNo;//收款方开户行行号 000000000000
	private String bankName;//收款方开户行名称
	private Long amt;//金额，正整数，单位为分
	private String purpose;//用途
	private String remark;//备注
	private String notifyUrl;//用于接收开联通的交易结果通知 
	
	private String sign;//商户付款订单日期
	private String key;//秘钥
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> head = new HashMap<String, Object>();
		Map<String, Object> content = new HashMap<String, Object>();
		
		head.put("version", version);
		head.put("merchantId", merchantId);
		head.put("signType", signType);
		head.put("sign", sign);
		
		if (amt != null) content.put("amt", amt);
		if (mchtOrderNo != null) content.put("mchtOrderNo", mchtOrderNo);
		if (orderDateTime != null) content.put("orderDateTime", orderDateTime);
		if (accountNo != null) content.put("accountNo", accountNo);
		if (accountName != null) content.put("accountName", accountName);
		if (accountType != null) content.put("accountType", accountType);
		if (bankNo != null) content.put("bankNo", bankNo);
		if (bankName != null) content.put("bankName", bankName);
		if (purpose != null) content.put("purpose", purpose);
		if (remark != null) content.put("remark", remark);
		if (notifyUrl != null) content.put("notifyUrl", notifyUrl);
		
		result.put("head" , head);
		result.put("content" , content);
		return result;
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + "&key=" + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam).toUpperCase();
		System.out.println(sign);
		return sign;
	}
	
	public StringBuffer returnParamStr() {
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		
		paramMap.put("version", version);
		paramMap.put("merchantId", merchantId);
		paramMap.put("signType", signType);
		
		if (amt != null) paramMap.put("amt", amt);
		if (mchtOrderNo != null) paramMap.put("mchtOrderNo", mchtOrderNo);
		if (orderDateTime != null) paramMap.put("orderDateTime", orderDateTime);
		if (accountNo != null) paramMap.put("accountNo", accountNo);
		if (accountName != null) paramMap.put("accountName", accountName);
		if (accountType != null) paramMap.put("accountType", accountType);
		if (bankNo != null) paramMap.put("bankNo", bankNo);
		if (bankName != null) paramMap.put("bankName", bankName);
		if (purpose != null) paramMap.put("purpose", purpose);
		if (remark != null) paramMap.put("remark", remark);
		if (notifyUrl != null) paramMap.put("notifyUrl", notifyUrl);
		StringBuffer sb = new StringBuffer();
        Set<Entry<String, Object>> entrySet = paramMap.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getValue() != null)
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
        System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		result.append("&sign=" + sign);
		return result.toString().toUpperCase();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
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

	public String getTransactType() {
		return transactType;
	}

	public void setTransactType(String transactType) {
		this.transactType = transactType;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getMchtOrderNo() {
		return mchtOrderNo;
	}

	public void setMchtOrderNo(String mchtOrderNo) {
		this.mchtOrderNo = mchtOrderNo;
	}

	public String getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

	public String getAccountNo() {
		return accountNo;
	}

	public void setAccountNo(String accountNo) {
		this.accountNo = accountNo;
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

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getAmt() {
		return amt;
	}

	public void setAmt(Long amt) {
		this.amt = amt;
	}

	public String getPurpose() {
		return purpose;
	}

	public void setPurpose(String purpose) {
		this.purpose = purpose;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
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
