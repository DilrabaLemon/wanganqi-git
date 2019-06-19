package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.ExtractionRecordForAgent;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface ExtractionAgentDao extends BaseMapper<ExtractionRecordForAgent> {

	ExtractionRecordForAgent getExtractionByNumber(String extraction_number);
	// 条件查询代理商提现  总记录数
	int getExtractionRecordAgentCount(QueryBean query);
	// 条件查询代理商提现  分页数据
	List<ExtractionRecordForAgent> getExtractionRecordAgent(QueryBean query);

	int getExtractionCountByAgentId(Long id);

}
