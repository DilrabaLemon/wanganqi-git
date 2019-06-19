package com.boye.service;

import com.boye.bean.entity.SelfCheck;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface ABCOrderService {

	String getQrCodeResponse(String platOrderNo);

	Page<SelfCheck> findSelfCheckList(QueryBean query);

}
