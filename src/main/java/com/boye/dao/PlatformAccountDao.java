package com.boye.dao;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PlatformAccount;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface PlatformAccountDao extends BaseMapper<PlatformAccount> {

	PlatformAccount getPlatformAccountByAccountNumber(String string);

	List<PlatformAccount> getPlatformAccountByOrderId(long parseLong);

	int getPlatformAccountListByCount(QueryBean query);

	List<PlatformAccount> getPlatformAccountListBypage(QueryBean query);

	BigDecimal getExtractionStatisticsByAdmin(@Param("start_time")String start_time, @Param("end_time")String end_time);

	BigDecimal getExceptionOrderStatisticsByAdmin(@Param("start_time")String start_time, @Param("end_time")String end_time);

	List<PlatformAccount> platformAccountStatistics(QueryBean query);

}
