package com.boye.dao;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.LoginWhiteIp;

@Mapper
public interface LoginWhiteIpDao extends BaseMapper<LoginWhiteIp>{

	LoginWhiteIp findLoginWhiteIp(LoginWhiteIp loginWhiteIp);

}
