package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.bean.entity.AdminLoginRecord;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.AgentLoginRecord;
import com.boye.bean.entity.AgentOperationRecord;
import com.boye.bean.entity.ShopLoginRecord;
import com.boye.bean.entity.ShopOperationRecord;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface LogDao {
	
	List<AdminOperationRecord> getAdminOperationRecord(QueryBean query);

	int getAdminOperationRecordCount(QueryBean query);

	List<ShopLoginRecord> getShopLoginRecord(QueryBean query);

	int getShopLoginRecordCount(QueryBean query);
	
	List<ShopOperationRecord> getShopOperationRecord(QueryBean query);

	int getShopOperationRecordCount(QueryBean query);

	int getAgentLoginRecordCount(QueryBean query);

	List<AgentLoginRecord> getAgentLoginRecord(QueryBean query);

	int getAgentOperationRecordCount(QueryBean query);

	List<AgentOperationRecord> getAgentOperationRecord(QueryBean query);

	int getAdminLoginRecordCount(QueryBean query);

	List<AdminLoginRecord> getAdminLoginRecord(QueryBean query);
}
