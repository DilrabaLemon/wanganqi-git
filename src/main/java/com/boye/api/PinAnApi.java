package com.boye.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.entity.SubstituteAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.common.http.query.CorAcctBalanceQueryBean;
import com.boye.common.http.query.CorAcctTxDetailQueryBean;
import com.boye.common.http.query.PinAnQueryBalanceResultBean;
import com.boye.common.http.query.PinAnSubQueryResultBean;
import com.boye.common.http.query.PinAnSubQueryResultBean.TranInfoArrayContent;
import com.boye.common.http.query.TranInfoQueryBean;
import com.boye.common.http.subpay.PinAnSubResultBean;
import com.boye.common.http.subpay.SingleDfTranDealBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.common.hash.HashCode;
import com.google.gson.Gson;

public class PinAnApi {
	
	private static Logger logger = LoggerFactory.getLogger(VPayApi.class);

//    public static final String PAYURL = "/api/v0.1/Deposit/Order";

    public static final String SUBURL = "/subpay/singleDfTranDeal";
      
    public static final String SUBQUERYURL = "/subpay/tranInfoQuery";
//
//    public static final String QUERYURL = "https://api.sumvic.com/api/v0.1/Deposit/Query";
//    
    public static final String BALANCEURL = "/subpay/corAcctBalanceQuery";
    
    public static final String QUERYTEST = "/subpay/corAcctTxDetailQuery";

//    public static Map<String, Object> getQrCode(YouPayParamBean payParam, String url) {
//    	Map<String, Object> resultMap = new HashMap<String, Object>();
//		Gson gson = new Gson();
//        String rest = JsonHttpClientUtils.httpPostform(url + PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
//        TopSubResultBean qrurl = null;
//        try {
//        	qrurl = (TopSubResultBean) gson.fromJson(rest, TopSubResultBean.class);
//        	if (qrurl.getIsSuccess()) {
//        		resultMap.put("code", "1");
//            	resultMap.put("msg", qrurl.getMessage());
//            	resultMap.put("data", qrurl.getData().getPayUrl());
//			}else {
//				resultMap.put("code", "2");
//	        	resultMap.put("msg", qrurl.getMessage());
//	        	resultMap.put("data", "");
//			}
//        	
//        } catch(Exception e) {
//        	resultMap.put("code","2");
//        	resultMap.put("msg", "远程服务响应异常");
//        	resultMap.put("data", "");
//        }
//	    return resultMap;
//	}
    
