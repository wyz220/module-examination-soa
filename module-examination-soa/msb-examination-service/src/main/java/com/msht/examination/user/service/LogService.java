/**
 * 
 */
package com.msht.examination.user.service;

import java.util.List;

import com.msht.examination.user.entity.LogPo;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.entity.Page;

/**
 * @author lindaofen
 *
 */
public interface LogService {

	public LogPo get(LogPo po) throws BusinessException;
	
	public void insert(LogPo po) throws BusinessException;
	
	public void insertBatch(List<LogPo> list) throws BusinessException;
	
	public void delete(Object id) throws BusinessException;
	
	public void update(LogPo po) throws BusinessException;
	
	public List<LogPo> list(LogPo po) throws BusinessException;
	
	public Page<LogPo> page(LogPo po) throws BusinessException;
}
