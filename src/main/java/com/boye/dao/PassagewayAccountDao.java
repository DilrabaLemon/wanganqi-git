package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PassagewayAccount;

@Mapper
public interface PassagewayAccountDao extends BaseMapper<PassagewayAccount> {

	List<PassagewayAccount> getAccountByPlatformOrderNumberAndType(@Param("platform_order_number")String platform_order_number, @Param("type")Integer type);

	List<PassagewayAccount> getAccountByPlatformOrderNumber(String platform_order_number);

}
