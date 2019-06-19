package com.boye.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boye.bean.entity.PassagewayConfig;
import com.boye.bean.entity.ShopBankcardInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.PassagewayConfigDao;
import com.boye.service.IMappingPassagewayConfigService;

@Service
public class MappingPassagewayConfigServiceImpl implements IMappingPassagewayConfigService {
	
	@Autowired
	private PassagewayConfigDao passagewayConfigDao;

	@Override
	public int add(PassagewayConfig passagewayConfig) {
		if (StringUtils.isNotBlank(passagewayConfig.getShop_ids())) {
			return addAll(passagewayConfig);
		}
		PassagewayConfig findConfig = passagewayConfigDao.findByIdAndSubIdAndShopId(passagewayConfig);
		if (findConfig != null) return -2;
		passagewayConfig.setEnable(0);
		return passagewayConfigDao.insert(passagewayConfig);
	}

	public int addAll(PassagewayConfig passagewayConfig) {
		List<PassagewayConfig> allConfig = getConfigList(passagewayConfig);
		if(passagewayConfigDao.insertAll(allConfig) == allConfig.size()) {
			return 1;
		}
		return 0;
	}

	private List<PassagewayConfig> getConfigList(PassagewayConfig passagewayConfig) {
		List<PassagewayConfig> result = new ArrayList<PassagewayConfig>();
		String[] ids =passagewayConfig.getShop_ids().split(",");
		for(String id : ids) {
			PassagewayConfig itemConfig = (PassagewayConfig) passagewayConfig.clone();
			if (id.equals("0")) {
				PassagewayConfig findConfig = passagewayConfigDao.findByIdAndSubIdAndShopId(itemConfig);
				if (findConfig != null) continue;
			}
			if (itemConfig == null) continue;
			itemConfig.setShop_id(Long.parseLong(id));
			result.add(itemConfig);
		}
		return result;
	}

	@Override
	public int edit(PassagewayConfig passagewayConfig) {
		PassagewayConfig findConfig = passagewayConfigDao.getObjectById(passagewayConfig);
		if (findConfig == null) return -1; 
		findConfig.setShop_id(passagewayConfig.getShop_id());
		findConfig.setMax_money(passagewayConfig.getMax_money());
		findConfig.setMin_money(passagewayConfig.getMin_money());
		findConfig.setConfig_describe(passagewayConfig.getConfig_describe());
		findConfig.setMapping_passageway_id(passagewayConfig.getMapping_passageway_id());
		return passagewayConfigDao.updateByPrimaryKey(passagewayConfig);
	}

	@Override
	public int delete(Long id) {
		PassagewayConfig findConfig = passagewayConfigDao.getObjectById(new PassagewayConfig(id));
		if (findConfig == null) return -1;
		return passagewayConfigDao.delete(id);
	}

	@Override
	public Page<PassagewayConfig> queryPage(QueryBean query) {
		Page<PassagewayConfig> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = passagewayConfigDao.getPassagewayConfigListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(passagewayConfigDao.getPassagewayConfigListByPage(query));
        return page;
	}

	@Override
	public PassagewayConfig findById(Long id) {
		PassagewayConfig findConfig = passagewayConfigDao.getObjectById(new PassagewayConfig(id));
		return findConfig;
	}

	@Override
	public int enable(Long id) {
		PassagewayConfig findConfig = passagewayConfigDao.getObjectById(new PassagewayConfig(id));
		if (findConfig == null) return -1; 
		findConfig.setEnable(0);
		return passagewayConfigDao.updateByPrimaryKey(findConfig);
	}

	@Override
	public int disuse(Long id) {
		PassagewayConfig findConfig = passagewayConfigDao.getObjectById(new PassagewayConfig(id));
		if (findConfig == null) return -1; 
		findConfig.setEnable(1);
		return passagewayConfigDao.updateByPrimaryKey(findConfig);
	}

}
