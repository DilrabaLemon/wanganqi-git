package com.boye.common.http.pay;


import java.util.HashMap;
import java.util.Map;



public class JzfPayParamBean {
	
	private  String accessToken;
	private  String outTradeNo;
	private  String money;
	private  String type;
	private  String body;
	private  String detail;
	private  String productId;
	private  String notifyUrl;
	private  String successUrl;
	
	private String cardNo;  //收款银行账号
	private String channelNo;  //通道号（查询接口 4 查询返回的）
	private String cardName;  //收款开户人姓名
	private String idCard;  //收款人身份证号
	private String bank; //收款开户行
	private String bankNo; // 收款卡清算大行号
	private String inerBankNo; //收款卡清算支行号
	private String phone; //收款人手机号
	private String bankProvince; //收款开户行省
	private String bankCity; // 收款开户行市
	private String notifyOutUrl;
	private String branchBankName; //收款开户行支行号
	
	public Map<String, Object> returnParamMap(){
		Map<String, Object> result = new HashMap<String, Object>();
		if (outTradeNo != null) result.put("outTradeNo", outTradeNo);
		if (money != null) result.put("money", money);
		if (body != null) result.put("body", body);
		if (type != null) result.put("type", type);
		if (detail != null) result.put("detail", detail);
		if (productId != null) result.put("productId", productId);
		if (notifyUrl != null) result.put("notifyUrl", notifyUrl);
		if (successUrl != null) result.put("successUrl", successUrl);
		return result;
	}
	
	public Map<String, Object> returnParamMapByDF(){
		Map<String, Object> result = new HashMap<String, Object>();
		if (outTradeNo != null) result.put("outTradeNo", outTradeNo);
		if (cardNo != null) result.put("cardNo", cardNo);
		if (channelNo != null) result.put("channelNo", channelNo);
		if (cardName != null) result.put("cardName", cardName);
		if (idCard != null) result.put("idCard", idCard);
		if (detail != null) result.put("detail", detail);
		if (bank != null) result.put("bank", bank);
		if (bankNo != null) result.put("bankNo", bankNo);
		if (inerBankNo != null) result.put("inerBankNo", inerBankNo);
		if (phone != null) result.put("phone", phone);
		if (bankProvince != null) result.put("bankProvince", bankProvince);
		if (bankCity != null) result.put("bankCity", bankCity);
		if (money != null) result.put("money", money);
		if (notifyOutUrl != null) result.put("notifyOutUrl", notifyOutUrl);
		if (branchBankName != null) result.put("branchBankName", branchBankName);
		return result;
	}
	
	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getDetail() {
		return detail;
	}
	public void setDetail(String detail) {
		this.detail = detail;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getSuccessUrl() {
		return successUrl;
	}

	public void setSuccessUrl(String successUrl) {
		this.successUrl = successUrl;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getChannelNo() {
		return channelNo;
	}

	public void setChannelNo(String channelNo) {
		this.channelNo = channelNo;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public String getIdCard() {
		return idCard;
	}

	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankNo() {
		return bankNo;
	}

	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}

	public String getInerBankNo() {
		return inerBankNo;
	}

	public void setInerBankNo(String inerBankNo) {
		this.inerBankNo = inerBankNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getBankProvince() {
		return bankProvince;
	}

	public void setBankProvince(String bankProvince) {
		this.bankProvince = bankProvince;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBranchBankName() {
		return branchBankName;
	}

	public void setBranchBankName(String branchBankName) {
		this.branchBankName = branchBankName;
	}
	
	
	
}
