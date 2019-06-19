package com.boye.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.base.constant.Constants;
import com.boye.bean.bo.OrderAndAccountBo;
import com.boye.bean.entity.AgentAccount;
import com.boye.bean.entity.CpOrderInfoFail;
import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.PlatformAccount;
import com.boye.bean.entity.PlatformIncomeAccount;
import com.boye.bean.entity.ShopAccount;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.utils.CommonUtils;
import com.boye.dao.AgentAccountDao;
import com.boye.dao.CpOrderInfoFailDao;
import com.boye.dao.ExtractionDao;
import com.boye.dao.OrderDao;
import com.boye.dao.PaymentDao;
import com.boye.dao.PlatformAccountDao;
import com.boye.dao.PlatformIncomeAccountDao;
import com.boye.dao.ShopAccountDao;
import com.boye.dao.ShopUserDao;
import com.boye.service.IOrderService;
import com.boye.service.ITaskService;

@Service
@Transactional(propagation = Propagation.NOT_SUPPORTED)
public class OrderServiceImpl extends BaseServiceImpl implements IOrderService {
	
	private static Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

	@Resource
	private OrderDao orderDao;
	
	@Autowired
	private ITaskService taskService;
	
	@Autowired
	private ShopUserDao shopUserDao;
	
	@Autowired
	private CpOrderInfoFailDao cpOrderInfoFailDao;
	
	@Autowired
	private PlatformAccountDao platformAccountDao;
	
	@Autowired
	private ShopAccountDao shopAccountDao;
	
	@Autowired
	private AgentAccountDao agentAccountDao;
	
	@Autowired
	private PlatformIncomeAccountDao platformIncomeDao;
	
	@Autowired
	private PaymentDao paymentDao;
	
