package com.boye.dao;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PayServerInfo;

@Mapper
public interface PayServerInfoDao extends BaseMapper<PayServerInfo> {

	PayServerInfo getPayServerInfoByCode(String send_code);

}
