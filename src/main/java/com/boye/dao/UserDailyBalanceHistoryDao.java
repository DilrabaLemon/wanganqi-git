package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.UserDailyBalanceHistory;
import com.boye.bean.vo.QueryBean;


@Mapper
public interface UserDailyBalanceHistoryDao extends BaseMapper<UserDailyBalanceHistory>{

	int getUserDailyBalanceHistoryConutByAdmin(QueryBean query);

	List<UserDailyBalanceHistory> getUserDailyBalanceHistoryPageByAdmin(QueryBean query);
	// 获取代理商/商户每日数据统计
	UserDailyBalanceHistory getUserDailyBalanceHistory(@Param("id")Long id, @Param("type")Integer type);

}
