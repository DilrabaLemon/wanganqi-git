package com.boye.controller.shop;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.base.constant.Constants;
import com.boye.bean.ShopUserDataStatisticsBean;
import com.boye.bean.bo.ShopConfigInfoBean;
import com.boye.bean.entity.LoginWhiteIp;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.LoginInfoBean;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.ShopInformationBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.IGoogleAuthService;
import com.boye.service.IShopUserService;
import com.boye.service.LoginWhiteIpService;
import com.boye.service.shop.ShopUserByShopService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/shop")
public class ShopUserControllerByShop extends BaseController{
	
	@Autowired
    private ShopUserByShopService shopUserService;
	
    @Autowired
    private IShopUserService shopUserInfoService;
	
	@Autowired
	private LoginWhiteIpService loginWhiteIpService;
	
	@Autowired
	private IGoogleAuthService googleAuthService;
	
	/**
     * 商户登录
     * 操作方：商户
     *
     * @param login_number
     * @param password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopUserLogin", method = RequestMethod.POST)
    @ApiOperation(value = "shopUserLogin", notes = "商户登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "login_number", value = "账户名"),
            @ApiImplicitParam(name = "password", value = "密码"), @ApiImplicitParam(name = "ip", value = "ip"), 
            @ApiImplicitParam(name = "code", value = "认证码")})
    public Map<String, Object> shopUserLogin(LoginInfoBean param) {
        if (ParamUtils.paramCheckNull(param.getLogin_number(), param.getPassword()))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        if (StringUtils.isBlank(param.getIp())) param.setIp(getIpAddr());
        ShopUserInfo result = shopUserService.shopUserLogin(param.getLogin_number(), param.getPassword(),param.getIp());  
        if (result != null) {
        	if (result.getId().equals(-3L)) return  returnResultMap(ResultMapInfo.LOGINERRORCOUNTERRROR);
        	if (result.getGoogle_auth_flag() == 1) {
        		if (StringUtils.isBlank(param.getCode())) return returnResultMap(ResultMapInfo.NOTAUTHENTICATION); // 参数不能为空
        		if (googleAuthService.authentication(result.getId(), Constants.USER_CODE_SHOP, param.getCode()) != 1) return returnResultMap(ResultMapInfo.AUTHENTICATIONFAIL);
        	}
        	// 初始化查询参数
            LoginWhiteIp loginWhiteIp = new LoginWhiteIp();
            loginWhiteIp.setUserId(result.getId());
            loginWhiteIp.setUsertype(1);
            LoginWhiteIp findLoginWhiteIp =loginWhiteIpService.getWhiteIP(loginWhiteIp);
            //判断是否设置ip白名单
            Boolean flag = false;
            if(findLoginWhiteIp != null && findLoginWhiteIp.getIp() != null) {
            	String[] findIps = findLoginWhiteIp.getIp().split(",");
            	Map<String,Boolean> map= new HashMap<String,Boolean>();
            	for (String findIp : findIps) {
            		map.put(findIp, true);
				}
            	if (map.get(param.getIp())!= null && map.get(param.getIp()).equals(true)) {
            		flag =true;
				} 	
            }else {
            	flag =true;
            }
        	if(flag == false){
        		return returnResultMap(ResultMapInfo.LONGINIPFAIL);
        	}
            
            result.setPassword("***");
            result.setUser_code("***");
            result.setOpen_key("***");
            result.setSub_open_key("***"); 
            //ShopLoginRecord shopLogin = ShopLoginRecord.getShopLoginRecord(result, ip);// 获取商户登录记录
            getSession().setAttribute("login_shopUser", result);
            getSession().setAttribute("login_admin", null);
            getSession().setAttribute("login_agent", null);
            return returnResultMap(ResultMapInfo.LOGINSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.LOGININFOERRO);
        }
    }
    
    /**
     * 设置商户回调地址
     * 操作方：商户
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/setShopReturnSite", method = RequestMethod.GET)
    @ApiOperation(value = "setShopReturnSite", notes = "获取商户Openid")
    @ApiImplicitParam(name = "return_size", value = "返回地址")
    public Map<String, Object> setShopReturnSite(String return_site) {
        if (ParamUtils.paramCheckNull(return_site))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        int result = shopUserInfoService.setShopReturnSite(shopUser, return_site);
        if (result == 1) {
        	resetSessionUser();
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);
        }
    }
    
    private void resetSessionUser() {
    	ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
    	if (shopUser == null) return;
    	ShopUserInfo result = shopUserService.getShopUserInfoByShop(shopUser);  
    	result.setPassword("***");
        result.setUser_code("***");
        result.setOpen_key("***");
        result.setSub_open_key("***"); 
        getSession().setAttribute("login_shopUser", result);
	}

	/*
	 * 修改密码
	 * 操作方：商户    
	 */
    @ResponseBody
    @RequestMapping(value = "/changePassword", method = RequestMethod.POST)
    @ApiOperation(value = "changePassword", notes = "修改密码")
    public Map<String, Object> changePassword(@RequestBody Map<String, String> param){
    	//获取登入商户
    	if (!param.containsKey("oldPassword") || !param.containsKey("newPassword"))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
    	ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
    	if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
		 int result=shopUserService.changePassword(param.get("oldPassword"), param.get("newPassword"), shopUser);
		 // 判断是否操作成功
		 if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
		 }else if (result == -2) {
			return returnResultMap(ResultMapInfo.OLDPASSWORDERROR);
		 }else if (result == -3) {
			return returnResultMap(ResultMapInfo.PASSWORDFORMATERROR);
		 }else {
			return returnResultMap(ResultMapInfo.ADDFAIL);
		 }
    }
    
    /*
	 * 修改提现密码
	 * 操作方：商户    
	 */
    @ResponseBody
    @RequestMapping(value = "/changeExtractionCode", method = RequestMethod.POST)
    @ApiOperation(value = "changeExtractionCode", notes = "修改密码")
    public Map<String, Object> changeExtractionCode(@RequestBody Map<String, String> param){
    	//获取登入商户
    	if (!param.containsKey("oldExtractionCode") || !param.containsKey("newExtractionCode"))
    		return returnResultMap(ResultMapInfo.NOTPARAM);
    	ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
    	if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
		 int result=shopUserService.changeExtractionCode(param.get("oldExtractionCode"), param.get("newExtractionCode"), shopUser);
		 // 判断是否操作成功
		 if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
		 }else if (result == -2) {
			return returnResultMap(ResultMapInfo.OLDPASSWORDERROR);
		 }else if (result == -3) {
			return returnResultMap(ResultMapInfo.PASSWORDFORMATERROR);
		 }else {
			return returnResultMap(ResultMapInfo.ADDFAIL);
		 }
    }
    
    
	/*
	 * 操作商户ip白名单
	 * 操作方：商户    
	 */
    @ResponseBody
    @RequestMapping(value = "/operateWhiteIPByShop", method = RequestMethod.GET)
    @ApiOperation(value = "operateWhiteIPByShop", notes = "操作商户ip白名单")
    @ApiImplicitParam(name = "loginwhiteip", value = "白名单参数")
    public Map<String, Object> operateWhiteIPByShop(LoginWhiteIp loginWhiteIp){
    	//获取登入商户
    	ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
    	if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
    	// 设置商户id
    	loginWhiteIp.setUserId(shopUser.getId());
    	// 设置用户为商户
    	loginWhiteIp.setUsertype(1);
    	 int result=loginWhiteIpService.operateWhiteIP(loginWhiteIp);
    	 // 判断是否操作成功
    	 if (result == 1) {
			return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	 }else {
			return returnResultMap(ResultMapInfo.ADDFAIL);
    	 }
    	
    }
    
    /*
	 * 获取商户ip白名单
	 * 操作方：商户    
	 */
    @ResponseBody
    @RequestMapping(value = "/getWhiteIPByShop", method = RequestMethod.GET)
    @ApiOperation(value = "getWhiteIPByShop", notes = "获取商户ip白名单")
    public Map<String, Object> getWhiteIPByShop(){
    	LoginWhiteIp loginWhiteIp = new LoginWhiteIp();
    	ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
    	if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
    	loginWhiteIp.setUserId(shopUser.getId());
    	loginWhiteIp.setUsertype(1);
    	LoginWhiteIp result =loginWhiteIpService.getWhiteIP(loginWhiteIp);
    	if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.LISTIFEMPTY, new LoginWhiteIp());
        }
    }
    
    /**
     * 获取商户信息
     * 操作方：商户
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getShopInfo", method = RequestMethod.GET)
    @ApiOperation(value = "getShopInfo", notes = "获取商户信息")
    public Map<String, Object> getShopInfo() {
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        shopUser = shopUserService.getShopUserInfoByShop(shopUser);
        shopUser.setPassword("***");
        shopUser.setUser_code("***");
        shopUser.setOpen_key("***");
        shopUser.setSub_open_key("***");
        return returnResultMap(ResultMapInfo.GETSUCCESS, shopUser);
    }
    
    /**
     * 获取商户OpenKey
     * 操作方：商户
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getShopInfoOpenKey", method = RequestMethod.GET)
    @ApiOperation(value = "getShopInfoOpenKey", notes = "获取商户OpenKey")
    public Map<String, Object> getShopInfoOpenKey() {
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        shopUser = shopUserService.getShopUserInfoByShopSubOpenKey(shopUser);
        return returnResultMap(ResultMapInfo.GETSUCCESS, shopUser.getOpen_key());
    }
    
    /**
     * 获取商户代付OpenKey
     * 操作方：商户
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getShopInfoSubOpenKey", method = RequestMethod.GET)
    @ApiOperation(value = "getShopInfoSubOpenKey", notes = "获取商户代付OpenKey")
    public Map<String, Object> getShopInfoSubOpenKey(String extractionCode) {
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        shopUser = shopUserService.getShopUserInfoByShop(shopUser);
        if (!shopUser.getUser_code().equals(extractionCode)) {
        	return returnResultMap(ResultMapInfo.EXTRACTIONPASSWORDERROR);
        }
        shopUser = shopUserService.getShopUserInfoByShopSubOpenKey(shopUser);
        return returnResultMap(ResultMapInfo.GETSUCCESS, shopUser.getSub_open_key());
    }
    
    /**
     * 获取商户支付通道信息列表
     * 操作方：商户
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getShopConfigByShop", method = RequestMethod.GET)
    @ApiOperation(value = "getShopConfigByShop", notes = "获取商户支付通道信息列表")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> getShopConfigByShop(QueryBean query) {
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        Page<ShopConfigInfoBean> result = shopUserService.getShopConfigByShopUserInfoId(shopUser.getId(), query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 获取商户数据统计
     * 操作方：商户
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getDataStatisticsByShop", method = RequestMethod.GET)
    @ApiOperation(value = "getDataStatisticsByShop", notes = "获取商户数据统计")
    @GetMapping("/getDataStatisticsByShop")
    public Map<String, Object> getDataStatisticsByShop() {
        ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        ShopUserDataStatisticsBean result = shopUserService.getShopDataStatistics(shopUser.getId());

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }
    
    /**
     * 商户修改个人信息
     * 操作方:商户
     */

    @ResponseBody
    @RequestMapping(value = "/editShopUser", method = RequestMethod.POST)
    @ApiOperation(value = "editShopUser", notes = "修改商户信息")
    @ApiImplicitParam(name = "shopInfo", value = "商户信息对象")
    public Map<String, Object> editshopUser(@RequestBody ShopUserInfo shopInfo) {
        if (ParamUtils.paramCheckNull(shopInfo))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        ShopUserInfo shopUserInfo = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUserInfo == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        if (!shopInfo.getId().equals(shopUserInfo.getId())) return returnResultMap(ResultMapInfo.ILLEGALOPERATION);
        int result = shopUserService.shopEditShopUserInfo(shopInfo);
        //ShopUserDataStatisticsBean result = shopUserService.getShopDataStatistics(shopInfo.getId());

        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);
        }
    }
    
    
    /**
     * 商户修改银行卡信息
     * 操作方:商户
     */
    @ResponseBody
    @RequestMapping(value = "/shopUserEditbankCard", method = RequestMethod.POST)
    @ApiOperation(value = "shopUserEditbankCard", notes = "商户修改银行卡信息")
    @ApiImplicitParam(name = "shopInfo", value = "商店信息对象")
    public Map<String, Object> shopUserEditbankCard(@RequestBody ShopInformationBean shopInfo) {
        if (ParamUtils.paramCheckNull(shopInfo))
            return returnResultMap(ResultMapInfo.NOTPARAM);

        ShopUserInfo shopUserInfo = (ShopUserInfo) getSession().getAttribute("login_admin");

        if (shopUserInfo == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        if (shopInfo.getShopUser().getId() == shopUserInfo.getId()) {
        	shopUserService.shopEditBankCard(shopUserInfo);
        }

        ShopUserDataStatisticsBean result = shopUserService.getShopDataStatistics(shopUserInfo.getId());

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }
}
