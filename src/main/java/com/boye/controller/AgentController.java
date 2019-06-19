package com.boye.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.vo.AgentInformationBean;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.IAgentService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/agent")
public class AgentController extends BaseController {

    @Autowired
    private IAgentService agentService;

    /**
     * 代理商登录
     * 操作方：代理商
     *
     * @param login_number
     * @param password
     * @return
     */
   /* @ResponseBody
    @RequestMapping(value = "/agentLogin", method = RequestMethod.GET)
    @ApiOperation(value = "agentLogin", notes = "代理商登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "login_number", value = "账户名", required = true),
            @ApiImplicitParam(name = "password", value = "密码", required = true)})
    public Map<String, Object> agentLogin(String login_number, String password) {
        if (ParamUtils.paramCheckNull(login_number, password))
            return returnResultMap(ResultMapInfo.NOTPARAM); // 参数判断
        AgentInfo result = agentService.agentLogin(login_number, password); // 根据账号密码查询
        if (result != null) {
            result.setPassword("***");
            getSession().setAttribute("login_agent", result);
            getSession().setAttribute("login_shopUser", null);
            getSession().setAttribute("login_admin", null);
            return returnResultMap(ResultMapInfo.LOGINSUCCESS, result); // 登陆成功
        } else {
            return returnResultMap(ResultMapInfo.LOGINFAIL); // 登录失败
        }
    }*/

    /**
     * 添加代理商
     * 操作方：管理员
     *
     * @param agentInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addAndEditAgent", method = RequestMethod.POST)
    @ApiOperation(value = "addAndEditAgent", notes = "添加代理商")
    @ApiImplicitParam(name = "agentInfo", value = "代理商信息")
    public Map<String, Object> addAndEditAgent(@RequestBody AgentInfo agentInfo) {
        if (ParamUtils.paramCheckNull(agentInfo))
            return returnResultMap(ResultMapInfo.NOTPARAM);//餐宿判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin"); // 获取管理员
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);// 请重新登陆
        }

        int result = 0;
        if (agentInfo.getId() == null) {
            result = agentService.addAgent(agentInfo); // 添加代理商
        } else {
            result = agentService.editAgent(agentInfo); // 修改代理商
        }
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDANDEDITAGENT);// 管理员操作记录
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation); //操作成功
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation); // 账户不存在
        } else if (result == -2) {
            return returnResultMap(ResultMapInfo.PHONENUMBERDUPLTCATION, adminOperation); //该用户已注册
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);//操作失败
        }
    }

    /**
     * 删除代理商
     * 操作方：管理员
     *
     * @param agent_id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteAgent", method = RequestMethod.GET)
    @ApiOperation(value = "deleteAgent", notes = "删除代理商")
    @ApiImplicitParam(name = "agent_id", value = "代理商id")
    public Map<String, Object> deleteAgent(String agent_id) {
        if (ParamUtils.paramCheckNull(agent_id))
            return returnResultMap(ResultMapInfo.NOTPARAM); //参数判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin"); //获取当前登录用户
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); //请重新登陆
        }

        int result = agentService.deleteAgent(agent_id);//根据代理商id删除代理商
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETEAGENT); // 管理员操作记录
        if (result == 1) {
            return returnResultMap(ResultMapInfo.DELETESUCCESS, adminOperation); //删除成功
        } else {
            return returnResultMap(ResultMapInfo.DELETEFAIL, adminOperation);//删除失败
        }
    }

    /**
     * 代理商列表
     * 操作方：管理员
     */
    @ResponseBody
    @RequestMapping(value = "/agentList", method = RequestMethod.GET)
    @ApiOperation(value = "agentList", notes = "代理商列表")
    @ApiImplicitParam(name = "query" ,value = "根据条件查询代理商")
    public Map<String, Object> agentList(QueryBean query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }
        Page<AgentInfo> result = agentService.agentList(query); //分页
        if (result != null) {
        	for (AgentInfo agent : result.getDatalist()) {
        		agent.setPassword("********");
        	}
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); // 获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());// 获取失败
        }
    }

    /**
     * 代理商列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/agentAllList", method = RequestMethod.GET)
    @ApiOperation(value = "agentAllList", notes = "代理商列表")
    public Map<String, Object> agentAllList() {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);// 重新登录
        }
        List<AgentInfo> result = agentService.agentAll();//获取所有的代理商
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);// 操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
        }
    }

    /**
     * 代理商详情
     * 操作方：管理员
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/agentInfo", method = RequestMethod.GET)
    @ApiOperation(value = "agentInfo", notes = "代理商详情")
    @ApiImplicitParam(name = "agent_id" ,value = "代理商id")
    public Map<String, Object> agentInfo(String agent_id) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }

        AgentInfo result = agentService.agentInfo(agent_id);

        if (result != null) {
        	result.setPassword("********");
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//操作失败
        }
    }
    
    
    /**
     * 获取代理商id与名称列表
     * 操作方：管理员
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getAgentIdAndName", method = RequestMethod.GET)
    @ApiOperation(value = "getAgentIdAndName", notes = "获取代理商id与名称列表")
    public Map<String, Object> getAgentIdAndName() {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }
        List<AgentInfo> result=agentService.getAgentIdAndNameList();
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }
    /**
     * 获取代理商数据统计
     * 操作方：代理商
     *
     * @return
     */
   /* @ResponseBody
    @RequestMapping(value = "/getDataStatisticsByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "getDataStatisticsByAgent", notes = "获取代理商数据统计")
    @GetMapping("/getDataStatisticsByAgent")
    public Map<String, Object> getDataStatisticsByAgent() {
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
        }

        AgentDataStatisticsBean result = agentService.getAgentDataStatistics(agent);//代理数据统计

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//操作失败
        }
    }*/
}