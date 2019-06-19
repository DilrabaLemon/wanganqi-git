package com.boye.common.http.subpay;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class FoPaySubParamBean {
	
	private String company_no;
	
	private String format;
	
	private String charset;
	
	private String sign_type;
	
	private String sign;
	
	private String order_sn;
	
	private String order_amount;
	
	private String notify_url;
	
	private String pay_method;
	
	private String phone_no;
	
	private String ali_user_name;
	
	private String wx_user_name;
	
	private String customer_name;
	
	private String acc_bank_name;
	
	private String acct_no;
	
	private String key;

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("company_no", company_no);
		signParam.put("charset", charset);
		signParam.put("sign_type", sign_type);
		signParam.put("sign", sign);
		signParam.put("format", format);
		signParam.put("order_sn", order_sn);
		signParam.put("order_amount", order_amount.toString());
		signParam.put("notify_url", notify_url);
		signParam.put("pay_method", pay_method);
		signParam.put("phone_no", phone_no);
		signParam.put("ali_user_name", ali_user_name);
		signParam.put("wx_user_name", wx_user_name);
		signParam.put("customer_name", customer_name);
		signParam.put("acc_bank_name", acc_bank_name);
		signParam.put("acct_no", acct_no);
		return signParam;
	}
	
	public String generateSign() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("company_no", company_no);
		signParam.put("charset", charset);
		signParam.put("format", format);
		signParam.put("order_sn", order_sn);
		signParam.put("order_amount", order_amount.toString());
		signParam.put("notify_url", notify_url);
		signParam.put("pay_method", pay_method);
		signParam.put("phone_no", phone_no);
		signParam.put("ali_user_name", ali_user_name);
		signParam.put("wx_user_name", wx_user_name);
		signParam.put("customer_name", customer_name);
		signParam.put("acc_bank_name", acc_bank_name);
		signParam.put("acct_no", acct_no);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry entry : entrySet) {
            if (entry.getValue() != null)
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		sb.append(key);
		System.out.println(sb);
		sign = MD5.md5Str(sb.toString());
		System.out.println(sign);
		return sign;
	}
}
