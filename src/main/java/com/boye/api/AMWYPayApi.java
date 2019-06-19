package com.boye.api;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.entity.ExtractionRecordForAgent;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.entity.SubstituteAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.bean.vo.QuickAuthenticationInfo;
import com.boye.common.enums.BankCodeEnum;
import com.boye.common.http.pay.AMWYPayParamBean;
import com.boye.common.http.pay.AMWYPayResultBean;
import com.boye.common.http.query.AMWYQueryBalanceParamBean;
import com.boye.common.http.query.AMWYQueryBalanceResultBean;
import com.boye.common.http.query.AMWYQueryParamBean;
import com.boye.common.http.query.AMWYQueryResultBean;
import com.boye.common.http.query.AMWYSubQueryParamBean;
import com.boye.common.http.query.AMWYSubQueryResultBean;
import com.boye.common.http.subpay.AMWYSubParamBean;
import com.boye.common.http.subpay.AMWYSubResultBean;
import com.boye.common.utils.APISecurityUtils;
import com.boye.common.utils.Base64Utils;
import com.boye.common.utils.Matchers;
import com.boye.common.utils.XMLHttpClientUtils;
import com.boye.common.utils.XMLUtils;
import com.google.gson.Gson;

public class AMWYPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(AMWYPayApi.class);

    public static final String PAYURL = "http://pay.bulafosi.com/payserver/x2xpay/doRequest.action";

    public static final String SUBURL = "http://150.109.60.221:7780/transferAdmin/x2xtsfpay/doTransferApi.action";
    
    public static final String SUBQUERYURL = "http://150.109.60.221:7780/transferAdmin/x2xtsfpay/doTransferApi.action";

    public static final String QUERYURL = "http://pay.bulafosi.com/payserver/x2xpay/doRequest.action";
    
    public static final String BALANCEURL = "http://150.109.60.221:7780/transferAdmin/x2xtsfpay/doTransferApi.action";

    @SuppressWarnings("unchecked")
	public static Map<String, Object> getQrCode(AMWYPayParamBean payParam) {
    	Map<String, Object> result = new HashMap<String, Object>();
		String xmlContent = XMLUtils.convertToXml(payParam);
		System.out.println(xmlContent);
        String rest = XMLHttpClientUtils.httpsPost(PAYURL, xmlContent, "UTF-8"); 	//返回json串
        Map<String, Object> resMap;
		try {
			resMap = XMLUtils.xml2map(rest, false);
			Gson gson = new Gson();
			String resStr = gson.toJson(resMap);
			AMWYPayResultBean resBean = gson.fromJson(resStr, AMWYPayResultBean.class);
	        if (resBean.getHead() != null && resBean.getHead().getRespCd().equals("0000")) {
        		result.put("code", "1");
            	result.put("data", resBean.getData().getPay_url());
	        } else {
	        	result.put("code", "2");
	        	result.put("data", "");
	        }
	        result.put("msg", resBean.getHead() != null ? resBean.getHead().getRespMsg() : "远程服务响应异常");
		} catch (DocumentException e) {
			result.put("code", "2");
        	result.put("data", "");
        	result.put("msg", "远程服务响应异常");
        	
		}
        return result;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> sendSubInfo(AMWYSubParamBean subParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		String xmlContent = XMLUtils.convertToXml(subParam);
		System.out.println(xmlContent);
        String rest = XMLHttpClientUtils.httpsPost(SUBURL, xmlContent, "UTF-8"); 	//返回json串
        Map<String, Object> resMap;
        try {
        	resMap = XMLUtils.xml2map(rest, false);
			Gson gson = new Gson();
			String resStr = gson.toJson(resMap);
			AMWYSubResultBean resBean = gson.fromJson(resStr, AMWYSubResultBean.class);
			if (resBean.getHead() != null && resBean.getHead().getRespCd().equals("0000")) {
				resultMap.put("msg", resBean.getData().getdInfo());
        		resultMap.put("code", "1");
        	} else {
        		resultMap.put("msg", resBean.getHead().getRespMsg());
        		resultMap.put("code", "2");
        	}
        	
        	resultMap.put("data", "");
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}
	
	@SuppressWarnings("unchecked")
	public static SearchPayBo queryPayInfo(AMWYQueryParamBean queryParam) {
		String xmlContent = XMLUtils.convertToXml(queryParam);
		System.out.println(xmlContent);
        String rest = XMLHttpClientUtils.httpsPost(QUERYURL, xmlContent, "UTF-8"); 	//返回json串
        Map<String, Object> resMap;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	resMap = XMLUtils.xml2map(rest, false);
	    	Gson gson = new Gson();
			String resStr = gson.toJson(resMap);
			AMWYQueryResultBean resBean = gson.fromJson(resStr, AMWYQueryResultBean.class);
	    	logger.info("获取成功：" + resStr);
	    	if (resBean.getHead() != null && resBean.getHead().getRespCd().equals("0000")) {
	    		if (resBean.getData() != null && !resBean.getData().getTotal_fee().equals(0L)) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(resBean.getData().getTotal_fee());
		    		searchPayBo.setMoney(df.format(money));
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		
	    	}else {
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number("");
	    	searchPayBo.setMsg(queryParam.getHead().getMchid());
	    	searchPayBo.setOrder_number(queryParam.getData().getOri_seq());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    	searchPayBo.setOrder_number(queryParam.getData().getOri_seq());
	    }
	    return searchPayBo;
	}

	public static Map<String, Object> getQrCode(QuickAuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo, String key) { 
		if (usePayment == null) return null;
		AMWYPayParamBean payParam = new AMWYPayParamBean();
		payParam.setData(new AMWYPayParamBean.DataContent());
		payParam.setHead(new AMWYPayParamBean.HeadContent());
//		payParam.getData().setBiz_content(biz_content);
//		payParam.getData().setDevice_ip("223.93.166.117");
		payParam.getData().setPaytype("1");
		payParam.getData().setSubject(authentication.getOrder_number());
		payParam.getData().setTotal_fee(new BigDecimal(authentication.getPayment()).multiply(BigDecimal.valueOf(100)).longValue());
		payParam.getHead().setAppId("DEFAULT");
		payParam.getHead().setBackURL(passagewayInfo.getNotify_url());
		payParam.getHead().setChannel(passagewayInfo.getPay_type());
//		payParam.getHead().setClientIp("223.93.166.117");
		payParam.getHead().setFrontURL(passagewayInfo.getReturn_url());
		payParam.getHead().setMchid(usePayment.getAccount_number());
//		payParam.getHead().setProxyMchid("177800000004412");
		payParam.getHead().setReqNo(authentication.getPlatform_order_number());
		payParam.getHead().setReqType("h5_pay_request");
		payParam.getHead().setSignType("RSA1");
		payParam.getHead().setVersion("1.0");
		payParam.getHead().setSign(payParam.generateSign(key));
		return getQrCode(payParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
//		DecimalFormat df = new DecimalFormat("#0.00");
		AMWYSubParamBean subParam = new AMWYSubParamBean();
		return sendSubInfo(subParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		AMWYSubParamBean subParam = new AMWYSubParamBean();
		return sendSubInfo(subParam);
	}

	@SuppressWarnings("unchecked")
	public static SearchPayBo subInfoQuery(AMWYSubQueryParamBean queryParam) {
		Map<String, Object> result = new HashMap<String, Object>();
		String xmlContent = XMLUtils.convertToXml(queryParam);
		System.out.println(xmlContent);
        String rest = XMLHttpClientUtils.httpsPost(SUBQUERYURL, xmlContent, "UTF-8"); 	//返回json串
        Map<String, Object> resMap;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	resMap = XMLUtils.xml2map(rest, false);
			Gson gson = new Gson();
			String resStr = gson.toJson(resMap);
			AMWYSubQueryResultBean resBean = gson.fromJson(resStr, AMWYSubQueryResultBean.class);
	    	logger.info("获取成功");
	    	if (resBean.getHead() != null && resBean.getHead().getRespCd().equals("0000")) {
	    		if (resBean.getData().getdStatus().equals("2")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(resBean.getData().getMoney());
		    		searchPayBo.setMoney(df.format(money));
	    		} else if (resBean.getData().getdStatus().equals("3")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		searchPayBo.setMsg(resBean.getData().getdInfo());
	    	}else {
	    		searchPayBo.setMsg(resBean.getHead().getRespMsg());
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryParam.getHead().getMchid());
	    	searchPayBo.setOrder_number(resBean.getHead().getReqNo());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    }
	    return searchPayBo;
	}

	public static SearchPayBo subInfoQuery(String extraction_number, SubstituteAccount substitute) {
		AMWYSubQueryParamBean queryParam = new AMWYSubQueryParamBean();
		return subInfoQuery(queryParam);
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> queryBalance(AMWYQueryBalanceParamBean queryBean) {
		String xmlContent = XMLUtils.convertToXml(queryBean);
		System.out.println(xmlContent);
        String rest = XMLHttpClientUtils.httpsPost(BALANCEURL, xmlContent, "UTF-8"); 	//返回json串
        Map<String, Object> resMap;
		Map<String, Object> resultMap = new HashMap<String, Object>();
        try {
        	resMap = XMLUtils.xml2map(rest, false);
			Gson gson = new Gson();
			String resStr = gson.toJson(resMap);
			AMWYQueryBalanceResultBean resBean = gson.fromJson(resStr, AMWYQueryBalanceResultBean.class);
			  if (resBean.getHead() != null && resBean.getHead().getRespCd().equals("0000")) {
        		resultMap.put("code", "1");
        		resultMap.put("data", resBean.getData().getAllAmt());
        	} else {
        		resultMap.put("code", "2");
        	}
        	resultMap.put("msg", resBean.getHead().getRespMsg());
        } catch(Exception e) {
        	resultMap.put("code","2");
        	resultMap.put("msg", "远程服务响应异常");
        	resultMap.put("data", "");
        }
	    return resultMap;
	}

	public static Map<String, Object> sendSubInfo(SubPaymentInfo subPaymentInfo, SubstituteAccount useSubstitute,
			PassagewayInfo passageway, String priKey, String subKey) {
		AMWYSubParamBean subParam = new AMWYSubParamBean();
		AMWYSubParamBean.DataContent data = new AMWYSubParamBean.DataContent();
		subParam.setHead(new AMWYSubParamBean.HeadContent());
		AMWYSubParamBean.SendBean dataEncode = new AMWYSubParamBean.SendBean();

		BankCodeEnum bankCode = BankCodeEnum.key(subPaymentInfo.getBank_name());
		if (bankCode == null) return null;
		data.setBankCode(bankCode.getCode());
		data.setBankName(subPaymentInfo.getBank_name());
		data.setBankSubbranch(subPaymentInfo.getRegist_bank_name());
		data.setCity(subPaymentInfo.getRegist_bank().split(",")[1]);
		data.setOrderId(getAMWYOrderId(subPaymentInfo.getId()));
		data.setProp("0");
		data.setProvince(subPaymentInfo.getRegist_bank().split(",")[0]);
		data.setWalletId(useSubstitute.getCounter_number());
		data.setAccountName(subPaymentInfo.getCard_user_name());
		data.setBankAccount(subPaymentInfo.getBank_card_number());
		data.setMoney(subPaymentInfo.getSub_money().multiply(BigDecimal.valueOf(100)).longValue());
		subParam.getHead().setAppId("1001");
		subParam.getHead().setBackURL(passageway.getNotify_url());
		subParam.getHead().setMchid(useSubstitute.getAccount_number());
		subParam.getHead().setReqNo(subPaymentInfo.getSub_payment_number());
		subParam.getHead().setReqType("transfer_record_request");
		subParam.getHead().setSignType("RSA1");
		subParam.getHead().setVersion("1.0");
		dataEncode.setData(data);
		dataEncode.setHead(subParam.getHead());
		String xmlstr = XMLUtils.convertToXml(dataEncode);
		subParam.getHead().setSign(subParam.generateSign(data, priKey));
		try {
			String encodedata=encodeData(Matchers.match("<data>(.*)</data>", xmlstr.replace("\n", "").replace("\t", "")), useSubstitute.getAccount_number(), subKey);
			subParam.setData(encodedata);
		} catch (Exception e) {
			logger.info("not sign param data");
		}
		return sendSubInfo(subParam);
	}
	
	/**
	 * 加密
	 * @param data
	 * @param mchid
	 * @return
	 * @throws Exception 
	 */
	public static String encodeData(String dataxml,String mchid, String subKey) throws Exception{
		if(StringUtils.isBlank(dataxml))return "";
		try {
			byte[] datastr = APISecurityUtils.encryptByPublicKey(dataxml.toString().getBytes("UTF-8"), subKey);
			return new String(Base64Utils.encode(datastr),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("加密失败");
		}
	}
	
	private static String getAMWYOrderId(Long id) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String idStr = id.toString();
		StringBuffer sb = new StringBuffer();
		sb.append(sf.format(now));
		for (int i = 0 ; i < (10-idStr.length()) ; i++) {
			sb.append("0");
		}
		sb.append(idStr);
		return sb.toString();
	}
}
