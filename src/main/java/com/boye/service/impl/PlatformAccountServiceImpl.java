package com.boye.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PlatformAccount;
import com.boye.bean.entity.PlatformBalanceNew;
import com.boye.bean.entity.PlatformIncomeAccount;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.PlatformAccountDao;
import com.boye.dao.PlatformBalanceNewDao;
import com.boye.dao.PlatformIncomeAccountDao;
import com.boye.dao.PlatformProfitDao;
import com.boye.service.IPlatformAccountService;

@Service
public class PlatformAccountServiceImpl extends BaseServiceImpl implements IPlatformAccountService {
	
	@Autowired
	private PlatformAccountDao platformAccountDao;
	
	@Autowired
	private PlatformBalanceNewDao platformBalanceNewDao;
	
	@Autowired
	private PlatformIncomeAccountDao platformIncomeAccountDao;
	
	@Autowired
	private PlatformProfitDao platformProfitDao;
	
	@Override
	public Page<PlatformAccount> platformAccountList(QueryBean query) {
		Page<PlatformAccount> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = platformAccountDao.getPlatformAccountListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(platformAccountDao.getPlatformAccountListBypage(query));
        return page;
	}
	
	@Override
	public Map<String, Object> platformAccountStatistics(QueryBean query) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("收入统计金额", 0);
		map.put("异常订单回滚总金额", 0);
		List<PlatformAccount> list=platformAccountDao.platformAccountStatistics(query);
		for (PlatformAccount platformAccount : list) {
			if (platformAccount != null && platformAccount.getType() == 1) {
				map.put("收入统计金额", platformAccount.getOrder_money());
			}
			if (platformAccount != null && platformAccount.getType() == 5) {
				map.put("异常订单回滚总金额", platformAccount.getOrder_money());
			}
		}
		return map;
	}
	
	@Override
	public Page<PlatformIncomeAccount> incomeAccountList(QueryBean query) {
		Page<PlatformIncomeAccount> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = platformIncomeAccountDao.getPlatformIncomeAccountListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(platformIncomeAccountDao.getPlatformIncomeAccountListBypage(query));
        return page;
	}
	
	@Override
	public Map<String, Object> incomeAccountStatistics(QueryBean query) {
		HashMap<String, Object> map = new HashMap<>();
		map.put("支付手续费总计", 0);
		map.put("商户提现手续费总计", 0);
		map.put("代理商提现手续费总计", 0);
		map.put("异常订单回滚总计", 0);
		List<PlatformIncomeAccount> list = platformIncomeAccountDao.incomeAccountStatistics(query);
		for (PlatformIncomeAccount platformIncomeAccount : list) {
			if (platformIncomeAccount != null && platformIncomeAccount.getType() ==1) {
				map.put("支付手续费总计", platformIncomeAccount.getActual_money());
			}
			if (platformIncomeAccount != null && platformIncomeAccount.getType() ==2) {
				map.put("商户提现手续费总计", platformIncomeAccount.getActual_money());
			}
			if (platformIncomeAccount != null && platformIncomeAccount.getType() ==3) {
				map.put("代理商提现手续费总计", platformIncomeAccount.getActual_money());
			}
			if (platformIncomeAccount != null && platformIncomeAccount.getType() ==5) {
				map.put("异常订单回滚总计", platformIncomeAccount.getActual_money());
			}
		}
		return map;
	}
	
	@Override
	public Map<String, Object> getExceptionOrderStatisticsByAdmin(Integer monthType) {
		Map<String, Object> timeMap = getMonthTime(monthType);
		if (timeMap == null) {
			return null;
		}
		String start_time = (String) timeMap.get("start_time");
		String end_time = (String) timeMap.get("end_time");
		BigDecimal sumExceptionOrder = platformAccountDao.getExceptionOrderStatisticsByAdmin(start_time,end_time);
		HashMap<String, Object> map = new HashMap<>();
		map.put("sumExceptionOrder", sumExceptionOrder);
		return map;
	}
	
	// 获取月份时间
	private Map<String, Object> getMonthTime(Integer monthType){
		Map<String, Object> map =new HashMap<String, Object>();
		// 创建时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(monthType == 1) {
			// 获取本月第一天
			Calendar firstCal = Calendar.getInstance(); 
			firstCal.set(Calendar.HOUR_OF_DAY, 0);
			firstCal.set(Calendar.MINUTE, 0);
			firstCal.set(Calendar.SECOND, 0);
			firstCal.set(Calendar.DAY_OF_MONTH,1); 
			Date firstTime = firstCal.getTime();
			// 获取当前时间
			Date lastTime = new Date();
			map.put("start_time", format.format(firstTime));
			map.put("end_time", format.format(lastTime));
		}
		if(monthType == 2) {
			Calendar firstCal = Calendar.getInstance(); 
			firstCal.set(Calendar.HOUR_OF_DAY, 0);
			firstCal.set(Calendar.MINUTE, 0);
			firstCal.set(Calendar.SECOND, 0);
			firstCal.add(Calendar.MONTH, -1); 
			firstCal.set(Calendar.DAY_OF_MONTH,1); 
			Date firstTime = firstCal.getTime();
			// 获取上月最后一天
			Calendar lastCal = Calendar.getInstance();
			lastCal.set(Calendar.HOUR_OF_DAY, 23);
			lastCal.set(Calendar.MINUTE, 59);
			lastCal.set(Calendar.SECOND, 59);
			lastCal.set(Calendar.DAY_OF_MONTH, 0);
			Date lastTime = lastCal.getTime();
			map.put("start_time", format.format(firstTime));
			map.put("end_time", format.format(lastTime));
		}
		
		return map;
	}

	@Override
	public Map<String, Object> getPlatformProfitInfo() {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> dsnList = platformProfitDao.getDataStatisticsNumber();
		if (dsnList.size() > 0) {
			for (Map<String, Object> dsn : dsnList) {
				if (dsn.get("cot") != null && (long)dsn.get("cot") > 0) 
					result.put(dsn.get("data_statistics_number").toString(), platformProfitDao.getDataByStatisticsNumber(dsn.get("data_statistics_number").toString()));
			}
		}
		return result;
	}

	@Override
	public List<PlatformBalanceNew> getPlatformBalance() {
		List<PlatformBalanceNew> result = platformBalanceNewDao.getAllBalance();
		return result;
	}

	@Override
	public int updatePlatformBalance(PlatformBalanceNew platformBalance) {
		if (platformBalance == null) {
			return 0;
		}
		//平台余额
		PlatformBalanceNew findPlatformBalance = platformBalanceNewDao.getObjectById(platformBalance);
		if (findPlatformBalance == null) return 0;
		if (platformBalance.getBalance() != null && platformBalance.getBalance() != BigDecimal.ZERO) {
			BigDecimal balance = findPlatformBalance.getBalance().add(platformBalance.getBalance());
			findPlatformBalance.setBalance(balance);
			int result = platformBalanceNewDao.updateByPrimaryKey(findPlatformBalance);
			return result;
		}
		return 0;
	}

}
