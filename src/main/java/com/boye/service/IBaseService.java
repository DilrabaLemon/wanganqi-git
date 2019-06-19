package com.boye.service;

import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.AgentOperationRecord;
import com.boye.bean.entity.ShopLoginRecord;
import com.boye.bean.entity.ShopOperationRecord;
import com.boye.common.ServiceReturnMessage;

public interface IBaseService {

	void addRecord(ServiceReturnMessage operation, String message);

	void addAdminOperationRecord(AdminOperationRecord operation);

	void addShopLoginRecord(ShopLoginRecord operation);

	void addShopOperationRecord(ShopOperationRecord operation);

	void addAgentOperationRecord(AgentOperationRecord operation);
	

}
