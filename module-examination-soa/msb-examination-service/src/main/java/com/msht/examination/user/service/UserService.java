/**
 * 
 */
package com.msht.examination.user.service;

import java.util.List;
import java.util.Set;

import com.msht.examination.user.entity.UserPo;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.entity.Page;

/**
 * @author lindaofen
 *
 */
public interface UserService {

	public UserPo get(UserPo po) throws BusinessException;
	
	public void insert(UserPo po) throws BusinessException;
	
	public void insertBatch(List<UserPo> list) throws BusinessException;
	
	public void delete(Object id) throws BusinessException;
	
	public void update(UserPo po) throws BusinessException;
	
	public List<UserPo> list(UserPo po) throws BusinessException;
	
	public Page<UserPo> page(UserPo po) throws BusinessException;
	
	public Set<String> findRoles(String username) throws BusinessException;
	
	public Set<String> findPermissions(String username) throws BusinessException;
	
	public void batchDelete(Long[] userIds) throws BusinessException;
	
	public Long findUserRoleId(Long userId) throws BusinessException;
	
	public void modifyMemberProfile(UserPo po) throws BusinessException;
	
	public void delete(UserPo po) throws BusinessException;
}
