package com.boye.service.agent;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopUserAuditing;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.ShopInformationBean;

public interface AgentShopService {
	
	int editShopConfigByAgent(ShopConfig shopConfig);
	
	Page<ShopInformationBean> shopUserPageByAgent(AgentInfo agent, QueryBean query);
	
	ShopInformationBean getShopInfoByAgent(String shop_id);
	
	ShopUserInfo getShopUserByMobile(String mobile);
	
	// 添加待审核的商户
	int insertShopAuditingByAgent(ShopUserAuditing shopUserAuditing);
}
