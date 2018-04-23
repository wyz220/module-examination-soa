/**
 * 
 */
package com.msht.examination.user.service;

import java.util.List;
import java.util.Set;

import com.msht.examination.user.entity.MenuPo;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.entity.Page;

/**
 * @author lindaofen
 *
 */
public interface MenuService {

	public MenuPo get(MenuPo po) throws BusinessException;
	
	public void insert(MenuPo po) throws BusinessException;
	
	public void insertBatch(List<MenuPo> list) throws BusinessException;
	
	public void delete(Object id) throws BusinessException;
	
	public void update(MenuPo po) throws BusinessException;
	
	public List<MenuPo> list(MenuPo po) throws BusinessException;
	
	public Page<MenuPo> page(MenuPo po) throws BusinessException;
	
	public List<MenuPo> gets(Long[] menuIds) throws BusinessException;
	
	public Set<String> findPermissions(Set<Long> menuIds) throws BusinessException;
	
	public List<MenuPo> findMenus(Set<String> permissions) throws BusinessException;
	
	public List<MenuPo> findMenuByRoleId(Long roleId) throws BusinessException;
	
	public List<MenuPo> findMenusAndButtons(Set<String> permissions) throws BusinessException;
}
