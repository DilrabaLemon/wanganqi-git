package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("login_whiteip")
public class LoginWhiteIp extends BaseEntity implements Serializable {

	private static final long serialVersionUID = -9183782544233727447L;
	
	@Column
	private Long userId; //商户id，代理商id，管理员id
	
	@Column
	private int usertype; //1为商户，2为代理，3为管理员
	
	@Column
	private String ip; // ip串格式如下 1.1.1.1,2.2.2.2,3.3.3.3,4.4.4.4\r\n若为空，或者数据为空则都允许

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public int getUsertype() {
		return usertype;
	}

	public void setUsertype(int usertype) {
		this.usertype = usertype;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	

	
	
}
