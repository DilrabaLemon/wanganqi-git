package com.boye.controller;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import com.boye.service.impl.PayNewServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.api.HtmlTemplate;
import com.boye.base.constant.EncryptionUtils;
import com.boye.bean.bo.OrderInfoBo;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.bean.vo.PaymentChannelParam;
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.bean.vo.ShopAuthenticationInfo;
import com.boye.bean.vo.SubAuthenticationInfo;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.interfaces.NoRepeatSubmit;

import com.boye.service.IDictService;
import com.boye.service.IPayService;
import com.boye.service.IProvideService;
import com.boye.service.PayNewService;
import com.google.gson.Gson;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/pay")
public class PayController extends BaseController {
	private static Logger logger = LoggerFactory.getLogger(PayController.class);
	@Autowired
	private IPayService payService;
	
	@Autowired
	private IProvideService provideService;
	
	@Autowired
	private PayNewService payNewService;
	
//	@Autowired
//	private IPayReturnMsgService payReturnMsgService;
	
	@Autowired
	private IDictService dictService;

	/**
	 * 商户鉴权接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/shopAuthentication", method = RequestMethod.POST)
	@ApiOperation(value="shopAuthentication", notes="商户鉴权接口")
	@ApiImplicitParam(name = "shopAuthentication" , value = "商店认证信息")
	public Map<String, Object> shopAuthentication(@RequestBody ShopAuthenticationInfo shopAuthentication) {
//		if(shopAuthentication.paramCheck()) {
//			return returnResultMap(ResultMapInfo.NOTPARAM);
//		}
		Map<String, Object> result = payService.shopAuthentication(shopAuthentication);
		return result;
	}
	
	/**
	 * 用户鉴权接口（四要素，订单编号，支付金额）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/customerAuthentication", method = RequestMethod.POST)
	@ApiOperation(value="customerAuthentication", notes="用户鉴权接口")
	@ApiImplicitParam(name = "authentication" , value = "身份验证信息")
	public Map<String, Object> customerAuthentication(@RequestBody AuthenticationInfo authentication) {
		if(authentication.paramCheck()) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
		Map<String, Object> result = payService.customerAuthentication(authentication);
		return result;
	}
	
	/**
	 * 代付通道接口（订单编号，支付金额）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/paymentChannel", method = RequestMethod.POST)
	@ApiOperation(value="paymentChannel", notes="代付通道接口")
	@ApiImplicitParam(name = "param" , value = "付款频道接口对象")
	public Map<String, Object> paymentChannel(@RequestBody PaymentChannelParam param) {
		if(param.paramCheck()) {
			return returnResultMap(ResultMapInfo.NOTPARAM);
		}
//		Map<String, Object> result = payService.getPayInformation(param);
		return null;
	}
	
	/**
	 * 二维码支付通道接口（订单编号，支付金额）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getQrAuthentication", method = RequestMethod.GET)
	@ApiOperation(value="getQrAuthentication", notes="二维码代付通道接口")
	@ApiImplicitParam(name = "authentication" , value = "身份验证信息")
	@NoRepeatSubmit
	public Map<String, Object> getQRAuthentication(AuthenticationInfo authentication) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(authentication.paramCheck()) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "参数不能为空");
			return result;
		}
		if (authentication.getOrder_number().length() > 40) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "订单号的长度不能大于40位");
			return result;
		}
		logger.info("订单号："+authentication.getOrder_number()+"订单金额"+authentication.getPayment());
		// 风控规则判断
		result = payNewService.windControlJudgement(authentication.getPayment(),authentication.getPassageway_code(), authentication.getShop_phone());
		if (result != null && result.get("code").equals(2)) {
			return result;
		}
		// 根据通道代码，获取第三方接口
		ProvideInfo provide = provideService.getProvideByPassagewayCode(authentication.getPassageway_code(), authentication.getShop_phone(), authentication.getPayment());
		
		Map<String, Object> res = null;
		if (provide != null) {
			// 根据第三方接口代码，判决具体使用那个接口
			if (provide.getProvide_code().equals("jhzf")) {
				res = payService.getQRByJh(authentication, provide);
			} else if (provide.getProvide_code().equals("h5tl")) {
				res = payService.getQRByH5(authentication, provide);
//				res = getAlipayUrl(res);
			} else if (provide.getProvide_code().equals("ytcpu")) {
				res = payService.getQRByYtcpu(authentication, provide);
			} else if (provide.getProvide_code().equals("ysh5")) {
				res = payService.getQRByYsH5(authentication, provide);
//				res = getAlipayUrl(res);
			} else if (provide.getProvide_code().equals("hhlh5")) {
				res = payService.getQRByHhl(authentication, provide);
			} else if (provide.getProvide_code().equals("jhaltl")) {
				res = payService.getQRByJh(authentication, provide);
				res = getAlipayUrl(res);
			} else if (provide.getProvide_code().equals("zhfh5")) {
				res = payService.getQRByZHF(authentication, provide);
			} else if (provide.getProvide_code().equals("alipayjh")) {
				res = payService.getAlipayJh(authentication, provide);
			}
			if (res != null) return res;
		}
		res = new HashMap<String, Object>();
		res.put("code", 2);
		res.put("data", "");
		res.put("msg", "错误的支付通道代码");
		return res;
	}
	
	/**
	 * 银行支付通道接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/bankPay", method = RequestMethod.GET)
	@ApiOperation(value="bankPay", notes=" 银行支付通道接口")
	@ApiImplicitParam(name = "authentication" , value = "身份验证信息")
	@NoRepeatSubmit
	public Map<String, Object> bankPay(QuickAuthenticationInfo authentication) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(authentication.paramCheck()) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "参数不能为空");
			return result;
		}
		if (authentication.getOrder_number().length() > 40) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "订单号的长度不能大于40位");
			return result;
		}
		
		// 风控规则判断
		result = payNewService.windControlJudgement(authentication.getPayment(),authentication.getPassageway_code(), authentication.getShop_phone());
		if (result != null && result.get("code").equals(2)) {
			return result;
		}
		
		ProvideInfo provide = provideService.getProvideByPassagewayCode(authentication.getPassageway_code(), authentication.getShop_phone(), authentication.getPayment());
		
		Map<String, Object> res = null;
		if (provide != null) {
			if (provide.getProvide_code().equals("quickpay")) {
				res = payService.getQuickPayPage(authentication, provide);
			}else if (provide.getProvide_code().equals("hhlquick")) {
				res = payService.getHhlQuickPayPage(authentication, provide);
			}else if (provide.getProvide_code().equals("hhlwy")) {
				res = payService.getHhlWYPayPage(authentication, provide);
			}
			if (res != null) return res;
		}
		res = new HashMap<String, Object>();
		res.put("code", 2);
		res.put("data", "");
		res.put("msg", "错误的支付通道代码");
		return res;
	}
	
	/**
	 * 支付宝跳链接口（订单编号，支付金额）
	 */
    private Map<String, Object> getAlipayUrl(Map<String, Object> res) {
		String base_url = dictService.getObjectByKeyAndType("ccbalipay", 2);
		if (!res.get("code").equals(1)) return res;
		String pay_url = res.get("data").toString();
		try {
			pay_url = URLEncoder.encode(pay_url, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			res.put("code", 2);
			res.put("data", "");
		}
		res.put("data", base_url + "?url=" + pay_url);
        return res;
    }
	
	/**
	 * 测试用，获取签名
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getQrSign", method = RequestMethod.GET)
	@ApiOperation(value="getQrSign", notes="测试用，获取签名")
	@ApiImplicitParam(name = "authentication" , value = "身份验证信息")
	public Map<String, Object> getQrSign(QuickAuthenticationInfo authentication) {
		String res = payService.getQRSign(authentication);
		Map<String, Object> resultMap = returnResultMap(ResultMapInfo.GETSUCCESS);
		resultMap.put("data", res);
		return resultMap;
	}
	
    private String getFormatString(String str) {
        BigDecimal de = new BigDecimal(str);
        DecimalFormat df = new DecimalFormat("#0.00");
        return df.format(de);
    }
    
    /**
     * 回调补发接口
     * 操作方： 管理员
     *
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/sendOrderInfoToUser", method = RequestMethod.GET)
    @ApiOperation(value = "sendOrderInfoToUser", notes = "回调补发接口")
    public Map<String, Object> sendOrderInfoToUser(String order_id, String psd) {
    	if (order_id == null)
            return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
        AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录  
        }

        int result = payService.sendOrderInfoToUser(order_id, psd); // 调用发送回调信息的接口  
        
        if (result == 1) {
            return returnResultMap(ResultMapInfo.SENDSUCCESS);//发送成功
        } if (result == -1) {
        	return returnResultMap(ResultMapInfo.NOTFINDORDER);//没有找到订单
        } if (result == -2) {
        	return returnResultMap(ResultMapInfo.ORDERSTATEERROR);//订单状态异常（只有已成功的订单才能发送回调）
        } else {
            return returnResultMap(ResultMapInfo.SENDFAIL); //发送失败
        }
    }
    
    /*
     * 多次请求测试
     */
    @ResponseBody
    @RequestMapping(value = "/textRequest")
    @ApiOperation(value = "textRequest", notes = "多次请求测试")
    @NoRepeatSubmit
    public String textRequest() {
    	
        System.out.println("程序逻辑返回");
    	 return "程序逻辑返回";
    }

	/*
	 * 多次请求测试
	 */
	@ResponseBody
	@RequestMapping(value = "/returnRequest",method = RequestMethod.GET)
	@ApiOperation(value = "returnRequest", notes = "多次请求测试")
	public String returnRequest() {

//		System.out.println("程序逻辑返回");
		return "success";
	}
    
    
    /**
     * 查询订单状态
     * 操作方：商户
     *
     * @param order_num
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/orderState", method = RequestMethod.GET)
    @ApiOperation(value = "orderState", notes = "查询订单状态")
    @ApiImplicitParams({@ApiImplicitParam(name = "order_number", value = "订单号", required = true), 
    	@ApiImplicitParam(name = "shop_phone", value = "商户手机号", required = true), 
    	@ApiImplicitParam(name = "sign", value = "签名" , required = true)})
    public Map<String, Object> orderState(String order_number,String shop_phone,String sign) {
    	// 校验参数
        if (ParamUtils.paramCheckNull(order_number,shop_phone,sign)){
        	return returnResultMap(ResultMapInfo.NOTPARAM);
        }
        //判断商户是否存在
        ShopUserInfo shopUser = payService.getShopUserByMobile(shop_phone);
        if(shopUser == null) return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND);
        //校验签名
        StringBuffer result = new StringBuffer();
        result.append("order_number=" + order_number);
        result.append("&shop_phone=" + shop_phone);
        String signCode =EncryptionUtils.sign(result.toString(),shopUser.getOpen_key(),"SHA-256");
        System.out.println(signCode);
        if(!signCode.equals(sign)) return returnResultMap(ResultMapInfo.PARAMERROR);
        //查询订单状态
        OrderInfo order = payService.getOrderState(order_number,shopUser.getId());
        OrderInfoBo orderBo = new OrderInfoBo();
        if (order != null) {
        	orderBo.setOrderInfo(order);
        }
        if(order != null){
        	//操作成功
            return returnResultMap(ResultMapInfo.GETSUCCESS, orderBo); 
        }else{
        	return returnResultMap(ResultMapInfo.NOTFINDPLATFORMACCOUNT);
        }
    }
    
    
    /**
     * 查询订单状态
     * 操作方：商户（新）
     *
     * @param order_num
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/orderStateByUser", method = RequestMethod.GET)
    @ApiOperation(value = "orderStateByUser", notes = "用户查询订单状态")
    @ApiImplicitParams({@ApiImplicitParam(name = "order_number", value = "订单号", required = true), 
    	@ApiImplicitParam(name = "shop_phone", value = "商户手机号", required = true), 
    	@ApiImplicitParam(name = "sign", value = "签名" , required = true)})
    public Map<String, Object> orderStateByUser(String order_number,String shop_phone,String sign) {
    	// 校验参数
        if (ParamUtils.paramCheckNull(order_number,shop_phone,sign)){
        	return returnResultMap(ResultMapInfo.NOTPARAM);
        }
        //判断商户是否存在
        ShopUserInfo shopUser = payService.getShopUserByMobile(shop_phone);
        if(shopUser == null) return returnResultMap(ResultMapInfo.ACCOUNTNOTFIND);
        //校验签名
        StringBuffer result = new StringBuffer();
        result.append("order_number=" + order_number);
        result.append("&shop_phone=" + shop_phone);
        String signCode =EncryptionUtils.sign(result.toString(),shopUser.getOpen_key(),"SHA-256");
        System.out.println(signCode);
        if(!signCode.equals(sign)) return returnResultMap(ResultMapInfo.PARAMERROR);
        //查询订单状态
        OrderInfo order = payService.orderStateByUser(order_number,shopUser.getId());
        OrderInfoBo orderBo = new OrderInfoBo();
        if (order != null) {
        	orderBo.setOrderInfo(order);
        }
        if(order != null){
        	//操作成功
            return returnResultMap(ResultMapInfo.GETSUCCESS, orderBo); 
        }else{
        	return returnResultMap(ResultMapInfo.NOTFINDPLATFORMACCOUNT);
        }
    }
    
    /**
     * 查询订单状态
     * 操作方：管理员
     *
     * @param order_num
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/checkOrderStateByAdmin", method = RequestMethod.GET)
    @ApiOperation(value = "checkOrderStateByAdmin", notes = "查询订单状态")
    public Map<String, Object> checkOrderStateByAdmin(String order_id) {
    	AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
        if (admin == null) {
            return returnResultMap(ResultMapInfo.RELOGIN); // 重新登录
        }
    	
        //查询订单状态
        OrderInfo order = payService.getOrderState(order_id);
        OrderInfoBo orderBo = new OrderInfoBo();
        if (order != null) {
        	orderBo.setOrderInfo(order);
        }
        if(order != null){
        	//操作成功
            return returnResultMap(ResultMapInfo.GETSUCCESS, orderBo); 
        }else{
        	return returnResultMap(ResultMapInfo.NOTFINDPLATFORMACCOUNT);
        }
    }
  
}