package com.boye.service.agent;

import com.boye.bean.entity.OrderInfo;

public interface AgentOrderService {
	
//	OrderInfo getOrderState(String order_num,Long shop_id);
//
//	OrderInfo getOrderState(String order_id);

	OrderInfo orderStateByUser(String order_number, Long id);

}
