/**
 * 
 */
package com.msht.examination.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.msht.examination.common.RestResult;
import com.msht.examination.common.ResultInfo;
import com.msht.examination.common.ResultInfo.ResultCode;
import com.msht.examination.security.bind.annotation.CurrentUser;
import com.msht.examination.user.entity.MenuPo;
import com.msht.examination.user.entity.RolePo;
import com.msht.examination.user.entity.UserPo;
import com.msht.examination.user.service.MenuService;
import com.msht.examination.user.service.RoleService;
import com.msht.examination.user.service.UserService;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.entity.Page;

/**
 * @author lindaofen
 *
 */
@Controller  
@RequestMapping("/role") 
public class RoleController {

	private Logger logger = LoggerFactory.getLogger(RoleController.class);
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private MenuService menuService;
	
	@RequestMapping("/list")  
    public String page(@CurrentUser UserPo loginUser,Model model,@RequestParam(value="pageNo",defaultValue="1") Integer pageNo,
    		@RequestParam(value="pageSize",defaultValue="20") Integer pageSize){  
		RolePo po = new RolePo();
		Page<RolePo> page = new Page<RolePo>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		po.setPage(page);
		StringBuffer condition = new StringBuffer();
		condition.append(" and is_supper != 1");
		po.getPage().setCondition(condition.toString());
		
		logger.info(JSON.toJSONString(po));
		Page<RolePo> pageObj = roleService.page(po);
		//pageObj.setPageUrl("/role/page");
		model.addAttribute("pageObj", pageObj);
		return "role/role_list";
    }  
	
	@RequestMapping("/add")  
    public String add(@CurrentUser UserPo loginUser, Model model,@RequestParam(value="id",required=false) Long id){  
		RolePo role = null;
		if (id != null && id.longValue() > 0){
			RolePo param = new RolePo();
			param.setId(id);
			role = roleService.get(param);
		}

		List<MenuPo> allMenus = findMenus(loginUser, id);
		model.addAttribute("role", role);
		model.addAttribute("allMenus", allMenus);
		return "role/role_add";
    } 
	
	//@OperLog(module=LogConstants.PERMISSION_MODULE, methods="新增/修改权限组")
	@RequestMapping("/saveOrUpdate")  
	@ResponseBody
    public Map<String, Object> saveOrUpdate(@CurrentUser UserPo loginUser, RolePo role){  
		
		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{
			if (role.getId() == null || role.getId().longValue() <= 0){
				role.setIsSupper(0);
				role.setCreateBy(loginUser.getId());
				role.setCreateTime(new Date());
				roleService.insert(role);
			}else{
				role.setUpdateBy(loginUser.getId());
				role.setUpdateTime(new Date());
				roleService.update(role);
			}
		} catch (BusinessException e){
			logger.error(
					"Save or update role exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"Save or update role exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}
		return RestResult.restResult(result, null);
    } 
	
	//@OperLog(module=LogConstants.PERMISSION_MODULE, methods="删除权限组")
	@RequestMapping("/delete")  
	@ResponseBody
    public Map<String, Object> delete(@RequestParam(value="id") Long id){  
		
		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{
			roleService.delete(id);
		} catch (BusinessException e){
			logger.error(
					"Delete role exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"Delete role exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}
		return RestResult.restResult(result, null);
    } 
	
	private List<MenuPo> findMenus(UserPo loginUser, Long roleId){
		//查询用户菜单
        Set<String> permissions = userService.findPermissions(loginUser.getUsername());
        List<MenuPo> userMenus = menuService.findMenusAndButtons(permissions);
        Map<String, MenuPo> userMenuMap = new HashMap<String, MenuPo>();
        for (int i = 0; i < userMenus.size(); i++){
        	MenuPo parent = userMenus.get(i);
        	userMenuMap.put(parent.getCode(), parent);
        	if (parent.getChilds() != null){
        		for (MenuPo child : parent.getChilds()){
        			userMenuMap.put(child.getCode(), child);
        			if (child.getChilds() != null){
        				for (MenuPo button : child.getChilds()){
        					userMenuMap.put(button.getCode(), button);
        				}
        			}
        		}
        	}
        }
    
        if (roleId != null && roleId.longValue() > 0){
            List<MenuPo> roleMenus = menuService.findMenuByRoleId(roleId);
            Iterator<MenuPo> it = roleMenus.iterator();
            while (it.hasNext()){
            	MenuPo menu = it.next();
            	if (userMenuMap.containsKey(menu.getCode())){
            		userMenuMap.get(menu.getCode()).setChecked(true);
            	}
            }
        }
        
        return userMenus;
	}
}
