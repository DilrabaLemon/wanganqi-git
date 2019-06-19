package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.common.HttpRequestDeviceUtils;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

@Table("admin_login_record")
public class AdminLoginRecord extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -9057680434702833451L;

	private String login_number;// 账户名
	
	@Column
	private Long admin_id;// 商户id
	
	@Column
	private int login_type;// 登录类型
	
	@Column
	private String login_ip;// 登录ip
	
	@Column
	private int state;// 登录状态
	
	private String admin_name;// 管理员名称
	
	

	public Long getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(Long admin_id) {
		this.admin_id = admin_id;
	}

	public String getAdmin_name() {
		return admin_name;
	}

	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
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

	
	
}
