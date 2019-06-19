package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;
import com.boye.common.HttpRequestDeviceUtils;

import java.io.Serializable;
import javax.servlet.http.HttpServletRequest;

@Table("agent_login_record")
public class AgentLoginRecord extends BaseEntity implements Serializable {

	

	private static final long serialVersionUID = 8310501199414525268L;
	
	private String agent_name;

	private String login_number;// 账户名
	
	@Column
	private Long agent_id;// 代理商id
	
	@Column
	private int login_type;// 登录类型
	
	@Column
	private String login_ip;// 登录ip
	
	@Column
	private int state;// 登录状态

	
	

	public String getAgent_name() {
		return agent_name;
	}

	public void setAgent_name(String agent_name) {
		this.agent_name = agent_name;
	}

	public Long getAgent_id() {
		return agent_id;
	}

	public void setAgent_id(Long agent_id) {
		this.agent_id = agent_id;
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
