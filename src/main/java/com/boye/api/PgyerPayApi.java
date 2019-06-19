package com.boye.api;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.pay.PgyerParamBean;
import com.boye.common.http.pay.PgyerResultBean;
import com.boye.common.http.query.PgyerQueryParamBean;
import com.boye.common.http.query.PgyerQueryResultBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class PgyerPayApi {

	private static Logger logger = LoggerFactory.getLogger(PgyerPayApi.class);

    public static final String PAYURL = "http://118.24.63.21:8585/pay/doAction";
    
    public static final String QUERYURL = "http://118.24.63.21:8585/pay/doAction";
    
    public static Map<String, Object> getQrCode(PgyerParamBean payParam) {
    	Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpsPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        PgyerResultBean reMsg = gson.fromJson(rest, PgyerResultBean.class);
        if (reMsg.getCode().equals(0)) {
        	result.put("code", 1);
        	result.put("data", reMsg.getData().getUrl());
        } else {
        	result.put("code", 2);
        	result.put("data", "");
        }
        
//        result.put("passageway_order_number", reMsg.getMchtOrderId());
        result.put("msg", reMsg.getMsg());
        return result;
	}
	
//	public static Map<String, Object> sendSubInfo(YsH5SubParamBean subParam) {
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		Gson gson = new Gson();
//        String rest = JsonHttpClientUtils.httpsPostform(SUBURL, subParam.hasSignParamMap(), "UTF-8"); 	//返回json串
//        YsH5SubResultBean result = null;
//        try {
//        	result = (YsH5SubResultBean) gson.fromJson(rest, YsH5SubResultBean.class);
//        	if (result.getRet_code().equals("0") && result.getBiz_content() != null && result.getBiz_content().getOrder_status().equals("1")) {
//        		resultMap.put("code", "1");
//        	} else {
//        		resultMap.put("code", "2");
//        	}
//        	resultMap.put("msg", result.getRet_msg());
//        	resultMap.put("data", result);
//        } catch(Exception e) {
//        	resultMap.put("code","2");
//        	resultMap.put("msg", "远程服务响应异常");
//        	resultMap.put("data", "");
//        }
//	    return resultMap;
//	}
//	
//	public static Map<String, Object> queryBalance(YsH5QueryBalanceParamBean queryParam) {
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		Gson gson = new Gson();
//        String rest = JsonHttpClientUtils.httpsPostform(BALANCEURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
//        YsH5QueryBalanceResultBean result = null;
//        try {
//        	result = (YsH5QueryBalanceResultBean) gson.fromJson(rest, YsH5QueryBalanceResultBean.class);
//        	if (result.getRet_code().equals("0") && result.getBiz_content() != null) {
//        		resultMap.put("code", "1");
//        		resultMap.put("data", result.getBiz_content().getAmount());
//        	} else {
//        		resultMap.put("code", "2");
//        	}
//        	resultMap.put("msg", result.getRet_msg());
//        } catch(Exception e) {
//        	resultMap.put("code","2");
//        	resultMap.put("msg", "远程服务响应异常");
//        	resultMap.put("data", "");
//        }
//	    return resultMap;
//	}
	
	public static SearchPayBo queryPayInfo(PgyerQueryParamBean queryParam) {
		Gson gson = new Gson();
	    String rest = JsonHttpClientUtils.httpsPostform(QUERYURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	    PgyerQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (PgyerQueryResultBean) gson.fromJson(rest, PgyerQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if (queryBean.getCode().equals(0)) {
	    		if (queryBean.getData().getStatus().equals(1)) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
		    		searchPayBo.setMoney(queryBean.getData().getAmount());
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryBean.getData().getMerchants());
	    	searchPayBo.setMsg(queryBean.getMsg());
	    	searchPayBo.setOrder_number(queryBean.getData().getOrder_no());
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getOrder_no());
	    }
	    return searchPayBo;
	}

//	private static boolean queryBeanHasHizContent(YsH5QueryResultBean queryBean) {
//		if (queryBean.getBiz_content() == null || queryBean.getBiz_content().getLists() == null || queryBean.getBiz_content().getLists().size() == 0) return false; 
//		return true;
//	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		PgyerParamBean payParam = new PgyerParamBean();
//		DecimalFormat df = new DecimalFormat("#0.00");
		payParam.setAmount(new BigDecimal(authentication.getPayment()).multiply(BigDecimal.valueOf(100)).intValue() + "");
		payParam.setMerchant_no(usePayment.getCounter_number());
		payParam.setMerchants(usePayment.getAccount_number());
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		payParam.setOrder_no(authentication.getPlatform_order_number());
		payParam.setPay_code("GATEWAY_PAY_PC");
		payParam.setProduct_name(authentication.getOrder_number());
		payParam.setService("unifiedorder");
		payParam.setType("2");
		payParam.setKey(usePayment.getAccount_key());
		
//		payParam.setAmount("5000");
//		payParam.setMerchant_no("10008");
//		payParam.setMerchants("10008");
//		payParam.setNotify_url("http://www.baidu.com/notify/doAction");
//		payParam.setOrder_no("201812281800060684785200017");
//		payParam.setPay_code("GATEWAY_PAY_PC");
//		payParam.setProduct_name("test");
//		payParam.setService("unifiedorder");
//		payParam.setType("2");
//		payParam.setKey("MIGfMA0GCSqGSIb3D");
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}
	
