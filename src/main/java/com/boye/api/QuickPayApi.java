package com.boye.api;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
import com.boye.common.http.pay.QuickPayParamBean;
import com.boye.common.http.pay.QuickResultBean;
import com.boye.common.http.query.QuickQueryParamBean;
import com.boye.common.http.query.QuickQueryResultBean;
import com.boye.common.http.subpay.YtcpuSubParamBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class QuickPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(QuickPayApi.class);
	
	public static final String PAYURL = "https://gateway.handpay.cn/hpayTransGatewayWeb/trans/df/transdf.htm";

//	public static final String SUBURL = "http://pay.ytcpu.com/quick/pay/for";
	
	public static final String QUERYURL = "https://gateway.handpay.cn/hpayTransGatewayWeb/trans/query.htm";
	
	public static Map<String, Object> getQrCode(QuickPayParamBean payParam) {
		Gson gson = new Gson();
		Map<String, Object> resultMap = new HashMap<String, Object>();
        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        QuickResultBean qrurl = null;
        try {
        	qrurl = (QuickResultBean) gson.fromJson(rest, QuickResultBean.class);
        	resultMap.put("code", qrurl.getStatusCode());
        	resultMap.put("msg", qrurl.getStatusMsg());
        	resultMap.put("data", qrurl);
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
        resultMap.put("code","1");
    	resultMap.put("msg", "");
    	resultMap.put("data", rest);
	    return resultMap;
	}
	
	private static Map<String, Object> sendSubInfo(YtcpuSubParamBean subParam) {
//		Map<String, Object> resultMap = new HashMap<String, Object>();
//		Gson gson = new Gson();
//		String json = gson.toJson(subParam.hasSignParamMap());
//        String rest = JsonHttpClientUtils.httpPost(SUBURL, json, "UTF-8"); 	//返回json串
//        System.out.println(rest);
////        return rest;
//        YtcpuSubResultBean result = null;
//        try {
//        	result = (YtcpuSubResultBean) gson.fromJson(rest, YtcpuSubResultBean.class);
//        	resultMap.put("code", result.getCode());
//        	resultMap.put("msg", result.getMsg());
//        	resultMap.put("data", result);
//        } catch(Exception e) {
//        	resultMap.put("code","2");
//        	resultMap.put("msg", "远程服务响应异常");
//        	resultMap.put("data", "");
//        }
//	    return resultMap;
		return null;
	}
	
	public static SearchPayBo queryPayInfo(QuickQueryParamBean queryParam) {
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpPostform(QUERYURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        System.out.println(rest);
		QuickQueryResultBean queryBean = null;
		SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (QuickQueryResultBean) gson.fromJson(rest, QuickQueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if (queryBean.getStatusCode().equals("00")) {
	    		if (queryBean.getTransStatus().equals("00")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			searchPayBo.setMoney(queryBean.getTransAmount());
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    		}
	    	} else {
	    		searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    	}

	    	searchPayBo.setAccount_number(queryBean.getInsMerchantCode());
	    	searchPayBo.setMsg(queryBean.getStatusMsg());
	    	searchPayBo.setOrder_number(queryBean.getOrderNo());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setAccount_number(queryParam.getInsMerchantCode());
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getOrderNo());
	    }
	    return searchPayBo;
	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		QuickPayParamBean payParam = new QuickPayParamBean();
		payParam.setAccountNumber("6221558812340000"); // 银行卡号  authentication
		payParam.setCurrencyCode("156");  //  订单币种   default
		payParam.setHpMerCode("WKJGWKQTCS@20180813173307");   //  瀚银商户号   usePayment
//		payParam.setIdNumber("341126197709218366");
		payParam.setInsCode("80000384");   //  机构号  usePayment
		payParam.setInsMerchantCode("887581298600467");   // 机构商户编号   usePayment
		payParam.setAccountName("互联网");  //  用户名称   authentication  
		String code = UUID.randomUUID().toString().replaceAll("-", "");  
		payParam.setNonceStr(code);  //  随机串  
		payParam.setOrderAmount("10");  //  订单金额  authentication 
		payParam.setOrderNo("1000000000031236");  // 订单号  authentication
		Date nowDate = new Date();  
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		SimpleDateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
		payParam.setOrderTime(df.format(nowDate));  //  下单时间
		payParam.setSignKey("3F7DB75AFBE34A4B40ECD0CC4A8B6492");   //  密钥  usePayment
		payParam.setAccountType("01");  // 账户类型  default
		payParam.setAttach("123");  //  商户私有域  
		payParam.setCertNumber("341126197709218366");  //  证件号  authentication  
		payParam.setCertType("01");  // 证件类型   authentication
		payParam.setMainBankCode("307584007998");  //银行编号  query
		payParam.setMainBankName("平安银行");  //  银行名称   authentication
		payParam.setMobile("13552535506");  //  手机号  authentication
		payParam.setOpenBranchBankName("平安银行");  //  注册行   authentication
		payParam.setOrderDate(dfDate.format(nowDate));  //  下单时间
		payParam.setOrderType("D0");  //  订单类型  default
		payParam.setSignature(payParam.generateSign());   // 签名
		return getQrCode(payParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		DecimalFormat df = new DecimalFormat("#0.00");
		YtcpuSubParamBean subParam = new YtcpuSubParamBean();
		subParam.setKey(usePayment.getAccount_key());
		subParam.setAcc_name(extraction.getCard_user_name());
		subParam.setAcc_no(extraction.getBank_card_number());
		subParam.setAcc_type("0");
		subParam.setBackCity(extraction.getCity_number());
		subParam.setAmount(df.format(extraction.getExtraction_money()));
		subParam.setBank_city(extraction.getRegist_bank());
		subParam.setBank_name(extraction.getBank_name());
		subParam.setBody("361pay");
		subParam.setChannel(passagewayInfo.getPay_type());
		subParam.setOut_order_no(extraction.getExtraction_number());
		subParam.setPartner_id(usePayment.getAccount_number());
		subParam.setSign(subParam.generateSign());
		return sendSubInfo(subParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		DecimalFormat df = new DecimalFormat("#0.00");
		YtcpuSubParamBean subParam = new YtcpuSubParamBean();
		subParam.setKey(usePayment.getAccount_key());
		subParam.setAcc_name(extraction.getCard_user_name());
		subParam.setAcc_no(extraction.getBank_card_number());
		subParam.setAcc_type("0");
		subParam.setBackCity(extraction.getCity_number());
		subParam.setAmount(df.format(extraction.getExtraction_money()));
		subParam.setBank_city(extraction.getRegist_bank());
		subParam.setBank_name(extraction.getBank_name());
		subParam.setBody("361pay");
		subParam.setChannel(passagewayInfo.getPay_type());
		subParam.setOut_order_no(extraction.getExtraction_number());
		subParam.setPartner_id(usePayment.getAccount_number());
		subParam.setSign(subParam.generateSign());
		return sendSubInfo(subParam);
	}
}
