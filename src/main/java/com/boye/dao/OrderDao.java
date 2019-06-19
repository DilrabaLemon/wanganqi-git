package com.boye.dao;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.bo.OrderAndAccountBo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.vo.QueryBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface OrderDao extends BaseMapper<OrderInfo> {

//    int addOrder(ShopUserInfo shopUser, OrderInfo order);

	List<Long> getShopIdsByAgent(Long id);

	int getOrderCount(Map<String, Object> query);

	List<OrderInfo> getOrder(Map<String, Object> query);

	OrderInfo getOrderByOrderNumber(@Param("shop_id")Long shop_id, @Param("order_number")String order_number);

	int getShopOrderListCount(QueryBean query);

	List<OrderInfo> getShopOrderList(QueryBean query);

	OrderInfo getOrderByPlatformOrderNumber(String platform_order_number);
	
	OrderInfo getOrderStateByOrderNumber(@Param("order_number")String order_number);

	int setOrderStateByPlatformOrderNumber(OrderInfo order);


	BigDecimal getOrderMoneyByPassagewayId(@Param("passageway_id")Long id, @Param("start_time")String start_time, @Param("end_time")String end_time);

	int getErrorOrderCount(Map<String, Object> query);

	List<OrderInfo> getErrorOrder(Map<String, Object> query);

	OrderAndAccountBo getOrderInfoById(Long order_id);
	
	BigDecimal getCompensateOrderStatisticsByAdmin(@Param("start_time")String start_time, @Param("end_time")String end_time);

	int findOrderErrorCount(Map<String, Object> query);

	List<OrderInfo> findOrderError(Map<String, Object> query);

	BigDecimal getOrderCountByPassagewayId(@Param("passageway_id")Long id, @Param("start_time")String start_time,@Param("end_time") String end_time);

	BigDecimal getOrderSuccessCountByPassagewayId(@Param("passageway_id")Long id, @Param("start_time")String start_time,@Param("end_time") String end_time);

	BigDecimal passagewayCountByShop(@Param("shop_id")Long id, @Param("passageway_id")Long id2, @Param("start_time")String start_time,@Param("end_time") String end_time);

	BigDecimal passagewaySuccessCountByShop(@Param("shop_id")Long id, @Param("passageway_id")Long id2, @Param("start_time")String start_time,@Param("end_time") String end_time);

	String getShopNumberByPlatformOrderNumber(@Param("platform_order_number")Object platform_order_number,@Param("order_number")Object order_number);

	int getOrderListCountByShop(QueryBean query);

	List<OrderInfo> getOrderListByShop(QueryBean query);

	int getFlowStatisticsListByCount(QueryBean query);

	List<OrderInfo> getFlowStatisticsListByPage(QueryBean query);

	OrderInfo orderListStatisticsByShop(Map<String, Object> query);

	int getFlowStatisticsCounterByCount(QueryBean query);

	List<OrderInfo> getFlowStatisticsCounterByPage(QueryBean query);

	int findSuccessCountByPassagewayId(QueryBean query);

	int findFailCountByPassagewayId(QueryBean query);

}
