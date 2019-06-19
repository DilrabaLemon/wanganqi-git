package com.boye.dao;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.RoleTable;
import com.boye.bean.vo.QueryBean;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
	
public interface RoleDao extends BaseMapper<RoleTable> {
	// 条件查询角色列表分页数据
	List<RoleTable> getRoleByPage(QueryBean query);
	// 条件查询角色列表记录数
	int getRoleByCount(QueryBean query);
}
