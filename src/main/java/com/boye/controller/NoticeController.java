package com.boye.controller;

import java.util.Map;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.AdminOperationRecord;
import com.boye.bean.entity.NoticeInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.common.ServiceReturnMessage;
import com.boye.service.INoticeService;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/notice")
public class NoticeController extends BaseController {
	
	@Autowired
	private INoticeService noticeService;
	
	/**
     *  添加公告
     *  操作方：管理员
     * @param notice
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/addAndEditNotice", method = RequestMethod.POST)
	@ApiOperation(value="addAndEditNotice", notes="添加公告")
	@ApiImplicitParam(name = "notice",value = "公告信息")
    public Map<String, Object> addAndEditNotice(@RequestBody NoticeInfo notice){
    	if (ParamUtils.paramCheckNull(notice))
    		return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
		}
    	int result = 0;
    	if (notice.getId() == null) {
    		result = noticeService.addNotice(admin, notice);// 添加公告
    	}else {
    		result = noticeService.editNotice(admin, notice);//修改公告
    	}
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_ADDANDEDITNOTICE);//添加操作记录
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.ADDSUCCESS, adminOperation);//操作成功
    	} else {
    		return returnResultMap(ResultMapInfo.ADDFAIL, adminOperation);//操作失败
    	}
    }
	
    /**
     * 删除公告
     * 操作方：管理员
     * @param notice_id
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/deleteNotice", method = RequestMethod.GET)
	@ApiOperation(value="deleteNotice", notes="删除公告")
	@ApiImplicitParam(name = "notice_id" ,value = "公告信息id")
    public Map<String, Object> deleteNotice(String notice_id){
    	if (ParamUtils.paramCheckNull(notice_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
		}
    	int result = noticeService.deleteNotice(admin, notice_id);// 删除返回结果
    	AdminOperationRecord adminOperation = AdminOperationRecord.getAdminOperation(admin, ServiceReturnMessage.ADMIN_DELETENOTICE);//操作记录
    	if (result == 1) {
    		return returnResultMap(ResultMapInfo.DELETESUCCESS, adminOperation);//删除成功
    	} else {
    		return returnResultMap(ResultMapInfo.DELETEFAIL, adminOperation);//删除失败
    	}
    }
	
    /**
     * 获取公告列表
     * 操作方：管理员
     * @param page_size
     * @param page_index
     * @returnS
     */
    @ResponseBody
	@RequestMapping(value = "/noticeList", method = RequestMethod.GET)
	@ApiOperation(value="noticeList", notes="获取公告列表")
	@ApiImplicitParam(name="query",value = "查询条件")
    public Map<String, Object> noticeList(QueryBean query){
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
		}

    	Page<NoticeInfo> result = noticeService.noticeList(query,admin);//分页
    	
    	if (result != null) {
    		return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
    	}
    }

    /**
     * 获取公告详情
     * 操作方：管理员
     * @param notice_id
     * @return
     */
    @ResponseBody
	@RequestMapping(value = "/noticeInfo", method = RequestMethod.GET)
	@ApiOperation(value="noticeInfo", notes="获取公告详情")
	@ApiImplicitParam(name = "notice_id" , value = "公告信息id")
    public Map<String, Object> noticeInfo(String notice_id){
    	if (ParamUtils.paramCheckNull(notice_id))
    		return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
		AdminInfo admin =  (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
		}
		NoticeInfo result = noticeService.noticeInfo(notice_id);//公告对象id
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
    	} else {
    		return returnResultMap(ResultMapInfo.GETFAIL);//操作失败
    	}
    }
}