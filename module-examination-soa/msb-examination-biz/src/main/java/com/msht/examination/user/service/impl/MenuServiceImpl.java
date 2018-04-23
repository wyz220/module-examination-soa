/**
 * 
 */
package com.msht.examination.user.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.msht.examination.user.dao.MenuDao;
import com.msht.examination.user.entity.MenuPo;
import com.msht.examination.user.entity.MenuPo.MenuType;
import com.msht.examination.user.service.MenuService;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.entity.Page;

/**
 * @author lindaofen
 *
 */
@Component("menuService")
@Service(interfaceName="com.msht.examination.user.service.MenuService",version = "1.0.0")
@Transactional
public class MenuServiceImpl implements MenuService {

	@Autowired
	private MenuDao menuDao;
	
	@Override
	public MenuPo get(MenuPo po) throws BusinessException {
		return menuDao.get(po);
	}

	@Override
	public void insert(MenuPo po) throws BusinessException {
		menuDao.insert(po);

	}

	@Override
	public void insertBatch(List<MenuPo> list) throws BusinessException {
		menuDao.insertBatch(list);
	}

	@Override
	public void delete(Object id) throws BusinessException {
		menuDao.delete(id);
	}

	@Override
	public void update(MenuPo po) throws BusinessException {
		menuDao.update(po);
	}

	@Override
	public List<MenuPo> list(MenuPo po) throws BusinessException {
		return menuDao.list(po);
	}

	@Override
	public Page<MenuPo> page(MenuPo po) throws BusinessException {
		int count = menuDao.count(po);
		po.getPage().setCount(count);
		List<MenuPo> list =  menuDao.page(po);
		po.getPage().setList(list);
		return po.getPage();
	}
	
	@Override
	public List<MenuPo> gets(Long[] menuIds) throws BusinessException {
		return menuDao.gets(menuIds);
	}

	@Override
	public Set<String> findPermissions(Set<Long> menuIds) throws BusinessException {
        Set<String> permissions = new HashSet<String>();
        List<MenuPo> menuList = this.gets(menuIds.toArray(new Long[]{}));
        for(MenuPo menu : menuList) {
        	permissions.add(menu.getCode());
        }
        return permissions;
	}
	

	@Override
	public List<MenuPo> findMenus(Set<String> permissions) throws BusinessException {
		MenuPo param = new MenuPo();
        List<MenuPo> allMenu = this.list(param);
        List<MenuPo> menuList = new ArrayList<MenuPo>();
        for(MenuPo menu : allMenu) {
            if(MenuType.getType(menu.getType()) != MenuType.MENU) {
                continue;
            }
            if(!hasPermission(permissions, menu)) {
                continue;
            }
            menuList.add(menu);
        }
        
        List<MenuPo> parentList = new ArrayList<MenuPo>();
        for (int i = 0; i < menuList.size(); i++){
        	MenuPo parent = menuList.get(i);
        	for (int j = 0; j < menuList.size(); j++){
        		MenuPo child = menuList.get(j);
        		if (child.getParentId().intValue() == parent.getId().intValue()){
        			if (parent.getChilds() == null){
        				parent.setChilds(new ArrayList<MenuPo>());
        				parent.setHasChild(true);
        			}
        			
        			parent.getChilds().add(child);
        		}
        	}
        	
        	if (parent.getParentId().intValue() == 0){
        		parentList.add(parent);
        	}
        }
        
        
        return parentList;
   
	}

	/*
	 * 
	 * @param permissions
	 * @param menu
	 * @return
	 */
    private boolean hasPermission(Set<String> permissions, MenuPo menu) {
        if(StringUtils.isEmpty(menu.getCode())) {
            return true;
        }
        for(String permission : permissions) {
/*            WildcardPermission p1 = new WildcardPermission(permission);
            WildcardPermission p2 = new WildcardPermission(menu.getCode());
            if(p1.implies(p2) || p2.implies(p1)) {
                return true;
            }*/
        	if (permission.equals(menu.getCode())){
        		return true;
        	}
        }
        return false;
    }

	@Override
	public List<MenuPo> findMenuByRoleId(Long roleId) throws BusinessException {
		return menuDao.findMenuByRoleId(roleId);
	}

	@Override
	public List<MenuPo> findMenusAndButtons(Set<String> permissions) throws BusinessException {
		MenuPo param = new MenuPo();
        List<MenuPo> allMenu = this.list(param);
        List<MenuPo> menuList = new ArrayList<MenuPo>();
        for(MenuPo menu : allMenu) {
/*            if(MenuType.getType(menu.getType()) != MenuType.MENU) {
                continue;
            }*/
            if(!hasPermission(permissions, menu)) {
                continue;
            }
            menuList.add(menu);
        }
        
        List<MenuPo> parentList = new ArrayList<MenuPo>();
        for (int i = 0; i < menuList.size(); i++){
        	MenuPo parent = menuList.get(i);
        	if (parent.getParentId().intValue() == 0){
            	for (int j = 0; j < menuList.size(); j++){
            		MenuPo childMenu = menuList.get(j);
            		if(MenuType.getType(childMenu.getType()) == MenuType.MENU) {
                		if (childMenu.getParentId().intValue() == parent.getId().intValue()){
                			if (parent.getChilds() == null){
                				parent.setChilds(new ArrayList<MenuPo>());
                				parent.setHasChild(true);
                			}
                			
                			parent.getChilds().add(childMenu);
                			
                			for (int k = 0; k < menuList.size(); k++){
                				MenuPo button = menuList.get(k);
                				if (button.getParentId().intValue() == childMenu.getId().intValue()){
                        			if (childMenu.getChilds() == null){
                        				childMenu.setChilds(new ArrayList<MenuPo>());
                        				childMenu.setHasChild(true);
                        			}
                        			
                        			childMenu.getChilds().add(button);
                				}
                			}
                		}
            		}

            	}
            	
            	parentList.add(parent);
        	}
        }
        
        
        return parentList;
   
	}


}
