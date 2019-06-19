package com.boye.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.base.constant.Constants;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.bean.vo.NewQuickAuthenticationInfo;
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.bean.vo.SubAuthenticationInfo;
import com.boye.common.ResultMapInfo;
import com.boye.common.interfaces.NoRepeatSubmit;
import com.boye.service.IDictService;
import com.boye.service.IProvideService;
import com.boye.service.PayNewService;
import com.google.gson.Gson;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/paynew")
public class PayNewController extends BaseController{
	private static Logger logger = LoggerFactory.getLogger(PayNewController.class);
	@Autowired
	private PayNewService payNewService;
	
	@Autowired
	private IProvideService provideService;
	
	@Autowired
	private IDictService dictService;
	
	/**
	 * 支付宝H5跳链支付
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getQrAuthentication", method = RequestMethod.GET)
	@ApiOperation(value="getQrAuthentication", notes="二维码代付通道接口")
	@ApiImplicitParam(name = "authentication" , value = "身份验证信息")
	@NoRepeatSubmit
	public Map<String, Object> getQRAuthentication(AuthenticationInfo authentication) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(authentication == null){
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "请求错误，无法解析参数");
			return result;
		}
		if(authentication.paramCheck()) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "参数不能为空:"+authentication.strparamCheck());
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
		ProvideInfo provide = provideService.getProvideByPassagewayCode(authentication.getPassageway_code(), authentication.getShop_phone(), authentication.getPayment());
		Gson gson = new Gson();
		Map<String, Object> res = null;
		if (provide != null) {
			if (provide.getProvide_code().equals("jhzf")) {
				res = payNewService.getQRByJh(authentication, provide);
			} else if (provide.getProvide_code().equals("h5tl")) {
				res = payNewService.getQRByH5(authentication, provide);
//				res = getAlipayUrl(res);
			} else if (provide.getProvide_code().equals("ytcpu")) {
				res = payNewService.getQRByYtcpu(authentication, provide);
			} else if (provide.getProvide_code().equals("ysh5")) {
				res = payNewService.getQRByYsH5(authentication, provide);
//				res = getAlipayUrl(res);
			} else if (provide.getProvide_code().equals("hhlh5")) {
				res = payNewService.getQRByHhl(authentication, provide, Constants.HHL_PAY_TYPE_ALIPAY);
			} else if (provide.getProvide_code().equals("hhlh5altl")) {
				res = payNewService.getQRByHhl(authentication, provide, Constants.HHL_PAY_TYPE_ALIPAY);
			} else if (provide.getProvide_code().equals("jhaltl")) {
				res = payNewService.getQRByJh(authentication, provide);
				res = getAlipayUrl(res, "ccbalipay");
			} else if (provide.getProvide_code().equals("zhfh5")) {
				res = payNewService.getQRByZHF(authentication, provide);
			} else if (provide.getProvide_code().equals("payhh5")) {
				res = payNewService.getQRByPAYH(authentication, provide);
				res = getAlipayUrl(res, "pinganalipay");
			} else if(provide.getProvide_code().equals("jjsmpay")){
				res =payNewService.getByJjsmPay(authentication, provide);
			}else if (provide.getProvide_code().equals("newzhfh5")) {
				res = payNewService.getQRByNEWZHF(authentication, provide);	
			} else if (provide.getProvide_code().equals("jzfsm")) {
				res = payNewService.getJZFSM(authentication, provide);
			} else if (provide.getProvide_code().equals("jzfh5")) {
				res = payNewService.getJZFH5(authentication, provide);
			} else if (provide.getProvide_code().equals("alipayjh")) {
				res = payNewService.getAlipayJh(authentication, provide);
			} else if (provide.getProvide_code().equals("pgyer")) {
				res = payNewService.getByPgyer(authentication, provide);
			} else if (provide.getProvide_code().equals("fopay")) {
				res = payNewService.getByFoPay(authentication, provide);
			} else if (provide.getProvide_code().equals("facaipay")) {
				res = payNewService.getByFaCaiPay(authentication, provide);
			} else if (provide.getProvide_code().equals("hjpay")) {
				res = payNewService.getByHJPay(authentication, provide);
			} else if (provide.getProvide_code().equals("upay")) {
				res = payNewService.getByUPay(authentication, provide);
			} else if (provide.getProvide_code().equals("bepay")) {
				res = payNewService.getByBePay(authentication, provide);
			} else if (provide.getProvide_code().equals("onlinepay")) {
				res = payNewService.getPayByOnlinePay(authentication, provide);
			} else if (provide.getProvide_code().equals("cccpay")) {
				res = payNewService.getPayByCccPay(authentication, provide);
			} else if (provide.getProvide_code().equals("vpay")) {
				res = payNewService.getByBeTwoPay(authentication, provide);
			} else if (provide.getProvide_code().equals("mypay")) {
				res = payNewService.getByMYPay(authentication, provide);
			} else if (provide.getProvide_code().equals("jdypay")) {
				res = payNewService.getByJDYPay(authentication, provide);
			} else if (provide.getProvide_code().equals("keyuanpay")) {
				res = payNewService.getByKeyuanPay(authentication, provide);
				res = getAlipayUrl(res, "ccbalipay");
			}else if (provide.getProvide_code().equals("hhlpay94")) {
				res = payNewService.getQRByHhl(authentication, provide, Constants.HHL_PAY_TYPE_YL);
			} else if (provide.getProvide_code().equals("dingepay")) {
				res = payNewService.getByDingEPay(authentication, provide);
			} else if (provide.getProvide_code().equals("toppay")) {
				res = payNewService.getByTopPay(authentication, provide);
			} else if (provide.getProvide_code().equals("youpay")) {
				res = payNewService.getByYouPay(authentication, provide);
			} else if (provide.getProvide_code().equals("testpay")) {
				System.out.println(gson.toJson(provide.getPassageway()));
				res = payNewService.testPayByTest(authentication, provide);
			}

			if (res != null) return res;
		}
		res = new HashMap<String, Object>();
		res.put("code", 2);
		res.put("data", "");
		res.put("msg", "错误的支付通道代码");
		return res;
	}
	
//	/**
//	 * 支付宝H5跳链支付(包装)
//	 * @return
//	 */
//	@ResponseBody
//	@RequestMapping(value = "/getQrAuthenticationByAltl", method = RequestMethod.GET)
//	@ApiOperation(value="getQrAuthenticationByAltl", notes="二维码代付通道接口")
//	@ApiImplicitParam(name = "authentication" , value = "身份验证信息")
//	@NoRepeatSubmit
//	public Map<String, Object> getQrAuthenticationByAltl(AuthenticationInfo authentication) {
//		Map<String, Object> res = getQRAuthentication(authentication);
//		if (res.get("code").toString().equals("1")) {
//			res = getAlipayUrl(res);
//		}
//		return res;
//	}
	
