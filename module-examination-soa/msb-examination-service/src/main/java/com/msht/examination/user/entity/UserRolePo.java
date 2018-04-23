/**
 * 
 */
package com.msht.examination.user.entity;

import com.msht.framework.entity.Entity;

/**
 * @author lindaofen
 *
 */
public class UserRolePo extends Entity<Long, UserRolePo> {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long userId;
	
	private Long roleId;

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

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	
	

}
