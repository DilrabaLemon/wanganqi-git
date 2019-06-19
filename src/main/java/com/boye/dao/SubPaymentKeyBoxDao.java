package com.boye.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PaymentKeyBox;
import com.boye.bean.entity.SubPaymentKeyBox;

@Mapper
public interface SubPaymentKeyBoxDao extends BaseMapper<SubPaymentKeyBox>  {

	@Select("select * from sub_payment_key_box where payment_id = #{id} and delete_flag=0")
	SubPaymentKeyBox getKeyBoxByPaymentId(@Param("id") Long id);

}
