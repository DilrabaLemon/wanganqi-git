package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.RechargeBankCard;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface RechargeBankCardDao extends BaseMapper<RechargeBankCard> {

	int getRechargeBankCardListByCount(QueryBean query);

	List<RechargeBankCard> getRechargeBankCardListByPage(QueryBean query);

	@Delete("delete from recharge_bank_card where id = #{id}")
	int delete(Long id);

	@Select("select * from recharge_bank_card where delete_flag = 0 and payment_id = #{paymentId}")
	RechargeBankCard findByPaymentId(Long paymentId);

}
