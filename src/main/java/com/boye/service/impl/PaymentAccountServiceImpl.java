package com.boye.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.PaymentKeyBox;
import com.boye.bean.entity.RechargeBankCard;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.OrderDao;
import com.boye.dao.PassagewayDao;
import com.boye.dao.PaymentDao;
import com.boye.dao.PaymentKeyBoxDao;
import com.boye.dao.RechargeBankCardDao;
import com.boye.service.IPaymentAccountService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class PaymentAccountServiceImpl extends BaseServiceImpl implements IPaymentAccountService {

	@Resource
	private PaymentDao paymentDao;
	
	@Autowired
	private PassagewayDao passagewayDao;
	
	@Resource
	private PaymentKeyBoxDao paymentKeyBoxDao;
	
	@Autowired
	private RechargeBankCardDao rechargeBankCardDao;
	
	@Autowired 
	private OrderDao orderDao;

	@Override
	public int addPayment(AdminInfo admin, PaymentAccount payment, PassagewayInfo passageway) {
    	payment.setDelete_flag(0);
    	payment.setState(0);
    	if (payment.getCounter_number() == null || payment.getCounter_number().trim().equals("")) payment.setCounter_number("00000000");
    	payment.setMin_money(payment.getMin_money() == null ? BigDecimal.ZERO: payment.getMin_money());
    	payment.setMax_money(payment.getMax_money() == null ? BigDecimal.valueOf(5000): payment.getMax_money());
    	payment.setUsable_quota(payment.getMax_quota());
    	payment.setAccount_code("0000000000");
    	if (passageway.getPassageway_type() == 3) {
    		payment.setState(1);
    	}
    	return paymentDao.insert(payment);
	}
	
	@Override
	public int editPayment(AdminInfo admin, PaymentAccount payment, PassagewayInfo passageway) {
		PaymentAccount findPayment = paymentDao.getObjectById(payment);
		if (findPayment == null) return -1;
		findPayment.setCounter_number(payment.getCounter_number());
    	findPayment.setAccount_number(payment.getAccount_number());
    	findPayment.setMax_quota(payment.getMax_quota());
    	findPayment.setMin_money(payment.getMin_money());
    	findPayment.setAccount_key(payment.getAccount_key());
    	findPayment.setMax_money(payment.getMax_money());
    	findPayment.setPassageway_id(payment.getPassageway_id());
    	findPayment.setOperation_number(payment.getOperation_number());
    	findPayment.setOperation_password(payment.getOperation_password());
    	return paymentDao.updateByPrimaryKey(findPayment);
	}

	@Override
	public int deletePayment(AdminInfo admin, String payment_id) {
		PaymentAccount payment = new PaymentAccount();
    	payment.setId(Long.parseLong(payment_id));
    	payment = paymentDao.getObjectById(payment);
    	if (payment == null) return -1;
    	payment.setDelete_flag(1);
    	int result  = paymentDao.updateByPrimaryKey(payment);
        return result;
	}

	@Override
	public Page<PaymentAccount> paymentList(AdminInfo admin,QueryBean query) {
		query.setType(0);
		Page<PaymentAccount> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = paymentDao.getPaymentAccountListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(paymentDao.getPaymentAccountListByPage(query));
        return page;
	}
	
	@Override
	public Page<PaymentAccount> paymentListByRecharge(QueryBean query) {
		query.setType(3);
		Page<PaymentAccount> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = paymentDao.getPaymentAccountListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(paymentDao.getPaymentAccountListByPage(query));
        return page;
	}

	@Override
	@Transactional
	public int paymentAvailable(AdminInfo admin,String payment_id, Integer state) {
		PaymentAccount payment = new PaymentAccount();
    	payment.setId(Long.parseLong(payment_id));
    	payment = paymentDao.getObjectById(payment);
    	if (state != 0 && state != 1) return -6;
    	if (payment == null) return -1;
    	PassagewayInfo passageway = passagewayDao.getObjectById(new PassagewayInfo(payment.getPassageway_id()));
    	if (passageway.getPassageway_type() == 3) {
    		RechargeBankCard rbc = rechargeBankCardDao.findByPaymentId(payment.getId());
    		if (rbc == null) return -8;
    	}
    	payment.setState(state);
    	int result  = paymentDao.updateByPrimaryKey(payment);
        return result;
	}

	@Override
	public PaymentAccount paymentInfo(String payment_id) {
		System.out.println(payment_id);
		PaymentAccount payment = new PaymentAccount();
		payment.setId(Long.parseLong(payment_id));
		return paymentDao.getObjectById(payment);
	}

	@Override
	public int copyPaymentAccount(PaymentAccount paymentAccount) {
		// 设置柜台号
		List<PaymentAccount> list =paymentDao.findPaymentByCounterNumber(paymentAccount.getCounter_number());
		if (list != null && list.size() > 0) {
			paymentAccount.setCounter_number("CP"+list.size()+paymentAccount.getCounter_number());
		}
		//ID重置
		paymentAccount.setId(null);
		// 状态设为停用
		paymentAccount.setState(1);
		paymentAccount.setAccount_code("0000000000");
		//设置可用额度为最大额度
		paymentAccount.setUsable_quota(paymentAccount.getMax_quota());
		paymentAccount.setCreate_time(null);
		paymentAccount.setUpdate_time(null);
		// 添加到数据库
		int result = paymentDao.insert(paymentAccount);
		
		return result;
	}

	@Override
	public int resetPaymentAccount(Long paymentId, BigDecimal money) {
		return paymentDao.resetPaymentUsableMoney(money, paymentId);
	}

	@Override
	public int addKeyBox(PaymentKeyBox keyBox) {
		PaymentKeyBox findKeyBox = paymentKeyBoxDao.getKeyBoxByPaymentId(keyBox.getPayment_id());
		if (findKeyBox == null) {
			return paymentKeyBoxDao.insertInfoToKeyBox(keyBox);
		}
		return paymentKeyBoxDao.updateKeyBox(keyBox);
	}

	@Override
	public PaymentKeyBox getKeyBox(Long id) {
		PaymentKeyBox findKeyBox = paymentKeyBoxDao.getKeyBoxByPaymentId(id);
		if (findKeyBox == null) {
			findKeyBox = new PaymentKeyBox();
			findKeyBox.setPayment_id(id);
		}
		return findKeyBox;
	}

	@Override
	public Page<OrderInfo> flowstatisticsByCounter(QueryBean query) {
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
		page.setTotals(orderDao.getFlowStatisticsCounterByCount(query));
		if (page.getTotals() == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(orderDao.getFlowStatisticsCounterByPage(query));
        return page;
	}
}
