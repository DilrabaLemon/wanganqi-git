package com.boye.bean.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.constant.DateConstant;
import com.boye.base.entity.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Table("platform_admin_info")
@Data
public class AdminInfo extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8957513236294209156L;
	
	@Column
	private String admin_code;// 管理员识别码
	
	@Column
	private String admin_name;// 管理员名字
	
	@Column
	private Long role_id;// 角色id
	
	@Column
	private String login_number;// 账户名
	
	@Column
	private String password;// 密码
	
	@Column
	private int google_auth_flag;// 是否开启google认证  0 不开启  1 开启
	
	@Column
	private int login_error_count;// 登录失败次数
	
	@Column
	@JsonFormat(pattern = DateConstant.DATE_TIME_FORMAT_TWO, timezone = DateConstant.TIME_ZONE)
	private Timestamp last_login_time;
	
	private RoleTable role;
	
	public AdminInfo(long id) {
		this.id = id;
	}
	
	public AdminInfo() {}

	@Override
	public boolean paramIsNull() {
		if (admin_name == null || admin_name.trim().isEmpty()) return true;
		if (role_id == null) return true;
		if (login_number == null || login_number.trim().isEmpty()) return true;
		if (password == null || password.trim().isEmpty()) return true;
		return false;
	}

	@Override
	public String toString() {
		return "AdminInfo{" +
				"id='" + id + '\'' +
				", admin_name='" + admin_name + '\'' +
				", role_id='" + role_id + '\'' +
				", login_number='" + login_number + '\'' +
				", password='" + password + '\'' +
				", delete_flag=" + delete_flag +
				", last_login_time=" + last_login_time +
				'}';
	}
}
