package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.RefundRecord;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface RefundDao extends BaseMapper<RefundRecord> {
	// 条件查询退款记录   总记录

	int getRefundRecordCount(QueryBean query);
	// 条件查询退款记录   分页数据
	List<RefundRecord> getRefundRecord(QueryBean query);

}
