package com.boye.service.shop.impl;

import java.util.ArrayList;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.ShopLoginRecord;
import com.boye.bean.entity.ShopOperationRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.LogDao;
import com.boye.service.impl.BaseServiceImpl;
import com.boye.service.shop.ShopLogService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ShopLogServiceImpl extends BaseServiceImpl implements ShopLogService{
	
	@Resource
    private LogDao logDao;
	
	@Override
	public Page<?> getShopLoginRecordListByShop(ShopUserInfo shopUser, QueryBean query) {
		// 设置主要条件
		String main_condition = shopUser.getId().toString();
		query.setMain_condition(main_condition);
		return getShopLoginRecordList(query);
	}

	@Override
	public Page<?> getShopOperationRecordListByShop(ShopUserInfo shopUser, QueryBean query) {
		String main_condition = shopUser.getId().toString();
		query.setMain_condition(main_condition);
		return getShopOperationRecordList(query);
	}
	
	private Page<ShopOperationRecord> getShopOperationRecordList(QueryBean query) {
		//获取商户操作记录
		Page<ShopOperationRecord> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = logDao.getShopOperationRecordCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(logDao.getShopOperationRecord(query));
        return page;
	}
	
	private Page<ShopLoginRecord> getShopLoginRecordList(QueryBean query) {
		// 创建分页数据对象
		Page<ShopLoginRecord> page = new Page<>(query.getPage_index(), query.getPage_size());
		// 查询登录记录记录数
        int count = logDao.getShopLoginRecordCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
        	// 查询登录记录记录数据
            page.setDatalist(logDao.getShopLoginRecord(query));
        return page;
	}
}
