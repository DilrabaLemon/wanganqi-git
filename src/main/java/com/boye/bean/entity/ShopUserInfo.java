package com.boye.bean.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("shop_user_info")
public class ShopUserInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -5344511327040251587L;
	
	@Column
	private String user_code;// 用户识别码
	
	@Column
	private String user_name;// 用户拥有者名字
	
	@Column
	private String login_number;// 登录账号
	
	@Column
	private String password;// 密码
	
	@Column
	private int verification_flag;// 是否开启短信验证功能  0 不开启   1 开启
	
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
	private String sub_open_key; //商户代付openkey

	@Column
	private String bank_card_number;// 银行卡号

	@Column
	private String bank_name;// 所属银行
	
	@Column
	private Double min_amount;// 最小支付金额
	
	@Column
	private Double min_extraction;// 最小提现金额

	@Column
	private String regist_bank;// 银行描述
	
	@Column
	private int google_auth_flag;// 是否开启google认证  0 不开启  1 开启
	
	@Column
	private Integer middleman_flag; //中间人标识，0没有中间人，1有中间人
	
	@Column
	private int login_error_count;
	
	@Column
	private String middleman_remark; //中间人备注
	
	private List<ShopBalanceNew> balanceList;
	
	private String agent_name; //所属代理商名称
	
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
	
	public Double getMin_extraction() {
		return min_extraction;
	}


	public void setMin_extraction(Double min_extraction) {
		this.min_extraction = min_extraction;
	}
	
	public String getAgent_name() {
		return agent_name;
	}


	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
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

	public String getBank_card_number() {
		return bank_card_number;
	}

	public void setBank_card_number(String bank_card_number) {
		this.bank_card_number = bank_card_number;
	}
	
	public ShopUserInfo(Long shop_id) {
		this.id = shop_id;
	}
	
	public ShopUserInfo() {
		// TODO Auto-generated constructor stub
	}

	public String getUser_code() {
		return user_code;
	}

	public void setUser_code(String user_code) {
		this.user_code = user_code;
	}

	public Long getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(Long agent_id) {
		this.agent_id = agent_id;
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

	public int getExamine() {
		return examine;
	}

	public void setExamine(int examine) {
		this.examine = examine;
	}

	public Double getMin_amount() {
		return min_amount;
	}

	public void setMin_amount(Double min_amount) {
		this.min_amount = min_amount;
	}

	public String getOpen_key() {
		return open_key;
	}

	public void setOpen_key(String open_key) {
		this.open_key = open_key;
	}

	public String getReturn_site() {
		return return_site;
	}

	public void setReturn_site(String return_site) {
		this.return_site = return_site;
	}

	public int getShop_category() {
		return shop_category;
	}

	public void setShop_category(int shop_category) {
		this.shop_category = shop_category;
	}

	public Integer getVerification_flag() {
		return verification_flag;
	}

	public void setVerification_flag(Integer verification_flag) {
		this.verification_flag = verification_flag;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public void setVerification_flag(int verification_flag) {
		this.verification_flag = verification_flag;
	}

	public Integer getMiddleman_flag() {
		return middleman_flag;
	}

	public void setMiddleman_flag(Integer middleman_flag) {
		this.middleman_flag = middleman_flag;
	}

	public String getMiddleman_remark() {
		return middleman_remark;
	}

	public void setMiddleman_remark(String middleman_remark) {
		this.middleman_remark = middleman_remark;
	}

	public List<ShopBalanceNew> getBalanceList() {
		return balanceList;
	}

	public void setBalanceList(List<ShopBalanceNew> balanceList) {
		this.balanceList = balanceList;
	}

	public String getSub_open_key() {
		return sub_open_key;
	}

	public void setSub_open_key(String sub_open_key) {
		this.sub_open_key = sub_open_key;
	}

	public int getLogin_error_count() {
		return login_error_count;
	}

	public void setLogin_error_count(int login_error_count) {
		this.login_error_count = login_error_count;
	}

	public int getGoogle_auth_flag() {
		return google_auth_flag;
	}

	public void setGoogle_auth_flag(int google_auth_flag) {
		this.google_auth_flag = google_auth_flag;
	}

	@Override
	public String toString() {
		return "ShopUserInfo [user_code=" + user_code + ", user_name=" + user_name + ", login_number=" + login_number
				+ ", password=" + password + ", verification_flag=" + verification_flag + ", shop_name=" + shop_name
				+ ", shop_type=" + shop_type + ", shop_category=" + shop_category + ", card_name=" + card_name
				+ ", examine=" + examine + ", agent_id=" + agent_id + ", return_site=" + return_site + ", open_key="
				+ open_key + ", bank_card_number=" + bank_card_number + ", bank_name=" + bank_name + ", min_amount="
				+ min_amount + ", min_extraction=" + min_extraction + ", regist_bank=" + regist_bank
				+ ", middleman_flag=" + middleman_flag + ", middleman_remark=" + middleman_remark + ", agent_name=" + agent_name + "]";
	}
}
