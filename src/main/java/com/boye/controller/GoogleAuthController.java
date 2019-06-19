package com.boye.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boye.bean.entity.AdminInfo;
import com.boye.common.ResultMapInfo;
import com.boye.service.IGoogleAuthAdminService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/authenticator")
public class GoogleAuthController extends BaseController {
	
	@Autowired
	IGoogleAuthAdminService authService;
	
	/**
     * 启用谷歌令牌校验
     * 操作方：管理员
     *
     * @param shopInfo
     * @return
     */
    @RequestMapping(value = "/enableGoogleAuthenticatorByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "enableGoogleAuthenticatorByAdmin", notes = "启用谷歌令牌校验")
    @ApiImplicitParam(name = "password", value = "登录密码")
    public Map<String, Object> enableGoogleAuthenticatorByAdmin(String password) {
    	if(StringUtils.isBlank(password)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
		}
//    	AdminInfo admin = new AdminInfo(11l);
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		String result = authService.enableGoogleAuthenticator(admin);
		if (result != null) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.ADDFAIL);
		}
    }
    
    /**
     * 启用后验证令牌
     * 操作方：管理员
     *
     * @param shopInfo
     * @return
     */
    @RequestMapping(value = "/firstAuthenticationByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "firstAuthenticationByAdmin", notes = "启用后验证令牌")
    @ApiImplicitParam(name = "code", value = "认证码")
    public Map<String, Object> firstAuthenticationByAdmin(String code) {
    	if(StringUtils.isBlank(code)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
		}
//    	AdminInfo admin = new AdminInfo(11l);
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = authService.firstAuthentication(admin.getId(), code);
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
    @RequestMapping(value = "/disuseGoogleAuthenticatorByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "disuseGoogleAuthenticatorByAdmin", notes = "停用谷歌令牌校验")
    @ApiImplicitParams({
        @ApiImplicitParam(name="password",value="密码"),
        @ApiImplicitParam(name="code",value="认证码")
    })

    public Map<String, Object> disuseGoogleAuthenticatorByAdmin(String password, String code) {
    	if(StringUtils.isBlank(password) || StringUtils.isBlank(code)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
    	}
//    	AdminInfo admin = new AdminInfo(11l);
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = authService.disuseGoogleAuthenticator(admin.getId(), password, code);
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
    @RequestMapping(value = "/authenticationByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "authenticationByAdmin", notes = "验证令牌")
    @ApiImplicitParam(name = "code", value = "认证码")
    public Map<String, Object> authenticationByAdmin(String code) {
    	if(StringUtils.isBlank(code)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
		}
//    	AdminInfo admin = new AdminInfo(11l);
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = authService.authentication(admin.getId(), code);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.AUTHENTICATIONSUCCESS);
		} else {
			return returnResultMap(ResultMapInfo.AUTHENTICATIONFAIL);
		}
    }
	
    /**
     * 停用商户谷歌令牌校验（管理员操作）
     * 操作方：管理员
     *
     * @param shopInfo
     * @return
     */
    @RequestMapping(value = "/disuseShopGoogleAuthenticatorByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "disuseShopGoogleAuthenticatorByAdmin", notes = "停用谷歌令牌校验（管理员操作）")
    @ApiImplicitParam(name = "shopId", value = "商户ID")
    public Map<String, Object> disuseShopGoogleAuthenticatorByAdmin(long shopId) {
//    	AdminInfo admin = new AdminInfo(11l);
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = authService.disuseShopGoogleAuthenticator(shopId);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
		} else {
			return returnResultMap(ResultMapInfo.ADDFAIL);
		}
    }
    
    /**
     * 停用代理商谷歌令牌校验（管理员操作）
     * 操作方：管理员
     *
     * @param shopInfo
     * @return
     */
    @RequestMapping(value = "/disuseAgentGoogleAuthenticatorByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "disuseAgentGoogleAuthenticatorByAdmin", notes = "停用代理商谷歌令牌校验（管理员操作）")
    @ApiImplicitParam(name = "agentId", value = "代理商ID")
    public Map<String, Object> disuseAgentGoogleAuthenticatorByAdmin(long agentId) {
//    	AdminInfo admin = new AdminInfo(11l);
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = authService.disuseAgentGoogleAuthenticator(agentId);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
		} else {
			return returnResultMap(ResultMapInfo.ADDFAIL);
		}
    }
}
