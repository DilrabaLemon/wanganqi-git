package com.boye.bean.entity;

import java.io.Serializable;

import com.boye.base.annotation.Column;
import com.boye.base.annotation.Table;
import com.boye.base.entity.BaseEntity;

@Table("role_menu")
public class RoleMenu extends BaseEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7742156986581572711L;

	@Column
	private Long role_id;
	
	@Column 
	private Long menu_id;

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public Long getMenu_id() {
		return menu_id;
	}

	public void setMenu_id(Long menu_id) {
		this.menu_id = menu_id;
	}
	
	
}
