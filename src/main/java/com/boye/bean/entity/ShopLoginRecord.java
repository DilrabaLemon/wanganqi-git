package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.common.HttpRequestDeviceUtils;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

@Table("shop_login_record")
public class ShopLoginRecord extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -9057680434702833451L;

	private String login_number;// 账户名
	
	@Column
	private Long shop_id;// 商户id
	
	@Column
	private int login_type;// 登录类型
	
	@Column
	private String login_ip;// 登录ip
	
	@Column
	private int state;// 登录状态
	
	private String shop_name;// 商户名字
	
	public Long getShop_id() {
		return shop_id;
	}

	public void setShop_id(Long shop_id) {
		this.shop_id = shop_id;
	}

	public String getLogin_number() {
		return login_number;
	}

	public void setLogin_number(String login_number) {
		this.login_number = login_number;
	}

	public int getLogin_type() {
		return login_type;
	}

	public void setLogin_type(int login_type) {
		this.login_type = login_type;
	}

	public String getLogin_ip() {
		return login_ip;
	}

	public void setLogin_ip(String login_ip) {
		this.login_ip = login_ip;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public static ShopLoginRecord getShopLoginRecord(ShopUserInfo shopUser, String ip) {
		ShopLoginRecord shopLogin = new ShopLoginRecord();
		shopLogin.setDelete_flag(0);
		shopLogin.setLogin_ip(ip);
		if(shopUser != null) shopLogin.setShop_id(shopUser.getId());
		return shopLogin;
	}
//	
//	public static ShopLoginRecord getShopLoginRecord(ShopUserSessionBean shopUser, String ip) {
//		ShopLoginRecord shopLogin = new ShopLoginRecord();
//		shopLogin.setDelete_flag(0);
//		shopLogin.setLogin_ip(ip);
//		if(shopUser != null) shopLogin.setShop_id(shopUser.getId());
//		return shopLogin;
//	}
	public String getShop_name() {
		return shop_name;
	}

	public void setShop_name(String shop_name) {
		this.shop_name = shop_name;
	}
	
}
