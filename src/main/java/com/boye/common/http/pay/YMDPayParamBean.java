package com.boye.common.http.pay;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.APISecurityUtils;
import com.boye.common.utils.RSAutil;

//@XmlRootElement(name = "xml")
//@XmlAccessorType(XmlAccessType.FIELD)
public class YMDPayParamBean {
	
	private String MerNo;
	private String BillNo;
	private String Amount;
	private String ReturnURL;
	private String AdviceURL;
	private String SignInfo;
	private String OrderTime;
	private String defaultBankNumber;
	private String payType;
	private String bankCardNo;
	private String Remark;
	private String products;
	
	public Map<String, Object> returnParamMap(){
		Map<String, Object> result = new HashMap<String, Object>();
		if (MerNo != null) result.put("MerNo", MerNo);
		if (BillNo != null) result.put("BillNo", BillNo);
		if (Amount != null) result.put("Amount", Amount);
		if (ReturnURL != null) result.put("ReturnURL", ReturnURL);
		if (AdviceURL != null) result.put("AdviceURL", AdviceURL);
		if (SignInfo != null) result.put("SignInfo", SignInfo);
		if (OrderTime != null) result.put("OrderTime", OrderTime);
		if (defaultBankNumber != null) result.put("defaultBankNumber", defaultBankNumber);
		if (payType != null) result.put("payType", payType);
		if (bankCardNo != null) result.put("bankCardNo", bankCardNo);
		if (Remark != null) result.put("Remark", Remark);
		if (products != null) result.put("products", products);
		return result;
	}
	
	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("MerNo=" + MerNo);
		sb.append("&BillNo=" + BillNo);
		sb.append("&Amount=" + Amount);
		sb.append("&OrderTime=" + OrderTime);
		sb.append("&ReturnURL=" + ReturnURL);
		sb.append("&AdviceURL=" + AdviceURL);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString().toUpperCase();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString().toUpperCase();
	}
	
	public String generateSign(String key) {
		StringBuffer sb = returnParamStr();
		System.out.println(sb.toString());
		String signParam = null;
		try {
			signParam = RSAutil.sign(sb.toString(), RSAutil.getPrivateKey(key));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(signParam);
		return signParam;
	}
	
	public String getMerNo() {
		return MerNo;
	}
	public void setMerNo(String merNo) {
		MerNo = merNo;
	}
	public String getBillNo() {
		return BillNo;
	}
	public void setBillNo(String billNo) {
		BillNo = billNo;
	}
	public String getAmount() {
		return Amount;
	}
	public void setAmount(String amount) {
		Amount = amount;
	}
	public String getReturnURL() {
		return ReturnURL;
	}
	public void setReturnURL(String returnURL) {
		ReturnURL = returnURL;
	}
	public String getAdviceURL() {
		return AdviceURL;
	}
	public void setAdviceURL(String adviceURL) {
		AdviceURL = adviceURL;
	}
	public String getSignInfo() {
		return SignInfo;
	}
	public void setSignInfo(String signInfo) {
		SignInfo = signInfo;
	}
	public String getOrderTime() {
		return OrderTime;
	}

	public void setOrderTime(String orderTime) {
		OrderTime = orderTime;
	}

	public String getDefaultBankNumber() {
		return defaultBankNumber;
	}
	public void setDefaultBankNumber(String defaultBankNumber) {
		this.defaultBankNumber = defaultBankNumber;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getBankCardNo() {
		return bankCardNo;
	}
	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	public String getRemark() {
		return Remark;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public String getProducts() {
		return products;
	}
	public void setProducts(String products) {
		this.products = products;
	}
}
