package com.boye.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.BillItem;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.vo.Page;
import com.boye.dao.BillItemDao;
import com.boye.dao.OrderDao;
import com.boye.service.IBillItemService;
import com.boye.service.IProvideService;
import com.boye.service.ITaskService;

@Service
public class BillItemServiceImpl implements IBillItemService {
	
	@Autowired
	private BillItemDao billItemDao;
	
	@Autowired
	private IProvideService provideService;
 	
	@Autowired
	private OrderDao orderInfoDao;
	
	@Autowired
	private ITaskService taskService;

	@Override
	public Page<BillItem> billItemList(Map<String, Object> query) {
		Page<BillItem> page = new Page<BillItem>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
        int count = billItemDao.getBillItemListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(billItemDao.getBillItemListByPage(query));
        return page;
	}

	@Override
	@Transactional
	public int completeBillItem(Long billItemId) {
		BillItem findBillItem = billItemDao.getObjectById(new BillItem(billItemId));
		if (findBillItem == null) return -1;
		OrderInfo orderInfo = orderInfoDao.getOrderByPlatformOrderNumber(findBillItem.getInfo());
		if (orderInfo == null) return -2;
		findBillItem.setReturn_state(1);
		if (orderInfo.getOrder_state() != 1) {
			taskService.sendTaskByQuery(orderInfo.getPlatform_order_number());
		}
		return billItemDao.updateByPrimaryKey(findBillItem);
	}

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public int supplementBillItem(BillItem billItem) {
		OrderInfo orderInfo = orderInfoDao.getOrderByPlatformOrderNumber(billItem.getInfo());
		if (orderInfo == null) return -2;
		if (orderInfo.getOrder_state() == 1) return -3;
		ProvideInfo provide = provideService.getProvideByPassagewayId(orderInfo.getPassageway_id());
		if (provide == null || !provide.getProvide_code().equals("zhfh5")) return -4;
		billItem.setReturn_state(1);
		if (orderInfo.getOrder_state() != 1) {
			taskService.sendTaskByQuery(orderInfo.getPlatform_order_number());
		}
		billItem.setApp_id(orderInfo.getCounter_number());
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		billItem.setDate_key(formatter.format(new Date()));
		billItem.setReturn_state(1);
		billItem.setTrade_trans_amount(orderInfo.getMoney().toString());
		return billItemDao.insert(billItem);
	}

}
