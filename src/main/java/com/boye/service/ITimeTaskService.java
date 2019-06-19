package com.boye.service;

import java.util.List;

import com.boye.bean.entity.TaskInfo;
import com.boye.bean.entity.TimeTaskInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface ITimeTaskService {

	int createTimeTask(TimeTaskInfo timeTaskInfo);

	int editTimeTask(TimeTaskInfo timeTaskInfo);

	int deleteTimeTaskById(Long id);

	Page<TimeTaskInfo> findTimeTaskByPage(QueryBean query);

	List<TimeTaskInfo> findTimeTaskAll();

	TimeTaskInfo findTimeTaskInfoById(Long id);

	int settingTaskEnable(Long id, Integer enable);

}