    public static Map<String, Object> corAcctTxDetailQuery(CorAcctTxDetailQueryBean queryBean, String serverUrl) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpPostform(serverUrl + QUERYTEST, queryBean.hasSignParamMap(), "UTF-8"); 	//返回json串
        logger.info(rest);
        PinAnSubResultBean result = null;
        try {
        	result = gson.fromJson(rest, PinAnSubResultBean.class);
        	if ("000000".equals(result.getTxnReturnCode())) {
        		resultMap.put("code", "1");
        	} else {
        		resultMap.put("code", "2");
        	}
        	resultMap.put("msg", result.getTxnReturnMsg());
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
	public static Map<String, Object> sendSubInfo(SingleDfTranDealBean subParam, String serverUrl) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpPostform(serverUrl + SUBURL, subParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        logger.info(rest);
        PinAnSubResultBean result = null;
        try {
        	result = gson.fromJson(rest, PinAnSubResultBean.class);
        	if ("000000".equals(result.getTxnReturnCode())) {
        		resultMap.put("code", "1");
        	} else {
        		resultMap.put("code", "2");
        	}
        	resultMap.put("msg", result.getTxnReturnMsg());
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
	public static Map<String, Object> sendSubInfo(SubPaymentInfo subPaymentInfo, SubstituteAccount useSubstitute,
			PassagewayInfo passageway, String serverUrl) {
		SingleDfTranDealBean subParam = new SingleDfTranDealBean();
		DecimalFormat df = new DecimalFormat("#0.00");
		
		subParam.setBussTypeNo("100157");
		subParam.setAgreeNo(useSubstitute.getCounter_number().split("-")[0]);
		subParam.setInoutFlag("2");
		subParam.setToAcctNo(subPaymentInfo.getBank_card_number());
		subParam.setToClientName(subPaymentInfo.getCard_user_name());
		subParam.setCcy("RMB");
		subParam.setTranAmt(df.format(subPaymentInfo.getSub_money()));
		subParam.setCnsmrSeqNo(subPaymentInfo.getSub_payment_number());
		subParam.setMrchCode(useSubstitute.getAccount_number());
		
		subParam.setKey(useSubstitute.getAccount_key());
		subParam.setSign(subParam.generateSign());
		
		return sendSubInfo(subParam, serverUrl);
	}
	
	public static SearchPayBo subInfoQuery(TranInfoQueryBean queryParam, String serverUrl) {
		Gson gson = new Gson();
        	//返回json串
		String rest = JsonHttpClientUtils.httpPostform(serverUrl + SUBQUERYURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	    System.out.println(rest);
		PinAnSubQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = gson.fromJson(rest, PinAnSubQueryResultBean.class);
	    	logger.info("获取成功：" + gson.toJson(queryBean));
	    	if ("000000".equals(queryBean.getTxnReturnCode())) {
	    		PinAnSubQueryResultBean.TranInfoArrayContent content = queryBean.getTranInfoArray().get(0);
	    		if ("1".equals(content.getDealStatus()) && "0000".equals(content.getReturnCode())) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(content.getTotalOccrAmt());
		    		searchPayBo.setMoney(df.format(money));
	    		} else if ("1".equals(content.getDealStatus()) && checkReturnCode(content)) {
	    			searchPayBo.setState(SearchPayStatus.ORDERFAIL);
	    			searchPayBo.setMsg("处理失败");
	    		}else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
//	    	searchPayBo.setAccount_number(queryParam.getBrandNo());
//	    	searchPayBo.setOrder_number(queryParam.getOrderNo());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getCnsmrSeqNo());
	    }
	    return searchPayBo;
	}

	private static boolean checkReturnCode(TranInfoArrayContent content) {
		Set<String> failCode = new HashSet<String>();
		if (content.getReturnCode().equals("9001")) return true;
		if (content.getReturnCode().equals("7000")) return true;
		if (content.getReturnCode().equals("2004")) return true;
		if (content.getReturnCode().equals("2018")) return true;
		if (content.getReturnCode().equals("5012")) return true;
		if (content.getReturnCode().equals("5022")) return true;
		if (content.getReturnCode().equals("5223")) return true;
		if (content.getReturnCode().equals("5225")) return true;
		if (content.getReturnCode().equals("5415")) return true;
		if (content.getReturnCode().equals("5416")) return true;
		if (content.getReturnCode().equals("5501")) return true;
		if (content.getReturnCode().equals("5502")) return true;
		if (content.getReturnCode().equals("5503")) return true;
		if (content.getReturnCode().equals("5511")) return true;
		if (content.getReturnCode().equals("5512")) return true;
		if (content.getReturnCode().equals("5513")) return true;
		if (content.getReturnCode().equals("6063")) return true;
		if (content.getReturnCode().equals("6065")) return true;
		if (content.getReturnCode().equals("6066")) return true;
		if (content.getReturnCode().equals("6196")) return true;
		if (content.getReturnCode().equals("5419")) return true;
		if (content.getReturnCode().equals("8012")) return true;
		if (content.getReturnCode().equals("6401")) return true;
		if (content.getReturnCode().equals("9999")) return true;
		if (content.getReturnCode().equals("6061")) return true;
		return false;
	}
	
	public static Map<String, Object> queryBalance(CorAcctBalanceQueryBean queryParam, String serverUrl) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpPostform(serverUrl + BALANCEURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        PinAnQueryBalanceResultBean result = null;
        try {
        	result = (PinAnQueryBalanceResultBean) gson.fromJson(rest, PinAnQueryBalanceResultBean.class);
        	System.out.println(rest);
        	if ("000000".equals(result.getTxnReturnCode())) {
        		resultMap.put("code", "1");
        		resultMap.put("data", result.getBalance());
        	} else {
        		resultMap.put("code", "2");
        	}
        	resultMap.put("msg", result.getTxnReturnMsg());
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
//	public static SearchPayBo queryPayInfo(YouPayQueryParamBean queryParam) {
//		Gson gson = new Gson();
//		String json = gson.toJson(queryParam.hasSignParamMap());
//		System.out.println(json);
//        String rest = JsonHttpClientUtils.httpPost(QUERYURL, json, "UTF-8"); 	//返回json串
//	    YouPayQueryResultBean queryBean = null;
//	    SearchPayBo searchPayBo = new SearchPayBo();
//	    try {
//	    	queryBean = gson.fromJson(rest, YouPayQueryResultBean.class);
//	    	logger.info("获取成功：" + rest);
//	    	if ("0".equals(queryBean.getCode())) {
//	    		if ("1".equals(queryBean.getData().getOrderStatus())) {
//	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
//	    			DecimalFormat df = new DecimalFormat("#0.00");
//	    			BigDecimal money = new BigDecimal(queryBean.getData().getPrice());
//		    		searchPayBo.setMoney(df.format(money));
//	    		} else {
//	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
//	    		}
//	    		
//	    	}else {
//	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
//	    	}
//	    	searchPayBo.setAccount_number(queryParam.getBrandNo());
//	    	searchPayBo.setMsg("查询成功");
//	    	searchPayBo.setOrder_number(queryParam.getOrderNo());
//	    } catch(Exception e) {
//	    	e.printStackTrace();
//	    	logger.info("返回格式异常：" + rest);
//	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
//	    	searchPayBo.setMsg("返回信息异常");
//	    	searchPayBo.setOrder_number(queryParam.getOrderNo());
//	    }
//	    return searchPayBo;
//	}

//	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
//		if (usePayment == null) return null;
//		YouPayParamBean payParam = new YouPayParamBean();
//		payParam.setKey(usePayment.getPaymentKeyBox().getPrivate_key());
//		payParam.setPkey(usePayment.getPaymentKeyBox().getPublic_key());
//		payParam.setBrandNo(usePayment.getAccount_number());
//		payParam.setCallbackUrl(passagewayInfo.getNotify_url());
//		payParam.setOrderNo(authentication.getPlatform_order_number());
//		BigDecimal decimal = new BigDecimal(authentication.getPayment());
//		BigDecimal setScale = decimal.setScale(2,BigDecimal.ROUND_HALF_UP);
//		payParam.setPrice(setScale);
//		payParam.setServiceType(passagewayInfo.getPay_type());// 支付宝
//		payParam.setSignType("RSA-S");
//		payParam.setUserName(usePayment.getCounter_number());
//		payParam.setClientIP("223.93.166.117");
//		payParam.setSignature(payParam.generateSign());
//		
//		return getQrCode(payParam);
//	}
	
}
