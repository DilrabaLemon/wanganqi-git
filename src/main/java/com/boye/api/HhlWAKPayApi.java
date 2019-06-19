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
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.entity.SubstituteAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.common.http.pay.HhlQuickParamBean;
import com.boye.common.http.pay.HhlQuickPayResultBean;
import com.boye.common.http.pay.HhlQuickResultBean;
import com.boye.common.http.pay.HhlWAKParamBean;
import com.boye.common.http.pay.HhlWYParamBean;
import com.boye.common.http.query.HhlQueryParamBean;
import com.boye.common.http.query.HhlQueryResultBean;
import com.boye.common.http.subpay.HhlWAKSubParamBean;
import com.boye.common.http.subpay.HhlYSFSubParamBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;

public class HhlWAKPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(HhlWAKPayApi.class);

    public static final String PAYURL = "http://pay.100cpay.com/api/v1/order/hanyin/qrcode/add";

    public static final String SUBURL = "https://pay.100cpay.com/api/v1/order/hanyin/cash";

//  public static final String QUERYURL = "https://pay.100cpay.com/api/v1/order/query";
    
    public static Map<String, Object> sendSubInfo(HhlWAKSubParamBean payParam) {
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

    public static Map<String, Object> sendSubInfo(SubPaymentInfo subPaymentInfo, SubstituteAccount useSubstitute,
			PassagewayInfo passageway) {
    	HhlWAKSubParamBean subParam = new HhlWAKSubParamBean();
		
    	subParam.setAcc_no(subPaymentInfo.getBank_card_number());
    	subParam.setCash_amount(subPaymentInfo.getSub_money().multiply(new BigDecimal(100)).intValue() + "");
    	subParam.setMerchant_open_id(useSubstitute.getAccount_number());
    	subParam.setName(subPaymentInfo.getCard_user_name());
    	subParam.setTimestamp((new Date()).getTime() + "");
    	subParam.setBank_name(subPaymentInfo.getBank_name());
    	subParam.setId_number(subPaymentInfo.getCert_number() == null ? "" : subPaymentInfo.getCert_number());
    	subParam.setTel_no("");
    	subParam.setDistrict(subPaymentInfo.getRegist_bank_name() == null ? "" : subPaymentInfo.getRegist_bank_name());
		subParam.setKey(useSubstitute.getAccount_key());
		subParam.setSign(subParam.generateSign());
		
		return sendSubInfo(subParam);
	}

    public static Map<String, Object> getQrCode(HhlWAKParamBean payParam) {
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
	
	public static Map<String, Object> getQrCode(QuickAuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		HhlWAKParamBean payParam = new HhlWAKParamBean();
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		payParam.setAcc_no(authentication.getBank_card_number());
		payParam.setMerchant_open_id(usePayment.getAccount_number());
		payParam.setCope_pay_amount(new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).longValue() + "");
		payParam.setMerchant_order_number(authentication.getPlatform_order_number());
		Date nowDate = new Date();
		payParam.setTimestamp(nowDate.getTime() + "");
		payParam.setPay_type("3");
		payParam.setSubject("361pay");
		payParam.setPay_wap_mark(passagewayInfo.getPay_type());
		payParam.setKey(usePayment.getAccount_key());
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}
	
}
