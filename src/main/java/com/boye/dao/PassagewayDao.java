package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.bo.PassagewayHasConfigBo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface PassagewayDao extends BaseMapper<PassagewayInfo> {

	PassagewayInfo getPassagewayByCode(String passageway_code);
	// 条件查询通道列表记录数
	int getPassagewayListByCount(QueryBean query);
	// 条件查询通道列表分页数据
	List<PassagewayInfo> getPassagewayListByPage(QueryBean query);
	// 条件查询通道列表分页数据（包含映射通道配置信息）
	List<PassagewayHasConfigBo> getPassagewayHasConfigListByPage(QueryBean query);
	// 获取所有的通道列表
	List<PassagewayInfo> findAllPassageway();
	
	List<PassagewayInfo> getPassagewayByProvideId(Long provide_id);
	
	List<PassagewayInfo> getPassagewayByShopIdAndProvideId(@Param("shop_id") Long shop_id, @Param("passageway_id") Long passageway_id);

	PassagewayInfo getPassagewayById(Long passageway_id);
	
	int getSubPaymentPassagewayListByCount(QueryBean query);
	
	List<PassagewayInfo> getSubPaymentPassagewayListByPage(QueryBean query);
	
	List<PassagewayInfo> getAllByType(@Param("type") Integer type);
	

	List<PassagewayInfo> findPassagewayListByCode(String passageway_code);

	int getEtrPaymentPassagewayListByCount(QueryBean query);
	
	List<PassagewayInfo> getEtrPaymentPassagewayListByPage(QueryBean query);
	
	int getPassagewayHasConfigListByCount(QueryBean query);
	
	List<PassagewayInfo> findPassagewayNotUserAndPassagewayId(@Param("shop_id")Long shopId, @Param("passageway_id")Long passagewayId, @Param("type")int type);

}
