package com.boye.service;

import java.util.List;

import com.boye.bean.bo.ShopOpenKeyBo;
import com.boye.bean.bo.ShopUserBo;
import com.boye.bean.entity.AgentInfo;
import com.boye.bean.entity.ShopBalanceNew;
import com.boye.bean.entity.ShopConfig;
import com.boye.bean.entity.ShopSubConfig;
import com.boye.bean.entity.ShopUserAuditing;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.entity.SubPaymentWhiteIp;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.PassagewayShopStatistics;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.ShopInformationBean;

public interface IShopUserService {

	//ShopUserInfo shopUserLogin(String login_number, String password,String ip);
	
	int addShopUser(ShopUserInfo shopInfo);
	
	int editShopUser(ShopUserInfo shopInfo);

	int deleteShopUser(String shopUser_id);
	
	int addShopConfig(ShopConfig shopConfig);

	int editShopConfig(ShopConfig shopInfo);
	
	//int editShopConfigByAgent(ShopConfig shopConfig);
	
	ShopUserInfo getShopInfoByAdmin(String shop_id);
	
	List<ShopConfig> shopConfigListByAgent(AgentInfo agent, String shop_id);

	Page<ShopUserInfo> shopUserList(QueryBean query);

	ShopInformationBean getShopInfo(ShopUserInfo shopUser);

	//Page<ShopInformationBean> shopUserPageByAgent(AgentInfo agent, QueryBean query);

	ShopOpenKeyBo getShopOpenKey(String shop_id);

	int changeShopPassword(String shop_id, String new_password);

	int shopFrozen(String shop_id, Integer state);

	int shopExtractionAvailable(String shop_id, Integer state);

//	ShopUserDataStatisticsBean getAgentDataStatistics(ShopUserInfo shopUser);

	//ShopInformationBean getShopInfoByAgent(String shop_id);

	Page<ShopConfig> shopConfigList(QueryBean query, String shop_id);

	//Page<ShopConfig> shopConfigPageByAgent(AgentInfo agent, QueryBean query, String shop_id);

	Page<ShopConfig> getShopConfigByShop(ShopUserInfo shopUser, QueryBean query);

	int setShopReturnSite(ShopUserInfo shopUser, String return_site);

	int deleteShopConfig(String config_id);
	
	Page<ShopUserAuditing> getShopAuditingByAgent(QueryBean query);

	ShopUserAuditing findShopAuditingByAgent(Integer shopUserAuditing_id);

	int editShopAuditingByAgent(ShopUserInfo shopUserInfo);

	int refuseShopAuditingByAgent(Integer shopUserAuditing_id);

	List<ShopUserInfo> shopUserIDAndName();

	int enableShopConfig(String config_id, int enable);

	int editShopMinAmount(String shop_id, Double min_amount);

	int passagewayShopTurnoverRateByDay();

	Page<PassagewayShopStatistics> getpassagewayShopTurnoverRateByDay(QueryBean query);

	List<ShopBalanceNew> shopUserListCount(QueryBean query);

	int editShopMinExtraction(String shop_id, Double min_extraction);

	ShopUserInfo getShopUserInfo(ShopUserInfo shopUserInfo);

	int insertAndEditSubPaymentWhiteIp(SubPaymentWhiteIp subPaymentWhiteIp);

	int updateSubPaymentWhiteIp(SubPaymentWhiteIp subPaymentWhiteIp);

	int deleteSubPaymentWhiteIp(Long shop_id);

	SubPaymentWhiteIp getSubPaymentWhiteIp(Long shop_id);

	ShopUserInfo getShopInfo(String shopPhone);

	int updateRedisByShop();

	Page<ShopSubConfig> shopSubConfigList(QueryBean query, String shop_id);

	int deleteShopSubConfig(String config_id);

	int enableShopSubConfig(String config_id, int enable);

	int addShopSubConfig(ShopSubConfig shopSubConfig);

	int editShopSubConfig(ShopSubConfig shopSubConfig);

	List<ShopUserBo> findNotConfigUser(Long passageway_id, Long mapping_passageway_id);

	int changeShopSubOpenKey(Long shop_id);

	int changeGoogleAuthFlag(long id, int googleAuthFlag);

}
