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
public class UserPo extends Entity<Long, UserPo> {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String username;
	
	private String password;
	
	private String salt;
	
	private String email;
	
	private Integer status;
	
	private Integer userType;
	
	private Integer isDel;
	
	private Long createBy;
	
	private Date createTime;
	
	private Long updateBy;
	
	private Date updateTime;
	
	private Long roleId;
	
	private RolePo role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Long getCreateBy() {
		return createBy;
	}

	public void setCreateBy(Long createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(Long updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public RolePo getRole() {
		return role;
	}

	public void setRole(RolePo role) {
		this.role = role;
	}

	public enum UserStatus{
		WAIT_ACTIVATE(0), NORMAL(1),DISABLE(2);
		
		private int status;
		
		private UserStatus(int status){
			this.status = status;
		}

		public int getStatus() {
			return status;
		}

		public static UserStatus getStatus(int status) {
			switch (status){
			case 0:
				return WAIT_ACTIVATE;
			case 1:
				return NORMAL;
			case 2:
				return DISABLE;
			}
			return NORMAL;
		}
	}
	
	public enum UserType{
		SUPPER(1), ADMIN(2),NORMAL(3);
		
		private int type;
		
		private UserType(int type){
			this.type = type;
		}

		public int getType() {
			return type;
		}
		
		public static UserType getType(int type) {
			switch (type){
			case 1:
				return SUPPER;
			case 2:
				return ADMIN;
			case 3:
				return NORMAL;
			}
			return NORMAL;
		}
	}
}
