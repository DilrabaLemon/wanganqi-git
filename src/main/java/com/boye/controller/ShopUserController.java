package com.boye.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.bo.ShopOpenKeyBo;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.ShopBalanceNew;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopSubConfig;
import com.boye.bean.entity.ShopUserAuditing;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SubPaymentWhiteIp;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.PassagewayShopStatistics;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.IShopUserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/shop")
public class ShopUserController extends BaseController {

    @Autowired
    private IShopUserService shopUserService;
   

    /**
     * 商户登录
     * 操作方：商户
     *
     * @param login_number
     * @param password
     * @return
     */
    /*@ResponseBody
    @RequestMapping(value = "/shopUserLogin", method = RequestMethod.GET)
    @ApiOperation(value = "shopUserLogin", notes = "商户登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "login_number", value = "账户名"),
            @ApiImplicitParam(name = "password", value = "密码"), @ApiImplicitParam(name = "ip", value = "ip")})
    public Map<String, Object> shopUserLogin(String login_number, String password, String ip) {
        if (ParamUtils.paramCheckNull(login_number, password))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        ShopUserInfo result = shopUserService.shopUserLogin(login_number, password,ip);
        if (result != null) {
            System.out.println(result.getId());
            result.setPassword("***");
            //ShopLoginRecord shopLogin = ShopLoginRecord.getShopLoginRecord(result, ip);// 获取商户登录记录
            getSession().setAttribute("login_shopUser", result);
            getSession().setAttribute("login_admin", null);
            getSession().setAttribute("login_agent", null);
            return returnResultMap(ResultMapInfo.LOGINSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.LOGINFAIL);
        }
    }*/

    /**
     * 添加商户
     * 操作方：管理员
     *
     * @param shopInfo
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addAndEditshopUser", method = RequestMethod.POST)
    @ApiOperation(value = "addAndEditshopUser", notes = "添加商户")
    @ApiImplicitParam(name = "shopInfo", value = "商店信息对象")
    public Map<String, Object> addAndEditshopUser(@RequestBody ShopUserInfo shopInfo) {
        if (ParamUtils.paramCheckNull(shopInfo))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        int result = 0;
        if (shopInfo.getId() == null) {
            result = shopUserService.addShopUser(shopInfo);
        } else {
            result = shopUserService.editShopUser(shopInfo);
        }
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDANDEDITSHOP);// 添加管理员操作记录
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
        } else if (result == -2) {
            return returnResultMap(ResultMapInfo.PHONENUMBERDUPLTCATION, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
        }
    }

    /**
     * 删除商户
     * 操作方：管理员
     *
     * @param shopUser_id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteshopUser", method = RequestMethod.GET)
    @ApiOperation(value = "deleteshopUser", notes = "删除商户")
    @ApiImplicitParam(name = "shopUser_id", value = "商户id")
    public Map<String, Object> deleteshopUser(String shopUser_id) {
        if (ParamUtils.paramCheckNull(shopUser_id))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        int result = shopUserService.deleteShopUser(shopUser_id);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETESHOP);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.DELETESUCCESS, adminOperation);
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
        } else if (result == -11) {
            return returnResultMap(ResultMapInfo.ACCOUNTUSENOTDELETE, adminOperation);
        } else if (result == -12) {
            return returnResultMap(ResultMapInfo.HASBALANCENOTDELETE, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.DELETEFAIL, adminOperation);
        }
    }
    
    /**
     * 商户列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopUserList", method = RequestMethod.GET)
    @ApiOperation(value = "shopUserList", notes = "商户列表")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> shopUserList(QueryBean query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        Page<ShopUserInfo> result = shopUserService.shopUserList(query);

        if (result != null) {
        	for (ShopUserInfo shopUser : result.getDatalist()) {
        		shopUser.setPassword("***");
        	}
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 商户列表金额统计
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopUserListCount", method = RequestMethod.GET)
    @ApiOperation(value = "shopUserListCount", notes = "商户列表统计")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> shopUserListCount(QueryBean query) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        List<ShopBalanceNew> result = shopUserService.shopUserListCount(query);
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL,new ShopUserInfo());
        }
        
    }
    
    /**
     * 商户列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopUserIDAndName", method = RequestMethod.GET)
    @ApiOperation(value = "shopUserIDAndName", notes = "获取所有商户名称和id")
    public Map<String, Object> shopUserIDAndName(){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);
         }
         // 获取商户的id和name
         List<ShopUserInfo> result = shopUserService.shopUserIDAndName();
         if (result != null) {
             return returnResultMap(ResultMapInfo.GETSUCCESS, result);
         } else {
             return returnResultMap(ResultMapInfo.GETFAIL);
         }
    }
    
    /**
     * 商户列表(代理商)
     * 操作方：代理商
     *
     * @return
     */
    /*@ResponseBody
    @RequestMapping(value = "/shopUserListByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "shopUserListByAgent", notes = "商户列表(代理商)")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> shopUserListByAgent(QueryBean query) {
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        Page<ShopInformationBean> result = shopUserService.shopUserPageByAgent(agent, query);

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }*/

