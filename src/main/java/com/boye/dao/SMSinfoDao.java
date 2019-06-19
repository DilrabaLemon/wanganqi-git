package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.SMSinfo;

@Mapper
public interface SMSinfoDao extends BaseMapper<SMSinfo> {

	List<SMSinfo> getSmsInfoByMobile(String mobile);

}
