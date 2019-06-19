package com.boye.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.BillItem;
import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface BillItemDao extends BaseMapper<BillItem> {
	
	List<ExtractionRecord> getExtractionRecord(QueryBean query);

	int getBillItemListByCount(Map<String, Object> query);

	List<BillItem> getBillItemListByPage(Map<String, Object> query);

	@Select("select * from bill_item  where info = #{platformOrderNumber} and delete_flag = 0")
	BillItem getByPlatformOrderNumber(String platformOrderNumber);

}
