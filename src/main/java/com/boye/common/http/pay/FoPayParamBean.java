package com.boye.common.http.pay;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;
import com.google.gson.Gson;

public class FoPayParamBean {
	
	private String company_no;
	
	private String charset;
	
	private String sign_type;
	
	private String sign;
	
	private String format;
	
	private String order_sn;
	
	private String pay_type;
	
	private BigDecimal order_amount;
	
	private String notify_url;
	
	private String goods_name;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Gson gson = new Gson();
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("company_no", company_no);
		signParam.put("charset", charset);
		signParam.put("sign_type", sign_type);
		signParam.put("sign", sign);
		signParam.put("format", format);
		signParam.put("order_sn", order_sn);
		signParam.put("order_amount", order_amount.toString());
		signParam.put("notify_url", notify_url);
		signParam.put("goods_name", goods_name);
		signParam.put("pay_type", pay_type);
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
		signParam.put("goods_name", goods_name);
		signParam.put("pay_type", pay_type);
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
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getCompany_no() {
		return company_no;
	}


	public void setCompany_no(String company_no) {
		this.company_no = company_no;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getSign_type() {
		return sign_type;
	}

	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getOrder_sn() {
		return order_sn;
	}

	public void setOrder_sn(String order_sn) {
		this.order_sn = order_sn;
	}

	public String getPay_type() {
		return pay_type;
	}

	public void setPay_type(String pay_type) {
		this.pay_type = pay_type;
	}

	public BigDecimal getOrder_amount() {
		return order_amount;
	}

	public void setOrder_amount(BigDecimal order_amount) {
		this.order_amount = order_amount;
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getGoods_name() {
		return goods_name;
	}

	public void setGoods_name(String goods_name) {
		this.goods_name = goods_name;
	}

}
