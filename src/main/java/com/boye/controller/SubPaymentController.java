package com.boye.controller;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.interfaces.NoRepeatSubmit;
import com.boye.common.utils.PropertyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.base.constant.EncryptionUtils;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.SubPaymentQueryVo;
import com.boye.bean.vo.SubPaymentVo;
import com.boye.service.IProvideService;
import com.boye.service.IShopUserService;
import com.boye.service.ISubPaymentService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/subpayment")
public class SubPaymentController extends BaseController {
	
	@Autowired
	private ISubPaymentService subPaymentService;
	
	@Autowired
	private IShopUserService shopUserService;
	
	@Autowired
	private IProvideService provideService;
	
	 /*
     * 开放的代付接口
     * 
     */
    @ResponseBody
    @RequestMapping(value = "/subPaymentInterface", method = RequestMethod.GET)
    @ApiOperation(value = "subPaymentInterface", notes = "代付接口")
    @NoRepeatSubmit
    public Map<String, Object> subPaymentInterface(SubPaymentVo subPayment){
    	
    	if (!subPaymentService.checkShopIp(getIpAddr(), subPayment.getShop_phone())) return resultFail("未添加IP白名单");
    	String checkParamMsg = subPayment.paramCheck();
		if(checkParamMsg != null) return resultFail(checkParamMsg);
		// 根据通道代码，获取第三方接口
		ProvideInfo provide = provideService.getProvideByPassagewayCode(subPayment.getPassageway_code(), subPayment.getShop_phone(), subPayment.getMoney().toString());
		
		Map<String, Object> res = null;
		if (provide != null) {
			// 根据第三方接口代码，判决具体使用那个接口
			switch (provide.getProvide_code()) {
			case "amwydf":
				res = subPaymentService.sendSubPaymentInfoByAMWY(subPayment, provide);
				break;
			case "ymddf":
				res = subPaymentService.sendSubPaymentInfoByYMD(subPayment, provide);
				break;
			case "fopaydf":
				res = subPaymentService.sendSubPaymentInfoByFoPay(subPayment, provide);
				break;
			case "youpay":
				res = subPaymentService.sendSubPaymentInfoByYouPay(subPayment, provide);
				break;
			case "pinansubpay":
				res = subPaymentService.sendSubPaymentInfoByPinAn(subPayment, provide);
				break;
			case "hhlysf":
				res = subPaymentService.sendSubPaymentInfoByHhlYsf(subPayment, provide);
				break;
			case "hhlwak":
				res = subPaymentService.sendSubPaymentInfoByHhlWAK(subPayment, provide);
				break;
			case "test":
				res = subPaymentService.sendSubPaymentInfoByTest(subPayment, provide);
				break;
			}
			if (res != null) return res;
		}
		res = new HashMap<String, Object>();
		res.put("code", 2);
		res.put("data", "");
		res.put("msg", "错误的代付通道代码");
		return res;
    }
    
    /*
     * 开放的代付接口
     * 
     */
    @ResponseBody
    @RequestMapping(value = "/subPaymentOnly", method = RequestMethod.GET)
    @ApiOperation(value = "subPaymentOnly", notes = "代付接口")
    @NoRepeatSubmit
    public Map<String, Object> subPaymentOnly(SubPaymentVo subPayment){
    	
    	if (!subPaymentService.checkShopIp(getIpAddr(), subPayment.getShop_phone())) return resultFail("未添加IP白名单");
    	String checkParamMsg = subPayment.paramCheck();
		if(checkParamMsg != null) return resultFail(checkParamMsg);
		// 根据通道代码，获取第三方接口
		ProvideInfo provide = provideService.getProvideByPassagewayCode(subPayment.getPassageway_code(), subPayment.getShop_phone(), subPayment.getMoney().toString());
		
		Map<String, Object> res = null;
		if (provide != null) {
			// 根据第三方接口代码，判决具体使用那个接口
			switch (provide.getProvide_code()) {
			case "amwydf":
				res = subPaymentService.sendSubPaymentInfoByAMWY(subPayment, provide);
				break;
			case "ymddf":
				res = subPaymentService.sendSubPaymentInfoByYMD(subPayment, provide);
				break;
			case "fopaydf":
				res = subPaymentService.sendSubPaymentInfoByFoPay(subPayment, provide);
				break;
			case "youpay":
				res = subPaymentService.sendSubPaymentInfoByYouPay(subPayment, provide);
				break;
			case "pinansubpay":
				res = subPaymentService.sendSubPaymentInfoByPinAn(subPayment, provide);
				break;
			case "test":
				res = subPaymentService.sendSubPaymentInfoByTest(subPayment, provide);
				break;
			}
			if (res != null) return res;
		}
		res = new HashMap<String, Object>();
		res.put("code", 2);
		res.put("data", "");
		res.put("msg", "错误的支付通道代码");
		return res;
    }
    
    @ResponseBody
    @RequestMapping(value = "/subPaymentQuery", method = RequestMethod.GET)
    @ApiOperation(value = "subPaymentQuery", notes = "代付接口")
    public Map<String, Object> subPaymentQuery(SubPaymentQueryVo subPaymentQuery){
    	if (!subPaymentService.checkShopIp(getIpAddr(), subPaymentQuery.getShop_phone())) return resultFail("未添加IP白名单");
    	String checkParamMsg = subPaymentQuery.paramCheck();
    	if(checkParamMsg != null) return resultFail(checkParamMsg);
    	Map<String, Object> res = subPaymentService.querySubPayment(subPaymentQuery);
    	return res;
    }
    
	private Map<String, Object> resultFail(String msg) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	result.put("code", 2);
		result.put("data", new HashMap<String, Object>());
		result.put("msg", msg);
		return result;
    }
    
	private Map<String, Object> resultSuccess(Object data) {
    	Map<String, Object> result = new HashMap<String, Object>();
    	result.put("code", 2);
		result.put("data", data); 
		result.put("msg", "获取成功");
		return result;
    }
    
    @ResponseBody
    @RequestMapping(value = "/getSubSign", method = RequestMethod.GET)
    @ApiOperation(value = "getSubSign", notes = "代付接口")
    public Map<String, Object> getSubSign(SubPaymentVo subPayment){
		//判断是否是开发环境
		if(!PropertyUtil.isDev()){
			return null;
		};
    	ShopUserInfo shopUser = shopUserService.getShopUserInfo(new ShopUserInfo(10L));
    	String signParam = subPayment.signParam();
    	System.out.println(signParam + shopUser.getSub_open_key());
		String signCode = EncryptionUtils.sign(signParam, shopUser.getSub_open_key(), "SHA-256");
    	return resultSuccess(signCode);
    }
    
    @ResponseBody
    @RequestMapping(value = "/getSubQuerySign", method = RequestMethod.GET)
    @ApiOperation(value = "getSubQuerySign", notes = "代付查询接口")
    public Map<String, Object> getSubQuerySign(SubPaymentQueryVo subPayment){
    	if(!PropertyUtil.isDev()){
			return null;
		};
    	ShopUserInfo shopUser = shopUserService.getShopUserInfo(new ShopUserInfo(10L));
    	String signParam = subPayment.signParam();
    	System.out.println(signParam + shopUser.getSub_open_key());
		String signCode = EncryptionUtils.sign(signParam, shopUser.getSub_open_key(), "SHA-256");
    	return resultSuccess(signCode);
    }
}
