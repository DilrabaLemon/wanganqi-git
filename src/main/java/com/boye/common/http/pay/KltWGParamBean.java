package com.boye.common.http.pay;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.boye.common.utils.MD5;

public class KltWGParamBean {
	
	//报文内容（content）
	private long orderAmount; // M 订单金额
	private String payerName;  // O 付款人姓名
	private String payerEmail; // O 付款人邮件联系方式
	private String payerTelPhone; // O 付款人电话联系方式
	private String payerAcctNo; // O 微信公众号支付 / 微信小程序支付 / 微信APP支付    openid填入此字段
	private String orderNo; // M 订单号
	private int orderCurrency;	// M 币种
	private String orderDateTime;  // M 商户订单时间 yyyyMMDDhhmmss
	private int orderExpireDatetime; // O 订单过期时间 单位为分钟
	private String productName; // M 商品名称
	private String productPrice; // O 商品价格
	private String productNum; // O 商品数量
	private String productId; // O 商品代码
	private String productDesc; // O 商品描述
	private String ext1; // O 扩展字段
	private String ext2; // O  扩展字段  
	private String termId; // O  终端类型，app支付
	private int payType; // M  支付方式
	private String issuerId; // C 机构代码    payType=1或者4时必传
	private String receiveUrl;  //后台通知地址URL
	private String pickupUrl;  //页面跳转地址
	
	//报文头（head）
	private String version; // O 版本号
	private String merchantId; // M 商户号
	private String signType; // M 签名类型，0：数字证书 1：md5
	private String sign; // M 签名信息
	
	//特殊
	private String key; //密钥
	
