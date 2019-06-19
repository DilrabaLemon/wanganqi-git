package com.boye.common.http.subpay;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

public class HhlQuickSubParamBean {
	
	private String acc_no;
	
	private String bank_name;
	
	private String cash_amount;
	
	private String district;
	
	private String id_number;
	
	private String merchant_open_id;
	
	private String name;
	
	private String sign;
	
	private String tel_no;
	
	private String timestamp;
	
	private String key;
	

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("cash_amount" , cash_amount);
		result.put("merchant_open_id" , merchant_open_id);
		result.put("acc_no" , acc_no);
		result.put("bank_name" , bank_name);
		result.put("district" , district);
		result.put("id_number" , id_number);
		result.put("name" , name);
		result.put("tel_no" , tel_no);
		result.put("timestamp" , timestamp);
		result.put("sign", sign);
		return result;
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam);
		return sign;
	}
	
	public StringBuffer returnParamStr() {
		StringBuffer sb = new StringBuffer();
		sb.append("acc_no=" + acc_no);
		sb.append("&bank_name=" + bank_name);
		sb.append("&cash_amount=" + cash_amount);
		sb.append("&district=" + district);
		sb.append("&id_number=" + id_number);
		sb.append("&merchant_open_id=" + merchant_open_id);
		sb.append("&name=" + name);
		 sb.append("&tel_no=" + tel_no);
		sb.append("&timestamp=" + timestamp);
		System.out.println(sb);
		return sb;
	}
	
	public String hasSignParam() {
		StringBuffer result = returnParamStr();
		result.append("&sign=" + sign);
		return result.toString();
	}
	
	public String notSignParam() {
		StringBuffer result = returnParamStr();
		return result.toString();
	}

	public String getAcc_no() {
		return acc_no;
	}

	public void setAcc_no(String acc_no) {
		this.acc_no = acc_no;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getCash_amount() {
		return cash_amount;
	}

	public void setCash_amount(String cash_amount) {
		this.cash_amount = cash_amount;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getId_number() {
		return id_number;
	}

	public void setId_number(String id_number) {
		this.id_number = id_number;
	}

	public String getMerchant_open_id() {
		return merchant_open_id;
	}

	public void setMerchant_open_id(String merchant_open_id) {
		this.merchant_open_id = merchant_open_id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

//	public String getTel_no() {
//		return tel_no;
//	}
//
//	public void setTel_no(String tel_no) {
//		this.tel_no = tel_no;
//	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	

}
