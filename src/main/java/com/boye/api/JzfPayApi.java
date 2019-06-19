package com.boye.api;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.client.RestTemplate;

import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.pay.JzfPayParamBean;
import com.boye.common.utils.jzf.Conts;



public class JzfPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(JzfPayApi.class);
	
	public static final String SMPAYURL = "http://api.qwebank.top/open/v1/order/alipayScan";
	public static final String WAPPAYURL = "http://api.qwebank.top/open/v1/order/alipayWapPay";
	public static final String DFPAYURL = "http://api.qwebank.top/open/v1/pay/createChannel";
	public static final String QUERYURL = "http://api.xjockj.com/open/v1/order/getByCustomerNo";
	
	private static final RestTemplate restTemplate = new RestTemplate();
	


	private static Map<String, Object> getQrCode(JzfPayParamBean payParam) {
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("accessToken", payParam.getAccessToken());
		// 构造请求参数
		Map<String, Object> paramMap = payParam.returnParamMap();
		map.put("param", paramMap);
		Map<String, Object> result =new HashMap<>();
		try {
			Map<String, Object> postForObject = restTemplate.postForObject(SMPAYURL, map, Map.class);
			Set<String> keySet = postForObject.keySet();
			for (String string : keySet) {
				System.out.println("key"+"="+string+",value="+postForObject.get(string));
			}
			if (postForObject.get("success").equals(true)) {
				result.put("code", 1);
			}else {
				result.put("code", 2);
			}
	    	result.put("msg", postForObject.get("message"));
	    	result.put("data", postForObject.get("value"));
		} catch (Exception e) {
			result.put("code", 2);
	    	result.put("msg", "远程服务响应异常");
	    	result.put("data", "");
		}
			
		return result;
	}
	
	
	
	private static Map<String, Object> getQrCodeByH5(JzfPayParamBean payParam) {
		
		Map<String, Object> map = new HashMap<>();
		
		map.put("accessToken", payParam.getAccessToken());
		// 构造请求参数
		Map<String, Object> paramMap = payParam.returnParamMap();
		map.put("param", paramMap);
		Map<String, Object> result =new HashMap<>();
		try {
			Map<String, Object> postForObject = restTemplate.postForObject(WAPPAYURL, map, Map.class);
			Set<String> keySet = postForObject.keySet();
			for (String string : keySet) {
				System.out.println("key"+"="+string+",value="+postForObject.get(string));
			}
			if (postForObject.get("success").equals(true)) {
				result.put("code", 1);
			}else {
				result.put("code", 2);
			}
	    	result.put("msg", postForObject.get("message"));
	    	result.put("data", postForObject.get("value"));
		} catch (Exception e) {
			result.put("code", 2);
	    	result.put("msg", "远程服务响应异常");
	    	result.put("data", "");
		}
			
		return result;
	}
	
	
	// 支付宝扫码下单
	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo){
		if (usePayment == null) return null;
		// 该接口金额已分为单位
		BigDecimal payment = new BigDecimal(authentication.getPayment());
		BigDecimal bigDecimal = new BigDecimal(100);
		BigDecimal moneyBefore = bigDecimal.multiply(payment);
		// 向上取整
		BigDecimal money = moneyBefore.setScale(0, BigDecimal.ROUND_UP);
		
		JzfPayParamBean payParam = new JzfPayParamBean();
		payParam.setAccessToken(authentication.getAccessToken());
		payParam.setOutTradeNo(authentication.getPlatform_order_number());
		payParam.setMoney(money.toString());
		payParam.setType(passagewayInfo.getPay_type());
		payParam.setBody(authentication.getOrder_number());
		payParam.setDetail(authentication.getOrder_number());
		payParam.setProductId(authentication.getOrder_number());
		payParam.setNotifyUrl(passagewayInfo.getNotify_url());
		payParam.setSuccessUrl(passagewayInfo.getReturn_url());
		return getQrCode(payParam);
	}

	// H5下单
	public static Map<String, Object> getQrCodeByH5(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo){
		if (usePayment == null) return null;
		// 该接口金额已分为单位
		BigDecimal payment = new BigDecimal(authentication.getPayment());
		BigDecimal bigDecimal = new BigDecimal(100);
		BigDecimal moneyBefore = bigDecimal.multiply(payment);
		// 向上取整
		BigDecimal money = moneyBefore.setScale(0, BigDecimal.ROUND_UP);
		System.out.println("money="+money);		
		JzfPayParamBean payParam = new JzfPayParamBean();
		payParam.setAccessToken(authentication.getAccessToken());
		payParam.setOutTradeNo(authentication.getPlatform_order_number());
		payParam.setMoney(money.toString());
		payParam.setType(passagewayInfo.getPay_type());
		payParam.setBody(authentication.getOrder_number());
		payParam.setDetail(authentication.getOrder_number());
		payParam.setProductId(authentication.getOrder_number());
		payParam.setNotifyUrl(passagewayInfo.getNotify_url());
		payParam.setSuccessUrl(passagewayInfo.getReturn_url());
		return getQrCodeByH5(payParam);
	}
	
	
	// 代付下单
	public static Map<String, Object> getQrCodeByDF(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo){
		if (usePayment == null) return null;
		// 该接口金额已分为单位
		BigDecimal payment = new BigDecimal(authentication.getPayment());
		BigDecimal bigDecimal = new BigDecimal(100);
		BigDecimal moneyBefore = bigDecimal.multiply(payment);
		// 向上取整
		BigDecimal money = moneyBefore.setScale(0, BigDecimal.ROUND_UP);
		JzfPayParamBean payParam = new JzfPayParamBean();
		payParam.setAccessToken(authentication.getAccessToken());
		payParam.setOutTradeNo(authentication.getPlatform_order_number());
		return null;
	}
	
}

