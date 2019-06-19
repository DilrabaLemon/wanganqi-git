package com.boye.controller.agent;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.agent.AgentPassagewayService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/shop")
public class AgentPassagewayController extends BaseController{
	@Autowired
	private AgentPassagewayService agentPassagewayService;
	/**
     * 商户通道配置列表(代理商)
     * 操作方：代理商
     *
     * @param page_index
     * @param page_size
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopConfigListByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "shopConfigListByAgent", notes = "商户通道配置列表(代理商)")
    @ApiImplicitParams({@ApiImplicitParam(name = "query", value = "查询条件"), @ApiImplicitParam(name = "shop_id", value = "商户id")})
    public Map<String, Object> shopConfigListByAgent(QueryBean query, String shop_id) {
    	//获取代理商登入信息
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        //判断代理商是否登入
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        // 根据条件查询商户配置信息列表
        Page<ShopConfig> result = agentPassagewayService.shopConfigPageByAgent(agent, query, shop_id);

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
	
}
