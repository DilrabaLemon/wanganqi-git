package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("platform_extraction_record")
public class PlatformExtractionRecord extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1147367881494217012L;
	
	@Column
	private Long admin_id;// 管理员id
	
	@Column
	private String user_mobile;// 商户手机号
	
	@Column
	private String bank_card_number;// 银行卡账号
	
	@Column
	private String bank_name;// 所属银行
	
	@Column
	private String regist_bank;// 开户银行所在地区
	
	@Column
	private String regist_bank_name;// 开户银行
	
	@Column
	private String city_number;// 城市编码
	
	@Column
	private BigDecimal extraction_money;//提现金额
	
	@Column
	private BigDecimal service_charge;//手续费
	
	@Column
	private BigDecimal actual_money;// 实际到账金额
	
	@Column
	private Integer type;   //代付类型  

	@Column
	private int state;// 提现订单状态
	
	@Column
	private String account_number;// 商户号
	
	@Column
	private String card_user_name;// 拥有者姓名

	@Column
	private String cert_number;// 拥有者身份证号码

	@Column
	private Integer service_type;// 手续费收取方式
	
	@Column
	private String extraction_number;// 提现标识码
	
	private String verification;
	
	private String code;
	
	public PlatformExtractionRecord() {
		
	}

	public PlatformExtractionRecord(Long extraction_id) {
		this.id = extraction_id;
	}

	public Long getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Long admin_id) {
		this.admin_id = admin_id;
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

	public BigDecimal getExtraction_money() {
		return extraction_money;
	}

	public void setExtraction_money(BigDecimal extraction_money) {
		this.extraction_money = extraction_money;
	}

	public BigDecimal getService_charge() {
		return service_charge;
	}

	public void setService_charge(BigDecimal service_charge) {
		this.service_charge = service_charge;
	}

	public BigDecimal getActual_money() {
		return actual_money;
	}

	public void setActual_money(BigDecimal actual_money) {
		this.actual_money = actual_money;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
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

	public String getAccount_number() {
		return account_number;
	}

	public void setAccount_number(String account_number) {
		this.account_number = account_number;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
