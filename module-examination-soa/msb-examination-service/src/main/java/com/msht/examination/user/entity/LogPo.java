/**
 * 
 */
package com.msht.examination.user.entity;

import java.util.Date;

import com.msht.framework.entity.Entity;

/**
 * @author lindaofen
 *
 */
public class LogPo extends Entity<Long, LogPo> {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long userId;
	
	private String username;
	
	private String module;
	
	private String content;
	
	private Integer status;
	
	private Long respTime;
	
	private String ip;
	
	private Long groupId;
	
	private Date operTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getRespTime() {
		return respTime;
	}

	public void setRespTime(Long respTime) {
		this.respTime = respTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public Date getOperTime() {
		return operTime;
	}

	public void setOperTime(Date operTime) {
		this.operTime = operTime;
	}
	
	public enum LogStatus{
		SUCCESS(1), FAIL(2),DISABLE(2);
		
		private int status;
		
		private LogStatus(int status){
			this.status = status;
		}

		public int getStatus() {
			return status;
		}

		public static LogStatus getStatus(int status) {
			switch (status){
			case 1:
				return SUCCESS;
			case 2:
				return FAIL;
			}
			return FAIL;
		}
	}

}
