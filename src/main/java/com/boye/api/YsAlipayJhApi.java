package com.boye.api;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.pay.YsAlipayParamBean;
import com.google.gson.Gson;

public class YsAlipayJhApi {

	private static Logger logger = LoggerFactory.getLogger(YsAlipayJhApi.class);

    public static final String PAYURL = "https://openapi.alipay.com/gateway.do";

//    public static final String SUBURL = "https://8oep1k.apolo-pay.com/agentpay/pay";
//    
//    public static final String SUBQUERYURL = "https://8oep1k.apolo-pay.com/agentpay/query";
//
//    public static final String QUERYURL = "https://8oep1k.apolo-pay.com/orderquery";
//    
//    public static final String BALANCEURL = "https://8oep1k.apolo-pay.com/amountquery";
    
    public static Map<String, Object> getQrCode(YsAlipayParamBean payParam) {
    	Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
		AlipayClient alipayClient = new DefaultAlipayClient(PAYURL, payParam.getCssId(), payParam.getPrivateKey(), "json", "utf-8",payParam.getPublicKey(), "RSA2");
		AlipayTradeWapPayRequest  alipayRequest = new AlipayTradeWapPayRequest();//创建API对应的request
        alipayRequest.setNotifyUrl(payParam.getNotifyUrl());//在公共参数中设置回跳和通知地址
        System.out.println(gson.toJson(payParam.getBizContent()));
        alipayRequest.setBizContent(gson.toJson(payParam.getBizContent()));//填充业务参数
        try {
        	String form = alipayClient.pageExecute(alipayRequest).getBody(); //调用SDK生成表单
            String url = form.substring(form.indexOf("action=") + 8, form.indexOf("\">"));
            url += "&biz_content=";
            url += URLEncoder.encode(form.substring(form.indexOf("value=\"") + 7, form.indexOf("}\">") + 1).replaceAll("&quot;", "\""), "utf-8");
            url = url.replaceAll("\\s+", "");
            result.put("code", 1);
            result.put("data", url);
            result.put("msg", "获取成功");// 打开支付连接选择 0 webview  1 浏览器
            return result;
        } catch (Exception e) {
        	e.printStackTrace();
        	result.put("code", 2);
            result.put("data", "");
            result.put("msg", "获取失败，回调异常");// 打开支付连接选择 0 webview  1 浏览器
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

//	private static boolean queryBeanHasHizContent(YsH5QueryResultBean queryBean) {
//		if (queryBean.getBiz_content() == null || queryBean.getBiz_content().getLists() == null || queryBean.getBiz_content().getLists().size() == 0) return false; 
//		return true;
//	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null || usePayment.getPaymentKeyBox() == null) return null;
		YsAlipayParamBean payParam = new YsAlipayParamBean();
		
		payParam.setBankName(authentication.getBank_name());
		payParam.setBizContent(new YsAlipayParamBean.BizContent());
		payParam.setCssId(usePayment.getAccount_number());
		payParam.setInType(passagewayInfo.getPay_type());
		payParam.setNotifyUrl(passagewayInfo.getNotify_url());
		payParam.setPrivateKey(usePayment.getPaymentKeyBox().getPrivate_key());
		payParam.setPublicKey(usePayment.getPaymentKeyBox().getPublic_key());
		
		payParam.getBizContent().setOut_trade_no(authentication.getPlatform_order_number());
		payParam.getBizContent().setProduct_code("QUICK_WAP_PAY");
		payParam.getBizContent().setSubject(authentication.getOrder_number());
		payParam.getBizContent().setTotal_amount(authentication.getPayment());
		
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

//	public static SearchPayBo subInfoQuery(YsH5SubQueryParamBean queryParam) {
//		Gson gson = new Gson();
//	    String rest = JsonHttpClientUtils.httpsPostform(SUBQUERYURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
//	    YsH5SubQueryResultBean queryBean = null;
//	    SearchPayBo searchPayBo = new SearchPayBo();
//	    try {
//	    	queryBean = (YsH5SubQueryResultBean) gson.fromJson(rest, YsH5SubQueryResultBean.class);
//	    	logger.info("获取成功：" + rest);
//	    	if (queryBean.getRet_code().equals("0")) {
//	    		if (queryBean.getBiz_content().getLists().get(0).getOrder_status().equals("2")) {
//	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
//	    			DecimalFormat df = new DecimalFormat("#0.00");
//	    			BigDecimal money = new BigDecimal(queryBean.getBiz_content().getLists().get(0).getPayment_fee()).divide(new BigDecimal(100));
//		    		searchPayBo.setMoney(df.format(money));
//	    		} else if (queryBean.getBiz_content().getLists().get(0).getOrder_status().equals("3")) {
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
//	    	searchPayBo.setOrder_number(queryBean.getBiz_content().getLists().get(0).getOrder_no());
//	    } catch(Exception e) {
//	    	logger.info("返回格式异常：" + rest);
//	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
//	    	searchPayBo.setMsg("返回信息异常");
//	    	searchPayBo.setOrder_number(queryParam.getOut_order_no());
//	    }
//	    return searchPayBo;
//	}

//	public static SearchPayBo subInfoQuery(String extraction_number, SubstituteAccount substitute) {
//		YsH5SubQueryParamBean queryParam = new YsH5SubQueryParamBean();
//		queryParam.setKey(substitute.getAccount_key());
//		queryParam.setMch_id(substitute.getAccount_number());
//		queryParam.setOrder_no("");
//		queryParam.setOut_order_no(extraction_number);
//		queryParam.setSign_type("MD5");
//		queryParam.setKey(substitute.getAccount_key());
//		queryParam.setSignature(queryParam.generateSign());
//		return subInfoQuery(queryParam);
//	}

}
