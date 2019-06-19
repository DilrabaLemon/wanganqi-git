package com.boye.service.shop.impl;

import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.vo.Page;
import com.boye.dao.SubPaymentInfoDao;
import com.boye.service.impl.BaseServiceImpl;
import com.boye.service.shop.ShopSubOrderService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ShopSubOrderServiceImpl extends BaseServiceImpl implements ShopSubOrderService{
	
	private static Logger logger = LoggerFactory.getLogger(ShopSubOrderServiceImpl.class);
	
	@Autowired
	private SubPaymentInfoDao subPaymentInfoDao;
	
	@Override
	public Page<SubPaymentInfo> subPaymentListByShop(ShopUserInfo shopUser, Map<String, Object> query) {
		// 固定查询该商户代付订单
		query.put("shop_id", shopUser.getId());
		Page<SubPaymentInfo> page = new Page<SubPaymentInfo>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		int count = subPaymentInfoDao.getSubPaymentListByShopCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<SubPaymentInfo>());
        else
            page.setDatalist(subPaymentInfoDao.getSubPaymentListByShop(query));
        return page;
	}
	
}
