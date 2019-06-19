package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.CpOrderInfoFail;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface CpOrderInfoFailDao extends BaseMapper<CpOrderInfoFail>{

	int getCpOrderInfoFailByCount(QueryBean query);

	List<CpOrderInfoFail> getCpOrderInfoFailByPage(QueryBean query);
	
}
