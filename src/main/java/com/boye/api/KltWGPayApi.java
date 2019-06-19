package com.boye.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.common.http.pay.KltWGParamBean;
import com.boye.common.http.pay.KltWGResultBean;
import com.boye.common.http.query.KltQueryParamBean;
import com.boye.common.http.query.KltQueryResultBean;
import com.boye.common.http.query.KltSubQueryParamBean;
import com.boye.common.http.query.KltSubQueryResultBean;
import com.boye.common.http.subpay.KltSubParamBean;
import com.boye.common.http.subpay.KltSubResultBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class KltWGPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(KltWGPayApi.class);

    public static final String PAYURL = "https://openapi.openepay.com/openapi/merchantPayment/order";

    public static final String SUBURL = "https://openapi.openepay.com/openapi/singlePayment/payment";
    
    public static final String SUBQUERYURL = "https://openapi.openepay.com/openapi/singlePayment/query";

    public static final String QUERYURL = "https://openapi.openepay.com/openapi/merchantPayment/orderQuery";

    public static Map<String, Object> getQrCode(KltWGParamBean payParam) {
    	Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
		String json = gson.toJson(payParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpsPost(PAYURL, json, "UTF-8"); 	//返回json串
        KltWGResultBean reMsg = gson.fromJson(rest, KltWGResultBean.class);
        if (reMsg.getResponseCode().equals("000000")) {
        	result.put("code", "1");
        } else {
        	result.put("code", "2");
        }
        result.put("data", reMsg.getRedirectUrl());
//        result.put("passageway_order_number", reMsg.getMchtOrderId());
        result.put("msg", reMsg.getResponseMsg());
        return result;
//        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	}
	
	public static Map<String, Object> sendSubInfo(KltSubParamBean subParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String json = gson.toJson(subParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpsPost(SUBURL, json, "UTF-8"); 	//返回json串
        KltSubResultBean result = null;
        try {
        	result = (KltSubResultBean) gson.fromJson(rest, KltSubResultBean.class);
        	if (result.getResponseCode().equals("000000") && result.getOrderState().equals("IN_PROCESS")) {
        		resultMap.put("code", "1");
        	} else {
        		resultMap.put("code", "2");
        	}
        	resultMap.put("msg", result.getResponseMsg());
        	resultMap.put("data", result);
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
	public static SearchPayBo queryPayInfo(KltQueryParamBean queryParam) {
		Gson gson = new Gson();
		String json = gson.toJson(queryParam.hasSignParamMap());
		System.out.println(json);
	    String rest = JsonHttpClientUtils.httpsPost(QUERYURL, json, "UTF-8"); 	//返回json串
	    KltQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (KltQueryResultBean) gson.fromJson(rest, KltQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if (queryBean.getResponseCode().equals("000000")) {
	    		if (queryBean.getPayResult().equals("1")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(queryBean.getOrderAmount()).divide(new BigDecimal(100));
		    		searchPayBo.setMoney(df.format(money));
	    		} else if (queryBean.getPayResult().equals("2")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryBean.getMchtId());
	    	searchPayBo.setMsg(queryBean.getResponseMsg());
	    	searchPayBo.setOrder_number(queryBean.getOrderNo());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getOrderNo());
	    }
	    return searchPayBo;
	}

	public static Map<String, Object> getQrCode(QuickAuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		KltWGParamBean payParam = new KltWGParamBean();
//		payParam.setExt1("");
//		payParam.setExt2("");
		payParam.setIssuerId(authentication.getBank_code());
		payParam.setMerchantId(usePayment.getAccount_number());
		payParam.setOrderAmount(new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).longValue());
		payParam.setOrderCurrency(156);
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		payParam.setOrderDateTime(sdf.format(now));
		payParam.setOrderExpireDatetime(10);
		payParam.setOrderNo(authentication.getPlatform_order_number());
//		payParam.setPayerAcctNo("dsfsdfsdf");
//		payParam.setPayerEmail("2247762766@qq.com");
//		payParam.setPayerName(authentications.getCert_name());
//		payParam.setPayerTelPhone("13552535506");
		payParam.setPayType(1);
		payParam.setPickupUrl(passagewayInfo.getReturn_url());
//		payParam.setProductDesc(productDesc);
//		payParam.setProductId(productId);
		payParam.setProductName(authentication.getOrder_number());
//		payParam.setProductNum(productNum);
//		payParam.setProductPrice(productPrice);
		payParam.setReceiveUrl(passagewayInfo.getNotify_url());
		payParam.setSignType("1");
//		payParam.setTermId(termId);
		payParam.setVersion("18");
		payParam.setKey(usePayment.getAccount_key());
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
//		DecimalFormat df = new DecimalFormat("#0.00");
		KltSubParamBean subParam = new KltSubParamBean();
		subParam.setAccountName(extraction.getCard_user_name());
		subParam.setAccountNo(extraction.getBank_card_number());
		subParam.setAccountType("1");
		subParam.setAmt(extraction.getExtraction_money().multiply(new BigDecimal(100)).longValue());
		subParam.setBankName(extraction.getBank_name());
		subParam.setBankNo("000000000000");
		subParam.setMchtOrderNo(extraction.getExtraction_number());
		subParam.setMerchantId(usePayment.getAccount_number());
		subParam.setNotifyUrl(passagewayInfo.getNotify_url());
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		subParam.setOrderDateTime(sdf.format(now));
		subParam.setPurpose("餐饮");
//		subParam.setRemark(remark);
		subParam.setSignType("1");
//		subParam.setTransactType(transactType);
		subParam.setVersion("18");
		subParam.setKey(usePayment.getAccount_key());
		
//		subParam.setAccountName("王佳婧");
//		subParam.setAccountNo("6217680802791173");
//		subParam.setAccountType("1");
//		subParam.setAmt(1000L);
//		subParam.setBankName("中信银行");
//		subParam.setBankNo("000000000000");
//		subParam.setMchtOrderNo("sdf0505222200523452231");
//		subParam.setMerchantId("903110153110001");
//		subParam.setNotifyUrl("http://test361pay.361fit.com/pay/getKltSubResult");
//		Date now = new Date();
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
//		subParam.setOrderDateTime(sdf.format(now));
//		subParam.setPurpose("餐饮");
////		subParam.setRemark(remark);
//		subParam.setSignType("1");
////		subParam.setTransactType(transactType);
//		subParam.setVersion("18");
//		subParam.setKey("742fa3ffd050fb441763bf8fb6c0594f");
		
		subParam.setSign(subParam.generateSign());
		return sendSubInfo(subParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		KltSubParamBean subParam = new KltSubParamBean();
		subParam.setAccountName(extraction.getCard_user_name());
		subParam.setAccountNo(extraction.getBank_card_number());
		subParam.setAccountType("1");
		subParam.setAmt(1000L);
		subParam.setBankName(extraction.getBank_name());
		subParam.setBankNo("000000000000");
		subParam.setMchtOrderNo(extraction.getExtraction_number());
		subParam.setMerchantId(usePayment.getAccount_number());
		subParam.setNotifyUrl(passagewayInfo.getNotify_url());
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		subParam.setOrderDateTime(sdf.format(now));
		subParam.setPurpose("餐饮");
//		subParam.setRemark(remark);
		subParam.setSignType("1");
//		subParam.setTransactType(transactType);
		subParam.setVersion("18");
		subParam.setKey(usePayment.getAccount_key());
		subParam.setSign(subParam.generateSign());
		return sendSubInfo(subParam);
	}

	public static SearchPayBo subInfoQuery(KltSubQueryParamBean queryParam) {
		Gson gson = new Gson();
		String json = gson.toJson(queryParam.hasSignParamMap());
		System.out.println(json);
	    String rest = JsonHttpClientUtils.httpsPost(SUBQUERYURL, json, "UTF-8"); 	//返回json串
	    KltSubQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (KltSubQueryResultBean) gson.fromJson(rest, KltSubQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if (queryBean.getResponseCode().equals("000000")) {
	    		if (queryBean.getOrderState().equals("SUCCESS")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(queryBean.getAmount()).divide(new BigDecimal(100));
		    		searchPayBo.setMoney(df.format(money));
	    		} else if (queryBean.getOrderState().equals("FAIL")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryBean.getMchtId());
	    	searchPayBo.setMsg(queryBean.getResponseMsg());
	    	searchPayBo.setOrder_number(queryBean.getMchtOrderNo());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getMchtOrderNo());
	    }
	    return searchPayBo;
	}

	public static SearchPayBo subInfoQuery(String extraction_number, SubstituteAccount substitute) {
		KltSubQueryParamBean queryParam = new KltSubQueryParamBean();
		queryParam.setKey(substitute.getAccount_key());
		queryParam.setMchtOrderNo(extraction_number);
		queryParam.setMerchantId(substitute.getAccount_number());
		queryParam.setPaymentBusinessType("SINGLE_PAY");
		queryParam.setSignType("1");
		queryParam.setVersion("18");
		queryParam.setSign(queryParam.generateSign());
		return subInfoQuery(queryParam);
	}
}
