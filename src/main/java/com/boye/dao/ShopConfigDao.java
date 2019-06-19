package com.boye.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.bo.ShopConfigInfoBean;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface ShopConfigDao extends BaseMapper<ShopConfig> {

	List<ShopConfig> getShopConfigByShopMap(Map<String, Object> shopInfo);

	List<ShopConfig> getShopConfigByShopId(Long id);

	ShopConfig bankCardRateByShopId(Long shop_id);

	ShopConfig getShopConfigByShopAndPsway(ShopConfig shopConfig);

	ShopConfig getBankShopConfigByShopId(Long shop_id);
	// 条件查询商户配置 分页数据
	List<ShopConfigInfoBean> shopConfigListByShop(QueryBean query);
	// 条件查询商户配置  总记录数
	int shopConfigListCountByShop(QueryBean query);

	List<ShopConfig> findShopConfigByShopId(Long shop_id);

	List<ShopConfig> findAll();

	int shopConfigListCountNew(QueryBean query);

	List<ShopConfig> shopConfigListNew(QueryBean query);

	@Delete("delete from shop_config where id = #{id}")
	int deleteShopConfig(Long id);
}
