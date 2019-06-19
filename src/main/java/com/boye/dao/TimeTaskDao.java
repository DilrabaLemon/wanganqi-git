package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.TaskInfo;
import com.boye.bean.entity.TimeTaskInfo;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface TimeTaskDao extends BaseMapper<TimeTaskInfo> {

	@Select("select * from time_task_info where delete_flag = 0")
	List<TimeTaskInfo> getTimeTaskInfoAll();

	int getTimeTaskInfoListByCount(QueryBean query);

	List<TimeTaskInfo> getTimeTaskInfoListByPage(QueryBean query);

	@Select("select * from time_task_info where delete_flag = 0 and time_task_name = #{time_task_name}")
	TimeTaskInfo findTimeTaskByTimeTaskName(String time_task_name);
}
