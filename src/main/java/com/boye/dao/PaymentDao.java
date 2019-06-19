package com.boye.dao;

import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.vo.QueryBean;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.boye.base.mapper.BaseMapper;

@Mapper
public interface PaymentDao extends BaseMapper<PaymentAccount> {
	
	PaymentAccount getPaymentByAccountNumber(String account_number);

	PaymentAccount getPaymentByCounterNumber(String counter_number);

	List<PaymentAccount> getPaymentByPassageway(@Param("passageway_id")Long passageway_id, @Param("payment")BigDecimal payment);
	
	// 支付账户条件查询记录数
	int getPaymentAccountListByCount(QueryBean query);
	// 支付账户条件查询分页数据
	List<PaymentAccount> getPaymentAccountListByPage(QueryBean query);

	int changePaymentUsableMoney(@Param("money")BigDecimal money, @Param("payment_id")Long payment_id);
	
	int resetPaymentUsableMoney(@Param("money")BigDecimal money, @Param("payment_id")Long payment_id);

	List<PaymentAccount> findPaymentByPassageway(Long oldPassagewayId);

	List<PaymentAccount> findPaymentByCounterNumber(String counter_number);

	List<PaymentAccount> getPaymentByPassagewayAndState(@Param("passageway_id")Long passageway_id, @Param("payment")BigDecimal payment);

	
}
