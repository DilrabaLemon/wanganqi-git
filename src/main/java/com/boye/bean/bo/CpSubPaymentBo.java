package com.boye.bean.bo;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import com.boye.bean.entity.CpSubPaymentInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import lombok.Data;

@Data
public class CpSubPaymentBo implements Serializable {

	private static final long serialVersionUID = 8223436366941622316L;

	private String sub_payment_number;
	
	private String shop_sub_number;
	
	private BigDecimal actual_money;
	
	private BigDecimal sub_money;
	
	private BigDecimal service_charge;
	
	private Integer sub_state;
	
	private String shop_phone;
	
	private String sign;
	
	public String paramNotSignStr() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		Type type = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, Object> paramMap = gson.fromJson(json, type);
		Map<String, Object> signParamMap = new TreeMap<String, Object>(paramMap);
		signParamMap.remove("sign");
		StringBuffer sb = new StringBuffer();
		for (String keyString : signParamMap.keySet()) {
			sb.append(keyString + "=" + (signParamMap.get(keyString) == null ? "" : signParamMap.get(keyString)) + "&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public String paramStr() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		Type type = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, Object> paramMap = gson.fromJson(json, type);
		Map<String, Object> signParamMap = new TreeMap<String, Object>();
		StringBuffer sb = new StringBuffer();
//		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
		for (String keyString : paramMap.keySet()) {
			signParamMap.put(keyString, paramMap.get(keyString));
		}
		for (String keyString : signParamMap.keySet()) {
			sb.append(keyString + "=" + (signParamMap.get(keyString) == null ? "" : signParamMap.get(keyString)) + "&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}

	public static CpSubPaymentBo getSubPaymentVoByInfo(CpSubPaymentInfo cpSub) {
		CpSubPaymentBo result = new CpSubPaymentBo();
		result.actual_money = cpSub.getActual_money();
		result.service_charge = cpSub.getService_charge();
		result.shop_phone = cpSub.getShop_phone();
		result.shop_sub_number = cpSub.getShop_sub_number();
		result.sub_money = cpSub.getSub_money();
		result.sub_payment_number = cpSub.getShop_sub_number();
		result.sub_state = cpSub.getSub_state();
		return result;
	}
}
