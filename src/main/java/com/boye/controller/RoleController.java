package com.boye.controller;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.ApiImplicitParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.MenuTable;
import com.boye.bean.entity.RoleTable;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.IRoleService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/role")
public class RoleController extends BaseController {

	@Autowired
	private IRoleService roleService;
	
	
	/*
	 * 根据用户获取角色对应的菜单列表
	 */
	@ResponseBody
	@RequestMapping(value = "/getMenuListByAdmin", method = RequestMethod.GET)
	@ApiOperation(value="getMenuListByAdmin", notes="用户获取角色对应的菜单列表")
	 public Map<String, Object> getMenuListByAdmin(){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		List<MenuTable> result =null;
		if(admin.getAdmin_name().equals("admin")) {
			 result = roleService.findMenuList();
		}else {
			 result = roleService.getMenuListByAdmin(admin);
		}
		if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
	}
	
	/*
	 * 获取所有的菜单列表
	 */
	@ResponseBody
	@RequestMapping(value = "/findMenuList", method = RequestMethod.GET)
	@ApiOperation(value="findMenuList", notes="获取所有的菜单列表")
	public Map<String, Object> findMenuList(){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		List<MenuTable> result = roleService.findMenuList();
		if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
	}
	
	
	/*
	 * 获取某个角色已选的菜单列表
	 */
	@ResponseBody
	@RequestMapping(value = "/findMenuListByRoleId", method = RequestMethod.GET)
	@ApiOperation(value="findMenuListByRoleId", notes="获取某个角色已选的菜单列表")
	public Map<String, Object> findMenuListByRoleId(Long role_id){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		AdminInfo adminInfo = new AdminInfo();
		adminInfo.setRole_id(role_id);
		List<MenuTable> result = roleService.getMenuListByAdmin(adminInfo);
		if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
	}
	
	
	/*
	 * 为角色添加对应菜单
	 */
	@ResponseBody
	@RequestMapping(value = "/setMenuRoleByRoleId", method = RequestMethod.GET)
	@ApiOperation(value="setMenuRoleByRoleId", notes="为角色添加对应菜单")
	public Map<String, Object> setMenuRoleByRoleId(Long role_id,String menu_ids){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		if (role_id == null || role_id == 0 || menu_ids == null || menu_ids == "") {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int result = roleService.setMenuRoleByRoleId(role_id,menu_ids);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL);
    	}
		
	}
	
	/*
	 * 修改角色对应菜单
	 */
	@ResponseBody
	@RequestMapping(value = "/updateMenuRoleByRoleId", method = RequestMethod.GET)
	@ApiOperation(value="updateMenuRoleByRoleId", notes="修改角色对应菜单")
	public Map<String, Object> updateMenuRoleByRoleId(Long role_id,String menu_ids){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		if (role_id == null || role_id == 0 ) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		int result = roleService.updateMenuRoleByRoleId(role_id,menu_ids);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL);
    	}
	}
	
	
	
	/**
     *  添加角色
     *  操作方：管理员
     * @param roleInfo
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/addAndEditRole", method = RequestMethod.POST)
	@ApiOperation(value="addAndEditRole", notes="添加角色")
	@ApiImplicitParam(name = "roleInfo",value = "角色信息")
    public Map<String, Object> addAndEditRole(@RequestBody RoleTable roleInfo){
    	if (ParamUtils.paramCheckNull(roleInfo))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = 0;
    	if (roleInfo.getId() == null) {
    		result = roleService.addRole(admin, roleInfo);
    	}else {
    		result = roleService.editRole(roleInfo);
    	}
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDANDEDITROLE);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
    	}
    }
	
    /**
     *  删除角色
     *  操作方：管理员
     * @param roleInfo
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/deleteRole", method = RequestMethod.GET)
	@ApiOperation(value="deleteRole", notes="删除角色")
	@ApiImplicitParam(name = "role_id" , value = "角色id")
    public Map<String, Object> deleteRole(String role_id){
    	if (ParamUtils.paramCheckNull(role_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	int result = roleService.deleteRole(role_id);
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETEROLE);
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.DELETESUCCESS, adminOperation);
    	} else {
    		return returnResultMap(ResultMapInfo.DELETEFAIL, adminOperation);
    	}
    }
	
    /**
     *  获取角色列表
     *  操作方：管理员
     * @param page_size
     * @param page_index
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/roleList", method = RequestMethod.GET)
	@ApiOperation(value="roleList", notes="获取角色列表")
	@ApiImplicitParam(name = "query" , value = "查询条件")
    public Map<String, Object> roleList(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	Page<RoleTable> result = roleService.roleList(query);
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
    	}
    }
	
    /**
     *  获取角色详情
     *  操作方：管理员
     * @param role_id
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/roleInfo", method = RequestMethod.GET)
	@ApiOperation(value="roleInfo", notes="获取角色详情")
	@ApiImplicitParam(name = "role_id",value = "角色id")
    public Map<String, Object> roleInfo(String role_id){
    	if (ParamUtils.paramCheckNull(role_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	
    	RoleTable result = roleService.roleInfo(role_id);
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
    }
    
    /**
     *  获取所有角色
     *  操作方：管理员
     * @param page_size
     * @param page_index
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/roleAll", method = RequestMethod.GET)
	@ApiOperation(value="roleAll", notes="获取所有角色")
    public Map<String, Object> roleAll(){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
    	List<RoleTable> result = roleService.roleAll();
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
    }
}