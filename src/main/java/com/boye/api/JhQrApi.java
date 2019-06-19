package com.boye.api;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.pay.JhResultBean;
import com.boye.common.utils.HttpClientUtil;
import com.boye.common.utils.MD5;
import com.google.gson.Gson;

public class JhQrApi {
	
	public static Map<String, Object> getQrCode(AuthenticationInfo authentication, PaymentAccount usePayment){
		String bankURL="https://ibsbjstar.ccb.com.cn/CCBIS/ccbMain";
        Map<String, Object> sendParam = new HashMap<String, Object>();
		//传输参数，F为空值也需要写入参数名的参数，C为可选参数
		//商户代码 		F
		sendParam.put("MERCHANTID", usePayment.getAccount_number());
		//商户柜台代码	F
		sendParam.put("POSID", usePayment.getCounter_number());
		//分行代码		F
		sendParam.put("BRANCHID", "330000000");
		//定单号			F
		sendParam.put("ORDERID", authentication.getPlatform_order_number());
		System.out.println(authentication.getOrder_number());
		//付款金额		F
		sendParam.put("PAYMENT", authentication.getPayment());
		//币种			F
		sendParam.put("CURCODE", "01");
		//备注1			C
		sendParam.put("REMARK1", "");
		//备注2			C
		sendParam.put("REMARK2", "");
		//交易码			F
		sendParam.put("TXCODE", "530550");
		//订单超时时间	C
		sendParam.put("TIMEOUT", "");
		//返回类型		C
		sendParam.put("RETURNTYPE", "3");
		
		//-----------------校验信息-------------------
		
		//MAC校验域		F
		Map<String, Object> result = new HashMap<String, Object>();
		sendParam.put("MAC", getMac(sendParam, usePayment));
		String findUrl = getUrl(bankURL, sendParam);
        String ret = HttpClientUtil.httpGet(findUrl, "UTF-8"); 	//请求二维码生成链接串
        Gson gson = new Gson();
        JhResultBean qrurl = null;
        
        try {
        	qrurl = (JhResultBean) gson.fromJson(ret, JhResultBean.class);
        	if (qrurl.getSUCCESS().equals("true")) {
        		ret = HttpClientUtil.httpGet(qrurl.getPAYURL(), "UTF-8"); //获取二维码串
            	qrurl = (JhResultBean) gson.fromJson(ret, JhResultBean.class);
            	if (qrurl.getSUCCESS().equals("true")) result.put("code", 1);
            	else result.put("code", 2);
            	result.put("data", URLDecoder.decode(qrurl.getQRURL(), "UTF-8"));
            	result.put("msg","获取成功");
        	} else {
        		result.put("code",2);
            	result.put("data", "");
            	result.put("msg","远程服务响应异常");
        	}
        } catch(Exception e) {
        	result.put("code",2);
        	result.put("data", "");
        	result.put("msg","远程服务响应异常");
        }
		return result;
	}

	private static String getUrl(String bankURL, Map<String, Object> sendParam) {
		StringBuffer result = new StringBuffer();
		result.append(bankURL + "?CCB_IBSVersion=V6&");
		result.append("MERCHANTID="+sendParam.get("MERCHANTID")+"&POSID="+sendParam.get("POSID")+"&BRANCHID="+sendParam.get("BRANCHID")+"&ORDERID="+sendParam.get("ORDERID"));
		result.append("&PAYMENT="+sendParam.get("PAYMENT")+"&CURCODE="+sendParam.get("CURCODE")+"&TXCODE="+sendParam.get("TXCODE")+"&REMARK1="+sendParam.get("REMARK1"));
		result.append("&REMARK2="+sendParam.get("REMARK2")+"&RETURNTYPE="+sendParam.get("RETURNTYPE")+"&TIMEOUT="+sendParam.get("TIMEOUT"));
		result.append("&MAC="+sendParam.get("MAC"));
		return result.toString();
	}

	private static String getMac(Map<String, Object> sendParam, PaymentAccount usePayment) {
		StringBuffer tmp = new StringBuffer(); //验签字段
	    tmp.append("MERCHANTID=");
	    tmp.append(sendParam.get("MERCHANTID"));
	    tmp.append("&POSID=");
	    tmp.append(sendParam.get("POSID"));
	    tmp.append("&BRANCHID=");
	    tmp.append(sendParam.get("BRANCHID"));
	    tmp.append("&ORDERID=");
	    tmp.append(sendParam.get("ORDERID"));
	    tmp.append("&PAYMENT=");
	    tmp.append(sendParam.get("PAYMENT"));
	    tmp.append("&CURCODE=");
	    tmp.append(sendParam.get("CURCODE"));
	    tmp.append("&TXCODE=");
	    tmp.append(sendParam.get("TXCODE"));
	    tmp.append("&REMARK1=");
	    tmp.append(sendParam.get("REMARK1"));
	    tmp.append("&REMARK2=");
	    tmp.append(sendParam.get("REMARK2"));
	    tmp.append("&RETURNTYPE=");
	    tmp.append(sendParam.get("RETURNTYPE"));
	    tmp.append("&TIMEOUT=");
	    tmp.append(sendParam.get("TIMEOUT"));
	    tmp.append("&PUB=");
	    tmp.append(usePayment.getAccount_key());
	    System.out.println(tmp);
		return MD5.md5Str(tmp.toString());
	}

	public static String getQrCode() {
		// TODO Auto-generated method stub
		return null;
	}

	public static String sendRefundRequest(Map<String, Object> sendAuthenticationParam) {
		// TODO Auto-generated method stub
		return null;
	}
}
