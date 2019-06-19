package com.boye.service;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.entity.ExtractionRecordForAgent;
import com.boye.bean.entity.ShopUserInfo;

public interface IVerificationCodeService {

	int sendAdminUserMsg(String mobile, AdminInfo admin);

	int sendAgentUserMsg(String mobile, AgentInfo agent);

	int sendShopUserMsg(String mobile, ShopUserInfo shopUser);

	int compareVerification(ExtractionRecord extract, ShopUserInfo shopUesr);

	int compareVerification(ExtractionRecordForAgent extract, AgentInfo agent);

	int sendExtractionCode(AdminInfo admin, String mobile);

	boolean checkUserPhone(Long admin_id, String user_mobile);

}
