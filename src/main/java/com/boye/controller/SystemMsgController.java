package com.boye.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.SystemMsg;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.service.SystemMsgService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/systemmsg")
public class SystemMsgController extends BaseController{
	
	@Autowired
	private SystemMsgService systemMsgService;
    /*
     * 添加系统消息
     * 操作方：管理员
     */
	@ResponseBody
	@RequestMapping(value = "/insertSystemMsgByAdmin", method = RequestMethod.POST)
	@ApiOperation(value="insertSystemMsgByAdmin", notes="添加系统消息")
	public Map<String, Object> insertSystemMsgByAdmin(@RequestBody SystemMsg systemMsg){
		if (ParamUtils.paramCheckNull(systemMsg)) {
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = systemMsgService.insertSystemMsgByAdmin(systemMsg);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.SENDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.SENDFAIL);
    	}
	}
	
	 /*
     * 删除系统消息
     * 操作方：管理员
     */
	@ResponseBody
	@RequestMapping(value = "/deleteSystemMsgByAdmin", method = RequestMethod.GET)
	@ApiOperation(value="deleteSystemMsgByAdmin", notes="删除系统消息")
	public Map<String, Object> deleteSystemMsgByAdmin(Long id){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = systemMsgService.deleteSystemMsgByAdmin(id);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.SENDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.SENDFAIL);
    	}
	}
	
	/*
     * 获取系统消息列表
     * 操作方：管理员
     */
	@ResponseBody
	@RequestMapping(value = "/findSystemMsgListByAdmin", method = RequestMethod.GET)
	@ApiOperation(value="findSystemMsgListByAdmin", notes="获取系统消息列表")
	public Map<String, Object> findSystemMsgListByAdmin(@RequestParam Map<String, Object> query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Page<SystemMsg> result = systemMsgService.findSystemMsgListByAdmin(query);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
		}
	}
	
}

