/**
 * 
 */
package com.msht.examination.common.log.aop;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.alibaba.fastjson.JSON;
import com.msht.examination.common.log.annotation.OperLog;
import com.msht.examination.security.Constants;
import com.msht.examination.user.entity.LogPo;
import com.msht.examination.user.entity.UserPo;
import com.msht.examination.user.service.LogService;
import com.msht.examination.utils.PropertyUtils;

/**
 * @author lindaofen
 *
 */
@Aspect
@Component
public class OperLogAop {

	@Autowired
	private LogService logService;
	
	// 配置接入点
	@Pointcut("execution(* com.msht.msb_property.controller..*.*(..))")
	private void controllerAspect() {}

	@Around("controllerAspect()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {

		// 获取登录用户账户
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		UserPo user = (UserPo) request.getAttribute(Constants.CURRENT_USER);
		// 如果用户没有登录
		if (user == null) {
			Object object = pjp.proceed();
			return object;
		}

		//方法通知前获取时间,用来计算模块执行时间的
		long start = System.currentTimeMillis();
		// 拦截的实体类，就是当前正在执行的controller
		Object target = pjp.getTarget();
		//拦截的方法名称。当前正在执行的方法
		String methodName = pjp.getSignature().getName();
		// 拦截的方法参数
		Object[] args = pjp.getArgs();
		// 拦截的放参数类型
		Signature sig = pjp.getSignature();
		MethodSignature msig = null;
		if (!(sig instanceof MethodSignature)) {
			throw new IllegalArgumentException("该注解只能用于方法");
		}
		msig = (MethodSignature) sig;
		Class[] parameterTypes = msig.getMethod().getParameterTypes();

		Object object = null;
		// 获得被拦截的方法
		Method method = null;
		try {
			method = target.getClass().getMethod(methodName, parameterTypes);
			if (method == null){
				object = pjp.proceed();
				return object;
			}
			// 判断是否包含自定义的注解，说明一下这里的OperLog就是我自己自定义的注解
			if (method.isAnnotationPresent(OperLog.class)) {
				LogPo log = new LogPo();
				log.setUserId(user.getId());
				log.setUsername(user.getUsername());
				log.setStatus(LogPo.LogStatus.SUCCESS.getStatus());
				// 获取用户ip
				String ip = PropertyUtils.getRemoteHost(request);
				log.setIp(ip);
				log.setGroupId(0l);
				log.setOperTime(new Date());
				
				//获取注解内容
				OperLog systemlog = method.getAnnotation(OperLog.class);
				log.setModule(systemlog.module());
				log.setContent(systemlog.methods());
				try {
					object = pjp.proceed();
					if (object == null){
						log.setStatus(LogPo.LogStatus.FAIL.getStatus());
					}
					
					if (PropertyUtils.isGoodJson((String) object)){
						Map<String, Object> map = JSON.parseObject((String) object);
						int code = (Integer) map.get("code");
						if (code != 0){
							log.setStatus(LogPo.LogStatus.FAIL.getStatus());
						}
					}
					
					long end = System.currentTimeMillis();
					log.setRespTime(end - start);
					logService.insert(log);;

				} catch (Throwable e) {
					long end = System.currentTimeMillis();
					log.setStatus(LogPo.LogStatus.FAIL.getStatus());
					log.setModule(systemlog.module());
					log.setContent(systemlog.methods());
					log.setRespTime(end - start);
					logService.insert(log);;

				}
			} else {
				object = pjp.proceed();
			}
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		return object;
	}
	
}
