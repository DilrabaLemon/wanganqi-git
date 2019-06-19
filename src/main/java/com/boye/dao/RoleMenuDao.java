package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.RoleMenu;

@Mapper
public interface RoleMenuDao extends BaseMapper<RoleMenu>{

	List<RoleMenu> findRoleMenuListByRoleId(Long role_id);

	void updateMenuRoleByRoleId(Long role_id);

	

	

}
