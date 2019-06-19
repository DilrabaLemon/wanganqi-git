package com.boye.service;

import java.util.Map;

public interface IPayReturnMsgService {

	int setReturnMsg(String order_number, Map<String, Object> param);

	int setReturnMsg(String order_number, String param);

}
