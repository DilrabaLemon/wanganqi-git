package com.boye.service;

import com.boye.bean.entity.AdminLoginRecord;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.AgentLoginRecord;
import com.boye.bean.entity.AgentOperationRecord;
import com.boye.bean.entity.ShopLoginRecord;
import com.boye.bean.entity.ShopOperationRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface ILogService {

	Page<AdminOperationRecord> getAdminOperationRecordList(QueryBean query);

	Page<ShopLoginRecord> getShopLoginRecordList(QueryBean query);

	Page<ShopOperationRecord> getShopOperationRecordList(QueryBean query);

	Page<AgentLoginRecord> getAgentLoginRecordList(QueryBean query);

	Page<AgentOperationRecord> getAgentOperationRecordList(QueryBean query);

	Page<AdminLoginRecord> getAdminLoginRecordList(QueryBean query);

	//Page<?> getShopLoginRecordListByShop(ShopUserInfo shopUser, QueryBean query);

	//Page<?> getShopOperationRecordListByShop(ShopUserInfo shopUser, QueryBean query);

}
