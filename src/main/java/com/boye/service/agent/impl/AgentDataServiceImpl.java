package com.boye.service.agent.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.AgentDataStatisticsBean;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.UserDailyBalanceHistory;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.utils.FormatUtils;
import com.boye.dao.AgentBalanceNewDao;
import com.boye.dao.AgentDao;
import com.boye.dao.OrderDao;
import com.boye.dao.UserDailyBalanceHistoryDao;
import com.boye.service.agent.AgentDataService;
import com.boye.service.impl.BaseServiceImpl;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AgentDataServiceImpl extends BaseServiceImpl implements AgentDataService{
	
	@Resource
	private AgentDao agentDao;
	
	@Autowired
	private AgentBalanceNewDao agentBalanceNewDao;
	
	@Resource
	private OrderDao orderDao;
	
	@Resource
	private UserDailyBalanceHistoryDao userDailyBalanceHistoryDao;
	@Override
	public AgentDataStatisticsBean getAgentDataStatistics(AgentInfo agent) {
		// 获取代理商数据实体
		AgentDataStatisticsBean agentDataBean = new AgentDataStatisticsBean();
		Calendar cal = Calendar.getInstance();
		//按时分秒获取代理商营业额和提现金额
		cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        agentDataBean.setDay_turnover(FormatUtils.getMoneyString(agentDao.getTurnoverByCondition(cal.getTime(), agent.getId())));
        agentDataBean.setDay_extraction(FormatUtils.getMoneyString(agentDao.getExtractionByCondition(cal.getTime(), agent.getId())));
        //按天获取代理商营业额和提现金额
        cal.set(Calendar.DAY_OF_MONTH, 1);
        agentDataBean.setMonth_turnover(FormatUtils.getMoneyString(agentDao.getTurnoverByCondition(cal.getTime(), agent.getId())));
        agentDataBean.setMonth_extraction(FormatUtils.getMoneyString(agentDao.getExtractionByCondition(cal.getTime(), agent.getId())));
        //按月获取代理商营业额和提现金额
        Date now_month = cal.getTime();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        agentDataBean.setLast_month_turnover(FormatUtils.getMoneyString(agentDao.getTurnoverByLastMonth(now_month, cal.getTime(), agent.getId())));
        agentDataBean.setLast_month_extraction(FormatUtils.getMoneyString(agentDao.getExtractionByLastMonth(now_month, cal.getTime(), agent.getId())));
        agentDataBean.setBalanceList(agentBalanceNewDao.getByAgentId(agent.getId()));
        return agentDataBean;
	}

	@Override
	public Page<OrderInfo> orderListByAgent(AgentInfo agent, QueryBean query) {
		List<Long> ids = orderDao.getShopIdsByAgent(agent.getId());
		// 将商户IDS 传入条件query
		if (ids.size() > 0 && ids != null ) {
			query.setShop_ids(ids);
		}else {
			ids.add((long) -1);
			query.setShop_ids(ids);
		}
		Page<OrderInfo> page = new Page<>(query.getPage_index(), query.getPage_size());
		int count = orderDao.getOrderListCountByShop(query);
	    page.setTotals(count);
	    if (count == 0)
	        page.setDatalist(new ArrayList<OrderInfo>());
	    else
	        page.setDatalist(orderDao.getOrderListByShop(query));
	    return page;
		// 创建分页查询条件存入条件query
	}

	@Override
	public Map<String,Object> getUserDailyBalanceHistoryByAgent(AgentInfo agent) {
		Long id =agent.getId();
		// 类型为代理商
		Integer type = 2;
		UserDailyBalanceHistory dailyHistory = userDailyBalanceHistoryDao.getUserDailyBalanceHistory(id,type);
		BigDecimal income = dailyHistory.getIncome_money().setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal expenditure = dailyHistory.getExpenditure_money().setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal balance = dailyHistory.getBalance_change().setScale(2, BigDecimal.ROUND_HALF_UP);
		HashMap<String, Object> hashMap = new HashMap<>();
		hashMap.put("expenditure_money", expenditure.toString());
		hashMap.put("balance_change", balance.toString());
		hashMap.put("income_money", income.toString());
		hashMap.put("income_count", dailyHistory.getIncome_count());
		hashMap.put("expend_count", dailyHistory.getExpend_count());
		hashMap.put("user_id", dailyHistory.getUser_id());
		hashMap.put("type", dailyHistory.getType());
		return hashMap;
	}
	
	
//	private List<Long> getShopIdsByAgentId(AgentInfo agent) {
//		// 根据代理商id获取给代理商名下的所有商户ids
//		return orderDao.getShopIdsByAgent(agent.getId());
//		StringBuffer idStr = new StringBuffer();
//		// 循环生成商户ids字符串
//		for (Long id: ids) {
//			if (id != null) idStr.append(id.toString() + ",");
//		}
//		//判断商户IDS字符串是否为空串，不是 删除最后一个逗号，是返回-1
//		if (idStr.length() > 0) idStr.delete(idStr.length()-1, idStr.length());
//		else {
//			idStr.append("-1");
//		}
//		return ids;
//	}

}
