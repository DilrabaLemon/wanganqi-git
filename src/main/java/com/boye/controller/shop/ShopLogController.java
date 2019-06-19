package com.boye.controller.shop;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boye.api.juheSMSApi;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.ILogService;
import com.boye.service.shop.ShopLogService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/log")
public class ShopLogController extends BaseController{
	
	@Autowired
	private ShopLogService shopLogService;
	/**
	 * 获取日志记录(商户)
	 * 操作方：商户
	 *
	 * @param record_type 日志类型（ 2.商户登录日志， 3.商户操作日志 ）
	 * @param page_index
	 * @param page_size
	 * @return
	 */
	@ResponseBody
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
				result = shopLogService.getShopLoginRecordListByShop(shopUser, query); 
			//通过商店获取商店运营记录列表
			} else if (record_type == 3) {
				result = shopLogService.getShopOperationRecordListByShop(shopUser, query);
			}
		}
		if (result != null) {
			return returnResultMap(ResultMapInfo.GETSUCCESS, result);//操作成功
		} else {
			return returnResultMap(ResultMapInfo.GETFAIL, Page.nullPage());//操作失败
		}
	}
}
