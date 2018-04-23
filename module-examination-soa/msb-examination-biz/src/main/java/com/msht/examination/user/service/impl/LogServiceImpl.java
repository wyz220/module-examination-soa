/**
 * 
 */
package com.msht.examination.user.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.msht.examination.user.dao.LogDao;
import com.msht.examination.user.entity.LogPo;
import com.msht.examination.user.service.LogService;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.entity.Page;

/**
 * @author lindaofen
 *
 */
@Component("logService")
@Service(interfaceName="com.msht.examination.user.service.LogService",version = "1.0.0")
@Transactional
public class LogServiceImpl implements LogService {
	
	@Autowired
	private LogDao logDao;

	@Override
	public LogPo get(LogPo po) throws BusinessException {
		return logDao.get(po);
	}
	
	@Override
	public void insert(LogPo po) throws BusinessException {
		logDao.insert(po);
	}

	@Override
	public void insertBatch(List<LogPo> list) throws BusinessException {
		logDao.insertBatch(list);
	}

	@Override
	public void delete(Object id) throws BusinessException {
		logDao.delete(id);
	}

	@Override
	public void update(LogPo po) throws BusinessException {
		logDao.update(po);
	}

	@Override
	public List<LogPo> list(LogPo po) throws BusinessException {
		return logDao.list(po);
	}

	@Override
	public Page<LogPo> page(LogPo po) throws BusinessException {
		int count = logDao.count(po);
		po.getPage().setCount(count);
		List<LogPo> list =  logDao.page(po);
		po.getPage().setList(list);
		return po.getPage();
	}

}
