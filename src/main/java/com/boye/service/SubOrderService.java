package com.boye.service;

import java.util.Map;

import com.boye.bean.entity.CpSubPaymentInfoFail;
import com.boye.bean.entity.SubPaymentInfo;
import com.boye.bean.vo.Page;

public interface SubOrderService {

	Page<SubPaymentInfo> subPaymentInfoList(Map<String, Object> query);

	Page<SubPaymentInfo> errorSubPaymentInfoList(Map<String, Object> query);

	Page<SubPaymentInfo> findSubPaymentInfoErrorList(Map<String, Object> query);

	Page<CpSubPaymentInfoFail> getSubPaymentInfoFailList(Map<String, Object> query);

	int querySubStateByPlatform(Long sub_id);

	String getResultMsg();

	int sendSubPaymentInfoToUser(Long sub_id);

	int examineSubOrder(Long sub_id, Integer examine);

}
