package com.boye.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.bo.PassagewayHasConfigBo;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayCostInfo;
import com.boye.bean.entity.PassagewayHistory;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.OrderDao;
import com.boye.dao.PassagewayCostDao;
import com.boye.dao.PassagewayDao;
import com.boye.dao.PassagewayHistoryDao;
import com.boye.dao.PaymentDao;
import com.boye.dao.ProvideDao;
import com.boye.service.IPassagewayService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class PassagewayServiceImpl extends BaseServiceImpl implements IPassagewayService {
	
	@Autowired
	private PassagewayDao passagewayDao;
	
	@Autowired
	private ProvideDao provideDao;
	
	@Autowired 
	private OrderDao orderDao;
	
	@Autowired
	private PassagewayCostDao passagewayCostDao;
	
	@Autowired
	private PassagewayHistoryDao passagewayHistoryDao;
	
	@Autowired
	private PaymentDao paymentDao;

	@Override
	public int addPassageway(AdminInfo admin, PassagewayInfo passageway) {
		PassagewayInfo findPassageway = passagewayDao.getPassagewayByCode(passageway.getPassageway_code());
		if (findPassageway != null) return -2;
		// 添加通道类型,为支付通道
		return passagewayDao.insert(passageway);
	}

	@Override
	public int editPassageway(PassagewayInfo passageway) {
		PassagewayInfo findPassageway = passagewayDao.getObjectById(passageway);
		if (findPassageway == null) return -1;
		PassagewayInfo byCodePass = passagewayDao.getPassagewayByCode(passageway.getPassageway_code());
		if (byCodePass != null && !byCodePass.getId().equals(findPassageway.getId())) return -2;
		findPassageway.setPassageway_name(passageway.getPassageway_name());
		findPassageway.setPassageway_rate(passageway.getPassageway_rate());
		findPassageway.setPassageway_code(passageway.getPassageway_code());
		findPassageway.setPassageway_describe(passageway.getPassageway_describe());
		findPassageway.setProvide_id(passageway.getProvide_id());
		findPassageway.setNotify_url(passageway.getNotify_url());
		findPassageway.setReturn_url(passageway.getReturn_url());
		findPassageway.setServer_url(passageway.getServer_url());
		findPassageway.setMapping_flag(passageway.getMapping_flag() > 0 ? 1 : 0);
		findPassageway.setGetpayment_type(passageway.getGetpayment_type());
		findPassageway.setCheck_flag(passageway.getCheck_flag());
		//修改风控参数
		if (passageway.getPay_type() != null) findPassageway.setPay_type(passageway.getPay_type());
		findPassageway.setMax_money(passageway.getMax_money());
		findPassageway.setMin_money(passageway.getMin_money());
		findPassageway.setPoint_flag(passageway.getPoint_flag());
		findPassageway.setRestrict_number(passageway.getRestrict_number() == null? "": passageway.getRestrict_number() );
		findPassageway.setBalance_type(passageway.getBalance_type());
		findPassageway.setIncome_type(passageway.getIncome_type());
		return passagewayDao.updateByPrimaryKey(findPassageway);
	}

	@Override
	public int deletePassageway(String passageway_id) {
		PassagewayInfo findPassageway = passagewayDao.getObjectById(new PassagewayInfo(Long.parseLong(passageway_id)));
		if (findPassageway  == null) return -1;
		findPassageway.setDelete_flag(1);
		return passagewayDao.updateByPrimaryKey(findPassageway);
	}

	@Override
	public Page<PassagewayInfo> passagewayList(QueryBean query) {
		Page<PassagewayInfo> page = new Page<PassagewayInfo>(query.getPage_index(), query.getPage_size());
		page.setTotals(passagewayDao.getPassagewayListByCount(query));
		if (page.getTotals() == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(passagewayDao.getPassagewayListByPage(query));
        return page;
	}
	
	@Override
	public Page<PassagewayHasConfigBo> passagewayHasConfigList(QueryBean query) {
		Page<PassagewayHasConfigBo> page = new Page<PassagewayHasConfigBo>(query.getPage_index(), query.getPage_size());
		page.setTotals(passagewayDao.getPassagewayHasConfigListByCount(query));
		if (page.getTotals() == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(passagewayDao.getPassagewayHasConfigListByPage(query));
        return page;
	}

	@Override
	public PassagewayInfo passagewayInfo(String passageway_id) {
		return passagewayDao.getObjectById(new PassagewayInfo(Long.parseLong(passageway_id)));
	}

	@Override
	public List<PassagewayInfo> passagewayAll(Integer type) {
		return passagewayDao.getAllByType(type);
	}
	
	@Override
	public List<PassagewayInfo> passagewayAllByConfig(Long shopId, Long passagewayId, int type) {
		return passagewayDao.findPassagewayNotUserAndPassagewayId(shopId, passagewayId, type);
	}
	
	@Override
	public List<PassagewayInfo> passagewayAllRecharge() {
		return passagewayDao.getAllByType(3);
	}
	
	@Override
	public List<PassagewayInfo> passagewayAllByHy() {
		ProvideInfo provide = provideDao.getProvideByCode("quickpay");
		return passagewayDao.getPassagewayByProvideId(provide.getId());
	}

	@Override
	public List<ProvideInfo> provideAll() {
		return provideDao.getObjectAll(new ProvideInfo());
	}

	@Override
	public List<HashMap<String, Object>> getLastMonthpassagewayMoney() {
		// 创建时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取上月第一天
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
		// 创建一个数据列表
		List<HashMap<String, Object>> orderInfos= new ArrayList<>();

		String start_time = format.format(firstTime);
		String end_time = format.format(lastTime);
		// 查询通道数据
		List<PassagewayInfo> list=passagewayDao.findAllPassageway();
		if(list != null) {
			for (PassagewayInfo passagewayInfo : list) {
				 HashMap<String, Object> hashMap = new HashMap<>();
				// 获取该通道id金额总和
				BigDecimal  sumOrderMoney=orderDao.getOrderMoneyByPassagewayId(passagewayInfo.getId(),start_time,end_time);
				if(sumOrderMoney == null) {
					hashMap.put("sumOrderMoney", new BigDecimal("0"));
					hashMap.put("passagewayName",passagewayInfo.getPassageway_name());
					orderInfos.add(hashMap);
				}else {
					hashMap.put("sumOrderMoney", sumOrderMoney);
					hashMap.put("passagewayName",passagewayInfo.getPassageway_name());
					orderInfos.add(hashMap);
				}
			}
			return orderInfos;
		}else {
			return null;
		}
		
	}

	@Override
	public List<HashMap<String, Object>> getThisMonthpassagewayMoney() {
		// 创建时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取本月第一天
		Calendar firstCal = Calendar.getInstance(); 
		firstCal.set(Calendar.HOUR_OF_DAY, 0);
		firstCal.set(Calendar.MINUTE, 0);
		firstCal.set(Calendar.SECOND, 0);
		firstCal.set(Calendar.DAY_OF_MONTH,1); 
		Date firstTime = firstCal.getTime();
		// 获取当前时间
		Date lastTime = new Date();
		// 创建一个数据列表
		List<HashMap<String, Object>> orderInfos= new ArrayList<>();

		String start_time = format.format(firstTime);
		String end_time = format.format(lastTime);
		System.out.println(start_time);
		System.out.println(end_time);
		// 查询通道数据
		List<PassagewayInfo> list=passagewayDao.findAllPassageway();
		if(list != null) {
			for (PassagewayInfo passagewayInfo : list) {
				 HashMap<String, Object> hashMap = new HashMap<>();
				// 获取该通道id金额总和
				BigDecimal  sumOrderMoney=orderDao.getOrderMoneyByPassagewayId(passagewayInfo.getId(),start_time,end_time);
				if(sumOrderMoney == null) {
					hashMap.put("sumOrderMoney", new BigDecimal("0"));
					hashMap.put("passagewayName",passagewayInfo.getPassageway_name());
					orderInfos.add(hashMap);
				}else {
					hashMap.put("sumOrderMoney", sumOrderMoney);
					hashMap.put("passagewayName",passagewayInfo.getPassageway_name());
					orderInfos.add(hashMap);
				}
			}
			return orderInfos;
		}else {
			return null;
		}
	}

	@Override
	public Map<String, Object> passagewayTurnoverRateByDay() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		// 获取今天零点的时间
		String start_time = format.format(getTodayTime());
		//获取当前时间
		String end_time = format.format(new Date());
		// 获取所有通道
		HashMap<String, Object> map = new HashMap<>();
		List<PassagewayInfo> passagewayList = passagewayDao.findAllPassageway();
		for (PassagewayInfo passagewayInfo : passagewayList) {
			// 该通道订单总数
			BigDecimal totalConut = orderDao.getOrderCountByPassagewayId(passagewayInfo.getId(),start_time,end_time);
			// 该通道成功总是
			BigDecimal successCount = orderDao.getOrderSuccessCountByPassagewayId(passagewayInfo.getId(),start_time,end_time);
			// 计算成交率
			String rate ="0.00%";
			if(totalConut !=null && !totalConut.equals(BigDecimal.ZERO)) {
				BigDecimal divide = successCount.divide(totalConut, 2, BigDecimal.ROUND_HALF_UP);
				rate=divide.multiply(new BigDecimal("100"))+"%";
			}
			map.put(passagewayInfo.getPassageway_name(), rate);
		}
		return map;
	}
	
	private Date getTodayTime() {
		Calendar cal = Calendar.getInstance();
    	cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal.getTime();
	}

	@Override
	public Page<PassagewayCostInfo> getPassagewayCostList(QueryBean query) {
		Page<PassagewayCostInfo> page = new Page<PassagewayCostInfo>(query.getPage_index(), query.getPage_size());
		page.setTotals(passagewayCostDao.getPassagewayCostByCount(query));
		if (page.getTotals() == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(passagewayCostDao.getPassagewayCostByPage(query));
        return page;
	}

	@Override
	public List<PassagewayHistory> getPassagewayHistory(QueryBean query) {
		//取近三十天的记录
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date today = new Date();
        String endDate = sdf.format(today);//当前日期
        //获取三十天前日期
        Calendar theCa = Calendar.getInstance();
        theCa.setTime(today);
        theCa.add(theCa.DATE, -30);//最后一个数字30可改，30天的意思
        Date start = theCa.getTime();
        String startDate = sdf.format(start);//三十天之前日期
        query.setStart_time(startDate);
        query.setEnd_time(endDate);
		List<PassagewayHistory> list = passagewayHistoryDao.getPassagewayHistory(query);
		return list;
	}

	@Override
	public int addSubPaymentPassagewa(AdminInfo admin, PassagewayInfo passageway) {
		PassagewayInfo findPassageway = passagewayDao.getPassagewayByCode(passageway.getPassageway_code());
		if (findPassageway != null) return -2;
		// 添加通道类型,为代付通道
		passageway.setPassageway_type(1);
		return passagewayDao.insert(passageway);
	}
	
	@Override
	public int addEtrPaymentPassagewa(AdminInfo admin, PassagewayInfo passageway) {
		PassagewayInfo findPassageway = passagewayDao.getPassagewayByCode(passageway.getPassageway_code());
		if (findPassageway != null) return -2;
		// 添加通道类型,为代付通道
		passageway.setPassageway_type(2);
		return passagewayDao.insert(passageway);
	}

	@Override
	public Page<PassagewayInfo> subPaymentPassagewayList(QueryBean query) {
		Page<PassagewayInfo> page = new Page<PassagewayInfo>(query.getPage_index(), query.getPage_size());
		page.setTotals(passagewayDao.getSubPaymentPassagewayListByCount(query));
		if (page.getTotals() == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(passagewayDao.getSubPaymentPassagewayListByPage(query));
        return page;
	}
	
	@Override
	public Page<PassagewayInfo> etrPaymentPassagewayList(QueryBean query) {
		Page<PassagewayInfo> page = new Page<PassagewayInfo>(query.getPage_index(), query.getPage_size());
		page.setTotals(passagewayDao.getEtrPaymentPassagewayListByCount(query));
		if (page.getTotals() == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(passagewayDao.getEtrPaymentPassagewayListByPage(query));
        return page;
	}

	@Override
	public int copyPassageway(PassagewayInfo passagewayInfo) {
		// 获取原通道id
		Long oldPassagewayId = passagewayInfo.getId();
		//重置新通道的id,并保存
		passagewayInfo.setId(null);
		List<PassagewayInfo> passagewayList = passagewayDao.findPassagewayListByCode(passagewayInfo.getPassageway_code());
		if (passagewayList != null && passagewayList.size() >0) {
			if (passagewayList.size() == 1) {
				passagewayInfo.setPassageway_code("CP"+passagewayInfo.getPassageway_code());
			}else {
				passagewayInfo.setPassageway_code("CP"+(passagewayList.size()-1)+passagewayInfo.getPassageway_code());
			}
		}
		int insert = passagewayDao.insert(passagewayInfo);
		// 插入失败
		if (insert != 1) {
			return 0;
		}
		//获取新增通道id
		Long newPassagewayId = passagewayInfo.getId();
		
		// 查询原通道的支付账户
		List<PaymentAccount> list = paymentDao.findPaymentByPassageway(oldPassagewayId);
		for (PaymentAccount paymentAccount : list) {
			//重置部分字段
			paymentAccount.setId(null);
			paymentAccount.setCreate_time(null);
			paymentAccount.setUpdate_time(null);
			//关联新通道id
			paymentAccount.setPassageway_id(newPassagewayId);
			paymentDao.insert(paymentAccount);
		}
		return 1;
	}

	@Override
	public Page<OrderInfo> flowStatistics(QueryBean query) {
		// 时间默认为当天
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (StringUtils.isBlank(query.getStart_time())) {
			Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            query.setStart_time(format.format(calendar.getTime()));
		}
		if (StringUtils.isBlank(query.getEnd_time())) {
			Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            query.setEnd_time(format.format(calendar.getTime()));
		}
		Page<OrderInfo> page = new Page<OrderInfo>(query.getPage_index(), query.getPage_size());
		page.setTotals(orderDao.getFlowStatisticsListByCount(query));
		if (page.getTotals() == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(orderDao.getFlowStatisticsListByPage(query));
        return page;
	}
	
	@Override
	public Map<String, Object> turnoverRateRuntime(QueryBean query){
		Map<String, Object> result = new HashMap<String, Object>();
		if (query.getType() == null) query.setType(1);
		if (query.getType() == 2) {
			query.setStart_time(getStartTime(30));
		} else if (query.getType() == 3) {
			query.setStart_time(getStartTime(60));
		} else {
			query.setStart_time(getStartTime(10));
		}
		PassagewayInfo pi = passagewayDao.getObjectById(new PassagewayInfo(Long.parseLong(query.getPassageway_id())));
		result.put("passageway", pi);
		query.setState(1);
		int successCount = orderDao.findSuccessCountByPassagewayId(query);
		int failCount = orderDao.findFailCountByPassagewayId(query);
		BigDecimal totalCount = new BigDecimal(successCount + failCount);
		result.put("turnoverRate", totalCount.compareTo(BigDecimal.ZERO) == 0 ? totalCount : new BigDecimal(successCount).divide(totalCount, 2, BigDecimal.ROUND_HALF_UP));
		return result;
	}
	
	private String getStartTime(int time) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		cal.set(Calendar.MINUTE, cal.get(Calendar.MINUTE) - time);
		return sdf.format(cal.getTime());
	}
}
