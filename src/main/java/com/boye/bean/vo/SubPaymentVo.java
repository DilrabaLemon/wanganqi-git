package com.boye.bean.vo;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Map;
import java.util.TreeMap;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SubPaymentVo {
	
	private String shop_phone;// 商户手机号
	
	private String bank_card_number;// 银行卡账号
	
	private String bank_name;// 所属银行
	
	private String regist_bank;// 开户银行所在地区
	
	private String regist_bank_name;// 开户银行
	
	private String city_number;// 城市编码
	
	private BigDecimal money;//提现金额
	
	private String passageway_code;   //通道代码
	
	private String card_user_name;// 拥有者姓名
	
	private String cert_number;// 拥有者身份证号码

	private String shop_sub_number;// 商户代付单号
	
	private String notify_url; //回调地址
	
	private String sign;
	
	public String signParam() {
		Gson gson = new Gson();
		String json = gson.toJson(this);
		Type type = new TypeToken<Map<String, String>>() {}.getType();
		Map<String, Object> paramMap = gson.fromJson(json, type);
		Map<String, Object> signParamMap = new TreeMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		signParamMap.put("shop_phone", paramMap.get("shop_phone"));
		signParamMap.put("bank_card_number", paramMap.get("bank_card_number"));
		signParamMap.put("bank_name", paramMap.get("bank_name"));
		signParamMap.put("regist_bank_name", paramMap.get("regist_bank_name"));
		signParamMap.put("money", paramMap.get("money"));
		signParamMap.put("passageway_code", paramMap.get("passageway_code"));
		signParamMap.put("card_user_name", paramMap.get("card_user_name"));
		signParamMap.put("shop_sub_number", paramMap.get("shop_sub_number"));
		signParamMap.put("notify_url", paramMap.get("notify_url"));
		for (String keyString : signParamMap.keySet()) {
			sb.append(keyString + "=" + (signParamMap.get(keyString) == null ? "" : signParamMap.get(keyString)) + "&");
		}
		sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
}

	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public String getBank_card_number() {
		return bank_card_number;
	}

	public void setBank_card_number(String bank_card_number) {
		this.bank_card_number = bank_card_number;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getRegist_bank() {
		return regist_bank;
	}

	public void setRegist_bank(String regist_bank) {
		this.regist_bank = regist_bank;
	}

	public String getRegist_bank_name() {
		return regist_bank_name;
	}

	public void setRegist_bank_name(String regist_bank_name) {
		this.regist_bank_name = regist_bank_name;
	}

	public String getCity_number() {
		return city_number;
	}

	public void setCity_number(String city_number) {
		this.city_number = city_number;
	}

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public String getPassageway_code() {
		return passageway_code;
	}

	public void setPassageway_code(String passageway_code) {
		this.passageway_code = passageway_code;
	}

	public String getCard_user_name() {
		return card_user_name;
	}

	public void setCard_user_name(String card_user_name) {
		this.card_user_name = card_user_name;
	}

	public String getCert_number() {
		return cert_number;
	}

	public void setCert_number(String cert_number) {
		this.cert_number = cert_number;
	}

	public String getShop_sub_number() {
		return shop_sub_number;
	}

	public void setShop_sub_number(String shop_sub_number) {
		this.shop_sub_number = shop_sub_number;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String paramCheck() {
		if (shop_phone == null) return "shop_phone不能为空";// 商户手机号
//		if (bank_card_number == null) return "bank_card_number不能为空";// 银行卡账号
//		if (bank_name == null) return "bank_name不能为空";// 所属银行
//		if (regist_bank == null) return "regist_bank不能为空";// 开户银行所在地区
//		if (city_number == null) return "city_number不能为空";// 城市编码
		if (money == null) return "money不能为空";//提现金额
		if (passageway_code == null) return "passageway_code不能为空";   //通道代码
		if (card_user_name == null) return "card_user_name不能为空";// 拥有者姓名
		if (shop_sub_number == null) return "shop_sub_number不能为空";// 商户代付单号
		if (notify_url == null) return "notify_url不能为空";// 商户代付单号
		if (sign == null) return "sign不能为空";
		return null;
	}

}
