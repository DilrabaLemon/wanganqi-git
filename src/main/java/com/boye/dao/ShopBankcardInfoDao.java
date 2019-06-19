package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.NoticeInfo;
import com.boye.bean.entity.ShopBankcardInfo;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface ShopBankcardInfoDao extends BaseMapper<ShopBankcardInfo> {

	@Select("select * from shop_bankcard_info where delete_flag = 0 and shop_id = #{shop_id}")
	List<ShopBankcardInfo> getShopBankcardInfoAllByShopId(Long shop_id);

	int getShopBankcardInfoListByCount(QueryBean query);

	List<ShopBankcardInfo> getShopBankcardInfoListByPage(QueryBean query);

}
