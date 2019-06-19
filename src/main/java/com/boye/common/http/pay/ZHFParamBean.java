package com.boye.common.http.pay;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;

public class ZHFParamBean {
	
	private String appId;
    private String amount;// 金额
	private String platOrderNo;// 系统订单号，后端自己生成唯一标识
	private String url;// 生成成功的二维码对应的url
	private int type;// 1为支付宝，2为微信
	private String sign;
	
	//特殊
	private String key; //密钥

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getPlatOrderNo() {
		return platOrderNo;
	}

	public void setPlatOrderNo(String platOrderNo) {
		this.platOrderNo = platOrderNo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = returnParamStr();
		result.put("appId", appId);
		result.put("amount", amount);
		result.put("platOrderNo", platOrderNo);
		result.put("url", url);
		result.put("type", type);
		return result;
	}
	
	public String generateSign() {
		StringBuffer sb = new StringBuffer();
        Set<Entry<String, Object>> entrySet = returnParamStr().entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            if (entry.getValue() != null)
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
		String signParam = sb.toString() + "key=" + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam).toLowerCase();
		return sign;
	}
	
	public Map<String, Object> returnParamStr() {
		Map<String, Object> param = new TreeMap<String, Object>();
		return param;
	}
	
	public String hasSignParam() {
		return null;
	}
	
	public String notSignParam() {
		StringBuffer result = new StringBuffer();
		result.append("appId=" + appId);
		result.append("&amount=" + amount);
		result.append("&platOrderNo=" + platOrderNo);
		result.append("&url=" + url);
		result.append("&type=" + type);
		return result.toString();
	}
	
}
