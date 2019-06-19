package com.boye.service;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.RefundRecord;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface IRefundService {

	//int submitRefund(ShopUserInfo shopUser, String order_id);

	int refundExamine(AdminInfo admin, String refund_id, String examine);

	Page<RefundRecord> getRefundPage(Integer state, QueryBean query);

}
