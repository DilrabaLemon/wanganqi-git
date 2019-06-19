package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.SelfCheck;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface SelfCheckDao extends BaseMapper<SelfCheck>{

	SelfCheck findByAppid(String appid);

	int findSelfCheckListByCount(QueryBean query);

	List<SelfCheck> findSelfCheckList(QueryBean query);

}
