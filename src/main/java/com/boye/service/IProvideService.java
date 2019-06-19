package com.boye.service;

import com.boye.bean.entity.ProvideInfo;

public interface IProvideService {

	ProvideInfo getProvideByPassagewayCode(String passageway_code, String string, String string2);
	
	ProvideInfo getProvideByPassagewayCode(String passageway_code);

	ProvideInfo getProvideByProvideCode(String string);

	ProvideInfo getProvideByPassagewayId(Long passageway_id);

}
