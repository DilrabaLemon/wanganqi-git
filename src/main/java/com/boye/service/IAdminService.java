package com.boye.service;

import com.boye.bean.vo.Page;
import java.util.Map;

import com.boye.bean.PlatformDataStatisticsBean;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.UserDailyBalanceHistory;

public interface IAdminService {

	AdminInfo adminLogin(String login_number, String password, String ip);

	int addAdmin(AdminInfo adminInfo);
	
	int editAdmin(AdminInfo adminInfo);

	int deleteAdmin(String admin_id);

	Page<AdminInfo> adminList(QueryBean query);

	Map<String, Object> getPlatformDataStatistics();

	AdminInfo adminInfo(String admin_id);

	Map<String, Object> getComprehensiveStatistics();

	Page<UserDailyBalanceHistory> getUserDailyBalanceHistoryByAdmin(QueryBean queryBean);

	int changeGoogleAuthFlag(long id, int googleAuthFlag);

}
