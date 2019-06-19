package com.boye.service.shop;

import com.boye.bean.entity.ShopUserInfo;

public interface ShopRefundService {
	
	int submitRefund(ShopUserInfo shopUser, String order_id);
}
