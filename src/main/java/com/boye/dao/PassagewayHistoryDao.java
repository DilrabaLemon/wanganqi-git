package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PassagewayHistory;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface PassagewayHistoryDao extends BaseMapper<PassagewayHistory>{

	List<PassagewayHistory> getPassagewayHistory(QueryBean query);

}
