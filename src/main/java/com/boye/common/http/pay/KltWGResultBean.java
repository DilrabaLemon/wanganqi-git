package com.boye.common.http.pay;

public class KltWGResultBean {
	
	private String version; // 网关返回支付结果接口版本
	private String payType; // 支付方式
	private String issuerId; // 发卡方机构代码
	private String mchtOrderId; // 开联订单号
	private String orderNo; // 商户订单号
	private String orderDatetime; // 商户订单提交时间
	private Long orderAmount; // 商户订单金额
	private String payDatetime; // 支付完成时间
	private String ext1; // 扩展字段1
	private String ext2; // 扩展字段2
	private String payData; // 小程序支付参数
	private String redirectUrl; // 跳转网银url
	private String payResult; // 支付状态，0：支付中，1：支付成功，2：失败
	private String mchtId; // 商户号
	private String signType; // 签名类型
	private String requestId; // 请求流水
	private String responseCode; // 响应码，000000表示接口响应正常，其它表示失败
	private String responseMsg; // 响应信息
	private String signMsg; // 签名信息
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getIssuerId() {
		return issuerId;
	}
	public void setIssuerId(String issuerId) {
		this.issuerId = issuerId;
	}
	public String getMchtOrderId() {
		return mchtOrderId;
	}
	public void setMchtOrderId(String mchtOrderId) {
		this.mchtOrderId = mchtOrderId;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}
	public String getOrderDatetime() {
		return orderDatetime;
	}
	public void setOrderDatetime(String orderDatetime) {
		this.orderDatetime = orderDatetime;
	}
	public Long getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(Long orderAmount) {
		this.orderAmount = orderAmount;
	}
	public String getPayDatetime() {
		return payDatetime;
	}
	public void setPayDatetime(String payDatetime) {
		this.payDatetime = payDatetime;
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
	public String getPayData() {
		return payData;
	}
	public void setPayData(String payData) {
		this.payData = payData;
	}
	public String getRedirectUrl() {
		return redirectUrl;
	}
	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	public String getPayResult() {
		return payResult;
	}
	public void setPayResult(String payResult) {
		this.payResult = payResult;
	}
	public String getMchtId() {
		return mchtId;
	}
	public void setMchtId(String mchtId) {
		this.mchtId = mchtId;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(String responseCode) {
		this.responseCode = responseCode;
	}
	public String getResponseMsg() {
		return responseMsg;
	}
	public void setResponseMsg(String responseMsg) {
		this.responseMsg = responseMsg;
	}
	public String getSignMsg() {
		return signMsg;
	}
	public void setSignMsg(String signMsg) {
		this.signMsg = signMsg;
	}
}
