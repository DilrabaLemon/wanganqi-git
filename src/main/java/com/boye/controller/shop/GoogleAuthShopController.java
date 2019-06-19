package com.boye.controller.shop;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.boye.bean.entity.ShopUserInfo;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.IShopUserService;
import com.boye.service.shop.IGoogleAuthShopService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value = "/authenticator")
public class GoogleAuthShopController extends BaseController {
	
	@Autowired
	IGoogleAuthShopService authService;
	
	@Autowired
	IShopUserService shopService;
	
	/**
     * 启用谷歌令牌校验
     * 操作方：商户
     *
     * @param shopInfo
     * @return
     */
    @RequestMapping(value = "/enableGoogleAuthenticatorByShop", method = RequestMethod.GET)
    @ApiOperation(value = "enableGoogleAuthenticatorByShop", notes = "启用谷歌令牌校验")
    @ApiImplicitParam(name = "shopId", value = "商店信息对象")
    public Map<String, Object> enableGoogleAuthenticatorByShop(String password) {
    	if(StringUtils.isBlank(password)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
		}
    	ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
		if (shopUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		String result = authService.enableGoogleAuthenticator(shopUser);
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
    @RequestMapping(value = "/firstAuthenticationByShop", method = RequestMethod.GET)
    @ApiOperation(value = "firstAuthenticationByShop", notes = "启用后验证令牌")
    @ApiImplicitParam(name = "code", value = "商店信息对象")
    public Map<String, Object> firstAuthenticationByShop(String code) {
    	if(StringUtils.isBlank(code)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
		}
    	ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
		if (shopUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = authService.firstAuthentication(shopUser.getId(), code);
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
    @RequestMapping(value = "/disuseGoogleAuthenticatorByShop", method = RequestMethod.GET)
    @ApiOperation(value = "disuseGoogleAuthenticatorByShop", notes = "停用谷歌令牌校验")
    @ApiImplicitParam(name = "shopId", value = "商店信息对象")
    public Map<String, Object> disuseGoogleAuthenticatorByShop(String password, String code) {
    	if(StringUtils.isBlank(password) || StringUtils.isBlank(code)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
    	}
    	ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
		if (shopUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = authService.disuseGoogleAuthenticator(shopUser.getId(), password, code);
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
    @RequestMapping(value = "/authenticationByShop", method = RequestMethod.GET)
    @ApiOperation(value = "authenticationByShop", notes = "验证令牌")
    @ApiImplicitParam(name = "code", value = "商店信息对象")
    public Map<String, Object> authenticationByShop(String code) {
    	if(StringUtils.isBlank(code)) {
			 return returnResultMap(ResultMapInfo.NOTPARAM); 
		}
   	ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
		if (shopUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = authService.authentication(shopUser.getId(), code);
		if (result == 1) {
			return returnResultMap(ResultMapInfo.AUTHENTICATIONSUCCESS);
		} else {
			return returnResultMap(ResultMapInfo.AUTHENTICATIONFAIL);
		}
    }
}
