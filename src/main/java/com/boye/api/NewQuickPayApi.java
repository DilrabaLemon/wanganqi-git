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
import com.boye.common.http.pay.HhlResultBean;
import com.boye.common.http.pay.NewQuickPayParamBean;
import com.boye.common.http.pay.NewQuickPayResultBean;
import com.boye.common.http.query.NewQuickQueryBalanceParamBean;
import com.boye.common.http.query.NewQuickQueryBalanceResultBean;
import com.boye.common.http.query.NewQuickQueryParamBean;
import com.boye.common.http.query.NewQuickQueryResultBean;
import com.boye.common.http.query.NewQuickSubQueryParamBean;
import com.boye.common.http.query.NewQuickSubQueryResultBean;
import com.boye.common.http.query.YsH5QueryBalanceResultBean;
import com.boye.common.http.subpay.NewQuickSubParamBean;
import com.boye.common.http.subpay.NewQuickSubResultBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class NewQuickPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(NewQuickPayApi.class);
	
	public static final String PAYURL = "http://212.64.89.203:8889/tran/cashier/pay.ac";
	
	public static final String QUERYURL = "http://212.64.89.203:8889/tran/cashier/query.ac";
	
	public static final String SUBURL = "http://212.64.89.203:8889/tran/cashier/TX0001.ac";
	
	public static final String SUBQUERYURL = "http://212.64.89.203:8889/tran/cashier/TX0002.ac";
	
	public static final String BALANCEURL = "http://212.64.89.203:8889/tran/cashier/balance.ac";
	
	public static Map<String, Object> getQrCode(NewQuickPayParamBean payParam) {
	    Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        System.out.println(rest);
        NewQuickPayResultBean qrurl = null;
	    try {
	    	qrurl = (NewQuickPayResultBean) gson.fromJson(rest, NewQuickPayResultBean.class);
	    	if (qrurl.getCode().equals("000000")) {
	    		result.put("code", "1");
	    		result.put("data", qrurl.getBusContent());
	    	}else {
	    		result.put("code", "2");
	    		result.put("data", "");
	    	}
	    	result.put("passageway_order_number", qrurl.getPrdOrdNo());
	    	result.put("msg", qrurl.getMsg());
	    } catch(Exception e) {
	    	result.put("code", 2);
	    	result.put("msg", "远程服务响应异常");
	    	result.put("data", "");
	    }
	    return result;
	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		NewQuickPayParamBean payParam = new NewQuickPayParamBean();
		payParam.setBackUrl(passagewayInfo.getNotify_url());
//		payParam.setBankCode("");
//		payParam.setBuyIp("");
		payParam.setCardNo(authentication.getBank_card_number());
		payParam.setCustId(usePayment.getAccount_number());
		payParam.setCustOrderNo(authentication.getPlatform_order_number());
		payParam.setFrontUrl(passagewayInfo.getReturn_url());
		payParam.setGoodsName(authentication.getOrder_number());
//		payParam.setOrderDesc("");
		payParam.setOrgNo(usePayment.getCounter_number());
		payParam.setPayAmt(new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).intValue() + "");
		payParam.setTranType(passagewayInfo.getPay_type());
		payParam.setVersion("2.1");
		
		payParam.setKey(usePayment.getAccount_key());
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}
	
	public static SearchPayBo queryPayInfo(NewQuickQueryParamBean queryParam) {
		Gson gson = new Gson();
		String json = gson.toJson(queryParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpPost(QUERYURL, json, "UTF-8"); 	//返回json串
	    NewQuickQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (NewQuickQueryResultBean) gson.fromJson(rest, NewQuickQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if (queryBean.getCode().equals("000000")) {
	    		if (queryBean.getOrdStatus().equals("01")) {
		    		searchPayBo.setState(SearchPayStatus.GETSUCCESS);
		    		searchPayBo.setMoney(queryBean.getPayAmt());
	    		} else if (queryBean.getOrdStatus().equals("02")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    		}
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryBean.getPrdOrdNo());
	    	searchPayBo.setMsg(queryBean.getMsg());
	    	searchPayBo.setOrder_number(queryBean.getCustOrderNo());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
        	searchPayBo.setMsg("返回信息异常");
        	searchPayBo.setOrder_number(queryParam.getCustOrderNo());
	    }
	    return searchPayBo;
	}
	
	public static Map<String, Object> sendSubInfo(NewQuickSubParamBean subParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpsPostform(SUBURL, subParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        NewQuickSubResultBean result = null;
        try {
        	result = (NewQuickSubResultBean) gson.fromJson(rest, NewQuickSubResultBean.class);
        	if (result.getCode().equals("000000") && !result.getOrdStatus().equals("08")) {
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
	
	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
//		DecimalFormat df = new DecimalFormat("#0.00");
		NewQuickSubParamBean subParam = new NewQuickSubParamBean();
		subParam.setAccountName(extraction.getCard_user_name());
		subParam.setAccountType("1");
		subParam.setBankName(extraction.getBank_name());
		subParam.setVersion("2.1");
		subParam.setCallBackUrl(passagewayInfo.getNotify_url());
		subParam.setCardNo(extraction.getBank_card_number());
		subParam.setCasAmt(extraction.getExtraction_money().multiply(BigDecimal.valueOf(100)).longValue());
		subParam.setCasType("00");
//		subParam.setCnapsCode("");
		subParam.setCustId(usePayment.getAccount_number());
		subParam.setCustOrdNo(extraction.getExtraction_number());
		subParam.setDeductWay("02");
		subParam.setOrgNo(usePayment.getCounter_number());
		subParam.setSubBankName(extraction.getRegist_bank_name());
		subParam.setKey(usePayment.getAccount_key());
		
//		subParam.setAccountName("王佳婧");
//		subParam.setAccountType("1");
//		subParam.setBankName("中信银行");
//		subParam.setVersion("2.1");
//		subParam.setCallBackUrl("http://test361pay.361fit.com/pay/getNewQuickSubResultMessage");
//		subParam.setCardNo("6217680802791173");
//		subParam.setCasAmt(1000L);
//		subParam.setCasType("00");
////		subParam.setCnapsCode("");
//		subParam.setCustId("18121700001906");
//		subParam.setCustOrdNo("1000001211215411000001");
//		subParam.setDeductWay("02");
//		subParam.setOrgNo("8181200286");
////		subParam.setSubBankName("");
//		subParam.setKey("DAE5656CBF0A1614B9789DE97CAD564F");
		subParam.setSign(subParam.generateSign());
		return sendSubInfo(subParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		NewQuickSubParamBean subParam = new NewQuickSubParamBean();
		subParam.setAccountName(extraction.getCard_user_name());
		subParam.setAccountType("1");
		subParam.setBankName(extraction.getBank_name());
		subParam.setVersion("2.1");
		subParam.setCallBackUrl(passagewayInfo.getNotify_url());
		subParam.setCardNo(extraction.getBank_card_number());
		subParam.setCasAmt(extraction.getExtraction_money().multiply(BigDecimal.valueOf(100)).longValue());
		subParam.setCasType("00");
//		subParam.setCnapsCode("");
		subParam.setCustId(usePayment.getAccount_number());
		subParam.setCustOrdNo(extraction.getExtraction_number());
		subParam.setDeductWay("02");
		subParam.setOrgNo(usePayment.getCounter_number());
		subParam.setSubBankName(extraction.getRegist_bank_name());
		subParam.setKey(usePayment.getAccount_key());
		
		subParam.setSign(subParam.generateSign());
		return sendSubInfo(subParam);
	}
	
	public static SearchPayBo subInfoQuery(NewQuickSubQueryParamBean queryParam) {
		Gson gson = new Gson();
	    String rest = JsonHttpClientUtils.httpsPostform(SUBQUERYURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	    NewQuickSubQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (NewQuickSubQueryResultBean) gson.fromJson(rest, NewQuickSubQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if (queryBean.getCode().equals("000000")) {
	    		if (queryBean.getOrdStatus().equals("07")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(queryBean.getCasAmt()).divide(new BigDecimal(100));
		    		searchPayBo.setMoney(df.format(money));
	    		} else if (queryBean.getOrdStatus().equals("08")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryBean.getCustId());
	    	searchPayBo.setMsg(queryBean.getMsg());
	    	searchPayBo.setOrder_number(queryBean.getCustOrdNo());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getCustOrdNo());
	    }
	    return searchPayBo;
	}
	
	public static SearchPayBo subInfoQuery(String extraction_number, SubstituteAccount substitute) {
		NewQuickSubQueryParamBean queryParam = new NewQuickSubQueryParamBean();
//		queryParam.setCasOrdNo(casOrdNo);
		queryParam.setCustId(substitute.getAccount_number());
		queryParam.setCustOrdNo(extraction_number);
		queryParam.setOrgNo(substitute.getCounter_number());
		queryParam.setVersion("2.1");
		
		
		queryParam.setKey(substitute.getAccount_key());
		queryParam.setSign(queryParam.generateSign());
		return subInfoQuery(queryParam);
	}

	public static Map<String, Object> queryBalance(NewQuickQueryBalanceParamBean queryBean) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpsPostform(BALANCEURL, queryBean.hasSignParamMap(), "UTF-8"); 	//返回json串
        NewQuickQueryBalanceResultBean result = null;
        try {
        	result = (NewQuickQueryBalanceResultBean) gson.fromJson(rest, NewQuickQueryBalanceResultBean.class);
        	if (result.getCode().equals("000000") && result.getAcBal() != null) {
        		resultMap.put("code", "1");
        		resultMap.put("data", result.getAcBal());
        	} else {
        		resultMap.put("code", "2");
        	}
        	resultMap.put("msg", result.getMsg());
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
}
