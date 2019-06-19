package com.boye.api;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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
import com.boye.common.http.pay.YMDPayParamBean;
import com.boye.common.http.query.AMWYQueryBalanceParamBean;
import com.boye.common.http.query.AMWYQueryBalanceResultBean;
import com.boye.common.http.query.AMWYQueryParamBean;
import com.boye.common.http.query.AMWYQueryResultBean;
import com.boye.common.http.query.YMDSubQueryParamBean;
import com.boye.common.http.subpay.YMDSubParamBean;
import com.boye.common.http.subpay.YMDSubParamBean.TransferList;
import com.boye.common.utils.APISecurityUtils;
import com.boye.common.utils.Base64Utils;
import com.boye.common.utils.JsonHttpClientUtils;
import com.boye.common.utils.XMLHttpClientUtils;
import com.boye.common.utils.XMLUtils;
import com.google.gson.Gson;

public class YMDPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(YMDPayApi.class);

    public static final String PAYURL = "https://gwapi.yemadai.com/pay/sslpayment";
    
    public static final String QUERYURL = "https://gwapi.yemadai.com/merchantBatchQueryAPI";

    public static final String SUBURL = "https://gwapi.yemadai.com/transfer/transferFixed";
    
    public static final String SUBQUERYURL = "https://gwapi.yemadai.com/transfer/transferQueryFixed";

    public static final String BALANCEURL = "https://gwapi.yemadai.com/checkBalance";

