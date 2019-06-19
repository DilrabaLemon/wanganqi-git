package com.boye.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
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
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.common.http.pay.HhlQuickParamBean;
import com.boye.common.http.pay.HhlQuickPayResultBean;
import com.boye.common.http.pay.HhlQuickResultBean;
import com.boye.common.http.query.HhlQueryParamBean;
import com.boye.common.http.query.HhlQueryResultBean;
import com.boye.common.http.query.HhlQuickSubQueryParamBean;
import com.boye.common.http.query.HhlQuickSubQueryResultBean;
import com.boye.common.http.query.NewQuickQueryBalanceParamBean;
import com.boye.common.http.query.NewQuickQueryBalanceResultBean;
import com.boye.common.http.subpay.HhlQuickSubParamBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class HhlQuickPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(HhlQuickPayApi.class);

    public static final String PAYURL = "https://pay.100cpay.com/api/v1/order/hanyin/add";

    public static final String SUBURL = "https://pay.100cpay.com/api/v1/order/hanyin/cash";

    public static final String QUERYURL = "https://pay.100cpay.com/api/v1/order/query";
    
    public static final String SUBQUERYURL = "https://pay.100cpay.com/api/v1/order/hanyin/cash/query";
    
    public static final String BALANCEURL = "";

    public static Map<String, Object> sendSubInfo(HhlQuickSubParamBean payParam) {
    	Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		String json = gson.toJson(payParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpsPost(SUBURL, json, "UTF-8"); 	//返回json串
        System.out.println(rest);
        HhlQuickPayResultBean qpr = null;
        try {
            qpr = gson.fromJson(rest, HhlQuickPayResultBean.class);
            if (qpr.getMark().equals("0")) {
                //支付成功
                resultMap.put("code", "1");
                resultMap.put("data", qpr);
                resultMap.put("cash_number", qpr.getCash_number());
            } else {
                //支付失败
                resultMap.put("code", "2");
                resultMap.put("data", "");
            }
            resultMap.put("msg", qpr.getTip());
        } catch (Exception e) {
            resultMap.put("code", "2");
            resultMap.put("msg", "远程服务响应异常");
            resultMap.put("data", "");
        }
        return resultMap;
    }

    public static Map<String, Object> getQrCode(HhlQuickParamBean payParam) {
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
	
	public static SearchPayBo subInfoQuery(HhlQuickSubQueryParamBean queryParam) {
		Gson gson = new Gson();
		String json = gson.toJson(queryParam.hasSignParamMap());
		System.out.println(json);
        String rest = JsonHttpClientUtils.httpsPost(SUBQUERYURL, json, "UTF-8");  	//返回json串
	    HhlQuickSubQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = gson.fromJson(rest, HhlQuickSubQueryResultBean.class);
	    	logger.info("获取成功：" + gson.toJson(queryBean));
	    	if (queryBean.getMark().equals("0")) {
    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
    			DecimalFormat df = new DecimalFormat("#0.00");
    			BigDecimal money = new BigDecimal(queryBean.getCash_amount()).divide(new BigDecimal(100));
	    		searchPayBo.setMoney(df.format(money));
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryParam.getMerchant_open_id());
	    	searchPayBo.setMsg(queryBean.getTip());
	    	searchPayBo.setOrder_number(queryParam.getCash_number());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getCash_number());
	    }
	    return searchPayBo;
	}

	public static Map<String, Object> getQrCode(QuickAuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		HhlQuickParamBean payParam = new HhlQuickParamBean();
		payParam.setAcc_no(authentication.getBank_card_number());
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		payParam.setMerchant_open_id(usePayment.getAccount_number());
		payParam.setName(authentication.getCert_name()== null?"":authentication.getCert_name());
		payParam.setId_number(authentication.getCert_number() == null?"":authentication.getCert_number());
		payParam.setCope_pay_amount(new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).longValue() + "");
		payParam.setMerchant_order_number(authentication.getPlatform_order_number());
		Date nowDate = new Date();
		payParam.setTimestamp(nowDate.getTime() + "");
		payParam.setPay_type("3");
		payParam.setSubject("361pay");
		payParam.setPay_wap_mark(passagewayInfo.getPay_type());
		payParam.setTel_no(authentication.getMobile() == null ? "" : authentication.getMobile());
		payParam.setKey(usePayment.getAccount_key());
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		HhlQuickSubParamBean subParam = new HhlQuickSubParamBean();
		subParam.setAcc_no(extraction.getBank_card_number());
		subParam.setBank_name(extraction.getBank_name());
		subParam.setCash_amount(extraction.getExtraction_money().add(new BigDecimal(2)).multiply(new BigDecimal(100)).intValue() + "");
		subParam.setDistrict(extraction.getRegist_bank_name());
		subParam.setKey(usePayment.getAccount_key());
		subParam.setMerchant_open_id(usePayment.getAccount_number());
		subParam.setName(extraction.getCard_user_name());
//		subParam.setTel_no(extraction.getUser_mobile());
//		subParam.setAcc_no("6217680802791173");
//		subParam.setBank_name("中信银行");
//		subParam.setCash_amount("2000");
//		subParam.setDistrict("杭州下沙开发区分行");
//		subParam.setId_number("330602199002230524");
//		subParam.setKey("a8344d20f0bb1f3eab094de05a5874ca");
//		subParam.setMerchant_open_id("6bd8de4eb2f97789c8af17a32316b471");
//		subParam.setName("王佳婧");
//		subParam.setTel_no("18657148576");
		Date nowDate = new Date();
		subParam.setTimestamp(nowDate.getTime() + "");
		subParam.setSign(subParam.generateSign());
		return sendSubInfo(subParam);
	}

	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment,
			PassagewayInfo passagewayInfo) {
		if (usePayment == null) return null;
		HhlQuickSubParamBean subParam = new HhlQuickSubParamBean();
		subParam.setAcc_no(extraction.getBank_card_number());
		subParam.setBank_name(extraction.getBank_name());
		subParam.setCash_amount(extraction.getExtraction_money().multiply(new BigDecimal(100)).intValue() + "");
		subParam.setDistrict(extraction.getRegist_bank());
		subParam.setId_number(extraction.getCert_number());
		subParam.setKey(usePayment.getAccount_key());
		subParam.setMerchant_open_id(usePayment.getAccount_number());
		subParam.setName(extraction.getCard_user_name());
//		subParam.setTel_no(extraction.getUser_mobile());
		Date nowDate = new Date();
		subParam.setTimestamp(nowDate.getTime() + "");
		subParam.setSign(subParam.generateSign());
		return sendSubInfo(subParam);
	}
	
	public static Map<String, Object> queryBalance(NewQuickQueryBalanceParamBean queryBean) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		resultMap.put("code","2");
    	resultMap.put("msg", "该提现方式没有无法查询到可用余额");
    	resultMap.put("data", "");
	    return resultMap;
	}
	
}
