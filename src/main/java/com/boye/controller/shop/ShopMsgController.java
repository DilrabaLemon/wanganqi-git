package com.boye.controller.shop;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SystemMsg;
import com.boye.bean.vo.Page;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.shop.ShopMsgService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/shopMsg")
public class ShopMsgController extends BaseController{
	
	@Autowired
	private ShopMsgService shopMsgService;
	
	/*
	 * 获取系统消息列表
	 * 操作方：商户
	 */
	
	@ResponseBody
    @RequestMapping(value = "/findSystemMsgListByShop", method = RequestMethod.GET)
    @ApiOperation(value = "findSystemMsgListByShop", notes = "获取系统消息列表")
	public Map<String, Object> findSystemMsgListByShop(@RequestParam Map<String, Object> query){
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
		if (shopUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Page<SystemMsg> result = shopMsgService.findSystemMsgListByShop(shopUser,query);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());
		}
	}
	
	
	/*
	 * 获取系统消息详细内容
	 * 操作方：商户
	 */
	@ResponseBody
    @RequestMapping(value = "/getSystemMsgByShop", method = RequestMethod.GET)
    @ApiOperation(value = "getSystemMsgByShop", notes = "获取系统消息详细内容")
	public Map<String, Object> getSystemMsgByShop(Long id){
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
		if (shopUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		SystemMsg result = shopMsgService.getSystemMsgByShop(id);
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL);
		}
	}
	
	
	/*
	 * 删除系统消息
	 * 操作方：商户
	 */
	@ResponseBody
	@RequestMapping(value = "/deleteSystemMsgByShop", method = RequestMethod.GET)
	@ApiOperation(value="deleteSystemMsgByShop", notes="删除系统消息")
	public Map<String, Object> deleteSystemMsgByShop(Long id){
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
		if (shopUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		int result = shopMsgService.deleteSystemMsgByShop(id);
		if (result == 1) {
    		return returnResultMap(ResultMapInfo.SENDSUCCESS);
    	} else {
    		return returnResultMap(ResultMapInfo.SENDFAIL);
    	}
	}
}
