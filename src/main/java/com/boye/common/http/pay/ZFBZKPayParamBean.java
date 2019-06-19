package com.boye.common.http.pay;

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
public class ZFBZKPayParamBean {

	private String uid; //商户uid

	private BigDecimal  price; //价格 单位：元。精确小数点后2位

	private Integer istype; //支付渠道	int	必填。1：支付宝；2：微信支付；3：QQ钱包支付；4：招商银行支付；5：比特币支付；6：云闪付

	private String notify_url; //通知回调网址	string(255)	必填。用户支付成功后，我们服务器会主动发送一个post消息到这个网址。由您自定义。不要urlencode。例：http://www.aaa.com/kuairubao_notify

	private String return_url; //跳转网址	string(255)	必填。用户支付成功后，我们会让用户浏览器自动跳转到这个网址。由您自定义。不要urlencode。例：http://www.aaa.com/kuairubao_return

	private String orderid; //商户自定义订单号

	private String orderuid; //商户自定义客户号

	private String goodsname; //商品名称

	private String key; //签名

	private Integer version; //协议版本号	int	必填。当前为2

	private Integer isgo_alipay; //是否自动打开支付宝 1表示自动打开，0表示不自动打开

	private String token; //秘钥

	@SuppressWarnings("unchecked")
	public Map<String, Object> hasSignParamMap() {
		DecimalFormat df = new DecimalFormat("#0.00");
		Gson gson = new Gson();
		String toJson = gson.toJson(this);
		Map<String, Object> signParam = gson.fromJson(toJson, Map.class);
		signParam = new TreeMap<String, Object>(signParam);
		signParam.remove("token");
		signParam.put("istype", istype);
		signParam.put("price", df.format(price));
		signParam.put("version", version);
//		signParam.put("isgo_alipay", isgo_alipay);
		return signParam;
	}
	
	@SuppressWarnings("unchecked")
	public String hasSignGetParam() {
		DecimalFormat df = new DecimalFormat("#0.00");
		Gson gson = new Gson();
		String toJson = gson.toJson(this);
		Map<String, Object> signParam = gson.fromJson(toJson, Map.class);
		signParam = new TreeMap<String, Object>(signParam);
		signParam.remove("token");
		signParam.put("istype", istype);
		signParam.put("price", df.format(price));
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
	
	@SuppressWarnings("unchecked")
	public String generateSign() {
		DecimalFormat df = new DecimalFormat("#0.00");
		Gson gson = new Gson();
		String toJson = gson.toJson(this);
		Map<String, Object> signParam = gson.fromJson(toJson, Map.class);
		signParam = new TreeMap<String, Object>(signParam);
		signParam.remove("key");
		signParam.put("istype", istype);
		signParam.put("price", df.format(price));
		signParam.put("version", version);
//		signParam.put("isgo_alipay", isgo_alipay);
		StringBuilder sb = new StringBuilder();
		Set<Entry<String, Object>> entrySet = signParam.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
        	if (entry.getValue() != null && !entry.getValue().equals(""))
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
		System.out.println(sb);
		key = MD5.md5Str(sb.toString());
		System.out.println(key);
		return key;
	}

}
