package com.boye.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.SystemMsg;

@Mapper
public interface SystemMsgDao extends BaseMapper<SystemMsg>{
	
	// 根据id查询实体类
	SystemMsg getSystemMsgById(Long id);
	
	// 查询记录条数
	int getSystemMsgListByCount(Map<String, Object> query);
	
	// 查询记录内容
	List<SystemMsg> getSystemMsgListByPage(Map<String, Object> query);

}
