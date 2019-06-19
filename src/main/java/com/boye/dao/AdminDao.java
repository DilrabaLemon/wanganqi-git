package com.boye.dao;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.vo.QueryBean;


@Mapper
public interface AdminDao extends BaseMapper<AdminInfo> {

    AdminInfo adminLogin(@Param("login_number") String login_number, @Param("password") String password);
    
    AdminInfo getAdminByLoginNumber(String login_number);
//
//    int addAdmin(AdminInfo adminInfo);
//
//    // 禁用/启用账户
//    int deleteOrOnAdmin(@Param("admin_id") String admin_id, @Param("del_flag") int del_flag);
//
//    int count();
//
//    //获取管理员列表
//    List<AdminInfo> findByPage(@Param("page") Page<AdminInfo> page);

	Double getTurnoverByCondition(Date time);
	
	Double getExtractionByCondition(Date time);
	
	Double getTurnover();
	
	Double getExtraction();

	Double getTurnoverByLastMonth(@Param("now_month")Date now_month, @Param("last_month")Date last_month);

	Double getExtractionByLastMonth(@Param("now_month")Date now_month, @Param("last_month")Date last_month);

	int getShopCountByCondition(Date time);

	int getShopCount();

	int getAgentCount();

	double getAgentMoney();

	double getPlatformPoundage();
	// 条件查询管理员列表记录数
	int getAdminListByCount(QueryBean query);
	// 条件查询管理员列表分页数据
	List<AdminInfo> getAdminListByPage(QueryBean query);

	@Select("select * from platform_admin_info where delete_flag = 0 and login_number = #{login_number}")
	AdminInfo findAdminByLoginNumber(String login_number);

	@Update("UPDATE platform_admin_info SET google_auth_flag = #{googleAuthFlag} WHERE id = #{id}")
	int changeGoogleAuthFlagById(@Param("id")long id, @Param("googleAuthFlag")int googleAuthFlag);
}
