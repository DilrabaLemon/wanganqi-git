package com.boye.api;

import java.util.HashMap;
import java.util.Map;

import com.boye.common.utils.JsonHttpClientUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.pay.H5PayParamBean;
import com.boye.common.http.pay.H5ResultBean;
import com.boye.common.http.query.H5QueryParamBean;
import com.boye.common.http.query.H5QueryResultBean;
import com.boye.common.utils.HttpClientUtil;
import com.google.gson.Gson;

public class H5PayApi {
	
	private static Logger logger = LoggerFactory.getLogger(H5PayApi.class);
	
	public static final String PAYURL = "https://jifu.szsdfm.com/api/submit.php";
	public static final String QUERYURL = "https://jifu.szsdfm.com/api/api.php";
	
	public static Map<String, Object> getQrCode(H5PayParamBean payParam) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 修改请求为post移除。20181201
		String findUrl =  PAYURL + "?" + payParam.hasSignParam();
        String rest = HttpClientUtil.httpGet(findUrl, "UTF-8"); 	//返回json串
//		Gson paygson = new Gson();
//		//打印串
//		String payjson = paygson.toJson(payParam.hasSignParamMap());
//		System.out.println(payjson);
//		//发送请求
//		String rest = HttpClientUtil.httpPost(PAYURL,payParam.hasSignParamMap() ,"UTF-8");
        System.out.println(rest);
		Gson gson = new Gson();
	    H5ResultBean qrurl = null;
	    try {
	    	qrurl = (H5ResultBean) gson.fromJson(rest, H5ResultBean.class);
	    	result.put("code", qrurl.getCode());
	    	result.put("msg", qrurl.getMsg());
	    	result.put("data", qrurl.getPayurl());
	    } catch(Exception e) {
	    	result.put("code", 2);
	    	result.put("msg", "远程服务响应异常");
	    	result.put("data", "");
	    }
	    return result;
	}

	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		H5PayParamBean payParam = new H5PayParamBean();
		payParam.setMd5_key(usePayment.getAccount_key());
		payParam.setMoney(authentication.getPayment());
		payParam.setName(authentication.getOrder_number());//H5可以通过商品名称查询
		payParam.setNotify_url(passagewayInfo.getNotify_url());
		payParam.setOut_trade_no(authentication.getPlatform_order_number());
		payParam.setPid(usePayment.getAccount_number());
		payParam.setReturn_url(passagewayInfo.getReturn_url());
		payParam.setSign_type("MD5");
		payParam.setSitename("361pay");
		payParam.setFormat("json");
		payParam.setType(passagewayInfo.getPay_type());
		payParam.setSign(payParam.generateSign());
		return getQrCode(payParam);
	}
	
	public static SearchPayBo queryPayInfo(H5QueryParamBean queryParam) {
		String findUrl =  QUERYURL + "?" + queryParam.hasSignParam();
        String rest = HttpClientUtil.httpGet(findUrl, "UTF-8"); 	//返回json串
        System.out.println(rest);
		Gson gson = new Gson();
	    H5QueryResultBean queryBean = null;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	queryBean = (H5QueryResultBean) gson.fromJson(rest, H5QueryResultBean.class);
	    	logger.info("获取成功：" + rest);
	    	if (queryBean.getCode().equals(1)) {
	    		if (queryBean.getStatus().equals("1")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
		    		searchPayBo.setMoney(queryBean.getMoney());
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    	}
	    	
	    	searchPayBo.setAccount_number(queryBean.getPid());
	    	searchPayBo.setMsg(queryBean.getMsg());
	    	searchPayBo.setOrder_number(queryBean.getOut_trade_no());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
        	searchPayBo.setMsg("返回信息异常");
        	searchPayBo.setOrder_number(queryParam.getOut_trade_no());
	    }
	    return searchPayBo;
	}
	
}
