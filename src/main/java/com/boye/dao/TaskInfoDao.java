package com.boye.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.boye.base.mapper.BaseMapper;
import com.boye.bean.entity.TaskInfo;
import com.boye.bean.vo.QueryBean;

@Mapper
public interface TaskInfoDao extends BaseMapper<TaskInfo> {

	@Select("select * from task_info where delete_flag = 0")
	List<TaskInfo> getTaskInfoAll();

	int getTaskInfoListByCount(QueryBean query);

	List<TaskInfo> getTaskInfoListByPage(QueryBean query);

	@Select("select * from task_info where delete_flag = 0 and task_name = #{task_name}")
	TaskInfo findTimeTaskByTaskName(String task_name);

}
