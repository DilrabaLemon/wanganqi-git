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
import com.boye.common.http.pay.ZFBZKPayParamBean;
import com.boye.common.http.query.ZFBZKQueryParamBean;
import com.boye.common.http.query.ZFBZKQueryResultBean;
import com.boye.common.utils.HttpClientUtil;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class ZFBZKPayApi {
    private static Logger logger = LoggerFactory.getLogger(ZFBZKPayApi.class);

    public static final String PAYURL = "http://47.75.123.80:9913/pay";


    public static final String QUERYURL = "http://47.75.123.80:9913/get_order_staus_by_id_real_time";

    public static Map<String, Object> getQrCode(ZFBZKPayParamBean payParam) {
	    Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
      	result.put("code", 1);
      	result.put("data", payParam.hasSignParamMap());
      	result.put("msg","获取成功");
        return result;
	}

	
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
//      String rest = JsonHttpClientUtils.httpsPostform(BALANCEURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
//      YsH5QueryBalanceResultBean result = null;
//      try {
//      	result = (YsH5QueryBalanceResultBean) gson.fromJson(rest, YsH5QueryBalanceResultBean.class);
//      	if (result.getRet_code().equals("0") && result.getBiz_content() != null) {
//      		resultMap.put("code", "1");
//      		resultMap.put("data", result.getBiz_content().getAmount());
//      	} else {
//      		resultMap.put("code", "2");
//      	}
//      	resultMap.put("msg", result.getRet_msg());
//      } catch(Exception e) {
//      	resultMap.put("code","2");
//      	resultMap.put("msg", "远程服务响应异常");
//      	resultMap.put("data", "");
//      }
//	    return resultMap;
//	}
	
	public static SearchPayBo queryPayInfo(ZFBZKQueryParamBean queryParam) {
		Gson gson = new Gson();
	    String rest = HttpClientUtil.httpGetByStream(QUERYURL + "?" +queryParam.hasSignGetParam(), "UTF-8"); 	//返回json串
	    ZFBZKQueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    rest = rest.trim();
	    try {
	    	logger.info("获取成功：" + rest);
	    	queryBean = gson.fromJson(rest, ZFBZKQueryResultBean.class);
	    	if ("1".equals(queryBean.getCode())) {
	    		if ("1".equals(queryBean.getData().getStatus())) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryParam.getUid());
	    	searchPayBo.setMsg(queryBean.getMsg());
	    	searchPayBo.setOrder_number(queryParam.getOrderid());
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
		ZFBZKPayParamBean payParam = new ZFBZKPayParamBean();
		payParam.setPrice(new BigDecimal(authentication.getPayment()));
		payParam.setGoodsname(authentication.getOrder_number());
		payParam.setUid(usePayment.getAccount_number());
		payParam.setOrderid(authentication.getPlatform_order_number());
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		payParam.setReturn_url(passagewayInfo.getReturn_url());
		payParam.setOrderuid(usePayment.getAccount_number());  //orderuid=uid
		payParam.setIstype(new BigDecimal(passagewayInfo.getPay_type()).intValue());
		payParam.setVersion(2);
		
		payParam.setToken(usePayment.getAccount_key());
		payParam.setKey(payParam.generateSign());
		return getQrCode(payParam);
	}

	public static Map<String, Object> getQrCodeParamUrl(AuthenticationInfo authentication, PassagewayInfo passagewayInfo) {
		Map<String, Object> result = new HashMap<String, Object>();
	      	result.put("code", 1);
	      	result.put("data", passagewayInfo.getServer_url() + "?" + authentication.findGetParam());
	      	result.put("msg","获取成功");
	    return result;
	}



}
