package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.MenuTable;

@Mapper
public interface MenuTableDao extends BaseMapper<MenuTable>{

	MenuTable getMenuTableById(Long menu_id);

	List<MenuTable> getMenuTableListByRoleId(Long role_id);

	List<MenuTable> getMenuTableList();

}
