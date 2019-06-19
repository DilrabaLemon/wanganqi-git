package com.boye.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.AgentOperationRecord;
import com.boye.bean.entity.ShopLoginRecord;
import com.boye.bean.entity.ShopOperationRecord;
import com.boye.common.ServiceReturnMessage;
import com.boye.dao.AdminOperationRecordDao;
import com.boye.dao.AgentOperationDao;
import com.boye.dao.ShopLoginRecordDao;
import com.boye.dao.ShopOperationRecordDao;
import com.boye.service.IBaseService;

@Service("baseService")
public class BaseServiceImpl implements IBaseService {
	
	@Autowired
	private AdminOperationRecordDao adminOperationDao;
	
	@Autowired
	private ShopLoginRecordDao shopLoginDao;
	
	@Autowired
	private ShopOperationRecordDao shopOperationDao;
	
	@Autowired
	private AgentOperationDao agentOperationDao;

	@Override
	public void addRecord(ServiceReturnMessage operation, String message) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addAdminOperationRecord(AdminOperationRecord operation) {
		adminOperationDao.insert(operation);
	}

	@Override
	public void addShopLoginRecord(ShopLoginRecord operation) {
		shopLoginDao.insert(operation);
		
	}

	@Override
	public void addShopOperationRecord(ShopOperationRecord operation) {
		shopOperationDao.insert(operation);
	}

	@Override
	public void addAgentOperationRecord(AgentOperationRecord operation) {
		agentOperationDao.insert(operation);
		
	}

}
