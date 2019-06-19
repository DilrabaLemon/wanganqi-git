package com.boye.service.agent.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.vo.Page;
import com.boye.dao.OrderDao;
import com.boye.dao.SubPaymentInfoDao;
import com.boye.service.agent.AgentSubOrderService;
import com.boye.service.impl.BaseServiceImpl;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AgentSubOrderServiceImpl extends BaseServiceImpl implements AgentSubOrderService {
	
	@Autowired
	private OrderDao orderDao;
	
	@Autowired
	private SubPaymentInfoDao subPaymentInfoDao;
	
	
	@Override
	public Page<SubPaymentInfo> subOrderListByAgent(AgentInfo agent, Map<String, Object> query) {
		List<Long> ids = orderDao.getShopIdsByAgent(agent.getId());
		// 将商户IDS 传入条件query
		if (ids.size() > 0 && ids != null ) {
			query.put("shop_ids", ids);
		}else {
			ids.add((long) -1);
			query.put("shop_ids", ids);
		}
		// 创建分页查询条件存入条件query
		Page<SubPaymentInfo> page = new Page<SubPaymentInfo>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		// 根据条件查询总记录数
		int count = subPaymentInfoDao.getSubOrderListByAgentCount(query);
        page.setTotals(count);
        // 如果记录数为0，传一个空列表到page，或者根据查询订单列表  传入page
        if (count == 0)
            page.setDatalist(new ArrayList<SubPaymentInfo>());
        else
            page.setDatalist(subPaymentInfoDao.getSubOrderListByAgent(query));
		return page;
	}

}