    /**
     * 商户通道配置列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopConfigList", method = RequestMethod.GET)
    @ApiOperation(value = "shopConfigList", notes = "商户通道配置列表")
    public Map<String, Object> shopConfigList(QueryBean query, String shop_id) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
    	query.setType(0);
        Page<ShopConfig> result = shopUserService.shopConfigList(query, shop_id);

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 商户充值通道配置列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopRechargeConfigList", method = RequestMethod.GET)
    @ApiOperation(value = "shopConfigList", notes = "商户充值通道配置列表")
    public Map<String, Object> shopRechargeConfigList(QueryBean query, String shop_id) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
    	query.setType(3);
        Page<ShopConfig> result = shopUserService.shopConfigList(query, shop_id);

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 删除商户的支付通道配置
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteShopConfig", method = RequestMethod.GET)
    @ApiOperation(value = "deleteShopConfig", notes = "商户通道配置列表")
    public Map<String, Object> deleteShopConfig(String config_id) {
    	if (ParamUtils.paramCheckNull(config_id))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        int result = shopUserService.deleteShopConfig(config_id);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETESHOPCONFIG);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
        } else if (result == -1) {
        	return returnResultMap(ResultMapInfo.SHOPCONFIGERROR, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
        }
    }
    
    /**
     * 商户代付通道配置列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopSubConfigList", method = RequestMethod.GET)
    @ApiOperation(value = "shopSubConfigList", notes = "商户代付通道配置列表")
    public Map<String, Object> shopSubConfigList(QueryBean query, String shop_id) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        query.setType(1);
        Page<ShopSubConfig> result = shopUserService.shopSubConfigList(query, shop_id);

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 删除商户的支付通道配置
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/deleteShopSubConfig", method = RequestMethod.GET)
    @ApiOperation(value = "deleteShopSubConfig", notes = "商户通道配置列表")
    public Map<String, Object> deleteShopSubConfig(String config_id) {
    	if (ParamUtils.paramCheckNull(config_id))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        int result = shopUserService.deleteShopSubConfig(config_id);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETESHOPSUBCONFIG);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
        } else if (result == -1) {
        	return returnResultMap(ResultMapInfo.SHOPCONFIGERROR, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
        }
    }
    
    /**
     * 停用/恢复商户配置
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/enableShopSubConfig", method = RequestMethod.GET)
    @ApiOperation(value = "enableShopSubConfig", notes = "停用/恢复商户配置")
    public Map<String, Object> enableShopSubConfig(String config_id, Integer enable) {
    	if (ParamUtils.paramCheckNull(config_id))
            return returnResultMap(ResultMapInfo.NOTPARAM);
    	if (ParamUtils.paramCheckNull(enable))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        int result = shopUserService.enableShopSubConfig(config_id, enable);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ENABLESHOPSUBCONFIG);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
        } else if (result == -1) {
        	return returnResultMap(ResultMapInfo.SHOPCONFIGERROR, adminOperation);
        } else if (result == -3) {
        	return returnResultMap(ResultMapInfo.PARAMERROR, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
        }
    }
    
    /**
     * 停用/恢复商户配置
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/enableShopConfig", method = RequestMethod.GET)
    @ApiOperation(value = "enableShopConfig", notes = "停用/恢复商户配置")
    public Map<String, Object> enableShopConfig(String config_id, Integer enable) {
    	if (ParamUtils.paramCheckNull(config_id))
            return returnResultMap(ResultMapInfo.NOTPARAM);
    	if (ParamUtils.paramCheckNull(enable))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        int result = shopUserService.enableShopConfig(config_id, enable);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ENABLESHOPCONFIG);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
        } else if (result == -1) {
        	return returnResultMap(ResultMapInfo.SHOPCONFIGERROR, adminOperation);
        } else if (result == -3) {
        	return returnResultMap(ResultMapInfo.PARAMERROR, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
        }
    }

    /**
     * 商户通道配置列表(代理商)
     * 操作方：代理商
     *
     * @return
     */
    /*@ResponseBody
    @RequestMapping(value = "/shopConfigListByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "shopConfigListByAgent", notes = "商户通道配置列表(代理商)")
    @ApiImplicitParams({@ApiImplicitParam(name = "query", value = "查询条件"), @ApiImplicitParam(name = "shop_id", value = "商户id")})
    public Map<String, Object> shopConfigListByAgent(QueryBean query, String shop_id) {
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        Page<ShopConfig> result = shopUserService.shopConfigPageByAgent(agent, query, shop_id);

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }*/


