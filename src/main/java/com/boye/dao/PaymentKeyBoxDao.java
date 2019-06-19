package com.boye.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PaymentKeyBox;

@Mapper
public interface PaymentKeyBoxDao extends BaseMapper<PaymentKeyBox>  {

	PaymentKeyBox getKeyBoxByPaymentId(@Param("id") Long id);

	@Update("UPDATE payment_key_box SET private_key = #{private_key}, public_key = #{public_key} WHERE payment_id = #{payment_id} ")
	int updateKeyBox(PaymentKeyBox keyBox);

	@Insert("INSERT INTO `payment_key_box` (`delete_flag`,`private_key`,`create_time`,`payment_id`,`public_key`) VALUES (#{delete_flag},#{private_key},#{create_time},#{payment_id},#{public_key})")
	int insertInfoToKeyBox(PaymentKeyBox keyBox);

}
