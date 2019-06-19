package com.boye.controller.agent;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopUserAuditing;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.ShopInformationBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.utils.RedisUtil;
import com.boye.controller.BaseController;
import com.boye.service.agent.AgentShopService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/shop")
public class AgentShopController extends BaseController {
	@Autowired
	private AgentShopService agentShopService;
	/**
	 * 修改商铺抽成(代理商)
	 * 操作方：代理商
     *
     * @param shop_id
     * @param weixin_rote
     * @param alipay_rote
     * @param bank_card_rote
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/addAndEditshopConfigByAgent", method = RequestMethod.POST)
    @ApiOperation(value = "addAndEditshopConfigByAgent", notes = "修改商铺抽成(代理商)")
    @ApiImplicitParam(name = "shopConfig", value = "商户配置")
    public Map<String, Object> addAndEditshopConfigByAgent(@RequestBody ShopConfig shopConfig) {
    	// 获取登入信息
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        // 判断是否登入
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        // 修改商户配置信息
        int result = agentShopService.editShopConfigByAgent(shopConfig);

        if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS);
        } else if (result == -1) {
            return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND);
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);
        }
    }
    
    /**
     	* 商户列表(代理商)
     	* 操作方：代理商
     *
     * @param shop_phone
     * @param start_time
     * @param end_time
     * @param page_index
     * @param page_size
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/shopUserListByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "shopUserListByAgent", notes = "商户列表(代理商)")
    @ApiImplicitParam(name = "query", value = "查询条件")
    public Map<String, Object> shopUserListByAgent(QueryBean query) {
    	// 判断是否登入
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        // 根据条件查询商户信息列表
        Page<ShopInformationBean> result = agentShopService.shopUserPageByAgent(agent, query);

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    
    /**
     * 获取商户信息（代理商）
     * 操作方：代理商
     *
     * @param shop_id
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getShopInfoByAgent", method = RequestMethod.GET)
    @ApiOperation(value = "getShopInfoByAgent", notes = "获取商户信息（代理商）")
    @ApiImplicitParam(name = "shop_id", value = "商户id")
    public Map<String, Object> getShopInfoByAgent(String shop_id) {
    	// 判断参数是否为空
        if (ParamUtils.paramCheckNull(shop_id))
            return returnResultMap(ResultMapInfo.NOTPARAM);
        AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        // 判断是否登入
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        // 根据商户ID 查询商户信息
        ShopInformationBean result = agentShopService.getShopInfoByAgent(shop_id);

        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result);
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
        }
    }
    
    /**
     * 添加商户（代理商）
     * 操作方：代理商
     *
     * 
     */
    @ResponseBody
    @RequestMapping(value = "/insertShopAuditingByAgent", method = RequestMethod.POST)
    @ApiOperation(value = "insertShopAuditingByAgent", notes = "添加商户")
    @ApiImplicitParam(name = "ShopUserAuditing", value = "商户信息")
    public Map<String, Object> insertShopAuditingByAgent(@RequestBody ShopUserAuditing shopUserAuditing){
    	if (ParamUtils.paramCheckNull(shopUserAuditing)) {
    		 return returnResultMap(ResultMapInfo.NOTPARAM);
    	}
    	/*if (shopUserAuditing.getShop_name() == null || shopUserAuditing.getUser_name() == null ||
    			shopUserAuditing.getPassword() == null || shopUserAuditing.getBank_card_number() == null 
    			|| shopUserAuditing.getBank_name()==null || shopUserAuditing.getRegist_bank()==null) {
    		
   		 return returnResultMap(ResultMapInfo.NOTPARAM);
   		 
    	}*/
           
    	AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
        // 判断是否登入
        if (agent == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        shopUserAuditing.setAgent_id(agent.getId());
        //添加审核状态为未审核
        shopUserAuditing.setAuditing_state(0);
        String code = UUID.randomUUID().toString().replaceAll("-", "");
        shopUserAuditing.setUser_code(code);
        // 添加待审核的商户
        int result = agentShopService.insertShopAuditingByAgent(shopUserAuditing);
        if (result == 1) {
            return returnResultMap(ResultMapInfo.SUBMITSUCCESS);
        } else {
            return returnResultMap(ResultMapInfo.SUBMITFAIL);
        }
    }
    
    
    /**
     * 获取redis商户信息测试
     * 操作方：代理商
     *
     * @param shop_id
     * @return
     */
    @Autowired
	private RedisTemplate<String, Object> redisTemplate;
   
    @ResponseBody
    @RequestMapping(value = "/getShopInfoRedisTest", method = RequestMethod.GET)
    @ApiOperation(value = "getShopInfoRedisTest", notes = "获取redis商户信息测试")
    public Map<String, Object> getShopInfoRedisTest(){
    	// id为map的key
    	Object object = redisTemplate.opsForValue().get("ShopUserInfoById");
    	Map<String, ShopUserInfo> map=(Map<String, ShopUserInfo>) object;
    	 Set<String> keySet = map.keySet();
    	for (String string : keySet) {
			System.out.println(map.get(string));
			System.out.println(map.get(string).getId());
		}
    	// loginnumber为map的key
    	Object object1 = redisTemplate.opsForValue().get("ShopUserInfoByLoginNumber");
    	Map<String, ShopUserInfo> map1=(Map<String, ShopUserInfo>) object1;
    	Set<String> keySet2 = map1.keySet();
    	for (String string1 : keySet2) {
			System.out.println(map1.get(string1));
			System.out.println(map1.get(string1).getLogin_number());
		}
    	return null;
    }
}
