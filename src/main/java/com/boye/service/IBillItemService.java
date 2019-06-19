package com.boye.service;

import java.util.Map;

import com.boye.bean.entity.BillItem;
import com.boye.bean.vo.Page;

public interface IBillItemService {

	Page<BillItem> billItemList(Map<String, Object> query);

	int completeBillItem(Long billItemId);

	int supplementBillItem(BillItem billItem);

}
