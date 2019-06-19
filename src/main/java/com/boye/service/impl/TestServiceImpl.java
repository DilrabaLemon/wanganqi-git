package com.boye.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boye.api.FaCaiPayApi;
import com.boye.api.PinAnApi;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PaymentAccount;
import com.boye.bean.vo.AuthenticationInfo;
import com.boye.common.http.query.CorAcctTxDetailQueryBean;
import com.boye.common.utils.CommonUtils;
import com.boye.config.ServerConfigurer;
import com.boye.dao.PassagewayDao;
import com.boye.service.ITestService;

@Service
public class TestServiceImpl extends BaseServiceImpl implements ITestService {
	
	@Autowired
	private PassagewayDao passagewayDao;
	
    @Autowired
    private ServerConfigurer serverConf;
	
	@Override
	public Map<String, Object> getQueryInfo(String param1) {
		CorAcctTxDetailQueryBean catd = new CorAcctTxDetailQueryBean();
		
		catd.setAcctNo("15000093921595");
		catd.setCcyCode("RMB");
		catd.setBeginDate("20190501");
		catd.setEndDate("20190614");
		catd.setOrderMode("002");
		catd.setPageNo("1");
		catd.setPageSize("100");
		//公共字段	
		catd.setCnsmrSeqNo("P28523190614" + CommonUtils.getUserNumber(10));
		catd.setMrchCode("15000093921595");
		return PinAnApi.corAcctTxDetailQuery(catd, serverConf.getPinAnServerUrl());
	}

	@Override
	public String getQueryInfoByPage() {
		// TODO Auto-generated method stub
		Map<String, Object> res = FaCaiPayApi.getQrCode(new AuthenticationInfo(), new PaymentAccount(), new PassagewayInfo());
		return res.get("data").toString();
	}

	@Override
	public String changeReturnUrl(String reUrl, String changeUrl) {
		System.out.println(reUrl.length());
		List<PassagewayInfo> pslist = passagewayDao.findAllPassageway();
		for(PassagewayInfo ps: pslist) {
			String url = ps.getReturn_url();
			if (url == null || url.indexOf(reUrl) == -1) continue;
			String subUrl = url.substring(reUrl.length());
			ps.setReturn_url(changeUrl + subUrl);
			passagewayDao.updateByPrimaryKey(ps);
		}
		return "success";
		
	}

	@Override
	public String changeCallBackUrl(String reUrl, String changeUrl) {
		System.out.println(reUrl.length());
		List<PassagewayInfo> pslist = passagewayDao.findAllPassageway();
		for(PassagewayInfo ps: pslist) {
			String url = ps.getNotify_url();
			if (url == null || url.indexOf(reUrl) == -1) continue;
			String subUrl = url.substring(reUrl.length());
			ps.setNotify_url(changeUrl + subUrl);
			passagewayDao.updateByPrimaryKey(ps);
		}
		return "success";
	}

}
