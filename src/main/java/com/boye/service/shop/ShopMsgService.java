package com.boye.service.shop;

import java.util.Map;

import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SystemMsg;
import com.boye.bean.vo.Page;

public interface ShopMsgService {

	Page<SystemMsg> findSystemMsgListByShop(ShopUserInfo shopUser, Map<String, Object> query);

	SystemMsg getSystemMsgByShop(Long id);

	int deleteSystemMsgByShop(Long id);

}
