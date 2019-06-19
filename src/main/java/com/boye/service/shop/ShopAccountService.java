package com.boye.service.shop;

import java.util.Map;

import com.boye.bean.entity.ShopAccount;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface ShopAccountService {
	
	Page<ShopAccount> shopAccountListByShop(ShopUserInfo shopInfo, QueryBean query);
	/**
	 * 商户账单列表
	 * @param query
	 * @return
	 */
	Page<ShopAccount> shopAccountList(QueryBean query);
	
//	Page<PlatformAccount> platformAccountList(QueryBean query);
	
//	Page<PlatformIncomeAccount> incomeAccountList(QueryBean query);
	
//	Page<AgentAccount> agentAccountList(QueryBean query);
	
//	Page<AgentAccount> agentAccountListByAgent(AgentInfo agent, QueryBean query);
	//获取商户每日数据统计
	Map<String,Object> getUserDailyBalanceHistoryByShop(ShopUserInfo shopUser);
	
//	Map<String, Object> getExceptionOrderStatisticsByAdmin(Integer monthType);
	
	//获取账单列表统计收入总金额和异常回滚总金额 
	Map<String, Object> shopAccountStatisticeByShop(ShopUserInfo shopUser, QueryBean query);
	
	//ShopBalance getBalanceByShop(ShopUserInfo shopUser);
	
//	Map<String, Object> agentAccountStatisticsByAgent(AgentInfo agent, QueryBean query);
	
//	Map<String, Object> platformAccountStatistics(QueryBean query);
	
//	Map<String, Object> incomeAccountStatistics(QueryBean query);
	
	
	
}
