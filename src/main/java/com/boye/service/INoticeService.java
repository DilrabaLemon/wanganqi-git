package com.boye.service;

import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.NoticeInfo;

public interface INoticeService {

	int addNotice(AdminInfo admin, NoticeInfo notice);
	
	int editNotice(AdminInfo admin, NoticeInfo notice);

	int deleteNotice(AdminInfo admin, String notice_id);

	Page<NoticeInfo> noticeList(QueryBean query,AdminInfo admin);

	NoticeInfo noticeInfo(String notice_id);

}
