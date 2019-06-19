package com.boye.service.impl;

import org.springframework.stereotype.Service;
import com.boye.dao.IndexDao;
import com.boye.service.IIndexService;
import javax.annotation.Resource;

@Service
public class IndexServiceImpl extends BaseServiceImpl implements IIndexService {

	@Resource
	private IndexDao indexDao;

}
