package com.boye.service;

import java.util.List;
import java.util.Map;

import com.boye.bean.entity.PlatformAccount;
import com.boye.bean.entity.PlatformBalanceNew;
import com.boye.bean.entity.PlatformIncomeAccount;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface IPlatformAccountService {

	Page<PlatformAccount> platformAccountList(QueryBean query);

//	Map<String, Object> shopAccountStatisticeByShop(ShopUserInfo shopUser, QueryBean query);

	Map<String, Object> platformAccountStatistics(QueryBean query);

	Page<PlatformIncomeAccount> incomeAccountList(QueryBean query);

	Map<String, Object> incomeAccountStatistics(QueryBean query);
	
	Map<String, Object> getExceptionOrderStatisticsByAdmin(Integer monthType);

	Map<String, Object> getPlatformProfitInfo();

	List<PlatformBalanceNew> getPlatformBalance();

	int updatePlatformBalance(PlatformBalanceNew platformBalance);

}
