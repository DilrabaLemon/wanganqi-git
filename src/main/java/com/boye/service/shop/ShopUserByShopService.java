package com.boye.service.shop;

import com.boye.bean.ShopUserDataStatisticsBean;
import com.boye.bean.bo.ShopConfigInfoBean;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.ShopInformationBean;

public interface ShopUserByShopService {
	
	ShopUserInfo shopUserLogin(String login_number, String password,String ip);
	
	ShopUserInfo getShopUserInfoByShop(ShopUserInfo shopUserInfo);
	
	 Page<ShopConfigInfoBean> getShopConfigByShopUserInfoId(Long shopUserInfoId , QueryBean query);
	 
	 ShopUserDataStatisticsBean getShopDataStatistics(Long shopUserInfoId);
	 
	 int shopEditShopUserInfo(ShopUserInfo shopUserInfo);
	 
	 void shopEditBankCard(ShopUserInfo shopUserInfo);

	int changePassword(String oldPassword, String newPassword, ShopUserInfo shopUser);

	int changeExtractionCode(String oldExtractionCode, String newExtractionCode, ShopUserInfo shopUser);

	ShopUserInfo getShopUserInfoByShopSubOpenKey(ShopUserInfo shopUser);
}
