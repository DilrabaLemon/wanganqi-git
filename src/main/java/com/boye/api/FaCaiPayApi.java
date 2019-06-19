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
import com.boye.common.http.pay.FaCaiPayParamBean;
import com.boye.common.http.query.FaCaiPayQueryParamBean;
import com.boye.common.http.query.FaCaiPayQueryResultBean;
import com.boye.common.utils.HttpClientUtil;
import com.google.gson.Gson;

public class FaCaiPayApi {

	private static Logger logger = LoggerFactory.getLogger(FaCaiPayApi.class);

    public static final String PAYURL = "http://gate.facaipay.net/API/PostOrderHandler.ashx";

//    public static final String SUBURL = "http://gate.facaipay.net/API/PostDistributionHandler.ashx";
    
//    public static final String SUBQUERYURL = "https://8oep1k.apolo-pay.com/agentpay/query";
//
    public static final String QUERYURL = "http://gate.facaipay.net/API/SearchOrderHandler.ashx";
//    
//    public static final String BALANCEURL = "https://8oep1k.apolo-pay.com/amountquery";

    public static Map<String, Object> getQrCode(FaCaiPayParamBean payParam) {
    	Map<String, Object> result = new HashMap<String, Object>();
//        String rest = HttpClientUtil.httpGet(PAYURL + "?" + payParam.hasSignGetParam(), "UTF-8"); 	//返回json串
//        logger.info(rest);
        result.put("code", 1);
        result.put("data", PAYURL + "?" + payParam.hasSignGetParam());
        result.put("msg", "获取成功");
        return result;
//      String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
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
//	
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
	
	public static SearchPayBo queryPayInfo(FaCaiPayQueryParamBean queryParam) {
		Gson gson = new Gson();
	    String rest = HttpClientUtil.httpGet(QUERYURL + "?" + queryParam.hasSignGetParam(), "UTF-8"); 	//返回json串
	    FaCaiPayQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (FaCaiPayQueryResultBean) gson.fromJson(rest, FaCaiPayQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if ("OK".equals(queryBean.getResult())) {
	    		if ("2".equals(queryBean.getStatus())) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(queryBean.getOvalue());
		    		searchPayBo.setMoney(df.format(money));
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryParam.getMerchant());
	    	searchPayBo.setMsg(queryBean.getMsg());
	    	searchPayBo.setOrder_number(queryBean.getOrderId());
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getOrderid());
	    }
	    return searchPayBo;
	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		FaCaiPayParamBean payParam = new FaCaiPayParamBean();
		
		payParam.setAttach(authentication.getOrder_number());
		payParam.setCallbackurl(passagewayInfo.getNotify_url());
//		payParam.setCardNo(CardNo);
//		payParam.setFullName(FullName);
		payParam.setHrefbackurl(passagewayInfo.getReturn_url());
//		payParam.setIDCard(IDCard);
		payParam.setMerchant(usePayment.getAccount_number());
		payParam.setOrderid(authentication.getPlatform_order_number());
//		payParam.setOrdertype("");
//		payParam.setPayerIp("");
//		payParam.setPhone(Phone);
		payParam.setType("210");
		payParam.setValue(new BigDecimal(authentication.getPayment()));
		payParam.setKey(usePayment.getAccount_key());
		
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}

}
