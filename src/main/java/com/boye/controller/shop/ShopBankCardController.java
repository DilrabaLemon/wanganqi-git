package com.boye.controller.shop;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.model.Sheet;
import org.apache.poi.hssf.record.formula.functions.Row;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.boye.bean.entity.ShopBankcardInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.ParamUtils;
import com.boye.common.ResultMapInfo;
import com.boye.controller.BaseController;
import com.boye.service.shop.IShopBankCardService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping(value = "/shop/bankcard")
public class ShopBankCardController extends BaseController {
	
	@Autowired
	private IShopBankCardService shopBCService;
	
	@ResponseBody
	@RequestMapping(value = "/add", method = RequestMethod.POST)
	@ApiOperation(value="add", notes="添加银行卡")
	@ApiImplicitParam(name = "shopBankCardInfo" , value = "银行卡信息")
	public Map<String, Object> add(@RequestBody ShopBankcardInfo shopBankCardInfo) {
		if (ParamUtils.paramCheckNull(shopBankCardInfo))
            return returnResultMap(ResultMapInfo.NOTPARAM);
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
        shopBankCardInfo.setShop_id(shopUser.getId());
		int result = shopBCService.add(shopBankCardInfo);
		if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS); //操作成功
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);//操作失败
        }
	}
	
	@ResponseBody
	@RequestMapping(value = "/edit", method = RequestMethod.POST)
	@ApiOperation(value="edit", notes="编辑银行卡")
	@ApiImplicitParam(name = "shopBankCardInfo" , value = "银行卡信息")
	public Map<String, Object> edit(@RequestBody ShopBankcardInfo shopBankCardInfo) {
		if (ParamUtils.paramCheckNull(shopBankCardInfo))
            return returnResultMap(ResultMapInfo.NOTPARAM);
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
		int result = shopBCService.edit(shopBankCardInfo, shopUser); 
		if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS); //操作成功
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);//操作失败
        }
	}
	
	@ResponseBody
	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	@ApiOperation(value="delete", notes="删除银行卡")
	@ApiImplicitParam(name = "id" , value = "银行卡信息ID")
	public Map<String, Object> delete(Long id) {
		if (ParamUtils.paramCheckNull(id))
            return returnResultMap(ResultMapInfo.NOTPARAM);
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
		int result = shopBCService.delete(id, shopUser); 
		if (result == 1) {
            return returnResultMap(ResultMapInfo.DELETESUCCESS); //删除成功
        } else {
            return returnResultMap(ResultMapInfo.DELETEFAIL);//删除失败
        }
	}
	
	@ResponseBody
	@RequestMapping(value = "/queryPage", method = RequestMethod.GET)
	@ApiOperation(value="queryPage", notes="查询银行卡信息分页")
	@ApiImplicitParam(name = "query" , value = "分页信息")
	public Map<String, Object> queryPage(QueryBean query) {
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
		Page<ShopBankcardInfo> result = shopBCService.queryPage(query, shopUser); 
		if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); //获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//获取失败
        }
	}
	
	@ResponseBody
	@RequestMapping(value = "/findAll", method = RequestMethod.GET)
	@ApiOperation(value="findAll", notes="查询下拉选项")
	public Map<String, Object> findAll() {
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
		List<ShopBankcardInfo> result = shopBCService.findAll(shopUser); 
		if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); //获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//获取失败
        }
	}
	
	@ResponseBody
	@RequestMapping(value = "/findById", method = RequestMethod.GET)
	@ApiOperation(value="findById", notes="查询下拉选项")
	@ApiImplicitParam(name = "id" , value = "记录ID")
	public Map<String, Object> findById(Long id) {
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
		ShopBankcardInfo result = shopBCService.findById(id, shopUser); 
		if (result != null) {
            return returnResultMap(ResultMapInfo.GETSUCCESS, result); //获取成功
        } else {
            return returnResultMap(ResultMapInfo.GETFAIL);//获取失败
        }
	}
	
	@ResponseBody
	@RequestMapping(value = "/importExcelInfo", method = RequestMethod.POST)
	@ApiOperation(value="importExcelInfo", notes="上传Excel")
	@ApiImplicitParam(name = "file" , value = "文件信息")
	public Map<String, Object> importExcelInfo(@RequestParam("file") MultipartFile file) {
		ShopUserInfo shopUser = (ShopUserInfo) getSession().getAttribute("login_shopUser");
        if (shopUser == null) {
            return returnResultMap(ResultMapInfo.RELOGIN);
        }
		int result = 0;
        if (!file.isEmpty()) {    
        	result = shopBCService.importExcelInfo(file, shopUser);
        }
		if (result == 1) {
            return returnResultMap(ResultMapInfo.ADDSUCCESS); //操作成功
		} else if (result == -3) {
            return returnResultMap(ResultMapInfo.NULLFILEERROR);//操作失败
		} else if (result == -7) {
            return returnResultMap(ResultMapInfo.FILETYPEERROR);//操作失败
        } else {
            return returnResultMap(ResultMapInfo.ADDFAIL);//操作失败
        }
	}

}
