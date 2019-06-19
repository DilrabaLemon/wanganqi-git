package com.boye.controller.agent;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.agent.AgentLogService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/log")
public class AgentLogController extends BaseController{
	
	@Autowired
	private AgentLogService agentLogService;
	/**
	 * 获取日志记录(代理商)
	 * 操作方：代理商
	 *
	 * @param record_type 日志类型（ 4.代理商登录日志， 5.代理商操作日志 ）
	 * @param page_index
	 * @param page_size
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAgentOperationRecord", method = RequestMethod.GET)
	@ApiOperation(value = "getAgentOperationRecord", notes = "获取日志记录")
	@ApiImplicitParams({@ApiImplicitParam(name = "query", value = "查询条件"), @ApiImplicitParam(name = "记录类型")})
	public Map<String, Object> getAgentOperationRecord(QueryBean query, Integer record_type) {
		// 参数判断
		if (ParamUtils.paramCheckNull(record_type))
			return returnResultMap(ResultMapInfo.NOTPARAM); 
		// 判断是否登入
		AgentInfo agentInfo = (AgentInfo) getSession().getAttribute("login_agent");
				if (agentInfo == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Page<?> result = null;
		if (record_type != null) {
			// 根据代理商,查询条件登录记录列表
			if (record_type == 4) {
				result = agentLogService.getAgentLoginRecordListByAgent(agentInfo, query); 
			//获取运营记录列表
			} else if (record_type == 5) {
				result = agentLogService.getAgentOperationRecordListByAgent(agentInfo, query);
			}
		}
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
		}
	}

	

}
