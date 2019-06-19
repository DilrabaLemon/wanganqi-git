package com.boye.service.impl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.api.AMWYPayApi;
import com.boye.api.HhlQuickPayApi;
import com.boye.api.HhlWAKPayApi;
import com.boye.api.HhlYLPayApi;
import com.boye.api.KltWGPayApi;
import com.boye.api.NewQuickPayApi;
import com.boye.api.PinAnApi;
import com.boye.api.YsH5PayApi;
import com.boye.api.YtcpuPayApi;
import com.boye.bean.bo.SearchPayBo;
import com.boye.bean.entity.DictTable;
import com.boye.bean.entity.ExtractionRecord;
import com.boye.bean.entity.ExtractionRecordForAgent;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.PlatformExtractionRecord;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.entity.SubstituteAccount;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.enums.BankCodeEnum;
import com.boye.common.http.query.AMWYQueryBalanceParamBean;
import com.boye.common.http.query.AMWYSubQueryParamBean;
import com.boye.common.http.query.CorAcctBalanceQueryBean;
import com.boye.common.http.query.HhlQuickSubQueryParamBean;
import com.boye.common.http.query.NewQuickQueryBalanceParamBean;
import com.boye.common.http.query.NewQuickSubQueryParamBean;
import com.boye.common.http.query.TranInfoQueryBean;
import com.boye.common.http.query.YsH5QueryBalanceParamBean;
import com.boye.common.http.query.YsH5SubQueryParamBean;
import com.boye.common.http.subpay.AMWYSubParamBean;
import com.boye.common.http.subpay.HhlQuickSubParamBean;
import com.boye.common.http.subpay.HhlWAKSubParamBean;
import com.boye.common.http.subpay.HhlYSFSubParamBean;
import com.boye.common.http.subpay.NewQuickSubParamBean;
import com.boye.common.http.subpay.SingleDfTranDealBean;
import com.boye.common.http.subpay.YsH5SubParamBean;
import com.boye.common.utils.APISecurityUtils;
import com.boye.common.utils.Base64Utils;
import com.boye.common.utils.CommonUtils;
import com.boye.common.utils.Matchers;
import com.boye.common.utils.XMLUtils;
import com.boye.config.ServerConfigurer;
import com.boye.dao.DictDao;
import com.boye.dao.ExtractionAgentDao;
import com.boye.dao.ExtractionDao;
import com.boye.dao.PassagewayDao;
import com.boye.dao.PlatformAccountDao;
import com.boye.dao.PlatformExtractionRecordDao;
import com.boye.dao.ShopConfigDao;
import com.boye.dao.SubstituteDao;
import com.boye.service.IExtractionService;
import com.boye.service.IProvideService;
import com.boye.service.ITaskService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class ExtractionServiceImpl extends BaseServiceImpl implements IExtractionService {
	
	private static Logger logger = LoggerFactory.getLogger(ExtractionServiceImpl.class);

	@Autowired
    private ExtractionDao extractionDao;
    
    @Autowired
    private ExtractionAgentDao extractionAgentDao;
    
    @Autowired
    private PlatformAccountDao platformAccountDao;
    
    @Autowired
	private IProvideService provideService;

    @Autowired
    private SubstituteDao substituteDao;
     
    @Autowired
    private ITaskService taskService;
    
    @Autowired
    private DictDao dictDao;
    
    @Autowired
    private ServerConfigurer serverConf;
    
    @Autowired
    private PlatformExtractionRecordDao platformExtractionRecordDao;
    
    private String remoteMessage;

    @Override
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public int extractionExamine(String extraction_id, String examine) {
    	int state = Integer.parseInt(examine);
    	if (state != 1 && state != 2) return -6;
    	ExtractionRecord extraction = extractionDao.getObjectById(new ExtractionRecord(Long.parseLong(extraction_id)));
    	if (extraction == null) {
    		logger.info("not find extraction by extraction_id: " + extraction_id);
    		return -1;
    	}
    	Map<String, Object> result = null;
    	if (state == 2) {
    		result = taskService.sendExtractionRefuseServer(extraction_id);
    		return 1;
    	}
//    	else {
//    		if (extraction.getType() == 2)  {
//    			int res = h5Substitute(extraction);
//    			if (res != 1) return res;
//    			extraction.setState(4);
//    			return extractionDao.updateByPrimaryKey(extraction);
//    		} else if (extraction.getType() == 3) {
//    			int res = bankSubstitute(extraction);
//    			if (res != 1) return res;
//    		} else if (extraction.getType() == 5) {
//    			int res = kltSubstitute(extraction);
//    			if (res != 1) return res;
//    			extraction.setState(4);
//    			return extractionDao.updateByPrimaryKey(extraction);
//    		} else if (extraction.getType() == 7) {
//    			int res = newBankSubstitute(extraction);
//    			if (res != 1) return res;
//    			extraction.setState(4);
//    			return extractionDao.updateByPrimaryKey(extraction);
//    		} else {
//    			extraction.setPassageway_id(0L);
//    		}
//    		extractionDao.updateByPrimaryKey(extraction);
//    		result = taskService.sendExtractionAdopServer(extraction_id);
//    	}
    	result = taskService.sendExtractionAdopServer(extraction_id);
    	if (((Double)result.get("code")).intValue() != 1) {
    		logger.info("extraction fail:" + result.get("message"));
    		extraction.setState(3);
    		extractionDao.updateByPrimaryKey(extraction);
    	}
    	return 1;
    }
    
//    private int kltSubstitute(ExtractionRecord extraction) {
//		ProvideInfo provide = provideService.getProvideByProvideCode("kltdf");
//		List<PassagewayInfo> piList = passagewayDao.getPassagewayByShopIdAndProvideId(extraction.getShop_id(), provide.getId());
//		PassagewayInfo passagewayInfo = null;
//		for (PassagewayInfo pi: piList) {
//			if(pi.getPassageway_code().contains("S" + extraction.getShop_id())) 
//				passagewayInfo = pi;
//		}
//		if (piList.size() != 0 && passagewayInfo == null)
//			passagewayInfo = piList.get(0);
//    	if (passagewayInfo == null) return -15;
//    	extraction.setPassageway_id(passagewayInfo.getId());
//
//    	SubstituteAccount useSubstitute = getUseSubstitute(extraction.getActual_money(), passagewayInfo);
//    	if (useSubstitute == null) return -15;
//    	
//    	extraction.setSubstitute_id(useSubstitute.getId());
//    	Map<String, Object> res = KltWGPayApi.sendSubInfo(extraction, useSubstitute, passagewayInfo);
//		if (!res.get("code").toString().equals("1")) {
//			remoteMessage = res.get("msg").toString();
//			return -21;
//		}
//		useSubstitute.subtractUsable_quota(extraction.getActual_money());
//    	return substituteDao.updateByPrimaryKey(useSubstitute);
//	}
//    
//    private int newBankSubstitute(ExtractionRecord extraction) {
//		ProvideInfo provide = provideService.getProvideByProvideCode("newquickdf");
//		List<PassagewayInfo> piList = passagewayDao.getPassagewayByProvideId(provide.getId());
//		PassagewayInfo passagewayInfo = null;
//		if (piList.size() > 0) passagewayInfo = piList.get(0);
//    	if (passagewayInfo == null) return -15;
//    	extraction.setPassageway_id(passagewayInfo.getId());
//
//    	SubstituteAccount useSubstitute = getUseSubstitute(extraction.getActual_money(), passagewayInfo);
//    	if (useSubstitute == null) return -15;
//    	
//    	extraction.setSubstitute_id(useSubstitute.getId());
//    	Map<String, Object> res = NewQuickPayApi.sendSubInfo(extraction, useSubstitute, passagewayInfo);
//		if (!res.get("code").toString().equals("1")) {
//			remoteMessage = res.get("msg").toString();
//			return -21;
//		}
//		useSubstitute.subtractUsable_quota(extraction.getActual_money());
//    	return substituteDao.updateByPrimaryKey(useSubstitute);
//	}
//
//	private int bankSubstitute(ExtractionRecord extraction) {
//		ProvideInfo provide = provideService.getProvideByProvideCode("hhldf");
//		List<PassagewayInfo> piList = passagewayDao.getPassagewayByProvideId(provide.getId());
//		PassagewayInfo passagewayInfo = null;
//		if (piList.size() > 0) passagewayInfo = piList.get(0);
//    	if (passagewayInfo == null) return -15;
//    	extraction.setPassageway_id(passagewayInfo.getId());
//
//    	SubstituteAccount useSubstitute = getUseSubstitute(extraction.getActual_money(), passagewayInfo);
//    	if (useSubstitute == null) return -15;
//    	
//    	extraction.setSubstitute_id(useSubstitute.getId());
//    	Map<String, Object> res = HhlQuickPayApi.sendSubInfo(extraction, useSubstitute, passagewayInfo);
//		if (!res.get("code").toString().equals("1")) {
//			remoteMessage = res.get("msg").toString();
//			return -21;
//		}
//		useSubstitute.subtractUsable_quota(extraction.getActual_money());
//    	return substituteDao.updateByPrimaryKey(useSubstitute);
//	}
//
//	private int h5Substitute(ExtractionRecord extraction) {
//		ProvideInfo provide = provideService.getProvideByProvideCode("ysh5df");
//		List<PassagewayInfo> piList = passagewayDao.getPassagewayByProvideId(provide.getId());
//		PassagewayInfo passagewayInfo = null;
//		if (piList.size() > 0) passagewayInfo = piList.get(0);
//    	if (passagewayInfo == null) return -15;
//    	extraction.setPassageway_id(passagewayInfo.getId());
//		
//    	SubstituteAccount useSubstitute = getUseSubstitute(extraction.getActual_money(), passagewayInfo);
//    	if (useSubstitute == null) return -15;
//    	
//    	extraction.setSubstitute_id(useSubstitute.getId());
//    	Map<String, Object> res = YsH5PayApi.sendSubInfo(extraction, useSubstitute, passagewayInfo);
//    	if (!res.get("code").toString().equals("1")) {
//			remoteMessage = res.get("msg").toString();
//			return -21;
//		}
//		useSubstitute.subtractUsable_quota(extraction.getActual_money());
//    	return substituteDao.updateByPrimaryKey(useSubstitute);
//	}

	@Override
    public Page<ExtractionRecord> extractionList(Integer state, QueryBean query) {
    	// 设置状态码
    	if (state == null) state = 0;
    	query.setState(state);
    	// 创建分页对象
    	Page<ExtractionRecord> page = new Page<ExtractionRecord>(query.getPage_index(), query.getPage_size());
    	// 获取总记录数
        int count = extractionDao.getExtractionRecordCount(query);
        page.setTotals(count);
        // 获取数据列表
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(extractionDao.getExtractionRecord(query));
        return page;
    }

	@Override
	@Transactional(propagation = Propagation.NOT_SUPPORTED)
	public int extractionExamineForAgent(String extraction_id, String examine) {
    	int state = Integer.parseInt(examine);
    	if (state != 1 && state != 2) return -6;
    	Map<String, Object> result = null;
    	ExtractionRecordForAgent extraction = extractionAgentDao.getObjectById(new ExtractionRecordForAgent(Long.parseLong(extraction_id)));
    	if (extraction == null) return -1;
    	if (state == 2) {
    		result = taskService.sendAgentExtractionRefuseServer(extraction_id);
    	}
//    	else {
//    		if (extraction.getType() == 2) {
//				int res = h5Substitute(extraction);
//				if (res != 1) return res;
//				extraction.setState(4);
//    			return extractionAgentDao.updateByPrimaryKey(extraction);
//			} else if (extraction.getType() == 3) {
//    			int res = bankSubstitute(extraction);
//    			if (res != 1) return res;
//			} else if (extraction.getType() == 5) {
//    			int res = kltSubstitute(extraction);
//    			if (res != 1) return res;
//    			extraction.setState(4);
//    			return extractionAgentDao.updateByPrimaryKey(extraction);
//			} else if (extraction.getType() == 7) {
//    			int res = newBankSubstitute(extraction);
//    			if (res != 1) return res;
//    		} else {
//    			extraction.setPassageway_id(0L);
//    		}
//    		extractionAgentDao.updateByPrimaryKey(extraction);
//    		result = taskService.sendAgentExtractionAdopServer(extraction_id);
//    	}
    	result = taskService.sendAgentExtractionAdopServer(extraction_id);
    	if (((Double)result.get("code")).intValue() != 1) {
    		extraction.setState(3);
    		extractionAgentDao.updateByPrimaryKey(extraction);
    	}
    	return 1;
	}
	
//	private int kltSubstitute(ExtractionRecordForAgent extraction) {
//		ProvideInfo provide = provideService.getProvideByPassagewayCode("kltdf");
//		List<PassagewayInfo> piList = passagewayDao.getPassagewayByProvideId(provide.getId());
//		PassagewayInfo passagewayInfo = null;
//		if (piList.size() > 0) passagewayInfo = piList.get(0);
//    	if (passagewayInfo == null) return -15;
//    	extraction.setPassageway_id(passagewayInfo.getId());
//    	
//    	SubstituteAccount useSubstitute = getUseSubstitute(extraction.getActual_money(), passagewayInfo);
//    	if (useSubstitute == null) return -15;
//    	
//    	extraction.setSubstitute_id(useSubstitute.getId());
//    	Map<String, Object> res = KltWGPayApi.sendSubInfo(extraction, useSubstitute, passagewayInfo);
//		if (!res.get("code").toString().equals("1")) return -21;
//		useSubstitute.subtractUsable_quota(extraction.getActual_money());
//    	return substituteDao.updateByPrimaryKey(useSubstitute);
//	}
//	
//	private int newBankSubstitute(ExtractionRecordForAgent extraction) {
//		ProvideInfo provide = provideService.getProvideByPassagewayCode("newquickdf");
//		List<PassagewayInfo> piList = passagewayDao.getPassagewayByProvideId(provide.getId());
//		PassagewayInfo passagewayInfo = null;
//		if (piList.size() > 0) passagewayInfo = piList.get(0);
//    	if (passagewayInfo == null) return -15;
//    	extraction.setPassageway_id(passagewayInfo.getId());
//    	
//    	SubstituteAccount useSubstitute = getUseSubstitute(extraction.getActual_money(), passagewayInfo);
//    	if (useSubstitute == null) return -15;
//    	
//    	extraction.setSubstitute_id(useSubstitute.getId());
//    	Map<String, Object> res = NewQuickPayApi.sendSubInfo(extraction, useSubstitute, passagewayInfo);
//		if (!res.get("code").toString().equals("1")) return -21;
//		useSubstitute.subtractUsable_quota(extraction.getActual_money());
//    	return substituteDao.updateByPrimaryKey(useSubstitute);
//	}
//	
//	private int bankSubstitute(ExtractionRecordForAgent extraction) {
//		ProvideInfo provide = provideService.getProvideByPassagewayCode("hhldf");
//		List<PassagewayInfo> piList = passagewayDao.getPassagewayByProvideId(provide.getId());
//		PassagewayInfo passagewayInfo = null;
//		if (piList.size() > 0) passagewayInfo = piList.get(0);
//    	if (passagewayInfo == null) return -15;
//    	extraction.setPassageway_id(passagewayInfo.getId());
//    	
//    	SubstituteAccount useSubstitute = getUseSubstitute(extraction.getActual_money(), passagewayInfo);
//    	if (useSubstitute == null) return -15;
//    	
//    	extraction.setSubstitute_id(useSubstitute.getId());
//    	Map<String, Object> res = HhlQuickPayApi.sendSubInfo(extraction, useSubstitute, passagewayInfo);
//		if (!res.get("code").toString().equals("1")) return -21;
//		useSubstitute.subtractUsable_quota(extraction.getActual_money());
//    	return substituteDao.updateByPrimaryKey(useSubstitute);
//	}
//
//	private int h5Substitute(ExtractionRecordForAgent extraction) {
//		ProvideInfo provide = provideService.getProvideByPassagewayCode("hhlh5");
//		List<PassagewayInfo> piList = passagewayDao.getPassagewayByProvideId(provide.getId());
//		PassagewayInfo passagewayInfo = null;
//		if (piList.size() > 0) passagewayInfo = piList.get(0);
//    	if (passagewayInfo == null) return -15;
//    	extraction.setPassageway_id(passagewayInfo.getId());
//
//    	SubstituteAccount useSubstitute = getUseSubstitute(extraction.getActual_money(), passagewayInfo);
//    	if (useSubstitute == null) return -15;
//    	
//    	extraction.setSubstitute_id(useSubstitute.getId());
//    	Map<String, Object> res = YsH5PayApi.sendSubInfo(extraction, useSubstitute, passagewayInfo);
//		if (!res.get("code").toString().equals("1")) return -21;
//		useSubstitute.subtractUsable_quota(extraction.getActual_money());
//    	return substituteDao.updateByPrimaryKey(useSubstitute);
//	}


	@Override
	public Page<ExtractionRecordForAgent> extractionListForAgent(Integer state, QueryBean query) {
		if (state == null) state = 0;
    	query.setState(state);
    	Page<ExtractionRecordForAgent> page = new Page<ExtractionRecordForAgent>(query.getPage_index(), query.getPage_size());
        int count = extractionAgentDao.getExtractionRecordAgentCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(extractionAgentDao.getExtractionRecordAgent(query));
        return page;
	}

	@Override
	public Map<String, Object> getSendInfo() {
		return YtcpuPayApi.sendSubInfo(new ExtractionRecord(), new SubstituteAccount(), new PassagewayInfo());
	}

	@Override
	public Map<String, Object> getExtractionStatisticsByAdmin(Integer monthType) {
		Map<String, Object> timeMap = getMonthTime(monthType);
		if (timeMap == null) {
			return null;
		}
		String start_time = (String) timeMap.get("start_time");
		String end_time = (String) timeMap.get("end_time");
		BigDecimal sumExtraction = platformAccountDao.getExtractionStatisticsByAdmin(start_time,end_time);
		HashMap<String, Object> map = new HashMap<>();
		map.put("sumExtraction", sumExtraction);
		return map;
	}
	
	// 获取月份时间
	private Map<String, Object> getMonthTime(Integer monthType){
		Map<String, Object> map =new HashMap<String, Object>();
		// 创建时间格式
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if(monthType == 1) {
			// 获取本月第一天
			Calendar firstCal = Calendar.getInstance(); 
			firstCal.set(Calendar.HOUR_OF_DAY, 0);
			firstCal.set(Calendar.MINUTE, 0);
			firstCal.set(Calendar.SECOND, 0);
			firstCal.set(Calendar.DAY_OF_MONTH,1); 
			Date firstTime = firstCal.getTime();
			// 获取当前时间
			Date lastTime = new Date();
			map.put("start_time", format.format(firstTime));
			map.put("end_time", format.format(lastTime));
		}
		if(monthType == 2) {
			Calendar firstCal = Calendar.getInstance(); 
			firstCal.set(Calendar.HOUR_OF_DAY, 0);
			firstCal.set(Calendar.MINUTE, 0);
			firstCal.set(Calendar.SECOND, 0);
			firstCal.add(Calendar.MONTH, -1); 
			firstCal.set(Calendar.DAY_OF_MONTH,1); 
			Date firstTime = firstCal.getTime();
			// 获取上月最后一天
			Calendar lastCal = Calendar.getInstance();
			lastCal.set(Calendar.HOUR_OF_DAY, 23);
			lastCal.set(Calendar.MINUTE, 59);
			lastCal.set(Calendar.SECOND, 59);
			lastCal.set(Calendar.DAY_OF_MONTH, 0);
			Date lastTime = lastCal.getTime();
			map.put("start_time", format.format(firstTime));
			map.put("end_time", format.format(lastTime));
		}
		
		return map;
	}

	public String getRemoteMessage() {
		return remoteMessage;
	}

	public void setRemoteMessage(String remoteMessage) {
		this.remoteMessage = remoteMessage;
	}
	
//	 private SubstituteAccount getUseSubstitute(BigDecimal money, PassagewayInfo passagewayInfo) {
//    	List<SubstituteAccount> useSubList = new ArrayList<SubstituteAccount>();
//    	List<SubstituteAccount> substituteList = substituteDao.getSubstituteByPassagewayId(passagewayInfo.getId());
//    	for (SubstituteAccount substitute: substituteList) {
//    		if (substitute.getUsable_quota().compareTo(money) == 1) {
//    			useSubList.add(substitute);
//    		}
//    	}
//    	if (useSubList.size() == 0) return null;
//    	int index = (int)(Math.random() * useSubList.size());
//		return useSubList.get(index);
//	}

	@Override
	public int queryExtractionSubState(Long extraction_id) {
		ExtractionRecord extract = extractionDao.getObjectById(new ExtractionRecord(extraction_id));
		if (extract == null || extract.getState() != 4) return -3;
		SubstituteAccount substitute = substituteDao.getObjectById(new SubstituteAccount(extract.getSubstitute_id()));
		if (substitute == null) return -4;
		ProvideInfo provide = provideService.getProvideByPassagewayId(substitute.getPassageway_id());
		if (provide == null) return -4;
		SearchPayBo res = null;
		switch(provide.getProvide_code()) {
		case ProvideInfo.SUB_NEWQUICK:
			res = NewQuickPayApi.subInfoQuery(extract.getExtraction_number(), substitute);
			break;
		case ProvideInfo.SUB_KLTDF:
			res = KltWGPayApi.subInfoQuery(extract.getExtraction_number(), substitute);
			break;
		}
		if (res == null) return -1;
		if (res.getState().getCode() == 1) {
			logger.info("send adop extraction to task");
			Map<String, Object> result = taskService.sendExtractionAdopServer(extraction_id.toString());
			return ((Double)result.get("code")).intValue();
		} else if (res.getState().getCode() == -2) {
			logger.info("send refuse extraction to task");
			Map<String, Object> result = taskService.sendExtractionRefuseServer(extraction_id.toString());
			if (((Double)result.get("code")).intValue() == 1) return -2;
		}
		return res.getState().getCode();
	}
	
	@Override
	public int queryExtractionSubStateByAgent(Long extraction_id) {
		ExtractionRecordForAgent extract = extractionAgentDao.getObjectById(new ExtractionRecordForAgent(extraction_id));
		if (extract == null) return -3;
		SubstituteAccount substitute = substituteDao.getObjectById(new SubstituteAccount(extract.getSubstitute_id()));
		if (substitute == null) return -4;
		ProvideInfo provide = provideService.getProvideByPassagewayId(substitute.getPassageway_id());
		if (provide == null) return -4;
		SearchPayBo res = null;
		switch(provide.getProvide_code()) {
		case ProvideInfo.SUB_NEWQUICK:
			res = NewQuickPayApi.subInfoQuery(extract.getExtraction_number(), substitute);
			break;
		case ProvideInfo.SUB_KLTDF:
			res = KltWGPayApi.subInfoQuery(extract.getExtraction_number(), substitute);
			break;
		}
		if (res == null) return -1;
		if (res.getState().getCode() == 1) {
			logger.info("send adop agent extraction to task");
			Map<String, Object> result = taskService.sendAgentExtractionAdopServer(extraction_id.toString());
			return ((Double)result.get("code")).intValue();
		} else if (res.getState().getCode() == -2) {
			logger.info("send refuse agent extraction to task");
			Map<String, Object> result = taskService.sendAgentExtractionRefuseServer(extraction_id.toString());
			if (((Double)result.get("code")).intValue() == 1) return -2;
		}
		return res.getState().getCode();
	}
	
	@Override
	public int queryExtractionSubStateByPlatform(Long extraction_id) {
		PlatformExtractionRecord extract = platformExtractionRecordDao.getObjectById(new PlatformExtractionRecord(extraction_id));
		if (extract == null || extract.getState() != 4) return -3;
		SearchPayBo res = null;
		switch(extract.getType()) {
		case 1:
			res = getBankSubQuick(extract);
			break;
		case 2:
			res = getH5SubQuick(extract);
			break;
		case 3:
			res = getKltSubQuick(extract);
			break;
		case 4:
			res = getNewQuickSubQuick(extract);
			break;
		case 5:
			res = getAMWYSubQuick(extract);
			break;
		case 6:
		case 8:
			res = getHhlQuickSubQuick(extract);
			break;
		case 7:
			res = getPinAnSubQuick(extract);
			break;
		case 9:
			res = getHhlWAKSubQuick(extract);
		}
		if (res == null) return -1;
		if (res.getState().getCode() == 1) {
			logger.info("platform extraction complate");
			extract.setState(1);
			return platformExtractionRecordDao.updateByPrimaryKey(extract);
		} else if (res.getState().getCode() == -2) {
			logger.info("platform extraction fail");
			extract.setState(2);
			platformExtractionRecordDao.updateByPrimaryKey(extract);
			return -2;
		}
		logger.info("platform extraction hang in the air");
		return res.getState().getCode();
	}
	
	private SearchPayBo getAMWYSubQuick(PlatformExtractionRecord extract) {
		AMWYSubQueryParamBean queryParam = new AMWYSubQueryParamBean();
		queryParam.setHead(new AMWYSubQueryParamBean.HeadContent());
		AMWYSubQueryParamBean.DataContent data = new AMWYSubQueryParamBean.DataContent();
		AMWYSubQueryParamBean.SendBean dataEncode = new AMWYSubQueryParamBean.SendBean();
		
		data.setOrderId(getAMWYOrderId(extract.getId()));
		data.setSerialNum(extract.getExtraction_number());

		queryParam.getHead().setAppId("1001");
		queryParam.getHead().setBackURL("");
		queryParam.getHead().setMchid(serverConf.getAccountNumberYsAMWY());
		String code = UUID.randomUUID().toString().replaceAll("-", "");
		queryParam.getHead().setReqNo(code);
		queryParam.getHead().setReqType("transfer_query_order_request");
		queryParam.getHead().setSignType("RSA1");
		queryParam.getHead().setVersion("1.0");
		dataEncode.setData(data);
		dataEncode.setHead(queryParam.getHead());
		String xmlstr = XMLUtils.convertToXml(dataEncode);
		queryParam.getHead().setSign(queryParam.generateSign(data, serverConf.getAmwySubPrivateKey()));
		try {
			String encodedata=encodeData(Matchers.match("<data>(.*)</data>", xmlstr.replace("\n", "").replace("\t", "")), serverConf.getAccountNumberYsAMWY());
			queryParam.setData(encodedata);
		} catch (Exception e) {
			logger.info("not sign param data");
		}
		return AMWYPayApi.subInfoQuery(queryParam);
	}
	
	private SearchPayBo getBankSubQuick(PlatformExtractionRecord extract) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private SearchPayBo getH5SubQuick(PlatformExtractionRecord extract) {
		YsH5SubQueryParamBean queryParam = new YsH5SubQueryParamBean();
		queryParam.setKey(serverConf.getAccountKeyYsH5());
		queryParam.setMch_id(serverConf.getAccountNumberYsH5());
		queryParam.setOrder_no("");
		queryParam.setOut_order_no(extract.getExtraction_number());
		queryParam.setSign_type("MD5");
		queryParam.setSignature(queryParam.generateSign());
		return YsH5PayApi.subInfoQuery(queryParam);
	}
	
	private SearchPayBo getKltSubQuick(PlatformExtractionRecord extract) {
		// TODO Auto-generated method stub
		return null;
	}
	
	private SearchPayBo getPinAnSubQuick(PlatformExtractionRecord extract) {
		
		TranInfoQueryBean queryBean = new TranInfoQueryBean();
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(now);
		dateStr = dateStr.substring(2, dateStr.length());
		queryBean.setCnsmrSeqNo(CommonUtils.generatePlatformOrderNumberByPinAn(serverConf.getShortNumberPinAn() + dateStr));
		queryBean.setKey(serverConf.getAccountKeyPinAn());
		queryBean.setBussTypeNo("100157");
		queryBean.setCorpAgreementNo(serverConf.getCounterNumberPinAn());
//		queryBean.setAcctNo("15000093921595");
//		queryBean.setBankSeqNo(bankSeqNo);
		queryBean.setRequestSeqNo(extract.getExtraction_number());
		queryBean.setStartDate(sdf.format(now));
		queryBean.setEndDate(sdf.format(now));
		queryBean.setTranStatus("0");
		queryBean.setMrchCode(serverConf.getAccountNumberPinAn());
		queryBean.setSign(queryBean.generateSign());
		return PinAnApi.subInfoQuery(queryBean, serverConf.getPinAnServerUrl());
	}
	
	private SearchPayBo getHhlQuickSubQuick(PlatformExtractionRecord extract) {
		HhlQuickSubQueryParamBean queryParam = new HhlQuickSubQueryParamBean();
//		queryParam.setCasOrdNo(casOrdNo);
		queryParam.setMerchant_open_id(serverConf.getAccountNumberHhlQuick());
		if (extract.getExtraction_number().indexOf("DSF_") != -1) {
			queryParam.setCash_number(extract.getExtraction_number().split("_")[1]);
		}	else {
			queryParam.setCash_number(extract.getExtraction_number());
		}
		
		Date nowDate = new Date();
		queryParam.setTimestamp(nowDate.getTime() + "");
		queryParam.setKey(serverConf.getAccountKeyHhlQuick());
		queryParam.setSign(queryParam.generateSign());
		return HhlQuickPayApi.subInfoQuery(queryParam);
	}
	
	private SearchPayBo getHhlWAKSubQuick(PlatformExtractionRecord extract) {
		HhlQuickSubQueryParamBean queryParam = new HhlQuickSubQueryParamBean();
		queryParam.setMerchant_open_id(serverConf.getAccountNumberHhlQuick());
		if (extract.getExtraction_number().indexOf("DSF_") != -1) {
			queryParam.setCash_number(extract.getExtraction_number().split("_")[1]);
		}	else {
			queryParam.setCash_number(extract.getExtraction_number());
		}
		
		Date nowDate = new Date();
		queryParam.setTimestamp(nowDate.getTime() + "");
		queryParam.setKey(serverConf.getAccountKeyHhlQuick());
		queryParam.setSign(queryParam.generateSign());
		return HhlQuickPayApi.subInfoQuery(queryParam);
	}

	private SearchPayBo getNewQuickSubQuick(PlatformExtractionRecord extract) {
		NewQuickSubQueryParamBean queryParam = new NewQuickSubQueryParamBean();
//		queryParam.setCasOrdNo(casOrdNo);
		queryParam.setCustId(serverConf.getAccountNumberNewQuick());
		queryParam.setCustOrdNo(extract.getExtraction_number());
		queryParam.setOrgNo(serverConf.getCounterNumberNewQuick());
		queryParam.setVersion("2.1");
		
		queryParam.setKey(serverConf.getAccountKeyNewQuick());
		queryParam.setSign(queryParam.generateSign());
		return NewQuickPayApi.subInfoQuery(queryParam);
	}

	@Override
	public Map<String, Object> autoExtractionToPlatform() {
		YsH5QueryBalanceParamBean queryBean = new YsH5QueryBalanceParamBean();
		queryBean.setKey(serverConf.getAccountKeyYsH5());
		queryBean.setMch_id(serverConf.getAccountNumberYsH5());
		queryBean.setSign_type("MD5");
		queryBean.setType("2");
		
		queryBean.setSignature(queryBean.generateSign());
		
		Map<String, Object> res = YsH5PayApi.queryBalance(queryBean);
		if (res.get("code").equals("1")) {
			Double h5Balance = Double.parseDouble(res.get("data").toString());
			if (h5Balance > serverConf.getMaxAmountYsH5()) {
				return extractionToPlatform();
			}
			return extractionFailBy("amount < 30000");
		}
		return extractionFailBy("query balance fail");
	}

	private Map<String, Object> extractionFailBy(String msg) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", "2");
		result.put("msg", msg);
		result.put("data", "");
		return result;
	}

	private Map<String, Object> extractionToPlatform() {
		List<DictTable> bankNumbers = dictDao.getDictByType(5);
		if (bankNumbers.size() == 0) return extractionFailBy("not find bank param");
		DictTable bankParam = bankNumbers.get(0);
		PlatformExtractionRecord extract = new PlatformExtractionRecord();
		extract.setActual_money(new BigDecimal(serverConf.getMaxAmountYsH5()));
		extract.setAdmin_id(0L);
		extract.setBank_card_number(bankParam.getDict_value());
		extract.setBank_name("");
		extract.setCard_user_name(bankParam.getDict_name());
		extract.setCity_number("310000");
		extract.setExtraction_money(new BigDecimal(serverConf.getMaxAmountYsH5()));
		String code = UUID.randomUUID().toString().replaceAll("-", "");
		extract.setExtraction_number(code);
		extract.setRegist_bank("");
		extract.setRegist_bank_name("");
		extract.setService_charge(new BigDecimal(2));
		extract.setService_type(1);
		extract.setState(0);
		extract.setType(1);
		extract.setUser_mobile("auto");
		platformExtractionRecordDao.insert(extract);
		return extractionPlatform(extract);
	}
	
	@Override
	public int extractionToPlatformByAdmin(PlatformExtractionRecord extract) {
		extract.setAccount_number(serverConf.getAccountNumberYsH5());
		String code = UUID.randomUUID().toString().replaceAll("-", "");
		extract.setExtraction_number(code);
		
		return platformExtractionRecordDao.insert(extract);
		
	}

	private Map<String, Object> extractionPlatform(PlatformExtractionRecord extraction) {
		Map<String, Object> result = null;
		switch(extraction.getType()) {
		case 1:
			result = bankExtractionToPlatform(extraction);
			break;
		case 2:
			result = ysH5ExtractionToPlatform(extraction);
			break;
		case 3:
			result = kltExtractionToPlatform(extraction);
			break;
		case 4:
			result = newQuickExtractionToPlatform(extraction);
			break;
		case 5:
			result = amwyExtractionToPlatform(extraction);
			break;
		case 6:
			result = hhlYLExtractionToPlatform(extraction);
			break;
		case 7:
			result = pinAnExtractionToPlatform(extraction);
			break;
		case 8:
			result = hhlYsfExtractionToPlatform(extraction);
			break;
		case 9:
			result = hhlWAKExtractionToPlatform(extraction);
			break;
		}
		if (result == null) result = extractionFailBy("type error");
		return result;
	}
	
	private Map<String, Object> pinAnExtractionToPlatform(PlatformExtractionRecord extraction) {
		SingleDfTranDealBean subParam = new SingleDfTranDealBean();
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String formatDate = sdf.format(now);
		formatDate = formatDate.substring(2, formatDate.length());
		extraction.setExtraction_number(CommonUtils.generatePlatformOrderNumberByPinAn(serverConf.getShortNumberPinAn() + formatDate));
		platformExtractionRecordDao.updateByPrimaryKey(extraction);
		DecimalFormat df = new DecimalFormat("#0.00");
		
		subParam.setBussTypeNo("100157");
		subParam.setAgreeNo(serverConf.getCounterNumberPinAn());
		subParam.setInoutFlag("2");
		subParam.setToAcctNo(extraction.getBank_card_number());
		subParam.setToClientName(extraction.getCard_user_name());
		subParam.setCcy("RMB");
		subParam.setTranAmt(df.format(extraction.getExtraction_money()));
		subParam.setCnsmrSeqNo(extraction.getExtraction_number());
		subParam.setMrchCode(serverConf.getAccountNumberPinAn());
		
		subParam.setKey(serverConf.getAccountKeyPinAn());
		subParam.setSign(subParam.generateSign());
		Map<String, Object> result = PinAnApi.sendSubInfo(subParam, serverConf.getPinAnServerUrl());
		return result;
	}
	
	private Map<String, Object> hhlWAKExtractionToPlatform(PlatformExtractionRecord extraction) {
		HhlWAKSubParamBean subParam = new HhlWAKSubParamBean();
		subParam.setAcc_no(extraction.getBank_card_number());
		subParam.setBank_name(extraction.getBank_name());
		subParam.setCash_amount(extraction.getExtraction_money().multiply(new BigDecimal(100)).intValue() + "");
		subParam.setDistrict(extraction.getRegist_bank());
		subParam.setId_number(extraction.getCert_number());
		subParam.setMerchant_open_id(serverConf.getAccountNumberHhlQuick());
		subParam.setName(extraction.getCard_user_name());
//		subParam.setTel_no(extraction.getUser_mobile());
		Date nowDate = new Date();
		subParam.setTimestamp(nowDate.getTime() + "");
		subParam.setKey(serverConf.getAccountKeyHhlQuick());
		subParam.setSign(subParam.generateSign());
		Map<String, Object> result = HhlWAKPayApi.sendSubInfo(subParam);
		if (result.get("code").equals("1")) {
			extraction.setExtraction_number("DSF_" + result.get("cash_number"));
			platformExtractionRecordDao.updateByPrimaryKey(extraction);
		}
		return result;
	}
	
	private Map<String, Object> hhlYsfExtractionToPlatform(PlatformExtractionRecord extraction) {
		HhlYSFSubParamBean subParam = new HhlYSFSubParamBean();
		subParam.setAcc_no(extraction.getBank_card_number());
		subParam.setCash_amount(extraction.getExtraction_money().multiply(new BigDecimal(100)).intValue() + "");
		subParam.setMerchant_open_id(serverConf.getAccountNumberHhlQuick());
		subParam.setName(extraction.getCard_user_name());
//		subParam.setTel_no(extraction.getUser_mobile());
		Date nowDate = new Date();
		subParam.setTimestamp(nowDate.getTime() + "");
		subParam.setKey(serverConf.getAccountKeyHhlQuick());
		subParam.setSign(subParam.generateSign());
		Map<String, Object> result = HhlYLPayApi.sendSubInfo(subParam);
		if (result.get("code").equals("1")) {
			extraction.setExtraction_number("DSF_" + result.get("cash_number"));
			platformExtractionRecordDao.updateByPrimaryKey(extraction);
		}
		return result;
	}
	
	private Map<String, Object> hhlYLExtractionToPlatform(PlatformExtractionRecord extraction) {
		HhlQuickSubParamBean subParam = new HhlQuickSubParamBean();
		subParam.setAcc_no(extraction.getBank_card_number());
		subParam.setBank_name(extraction.getBank_name());
		subParam.setCash_amount(extraction.getExtraction_money().multiply(new BigDecimal(100)).intValue() + "");
		subParam.setDistrict(extraction.getRegist_bank());
		subParam.setId_number(extraction.getCert_number());
		subParam.setMerchant_open_id(serverConf.getAccountNumberHhlQuick());
		subParam.setName(extraction.getCard_user_name());
//		subParam.setTel_no(extraction.getUser_mobile());
		Date nowDate = new Date();
		subParam.setTimestamp(nowDate.getTime() + "");
		subParam.setKey(serverConf.getAccountKeyHhlQuick());
		subParam.setSign(subParam.generateSign());
		Map<String, Object> result = HhlQuickPayApi.sendSubInfo(subParam);
		if (result.get("code").equals("1")) {
			extraction.setExtraction_number("DSF_" + result.get("cash_number"));
			platformExtractionRecordDao.updateByPrimaryKey(extraction);
		}
		return result;
	}

	private Map<String, Object> newQuickExtractionToPlatform(PlatformExtractionRecord extraction) {
		NewQuickSubParamBean subParam = new NewQuickSubParamBean();
		subParam.setAccountName(extraction.getCard_user_name());
		subParam.setAccountType("1");
		subParam.setBankName(extraction.getBank_name());
		subParam.setVersion("2.1");
		subParam.setCallBackUrl(serverConf.getNotifyUrlNewQuick());
		subParam.setCardNo(extraction.getBank_card_number());
		subParam.setCasAmt(extraction.getExtraction_money().multiply(BigDecimal.valueOf(100)).longValue());
		subParam.setCasType("00");
//		subParam.setCnapsCode("");
		subParam.setCustId(serverConf.getAccountNumberNewQuick());
		subParam.setCustOrdNo(extraction.getExtraction_number());
		subParam.setDeductWay("02");
		subParam.setOrgNo(serverConf.getCounterNumberNewQuick());
		subParam.setSubBankName(extraction.getRegist_bank_name());
		subParam.setKey(serverConf.getAccountKeyNewQuick());
		subParam.setSign(subParam.generateSign());
		return NewQuickPayApi.sendSubInfo(subParam);
	}
	
	private Map<String, Object> amwyExtractionToPlatform(PlatformExtractionRecord extraction) {
		AMWYSubParamBean subParam = new AMWYSubParamBean();
		AMWYSubParamBean.DataContent data = new AMWYSubParamBean.DataContent();
		subParam.setHead(new AMWYSubParamBean.HeadContent());
		AMWYSubParamBean.SendBean dataEncode = new AMWYSubParamBean.SendBean();

		BankCodeEnum bankCode = BankCodeEnum.key(extraction.getBank_name());
		if (bankCode == null) return null;
		data.setBankCode(bankCode.getCode());
		data.setBankName(extraction.getBank_name());
		data.setBankSubbranch(extraction.getRegist_bank_name());
		data.setCity(extraction.getRegist_bank().split(",")[1]);
		data.setOrderId(getAMWYOrderId(extraction.getId()));
		data.setProp("0");
		data.setProvince(extraction.getRegist_bank().split(",")[0]);
		data.setWalletId(serverConf.getWalletIdYsAMWY());
		data.setAccountName(extraction.getCard_user_name());
		data.setBankAccount(extraction.getBank_card_number());
		data.setMoney(extraction.getExtraction_money().multiply(BigDecimal.valueOf(100)).longValue());
		subParam.getHead().setAppId("1001");
		subParam.getHead().setBackURL(serverConf.getNotifyUrlYsAMWY());
		subParam.getHead().setMchid(serverConf.getAccountNumberYsAMWY());
		subParam.getHead().setReqNo(extraction.getExtraction_number());
		subParam.getHead().setReqType("transfer_record_request");
		subParam.getHead().setSignType("RSA1");
		subParam.getHead().setVersion("1.0");
		dataEncode.setData(data);
		dataEncode.setHead(subParam.getHead());
		String xmlstr = XMLUtils.convertToXml(dataEncode);
		subParam.getHead().setSign(subParam.generateSign(data, serverConf.getAmwySubPrivateKey()));
		try {
			String encodedata=encodeData(Matchers.match("<data>(.*)</data>", xmlstr.replace("\n", "").replace("\t", "")), serverConf.getAccountNumberYsAMWY());
			subParam.setData(encodedata);
		} catch (Exception e) {
			logger.info("not sign param data");
		}
		return AMWYPayApi.sendSubInfo(subParam);
	}

	private String getAMWYOrderId(Long id) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
		Date now = new Date();
		String idStr = id.toString();
		StringBuffer sb = new StringBuffer();
		sb.append(sf.format(now));
		for (int i = 0 ; i < (10-idStr.length()) ; i++) {
			sb.append("0");
		}
		sb.append(idStr);
		return sb.toString();
	}

	private Map<String, Object> kltExtractionToPlatform(PlatformExtractionRecord extraction) {
		return null;
	}

	private Map<String, Object> ysH5ExtractionToPlatform(PlatformExtractionRecord extraction) {
		YsH5SubParamBean subParam = new YsH5SubParamBean();
		subParam.setVersion("1.0");
		subParam.setBank_code("");
		subParam.setPayee_branch_name("");
		subParam.setPayee_branch_no("");
		subParam.setCard_type("1");
		subParam.setPayee_acct_type("1");
		subParam.setCvv2("");
		subParam.setIdcard_no("");
		subParam.setMch_id(serverConf.getAccountNumberYsH5());
		subParam.setMobile("");
		subParam.setNotify_url(serverConf.getNotifyUrlYsH5());
		subParam.setOut_order_no(extraction.getExtraction_number());
		subParam.setPayee_acct_name(extraction.getCard_user_name());
		subParam.setPayee_acct_no(extraction.getBank_card_number());
		subParam.setPayment_fee(extraction.getExtraction_money().multiply(new BigDecimal(100)).intValue());
		subParam.setRemark("361pay");
		subParam.setSettle_type("1");
		subParam.setSign_type("MD5");
		
		subParam.setKey(serverConf.getAccountKeyYsH5());
		subParam.setSignature(subParam.generateSign());
		return YsH5PayApi.sendSubInfo(subParam);
	}

	private Map<String, Object> bankExtractionToPlatform(PlatformExtractionRecord extraction) {
		// TODO Auto-generated method stub
		return null;
	}

	public Page<PlatformExtractionRecord> extractionListByAdmin(QueryBean query) {
		if (query.getState() == null) query.setState(-1);
		Page<PlatformExtractionRecord> page = new Page<PlatformExtractionRecord>(query.getPage_index(), query.getPage_size());
        int count = platformExtractionRecordDao.extractionListByAdminCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(platformExtractionRecordDao.extractionListByAdminPage(query));
        return page;
	}

	@Override
	public Map<String, Object> queryProvideBalance(int type) {
		Map<String, Object> result = null;
		switch(type) {
		case 1:
			result = queryBankProvideBalance();
			break;
		case 2:
			result = queryH5ProvideBalance();
			break;
		case 3:
			result = queryKltProvideBalance();
			break;
		case 4:
			result = queryNewQuickProvideBalance();
			break;
		case 5:
			result = queryAMWYProvideBalance();
			break;
		case 6:
			result = queryHhlQuickProvideBalance();
			break;
		case 7:
			result = queryPinAnProvideBalance();
			break;
		}
		if (result == null) result = extractionFailBy("type error");
		return result;
	}
	
	private Map<String, Object> queryPinAnProvideBalance() {
		CorAcctBalanceQueryBean queryBean = new CorAcctBalanceQueryBean();
		queryBean.setAccount(serverConf.getAccountNumberPinAn());
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dateStr = sdf.format(now);
		dateStr = dateStr.substring(2, dateStr.length());
		queryBean.setCnsmrSeqNo(CommonUtils.generatePlatformOrderNumberByPinAn(serverConf.getShortNumberPinAn() + dateStr));
		queryBean.setKey(serverConf.getAccountKeyPinAn());
		queryBean.setCcyCode("RMB");
		queryBean.setSign(queryBean.generateSign());
		Map<String, Object> res = PinAnApi.queryBalance(queryBean, serverConf.getPinAnServerUrl());
		res.put("code", Integer.parseInt(res.get("code").toString()));
		return res;
	}
	
	private Map<String, Object> queryHhlQuickProvideBalance() {
		NewQuickQueryBalanceParamBean queryBean = new NewQuickQueryBalanceParamBean();
		Map<String, Object> res = HhlQuickPayApi.queryBalance(queryBean);
		res.put("code", Integer.parseInt(res.get("code").toString()));
		res.put("data", "0.0");
		return res;
	}

	private Map<String, Object> queryAMWYProvideBalance() {
		AMWYQueryBalanceParamBean queryParam = new AMWYQueryBalanceParamBean();
		queryParam.setHead(new AMWYQueryBalanceParamBean.HeadContent());
		AMWYQueryBalanceParamBean.DataContent data = new AMWYQueryBalanceParamBean.DataContent();
		AMWYQueryBalanceParamBean.SendBean dataEncode = new AMWYQueryBalanceParamBean.SendBean();
		
		data.setbCode(serverConf.getAccountNumberYsAMWY());
		data.setWalletId(serverConf.getWalletIdYsAMWY());

		queryParam.getHead().setAppId("1001");
		queryParam.getHead().setBackURL("");
		queryParam.getHead().setMchid(serverConf.getAccountNumberYsAMWY());
		String code = UUID.randomUUID().toString().replaceAll("-", "");
		queryParam.getHead().setReqNo(code);
		queryParam.getHead().setReqType("transfer_query_wallet_request");
		queryParam.getHead().setSignType("RSA1");
		queryParam.getHead().setVersion("1.0");
		dataEncode.setData(data);
		dataEncode.setHead(queryParam.getHead());
		String xmlstr = XMLUtils.convertToXml(dataEncode);
		queryParam.getHead().setSign(queryParam.generateSign(data, serverConf.getAmwySubPrivateKey()));
		try {
			String encodedata=encodeData(Matchers.match("<data>(.*)</data>", xmlstr.replace("\n", "").replace("\t", "")), serverConf.getAccountNumberYsAMWY());
			queryParam.setData(encodedata);
		} catch (Exception e) {
			logger.info("not sign param data");
		}
		DecimalFormat df = new DecimalFormat("0.00");
		Map<String, Object> res = AMWYPayApi.queryBalance(queryParam);
		res.put("code", Integer.parseInt(res.get("code").toString()));
		res.put("data", df.format(new BigDecimal(res.get("data").toString()).divide(new BigDecimal(100))));
		return res;
	}
	
	/**
	 * 加密
	 * @param data
	 * @param mchid
	 * @return
	 * @throws Exception 
	 */
	public String encodeData(String dataxml,String mchid) throws Exception{
		if(StringUtils.isBlank(dataxml))return "";
		String subMchPubKey=serverConf.getAmwySubPublicKey();
		try {
			byte[] datastr = APISecurityUtils.encryptByPublicKey(dataxml.toString().getBytes("UTF-8"), subMchPubKey);
			return new String(Base64Utils.encode(datastr),"UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("加密失败");
		}
	}

	private Map<String, Object> queryNewQuickProvideBalance() {
		NewQuickQueryBalanceParamBean queryBean = new NewQuickQueryBalanceParamBean();
		queryBean.setKey(serverConf.getAccountKeyNewQuick());
		queryBean.setCustId(serverConf.getAccountNumberNewQuick());
		queryBean.setOrgNo(serverConf.getCounterNumberNewQuick());
		queryBean.setVersion("2.1");
		queryBean.setSign(queryBean.generateSign());
		DecimalFormat df = new DecimalFormat("0.00");
		Map<String, Object> res = NewQuickPayApi.queryBalance(queryBean);
		res.put("code", Integer.parseInt(res.get("code").toString()));
		res.put("data", df.format(new BigDecimal(res.get("data").toString()).divide(new BigDecimal(100))));
		return res;
	}

	private Map<String, Object> queryKltProvideBalance() {
		// TODO Auto-generated method stub
		return extractionFailBy("not klt");
	}

	private Map<String, Object> queryBankProvideBalance() {
		return extractionFailBy("not bank");
	}

	private Map<String, Object> queryH5ProvideBalance() {
		YsH5QueryBalanceParamBean queryBean = new YsH5QueryBalanceParamBean();
		queryBean.setKey(serverConf.getAccountKeyYsH5());
		queryBean.setMch_id(serverConf.getAccountNumberYsH5());
		queryBean.setSign_type("MD5");
		queryBean.setType("2");
		
		queryBean.setSignature(queryBean.generateSign());
		DecimalFormat df = new DecimalFormat("0.00");
		Map<String, Object> res = YsH5PayApi.queryBalance(queryBean);
		res.put("code", Integer.parseInt(res.get("code").toString()));
		res.put("data", df.format(new BigDecimal(res.get("data").toString()).divide(new BigDecimal(100))));
		return res;
	}

	@Override
	public int extractionExamineByAdmin(Long extraction_id, Integer type) {
		if (type == 2) return refuseExtractionByAdmin(extraction_id);
		if (type != 1) return -3;
		return adoptExtractionByAdmin(extraction_id);
	}

	private int adoptExtractionByAdmin(Long extraction_id) {
		PlatformExtractionRecord extract = platformExtractionRecordDao.getObjectById(new PlatformExtractionRecord(extraction_id));
		if (extract == null) return -2;
		Map<String, Object> res = extractionPlatform(extract);
		if (!res.get("code").equals("1")) {
			extract.setState(2);
			remoteMessage = res.get("msg").toString();
			platformExtractionRecordDao.updateByPrimaryKey(extract);
			return -5;
		}
		extract.setState(4);
		return platformExtractionRecordDao.updateByPrimaryKey(extract);
	}

	private int refuseExtractionByAdmin(Long extraction_id) {
		PlatformExtractionRecord extract = platformExtractionRecordDao.getObjectById(new PlatformExtractionRecord(extraction_id));
		if (extract == null) return -2;
		extract.setState(2);
		return platformExtractionRecordDao.updateByPrimaryKey(extract);
	}

}
