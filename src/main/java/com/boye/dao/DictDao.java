package com.boye.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.DictTable;

@Mapper
public interface DictDao extends BaseMapper<DictTable> {

	DictTable getDictByKeyAndType(@Param("dict_key")String dict_key, @Param("dict_type")int dict_type);
	
	// 根据id查询字典
	DictTable findDictTableById(Long id);
	// 查询分页记录数
	int findDictTableCount(Map<String, Object> query);
	// 查询分页数据
	List<DictTable> findDictTablePage(Map<String, Object> query);

	List<DictTable> getDictByType(@Param("dict_type")int dict_type);

	DictTable getSubPaymentAccount(@Param("dict_name")String code, @Param("dict_type")int dict_type);

	DictTable findByDictTypeAndDictName(@Param("dict_type")Integer dict_type, @Param("dict_name")String dict_name);

	List<DictTable> getDictByPublic();

}
