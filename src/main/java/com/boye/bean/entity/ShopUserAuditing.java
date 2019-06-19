package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("shop_user_auditing")
public class ShopUserAuditing extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8816309636268888453L;
	
	@Column
	private String user_code;// 用户识别码
	
	@Column
	private String user_name;// 用户拥有者名字
	
	@Column
	private String login_number;// 登录账号
	
	@Column
	private String password;// 密码
	
	@Column
	private Integer verification_flag;// 是否开启短信验证功能  0 不开启   1 开启
	
	@Column
	private String shop_name;// 商户企业名字
	
	@Column
	private int shop_type;// 商户类型

	@Column
	private int shop_category;// 商户类别

	@Column
	private String card_name;//持卡人姓名
	
	@Column
	private int examine;// 状态
	
	@Column
	private Long agent_id;// 代理商id
	
	@Column
	private String return_site;// 商户回调地址
	
	@Column
	private String open_key;// 商户openkey

	@Column
	private String bank_card_number;// 银行卡号

	@Column
	private String bank_name;// 所属银行

	@Column
	private String regist_bank;// 银行描述
	
	@Column
	private int auditing_state;//审核状态，0未审核，1已审核,2拒绝
	
	private String agent_name;
	
	@Override
	public boolean paramIsNull() {
		if (shop_name == null || shop_name.isEmpty()) return true;
		if (user_name == null || user_name.isEmpty()) return true;
		if (password == null || password.isEmpty()) return true;
		if (bank_card_number == null || bank_card_number.isEmpty()) return true;
		if (bank_name == null || bank_name.isEmpty()) return true;
		if (regist_bank == null || regist_bank.isEmpty()) return true;
		return false;
	}
	
	
	public String getAgent_name() {
		return agent_name;
	}


	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}


	public String getUser_code() {
		return user_code;
	}

	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getLogin_number() {
		return login_number;
	}

	public void setLogin_number(String login_number) {
		this.login_number = login_number;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getVerification_flag() {
		return verification_flag;
	}

	public void setVerification_flag(Integer verification_flag) {
		this.verification_flag = verification_flag;
	}

	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}

	public int getShop_type() {
		return shop_type;
	}

	public void setShop_type(int shop_type) {
		this.shop_type = shop_type;
	}

	public int getShop_category() {
		return shop_category;
	}

	public void setShop_category(int shop_category) {
		this.shop_category = shop_category;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public int getExamine() {
		return examine;
	}

	public void setExamine(int examine) {
		this.examine = examine;
	}

	public Long getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(Long agent_id) {
		this.agent_id = agent_id;
	}

	public String getReturn_site() {
		return return_site;
	}

	public void setReturn_site(String return_site) {
		this.return_site = return_site;
	}

	public String getOpen_key() {
		return open_key;
	}

	public void setOpen_key(String open_key) {
		this.open_key = open_key;
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

	public int getAuditing_state() {
		return auditing_state;
	}

	public void setAuditing_state(int auditing_state) {
		this.auditing_state = auditing_state;
	}
	
	
}
