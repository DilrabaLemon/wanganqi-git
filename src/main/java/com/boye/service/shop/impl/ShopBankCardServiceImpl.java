package com.boye.service.shop.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.boye.bean.entity.ShopBankcardInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.common.utils.CommonUtils;
import com.boye.dao.ShopBankcardInfoDao;
import com.boye.service.shop.IShopBankCardService;

@Service
public class ShopBankCardServiceImpl implements IShopBankCardService {
	
	@Autowired
	private ShopBankcardInfoDao shopBCIDao;

	@Override
	public int add(ShopBankcardInfo shopBankCardInfo) {
		shopBankCardInfo.setDelete_flag(0);
		try {
			return shopBCIDao.insert(shopBankCardInfo);
		} catch (Exception e) {
			e.printStackTrace();
			return -6;
		}
	}

	@Override
	public int edit(ShopBankcardInfo shopBankCardInfo, ShopUserInfo shopUser) {
		ShopBankcardInfo findSBCI = shopBCIDao.getObjectById(shopBankCardInfo);
		if (findSBCI == null || !findSBCI.getShop_id().equals(shopUser.getId())) return 0;
		findSBCI.setBank_card_number(shopBankCardInfo.getBank_card_number());
		findSBCI.setBank_name(shopBankCardInfo.getBank_name());
		findSBCI.setCity_number(shopBankCardInfo.getCity_number());
		findSBCI.setName(shopBankCardInfo.getName());
		findSBCI.setRegist_bank(shopBankCardInfo.getRegist_bank());
		findSBCI.setRegist_bank_name(shopBankCardInfo.getRegist_bank_name());
		findSBCI.setUser_mobile(shopBankCardInfo.getUser_mobile());
		findSBCI.setCard_user_name(shopBankCardInfo.getCard_user_name());
		return shopBCIDao.updateByPrimaryKey(findSBCI);
	}

	@Override
	public int delete(Long shopBCID, ShopUserInfo shopUser) {
		ShopBankcardInfo findSBCI = shopBCIDao.getObjectById(new ShopBankcardInfo(shopBCID));
		if (findSBCI == null || !findSBCI.getShop_id().equals(shopUser.getId())) return 0;
		findSBCI.setBank_card_number("DELETE_" + findSBCI.getBank_card_number() + "_" + CommonUtils.getUserNumber(6));
		findSBCI.setDelete_flag(1);
		return shopBCIDao.updateByPrimaryKey(findSBCI);
	}

	@Override
	public Page<ShopBankcardInfo> queryPage(QueryBean query, ShopUserInfo shopUser) {
		Page<ShopBankcardInfo> page = new Page<>(query.getPage_index(), query.getPage_size());
		query.setShop_id(shopUser.getId());
        int count = shopBCIDao.getShopBankcardInfoListByCount(query);
        page.setTotals(count);
        if (count == 0)
            page.setDatalist(new ArrayList<>());
        else
            page.setDatalist(shopBCIDao.getShopBankcardInfoListByPage(query));
        return page;
	}

	@Override
	public List<ShopBankcardInfo> findAll(ShopUserInfo shopUser) {
		List<ShopBankcardInfo> result = shopBCIDao.getShopBankcardInfoAllByShopId(shopUser.getId());
		return result;
	}

	@Override
	public ShopBankcardInfo findById(Long shopBCID, ShopUserInfo shopUser) {
		ShopBankcardInfo findSBCI = shopBCIDao.getObjectById(new ShopBankcardInfo(shopBCID));
		if (findSBCI == null || !findSBCI.getShop_id().equals(shopUser.getId())) return null;
		return findSBCI;
	}

	@Override
	public int importExcelInfo(MultipartFile file, ShopUserInfo shopUser) {
		try {    
			Workbook wb = null;
			String[] names = file.getOriginalFilename().split("\\.");
			if (names.length == 2 && names[1].equals("xls")) {  
	            wb = new HSSFWorkbook(new POIFSFileSystem(file.getInputStream()));  
	        }  
	        else if (names.length == 2 && names[1].equals("xlsx")) {  
	            wb = new XSSFWorkbook(file.getInputStream());  
	        }  
	        else {  
	        	return -7;
	        } 
			Sheet sheet = wb.getSheetAt(0);
			//获取第二行
			List<ShopBankcardInfo> shopBCList = getShopBankcardInfoList(sheet);
			//循环读取科目
			if (shopBCList.size() == 0) return -3;
			for(ShopBankcardInfo sbi : shopBCList) {
				sbi.setShop_id(shopUser.getId());
				try {
					shopBCIDao.insert(sbi);
				}catch(Exception e) {
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {    
		    e.printStackTrace();    
		    return 0;
		} catch (IOException e) {    
		    e.printStackTrace();    
		    return 0;
		}  
		return 1;
	}

	private List<ShopBankcardInfo> getShopBankcardInfoList(Sheet sheet) {
		
		List<ShopBankcardInfo> shopBCList = new ArrayList<ShopBankcardInfo>();
		int pointRow = 2;
		int failMax = 0;
		do{
			ShopBankcardInfo sbci = getShopBankcardInfoByExcelRow(sheet.getRow(pointRow));
			if (sbci == null) {
				failMax++;
			}else {
				shopBCList.add(sbci);
				failMax = 0;
			}
			pointRow++;
		}while(failMax < 10);
		return shopBCList;
	}

	private ShopBankcardInfo getShopBankcardInfoByExcelRow(Row row) {
		for(int i = 1; i < 8; i++) {
			if (StringUtils.isBlank(row.getCell(i).toString())) return null;
		}
		ShopBankcardInfo result = new ShopBankcardInfo();
		result.setName(row.getCell(1).getStringCellValue());
		result.setBank_card_number(row.getCell(2).getStringCellValue());
		result.setBank_name(row.getCell(3).getStringCellValue());
		result.setRegist_bank_name(row.getCell(4).getStringCellValue());
		result.setCard_user_name(row.getCell(5).toString());
		result.setUser_mobile(new BigDecimal(row.getCell(6).getNumericCellValue()).longValue() + "");
		result.setRegist_bank(row.getCell(7).getStringCellValue());
		result.setCity_number(new BigDecimal(row.getCell(8).getNumericCellValue()).longValue() + "");
		return result;
	}

}
