package com.boye.service.impl;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.api.juheSMSApi;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.SystemMsg;
import com.boye.bean.vo.Page;
import com.boye.dao.OrderDao;
import com.boye.dao.SystemMsgDao;
import com.boye.service.SystemMsgService;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class SystemMsgServiceImpl extends BaseServiceImpl implements SystemMsgService{
	
	@Autowired
	private SystemMsgDao systemMsgDao;
	
	@Autowired
	private OrderDao orderDao;
	
	@Override
	public int insertSystemMsgByAdmin(SystemMsg systemMsg) {
		// 设置新插入的消息为未读状态
		systemMsg.setMsg_state(1);
		int result = systemMsgDao.insert(systemMsg);
		return result;
	}

	@Override
	public int deleteSystemMsgByAdmin(Long id) {
		SystemMsg getSystemMsg=systemMsgDao.getSystemMsgById(id);
		if (getSystemMsg == null) {
			return 0;
		}
		// 逻辑删除
		getSystemMsg.setDelete_flag(1);
		int result = systemMsgDao.updateByPrimaryKey(getSystemMsg);
		return result;
	}

	@Override
	public Page<SystemMsg> findSystemMsgListByAdmin(Map<String, Object> query) {
		Page<SystemMsg> page = new Page<SystemMsg>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		// 根据条件查询总记录数
		int count = systemMsgDao.getSystemMsgListByCount(query);
        page.setTotals(count);
        // 如果记录数为0，传一个空列表到page，或者根据查询订单列表  传入page
        if (count == 0) {
        	page.setDatalist(new ArrayList<SystemMsg>());
        }else {
        	page.setDatalist(systemMsgDao.getSystemMsgListByPage(query));
        }
           
		return page;
	}

	@Override
	public int sendMsgByUser(Map<String, Object> param) {
		// 根据平台订单号和用户订单号，联表查询用户手机号
		String shopNumber = orderDao.getShopNumberByPlatformOrderNumber(param.get("platform_order_number"),param.get("order_number"));
		if (shopNumber == null || shopNumber == "") {
			return -1;
		}
		// 将回调失败的订单号发送给商户
		juheSMSApi.getRequest2(shopNumber, param.get("order_number").toString());
		return 1;
	}

}
