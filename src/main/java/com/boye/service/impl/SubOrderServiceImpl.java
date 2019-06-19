package com.boye.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.api.AMWYPayApi;
import com.boye.api.CallBackApi;
import com.boye.api.HhlQuickPayApi;
import com.boye.api.PinAnApi;
import com.boye.api.YMDPayApi;
import com.boye.api.YouPayApi;
import com.boye.base.constant.EncryptionUtils;
import com.boye.bean.bo.CpSubPaymentBo;
import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.CpSubPaymentInfo;
import com.boye.bean.entity.CpSubPaymentInfoFail;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.entity.SubstituteAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.bean.vo.Page;
import com.boye.common.http.query.AMWYSubQueryParamBean;
import com.boye.common.http.query.HhlQuickSubQueryParamBean;
import com.boye.common.http.query.TranInfoQueryBean;
import com.boye.common.http.query.YMDSubQueryParamBean;
import com.boye.common.http.query.YouPaySubQueryParamBean;
import com.boye.common.utils.APISecurityUtils;
import com.boye.common.utils.Base64Utils;
import com.boye.common.utils.CommonUtils;
import com.boye.common.utils.Matchers;
import com.boye.common.utils.XMLUtils;
import com.boye.config.ServerConfigurer;
import com.boye.dao.CpSubPaymentInfoDao;
import com.boye.dao.CpSubPaymentInfoFailDao;
import com.boye.dao.PassagewayDao;
import com.boye.dao.SubPaymentInfoDao;
import com.boye.dao.SubPaymentKeyBoxDao;
import com.boye.dao.SubstituteDao;
import com.boye.service.IProvideService;
import com.boye.service.IShopUserService;
import com.boye.service.ITaskService;
import com.boye.service.SubOrderService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class SubOrderServiceImpl  extends BaseServiceImpl implements SubOrderService{
	
	private static Logger logger = LoggerFactory.getLogger(ExtractionServiceImpl.class);
	
	@Autowired
	private IShopUserService shopUserService;
	
	@Autowired
	private IProvideService provideService;
	
	@Autowired
	private SubPaymentInfoDao subPaymentInfoDao;
	
	@Autowired
	private CpSubPaymentInfoFailDao cpSubPaymentInfoFailDao;
	
	@Autowired
	private SubstituteDao substituteDao;
	
	@Autowired
	private PassagewayDao passagewayDao;
	
	@Autowired
	private SubPaymentKeyBoxDao keyBoxDao;
	
	@Autowired
	private CpSubPaymentInfoDao cpSubPaymentInfoDao;
	
	@Autowired
	private ITaskService taskService;
	
    @Autowired
    private ServerConfigurer serverConf;
    
    private String resultMsg;

	@Override
	public Page<SubPaymentInfo> subPaymentInfoList(Map<String, Object> query) {
		Page<SubPaymentInfo> page = new Page<SubPaymentInfo>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		int count = subPaymentInfoDao.getSubPaymentInfoListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<SubPaymentInfo>());
        else {
        	page.setDatalist(subPaymentInfoDao.getSubPaymentInfoList(query));
        }
        return page;
	}
	
	
	@Override
	public Page<SubPaymentInfo> errorSubPaymentInfoList(Map<String, Object> query) {
		Page<SubPaymentInfo> page = new Page<SubPaymentInfo>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		int count = subPaymentInfoDao.getErrorSubPaymentInfoCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<SubPaymentInfo>());
        else {
        	page.setDatalist(subPaymentInfoDao.getErrorSubPaymentInfo(query));
        }
        return page;
	}

	@Override
	public Page<SubPaymentInfo> findSubPaymentInfoErrorList(Map<String, Object> query) {
		Page<SubPaymentInfo> page = new Page<SubPaymentInfo>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		int count = subPaymentInfoDao.findSubPaymentInfoErrorCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<SubPaymentInfo>());
        else {
        	page.setDatalist(subPaymentInfoDao.findSubPaymentInfoError(query));
        }
        return page;
	}

	@Override
	public Page<CpSubPaymentInfoFail> getSubPaymentInfoFailList(Map<String, Object> query) {
		Page<CpSubPaymentInfoFail> page = new Page<CpSubPaymentInfoFail>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		int count = cpSubPaymentInfoFailDao.getCpSubPaymentInfoFailCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<CpSubPaymentInfoFail>());
        else {
        	page.setDatalist(cpSubPaymentInfoFailDao.getCpSubPaymentInfoFail(query));
        }
        return page;
	}


	@Override
	public int querySubStateByPlatform(Long sub_id) {
		SubPaymentInfo subPayment = subPaymentInfoDao.getObjectById(new SubPaymentInfo(sub_id));
		if (subPayment == null || subPayment.getState() != 5) return -3;
		ProvideInfo provide = provideService.getProvideByPassagewayId(subPayment.getPassageway_id());
		SearchPayBo res = null;
		switch(provide.getProvide_code()) {
		case "amwydf":
			res = getAMWYSubQuick(subPayment);
			break;
		case "ymddf":
			res = getYMDSubQuick(subPayment);
			break;
		case "youpay":
			res = getYouPaySubQuick(subPayment);
			break;
		case "pinansubpay":
			res = getPinAnSubPay(subPayment);
			break;
		case "hhlysf":
			res = getHhlYsfSubPay(subPayment);
			break;
		case "test":
			res =  getTestSubQuick(subPayment);
			break;
		}
		if (res == null) return -1;
		if (res.getState().getCode() == 1) {
			logger.info("subOrder complate");
			if (taskService.sendSubPaymentSuccess(subPayment.getSub_payment_number()).equals("success"))return 1;
			logger.info("subOrder fail");
			return -6;
		} else if (res.getState().getCode() == -2) {
			logger.info("platform extraction fail");
			resultMsg = res.getMsg();
			taskService.sendSubPaymentFail(subPayment.getSub_payment_number());
		}
		resultMsg = res.getMsg();
		logger.info("platform extraction hang in the air");
		return -5;
	}
	
	private SearchPayBo getTestSubQuick(SubPaymentInfo subPayment) {
		SearchPayBo res = new SearchPayBo();
		res.setState(SearchPayStatus.GETSUCCESS);
		res.setMoney("0.00");
		res.setMsg("test");
		return res;
	}
	
	private SearchPayBo getYMDSubQuick(SubPaymentInfo subPayment) {
		YMDSubQueryParamBean queryParam = new YMDSubQueryParamBean();
		SubstituteAccount subAccount = substituteDao.getObjectById(new SubstituteAccount(subPayment.getSubstitute_id()));
		if (subAccount == null) return null;
		
		queryParam.setMerchantNumber(subAccount.getAccount_number());
		queryParam.setMertransferID(subPayment.getSub_payment_number());
//		queryParam.setQueryTimeBegin(queryTimeBegin);
//		queryParam.setQueryTimeEnd(queryTimeEnd);
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMDDhhmmss");
		queryParam.setRequestTime(sf.format(new Date()));
		queryParam.setSignType("RSA");
		queryParam.setSign(queryParam.generateSign(serverConf.getYmdSubPrivateKey()));
		return YMDPayApi.subInfoQuery(queryParam);
	}
	
	private SearchPayBo getPinAnSubPay(SubPaymentInfo subPayment) {
		SubstituteAccount substitute = substituteDao.getObjectById(new SubstituteAccount(subPayment.getSubstitute_id()));
		if (substitute == null) return null;
		substitute.setKeyBox(keyBoxDao.getKeyBoxByPaymentId(substitute.getId()));
		TranInfoQueryBean queryParam = new TranInfoQueryBean();
		SubstituteAccount subAccount = substituteDao.getObjectById(new SubstituteAccount(subPayment.getSubstitute_id()));
		if (subAccount == null) return null;
		
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String nowFormat = sdf.format(now);
		queryParam.setCnsmrSeqNo(subAccount.getCounter_number().split("-")[1] + nowFormat.substring(2, nowFormat.length()) + CommonUtils.getUserNumber(10));
		queryParam.setMrchCode(subAccount.getAccount_number());
		queryParam.setBussTypeNo("100157");
		queryParam.setCorpAgreementNo(subAccount.getCounter_number().split("-")[0]);
		queryParam.setRequestSeqNo(subPayment.getSub_payment_number());
		queryParam.setStartDate(nowFormat);
		queryParam.setEndDate(nowFormat);
		queryParam.setTranStatus("0");
		queryParam.setKey(subAccount.getAccount_key());
		queryParam.setSign(queryParam.generateSign());
		return PinAnApi.subInfoQuery(queryParam, serverConf.getPinAnServerUrl());
	}
	
	private SearchPayBo getHhlYsfSubPay(SubPaymentInfo subPayment) {
		SubstituteAccount substitute = substituteDao.getObjectById(new SubstituteAccount(subPayment.getSubstitute_id()));
		if (substitute == null) return null;
		substitute.setKeyBox(keyBoxDao.getKeyBoxByPaymentId(substitute.getId()));
		HhlQuickSubQueryParamBean queryParam = new HhlQuickSubQueryParamBean();
		SubstituteAccount subAccount = substituteDao.getObjectById(new SubstituteAccount(subPayment.getSubstitute_id()));
		if (subAccount == null) return null;
		
		queryParam.setCash_number(subPayment.getSub_payment_number());
		queryParam.setMerchant_open_id(subAccount.getAccount_number());
		queryParam.setTimestamp(new Date().getTime() + "");
		queryParam.setKey(subAccount.getAccount_key());
		queryParam.setSign(queryParam.generateSign());
		return HhlQuickPayApi.subInfoQuery(queryParam);
	}
	
	private SearchPayBo getYouPaySubQuick(SubPaymentInfo subPayment) {
		SubstituteAccount substitute = substituteDao.getObjectById(new SubstituteAccount(subPayment.getSubstitute_id()));
		if (substitute == null) return null;
		substitute.setKeyBox(keyBoxDao.getKeyBoxByPaymentId(substitute.getId()));
		YouPaySubQueryParamBean queryParam = new YouPaySubQueryParamBean();
		SubstituteAccount subAccount = substituteDao.getObjectById(new SubstituteAccount(subPayment.getSubstitute_id()));
		if (subAccount == null) return null;
		
		queryParam.setBrandNo(subAccount.getAccount_number());
		queryParam.setOrderNo(subPayment.getSub_payment_number());
		queryParam.setKey(substitute.getKeyBox().getPrivate_key());
		queryParam.setSignType("RSA-S");
		queryParam.setSignature(queryParam.generateSign());
		return YouPayApi.subInfoQuery(queryParam);
	}

	private SearchPayBo getAMWYSubQuick(SubPaymentInfo subPayment) {
		AMWYSubQueryParamBean queryParam = new AMWYSubQueryParamBean();
		queryParam.setHead(new AMWYSubQueryParamBean.HeadContent());
		AMWYSubQueryParamBean.DataContent data = new AMWYSubQueryParamBean.DataContent();
		AMWYSubQueryParamBean.SendBean dataEncode = new AMWYSubQueryParamBean.SendBean();
		SubstituteAccount subAccount = substituteDao.getObjectById(new SubstituteAccount(subPayment.getSubstitute_id()));
		if (subAccount == null) return null;
		data.setOrderId(getAMWYOrderId(subPayment.getId()));
		data.setSerialNum(subPayment.getSub_payment_number());
		
		queryParam.getHead().setAppId("1001");
		queryParam.getHead().setBackURL("");
		queryParam.getHead().setMchid(subAccount.getAccount_number());
		String code = UUID.randomUUID().toString().replaceAll("-", "");
		queryParam.getHead().setReqNo(code);
		queryParam.getHead().setReqType("transfer_query_order_request");
		queryParam.getHead().setSignType("RSA1");
		queryParam.getHead().setVersion("1.0");
		dataEncode.setData(data);
		dataEncode.setHead(queryParam.getHead());
		String xmlstr = XMLUtils.convertToXml(dataEncode);
		queryParam.getHead().setSign(queryParam.generateSign(data, serverConf.getAmwySubPrivateKey()));
		try {
			String encodedata=encodeData(Matchers.match("<data>(.*)</data>", xmlstr.replace("\n", "").replace("\t", "")), serverConf.getAccountNumberYsAMWY());
			queryParam.setData(encodedata);
		} catch (Exception e) {
			logger.info("not sign param data");
		}
		return AMWYPayApi.subInfoQuery(queryParam);
	}
	
	/**
	 * 加密
	 * @param data
	 * @param mchid
	 * @return
	 * @throws Exception 
	 */
	public String encodeData(String dataxml,String mchid) throws Exception{
		if(StringUtils.isBlank(dataxml))return "";
		String subMchPubKey=serverConf.getAmwySubPublicKey();
		try {
			byte[] datastr = APISecurityUtils.encryptByPublicKey(dataxml.toString().getBytes("UTF-8"), subMchPubKey);
			return new String(Base64Utils.encode(datastr),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("加密失败");
		}
	}
	
	private String getAMWYOrderId(Long id) {
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

	@Override
	public String getResultMsg() {
		return resultMsg;
	}


	@Override
	public int sendSubPaymentInfoToUser(Long cp_sub_id) {
		CpSubPaymentInfo cpSub = cpSubPaymentInfoDao.getObjectById(new CpSubPaymentInfo(cp_sub_id));
		String res = sendReturnMessage(cpSub).toUpperCase();
		if (res.equals("SUCCESS")) return 1;
		return 0;
	}
	
	private String sendReturnMessage(CpSubPaymentInfo cpSub) {
		String return_url = cpSub.getNotify_url();
		// 从数据库获取商户信息
		ShopUserInfo shopUser = shopUserService.getShopInfo(cpSub.getShop_phone());
		if (return_url == null) return "fail";
		CpSubPaymentBo cpSubVo = CpSubPaymentBo.getSubPaymentVoByInfo(cpSub);
		String signCode = EncryptionUtils.sign(cpSubVo.paramNotSignStr(), shopUser.getSub_open_key(), "SHA-256");
		cpSubVo.setSign(signCode);
		String paramStr = cpSubVo.paramStr();
		System.out.println(paramStr);
		String result = CallBackApi.sendRQReturnMessage(return_url, paramStr);
		if (result == null) result = "fail";
		return result;
	}


	@Override
	public int examineSubOrder(Long sub_id, Integer examine) {
		SubPaymentInfo subPaymentInfo = subPaymentInfoDao.getObjectById(new SubPaymentInfo(sub_id));
		if (subPaymentInfo == null) return -3;
		if (examine == 2) {
			taskService.sendSubPaymentFail(subPaymentInfo.getSub_payment_number());
			return 2;
		} else if (examine != 1) return -2;
		SubstituteAccount useSubstitute = substituteDao.getObjectById(new SubstituteAccount(subPaymentInfo.getSubstitute_id()));
		PassagewayInfo passageway = passagewayDao.getObjectById(new PassagewayInfo(subPaymentInfo.getPassageway_id()));
		if (useSubstitute == null || passageway == null) return -4;
		Map<String, Object> res = AMWYPayApi.sendSubInfo(subPaymentInfo, useSubstitute, passageway, serverConf.getAmwySubPrivateKey(), serverConf.getAmwySubPublicKey());
//		Map<String, Object> res = testSubApi();
		if (res.get("code").equals("1")) {
			res.put("shop_sub_number", subPaymentInfo.getShop_sub_number());
			res.put("sub_payment_number", subPaymentInfo.getSub_payment_number());
			subPaymentInfo.setState(5);
			subPaymentInfoDao.updateByPrimaryKey(subPaymentInfo);
		} else {
			taskService.sendSubPaymentFail(subPaymentInfo.getSub_payment_number());
		}
		return 1;
	}
}
