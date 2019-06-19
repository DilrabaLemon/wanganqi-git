package com.boye.common.http.pay;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;

import lombok.Data;

@Data
public class Yun9PayParamBean {
	
	private String pay_memberid;
	
	private String pay_orderid;
	
	private Date pay_applydate;
	
	private String pay_bankcode;
	
	private String pay_notifyurl;
	
	private String pay_callbackurl;
	
	private BigDecimal pay_amount;
	
	private String pay_md5sign;
	
	private String pay_attach;
	
	private String pay_productname;
	
	private String pay_productnum;
	
	private String pay_productdesc;
	
	private String pay_producturl;
	
	private String sign;
	
	private String key;
	
	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("pay_memberid", pay_memberid);
		signParam.put("pay_orderid", pay_orderid);
		signParam.put("pay_applydate", pay_applydate);
		signParam.put("pay_bankcode", pay_bankcode);
		signParam.put("pay_notifyurl", pay_notifyurl);
		signParam.put("pay_callbackurl", pay_callbackurl);
		signParam.put("pay_amount", pay_amount);
		signParam.put("pay_md5sign", pay_md5sign);
		signParam.put("pay_attach", pay_attach);
		signParam.put("pay_productname", pay_productname);
		signParam.put("pay_productnum", pay_productnum);
		signParam.put("pay_productdesc", pay_productdesc);
		signParam.put("pay_producturl", pay_producturl);
		signParam.put("sign", sign);
		return signParam;
	}
	
	public String hasSignGetParam() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("pay_memberid", pay_memberid);
		signParam.put("pay_orderid", pay_orderid);
		signParam.put("pay_applydate", pay_applydate);
		signParam.put("pay_bankcode", pay_bankcode);
		signParam.put("pay_notifyurl", pay_notifyurl);
		signParam.put("pay_callbackurl", pay_callbackurl);
		signParam.put("pay_amount", pay_amount);
		signParam.put("pay_md5sign", pay_md5sign);
		signParam.put("pay_attach", pay_attach);
		signParam.put("pay_productname", pay_productname);
		signParam.put("pay_productnum", pay_productnum);
		signParam.put("pay_productdesc", pay_productdesc);
		signParam.put("pay_producturl", pay_producturl);
		signParam.put("sign", sign);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry entry : entrySet) {
            if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		return sb.toString();
	}
	
	public String generateSign() {
		Map<String, Object> signParam = new TreeMap<String, Object>();
		signParam.put("pay_memberid", pay_memberid);
		signParam.put("pay_orderid", pay_orderid);
		signParam.put("pay_applydate", pay_applydate);
		signParam.put("pay_bankcode", pay_bankcode);
		signParam.put("pay_notifyurl", pay_notifyurl);
		signParam.put("pay_callbackurl", pay_callbackurl);
		signParam.put("pay_amount", pay_amount);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry entry : entrySet) {
        	if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
		sb.append("key=" + key);
		System.out.println(sb);
		sign = MD5.md5Str(sb.toString()).toUpperCase();
		System.out.println(sign);
		return sign;
	}
}
