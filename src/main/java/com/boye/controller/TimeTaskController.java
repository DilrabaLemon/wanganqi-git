package com.boye.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.TaskInfo;
import com.boye.bean.entity.TimeTaskInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ResultMapInfo;
import com.boye.service.ITimeTaskService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/timetask")
public class TimeTaskController extends BaseController {
	
	@Autowired
	private ITimeTaskService timeTaskService;
    /*
     * 添加定时任务
     * 操作方：管理员
     */
	@RequestMapping(value = "/createTimeTask", method = RequestMethod.POST)
	@ApiOperation(value="createTimeTask", notes="添加定时任务")
	public Map<String, Object> createTimeTask(@RequestBody TimeTaskInfo timeTaskInfo){
//		if (ParamUtils.paramCheckNull(taskInfoVo)) {
//    		return returnResultMap(ResultMapInfo.NOTPARAM);
//		}
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = 0;
		if (timeTaskInfo.getId() == null) {
			result = timeTaskService.createTimeTask(timeTaskInfo);
		} else {
			result = timeTaskService.editTimeTask(timeTaskInfo);
		}
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.SENDSUCCESS);
		} else if (result == -1) {
    		return returnResultMap(ResultMapInfo.TASKDUPLICATION);
    	} else {
    		return returnResultMap(ResultMapInfo.SENDFAIL);
    	}
	}
	
	 /*
     * 删除定时任务
     * 操作方：管理员
     */
	@RequestMapping(value = "/deleteTimeTask", method = RequestMethod.GET)
	@ApiOperation(value="deleteTimeTask", notes="删除定时任务")
	public Map<String, Object> deleteTimeTask(Long id){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = timeTaskService.deleteTimeTaskById(id);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.SENDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.SENDFAIL);
    	}
	}
	
	/*
     * 获取分页定时任务列表
     * 操作方：管理员
     */
	@RequestMapping(value = "/findTimeTaskByPage", method = RequestMethod.GET)
	@ApiOperation(value="findTimeTaskByPage", notes="获取分页定时任务列表")
	public Map<String, Object> findTimeTaskByPage(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Page<TimeTaskInfo> result = timeTaskService.findTimeTaskByPage(query);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
		}
	}
	
	/*
     * 获取所有定时任务列表
     * 操作方：管理员
     */
	@RequestMapping(value = "/findTimeTaskAll", method = RequestMethod.GET)
	@ApiOperation(value="findTimeTaskAll", notes="获取所有定时任务列表")
	public Map<String, Object> findTimeTaskAll(){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		List<TimeTaskInfo> result = timeTaskService.findTimeTaskAll();
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
		}
	}
	
	/*
     * 获取定时任务详情
     * 操作方：管理员
     */
	@RequestMapping(value = "/findTimeTaskInfo", method = RequestMethod.GET)
	@ApiOperation(value="findTimeTaskInfo", notes="获取系统消息列表")
	public Map<String, Object> findTimeTaskInfo(Long id){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		TimeTaskInfo result = timeTaskService.findTimeTaskInfoById(id);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
		}
	}
	
	/*
     * 设置任务是否启用
     * 操作方：管理员
     */
	@RequestMapping(value = "/settingTaskEnable", method = RequestMethod.GET)
	@ApiOperation(value="settingTaskEnable", notes="设置任务是否启用")
	public Map<String, Object> settingTaskEnable(Long id, Integer enable){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = timeTaskService.settingTaskEnable(id, enable);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.SENDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.SENDFAIL);
    	}
	}

}
