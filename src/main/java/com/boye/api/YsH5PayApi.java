package com.boye.api;

import java.math.BigDecimal;
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
import com.boye.common.http.pay.YsH5ParamBean;
import com.boye.common.http.pay.YsH5ResultBean;
import com.boye.common.http.query.YsH5QueryBalanceParamBean;
import com.boye.common.http.query.YsH5QueryBalanceResultBean;
import com.boye.common.http.query.YsH5QueryParamBean;
import com.boye.common.http.query.YsH5QueryResultBean;
import com.boye.common.http.query.YsH5SubQueryParamBean;
import com.boye.common.http.query.YsH5SubQueryResultBean;
import com.boye.common.http.subpay.YsH5SubParamBean;
import com.boye.common.http.subpay.YsH5SubResultBean;
import com.boye.common.utils.CommonUtils;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class YsH5PayApi {

	private static Logger logger = LoggerFactory.getLogger(YsH5PayApi.class);

    public static final String PAYURL = "https://8oep1k.apolo-pay.com/unifiedorder";

    public static final String SUBURL = "https://8oep1k.apolo-pay.com/agentpay/pay";
    
    public static final String SUBQUERYURL = "https://8oep1k.apolo-pay.com/agentpay/query";

    public static final String QUERYURL = "https://8oep1k.apolo-pay.com/orderquery";
    
    public static final String BALANCEURL = "https://8oep1k.apolo-pay.com/amountquery";

    public static Map<String, Object> getQrCode(YsH5ParamBean payParam) {
    	Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpsPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        YsH5ResultBean reMsg = gson.fromJson(rest, YsH5ResultBean.class);
        if (reMsg.getRet_code().equals("0") && reMsg.getBiz_content() != null ) {
        	result.put("code", 1);
        	result.put("data", reMsg.getBiz_content().getMweb_url());
        } else {
        	result.put("code", 2);
        	result.put("data", "");
        }
        
//        result.put("passageway_order_number", reMsg.getMchtOrderId());
        result.put("msg", reMsg.getRet_msg());
        return result;
//        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	}
	
	public static Map<String, Object> sendSubInfo(YsH5SubParamBean subParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpsPostform(SUBURL, subParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        YsH5SubResultBean result = null;
        try {
        	result = (YsH5SubResultBean) gson.fromJson(rest, YsH5SubResultBean.class);
        	if (result.getRet_code().equals("0") && result.getBiz_content() != null && result.getBiz_content().getOrder_status().equals("1")) {
        		resultMap.put("code", "1");
        	} else {
        		resultMap.put("code", "2");
        	}
        	resultMap.put("msg", result.getRet_msg());
        	resultMap.put("data", result);
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
	public static Map<String, Object> queryBalance(YsH5QueryBalanceParamBean queryParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpsPostform(BALANCEURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        YsH5QueryBalanceResultBean result = null;
        try {
        	result = (YsH5QueryBalanceResultBean) gson.fromJson(rest, YsH5QueryBalanceResultBean.class);
        	if (result.getRet_code().equals("0") && result.getBiz_content() != null) {
        		resultMap.put("code", "1");
        		resultMap.put("data", result.getBiz_content().getAmount());
        	} else {
        		resultMap.put("code", "2");
        	}
        	resultMap.put("msg", result.getRet_msg());
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
	public static SearchPayBo queryPayInfo(YsH5QueryParamBean queryParam) {
		Gson gson = new Gson();
	    String rest = JsonHttpClientUtils.httpsPostform(QUERYURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	    YsH5QueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (YsH5QueryResultBean) gson.fromJson(rest, YsH5QueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if (queryBean.getRet_code().equals("0") && queryBeanHasHizContent(queryBean)) {
	    		YsH5QueryResultBean.Content result = queryBean.getBiz_content().getLists().get(0);
	    		if (result.getOrder_status().equals("2")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(result.getPayment_fee()).divide(new BigDecimal(100));
		    		searchPayBo.setMoney(df.format(money));
	    		} else if (result.getOrder_status().equals("3")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryBean.getBiz_content().getLists().get(0).getMch_id());
	    	searchPayBo.setMsg(queryBean.getRet_msg());
	    	searchPayBo.setOrder_number(queryBean.getBiz_content().getLists().get(0).getOut_order_no());
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getOut_order_no());
	    }
	    return searchPayBo;
	}

	private static boolean queryBeanHasHizContent(YsH5QueryResultBean queryBean) {
		if (queryBean.getBiz_content() == null || queryBean.getBiz_content().getLists() == null || queryBean.getBiz_content().getLists().size() == 0) return false; 
		return true;
	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		if (new BigDecimal(authentication.getPayment()).compareTo(new BigDecimal(5000)) == 1) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 2);
			result.put("msg", "金额不得大于5000");
			result.put("data", "");
			return result;
		}
		YsH5ParamBean payParam = new YsH5ParamBean();
		payParam.setAppid("");
		payParam.setAuth_code("");
		payParam.setBill_create_ip(CommonUtils.getServerAddress());
		payParam.setBiz_content("");
		payParam.setBody(authentication.getOrder_number());
		payParam.setCashier_id("");
		payParam.setCur_type("CNY");
		payParam.setMch_id(usePayment.getAccount_number());
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		payParam.setOpenid("");
		payParam.setOut_order_no(authentication.getPlatform_order_number());
		payParam.setPay_limit("");
		payParam.setPay_platform("ALIPAY");
		payParam.setPay_type(passagewayInfo.getPay_type());
		payParam.setPayment_fee(new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).intValue());
		payParam.setRemark("");
		payParam.setScene_info("");
		payParam.setSign_type("MD5");
		payParam.setVersion("1.0");
		
//		payParam.setAppid("");
//		payParam.setAuth_code("");
//		payParam.setBill_create_ip("192.168.1.1");
//		payParam.setBiz_content("");
//		payParam.setBody("361pay");
//		payParam.setCashier_id("");
//		payParam.setCur_type("CNY");
//		payParam.setMch_id("10005306");
//		payParam.setNotify_url("http://361paytest.361fit.com/api/pay/h5payreturn");
//		payParam.setOpenid("");
//		payParam.setOut_order_no("1000012000123000012540005");
//		payParam.setPay_limit("");
//		payParam.setPay_platform("ALIPAY");
//		payParam.setPay_type("MWEB");
//		payParam.setPayment_fee(1000);
//		payParam.setRemark("");
//		payParam.setScene_info("");
//		payParam.setSign_type("MD5");
//		payParam.setVersion("1.0");
		
		payParam.setKey(usePayment.getAccount_key());
		payParam.setSignature(payParam.generateSign());
		return getQrCode(payParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
//		DecimalFormat df = new DecimalFormat("#0.00");
		YsH5SubParamBean subParam = new YsH5SubParamBean();
		subParam.setVersion("1.0");
		subParam.setBank_code("");
		subParam.setPayee_branch_name("");
		subParam.setPayee_branch_no("");
		subParam.setCard_type("1");
		subParam.setPayee_acct_type("1");
		subParam.setCvv2("");
		subParam.setIdcard_no("");
		subParam.setMch_id(usePayment.getAccount_number());
		subParam.setMobile("");
		subParam.setNotify_url(passagewayInfo.getNotify_url());
		subParam.setOut_order_no(extraction.getExtraction_number());
		subParam.setPayee_acct_name(extraction.getCard_user_name());
		subParam.setPayee_acct_no(extraction.getBank_card_number());
		subParam.setPayment_fee(extraction.getExtraction_money().multiply(new BigDecimal(100)).intValue());
		subParam.setRemark("361pay");
		subParam.setSettle_type("1");
		subParam.setSign_type("MD5");
		
		subParam.setKey(usePayment.getAccount_key());
		subParam.setSignature(subParam.generateSign());
		return sendSubInfo(subParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		YsH5SubParamBean subParam = new YsH5SubParamBean();
		subParam.setVersion("1.0");
		subParam.setBank_code("");
		subParam.setPayee_branch_name("");
		subParam.setPayee_branch_no("");
		subParam.setCard_type("1");
		subParam.setPayee_acct_type("1");
		subParam.setCvv2("");
		subParam.setIdcard_no("");
		subParam.setMch_id(usePayment.getAccount_number());
		subParam.setMobile("");
		subParam.setNotify_url(passagewayInfo.getNotify_url());
		subParam.setOut_order_no(extraction.getExtraction_number());
		subParam.setPayee_acct_name(extraction.getCard_user_name());
		subParam.setPayee_acct_no(extraction.getBank_card_number());
		subParam.setPayment_fee(extraction.getExtraction_money().multiply(new BigDecimal(100)).intValue());
		subParam.setRemark("361pay");
		subParam.setSettle_type("1");
		subParam.setSign_type("MD5");
		
		subParam.setKey(usePayment.getAccount_key());
		subParam.setSignature(subParam.generateSign());
		return sendSubInfo(subParam);
	}

	public static SearchPayBo subInfoQuery(YsH5SubQueryParamBean queryParam) {
		Gson gson = new Gson();
	    String rest = JsonHttpClientUtils.httpsPostform(SUBQUERYURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	    YsH5SubQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (YsH5SubQueryResultBean) gson.fromJson(rest, YsH5SubQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if (queryBean.getRet_code().equals("0")) {
	    		if (queryBean.getBiz_content().getLists().get(0).getOrder_status().equals("2")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(queryBean.getBiz_content().getLists().get(0).getPayment_fee()).divide(new BigDecimal(100));
		    		searchPayBo.setMoney(df.format(money));
	    		} else if (queryBean.getBiz_content().getLists().get(0).getOrder_status().equals("3")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryBean.getBiz_content().getLists().get(0).getMch_id());
	    	searchPayBo.setMsg(queryBean.getRet_msg());
	    	searchPayBo.setOrder_number(queryBean.getBiz_content().getLists().get(0).getOrder_no());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getOut_order_no());
	    }
	    return searchPayBo;
	}

	public static SearchPayBo subInfoQuery(String extraction_number, SubstituteAccount substitute) {
		YsH5SubQueryParamBean queryParam = new YsH5SubQueryParamBean();
		queryParam.setKey(substitute.getAccount_key());
		queryParam.setMch_id(substitute.getAccount_number());
		queryParam.setOrder_no("");
		queryParam.setOut_order_no(extraction_number);
		queryParam.setSign_type("MD5");
		queryParam.setKey(substitute.getAccount_key());
		queryParam.setSignature(queryParam.generateSign());
		return subInfoQuery(queryParam);
	}

}
