package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.PassagewayConfig;
import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface PassagewayConfigDao extends BaseMapper<PassagewayConfig> {

	@Select("select * from passageway_config where passageway_id = #{passageway_id} and mapping_passageway_id = #{mapping_passageway_id} and shop_id = #{shop_id} and delete_flag = 0")
	PassagewayConfig findByIdAndSubIdAndShopId(PassagewayConfig passagewayConfig);

	int getPassagewayConfigListByCount(QueryBean query);

	List<PassagewayConfig> getPassagewayConfigListByPage(QueryBean query);

	@Delete("delete from passageway_config where id = #{id}")
	int delete(Long id);

	List<PassagewayConfig> findConfigByPassagewayIdAndMoney(@Param("passageway_id") Long passageway_id, @Param("shop_id")Long shop_id, @Param("money") String money);

}
