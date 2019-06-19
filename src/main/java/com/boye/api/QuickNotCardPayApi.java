package com.boye.api;

import java.math.BigDecimal;
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
import com.boye.common.http.pay.QuickNotCardParamBean;
import com.boye.common.http.query.QuickQueryParamBean;
import com.boye.common.http.query.QuickQueryResultBean;
import com.boye.common.http.subpay.QuickNotCardSubResultBean;
import com.boye.common.http.subpay.QuickSubParamBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

/**
 * QuickPayApi
 * 2018/10/19
 *
 * @author max
 */
public class QuickNotCardPayApi {

    private static Logger logger = LoggerFactory.getLogger(YtcpuPayApi.class);

    public static final String PAYURL = "https://gateway.handpay.cn/hpayTransGatewayWeb/trans/debit.htm";
//    public static final String PAYURL = "http://180.168.61.86:27380 /hpayTransGatewayWeb/trans/debit.htm";

    public static final String SUBURL = "https://gateway.handpay.cn/hpayTransGatewayWeb/withdraw/apply.htm";
//    public static final String SUBURL = "http://180.168.61.86:27380/hpayTransGatewayWeb/trans/df/transdf.htm";

    public static final String QUERYURL = "https://gateway.handpay.cn/hpayTransGatewayWeb/trans/query.htm";
//    public static final String QUERYURL = "http://180.168.61.86:27380/hpayTransGatewayWeb/trans/query.htm";

    public static Map<String, Object> sendSubInfo(QuickNotCardParamBean payParam) {
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
    	return null;
    }

    public static String getQrCode(QuickNotCardParamBean payParam) {
        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	    return rest;
	}
	
	public static Map<String, Object> sendSubInfo(QuickSubParamBean subParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpPostform(SUBURL, subParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        System.out.println(rest);
//        return rest;
        QuickNotCardSubResultBean result = null;
        try {
        	result = (QuickNotCardSubResultBean) gson.fromJson(rest, QuickNotCardSubResultBean.class);
        	resultMap.put("code", result.getStatusCode());
        	resultMap.put("msg", result.getStatusMsg());
        	resultMap.put("data", result);
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
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
	public static String getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		QuickNotCardParamBean payParam = new QuickNotCardParamBean();
		payParam.setAccNo(authentication.getBank_card_number());
		payParam.setBackUrl(passagewayInfo.getReturn_url());
		payParam.setCurrencyCode("156");
		payParam.setFrontUrl(passagewayInfo.getNotify_url());
		payParam.setHpMerCode(usePayment.getAccount_code());
		payParam.setInsCode(usePayment.getCounter_number());
		payParam.setInsMerchantCode(usePayment.getAccount_number());
		payParam.setMerGroup("");
		payParam.setName(authentication.getCert_name());
		String code = UUID.randomUUID().toString().replaceAll("-", "");
		payParam.setNonceStr(code);
		payParam.setOrderAmount(new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).longValue() + "");
		payParam.setOrderNo(authentication.getPlatform_order_number());
		Date nowDate = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		payParam.setOrderTime(df.format(nowDate));
		payParam.setPaymentType("2008");
		payParam.setProductType("100000");
		payParam.setTelNo(authentication.getMobile());
		payParam.setKey(usePayment.getAccount_key());
		payParam.setSignature(payParam.generateSign());
		return getQrCode(payParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
//		DecimalFormat df = new DecimalFormat("#0.00");s
		QuickSubParamBean subParam = new QuickSubParamBean();
//		subParam.setAccountNumber(extraction.getBank_card_number()); // 银行卡号  authentication
//		subParam.setCurrencyCode("156");  //  订单币种   default
//		subParam.setHpMerCode(usePayment.getAccount_code());   //  瀚银商户号   usePayment
//		subParam.setInsCode(usePayment.getCounter_number());   //  机构号  usePayment
//		subParam.setInsMerchantCode(usePayment.getAccount_number());   // 机构商户编号   usePayment
//		subParam.setAccountName(extraction.getCard_user_name());  //  用户名称   authentication  
//		String code = UUID.randomUUID().toString().replaceAll("-", "");  
//		subParam.setNonceStr(code);  //  随机串  
//		subParam.setOrderAmount((int)(extraction.getActual_money().doubleValue() * 100) + "");  //  订单金额  authentication 
//		subParam.setOrderNo(extraction.getExtraction_number());  // 订单号  authentication
//		Date nowDate = new Date();  
//		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
//		SimpleDateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
//		subParam.setOrderTime(df.format(nowDate));  //  下单时间
//		subParam.setSignKey(usePayment.getAccount_key());   //  密钥  usePayment
//		subParam.setAccountType("01");  // 账户类型  default
//		subParam.setAttach("123");  //  商户私有域  
//		subParam.setCertNumber("");  //  证件号  authentication  
//		subParam.setCertType("01");  // 证件类型   authentication
//		subParam.setMainBankCode("302100011000");  //银行编号  query
//		subParam.setMainBankName(extraction.getBank_name());  //  银行名称   authentication
//		subParam.setMobile(extraction.getUser_mobile());  //  手机号  authentication
//		subParam.setOpenBranchBankName(extraction.getRegist_bank_name());  //  注册行   authentication
//		subParam.setOrderDate(dfDate.format(nowDate));  //  下单时间
//		subParam.setOrderType("DF");  //  订单类型  default
//		subParam.setSignature(subParam.generateSign());   // 签名
		
		subParam.setAccountNumber("6217680802791173"); // 银行卡号  authentication
		subParam.setCurrencyCode("156");  //  订单币种   default
		subParam.setHpMerCode("MFDMYWDTFDIRWK@20181024171803");   //  瀚银商户号   usePayment
		subParam.setInsCode("80000935");   //  机构号  usePayment
		subParam.setInsMerchantCode("887581298601477");   // 机构商户编号   usePayment
		subParam.setAccountName("王佳婧");  //  用户名称   authentication  
		String code = UUID.randomUUID().toString().replaceAll("-", "");  
		subParam.setNonceStr(code);  //  随机串  
		subParam.setOrderAmount("100");  //  订单金额  authentication 
		subParam.setOrderNo("1000000000031236");  // 订单号  authentication
		Date nowDate = new Date();  
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddhhmmss");
		SimpleDateFormat dfDate = new SimpleDateFormat("yyyyMMdd");
		subParam.setOrderTime(df.format(nowDate));  //  下单时间
		subParam.setSignKey("3F7DB75AFBE34A4B40ECD0CC4A8B6492");   //  密钥  usePayment
		subParam.setAccountType("01");  // 账户类型  default
		subParam.setAttach("123");  //  商户私有域  
		subParam.setCertNumber("330602199002230524");  //  证件号  authentication  
		subParam.setCertType("01");  // 证件类型   authentication
		subParam.setMainBankCode("302100011000");  //银行编号  query
		subParam.setMainBankName("中信银行");  //  银行名称   authentication
		subParam.setMobile("18657148576");  //  手机号  authentication
		subParam.setOpenBranchBankName("中信银行杭州经济技术开发区分行");  //  注册行   authentication
		subParam.setOrderDate(dfDate.format(nowDate));  //  下单时间
		subParam.setOrderType("DF");  //  订单类型  default
		subParam.setSignature(subParam.generateSign());   // 签名
		return sendSubInfo(subParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
//		DecimalFormat df = new DecimalFormat("#0.00");
		QuickSubParamBean subParam = new QuickSubParamBean();
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
		return sendSubInfo(subParam);
	}
}
