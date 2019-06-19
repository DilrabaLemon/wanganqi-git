package com.boye.api;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.pay.HhlPayParamBean;
import com.boye.common.http.pay.HhlResultBean;
import com.boye.common.http.query.HhlQueryParamBean;
import com.boye.common.http.query.HhlQueryResultBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class HhlPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(HhlPayApi.class);
	
	public static final String PAYURL = "https://pay.100cpay.com/api/v1/order/add";
	
	public static final String QUERYURL = "https://pay.100cpay.com/api/v1/order/query";
	
	public static Map<String, Object> getQrCode(HhlPayParamBean payParam) {
	    Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
		String json = gson.toJson(payParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpsPost(PAYURL, json, "UTF-8"); 	//返回json串
        System.out.println(rest);
        HhlResultBean qrurl = null;
	    try {
	    	qrurl = (HhlResultBean) gson.fromJson(rest, HhlResultBean.class);
	    	if (qrurl.getMark().equals("0")) {
	    		result.put("code", 1);
	    		result.put("data", qrurl.getPay_url());
	    	}else {
	    		result.put("code", 2);
	    		result.put("data", "");
	    	}
	    	result.put("passageway_order_number", qrurl.getOut_trade_no());
	    	result.put("msg", qrurl.getTip());
	    	
	    } catch(Exception e) {
	    	result.put("code", 2);
	    	result.put("msg", "远程服务响应异常");
	    	result.put("data", "");
	    }
	    return result;
	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo, int aliPayType) { 
		if (usePayment == null) return null;
		HhlPayParamBean payParam = new HhlPayParamBean();
		payParam.setMd5_key(usePayment.getAccount_key());
		payParam.setCope_pay_amount(new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).longValue());
		payParam.setMerchant_open_id(usePayment.getAccount_number());
		payParam.setMerchant_order_number(authentication.getPlatform_order_number());
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		payParam.setPay_type(aliPayType);
		payParam.setPay_wap_mark(passagewayInfo.getPay_type());
		payParam.setSubject("361pay");
		payParam.setTimestamp((new Date()).getTime() + "");
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}
	
	public static SearchPayBo queryPayInfo(HhlQueryParamBean queryParam) {
		Gson gson = new Gson();
		String json = gson.toJson(queryParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpsPost(QUERYURL, json, "UTF-8"); 	//返回json串
	    HhlQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (HhlQueryResultBean) gson.fromJson(rest, HhlQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if (queryBean.getMark().equals("0")) {
	    		searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    		searchPayBo.setMoney(queryBean.getAccount_amount());
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryBean.getMerchant_open_id());
	    	searchPayBo.setMsg(queryBean.getTip());
	    	searchPayBo.setOrder_number(queryBean.getOut_trade_no());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
        	searchPayBo.setMsg("返回信息异常");
        	searchPayBo.setOrder_number(queryParam.getOut_trade_no());
	    }
	    return searchPayBo;
	}
	
}
