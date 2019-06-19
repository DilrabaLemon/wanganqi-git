package com.boye.service.shop.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.ShopAccount;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.UserDailyBalanceHistory;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.ShopAccountDao;
import com.boye.dao.UserDailyBalanceHistoryDao;
import com.boye.service.impl.BaseServiceImpl;
import com.boye.service.shop.ShopAccountService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ShopAccountServiceImpl extends BaseServiceImpl implements ShopAccountService{
	
	@Autowired
	private ShopAccountDao shopAccountDao;
	
	@Resource
	private UserDailyBalanceHistoryDao userDailyBalanceHistoryDao;
	
	@Override
	public Page<ShopAccount> shopAccountListByShop(ShopUserInfo shopInfo, QueryBean query) {
		if (shopInfo.getId() == null) shopInfo.setId(-1l);
		String main_condition = shopInfo.getId().toString();
		query.setMain_condition(main_condition);
		return shopAccountList(query);
	}
	
	@Override
	public Page<ShopAccount> shopAccountList(QueryBean query) {
		Page<ShopAccount> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = shopAccountDao.getShopAccountListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(shopAccountDao.getShopAccountListByPage(query));
        return page;
	}

//	@Override
//	public Page<PlatformAccount> platformAccountList(QueryBean query) {
//		Page<PlatformAccount> page = new Page<>(query.getPage_index(), query.getPage_size());
//        int count = platformAccountDao.getPlatformAccountListByCount(query);
//        page.setTotals(count);
//        if (count == 0)
//            page.setDatalist(new ArrayList<>());
//        else
//            page.setDatalist(platformAccountDao.getPlatformAccountListBypage(query));
//        return page;
//	}

//	@Override
//	public Page<PlatformIncomeAccount> incomeAccountList(QueryBean query) {
//		Page<PlatformIncomeAccount> page = new Page<>(query.getPage_index(), query.getPage_size());
//        int count = platformIncomeAccountDao.getPlatformIncomeAccountListByCount(query);
//        page.setTotals(count);
//        if (count == 0)
//            page.setDatalist(new ArrayList<>());
//        else
//            page.setDatalist(platformIncomeAccountDao.getPlatformIncomeAccountListBypage(query));
//        return page;
//	}

//	@Override
//	public Page<AgentAccount> agentAccountList(QueryBean query) {
//		Page<AgentAccount> page = new Page<>(query.getPage_index(), query.getPage_size());
//        int count = agentAccountDao.getAgentAccountListByCount(query);
//        page.setTotals(count);
//        if (count == 0)
//            page.setDatalist(new ArrayList<>());
//        else
//            page.setDatalist(agentAccountDao.getAgentAccountListBypage(query));
//        return page;
//	}

//	@Override
//	public Page<AgentAccount> agentAccountListByAgent(AgentInfo agent, QueryBean query) {
//		if (agent.getId() == null) agent.setId(-1l);
//		String main_condition = agent.getId().toString();
//		query.setMain_condition(main_condition);
//		return agentAccountList(query);
//	}

	@Override
	public Map<String,Object> getUserDailyBalanceHistoryByShop(ShopUserInfo shopUser) {
		Long id =shopUser.getId();
		Integer type =1;
		 UserDailyBalanceHistory balanceHistory = userDailyBalanceHistoryDao.getUserDailyBalanceHistory(id, type);
		 BigDecimal balance = balanceHistory.getBalance_change().setScale(2, BigDecimal.ROUND_HALF_UP);
		 BigDecimal expenditure = balanceHistory.getExpenditure_money().setScale(2, BigDecimal.ROUND_HALF_UP);
		 BigDecimal platformIncome = balanceHistory.getPlatform_income().setScale(2, BigDecimal.ROUND_HALF_UP);
		 BigDecimal income = balanceHistory.getIncome_money().setScale(2, BigDecimal.ROUND_HALF_UP);
		 HashMap<String, Object> hashMap = new HashMap<>();
		 hashMap.put("expenditure_money", expenditure.toString());
		 hashMap.put("balance_change", balance.toString());
		 hashMap.put("income_money", income.toString());
		 hashMap.put("platform_income", platformIncome.toString());
		 hashMap.put("expend_count", balanceHistory.getExpend_count());
		 hashMap.put("income_count", balanceHistory.getIncome_count());
		 hashMap.put("user_id", balanceHistory.getUser_id());
		 hashMap.put("type", balanceHistory.getType());
		 return hashMap;
	}

//	@Override
//	public Map<String, Object> getExceptionOrderStatisticsByAdmin(Integer monthType) {
//		Map<String, Object> timeMap = getMonthTime(monthType);
//		if (timeMap == null) {
//			return null;
//		}
//		String start_time = (String) timeMap.get("start_time");
//		String end_time = (String) timeMap.get("end_time");
//		BigDecimal sumExceptionOrder = platformAccountDao.getExceptionOrderStatisticsByAdmin(start_time,end_time);
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("sumExceptionOrder", sumExceptionOrder);
//		return map;
//	}
	
//	// 获取月份时间
//	private Map<String, Object> getMonthTime(Integer monthType){
//		Map<String, Object> map =new HashMap<String, Object>();
//		// 创建时间格式
//		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//		if(monthType == 1) {
//			// 获取本月第一天
//			Calendar firstCal = Calendar.getInstance(); 
//			firstCal.set(Calendar.HOUR_OF_DAY, 0);
//			firstCal.set(Calendar.MINUTE, 0);
//			firstCal.set(Calendar.SECOND, 0);
//			firstCal.set(Calendar.DAY_OF_MONTH,1); 
//			Date firstTime = firstCal.getTime();
//			// 获取当前时间
//			Date lastTime = new Date();
//			map.put("start_time", format.format(firstTime));
//			map.put("end_time", format.format(lastTime));
//		}
//		if(monthType == 2) {
//			Calendar firstCal = Calendar.getInstance(); 
//			firstCal.set(Calendar.HOUR_OF_DAY, 0);
//			firstCal.set(Calendar.MINUTE, 0);
//			firstCal.set(Calendar.SECOND, 0);
//			firstCal.add(Calendar.MONTH, -1); 
//			firstCal.set(Calendar.DAY_OF_MONTH,1); 
//			Date firstTime = firstCal.getTime();
//			// 获取上月最后一天
//			Calendar lastCal = Calendar.getInstance();
//			lastCal.set(Calendar.HOUR_OF_DAY, 23);
//			lastCal.set(Calendar.MINUTE, 59);
//			lastCal.set(Calendar.SECOND, 59);
//			lastCal.set(Calendar.DAY_OF_MONTH, 0);
//			Date lastTime = lastCal.getTime();
//			map.put("start_time", format.format(firstTime));
//			map.put("end_time", format.format(lastTime));
//		}
//		
//		return map;
//	}

		@Override
		//获取账单列表统计收入总金额和异常回滚总金额 
		public Map<String, Object> shopAccountStatisticeByShop(ShopUserInfo shopUser,QueryBean query) {
			HashMap<String, Object> map = new HashMap<>();
			map.put("收入统计金额", 0);
			map.put("异常订单回滚总金额", 0);
			if(shopUser != null && shopUser.getId() != null ) {
				query.setShop_id(shopUser.getId());
			}
			List<ShopAccount> list = shopAccountDao.shopAccountStatisticeByShop(query);
			for (ShopAccount shopAccount : list) {
				// 获取收入总金额
				if (shopAccount != null && shopAccount.getType() == 1) {
					map.put("收入统计金额", shopAccount.getActual_money());
					map.put("订单统计金额", shopAccount.getOrder_money());
					map.put("手续费金额", shopAccount.getOrder_money().subtract(shopAccount.getActual_money()));
				}
				// 获取异常总金额
				if (shopAccount != null && shopAccount.getType() == 5) {
					map.put("异常订单回滚总金额", shopAccount.getActual_money());
				}
			}
			return map;
		}

//		@Override
//		public ShopBalance getBalanceByShop(ShopUserInfo shopUser) {
//			Long shopid = shopUser.getId();
//			ShopBalance shopBalance = shopBalanceDao.getBalanceByShopId(shopid);
//			return shopBalance;
//		}

//	@Override
//	public Map<String, Object> agentAccountStatisticsByAgent(AgentInfo agent, QueryBean query) {
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("收入统计金额", 0);
//		map.put("异常订单回滚总金额", 0);
//		if (agent != null && agent.getId() != null) {
//			query.setMain_condition(agent.getId().toString());
//		}
//		List<AgentAccount> list = agentAccountDao.agentAccountStatisticsByAgent(query);
//		for (AgentAccount agentAccount : list) {
//			// 获取收入总金额
//			if (agentAccount !=null && agentAccount.getType() == 1) {
//				map.put("收入统计金额", agentAccount.getActual_money());
//			}
//			// 获取异常总金额
//			if (agentAccount !=null && agentAccount.getType() == 5) {
//				map.put("异常订单回滚总金额", agentAccount.getActual_money());
//			}
//		}
//		return map;
//	}

//		@Override
//		public Map<String, Object> platformAccountStatistics(QueryBean query) {
//			HashMap<String, Object> map = new HashMap<>();
//			map.put("收入统计金额", 0);
//			map.put("异常订单回滚总金额", 0);
//			List<PlatformAccount> list=platformAccountDao.platformAccountStatistics(query);
//			for (PlatformAccount platformAccount : list) {
//				if (platformAccount != null && platformAccount.getType() == 1) {
//					map.put("收入统计金额", platformAccount.getOrder_money());
//				}
//				if (platformAccount != null && platformAccount.getType() == 5) {
//					map.put("异常订单回滚总金额", platformAccount.getOrder_money());
//				}
//			}
//			return map;
//		}

//	@Override
//	public Map<String, Object> incomeAccountStatistics(QueryBean query) {
//		HashMap<String, Object> map = new HashMap<>();
//		map.put("支付手续费总计", 0);
//		map.put("商户提现手续费总计", 0);
//		map.put("代理商提现手续费总计", 0);
//		map.put("异常订单回滚总计", 0);
//		List<PlatformIncomeAccount> list = platformIncomeAccountDao.incomeAccountStatistics(query);
//		for (PlatformIncomeAccount platformIncomeAccount : list) {
//			if (platformIncomeAccount != null && platformIncomeAccount.getType() ==1) {
//				map.put("支付手续费总计", platformIncomeAccount.getActual_money());
//			}
//			if (platformIncomeAccount != null && platformIncomeAccount.getType() ==2) {
//				map.put("商户提现手续费总计", platformIncomeAccount.getActual_money());
//			}
//			if (platformIncomeAccount != null && platformIncomeAccount.getType() ==3) {
//				map.put("代理商提现手续费总计", platformIncomeAccount.getActual_money());
//			}
//			if (platformIncomeAccount != null && platformIncomeAccount.getType() ==5) {
//				map.put("异常订单回滚总计", platformIncomeAccount.getActual_money());
//			}
//		}
//		return map;
//	}
}
