package com.boye.dao;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.QrCodeRedis;

@Mapper
public interface QrCodeRedisDao extends BaseMapper<QrCodeRedis>{

	QrCodeRedis getQrCodeRedisByKey(String redis_key);

}
