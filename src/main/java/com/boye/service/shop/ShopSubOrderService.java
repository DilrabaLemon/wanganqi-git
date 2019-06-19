package com.boye.service.shop;

import java.util.Map;

import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.vo.Page;

public interface ShopSubOrderService {
	
	Page<SubPaymentInfo> subPaymentListByShop(ShopUserInfo shopUser, Map<String, Object> query);

}
