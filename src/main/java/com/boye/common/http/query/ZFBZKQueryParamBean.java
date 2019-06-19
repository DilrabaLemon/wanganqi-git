package com.boye.common.http.query;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;
import com.google.gson.Gson;

import lombok.Data;


@Data
public class ZFBZKQueryParamBean {

	private String uid; //商户uid

	private String r; //随机字符串	string(20)	必填。每次请求确保不一样

	private String orderid; //商户自定义订单号

	private String key; //签名

	private Integer version; //协议版本号	int	必填。当前为2

	private String token; //秘钥

	@SuppressWarnings("unchecked")
	public Map<String, Object> hasSignParamMap() {
		Gson gson = new Gson();
		String toJson = gson.toJson(this);
		Map<String, Object> signParam = gson.fromJson(toJson, Map.class);
		signParam = new TreeMap<String, Object>(signParam);
		signParam.remove("token");
		signParam.put("version", version);
//		signParam.put("isgo_alipay", isgo_alipay);
		return signParam;
	}
	
	@SuppressWarnings("unchecked")
	public String hasSignGetParam() {
		Gson gson = new Gson();
		String toJson = gson.toJson(this);
		Map<String, Object> signParam = gson.fromJson(toJson, Map.class);
		signParam = new TreeMap<String, Object>(signParam);
		signParam.remove("token");
		signParam.put("version", version);
//		signParam.put("isgo_alipay", isgo_alipay);
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
		StringBuilder sb = new StringBuilder();
		sb.append("uid=" + uid);
		sb.append("&orderid=" + orderid);
		sb.append("&r=" + r);
		sb.append("&token=" + token);
		sb.append("&version=" + version);
		System.out.println(sb);
		key = MD5.md5Str(sb.toString()); 
		System.out.println(key);
		return key;
	}

}
