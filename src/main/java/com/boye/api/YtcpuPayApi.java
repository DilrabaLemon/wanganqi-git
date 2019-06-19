package com.boye.api;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.entity.ExtractionRecordForAgent;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.SubstituteAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.pay.YtcpuPayParamBean;
import com.boye.common.http.pay.YtcpuResultBean;
import com.boye.common.http.query.YtcpuQueryParamBean;
import com.boye.common.http.query.YtcpuQueryResultBean;
import com.boye.common.http.subpay.YtcpuSubParamBean;
import com.boye.common.http.subpay.YtcpuSubResultBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;


public class YtcpuPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(YtcpuPayApi.class);
	
	public static final String PAYURL = "http://pay.hh-hhjr.com/quick/pay";

	public static final String SUBURL = "http://pay.hh-hhjr.com/quick/pay/for";
	
	public static final String QUERYURL = "http://pay.hh-hhjr.com/quick/order/query";
	
	public static Map<String, Object> getQrCode(YtcpuPayParamBean payParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String json = gson.toJson(payParam.hasSignParamMap());
        String rest = JsonHttpClientUtils.httpPost(PAYURL, json, "UTF-8"); 	//返回json串
        YtcpuResultBean qrurl = null;
        try {
        	qrurl = (YtcpuResultBean) gson.fromJson(rest, YtcpuResultBean.class);
        	resultMap.put("code", qrurl.getCode());
        	resultMap.put("msg", qrurl.getMsg());
        	resultMap.put("data", qrurl.getUrl());
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
	private static Map<String, Object> sendSubInfo(YtcpuSubParamBean subParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String json = gson.toJson(subParam.hasSignParamMap());
        String rest = JsonHttpClientUtils.httpPost(SUBURL, json, "UTF-8"); 	//返回json串
        logger.info(rest);
//        return rest;
        YtcpuSubResultBean result = null;
        try {
        	result = (YtcpuSubResultBean) gson.fromJson(rest, YtcpuSubResultBean.class);
        	resultMap.put("code", result.getCode());
        	resultMap.put("msg", result.getMsg());
        	resultMap.put("data", result);
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
	public static SearchPayBo queryPayInfo(YtcpuQueryParamBean queryParam) {
		Gson gson = new Gson();
		String json = gson.toJson(queryParam.hasSignParamMap());
        String rest = JsonHttpClientUtils.httpPost(QUERYURL, json, "UTF-8"); 	//返回json串
        System.out.println(rest);
		YtcpuQueryResultBean queryBean = null;
		SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (YtcpuQueryResultBean) gson.fromJson(rest, YtcpuQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	System.out.println(queryBean.getCode());
	    	if (queryBean.getCode().equals(1)) {
	    		if (queryBean.getOrder_status().equals("01")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			searchPayBo.setMoney(queryBean.getAmount());
	    		} else if (queryBean.getOrder_status().equals("00")) {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    	} else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}

	    	searchPayBo.setAccount_number(queryBean.getPartner_id());
	    	searchPayBo.setMsg(queryBean.getMsg());
	    	searchPayBo.setOrder_number(queryBean.getOut_order_no());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setAccount_number(queryParam.getPartner_id());
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getOut_order_no());
	    }
	    return searchPayBo;
	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		YtcpuPayParamBean payParam = new YtcpuPayParamBean();
		payParam.setKey(usePayment.getAccount_key());
		payParam.setAmount(authentication.getPayment());
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		payParam.setOut_order_no(authentication.getPlatform_order_number());
		payParam.setPartner_id(usePayment.getAccount_number());
		payParam.setReturn_url(passagewayInfo.getReturn_url());
		payParam.setPay_type(passagewayInfo.getPay_type());
		payParam.setBody("361pay");
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		DecimalFormat df = new DecimalFormat("#0.00");
		YtcpuSubParamBean subParam = new YtcpuSubParamBean();
		subParam.setKey(usePayment.getAccount_key());
		subParam.setAcc_name(extraction.getCard_user_name());
		subParam.setAcc_no(extraction.getBank_card_number());
		subParam.setAcc_type("0");
		subParam.setBackCity(extraction.getCity_number());
		subParam.setAmount(df.format(extraction.getExtraction_money()));
		subParam.setBank_city(extraction.getRegist_bank());
		subParam.setBank_name(extraction.getBank_name());
		subParam.setBody("361pay");
		subParam.setChannel(passagewayInfo.getPay_type());
		subParam.setOut_order_no(extraction.getExtraction_number());
		subParam.setPartner_id(usePayment.getAccount_number());
		subParam.setSign(subParam.generateSign());
		return sendSubInfo(subParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		DecimalFormat df = new DecimalFormat("#0.00");
		YtcpuSubParamBean subParam = new YtcpuSubParamBean();
		subParam.setKey(usePayment.getAccount_key());
		subParam.setAcc_name(extraction.getCard_user_name());
		subParam.setAcc_no(extraction.getBank_card_number());
		subParam.setAcc_type("0");
		subParam.setBackCity(extraction.getCity_number());
		subParam.setAmount(df.format(extraction.getExtraction_money()));
		subParam.setBank_city(extraction.getRegist_bank());
		subParam.setBank_name(extraction.getBank_name());
		subParam.setBody("361pay");
		subParam.setChannel(passagewayInfo.getPay_type());
		subParam.setOut_order_no(extraction.getExtraction_number());
		subParam.setPartner_id(usePayment.getAccount_number());
		subParam.setSign(subParam.generateSign());
		return sendSubInfo(subParam);
	}
}
