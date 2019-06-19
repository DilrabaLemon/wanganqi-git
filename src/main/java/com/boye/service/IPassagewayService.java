package com.boye.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.boye.bean.bo.PassagewayHasConfigBo;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PassagewayCostInfo;
import com.boye.bean.entity.PassagewayHistory;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface IPassagewayService {

	int addPassageway(AdminInfo admin, PassagewayInfo passageway);

	int editPassageway(PassagewayInfo passageway);

	int deletePassageway(String passageway_id);

	Page<PassagewayInfo> passagewayList(QueryBean query);

	PassagewayInfo passagewayInfo(String passageway_id);

	List<PassagewayInfo> passagewayAll(Integer type);

	List<ProvideInfo> provideAll();

	List<HashMap<String, Object>> getLastMonthpassagewayMoney();

	List<HashMap<String, Object>> getThisMonthpassagewayMoney();

	Map<String, Object> passagewayTurnoverRateByDay();

	List<PassagewayInfo> passagewayAllByHy();

	Page<PassagewayCostInfo> getPassagewayCostList(QueryBean query);

	List<PassagewayHistory> getPassagewayHistory(QueryBean query);

	int addSubPaymentPassagewa(AdminInfo admin, PassagewayInfo passageway);

	Page<PassagewayInfo> subPaymentPassagewayList(QueryBean query);

	List<PassagewayInfo> passagewayAllRecharge();

	int copyPassageway(PassagewayInfo passagewayInfo);

	Page<PassagewayInfo> etrPaymentPassagewayList(QueryBean query);

	int addEtrPaymentPassagewa(AdminInfo admin, PassagewayInfo passageway);

	Page<OrderInfo> flowStatistics(QueryBean query);

	Page<PassagewayHasConfigBo> passagewayHasConfigList(QueryBean query);

	Map<String, Object> turnoverRateRuntime(QueryBean query);

	List<PassagewayInfo> passagewayAllByConfig(Long userId, Long passagewayId, int type);

}
