/**
 * 
 */
package com.msht.examination.security.filter;

import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.springframework.beans.factory.annotation.Autowired;

import com.msht.examination.common.log.LogConstants;
import com.msht.examination.user.entity.LogPo;
import com.msht.examination.user.entity.UserPo;
import com.msht.examination.user.service.LogService;
import com.msht.examination.user.service.UserService;

/**
 * @author lindaofen
 *
 */
public class SysFormAuthenticationFilter extends FormAuthenticationFilter {

	@Autowired
	private LogService logService;
	
	@Autowired
	private UserService userService;
	
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		//方法通知前获取时间,用来计算模块执行时间的
		long start = System.currentTimeMillis();
		boolean result = super.onAccessDenied(request, response);
		if (!result){
			WebDelegatingSubject subject = (WebDelegatingSubject) SecurityUtils.getSubject();
			if (subject != null){
				String username = subject.getPrincipal().toString();
				UserPo po = new UserPo();
				po.setUsername(username);
				UserPo user = userService.get(po);
				LogPo log = new LogPo();
				log.setUserId(user.getId());
				log.setUsername(subject.getPrincipal().toString());
				log.setStatus(LogPo.LogStatus.SUCCESS.getStatus());
				// 获取用户ip
				//String ip = request.getRemoteAddr();
				log.setIp(subject.getHost());
				//log.setGroupId(PropertyUtils.getGroupId(user));
				log.setOperTime(new Date());
				log.setModule(LogConstants.ADMIN_MODULE);
				log.setContent("登录成功");
				long end = System.currentTimeMillis();
				log.setRespTime(end - start);
				logService.insert(log);;
			}
		}
		return result;
	}

	
}
