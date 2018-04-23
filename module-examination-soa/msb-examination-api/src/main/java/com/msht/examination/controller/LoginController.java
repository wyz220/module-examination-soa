/**
 * 
 *//*
package com.msht.examination.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.msht.framework.common.exception.BusinessException;
import com.msht.examination.authorization.annotation.Authorization;
import com.msht.examination.authorization.annotation.CurrentUser;
import com.msht.examination.authorization.manager.TokenManager;
import com.msht.examination.authorization.model.TokenModel;
import com.msht.examination.common.Constants;
import com.msht.examination.common.RestResult;
import com.msht.examination.common.ResultInfo;
import com.msht.examination.common.ResultInfo.ResultCode;
import com.msht.examination.user.entity.UserPo;
import com.msht.examination.user.service.UserService;
import com.msht.examination.utils.PwdHelper;


*//**
 * @author lindaofen
 *
 *//*
@Controller  
@RequestMapping("/") 
public class LoginController {

	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PwdHelper passwordHelper;
	
    @Autowired
    private TokenManager tokenManager;
	
	@RequestMapping("/login")  
	@ResponseBody
    public Map<String, Object> login(@RequestParam(value="username",required=true) String username,
    		@RequestParam(value="password",required=true) String password){  

		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		TokenModel token = null;
		try{
			
			UserPo po = new UserPo();
			po.setUsername(username);
			UserPo user = userService.get(po);
			if (user == null){
				result.setCode(ResultCode.INVALID_USERNAME.getCode());
				result.setMessage(ResultCode.INVALID_USERNAME.getMessage());
				return RestResult.restResult(result, null);
			}
			
			String inputPassword = passwordHelper.encryptPassword(password, user.getSalt());
			if (!user.getPassword().equals(inputPassword)){
				result.setCode(ResultCode.INVALID_USERNAME.getCode());
				result.setMessage(ResultCode.INVALID_USERNAME.getMessage());
				return RestResult.restResult(result, null);
			}
			
		    token = tokenManager.createToken(user.getId());
			
		} catch (BusinessException e){
			logger.error(
					"User Login exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"User Login exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}
		return RestResult.restResult(result, token.getToken());
    } 
	
	@RequestMapping("/logout")  
	@ResponseBody
	@Authorization
    public Map<String, Object> logout(HttpServletRequest request,@CurrentUser UserPo user){  

		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{
			String authorization = request.getHeader(Constants.AUTHORIZATION);
		    tokenManager.deleteToken(authorization);
		} catch (BusinessException e){
			logger.error(
					"User logout exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"User logout exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}
		return RestResult.restResult(result, null);
    } 
}
*/