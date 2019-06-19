package com.boye.service;

import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

import java.util.Map;

import com.boye.bean.entity.*;


public interface IExtractionService {

	//int addExtractionRecord(ShopUserInfo shopUser, ExtractionRecord extract);

	int extractionExamine(String extraction_id, String examine);
	
	Page<ExtractionRecord> extractionList(Integer state, QueryBean query);

	//Page<ExtractionRecord> extractionListByShop(ShopUserInfo shopUser, Integer state, QueryBean query);

	//int addExtractionRecordByAgent(AgentInfo agent, ExtractionRecordForAgent extract);

	int extractionExamineForAgent(String extraction_id, String examine);

	Page<ExtractionRecordForAgent> extractionListForAgent(Integer state, QueryBean query);

	Map<String, Object> getSendInfo();

	Map<String, Object> getExtractionStatisticsByAdmin(Integer monthType);
	
	String getRemoteMessage();

	int queryExtractionSubState(Long extraction_id);

	int queryExtractionSubStateByAgent(Long extraction_id);

	Map<String, Object> autoExtractionToPlatform();

	Page<PlatformExtractionRecord> extractionListByAdmin(QueryBean query);

	int extractionToPlatformByAdmin(PlatformExtractionRecord extract);

	Map<String, Object> queryProvideBalance(int type);

	int extractionExamineByAdmin(Long extraction_id, Integer type);

	int queryExtractionSubStateByPlatform(Long extraction_id);

	//Page<ExtractionRecordForAgent> extractionListByAgent(AgentInfo agent, Integer state, QueryBean query);

}
