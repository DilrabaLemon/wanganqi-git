package com.boye.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boye.bean.entity.PassagewayInfo;
import com.boye.bean.entity.ProvideInfo;
import com.boye.bean.entity.TaskInfo;
import com.boye.bean.entity.TimeTaskInfo;
import com.boye.bean.vo.BalanceTransferParam;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.vo.TaskInfoVo;
import com.boye.dao.TaskInfoDao;
import com.boye.service.ITaskInfoService;
import com.google.gson.Gson;

@Service
public class TaskInfoServiceImpl implements ITaskInfoService {
	
	private Gson gson = new Gson();
	
	@Autowired
	private TaskInfoDao taskInfoDao;

	@Override
	public int createTaskInfo(TaskInfoVo taskInfoVo) {
		TaskInfo taskInfo = taskInfoVo.getTaskInfo();
		TaskInfo findTask = taskInfoDao.findTimeTaskByTaskName(taskInfo.getTask_name());
		if (findTask != null) return -1;
		BalanceTransferParam transferParam = taskInfoVo.getTransferParam();
		taskInfo.setTask_param_json(gson.toJson(transferParam));
		return taskInfoDao.insert(taskInfo);
	}

	@Override
	public int editTaskInfo(TaskInfoVo taskInfoVo) {
		TaskInfo taskInfo = taskInfoVo.getTaskInfo();
		TaskInfo findTask = taskInfoDao.findTimeTaskByTaskName(taskInfo.getTask_name());
		if (findTask != null) {
			if (!findTask.getId().equals(taskInfo.getId())) return -1;
		}
		BalanceTransferParam transferParam = taskInfoVo.getTransferParam();
		TaskInfo findTaskInfo = taskInfoDao.getObjectById(taskInfo);
		findTaskInfo.setTask_name(taskInfo.getTask_name());
		findTaskInfo.setTask_type(taskInfo.getTask_type());
		findTaskInfo.setTask_param_json(gson.toJson(transferParam));
		return taskInfoDao.updateByPrimaryKey(findTaskInfo);
	}

	@Override
	public int deleteTaskInfoById(Long id) {
		TaskInfo findTaskInfo = taskInfoDao.getObjectById(new TaskInfo(id));
		if (findTaskInfo != null) {
			findTaskInfo.setDelete_flag(1);
			return taskInfoDao.updateByPrimaryKey(findTaskInfo);
		}
		return 0;
	}

	@Override
	public Page<TaskInfo> findTaskInfoByPage(QueryBean query) {
		Page<TaskInfo> page = new Page<TaskInfo>(query.getPage_index(), query.getPage_size());
		page.setTotals(taskInfoDao.getTaskInfoListByCount(query));
		if (page.getTotals() == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(taskInfoDao.getTaskInfoListByPage(query));
        return page;
	}

	@Override
	public List<TaskInfo> findTaskInfoAll() {
		return taskInfoDao.getTaskInfoAll();
	}

	@Override
	public TaskInfo findTaskInfoById(Long id) {
		return taskInfoDao.getObjectById(new TaskInfo(id));
	}

}
