package com.boye.bean.entity;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

import java.io.Serializable;

@Table("role_table")
public class RoleTable extends BaseEntity implements Serializable {

	private static final long serialVersionUID = 8618757288714411839L;
	
	@Column
	private String role_name;// 角色名称
	
	@Column
	private int role_type;// 角色类型
	
	@Column
	private Long create_admin_id;// 创建者id
	
	private String admin_name;// 管理员名字
	
	@Override
	public boolean paramIsNull() {
		if (role_name == null | role_name.trim().isEmpty()) return true;
		return false;
	}
	
	public String getAdmin_name() {
		return admin_name;
	}

	public void setAdmin_name(String admin_name) {
		this.admin_name = admin_name;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public int getRole_type() {
		return role_type;
	}

	public void setRole_type(int role_type) {
		this.role_type = role_type;
	}

	public Long getCreate_admin_id() {
		return create_admin_id;
	}

	public void setCreate_admin_id(Long create_admin_id) {
		this.create_admin_id = create_admin_id;
	}

}
