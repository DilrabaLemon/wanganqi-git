package com.boye.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.QrCodeResponse;
import com.boye.bean.entity.SelfCheck;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ResultMapInfo;
import com.boye.common.utils.MD5;
import com.boye.service.ABCOrderService;
import com.boye.service.impl.ABCOrderServiceImpl;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONObject;

@Controller
@RequestMapping(value = "/abcorder")
public class ABCOrderController extends BaseController{
	
	public static final String RESULTKEY = "361payABC";
	 private static Logger logger = LoggerFactory.getLogger(ABCOrderController.class);
	
	@Autowired
	private ABCOrderService abcOrderService;
	
	/**
     * 获取redis缓存中ABC项目存储内容
     *
     * @return
     */
    
	@ResponseBody
    @RequestMapping(value = "/getQrCodeResponse", method = RequestMethod.GET)
    @ApiOperation(value = "getQrCodeResponse", notes = "获取redis缓存中ABC项目存储内容")
    public Map<String, Object> getQrCodeResponse(String orderNumber,String sign) {
//    	String make = MD5.md5Str(orderNumber + RESULTKEY);
//    	System.out.println(make);
    	
    	if (orderNumber == null || sign == null) {
    		return returnResultMap(ResultMapInfo.NOTPARAM);
		}
    	
    	String makeSign = MD5.md5Str(orderNumber + RESULTKEY);
    	if (!sign.equals(makeSign)) {
    		return returnResultMap(ResultMapInfo.FAILESIGN);
		}

    	String result = abcOrderService.getQrCodeResponse(orderNumber);
    	
        if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS,result);
        } else {
            return returnResultMap(ResultMapInfo.ACCESSTIMEOUT);
        }
    }
	
	
	
	/*
	 * 获取abc系统中selfcheck列表
	 * 
	 */
	
	@ResponseBody
    @RequestMapping(value = "/findSelfCheckList", method = RequestMethod.POST)
    @ApiOperation(value = "findSelfCheckList", notes = "获取abc系统中selfcheck列表")
    public Map<String, Object> findSelfCheckList(@RequestBody QueryBean query) {
		

		Page<SelfCheck> result = abcOrderService.findSelfCheckList(query);
    	
        if (result != null) {
        	 return returnResultMap(ResultMapInfo.GETSUCCESS, result); // 获取成功
        } else {
        	 return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());// 获取失败
        }
    }
	

}
