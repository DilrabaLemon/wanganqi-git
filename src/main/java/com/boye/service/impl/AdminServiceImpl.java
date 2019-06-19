package com.boye.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.boye.bean.PlatformDataStatisticsBean;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminLoginRecord;
import com.boye.bean.entity.DailyBalanceHistory;
import com.boye.bean.entity.UserDailyBalanceHistory;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.utils.FormatUtils;
import com.boye.dao.AdminDao;
import com.boye.dao.AdminLoginRecordDao;
import com.boye.dao.DailyBalanceHistoryDao;
import com.boye.dao.PlatformBalanceNewDao;
import com.boye.dao.UserDailyBalanceHistoryDao;
import com.boye.service.IAdminService;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class AdminServiceImpl extends BaseServiceImpl implements IAdminService {

    @Resource
    private AdminDao adminDao;
    
    @Autowired
    private PlatformBalanceNewDao platformBalanceNewDao;
    
    @Autowired
    private UserDailyBalanceHistoryDao userDailyBalanceHistoryDao;
    
    @Autowired
    private DailyBalanceHistoryDao dailyBalanceHistoryDao;
    
    @Autowired
    private AdminLoginRecordDao adminLoginRecordDao;

    @Override
    public AdminInfo adminLogin(String login_number, String password, String ip) {
        AdminInfo admin = adminDao.findAdminByLoginNumber(login_number); // 根据账号密码去查询管理员对象
        if (admin == null) return null;
        if (admin.getLogin_error_count() > 4) return new AdminInfo(-3L);
        if (!admin.getPassword().equals(password)) {
        	admin.setLogin_error_count(admin.getLogin_error_count() + 1);
        	adminDao.updateByPrimaryKey(admin);
        	return null;
        }
        admin.setLast_login_time(new Timestamp(new Date().getTime()));
        adminDao.updateByPrimaryKey(admin);
        // 登入日志记录
        AdminLoginRecord adminLoginRecord = new AdminLoginRecord();
        adminLoginRecord.setAdmin_id(admin.getId());
        adminLoginRecord.setLogin_ip(ip);
        adminLoginRecord.setState(1);
        adminLoginRecord.setDelete_flag(0);
        adminLoginRecordDao.insert(adminLoginRecord);
        return admin;
    }

    @Override
    public int addAdmin(AdminInfo adminInfo) {
        AdminInfo findAdmin = adminDao.getAdminByLoginNumber(adminInfo.getLogin_number()); //通过登录号获取管理员对象
        if (findAdmin != null) return -2;
        adminInfo.setDelete_flag(0);
        String code = UUID.randomUUID().toString().replaceAll("-", "");
        adminInfo.setAdmin_code(code);
        return adminDao.insert(adminInfo);
    }

    @Override
    public int editAdmin(AdminInfo adminInfo) {
        AdminInfo findAdmin = adminDao.getAdminByLoginNumber(adminInfo.getLogin_number());
        if (findAdmin != null) {
            if (!findAdmin.getId().equals(adminInfo.getId())) return -2;
        } else {
            findAdmin = adminDao.getObjectById(adminInfo);
            if (findAdmin == null) return -1;
        }
        findAdmin.setAdmin_name(adminInfo.getAdmin_name());
        findAdmin.setPassword(adminInfo.getPassword());
        findAdmin.setRole_id(adminInfo.getRole_id());
        return adminDao.updateByPrimaryKey(findAdmin);
    }

    @Override
    public int deleteAdmin(String admin_id) {
        AdminInfo admin = new AdminInfo();
        admin.setId(Long.parseLong(admin_id));
        admin = adminDao.getObjectById(admin);
        admin.setDelete_flag(1);
        int result = adminDao.updateByPrimaryKey(admin);
        return result;
    }

    @Override
    public Page<AdminInfo> adminList(QueryBean query) {
        Page<AdminInfo> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = adminDao.getAdminListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(adminDao.getAdminListByPage(query));
        return page;
    }

    @Override
    public Map<String, Object> getPlatformDataStatistics() {
        PlatformDataStatisticsBean platformBean = new PlatformDataStatisticsBean();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        platformBean.setDay_turnover(FormatUtils.getMoneyString(adminDao.getTurnoverByCondition(cal.getTime())));
        platformBean.setDay_extraction(FormatUtils.getMoneyString(adminDao.getExtractionByCondition(cal.getTime())));
        cal.set(Calendar.DAY_OF_MONTH, 1);
        platformBean.setMonth_turnover(FormatUtils.getMoneyString(adminDao.getTurnoverByCondition(cal.getTime())));
        platformBean.setMonth_extraction(FormatUtils.getMoneyString(adminDao.getExtractionByCondition(cal.getTime())));
        Date now_month = cal.getTime();
        cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
        platformBean.setLast_month_turnover(FormatUtils.getMoneyString(adminDao.getTurnoverByLastMonth(now_month, cal.getTime())));
        platformBean.setLast_month_extraction(FormatUtils.getMoneyString(adminDao.getExtractionByLastMonth(now_month, cal.getTime())));
        platformBean.setBalanceList(platformBalanceNewDao.getAllBalance());
        //获取平台昨日统计数据
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("PlatformDataStatisticsBean", platformBean);
        List<DailyBalanceHistory> dailyList = dailyBalanceHistoryDao.getDailyBalanceHistoryNews();
        if (dailyList.size() > 0) {
        	hashMap.put("balance_change", dailyList.get(0).getBalance_change().toString());
        	hashMap.put("income_money", dailyList.get(0).getIncome_money().toString());
        	hashMap.put("expenditure_money", dailyList.get(0).getExpenditure_money().toString());
        	hashMap.put("day_count", dailyList.get(0).getDay_count().toString());
        	hashMap.put("type", dailyList.get(0).getType().toString());
        }else {
        	hashMap.put("balance_change", DailyBalanceHistory.getNullDailyBalanceHistory().getBalance_change().toString());
        	hashMap.put("income_money", DailyBalanceHistory.getNullDailyBalanceHistory().getIncome_money().toString());
        	hashMap.put("expenditure_money", DailyBalanceHistory.getNullDailyBalanceHistory().getExpenditure_money().toString());
        	hashMap.put("day_count", DailyBalanceHistory.getNullDailyBalanceHistory().getDay_count().toString());
        	hashMap.put("type", DailyBalanceHistory.getNullDailyBalanceHistory().getType().toString());
        }
        return hashMap;
    }

    @Override
    public AdminInfo adminInfo(String admin_id) {
        AdminInfo admin = new AdminInfo();
        admin.setId(Long.parseLong(admin_id));
        return adminDao.getObjectById(admin); // 根据id去查询管理员信息
    }

    @Override
    public Map<String, Object> getComprehensiveStatistics() {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        resultMap.put("shopCountByDay", adminDao.getShopCountByCondition(cal.getTime())); //按条件获取店铺数量
        resultMap.put("turnoverByDay", adminDao.getTurnoverByCondition(cal.getTime())); // 按条件获取营业额
        resultMap.put("shopCount", adminDao.getShopCount()); //获取店铺数量
        resultMap.put("turnover", adminDao.getTurnover());// 获取营业额
        resultMap.put("agentCount", adminDao.getAgentCount());//获取代理人数
        resultMap.put("agentMoney", adminDao.getAgentMoney());//获取代理金额
        resultMap.put("platformPoundage", adminDao.getPlatformPoundage());//平台手续费
        return resultMap;
    }

	@Override
	public Page<UserDailyBalanceHistory> getUserDailyBalanceHistoryByAdmin(QueryBean query) {
		Page<UserDailyBalanceHistory> page = new Page<>(query.getPage_index(), query.getPage_size());
		Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR, 12);
        cal.set(Calendar.MINUTE,12);
        cal.set(Calendar.SECOND, 12);
        cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH ) - 1);
		query.setStart_time(cal.getTime()+"");
		int count = userDailyBalanceHistoryDao.getUserDailyBalanceHistoryConutByAdmin(query);
		 page.setTotals(count);
	        if (count == 0) {
	            page.setDatalist(new ArrayList<>());
	        }else {
	            page.setDatalist(userDailyBalanceHistoryDao.getUserDailyBalanceHistoryPageByAdmin(query));
	        }
	        return page;
	}

	@Override
	public int changeGoogleAuthFlag(long id, int googleAuthFlag) {
		return adminDao.changeGoogleAuthFlagById(id, googleAuthFlag);
	}
}
