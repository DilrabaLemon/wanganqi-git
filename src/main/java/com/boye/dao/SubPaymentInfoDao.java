package com.boye.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.SubPaymentInfo;

@Mapper
public interface SubPaymentInfoDao extends BaseMapper<SubPaymentInfo> {

	int getSubPaymentInfoListByCount(Map<String, Object> query);

	List<SubPaymentInfo> getSubPaymentInfoList(Map<String, Object> query);

	int getErrorSubPaymentInfoCount(Map<String, Object> query);

	List<SubPaymentInfo> getErrorSubPaymentInfo(Map<String, Object> query);

	int findSubPaymentInfoErrorCount(Map<String, Object> query);

	List<SubPaymentInfo> findSubPaymentInfoError(Map<String, Object> query);

	int getSubPaymentListByShopCount(Map<String, Object> query);

	List<SubPaymentInfo> getSubPaymentListByShop(Map<String, Object> query);

	int getSubOrderListByAgentCount(Map<String, Object> query);

	List<SubPaymentInfo> getSubOrderListByAgent(Map<String, Object> query);

	SubPaymentInfo getSubPaymentByShopIdAndShopSubNumber(@Param("shop_id")Long shop_id, @Param("shop_sub_number")String shop_sub_number);

	SubPaymentInfo getSubPaymentBySubPaymentNumber(@Param("sub_payment_number")String sub_payment_number);
	
	@Select("select * from sub_payment_info where delete_flag = 0 and shop_sub_number = #{shop_sub_number}")
	SubPaymentInfo getSubPaymentByshopSubNumber(@Param("shop_sub_number")String shop_sub_number);

}
