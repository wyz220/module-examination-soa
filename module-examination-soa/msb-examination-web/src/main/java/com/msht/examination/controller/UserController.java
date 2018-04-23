/**
 * 
 */
package com.msht.examination.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.msht.examination.user.entity.RolePo;
import com.msht.examination.user.entity.UserPo;
import com.msht.examination.user.entity.UserPo.UserStatus;
import com.msht.examination.user.entity.UserPo.UserType;
import com.msht.examination.user.service.RoleService;
import com.msht.examination.user.service.UserService;
import com.msht.examination.user.service.impl.PasswordHelper;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.common.utils.Delimiters;
import com.msht.framework.entity.Page;

/**
 * @author lindaofen
 *
 */
@Controller  
@RequestMapping("/user") 
public class UserController {

	private Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private RoleService roleService;
	
	@Autowired
	private PasswordHelper passwordHelper;
	
	@RequestMapping("/list")  
    public String list(@CurrentUser UserPo loginUser,Model model,@RequestParam(value="pageNo",defaultValue="1") Integer pageNo,
    		@RequestParam(value="pageSize",defaultValue="20") Integer pageSize){  
		
		UserPo po = new UserPo();
		po.setIsDel(0);
		Page<UserPo> page = new Page<UserPo>();
		page.setPageNo(pageNo);
		page.setPageSize(pageSize);
		po.setPage(page);
		
		StringBuffer condition = new StringBuffer();
		condition.append(" and id != " + loginUser.getId());
		po.getPage().setCondition(condition.toString());
		
		logger.info(JSON.toJSONString(po));
		Page<UserPo> pageObj = userService.page(po);
		if (page.getList() != null && page.getList().size() > 0){
			List<Long> userIds = new ArrayList<Long>();
			for (UserPo user : page.getList()){
				userIds.add(user.getId());
			}
			List<RolePo> roleList = roleService.findRoleByUserId(userIds.toArray(new Long[]{}));
			Map<Long, RolePo> roleMap = new HashMap<Long, RolePo>();
			for (RolePo role : roleList){
				roleMap.put(role.getUserId(), role);
			}
			
			for (UserPo user : page.getList()){
				user.setRole(roleMap.get(user.getId()));
			}
			
		}

		//pageObj.setPageUrl("/user/page");
		model.addAttribute("pageObj", pageObj);
		return "user/user_list";
    } 
	
	@RequestMapping("/add")  
    public String add(@CurrentUser UserPo loginUser,Model model,@RequestParam(value="id",required=false) Long id){  
		
		UserPo userPo = null;
		if (id != null && id.longValue() >= 0){
			UserPo param = new UserPo();
			param.setId(id);
		    userPo = userService.get(param);
		}
		StringBuffer condition = new StringBuffer();
		condition.append(" and is_supper != 1");
		RolePo queryParam = new RolePo();
		queryParam.getPage().setCondition(condition.toString());
		List<RolePo> roleList = roleService.list(queryParam);
		Long roleId = userService.findUserRoleId(id);
		
		model.addAttribute("roleId", roleId);
		model.addAttribute("user", userPo);
		model.addAttribute("roleList", roleList);
		return "user/user_add";
    } 
	
	//@OperLog(module=LogConstants.ADMIN_MODULE, methods="新增/修改管理员")
	@RequestMapping("/saveOrUpdate")  
	@ResponseBody
    public Map<String, Object> saveOrUpdate(@CurrentUser UserPo loginUser,
    		@RequestParam(value="id",required=false) Long id,
    		@RequestParam(value="username",required=true) String username,
    		@RequestParam(value="roleId",required=true) Long roleId,
    		@RequestParam(value="password",required=true) String password,
    		@RequestParam(value="confirmPassword",required=true) String confirmPassword){  

		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{
			UserPo userPo = new UserPo();
			userPo.setUsername(username);
			userPo.setPassword(password);
			userPo.setRoleId(roleId);
			if (id == null || id.longValue() <= 0){
				userPo.setStatus(UserStatus.NORMAL.getStatus());
				userPo.setUserType(UserType.NORMAL.getType());
				userPo.setCreateBy(loginUser.getId());
				userPo.setCreateTime(new Date());
				userService.insert(userPo);
			}else{
				userPo.setId(id);
				userPo.setUpdateBy(loginUser.getId());
				userPo.setUpdateTime(new Date());
				userService.update(userPo);
			}
		} catch (BusinessException e){
			logger.error(
					"Save or update user exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"Save or update user exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}
		return RestResult.restResult(result, null);
    } 
	
	//@OperLog(module=LogConstants.ADMIN_MODULE, methods="删除管理员")
	@RequestMapping("/delete")  
	@ResponseBody
    public Map<String, Object> delete(@CurrentUser UserPo loginUser,@RequestParam(value="id") Long id){  
		
		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{
			UserPo user = new UserPo();
			user.setId(id);
			user.setIsDel(1);
			user.setUpdateBy(loginUser.getId());
			user.setUpdateTime(new Date());
			userService.delete(user);
		} catch (BusinessException e){
			logger.error(
					"Delete user exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"Delete user exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}
		return RestResult.restResult(result, null);
    } 
	
	//@OperLog(module=LogConstants.ADMIN_MODULE, methods="批量删除管理员")
	@RequestMapping("/batchDelete")  
	@ResponseBody
    public Map<String, Object> batchDelete(@RequestParam(value="userIds") String userIds){  
		
		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{
			String[] userIdArr = userIds.split(Delimiters.COMMA);
			Long[] ids = new Long[userIdArr.length];
			for (int i = 0; i < userIdArr.length; i++){
				ids[i] = Long.valueOf(userIdArr[i]);
			}
			userService.batchDelete(ids);
		} catch (BusinessException e){
			logger.error(
					"Delete user exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"Delete user exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}
		return RestResult.restResult(result, null);
    } 
	


}
