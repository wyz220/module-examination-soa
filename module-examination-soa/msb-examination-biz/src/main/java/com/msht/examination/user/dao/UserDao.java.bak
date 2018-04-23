/**
 * 
 */
package com.msht.examination.user.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.msht.examination.user.entity.UserPo;
import com.msht.examination.user.entity.UserRolePo;
import com.msht.framework.persistent.db.dao.BaseDao;

/**
 * @author lindaofen
 *
 */
@Component
public class UserDao extends BaseDao<UserPo> {

	@Override
	public String getNamespace() {
		return "com.msht.examination.user.dao.UserDao";
	}
	
	public List<Long> getUserRoleIds(Long userId){
		return sqlSessionTemplate.selectList(getNamespace() + ".getUserRoleIds", userId);
	}
	
    public int deleteUserRoleByRoleId( Object entity)
    {
        return sqlSessionTemplate.delete(getNamespace() + ".deleteUserRoleByRoleId", entity);
    }
    
	public int insertUserRole(UserRolePo po){
		 return sqlSessionTemplate.insert(getNamespace() + ".insertUserRole", po );
	}
    
    public int deleteUserRoleByUserId( Object entity)
    {
        return sqlSessionTemplate.delete(getNamespace() + ".deleteUserRoleByUserId", entity);
    }
}
