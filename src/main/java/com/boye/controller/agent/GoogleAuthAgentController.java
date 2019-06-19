package com.boye.controller.agent;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.IAgentService;
import com.boye.service.IShopUserService;
import com.boye.service.agent.IGoogleAuthAgentService;
import com.boye.service.shop.IGoogleAuthShopService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/authenticator")
public class GoogleAuthAgentController extends BaseController {
	
	@Autowired
	IGoogleAuthAgentService authService;
	
	/**
     * 启用谷歌令牌校验
     * 操作方：商户
     *
     * @param shopInfo
     * @return
     */
    @RequestMapping(value = "/enableGoogleAuthenticatorByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "enableGoogleAuthenticatorByAgent", notes = "启用谷歌令牌校验")
    @ApiImplicitParam(name = "shopId", value = "商店信息对象")
    public Map<String, Object> enableGoogleAuthenticatorByAgent(String password) {
    	if(StringUtils.isBlank(password)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
		}
    	AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		if (agent == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		String result = authService.enableGoogleAuthenticator(agent);
		if (result != null) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.ADDFAIL);
		}
    }
    
    /**
     * 启用后验证令牌
     * 操作方：商户
     *
     * @param shopInfo
     * @return
     */
    @RequestMapping(value = "/firstAuthenticationByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "firstAuthenticationByAgent", notes = "启用后验证令牌")
    @ApiImplicitParam(name = "code", value = "商店信息对象")
    public Map<String, Object> firstAuthenticationByAgent(String code) {
    	if(StringUtils.isBlank(code)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
		}
    	AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		if (agent == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = authService.firstAuthentication(agent.getId(), code);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.AUTHENTICATIONSUCCESS);
		} else {
			return returnResultMap(ResultMapInfo.AUTHENTICATIONFAIL);
		}
    }
    
    /**
     * 停用谷歌令牌校验
     * 操作方：商户
     *
     * @param shopInfo
     * @return
     */
    @RequestMapping(value = "/disuseGoogleAuthenticatorByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "disuseGoogleAuthenticatorByAgent", notes = "停用谷歌令牌校验")
    @ApiImplicitParam(name = "shopId", value = "商店信息对象")
    public Map<String, Object> disuseGoogleAuthenticatorByAgent(String password, String code) {
    	if(StringUtils.isBlank(password) || StringUtils.isBlank(code)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
    	}
    	AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		if (agent == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = authService.disuseGoogleAuthenticator(agent.getId(), password, code);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
		} else {
			return returnResultMap(ResultMapInfo.ADDFAIL);
		}
    }
    
    /**
     * 验证令牌
     * 操作方：商户
     *
     * @param shopInfo
     * @return
     */
    @RequestMapping(value = "/authenticationByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "authenticationByAgent", notes = "验证令牌")
    @ApiImplicitParam(name = "code", value = "商店信息对象")
    public Map<String, Object> authenticationByAgent(String code) {
    	if(StringUtils.isBlank(code)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
		}
   	AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
		if (agent == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = authService.authentication(agent.getId(), code);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.AUTHENTICATIONSUCCESS);
		} else {
			return returnResultMap(ResultMapInfo.AUTHENTICATIONFAIL);
		}
    }
}
