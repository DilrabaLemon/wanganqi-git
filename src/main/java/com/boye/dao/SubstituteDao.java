package com.boye.dao;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.SubstituteAccount;
import com.boye.bean.vo.QueryBean;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SubstituteDao extends BaseMapper<SubstituteAccount> {

	SubstituteAccount getSubstituteByAccountNumber(String account_number);
	
	// 条件查询扣除流水记录数
	int getSubstituteAccountListByCount(QueryBean query);
	// 条件查询扣除流水分页数据
	List<SubstituteAccount> getSubstituteAccountListByPage(QueryBean query);

	List<SubstituteAccount> getSubstituteByPassagewayId(Long passageway_id);

}
