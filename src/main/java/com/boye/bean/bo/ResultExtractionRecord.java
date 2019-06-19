package com.boye.bean.bo;

import java.text.DecimalFormat;

import com.boye.bean.entity.ExtractionRecord;

public class ResultExtractionRecord {

	private String user_mobile;// 商户手机号
	
	private String bank_card_number;// 银行卡账号
	
	private String bank_name;// 所属银行
	
	private String regist_bank;// 开户银行所在地区
	
	private String regist_bank_name;// 开户银行
	
	private String city_number;// 城市编码
	
	private String extraction_money;//提现金额
	
	private String service_charge;//手续费
	
	private String actual_money;// 实际到账金额
	
	private int state;// 提现订单状态

	private String card_user_name;// 持卡人姓名
	
	private String cert_number;// 持卡人身份证号码

	private Integer service_type;// 手续费收取方式
	
	private String extraction_number;// 提现标识码
	
	private String order_number; // 商户订单号
	
	public ResultExtractionRecord(ExtractionRecord extract) {
		DecimalFormat df = new DecimalFormat("#0.00");
		user_mobile = extract.getUser_mobile();
		bank_card_number = extract.getBank_card_number();
		bank_name = extract.getBank_name();
		regist_bank = extract.getRegist_bank();
		regist_bank_name = extract.getRegist_bank_name();
		city_number = extract.getCity_number();
		extraction_money = df.format(extract.getExtraction_money());
		service_charge = df.format(extract.getService_charge());
		actual_money = df.format(extract.getActual_money());
		state= extract.getState();
		card_user_name = extract.getCard_user_name();
		cert_number = extract.getCert_number();
		service_type = extract.getService_type();
		extraction_number = extract.getExtraction_number();
		order_number = extract.getOrder_number();
	}

	public String getUser_mobile() {
		return user_mobile;
	}

	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}

	public String getBank_card_number() {
		return bank_card_number;
	}

	public void setBank_card_number(String bank_card_number) {
		this.bank_card_number = bank_card_number;
	}

	public String getBank_name() {
		return bank_name;
	}

	public void setBank_name(String bank_name) {
		this.bank_name = bank_name;
	}

	public String getRegist_bank() {
		return regist_bank;
	}

	public void setRegist_bank(String regist_bank) {
		this.regist_bank = regist_bank;
	}

	public String getRegist_bank_name() {
		return regist_bank_name;
	}

	public void setRegist_bank_name(String regist_bank_name) {
		this.regist_bank_name = regist_bank_name;
	}

	public String getCity_number() {
		return city_number;
	}

	public void setCity_number(String city_number) {
		this.city_number = city_number;
	}

	public String getExtraction_money() {
		return extraction_money;
	}

	public void setExtraction_money(String extraction_money) {
		this.extraction_money = extraction_money;
	}

	public String getService_charge() {
		return service_charge;
	}

	public void setService_charge(String service_charge) {
		this.service_charge = service_charge;
	}

	public String getActual_money() {
		return actual_money;
	}

	public void setActual_money(String actual_money) {
		this.actual_money = actual_money;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getCard_user_name() {
		return card_user_name;
	}

	public void setCard_user_name(String card_user_name) {
		this.card_user_name = card_user_name;
	}

	public String getCert_number() {
		return cert_number;
	}

	public void setCert_number(String cert_number) {
		this.cert_number = cert_number;
	}

	public Integer getService_type() {
		return service_type;
	}

	public void setService_type(Integer service_type) {
		this.service_type = service_type;
	}

	public String getExtraction_number() {
		return extraction_number;
	}

	public void setExtraction_number(String extraction_number) {
		this.extraction_number = extraction_number;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}
	
}
