package com.boye.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface AgentDao extends BaseMapper<AgentInfo> {
	
    AgentInfo agentLogin(@Param("login_number") String login_number, @Param("password") String password);

    AgentInfo getAgentByLoginNumber(String login_number);

	Double getTurnoverByCondition(@Param("time")Date time, @Param("agent_id")Long agent_id);

	Double getExtractionByCondition(@Param("time")Date time, @Param("agent_id")Long agent_id);

	Double getTurnover(Long agent_id);

	Double getExtraction(Long agent_id);
	//  条件查询代理商列表记录数
	int getAgentListByCount(QueryBean query);
	// 条件查询代理商列表分页数据
	List<AgentInfo> getAgentListByPage(QueryBean query);

	Double getTurnoverByLastMonth(@Param("now_month")Date now_month, @Param("last_month")Date last_month, @Param("agent_id")Long agent_id);

	Double getExtractionByLastMonth(@Param("now_month")Date now_month, @Param("last_month")Date last_month, @Param("agent_id")Long agent_id);

	List<AgentInfo> getAgentIdAndNameList();

	@Select("select * from agent_info where delete_flag = 0 and login_number = #{login_number}")
	AgentInfo findAgentByLoginNumber(String login_number);

	@Update("update agent_info set google_auth_flag = #{googleAuthFlag} where id = #{id}")
	int changeGoogleAuthFlagById(@Param("id")Long id, @Param("googleAuthFlag")int googleAuthFlag);
}
