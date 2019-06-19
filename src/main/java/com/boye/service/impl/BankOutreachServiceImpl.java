package com.boye.service.impl;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.api.HttpRequest;
import com.boye.api.JhPayApi;
import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.entity.RefundRecord;
import com.boye.bean.entity.SendServerInfo;
import com.boye.common.http.pay.BankRefundBean;
import com.boye.common.http.query.BankQueryBean;
import com.boye.common.utils.XmlTemplate;
import com.boye.dao.SendServerInfoDao;
import com.boye.service.IBankOutreachService;
import com.google.gson.Gson;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class BankOutreachServiceImpl extends BaseServiceImpl implements IBankOutreachService {
	
	private static Logger logger = LoggerFactory.getLogger(BankOutreachServiceImpl.class);
	
	private static final String QUERYURL = "http://47.99.135.58:12345";
	
	@Autowired
	private SendServerInfoDao sendServerDao;

	@Override
	public SearchPayBo queryBankAccount(Map<String, Object> param) {
//		String xmlRequest = XmlTemplate.queryTemplate(param, order, payment);
//		String res = HttpRequest.sendXMLPost(QUERYURL, "requestXml="+xmlRequest);
//		XMLSerializer xmlSerializer = new XMLSerializer();
//        JSON json = xmlSerializer.read(res);
//        String jsonStr = json.toString();
//        jsonStr = jsonStr.replace("[]", "\"\"");
//        Gson gson = new Gson();
//        BankQueryBean qrurl = (BankQueryBean) gson.fromJson(jsonStr, BankQueryBean.class);
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("data", json);
		
		//请求序列号（只可以使用数字）
		String query_number = "";
		//需要查询的订单
		OrderInfo order = new OrderInfo();
		//该订单所使用的支付账户
		PaymentAccount payment = new PaymentAccount();
//		return JhPayApi.queryBankAccount(query_number, order, payment);
		return null;
	}
	
	@Override
	public Map<String, Object> sendRefund(Map<String, Object> param) {
		return null;
	}

	@Override
	public BankQueryBean queryOrderForBank(SendServerInfo sendServer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BankRefundBean sendRefund(OrderInfo order, RefundRecord refund, PaymentAccount payment) {
		SendServerInfo sendServer = SendServerInfo.getSendServerInfo(order, payment);
		if (sendServerDao.insert(sendServer) != 1) logger.info("sendServer save fail by refund_id = " + refund.getId());
		String xmlRequest = XmlTemplate.refundTemplate(sendServer);
		String res = HttpRequest.sendXMLPost(QUERYURL, "requestXml="+xmlRequest);
		XMLSerializer xmlSerializer = new XMLSerializer();
        JSON json = xmlSerializer.read(res);
        String jsonStr = json.toString();
        jsonStr = jsonStr.replace("[]", "\"\"");
        Gson gson = new Gson();
        return (BankRefundBean) gson.fromJson(jsonStr, BankRefundBean.class);
	}
	
}
