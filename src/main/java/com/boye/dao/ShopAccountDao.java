package com.boye.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.ShopAccount;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface ShopAccountDao extends BaseMapper<ShopAccount> {

	List<ShopAccount> getShopAccountByOrderId(Long order_id);

	int getShopAccountCountByUserId(Long shop_id);
	// 条件查询商户流水 总记录
	int getShopAccountListByCount(QueryBean query);
	// 条件查询商户流水 分页数据
	List<ShopAccount> getShopAccountListByPage(QueryBean query);

	List<ShopAccount> shopAccountStatisticeByShop(QueryBean query);
}
