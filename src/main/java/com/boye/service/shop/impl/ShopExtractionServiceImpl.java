package com.boye.service.shop.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.ExtractionDao;
import com.boye.service.ITaskService;
import com.boye.service.impl.BaseServiceImpl;
import com.boye.service.shop.ShopExtractionService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ShopExtractionServiceImpl extends BaseServiceImpl implements ShopExtractionService {
	
	@Resource
    private ExtractionDao extractionDao;
	
	@Autowired
	private ITaskService taskService;
	
	@Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public int addExtractionRecord(ShopUserInfo shopUser, ExtractionRecord extract) {
    	extract.setShop_id(shopUser.getId());
    	//判断用户是否异常
    	if (extract.getExtraction_money().compareTo(new BigDecimal(500000)) == 1) return -21;
    	if (shopUser.getExamine() != 0) return -12;
    	String code = UUID.randomUUID().toString().replaceAll("-", "");
    	extract.setExtraction_number(code);
    	// 设置提现状态,为未审核
    	extract.setState(-1);//临时存储提现申请
    	//设置逻辑删除标识
    	extract.setDelete_flag(0);
    	// 设置 从余额中扣除手续费
    	extract.setService_type(1);
    	if (extractionDao.insert(extract) == 1) {
    		Map<String, Object> result = taskService.sendExtractionSubmitServer(extract.getExtraction_number());
    		if (result.get("code").equals(1.0)) {
    			return 1;
    		} else {
    			extract = extractionDao.getExtractionByNumber(extract.getExtraction_number());
    			if (extract != null) {
    				extract.setDelete_flag(1);
    				extractionDao.updateByPrimaryKey(extract);
    			}
    			return ((Double)result.get("code")).intValue();
    		}
    	}
    	return 0;
    }
	
	@Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public int addRechargeExtractionRecord(ShopUserInfo shopUser, ExtractionRecord extract) {
    	extract.setShop_id(shopUser.getId());
    	//判断用户是否异常
    	extract.setState(-1);//临时存储提现申请
    	//设置逻辑删除标识
    	extract.setDelete_flag(0);
    	// 设置 从余额中扣除手续费
    	if (extractionDao.insert(extract) == 1) {
    		Map<String, Object> result = taskService.sendExtractionSubmitServer(extract.getExtraction_number());
    		if (result.get("code").equals(1.0)) {
    			return 1;
    		} else {
    			extract = extractionDao.getExtractionByNumber(extract.getExtraction_number());
    			if (extract != null) {
    				extract.setDelete_flag(1);
    				extractionDao.updateByPrimaryKey(extract);
    			}
    			return ((Double)result.get("code")).intValue();
    		}
    	}
    	return 0;
    }
	
	 @Override
	    public Page<ExtractionRecord> extractionListByShop(ShopUserInfo shopUser, Integer state, QueryBean query) {
	    	query.setMain_condition(shopUser.getId().toString());
	        return extractionList(state, query);
	    }
	 
	 private Page<ExtractionRecord> extractionList(Integer state, QueryBean query) {
	    	// 设置状态码
	    	if (state == null) state = 0;
	    	query.setState(state);
	    	// 创建分页对象
	    	Page<ExtractionRecord> page = new Page<ExtractionRecord>(query.getPage_index(), query.getPage_size());
	    	// 获取总记录数
	        int count = extractionDao.getExtractionRecordCount(query);
	        page.setTotals(count);
	        // 获取数据列表
	        if (count == 0)
	            page.setDatalist(new ArrayList<>());
	        else
	            page.setDatalist(extractionDao.getExtractionRecord(query));
	        return page;
	    }

	@Override
	public ExtractionRecord getExtractionByExtractionNumber(String extraction_number) {
		return extractionDao.getExtractionByNumber(extraction_number);
	}
}
