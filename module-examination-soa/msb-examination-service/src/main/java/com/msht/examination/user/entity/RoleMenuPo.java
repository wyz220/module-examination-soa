/**
 * 
 */
package com.msht.examination.user.entity;

import com.msht.framework.entity.Entity;

/**
 * @author lindaofen
 *
 */
public class RoleMenuPo extends Entity<Long, RoleMenuPo> {

	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private Long roleId;
	
	private Long menuId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getMenuId() {
		return menuId;
	}

	public void setMenuId(Long menuId) {
		this.menuId = menuId;
	}
	
	

}
