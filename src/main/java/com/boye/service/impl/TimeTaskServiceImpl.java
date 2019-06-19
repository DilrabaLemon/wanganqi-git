package com.boye.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.boye.base.constant.Constants;
import com.boye.bean.entity.TimeTaskInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.dao.TimeTaskDao;
import com.boye.service.ITimeTaskService;

@Service
public class TimeTaskServiceImpl implements ITimeTaskService {
	
	@Autowired
	private TimeTaskDao timeTaskDao;

	@Override
	public int createTimeTask(TimeTaskInfo timeTaskInfo) {
		TimeTaskInfo findTimeTask = timeTaskDao.findTimeTaskByTimeTaskName(timeTaskInfo.getTime_task_name());
		if (findTimeTask != null) return -1;
		timeTaskInfo.setTask_enable(2);
		return timeTaskDao.insert(timeTaskInfo);
	}

	@Override
	public int editTimeTask(TimeTaskInfo timeTaskInfo) {
		TimeTaskInfo findTimeTask = timeTaskDao.findTimeTaskByTimeTaskName(timeTaskInfo.getTime_task_name());
		if (findTimeTask != null) {
			if (!findTimeTask.getId().equals(timeTaskInfo.getId())) return -1;
		}
		findTimeTask = timeTaskDao.getObjectById(timeTaskInfo);
		findTimeTask.setTask_id(timeTaskInfo.getTask_id());
		findTimeTask.setTask_run_type(timeTaskInfo.getTask_run_type());
		findTimeTask.setTask_start_time(timeTaskInfo.getTask_start_time());
		findTimeTask.setTime_task_name(timeTaskInfo.getTime_task_name());
		findTimeTask.setTime_task_type(timeTaskInfo.getTime_task_type());
		Date now = new Date();
		if (now.before(findTimeTask.getTask_start_time())) {
			findTimeTask.setTask_next_time(findTimeTask.getTask_start_time());
		} else {
			findTimeTask.setTask_next_time(nextStartTaskTime(findTimeTask.getTime_task_type(), findTimeTask.getTask_start_time()));
		}
		return timeTaskDao.updateByPrimaryKey(findTimeTask);
	}

	@Override
	public int deleteTimeTaskById(Long id) {
		TimeTaskInfo findTaskInfo = timeTaskDao.getObjectById(new TimeTaskInfo(id));
		if (findTaskInfo != null) {
			findTaskInfo.setDelete_flag(1);
			return timeTaskDao.updateByPrimaryKey(findTaskInfo);
		}
		return 0;
	}

	@Override
	public Page<TimeTaskInfo> findTimeTaskByPage(QueryBean query) {
		Page<TimeTaskInfo> page = new Page<TimeTaskInfo>(query.getPage_index(), query.getPage_size());
		page.setTotals(timeTaskDao.getTimeTaskInfoListByCount(query));
		if (page.getTotals() == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(timeTaskDao.getTimeTaskInfoListByPage(query));
        return page;
	}

	@Override
	public List<TimeTaskInfo> findTimeTaskAll() {
		return timeTaskDao.getTimeTaskInfoAll();
	}

	@Override
	public TimeTaskInfo findTimeTaskInfoById(Long id) {
		return timeTaskDao.getObjectById(new TimeTaskInfo(id));
	}

	@Override
	public int settingTaskEnable(Long id, Integer enable) {
		if (enable != 1 && enable != 2) return -3;
		TimeTaskInfo findTaskInfo = timeTaskDao.getObjectById(new TimeTaskInfo(id));
		if (findTaskInfo == null) return 0;
		if (enable == 2) {
			findTaskInfo.setTask_enable(2);
			return timeTaskDao.updateByPrimaryKey(findTaskInfo);
		}
		findTaskInfo.setTask_enable(1);
		Date now = new Date();
		if (now.before(findTaskInfo.getTask_start_time())) {
			findTaskInfo.setTask_next_time(findTaskInfo.getTask_start_time());
		} else {
			findTaskInfo.setTask_next_time(nextStartTaskTime(findTaskInfo.getTime_task_type(), findTaskInfo.getTask_start_time()));
		}
		
		return timeTaskDao.updateByPrimaryKey(findTaskInfo);
	}
	
	private Date nextStartTaskTime(Integer timeTaskType, Date startTime) {
		Calendar cal = Calendar.getInstance();
		Calendar createC = Calendar.getInstance();
		createC.setTime(startTime);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		switch(timeTaskType) {
		case Constants.TIME_TASK_TYPE_HOURS:
			cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) + 1);
			break;
		case Constants.TIME_TASK_TYPE_DAYS:
			cal.set(Calendar.HOUR_OF_DAY, createC.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 1);
			break;
		case Constants.TIME_TASK_TYPE_WEEKS:
			cal.set(Calendar.HOUR_OF_DAY, createC.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.DAY_OF_WEEK, createC.get(Calendar.DAY_OF_WEEK));
			cal.set(Calendar.WEEK_OF_YEAR, cal.get(Calendar.WEEK_OF_YEAR) + 1); 
			break;
		case Constants.TIME_TASK_TYPE_MONTHS:
			cal.set(Calendar.HOUR_OF_DAY, createC.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.DAY_OF_MONTH, createC.get(Calendar.DAY_OF_MONTH));
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
			break;
		case Constants.TIME_TASK_TYPE_JOP:
			cal.set(Calendar.HOUR_OF_DAY, createC.get(Calendar.HOUR_OF_DAY));
			if (cal.get(Calendar.DAY_OF_WEEK) == 6) {
				cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 3);
			} else {
				cal.set(Calendar.DAY_OF_YEAR, cal.get(Calendar.DAY_OF_YEAR) + 1);
			}
			break;
		}
		return cal.getTime();
	}

}
