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
import com.boye.bean.vo.BalanceTransferParam;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.TaskInfoVo;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.service.ITaskInfoService;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/taskinfo")
public class TaskInfoController extends BaseController {

	@Autowired
	private ITaskInfoService taskInfoService;
    /*
     * 添加任务计划
     * 操作方：管理员
     */
	@RequestMapping(value = "/createTaskInfo", method = RequestMethod.POST)
	@ApiOperation(value="createTaskInfo", notes="添加系统消息")
	public Map<String, Object> createTaskInfo(@RequestBody TaskInfoVo taskInfoVo){
//		if (ParamUtils.paramCheckNull(taskInfoVo)) {
//    		return returnResultMap(ResultMapInfo.NOTPARAM);
//		}
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = 0;
		if (taskInfoVo.getId() == null) {
			result = taskInfoService.createTaskInfo(taskInfoVo);
		} else {
			result = taskInfoService.editTaskInfo(taskInfoVo);
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
     * 删除任务计划
     * 操作方：管理员
     */
	@RequestMapping(value = "/deleteTaskInfo", method = RequestMethod.GET)
	@ApiOperation(value="deleteTaskInfo", notes="删除系统消息")
	public Map<String, Object> deleteTaskInfo(Long id){
		if (ParamUtils.paramCheckNull(id)) return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = taskInfoService.deleteTaskInfoById(id);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.SENDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.SENDFAIL);
    	}
	}
	
	/*
     * 获取分页任务计划列表
     * 操作方：管理员
     */
	@RequestMapping(value = "/findTaskInfoByPage", method = RequestMethod.GET)
	@ApiOperation(value="findTaskInfoByPage", notes="获取系统消息列表")
	public Map<String, Object> findTaskInfoByPage(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Page<TaskInfo> result = taskInfoService.findTaskInfoByPage(query);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
		}
	}
	
	/*
     * 获取全部任务计划列表
     * 操作方：管理员
     */
	@RequestMapping(value = "/findTaskInfoAll", method = RequestMethod.GET)
	@ApiOperation(value="findTaskInfoAll", notes="获取系统消息列表")
	public Map<String, Object> findTaskInfoAll(){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		List<TaskInfo> result = taskInfoService.findTaskInfoAll();
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
		}
	}
	
	/*
     * 获取任务计划详情
     * 操作方：管理员
     */
	@RequestMapping(value = "/findTaskInfo", method = RequestMethod.GET)
	@ApiOperation(value="findTaskInfo", notes="获取系统消息列表")
	public Map<String, Object> findTaskInfo(Long id){
		if (ParamUtils.paramCheckNull(id)) return returnResultMap(ResultMapInfo.NOTPARAM);
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		TaskInfo result = taskInfoService.findTaskInfoById(id);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
		}
	}
}
