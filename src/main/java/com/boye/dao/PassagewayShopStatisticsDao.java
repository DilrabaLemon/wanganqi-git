package com.boye.dao;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.vo.PassagewayShopStatistics;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface PassagewayShopStatisticsDao extends BaseMapper<PassagewayShopStatistics>{

	String findFlagTime();

	int getPassagewayShopStatisticsCount(HashMap<String, Object> queryMap);

	List<PassagewayShopStatistics> getPassagewayShopStatisticsPage(HashMap<String, Object> queryMap);

}
