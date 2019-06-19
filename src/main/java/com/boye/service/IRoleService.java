package com.boye.service;

import java.util.List;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.MenuTable;
import com.boye.bean.entity.RoleTable;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface IRoleService {

	public int addRole(AdminInfo admin, RoleTable roleInfo);
	
	public int editRole(RoleTable roleInfo);

	public int deleteRole(String role_id);

	public Page<RoleTable> roleList(QueryBean query);

	public RoleTable roleInfo(String role_id);

	public List<RoleTable> roleAll();

	public List<MenuTable> getMenuListByAdmin(AdminInfo admin);

	public List<MenuTable> findMenuList();

	public int setMenuRoleByRoleId(Long role_id, String menu_ids);

	public int updateMenuRoleByRoleId(Long role_id, String menu_ids);

}
