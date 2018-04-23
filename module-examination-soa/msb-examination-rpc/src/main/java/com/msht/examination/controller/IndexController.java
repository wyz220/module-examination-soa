/**
 * 
 */
package com.msht.examination.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author lindaofen
 *
 */
@Controller  
@RequestMapping("/")  
public class IndexController {

	@RequestMapping("/index.html")  
	public String login(HttpServletRequest request, HttpServletResponse response){
		
		return "index";
	}
}
