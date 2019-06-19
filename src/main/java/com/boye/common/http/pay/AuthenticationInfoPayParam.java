package com.boye.common.http.pay;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.base.constant.EncryptionUtils;
import com.boye.bean.vo.AuthenticationInfo;

public class AuthenticationInfoPayParam extends AuthenticationInfo {
	
	private String key;
	
//	public String getSignParam() {
//		StringBuffer result = new StringBuffer();
//		if (shop_phone == null) result.append("SHOPPHONE=&");
//		else result.append("SHOPPHONE=" + shop_phone + "&");
//		if (order_number == null) result.append("ORDERNUMBER=&");
//		else result.append("ORDERNUMBER=" + order_number + "&");
//		if (passageway_code == null) result.append("PASSAGEWAYCODE=&");
//		else result.append("PASSAGEWAYCODE=" + passageway_code + "&");
//		if (payment == null) result.append("PAYMENT=&");
//		else result.append("PAYMENT=" + payment + "&");
//		return result.toString();
//	}
//	
	public String getNewSignParam() {
		StringBuffer result = new StringBuffer();
		if (order_number == null) result.append("order_number=&");
		else result.append("order_number=" + order_number + "&");
		if (passageway_code == null) result.append("passageway_code=&");
		else result.append("passageway_code=" + passageway_code + "&");
		if (payment == null) result.append("payment=&");
		else result.append("payment=" + payment + "&");
		if (shop_phone == null) result.append("shop_phone=&");
		else result.append("shop_phone=" + shop_phone);
		return result.toString();
		
	}
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("shop_phone", shop_phone);
		signParam.put("order_number", order_number);
		signParam.put("payment", payment);
		signParam.put("passageway_code", passageway_code);
		signParam.put("notify_url", notify_url);
		signParam.put("bank_card_number", bank_card_number);
		signParam.put("sign", sign);
		return signParam;
	}
	
	public String hasSignGetParam() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("shop_phone", shop_phone);
		signParam.put("order_number", order_number);
		signParam.put("payment", payment);
		signParam.put("passageway_code", passageway_code);
		signParam.put("notify_url", notify_url);
		signParam.put("bank_card_number", bank_card_number);
		signParam.put("sign", sign);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	public String generateSign() {
		String signParam = this.getNewSignParam();
		System.out.println(signParam);
		return EncryptionUtils.sign(signParam, key, "SHA-256");
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
