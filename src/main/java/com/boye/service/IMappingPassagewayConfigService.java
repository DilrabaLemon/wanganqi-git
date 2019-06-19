package com.boye.service;

import com.boye.bean.entity.PassagewayConfig;
import com.boye.bean.vo.Page;
import com.boye.bean.vo.QueryBean;

public interface IMappingPassagewayConfigService {

	int add(PassagewayConfig passagewayConfig);

	int edit(PassagewayConfig passagewayConfig);

	int delete(Long id);

	Page<PassagewayConfig> queryPage(QueryBean query);

	PassagewayConfig findById(Long id);

	int enable(Long id);

	int disuse(Long id);

}
