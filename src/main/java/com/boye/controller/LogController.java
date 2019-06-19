package com.boye.controller;

import java.util.Map;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.service.ILogService;

import io.swagger.annotations.ApiOperation;

import javax.naming.Name;

@Controller
@RequestMapping(value = "/log")
public class LogController extends BaseController {

	@Autowired
	private ILogService logService;

	/**
	 * 获取日志记录
	 * 操作方：管理员
	 *
	 * @param record_type 日志类型（1.管理员操作日志， 2.商户登录日志， 3.商户操作日志 ，4.代理商登入日志，5.代理商操作日志，管理员登入日志）
	 * @param page_index
	 * @param page_size
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/getAdminOperationRecord", method = RequestMethod.GET)
	@ApiOperation(value = "getAdminOperationRecord", notes = "获取日志记录")
	@ApiImplicitParams({@ApiImplicitParam(name = "query", value = "查询条件"), @ApiImplicitParam(name = "record_type", value = "记录类型")})
	public Map<String, Object> getAdminOperationRecord(QueryBean query, Integer record_type) {
		if (ParamUtils.paramCheckNull(record_type))
			return returnResultMap(ResultMapInfo.NOTPARAM);//参数判断
		AdminInfo admin = (AdminInfo) getSession().getAttribute("login_admin");
		if (admin == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);//重新登录
		}
		Page<?> result = null;
		if (record_type != null) {
			if (record_type == 1) {
				result = logService.getAdminOperationRecordList(query);//获取管理员操作记录列表
			} else if (record_type == 2) {
				result = logService.getShopLoginRecordList(query);//获取商户登录记录列表
			} else if (record_type == 3) {
				result = logService.getShopOperationRecordList(query);//获得商户运营记录清单
			} else if (record_type == 4) {
				result = logService.getAgentLoginRecordList(query);//获取代理商登录记录列表
			} else if (record_type == 5) {
				result = logService.getAgentOperationRecordList(query);//获得代理商运营记录清单
			}else if (record_type == 6) {
				result = logService.getAdminLoginRecordList(query);//获取管理员登录记录列表
			}
		}
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
		}
	}

	/**
	 * 获取日志记录(商户)
	 * 操作方：商户
	 *
	 * @param record_type 日志类型（ 2.商户登录日志， 3.商户操作日志 ）
	 * @param page_index
	 * @param page_size
	 * @return
	 */
	/*@ResponseBody
	@RequestMapping(value = "/getUserOperationRecord", method = RequestMethod.GET)
	@ApiOperation(value = "getAdminOperationRecord", notes = "获取日志记录")
	@ApiImplicitParams({@ApiImplicitParam(name = "query", value = "查询条件"), @ApiImplicitParam(name = "记录类型")})
	public Map<String, Object> getUserOperationRecord(QueryBean query, Integer record_type) {
		// 参数判断
		if (ParamUtils.paramCheckNull(record_type))
			return returnResultMap(ResultMapInfo.NOTPARAM); 
		// 判断是否登入
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
				if (shopUser == null) {
			return returnResultMap(ResultMapInfo.RELOGIN);
		}
		Page<?> result = null;
		if (record_type != null) {
			// 根据商户,查询条件查询通过商店获得商店登录记录列表
			if (record_type == 2) {
				result = logService.getShopLoginRecordListByShop(shopUser, query); 
			//通过商店获取商店运营记录列表
			} else if (record_type == 3) {
				result = logService.getShopOperationRecordListByShop(shopUser, query);
			}
		}
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
		}
	}*/
}