	@Override
	public Page<OrderInfo> orderList(Map<String, Object> query) {
		Page<OrderInfo> page = new Page<OrderInfo>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		int count = orderDao.getOrderCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<OrderInfo>());
        else {
        	page.setDatalist(orderDao.getOrder(query));
        }
        return page;
	}
	
	@Override
	public Page<OrderInfo> getRechargeOrderList(QueryBean query) {
		Page<OrderInfo> page = new Page<>(query.getPage_index(), query.getPage_size());
		int count=orderDao.getOrderListCountByShop(query);
		page.setTotals(count);
		if (count == 0) {
           page.setDatalist(new ArrayList<>());
		}else {
           page.setDatalist(orderDao.getOrderListByShop(query));
		}
        return page;
	}
	
	@Override
	public Page<OrderInfo> errorOrderList(Map<String, Object> query) {
		Page<OrderInfo> page = new Page<OrderInfo>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		int count = orderDao.getErrorOrderCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<OrderInfo>());
        else {
        	page.setDatalist(orderDao.getErrorOrder(query));
        }
        return page;
	}
	
	@Override
	public Page<OrderInfo> findOrderErrorList(Map<String, Object> query) {
		Page<OrderInfo> page = new Page<OrderInfo>(query.get("page_index"), query.get("page_size"));
		query.put("page_size", page.getPageSize());
		query.put("start", (page.getCurrent_page() - 1) * page.getPageSize());
		int count = orderDao.findOrderErrorCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<OrderInfo>());
        else {
        	page.setDatalist(orderDao.findOrderError(query));
        }
        return page;
	}
	
	@Override
	public int orderCancellation(String order_id, String psd) {
		Map<String, Object> result = taskService.sendOrderCancellation(order_id);
		return ((Double)result.get("code")).intValue();
	}
	
	@Override
	public int autoOrderCancellation(String psd) {
		Map<String, Object> result = taskService.sendAutoOrderCancellation();
		return ((Double)result.get("code")).intValue();
	}

	@Override
	public int supplementOrder(String order_id, String psd) {
		OrderInfo findOrder = orderDao.getObjectById(new OrderInfo(Long.parseLong(order_id)));
		if (findOrder == null) return -1;
		OrderInfo newOrder = new OrderInfo();
		newOrder.setCounter_number(findOrder.getCounter_number());
		newOrder.setMoney(findOrder.getMoney());
		newOrder.setOrder_number(supplementOrderNumber(findOrder.getOrder_number()));
		newOrder.setOrder_state(0);
		newOrder.setPay_type(findOrder.getPay_type());
		newOrder.setPassageway_id(findOrder.getPassageway_id());
		newOrder.setPayment_id(findOrder.getPayment_id());
		newOrder.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(findOrder.getPlatform_order_number().substring(0, findOrder.getPlatform_order_number().length() - 17)));
		newOrder.setShop_id(findOrder.getShop_id());
		newOrder.setShop_name(findOrder.getShop_name());
		int res = orderDao.insert(newOrder);
		if (res == 1) {
			String result = taskService.sendTaskByQuery(newOrder.getPlatform_order_number());
			if (result.equals("success")) {
				return 1;
			} else {
				return 0;
			}
		}
		return 0;
	}
	
	@Override
	public int supplementCustomAmountOrder(String order_id, BigDecimal amount) {
		OrderInfo findOrder = orderDao.getObjectById(new OrderInfo(Long.parseLong(order_id)));
		if (findOrder == null) return -1;
		OrderInfo newOrder = new OrderInfo();
		newOrder.setCounter_number(findOrder.getCounter_number());
		newOrder.setMoney(amount);
		newOrder.setOrder_number(supplementCustomAmountOrderNumber(findOrder.getOrder_number()));
		newOrder.setOrder_state(0);
		newOrder.setPay_type(findOrder.getPay_type());
		newOrder.setPassageway_id(findOrder.getPassageway_id());
		newOrder.setPayment_id(findOrder.getPayment_id());
		newOrder.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber(findOrder.getPlatform_order_number().substring(0, findOrder.getPlatform_order_number().length() - 17)));
		newOrder.setShop_id(findOrder.getShop_id());
		newOrder.setShop_name(findOrder.getShop_name());
		int res = orderDao.insert(newOrder);
		if (res == 1) {
			String result = taskService.sendTaskByQuery(newOrder.getPlatform_order_number());
			if (result.equals("success")) {
				return 1;
			} else {
				return 0;
			}
		}
		return 0;
	}
	
	@Override
	public int addFlatAccountOrder(OrderInfo order) {
		if (order.getPassageway_id() == null) return -4;
		List<PaymentAccount> paList = paymentDao.getPaymentByPassageway(order.getPassageway_id(), order.getMoney());
		if (order.getShop_id() == null) return -3;
		ShopUserInfo shopUser = shopUserDao.getObjectById(new ShopUserInfo(order.getShop_id()));
		if (shopUser == null) return -3;
		if (paList.size() == 0) return -5;
		PaymentAccount usePayment = paList.get(0);
		OrderInfo newOrder = new OrderInfo();
		newOrder.setCounter_number(usePayment.getCounter_number());
		newOrder.setMoney(order.getMoney());
		newOrder.setOrder_number(createFlatAccountOrderNumber());
		newOrder.setOrder_state(0);
		newOrder.setPay_type(order.getPay_type());
		newOrder.setPassageway_id(order.getPassageway_id());
		newOrder.setPayment_id(usePayment.getId());
		newOrder.setPlatform_order_number(CommonUtils.generatePlatformOrderNumber("FLAT@" + (usePayment.getAccount_number().length() > 15 ? usePayment.getAccount_number().substring(usePayment.getAccount_number().length() - 15) : usePayment.getAccount_number())));
		newOrder.setShop_id(order.getShop_id());
		newOrder.setShop_name(shopUser.getShop_name());
		int res = orderDao.insert(newOrder);
		if (res == 1) {
			String result = taskService.sendTaskByQuery(newOrder.getPlatform_order_number());
			if (result.equals("success")) {
				return 1;
			} else {
				return 0;
			}
		}
		return 0;
	}

	private String createFlatAccountOrderNumber() {
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
		return "PINGZHANG" + format.format(now) + CommonUtils.getUserNumber(6);
	}

	private String supplementCustomAmountOrderNumber(String order_number) {
		return order_number + "@BUDAN" + CommonUtils.getUserNumber(6);
	}

	private String supplementOrderNumber(String order_number) {
		return order_number + "@" + CommonUtils.getUserNumber(6);
	}

	@Override
	public Page<CpOrderInfoFail> getCpOrderInfoFailList(QueryBean query) {
		Page<CpOrderInfoFail> page = new Page<>(query.getPage_index(), query.getPage_size());
		int count=cpOrderInfoFailDao.getCpOrderInfoFailByCount(query);
		page.setTotals(count);
		if (count == 0) {
           page.setDatalist(new ArrayList<>());
		}else {
           page.setDatalist(cpOrderInfoFailDao.getCpOrderInfoFailByPage(query));
		}
		 return page;
	}

	@Override
	public OrderAndAccountBo getOrderAndAccountInfo(String order_id) {
		OrderAndAccountBo oaab = orderDao.getOrderInfoById(Long.parseLong(order_id));
		List<PlatformAccount> platformAccountList = platformAccountDao.getPlatformAccountByOrderId(oaab.getId());
		List<PlatformIncomeAccount> platformIncomeAccountList = platformIncomeDao.getPlatformIncomeByOrderId(oaab.getId());
		List<AgentAccount> agentAccountList = agentAccountDao.getAgentAccountByOrderId(oaab.getId());
		List<ShopAccount> shopAccountList = shopAccountDao.getShopAccountByOrderId(oaab.getId());
		
		oaab.setPlatformAccountList(platformAccountList == null ? new ArrayList<PlatformAccount>() : platformAccountList);
		oaab.setPlatformIncomeAccountList(platformIncomeAccountList == null ? new ArrayList<PlatformIncomeAccount>() : platformIncomeAccountList);
		oaab.setAgentAccountList(agentAccountList == null ? new ArrayList<AgentAccount>() : agentAccountList);
		oaab.setShopAccountList(shopAccountList == null ? new ArrayList<ShopAccount>() : shopAccountList);
		return oaab;
	}
	
	public Map<String, Object> getCompensateOrderStatisticsByAdmin(Integer monthType) {
		Map<String, Object> timeMap = getMonthTime(monthType);
		if (timeMap == null) {
			return null;
		}
		String start_time = (String) timeMap.get("start_time");
		String end_time = (String) timeMap.get("end_time");
		BigDecimal sumCompensateOrder = orderDao.getCompensateOrderStatisticsByAdmin(start_time,end_time);
		HashMap<String, Object> map = new HashMap<>();
		map.put("sumCompensateOrder", sumCompensateOrder);
		return map;
	}
	
	// 获取月份时间
	private Map<String, Object> getMonthTime(Integer monthType){
		Map<String, Object> map =new HashMap<String, Object>();
		// 创建时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(monthType == 1) {
			// 获取本月第一天
			Calendar firstCal = Calendar.getInstance(); 
			firstCal.set(Calendar.HOUR_OF_DAY, 0);
			firstCal.set(Calendar.MINUTE, 0);
			firstCal.set(Calendar.SECOND, 0);
			firstCal.set(Calendar.DAY_OF_MONTH,1); 
			Date firstTime = firstCal.getTime();
			// 获取当前时间
			Date lastTime = new Date();
			map.put("start_time", format.format(firstTime));
			map.put("end_time", format.format(lastTime));
		}
		if(monthType == 2) {
			Calendar firstCal = Calendar.getInstance(); 
			firstCal.set(Calendar.HOUR_OF_DAY, 0);
			firstCal.set(Calendar.MINUTE, 0);
			firstCal.set(Calendar.SECOND, 0);
			firstCal.add(Calendar.MONTH, -1); 
			firstCal.set(Calendar.DAY_OF_MONTH,1); 
			Date firstTime = firstCal.getTime();
			// 获取上月最后一天
			Calendar lastCal = Calendar.getInstance();
			lastCal.set(Calendar.HOUR_OF_DAY, 23);
			lastCal.set(Calendar.MINUTE, 59);
			lastCal.set(Calendar.SECOND, 59);
			lastCal.set(Calendar.DAY_OF_MONTH, 0);
			Date lastTime = lastCal.getTime();
			map.put("start_time", format.format(firstTime));
			map.put("end_time", format.format(lastTime));
		}
		
		return map;
	}
	
	@Override
	public int subAccountRecharge(Long order_id, Integer examine) {
		OrderInfo orderInfo = orderDao.getObjectById(new OrderInfo(order_id));
		if (examine == 2) {
			orderInfo.setOrder_state(-3);
			orderDao.setOrderStateByPlatformOrderNumber(orderInfo);
			return 1;
		}
		if (examine != 1) return -3;
		orderInfo.setOrder_state(5);
		return orderDao.setOrderStateByPlatformOrderNumber(orderInfo);
	}

	@Override
	public int subAccountRechargeTwo(Long order_id, Integer examine) {
		OrderInfo orderInfo = orderDao.getObjectById(new OrderInfo(order_id));
		if (examine == 2) {
			orderInfo.setOrder_state(-3);
			orderDao.setOrderStateByPlatformOrderNumber(orderInfo);
			return 1;
		}
		if (examine != 1) return -3;
		orderInfo.setOrder_state(6);
		orderDao.setOrderStateByPlatformOrderNumber(orderInfo);
		String result = taskService.sendOrderCompleteByRechargeDownLine(orderInfo.getPlatform_order_number());
//		switch(orderInfo.getPay_type()) {
//		case Constants.PAY_TYPE_YMD_DOWNLINE:
//			result = taskService.sendOrderCompleteByYmdDownLine(orderInfo.getPlatform_order_number());
//			break;
//		case Constants.PAY_TYPE_YMD_BLANCE:
//			ExtractionRecord extraction = extractionDao.getExtractionByNumber(orderInfo.getOrder_number());
//			if (extraction == null) return -6;
//			Map<String, Object> resMap = taskService.sendExtractionAdopServer(extraction.getId().toString());
//			if (((Double)resMap.get("code")).intValue() != 1) return -7;
//			orderInfo.setOrder_state(6);
//			orderDao.setOrderStateByPlatformOrderNumber(orderInfo);
//			result = taskService.sendOrderCompleteByYmdDownLine(orderInfo.getPlatform_order_number());
//			break;
//		}
		if (result.equals("success")) return 1;
		logger.info(result);
		return 0;
	}

}
