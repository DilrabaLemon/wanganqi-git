package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("extraction_record")
public class ExtractionRecord extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5264585385384922271L;
	
	@Column
	private Long shop_id;// 商户id
	
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
	private Integer type;   //代付类型   1 线下打款   2 H5代付（弃用）    3 网银代付
	
	@Column
	private Long passageway_id;   //通道ID
	
	@Column
	private Long substitute_id;   //代付账户ID

	@Column
	private int state;// 提现订单状态

	@Column
	private String card_user_name;// 拥有者姓名
	
	@Column
	private String cert_number;// 拥有者身份证号码

	@Column
	private Integer service_type;// 手续费收取方式
	
	@Column
	private String extraction_number;// 提现标识码
	
	private String order_number;
	
	private String shop_name;// 商户姓名
	
	private String substitute_number;// 替代号码
	
	private String verification;
	
	private String code;
	
	private String shop_extraction_code;
	
	public String getShop_extraction_code() {
		return shop_extraction_code;
	}

	public void setShop_extraction_code(String shop_extraction_code) {
		this.shop_extraction_code = shop_extraction_code;
	}

	public ExtractionRecord(long id) {
		this.id = id;
	}
	
	public ExtractionRecord() {
		// TODO Auto-generated constructor stub
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	@Override
	public boolean paramIsNull() {
		if (extraction_money.compareTo(BigDecimal.ZERO) != 1) return true;
		return false;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getCard_user_name() {
		return card_user_name;
	}

	public void setCard_user_name(String card_user_name) {
		this.card_user_name = card_user_name;
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

	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public String getUser_mobile() {
		return user_mobile;
	}

	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getPassageway_id() {
		return passageway_id;
	}

	public void setPassageway_id(Long passageway_id) {
		this.passageway_id = passageway_id;
	}

	public String getSubstitute_number() {
		return substitute_number;
	}

	public void setSubstitute_number(String substitute_number) {
		this.substitute_number = substitute_number;
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

	public String getCity_number() {
		return city_number;
	}

	public void setCity_number(String city_number) {
		this.city_number = city_number;
	}

	public Long getSubstitute_id() {
		return substitute_id;
	}

	public void setSubstitute_id(Long substitute_id) {
		this.substitute_id = substitute_id;
	}

	public String getRegist_bank() {
		return regist_bank;
	}

	public void setRegist_bank(String regist_bank) {
		this.regist_bank = regist_bank;
	}

	public BigDecimal getExtraction_money() {
		return extraction_money;
	}

	public void setExtraction_money(BigDecimal extraction_money) {
		this.extraction_money = extraction_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getService_charge() {
		return service_charge;
	}

	public void setService_charge(BigDecimal service_charge) {
		this.service_charge = service_charge.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal getActual_money() {
		return actual_money;
	}

	public void setActual_money(BigDecimal actual_money) {
		this.actual_money = actual_money.setScale(4, BigDecimal.ROUND_HALF_UP);
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getCert_number() {
		return cert_number;
	}

	public void setCert_number(String cert_number) {
		this.cert_number = cert_number;
	}

	public String getRegist_bank_name() {
		return regist_bank_name;
	}

	public void setRegist_bank_name(String regist_bank_name) {
		this.regist_bank_name = regist_bank_name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}
}
