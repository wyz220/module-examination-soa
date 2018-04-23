/**
 * 
 */
package com.msht.examination.security.filter;

import java.util.Date;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.session.SessionException;
import org.apache.shiro.web.filter.authc.LogoutFilter;
import org.apache.shiro.web.subject.support.WebDelegatingSubject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SysLogoutFilter extends LogoutFilter {

	private static final Logger log = LoggerFactory.getLogger(SysLogoutFilter.class);
	
	@Autowired
	private LogService logService;
	
	@Autowired
	private UserService userService;
	
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
		//方法通知前获取时间,用来计算模块执行时间的
		long start = System.currentTimeMillis();
		WebDelegatingSubject subject = (WebDelegatingSubject) getSubject(request, response);
        String redirectUrl = getRedirectUrl(request, response, subject);
        //try/catch added for SHIRO-298:
		LogPo logPo = null;
        try {
        	if (subject != null && subject.getPrincipal() != null){
        		logPo = new LogPo();
    			String username = subject.getPrincipal().toString();
    			UserPo po = new UserPo();
    			po.setUsername(username);
    			UserPo user = userService.get(po);
    			logPo.setUserId(user.getId());
    			logPo.setUsername(subject.getPrincipal().toString());
    			logPo.setStatus(LogPo.LogStatus.SUCCESS.getStatus());
    			// 获取用户ip
    			//String ip = request.getRemoteAddr();
    			logPo.setIp(subject.getHost());
    			//logPo.setGroupId(PropertyUtils.getGroupId(user));
    			logPo.setOperTime(new Date());
    			logPo.setModule(LogConstants.ADMIN_MODULE);
    			logPo.setContent("退出成功");
        	}
			
            subject.logout();
            if (logPo != null){
    			long end = System.currentTimeMillis();
    			logPo.setRespTime(end - start);
    			logService.insert(logPo);
            }

        } catch (SessionException ise) {
            log.debug("Encountered session exception during logout.  This can generally safely be ignored.", ise);
        }
        issueRedirect(request, response, redirectUrl);
        return false;
    }
}
