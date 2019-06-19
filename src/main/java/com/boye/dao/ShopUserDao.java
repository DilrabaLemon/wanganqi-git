package com.boye.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.bo.ShopUserBo;
import com.boye.bean.entity.ShopBalanceNew;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.ShopInformationBean;

@Mapper
public interface ShopUserDao extends BaseMapper<ShopUserInfo> {

	ShopUserInfo shopUserLogin(@Param("login_number") String login_number, @Param("password") String password);
    
	ShopUserInfo getShopUserByLoginNumber(String login_number);

	List<ShopUserInfo> getShopInfoPage(QueryBean query);

	int getShopInformationCount(QueryBean query);
	// 代理商获取商户信息 总记录数
	int getShopInformationCountByAgent(QueryBean query);
	// 代理商获取商户信息 分页数据
	List<ShopInformationBean> getShopInformationPageByAgent(QueryBean query);
	
	//获取代理商下的所有商户
	List<ShopUserInfo> getShopInfoByAgent(Long agent_id);

	ShopInformationBean getShopInformation(Long id);

	Double getTurnoverByCondition(@Param("time")Date time, @Param("shop_id")Long shop_id);

	Double getExtractionByCondition(@Param("time")Date time, @Param("shop_id")Long shop_id);

	Double getTurnover(Long shop_id);

	Double getExtraction(Long shop_id);

	ShopInformationBean getShopInformationByAgent(long parseLong);

	int getShopUserCountByAgentId(Long id);

	ShopUserInfo getShopUserByOpenKey(String shop_open_key);

	Double getTurnoverByLastMonth(@Param("now_month")Date now_month, @Param("last_month")Date last_month, @Param("shop_id")Long shop_id);

	Double getExtractionByLastMonth(@Param("now_month")Date now_month, @Param("last_month")Date last_month, @Param("shop_id")Long shop_id);
	
	List<ShopUserInfo> shopUserIDAndName();

	List<ShopBalanceNew> shopUserListCount(QueryBean query);

	List<ShopUserInfo> findAll();

	List<ShopUserBo> findPassagewayConfigNotUser(@Param("passageway_id")Long passageway_id, @Param("mapping_passageway_id")Long mapping_passageway_id);

	@Update("update shop_user_info set google_auth_flag = #{googleAuthFlag} where id = #{id}")
	int changeGoogleAuthFlagById(@Param("id")long id, @Param("googleAuthFlag")int googleAuthFlag);
}
