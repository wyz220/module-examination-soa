/**
 * 
 */
package com.msht.examination.controller;

import java.io.UnsupportedEncodingException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.net.URLCodec;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Constants;
import com.msht.examination.common.RestResult;
import com.msht.examination.common.ResultInfo;
import com.msht.examination.common.ResultInfo.ResultCode;
import com.msht.examination.common.SysConfig;
import com.msht.examination.user.entity.UserPo;
import com.msht.examination.user.entity.UserPo.UserStatus;
import com.msht.examination.user.service.UserService;
import com.msht.examination.user.service.impl.PasswordHelper;
import com.msht.framework.common.exception.BusinessException;
import com.msht.framework.common.redis.core.JedisPoolManager;
import com.msht.framework.common.utils.EmailUtils;
import com.msht.framework.common.utils.StringUtils;

/**
 * @author lindaofen
 *
 */
@Controller  
@RequestMapping("/")  
public class LoginController {
	
	private Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private PasswordHelper passwordHelper;

	@RequestMapping("/login")  
	public String login(Model model,HttpServletRequest request, HttpServletResponse response){
		Subject subject = SecurityUtils.getSubject();
		if (subject != null && subject.isAuthenticated()){
			return "redirect:/"; 
		}
        String exceptionClassName = (String)request.getAttribute("shiroLoginFailure");
        String error = null;
        if(UnknownAccountException.class.getName().equals(exceptionClassName)) {
            error = "用户名或密码错误";
        } else if(IncorrectCredentialsException.class.getName().equals(exceptionClassName)) {
            error = "用户名或密码错误";
        } else if (ExcessiveAttemptsException.class.getName().equals(exceptionClassName)){
        	error = "登录过于频繁，账号已经被锁定";
        }
        else if(exceptionClassName != null) {
            error = "系统超时，请与管理员联系";
        }
        model.addAttribute("error", error);
        
        return "login";
	}
	
	@RequestMapping("/register")  
	public String register(Model model,HttpServletRequest request, HttpServletResponse response){
        return "register";
	}
	
