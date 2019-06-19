package com.boye.api;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.common.http.pay.WebPayParamBean;
import com.boye.common.http.pay.WebPayResultBean;
import com.boye.common.http.query.BePayQueryParamBean;
import com.boye.common.http.query.BePayQueryResultBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class WebPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(VPayApi.class);

    public static final String PAYURL = "http://47.52.157.120/payment/trans/QuickPay";

//    public static final String SUBURL = "http://pay.yudugs.com:89/tran";
      
//    public static final String SUBQUERYURL = "http://pay.yudugs.com:89/tran";
//
    public static final String QUERYURL = "http://pay.yudugs.com:89/tran/cashier/query.ac";
//    
//    public static final String BALANCEURL = "http://pay.yudugs.com:89/tran";

    public static Map<String, Object> getQrCode(WebPayParamBean payParam) {
    	
        
//        result.put("passageway_order_number", reMsg.getMchtOrderId());
        
        return null;
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
	
	public static SearchPayBo queryPayInfo(BePayQueryParamBean queryParam) {
		Gson gson = new Gson();
	    String rest = JsonHttpClientUtils.httpsPostform(QUERYURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	    BePayQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = gson.fromJson(rest, BePayQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if ("SUCCESS".equals(queryBean.getRetCode())) {
	    		if ("2".equals(queryBean.getStatus()) || "3".equals(queryBean.getStatus())) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(queryBean.getAmount());
	    			money = money.divide(BigDecimal.valueOf(100));
		    		searchPayBo.setMoney(df.format(money));
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryParam.getMchId());
	    	searchPayBo.setMsg("查询成功");
	    	searchPayBo.setOrder_number(queryParam.getMchOrderNo());
	    } catch(Exception e) {
	    	e.printStackTrace();
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getMchOrderNo());
	    }
	    return searchPayBo;
	}

	public static Map<String, Object> getQrCode(QuickAuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		TreeMap<String, String> json = new TreeMap<>();
        json.put("MERCNUM", usePayment.getAccount_number());
        HashMap<String, String> trandata = new HashMap<String, String>();
        trandata.put("PRO_ID", "YHQUICK_PAY");
        trandata.put("TXNAMT", new BigDecimal(authentication.getPayment()).multiply(BigDecimal.valueOf(100)).toString());
        trandata.put("ORDERNO", authentication.getPlatform_order_number());
		trandata.put("MERNOTIFYURL", passagewayInfo.getNotify_url());
		trandata.put("PAYCRDNO", authentication.getBank_card_number());
        StringBuilder stringBuilder = new StringBuilder();
        for (Entry<String, String> item : trandata.entrySet()) {
            stringBuilder.append(item.getKey()).append("=").append(item.getValue()).append("&");
        }
        stringBuilder.setLength(stringBuilder.length() - 1);
        logger.info("请求原文:" + stringBuilder.toString());
        try {
	        json.put("TRANDATA", URLEncoder.encode(Base64Utils.encode(stringBuilder.toString().getBytes()), "utf-8"));
	        System.out.println(gson.toJson(usePayment));
	        String sign = RSAUtil.signByPrivate(stringBuilder.toString(), usePayment.getPaymentKeyBox().getPrivate_key(), "utf-8");
	        json.put("SIGN", URLEncoder.encode(sign, "utf-8"));
	        String result = HttpUtils.doPost(PAYURL, RequestUtil.getParamSrc(json), "utf-8");
	        logger.info("返回报文： " + result);
        	WebPayResultBean reMsg = gson.fromJson(result, WebPayResultBean.class);
            if ("000000".equals(reMsg.getRECODE())) {
            	resultMap.put("code", 1);
            	resultMap.put("data", reMsg.getURL());
            	resultMap.put("msg","获取成功");
            } else {
            	resultMap.put("code", 2);
            	resultMap.put("data", "");
            	resultMap.put("msg", reMsg.getREMSG());
            }
        } catch(Exception e) {
        	e.printStackTrace();
        	resultMap.put("code", 2);
        	resultMap.put("data", "");
        	resultMap.put("msg", "获取失败");
        }
        return resultMap;
	}

}