    /**
     * 配置商铺支付通道费率
     * 操作方：管理员
     *
     * @param shopConfig{shop_id, passageway_id, pay_rate, agent_rate}
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addAndEditshopConfig", method = RequestMethod.POST)
    @ApiOperation(value = "addAndEditshopConfig", notes = "配置商铺支付通道费率")
    @ApiImplicitParam(name = "shopConfig", value = "商户配置")
    public Map<String, Object> addAndEditshopConfig(@RequestBody ShopConfig shopConfig) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        int result = 0;
        if (shopConfig.getId() == null) {
            result = shopUserService.addShopConfig(shopConfig);
        } else {
            result = shopUserService.editShopConfig(shopConfig);
        }

        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_EDITSHOPCONFIG);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
        } else if (result == -2) {
            return returnResultMap(ResultMapInfo.NUMBERDUPLICATION, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
        }
    }
    
    /**
     * 配置商铺代付通道费率
     * 操作方：管理员
     *
     * @param shopConfig{shop_id, passageway_id, pay_rate, agent_rate}
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addAndEditshopSubConfig", method = RequestMethod.POST)
    @ApiOperation(value = "addAndEditshopSubConfig", notes = "配置商铺支付通道费率")
    @ApiImplicitParam(name = "shopSubConfig", value = "商户配置")
    public Map<String, Object> addAndEditshopSubConfig(@RequestBody ShopSubConfig shopSubConfig) {
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        int result = 0;
        if (shopSubConfig.getId() == null) {
            result = shopUserService.addShopSubConfig(shopSubConfig);
        } else {
            result = shopUserService.editShopSubConfig(shopSubConfig);
        }

        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_EDITSHOPCONFIG);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
        } else if (result == -2) {
            return returnResultMap(ResultMapInfo.NUMBERDUPLICATION, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
        }
    }

    /**
     * 获取商户信息（平台）
     * 操作方：管理员
     *
     * @param shop_id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getShopInfoByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "getShopInfoByAdmin", notes = "获取商户信息（平台）")
    @ApiImplicitParam(name = "shop_id", value = "商户id")
    public Map<String, Object> getShopInfoByAdmin(String shop_id) {
        if (ParamUtils.paramCheckNull(shop_id))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        ShopUserInfo result = shopUserService.getShopInfoByAdmin(shop_id);
        if (result != null) {
        	result.setPassword("********");
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }


    /**
     * 获取商户OpenKey
     * 操作方：管理员
     *
     * @param shop_id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getShopOpenKey", method = RequestMethod.GET)
    @ApiOperation(value = "getShopOpenKey", notes = "获取商户Openid")
    @ApiImplicitParam(name = "shop_id", value = "商户id")
    public Map<String, Object> getShopOpenKey(String shop_id) {
        if (ParamUtils.paramCheckNull(shop_id))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }

        ShopOpenKeyBo result = shopUserService.getShopOpenKey(shop_id);

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);
        }
    }

    /**
     * 修改商户密码
     * 操作方：管理员
     *
     * @param shop_id
     * @param new_password
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeShopPasswordByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "changeShopPasswordByAdmin", notes = "修改商户密码")
    @ApiImplicitParams({@ApiImplicitParam(name = "shop_id", value = "商户id"), @ApiImplicitParam(name = "new_password", value = "新密码")})
    public Map<String, Object> changeShopPasswordByAdmin(@RequestBody String shop_id, @RequestBody String new_password) {
        if (ParamUtils.paramCheckNull(shop_id, new_password))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        int result = shopUserService.changeShopPassword(shop_id, new_password);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_CHANGESHOPPWD);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.EDITSUCCESS, adminOperation);
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.EDITFAIL, adminOperation);
        }
    }
    
    /**
     * 变更商户代付openKey
     * 操作方：管理员
     *
     * @param shop_id
     * @param state
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/changeShopSubOpenKey", method = RequestMethod.GET)
    @ApiOperation(value = "changeShopSubOpenKey", notes = "冻结商户账户")
    @ApiImplicitParams({@ApiImplicitParam(name = "shop_id", value = "商户id"), @ApiImplicitParam(name = "state", value = "状态")})
    public Map<String, Object> changeShopSubOpenKey(Long shop_id) {
        if (ParamUtils.paramCheckNull(shop_id))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        int result = shopUserService.changeShopSubOpenKey(shop_id);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_CHANGESHOPOPENKEY);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
        } else if (result == -6) {
            return returnResultMap(ResultMapInfo.STATEERROR, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
        }
    }

    /**
     * 冻结商户账户
     * 操作方：管理员
     *
     * @param shop_id
     * @param state
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopFrozen", method = RequestMethod.GET)
    @ApiOperation(value = "shopFrozen", notes = "冻结商户账户")
    @ApiImplicitParams({@ApiImplicitParam(name = "shop_id", value = "商户id"), @ApiImplicitParam(name = "state", value = "状态")})
    public Map<String, Object> shopFrozen(String shop_id, Integer state) {
        if (ParamUtils.paramCheckNull(shop_id) || ParamUtils.paramCheckNull(state))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        int result = shopUserService.shopFrozen(shop_id, state);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_FROZENSHOP);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
        } else if (result == -6) {
            return returnResultMap(ResultMapInfo.STATEERROR, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
        }
    }

    /**
     * 禁止商户提现
     * 操作方：管理员
     *
     * @param shop_id
     * @param state
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopExtractionAvailable", method = RequestMethod.GET)
    @ApiOperation(value = "shopExtractionAvailable", notes = "禁止商户提现")
    @ApiImplicitParams({@ApiImplicitParam(name = "shop_id", value = "商户id"), @ApiImplicitParam(name = "state", value = "状态")})
    public Map<String, Object> shopExtractionAvailable(String shop_id, Integer state) {
        if (ParamUtils.paramCheckNull(shop_id) || ParamUtils.paramCheckNull(state))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        int result = shopUserService.shopExtractionAvailable(shop_id, state);
        AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_SHOPEXTRACTIONAVAILABLE);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND, adminOperation);
        } else if (result == -6) {
            return returnResultMap(ResultMapInfo.STATEERROR, adminOperation);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);
        }
    }
    
    
    /**
     *获取代理商添加的商户列表
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getShopAuditingByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "getShopAuditingByAgent", notes = "获取代理商添加的商户")
    public Map<String, Object> getShopAuditingByAgent(QueryBean query){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);
         }
         Page<ShopUserAuditing> result=shopUserService.getShopAuditingByAgent(query);
         if (result != null) {
             return returnResultMap(ResultMapInfo.GETSUCCESS, result);
         } else {
             return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
         }
    }
    
    /**
     * 获取代理商添加的商户信息
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findShopAuditingByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "findShopAuditingByAgent", notes = "获取代理商添加的商户详细信息")
    public Map<String, Object> findShopAuditingByAgent(@RequestParam Integer shopUserAuditing_id){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);
         }
         ShopUserAuditing result= shopUserService.findShopAuditingByAgent(shopUserAuditing_id);
         if (result != null) {
             return returnResultMap(ResultMapInfo.GETSUCCESS, result);
         } else {
             return returnResultMap(ResultMapInfo.GETFAIL);
         }
    }
    
    
    /**
     * 存入管理员审核后的商户信息并且删除代理商 临时存储的商户信息
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editShopAuditingByAgent", method = RequestMethod.POST)
    @ApiOperation(value = "editShopAuditingByAgent", notes = "审核代理商添加的商户")
    public Map<String, Object> editShopAuditingByAgent(@RequestBody ShopUserInfo shopUserInfo){
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        int result = shopUserService.editShopAuditingByAgent(shopUserInfo);
        
        if (result == 2) {
            return returnResultMap(ResultMapInfo.SUBMITSUCCESS);
        } else {
            return returnResultMap(ResultMapInfo.SUBMITFAIL);
        }
    }
    
    /**
     * 拒绝代理商添加的商户
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/refuseShopAuditingByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "refuseShopAuditingByAgent", notes = "拒绝代理商添加的商户")
    public Map<String, Object> refuseShopAuditingByAgent(@RequestParam Integer shopUserAuditing_id){
    	 AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
         if (admin == null) {
             return returnResultMap(ResultMapInfo.RELOGIN);
         }
         int result= shopUserService.refuseShopAuditingByAgent(shopUserAuditing_id);
         if (result == 1) {
             return returnResultMap(ResultMapInfo.GETSUCCESS);
         } else {
             return returnResultMap(ResultMapInfo.GETFAIL);
         }
    }
    
    /**
     * 设置商户最小支付金额
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editShopMinAmount", method = RequestMethod.GET)
    @ApiOperation(value = "editShopMinAmount", notes = "设置商户最小支付金额")
    public Map<String, Object> editShopMinAmount(String shop_id, Double min_amount){
    	if (ParamUtils.paramCheckNull(shop_id)) {
    		return returnResultMap(ResultMapInfo.NOTPARAM);
    	}
    	if (ParamUtils.paramCheckNull(min_amount)) {
    		return returnResultMap(ResultMapInfo.NOTPARAM);
    	}
		AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
		    return returnResultMap(ResultMapInfo.RELOGIN);
		}
		
		int result= shopUserService.editShopMinAmount(shop_id, min_amount);
		if (result == 1) {
		    return returnResultMap(ResultMapInfo.GETSUCCESS);
		} else if (result == -1) {
		     return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND);
		} else {
		     return returnResultMap(ResultMapInfo.GETFAIL);
		}
    }
    
    /**
     * 设置商户最小提现金额
     * 操作方：管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/editShopMinExtraction", method = RequestMethod.GET)
    @ApiOperation(value = "editShopMinExtraction", notes = "设置商户最小提现金额")
    public Map<String, Object> editShopMinExtraction(String shop_id, Double min_extraction){
    	if (ParamUtils.paramCheckNull(shop_id)) {
    		return returnResultMap(ResultMapInfo.NOTPARAM);
    	}
    	if (ParamUtils.paramCheckNull(min_extraction)) {
    		return returnResultMap(ResultMapInfo.NOTPARAM);
    	}
		AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
		    return returnResultMap(ResultMapInfo.RELOGIN);
		}
		
		int result= shopUserService.editShopMinExtraction(shop_id, min_extraction);
		if (result == 1) {
		    return returnResultMap(ResultMapInfo.GETSUCCESS);
		} else if (result == -1) {
		     return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND);
		} else {
		     return returnResultMap(ResultMapInfo.GETFAIL);
		}
    }
    
    /*
     * 更新当日商户通道成交率存入数据库
     * 操作方：管理员     */
    @ResponseBody
	@RequestMapping(value = "/passagewayShopTurnoverRateByDay", method = RequestMethod.GET)
    @ApiOperation(value="passagewayShopTurnoverRateByDay", notes="更新当日商户通道成交率存入数据库")
    public Map<String, Object> passagewayShopTurnoverRateByDay(){
    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int  result = shopUserService.passagewayShopTurnoverRateByDay();
		if (result != 0) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
    }
    
