package com.boye.service;

import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;
import com.boye.bean.entity.AdminInfo;
import com.boye.bean.entity.SubPaymentKeyBox;
import com.boye.bean.entity.SubstituteAccount;

public interface ISubstituteAccountService {

	int addSubstitute(AdminInfo admin, SubstituteAccount substitute);
	
	int editSubstitute(AdminInfo admin, SubstituteAccount substitute);

	int deleteSubstitute(AdminInfo admin, String substitute_id);

	Page<SubstituteAccount> substituteList(QueryBean query);

	int substituteAvailable(String substitute_id, Integer state);
	
	SubstituteAccount substituteInfo(String substitute_id);

	int addKeyBox(SubPaymentKeyBox keyBox);

	SubPaymentKeyBox getKeyBox(Long id);

}
