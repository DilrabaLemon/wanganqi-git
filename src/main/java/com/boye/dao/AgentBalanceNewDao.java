package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.AgentBalanceNew;

@Mapper
public interface AgentBalanceNewDao extends BaseMapper<AgentBalanceNew> {

	@Select("select * from agent_balance_new where agent_id = #{agent_id} and delete_flag = 0")
	List<AgentBalanceNew> getByAgentId(@Param("agent_id")Long agent_id);

}