	@RequestMapping("/checkCode")  
	@ResponseBody
	public Map<String, Object> checkRegisterParam(Model model,HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="email",required=true) String email
			,@RequestParam(value="code",required=true) String code){
		

		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{
			String verifyCode = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
			if (!code.equalsIgnoreCase(verifyCode)){
				result.setCode(ResultCode.INVALID_VERIFY_CODE.getCode());
				result.setMessage(ResultCode.INVALID_VERIFY_CODE.getMessage());
				return RestResult.restResult(result, null);
			}
			
			UserPo po = new UserPo();
			po.setUsername(email);
			UserPo user = userService.get(po);
			if (user != null && UserStatus.getStatus(user.getStatus()) == UserStatus.NORMAL){
				result.setCode(ResultCode.USER_ALREADY_EXISTS.getCode());
				result.setMessage(ResultCode.USER_ALREADY_EXISTS.getMessage());
				return RestResult.restResult(result, null);
			}
		} catch (BusinessException e){
			logger.error(
					"Check verify code exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"Check verify code exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}

		return RestResult.restResult(result, null);
	}
	
	/*@RequestMapping("/register_step2")  
	public String register_step2(Model model,HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="username",required=true) String username
			,@RequestParam(value="password",required=true) String password
			,@RequestParam(value="qcode",required=true) String qcode
			,@RequestParam(value="agreement",required=true) Boolean agreement){
		

		try{
			if (!RegexUtils.match(RegexUtils.EMAIL_EX, username)){
				String errorMsg = "邮箱格式不正确，请重新输入！";
				model.addAttribute("errorMsg", errorMsg);
				return "register_fail";
			}
			if (password.length() < 6 || password.length() > 20){
				String errorMsg = "密码长度只能在6-20个字符之间，请重新输入！";
				model.addAttribute("errorMsg", errorMsg);
				return "register_fail";
			}
			if (!agreement){
				String errorMsg = "请勾选我已阅读并同意民生宝用户协议！！";
				model.addAttribute("errorMsg", errorMsg);
				return "register_fail";
			}
			String code = (String) request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
			if (!qcode.equalsIgnoreCase(code)){
				String errorMsg = "无效验证码，请重新注册！";
				model.addAttribute("errorMsg", errorMsg);
				return "register_fail";
			}
			
			
			UserPo po = new UserPo();
			po.setUsername(username);
			UserPo oldUser = userService.get(po);
			if (oldUser == null){
				po.setEmail(username);
				po.setPassword(password);
				po.setType(1);
				po.setStatus(UserStatus.WAIT_ACTIVATE.getStatus());
				po.setUserType(UserType.ADMIN.getType());
				po.setAuditStatus(AuditStatus.WAIT_AUDIT.getStatus());
				po.setGroupId(0L);
				po.setCreateTime(new Date());
				po.setRoleId(Long.valueOf(SysConfig.USER_REGISTER_DEFAULTROLEID));
				userService.insert(po);
			}else{
				oldUser.setPassword(password);
				oldUser.setType(1);
				oldUser.setStatus(UserStatus.WAIT_ACTIVATE.getStatus());
				oldUser.setUserType(UserType.ADMIN.getType());
				oldUser.setAuditStatus(AuditStatus.WAIT_AUDIT.getStatus());
				oldUser.setGroupId(0L);
				oldUser.setCreateTime(new Date());
				oldUser.setRoleId(Long.valueOf(SysConfig.USER_REGISTER_DEFAULTROLEID));
				userService.update(po);
			}
			
			String activeCode = StringUtils.generateRandomStr(StringUtils.BASE1, 108);
			JedisPoolManager.getMgr().set(username, JedisPoolManager.EXPIRE_DAY, activeCode, JedisPoolManager.TEMP_DB, true);
			sendRegisterEmail(username, activeCode);
		} catch (BusinessException e){
			logger.error(
					"User register exception occurred, cause by:{}",
					e);
			String errorMsg = "系统处理超时，请与管理员联系！";
			model.addAttribute("errorMsg", errorMsg);
			return "register_fail";
		} catch (Exception e){
			logger.error(
					"User register exception occurred, cause by:{}",
					e);
			String errorMsg = "系统处理超时，请与管理员联系！";
			model.addAttribute("errorMsg", errorMsg);
			return "register_fail";
		}

		model.addAttribute("username", username);
        return "register_step2";
	}*/
	
	@RequestMapping("/agreement")  
	public String agreement(Model model,HttpServletRequest request, HttpServletResponse response){
        return "agreement";
	}
	
	@RequestMapping("/activeUserValid")  
	public String activeUserValid(Model model,HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="ac",required=true) String ac
			,@RequestParam(value="email",required=true) String email){
		
		try{
/*			String activeCode = JedisPoolManager.getMgr().get(email, JedisPoolManager.TEMP_DB);
			if (StringUtils.isBlank(activeCode)){
				String errorMsg = "链接已经过期，请重新注册！";
				model.addAttribute("errorMsg", errorMsg);
				return "register_fail";
			}
			
			if (!ac.equals(activeCode)){
				String errorMsg = "无效链接，请重新注册！";
				model.addAttribute("errorMsg", errorMsg);
				return "register_fail";
			}
			
			UserPo po = new UserPo();
			po.setUsername(email);
			UserPo user = userService.get(po);
			user.setStatus(UserStatus.NORMAL.getStatus());
			userService.update(user);
			
			//删除验证码
			JedisPoolManager.getMgr().del(email, JedisPoolManager.TEMP_DB);*/
			UserPo po = new UserPo();
			po.setUsername(email);
			UserPo user = userService.get(po);
			model.addAttribute("user", user);
		} catch (BusinessException e){
			logger.error(
					"Activate the user exception occurred, cause by:{}",
					e);
			String errorMsg = "系统处理超时，请与管理员联系！";
			model.addAttribute("errorMsg", errorMsg);
			return "register_fail";
		} catch (Exception e){
			logger.error(
					"Activate the user exception occurred, cause by:{}",
					e);
			String errorMsg = "系统处理超时，请与管理员联系！";
			model.addAttribute("errorMsg", errorMsg);
			return "register_fail";
		}


        return "register_step3";
	}
	
