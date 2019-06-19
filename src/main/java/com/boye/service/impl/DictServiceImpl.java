package com.boye.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.DictTable;
import com.boye.bean.vo.Page;
import com.boye.dao.DictDao;
import com.boye.service.IDictService;
import com.boye.service.RedisService;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class DictServiceImpl extends BaseServiceImpl implements IDictService {
	
	private static Logger logger = LoggerFactory.getLogger(DictServiceImpl.class);
	
	@Autowired
	private DictDao dictDao;
	
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	
    @Autowired
	private RedisService redisService;
	
	@Override
	public String getObjectByKeyAndType(String dict_key, int dict_type) {
		DictTable dict = dictDao.getDictByKeyAndType(dict_key, dict_type);
		if (dict == null) return null;
		return dict.getDict_value();
	}

	@Override
	public int insertDictTable(DictTable dictTable) {
		// 如果字典类型和key已经存在不能重复添加
		DictTable fondDictTable = dictDao.findByDictTypeAndDictName(dictTable.getType(),dictTable.getDict_name());
		if (fondDictTable != null) {
			return 0;
		}
		int result = dictDao.insert(dictTable);
//		if (result != 0) {
//			redisService.delToRedis("dict_type_info_" + dictTable.getType());
//			redisService.delToRedis("dict_public_info");
//		}
		return result;
	}

	@Override
	public int deleteDictTable(Long id) {
		DictTable findDictTable = dictDao.findDictTableById(id);
		if(findDictTable == null) {
			return 0;
		}
		findDictTable.setDelete_flag(1);
		int result = dictDao.updateByPrimaryKey(findDictTable);
//		if (result != 0) {
//			redisService.delToRedis("dict_type_info_" + findDictTable.getType());
//			redisService.delToRedis("dict_public_info");
//		}
		return result;
	}

	@Override
	public int updateDictTable(DictTable dictTable) {
		DictTable findDictTable =dictDao.findDictTableById(dictTable.getId());
		if(findDictTable == null) {
			return 0;
		}
		if (dictTable.getType() != null) {
			findDictTable.setType(dictTable.getType());
		}
		if (dictTable.getDict_name() != null) {
			findDictTable.setDict_name(dictTable.getDict_name());
		}
		if (dictTable.getDict_value() != null) {
			findDictTable.setDict_value(dictTable.getDict_value());
		}
		int result = dictDao.updateByPrimaryKey(findDictTable);
//		if (result != 0) {
//			redisService.delToRedis("dict_type_info_" + findDictTable.getType());
//			redisService.delToRedis("dict_public_info");
//		}
		return result;
	}

	@Override
	public Page<DictTable> selectDictTable(Map<String, Object> query) {
		Page<DictTable> page = new Page<>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		int count=dictDao.findDictTableCount(query);
		 page.setTotals(count);
	        if (count == 0) {
	            page.setDatalist(new ArrayList<>());
	        }else {
	            page.setDatalist(dictDao.findDictTablePage(query));
	        }
	        return page;
	}

	@Override
	public DictTable getDictTable(Long id) {
		return dictDao.findDictTableById(id);
	}

	@Override
	public int updateDictTableByExtration(DictTable dictTable) {
		DictTable findDictTable =dictDao.findDictTableById(dictTable.getId());
		if(findDictTable == null) {
			return 0;
		}
		
		// 修改可用手机号
		findDictTable.setDict_value(dictTable.getDict_value());
		int result = dictDao.updateByPrimaryKey(findDictTable);
		return result;
	}

	@Override
	public int insertDictTableByNumber(AdminInfo admin, DictTable dictTable) {
		dictTable.setDict_name(admin.getId()+"");
		int result = dictDao.insert(dictTable);
		return result;
	}

	@Override
	public DictTable getSubPaymentAccount(String code) {
		DictTable dictTable = dictDao.getSubPaymentAccount(code,8);
		return dictTable;
	}

	@Override
	public List<DictTable> getDictByType(Integer type) {
		if(type == null) return dictDao.getDictByPublic();
		return dictDao.getDictByType(type);
	}

	@SuppressWarnings("unchecked")
	private List<DictTable> getDictByTypeFromRedis(Integer type) {
		List<DictTable> dictTypeInfoList = (List<DictTable>) redisTemplate.opsForValue().get("dict_type_info_" + type);
		// 如果从redis中取不到，再从数据库取
		if (dictTypeInfoList == null) {
			logger.info("从数据库中获取");
			dictTypeInfoList = dictDao.getDictByType(type);
			// 重新更新redis，将所有的商户信息存入redis
	        redisService.setToRedis("dict_type_info_" + type, dictTypeInfoList);
		}
		return dictTypeInfoList;
	}

	@SuppressWarnings("unchecked")
	private List<DictTable> getDictPublicInfo() {
		List<DictTable> dictPulbicInfoList = (List<DictTable>) redisTemplate.opsForValue().get("dict_public_info");
		// 如果从redis中取不到，再从数据库取
		if (dictPulbicInfoList == null) {
			logger.info("从数据库中获取");
			dictPulbicInfoList = dictDao.getDictByPublic();
			// 重新更新redis，将所有的商户信息存入redis
	        redisService.setToRedis("dict_public_info", dictPulbicInfoList);
		}
		return dictPulbicInfoList;
	}

}
