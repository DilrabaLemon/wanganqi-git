package com.boye.bean.vo;

import org.apache.commons.lang.StringUtils;

public class QuickAuthenticationInfo extends AuthenticationInfo {
	
	private String card_code;
	
	private String bank_code;

	public boolean paramCheck() {
		if (this.order_number == null) return true;
		if (this.passageway_code == null) return true;
		if (this.payment == null) return true;
		if (this.shop_phone == null) return true;
		order_number = order_number.trim();
		passageway_code = passageway_code.trim();
		payment = payment.trim();
		shop_phone.trim();
		if (this.card_code != null) card_code = card_code.trim();
		if (this.bank_code != null) bank_code = bank_code.trim();
		return false;
	}

	public String strparamCheck() {
		String result = "";
		if (StringUtils.isBlank(this.order_number)) result=result+" "+"order_number";
		if (StringUtils.isBlank( this.passageway_code))  result=result+" "+"passageway_code";
		if (StringUtils.isBlank(this.payment))  result=result+" "+"payment";
		if (StringUtils.isBlank( this.shop_phone))  result=result+" "+"shop_phone";
		return result;
	}

	public String getSignParam() {
		StringBuffer result = new StringBuffer();
		if (shop_phone == null) result.append("SHOPPHONE=&");
		else result.append("SHOPPHONE=" + shop_phone + "&");
		if (order_number == null) result.append("ORDERNUMBER=&");
		else result.append("ORDERNUMBER=" + order_number + "&");
		if (passageway_code == null) result.append("PASSAGEWAYCODE=&");
		else result.append("PASSAGEWAYCODE=" + passageway_code + "&");
		if (payment == null) result.append("PAYMENT=&");
		else result.append("PAYMENT=" + payment + "&");
		if (cert_name != null) result.append("CERTNAME=" + cert_name + "&");
		if (bank_card_number != null) result.append("BANKCARDNUMBER=" + bank_card_number + "&");
		if (mobile != null) result.append("MOBILE=" + mobile + "&");
		if (card_code != null) result.append("CARDCODE=" + card_code + "&");
		return result.toString();
	}
	
	public String getNewSignParam() {
		StringBuffer result = new StringBuffer();
		if (bank_card_number != null) result.append("bank_card_number=" + bank_card_number + "&");
		if (card_code != null) result.append("card_code=" + card_code + "&");
		if (cert_name != null) result.append("cert_name=" + cert_name + "&");
		if (mobile != null) result.append("mobile=" + mobile + "&");
		if (order_number == null) result.append("order_number=&");
		else result.append("order_number=" + order_number + "&");
		if (passageway_code == null) result.append("passageway_code=&");
		else result.append("passageway_code=" + passageway_code + "&");
		if (payment == null) result.append("payment=&");
		else result.append("payment=" + payment + "&");
		if (shop_phone == null) result.append("shop_phone=");
		else result.append("shop_phone=" + shop_phone);
		return result.toString();
	}

	public String getCard_code() {
		return card_code;
	}

	public void setCard_code(String card_code) {
		this.card_code = card_code;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

}
