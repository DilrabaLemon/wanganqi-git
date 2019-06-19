package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.ShopUserAuditing;
import com.boye.bean.vo.QueryBean;
@Mapper
public interface ShopUserAuditingDao  extends BaseMapper<ShopUserAuditing>{
	//查询记录数
	int getShopUserAuditingCount(QueryBean query);
	// 查询分页数据
	List<ShopUserAuditing> getShopUserAuditingByPage(QueryBean query);
	// 根据id查询待审核商户
	ShopUserAuditing findShopUserAuditing(Integer shopUserAuditing_id);

}
