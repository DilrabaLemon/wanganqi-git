package com.boye.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.CpOrderInfo;

@Mapper
public interface CpOrderInfoDao extends BaseMapper<CpOrderInfo> {

//	@Select("select * from cp_order_info where task_state = #{state}")
//	List<CpOrderInfo> findAllByTaskState(int state);

	@Update("update cp_order_info set task_state = #{state} where id = #{id}")
	void changeTaskState(@Param("state")int state,@Param("id") Long id) ;

	@Delete("delete from cp_order_info where id = #{id}")
	void deleteCpOrder(Long id);

	@Update("update cp_order_info set task_state = #{state}, return_count = return_count + 1, next_send_time = #{nextTime} where id = #{id}")
	void changeTaskStateByFail(@Param("state")int state, @Param("id")Long id, @Param("nextTime") Date date);

	@Select("select * from cp_order_info where task_state = #{state} and next_send_time < #{now} limit 0, 100")
	List<CpOrderInfo> findAllSendCpOrder(@Param("state")int state, @Param("now")Date now);

}
