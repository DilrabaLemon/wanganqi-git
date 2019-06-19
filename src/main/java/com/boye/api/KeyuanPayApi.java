package com.boye.api;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
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
import com.boye.common.http.pay.KeyuanPayParamBean;
import com.boye.common.http.pay.KeyuanPayResultBean;
import com.boye.common.http.query.KeyuanPayQueryParamBean;
import com.boye.common.http.query.KeyuanPayQueryResultBean;
import com.boye.common.utils.WebUtils;
import com.google.gson.Gson;

public class KeyuanPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(VPayApi.class);

    public static final String PAYURL = "http://api.keyuanpay.cn/router";

//    public static final String SUBURL = "http://pay.yudugs.com:89/tran";
      
//    public static final String SUBQUERYURL = "http://pay.yudugs.com:89/tran";
//
    public static final String QUERYURL = "http://api.keyuanpay.cn/router";
//    
//    public static final String BALANCEURL = "http://pay.yudugs.com:89/tran";

    public static Map<String, Object> getQrCode(KeyuanPayParamBean payParam) {
    	Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
//        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
//        logger.info(rest);
        try {
        	System.out.println(gson.toJson(payParam.hasSignParamMap()));
        	String responseContent = WebUtils.doPost(PAYURL, payParam.hasSignParamMap(), payParam.getHeaders(), 15000, 15000);
        	System.out.println(responseContent);
        	KeyuanPayResultBean reMsg = gson.fromJson(responseContent, KeyuanPayResultBean.class);
        	if ("0".equals(reMsg.getEcode().toString())) {
            	result.put("code", 1);
            	result.put("data", reMsg.getResult().getAction().getUrl());
            	result.put("msg","获取成功");
            } else {
            	result.put("code", 2);
            	result.put("data", "");
            	result.put("msg", reMsg.getEmsg());
            }
        } catch(Exception e) {
        	result.put("code", 2);
        	result.put("data", "");
        	result.put("msg", "获取失败");
        }
        
        
//        result.put("passageway_order_number", reMsg.getMchtOrderId());
        
        return result;
//        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	}
	
//	public static Map<String, Object> sendSubInfo(FoPaySubParamBean subParam) {
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		Gson gson = new Gson();
//		System.out.println(SUBURL);
//		System.out.println(gson.toJson(subParam.hasSignParamMap()));
//        String rest = JsonHttpClientUtils.httpPostform(SUBURL, subParam.hasSignParamMap(), "UTF-8"); 	//返回json串
//        logger.info(rest);
//        FoPaySubResultBean result = null;
//        try {
//        	result = (FoPaySubResultBean) gson.fromJson(rest, FoPaySubResultBean.class);
//        	if ("0000".equals(result.getCode())) {
//        		resultMap.put("code", "1");
//        	} else {
//        		resultMap.put("code", "2");
//        	}
//        	resultMap.put("msg", result.getMsg());
//        	resultMap.put("data", result);
//        } catch(Exception e) {
//        	resultMap.put("code","2");
//        	resultMap.put("msg", "远程服务响应异常");
//        	resultMap.put("data", "");
//        }
//	    return resultMap;
//	}
	
//	public static Map<String, Object> sendSubInfo(SubPaymentInfo subPaymentInfo, SubstituteAccount useSubstitute,
//			PassagewayInfo passageway, String subKey) {
//		FoPaySubParamBean subParam = new FoPaySubParamBean();
//		DecimalFormat format = new DecimalFormat("0.00");
//		
////		subParam.setPay_method("2");
//////		subParam.setAcc_bank_name(subPaymentInfo.getBank_name());
//////		subParam.setAcct_no(subPaymentInfo.getBank_card_number());
////		subParam.setAli_user_name(subPaymentInfo.getBank_card_number());
////		subParam.setCharset("utf-8");
////		subParam.setCompany_no(useSubstitute.getAccount_number());
//////		subParam.setCustomer_name(subPaymentInfo.getCard_user_name());
////		subParam.setFormat("JSON");
////		subParam.setPhone_no(subPaymentInfo.getShop_phone());
////		subParam.setNotify_url(passageway.getNotify_url());
////		subParam.setOrder_amount(format.format(subPaymentInfo.getSub_money()));
////		subParam.setOrder_sn(subPaymentInfo.getSub_payment_number());
////		subParam.setSign_type("MD5");
//////		subParam.setWx_user_name(wx_user_name);
////		subParam.setKey(useSubstitute.getAccount_key());
////		subParam.setSign(subParam.generateSign());
//		
//		subParam.setPay_method("2");
////		subParam.setAcc_bank_name(subPaymentInfo.getBank_name());
////		subParam.setAcct_no(subPaymentInfo.getBank_card_number());
//		subParam.setAli_user_name("13732000902");
//		subParam.setCharset("utf-8");
//		subParam.setCompany_no("66880050100535017948");
//		subParam.setCustomer_name("aaa");
//		subParam.setFormat("JSON");
//		subParam.setPhone_no("13732000902");
//		subParam.setNotify_url("http://callback.c2qay6.com/pay/getFoPayResultMessage");
//		subParam.setOrder_amount("10.00");
//		subParam.setOrder_sn("01005350179485242019022516335");
//		subParam.setSign_type("MD5");
////		subParam.setWx_user_name(wx_user_name);
//		subParam.setKey("675dff9445806ab14614f8c24748bbb2");
//		subParam.setSign(subParam.generateSign());
//
//		return sendSubInfo(subParam);
//	}
	
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
	
	public static SearchPayBo queryPayInfo(KeyuanPayQueryParamBean queryParam) {
		Gson gson = new Gson();
	    KeyuanPayQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    System.out.println(gson.toJson(queryParam.hasSignParamMap()));
	    try {
	    	String rest = WebUtils.doPost(PAYURL, queryParam.hasSignParamMap(), queryParam.getHeaders(), 15000, 15000);
        	System.out.println(rest);
        	queryBean = gson.fromJson(rest, KeyuanPayQueryResultBean.class);
        	logger.info("获取成功：" + rest);
	    	if ("0".equals(queryBean.getEcode())) {
	    		if ("true".equals(queryBean.getResult().getPaid()) || "false".equals(queryBean.getResult().getRefund())) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(queryBean.getResult().getAmount());
	    			money = money.divide(BigDecimal.valueOf(100));
		    		searchPayBo.setMoney(df.format(money));
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryParam.getApp_key());
	    	searchPayBo.setMsg("查询成功");
	    	searchPayBo.setOrder_number(queryParam.getCharge_id());
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	logger.info("返回格式异常：" + queryParam.getCharge_id());
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getCharge_id());
	    }
//	    try {
//	    	queryBean = gson.fromJson(rest, KeyuanPayQueryResultBean.class);
//	    	logger.info("获取成功：" + rest);
//	    	if ("SUCCESS".equals(queryBean.getRetCode())) {
//	    		if ("2".equals(queryBean.getStatus()) || "3".equals(queryBean.getStatus())) {
//	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
//	    			DecimalFormat df = new DecimalFormat("#0.00");
//	    			BigDecimal money = new BigDecimal(queryBean.getAmount());
//	    			money = money.divide(BigDecimal.valueOf(100));
//		    		searchPayBo.setMoney(df.format(money));
//	    		} else {
//	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
//	    		}
//	    		
//	    	}else {
//	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
//	    	}
//	    	searchPayBo.setAccount_number(queryParam.getMchId());
//	    	searchPayBo.setMsg("查询成功");
//	    	searchPayBo.setOrder_number(queryParam.getMchOrderNo());
//	    } catch(Exception e) {
//	    	e.printStackTrace();
//	    	logger.info("返回格式异常：" + rest);
//	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
//	    	searchPayBo.setMsg("返回信息异常");
//	    	searchPayBo.setOrder_number(queryParam.getMchOrderNo());
//	    }
	    return searchPayBo;
	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		KeyuanPayParamBean payParam = new KeyuanPayParamBean();
		payParam.setAmount(new BigDecimal(authentication.getPayment()));
		payParam.setSubject(authentication.getOrder_number());
		payParam.setApp_key(usePayment.getAccount_number());
		payParam.setOrder_no(authentication.getPlatform_order_number());
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		payParam.setReturn_url(passagewayInfo.getReturn_url());
		payParam.setBuyer_openid("buyer_id");
		payParam.setChannel("alipayh5_xlpay");
		payParam.setClient_ip("127.0.0.1");
		payParam.setMetadata("361pay");
		payParam.setMethod("keyuanpay.payment.charge.pay");
		payParam.setSign_method("md5");
		payParam.setSign_time(new BigDecimal(new Date().getTime() / 1000).intValue() + "");
		payParam.setClient_id(usePayment.getAccount_number());
		
		payParam.setKey(usePayment.getAccount_key());
		try {
			payParam.setSign(payParam.generateSign());
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return getQrCode(payParam);
	}

}
