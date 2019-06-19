package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

import lombok.Data;

@Table("google_authenticator")
@Data
public class GoogleAuthenticatorBean extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 7464417405167610848L;

	@Column
	private Long user_id; //商户id，代理商id，管理员id
	
	@Column
	private int user_type; //1为商户，2为代理，3为管理员
	
	@Column
	private String google_key; // 商户对应的GoogleAnthenticator的KEY
	
}