/*	@RequestMapping("/register_success")  
    public String register_step4(Model model,HttpServletRequest request, HttpServletResponse response,
    		UserPo po,
    		@RequestParam(value="legalIdCardFrontFile",required=false) MultipartFile legalIdCardFrontFile,
    		@RequestParam(value="legalIdCardBackFile",required=false) MultipartFile legalIdCardBackFile,
    		@RequestParam(value="legalBusLicenseFrontFile",required=false) MultipartFile legalBusLicenseFrontFile,
    		@RequestParam(value="legalBusLicenseBackFile",required=false) MultipartFile legalBusLicenseBackFile,    		
    		@RequestParam(value="linkmanIdCardFrontFile",required=false) MultipartFile linkmanIdCardFrontFile,
    		@RequestParam(value="linkmanIdCardBackFile",required=false) MultipartFile linkmanIdCardBackFile){  

		try{
			if (legalIdCardFrontFile != null && legalIdCardFrontFile.getSize() > 0){
				String legalIdCardFrontImg = AliyunOSS.putObject(AliyunOSS.bucketName, SysConfig.ALIYUN_OSS_USERIMGSAVEPATH + PropertyUtils.generateFileName(),
						legalIdCardFrontFile.getInputStream());
				po.setLegalIdCardFrontImg(legalIdCardFrontImg);
			}

			if (legalIdCardBackFile != null && legalIdCardBackFile.getSize() > 0){
				String legalIdCardBackImg = AliyunOSS.putObject(AliyunOSS.bucketName, SysConfig.ALIYUN_OSS_USERIMGSAVEPATH + PropertyUtils.generateFileName(),
						legalIdCardBackFile.getInputStream());
				po.setLegalIdCardBackImg(legalIdCardBackImg);
			}

			if (legalBusLicenseFrontFile != null && legalBusLicenseFrontFile.getSize() > 0){
				String legalBusLicenseFrontImg = AliyunOSS.putObject(AliyunOSS.bucketName, SysConfig.ALIYUN_OSS_USERIMGSAVEPATH + PropertyUtils.generateFileName(),
						legalBusLicenseFrontFile.getInputStream());
				po.setLegalBusLicenseFrontImg(legalBusLicenseFrontImg);
			}

			if (legalBusLicenseBackFile != null && legalBusLicenseBackFile.getSize() > 0){
				String legalBusLicenseBackImg = AliyunOSS.putObject(AliyunOSS.bucketName, SysConfig.ALIYUN_OSS_USERIMGSAVEPATH + PropertyUtils.generateFileName(),
						legalBusLicenseBackFile.getInputStream());
				po.setLegalBusLicenseBackImg(legalBusLicenseBackImg);
			}
			
			if (linkmanIdCardFrontFile != null && linkmanIdCardFrontFile.getSize() > 0){
				String linkmanIdCardFrontImg = AliyunOSS.putObject(AliyunOSS.bucketName, SysConfig.ALIYUN_OSS_USERIMGSAVEPATH + PropertyUtils.generateFileName(),
						linkmanIdCardFrontFile.getInputStream());
				po.setLinkmanIdCardFrontImg(linkmanIdCardFrontImg);
			}
			if (linkmanIdCardBackFile != null && linkmanIdCardBackFile.getSize() > 0){
				String linkmanIdCardBackImg = AliyunOSS.putObject(AliyunOSS.bucketName, SysConfig.ALIYUN_OSS_USERIMGSAVEPATH + PropertyUtils.generateFileName(),
						linkmanIdCardBackFile.getInputStream());
				po.setLinkmanIdCardBackImg(linkmanIdCardBackImg);
			}
			userService.update(po);
			
			
		} catch (BusinessException e){
			logger.error(
					"Save account exception occurred, cause by:{}",
					e);
			String errorMsg = "系统处理超时，请与管理员联系！";
			model.addAttribute("errorMsg", errorMsg);
			return "register_fail";
		} catch (Exception e){
			logger.error(
					"Save account exception occurred, cause by:{}",
					e);
			String errorMsg = "系统处理超时，请与管理员联系！";
			model.addAttribute("errorMsg", errorMsg);
			return "register_fail";
		}
		 return "register_step4";
    } */
	
	@RequestMapping("/forgetPassword")  
	public String forgetPassword(Model model,HttpServletRequest request, HttpServletResponse response){
        return "forgetPassword";
	}
	
	@RequestMapping("/sendVerifyCode")  
	@ResponseBody
	public Map<String, Object> sendVerifyCode(Model model,HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="email",required=true) String email){
		

		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{
			UserPo po = new UserPo();
			po.setUsername(email);
			UserPo user = userService.get(po);
			if (user == null){
				result.setCode(ResultCode.USER_NOT_EXIST.getCode());
				result.setMessage(ResultCode.USER_NOT_EXIST.getMessage());
				return RestResult.restResult(result, null);
			}
			String verifyCode = StringUtils.generateRandomStr(6);
			JedisPoolManager.getMgr().set(email, JedisPoolManager.EXPIRE_HALF_HOUR, verifyCode, JedisPoolManager.VERIFY_CODE_DB, true);
			sendVerifyCodeEmail(email, verifyCode);
		} catch (BusinessException e){
			logger.error(
					"Send verify code exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"Send verify code exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}

		return RestResult.restResult(result, null);
	}
	
	@RequestMapping("/validVerifyCode")  
	@ResponseBody
	public Map<String, Object> validVerifyCode(Model model,HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="email",required=true) String email
			,@RequestParam(value="code",required=true) String code){
		
		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{
			String verityCode = JedisPoolManager.getMgr().get(email, JedisPoolManager.VERIFY_CODE_DB);
			if (!code.equals(verityCode)){
				result.setCode(ResultCode.INVALID_VERIFY_CODE.getCode());
				result.setMessage(ResultCode.INVALID_VERIFY_CODE.getMessage());
				return RestResult.restResult(result, null);
			}
		} catch (BusinessException e){
			logger.error(
					"Valid verify code exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"Valid verify code exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}
		return RestResult.restResult(result, null);
	}
	
	@RequestMapping("/resetPassword")  
	public String resetPassword(Model model,HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="email",required=true) String email){
		model.addAttribute("email", email);
        return "resetPassword";
	}
	
	@RequestMapping("/updatePassword")  
	@ResponseBody
    public Map<String, Object> updatePassword(Model model,HttpServletRequest request, HttpServletResponse response,
    		@RequestParam(value="email",required=true) String email,
    		@RequestParam(value="password",required=true) String password,
    		@RequestParam(value="password2",required=true) String password2){  

		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{
			UserPo po = new UserPo();
			po.setUsername(email);
			UserPo user = userService.get(po);
			user.setPassword(password);
			userService.update(user);
		} catch (BusinessException e){
			logger.error(
					"Reset password exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"Reset password exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}
		return RestResult.restResult(result, null);
    } 
	
	@RequestMapping("/sendEmail")  
	@ResponseBody
	public Map<String, Object> sendEmail(Model model,HttpServletRequest request, HttpServletResponse response
			,@RequestParam(value="email",required=true) String email){
		
		ResultInfo result = new ResultInfo();
		result.setCode(ResultCode.SUCCESS.getCode());
		result.setMessage(ResultCode.SUCCESS.getMessage());
		try{

			String activeCode = StringUtils.generateRandomStr(StringUtils.BASE1, 108);
			JedisPoolManager.getMgr().set(email, JedisPoolManager.EXPIRE_DAY, activeCode, JedisPoolManager.TEMP_DB, true);
			sendRegisterEmail(email, activeCode);
		} catch (BusinessException e){
			logger.error(
					"Send email exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		} catch (Exception e){
			logger.error(
					"Send email exception occurred, cause by:{}",
					e);
			result.setCode(ResultCode.FAILED.getCode());
			result.setMessage(ResultCode.FAILED.getMessage());
			return RestResult.restResult(result, null);
		}
		return RestResult.restResult(result, null);
	}
	
	private void sendRegisterEmail(String email, String activeCode) throws UnsupportedEncodingException{
		URLCodec urlCodec = new URLCodec();
		String ac = urlCodec.encode(activeCode, CharEncoding.UTF_8);
		StringBuffer urlBuff = new StringBuffer();
		urlBuff.append(SysConfig.SYS_DOMAIN_ADDRESS)
		.append("/activeUserValid")
		.append("?ac=" + ac)
		.append("&email=" + urlCodec.encode(email, CharEncoding.UTF_8));

/*        StringBuffer sb=new StringBuffer("点击下面链接激活账号，24小时生效，否则重新注册账号，链接只能使用一次，请尽快激活！</br>");  
        sb.append("<a href="+ urlBuff.toString() + ">");   
        sb.append(urlBuff.toString());    
        sb.append("</a>");*/  
		StringBuffer sb = new StringBuffer();
		sb.append("<html><head>")
		  .append("<base target=\"_blank\"><style type=\"text/css\">::-webkit-scrollbar{ display: none; }</style><style id=\"cloudAttachStyle\" type=\"text/css\">#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}</style>")
		  .append("</head>")
		  .append("<body tabindex=\"0\" role=\"listitem\">")
		  
		  .append("<table style=\"\" cellspacing=\"0\" border=\"0\" cellpadding=\"0\" width=\"800\">")
		  .append("<tbody>")
		  .append("<tr><td style=\"font-size:0;\" align=\"left\" valign=\"top\"><img src=\"http://sp.msbapp.cn/images/bbg1.jpg\" height=\"70\" width=\"800\"></td></tr>")
		  .append("<tr>")
		  .append("<td style=\"font-size:14px;padding:50px 40px 20px 40px;\">")
		  .append("<p style=\"padding-bottom:20px;\">亲爱的<a href=\"#\">" + email + "</a>，您好！</p >")
		  .append("<p style=\"padding-bottom:20px;\">您正在进行民生宝-您正在进行民生宝-包工头企业端注册，点击以下链接完成验证，注册即可成功。 </p >")
		  .append("<p style=\"padding-bottom:20px;\"><a href=" + urlBuff.toString() + " target=\"_blank\" style=\"color:#699f00;\">" + urlBuff.toString() + "</a></p >")
		  .append("<p style=\"padding-bottom:20px;\">（该链接在24小时内有效，24小时后需要重新获取验证邮件）</p >")
		  .append("<p style=\"padding-bottom:20px;\">如果该链接无法点击，请将其复制粘贴到你的浏览器地址栏中访问。</p >")
		  .append("<p style=\"padding-bottom:20px;\">如果这不是您的邮件，请忽略此邮件。</p >")
		  .append("<p style=\"padding-bottom:20px;\">这是民生宝系统邮件，请勿回复。</p >")
		  .append("<p style=\"text-align:right;\">—— 民生宝·包工头</p >")
		  .append("</td>")
		  .append("</tr>")
		  .append("<tr><td style=\"font-size:0;\" align=\"left\" valign=\"top\"><img src=\"http://sp.msbapp.cn/images/bbg2.jpg\" height=\"77\" width=\"800\"></td></tr>")
		  .append("</tbody>")
		  .append("</table>")
		  
		  .append("<style type=\"text/css\">")
		  .append("body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}")
		  .append("td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}")
		  .append("pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}")
		  .append("th,td{font-family:arial,verdana,sans-serif;line-height:1.666}")
		  .append("img{ border:0}")
		  .append("header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}")
		  .append("blockquote{margin-right:0px}")
		  .append("</style>")
		  .append("<style id=\"ntes_link_color\" type=\"text/css\">a,td a{color:#064977}</style>")
		  .append("</body></html>");
        
           EmailUtils.send(email, "您正在注册民生宝·包工头，请激活验证链接！",sb.toString());
	}
	
	private void sendVerifyCodeEmail(String email, String code) throws UnsupportedEncodingException{

        StringBuffer sb = new StringBuffer();   
        sb.append("<html><head>")
        .append("<base target=\"_blank\">")
        .append("<style type=\"text/css\">::-webkit-scrollbar{ display: none; }</style>")
        .append("<style id=\"cloudAttachStyle\" type=\"text/css\">#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}</style>")
        .append("</head>")
        .append("<body tabindex=\"0\" role=\"listitem\">")
        
        .append("<table style=\"\" cellspacing=\"0\" border=\"0\" cellpadding=\"0\" width=\"800\">")
        .append("<tbody>")
        .append("<tr><td style=\"font-size:0;\" align=\"left\" valign=\"top\"><img src=\"http://sp.msbapp.cn/images/bbg1.jpg\" height=\"70\" width=\"800\"></td></tr>")
        .append("<tr>")
        .append("<td style=\"font-size:14px;padding:50px 40px 20px 40px;\">")
        .append("<p style=\"padding-bottom:20px;\">亲爱的<a href=\"#\">" + email + "</a>，您好！</p >")
        .append("<p style=\"padding-bottom:20px;\">您正在进行民生宝-包工头企业端密码找回，以下为验证码。</p >")
        .append("<b style=\"padding-bottom:20px;font-size:20px;\">" + code + "</b >")
        .append("<p style=\"padding-bottom:20px;\">（该验证码在30分钟内有效，30分钟后需要重新获取验证码邮件）</p >")
        .append("<p style=\"padding-bottom:20px;\">如果这不是您的邮件，请忽略此邮件。</p >")
        .append("<p style=\"padding-bottom:20px;\">这是民生宝系统邮件，请勿回复。</p >")
        .append("<p style=\"text-align:right;\">—— 民生宝·包工头</p >")
        .append("</td>")
        .append("</tr>")
        .append("<tr><td style=\"font-size:0;\" align=\"left\" valign=\"top\"><img src=\"http://sp.msbapp.cn/images/bbg2.jpg\" height=\"77\" width=\"800\"></td></tr>")
        .append("</tbody>")
        .append("</table>")
        .append("<style type=\"text/css\">")
        .append("body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}")
        .append("td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}")
        .append("pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}")
        .append("th,td{font-family:arial,verdana,sans-serif;line-height:1.666}")
        .append("img{ border:0}")
        .append("header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}")
        .append("blockquote{margin-right:0px}")
        .append("</style>")
        .append("<style id=\"ntes_link_color\" type=\"text/css\">a,td a{color:#064977}</style>")
        .append("</body></html>");
        EmailUtils.send(email, "您正在找回民生宝·包工头密码，请复制验证码！",sb.toString());
	}
}