//	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
//		if (usePayment == null) return null;
////		DecimalFormat df = new DecimalFormat("#0.00");
//		YsH5SubParamBean subParam = new YsH5SubParamBean();
//		subParam.setVersion("1.0");
//		subParam.setBank_code("");
//		subParam.setPayee_branch_name("");
//		subParam.setPayee_branch_no("");
//		subParam.setCard_type("1");
//		subParam.setPayee_acct_type("1");
//		subParam.setCvv2("");
//		subParam.setIdcard_no("");
//		subParam.setMch_id(usePayment.getAccount_number());
//		subParam.setMobile("");
//		subParam.setNotify_url(passagewayInfo.getNotify_url());
//		subParam.setOut_order_no(extraction.getExtraction_number());
//		subParam.setPayee_acct_name(extraction.getCard_user_name());
//		subParam.setPayee_acct_no(extraction.getBank_card_number());
//		subParam.setPayment_fee(extraction.getExtraction_money().multiply(new BigDecimal(100)).intValue());
//		subParam.setRemark("361pay");
//		subParam.setSettle_type("1");
//		subParam.setSign_type("MD5");
//		
//		subParam.setKey(usePayment.getAccount_key());
//		subParam.setSignature(subParam.generateSign());
//		return sendSubInfo(subParam);
//	}
//	
//	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
//		if (usePayment == null) return null;
//		YsH5SubParamBean subParam = new YsH5SubParamBean();
//		subParam.setVersion("1.0");
//		subParam.setBank_code("");
//		subParam.setPayee_branch_name("");
//		subParam.setPayee_branch_no("");
//		subParam.setCard_type("1");
//		subParam.setPayee_acct_type("1");
//		subParam.setCvv2("");
//		subParam.setIdcard_no("");
//		subParam.setMch_id(usePayment.getAccount_number());
//		subParam.setMobile("");
//		subParam.setNotify_url(passagewayInfo.getNotify_url());
//		subParam.setOut_order_no(extraction.getExtraction_number());
//		subParam.setPayee_acct_name(extraction.getCard_user_name());
//		subParam.setPayee_acct_no(extraction.getBank_card_number());
//		subParam.setPayment_fee(extraction.getExtraction_money().multiply(new BigDecimal(100)).intValue());
//		subParam.setRemark("361pay");
//		subParam.setSettle_type("1");
//		subParam.setSign_type("MD5");
//		
//		subParam.setKey(usePayment.getAccount_key());
//		subParam.setSignature(subParam.generateSign());
//		return sendSubInfo(subParam);
//	}
//
//	public static SearchPayBo subInfoQuery(YsH5SubQueryParamBean queryParam) {
//		Gson gson = new Gson();
//		String json = gson.toJson(queryParam.hasSignParamMap());
//		System.out.println(json);
//	    String rest = JsonHttpClientUtils.httpsPost(SUBQUERYURL, json, "UTF-8"); 	//返回json串
//	    YsH5QueryResultBean queryBean = null;
//	    SearchPayBo searchPayBo = new SearchPayBo();
////	    try {
////	    	queryBean = (YsH5QueryResultBean) gson.fromJson(rest, YsH5QueryResultBean.class);
////	    	logger.info("获取成功：" + rest);
////	    	if (queryBean.getRet_code().equals("0")) {
////	    		if (queryBean.getBiz_content().get(0).getOrder_status().equals("2")) {
////	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
////	    			DecimalFormat df = new DecimalFormat("#0.00");
////	    			BigDecimal money = new BigDecimal(queryBean.getBiz_content().get(0).getPayment_fee()).divide(new BigDecimal(100));
////		    		searchPayBo.setMoney(df.format(money));
////	    		} else if (queryBean.getBiz_content().get(0).getOrder_status().equals("3")) {
////	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
////	    		} else {
////	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
////	    		}
////	    		
////	    	}else {
////	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
////	    	}
////	    	searchPayBo.setAccount_number(queryBean.getBiz_content().get(0).getMch_id());
////	    	searchPayBo.setMsg(queryBean.getRet_msg());
////	    	searchPayBo.setOrder_number(queryBean.getBiz_content().get(0).getOut_order_no());
////	    } catch(Exception e) {
////	    	logger.info("返回格式异常：" + rest);
////	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
////	    	searchPayBo.setMsg("返回信息异常");
////	    	searchPayBo.setOrder_number(queryParam.getOut_order_no());
////	    }
//	    return searchPayBo;
//	}
//
//	public static SearchPayBo subInfoQuery(String extraction_number, SubstituteAccount substitute) {
//		YsH5SubQueryParamBean queryParam = new YsH5SubQueryParamBean();
//		queryParam.setKey(substitute.getAccount_key());
//		queryParam.setMch_id("10005306");
//		queryParam.setOrder_no("");
//		queryParam.setOut_order_no("1000530601320181211161233");
//		queryParam.setSign_type("MD5");
//		queryParam.setKey("fdc2f1ce40f543a19e40dd24a3b331f5");
//		queryParam.setSignature(queryParam.generateSign());
//		return subInfoQuery(queryParam);
//	}

}
