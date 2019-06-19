package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PlatformExtractionRecord;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface PlatformExtractionRecordDao extends BaseMapper<PlatformExtractionRecord>{

	int extractionListByAdminCount(QueryBean query);

	List<PlatformExtractionRecord> extractionListByAdminPage(QueryBean query);
	
	
}
