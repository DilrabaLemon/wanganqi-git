package com.boye.service.shop;

import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface ShopLogService {
	
	Page<?> getShopLoginRecordListByShop(ShopUserInfo shopUser, QueryBean query);
	
	Page<?> getShopOperationRecordListByShop(ShopUserInfo shopUser, QueryBean query);
	
}
