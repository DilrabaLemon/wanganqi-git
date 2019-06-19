package com.boye.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.common.ResultMapInfo;
import com.boye.service.IVerificationCodeService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/verification")
public class VerificationCodeController extends BaseController {
	
	@Autowired
	private IVerificationCodeService verificationService;
	
	/**
     *  发送消息验证码
     *  操作方：管理员
     * @param substitute
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/sendVerificationCode", method = RequestMethod.GET)
	@ApiOperation(value="sendVerificationCode", notes="发送消息验证码")
	@ApiImplicitParam(name ="mobile",value = "手机号码")
	public Map<String, Object> sendVerificationCode(String mobile) {
    	int result = 0;
    	if (getSession().getAttribute("login_admin") != null) {
    		AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
    		result = verificationService.sendAdminUserMsg(admin.getLogin_number(), admin);
    	} else if (getSession().getAttribute("login_agent") != null) {
    		AgentInfo agent = (AgentInfo) getSession().getAttribute("login_agent");
    		result = verificationService.sendAgentUserMsg(agent.getLogin_number(), agent);
    	} else if (getSession().getAttribute("login_shopUser") != null) {
    		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
    		result = verificationService.sendShopUserMsg(shopUser.getLogin_number(), shopUser);
    	}
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.SENDSUCCESS);//发送成功
    	} else {
    		return returnResultMap(ResultMapInfo.SENDFAIL);//发送失败
    	}
	}
    
    /**
     *  平台提现验证码
     *  操作方：管理员
     * @param substitute
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/sendExtractionCode", method = RequestMethod.GET)
	@ApiOperation(value="sendExtractionCode", notes="平台提现验证码")
	@ApiImplicitParam(name ="mobile",value = "手机号码")
	public Map<String, Object> sendExtractionCode(String mobile) {
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }
        int result = verificationService.sendExtractionCode(admin,mobile);
    	if (result == 2) {
    		return returnResultMap(ResultMapInfo.NONENUMBER);//没有可用的手机号
    	} else if(result == 3) {
    		return returnResultMap(ResultMapInfo.ERRONUMBER);//错误的手机号
    	}else {
    		return returnResultMap(ResultMapInfo.SENDSUCCESS);//发送成功
    	}
    }
}
