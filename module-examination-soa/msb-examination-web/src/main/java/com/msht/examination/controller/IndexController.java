/**
 * 
 */
package com.msht.examination.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.msht.examination.common.RestResult;
import com.msht.examination.common.ResultInfo;
import com.msht.examination.common.ResultInfo.ResultCode;
import com.msht.examination.security.Constants;
import com.msht.examination.security.bind.annotation.CurrentUser;
import com.msht.examination.user.entity.MenuPo;
import com.msht.examination.user.entity.UserPo;
import com.msht.examination.user.service.MenuService;
import com.msht.examination.user.service.UserService;
import com.msht.examination.user.service.impl.PasswordHelper;
import com.msht.examination.vo.NameValue;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.common.utils.CurrencyUtils;
import com.msht.framework.common.utils.DateUtils;


/**
 * @author lindaofen
 *
 */
@Controller  
@RequestMapping("/") 
public class IndexController {

	private Logger logger = LoggerFactory.getLogger(IndexController.class);
	
    @Autowired
    private MenuService menuService;
    @Autowired
    private UserService userService;

	@Autowired
	private PasswordHelper passwordHelper;
	
    @RequestMapping("/")
    public String index(@CurrentUser UserPo loginUser, Model model,HttpServletRequest request) {
        Set<String> permissions = userService.findPermissions(loginUser.getUsername());
        List<MenuPo> menus = menuService.findMenus(permissions);
        //model.addAttribute("menus", menus);
        request.getSession().setAttribute(Constants.CURRENT_MENUS, menus);
        
        return "index";
    }

    @RequestMapping("/welcome")
    public String welcome() {
        return "welcome";
    }
    
	@RequestMapping("/admin_password")  
    public String passwordEdit(Model model){  
		return "admin_password";
	}
	
	//@OperLog(module=LogConstants.ADMIN_MODULE, methods="修改密码")
	@RequestMapping(value="/modifyPwd",method=RequestMethod.POST)  
	@ResponseBody
    public Map<String, Object> modifyPwd(@CurrentUser UserPo loginUser,
    		@RequestParam(value="oldPwd",required=true) String oldPwd,
    		@RequestParam(value="newPwd",required=true) String newPwd,
    		@RequestParam(value="confirmPwd",required=true) String confirmPwd){  

		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{
			if (!newPwd.equals(confirmPwd)){
				result.setCode(ResultCode.FAILED.getCode());
				result.setMessage("确认密码不一致，请重新输入！");
				return RestResult.restResult(result, null);
			}
			UserPo po = new UserPo();
			po.setPassword(oldPwd);
			po.setSalt(loginUser.getSalt());
			passwordHelper.encryptPassword(po);
			if (!loginUser.getPassword().equals(po.getPassword())){
				result.setCode(ResultCode.PASSWORD_MISTAKE.getCode());
				result.setMessage(ResultCode.PASSWORD_MISTAKE.getMessage());
				return RestResult.restResult(result, null);
			}
			
			//修改密码
			po.setId(loginUser.getId());
			po.setPassword(newPwd);
			po.setUpdateTime(new Date());
			userService.update(po);
			
		} catch (BusinessException e){
			logger.error(
					"Modify the password exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"Modify the password exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}
		return RestResult.restResult(result, null);
    } 
	
}
