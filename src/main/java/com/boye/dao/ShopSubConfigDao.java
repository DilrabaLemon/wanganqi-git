package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.ShopSubConfig;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface ShopSubConfigDao extends BaseMapper<ShopSubConfig> {

	ShopSubConfig getShopSubConfigByShopAndPsway(ShopSubConfig shopSubConfig);

	int shopSubConfigListCountNew(QueryBean query);

	List<ShopSubConfig> shopSubConfigListNew(QueryBean query);

	@Delete("delete from shop_sub_config where id = #{id}")
	int deleteShopSubConfig(Long id);

}
