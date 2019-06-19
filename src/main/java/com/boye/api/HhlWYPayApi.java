package com.boye.api;

import java.math.BigDecimal;
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
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.common.http.pay.HhlQuickParamBean;
import com.boye.common.http.pay.HhlQuickResultBean;
import com.boye.common.http.pay.HhlWYParamBean;
import com.boye.common.http.query.HhlQueryParamBean;
import com.boye.common.http.query.HhlQueryResultBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class HhlWYPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(HhlWYPayApi.class);

    public static final String PAYURL = "https://pay.100cpay.com/api/v1/order/hanyin/net/silver/add";

//  public static final String SUBURL = "http://ccb.lanmeibank.com/api";

  public static final String QUERYURL = "https://pay.100cpay.com/api/v1/order/query";

//    public static Map<String, Object> sendSubInfo(QuickNotCardParamBean payParam) {
//        Map<String, Object> resultMap = new HashMap<String, Object>();
//        Gson gson = new Gson();
//        String json = gson.toJson(payParam.doSign());
//        String rest = JsonHttpClientUtils.httpPostform(SUBURL, json, "UTF-8");    //返回json串
//        QuickPayResultBean qpr = null;
//        try {
//            qpr = gson.fromJson(rest, QuickPayResultBean.class);
//            if (qpr.checkSign()) {
//                //校验签名
//                resultMap.put("code", "2");
//                resultMap.put("msg", "签名校验失败");
//                resultMap.put("data", "");
//            } else if ("00".equals(qpr.getStatusCode())) {
//                //支付成功
//                resultMap.put("code", qpr.getStatusCode());
//                resultMap.put("msg", qpr.getStatusMsg());
//                resultMap.put("data", qpr);
//            } else {
//                //支付失败
//                resultMap.put("code", "2");
//                resultMap.put("msg", qpr.getStatusMsg());
//                resultMap.put("data", "");
//            }
//        } catch (Exception e) {
//            resultMap.put("code", "2");
//            resultMap.put("msg", "远程服务响应异常");
//            resultMap.put("data", "");
//        }
//        return resultMap;
//    	return null;
//    }

    public static Map<String, Object> getQrCode(HhlWYParamBean payParam) {
    	Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
		String json = gson.toJson(payParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpsPost(PAYURL, json, "UTF-8"); 	//返回json串
        HhlQuickResultBean reMsg = gson.fromJson(rest, HhlQuickResultBean.class);
        result.put("code", reMsg.getMark());
        result.put("data", reMsg.getResult());
        result.put("passageway_order_number", reMsg.getOut_trade_no());
        result.put("msg", reMsg.getTip());
        return result;
//        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	}
	
//	public static Map<String, Object> sendSubInfo(QuickSubParamBean subParam) {
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		Gson gson = new Gson();
//        String rest = JsonHttpClientUtils.httpPostform(SUBURL, subParam.hasSignParamMap(), "UTF-8"); 	//返回json串
//        System.out.println(rest);
////        return rest;
//        QuickNotCardSubResultBean result = null;
//        try {
//        	result = (QuickNotCardSubResultBean) gson.fromJson(rest, QuickNotCardSubResultBean.class);
//        	resultMap.put("code", result.getStatusCode());
//        	resultMap.put("msg", result.getStatusMsg());
//        	resultMap.put("data", result);
//        } catch(Exception e) {
//        	resultMap.put("code","2");
//        	resultMap.put("msg", "远程服务响应异常");
//        	resultMap.put("data", "");
//        }
//	    return resultMap;
//	}
	
	public static SearchPayBo queryPayInfo(HhlQueryParamBean queryParam) {
		Gson gson = new Gson();
		String json = gson.toJson(queryParam.hasSignParamMap());
		System.out.println(json);
	    String rest = JsonHttpClientUtils.httpsPost(QUERYURL, json, "UTF-8"); 	//返回json串
	    HhlQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (HhlQueryResultBean) gson.fromJson(rest, HhlQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if (queryBean.getMark().equals("0")) {
	    		searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    		searchPayBo.setMoney(queryBean.getAccount_amount());
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryBean.getMerchant_open_id());
	    	searchPayBo.setMsg(queryBean.getTip());
	    	searchPayBo.setOrder_number(queryBean.getOut_trade_no());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getOut_trade_no());
	    }
	    return searchPayBo;
	}

//	机构号：80000384
//	机构商户编号：887581298600467
//	瀚银商户号：
//	WKJGWKQTCS@20180813173307 （D0+T1代付）
//	WKJGWKQTCS@20180814125309 （DT代付）
//	signkey: 3F7DB75AFBE34A4B40ECD0CC4A8B6492
//
//	测试银行卡一（贷记卡）
//	姓名：互联网
//	身份证号：341126197709218366
//	银行卡号：6221558812340000 （平安银行）  
//	cvn2:123   
//	卡有效期：1123
//	手机号：13552535506
	public static Map<String, Object> getQrCode(QuickAuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		HhlWYParamBean payParam = new HhlWYParamBean();
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		payParam.setMerchant_open_id(usePayment.getAccount_number());
		payParam.setCope_pay_amount(new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).longValue() + "");
		payParam.setMerchant_order_number(authentication.getPlatform_order_number());
		Date nowDate = new Date();
		payParam.setTimestamp(nowDate.getTime() + "");
		payParam.setPaymentChannel(authentication.getCard_code());
		payParam.setSubject("361pay");
		payParam.setPay_wap_mark(passagewayInfo.getPay_type());
		payParam.setKey(usePayment.getAccount_key());
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}
	
//	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
//		if (usePayment == null) return null;
////		DecimalFormat df = new DecimalFormat("#0.00");s
//		QuickSubParamBean subParam = new QuickSubParamBean();
//		subParam.setHpMerCode("WKJGWKQTCS@20180813173307");
//		subParam.setInsCode("80000384");
//		subParam.setInsMerchantCode("887581298600467");
//		subParam.setKey("3F7DB75AFBE34A4B40ECD0CC4A8B6492");
//		String code = UUID.randomUUID().toString().replaceAll("-", "");
//		subParam.setNonceStr(code);
//		subParam.setOrderNo("26165013003210001");
//		subParam.setPaymentType("2008");
//		subParam.setProductType("100000");
//		subParam.setTransAmount("100");
//		Date nowDate = new Date();
//		SimpleDateFormat dfDate = new SimpleDateFormat("yyyyMMddhhmmss");
//		subParam.setTransDate(dfDate.format(nowDate));
//		subParam.setSignature(subParam.generateSign());
//		return sendSubInfo(subParam);
//	}
	
//	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
//		if (usePayment == null) return null;
//		DecimalFormat df = new DecimalFormat("#0.00");
//		QuickSubParamBean subParam = new QuickSubParamBean();
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
