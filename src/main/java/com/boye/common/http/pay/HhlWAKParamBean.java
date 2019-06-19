package com.boye.common.http.pay;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class HhlWAKParamBean {
	
	private String cope_pay_amount; //订单金额
	private String merchant_open_id;   //商户号
	private String merchant_order_number; //订单号
	private String notify_url;  //后台通知地址URL
	private String pay_wap_mark;    //产品类型
	private String subject; //订单名称
	private String timestamp;  //订单时间
	
	private String acc_no; //银行卡号
	private String pay_type;
	
	
	private String sign;   //签名信息
	private String key; //密钥

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("acc_no" , acc_no);
		result.put("cope_pay_amount" , cope_pay_amount);
		result.put("merchant_open_id" , merchant_open_id);
		result.put("merchant_order_number" , merchant_order_number);
		result.put("notify_url" , notify_url);
		result.put("pay_type" , pay_type);
		result.put("pay_wap_mark" , pay_wap_mark);
		result.put("subject" , subject);
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
		sb.append("&cope_pay_amount=" + cope_pay_amount);
		sb.append("&merchant_open_id=" + merchant_open_id);
		sb.append("&merchant_order_number=" + merchant_order_number);
		sb.append("&notify_url=" + notify_url);
		sb.append("&pay_type=" + pay_type);
		sb.append("&pay_wap_mark=" + pay_wap_mark);
		sb.append("&subject=" + subject);
		sb.append("&timestamp=" + timestamp);
		System.out.println(sb);
		return sb;
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
