package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.ShopBalanceNew;

@Mapper
public interface ShopBalanceNewDao extends BaseMapper<ShopBalanceNew> {

	@Select("select * from shop_balance_new  where shop_id = #{shop_id} and delete_flag = 0")
	List<ShopBalanceNew> getByShopId(Long shop_id);

	@Select("select * from shop_balance_new  where shop_id = #{shopId} and delete_flag = 0 and balance_type = #{balanceType}")
	ShopBalanceNew getByShopIdAndBalanceType(@Param("shopId")Long shopId, @Param("balanceType")Integer balanceType);

}
