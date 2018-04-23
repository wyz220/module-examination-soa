package com.msht.examination.security.filter;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.msht.examination.security.Constants;
import com.msht.examination.user.entity.MenuPo;
import com.msht.examination.user.entity.UserPo;
import com.msht.examination.user.service.MenuService;
import com.msht.examination.user.service.UserService;
import com.msht.framework.common.utils.StringUtils;


public class SysUserFilter extends PathMatchingFilter {

    @Autowired
    private UserService userService;
    
    @Autowired
    private MenuService menuService;

    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {

        String username = (String)SecurityUtils.getSubject().getPrincipal();
        if (StringUtils.isNotBlank(username)){
            UserPo param = new UserPo();
            param.setUsername(username);
            request.setAttribute(Constants.CURRENT_USER, userService.get(param));
            
            Object obj = SecurityUtils.getSubject().getSession().getAttribute(Constants.CURRENT_MENUS);
            if (obj == null){
                Set<String> permissions = userService.findPermissions(username);
                List<MenuPo> menus = menuService.findMenus(permissions);
                SecurityUtils.getSubject().getSession().setAttribute(Constants.CURRENT_MENUS, menus);
            }
            
            Object urlMapMenus = SecurityUtils.getSubject().getSession().getAttribute(Constants.URL_MAP_MENUS);
            Object idMapMenus = SecurityUtils.getSubject().getSession().getAttribute(Constants.ID_MAP_MENUS);
            if (urlMapMenus == null || idMapMenus == null){
            	MenuPo menuParam = new MenuPo();
            	List<MenuPo> allMenuList = menuService.list(menuParam);
            	Map<String, MenuPo> urlMap = new HashMap<String, MenuPo>();
            	Map<String, MenuPo> idMap = new HashMap<String, MenuPo>();
            	for (MenuPo menu : allMenuList){
            		if (StringUtils.isNotBlank(menu.getUrl())){
                  		urlMap.put(menu.getUrl(), menu);
            		}
  
            		idMap.put(String.valueOf(menu.getId()), menu);
            	}
            	SecurityUtils.getSubject().getSession().setAttribute(Constants.URL_MAP_MENUS, urlMap);
            	SecurityUtils.getSubject().getSession().setAttribute(Constants.ID_MAP_MENUS, idMap);
            }

        }
        return true;
    }
}
