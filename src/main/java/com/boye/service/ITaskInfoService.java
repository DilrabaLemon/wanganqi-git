package com.boye.service;

import java.util.List;

import com.boye.bean.entity.TaskInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.TaskInfoVo;

public interface ITaskInfoService {

	int createTaskInfo(TaskInfoVo taskInfoVo);

	int editTaskInfo(TaskInfoVo taskInfoVo);

	int deleteTaskInfoById(Long id);

	Page<TaskInfo> findTaskInfoByPage(QueryBean query);

	List<TaskInfo> findTaskInfoAll();

	TaskInfo findTaskInfoById(Long id);

}
