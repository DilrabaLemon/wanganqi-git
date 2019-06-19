package com.boye.service.impl;

import java.util.ArrayList;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.NoticeInfo;
import com.boye.dao.NoticeDao;
import org.springframework.stereotype.Service;

import com.boye.service.INoticeService;

import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class NoticeServiceImpl extends BaseServiceImpl implements INoticeService {

	@Resource
	private NoticeDao noticeDao;

	@Override
	@Transactional
	public int addNotice(AdminInfo admin, NoticeInfo notice) {
    	notice.setDelete_flag(0);
    	notice.setAdmin_id(admin.getId());
    	notice.setAdmin_name(admin.getAdmin_name());
    	return noticeDao.insert(notice);
	}
	
	@Override
	@Transactional
	public int editNotice(AdminInfo admin, NoticeInfo notice) {
    	NoticeInfo findNotice = noticeDao.getObjectById(notice);
    	findNotice.setNotice_title(notice.getNotice_title());
    	findNotice.setNotice_content(notice.getNotice_content());
    	findNotice.setPush_party(notice.getPush_party());
    	return noticeDao.updateByPrimaryKey(findNotice);
	}

	@Override
	public int deleteNotice(AdminInfo admin, String notice_id) {
		NoticeInfo notice = new NoticeInfo();
    	notice.setId(Long.parseLong(notice_id));
    	notice = noticeDao.getObjectById(notice);
    	if (notice == null) return -1;
    	notice.setDelete_flag(1);
    	int result  = noticeDao.updateByPrimaryKey(notice);
        return result;
	}

	@Override
	public Page<NoticeInfo> noticeList(QueryBean query,AdminInfo admin) {
		Page<NoticeInfo> page = new Page<>(query.getPage_index(), query.getPage_size());
        int count = noticeDao.getNoticeInfoListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(noticeDao.getNoticeInfoListByPage(query));
        return page;
	}

	@Override
	public NoticeInfo noticeInfo(String notice_id) {
		NoticeInfo notice = new NoticeInfo();
		notice.setId(Long.parseLong(notice_id));
		return noticeDao.getObjectById(notice);

	}
}
