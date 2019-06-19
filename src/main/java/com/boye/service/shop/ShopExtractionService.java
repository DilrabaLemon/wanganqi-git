package com.boye.service.shop;

import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface ShopExtractionService {
	
	int addExtractionRecord(ShopUserInfo shopUser, ExtractionRecord extract);
	
	Page<ExtractionRecord> extractionListByShop(ShopUserInfo shopUser, Integer state, QueryBean query);

	ExtractionRecord getExtractionByExtractionNumber(String extraction_number);

	int addRechargeExtractionRecord(ShopUserInfo shopUser, ExtractionRecord extract);
}
