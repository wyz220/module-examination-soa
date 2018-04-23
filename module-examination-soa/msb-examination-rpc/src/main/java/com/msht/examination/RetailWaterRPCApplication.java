package com.msht.examination;

import java.io.IOException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class RetailWaterRPCApplication {
	
    public static void main(String[] args) throws IOException {
    	
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(  
                new String[] {"applicationContext.xml", "dubbo.xml"});  
        context.start();  
  
        System.in.read(); // 按任意键退出  
    }

}
