package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PlatformIncomeAccount;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface PlatformIncomeAccountDao extends BaseMapper<PlatformIncomeAccount> {

	List<PlatformIncomeAccount> getPlatformIncomeByOrderId(Long order_id);

	int getPlatformIncomeAccountListByCount(QueryBean query);

	List<PlatformIncomeAccount> getPlatformIncomeAccountListBypage(QueryBean query);

	List<PlatformIncomeAccount> incomeAccountStatistics(QueryBean query);

}
