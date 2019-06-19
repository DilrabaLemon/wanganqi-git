package com.boye.bean.vo;

import java.math.BigDecimal;

import com.boye.bean.entity.ExtractionRecord;

public class SubAuthenticationInfo {
	
	protected String shop_phone;
	
	protected String extraction_number;
	
	protected String passageway_code;
	
	protected Long passageway_id;
	
	protected String payment;
	
	protected String bank_card_number; // 四要素  银行卡号
	
	protected String cert_number; // 四要素  身份证号码
	
	protected String bank_name; // 四要素  银行名称
	
	protected String cert_name; // 四要素 持卡人姓名

	protected String mobile; // 四要素  手机号码
	
	protected String open_branch_bank_name;  // 开户行
	
	protected String sign;
	
	protected ExtractionRecord extraction;
	
	public String getShop_phone() {
		return shop_phone;
	}

	public void setShop_phone(String shop_phone) {
		this.shop_phone = shop_phone;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public Long getPassageway_id() {
		return passageway_id;
	}

	public void setPassageway_id(Long passageway_id) {
		this.passageway_id = passageway_id;
	}

	public String getSign() {
		return sign;
	}
	


	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getExtraction_number() {
		return extraction_number;
	}

	public void setExtraction_number(String extraction_number) {
		this.extraction_number = extraction_number;
	}

	public void setExtraction(ExtractionRecord extraction) {
		this.extraction = extraction;
	}

	public String getPassageway_code() {
		return passageway_code;
	}

	public void setPassageway_code(String passageway_code) {
		this.passageway_code = passageway_code;
	}

	public String getBank_card_number() {
		return bank_card_number;
	}

	public void setBank_card_number(String bank_card_number) {
		this.bank_card_number = bank_card_number;
	}

	public String getCert_number() {
		return cert_number;
	}

	public void setCert_number(String cert_number) {
		this.cert_number = cert_number;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getCert_name() {
		return cert_name;
	}

	public void setCert_name(String cert_name) {
		this.cert_name = cert_name;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getOpen_branch_bank_name() {
		return open_branch_bank_name;
	}

	public void setOpen_branch_bank_name(String open_branch_bank_name) {
		this.open_branch_bank_name = open_branch_bank_name;
	}

	public boolean paramCheck() {
		if (this.extraction_number == null) return true;
		if (this.passageway_code == null) return true;
		if (this.payment == null) return true;
		if (this.shop_phone == null) return true;
		return false;
	}

	public ExtractionRecord getExtraction() {
		if (extraction == null) {
			extraction = new ExtractionRecord();
			extraction.setExtraction_money(new BigDecimal(payment));
			extraction.setExtraction_number(extraction_number);
			extraction.setActual_money(new BigDecimal(payment));
			extraction.setBank_card_number(bank_card_number);
			extraction.setBank_name(bank_name);
			extraction.setCert_number(cert_number);
			extraction.setRegist_bank(open_branch_bank_name);
			extraction.setService_charge(new BigDecimal(2));
			extraction.setService_type(1);
			extraction.setState(0);
			extraction.setType(0);
			extraction.setUser_mobile(mobile);
		}
		return extraction;
	}
	
	public String getNewSignParam() {
		StringBuffer result = new StringBuffer();
		if (bank_card_number == null) result.append("bank_card_number=" + bank_card_number + "&");
		if (bank_name == null) result.append("bank_name=" + bank_name + "&");
		if (cert_name == null) result.append("cert_name=" + cert_name + "&");
		if (cert_number == null) result.append("cert_number=" + cert_number + "&");
		if (extraction_number == null) result.append("order_number=&");
		if (mobile == null) result.append("mobile=" + mobile + "&");
		if (open_branch_bank_name == null) result.append("open_branch_bank_name=" + open_branch_bank_name);
		else result.append("order_number=" + extraction_number + "&");
		if (passageway_code == null) result.append("passageway_code=&");
		else result.append("passageway_code=" + passageway_code + "&");
		if (payment == null) result.append("payment=&");
		else result.append("payment=" + payment + "&");
		if (shop_phone == null) result.append("shop_phone=&");
		else result.append("shop_phone=" + shop_phone + "&");
		return result.toString();
		
	}
}
