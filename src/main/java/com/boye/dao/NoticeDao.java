package com.boye.dao;

import com.boye.bean.entity.NoticeInfo;
import com.boye.bean.vo.QueryBean;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.boye.base.mapper.BaseMapper;

@Mapper
public interface NoticeDao extends BaseMapper<NoticeInfo> {
	// 条件查询公共 总记录数
	int getNoticeInfoListByCount(QueryBean query);
	
	List<NoticeInfo> getNoticeInfoListByPage(QueryBean query);
}
