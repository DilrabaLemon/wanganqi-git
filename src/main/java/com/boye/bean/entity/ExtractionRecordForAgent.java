package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("agent_extraction_record")
public class ExtractionRecordForAgent extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 209743168034074844L;

	@Column
	private Long agent_id;// 代理商id
	
	@Column
	private String extraction_number;//提现账号识别码
	
	@Column
	private String user_mobile;// 商户手机号
	
	@Column
	private String bank_card_number;// 银行卡账号
	
	@Column
	private Long passageway_id;// 通道ID
	
	@Column
	private Long substitute_id;// 代付账户ID
	
	@Column
	private Integer type;// 代付类型     1 线下打款     2 H5代付（弃用）    3 网银代付
	
	@Column
	private String bank_name;// 所属银行
	
	@Column
	private String regist_bank;// 开户行
	
	@Column
	private String regist_bank_name;// 开户行
	
	@Column
	private String city_number;// 城市编号
	
	@Column
	private BigDecimal extraction_money;// 提现金额
	
	@Column
	private BigDecimal service_charge;// 手续费
	
	@Column
	private BigDecimal actual_money;// 实际到账金额
	
	@Column
	private int state;// 提现订单状态
	
	@Column
	private String cert_number;// 拥有者身份证号码

	@Column
	private String card_user_name;// 拥有者姓名

	@Column
	private Integer service_type;// 手续费收取方式
	
	private String substitute_number;
	
	private String agent_name;
	
	private String verification;
	
	private String agent_extraction_code;
	
	public String getAgent_extraction_code() {
		return agent_extraction_code;
	}

	public void setAgent_extraction_code(String agent_extraction_code) {
		this.agent_extraction_code = agent_extraction_code;
	}

	public String getAgent_name() {
		return agent_name;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}

	public ExtractionRecordForAgent(long id) {
		this.id = id;
	}
	
	public ExtractionRecordForAgent() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean paramIsNull() {
		if (agent_id == null) return true;
		if (extraction_money.compareTo(BigDecimal.ZERO) != 1) return true;
		return false;
	}

	public Long getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(Long agent_id) {
		this.agent_id = agent_id;
	}
	
	public String getExtraction_number() {
		return extraction_number;
	}

	public void setExtraction_number(String extraction_number) {
		this.extraction_number = extraction_number;
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

	public String getUser_mobile() {
		return user_mobile;
	}

	public void setUser_mobile(String user_mobile) {
		this.user_mobile = user_mobile;
	}

	public String getCert_number() {
		return cert_number;
	}

	public void setCert_number(String cert_number) {
		this.cert_number = cert_number;
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

	public String getSubstitute_number() {
		return substitute_number;
	}

	public void setSubstitute_number(String substitute_number) {
		this.substitute_number = substitute_number;
	}

	public Long getPassageway_id() {
		return passageway_id;
	}

	public void setPassageway_id(Long passageway_id) {
		this.passageway_id = passageway_id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getVerification() {
		return verification;
	}

	public void setVerification(String verification) {
		this.verification = verification;
	}

	public String getRegist_bank_name() {
		return regist_bank_name;
	}

	public void setRegist_bank_name(String regist_bank_name) {
		this.regist_bank_name = regist_bank_name;
	}
	
}
