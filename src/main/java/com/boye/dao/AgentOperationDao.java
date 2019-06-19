package com.boye.dao;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.AgentOperationRecord;

@Mapper
public interface AgentOperationDao extends BaseMapper<AgentOperationRecord> {

}
