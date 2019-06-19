package com.boye.controller.agent;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.AgentOperationRecord;
import com.boye.bean.entity.ExtractionRecordForAgent;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.controller.BaseController;
import com.boye.service.IVerificationCodeService;
import com.boye.service.agent.AgentExtractionService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/extraction")
public class AgentExtractionController extends BaseController{
	
	@Autowired
	private AgentExtractionService agentExtractionService;
	
	@Autowired
	private IVerificationCodeService verificationService;
	 /**
     * 提交提现申请(代理商)
     * 操作方：代理商
     *
     * @param extract
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/submitExtractionByAgent", method = RequestMethod.POST)
    @ApiOperation(value = "submitExtractionByAgent", notes = "提交提现申请(代理商)")
    @ApiImplicitParam(name = "extract", value = "商户的提取记录")
    public Map<String, Object> submitExtractionByAgent(@RequestBody ExtractionRecordForAgent extract) {
    	//判断参数是否为空
    	System.out.println(extract);
        if (ParamUtils.paramCheckNull(extract))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        //获取登入信息
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        //判断是否登入
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        int suc = verificationService.compareVerification(extract, agent);
        
        if (suc == 0) {
        	return returnResultMap(ResultMapInfo.VERIFICATIONERROR);
        } else if (suc == -1) {
        	return returnResultMap(ResultMapInfo.VERIFICATIONTIMEOUT);
        } else if (suc == -4) {
        	return returnResultMap(ResultMapInfo.EXTRACTIONPASSWORDERROR);
        } else if (suc != 1) {
        	return returnResultMap(ResultMapInfo.VERIFICATIONERROR);
        }
        //传入提现信息
        int result = agentExtractionService.addExtractionRecordByAgent(agent, extract);
        AgentOperationRecord agentOperation = AgentOperationRecord.getAgentOperation(agent, ServiceReturnMessage.AGENT_SUBMITEXTRACTION);
        //判断情况
        if (result == 1) {
            return returnResultMap(ResultMapInfo.SUBMITSUCCESS,agentOperation);//提交成功
        } else if (result == 6) {
            return returnResultMap(ResultMapInfo.STATEERROR,agentOperation);//重复提交
        } else if (result == 3) {
            return returnResultMap(ResultMapInfo.INFORMATIONINCONSISTENT,agentOperation);//信息不一致
        } else if (result == 4) {
            return returnResultMap(ResultMapInfo.BALANCEINSUFFICIENT,agentOperation);//余额不足
        } else if (result == -5) {
            return returnResultMap(ResultMapInfo.EXTRACTIONFAIL);//不足2元手续费 
        } else if (result == -21) {
        	return returnResultMap(ResultMapInfo.EXTRACTIONMONEYMAXERROR,agentOperation);//提现金额不得超过15万元
        } else {
            return returnResultMap(ResultMapInfo.SUBMITFAIL,agentOperation);//操作失败
        }
    }
    
    
    /**
     * 获取提现列表(代理商)
     * 操作方：代理商
     *
     * @param shop_phone
     * @param start_time
     * @param end_time
     * @param page_size
     * @param page_index
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/extractionListByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "extractionListByAgent", notes = "获取提现列表(代理商)")
    @ApiImplicitParams({@ApiImplicitParam(name="query" , value = "查询条件"),@ApiImplicitParam(name = "state",value = "当前状态")})
    public Map<String, Object> extractionListByAgent(QueryBean query, Integer state) {
    	// 获取登入信息
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        Page<ExtractionRecordForAgent> result = agentExtractionService.extractionListByAgent(agent, state, query);//分页
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
        }
    }
}
