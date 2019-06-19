package com.boye.bean.vo;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.boye.base.entity.BaseEntity;
import com.boye.bean.entity.ShopBalanceNew;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopUserInfo;

public class ShopInformationBean extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = -6237734364192221039L;
	
	private Long id;
	private ShopUserInfo shopUser;// 商户id
	
	private List<ShopBalanceNew> balanceList;
	
	private Map<String, Object> balanceType;
	
	private List<ShopConfig> shopConfigs;
	
	private String shop_name;
	
	private int shop_type;
	
	private int shop_category;
	
	private String user_name;
	
	private String login_number;
	
	private int exmaine;
	
	private String password;
	
	private String bank_card_number;
	
	private String bank_name;
	
	private String regist_bank;
	
	private String open_key;
	
	private Integer verification_flag;
	
	private String return_site;
	
	private float turnover;
	
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
	
	public List<ShopConfig> getShopConfigs() {
		return shopConfigs;
	}

	public ShopUserInfo getShopUser() {
		return shopUser;
	}

	public void setShopConfigs(List<ShopConfig> shopConfigs) {
		this.shopConfigs = shopConfigs;
	}

	public void setShopUser(ShopUserInfo shopUser) {
		this.shopUser = shopUser;
	}

	public List<ShopBalanceNew> getBalanceList() {
		return balanceList;
	}

	public void setBalanceList(List<ShopBalanceNew> balanceList) {
		this.balanceList = balanceList;
	}

	public Map<String, Object> getBalanceType() {
		return balanceType;
	}

	public void setBalanceType(Map<String, Object> balanceType) {
		this.balanceType = balanceType;
	}

	public String getShop_name() {
		return shop_name;
	}

	public int getExmaine() {
		return exmaine;
	}

	public void setExmaine(int exmaine) {
		this.exmaine = exmaine;
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

	public Integer getVerification_flag() {
		return verification_flag;
	}

	public void setVerification_flag(Integer verification_flag) {
		this.verification_flag = verification_flag;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public float getTurnover() {
		return turnover;
	}

	public void setTurnover(float turnover) {
		this.turnover = turnover;
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
	
}
