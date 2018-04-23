/**
 * 
 */
package com.msht.examination.user.service;

import java.util.List;
import java.util.Set;

import com.msht.examination.user.entity.RolePo;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.entity.Page;

/**
 * @author lindaofen
 *
 */
public interface RoleService {

	public RolePo get(RolePo po) throws BusinessException;
	
	public void insert(RolePo po) throws BusinessException;
	
	public void insertBatch(List<RolePo> list) throws BusinessException;
	
	public void delete(Object id) throws BusinessException;
	
	public void update(RolePo po) throws BusinessException;
	
	public List<RolePo> list(RolePo po) throws BusinessException;
	
	public Page<RolePo> page(RolePo po) throws BusinessException;
	
	public List<RolePo> gets(Long[] roleIds) throws BusinessException;
	
	public Set<String> findRoles(Long[] roleIds) throws BusinessException;
	
	public Set<String> findPermissions(Long[] roleIds) throws BusinessException;
	
	public List<RolePo> findRoleByUserId(Long[] userIds) throws BusinessException;
}
