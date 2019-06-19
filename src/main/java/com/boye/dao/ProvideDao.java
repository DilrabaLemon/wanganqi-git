package com.boye.dao;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.ProvideInfo;

@Mapper
public interface ProvideDao extends BaseMapper<ProvideInfo> {

	ProvideInfo getProvideByCode(String code);

}
