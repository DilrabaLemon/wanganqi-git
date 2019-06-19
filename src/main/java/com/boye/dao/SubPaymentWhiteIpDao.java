package com.boye.dao;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.SubPaymentWhiteIp;

@Mapper
public interface SubPaymentWhiteIpDao extends BaseMapper<SubPaymentWhiteIp>{

	SubPaymentWhiteIp getSubPaymentWhiteIpByShopId(Long shop_id);

}
