/**
 * 
 */
package com.msht.examination.user.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.msht.examination.user.dao.RoleDao;
import com.msht.examination.user.dao.UserDao;
import com.msht.examination.user.entity.RoleMenuPo;
import com.msht.examination.user.entity.RolePo;
import com.msht.examination.user.service.MenuService;
import com.msht.examination.user.service.RoleService;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.common.utils.Delimiters;
import com.msht.framework.common.utils.StringUtils;
import com.msht.framework.entity.Page;

/**
 * @author lindaofen
 *
 */
@Component("roleService")
@Service(interfaceName="com.msht.examination.user.service.RoleService",version = "1.0.0")
@Transactional
public class RoleServiceImpl implements RoleService {

	@Autowired
	private RoleDao roleDao;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private UserDao userDao;
	
	@Override
	public RolePo get(RolePo po) throws BusinessException {
		return roleDao.get(po);
	}

	@Override
	public void insert(RolePo po) throws BusinessException {
		 roleDao.insert(po);
		 if (StringUtils.isNotBlank(po.getMenuIds())){
			 String[] menuIds = po.getMenuIds().split(Delimiters.COMMA);
			 for (int i = 0; i < menuIds.length; i++){
				 RoleMenuPo roleMenuPo = new RoleMenuPo();
				 roleMenuPo.setRoleId(po.getId());
				 roleMenuPo.setMenuId(Long.valueOf(menuIds[i]));
				 roleDao.insertRoleMenu(roleMenuPo);
			 }
		 }
		 
		
	}
	
	@Override
	public void insertBatch(List<RolePo> list) throws BusinessException {
		roleDao.insertBatch(list);
	}

	@Override
	public void delete(Object id) throws BusinessException {
		roleDao.delete(id);
		roleDao.deleteRoleMenuByRoleId(id);
		userDao.deleteUserRoleByRoleId(id);
	}

	@Override
	public void update(RolePo po) throws BusinessException {
		roleDao.update(po);
		roleDao.deleteRoleMenuByRoleId(po.getId());
		 if (StringUtils.isNotBlank(po.getMenuIds())){
			 String[] menuIds = po.getMenuIds().split(Delimiters.COMMA);
			 for (int i = 0; i < menuIds.length; i++){
				 RoleMenuPo roleMenuPo = new RoleMenuPo();
				 roleMenuPo.setRoleId(po.getId());
				 roleMenuPo.setMenuId(Long.valueOf(menuIds[i]));
				 roleDao.insertRoleMenu(roleMenuPo);
			 }
		 }
	}

	@Override
	public List<RolePo> list(RolePo po) throws BusinessException {
		return roleDao.list(po);
	}

	@Override
	public Page<RolePo> page(RolePo po) throws BusinessException {
		int count = roleDao.count(po);
		po.getPage().setCount(count);
		List<RolePo> list =  roleDao.page(po);
		po.getPage().setList(list);
		return po.getPage();
	}
	
	@Override
	public List<RolePo> gets(Long[] roleIds) throws BusinessException {
		return roleDao.gets(roleIds);
	}

	@Override
	public Set<String> findRoles(Long[] roleIds) throws BusinessException {
        Set<String> roles = new HashSet<String>();
        List<RolePo> roleList = this.gets(roleIds);
        for (RolePo role : roleList){
        	roles.add(role.getName());
        }
        return roles;
	}

	@Override
	public Set<String> findPermissions(Long[] roleIds) throws BusinessException {
		Set<Long> setMenuIds = new HashSet<Long>();
		List<Long> menuIds = roleDao.getRoleMenuIds(roleIds);
		for (Long menuId : menuIds){
			setMenuIds.add(menuId);
		}
        return menuService.findPermissions(setMenuIds);
	}

	@Override
	public List<RolePo> findRoleByUserId(Long[] userIds) throws BusinessException {
		return roleDao.findRoleByUserId(userIds);
	}



}
