package com.boye.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PlatformProfitInfo;

@Mapper
public interface PlatformProfitDao extends BaseMapper<PlatformProfitInfo> {

	List<Map<String, Object>> getDataStatisticsNumber();

	List<PlatformProfitInfo> getDataByStatisticsNumber(String string);

}