	public long getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(long orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getPayerName() {
		return payerName;
	}
	public void setPayerName(String payerName) {
		this.payerName = payerName;
	}
	public String getPayerEmail() {
		return payerEmail;
	}
	public void setPayerEmail(String payerEmail) {
		this.payerEmail = payerEmail;
	}
	public String getPayerTelPhone() {
		return payerTelPhone;
	}
	public void setPayerTelPhone(String payerTelPhone) {
		this.payerTelPhone = payerTelPhone;
	}
	public String getPayerAcctNo() {
		return payerAcctNo;
	}
	public void setPayerAcctNo(String payerAcctNo) {
		this.payerAcctNo = payerAcctNo;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public int getOrderCurrency() {
		return orderCurrency;
	}
	public void setOrderCurrency(int orderCurrency) {
		this.orderCurrency = orderCurrency;
	}
	public String getOrderDateTime() {
		return orderDateTime;
	}
	public void setOrderDateTime(String orderDateTime) {
		this.orderDateTime = orderDateTime;
	}
	public int getOrderExpireDatetime() {
		return orderExpireDatetime;
	}
	public void setOrderExpireDatetime(int orderExpireDatetime) {
		this.orderExpireDatetime = orderExpireDatetime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getProductPrice() {
		return productPrice;
	}
	public void setProductPrice(String productPrice) {
		this.productPrice = productPrice;
	}
	public String getProductNum() {
		return productNum;
	}
	public void setProductNum(String productNum) {
		this.productNum = productNum;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getProductDesc() {
		return productDesc;
	}
	public void setProductDesc(String productDesc) {
		this.productDesc = productDesc;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getTermId() {
		return termId;
	}
	public void setTermId(String termId) {
		this.termId = termId;
	}
	public int getPayType() {
		return payType;
	}
	public void setPayType(int payType) {
		this.payType = payType;
	}
	public String getIssuerId() {
		return issuerId;
	}
	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}
	public String getReceiveUrl() {
		return receiveUrl;
	}
	public void setReceiveUrl(String receiveUrl) {
		this.receiveUrl = receiveUrl;
	}
	public String getPickupUrl() {
		return pickupUrl;
	}
	public void setPickupUrl(String pickupUrl) {
		this.pickupUrl = pickupUrl;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMerchantId() {
		return merchantId;
	}
	public void setMerchantId(String merchantId) {
		this.merchantId = merchantId;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}

	public Map<String, Object> hasSignParamMap() {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> head = new HashMap<String, Object>();
		Map<String, Object> content = new HashMap<String, Object>();
		
		head.put("version", version);
		head.put("merchantId", merchantId);
		head.put("signType", signType);
		head.put("sign", sign);
		
		content.put("orderAmount", orderAmount);
		if (payerName != null) content.put("payerName", payerName);
		if (payerEmail != null) content.put("payerEmail", payerEmail);
		if (payerTelPhone != null) content.put("payerTelPhone", payerTelPhone);
		if (payerAcctNo != null) content.put("payerAcctNo", payerAcctNo);
		if (orderNo != null) content.put("orderNo", orderNo);
		content.put("orderCurrency", orderCurrency);
		if (orderDateTime != null) content.put("orderDateTime", orderDateTime);
		if (productName != null) content.put("productName", productName);
		if (productPrice != null) content.put("productPrice", productPrice);
		if (productNum != null) content.put("productNum", productNum);
		if (productId != null) content.put("productId", productId);
		if (productDesc != null) content.put("productDesc", productDesc);
		if (ext1 != null) content.put("ext1", ext1);
		if (ext2 != null) content.put("ext2", ext2);
		if (termId != null) content.put("termId", termId);
		content.put("payType", payType);
		if (issuerId != null) content.put("issuerId", issuerId);
		if (receiveUrl != null) content.put("receiveUrl", receiveUrl);
		if (pickupUrl != null) content.put("pickupUrl", pickupUrl);
		
		result.put("head" , head);
		result.put("content" , content);
		return result;
	}
	
	public String generateSign() {
		String signParam = returnParamStr().toString() + "&key=" + key;
		System.out.println(signParam);
		sign = MD5.md5Str(signParam).toUpperCase();
		System.out.println(sign);
		return sign;
	}
//	
	public StringBuffer returnParamStr() {
		Map<String, Object> paramMap = new TreeMap<String, Object>();
		paramMap.put("version", version);
		paramMap.put("merchantId", merchantId);
		paramMap.put("signType", signType);
		paramMap.put("sign", sign);
		
		paramMap.put("orderAmount", orderAmount);
		if (payerName != null) paramMap.put("payerName", payerName);
		if (payerEmail != null) paramMap.put("payerEmail", payerEmail);
		if (payerTelPhone != null) paramMap.put("payerTelPhone", payerTelPhone);
		if (payerAcctNo != null) paramMap.put("payerAcctNo", payerAcctNo);
		if (orderNo != null) paramMap.put("orderNo", orderNo);
		paramMap.put("orderCurrency", orderCurrency);
		if (orderDateTime != null) paramMap.put("orderDateTime", orderDateTime);
		if (productName != null) paramMap.put("productName", productName);
		if (productPrice != null) paramMap.put("productPrice", productPrice);
		if (productNum != null) paramMap.put("productNum", productNum);
		if (productId != null) paramMap.put("productId", productId);
		if (productDesc != null) paramMap.put("productDesc", productDesc);
		if (ext1 != null) paramMap.put("ext1", ext1);
		if (ext2 != null) paramMap.put("ext2", ext2);
		if (termId != null) paramMap.put("termId", termId);
		paramMap.put("payType", payType);
		if (issuerId != null) paramMap.put("issuerId", issuerId);
		if (receiveUrl != null) paramMap.put("receiveUrl", receiveUrl);
		if (pickupUrl != null) paramMap.put("pickupUrl", pickupUrl);
		StringBuffer sb = new StringBuffer();
        Set<Entry<String, Object>> entrySet = paramMap.entrySet();
        for (Map.Entry entry : entrySet) {
            if (entry.getValue() != null)
            	sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        sb.deleteCharAt(sb.length() - 1);
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
