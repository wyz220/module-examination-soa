/**
 * 
 */
package com.msht.examination.user.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.msht.examination.user.entity.MenuPo;
import com.msht.framework.persistent.db.dao.BaseDao;

/**
 * @author lindaofen
 *
 */
@Component
public class MenuDao extends BaseDao<MenuPo> {

	@Override
	public String getNamespace() {
		return "com.msht.examination.user.dao.MenuDao";
	}
	
	public List<MenuPo> gets(Long[] menuIds){
		return sqlSessionTemplate.selectList(getNamespace() + ".gets", menuIds );
	}
	
	public List<MenuPo> findMenuByRoleId(Long roleId){
		return sqlSessionTemplate.selectList(getNamespace() + ".findMenuByRoleId", roleId );
	}
}
