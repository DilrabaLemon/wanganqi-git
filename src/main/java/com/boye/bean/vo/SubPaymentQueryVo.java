package com.boye.bean.vo;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.Data;

@Data
public class SubPaymentQueryVo {
	
	private String shop_phone;// 商户手机号
	
	private String shop_sub_number;
	
	private String sign;
	
	public String paramCheck() {
		if (shop_phone == null) return "shop_phone不能为空";// 商户手机号
		if (shop_sub_number == null) return "shop_sub_number不能为空";// 商户代付单号
		if (sign == null) return "sign不能为空";
		return null;
	}

	public String signParam() {
		Map<String, Object> signParamMap = new TreeMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		signParamMap.put("shop_phone", this.getShop_phone());
		signParamMap.put("shop_sub_number", this.getShop_sub_number());
		for (String keyString : signParamMap.keySet()) {
			sb.append(keyString + "=" + (signParamMap.get(keyString) == null ? "" : signParamMap.get(keyString)) + "&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

}
