package com.boye.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.pay.ZHFParamBean;
import com.boye.common.utils.HttpClientUtil;
import com.boye.common.utils.MD5;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class ZHFPayApi {

	private static Logger logger = LoggerFactory.getLogger(ZHFPayApi.class);

    //public static final String PAYURL = "http://47.111.20.108:8080/internalApi/sendOrderInfoToMQ";
    
    public static final String RESULTKEY = "361payABC";

    public static Map<String, Object> getQrCode(ZHFParamBean payParam, String payUrl, String returnUrl) {
    	Gson gson = new Gson();
    	Map<String, Object> result = new HashMap<String, Object>();
        String rest = HttpClientUtil.httpGet(payUrl + "?" + payParam.notSignParam(), "UTF-8"); 	//返回json串
        String sign = MD5.md5Str(payParam.getPlatOrderNo() + RESULTKEY);
        Map<String, Object> resMap;
		try {
			resMap = gson.fromJson(rest, new TypeToken<Map<String, Object>>() {}.getType());
	        if (resMap.get("code").equals(1.0)) {
        		result.put("code", 1);
            	result.put("data", returnUrl + "?orderNumber=" + payParam.getPlatOrderNo() + "&sign=" +sign);
            	result.put("msg", "获取成功");
	        } else {
	        	result.put("code", 2);
	        	result.put("data", "");
	        	result.put("msg", "获取失败");
	        }
		} catch (Exception e) {
			result.put("code", 2);
        	result.put("data", "");
        	result.put("msg", "远程服务响应异常");
        	
		}
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
//	
//	public static SearchPayBo queryPayInfo(YsH5QueryParamBean queryParam) {
//		Gson gson = new Gson();
//	    String rest = JsonHttpClientUtils.httpsPostform(QUERYURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
//	    YsH5QueryResultBean queryBean = null;
//	    SearchPayBo searchPayBo = new SearchPayBo();
//	    try {
//	    	queryBean = (YsH5QueryResultBean) gson.fromJson(rest, YsH5QueryResultBean.class);
//	    	logger.info("获取成功：" + rest);
//	    	if (queryBean.getRet_code().equals("0") && queryBeanHasHizContent(queryBean)) {
//	    		YsH5QueryResultBean.Content result = queryBean.getBiz_content().getLists().get(0);
//	    		if (result.getOrder_status().equals("2")) {
//	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
//	    			DecimalFormat df = new DecimalFormat("#0.00");
//	    			BigDecimal money = new BigDecimal(result.getPayment_fee()).divide(new BigDecimal(100));
//		    		searchPayBo.setMoney(df.format(money));
//	    		} else if (result.getOrder_status().equals("3")) {
//	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
//	    		} else {
//	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
//	    		}
//	    		
//	    	}else {
//	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
//	    	}
//	    	searchPayBo.setAccount_number(queryBean.getBiz_content().getLists().get(0).getMch_id());
//	    	searchPayBo.setMsg(queryBean.getRet_msg());
//	    	searchPayBo.setOrder_number(queryBean.getBiz_content().getLists().get(0).getOut_order_no());
//	    } catch(Exception e) {
//	    	e.printStackTrace();
//	    	logger.info("返回格式异常：" + rest);
//	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
//	    	searchPayBo.setMsg("返回信息异常");
//	    	searchPayBo.setOrder_number(queryParam.getOut_order_no());
//	    }
//	    return searchPayBo;
//	}
//
//	private static boolean queryBeanHasHizContent(YsH5QueryResultBean queryBean) {
//		if (queryBean.getBiz_content() == null || queryBean.getBiz_content().getLists() == null || queryBean.getBiz_content().getLists().size() == 0) return false; 
//		return true;
//	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo, String payUrl, String returnUrl) { 
		if (usePayment == null) return null;
//		if (new BigDecimal(authentication.getPayment()).compareTo(new BigDecimal(5000)) == 1) {
//			Map<String, Object> result = new HashMap<String, Object>();
//			result.put("code", 2);
//			result.put("msg", "金额不得大于5000");
//			result.put("data", "");
//			return result;
//		}
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("code", 2);
//		result.put("msg", "触发风控规则");
//		result.put("data", "");
//		int money = new BigDecimal(authentication.getPayment()).multiply(BigDecimal.valueOf(100)).intValue();
//		if (money % 100 != 0) return result;
//		if (money % 1000 == 0) return result;
		ZHFParamBean payParam = new ZHFParamBean();
		DecimalFormat df = new DecimalFormat("#0.00");
		payParam.setAmount(df.format(new BigDecimal(authentication.getPayment())));
		payParam.setAppId(usePayment.getCounter_number());
		payParam.setPlatOrderNo(authentication.getPlatform_order_number());
		payParam.setType(Integer.parseInt(passagewayInfo.getPay_type()));
		payParam.setUrl("");
		
		return getQrCode(payParam, payUrl, returnUrl);
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
