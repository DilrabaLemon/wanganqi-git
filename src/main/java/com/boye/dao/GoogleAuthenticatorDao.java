package com.boye.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.GoogleAuthenticatorBean;

@Mapper
public interface GoogleAuthenticatorDao extends BaseMapper<GoogleAuthenticatorBean> {

	@Select("select * from google_authenticator where delete_flag = 0 and user_id = #{id} and user_type = #{type}")
	GoogleAuthenticatorBean getByUserIdAndType(@Param("id")long id, @Param("type")int type);

	@Delete("delete from google_authenticator where id = #{id}")
	int deleteGoogleAhtenById(@Param("id")Long id);

}
