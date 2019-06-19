package com.boye.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.boye.api.juheSMSApi;
import com.boye.base.entity.BaseEntity;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.entity.ExtractionRecordForAgent;
import com.boye.bean.entity.SMSinfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.enums.SmsType;
import com.boye.dao.AgentDao;
import com.boye.dao.SMSinfoDao;
import com.boye.dao.ShopUserDao;
import com.boye.service.IDictService;
import com.boye.service.IVerificationCodeService;

@Service
public class VerificationCodeServiceImpl extends BaseServiceImpl implements IVerificationCodeService {
	
	@Autowired
	private SMSinfoDao smsInfoDao;
	
	@Autowired
	private ShopUserDao shopUserDao;
	
	@Autowired
	private AgentDao agentDao;
	
	@Autowired
	private IDictService dictService;

	@Override
	public int sendAdminUserMsg(String mobile, AdminInfo admin) {
		SMSinfo sms = saveSendMessage(mobile, admin, 3);
		if (sms == null) return -2;
		juheSMSApi.getRequest2(admin.getLogin_number(), sms.getCode());
		return 1;
	}

	@Override
	public int sendAgentUserMsg(String mobile, AgentInfo agent) {
		SMSinfo sms = saveSendMessage(mobile, agent, 2);
		if (sms == null) return -2;
		juheSMSApi.getRequest2(agent.getLogin_number(), sms.getCode());
		return 1;
	}

	@Override
	public int sendShopUserMsg(String mobile, ShopUserInfo shopUser) {
		SMSinfo sms = saveSendMessage(mobile, shopUser, 1);
		if (sms == null) return -2;
		juheSMSApi.getRequest2(shopUser.getLogin_number(), sms.getCode());
		return 1;
	}
	
	@Override
	public int compareVerification(ExtractionRecord extract, ShopUserInfo shopUser) {
		shopUser = shopUserDao.getObjectById(shopUser);
		if (shopUser == null || shopUser.getDelete_flag() != 0) return -2;
		// 判断提现金额是否小于设置的最小提现金额
		Double min_extract = shopUser.getMin_extraction();
		BigDecimal min_extraction = new BigDecimal(min_extract);
		if (extract.getExtraction_money().compareTo(min_extraction) == -1) {
			return -3;
		}
		if (shopUser.getVerification_flag() != 1) {
			if (extract.getShop_extraction_code() == null || !extract.getShop_extraction_code().equals(shopUser.getUser_code()))
				return -4;
			return 1;
		}
		List<SMSinfo> smsList = smsInfoDao.getSmsInfoByMobile(shopUser.getLogin_number());
		if (smsList.size() <= 0) return 0;
		SMSinfo findSms = smsList.get(0);
		
		Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(findSms.getCreate_time());
        rightNow.add(Calendar.MINUTE,5);//日期加10天
        Date sendTime = rightNow.getTime();
        Date now = new Date();
        if (sendTime.getTime() < now.getTime()) {
        	findSms.setState(2);
			smsInfoDao.updateByPrimaryKey(findSms);
        	return -1;
        }
        
		if (findSms.getCode().equals(extract.getVerification())) {
			findSms.setState(2);
			smsInfoDao.updateByPrimaryKey(findSms);
			return 1;
		}
		return 0;
	}
	
	@Override
	public int compareVerification(ExtractionRecordForAgent extract, AgentInfo agent) {
		agent = agentDao.getObjectById(agent);
		if (agent == null || agent.getDelete_flag() != 0) return -2;
		if (agent.getVerification_flag() != 1) {
			if (extract.getAgent_extraction_code() == null || !agent.getAgent_code().equals(extract.getAgent_extraction_code()))
				return -4;
			return 1;
		}
		List<SMSinfo> smsList = smsInfoDao.getSmsInfoByMobile(agent.getLogin_number());
		if (smsList.size() <= 0) return 0;
		SMSinfo findSms = smsList.get(0);
		
		Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(findSms.getCreate_time());
        rightNow.add(Calendar.MINUTE,5);//日期加10天
        Date sendTime = rightNow.getTime();
        Date now = new Date();
        if (sendTime.getTime() < now.getTime()) {
        	findSms.setState(2);
			smsInfoDao.updateByPrimaryKey(findSms);
        	return -1;
        }
        
		if (findSms.getCode().equals(extract.getVerification())) {
			findSms.setState(2);
			smsInfoDao.updateByPrimaryKey(findSms);
			return 1;
		}
		return 0;
	}
	
	private SMSinfo saveSendMessage(String mobile, BaseEntity user, Integer user_type) {
		List<SMSinfo> smsList = smsInfoDao.getSmsInfoByMobile(mobile);
		// 判断smsList是否为空
		if(smsList != null) {
			for (SMSinfo findSms: smsList) {
				findSms.setState(2);
				smsInfoDao.updateByPrimaryKey(findSms);
			}
		}			
		SMSinfo sms = new SMSinfo();
		String code = getCode(6);
		sms.setCode(code);
		sms.setMobile(mobile);
		sms.setMsg(code);
		sms.setState(1);
		sms.setSmsType(SmsType.VERCODE);
		sms.setUser_type(user_type);
		sms.setUserid(user.getId());
		sms.setDelete_flag(0);
		int res = smsInfoDao.insert(sms);
		if (res == 1) {
			return sms;
		}
		return null;
	}

	private String getCode(int size) {
		String str = "";
		for (int i = 0; i < size; i++) {
			int number = (int)(Math.random()*10);
			str += number;
		}
		return str;
	}

	@Override
	public int sendExtractionCode(AdminInfo admin, String mobile) {
		// 获取允许提现手机号
		String dict_key =admin.getId()+"";
		String extractionNumbers = dictService.getObjectByKeyAndType(dict_key,3);
		// 没有可用的提现手机号
		if (extractionNumbers == null || extractionNumbers == "") {
			return 2;
		}
		if (extractionNumbers.equals(mobile)) {
			//是可用的提现手机号
			// 生成4位随机数
			String code = getCode(4);
			juheSMSApi.getRequest2(mobile, code);
			// 存入session
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			attrs.getRequest().getSession().setAttribute("extractionCode", code);
			return 1;
		}else {
			// 不是可用的手机号
			return 3;
		}
	}

	@Override
	public boolean checkUserPhone(Long admin_id, String user_mobile) {
		String extractionNumbers = dictService.getObjectByKeyAndType(admin_id.toString(),3);
		if (extractionNumbers == null) return false;
		if (extractionNumbers.equals(user_mobile)) return true;
		return false;
	}
}
		/*String[] split = extractionNumbers.split(",");
		Boolean flag = false;
		// 判断是否为可提现的手机号
		for (String number : split) {
			if (number.equals(mobile)) {
				flag=true;
			}
		}
		if (flag) {
			//是可用的提现手机号
			// 生成4位随机数
			String code = getCode(4);
			juheSMSApi.getRequest2(mobile, code);
			// 存入session
			ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			attrs.getRequest().getSession().setAttribute("extractionCode", code);
			return 1;
		}else {
			// 不是可用的手机号
			return 3;
		}
	}*/


