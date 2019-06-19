package com.boye.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.base.entity.BaseEntity;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.common.ResultMapInfo;
import com.boye.service.IRoleService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/index")
public class IndexController extends BaseController {
	
	@Autowired
	private IRoleService roleService;

    /**
	 * 获取登录信息
	 * 操作方： 所有
	 * @param 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@ApiOperation(value="profile", notes="获取登录信息")
    public Map<String, Object> profile(){
    	BaseEntity result = null;
    	if (getSession().getAttribute("login_admin") != null) {
    		AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
    		admin.setRole(roleService.roleInfo(admin.getRole_id().toString()));
    		admin.setPassword("***");
    		result = admin;
    	} else if (getSession().getAttribute("login_agent") != null) {
    		AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
    		agent.setPassword("***");
    		result = agent;
    	} else if (getSession().getAttribute("login_shopUser") != null) {
    		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
    		shopUser.setPassword("***");
    		result = shopUser;
    	}
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
    	} else {
    		return returnResultMap(ResultMapInfo.RELOGIN);//操作失败
    	}
    }
	
	/**
	 * 退出登录
	 * 操作方： 所有
	 * @param 
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	@ApiOperation(value="logout", notes="退出登录")
    public Map<String, Object> logout(){
		AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
		AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		ShopUserInfo shopUserInfo = (ShopUserInfo) getSession().getAttribute("login_shopUser");
		if (admin != null) {
			getSession().setAttribute("login_admin", null);
			return returnResultMap(ResultMapInfo.LOGOTSUCCESS, "admin");
		}
		if (agent != null) {
			getSession().setAttribute("login_agent", null);
			return returnResultMap(ResultMapInfo.LOGOTSUCCESS, "agent");
		}
		getSession().setAttribute("login_shopUser", null);
		return returnResultMap(ResultMapInfo.LOGOTSUCCESS, "shop");
	}
}
