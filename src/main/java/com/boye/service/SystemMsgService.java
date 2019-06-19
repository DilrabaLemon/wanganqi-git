package com.boye.service;

import java.util.Map;

import com.boye.bean.entity.SystemMsg;
import com.boye.bean.vo.Page;

public interface SystemMsgService {

	int insertSystemMsgByAdmin(SystemMsg systemMsg);

	int deleteSystemMsgByAdmin(Long id);

	Page<SystemMsg> findSystemMsgListByAdmin(Map<String, Object> query);

	int sendMsgByUser(Map<String, Object> param);

}