    /*
     * 获取当日商户通道成交率
     * 操作方：管理员
     */
    @ResponseBody
	@RequestMapping(value = "/getpassagewayShopTurnoverRateByDay", method = RequestMethod.GET)
    @ApiOperation(value="getpassagewayShopTurnoverRateByDay", notes="获取当日商户通道成交率")
    public Map<String, Object> getpassagewayShopTurnoverRateByDay(QueryBean query){
    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Page<PassagewayShopStatistics> result = shopUserService.getpassagewayShopTurnoverRateByDay(query);
		
	 if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    
    /*
     * 添加/修改商户代付IP白名单
     * 操作方：管理员
     */
    @ResponseBody
	@RequestMapping(value = "/insertAndEditSubPaymentWhiteIp", method = RequestMethod.POST)
    @ApiOperation(value="insertAndEditSubPaymentWhiteIp", notes="添加/修改商户代付IP白名单")
    public Map<String, Object> insertAndEditSubPaymentWhiteIp(@RequestBody SubPaymentWhiteIp subPaymentWhiteIp){
    	if (subPaymentWhiteIp == null || subPaymentWhiteIp.getShop_id() == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
    	
    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = shopUserService.insertAndEditSubPaymentWhiteIp(subPaymentWhiteIp);
		
		if (result != 0) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL);
    	}
    }
    
//    /*
//     * 修改商户代付IP白名单
//     * 操作方：管理员
//     */
//    @ResponseBody
//	@RequestMapping(value = "/updateSubPaymentWhiteIp", method = RequestMethod.POST)
//    @ApiOperation(value="updateSubPaymentWhiteIp", notes="修改商户代付IP白名单")
//    public Map<String, Object> updateSubPaymentWhiteIp(@RequestBody SubPaymentWhiteIp subPaymentWhiteIp){
//    	if (subPaymentWhiteIp == null || subPaymentWhiteIp.getShop_id() == null) {
//			return returnResultMap(ResultMapInfo.NOTPARAM);
//		}
//    	
//    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
//		if (admin == null) {
//			return returnResultMap(ResultMapInfo.RELOGIN);
//		}
//		int result = shopUserService.updateSubPaymentWhiteIp(subPaymentWhiteIp);
//		
//		if (result != 0) {
//    		return returnResultMap(ResultMapInfo.GETSUCCESS);
//    	} else {
//    		return returnResultMap(ResultMapInfo.GETFAIL);
//    	}
//    }
    
