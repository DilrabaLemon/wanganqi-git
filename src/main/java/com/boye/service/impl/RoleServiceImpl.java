package com.boye.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.MenuTable;
import com.boye.bean.entity.RoleMenu;
import com.boye.bean.entity.RoleTable;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.AdminDao;
import com.boye.dao.MenuTableDao;
import com.boye.dao.RoleDao;
import com.boye.dao.RoleMenuDao;
import com.boye.service.IRoleService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class RoleServiceImpl extends BaseServiceImpl implements IRoleService {

	@Resource
	private RoleDao roleDao;
	
	
	@Autowired
	private MenuTableDao menuTableDao;
	
	@Autowired
	private RoleMenuDao roleMenuDao;
	

	@Override
	public int addRole(AdminInfo admin, RoleTable roleInfo) {
		roleInfo.setDelete_flag(0);
    	roleInfo.setCreate_admin_id(admin.getId());
    	return roleDao.insert(roleInfo);
	}
	
	@Override
	public int editRole(RoleTable roleInfo) {
    	RoleTable findRole = roleDao.getObjectById(roleInfo);
    	findRole.setRole_name(roleInfo.getRole_name());
    	findRole.setRole_type(roleInfo.getRole_type());
    	return roleDao.updateByPrimaryKey(findRole);
	}

	@Override
	public int deleteRole(String role_id) {
		RoleTable role = new RoleTable();
    	role.setId(Long.parseLong(role_id));
    	role = roleDao.getObjectById(role);
    	if (role == null) return -1;
    	role.setDelete_flag(1);
    	int result  = roleDao.updateByPrimaryKey(role);
        return result;
	}

	@Override
	public Page<RoleTable> roleList(QueryBean query) {
		Page<RoleTable> page = new Page<>(query.getPage_index(), query.getPage_size());
		// 根据条件查询角色记录数
        int count = roleDao.getRoleByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(roleDao.getRoleByPage(query));
        return page;
	}

	@Override
	public RoleTable roleInfo(String role_id) {
		RoleTable role = new RoleTable();
		role.setId(Long.parseLong(role_id));
		return roleDao.getObjectById(role);
	}

	@Override
	public List<RoleTable> roleAll() {
		return roleDao.getObjectAll(new RoleTable());
	}

	@Override
	public List<MenuTable> getMenuListByAdmin(AdminInfo admin) {
		if(admin.getRole_id() == null || admin.getRole_id() == 0) {
			return null; 
		}
		// 创建一个存放父子级目录菜单集合
		List<MenuTable> newMenuList = new ArrayList<>();
		// 根据角色id查询所有关联的菜单列表
		List<MenuTable> list = menuTableDao.getMenuTableListByRoleId(admin.getRole_id());
			for (MenuTable menuTable : list) {
				// 存放子菜单列表
				 List<MenuTable> childMenuList = new ArrayList<>();
				 //判断是否为父目录
				 if (menuTable.getPid()==0) {
					 for (MenuTable childMenuTable : list) {
						 // 判断是否为该父目录下的子目录
						  if (Objects.equals(menuTable.getId(), childMenuTable.getPid())) {
		                         childMenuList.add(childMenuTable);
		                     }
					 }
					 menuTable.setChildren(childMenuList);
					 newMenuList.add(menuTable);
				}
			}
		return newMenuList;
	}

	@Override
	public List<MenuTable> findMenuList() {
		// 创建一个存放父子级目录菜单集合
				List<MenuTable> newMenuList = new ArrayList<>();
				// 根据角色id查询所有关联的菜单列表
				List<MenuTable> list = menuTableDao.getMenuTableList();
					for (MenuTable menuTable : list) {
						// 存放子菜单列表
						 List<MenuTable> childMenuList = new ArrayList<>();
						 //判断是否为父目录
						 if (menuTable.getPid()==0) {
							 for (MenuTable childMenuTable : list) {
								 // 判断是否为该父目录下的子目录
								  if (Objects.equals(menuTable.getId(), childMenuTable.getPid())) {
				                         childMenuList.add(childMenuTable);
				                     }
							 }
							 menuTable.setChildren(childMenuList);
							 newMenuList.add(menuTable);
						}
					}
				return newMenuList;
	}

	@Override
	public int setMenuRoleByRoleId(Long role_id, String menu_ids) {
		String[] menuIds = menu_ids.split(",");
		for (String menu_id : menuIds) {
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setRole_id(role_id);
			roleMenu.setMenu_id(Long.parseLong(menu_id));
			roleMenuDao.insert(roleMenu);
		}
		return 1;
	}

	@Override
	public int updateMenuRoleByRoleId(Long role_id, String menu_ids) {
		roleMenuDao.updateMenuRoleByRoleId(role_id);
		if(menu_ids == null  || menu_ids == "") {
			return 1;
		}
		String[] menuIds = menu_ids.split(",");
		for (String menu_id : menuIds) {
			RoleMenu roleMenu = new RoleMenu();
			roleMenu.setRole_id(role_id);
			roleMenu.setMenu_id(Long.parseLong(menu_id));
			roleMenuDao.insert(roleMenu);
		}
		return 1;
	}

}
