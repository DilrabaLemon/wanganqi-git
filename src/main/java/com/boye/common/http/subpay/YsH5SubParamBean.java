package com.boye.common.http.subpay;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.boye.common.utils.MD5;
import com.google.gson.Gson;

public class YsH5SubParamBean {
	
	private String sign_type;
	private String signature;
	private String biz_content;
	
	private String version;
	private String mch_id;
	private String out_order_no;
	private String payee_acct_no;
	private String payee_acct_name;
	private int payment_fee;
	private String card_type ;
	private String payee_acct_type;
	private String settle_type;
	private String cvv2;
	private String payee_branch_no;
	private String payee_branch_name;
	private String remark;
	private String notify_url;
	private String idcard_no;
	private String mobile;
	private String bank_code;
	
	//特殊
	private String key; //密钥
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMch_id() {
		return mch_id;
	}

	public void setMch_id(String mch_id) {
		this.mch_id = mch_id;
	}

	public String getOut_order_no() {
		return out_order_no;
	}

	public void setOut_order_no(String out_order_no) {
		this.out_order_no = out_order_no;
	}

	public int getPayment_fee() {
		return payment_fee;
	}

	public void setPayment_fee(int payment_fee) {
		this.payment_fee = payment_fee;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getSign_type() {
		return sign_type;
	}
	
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	
	public String getSignature() {
		return signature;
	}
	
	public void setSignature(String signature) {
		this.signature = signature;
	}
	
	public String getBiz_content() {
		return biz_content;
	}

	public void setBiz_content(String biz_content) {
		this.biz_content = biz_content;
	}

	public String getPayee_acct_no() {
		return payee_acct_no;
	}

	public void setPayee_acct_no(String payee_acct_no) {
		this.payee_acct_no = payee_acct_no;
	}

	public String getPayee_acct_name() {
		return payee_acct_name;
	}

	public void setPayee_acct_name(String payee_acct_name) {
		this.payee_acct_name = payee_acct_name;
	}

	public String getCard_type() {
		return card_type;
	}

	public void setCard_type(String card_type) {
		this.card_type = card_type;
	}

	public String getPayee_acct_type() {
		return payee_acct_type;
	}

	public void setPayee_acct_type(String payee_acct_type) {
		this.payee_acct_type = payee_acct_type;
	}

	public String getSettle_type() {
		return settle_type;
	}

	public void setSettle_type(String settle_type) {
		this.settle_type = settle_type;
	}

	public String getCvv2() {
		return cvv2;
	}

	public void setCvv2(String cvv2) {
		this.cvv2 = cvv2;
	}

	public String getPayee_branch_no() {
		return payee_branch_no;
	}

	public void setPayee_branch_no(String payee_branch_no) {
		this.payee_branch_no = payee_branch_no;
	}

	public String getPayee_branch_name() {
		return payee_branch_name;
	}

	public void setPayee_branch_name(String payee_branch_name) {
		this.payee_branch_name = payee_branch_name;
	}

	public String getIdcard_no() {
		return idcard_no;
	}

	public void setIdcard_no(String idcard_no) {
		this.idcard_no = idcard_no;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("sign_type", sign_type);
		result.put("signature", signature);
		result.put("biz_content", biz_content);
		
		return result;
	}
	
	public String generateSign() {
		Map<String, Object> param = new TreeMap<String, Object>();
		Gson gson = new Gson();
		param.put("version", version);
		param.put("mch_id", mch_id);
		param.put("out_order_no", out_order_no);
		param.put("payee_acct_no", payee_acct_no);
		param.put("payee_acct_name", payee_acct_name);
		param.put("payment_fee", payment_fee);
		param.put("card_type", card_type);
		param.put("payee_acct_type", payee_acct_type);
		param.put("settle_type", settle_type);
		param.put("cvv2", cvv2);
		param.put("payee_branch_no", payee_branch_no);
		param.put("payee_branch_name", payee_branch_name);
		param.put("remark", remark);
		param.put("notify_url", notify_url);
		param.put("idcard_no", idcard_no);
		param.put("mobile", mobile);
		param.put("bank_code", bank_code);
		
		biz_content = gson.toJson(param);
		String signParam = "biz_content=" + biz_content + "&key=" + key;
		System.out.println(signParam);
		signature = MD5.md5Str(signParam).toUpperCase();
		System.out.println(signature);
		return signature;
	}
//	
	public StringBuffer returnParamStr() {
		return null;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString().toUpperCase();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}
	
}
