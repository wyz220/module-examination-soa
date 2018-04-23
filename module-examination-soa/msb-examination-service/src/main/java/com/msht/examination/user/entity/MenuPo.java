/**
 * 
 */
package com.msht.examination.user.entity;

import java.util.Date;
import java.util.List;

import com.msht.framework.entity.Entity;

/**
 * @author lindaofen
 *
 */
public class MenuPo extends Entity<Long, MenuPo> {

	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String name;
	
	private String type;
	
	private String url;
	
	private String code;
	
	private Long parentId;
	
	private String path;
	
	private Integer priority;
	
	private String title;
	
	private Long createBy;
	
	private Date createTime;
	
	private Long updateBy;
	
	private Date updateTime;
	
	private List<MenuPo> childs;
	
	private Boolean hasChild = false;
	
	private Boolean checked = false;

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}
	
	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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
	
    public List<MenuPo> getChilds() {
		return childs;
	}

	public void setChilds(List<MenuPo> childs) {
		this.childs = childs;
	}

	public Boolean getHasChild() {
		return hasChild;
	}

	public void setHasChild(Boolean hasChild) {
		this.hasChild = hasChild;
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

	public static enum MenuType {
        MENU("menu"), BUTTON("button");

        private final String type;
        
        private MenuType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }
        
		public static MenuType getType(String type) {
			switch (type) {
			case "menu":
				return MENU;
			case "button":
				return BUTTON;
			}
			return MENU;
		}
    }

}
