package com.boye.service.shop;

import java.util.Map;

import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.GetQRReturnMessage;

public interface ShopOrderService {
	
	GetQRReturnMessage addOrderInfo(ShopUserInfo shopUser, OrderInfo order);
	
	Page<OrderInfo> orderListByShop(ShopUserInfo shopUser, QueryBean query);

	Page<OrderInfo> orderRechargeListByShop(ShopUserInfo shopUser, QueryBean query);

	OrderInfo orderListStatisticsByShop(ShopUserInfo shopUser, Map<String, Object> query);

}