//    @SuppressWarnings("unchecked")
	public static String getQrCode(YMDPayParamBean payParam) {
//    	Map<String, Object> result = new HashMap<String, Object>();
//    	Gson gson = new Gson();
        String rest =JsonHttpClientUtils.httpsPostform(PAYURL, payParam.returnParamMap(), "UTF-8"); 	//返回json串
//        System.out.println(rest);
//        Map<String, Object> resMap;
//		try {
//			resMap = XMLUtils.xml2map(rest, false);
//			String resStr = gson.toJson(resMap);
//			YMDPayResultBean resBean = gson.fromJson(resStr, YMDPayResultBean.class);
//	        if (resBean.getSucceed().equals("88")) {
//        		result.put("code", "1");
//            	result.put("data", resBean.getResult());
//	        } else {
//	        	result.put("code", "2");
//	        	result.put("data", "");
//	        }
//	        result.put("msg", resBean.getResult() != null ? resBean.getResult() : "远程服务响应异常");
//		} catch (DocumentException e) {
//			result.put("code", "2");
//        	result.put("data", "");
//        	result.put("msg", "远程服务响应异常");
//        	
//		}
        return rest;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String, Object> sendSubInfo(YMDSubParamBean subParam) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		Gson gson = new Gson();
		System.out.println(XMLUtils.convertToXml(subParam));
		String xmlContent = null;
		try {
			xmlContent = new String(Base64Utils.encode(XMLUtils.convertToXml(subParam).getBytes("utf-8")));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("transData", xmlContent);
		System.out.println(xmlContent);
        String rest = JsonHttpClientUtils.httpsPostform(SUBURL, param, "UTF-8"); 	//返回json串
        Map<String, Object> resMap;
        try {
        	rest = new String(Base64Utils.decode(rest.getBytes()));
        	System.out.println(rest);
        	resMap = XMLUtils.xml2map(rest, false);
			String resStr = gson.toJson(resMap);
			System.out.println(resStr);
			if (resMap.get("errCode").equals("0000")) {
				 Map<String, Object> transfer = (Map<String, Object>) resMap.get("transferList");
				 if (transfer.get("resCode").equals("0000")) {
					resultMap.put("msg", "请求成功");
		        	resultMap.put("code", "1");
				 } else {
					resultMap.put("msg", "请求失败");
			        resultMap.put("code", "2");
				 } 
        	} else {
        		resultMap.put("msg", "请求失败");
        		resultMap.put("code", "2");
        	}
        	return resultMap;
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

	public static String getQrCode(QuickAuthenticationInfo authentication, PaymentAccount usePayment, PassagewayInfo passagewayInfo, String key) { 
		if (usePayment == null) return null;
		YMDPayParamBean payParam = new YMDPayParamBean();
		payParam.setAdviceURL("http://361paytest.361fit.com/api/pay/textReturnSite");
		payParam.setAmount("10.00");
		payParam.setBankCardNo("");
		payParam.setBillNo("0000011111122222000002");
		payParam.setDefaultBankNumber("");
		payParam.setMerNo("46791");
		SimpleDateFormat sf = new SimpleDateFormat("YYYYMMDDHHmmss");
		payParam.setOrderTime(sf.format(new Date()));
		payParam.setPayType("B2B");
		payParam.setProducts("");
		payParam.setRemark("361pay");
		payParam.setReturnURL("http://361paytest.361fit.com/api/pay/textReturnSite");
		payParam.setSignInfo(payParam.generateSign(key));
		return getQrCode(payParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecord extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
//		DecimalFormat df = new DecimalFormat("#0.00");
		YMDSubParamBean subParam = new YMDSubParamBean();
		return sendSubInfo(subParam);
	}
	
	public static Map<String, Object> sendSubInfo(ExtractionRecordForAgent extraction, SubstituteAccount usePayment, PassagewayInfo passagewayInfo) { 
		if (usePayment == null) return null;
		YMDSubParamBean subParam = new YMDSubParamBean();
		return sendSubInfo(subParam);
	}

	@SuppressWarnings("unchecked")
	public static SearchPayBo subInfoQuery(YMDSubQueryParamBean queryParam) {
		Gson gson = new Gson();
		System.out.println(XMLUtils.convertToXml(queryParam));
		String xmlContent = null;
		try {
			xmlContent = new String(Base64Utils.encode(XMLUtils.convertToXml(queryParam).getBytes("utf-8")));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Map<String, Object> param = new HashMap<String, Object>();
		param.put("requestDomain", xmlContent);
        String rest = JsonHttpClientUtils.httpsPostform(SUBQUERYURL, param, "UTF-8"); 	//返回json串
        Map<String, Object> resMap;
	    SearchPayBo searchPayBo = new SearchPayBo();
	    try {
	    	rest = new String(Base64Utils.decode(rest.getBytes()));
        	System.out.println(rest);
        	resMap = XMLUtils.xml2map(rest, false);
			String resStr = gson.toJson(resMap);
			System.out.println(resStr);
			if (resMap.get("code").equals("0000")) {
				 Map<String, Object> transfer = (Map<String, Object>) resMap.get("transfer");
				 if (transfer.get("state").equals("00")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
	    			DecimalFormat df = new DecimalFormat("#0.00");
	    			BigDecimal money = new BigDecimal(transfer.get("amount").toString());
		    		searchPayBo.setMoney(df.format(money));
	    		} else if (transfer.get("state").equals("11")) {
	    			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
	    		} else {
	    			searchPayBo.setState(SearchPayStatus.GETFAIL);
	    		}
	    		searchPayBo.setMsg(transfer.get("memo").toString());
	    	}else {
	    		searchPayBo.setMsg("查询失败");
	    		searchPayBo.setState(SearchPayStatus.GETFAIL);
	    	}
	    	searchPayBo.setAccount_number(queryParam.getMerchantNumber());
	    	searchPayBo.setOrder_number(queryParam.getMertransferID());
	    } catch(Exception e) {
	    	logger.info("返回格式异常：" + rest);
	    	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
	    	searchPayBo.setMsg("返回信息异常");
	    }
	    return searchPayBo;
	}

	public static SearchPayBo subInfoQuery(String extraction_number, SubstituteAccount substitute) {
		YMDSubQueryParamBean queryParam = new YMDSubQueryParamBean();
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
			PassagewayInfo passageway, String subKey) {
		YMDSubParamBean subParam = new YMDSubParamBean();
		
		subParam.setAccountNumber(useSubstitute.getAccount_number());
		subParam.setNotifyURL(passageway.getNotify_url());
		subParam.setSignType("PAS");
		subParam.setTt(passageway.getPay_type());
		YMDSubParamBean.TransferList ymdTrans = new YMDSubParamBean.TransferList();
		ymdTrans.setTransId(subPaymentInfo.getSub_payment_number());
		ymdTrans.setBankCode(subPaymentInfo.getBank_name());
		ymdTrans.setProvice(subPaymentInfo.getRegist_bank().split(",")[0]);
		ymdTrans.setCity(subPaymentInfo.getRegist_bank().split(",")[1]);
		ymdTrans.setBranchName(subPaymentInfo.getRegist_bank_name());
		ymdTrans.setAccountName(subPaymentInfo.getCard_user_name());
		ymdTrans.setCardNo(subPaymentInfo.getBank_card_number());
//		ymdTrans.setIdNo(idNo);
//		ymdTrans.setPhone(phone);
		DecimalFormat df = new DecimalFormat("#0.00");
		ymdTrans.setAmount(df.format(subPaymentInfo.getSub_money()));
		ymdTrans.setRemark("");
		subParam.setTransferList(new ArrayList<TransferList>());
		subParam.getTransferList().add(ymdTrans);
		ymdTrans.setSecureCode(subParam.generateSign(subKey));

//		BankCodeEnum bankCode = BankCodeEnum.key(subPaymentInfo.getBank_name());
//		if (bankCode == null) return null;
//		String xmlstr = XMLUtils.convertToXml(dataEncode);
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