    /*
     * 删除商户代付IP白名单
     * 操作方：管理员
     */
    @ResponseBody
	@RequestMapping(value = "/deleteSubPaymentWhiteIp", method = RequestMethod.GET)
    @ApiOperation(value="deleteSubPaymentWhiteIp", notes="删除商户代付IP白名单")
    public Map<String, Object> deleteSubPaymentWhiteIp(@RequestParam Long shop_id){
    	if ( shop_id == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = shopUserService.deleteSubPaymentWhiteIp(shop_id);
		
		if (result != 0) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);
    	}
    }
    
    /*
     * 获取商户代付IP白名单
     * 操作方：管理员
     */
    @ResponseBody
	@RequestMapping(value = "/getSubPaymentWhiteIp", method = RequestMethod.GET)
    @ApiOperation(value="getSubPaymentWhiteIp", notes="获取商户代付IP白名单")
    public Map<String, Object> getSubPaymentWhiteIp(@RequestParam Long shop_id){
    	if ( shop_id == null) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		SubPaymentWhiteIp result = shopUserService.getSubPaymentWhiteIp(shop_id);
		
		if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS,result);
    	} else {
    		return returnResultMap(ResultMapInfo.LISTIFEMPTY);
    	}
    }
    
    
    /*
     * 刷新缓存中商户信息和商户配置
     * 操作方：管理员
     */
    @ResponseBody
	@RequestMapping(value = "/updateRedisByShop", method = RequestMethod.GET)
    @ApiOperation(value="updateRedisByShop", notes="刷新缓存中商户信息和商户配置")
    public Map<String, Object> updateRedisByShop(){
    	AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = shopUserService.updateRedisByShop();
		
		if (result != 0) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS);
    	}
    }
    
}