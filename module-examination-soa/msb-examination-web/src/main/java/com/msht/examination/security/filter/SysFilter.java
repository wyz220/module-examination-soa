/**
 * 
 */
package com.msht.examination.security.filter;

import java.io.IOException;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.msht.examination.user.entity.MenuPo;
import com.msht.examination.security.Constants;

/**
 * @author lindaofen
 *
 */
public class SysFilter extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		System.out.println(request.getRequestURI());
        Map<String, MenuPo> urlMapMenus = (Map<String, MenuPo>) SecurityUtils.getSubject().getSession().getAttribute(Constants.URL_MAP_MENUS);
        Map<String, MenuPo> idMapMenus = (Map<String, MenuPo>) SecurityUtils.getSubject().getSession().getAttribute(Constants.ID_MAP_MENUS);
        
		filterChain.doFilter(request, response);
	}

}
