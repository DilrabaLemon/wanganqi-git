package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PlatformBalanceNew;

@Mapper
public interface PlatformBalanceNewDao extends BaseMapper<PlatformBalanceNew> {

	@Select("select * from platform_balance_new where delete_flag = 0")
	List<PlatformBalanceNew> getAllBalance();

}
