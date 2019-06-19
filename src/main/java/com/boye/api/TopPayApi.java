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
import com.boye.bean.enums.SearchPayStatus;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.query.YouPayQueryParamBean;
import com.boye.common.http.query.YouPayQueryResultBean;
import com.boye.common.http.subpay.TopSubParamBean;
import com.boye.common.http.subpay.TopSubResultBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class TopPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(TopPayApi.class);
	
	public static final String PAYURL = "https://gateway.xbvnnn.top/api/v0.1/Deposit/Order";
	
	public static final String QUERYURL = "https://gateway.xbvnnn.top/api/v0.1/Deposit/Query";
//	
//	public static final String SUBURL = "https://gateway.xbvnnn.top/api/v0.1/Withdrawal/Order";
//    
//    public static final String SUBQUERYURL = "https://gateway.xbvnnn.top/api/v0.1/Withdrawal/Query";

	public static Map<String, Object> getQrCode(TopSubParamBean payParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String json = gson.toJson(payParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpsPost(PAYURL, json, "UTF-8"); 	//返回json串
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
		TopSubParamBean payParam = new TopSubParamBean();
		payParam.setKey(usePayment.getPaymentKeyBox().getPrivate_key());
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
	
//	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
//		if (usePayment == null) return null;
//		DecimalFormat df = new DecimalFormat("#0.00");
//		YtcpuSubParamBean subParam = new YtcpuSubParamBean();
//		subParam.setKey(usePayment.getAccount_key());
//		subParam.setAcc_name(extraction.getCard_user_name());
//		subParam.setAcc_no(extraction.getBank_card_number());
//		subParam.setAcc_type("0");
//		subParam.setBackCity(extraction.getCity_number());
//		subParam.setAmount(df.format(extraction.getExtraction_money()));
//		subParam.setBank_city(extraction.getRegist_bank());
//		subParam.setBank_name(extraction.getBank_name());
//		subParam.setBody("361pay");
//		subParam.setChannel(passagewayInfo.getPay_type());
//		subParam.setOut_order_no(extraction.getExtraction_number());
//		subParam.setPartner_id(usePayment.getAccount_number());
//		subParam.setSign(subParam.generateSign());
//		return sendSubInfo(subParam);
//	}
//	
//	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
//		if (usePayment == null) return null;
//		DecimalFormat df = new DecimalFormat("#0.00");
//		YtcpuSubParamBean subParam = new YtcpuSubParamBean();
//		subParam.setKey(usePayment.getAccount_key());
//		subParam.setAcc_name(extraction.getCard_user_name());
//		subParam.setAcc_no(extraction.getBank_card_number());
//		subParam.setAcc_type("0");
//		subParam.setBackCity(extraction.getCity_number());
//		subParam.setAmount(df.format(extraction.getExtraction_money()));
//		subParam.setBank_city(extraction.getRegist_bank());
//		subParam.setBank_name(extraction.getBank_name());
//		subParam.setBody("361pay");
//		subParam.setChannel(passagewayInfo.getPay_type());
//		subParam.setOut_order_no(extraction.getExtraction_number());
//		subParam.setPartner_id(usePayment.getAccount_number());
//		subParam.setSign(subParam.generateSign());
//		return sendSubInfo(subParam);
//	}

}
