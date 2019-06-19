package com.boye.controller.agent;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.SystemMsg;
import com.boye.bean.vo.Page;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.agent.AgentMsgService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/agentMsg")
public class AgentMsgController extends BaseController {
	
	@Autowired
	private AgentMsgService agentMsgService;
	
	/*
	 * 获取系统消息列表
	 * 操作方：代理商
	 */
	
	@ResponseBody
    @RequestMapping(value = "/findSystemMsgListByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "findSystemMsgListByAgent", notes = "获取系统消息列表")
	public Map<String, Object> findSystemMsgListByAgent(@RequestParam Map<String, Object> query){
		AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		if (agent == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Page<SystemMsg> result = agentMsgService.findSystemMsgListByAgent(agent,query);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
		}
	}
	
	/*
	 * 获取系统消息详细内容
	 * 操作方：代理商
	 */
	@ResponseBody
    @RequestMapping(value = "/getSystemMsgByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "getSystemMsgByAgent", notes = "获取系统消息详细内容")
	public Map<String, Object> getSystemMsgByAgent(Long id){
		AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		if (agent == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		SystemMsg result = agentMsgService.getSystemMsgByAgent(id);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
	}
	
	/*
	 * 删除系统消息
	 * 操作方：代理商
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteSystemMsgByAgent", method = RequestMethod.GET)
	@ApiOperation(value="deleteSystemMsgByAgent", notes="删除系统消息")
	public Map<String, Object> deleteSystemMsgByAgent(Long id){
		AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		if (agent == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = agentMsgService.deleteSystemMsgByAgent(id);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.SENDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.SENDFAIL);
    	}
	}
}
