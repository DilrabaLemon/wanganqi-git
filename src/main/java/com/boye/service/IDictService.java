package com.boye.service;

import java.util.List;
import java.util.Map;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.DictTable;
import com.boye.bean.vo.Page;

public interface IDictService {

	String getObjectByKeyAndType(String string, int i);

	int insertDictTable(DictTable dictTable);

	int deleteDictTable(Long id);

	int updateDictTable(DictTable dictTable);

	Page<DictTable> selectDictTable(Map<String, Object> query);

	DictTable getDictTable(Long id);

	int updateDictTableByExtration(DictTable dictTable);

	int insertDictTableByNumber(AdminInfo admin, DictTable dictTable);

	DictTable getSubPaymentAccount(String code);

	List<DictTable> getDictByType(Integer type);
	

}
