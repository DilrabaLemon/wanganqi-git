package com.boye.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.CpSubPaymentInfoFail;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface CpSubPaymentInfoFailDao extends BaseMapper<CpSubPaymentInfoFail>{

	int getCpSubPaymentInfoFailCount(Map<String, Object> query);

	List<CpSubPaymentInfoFail> getCpSubPaymentInfoFail(Map<String, Object> query);

}
