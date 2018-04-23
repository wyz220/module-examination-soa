package com.msht.examination.common;


import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class RestResult {

	public static Map<String, Object> restResult(ResultInfo resultInfo, Object data) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", resultInfo.getCode());
		if (resultInfo.getCode() == ResultInfo.SUCCESS) {
			result.put("result", "success");
		} else {
			result.put("result", "failed");
		}
		
		result.put("message", resultInfo.getMessage());
		if (data!=null) {
			result.put("data", data);
		}
		
		return result;
	}
	
	public static String restJSONResult(ResultInfo resultInfo, Object data) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", resultInfo.getCode());
		if (resultInfo.getCode() == ResultInfo.SUCCESS) {
			result.put("result", "success");
		} else {
			result.put("result", "failed");
		}
		
		result.put("message", resultInfo.getMessage());
		if (data!=null) {
			result.put("data", data);
		}
		
		return JSON.toJSONString(result);
	}
}
