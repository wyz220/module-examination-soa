/**
 * 
 */
package com.msht.examination.utils;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.apache.shiro.codec.Base64;

import com.alibaba.fastjson.JSON;
import com.msht.examination.user.entity.UserPo;
import com.msht.examination.user.entity.UserPo.UserType;
import com.msht.framework.common.utils.StringUtils;

/**
 * @author lindaofen
 *
 */
public class PropertyUtils {

/*	public static Long getGroupId(UserPo user) {

		switch (UserType.getType(user.getUserType())) {
		case SUPPER:
		case ADMIN:
			return user.getId();
		case NORMAL:
			return user.getGroupId();
		default:
			break;
		}

		return 0L;
	}*/

	public static String generateFileName() {
		String uuid = UUID.randomUUID().toString().replace("-", "");
		return uuid + ".jpg";
	}

	public static boolean isGoodJson(String text) {
		if (StringUtils.isBlank(text)) {
			return false;
		}

		if (text.contains(":")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取远程客户端Ip
	 * 
	 * @param request
	 * @return
	 */
	public static String getRemoteHost(javax.servlet.http.HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String base64Encoded = "4AvVhmFLUs0KTA3Kprsdag==";
		String encodeStr = Base64.encodeToString("MSB123ABC789MSHT==".getBytes());
		System.out.println(encodeStr);
		System.out.println(Base64.decodeToString("4AvVhmFLUs0KTA3Kprsdag=="));

		System.out.println("{user:user_add}".contains(":"));
		Object object = JSON.parseObject("{user:'user_add'}");
		System.out.println(object);

	}
}
