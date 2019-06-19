package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PassagewayCostInfo;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface PassagewayCostDao extends BaseMapper<PassagewayCostInfo> {

	List<PassagewayCostInfo> getPassagewayCostByPage(QueryBean query);

	int getPassagewayCostByCount(QueryBean query);

}
