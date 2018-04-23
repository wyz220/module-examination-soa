/**
 * 
 */
package com.msht.examination.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.msht.examination.user.entity.RoleMenuPo;
import com.msht.examination.user.entity.RolePo;
import com.msht.framework.persistent.db.dao.BaseDao;

/**
 * @author lindaofen
 *
 */
@Component
public class RoleDao extends BaseDao<RolePo> {

	@Override
	public String getNamespace() {
		return "com.msht.examination.user.dao.RoleDao";
	}
	
	public List<RolePo> gets(@Param("roleIds")Long[] roleIds){
		return sqlSessionTemplate.selectList(getNamespace() + ".gets", roleIds);
	}
	
	public List<Long> getRoleMenuIds(Long[] roleIds){
		return sqlSessionTemplate.selectList(getNamespace() + ".getRoleMenuIds", roleIds);
	}
	
	public int insertRoleMenu(RoleMenuPo po){
		 return sqlSessionTemplate.insert(getNamespace() + ".insertRoleMenu", po );
	}
	
    public int deleteRoleMenuByRoleId( Object entity)
    {
        return sqlSessionTemplate.delete(getNamespace() + ".deleteRoleMenuByRoleId", entity);
    }
    
    public List<RolePo> findRoleByUserId(Long[] userIds){
    	return sqlSessionTemplate.selectList(getNamespace() + ".findRoleByUserId", userIds);
    }
}
