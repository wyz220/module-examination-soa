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
public class RolePo extends Entity<Long, RolePo> {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private Integer isSupper;
	
	private Long createBy;
	
	private Date createTime;
	
	private Long updateBy;
	
	private Date updateTime;
	
	private String menuIds;
	
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Integer getIsSupper() {
		return isSupper;
	}

	public void setIsSupper(Integer isSupper) {
		this.isSupper = isSupper;
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

	public String getMenuIds() {
		return menuIds;
	}

	public void setMenuIds(String menuIds) {
		this.menuIds = menuIds;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	

}
