package com.boye.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.entity.SubstituteAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.enums.BankCodeEnum;
import com.boye.common.http.pay.YouPayParamBean;
import com.boye.common.http.query.YouPayQueryParamBean;
import com.boye.common.http.query.YouPayQueryResultBean;
import com.boye.common.http.query.YouPaySubQueryParamBean;
import com.boye.common.http.query.YouPaySubQueryResultBean;
import com.boye.common.http.subpay.TopSubResultBean;
import com.boye.common.http.subpay.YouPaySubParamBean;
import com.boye.common.http.subpay.YouPaySubResultBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class YouPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(VPayApi.class);

    public static final String PAYURL = "https://api.sumvic.com/api/v0.1/Deposit/Order";

    public static final String SUBURL = "https://api.sumvic.com/api/v0.1/Withdrawal/Order";
      
    public static final String SUBQUERYURL = "https://api.sumvic.com/api/v0.1/Withdrawal/Query";
//
    public static final String QUERYURL = "https://api.sumvic.com/api/v0.1/Deposit/Query";
//    
//    public static final String BALANCEURL = "https://portal.sumvic.com/api/v0.1/Deposit/Order";

    public static Map<String, Object> getQrCode(YouPayParamBean payParam) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String json = gson.toJson(payParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpPost(PAYURL, json, "UTF-8"); 	//返回json串
        TopSubResultBean qrurl = null;
        try {
        	qrurl = (TopSubResultBean) gson.fromJson(rest, TopSubResultBean.class);
        	if (qrurl.getIsSuccess()) {
        		resultMap.put("code", 1);
            	resultMap.put("msg", qrurl.getMessage());
            	resultMap.put("data", qrurl.getData().getPayUrl());
			}else {
				resultMap.put("code", 2);
	        	resultMap.put("msg", qrurl.getMessage());
	        	resultMap.put("data", "");
			}
        	
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
	public static Map<String, Object> sendSubInfo(YouPaySubParamBean subParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String json = gson.toJson(subParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpPost(SUBURL, json, "UTF-8"); 	//返回json串
        logger.info(rest);
        YouPaySubResultBean result = null;
        try {
        	result = gson.fromJson(rest, YouPaySubResultBean.class);
        	if ("0".equals(result.getCode())) {
        		resultMap.put("code", "1");
        	} else {
        		resultMap.put("code", "2");
        	}
        	resultMap.put("msg", result.getMessage());
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
	public static Map<String, Object> sendSubInfo(SubPaymentInfo subPaymentInfo, SubstituteAccount useSubstitute,
			PassagewayInfo passageway, String subKey) {
		YouPaySubParamBean subParam = new YouPaySubParamBean();
		
		subParam.setBankAccountName(subPaymentInfo.getCard_user_name());
		subParam.setBankAccountNo(subPaymentInfo.getBank_card_number());
		subParam.setBankCityCode(subPaymentInfo.getCity_number());
		subParam.setBankCode(BankCodeEnum.key(subPaymentInfo.getBank_name()).getCode());
		subParam.setBankProvinceCode(subPaymentInfo.getCity_number().substring(0, 2) + "0000");
		subParam.setBankSiteName(subPaymentInfo.getRegist_bank_name());
		subParam.setBrandNo(useSubstitute.getAccount_number());
		subParam.setCallbackUrl(passageway.getNotify_url());
		subParam.setClientIP("223.93.166.117");
		subParam.setOrderNo(subPaymentInfo.getSub_payment_number());
		subParam.setPrice(subPaymentInfo.getSub_money());
		subParam.setServiceType(passageway.getPay_type());
		subParam.setUserName(useSubstitute.getCounter_number());
		subParam.setSignType("RSA-S");
		
		subParam.setKey(useSubstitute.getKeyBox().getPrivate_key());
		subParam.setSignature(subParam.generateSign());
		
		return sendSubInfo(subParam);
	}
	
	public static SearchPayBo subInfoQuery(YouPaySubQueryParamBean queryParam) {
		Gson gson = new Gson();
        	//返回json串
		String json = gson.toJson(queryParam.hasSignParamMap());
		System.out.println(json);
		String rest = JsonHttpClientUtils.httpPost(SUBQUERYURL, json, "UTF-8"); 	//返回json串
	    YouPaySubQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = gson.fromJson(rest, YouPaySubQueryResultBean.class);
	    	logger.info("获取成功：" + gson.toJson(queryBean));
	    	if ("0".equals(queryBean.getCode())) {
	    		if ("1".equals(queryBean.getData().getOrderStatus())) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(queryBean.getData().getPrice());
		    		searchPayBo.setMoney(df.format(money));
	    		} else if ("2".equals(queryBean.getData().getOrderStatus())) {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    			searchPayBo.setMsg("订单正在处理中");
	    		} else if ("3".equals(queryBean.getData().getOrderStatus())) {
	    			searchPayBo.setState(SearchPayStatus.ORDERFAIL);
	    			searchPayBo.setMsg("订单支付失败");
	    		}else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryParam.getBrandNo());
	    	searchPayBo.setOrder_number(queryParam.getOrderNo());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getOrderNo());
	    }
	    return searchPayBo;
	}
	
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
	
	public static SearchPayBo queryPayInfo(YouPayQueryParamBean queryParam) {
		Gson gson = new Gson();
		String json = gson.toJson(queryParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpPost(QUERYURL, json, "UTF-8"); 	//返回json串
	    YouPayQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = gson.fromJson(rest, YouPayQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if ("0".equals(queryBean.getCode())) {
	    		if ("1".equals(queryBean.getData().getOrderStatus())) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(queryBean.getData().getPrice());
		    		searchPayBo.setMoney(df.format(money));
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryParam.getBrandNo());
	    	searchPayBo.setMsg("查询成功");
	    	searchPayBo.setOrder_number(queryParam.getOrderNo());
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getOrderNo());
	    }
	    return searchPayBo;
	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		YouPayParamBean payParam = new YouPayParamBean();
		payParam.setKey(usePayment.getPaymentKeyBox().getPrivate_key());
		payParam.setPkey(usePayment.getPaymentKeyBox().getPublic_key());
		payParam.setBrandNo(usePayment.getAccount_number());
		payParam.setCallbackUrl(passagewayInfo.getNotify_url());
		payParam.setOrderNo(authentication.getPlatform_order_number());
		BigDecimal decimal = new BigDecimal(authentication.getPayment());
		BigDecimal setScale = decimal.setScale(2,BigDecimal.ROUND_HALF_UP);
		payParam.setPrice(setScale);
		payParam.setServiceType(passagewayInfo.getPay_type());// 支付宝
		payParam.setSignType("RSA-S");
		payParam.setUserName(usePayment.getCounter_number());
		payParam.setClientIP("223.93.166.117");
		payParam.setSignature(payParam.generateSign());
		
		return getQrCode(payParam);
	}

}
