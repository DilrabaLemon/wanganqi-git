package com.boye.service.shop;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.boye.bean.entity.ShopBankcardInfo;
import com.boye.bean.entity.ShopUserInfo;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface IShopBankCardService {

	int add(ShopBankcardInfo shopBankCardInfo);

	int edit(ShopBankcardInfo shopBankCardInfo, ShopUserInfo shopUser);

	int delete(Long shopBCID, ShopUserInfo shopUser);

	Page<ShopBankcardInfo> queryPage(QueryBean query, ShopUserInfo shopUser);

	List<ShopBankcardInfo> findAll(ShopUserInfo shopUser);

	ShopBankcardInfo findById(Long shopBCID, ShopUserInfo shopUser);

	int importExcelInfo(MultipartFile file, ShopUserInfo shopUser);

}