	/**
	 * 支付宝H5页面支付
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getPageQrAuthentication", method = RequestMethod.GET)
	@ApiOperation(value="getPageQrAuthentication", notes="二维码代付通道接口")
	@ApiImplicitParam(name = "authentication" , value = "身份验证信息")
	@NoRepeatSubmit
	public Map<String, Object> getPageQrAuthentication(AuthenticationInfo authentication) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(authentication.paramCheck()) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "参数不能为空:"+authentication.strparamCheck());
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
		ProvideInfo provide = provideService.getProvideByPassagewayCode(authentication.getPassageway_code(), authentication.getShop_phone(), authentication.getPayment());
		
		Map<String, Object> res = null;
		if (provide != null) {
			if (provide.getProvide_code().equals("fopay")) {
				res = payNewService.getByFoPay(authentication, provide);
			} else if (provide.getProvide_code().equals("longpay")) {
				res = payNewService.getByLongPay(authentication, provide);	
			} else if (provide.getProvide_code().equals("zfbzkpay")) {
				res = payNewService.getByZFBZKPay(authentication, provide);	
			} else if (provide.getProvide_code().equals("testpay")) {
				res = payNewService.testPayByTest(authentication, provide);
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
	 * 银联网银页面支付
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
			result.put("msg", "参数不能为空:"+authentication.strparamCheck());
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
				res = payNewService.getQuickPayPage(authentication, provide);
			} else if (provide.getProvide_code().equals("hhlquick")) {
				res = payNewService.getHhlQuickPayPage(authentication, provide);
			} else if (provide.getProvide_code().equals("hhlwy")) {
				res = payNewService.getHhlWYPayPage(authentication, provide);
			} else if (provide.getProvide_code().equals("hlpay")) {
				res = payNewService.getHLWYPayPage(authentication, provide);
			} else if (provide.getProvide_code().equals("webpay")) {
				res = payNewService.getByWebPay(authentication, provide);
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
	 * 自调用支付接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/bankSfPay", method = RequestMethod.GET)
	@ApiOperation(value="bankSfPay", notes=" 银行支付通道接口")
	@ApiImplicitParam(name = "authentication" , value = "身份验证信息")
	@NoRepeatSubmit
	public Map<String, Object> bankSfPay(NewQuickAuthenticationInfo authentication) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(authentication.paramCheck()) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "参数不能为空:"+authentication.strparamCheck());
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
			if (provide.getProvide_code().equals("newquickpay")) {
				res = payNewService.getNewQuickPayPage(authentication, provide);
			} else if (provide.getProvide_code().equals("hhlylpay")) {
				res = payNewService.getQRByHhlYL(authentication, provide); 
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
	 * 银联网银连接支付接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/bankPayByUrl", method = RequestMethod.GET)
	@ApiOperation(value="bankPayByUrl", notes=" 银行支付通道接口")
	@ApiImplicitParam(name = "authentication" , value = "身份验证信息")
	@NoRepeatSubmit
	public Map<String, Object> bankPayByUrl(QuickAuthenticationInfo authentication) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(authentication.paramCheck()) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "参数不能为空:"+authentication.strparamCheck());
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
			if (provide.getProvide_code().equals("kltwg")) {
				res = payNewService.getKltWGPayPage(authentication, provide);
			} else if (provide.getProvide_code().equals("amwy")) {
				res = payNewService.getAmWGPayPage(authentication, provide);
			} else if (provide.getProvide_code().equals("hhlwak")) {
				res = payNewService.getByWAKPay(authentication, provide);
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
	 * 模板地址拼接支付接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/paramUrlPay", method = RequestMethod.GET)
	@ApiOperation(value="paramUrlPay", notes=" 银行支付通道接口")
	@ApiImplicitParam(name = "paramUrlPay" , value = "身份验证信息")
	@NoRepeatSubmit
	public Map<String, Object> paramUrlPay(QuickAuthenticationInfo authentication) {
		Map<String, Object> result = new HashMap<String, Object>();
		if(authentication.paramCheck()) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "参数不能为空:"+authentication.strparamCheck());
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
			if (provide.getProvide_code().equals("hhlurl")) {
				res = payNewService.getHhlParamUrl(authentication, provide);
			} else if (provide.getProvide_code().equals("newquickpay")) {
				res = payNewService.getNewQuickPayPage(authentication, provide);
			} else if (provide.getProvide_code().equals("zfbzkpay")) {
				res = payNewService.getZFBZKPayPage(authentication, provide);
			} else if (provide.getProvide_code().equals("hhlylpay")) {
				res = payNewService.getQRByHhlYLByUrl(authentication, provide); 
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
	 * 银行代付通道接口
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/bankSubstitute", method = RequestMethod.GET)
	@ApiOperation(value="bankSubstitute", notes=" 银行支付通道接口")
	@ApiImplicitParam(name = "authentication" , value = "身份验证信息")
	@NoRepeatSubmit
	public Map<String, Object> bankSubstitute(SubAuthenticationInfo subAuthentication) {
		Map<String, Object> result = new HashMap<String, Object>();
//		if(subAuthentication.paramCheck()) {
			result.put("code", 2);
			result.put("data", "");
			result.put("msg", "参数不能为空");
			return result;
//		}
//		if (subAuthentication.getOrder_number().length() > 40) {
//			result.put("code", 2);
//			result.put("data", "");
//			result.put("msg", "订单号的长度不能大于40位");
//			return result;
//		}
//		
//		ProvideInfo provide = provideService.getProvideByPassagewayCode(authentication.getPassageway_code());
//		
//		Map<String, Object> res = null;
//		if (provide != null) {
//			if (provide.getProvide_code().equals("quickpay")) {
//				res = payNewService.getQuickPayPage(authentication, provide);
//			}else if (provide.getProvide_code().equals("hhlquick")) {
//				res = payNewService.getHhlQuickPayPage(authentication, provide);
//			}else if (provide.getProvide_code().equals("hhlwy")) {
//				res = payNewService.getHhlWYPayPage(authentication, provide);
//			}
//			if (res != null) return res;
//		}
//		res = new HashMap<String, Object>();
//		res.put("code", 2);
//		res.put("data", "");
//		res.put("msg", "错误的支付通道代码");
//		return res;
	}

	/**
	 * 支付宝跳链接口（订单编号，支付金额）
	 */
    private Map<String, Object> getAlipayUrl(Map<String, Object> res, String dictName) {
		String base_url = dictService.getObjectByKeyAndType(dictName, 2);
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
		//判断是否是开发环境
		String res = payNewService.getQRSign(authentication);
		Map<String, Object> resultMap = returnResultMap(ResultMapInfo.GETSUCCESS);
		resultMap.put("data", res);
		return resultMap;
	}

}
