/**
 * 
 */
package com.msht.examination.task;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.msht.examination.common.SysConfig;
import com.msht.framework.common.utils.DateUtils;


/**
 * @author lindaofen
 *
 */
@Component
@EnableScheduling
@PropertySource("classpath:application.properties")
public class TaskManager {

	private Logger logger = LoggerFactory.getLogger(TaskManager.class);
	
	
    @Scheduled(cron = "${task.activity.time}")  
    public void activityJob() {
    	
    }  
}
