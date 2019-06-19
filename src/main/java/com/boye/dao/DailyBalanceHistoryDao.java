package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.DailyBalanceHistory;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface DailyBalanceHistoryDao extends BaseMapper<DailyBalanceHistory>{

	List<DailyBalanceHistory> getDailyBalanceHistoryNews();


}
