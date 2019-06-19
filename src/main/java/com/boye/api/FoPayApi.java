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
import com.boye.common.http.pay.FoPayParamBean;
import com.boye.common.http.pay.FoPayResultBean;
import com.boye.common.http.query.FoPayQueryParamBean;
import com.boye.common.http.query.FoPayQueryResultBean;
import com.boye.common.http.subpay.FoPaySubParamBean;
import com.boye.common.http.subpay.FoPaySubResultBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class FoPayApi {

	private static Logger logger = LoggerFactory.getLogger(FoPayApi.class);

    public static final String PAYURL = "http://plt.jkil.info/Api/Pay/pay_order";

    public static final String SUBURL = "http://plt.jkil.info/Api/OtherPay/other_pay_order";
    
//    public static final String SUBQUERYURL = "https://8oep1k.apolo-pay.com/agentpay/query";
//
    public static final String QUERYURL = "http://plt.jkil.info/Api/PayInterface/get_company_order";
//    
//    public static final String BALANCEURL = "https://8oep1k.apolo-pay.com/amountquery";

    public static Map<String, Object> getQrCode(FoPayParamBean payParam) {
    	Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        logger.info(rest);
        FoPayResultBean reMsg = gson.fromJson(rest, FoPayResultBean.class);
        if ("0".equals(reMsg.getCode()) && reMsg.getResult() != null) {
        	result.put("code", 1);
        	result.put("data", reMsg.getResult().getPay_html());
        } else {
        	result.put("code", 2);
        	result.put("data", "");
        }
        
//        result.put("passageway_order_number", reMsg.getMchtOrderId());
        result.put("msg", reMsg.getMsg());
        return result;
//        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	}
	
	public static Map<String, Object> sendSubInfo(FoPaySubParamBean subParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		System.out.println(SUBURL);
		System.out.println(gson.toJson(subParam.hasSignParamMap()));
        String rest = JsonHttpClientUtils.httpPostform(SUBURL, subParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        logger.info(rest);
        FoPaySubResultBean result = null;
        try {
        	result = (FoPaySubResultBean) gson.fromJson(rest, FoPaySubResultBean.class);
        	if ("0000".equals(result.getCode())) {
        		resultMap.put("code", "1");
        	} else {
        		resultMap.put("code", "2");
        	}
        	resultMap.put("msg", result.getMsg());
        	resultMap.put("data", result);
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
	public static Map<String, Object> sendSubInfo(SubPaymentInfo subPaymentInfo, SubstituteAccount useSubstitute,
			PassagewayInfo passageway, String subKey) {
		FoPaySubParamBean subParam = new FoPaySubParamBean();
		DecimalFormat format = new DecimalFormat("0.00");
		
//		subParam.setPay_method("2");
////		subParam.setAcc_bank_name(subPaymentInfo.getBank_name());
////		subParam.setAcct_no(subPaymentInfo.getBank_card_number());
//		subParam.setAli_user_name(subPaymentInfo.getBank_card_number());
//		subParam.setCharset("utf-8");
//		subParam.setCompany_no(useSubstitute.getAccount_number());
////		subParam.setCustomer_name(subPaymentInfo.getCard_user_name());
//		subParam.setFormat("JSON");
//		subParam.setPhone_no(subPaymentInfo.getShop_phone());
//		subParam.setNotify_url(passageway.getNotify_url());
//		subParam.setOrder_amount(format.format(subPaymentInfo.getSub_money()));
//		subParam.setOrder_sn(subPaymentInfo.getSub_payment_number());
//		subParam.setSign_type("MD5");
////		subParam.setWx_user_name(wx_user_name);
//		subParam.setKey(useSubstitute.getAccount_key());
//		subParam.setSign(subParam.generateSign());
		
		subParam.setPay_method("2");
//		subParam.setAcc_bank_name(subPaymentInfo.getBank_name());
//		subParam.setAcct_no(subPaymentInfo.getBank_card_number());
		subParam.setAli_user_name("13732000902");
		subParam.setCharset("utf-8");
		subParam.setCompany_no("66880050100535017948");
		subParam.setCustomer_name("aaa");
		subParam.setFormat("JSON");
		subParam.setPhone_no("13732000902");
		subParam.setNotify_url("http://callback.c2qay6.com/pay/getFoPayResultMessage");
		subParam.setOrder_amount("10.00");
		subParam.setOrder_sn("01005350179485242019022516335");
		subParam.setSign_type("MD5");
//		subParam.setWx_user_name(wx_user_name);
		subParam.setKey("675dff9445806ab14614f8c24748bbb2");
		subParam.setSign(subParam.generateSign());

		return sendSubInfo(subParam);
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
	
	public static SearchPayBo queryPayInfo(FoPayQueryParamBean queryParam) {
		Gson gson = new Gson();
	    String rest = JsonHttpClientUtils.httpsPostform(QUERYURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	    FoPayQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (FoPayQueryResultBean) gson.fromJson(rest, FoPayQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if ("0".equals(queryBean.getCode())) {
	    		FoPayQueryResultBean.Result result = queryBean.getResult();
	    		if ("20".equals(result.getStatus())) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(result.getPay_amount());
		    		searchPayBo.setMoney(df.format(money));
	    		} else if ("10".equals(result.getStatus())) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryBean.getResult().getCompany_no());
	    	searchPayBo.setMsg(queryBean.getMsg());
	    	searchPayBo.setOrder_number(queryBean.getResult().getOrder_sn());
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getKeyword());
	    }
	    return searchPayBo;
	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		FoPayParamBean payParam = new FoPayParamBean();
		payParam.setCharset("utf-8");
		payParam.setCompany_no(usePayment.getAccount_number());
		payParam.setSign_type("MD5");
		payParam.setGoods_name(authentication.getOrder_number().substring(9));
		payParam.setOrder_sn(authentication.getPlatform_order_number());
		payParam.setPay_type("alipay");
		payParam.setOrder_amount(new BigDecimal(authentication.getPayment()));
		payParam.setFormat("JSON");
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		
//		payParam.setCharset("utf-8");
//		payParam.setCompany_no("66880050100535017948");	
//		payParam.setGoods_name("111");
//		payParam.setOrder_sn("002132035161516003");
//		payParam.setPay_type("alipay");
//		payParam.setOrder_amount(BigDecimal.ONE);
//		payParam.setFormat("JSON");
//		payParam.setNotify_url("http://pay.c2qay6.com/api/pay/textReturnSite");
//		payParam.setSign_type("MD5");
//		payParam.setKey("675dff9445806ab14614f8c24748bbb2");
		
		payParam.setKey(usePayment.getAccount_key());
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}

}
