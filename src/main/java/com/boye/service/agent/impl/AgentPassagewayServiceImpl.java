package com.boye.service.agent.impl;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.PassagewayDao;
import com.boye.dao.ShopConfigDao;
import com.boye.dao.ShopUserDao;
import com.boye.service.agent.AgentPassagewayService;
import com.boye.service.impl.BaseServiceImpl;
@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AgentPassagewayServiceImpl extends BaseServiceImpl implements AgentPassagewayService{
	
	@Resource
    private ShopUserDao shopUserDao;
	
	@Resource
    private ShopConfigDao shopConfigDao;
	
	@Autowired
    private PassagewayDao passagewayDao;
	
	@Override
	public Page<ShopConfig> shopConfigPageByAgent(AgentInfo agent, QueryBean query, String shop_id) {
		// 根据商户id 查询商户信息
		if (shop_id == null) shop_id = "-1";
		ShopUserInfo shopUser = shopUserDao.getObjectById(new ShopUserInfo(Long.parseLong(shop_id)));
		// 判断商户是否为空
        // 判断商户关联的代理商 是否为空 和 商户关联的代理商是否和登入的代理商一致
        if (shopUser == null || shopUser.getAgent_id() == null || !shopUser.getAgent_id().equals(agent.getId())) shop_id = "-1";
        return shopConfigList(query, shop_id);
	}
	
		private  Page<ShopConfig> shopConfigList(QueryBean query, String shop_id) {
		// 商户id设置到查询条件
        String main_condition = "shop_id = " + shop_id;
        query.setMain_condition(main_condition);
        // 该代理商名下的商户配置列表分页对象
        Page<ShopConfig> page = new Page<ShopConfig>(query.getPage_index(), query.getPage_size());
        // 根据条件查询 该代理商名下的商户配置记录数
        int count = shopConfigDao.getObjectCount(new ShopConfig(), query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<ShopConfig>());
        else
        	// 获取该代理商的名下的商户配置列表
            page.setDatalist(shopConfigDao.getObjectByPage(new ShopConfig(), query));
        for (ShopConfig shopConfig : page.getDatalist()) {
            if (shopConfig.getPassageway_id() != null) {
            	// 根据商户配置中的商户id 为商户配置上添加上通道    根据通道id设置通道名
                shopConfig.setPassageway(passagewayDao.getObjectById(new PassagewayInfo(shopConfig.getPassageway_id())));
            }
        }
        return page;
    } 

}
