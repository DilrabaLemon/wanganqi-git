package com.boye.service.agent.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ExtractionRecordForAgent;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.ExtractionAgentDao;
import com.boye.service.ITaskService;
import com.boye.service.agent.AgentExtractionService;
import com.boye.service.impl.BaseServiceImpl;


@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AgentExtractionServiceImpl extends BaseServiceImpl implements AgentExtractionService{
	
	@Autowired
    private ExtractionAgentDao extractionAgentDao;
	
	@Autowired
	private ITaskService taskService;
	 
	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public int addExtractionRecordByAgent(AgentInfo agent, ExtractionRecordForAgent extract) {
    	extract.setAgent_id(agent.getId());
    	if (extract.getExtraction_money().compareTo(new BigDecimal(500000)) == 1) return -21;
    	//判断用户是否异常
    	String code = UUID.randomUUID().toString().replaceAll("-", "");
    	extract.setExtraction_number(code);
    	// 设置提现状态,为未审核
    	extract.setState(-1);//临时存储提现申请
    	//设置逻辑删除标识
    	extract.setDelete_flag(0);
    	extract.setService_charge(new BigDecimal(2));
    	extract.setActual_money(extract.getExtraction_money().add(BigDecimal.ZERO));
    	// 设置 从余额中扣除手续费
    	extract.setService_type(1);
    	if (extractionAgentDao.insert(extract) == 1) {
    		Map<String, Object> result = taskService.sendAgentExtractionSubmitServer(extract.getExtraction_number());
    		if (result.get("code").equals(1.0)) {
    			return 1;
    		} else {
    			extract = extractionAgentDao.getExtractionByNumber(extract.getExtraction_number());
    			if (extract != null) {
    				extract.setDelete_flag(1);
    				extractionAgentDao.updateByPrimaryKey(extract);
    			}
    			return ((Double)result.get("code")).intValue();
    		}
    	}
    	return 0;
	}

	@Override
	public Page<ExtractionRecordForAgent> extractionListByAgent(AgentInfo agent, Integer state, QueryBean query) {
		// 代理商ID 传入查询条件
		query.setMain_condition(agent.getId().toString());
		return extractionListForAgent(state, query);
	}
	
	private Page<ExtractionRecordForAgent> extractionListForAgent(Integer state, QueryBean query) {
		if (state == null) state = 0;
		// 设置描述信息
    	query.setState(state);
    	// 创建代理商提现记录分页对象
    	Page<ExtractionRecordForAgent> page = new Page<ExtractionRecordForAgent>(query.getPage_index(), query.getPage_size());
    	// 根据条件查询 提现记录数
        int count = extractionAgentDao.getExtractionRecordAgentCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
        	// 根据条件查询 提现记录列表
            page.setDatalist(extractionAgentDao.getExtractionRecordAgent(query));
        return page;
	}
}
