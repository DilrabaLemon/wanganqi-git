package com.boye.api;

import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.pay.HJPayParamBean;
import com.boye.common.http.pay.HJPayResultBean;
import com.boye.common.http.pay.JjsmPayParamBean;
import com.boye.common.http.pay.JjsmPayResultBean;
import com.boye.common.http.query.HJPayQueryParamBean;
import com.boye.common.http.query.HJPayQueryResultBean;
import com.boye.common.utils.JsonHttpClientUtils;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JjsmPayApi {

	private static Logger logger = LoggerFactory.getLogger(JjsmPayApi.class);

    public static final String PAYURL = "http://pay.youyuanxin.top/zfb_pay/PerPays.php";
    public static final String QUERYURL = "http://sel.youyuanxin.top:8168/synch/orderstatus.asp";

    public static Map<String, Object> getQrCode(JjsmPayParamBean payParam) {
    	Map<String, Object> result = new HashMap<String, Object>();
		Gson gson = new Gson();
        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
        logger.info(rest);
		JjsmPayResultBean reMsg = gson.fromJson(rest, JjsmPayResultBean.class);
        if ("success".equals(reMsg.getStatus()) && reMsg.getCode_url()!= null) {
        	result.put("code", 1);
        	result.put("data", reMsg.getCode_url());
        } else {
        	result.put("code", 2);
        	result.put("data", "");
        }
        
//        result.put("passageway_order_number", reMsg.getMchtOrderId());
        result.put("msg", reMsg.getStatus());
        return result;
//        String rest = JsonHttpClientUtils.httpPostform(PAYURL, payParam.hasSignParamMap(), "UTF-8"); 	//返回json串
	}

//
//	public static SearchPayBo queryPayInfo(HJPayQueryParamBean queryParam) {
//		Gson gson = new Gson();
//	    String rest = JsonHttpClientUtils.httpsPostform(QUERYURL, queryParam.hasSignParamMap(), "UTF-8"); 	//返回json串
//	    HJPayQueryResultBean queryBean = null;
//	    SearchPayBo searchPayBo = new SearchPayBo();
//	    try {
//	    	queryBean = (HJPayQueryResultBean) gson.fromJson(rest, HJPayQueryResultBean.class);
//	    	logger.info("获取成功：" + rest);
//	    	if (queryBean.getCode() == 1 && queryBean.getData() != null) {
//	    		HJPayQueryResultBean.ResultData data = queryBean.getData();
//	    		if ("2".equals(data.getStatus())) {
//	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
//	    			DecimalFormat df = new DecimalFormat("#0.00");
//	    			BigDecimal money = new BigDecimal(data.getCash());
//		    		searchPayBo.setMoney(df.format(money));
//	    		} else if ("0".equals(data.getStatus())) {
//	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
//	    		} else {
//	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
//	    		}
//
//	    	}else {
//	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
//	    	}
//	    	searchPayBo.setAccount_number(queryParam.getMerchant_number());
//	    	searchPayBo.setMsg(queryBean.getMsg());
//	    	searchPayBo.setOrder_number(queryParam.getOrder_id());
//	    } catch(Exception e) {
//	    	e.printStackTrace();
//	    	logger.info("返回格式异常：" + rest);
//	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
//	    	searchPayBo.setMsg("返回信息异常");
//	    	searchPayBo.setOrder_number(queryParam.getOrder_id());
//	    }
//	    return searchPayBo;
//	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		JjsmPayParamBean payParam = new JjsmPayParamBean();
		
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		payParam.setFee(new BigDecimal(authentication.getPayment()).multiply(new BigDecimal(100)).intValue());
		payParam.setMchid(usePayment.getAccount_number());
		payParam.setOrder_sn(authentication.getPlatform_order_number());
		payParam.setCreate_ip("68.192.12.1");
		payParam.setKey(usePayment.getAccount_key());
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}

}
