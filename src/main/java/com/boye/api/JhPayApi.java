package com.boye.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.OrderInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.enums.SearchPayStatus;
import com.boye.common.http.query.BankQueryBean;
import com.boye.common.utils.XmlTemplate;
import com.google.gson.Gson;

import net.sf.json.JSON;
import net.sf.json.xml.XMLSerializer;

public class JhPayApi {
	
	private static Logger logger = LoggerFactory.getLogger(JhPayApi.class);

	public static SearchPayBo queryBankAccount(String query_number, OrderInfo order, PaymentAccount payment, String url) {
		Gson gson = new Gson();
		String xmlRequest = XmlTemplate.queryTemplate(query_number, order, payment);
		String res = HttpRequest.sendXMLPost(url, "requestXml="+xmlRequest);
		XMLSerializer xmlSerializer = new XMLSerializer();
        JSON json = xmlSerializer.read(res);
        String jsonStr = json.toString();
        jsonStr = jsonStr.replace("[]", "\"\"");
        BankQueryBean queryBean = null;
        SearchPayBo searchPayBo = new SearchPayBo();
        try {
        	queryBean = (BankQueryBean) gson.fromJson(jsonStr, BankQueryBean.class);
        	if (getQueryCode(queryBean)) {
        		if (queryBean.getTX_INFO().getLIST().getORDER_STATUS().equals("1")) {
        			searchPayBo.setState(SearchPayStatus.GETSUCCESS);
            		searchPayBo.setMoney(queryBean.getTX_INFO().getLIST().getPAYMENT_MONEY());
        		} else if (queryBean.getTX_INFO().getLIST().getORDER_STATUS().equals("2")) {
        			searchPayBo.setState(SearchPayStatus.GETFAIL);
        		} else if (queryBean.getTX_INFO().getLIST().getORDER_STATUS().equals("3")) {
        			searchPayBo.setState(SearchPayStatus.GETREFUND);
        		} else if (queryBean.getTX_INFO().getLIST().getORDER_STATUS().equals("4")) {
        			searchPayBo.setState(SearchPayStatus.GETREFUND);
        		} else if (queryBean.getTX_INFO().getLIST().getORDER_STATUS().equals("5")) {
        			searchPayBo.setState(SearchPayStatus.GETFAIL);
        		} else if (queryBean.getTX_INFO().getLIST().getORDER_STATUS().equals("0")) {
        			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
        		} else {
        			searchPayBo.setState(SearchPayStatus.GETSUCCESSPAYFAIL);
        		}
        	} else if (queryBean.getRETURN_CODE().equals("YDCA02910001")) {
        		searchPayBo.setState(SearchPayStatus.GETFAIL);
        	} else {
        		searchPayBo.setState(SearchPayStatus.GETFAIL);
        	}
        	searchPayBo.setMsg(queryBean.getRETURN_MSG());
        	searchPayBo.setOrder_number(order.getPlatform_order_number());
        } catch (Exception e) {
        	logger.info("返回格式异常：" + jsonStr);
        	searchPayBo.setState(SearchPayStatus.UNREPONSIVE);
        	searchPayBo.setMsg("返回信息异常");
        	searchPayBo.setOrder_number(order.getPlatform_order_number());
        }
		return searchPayBo;
	}

	private static boolean getQueryCode(BankQueryBean queryBean) {
		if (!queryBean.getRETURN_CODE().equals("000000")) return false;
		if (queryBean.getTX_INFO() == null || queryBean.getTX_INFO().getLIST() == null) return false;
		return true;
	}

}
