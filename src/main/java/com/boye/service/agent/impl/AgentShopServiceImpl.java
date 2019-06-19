package com.boye.service.agent.impl;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopUserAuditing;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.ShopInformationBean;
import com.boye.dao.ShopConfigDao;
import com.boye.dao.ShopUserAuditingDao;
import com.boye.dao.ShopUserDao;
import com.boye.service.agent.AgentShopService;
import com.boye.service.impl.BaseServiceImpl;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AgentShopServiceImpl extends BaseServiceImpl implements AgentShopService {
	
	@Resource
    private ShopConfigDao shopConfigDao;
	
	@Resource
    private ShopUserDao shopUserDao;
	
	@Resource
	private ShopUserAuditingDao shopUserAuditingDao;
	
	@Override
	public int editShopConfigByAgent(ShopConfig shopConfig) {
		// 从数据库查询商户配置
		ShopConfig findShopConfig = shopConfigDao.getObjectById(shopConfig);
		// 判断查询到的商户配置是否为空
        if (findShopConfig == null) return -1;
        // 修改商户费率
        findShopConfig.setAgent_rate(shopConfig.getAgent_rate());
        return shopConfigDao.updateByPrimaryKey(findShopConfig);
	}

	@Override
	public Page<ShopInformationBean> shopUserPageByAgent(AgentInfo agent, QueryBean query) {
		 	if (agent.getId() == null) return null;
		 	// 创建商户信息 分页对象
	        Page<ShopInformationBean> page = new Page<>(query.getPage_index(), query.getPage_size());
	        // 设置查询的主要条件
	        query.setMain_condition(agent.getId().toString());
	        // 查询商户信息列表记录数
	        int count = shopUserDao.getShopInformationCountByAgent(query);
	        page.setTotals(count);
	        if (count == 0)
	            page.setDatalist(new ArrayList<>());
	        else {
	        	 // 查询商户信息列表记录
	            page.setDatalist(shopUserDao.getShopInformationPageByAgent(query));
	            setConfigToDatalist(page.getDatalist());
	        }
	        return page;
	}
	
	private void setConfigToDatalist(List<ShopInformationBean> datalist) {
        if (datalist == null) return;
        for (ShopInformationBean shopInfo : datalist) {
        	// 查询商户配置信息 设置商户信息
            shopInfo.setShopConfigs(shopConfigDao.getShopConfigByShopId(shopInfo.getId()));
        }
    }

	@Override
	public ShopInformationBean getShopInfoByAgent(String shop_id) {
		// 查询商户信息
		ShopInformationBean shopInfo = shopUserDao.getShopInformationByAgent(Long.parseLong(shop_id));
		// 查询商户配置信息
        shopInfo.setShopConfigs(shopConfigDao.getShopConfigByShopId(shopInfo.getId()));
        return shopInfo;
	}

	@Override
	public ShopUserInfo getShopUserByMobile(String mobile) {
		ShopUserInfo shopUser = shopUserDao.getShopUserByLoginNumber(mobile);
		return shopUser;
	}
	
	@Override
	public int insertShopAuditingByAgent(ShopUserAuditing shopUserAuditing) {
		// 添加待审核的商户
		int result = shopUserAuditingDao.insert(shopUserAuditing);
		return result;
	}
}
