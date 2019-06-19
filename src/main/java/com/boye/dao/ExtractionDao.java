package com.boye.dao;

import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.QueryBean;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.boye.base.mapper.BaseMapper;

@Mapper
public interface ExtractionDao extends BaseMapper<ExtractionRecord> {

	ExtractionRecord getExtractionByNumber(String extraction_number);
	// 条件查询提现记录 总记录数
	int getExtractionRecordCount(QueryBean query);
	// 条件查询提现记录 分页数据
	List<ExtractionRecord> getExtractionRecord(QueryBean query);

	int getExtractionCountByUserId(Long shop_id);
}
